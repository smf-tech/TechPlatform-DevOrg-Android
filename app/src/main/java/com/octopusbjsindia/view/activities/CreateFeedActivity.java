package com.octopusbjsindia.view.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.R;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static com.octopusbjsindia.utility.Util.getLoginObjectFromPref;
import static com.octopusbjsindia.utility.Util.getUserObjectFromPref;


public class CreateFeedActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AppCompatActivity.class.getName();
    final String upload_URL = BuildConfig.BASE_URL + Urls.Feeds.NEW_FEED;
    private RequestQueue rQueue;
    private HashMap<String, Bitmap> imageHashmap = new HashMap<>();
    private int imageCount = 0;
    Map<String,String> requestData = new HashMap<>();
    private Uri outputUri;
    private Uri finalUri;
    RelativeLayout progressBar;
    EditText etTitle, etDescription, etUrl;
    ImageView ivFeedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_feed);
        setTitle(getResources().getString(R.string.create_feed));
        progressBar = findViewById(R.id.ly_progress_bar);
        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etUrl = findViewById(R.id.et_url);
        ivFeedImage = findViewById(R.id.iv_feed_image);
        ivFeedImage.setOnClickListener(this);
        findViewById(R.id.bt_publish).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.iv_feed_image:
                onAddImageClick();
                break;
            case R.id.bt_publish:
                if (isAllDataValid()) {
                    uploadImage();
                }
                break;
        }
    }

    public void setTitle(String title) {
        TextView tvTitle = findViewById(R.id.toolbar_title);
        ImageView back = findViewById(R.id.toolbar_back_action);
        tvTitle.setText(title);
        back.setOnClickListener(this);
    }

    private void onAddImageClick() {
        if (Permissions.isCameraPermissionGranted(this, this)) {
            showPictureDialog();
        }
    }

    private boolean isAllDataValid() {

        if (TextUtils.isEmpty(etTitle.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please Enter Title.", Snackbar.LENGTH_LONG);
            return false;
        } else if(TextUtils.isEmpty(etDescription.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please Enter Description.", Snackbar.LENGTH_LONG);
            return false;
        } else {
            requestData.put("title",etTitle.getText().toString());
            requestData.put("description",etDescription.getText().toString());
            requestData.put("external_url",etUrl.getText().toString());
        }

        return true;
    }

    private void uploadImage() {
        showProgressBar();
        Log.d("url :", upload_URL);
        Log.d("request :", new Gson().toJson(requestData));

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        rQueue.getCache().clear();
                        hideProgressBar();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            CommonResponse commonResponse = new Gson().fromJson(jsonString, CommonResponse.class);
                            if (commonResponse.getStatus() == 200) {
                                Util.showToast(commonResponse.getMessage(), this);
                                finish();
                            } else {
                                Util.showToast(commonResponse.getMessage(), this);
                            }
                            Log.d("response :", jsonString);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Toast.makeText(CreateFeedActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressBar();
                        Toast.makeText(CreateFeedActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", new Gson().toJson(requestData));
                params.put("imageArraySize", String.valueOf(imageHashmap.size()));
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

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private byte[] getFileDataFromDrawable(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
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
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), finalUri);
                        ivFeedImage.setImageURI(finalUri);
                        imageHashmap.put("image" + imageCount, bitmap);
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


}
