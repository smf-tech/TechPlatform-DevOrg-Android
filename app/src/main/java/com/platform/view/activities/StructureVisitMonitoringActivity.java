package com.platform.view.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.BuildConfig;
import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.APIDataListener;
import com.platform.listeners.CustomSpinnerListener;
import com.platform.models.SujalamSuphalam.MasterDataList;
import com.platform.models.SujalamSuphalam.MasterDataValue;
import com.platform.models.SujalamSuphalam.SSMasterDatabase;
import com.platform.models.SujalamSuphalam.StructureData;
import com.platform.models.SujalamSuphalam.StructureVisitMonitoringData;
import com.platform.models.common.CustomSpinnerObject;
import com.platform.presenter.StructureVisitMonitoringActivityPresenter;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.Constants;
import com.platform.utility.GPSTracker;
import com.platform.utility.Permissions;
import com.platform.utility.Urls;
import com.platform.utility.Util;
import com.platform.utility.VolleyMultipartRequest;
import com.platform.view.customs.CustomSpinnerDialogClass;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StructureVisitMonitoringActivity extends AppCompatActivity implements View.OnClickListener,
        APIDataListener, CustomSpinnerListener {

    private final String TAG = StructurePripretionsActivity.class.getName();
    private final String STRUCTURE_DATA = "StructureData";

    private RelativeLayout progressBar;
    private StructureVisitMonitoringActivityPresenter presenter;

    private Uri outputUri;
    private Uri finalUri;
    boolean safetySignage = true, guidelines = true;
    ImageView ivStructure;
    EditText etStatus, etIssuesRelated, etIssuesDescription;

    private String selectedStatus, selectedStatusID, selectedIssue, selectedIssueID;
    private ArrayList<CustomSpinnerObject> statusList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> issueList = new ArrayList<>();
    private ArrayList<MasterDataList> masterDataLists = new ArrayList<>();


    final String upload_URL = BuildConfig.BASE_URL + Urls.SSModule.STRUCTURE_VISITE_MONITORING;
    private RequestQueue rQueue;
    private HashMap<String, Bitmap> imageHashmap = new HashMap<>();
    private ArrayList<Uri> imageUri = new ArrayList<>();
    private int imageCount = 0;
    StructureVisitMonitoringData requestData = new StructureVisitMonitoringData();
    StructureData structureData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_structure_visit_monitoring);

        progressBar = findViewById(R.id.ly_progress_bar);
        presenter = new StructureVisitMonitoringActivityPresenter(this);

        structureData = (StructureData) getIntent().getSerializableExtra(STRUCTURE_DATA);

