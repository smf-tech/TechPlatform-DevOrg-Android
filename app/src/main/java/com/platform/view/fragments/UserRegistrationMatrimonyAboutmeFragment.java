package com.platform.view.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.platform.R;
import com.platform.utility.Constants;
import com.platform.utility.Permissions;
import com.platform.utility.Util;
import com.platform.view.activities.UserRegistrationMatrimonyActivity;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class UserRegistrationMatrimonyAboutmeFragment extends Fragment implements View.OnClickListener {
    private final String TAG = UserRegistrationMatrimonyAboutmeFragment.class.getName();
    private View fragmentview;
    private String uploadImageType = "";
    private Button btn_load_next, btn_loadprevious;
    private TextView tv_pagetitle;
    private ImageView img_user_profle, img_education_cert, img_adhar;
    private EditText et_education, et_about_me, et_partner_expectation, et_achivements, et_other_remark;
    private CheckBox checkbox_community_preference;
    //image upload
    private Uri outputUri;
    private Uri finalUri;

    public static UserRegistrationMatrimonyAboutmeFragment newInstance() {
        return new UserRegistrationMatrimonyAboutmeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentview = inflater.inflate(R.layout.user_registration_matrimony_fragment_aboutme, container, false);
        initViews();
        return fragmentview;
    }

    private void initViews() {
        //views
        tv_pagetitle = fragmentview.findViewById(R.id.tv_pagetitle);
        //Button
        btn_load_next = fragmentview.findViewById(R.id.btn_loadnext);
        btn_loadprevious = fragmentview.findViewById(R.id.btn_loadprevious);
        //image view
        img_user_profle = fragmentview.findViewById(R.id.img_user_profle);
        img_education_cert = fragmentview.findViewById(R.id.img_education_cert);
        img_adhar = fragmentview.findViewById(R.id.img_adhar);

        //edittext

        et_about_me = fragmentview.findViewById(R.id.et_about_me);
        et_partner_expectation = fragmentview.findViewById(R.id.et_partner_expectation);
        et_achivements = fragmentview.findViewById(R.id.et_achivements);
        et_other_remark = fragmentview.findViewById(R.id.et_other_remark);


        //add listeners
        btn_load_next.setOnClickListener(this);
        btn_loadprevious.setOnClickListener(this);

        img_user_profle.setOnClickListener(this);
        img_education_cert.setOnClickListener(this);
        img_adhar.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //tvmessage.setText("Fragment ONe");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_loadnext:
                Util.showToast("Call Submit", getActivity());
                setValuesInModel();
              //  ((UserRegistrationMatrimonyActivity) getActivity()).submitUserRegistrationRequest();
                break;
            case R.id.btn_loadprevious:
                ((UserRegistrationMatrimonyActivity) getActivity()).loadNextScreen(3);
                break;
            case R.id.img_user_profle:
                uploadImageType = Constants.Image.IMAGE_TYPE_PROFILE;
                onAddImageClick();
                break;
            case R.id.img_education_cert:
                uploadImageType = Constants.Image.IMAGE_TYPE_EDUCATION;
                onAddImageClick();
                break;
            case R.id.img_adhar:
                uploadImageType = Constants.Image.IMAGE_TYPE_ADHARCARD;
                onAddImageClick();
                break;

        }
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
                    + "/MV/Image/picture.jpg";

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


        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
            try {
                String imageFilePath = getImageName();
                if (imageFilePath == null) return;

                finalUri = Util.getUri(imageFilePath);
                Crop.of(outputUri, finalUri).start(getContext(), this);
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
                    Crop.of(outputUri, finalUri).start(getContext(), this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {

                final File imageFile = new File(Objects.requireNonNull(finalUri.getPath()));

                if (Util.isConnected(getActivity())) {
                    if (Util.isValidImageSize(imageFile)) {
                        //profilePresenter.uploadProfileImage(imageFile, Constants.Image.IMAGE_TYPE_PROFILE);
                        UserRegistrationMatrimonyActivity.userRegistrationMatrimonyActivityPresenter.uploadProfileImage(imageFile, Constants.Image.IMAGE_TYPE_PROFILE, uploadImageType);
                        if (Constants.Image.IMAGE_TYPE_PROFILE.equalsIgnoreCase(uploadImageType)) {
                            img_user_profle.setImageURI(finalUri);
                        } else if (Constants.Image.IMAGE_TYPE_ADHARCARD.equalsIgnoreCase(uploadImageType)) {
                            img_adhar.setImageURI(finalUri);
                        }
                        if (Constants.Image.IMAGE_TYPE_EDUCATION.equalsIgnoreCase(uploadImageType)) {
                            img_education_cert.setImageURI(finalUri);
                        }

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

    /*public void onImageUploaded(String uploadedImageUrl) {
        mImageUploaded = true;
        mUploadedImageUrl = uploadedImageUrl;
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.CAMERA_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showPictureDialog();
            }
        }
    }


    //set selected data for request
    private void setValuesInModel() {
        if (isAllInputsValid())
        {
        if (UserRegistrationMatrimonyActivity.matrimonyUserRegRequestModel != null) {
            if (UserRegistrationMatrimonyActivity.otherMaritialInformation != null) {

                UserRegistrationMatrimonyActivity.otherMaritialInformation.setAbout_me(et_about_me.getText().toString());
                UserRegistrationMatrimonyActivity.otherMaritialInformation.setExpectation_from_life_partner(et_partner_expectation.getText().toString());
                UserRegistrationMatrimonyActivity.otherMaritialInformation.setActivity_achievements(et_achivements.getText().toString());
                UserRegistrationMatrimonyActivity.otherMaritialInformation.setOther_remarks(et_other_remark.getText().toString());
                //UserRegistrationMatrimonyActivity.otherMaritialInformation.setProfile_image("");


                UserRegistrationMatrimonyActivity.matrimonyUserRegRequestModel.setOther_maritial_information(UserRegistrationMatrimonyActivity.otherMaritialInformation);

                ((UserRegistrationMatrimonyActivity) getActivity()).submitUserRegistrationRequest();
            } else {
                Util.showToast("null object getPersonal_details()", getActivity());
            }
        } else {
            Util.showToast("null object", getActivity());
        }
    }
    }



    private boolean isAllInputsValid() {
        String msg = "";

        /*if (et_Annual_income.getText().toString().trim().length() == 0) {
            msg = "Please enter the mobile annual income";//getResources().getString(R.string.msg_enter_name);
        } else*/
        if (UserRegistrationMatrimonyActivity.otherMaritialInformation.getProfile_image().trim().length() == 0) {
            msg = "Please upload profile image.";//getResources().getString(R.string.msg_enter_proper_date);
        }
        else if (UserRegistrationMatrimonyActivity.otherMaritialInformation.getAadhar_url().trim().length() == 0) {
            msg = "Please upload Adhar card image.";//getResources().getString(R.string.msg_enter_name);
        } else if (UserRegistrationMatrimonyActivity.otherMaritialInformation.getEducational_url().trim().length() == 0) {
            msg = "Please education certificate image."; //getResources().getString(R.string.msg_enter_name);
        }
        /*else if (et_education.getText().toString().trim().length() == 0) {
            msg = "Please enter the qualification.";//getResources().getString(R.string.msg_enter_proper_date);
        }*/

        if (TextUtils.isEmpty(msg)) {
            return true;
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        return false;
    }
}
