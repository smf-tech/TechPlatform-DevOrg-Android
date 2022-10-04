package com.octopusbjsindia.view.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataValue;
import com.octopusbjsindia.models.SujalamSuphalam.SSMasterDatabase;
import com.octopusbjsindia.models.SujalamSuphalam.StructureData;
import com.octopusbjsindia.models.SujalamSuphalam.StructurePripretionData;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.login.Login;
import com.octopusbjsindia.presenter.StructurePripretionsActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GPSTracker;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.utility.VolleyMultipartRequest;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.octopusbjsindia.utility.Util.getLoginObjectFromPref;
import static com.octopusbjsindia.utility.Util.getUserObjectFromPref;

public class StructurePreparationActivity extends AppCompatActivity implements View.OnClickListener,
        APIDataListener , CustomSpinnerListener {

    private final String TAG = StructurePreparationActivity.class.getName();
    private final String STRUCTURE_DATA = "StructureData";
    ImageView selectedIV;
    EditText etFFName, etFFMobile, etReson,etTypeOfBeneficiary;
    private RelativeLayout progressBar;
    private Uri outputUri;
    private Uri finalUri;
    boolean ffIdentified = false;
    boolean ffTranningComplited = false;
    boolean structureFit = false;
    //    private String upload_URL = "http://13.235.124.3/api/prepareStructure";
    final String upload_URL = BuildConfig.BASE_URL
            + Urls.SSModule.STRUCTURE_PREPARATION;
    private RequestQueue rQueue;
    private HashMap<String, Bitmap> imageHashmap = new HashMap<>();
    private ArrayList<Uri> imageUri = new ArrayList<>();
    private int imageCount = 0;
    private StructurePripretionData requestData = new StructurePripretionData();
    private StructureData structureData;
    private ArrayList<MasterDataList> masterDataLists = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> typeOfBeneficiaryList = new ArrayList<>();
    private StructurePripretionsActivityPresenter presenter;
    private String currentPhotoPath, selectedTypeOfBeneficiaryId, selectedTypeOfBeneficiary;
    private GPSTracker gpsTracker;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_structure_pripretions);

        progressBar = findViewById(R.id.ly_progress_bar);
        presenter = new StructurePripretionsActivityPresenter(this);

        structureData = (StructureData) getIntent().getSerializableExtra(STRUCTURE_DATA);

        setMasterData();
        initView();
        setTitle("Structure Preparation");

    }

    private void initView() {

        //get lat,long of location
        gpsTracker = new GPSTracker(this);
        if(Permissions.isLocationPermissionGranted(this, this)) {
            if(gpsTracker.canGetLocation()) {
                location = gpsTracker.getLocation();
            } else {
                gpsTracker.showSettingsAlert();
            }
        }

        TextView tvStructureCode = findViewById(R.id.tv_structure_code);
        ImageView ivStructureImg1 = findViewById(R.id.tv_structure_img1);
        TextView tv_machin_code = findViewById(R.id.tv_machin_code);
        ImageView ivStructureImg2 = findViewById(R.id.tv_structure_img2);
        RadioGroup rgFFIdentified = findViewById(R.id.rg_ff_identified);
        LinearLayout lyFFDetailes = findViewById(R.id.ly_ff_detailes);
        etFFName = findViewById(R.id.et_ff_name);
        etFFMobile = findViewById(R.id.et_ff_mobile);
        RadioGroup rgFFTranningComplited = findViewById(R.id.rg_ff_tranning_complited);
        RadioGroup rgStructureFit = findViewById(R.id.rg_structure_fit);
        TextInputLayout lyReson = findViewById(R.id.ly_reason);
        etReson = findViewById(R.id.et_reason);
        etTypeOfBeneficiary = findViewById(R.id.et_type_of_beneficiary);
        Button btSubmit = findViewById(R.id.bt_submit);

        tvStructureCode.setText(structureData.getStructureCode());
        tv_machin_code.setText(structureData.getStructureMachineList());

        ivStructureImg1.setOnClickListener(this);
        ivStructureImg2.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        etTypeOfBeneficiary.setOnClickListener(this);

        rgFFIdentified.check(R.id.rb_ff_identified_no);
        rgFFIdentified.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_ff_identified_yes:
                        ffIdentified = true;
                        lyFFDetailes.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_ff_identified_no:
                        ffIdentified = false;
                        lyFFDetailes.setVisibility(View.GONE);
                        break;
                }
            }
        });

        rgFFTranningComplited.check(R.id.rb_ff_tranning_complited_no);
        rgFFTranningComplited.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_ff_tranning_complited_yes:
                        ffTranningComplited = true;
                        break;
                    case R.id.rb_ff_tranning_complited_no:
                        ffTranningComplited = false;
                        break;
                }
            }
        });

        rgStructureFit.check(R.id.rb_structure_fit_no);
        rgStructureFit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_structure_fit_yes:
                        structureFit = true;
                        lyReson.setVisibility(View.GONE);
                        break;
                    case R.id.rb_structure_fit_no:
                        structureFit = false;
                        lyReson.setVisibility(View.VISIBLE);
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
                getSSMasterDatabaseDao().getSSMasterData("SS");
        String masterDbString = list.get(0).getData();

        Gson gson = new Gson();
        TypeToken<ArrayList<MasterDataList>> token = new TypeToken<ArrayList<MasterDataList>>() {};
        ArrayList<MasterDataList> masterDataList = gson.fromJson(masterDbString, token.getType());

        for (MasterDataList obj : masterDataList) {
            if (obj.getForm().equalsIgnoreCase("structure_preparation")) {
                masterDataLists.add(obj);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
                if (isAllDataValid()) {

                    Uri uri1 = imageUri.get(0);
                    requestData.setStructureImg1(uri1.getPath());
                    Uri uri2 = imageUri.get(1);
                    requestData.setStructureImg2(uri2.getPath());

                    if(Util.isConnected(this)){
                        uploadImage(requestData, 2);
                    } else {
                        DatabaseManager.getDBInstance(Platform.getInstance()).getStructurePripretionDataDao()
                                .insert(requestData);
//                        SyncAdapterUtils.manualRefresh();
                        Util.showToast("Structure will be prepared soon.", this);
                        finish();
                    }


//                    presenter.submitPripretion(requestData, imageHashmap);
                }
                break;
            case R.id.tv_structure_img1:
                selectedIV = findViewById(R.id.tv_structure_img1);
                onAddImageClick();
                break;
            case R.id.tv_structure_img2:
                selectedIV = findViewById(R.id.tv_structure_img2);
                onAddImageClick();
                break;
            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.et_type_of_beneficiary:
                typeOfBeneficiaryList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("structureBeneficiary"))
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setSelected(false);
                            typeOfBeneficiaryList.add(temp);
                        }
                }
                CustomSpinnerDialogClass csdStructureDept = new CustomSpinnerDialogClass(this, this,
                        "Select type of beneficiary", typeOfBeneficiaryList, false);
                csdStructureDept.show();
                csdStructureDept.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
        }
    }

    private boolean isAllDataValid() {
        if (location != null) {
            if (ffIdentified) {
                if (TextUtils.isEmpty(etFFName.getText().toString())) {
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter FF Name.", Snackbar.LENGTH_LONG);
                    return false;
                } else if (TextUtils.isEmpty(etFFMobile.getText().toString())) {
                    Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                            "Please, enter FF Mobile Number.", Snackbar.LENGTH_LONG);
                    return false;
                }  else {
                    requestData.setFfName(etFFName.getText().toString());
                    requestData.setFfMobileNumber(etFFMobile.getText().toString());
                }
            } else if (TextUtils.isEmpty(selectedTypeOfBeneficiaryId)) {
                Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                        "Please, select type of beneficiary.", Snackbar.LENGTH_LONG);
                return false;
            }
            requestData.setStructureId(structureData.getStructureId());
            requestData.setLat(location.getLatitude());
            requestData.setLong(location.getLongitude());
            requestData.setFfIdentified(ffIdentified);
            requestData.setFfTraningDone(ffTranningComplited);
            requestData.setIsStructureFit(structureFit);
            requestData.setReason(etReson.getText().toString());
            requestData.setBeneficiary_id(selectedTypeOfBeneficiaryId);
        } else {
            Util.snackBarToShowMsg(this.getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Location not available, Please check GPS setting.",
                    Snackbar.LENGTH_LONG);

            if(gpsTracker.canGetLocation()) {
                location = gpsTracker.getLocation();
            } else {
                Toast.makeText(this, "Not able to get location.", Toast.LENGTH_LONG).show();
            }
            return false;
        }

        if (imageUri.size() == 0) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please, click images of structure.",
                    Snackbar.LENGTH_LONG);
            return false;
        } else if (imageUri.size() == 1) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please, click images of structure.",
                    Snackbar.LENGTH_LONG);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.GPS_REQUEST) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if(gpsTracker.canGetLocation()) {
                    location = gpsTracker.getLocation();
                } else {
                    gpsTracker.showSettingsAlert();
                }
            } else {
                Toast.makeText(this, "Location permission not granted.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void uploadImage(StructurePripretionData structurePripretionData, int imageCount) {

        showProgressBar();
        Log.d("request -", new Gson().toJson(requestData));

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        hideProgressBar();
                        rQueue.getCache().clear();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//                            Toast.makeText(StructurePripretionsActivity.this, jsonString, Toast.LENGTH_LONG).show();
                            Log.d("response -", jsonString);
                            Util.showToast("Structure is prepared", this);
                            finish();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Toast.makeText(StructurePreparationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressBar();
                        Toast.makeText(StructurePreparationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        DatabaseManager.getDBInstance(Platform.getInstance()).getStructurePripretionDataDao()
                                .insert(requestData);
                        Util.showToast("Structure will be prepared soon.", this);
                        finish();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", new Gson().toJson(structurePripretionData));
                params.put("imageArraySize", String.valueOf(imageCount));//add string parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json, text/plain, */*");
                headers.put("Content-Type", getBodyContentType());

                Login loginObj = getLoginObjectFromPref();
                if (loginObj != null && loginObj.getLoginData() != null &&
                        loginObj.getLoginData().getAccessToken() != null) {
                    headers.put(Constants.Login.AUTHORIZATION,
                            "Bearer " + loginObj.getLoginData().getAccessToken());
                    if (getUserObjectFromPref().getOrgId() != null) {
                        headers.put("orgId", getUserObjectFromPref().getOrgId());
                    }
                    if (getUserObjectFromPref().getProjectIds() != null) {
                        headers.put("projectId", getUserObjectFromPref().getProjectIds().get(0).getId());
                    }
                    if (getUserObjectFromPref().getRoleIds() != null) {
                        headers.put("roleId", getUserObjectFromPref().getRoleIds());
                    }
                }
                return headers;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
//                Iterator myVeryOwnIterator = imageHashmap.keySet().iterator();
//                for (int i = 0; i < imageHashmap.size(); i++) {
//                    String key = (String) myVeryOwnIterator.next();
                Drawable drawable = new BitmapDrawable(getResources(), structurePripretionData.getStructureImg1());
                params.put("Structure0", new DataPart("Structure0", getFileDataFromDrawable(drawable),
                        "image/jpeg"));
                Drawable drawable1 = new BitmapDrawable(getResources(), structurePripretionData.getStructureImg2());
                params.put("Structure1", new DataPart("Structure1", getFileDataFromDrawable(drawable1),
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

    private void submitPripretionData(StructurePripretionData requestData, int imageCount) {

        final String upload_URL = BuildConfig.BASE_URL + Urls.SSModule.STRUCTURE_PREPARATION;

        Log.d("STR_PREPARATION req:", new Gson().toJson(requestData));

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        rQueue.getCache().clear();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            Log.d("STR_PREPARATION res:", jsonString);

                            CommonResponse res = new Gson().fromJson(jsonString, CommonResponse.class);
                            if (res.getStatus() == 200) {
                                DatabaseManager.getDBInstance(Platform.getInstance()).getStructurePripretionDataDao()
                                        .delete(requestData.getId());
                            }

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Log.d("STR_PREPARATION exp:", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("STR_PREPARATION exp:", error.getMessage());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", new Gson().toJson(requestData));
                params.put("imageArraySize", String.valueOf(imageCount));//add string parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json, text/plain, */*");
                headers.put("Content-Type", getBodyContentType());

                Login loginObj = getLoginObjectFromPref();
                if (loginObj != null && loginObj.getLoginData() != null &&
                        loginObj.getLoginData().getAccessToken() != null) {
                    headers.put(Constants.Login.AUTHORIZATION,
                            "Bearer " + loginObj.getLoginData().getAccessToken());
                    if (getUserObjectFromPref().getOrgId() != null) {
                        headers.put("orgId", getUserObjectFromPref().getOrgId());
                    }
                    if (getUserObjectFromPref().getProjectIds() != null) {
                        headers.put("projectId", getUserObjectFromPref().getProjectIds().get(0).getId());
                    }
                    if (getUserObjectFromPref().getRoleIds() != null) {
                        headers.put("roleId", getUserObjectFromPref().getRoleIds());
                    }
                }
                return headers;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                Drawable drawable = new BitmapDrawable(getResources(), requestData.getStructureImg1());
                params.put("Structure0", new DataPart("Structure0", getFileDataFromDrawable(drawable),
                        "image/jpeg"));
                Drawable drawable1 = new BitmapDrawable(getResources(), requestData.getStructureImg1());
                params.put("Structure1", new DataPart("Structure1", getFileDataFromDrawable(drawable1),
                        "image/jpeg"));
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
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = getImageFile(); // 1
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
                uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID.concat(".file_provider"), file);
            else
                uri = Uri.fromFile(file); // 3
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
            startActivityForResult(pictureIntent, Constants.CHOOSE_IMAGE_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            //display an error message
            Toast.makeText(this, getResources().getString(R.string.msg_image_capture_not_support),
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(this, getResources().getString(R.string.msg_take_photo_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private File getImageFile() {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constants.Image.IMAGE_STORAGE_DIRECTORY);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File file;
        file = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        currentPhotoPath = file.getPath();
        return file;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            try {
                finalUri = Uri.fromFile(new File(currentPhotoPath));
                Crop.of(finalUri, finalUri).start(this);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    getImageFile();
                    outputUri = data.getData();
                    finalUri=Uri.fromFile(new File(currentPhotoPath));
                    Crop.of(outputUri, finalUri).start(this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                final File imageFile = new File(Objects.requireNonNull(finalUri.getPath()));
                Bitmap bitmap = Util.compressImageToBitmap(imageFile);
                if (Util.isValidImageSize(imageFile)) {

                    selectedIV.setImageURI(finalUri);
                    imageUri.add(finalUri);
//                        imageHashmap.put("Structure" + imageCount, bitmap);
//                        imageCount++;
                } else {
                    Util.showToast(getString(R.string.msg_big_image), this);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if(requestCode == 100) {
            if(gpsTracker.canGetLocation()) {
                location = gpsTracker.getLocation();
                Toast.makeText(this, "Location permission granted.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Location permission not granted.", Toast.LENGTH_LONG).show();
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

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {
        runOnUiThread(() -> {
            if (progressBar != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (progressBar != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {

    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type){
            case "Select type of beneficiary":
            for (CustomSpinnerObject obj : typeOfBeneficiaryList) {
                if (obj.isSelected()) {
                    selectedTypeOfBeneficiary = obj.getName();
                    selectedTypeOfBeneficiaryId = obj.get_id();
                }
            }
                etTypeOfBeneficiary.setText(selectedTypeOfBeneficiary);
            break;
        }
    }
}