//        setMasterData();

        {// set list data
            CustomSpinnerObject object1 = new CustomSpinnerObject();
            object1.set_id("1");
            object1.setName("Regular");
            statusList.add(object1);
            CustomSpinnerObject object2 = new CustomSpinnerObject();
            object2.set_id("2");
            object2.setName("Partial");
            statusList.add(object2);
            CustomSpinnerObject object3 = new CustomSpinnerObject();
            object3.set_id("3");
            object3.setName("Not maintained");
            statusList.add(object3);

            CustomSpinnerObject object4 = new CustomSpinnerObject();
            object4.set_id("1");
            object4.setName("Structure");
            issueList.add(object4);
            CustomSpinnerObject object5 = new CustomSpinnerObject();
            object5.set_id("2");
            object5.setName("Machine");
            issueList.add(object5);

        }
        initView();
        setTitle("Structure Visit and Monitoring");
    }

    private void initView() {

        TextView tvStructureCode = findViewById(R.id.tv_structure_code);
        RadioGroup rgSafetySignage = findViewById(R.id.rg_safety_signage);
        RadioGroup rgGuidelines = findViewById(R.id.rg_guidelines);
        ivStructure = findViewById(R.id.iv_structure);
        etStatus = findViewById(R.id.et_status);
        etIssuesRelated = findViewById(R.id.et_issues_related);
        etIssuesDescription = findViewById(R.id.et_issues_description);
        Button btSubmit = findViewById(R.id.bt_submit);

        tvStructureCode.setText(structureData.getStructureCode());

        btSubmit.setOnClickListener(this);
        ivStructure.setOnClickListener(this);
        etStatus.setOnClickListener(this);
        etIssuesRelated.setOnClickListener(this);

        rgSafetySignage.check(R.id.rb_safety_signage_yes);
        rgSafetySignage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_safety_signage_yes:
                        safetySignage = true;
                        break;
                    case R.id.rb_safety_signage_no:
                        safetySignage = false;
                        break;
                }
            }
        });
        rgGuidelines.check(R.id.rb_guidelines_yes);
        rgGuidelines.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_guidelines_yes:
                        guidelines = true;
                        break;
                    case R.id.rb_guidelines_no:
                        guidelines = false;
                        break;
                }
            }
        });
    }

    public void setTitle(String title) {
        TextView tvTitle = findViewById(R.id.toolbar_title);
        tvTitle.setText(title);
        findViewById(R.id.toolbar_back_action).setOnClickListener(this);
    }

    public void setMasterData() {

        List<SSMasterDatabase> list = DatabaseManager.getDBInstance(Platform.getInstance()).
                getSSMasterDatabaseDao().getSSMasterData();
        String masterDbString = list.get(0).getData();

        Gson gson = new Gson();
        TypeToken<ArrayList<MasterDataList>> token = new TypeToken<ArrayList<MasterDataList>>() {};
        ArrayList<MasterDataList> masterDataList = gson.fromJson(masterDbString, token.getType());

        for (MasterDataList obj : masterDataList) {
            if (obj.getForm().equalsIgnoreCase("structure_create")) {
                masterDataLists.add(obj);
            }
        }
        for (int i = 0; i < masterDataLists.size(); i++) {
            if (masterDataLists.get(i).getField().equalsIgnoreCase("structureDept"))
                for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                    CustomSpinnerObject temp = new CustomSpinnerObject();
                    temp.set_id(obj.getId());
                    temp.setName(obj.getValue());
                    temp.setSelected(false);
//                    structureDepartmentList.add(temp);
                }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_structure:
                onAddImageClick();

                break;
            case R.id.et_status:
                CustomSpinnerDialogClass csdStatus = new CustomSpinnerDialogClass(this, this,
                        "Status of the record mainentance", statusList, false);
                csdStatus.show();
                csdStatus.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

                break;
            case R.id.et_issues_related:
                CustomSpinnerDialogClass csdIssue = new CustomSpinnerDialogClass(this, this,
                        "Issues related to", issueList, false);
                csdIssue.show();
                csdIssue.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.bt_submit:
                if (isAllDataValid()) {

                    Uri uri = imageUri.get(0);
                    requestData.setStructureImage(uri.getPath());
                    DatabaseManager.getDBInstance(Platform.getInstance()).getStructureVisitMonitoringDataDao()
                            .insert(requestData);

                    SyncAdapterUtils.manualRefresh();

                    Util.showToast("Structure visit is submitted successfully.",this);

                    finish();

//                    List<StructureVisitMonitoringData> structureVisitMonitoringList = new ArrayList<>();
//                    structureVisitMonitoringList.addAll(DatabaseManager.getDBInstance(Platform.getInstance())
//                            .getStructureVisitMonitoringDataDao().getAllStructure());
//                    uploadImage(structureVisitMonitoringList.get(0),1);
//                    presenter.submitVisitMonitoring(requestData, imageHashmap);
                }
                break;
            case R.id.toolbar_back_action:
                finish();
                break;

        }
    }

    private boolean isAllDataValid() {

        GPSTracker gpsTracker = new GPSTracker(this);
        Location location = null;
        if (gpsTracker.isGPSEnabled(this, this)) {
            location = gpsTracker.getLocation();
        } else {
            Util.snackBarToShowMsg(this.getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Location not available, Please check GPS setting.",
                    Snackbar.LENGTH_LONG);
            return false;
        }

        if(location!=null){
            if (TextUtils.isEmpty(selectedStatusID)
                    || TextUtils.isEmpty(selectedIssueID)
                    || TextUtils.isEmpty(etIssuesDescription.getText().toString())) {
                Util.snackBarToShowMsg(this.getWindow().getDecorView()
                                .findViewById(android.R.id.content), "Please, feel proper information.",
                        Snackbar.LENGTH_LONG);
                return false;
            } else {
                requestData.setStructureId(structureData.getStructureId());
                requestData.setIsSafetySignage(safetySignage);
                requestData.setIsGuidelinesFollowed(guidelines);
                requestData.setStatusRecordId(selectedStatusID);
                requestData.setIssueRelated(selectedIssueID);
                requestData.setIssueDescription(etIssuesDescription.getText().toString());
            }
         } else {
            Util.snackBarToShowMsg(this.getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Location not available, Please check GPS setting.",
                    Snackbar.LENGTH_LONG);
            return false;
        }



        if (imageUri.size() == 0) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please, click images of structure.",
                    Snackbar.LENGTH_LONG);
            return false;
        }
        return true;
    }

    private void onAddImageClick() {
        if (Permissions.isCameraPermissionGranted(this, this)) {
//            showPictureDialog();
            takePhotoFromCamera();
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.title_choose_picture));
        String[] items = {getString(R.string.label_gallery), getString(R.string.label_camera)};

        dialog.setItems(items, (dialog1, which) -> {
            switch (which) {
                case 0:
                    choosePhotoFromGallery();
                    break;

                case 1:
                    takePhotoFromCamera();
                    break;
            }
        });
        dialog.show();
    }

    private void choosePhotoFromGallery() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, Constants.CHOOSE_IMAGE_FROM_GALLERY);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getResources().getString(R.string.msg_error_in_photo_gallery),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void takePhotoFromCamera() {
        try {
            //use standard intent to capture an image
            String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/Octopus/Image/picture.jpg";

            File imageFile = new File(imageFilePath);
            outputUri = FileProvider.getUriForFile(this, this.getPackageName()
                    + ".file_provider", imageFile);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(takePictureIntent, Constants.CHOOSE_IMAGE_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            //display an error message
            Toast.makeText(this, getResources().getString(R.string.msg_image_capture_not_support),
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, getResources().getString(R.string.msg_take_photo_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            try {
                String imageFilePath = getImageName();
                if (imageFilePath == null) return;
                finalUri = Util.getUri(imageFilePath);
                Crop.of(outputUri, finalUri).start(this);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    String imageFilePath = getImageName();
                    if (imageFilePath == null) return;
                    outputUri = data.getData();
                    finalUri = Util.getUri(imageFilePath);
                    Crop.of(outputUri, finalUri).start(this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                final File imageFile = new File(Objects.requireNonNull(finalUri.getPath()));
                if (Util.isConnected(this)) {
                    if (Util.isValidImageSize(imageFile)) {
                        ivStructure.setImageURI(finalUri);
                        imageUri.add(finalUri);
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), finalUri);
//                        imageHashmap.put("image" + imageCount, bitmap);
//                        imageCount++;
                    } else {
                        Util.showToast(getString(R.string.msg_big_image), this);
                    }
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), this);
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private String getImageName() {
        long time = new Date().getTime();
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + Constants.Image.IMAGE_STORAGE_DIRECTORY);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                Log.e(TAG, "Failed to create directory!");
                return null;
            }
        }
        return Constants.Image.IMAGE_STORAGE_DIRECTORY + Constants.Image.FILE_SEP
                + Constants.Image.IMAGE_PREFIX + time + Constants.Image.IMAGE_SUFFIX;
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(message, this);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        Util.showToast(error.getMessage(), this);
    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {
        runOnUiThread(() -> {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {

    }
    private void uploadImage(StructureVisitMonitoringData structureVisitMonitoringData, int noImages) {

        Log.d("request -",new Gson().toJson(requestData));

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        rQueue.getCache().clear();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            Toast.makeText(StructureVisitMonitoringActivity.this, jsonString, Toast.LENGTH_LONG).show();
                            Log.d("response -", jsonString);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Toast.makeText(StructureVisitMonitoringActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(StructureVisitMonitoringActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", new Gson().toJson(structureVisitMonitoringData));
                params.put("imageArraySize", String.valueOf(noImages));//add string parameters
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                Drawable drawable = null;
//                Iterator myVeryOwnIterator = imageHashmap.keySet().iterator();
//                for (int i = 0; i < imageHashmap.size(); i++) {
//                    String key = (String) myVeryOwnIterator.next();
                    drawable = new BitmapDrawable(getResources(), structureVisitMonitoringData.getStructureImage());
                    params.put("image0", new DataPart("image0", getFileDataFromDrawable(drawable),
                            "image/jpeg"));
//                }
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue = Volley.newRequestQueue(this);
        rQueue.add(volleyMultipartRequest);
    }

    private byte[] getFileDataFromDrawable(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }


    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Status of the record mainentance":
                for (CustomSpinnerObject obj : statusList) {
                    if (obj.isSelected()) {
                        selectedStatus = obj.getName();
                        selectedStatusID = obj.get_id();
                        break;
                    }
                }
                etStatus.setText(selectedStatus);
                break;
            case "Issues related to":
                for (CustomSpinnerObject obj : issueList) {
                    if (obj.isSelected()) {
                        selectedIssue = obj.getName();
                        selectedIssueID = obj.get_id();
                        break;
                    }
                }
                etIssuesRelated.setText(selectedIssue);
                break;
        }
    }
}
