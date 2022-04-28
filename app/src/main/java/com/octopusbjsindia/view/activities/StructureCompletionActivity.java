package com.octopusbjsindia.view.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.SujalamSuphalam.StructureData;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.login.Login;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.utility.VolleyMultipartRequest;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.octopusbjsindia.utility.Util.getLoginObjectFromPref;
import static com.octopusbjsindia.utility.Util.getUserObjectFromPref;

public class StructureCompletionActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = StructurePreparationActivity.class.getName();
    private final String STRUCTURE_DATA = "StructureData";
    final String STRUCTURE_STATUS = "StructureStatus";
    private ImageView selecteIV;
    private TextInputLayout lyReason;
    private EditText etReason;
//    etSiltQantity, etWorkStartDate, etWorkCompletionDate, etOperationalDays, etDieselConsumedAmount,
//            etDieselConsumedQuantity, etWorkDimension;
    private Uri outputUri;
    private Uri finalUri;
    private RelativeLayout progressBar;
    final String upload_URL = BuildConfig.BASE_URL + Urls.SSModule.STRUCTURE_COMPLETION;
    private RequestQueue rQueue;
    private HashMap<String, Bitmap> imageHashmap = new HashMap<>();
    private int imageCount = 0;
    private Map<String, String> requestData = new HashMap<>();
    private StructureData structureData;
    private int structureStatus;
    private String completion = "true";
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_structure_completion);

        structureData = (StructureData) getIntent().getSerializableExtra(STRUCTURE_DATA);
        structureStatus = getIntent().getIntExtra(STRUCTURE_STATUS, 0);
        requestData.put("structure_id", structureData.getStructureId());
        progressBar = findViewById(R.id.progress_bar);
        initView();
        if (structureStatus == Constants.SSModule.STRUCTURE_COMPLETED) {
            setTitle("Close Structure");
            TextView tvLabel = findViewById(R.id.tv_photo_lbl);
            tvLabel.setText("Certificate Images");
        } else {
            setTitle("Structure Completion");
        }
    }

    private void initView() {
        RadioGroup rgCompletion = findViewById(R.id.rg_completion);
//        etSiltQantity = findViewById(R.id.et_silt_qantity);
//        etWorkStartDate = findViewById(R.id.et_work_start_date);
//        etWorkCompletionDate = findViewById(R.id.et_work_completion_date);
//        etOperationalDays = findViewById(R.id.et_operational_days);
//        etDieselConsumedAmount = findViewById(R.id.et_diesel_consumed_amount);
//        etDieselConsumedQuantity = findViewById(R.id.et_diesel_consumed_quantity);
//        etWorkDimension = findViewById(R.id.et_work_dimension);
        lyReason = findViewById(R.id.ly_reason);
        etReason = findViewById(R.id.et_reason);

        ImageView iv1 = findViewById(R.id.iv_structure1);
        iv1.setOnClickListener(this);
        ImageView iv2 = findViewById(R.id.iv_structure2);
        iv2.setOnClickListener(this);

        if (structureStatus == Constants.SSModule.STRUCTURE_COMPLETED
                || structureStatus == Constants.SSModule.STRUCTURE_PARTIALLY_COMPLETED) {
            //findViewById(R.id.ly_closer).setVisibility(View.VISIBLE);
            iv1.setImageResource(R.drawable.ic_certifict);
            iv2.setImageResource(R.drawable.ic_certifict);

            findViewById(R.id.tv_complition).setVisibility(View.GONE);
            rgCompletion.setVisibility(View.GONE);
            findViewById(R.id.iv_structure3).setVisibility(View.GONE);
            findViewById(R.id.iv_structure4).setVisibility(View.GONE);
            TextView tv = findViewById(R.id.tv_img1);
            tv.setText("Completion Certificate 1");
            TextView tv1 = findViewById(R.id.tv_img2);
            tv1.setText("Completion Certificate 2");

            findViewById(R.id.tv_img3).setVisibility(View.GONE);
            findViewById(R.id.tv_img4).setVisibility(View.GONE);
        } else {
            //findViewById(R.id.ly_closer).setVisibility(View.GONE);
            findViewById(R.id.iv_structure3).setOnClickListener(this);
            findViewById(R.id.iv_structure4).setOnClickListener(this);
            rgCompletion.check(R.id.rb_completion_yes);
            rgCompletion.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    switch (i) {
                        case R.id.rb_completion_yes:
                            lyReason.setVisibility(View.GONE);
                            completion = "true";
                            break;
                        case R.id.rb_completion_no:
                            lyReason.setVisibility(View.VISIBLE);
                            completion = "false";
                            break;
                    }
                }
            });
        }

        findViewById(R.id.bt_submit).setOnClickListener(this);
