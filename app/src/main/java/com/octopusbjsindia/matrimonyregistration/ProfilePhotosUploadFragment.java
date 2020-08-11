package com.octopusbjsindia.matrimonyregistration;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.Matrimony.MatrimonyMasterRequestModel;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.ShowImagesPageRecyclerAdapter;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class ProfilePhotosUploadFragment extends Fragment implements View.OnClickListener, ShowImagesPageRecyclerAdapter.OnRequestItemClicked, ShowImagesPageRecyclerAdapter.OnRequestItemRemoval {
    private static final String TAG = ProfilePhotosUploadFragment.class.getName();
    private final String PROFILE_DATA = "profileData";

    public ShowImagesPageRecyclerAdapter mAdapter;
    private int currentSelectedItem = 0;
    private RecyclerView rvBaches;
    private TextView txtNoData;
    //private Profile profile;
    private ImageView toolbar_back_action, iv_toolbar_action2;
    private TextView toolbar_title;
    private String uploadImageType = "";
    private Uri outputUri;
    private Uri finalUri;
    private ArrayList<String> datalist = new ArrayList<>();
    private RelativeLayout pbLayout;

    private int sizeBefore = 0;
    private View fragmentview;
    private Button btn_load_next, btn_loadprevious;

    public static ProfilePhotosUploadFragment newInstance() {
        return new ProfilePhotosUploadFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentview = inflater.inflate(R.layout.fragment_profile_phto_upload, container, false);
        initView();


        return fragmentview;
    }


    private void initView() {

        /*Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            profile = (Profile) bundle.getSerializable(PROFILE_DATA);
        }*/
        btn_load_next = fragmentview.findViewById(R.id.btn_loadnext);
        btn_loadprevious = fragmentview.findViewById(R.id.btn_loadprevious);
        pbLayout = fragmentview.findViewById(R.id.progress_bar);
        txtNoData = fragmentview.findViewById(R.id.txt_no_data);
        txtNoData.setText("No Batches available yet.");
        txtNoData.setVisibility(View.GONE);
        rvBaches = fragmentview.findViewById(R.id.rvLandingPageView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        rvBaches.setLayoutManager(layoutManager);

        mAdapter = new ShowImagesPageRecyclerAdapter(getActivity(), datalist, this::onItemClicked, this::onItemRemoved);
        rvBaches.setAdapter(mAdapter);


        /*if (profile != null)
        {
            datalist.addAll((ArrayList<String>) profile.getMatrimonialProfile().getOtherMaritalInformation().getProfileImage());
            sizeBefore = datalist.size();
            mAdapter.notifyDataSetChanged();
        }*/
        iv_toolbar_action2 = fragmentview.findViewById(R.id.iv_toolbar_action2);
        iv_toolbar_action2.setVisibility(View.VISIBLE);
        iv_toolbar_action2.setImageResource(R.drawable.ic_save);
        toolbar_back_action = fragmentview.findViewById(R.id.iv_toobar_back);
        toolbar_title = fragmentview.findViewById(R.id.tv_toolbar_title);
        toolbar_title.setText("Profile Images");
        toolbar_back_action.setOnClickListener(this);
        iv_toolbar_action2.setOnClickListener(this);
        btn_load_next.setOnClickListener(this);
        btn_loadprevious.setOnClickListener(this);
        if (((RegistrationActivity) getActivity()).matrimonialProfile != null
                && ((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation() != null) {
            if (((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation().getProfileImage() != null) {
                setData();
            }
        }
        fragmentview.findViewById(R.id.iv_toobar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((RegistrationActivity) getActivity()).onBackPressed();
            }
        });
    }

    private void setData() {
        datalist.addAll(((RegistrationActivity) getActivity()).matrimonialProfile.getOtherMaritalInformation().getProfileImage());
        mAdapter.notifyDataSetChanged();
    }
/*
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_toobar_back:
                onBackPressed();
                break;
            case R.id.iv_toolbar_action2:
                if(Utils.isConnected(this)){
                    profile.getMatrimonialProfile().setUserId(Utils.getLoginObjectFromPref().getUserDetails().getId());
                    profile.getMatrimonialProfile().setFirebaseId(Utils.getStringPref(Constants.Pref.TOKEN));
                    profile.getMatrimonialProfile().getOtherMaritalInformation().setProfileImage(datalist);
                    presenter.UserRegistrationRequests(profile.getMatrimonialProfile());
                } else {
                    Utils.showToast(this,getResources().getString(R.string.msg_no_network));
                }
                break;

        }
    }*/

    @Override
    public void onItemClicked(int pos) {
        if (datalist.size() < 1) {
            if (pos == 0) {
                currentSelectedItem = pos;
                uploadImageType = "Profile";
                if (currentSelectedItem == 0) {
                    onAddImageClick(0);
                } else {
                    onAddImageClick(pos);
                }
            } else {
                Util.showToast(getActivity(), "Upload first profile Image.");
            }
        } else {
            currentSelectedItem = pos;
            uploadImageType = "Profile";
            if (currentSelectedItem == 0) {
                onAddImageClick(0);
            } else {
                onAddImageClick(pos);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                //((RegistrationActivity) getActivity()).presenter.uploadProfileImage(new File(resultUri.getPath()), Constants.Image.IMAGE_TYPE_PROFILE, uploadImageType);

                if (Util.isConnected(getActivity())) {
                    if (Util.isValidImageSize(new File(resultUri.getPath()))) {
                        //profilePresenter.uploadProfileImage(imageFile, Constants.Image.IMAGE_TYPE_PROFILE);
                        if (Util.isConnected(getActivity())) {
                            //presenter.uploadProfileImage(new File(resultUri.getPath()), Constants.Image.IMAGE_TYPE_PROFILE, uploadImageType);
                            ((RegistrationActivity) getActivity()).presenter.uploadProfileImage(new File(resultUri.getPath()), Constants.Image.IMAGE_TYPE_PROFILE, uploadImageType);
                        } else {
                            Util.showToast(getActivity(), getResources().getString(R.string.msg_no_network));
                        }
                        if (Constants.Image.IMAGE_TYPE_PROFILE.equalsIgnoreCase(uploadImageType)) {
                            //img_user_profle.setImageURI(finalUri);
                        } else if (Constants.Image.IMAGE_TYPE_ADHARCARD.equalsIgnoreCase(uploadImageType)) {
                            //img_adhar.setImageURI(finalUri);
                        }
                        if (Constants.Image.IMAGE_TYPE_EDUCATION.equalsIgnoreCase(uploadImageType)) {
                            //img_education_cert.setImageURI(finalUri);
                        }

                    } else {
                        Util.showToast(getActivity(), getString(R.string.msg_big_image));
                    }
                } else {
                    Util.showToast(getActivity(), getResources().getString(R.string.msg_no_network));
                }

            }
        }


    }

    /*@Override
    public void onBackPressed() {
        if(sizeBefore==datalist.size()){
            super.onBackPressed();
        } else {
            showDialog(getActivity(), "Alert", "Are you sure, want to discard the changes",
                    "Yes", "No");
        }
    }*/

    private void onAddImageClick(int position) {
        if (Permissions.isCameraPermissionGranted(getActivity(), this)) {
            if (position == 0) {

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setFixAspectRatio(true)
                        .setScaleType(CropImageView.ScaleType.CENTER_CROP)
                        .start(getContext(), this);

            } else if (position == 1) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;
                int width = displayMetrics.widthPixels;
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setScaleType(CropImageView.ScaleType.CENTER_CROP)
                        .setFixAspectRatio(true)
                        .setRequestedSize(width, width)
                        .setMinCropWindowSize(width, width)
                        .start(getContext(), this);
            } else {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .setFixAspectRatio(true)
                        .setScaleType(CropImageView.ScaleType.CENTER_CROP)
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
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .start(getActivity());
                    break;

                case 1:
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .start(getActivity());
                    break;
            }
        });

        dialog.show();
    }


    public void showProgressBar() {
        if (pbLayout != null && pbLayout.getVisibility() == View.GONE) {
            pbLayout.setVisibility(View.VISIBLE);
        }
    }

    public void imageUploadedSuccessfully(String url, String formName) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentSelectedItem == 0) {
                    datalist.add(currentSelectedItem, url);
                } else if (datalist.size() < 5) {
                    datalist.add(url);
                }

                mAdapter.notifyItemChanged(currentSelectedItem);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public void hideProgressBar() {
        if (pbLayout != null && pbLayout.getVisibility() == View.VISIBLE) {
            pbLayout.setVisibility(View.GONE);
        }
    }

    public void showMessage(String requestID, String message, int i) {
    }

    public void saveMasterData(List<MatrimonyMasterRequestModel.DataList.Master_data> master_data) {
    }

    public void profileCreatedSuccessfully(String response) {

    }

    @Override
    public void onItemRemoved(int pos) {
        if (datalist.size() > 1) {
            if (pos == 0) {
                Util.showToast(getActivity(), "First profile Image is compulsory");
            } else {
                datalist.remove(pos);
                mAdapter.notifyDataSetChanged();
                ((RegistrationActivity) getActivity()).otherMaritialInformation.setProfileImage(datalist);
                ((RegistrationActivity) getActivity()).matrimonialProfile.setOtherMaritalInformation(((RegistrationActivity) getActivity()).otherMaritialInformation);
            }
        } else {
            Util.showToast(getActivity(), "At least one profile Image is compulsory");
        }
    }

    public void showDialog(Context context, String dialogTitle, String message, String btn1String, String
            btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(context));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_alert_dialog);

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
                //onBackPressed();
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                // Close dialog
                dialog.dismiss();
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_loadnext:
                //setValuesInModel();
                if (isAllInputsValid()) {
                    ((RegistrationActivity) getActivity()).submitUserRegistrationRequest();
                }

                break;
            case R.id.btn_loadprevious:
                ((RegistrationActivity) getActivity()).loadNextScreen(4);
                break;
                /*case R.id.img_user_profle:
                    uploadImageType = Constants.Image.IMAGE_TYPE_PROFILE;
                    isSquare = true;
                    onAddImageClick();
                    break;
                case R.id.img_education_cert:
                    uploadImageType = Constants.Image.IMAGE_TYPE_EDUCATION;
                    isSquare = false;
                    onAddImageClick();
                    break;
                case R.id.img_adhar:
                    uploadImageType = Constants.Image.IMAGE_TYPE_ADHARCARD;
                    isSquare = false;
                    onAddImageClick();
                    break;*/
        }
    }

    public void reloadFragmentData(String url) {
        /*datalist.clear();
        datalist.addAll((ArrayList<String>) ((RegistrationActivity)getActivity()).otherMaritialInformation.getProfileImage());
        sizeBefore = datalist.size();
        mAdapter.notifyDataSetChanged();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });*/

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (datalist.size() < 5) {
                    if (currentSelectedItem < datalist.size()) {
                        datalist.set(currentSelectedItem, url);
                    } else {
                        datalist.add(url);
                    }
                } else if (currentSelectedItem == 0) {
                    datalist.set(currentSelectedItem, url);
                } else {
                    datalist.set(currentSelectedItem, url);
                }

                mAdapter.notifyItemChanged(currentSelectedItem);
                mAdapter.notifyDataSetChanged();
            }
        });
        /*if (((RegistrationActivity) getActivity()).otherMaritialInformation.getProfileImage() != null && ((RegistrationActivity) getActivity()).otherMaritialInformation.getProfileImage().size() > 0) {
            List<String> temp = ((RegistrationActivity) getActivity()).otherMaritialInformation.getProfileImage();
            temp.add(url);
            ((RegistrationActivity) getActivity()).otherMaritialInformation.setProfileImage(temp);


        } else {
            List<String> temp = new ArrayList<String>();
            temp.add(0, url);
            ((RegistrationActivity) getActivity()).otherMaritialInformation.setProfileImage(temp);
        }*/
        ((RegistrationActivity) getActivity()).otherMaritialInformation.setProfileImage(datalist);
        ((RegistrationActivity) getActivity()).matrimonialProfile.setOtherMaritalInformation(((RegistrationActivity) getActivity()).otherMaritialInformation);
    }

    private boolean isAllInputsValid() {
        String msg = "";

        if (((RegistrationActivity) getActivity()).otherMaritialInformation.getProfileImage() == null) {
            //&& ((RegistrationActivity)getActivity()).otherMaritialInformation.getProfileImage().size() == 0) {
            msg = "Please add Profile Image.";//getResources().getString(R.string.msg_enter_proper_date);
        }
        /* else if (((RegistrationActivity)getActivity()).otherMaritialInformation.getEducationalUrl()==null
         &&((RegistrationActivity)getActivity()).otherMaritialInformation.getEducationalUrl().length() == 0) {
            msg = "Please add Educational Image";//getResources().getString(R.string.msg_enter_name);
        } else if (((RegistrationActivity)getActivity()).otherMaritialInformation.getAadharUrl()==null
                &&((RegistrationActivity)getActivity()).otherMaritialInformation.getAadharUrl().length() == 0) {
            msg = "Please add Aadhar Image"; //getResources().getString(R.string.msg_enter_name);
        } else*/


        if (msg.length() == 0) {
            return true;
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        return false;
    }
}
