package com.platform.view.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.platform.Platform;
import com.platform.R;
import com.platform.listeners.ProfileTaskListener;
import com.platform.models.login.LoginInfo;
import com.platform.models.profile.Jurisdiction;
import com.platform.models.profile.JurisdictionType;
import com.platform.models.profile.Location;
import com.platform.models.profile.Organization;
import com.platform.models.profile.OrganizationProject;
import com.platform.models.profile.OrganizationRole;
import com.platform.models.profile.State;
import com.platform.models.profile.UserLocation;
import com.platform.models.user.UserInfo;
import com.platform.presenter.ProfileActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Permissions;
import com.platform.utility.Util;
import com.platform.widgets.MultiSelectSpinner;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("CanBeFinal")
public class ProfileActivity extends BaseActivity implements ProfileTaskListener,
        View.OnClickListener, AdapterView.OnItemSelectedListener,
        MultiSelectSpinner.MultiSpinnerListener {

    public static final String FILE_SEP = "/";
    public static final String IMAGE_PREFIX = "picture_";
    public static final String IMAGE_SUFFIX = ".jpg";
    public static final String IMAGE_STORAGE_DIRECTORY = "/MV/Image/profile";
    public static final String IMAGE_TYPE_PROFILE = "profile";
    private EditText etUserFirstName;
    private EditText etUserMiddleName;
    private EditText etUserLastName;
    private EditText etUserBirthDate;
    private EditText etUserMobileNumber;
    private EditText etUserEmailId;

    private Spinner spOrganization;
    private Spinner spState;
    private Spinner spRole;
    private Spinner spStructure;

    private MultiSelectSpinner spProject;
    private MultiSelectSpinner spDistrict;
    private MultiSelectSpinner spTaluka;
    private MultiSelectSpinner spCluster;
    private MultiSelectSpinner spVillage;

    private ImageView imgUserProfilePic;
    private ImageView backButton;
    private Button btnProfileSubmit;

    private String userGender = "Male";

    private List<Organization> organizations = new ArrayList<>();
    private List<OrganizationProject> projects = new ArrayList<>();
    private List<OrganizationRole> roles = new ArrayList<>();

    private List<JurisdictionType> states = new ArrayList<>();
    private List<JurisdictionType> districts = new ArrayList<>();
    private List<JurisdictionType> talukas = new ArrayList<>();
    private List<JurisdictionType> clusters = new ArrayList<>();
    private List<JurisdictionType> villages = new ArrayList<>();

    private ArrayList<String> selectedProjects = new ArrayList<>();
    private ArrayList<String> selectedRoles = new ArrayList<>();

    private ArrayList<JurisdictionType> selectedStates = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedDistricts = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedTalukas = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedClusters = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedVillages = new ArrayList<>();

    private Uri outputUri;
    private Uri finalUri;
    private ProfileActivityPresenter profilePresenter;

    //    private String selectedState;
    private OrganizationRole selectedRole;
    private Organization selectedOrg;

    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private final String TAG = ProfileActivity.class.getName();
    private File mImageFile;
    private boolean mImageUploaded;
    private String mUploadedImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePresenter = new ProfileActivityPresenter(this);

        if (getIntent().getStringExtra(Constants.Login.ACTION) == null ||
                !getIntent().getStringExtra(Constants.Login.ACTION)
                        .equalsIgnoreCase(Constants.Login.ACTION_EDIT)) {

            profilePresenter.getOrganizations();
        }

        initViews();
    }

    private void initViews() {
        setActionbar(getString(R.string.registration_title));

        progressBarLayout = findViewById(R.id.profile_act_progress_bar);
        progressBar = findViewById(R.id.pb_profile_act);

        backButton = findViewById(R.id.toolbar_back_action);
        etUserFirstName = findViewById(R.id.et_user_first_name);
        etUserMiddleName = findViewById(R.id.et_user_middle_name);
        etUserLastName = findViewById(R.id.et_user_last_name);
        etUserBirthDate = findViewById(R.id.et_user_birth_date);
        etUserMobileNumber = findViewById(R.id.et_user_mobile_number);
        etUserEmailId = findViewById(R.id.et_user_email_id);

        RadioGroup radioGroup = findViewById(R.id.user_gender_group);
        radioGroup.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
            switch (checkedId) {
                case R.id.gender_male:
                    userGender = getResources().getString(R.string.male);
                    break;

                case R.id.gender_female:
                    userGender = getResources().getString(R.string.female);
                    break;

                case R.id.gender_other:
                    userGender = getResources().getString(R.string.other);
                    break;
            }
        });

        spOrganization = findViewById(R.id.sp_user_organization);

        spProject = findViewById(R.id.sp_project);
        spProject.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_PROJECT);

        spRole = findViewById(R.id.sp_role);
        spState = findViewById(R.id.sp_user_state);

        spDistrict = findViewById(R.id.sp_district);
        spDistrict.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_DISTRICT);

        spTaluka = findViewById(R.id.sp_taluka);
        spTaluka.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_TALUKA);

        spCluster = findViewById(R.id.sp_cluster);
        spCluster.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_CLUSTER);

        spStructure = findViewById(R.id.sp_user_structure);

        spVillage = findViewById(R.id.sp_village);
        spVillage.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_VILLAGE);

        imgUserProfilePic = findViewById(R.id.user_profile_pic);
        btnProfileSubmit = findViewById(R.id.btn_profile_submit);

        hideJurisdictionLevel();
        setListeners();

        if (Platform.getInstance().getAppMode().equals(Constants.App.BJS_MODE)) {
            findViewById(R.id.user_geo_location_view).setVisibility(View.GONE);
            findViewById(R.id.input_user_address).setVisibility(View.GONE);
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            LoginInfo loginInfo = bundle.getParcelable(Constants.Login.LOGIN_OTP_VERIFY_DATA);
            if (loginInfo != null) {
                etUserMobileNumber.setText(loginInfo.getMobileNumber());
            } else {
                if (getIntent().getStringExtra(Constants.Login.ACTION)
                        .equalsIgnoreCase(Constants.Login.ACTION_EDIT)) {

                    setActionbar(getString(R.string.update_profile));

                    UserInfo userInfo = Util.getUserObjectFromPref();
                    String userName = userInfo.getUserName().trim();
                    if (userName.split(" ").length == 3) {
                        etUserFirstName.setText(userName.split(" ")[0]);
                        etUserMiddleName.setText(userName.split(" ")[1]);
                        etUserLastName.setText(userName.split(" ")[2]);
                    } else if (userName.split(" ").length == 2) {
                        etUserFirstName.setText(userName.split(" ")[0]);
                        etUserLastName.setText(userName.split(" ")[1]);
                    } else if (userName.split(" ").length == 1) {
                        etUserFirstName.setText(userName);
                    }

                    etUserBirthDate.setText(userInfo.getUserBirthDate());
                    etUserMobileNumber.setText(userInfo.getUserMobileNumber());
                    etUserEmailId.setText(userInfo.getUserEmailId());

                    if (!TextUtils.isEmpty(userInfo.getUserGender())) {
                        if (userInfo.getUserGender().equalsIgnoreCase(getResources().getString(R.string.male))) {
                            radioGroup.check(R.id.gender_male);
                            userGender = getResources().getString(R.string.male);
                        } else if (userInfo.getUserGender().equalsIgnoreCase(getResources().getString(R.string.female))) {
                            radioGroup.check(R.id.gender_female);
                            userGender = getResources().getString(R.string.female);
                        } else if (userInfo.getUserGender().equalsIgnoreCase(getResources().getString(R.string.other))) {
                            radioGroup.check(R.id.gender_other);
                            userGender = getResources().getString(R.string.other);
                        }
                    }

                    int id = 0;
                    List<Organization> orgData = Util.getUserOrgFromPref().getData();
                    showOrganizations(orgData);
                    for (int i = 0; i < orgData.size(); i++) {
                        if (userInfo.getOrgId().equals(orgData.get(i).getId())) {
                            id = i;
                        }
                    }
                    spOrganization.setSelection(id);

                    id = 0;
                    List<OrganizationProject> projectData = Util.getUserProjectsFromPref().getData();
                    showOrganizationProjects(projectData);
                    for (int i = 0; i < projectData.size(); i++) {
                        if (userInfo.getProjectIds().contains(projectData.get(i).getId())) {
                            id = i;
                        }
                    }
                    spProject.setSelection(id);

                    id = 0;
                    List<OrganizationRole> roleData = Util.getUserRoleFromPref().getData();
                    showOrganizationRoles(roleData);
                    for (int i = 0; i < roleData.size(); i++) {
                        if (userInfo.getRoleIds().equals(roleData.get(i).getId())) {
                            id = i;
                        }
                    }
                    spRole.setSelection(id);
                }
            }
        } else {
            etUserMobileNumber.setText(Util.getUserMobileFromPref());
        }

        etUserMobileNumber.setEnabled(false);
        etUserMobileNumber.setFocusable(false);
        etUserMobileNumber.setClickable(false);
    }

    private void setListeners() {
        backButton.setOnClickListener(this);
        etUserBirthDate.setOnClickListener(this);

        spOrganization.setOnItemSelectedListener(this);
        spState.setOnItemSelectedListener(this);
        spRole.setOnItemSelectedListener(this);
        spStructure.setOnItemSelectedListener(this);

        imgUserProfilePic.setOnClickListener(this);
        btnProfileSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_action:
                onBackPressed();
                break;

            case R.id.et_user_birth_date:
                showDateDialog(ProfileActivity.this, findViewById(R.id.et_user_birth_date));
                break;

            case R.id.user_profile_pic:
                onAddImageClick();
                break;

            case R.id.btn_profile_submit:
                submitProfileDetails();
                break;
        }
    }

    private void setActionbar(String title) {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
    }

    private void showDateDialog(Context context, final EditText editText) {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {
            String date = year + "-" + Util.getTwoDigit(monthOfYear + 1) + "-" + Util.getTwoDigit(dayOfMonth);
            editText.setText(date);
        }, mYear, mMonth, mDay);

        dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dateDialog.show();
    }

    private void onAddImageClick() {
        if (Permissions.isCameraPermissionGranted(this, this)) {
            showPictureDialog();
        }
    }

    private void submitProfileDetails() {
        Util.hideKeyboard(btnProfileSubmit);

        if (isAllInputsValid()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserFirstName(String.valueOf(etUserFirstName.getText()).trim());
            userInfo.setUserMiddleName(String.valueOf(etUserMiddleName.getText()).trim());
            userInfo.setUserLastName(String.valueOf(etUserLastName.getText()).trim());
            userInfo.setUserBirthDate(String.valueOf(etUserBirthDate.getText()).trim());
            userInfo.setUserMobileNumber(String.valueOf(etUserMobileNumber.getText()).trim());
            userInfo.setUserEmailId(String.valueOf(etUserEmailId.getText()).trim());
            userInfo.setUserGender(userGender);

            StringBuilder userName = new StringBuilder();
            if (!TextUtils.isEmpty(String.valueOf(etUserFirstName.getText()).trim())) {
                userName.append(String.valueOf(etUserFirstName.getText()).trim());
            }

            if (!TextUtils.isEmpty(String.valueOf(etUserMiddleName.getText()).trim())) {
                userName.append(String.format(" %s", String.valueOf(etUserMiddleName.getText()).trim()));
            }

            if (!TextUtils.isEmpty(String.valueOf(etUserLastName.getText()).trim())) {
                userName.append(String.format(" %s", String.valueOf(etUserLastName.getText()).trim()));
            }
            userInfo.setUserName(userName.toString());

            userInfo.setOrgId(selectedOrg.getId());
            userInfo.setType(selectedOrg.getType());
            userInfo.setProjectIds(selectedProjects);
            userInfo.setRoleIds(selectedRole.getId());

            if (mImageUploaded && !TextUtils.isEmpty(mUploadedImageUrl))
                userInfo.setProfilePic(mUploadedImageUrl);

            UserLocation userLocation = new UserLocation();
            ArrayList<String> s = new ArrayList<>();
            for (JurisdictionType state : selectedStates) {
                s.add(state.getId());
                userLocation.setStateId(s);
            }

            s = new ArrayList<>();
            for (JurisdictionType district : selectedDistricts) {
                s.add(district.getId());
                userLocation.setDistrictIds(s);
            }

            s = new ArrayList<>();
            for (JurisdictionType taluka : selectedTalukas) {
                s.add(taluka.getId());
                userLocation.setTalukaIds(s);
            }

            s = new ArrayList<>();
            for (JurisdictionType cluster : selectedClusters) {
                s.add(cluster.getId());
                userLocation.setClusterIds(s);
            }

            s = new ArrayList<>();
            for (JurisdictionType village : selectedVillages) {
                s.add(village.getId());
                userLocation.setVillageIds(s);
            }

            userInfo.setUserLocation(userLocation);
            Util.saveUserLocationInPref(userLocation);

            profilePresenter.submitProfile(userInfo);
        }
    }

    private boolean isAllInputsValid() {
        String msg = "";

        if (etUserFirstName.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_name);
        } else if (etUserMobileNumber.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_mobile_number);
        } else if (etUserMobileNumber.getText().toString().trim().length() != 10) {
            msg = getResources().getString(R.string.msg_enter_valid_mobile_no);
        } else if (etUserEmailId.getText().toString().trim().length() != 0 &&
                !Patterns.EMAIL_ADDRESS.matcher(etUserEmailId.getText().toString().trim()).matches()) {
            msg = getResources().getString(R.string.msg_enter_valid_email_id);
        } else if (TextUtils.isEmpty(userGender)) {
            msg = getString(R.string.msg_select_gender);
        } else if (selectedOrg == null || TextUtils.isEmpty(selectedOrg.getId())) {
            msg = getString(R.string.msg_select_gender);
        } else if (selectedProjects == null || selectedProjects.size() == 0) {
            msg = getString(R.string.msg_select_project);
        } else if (selectedRoles == null || selectedRoles.size() == 0) {
            msg = getString(R.string.msg_select_role);
        } else if (selectedStates == null || selectedStates.size() == 0) {
            msg = getString(R.string.msg_select_state);
        } else if ((spDistrict.getVisibility() == View.VISIBLE) && (selectedDistricts == null || selectedDistricts.size() == 0)) {
            msg = getString(R.string.msg_select_district);
        } else if ((spTaluka.getVisibility() == View.VISIBLE) && (selectedTalukas == null || selectedTalukas.size() == 0)) {
            msg = getString(R.string.msg_select_taluka);
        } else if ((spCluster.getVisibility() == View.VISIBLE) && (selectedClusters == null || selectedClusters.size() == 0)) {
            msg = getString(R.string.msg_select_cluster);
        } else if ((spVillage.getVisibility() == View.VISIBLE) && (selectedVillages == null || selectedVillages.size() == 0)) {
            msg = getString(R.string.msg_select_village);
        }

        if (TextUtils.isEmpty(msg)) {
            return true;
        }

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        return false;
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
                    + "/MV/Image/picture.jpg";

            File imageFile = new File(imageFilePath);
            outputUri = FileProvider.getUriForFile(this, getPackageName()
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
            try {
                String imageFilePath = getImageName();
                if (imageFilePath == null) return;

                finalUri = Util.getUri(imageFilePath);
                // FIXME: 22-02-2019 Replace Crop lib with Android STD lib
                Crop.of(outputUri, finalUri).asSquare().start(this);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    String imageFilePath = getImageName();
                    if (imageFilePath == null) return;

                    outputUri = data.getData();
                    finalUri = Util.getUri(imageFilePath);
                    // FIXME: 22-02-2019 Replace Crop lib with Android STD lib
                    Crop.of(outputUri, finalUri).asSquare().start(this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                imgUserProfilePic.setImageURI(finalUri);
                mImageFile = new File(Objects.requireNonNull(finalUri.getPath()));

                if (Util.isConnected(this)) {
                    profilePresenter.uploadProfileImage(mImageFile, IMAGE_TYPE_PROFILE);
                } else {
                    Util.showToast("Internet is not available!", this);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

//            Glide.with(this)
//                    .load(finalUri)
//                    .listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable final GlideException e, final Object model, final Target<Drawable> target, final boolean isFirstResource) {
//                            Util.showToast("Filed to load selected image!", ProfileActivity.this);
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(final Drawable drawable, final Object model, final Target<Drawable> target, final DataSource dataSource, final boolean isFirstResource) {
//                            imgUserProfilePic.setImageURI(finalUri);
//
//                            try {
//                                mImageFile = new File(Objects.requireNonNull(finalUri.getPath()));
//                                profilePresenter.uploadProfileImage(mImageFile);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                            return uploadImageHere(drawable);
//
//                        }
//                    })
//                    .into(imgUserProfilePic);
        }
    }

    private String getImageName() {
        long time = new Date().getTime();
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + IMAGE_STORAGE_DIRECTORY);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                Log.e(TAG, "Failed to create directory!");
                return null;
            }
        }
        return IMAGE_STORAGE_DIRECTORY + FILE_SEP + IMAGE_PREFIX + time + IMAGE_SUFFIX;
    }

    File saveBitmapToFile(File dir, String fileName, Bitmap bm) {

        File imageFile = new File(dir, fileName);
        if (!imageFile.exists()) {
            try {
                if (!imageFile.createNewFile()) {
                    Log.e("app", "Failed to create new file");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            return imageFile;
        } catch (IOException e) {
            Log.e("app", e.getMessage());
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    private void bitmapToFile(final Bitmap bitmap) {
        FileOutputStream fos;
        try {
            mImageFile = File.createTempFile("profile_image", ".jpg", getFilesDir());
            fos = new FileOutputStream(mImageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onImageUploaded(String uploadedImageUrl) {
        mImageUploaded = true;
        mUploadedImageUrl = uploadedImageUrl;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showPictureDialog();
                }
                break;
        }
    }

    private void hideJurisdictionLevel() {
        spState.setVisibility(View.GONE);
        findViewById(R.id.txt_state).setVisibility(View.GONE);

        spDistrict.setVisibility(View.GONE);
        findViewById(R.id.txt_district).setVisibility(View.GONE);

        spTaluka.setVisibility(View.GONE);
        findViewById(R.id.txt_taluka).setVisibility(View.GONE);

        spCluster.setVisibility(View.GONE);
        findViewById(R.id.txt_cluster).setVisibility(View.GONE);

        spVillage.setVisibility(View.GONE);
        findViewById(R.id.txt_village).setVisibility(View.GONE);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.sp_user_organization:
                if (organizations != null && !organizations.isEmpty() && organizations.get(i) != null
                        && !TextUtils.isEmpty(organizations.get(i).getId())) {
                    this.selectedOrg = organizations.get(i);
                    profilePresenter.getOrganizationProjects(organizations.get(i).getId());
                    profilePresenter.getOrganizationRoles(organizations.get(i).getId());
                }
                break;

            case R.id.sp_role:
                if (roles != null && !roles.isEmpty() && roles.get(i) != null) {

                    selectedRoles.clear();
                    selectedRole = roles.get(i);
                    selectedRoles.add(selectedRole.getDisplayName());

                    List<Jurisdiction> jurisdictions = selectedRole.getProject().getJurisdictions();
                    if (jurisdictions != null && jurisdictions.size() > 0) {
                        hideJurisdictionLevel();
                        for (Jurisdiction j : jurisdictions) {
                            switch (j.getLevelName()) {
                                case Constants.JurisdictionLevelName.STATE_LEVEL:
                                    spState.setVisibility(View.VISIBLE);
                                    findViewById(R.id.txt_state).setVisibility(View.VISIBLE);
                                    profilePresenter.getJurisdictionLevelData(selectedOrg.getId(),
                                            selectedRole.getProject().getJurisdictionTypeId(), j.getLevelName());
                                    break;

                                case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                                    spDistrict.setVisibility(View.VISIBLE);
                                    findViewById(R.id.txt_district).setVisibility(View.VISIBLE);
                                    break;

                                case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                                    spTaluka.setVisibility(View.VISIBLE);
                                    findViewById(R.id.txt_taluka).setVisibility(View.VISIBLE);
                                    break;

                                case Constants.JurisdictionLevelName.VILLAGE_LEVEL:
                                    spVillage.setVisibility(View.VISIBLE);
                                    findViewById(R.id.txt_village).setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
                    }
                }
                break;

            case R.id.sp_user_state:
                if (states != null && !states.isEmpty() && states.get(i) != null) {

                    selectedStates.clear();
                    selectedStates.add(states.get(i));

//                    Util.saveUserLocationJurisdictionLevel(Constants.JurisdictionLevelName.STATE_LEVEL);

                    if (spDistrict.getVisibility() == View.VISIBLE) {
                        profilePresenter.getJurisdictionLevelData(selectedOrg.getId(),
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                    }
                }
                break;

            case R.id.sp_user_structure:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void showProgressBar() {
        runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public <T> void showNextScreen(T data) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
        List<String> org = new ArrayList<>();
        for (int i = 0; i < organizations.size(); i++) {
            org.add(organizations.get(i).getOrgName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ProfileActivity.this,
                android.R.layout.simple_spinner_item, org);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrganization.setAdapter(adapter);

    }

    @Override
    public void showOrganizationProjects(List<OrganizationProject> organizationProjects) {
        if (organizationProjects != null && !organizationProjects.isEmpty()) {
            List<String> projects = new ArrayList<>();
            for (OrganizationProject organizationProject : organizationProjects) {
                projects.add(organizationProject.getOrgProjectName());
            }
            spProject.setItems(projects, getString(R.string.project), this);
            this.projects.clear();
            this.projects.addAll(organizationProjects);
        }
    }

    @Override
    public void showOrganizationRoles(List<OrganizationRole> organizationRoles) {
        if (organizationRoles != null && !organizationRoles.isEmpty()) {
            List<String> roles = new ArrayList<>();
            for (OrganizationRole organizationRole : organizationRoles) {
                roles.add(organizationRole.getDisplayName());
            }

            this.roles.clear();
            this.roles.addAll(organizationRoles);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(ProfileActivity.this,
                    android.R.layout.simple_spinner_item, roles);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRole.setAdapter(adapter);
        }
    }

    @Override
    public void showStates(List<State> states) {
        //this.states = states;
        List<String> stateNames = new ArrayList<>();
        for (int i = 0; i < states.size(); i++) {
            stateNames.add(states.get(i).getOrgName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ProfileActivity.this,
                android.R.layout.simple_spinner_item, stateNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spState.setAdapter(adapter);
    }

    @Override
    public void showJurisdictionLevel(List<Location> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.STATE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.states.clear();
                    List<String> stateNames = new ArrayList<>();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        Location location = jurisdictionLevels.get(i);
                        stateNames.add(location.getState().getName());
                        this.states.add(location.getState());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ProfileActivity.this,
                            android.R.layout.simple_spinner_item, stateNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spState.setAdapter(adapter);
                }
                break;

            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.districts.clear();
                    List<String> districts = new ArrayList<>();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        Location location = jurisdictionLevels.get(i);
                        for (JurisdictionType state : selectedStates) {
                            if (state.getName().equalsIgnoreCase(location.getState().getName())) {
                                districts.add(location.getDistrict().getName());
                                this.districts.add(location.getDistrict());
                            }
                        }
                    }

                    spDistrict.setItems(districts, getString(R.string.district), this);
                }
                break;

            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.talukas.clear();
                    List<String> talukas = new ArrayList<>();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        Location location = jurisdictionLevels.get(i);
                        for (JurisdictionType state : selectedStates) {
                            if (state.getName().equalsIgnoreCase(location.getState().getName())) {
                                for (JurisdictionType district : selectedDistricts) {
                                    if (district.getName().equalsIgnoreCase(location.getDistrict().getName())) {
                                        talukas.add(location.getTaluka().getName());
                                        this.talukas.add(location.getTaluka());
                                    }
                                }
                            }
                        }
                    }

                    spTaluka.setItems(talukas, getString(R.string.taluka), this);
                }
                break;

            case Constants.JurisdictionLevelName.VILLAGE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.villages.clear();
                    List<String> villages = new ArrayList<>();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        Location location = jurisdictionLevels.get(i);
                        for (JurisdictionType state : selectedStates) {
                            if (state.getName().equalsIgnoreCase(location.getState().getName())) {
                                for (JurisdictionType district : selectedDistricts) {
                                    if (district.getName().equalsIgnoreCase(location.getDistrict().getName())) {
                                        for (JurisdictionType taluka : selectedTalukas) {
                                            if (taluka.getName().equalsIgnoreCase(location.getTaluka().getName())) {
                                                villages.add(location.getVillage().getName());
                                                this.villages.add(location.getVillage());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    spVillage.setItems(villages, getString(R.string.village), this);
                }
                break;

            case Constants.JurisdictionLevelName.CLUSTER_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.clusters.clear();
                    List<String> clusters = new ArrayList<>();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        Location location = jurisdictionLevels.get(i);
                        for (JurisdictionType state : selectedStates) {
                            if (state.getName().equalsIgnoreCase(location.getState().getName())) {
                                for (JurisdictionType district : selectedDistricts) {
                                    if (district.getName().equalsIgnoreCase(location.getDistrict().getName())) {
                                        for (JurisdictionType taluka : selectedTalukas) {
                                            if (taluka.getName().equalsIgnoreCase(location.getTaluka().getName())) {
                                                clusters.add(location.getCluster().getName());
                                                this.clusters.add(location.getCluster());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    spCluster.setItems(clusters, getString(R.string.cluster), this);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void showErrorMessage(String result) {
        runOnUiThread(() -> Util.showToast(result, this));
    }

    @Override
    public void onBackPressed() {
        try {
            hideProgressBar();
            setResult(RESULT_CANCELED);
            finish();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onValuesSelected(boolean[] selected, String spinnerName) {
        switch (spinnerName) {
            case Constants.MultiSelectSpinnerType.SPINNER_PROJECT:
                selectedProjects.clear();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        selectedProjects.add(projects.get(i).getId());
                        Log.d("TAG", "Selected project " + projects.get(i).getOrgProjectName());
                    }
                }
                break;

            case Constants.MultiSelectSpinnerType.SPINNER_ROLE:
                selectedRoles.clear();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        selectedRoles.add(roles.get(i).getId());
                        Log.d("TAG", "Selected role " + roles.get(i).getDisplayName());
                    }
                }
                break;

            case Constants.MultiSelectSpinnerType.SPINNER_DISTRICT:
                selectedDistricts.clear();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        selectedDistricts.add(districts.get(i));
                        Log.d("TAG", "Selected district " + districts.get(i));
                    }
                }

                if (spTaluka.getVisibility() == View.VISIBLE) {
                    profilePresenter.getJurisdictionLevelData(selectedOrg.getId(),
                            selectedRole.getProject().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.TALUKA_LEVEL);
                }
                break;

            case Constants.MultiSelectSpinnerType.SPINNER_TALUKA:
                selectedTalukas.clear();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        selectedTalukas.add(talukas.get(i));
                        Log.d("TAG", "Selected taluka " + talukas.get(i));
                    }
                }

                if (spVillage.getVisibility() == View.VISIBLE) {
                    profilePresenter.getJurisdictionLevelData(selectedOrg.getId(),
                            selectedRole.getProject().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.VILLAGE_LEVEL);
                }
                break;

            case Constants.MultiSelectSpinnerType.SPINNER_CLUSTER:
                selectedClusters.clear();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        selectedClusters.add(clusters.get(i));
                        Log.d("TAG", "Selected cluster " + clusters.get(i));
                    }
                }
                break;

            case Constants.MultiSelectSpinnerType.SPINNER_VILLAGE:
                selectedVillages.clear();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        selectedVillages.add(villages.get(i));
                        Log.d("TAG", "Selected village " + villages.get(i)
                        );
                    }
                }
                break;
        }
    }
}
