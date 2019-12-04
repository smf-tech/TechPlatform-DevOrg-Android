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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.platform.BuildConfig;
import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.APIDataListener;
import com.platform.models.SujalamSuphalam.StructureData;
import com.platform.models.SujalamSuphalam.StructurePripretionData;
import com.platform.models.SujalamSuphalam.StructureVisitMonitoringData;
import com.platform.presenter.StructurePripretionsActivityPresenter;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.Constants;
import com.platform.utility.GPSTracker;
import com.platform.utility.Permissions;
import com.platform.utility.Urls;
import com.platform.utility.Util;
import com.platform.utility.VolleyMultipartRequest;
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

public class StructurePripretionsActivity extends AppCompatActivity implements View.OnClickListener, APIDataListener {

    private final String TAG = StructurePripretionsActivity.class.getName();
    private final String STRUCTURE_DATA = "StructureData";


    ImageView selectedIV;
    EditText etFFName, etFFMobile, etReson;

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
    StructurePripretionData requestData = new StructurePripretionData();
    StructureData structureData;

    StructurePripretionsActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_structure_pripretions);

        progressBar = findViewById(R.id.ly_progress_bar);
        presenter = new StructurePripretionsActivityPresenter(this);

        structureData = (StructureData) getIntent().getSerializableExtra(STRUCTURE_DATA);

        initView();
        setTitle("Structure Preparation");
    }

    private void initView() {

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
        Button btSubmit = findViewById(R.id.bt_submit);

        tvStructureCode.setText(structureData.getStructureCode());
        tv_machin_code.setText(structureData.getStructureMachineList());

        ivStructureImg1.setOnClickListener(this);
        ivStructureImg2.setOnClickListener(this);
        btSubmit.setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_submit:
                if (isAllDataValid()) {

                    Uri uri1 = imageUri.get(0);
                    requestData.setStructureImg1(uri1.getPath());
                    Uri uri2 = imageUri.get(0);
                    requestData.setStructureImg1(uri2.getPath());

                    if(Util.isConnected(this)){
                        uploadImage(requestData, 2);
                    } else {
                        DatabaseManager.getDBInstance(Platform.getInstance()).getStructurePripretionDataDao()
                                .insert(requestData);
                        SyncAdapterUtils.manualRefresh();
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
                } else {
                    requestData.setFfName(etFFName.getText().toString());
                    requestData.setFfMobileNumber(etFFMobile.getText().toString());
                }
            }
            requestData.setStructureId(structureData.getStructureId());
            requestData.setLat(location.getLatitude());
            requestData.setLong(location.getLongitude());
            requestData.setFfIdentified(ffIdentified);
            requestData.setFfTraningDone(ffTranningComplited);
            requestData.setIsStructureFit(structureFit);
            requestData.setReason(etReson.getText().toString());
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
        } else if (imageUri.size() == 1) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please, click images of structure.",
                    Snackbar.LENGTH_LONG);
            return false;
        }
        return true;
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
                            Toast.makeText(StructurePripretionsActivity.this, jsonString, Toast.LENGTH_LONG).show();
                            Log.d("response -", jsonString);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Toast.makeText(StructurePripretionsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressBar();
                        Toast.makeText(StructurePripretionsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
//                Iterator myVeryOwnIterator = imageHashmap.keySet().iterator();
//                for (int i = 0; i < imageHashmap.size(); i++) {
//                    String key = (String) myVeryOwnIterator.next();
                Drawable drawable = new BitmapDrawable(getResources(), structurePripretionData.getStructureImg1());
                params.put("Structure0", new DataPart("Structure0", getFileDataFromDrawable(drawable),
                        "image/jpeg"));
                Drawable drawable1 = new BitmapDrawable(getResources(), structurePripretionData.getStructureImg1());
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
                if (Util.isValidImageSize(imageFile)) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), finalUri);
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
}