//        etWorkStartDate.setOnClickListener(this);
//        etWorkCompletionDate.setOnClickListener(this);

    }

    public void setTitle(String title) {
        TextView tvTitle = findViewById(R.id.toolbar_title);
        tvTitle.setText(title);
        findViewById(R.id.toolbar_back_action).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_structure1:
                selecteIV = findViewById(R.id.iv_structure1);
                onAddImageClick();
                break;
            case R.id.iv_structure2:
                selecteIV = findViewById(R.id.iv_structure2);
                onAddImageClick();
                break;
            case R.id.iv_structure3:
                selecteIV = findViewById(R.id.iv_structure3);
                onAddImageClick();
                break;
            case R.id.iv_structure4:
                selecteIV = findViewById(R.id.iv_structure4);
                onAddImageClick();
                break;
//            case R.id.et_work_start_date:
//                Util.showDateDialog(this, etWorkStartDate);
//                break;
//            case R.id.et_work_completion_date:
//                Util.showDateDialog(this, etWorkCompletionDate);
//                break;
            case R.id.bt_submit:
                if (isAllDataValid()) {
                    uploadImage();
                }
                break;
            case R.id.toolbar_back_action:
                finish();
                break;
        }
    }

    private boolean isAllDataValid() {

//        GPSTracker gpsTracker = new GPSTracker(this);
//        Location location = null;
//        if (gpsTracker.isGPSEnabled(this, this)) {
//            location = gpsTracker.getLocation();
//        } else {
//            Util.snackBarToShowMsg(this.getWindow().getDecorView()
//                            .findViewById(android.R.id.content), "Location not available",
//                    Snackbar.LENGTH_LONG);
//            return false;
//        }

        requestData.put("is_completed", completion);
        if (structureStatus == Constants.SSModule.STRUCTURE_COMPLETED
                || structureStatus == Constants.SSModule.STRUCTURE_PARTIALLY_COMPLETED) {

//            if (TextUtils.isEmpty(etSiltQantity.getText().toString())) {
//                Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
//                        "Please, enter Silt Qantity.", Snackbar.LENGTH_LONG);
//                return false;
//            } else if (TextUtils.isEmpty(etWorkStartDate.getText().toString())) {
//                Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
//                        "Please, enter Work Start Date.", Snackbar.LENGTH_LONG);
//                return false;
//            } else if (TextUtils.isEmpty(etWorkCompletionDate.getText().toString())) {
//                Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
//                        "Please, enter Work Completion Date.", Snackbar.LENGTH_LONG);
//                return false;
//            } else if (TextUtils.isEmpty(etOperationalDays.getText().toString())) {
//                Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
//                        "Please, enter Operational Days.", Snackbar.LENGTH_LONG);
//                return false;
//            } else if (TextUtils.isEmpty(etDieselConsumedAmount.getText().toString())) {
//                Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
//                        "Please, enter Amount of Diesel Consumed (Rs.).", Snackbar.LENGTH_LONG);
//                return false;
//            } else if (TextUtils.isEmpty(etDieselConsumedQuantity.getText().toString())) {
//                Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
//                        "Please, enter Diesel Consumed (Liters).", Snackbar.LENGTH_LONG);
//                return false;
//            } else if (TextUtils.isEmpty(etWorkDimension.getText().toString())) {
//                Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
//                        "Please, enter Dimension of Work.", Snackbar.LENGTH_LONG);
//                return false;
//            } else {
//                requestData.put("etSiltQantity", etSiltQantity.getText().toString());
//                requestData.put("etWorkStartDate", etWorkStartDate.getText().toString());
//                requestData.put("etWorkCompletionDate", etWorkCompletionDate.getText().toString());
//                requestData.put("etOperationalDays", etOperationalDays.getText().toString());
//                requestData.put("etDieselConsumedAmount", etDieselConsumedAmount.getText().toString());
//                requestData.put("etDieselConsumedQuantity", etDieselConsumedQuantity.getText().toString());
//                requestData.put("etWorkDimension", etWorkDimension.getText().toString());
//            }

            if (imageCount < 2) {
                Util.snackBarToShowMsg(this.getWindow().getDecorView()
                                .findViewById(android.R.id.content), "Please, attach images of structure.",
                        Snackbar.LENGTH_LONG);
                return false;
            }
        } else {
            if (lyReason.getVisibility() == View.VISIBLE && TextUtils.isEmpty(etReason.getText().toString())) {
                Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                        "Please, enter Reason.", Snackbar.LENGTH_LONG);
                return false;
            } else {
                requestData.put("etReason", etReason.getText().toString());
            }

            if (imageCount < 2) {
                Util.snackBarToShowMsg(this.getWindow().getDecorView()
                                .findViewById(android.R.id.content), "Please, click images of structure.",
                        Snackbar.LENGTH_LONG);
                return false;
            }
        }

        return true;
    }


    private void onAddImageClick() {
        if (Permissions.isCameraPermissionGranted(this, this)) {
            showPictureDialog();
//            takePhotoFromCamera();
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
                if (Util.isConnected(this)) {
                    if (Util.isValidImageSize(imageFile)) {
                        Bitmap bitmap = Util.compressImageToBitmap(imageFile);
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), finalUri);
                        selecteIV.setImageURI(finalUri);
                        imageHashmap.put("Structure" + imageCount, bitmap);
                        imageCount++;
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


    private void uploadImage() {

        Log.d("request -", new Gson().toJson(requestData));
        progressBar.setVisibility(View.VISIBLE);
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        progressBar.setVisibility(View.GONE);
                        rQueue.getCache().clear();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            CommonResponse commonResponse = new Gson().fromJson(jsonString, CommonResponse.class);
                            if (commonResponse.getStatus() == 200) {
                                Util.showToast(commonResponse.getMessage(), this);
                                finish();
                            } else {
                                Util.showToast(commonResponse.getMessage(), this);
                            }
                            Log.d("response -", jsonString);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Toast.makeText(StructureCompletionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(StructureCompletionActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", new Gson().toJson(requestData));
                if (structureStatus == Constants.SSModule.STRUCTURE_COMPLETED) {
                    params.put("structureStatus", Constants.SSModule.STRUCTURE_CLOSED + "");
                } else if (structureStatus == Constants.SSModule.STRUCTURE_PARTIALLY_COMPLETED) {
                    params.put("structureStatus", Constants.SSModule.STRUCTURE_PARTIALLY_CLOSED + "");
                } else {
                    params.put("structureStatus", Constants.SSModule.STRUCTURE_COMPLETED + "");
                }
                params.put("imageArraySize", String.valueOf(imageHashmap.size()));//add string parameters
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
                Drawable drawable = null;
                Iterator myVeryOwnIterator = imageHashmap.keySet().iterator();
                for (int i = 0; i < imageHashmap.size(); i++) {
                    String key = (String) myVeryOwnIterator.next();
                    drawable = new BitmapDrawable(getResources(), imageHashmap.get(key));
                    params.put(key, new DataPart(key, getFileDataFromDrawable(drawable),
                            "image/jpeg"));
                }
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


}
