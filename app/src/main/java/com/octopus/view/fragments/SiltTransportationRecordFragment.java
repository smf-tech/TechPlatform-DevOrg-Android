package com.octopus.view.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.octopus.BuildConfig;
import com.octopus.R;
import com.octopus.listeners.APIDataListener;
import com.octopus.models.SujalamSuphalam.SiltTransportRecord;
import com.octopus.models.events.CommonResponse;
import com.octopus.models.login.Login;
import com.octopus.utility.Constants;
import com.octopus.utility.Permissions;
import com.octopus.utility.Urls;
import com.octopus.utility.Util;
import com.octopus.utility.VolleyMultipartRequest;
import com.octopus.view.activities.SSActionsActivity;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.octopus.utility.Util.getLoginObjectFromPref;
import static com.octopus.utility.Util.getUserObjectFromPref;

public class SiltTransportationRecordFragment extends Fragment  implements APIDataListener, View.OnClickListener{

    private View siltTransportationRecordFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    //private SiltTransportationRecordFragmentPresenter siltTransportationRecordFragmentPresenter;
    String machineId, currentStructureId;
    private ImageView imgRegisterOne, imgRegisterTwo, imgRegisterThree, clickedImageView;
    private Uri outputUri;
    private Uri finalUri;
    private final String TAG = MachineDieselRecordFragment.class.getName();
    private RequestQueue rQueue;
    private HashMap<String, Bitmap> imageHashmap = new HashMap<>();
    private int imageCount = 0;
    private Button btnSubmit;
    private EditText etDate, etTractorTripsCount, etTipperTripsCount, etFarmersCount, etBeneficiariesCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        siltTransportationRecordFragmentView = inflater.inflate(R.layout.fragment_silt_transportation_record,
                container, false);
        return siltTransportationRecordFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        machineId = getActivity().getIntent().getStringExtra("machineId");
        currentStructureId = getActivity().getIntent().getStringExtra("currentStructureId");
        init();
    }

    private void init() {
        progressBarLayout = siltTransportationRecordFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = siltTransportationRecordFragmentView.findViewById(R.id.pb_profile_act);
        //siltTransportationRecordFragmentPresenter = new SiltTransportationRecordFragmentPresenter(this);
        etDate = siltTransportationRecordFragmentView.findViewById(R.id.et_date);
        etDate.setOnClickListener(this);
        etTractorTripsCount = siltTransportationRecordFragmentView.findViewById(R.id.et_tractor_trips_count);
        etTipperTripsCount = siltTransportationRecordFragmentView.findViewById(R.id.et_tipper_trips_count);
        etFarmersCount = siltTransportationRecordFragmentView.findViewById(R.id.et_farmers_count);
        etBeneficiariesCount = siltTransportationRecordFragmentView.findViewById(R.id.et_beneficiaries_count);
        imgRegisterOne = siltTransportationRecordFragmentView.findViewById(R.id.img_register_one);
        imgRegisterOne.setOnClickListener(this);
        imgRegisterTwo = siltTransportationRecordFragmentView.findViewById(R.id.img_register_two);
        imgRegisterTwo.setOnClickListener(this);
        imgRegisterThree = siltTransportationRecordFragmentView.findViewById(R.id.img_register_three);
        imgRegisterThree.setOnClickListener(this);
        btnSubmit = siltTransportationRecordFragmentView.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);
        if(!Util.isConnected(getActivity())) {
            Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        if (siltTransportationRecordFragmentPresenter != null) {
//            siltTransportationRecordFragmentPresenter.clearData();
//            siltTransportationRecordFragmentPresenter = null;
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_date:
                Util.showDateDialog(getActivity(), etDate);
                break;
            case R.id.img_register_one:
                clickedImageView = imgRegisterOne;
                onAddImageClick();
                break;
            case R.id.img_register_two:
                clickedImageView = imgRegisterTwo;
                onAddImageClick();
                break;
            case R.id.img_register_three:
                clickedImageView = imgRegisterThree;
                onAddImageClick();
                break;
            case R.id.btn_submit:
                if(Util.isConnected(getActivity())) {
                    if (isAllDataValid()) {
                        SiltTransportRecord siltTransportRecord = new SiltTransportRecord();
                        siltTransportRecord.setStructureId(currentStructureId);
                        siltTransportRecord.setMachineId(machineId);
                        siltTransportRecord.setSiltTransportDate(Util.dateTimeToTimeStamp(etDate.getText().toString(),
                                "00:00"));
                        siltTransportRecord.setTractorTripsCount(etTractorTripsCount.getText().toString());
                        siltTransportRecord.setTipperTripsCount(etTipperTripsCount.getText().toString());
                        siltTransportRecord.setFarmersCount(etFarmersCount.getText().toString());
                        siltTransportRecord.setBeneficiariesCount(etBeneficiariesCount.getText().toString());
                        uploadData(siltTransportRecord);
                    }
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                }
                break;
        }
    }

    private boolean isAllDataValid() {
        if(imageCount == 0) {
            if (TextUtils.isEmpty(etDate.getText().toString().trim())
                || TextUtils.isEmpty(etTractorTripsCount.getText().toString().trim())
                || TextUtils.isEmpty(etTipperTripsCount.getText().toString().trim())
                || TextUtils.isEmpty(etFarmersCount.getText().toString().trim())
                || TextUtils.isEmpty(etBeneficiariesCount.getText().toString().trim())) {

                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                .findViewById(android.R.id.content), getString(R.string.enter_correct_details),
                        Snackbar.LENGTH_LONG);

                return false;
            }
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please, click image of register.",
                    Snackbar.LENGTH_LONG);
            return false;
        }
        return true;
    }

    private void onAddImageClick() {
        if (Permissions.isCameraPermissionGranted(getActivity(), this)) {
            showPictureDialog();
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
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
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_error_in_photo_gallery),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void takePhotoFromCamera() {
        try {
            //use standard intent to capture an image
            String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/Octopus/Image/picture.jpg";

            File imageFile = new File(imageFilePath);
            outputUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName()
                    + ".file_provider", imageFile);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(takePictureIntent, Constants.CHOOSE_IMAGE_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            //display an error message
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_image_capture_not_support),
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_take_photo_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            try {
                String imageFilePath = Util.getImageName();
                if (imageFilePath == null) return;
                finalUri = Util.getUri(imageFilePath);
                Crop.of(outputUri, finalUri).start(getContext(), this);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    String imageFilePath = Util.getImageName();
                    if (imageFilePath == null) return;
                    outputUri = data.getData();
                    finalUri = Util.getUri(imageFilePath);
                    Crop.of(outputUri, finalUri).start(getContext(), this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                final File imageFile = new File(Objects.requireNonNull(finalUri.getPath()));
                Bitmap bitmap = Util.compressImageToBitmap(imageFile);
                clickedImageView.setImageURI(finalUri);
                if (Util.isValidImageSize(imageFile)) {
                    imageHashmap.put("register"+imageCount, bitmap);
                    imageCount++;
                } else {
                    Util.showToast(getString(R.string.msg_big_image), this);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private void uploadData(SiltTransportRecord siltTransportRecord){
        showProgressBar();
        String upload_URL = BuildConfig.BASE_URL + Urls.SSModule.MACHINE_SILT_TRANSPORT_RECORD_FORM;
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        rQueue.getCache().clear();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            CommonResponse responseOBJ = new Gson().fromJson(jsonString, CommonResponse.class);
                            hideProgressBar();
                            if(responseOBJ.getStatus() == 200){
                                Util.showToast(responseOBJ.getMessage(), this);
                            } else {
                                Util.showToast(getResources().getString(R.string.msg_something_went_wrong), this);
                            }
                            Log.d("response -",jsonString);
                            backToMachineList();
                        } catch (UnsupportedEncodingException e) {
                            hideProgressBar();
                            e.printStackTrace();
                            Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressBar();
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", new Gson().toJson(siltTransportRecord));
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
                    if (getUserObjectFromPref().getOrgId()!=null) {
                        headers.put("orgId", getUserObjectFromPref().getOrgId());
                    }
                    if (getUserObjectFromPref().getProjectIds()!=null) {
                        headers.put("projectId", getUserObjectFromPref().getProjectIds().get(0).getId());
                    }
                    if (getUserObjectFromPref().getRoleIds()!=null) {
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
                for (int i = 0;i<imageHashmap.size(); i++) {
                    String key=(String)myVeryOwnIterator.next();
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
        rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(volleyMultipartRequest);
    }

    private byte[] getFileDataFromDrawable(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void backToMachineList(){
        getActivity().finish();
        Intent intent = new Intent(getActivity(), SSActionsActivity.class);
        intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
        intent.putExtra("viewType", 2);
        intent.putExtra("title", "Machine List");
        getActivity().startActivity(intent);
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
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {
        getActivity().finish();
    }
}
