package com.platform.view.fragments;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
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
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.platform.BuildConfig;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.listeners.CustomSpinnerListener;
import com.platform.models.SujalamSuphalam.OperatorDetails;
import com.platform.models.common.CustomSpinnerObject;
import com.platform.models.events.CommonResponse;
import com.platform.models.login.Login;
import com.platform.presenter.MachineDetailsFragmentPresenter;
import com.platform.presenter.MachineMouFourthFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.GPSTracker;
import com.platform.utility.Permissions;
import com.platform.utility.Urls;
import com.platform.utility.Util;
import com.platform.utility.VolleyMultipartRequest;
import com.platform.view.activities.MachineMouActivity;
import com.platform.view.activities.SSActionsActivity;
import com.platform.view.customs.CustomSpinnerDialogClass;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.platform.utility.Util.getLoginObjectFromPref;
import static com.platform.utility.Util.getUserObjectFromPref;

public class MachineMouFourthFragment extends Fragment implements View.OnClickListener, APIDataListener,
        CustomSpinnerListener {
    private View machineMouFourthFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private Button btnFourthPartMou, btnPreviousMou;
    private MachineMouFourthFragmentPresenter machineMouFourthFragmentPresenter;
    private EditText etOperatorName, etOperatorLastName, etOperatorContact, etLicenseNumber, etOperatorTraining,
            etAppInstalled;
    private ImageView imgLicense;
    private ArrayList<CustomSpinnerObject> isTrainingDoneList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> isAppInstalledList = new ArrayList<>();
    private String selectedtrainingOption, selectedAppInstalledOption;
    private GPSTracker gpsTracker;
    private Location location;
    private Uri outputUri;
    private Uri finalUri;
    private final String TAG = MachineMouFourthFragment.class.getName();
    private RequestQueue rQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineMouFourthFragmentView = inflater.inflate(R.layout.fragment_machine_mou_fourth, container, false);
        return machineMouFourthFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        progressBarLayout = machineMouFourthFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineMouFourthFragmentView.findViewById(R.id.pb_profile_act);
        btnFourthPartMou = machineMouFourthFragmentView.findViewById(R.id.btn_fourth_part_mou);
        btnFourthPartMou.setOnClickListener(this);
        btnPreviousMou = machineMouFourthFragmentView.findViewById(R.id.btn_previous_mou);
        btnPreviousMou.setOnClickListener(this);
        etOperatorName = machineMouFourthFragmentView.findViewById(R.id.et_operator_name);
        etOperatorLastName = machineMouFourthFragmentView.findViewById(R.id.et_operator_last_name);
        etOperatorContact = machineMouFourthFragmentView.findViewById(R.id.et_operator_contact);
        etLicenseNumber = machineMouFourthFragmentView.findViewById(R.id.et_license_number);
        imgLicense = machineMouFourthFragmentView.findViewById(R.id.img_license);
        imgLicense.setOnClickListener(this);
        etOperatorTraining = machineMouFourthFragmentView.findViewById(R.id.et_operator_training);
        etOperatorTraining.setOnClickListener(this);
        etAppInstalled = machineMouFourthFragmentView.findViewById(R.id.et_app_installed);
        etAppInstalled.setOnClickListener(this);
        CustomSpinnerObject optionYes = new CustomSpinnerObject();
        optionYes.setName("Yes");
        optionYes.set_id("1");
        optionYes.setSelected(false);
        isTrainingDoneList.add(optionYes);
        isAppInstalledList.add(optionYes);
        CustomSpinnerObject optionNo = new CustomSpinnerObject();
        optionNo.setName("No");
        optionNo.set_id("2");
        optionNo.setSelected(false);
        isTrainingDoneList.add(optionNo);
        isAppInstalledList.add(optionNo);
        gpsTracker = new GPSTracker(getActivity());
        machineMouFourthFragmentPresenter = new MachineMouFourthFragmentPresenter(this);
    }

    private void setMachineFourthData() {
        OperatorDetails operatorDetails = new OperatorDetails();
        ((MachineMouActivity) getActivity()).getMachineDetailData().setOperatorDetails(operatorDetails);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getOperatorDetails().setFirstName
                (etOperatorName.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getOperatorDetails().setLastName
                (etOperatorLastName.getText().toString().trim());
//        ((MachineMouActivity) getActivity()).getMachineDetailData().getOperatorDetails().setAddress
//                (etProviderFirstName.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getOperatorDetails().setContactNumnber
                (etOperatorContact.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getOperatorDetails().setLicenceNumber
                (etLicenseNumber.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getOperatorDetails().
                setIsTrainingDone(selectedtrainingOption);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getOperatorDetails().
                setIsAppInstalled(selectedAppInstalledOption);
//        List<String> operatorImages  = new ArrayList();
//        operatorImages.add("www.google.com");
//        ((MachineMouActivity) getActivity()).getMachineDetailData().getOperatorDetails().setOperatorImages(operatorImages);
        if (gpsTracker.isGPSEnabled(getActivity(), this)) {
            location = gpsTracker.getLocation();
            if (location != null) {
                ((MachineMouActivity) getActivity()).getMachineDetailData().setFormLat(String.valueOf(location.getLatitude()));
                ((MachineMouActivity) getActivity()).getMachineDetailData().setFormLong(String.valueOf(location.getLongitude()));
            }
        }

        //machineMouFourthFragmentPresenter.submitMouData(((MachineMouActivity) getActivity()).getMachineDetailData());
        uploadData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (machineMouFourthFragmentPresenter != null) {
            machineMouFourthFragmentPresenter.clearData();
            machineMouFourthFragmentPresenter = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_fourth_part_mou:
                if(isAllDataValid()) {
                    setMachineFourthData();
                }
                break;
            case R.id.btn_previous_mou:
                getActivity().onBackPressed();
                break;
            case R.id.et_operator_training:
                CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Is Training Completed?", isTrainingDoneList,
                        false);
                cdd1.show();
                cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_app_installed:
                CustomSpinnerDialogClass cdd2 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Is App Installed?", isAppInstalledList,
                        false);
                cdd2.show();
                cdd2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.img_license:
                onAddImageClick();
                break;
        }
    }

    public boolean isAllDataValid() {
        if (TextUtils.isEmpty(etOperatorName.getText().toString().trim())
                || TextUtils.isEmpty(etOperatorContact.getText().toString().trim())
                || TextUtils.isEmpty(etLicenseNumber.getText().toString().trim())
                || TextUtils.isEmpty(etOperatorTraining.getText().toString().trim())
                || TextUtils.isEmpty(etAppInstalled.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.enter_correct_details),
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
                imgLicense.setImageURI(finalUri);
                if (Util.isValidImageSize(imageFile)) {
                    ((MachineMouActivity) getActivity()).getImageHashmap().put("licenseImage", bitmap);
                } else {
                    Util.showToast(getString(R.string.msg_big_image), this);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private void backToMachineList(){
        getActivity().finish();
        Intent intent = new Intent(getActivity(), SSActionsActivity.class);
        intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
        intent.putExtra("viewType", 2);
        intent.putExtra("title", "Machine List");
        getActivity().startActivity(intent);
    }

    private void uploadData(){
        String upload_URL = BuildConfig.BASE_URL + Urls.SSModule.MACHINE_MOU_FORM;
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, upload_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        rQueue.getCache().clear();
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            CommonResponse responseOBJ = new Gson().fromJson(jsonString, CommonResponse.class);
                            if(responseOBJ.getStatus() == 200){
                                Util.showToast(responseOBJ.getMessage(), this);
                            } else {
                                Util.showToast(getResources().getString(R.string.msg_something_went_wrong), this);
                            }
                            Log.d("response -",jsonString);
                            backToMachineList();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", new Gson().toJson(((MachineMouActivity) getActivity()).getMachineDetailData()));
                if(location != null) {
                    params.put("lat", String.valueOf(location.getLatitude()));
                    params.put("long ", String.valueOf(location.getLongitude()));
                }
                params.put("imageArraySize", String.valueOf(((MachineMouActivity) getActivity()).getImageHashmap().size()));//add string parameters
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
                Iterator myVeryOwnIterator = ((MachineMouActivity) getActivity()).getImageHashmap().keySet().iterator();
                for (int i = 0;i<((MachineMouActivity) getActivity()).getImageHashmap().size(); i++) {
                    String key=(String)myVeryOwnIterator.next();
                    drawable = new BitmapDrawable(getResources(), ((MachineMouActivity) getActivity()).getImageHashmap().get(key));
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

    private void showResponseDialog(String dialogTitle, String message, String btn1String, String
            btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        if (!TextUtils.isEmpty(dialogTitle)) {
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(dialogTitle);
            title.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
            text.setText(message);
            text.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(btn1String)) {
            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setText(btn1String);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                // Close dialog
                dialog.dismiss();
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                // Close dialog
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private byte[] getFileDataFromDrawable(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void showResponse(String responseStatus, String requestId, int status) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);
        if(requestId.equals(MachineMouFourthFragmentPresenter.SUBMIT_MOU)){
            if(status == 200){
                getActivity().finish();
                Intent intent = new Intent(getActivity(), SSActionsActivity.class);
                intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
                intent.putExtra("viewType", 2);
                intent.putExtra("title", "Machine List");
                getActivity().startActivity(intent);
            }
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (getActivity() != null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.msg_failure),
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (getActivity() != null) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.msg_failure),
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (requestID.equalsIgnoreCase(MachineMouFourthFragmentPresenter.SUBMIT_MOU)) {
            CommonResponse responseOBJ = new Gson().fromJson(response, CommonResponse.class);
            showResponseDialog("Confirmation", responseOBJ.getMessage(), "OK", "");
        }
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

    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Is Training Completed?":
                for (CustomSpinnerObject trainingOption : isTrainingDoneList) {
                    if (trainingOption.isSelected()) {
                        selectedtrainingOption = trainingOption.getName();
                        break;
                    }
                }
                etOperatorTraining.setText(selectedtrainingOption);
                break;
            case "Is App Installed?":
                for (CustomSpinnerObject appInstallationOption : isAppInstalledList) {
                    if (appInstallationOption.isSelected()) {
                        selectedAppInstalledOption = appInstallationOption.getName();
                        break;
                    }
                }
                etAppInstalled.setText(selectedAppInstalledOption);
                break;
        }
    }
}
