package com.octopusbjsindia.matrimonyregistration;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octopusbjsindia.R;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class ProfileMatrimonyAboutmeFragment extends Fragment implements View.OnClickListener {
    private final String TAG = ProfileMatrimonyAboutmeFragment.class.getName();
    private View fragmentview;
    private String uploadImageType = "";
    private Button btn_load_next, btn_loadprevious;
    private TextView tv_pagetitle;
    private ImageView img_user_profle, img_education_cert, img_adhar, imgMaritalStatusCerificate;
    private EditText et_education, et_about_me, et_partner_expectation, et_achivements, et_other_remark;
    private RecyclerView rv_profile_pic;
    private CheckBox checkbox_community_preference;
    //image upload
    private Uri outputUri;
    private Uri finalUri;

    boolean isSquare = false;

    private RequestOptions requestOptions;
    private RequestOptions requestOptions_adhar;
    private RequestOptions requestOptions_edu;

    public static ProfileMatrimonyAboutmeFragment newInstance() {
        return new ProfileMatrimonyAboutmeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentview = inflater.inflate(R.layout.user_registration_matrimony_fragment_aboutme_new, container, false);
        return fragmentview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (((RegistrationActivity) getActivity()).matrimonialProfile != null
                && ((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation() != null) {
            //setData();
        }
        //et_about_me.setText("My name is "+((RegistrationActivity) getActivity()).personalDetails.getFirstName()+" and I am working as a "+((RegistrationActivity) getActivity()).occupationalDetails.getOccupation()+" in the "+((RegistrationActivity) getActivity()).occupationalDetails.getEmployerCompany()+". I have completed my "+((RegistrationActivity) getActivity()).educationalDetails.getQualificationDegree()+" I grew up in a middle class"+((RegistrationActivity) getActivity()).familyDetails.getFamilyType()+" family.");
        et_about_me.setText(((RegistrationActivity) getActivity()).aboutUsStr);
    }

    private void initViews() {

        requestOptions = new RequestOptions().placeholder(R.drawable.ic_image_holder);
        requestOptions = requestOptions.apply(RequestOptions.noTransformation());

        requestOptions_adhar = new RequestOptions().placeholder(R.drawable.ic_adhar_placeholder);
        requestOptions_adhar = requestOptions_adhar.apply(RequestOptions.noTransformation());

        requestOptions_edu = new RequestOptions().placeholder(R.drawable.ic_education_cert_placeholder);
        requestOptions_edu = requestOptions_edu.apply(RequestOptions.noTransformation());

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

        et_about_me.setText(((RegistrationActivity) getActivity()).aboutUsStr);
        if (((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails() != null &&
                !((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().
                        getMaritalStatus().equalsIgnoreCase("Unmarried")) {
            TextView tvMaritalStatusCerificateLabel = fragmentview.findViewById(R.id.tv_marital_status_cerificate_label);
            imgMaritalStatusCerificate = fragmentview.findViewById(R.id.img_marital_status_certificate);
            tvMaritalStatusCerificateLabel.setVisibility(View.VISIBLE);
            imgMaritalStatusCerificate.setVisibility(View.VISIBLE);
            imgMaritalStatusCerificate.setOnClickListener(this);
            if (((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().
                    getMaritalStatus().equalsIgnoreCase("Divorcee")) {
                tvMaritalStatusCerificateLabel.setText("Legal seperation certificate");
            } else {
                tvMaritalStatusCerificateLabel.setText("Partner's death certificate");
            }
        }
        if (((RegistrationActivity) getActivity()).matrimonialProfile != null
                && ((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation() != null) {
            setData();
        }
        //et_about_me.setText("My name is "+((RegistrationActivity) getActivity()).personalDetails.getFirstName()+" and I am working as a "+((RegistrationActivity) getActivity()).occupationalDetails.getOccupation()+" in the "+((RegistrationActivity) getActivity()).occupationalDetails.getEmployerCompany()+". I have completed my "+((RegistrationActivity) getActivity()).educationalDetails.getQualificationDegree()+" I grew up in a middle class"+((RegistrationActivity) getActivity()).familyDetails.getFamilyType()+" family.");
        fragmentview.findViewById(R.id.iv_toobar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RegistrationActivity) getActivity()).onBackPressed();
            }
        });
    }

    private void setData() {

        et_about_me.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation().getAboutMe());
        et_partner_expectation.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation().getExpectation_from_partner());
        et_achivements.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation().getActivityAchievements());
        et_other_remark.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation().getOtherRemarks());

        Glide.with(getActivity())
                .applyDefaultRequestOptions(requestOptions)
                .load(((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation().getProfileImage().get(0))
                .into(img_user_profle);
        ((RegistrationActivity) getActivity()).otherMaritialInformation.setProfileImage(
                ((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation().getProfileImage());


        Glide.with(getActivity())
                .applyDefaultRequestOptions(requestOptions_edu)
                .load(((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation().getEducationalUrl())
                .into(img_education_cert);
        ((RegistrationActivity) getActivity()).otherMaritialInformation.setEducationalUrl(
                ((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation().getEducationalUrl());


        Glide.with(getActivity())
                .applyDefaultRequestOptions(requestOptions_adhar)
                .load(((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation().getAadharUrl())
                .into(img_adhar);
        ((RegistrationActivity) getActivity()).otherMaritialInformation.setAadharUrl(
                ((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation().getAadharUrl());

        if (((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails() != null &&
                !((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().
                        getMaritalStatus().equalsIgnoreCase("Unmarried")) {
            Glide.with(getActivity())
                    .applyDefaultRequestOptions(requestOptions_adhar)
                    .load(((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation().getSupport_doc())
                    .into(imgMaritalStatusCerificate);
            ((RegistrationActivity) getActivity()).otherMaritialInformation.setSupport_doc(
                    ((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation().getSupport_doc());
        }

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
                setValuesInModel();
                //((RegistrationActivity) getActivity()).submitUserRegistrationRequest();
                break;
            case R.id.btn_loadprevious:
                ((RegistrationActivity) getActivity()).loadNextScreen(3);
                break;
            case R.id.img_user_profle:
                uploadImageType = Constants.Image.IMAGE_TYPE_PROFILE;
                isSquare = true;
                onAddImageClick();
                break;
            case R.id.img_education_cert:
                uploadImageType = Constants.Image.IMAGE_TYPE_EDUCATION;
                isSquare = true;
                onAddImageClick();
                break;
            case R.id.img_adhar:
                uploadImageType = Constants.Image.IMAGE_TYPE_ADHARCARD;
                isSquare = true;
                onAddImageClick();
                break;
            case R.id.img_marital_status_certificate:
                uploadImageType = Constants.Image.IMAGE_TYPE_MARITAL_CERTIFICATE;
                isSquare = true;
                onAddImageClick();
                break;
        }
    }


    private void onAddImageClick() {
        if (Permissions.isCameraPermissionGranted(getActivity(), this)) {
            if (isSquare) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(getContext(), this);
            } else {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(), this);
            }
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(getString(R.string.title_choose_picture));
        String[] items = {getString(R.string.label_gallery), getString(R.string.label_camera)};

        dialog.setItems(items, (dialog1, which) -> {
            switch (which) {
                case 0:

                    break;

                case 1:
                    if (isSquare) {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setAspectRatio(1, 1)
                                .start(getContext(), this);
                    } else {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(getContext(), this);
                    }
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
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                if (Util.isConnected(getActivity())) {
                    if (Util.isValidImageSize(new File(resultUri.getPath()))) {
                        //profilePresenter.uploadProfileImage(imageFile, Constants.Image.IMAGE_TYPE_PROFILE);
                        ((RegistrationActivity) getActivity()).presenter.uploadProfileImage
                                (new File(resultUri.getPath()), Constants.Image.IMAGE_TYPE_PROFILE, uploadImageType);

                        if (Constants.Image.IMAGE_TYPE_PROFILE.equalsIgnoreCase(uploadImageType)) {
//                            img_user_profle.setImageURI(finalUri);
                            Glide.with(getActivity())
                                    .applyDefaultRequestOptions(requestOptions)
                                    .load(resultUri)
                                    .into(img_user_profle);
                        } else if (uploadImageType.equalsIgnoreCase(Constants.Image.IMAGE_TYPE_ADHARCARD)) {
//                            img_adhar.setImageURI(finalUri);
                            Glide.with(getActivity())
                                    .applyDefaultRequestOptions(requestOptions_adhar)
                                    .load(resultUri)
                                    .into(img_adhar);
                        } else if (Constants.Image.IMAGE_TYPE_EDUCATION.equalsIgnoreCase(uploadImageType)) {
//                            img_education_cert.setImageURI(finalUri);
                            Glide.with(getActivity())
                                    .applyDefaultRequestOptions(requestOptions_edu)
                                    .load(resultUri)
                                    .into(img_education_cert);
                        } else if (uploadImageType.equalsIgnoreCase(Constants.Image.IMAGE_TYPE_MARITAL_CERTIFICATE)) {
                            Glide.with(getActivity())
                                    .applyDefaultRequestOptions(requestOptions_adhar)
                                    .load(resultUri)
                                    .into(imgMaritalStatusCerificate);
                        }

                    } else {
                        Util.showToast(getActivity(), getString(R.string.msg_big_image));
                    }
                } else {
                    Util.showToast(getActivity(), getResources().getString(R.string.msg_no_network));
                }

            }
        }

//        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == RESULT_OK) {
//            try {
//                String imageFilePath = getImageName();
//                if (imageFilePath == null) return;
//
//                finalUri = Utils.getUri(imageFilePath);
//
//                if (Constants.Image.IMAGE_TYPE_PROFILE.equalsIgnoreCase(uploadImageType)) {
//                    Crop.of(outputUri, finalUri).asSquare().start(getContext(), this);
//                } else if (Constants.Image.IMAGE_TYPE_ADHARCARD.equalsIgnoreCase(uploadImageType)) {
//                    Crop.of(outputUri, finalUri).start(getContext(), this);
//                } else if (Constants.Image.IMAGE_TYPE_EDUCATION.equalsIgnoreCase(uploadImageType)) {
//                    Crop.of(outputUri, finalUri).start(getContext(), this);
//                }
//            } catch (Exception e) {
//                Log.e(TAG, e.getMessage());
//            }
//        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
//            if (data != null) {
//                try {
//                    String imageFilePath = getImageName();
//                    if (imageFilePath == null) return;
//
//                    outputUri = data.getData();
//                    finalUri = Utils.getUri(imageFilePath);
//
//                    if (Constants.Image.IMAGE_TYPE_PROFILE.equalsIgnoreCase(uploadImageType)) {
//                        Crop.of(outputUri, finalUri).asSquare().start(getContext(), this);
//                    } else if (Constants.Image.IMAGE_TYPE_ADHARCARD.equalsIgnoreCase(uploadImageType)) {
//                        Crop.of(outputUri, finalUri).start(getContext(), this);
//                    } else if (Constants.Image.IMAGE_TYPE_EDUCATION.equalsIgnoreCase(uploadImageType)) {
//                        Crop.of(outputUri, finalUri).start(getContext(), this);
//                    }
//
//                } catch (Exception e) {
//                    Log.e(TAG, e.getMessage());
//                }
//            }
//        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
//            try {
//                final File imageFile = new File(Objects.requireNonNull(finalUri.getPath()));
//                if (Utils.isConnected(getActivity())) {
//                    if (Utils.isValidImageSize(imageFile)) {
//                        //profilePresenter.uploadProfileImage(imageFile, Constants.Image.IMAGE_TYPE_PROFILE);
//                        ((RegistrationActivity)getActivity()).presenter.uploadProfileImage(imageFile, Constants.Image.IMAGE_TYPE_PROFILE, uploadImageType);
//                        if (Constants.Image.IMAGE_TYPE_PROFILE.equalsIgnoreCase(uploadImageType)) {
////                            img_user_profle.setImageURI(finalUri);
//                            Glide.with(getActivity())
//                                    .applyDefaultRequestOptions(requestOptions)
//                                    .load(finalUri)
//                                    .into(img_user_profle);
//                        } else if (Constants.Image.IMAGE_TYPE_ADHARCARD.equalsIgnoreCase(uploadImageType)) {
////                            img_adhar.setImageURI(finalUri);
//                            Glide.with(getActivity())
//                                    .applyDefaultRequestOptions(requestOptions)
//                                    .load(finalUri)
//                                    .into(img_adhar);
//                        }
//                        if (Constants.Image.IMAGE_TYPE_EDUCATION.equalsIgnoreCase(uploadImageType)) {
////                            img_education_cert.setImageURI(finalUri);
//                            Glide.with(getActivity())
//                                    .applyDefaultRequestOptions(requestOptions)
//                                    .load(finalUri)
//                                    .into(img_education_cert);
//                        }
//
//                    } else {
//                        Utils.showToast(getActivity(),getString(R.string.msg_big_image));
//                    }
//                } else {
//                    Utils.showToast(getActivity(),getResources().getString(R.string.msg_no_network));
//                }
//
//            } catch (Exception e) {
//                Log.e(TAG, e.getMessage());
//            }
//        }
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
                onAddImageClick();
            }
        }
    }


    //set selected data for request
    private void setValuesInModel() {
        if (isAllInputsValid()) {
            if (((RegistrationActivity) getActivity()).matrimonialProfile != null) {
                if (((RegistrationActivity) getActivity()).otherMaritialInformation != null) {

                    ((RegistrationActivity) getActivity()).otherMaritialInformation.setAboutMe(et_about_me.getText().toString());
                    ((RegistrationActivity) getActivity()).otherMaritialInformation.setExpectation_from_partner(et_partner_expectation.getText().toString());
                    ((RegistrationActivity) getActivity()).otherMaritialInformation.setActivityAchievements(et_achivements.getText().toString());
                    ((RegistrationActivity) getActivity()).otherMaritialInformation.setOtherRemarks(et_other_remark.getText().toString());
                    //((RegistrationActivity)getActivity()).otherMaritialInformation.setProfile_image("");
                    ((RegistrationActivity) getActivity()).matrimonialProfile.setOtherMaritalInformation(((RegistrationActivity) getActivity()).otherMaritialInformation);
                    //((RegistrationActivity) getActivity()).submitUserRegistrationRequest();
                    ((RegistrationActivity) getActivity()).loadNextScreen(5);
                } else {
                    Util.showToast(getActivity(), "null object getPersonal_details()");
                }
            } else {
                Util.showToast(getActivity(), "null object");
            }
        }

    }

    private boolean isAllInputsValid() {
        String msg = "";

        /*if (((RegistrationActivity)getActivity()).otherMaritialInformation.getProfileImage() == null
         *//**//*&& ((RegistrationActivity)getActivity()).otherMaritialInformation.getProfileImage().length() == 0*//**//*) {
            msg = "Please add Profile Image.";//getResources().getString(R.string.msg_enter_proper_date);
        } else*//* if (((RegistrationActivity)getActivity()).otherMaritialInformation.getEducationalUrl()==null
         *//*&&((RegistrationActivity)getActivity()).otherMaritialInformation.getEducationalUrl().length() == 0*//*) {
            msg = "Please add Educational Image";//getResources().getString(R.string.msg_enter_name);
        } else if (((RegistrationActivity)getActivity()).otherMaritialInformation.getAadharUrl()==null
                *//*&&((RegistrationActivity)getActivity()).otherMaritialInformation.getAadharUrl().length() == 0*//*) {
            msg = "Please add Aadhar Image"; //getResources().getString(R.string.msg_enter_name);
        } else */

            if (et_about_me.getText().toString().trim().length() == 0) {
                msg = "Please enter about yourself"; //getResources().getString(R.string.msg_enter_name);
        } else if (et_partner_expectation.getText().toString().trim().length() == 0) {
                msg = "Please enter about expectation from partner"; //getResources().getString(R.string.msg_enter_name);
        } else if (((RegistrationActivity) getActivity()).otherMaritialInformation.getEducationalUrl() == null) {
            msg = "Please add Educational certificate";//getResources().getString(R.string.msg_enter_name);
        }

        if (msg.length() == 0) {
            return true;
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        return false;
    }

    public void reloadFragmentData() {
        if (((RegistrationActivity) getActivity()).matrimonialProfile != null
                && ((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation() != null) {
            et_about_me.setText(((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation().getAboutMe());
        } else {
            et_about_me.setText(((RegistrationActivity) getActivity()).aboutUsStr);
        }

        // for support doc
        if (((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails() != null &&
                !((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().
                        getMaritalStatus().equalsIgnoreCase("Unmarried")) {
            TextView tvMaritalStatusCerificateLabel = fragmentview.findViewById(R.id.tv_marital_status_cerificate_label);
            imgMaritalStatusCerificate = fragmentview.findViewById(R.id.img_marital_status_certificate);
            tvMaritalStatusCerificateLabel.setVisibility(View.VISIBLE);
            imgMaritalStatusCerificate.setVisibility(View.VISIBLE);
            imgMaritalStatusCerificate.setOnClickListener(this);
            if (((RegistrationActivity) getActivity()).matrimonialProfile.getPersonalDetails().
                    getMaritalStatus().equalsIgnoreCase("Divorcee")) {
                tvMaritalStatusCerificateLabel.setText("Legal seperation certificate");
            } else {
                tvMaritalStatusCerificateLabel.setText("Partner's death certificate");
            }
        } else {
            TextView tvMaritalStatusCerificateLabel = fragmentview.findViewById(R.id.tv_marital_status_cerificate_label);
            imgMaritalStatusCerificate = fragmentview.findViewById(R.id.img_marital_status_certificate);
            tvMaritalStatusCerificateLabel.setVisibility(View.GONE);
            imgMaritalStatusCerificate.setVisibility(View.GONE);
        }
    }
}
