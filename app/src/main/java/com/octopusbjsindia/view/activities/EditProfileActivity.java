package com.octopusbjsindia.view.activities;

import android.Manifest;
import android.app.Activity;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.ProfileTaskListener;
import com.octopusbjsindia.models.login.LoginInfo;
import com.octopusbjsindia.models.profile.Jurisdiction;
import com.octopusbjsindia.models.profile.JurisdictionLocation;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.models.profile.Organization;
import com.octopusbjsindia.models.profile.OrganizationProject;
import com.octopusbjsindia.models.profile.OrganizationRole;
import com.octopusbjsindia.models.profile.UserLocation;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.presenter.ProfileActivityPresenter;
import com.octopusbjsindia.utility.AppEvents;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.widgets.MultiSelectSpinner;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("CanBeFinal")
public class EditProfileActivity extends BaseActivity implements ProfileTaskListener,
        View.OnClickListener, AdapterView.OnItemSelectedListener,
        MultiSelectSpinner.MultiSpinnerListener, APIDataListener {

    private EditText etUserFirstName;
    private EditText etUserMiddleName;
    private EditText etUserLastName;
    private EditText etUserBirthDate;
    private EditText etUserMobileNumber;
    private EditText etUserEmailId;
    private EditText etUserOrganization;

    private Spinner spOrganization;
    private Spinner spCountry;
    private Spinner spState;
    private Spinner spRole;
    private Spinner spStructure;
    private Spinner spProject;

    //private MultiSelectSpinner spProject;
    private MultiSelectSpinner spDistrict;
    private MultiSelectSpinner spCity;
    private MultiSelectSpinner spTaluka;
    private MultiSelectSpinner spCluster;
    private MultiSelectSpinner spVillage;

    private ImageView imgUserProfilePic;
    private ImageView backButton;
    private Button btnProfileSubmit;

    private String userGender = Constants.Login.MALE;

    private List<Organization> organizations = new ArrayList<>();
    private List<OrganizationProject> projects = new ArrayList<>();
    private List<OrganizationRole> roles = new ArrayList<>();

    private List<JurisdictionType> countries = new ArrayList<>();
    private List<JurisdictionType> states = new ArrayList<>();
    private List<JurisdictionType> districts = new ArrayList<>();
    private List<JurisdictionType> cities = new ArrayList<>();
    private List<JurisdictionType> talukas = new ArrayList<>();
    private List<JurisdictionType> clusters = new ArrayList<>();
    private List<JurisdictionType> villages = new ArrayList<>();

    private ArrayList<JurisdictionType> selectedProjects = new ArrayList<>();
    private ArrayList<String> selectedRoles = new ArrayList<>();

    private ArrayList<JurisdictionType> selectedCountries = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedStates = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedDistricts = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedCities = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedTalukas = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedClusters = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedVillages = new ArrayList<>();

    private Uri outputUri;
    private Uri finalUri;
    private ProfileActivityPresenter profilePresenter;

    private JurisdictionType selectedProject;
    private OrganizationRole selectedRole;
    private Organization selectedOrg;

    private boolean mImageUploaded;
    private String mUploadedImageUrl;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private final String TAG = EditProfileActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

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
                    userGender = Constants.Login.MALE;
                    break;
                case R.id.gender_female:
                    userGender = Constants.Login.FEMALE;
                    break;
                case R.id.gender_other:
                    userGender = Constants.Login.OTHER;
                    break;
            }
        });

        spOrganization = findViewById(R.id.sp_user_organization);
        etUserOrganization = findViewById(R.id.etUserOrganization);

        spProject = findViewById(R.id.sp_project);
        //spProject.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_PROJECT);

        spRole = findViewById(R.id.sp_role);
        spCountry = findViewById(R.id.sp_user_country);
        spState = findViewById(R.id.sp_user_state);

        spDistrict = findViewById(R.id.sp_district);
        spDistrict.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_DISTRICT);

        spCity = findViewById(R.id.sp_city);
        spCity.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_CITY);

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
                if (getIntent().getStringExtra(Constants.Login.ACTION) != null
                        && getIntent().getStringExtra(Constants.Login.ACTION)
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

                    etUserBirthDate.setText(Util.getLongDateInString(
                            userInfo.getUserBirthDate(), Constants.FORM_DATE));
                    etUserMobileNumber.setText(userInfo.getUserMobileNumber());
                    etUserEmailId.setText(userInfo.getUserEmailId());

                    if (!TextUtils.isEmpty(userInfo.getUserGender())) {
                        if (userInfo.getUserGender().equalsIgnoreCase(Constants.Login.MALE)) {
                            radioGroup.check(R.id.gender_male);
                            userGender = Constants.Login.MALE;
                        } else if (userInfo.getUserGender().equalsIgnoreCase(Constants.Login.FEMALE)) {
                            radioGroup.check(R.id.gender_female);
                            userGender = Constants.Login.FEMALE;
                        } else if (userInfo.getUserGender().equalsIgnoreCase(Constants.Login.OTHER)) {
                            radioGroup.check(R.id.gender_other);
                            userGender = Constants.Login.OTHER;
                        }
                    }

                    List<Organization> orgData = Util.getUserOrgFromPref().getData();
                    if (orgData != null && orgData.size() > 0) {
                        int id = 0;
                        showOrganizations(orgData);

                        for (int i = 0; i < orgData.size(); i++) {
                            if (userInfo.getOrgId().equals(orgData.get(i).getId())) {
                                id = i;
                                this.selectedOrg = orgData.get(i);
                            }
                        }
                        spOrganization.setSelection(id);
                    } else {
                        if (Util.isConnected(this)) {
                            profilePresenter.getOrganizations();
                        } else {
                            List<String> org = new ArrayList<>();
                            org.add(userInfo.getOrgName());
                            setOrganizationData(org);

                            List<Organization> orgList = new ArrayList<>();
                            Organization orgObj = new Organization();
                            orgObj.setId(userInfo.getOrgId());
                            orgObj.setOrgName(userInfo.getOrgName());
                            orgList.add(orgObj);
                            this.organizations = orgList;
                        }
                    }
                    if (!TextUtils.isEmpty(userInfo.getProfilePic())) {

                        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_user_avatar);
                        requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());


                        Glide.with(this)
                                .applyDefaultRequestOptions(requestOptions)
                                .load(userInfo.getProfilePic())
                                .into(imgUserProfilePic);

                        ((TextView) findViewById(R.id.user_profile_pic_label))
                                .setText(getString(R.string.update_profile_pic));
                    }
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
        spCountry.setOnItemSelectedListener(this);
        spState.setOnItemSelectedListener(this);
        spProject.setOnItemSelectedListener(this);
        spRole.setOnItemSelectedListener(this);
        spStructure.setOnItemSelectedListener(this);

        imgUserProfilePic.setOnClickListener(this);
        btnProfileSubmit.setOnClickListener(this);
        etUserOrganization.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_action:
                onBackPressed();
                break;

            case R.id.et_user_birth_date:
                showDateDialog(EditProfileActivity.this, findViewById(R.id.et_user_birth_date));
                break;

            case R.id.user_profile_pic:
                onAddImageClick();
                break;

            case R.id.btn_profile_submit:
                if (Util.isConnected(this)) {
                    submitProfileDetails();
                } else {
                    Util.showToast(getString(R.string.msg_no_network), this);
                }

                break;
            case R.id.etUserOrganization:
                spOrganization.performClick();
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
            userInfo.setUserBirthDate(Util.getDateInLong(String.valueOf(etUserBirthDate.getText()).trim()));
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

            JurisdictionType org = new JurisdictionType();
            org.setId(selectedOrg.getId());
            userInfo.setOrgId(org);

            userInfo.setType(selectedOrg.getType());
            userInfo.setProjectIds(selectedProjects);

            JurisdictionType role = new JurisdictionType();
            role.setId(selectedRole.getId());
            userInfo.setRoleIds(role);

            if (mImageUploaded && !TextUtils.isEmpty(mUploadedImageUrl)) {
                userInfo.setProfilePic(mUploadedImageUrl);
            } else {
                // Set old profile url if profile unchanged
                UserInfo info = Util.getUserObjectFromPref();
                if (!TextUtils.isEmpty(info.getProfilePic())) {
                    userInfo.setProfilePic(info.getProfilePic());
                }
            }

            UserLocation userLocation = new UserLocation();
            ArrayList<JurisdictionType> s = new ArrayList<>();
            for (JurisdictionType country : selectedCountries) {
                JurisdictionType countryObj = new JurisdictionType();
                countryObj.setId(country.getId());
                countryObj.setName(country.getName());
                s.add(countryObj);
                userLocation.setCountryId(s);
            }

            s = new ArrayList<>();
            for (JurisdictionType state : selectedStates) {
                JurisdictionType stateObj = new JurisdictionType();
                stateObj.setId(state.getId());
                stateObj.setName(state.getName());
                s.add(stateObj);
                userLocation.setStateId(s);
            }

            s = new ArrayList<>();
            for (JurisdictionType district : selectedDistricts) {
                JurisdictionType districtObj = new JurisdictionType();
                districtObj.setId(district.getId());
                districtObj.setName(district.getName());
                s.add(districtObj);
                userLocation.setDistrictIds(s);
            }

            s = new ArrayList<>();
            for (JurisdictionType city : selectedCities) {
                JurisdictionType cityObj = new JurisdictionType();
                cityObj.setId(city.getId());
                cityObj.setName(city.getName());
                s.add(cityObj);
                userLocation.setCityIds(s);
            }

            s = new ArrayList<>();
            for (JurisdictionType taluka : selectedTalukas) {
                JurisdictionType talukaObj = new JurisdictionType();
                talukaObj.setId(taluka.getId());
                talukaObj.setName(taluka.getName());
                s.add(talukaObj);
                userLocation.setTalukaIds(s);
            }

            s = new ArrayList<>();
            for (JurisdictionType cluster : selectedClusters) {
                JurisdictionType clusterObj = new JurisdictionType();
                clusterObj.setId(cluster.getId());
                clusterObj.setName(cluster.getName());
                s.add(clusterObj);
                userLocation.setClusterIds(s);
            }

            s = new ArrayList<>();
            for (JurisdictionType village : selectedVillages) {
                JurisdictionType villageObj = new JurisdictionType();
                villageObj.setId(village.getId());
                villageObj.setName(village.getName());
                s.add(villageObj);
                userLocation.setVillageIds(s);
            }

            userInfo.setUserLocation(userLocation);
            Util.saveUserLocationInPref(userLocation);
            //if (addDeviceId()) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.READ_PHONE_STATE},
                                Constants.READ_PHONE_STORAGE);
                    }
                }
                if(getDeviceId().length()>0) {
                    userInfo.setDevice_id(getDeviceId());
                    profilePresenter.submitProfile(userInfo);
                } else {
                    Util.snackBarToShowMsg(this.getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Please allow - Read Phone State permission.",
                            Snackbar.LENGTH_LONG);
                }
        }
    }

    private String getDeviceId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        Constants.READ_PHONE_STORAGE);
            } else {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.
                        TELEPHONY_SERVICE);
                //String deviceId = telephonyManager.getDeviceId();
                String deviceId = Settings.Secure.getString(this.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                return deviceId;
            }
        }
        return "";
    }

    private boolean isAllInputsValid() {
        String msg = "";

        if (etUserFirstName.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_name);
        } else if (etUserMobileNumber.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_mobile_number);
        } else if (etUserMobileNumber.getText().toString().trim().length() != 10) {
            msg = getResources().getString(R.string.msg_enter_valid_mobile_no);
        }  else if (etUserBirthDate.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_birth_date);
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
        } else if ((spCountry.getVisibility() == View.VISIBLE) &&
                (selectedCountries == null || selectedCountries.size() == 0)) {
            msg = getString(R.string.msg_select_country);
        } else if ((spState.getVisibility() == View.VISIBLE) &&
                (selectedStates == null || selectedStates.size() == 0)) {
            msg = getString(R.string.msg_select_state);
        } else if ((spDistrict.getVisibility() == View.VISIBLE) &&
                (selectedDistricts == null || selectedDistricts.size() == 0)) {
            msg = getString(R.string.msg_select_district);
        } else if ((spCity.getVisibility() == View.VISIBLE) &&
                (selectedCities == null || selectedCities.size() == 0)) {
            msg = getString(R.string.msg_select_city);
        } else if ((spTaluka.getVisibility() == View.VISIBLE) &&
                (selectedTalukas == null || selectedTalukas.size() == 0)) {
            msg = getString(R.string.msg_select_taluka);
        } else if ((spCluster.getVisibility() == View.VISIBLE) &&
                (selectedClusters == null || selectedClusters.size() == 0)) {
            msg = getString(R.string.msg_select_cluster);
        } else if ((spVillage.getVisibility() == View.VISIBLE) &&
                (selectedVillages == null || selectedVillages.size() == 0)) {
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
                Crop.of(outputUri, finalUri).start(this);
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
                    Crop.of(outputUri, finalUri).start(this);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                imgUserProfilePic.setImageURI(finalUri);
                final File imageFile = new File(Objects.requireNonNull(finalUri.getPath()));

                if (Util.isConnected(this)) {
                    if (Util.isValidImageSize(imageFile)) {
                        profilePresenter.uploadProfileImage(imageFile, Constants.Image.IMAGE_TYPE_PROFILE);
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

    public void onImageUploaded(String uploadedImageUrl) {
        mImageUploaded = true;
        mUploadedImageUrl = uploadedImageUrl;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.CAMERA_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showPictureDialog();
            }
            if(requestCode == Constants.READ_PHONE_STORAGE) {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getDeviceId();
                } else {
                    Util.snackBarToShowMsg(this.getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Please allow - Read Phone State permission.",
                            Snackbar.LENGTH_LONG);
                }
                return;
            }
        }
    }

    private void hideJurisdictionLevel() {
        spCountry.setVisibility(View.GONE);
        findViewById(R.id.txt_country).setVisibility(View.GONE);
        countries.clear();
        selectedCountries.clear();

        spState.setVisibility(View.GONE);
        findViewById(R.id.txt_state).setVisibility(View.GONE);
        states.clear();
        selectedStates.clear();

        spDistrict.setVisibility(View.GONE);
        findViewById(R.id.txt_district).setVisibility(View.GONE);
        districts.clear();
        selectedDistricts.clear();

        spCity.setVisibility(View.GONE);
        findViewById(R.id.txt_city).setVisibility(View.GONE);
        cities.clear();
        selectedCities.clear();

        spTaluka.setVisibility(View.GONE);
        findViewById(R.id.txt_taluka).setVisibility(View.GONE);
        talukas.clear();
        selectedTalukas.clear();

        spCluster.setVisibility(View.GONE);
        findViewById(R.id.txt_cluster).setVisibility(View.GONE);
        clusters.clear();
        selectedClusters.clear();

        spVillage.setVisibility(View.GONE);
        findViewById(R.id.txt_village).setVisibility(View.GONE);
        villages.clear();
        selectedVillages.clear();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.sp_user_organization:
//                if (getIntent().getStringExtra(Constants.Login.ACTION) != null
//                        && getIntent().getStringExtra(Constants.Login.ACTION)
//                        .equalsIgnoreCase(Constants.Login.ACTION_EDIT)) {

                    //UserInfo userInfo = Util.getUserObjectFromPref();
//                    int projectPosition = 0;
//                    List<OrganizationProject> projectData = Util.getUserProjectsFromPref(this.selectedOrg.getId()).getData();
//                    if (projectData != null && projectData.size() > 0) {
//                        showOrganizationProjects(projectData);
//
////                        boolean[] selectedValues = new boolean[projectData.size()];
////                        for (int projectIndex = 0; projectIndex < projectData.size(); projectIndex++) {
////                            selectedValues[projectIndex]
////                                    = isContainsValue(userInfo.getProjectIds(), projectData.get(projectIndex).getId());
//////                            if (userInfo.getProjectIds().equals(projectData.get(projectIndex).getId())) {
//////                                projectPosition = projectIndex;
//////                            }
////                        }
//                        //spProject.setSelectedValues(selectedValues);
//                        for(int userProjectIndex = 0; userProjectIndex< userInfo.getProjectIds().size(); userProjectIndex++ ){
//                            for (int projectIndex = 0; projectIndex < projectData.size(); projectIndex++) {
//                                if (userInfo.getProjectIds().get(userProjectIndex).getId().
//                                        equals(projectData.get(projectIndex).getId())) {
//                                    projectPosition = projectIndex;
//                                    break;
//                                }
//                            }
//                        }
//                        spProject.setSelection(projectPosition);
//                        //spProject.setPreFilledText();
//                        etUserOrganization.setText(this.selectedOrg.getOrgName());
//                    } else {
                        if (Util.isConnected(this)) {
                            if (organizations != null && !organizations.isEmpty() && organizations.get(i) != null
                            && !TextUtils.isEmpty(organizations.get(i).getId())) {
                                this.selectedOrg = organizations.get(i);
                                profilePresenter.getOrganizationProjects(this.selectedOrg.getId());
                            }
                        }
//                        else {
//                            List<OrganizationProject> projects = new ArrayList<>();
//                            ArrayList<JurisdictionType> projectIds = userInfo.getProjectIds();
//                            for (JurisdictionType project : projectIds) {
//                                OrganizationProject op = new OrganizationProject();
//                                op.setId(project.getId());
//                                op.setOrgProjectName(project.getName());
//                                projects.add(op);
//                            }
//                            showOrganizationProjects(projects);
//                        }
                //    }
                //}
//                else {
//                    if (organizations != null && !organizations.isEmpty() && organizations.get(i) != null
//                            && !TextUtils.isEmpty(organizations.get(i).getId())) {
//                        this.selectedOrg = organizations.get(i);
//                        profilePresenter.getOrganizationProjects(organizations.get(i).getId());
////                        profilePresenter.getOrganizationRoles(organizations.get(i).getId(),
////                                Util.getUserObjectFromPref().getProjectIds().get(0).getId());
//                    }
//                }
                break;

            case R.id.sp_project:
//                if (getIntent().getStringExtra(Constants.Login.ACTION) != null
//                        && getIntent().getStringExtra(Constants.Login.ACTION)
//                        .equalsIgnoreCase(Constants.Login.ACTION_EDIT)) {

                    //UserInfo userInfo = Util.getUserObjectFromPref();
//                    int id = 0;
//                    List<OrganizationRole> roleData = Util.getUserRoleFromPref(this.selectedOrg.getId()).getData();
//                    if (roleData != null && roleData.size() > 0) {
//                        showOrganizationRoles(roleData);
//                        for (int roleIndex = 0; roleIndex < roleData.size(); roleIndex++) {
//                            if (userInfo.getRoleIds().equals(roleData.get(roleIndex).getId())) {
//                                id = roleIndex;
//                            }
//                        }
//                        spRole.setSelection(id);
//                    } else {
                        if (Util.isConnected(this)) {
                            if (projects != null && !projects.isEmpty() && projects.get(i) != null
                                    && !TextUtils.isEmpty(projects.get(i).getId())) {
                                profilePresenter.getOrganizationRoles(this.selectedOrg.getId(),
                                        projects.get(i).getId());
                            }
                        }
//                        else {
//                            List<OrganizationRole> orgRoles = new ArrayList<>();
//                            OrganizationRole or = new OrganizationRole();
//                            or.setId(userInfo.getRoleIds());
//                            or.setDisplayName(userInfo.getRoleNames());
//                            orgRoles.add(or);
//                            showOrganizationRoles(orgRoles);
//                        }
                //    }
            //    }
//                else {
//                    if (projects != null && !projects.isEmpty() && projects.get(i) != null
//                            && !TextUtils.isEmpty(projects.get(i).getId())) {
//                        //this.selectedProject = projects.get(i);
//                        profilePresenter.getOrganizationRoles(organizations.get(i).getId(),
//                                projects.get(i).getId());
//                    }
//                }
                selectedProjects.clear();
//                for (int i = 0; i < selected.length; i++) {
//                    if (selected[i]) {
                        JurisdictionType project = new JurisdictionType();
                        project.setId(projects.get(i).getId());
                        project.setName(projects.get(i).getOrgProjectName());
                        selectedProjects.add(project);
//
//                profilePresenter.getOrganizationRoles(organizations.get(i).getId(),
//                        projects.get(i).getId());
//                    }
//                }
                break;

            case R.id.sp_role:
                if (roles != null && !roles.isEmpty() && roles.get(i) != null) {

                    selectedRoles.clear();
                    selectedRole = roles.get(i);
                    selectedRoles.add(selectedRole.getDisplayName());

                    if (selectedRole.getProject() != null) {
                        List<Jurisdiction> jurisdictions = selectedRole.getProject().getJurisdictions();
                        if (jurisdictions != null && jurisdictions.size() > 0) {
                            hideJurisdictionLevel();
                            for (Jurisdiction j : jurisdictions) {
                                setJurisdictionLevel(j.getLevelName());
                            }
                        }
                    } else {
                        UserInfo userInfo = Util.getUserObjectFromPref();
                        UserLocation userLocation = userInfo.getUserLocation();
                        if (userLocation.getCountryId() != null && userLocation.getCountryId().size() > 0) {
                            setJurisdictionLevel(Constants.JurisdictionLevelName.COUNTRY_LEVEL);
                        }

                        if (userLocation.getStateId() != null && userLocation.getStateId().size() > 0) {
                            setJurisdictionLevel(Constants.JurisdictionLevelName.STATE_LEVEL);
                        }

                        if (userLocation.getDistrictIds() != null && userLocation.getDistrictIds().size() > 0) {
                            setJurisdictionLevel(Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                        }

                        if (userLocation.getCityIds() != null && userLocation.getCityIds().size() > 0) {
                            setJurisdictionLevel(Constants.JurisdictionLevelName.CITY_LEVEL);
                        }

                        if (userLocation.getTalukaIds() != null && userLocation.getTalukaIds().size() > 0) {
                            setJurisdictionLevel(Constants.JurisdictionLevelName.TALUKA_LEVEL);
                        }

                        if (userLocation.getVillageIds() != null && userLocation.getVillageIds().size() > 0) {
                            setJurisdictionLevel(Constants.JurisdictionLevelName.VILLAGE_LEVEL);
                        }

                        if (userLocation.getClusterIds() != null && userLocation.getClusterIds().size() > 0) {
                            setJurisdictionLevel(Constants.JurisdictionLevelName.CLUSTER_LEVEL);
                        }
                    }
                }
                break;

            case R.id.sp_user_country:
                if (countries != null && !countries.isEmpty() && countries.get(i) != null) {
                    selectedCountries.clear();
                    selectedCountries.add(countries.get(i));

                    if(spState.getVisibility() == View.VISIBLE){
                        if(Util.isConnected(this)){
                            profilePresenter.getLocationData(selectedCountries.get(0).getId(),
                                    selectedRole.getProject().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.STATE_LEVEL);
                        }
//                        else {
//                            List<String> stateNames = new ArrayList<>();
//                            UserInfo userInfo = Util.getUserObjectFromPref();
//                            List<JurisdictionType> stateObj = userInfo.getUserLocation().getStateId();
//
//                            Collections.sort(stateObj, (j1, j2) -> j1.getName().compareTo(j2.getName()));
//
//                            for (int k = 0; k < stateObj.size(); k++) {
//                                stateNames.add(stateObj.get(k).getName());
//                                this.states.add(stateObj.get(k));
//                            }
//                            setStateData(stateNames);
//                        }
                    }
                }

            case R.id.sp_user_state:
                if (states != null && !states.isEmpty() && states.get(i) != null) {

                    selectedStates.clear();
                    selectedStates.add(states.get(i));

                    if (spCity.getVisibility() == View.VISIBLE) {
                        if (Util.isConnected(this)) {
                            profilePresenter.getLocationData(selectedStates.get(0).getId(),
                                    selectedRole.getProject().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.CITY_LEVEL);
                        }
//                        else {
//                            List<String> cityNames = new ArrayList<>();
//                            UserInfo userInfo = Util.getUserObjectFromPref();
//                            List<JurisdictionType> cityObj = userInfo.getUserLocation().getCityIds();
//
//                            Collections.sort(cityObj, (j1, j2) -> j1.getName().compareTo(j2.getName()));
//
//                            for (int k = 0; k < cityObj.size(); k++) {
//                                cityNames.add(cityObj.get(k).getName());
//                                this.cities.add(cityObj.get(k));
//                            }
//                            setCityData(cityNames);
//                        }
                    }

                    if (spDistrict.getVisibility() == View.VISIBLE) {
                        if (Util.isConnected(this)) {
                            profilePresenter.getLocationData(selectedStates.get(0).getId(),
                                    selectedRole.getProject().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                        }
//                        else {
//                            List<String> districtNames = new ArrayList<>();
//                            UserInfo userInfo = Util.getUserObjectFromPref();
//                            List<JurisdictionType> districtObj = userInfo.getUserLocation().getDistrictIds();
//
//                            Collections.sort(districtObj, (j1, j2) -> j1.getName().compareTo(j2.getName()));
//
//                            for (int k = 0; k < districtObj.size(); k++) {
//                                districtNames.add(districtObj.get(k).getName());
//                                this.districts.add(districtObj.get(k));
//                            }
//
//                            setDistrictData(districtNames);
//                        }
                    }
                }
                break;

            case R.id.sp_user_structure:
                break;
        }
    }

    private boolean isContainsValue(ArrayList<JurisdictionType> projects, String value) {
        for (JurisdictionType project : projects) {
            if (project.getId().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(message,this);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        Util.showToast(error.getMessage(),this);
    }

    @Override
    public void onSuccessListener(String requestID, String response) {

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
    public void closeCurrentActivity() {

    }

    @Override
    public <T> void showNextScreen(T data) {
        Util.removeDatabaseRecords(false);
        Util.setSubmittedFormsLoaded(false);

        AppEvents.trackAppEvent(getString(R.string.event_update_profile_success));
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showErrorMessage(String result) {
        AppEvents.trackAppEvent(getString(R.string.event_update_profile_fail));
        runOnUiThread(() -> Util.showToast(result, this));
    }

    @Override
    public void showOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
        List<String> org = new ArrayList<>();
        for (int i = 0; i < organizations.size(); i++) {
            org.add(organizations.get(i).getOrgName());
        }

        setOrganizationData(org);
    }

    @Override
    public void showOrganizationProjects(List<OrganizationProject> organizationProjects) {
        if (organizationProjects != null && !organizationProjects.isEmpty()) {

            Collections.sort(organizationProjects, (j1, j2) -> j1.getOrgProjectName().compareTo(j2.getOrgProjectName()));

//            this.projects.clear();
//            this.projects.addAll(organizationProjects);

            List<String> projects = new ArrayList<>();
//            List<OrganizationProject> projects = new ArrayList<>();
            for (OrganizationProject organizationProject : organizationProjects) {
                //projects.add(organizationProject.getOrgProjectName());
                projects.add(organizationProject.getOrgProjectName());
            }

            this.projects.clear();
            this.projects.addAll(organizationProjects);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProfileActivity.this,
                    R.layout.layout_spinner_item, projects);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spProject.setAdapter(adapter);

            //spProject.setItems(projects, getString(R.string.project), this);

            if (getIntent().getStringExtra(Constants.Login.ACTION) != null
                    && getIntent().getStringExtra(Constants.Login.ACTION)
                    .equalsIgnoreCase(Constants.Login.ACTION_EDIT)) {

                int projectId = 0;
                UserInfo userInfo = Util.getUserObjectFromPref();

                //boolean[] selectedValues = new boolean[organizationProjects.size()];
                for(int userProjectIndex = 0; userProjectIndex< userInfo.getProjectIds().size(); userProjectIndex++ ){
                    for (int projectIndex = 0; projectIndex < organizationProjects.size(); projectIndex++) {
                        if (userInfo.getProjectIds().get(userProjectIndex).getId().
                                equals(organizationProjects.get(projectIndex).getId())) {
                            projectId = projectIndex;
                        }
                    }
                }
                //spProject.setSelectedValues(selectedValues);
                spProject.setSelection(projectId);
                //spProject.setPreFilledText();
            }
        }
    }

    @Override
    public void showOrganizationRoles(List<OrganizationRole> organizationRoles) {
        if (organizationRoles != null && !organizationRoles.isEmpty()) {

            Collections.sort(organizationRoles, (j1, j2) -> j1.getDisplayName().compareTo(j2.getDisplayName()));

            List<String> roles = new ArrayList<>();
            for (OrganizationRole organizationRole : organizationRoles) {
                roles.add(organizationRole.getDisplayName());
            }

            this.roles.clear();
            this.roles.addAll(organizationRoles);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProfileActivity.this,
                    R.layout.layout_spinner_item, roles);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spRole.setAdapter(adapter);

            if (getIntent().getStringExtra(Constants.Login.ACTION) != null
                    && getIntent().getStringExtra(Constants.Login.ACTION)
                    .equalsIgnoreCase(Constants.Login.ACTION_EDIT)) {

                int id = 0;
                UserInfo userInfo = Util.getUserObjectFromPref();
                for (int roleIndex = 0; roleIndex < organizationRoles.size(); roleIndex++) {
                    if (userInfo.getRoleIds().equals(organizationRoles.get(roleIndex).getId())) {
                        id = roleIndex;
                    }
                }
                spRole.setSelection(id);
            }
        }
    }

    private void setJurisdictionLevel(String level) {
        switch (level) {
            case Constants.JurisdictionLevelName.COUNTRY_LEVEL:
                spCountry.setVisibility(View.VISIBLE);
                findViewById(R.id.txt_country).setVisibility(View.VISIBLE);
                if (Util.isConnected(this)) {
                    profilePresenter.getLocationData("",
                            selectedRole.getProject().getJurisdictionTypeId(), level);
                } else {
                    List<String> countryNames = new ArrayList<>();
                    UserInfo userInfo = Util.getUserObjectFromPref();
                    List<JurisdictionType> countriesObj = userInfo.getUserLocation().getCountryId();

                    Collections.sort(countriesObj, (j1, j2) -> j1.getName().compareTo(j2.getName()));

                    for (int k = 0; k < countriesObj.size(); k++) {
                        countryNames.add(countriesObj.get(k).getName());
                        this.countries.add(countriesObj.get(k));
                    }
                    setCountryData(countryNames);
                }
                break;
            case Constants.JurisdictionLevelName.STATE_LEVEL:
                spState.setVisibility(View.VISIBLE);
                findViewById(R.id.txt_state).setVisibility(View.VISIBLE);
                if (Util.isConnected(this)) {
                    if(selectedCountries.size()>0 && selectedCountries.get(0).getId().length()>0) {
                        profilePresenter.getLocationData(selectedCountries.get(0).getId(),
                                selectedRole.getProject().getJurisdictionTypeId(), level);
                    } else {
                        profilePresenter.getLocationData("",
                                selectedRole.getProject().getJurisdictionTypeId(), level);
                    }
                } else {
                    List<String> stateNames = new ArrayList<>();
                    UserInfo userInfo = Util.getUserObjectFromPref();
                    List<JurisdictionType> statesObj = userInfo.getUserLocation().getStateId();

                    Collections.sort(statesObj, (j1, j2) -> j1.getName().compareTo(j2.getName()));

                    for (int k = 0; k < statesObj.size(); k++) {
                        stateNames.add(statesObj.get(k).getName());
                        this.states.add(statesObj.get(k));
                    }
                    setStateData(stateNames);
                }
                break;

            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                spDistrict.setVisibility(View.VISIBLE);
                findViewById(R.id.txt_district).setVisibility(View.VISIBLE);
                break;

            case Constants.JurisdictionLevelName.CITY_LEVEL:
                spCity.setVisibility(View.VISIBLE);
                findViewById(R.id.txt_city).setVisibility(View.VISIBLE);
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

    private void setOrganizationData(List<String> org) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProfileActivity.this,
                R.layout.layout_spinner_item, org);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrganization.setAdapter(adapter);
    }

    private void setCountryData(List<String> countryNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProfileActivity.this,
                R.layout.layout_spinner_item, countryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCountry.setAdapter(adapter);

        if (getIntent().getStringExtra(Constants.Login.ACTION) != null
                && getIntent().getStringExtra(Constants.Login.ACTION)
                .equalsIgnoreCase(Constants.Login.ACTION_EDIT)) {

            int id = 0;
            UserInfo userInfo = Util.getUserObjectFromPref();
            List<JurisdictionType> countryId = userInfo.getUserLocation().getCountryId();
            for (int i = 0; i < countries.size(); i++) {
                if(countryId != null) {
                    if (countryId.get(0).getId().equals(countries.get(i).getId())) {
                        id = i;
                    }
                }
            }
            spCountry.setSelection(id);
        }
    }

    private void setStateData(List<String> stateNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProfileActivity.this,
                R.layout.layout_spinner_item, stateNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spState.setAdapter(adapter);

        if (getIntent().getStringExtra(Constants.Login.ACTION) != null
                && getIntent().getStringExtra(Constants.Login.ACTION)
                .equalsIgnoreCase(Constants.Login.ACTION_EDIT)) {

            int id = 0;
            UserInfo userInfo = Util.getUserObjectFromPref();
            List<JurisdictionType> stateId = userInfo.getUserLocation().getStateId();
            for (int i = 0; i < states.size(); i++) {
                if(stateId != null) {
                    if (stateId.get(0).getId().equals(states.get(i).getId())) {
                        id = i;
                    }
                }
            }
            spState.setSelection(id);
        }
    }

    private void setDistrictData(List<String> districts) {
        spDistrict.setItems(districts, getString(R.string.district), this);

        if (Util.getUserObjectFromPref().getUserLocation() != null) {
            List<JurisdictionType> districtIds = Util.getUserObjectFromPref().getUserLocation().getDistrictIds();
            if (districtIds != null && districtIds.size() > 0) {
                boolean[] selectedValues = new boolean[this.districts.size()];
                for (int districtIndex = 0; districtIndex < this.districts.size(); districtIndex++) {
                    for (int districtIdIndex = 0; districtIdIndex < districtIds.size(); districtIdIndex++) {
                        if (this.districts.get(districtIndex).getId().equals(districtIds.get(districtIdIndex).getId())) {
                            selectedValues[districtIndex] = true;
                            break;
                        } else {
                            selectedValues[districtIndex] = false;
                        }
                    }
                }
                spDistrict.setSelectedValues(selectedValues);
                spDistrict.setPreFilledText();
            }
        }
    }

    private void setCityData(List<String> cities) {
        spCity.setItems(cities, getString(R.string.city), this);

        if (Util.getUserObjectFromPref().getUserLocation() != null) {
            List<JurisdictionType> cityIds = Util.getUserObjectFromPref().getUserLocation().getCityIds();
            if (cityIds != null && cityIds.size() > 0) {
                boolean[] selectedValues = new boolean[this.cities.size()];
                for (int cityIndex = 0; cityIndex < this.cities.size(); cityIndex++) {
                    for (int cityIdIndex = 0; cityIdIndex < cityIds.size(); cityIdIndex++) {
                        if (this.cities.get(cityIndex).getId().equals(cityIds.get(cityIdIndex).getId())) {
                            selectedValues[cityIndex] = true;
                            break;
                        } else {
                            selectedValues[cityIndex] = false;
                        }
                    }
                }

                spCity.setSelectedValues(selectedValues);
                spCity.setPreFilledText();
            }
        }
    }

    private void setTalukaData(List<String> talukas) {
        spTaluka.setItems(talukas, getString(R.string.taluka), this);

        if (Util.getUserObjectFromPref().getUserLocation() != null) {
            List<JurisdictionType> talukaIds = Util.getUserObjectFromPref().getUserLocation().getTalukaIds();
            if (talukaIds != null && talukaIds.size() > 0) {
                boolean[] selectedValues = new boolean[this.talukas.size()];
                for (int talukaIndex = 0; talukaIndex < this.talukas.size(); talukaIndex++) {
                    for (int talukaIdIndex = 0; talukaIdIndex < talukaIds.size(); talukaIdIndex++) {
                        if (this.talukas.get(talukaIndex).getId().equals(talukaIds.get(talukaIdIndex).getId())) {
                            selectedValues[talukaIndex] = true;
                            break;
                        } else {
                            selectedValues[talukaIndex] = false;
                        }
                    }
                }

                spTaluka.setSelectedValues(selectedValues);
                spTaluka.setPreFilledText();
            }
        }
    }

    private void setVillageData(List<String> villages) {
        spVillage.setItems(villages, getString(R.string.village), this);

        if (Util.getUserObjectFromPref().getUserLocation() != null) {
            List<JurisdictionType> villageIds = Util.getUserObjectFromPref().getUserLocation().getVillageIds();
            if (villageIds != null && villageIds.size() > 0) {
                boolean[] selectedValues = new boolean[this.villages.size()];
                for (int villageIndex = 0; villageIndex < this.villages.size(); villageIndex++) {
                    for (int villageIdIndex = 0; villageIdIndex < villageIds.size(); villageIdIndex++) {
                        if (this.villages.get(villageIndex).getId().equals(villageIds.get(villageIdIndex).getId())) {
                            selectedValues[villageIndex] = true;
                            break;
                        } else {
                            selectedValues[villageIndex] = false;
                        }
                    }
                }
                spVillage.setSelectedValues(selectedValues);
                spVillage.setPreFilledText();
            }
        }
    }

    @Override
    public void showJurisdictionLevel(List<JurisdictionLocation> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.COUNTRY_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.countries.clear();
                    List<String> countryNames = new ArrayList<>();

                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getCountry().getName().compareTo(j2.getCountry().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        countryNames.add(location.getName());
                        JurisdictionType jurisdictionType = new JurisdictionType();
                        jurisdictionType.setId(location.getId());
                        jurisdictionType.setName(location.getName());
                        this.countries.add(jurisdictionType);
                    }

                    setCountryData(countryNames);
                }
                break;
            case Constants.JurisdictionLevelName.STATE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.states.clear();
                    List<String> stateNames = new ArrayList<>();

                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getState().getName().compareTo(j2.getState().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        stateNames.add(location.getName());
                        JurisdictionType jurisdictionType = new JurisdictionType();
                        jurisdictionType.setId(location.getId());
                        jurisdictionType.setName(location.getName());
                        this.states.add(jurisdictionType);
                    }
                    setStateData(stateNames);
                }
                break;

            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.districts.clear();
                    List<String> districts = new ArrayList<>();

                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getDistrict().getName().compareTo(j2.getDistrict().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
//                        for (JurisdictionType state : selectedStates) {
//                            if (state.getName().equalsIgnoreCase(location.getState().getName())) {
                                districts.add(location.getName());
                            JurisdictionType jurisdictionType = new JurisdictionType();
                            jurisdictionType.setId(location.getId());
                            jurisdictionType.setName(location.getName());
                                this.districts.add(jurisdictionType);
                            //}
                        //}
                    }
                    setDistrictData(districts);
                }
                break;

            case Constants.JurisdictionLevelName.CITY_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.cities.clear();
                    List<String> cities = new ArrayList<>();

                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getCity().getName().compareTo(j2.getCity().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
//                        for (JurisdictionType state : selectedStates) {
                            //if (state.getName().equalsIgnoreCase(location.getState().getName())) {
                                cities.add(location.getName());
                                JurisdictionType jurisdictionType = new JurisdictionType();
                                jurisdictionType.setId(location.getId());
                                jurisdictionType.setName(location.getName());
                                this.cities.add(jurisdictionType);
                            //}
                        //}
                    }
                    setCityData(cities);
                }
                break;

            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.talukas.clear();
                    List<String> talukas = new ArrayList<>();

                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getTaluka().getName().compareTo(j2.getTaluka().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
//                        for (JurisdictionType state : selectedStates) {
//                            if (state.getName().equalsIgnoreCase(location.getState().getName())) {
//                                for (JurisdictionType district : selectedDistricts) {
                                    //if (district.getName().equalsIgnoreCase(location.getDistrict().getName())) {
                                        talukas.add(location.getName());
                                        JurisdictionType jurisdictionType = new JurisdictionType();
                                        jurisdictionType.setId(location.getId());
                                        jurisdictionType.setName(location.getName());
                                        this.talukas.add(jurisdictionType);
                                    //}
                                //}
//                            }
//                        }
                    }
                    setTalukaData(talukas);
                }
                break;

            case Constants.JurisdictionLevelName.VILLAGE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.villages.clear();
                    List<String> villages = new ArrayList<>();

                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getVillage().getName().compareTo(j2.getVillage().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
//                        for (JurisdictionType state : selectedStates) {
//                            if (state.getName().equalsIgnoreCase(location.getState().getName())) {
//                                for (JurisdictionType district : selectedDistricts) {
//                                    if (district.getName().equalsIgnoreCase(location.getDistrict().getName())) {
//                                        for (JurisdictionType taluka : selectedTalukas) {
//                                            if (taluka.getName().equalsIgnoreCase(location.getTaluka().getName())) {
                                                villages.add(location.getName());
                                                JurisdictionType jurisdictionType = new JurisdictionType();
                                                jurisdictionType.setId(location.getId());
                                                jurisdictionType.setName(location.getName());
                                                this.villages.add(jurisdictionType);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
                    }
                    setVillageData(villages);
                }
                break;

            case Constants.JurisdictionLevelName.CLUSTER_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.clusters.clear();
                    List<String> clusters = new ArrayList<>();

                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getCluster().getName().compareTo(j2.getCluster().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
//                        for (JurisdictionType state : selectedStates) {
//                            if (state.getName().equalsIgnoreCase(location.getState().getName())) {
//                                for (JurisdictionType district : selectedDistricts) {
//                                    if (district.getName().equalsIgnoreCase(location.getDistrict().getName())) {
//                                        for (JurisdictionType taluka : selectedTalukas) {
//                                            if (taluka.getName().equalsIgnoreCase(location.getTaluka().getName())) {
                                                clusters.add(location.getName());
                                                JurisdictionType jurisdictionType = new JurisdictionType();
                                                jurisdictionType.setId(location.getId());
                                                jurisdictionType.setName(location.getName());
                                                this.clusters.add(jurisdictionType);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
                        //}
                    }
                    spCluster.setItems(clusters, getString(R.string.cluster), this);

                    if (Util.getUserObjectFromPref().getUserLocation() != null) {
                        List<JurisdictionType> clusterIds = Util.getUserObjectFromPref().getUserLocation().getClusterIds();
                        if (clusterIds != null && clusterIds.size() > 0) {
                            boolean[] selectedValues = new boolean[this.clusters.size()];
                            for (int clusterIndex = 0; clusterIndex < this.clusters.size(); clusterIndex++) {
                                for (int clusterIdIndex = 0; clusterIdIndex < clusterIds.size(); clusterIdIndex++) {
                                    if (this.clusters.get(clusterIndex).getId().equals(clusterIds.get(clusterIdIndex).getId())) {
                                        selectedValues[clusterIndex] = true;
                                        break;
                                    } else {
                                        selectedValues[clusterIndex] = false;
                                    }
                                }
                            }
                            spCluster.setSelectedValues(selectedValues);
                            spCluster.setPreFilledText();
                        }
                    }
                }
                break;

            default:
                break;
        }
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
        try {
            switch (spinnerName) {
                case Constants.MultiSelectSpinnerType.SPINNER_PROJECT:
                    selectedProjects.clear();
                    for (int i = 0; i < selected.length; i++) {
                        if (selected[i]) {
                            JurisdictionType project = new JurisdictionType();
                            project.setId(projects.get(i).getId());
                            project.setName(projects.get(i).getOrgProjectName());
                            selectedProjects.add(project);
                        }
                    }
                    break;

                case Constants.MultiSelectSpinnerType.SPINNER_ROLE:
                    selectedRoles.clear();
                    for (int i = 0; i < selected.length; i++) {
                        if (selected[i]) {
                            selectedRoles.add(roles.get(i).getId());
                        }
                    }
                    break;

                case Constants.MultiSelectSpinnerType.SPINNER_CITY:
                    selectedCities.clear();
                    for (int i = 0; i < selected.length; i++) {
                        if (selected[i]) {
                            selectedCities.add(cities.get(i));
                        }
                    }
                    break;

                case Constants.MultiSelectSpinnerType.SPINNER_DISTRICT:
                    selectedDistricts.clear();
                    for (int i = 0; i < selected.length; i++) {
                        if (selected[i]) {
                            selectedDistricts.add(districts.get(i));
                        }
                    }

                    if (spTaluka.getVisibility() == View.VISIBLE) {
                        if (Util.isConnected(this)) {
                            profilePresenter.getLocationData(selectedDistricts.get(0).getId(),
                                    selectedRole.getProject().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.TALUKA_LEVEL);
                        } else {
                            List<String> talukaNames = new ArrayList<>();
                            UserInfo userInfo = Util.getUserObjectFromPref();
                            List<JurisdictionType> talukaObj = userInfo.getUserLocation().getTalukaIds();

                            Collections.sort(talukaObj, (j1, j2) -> j1.getName().compareTo(j2.getName()));

                            for (int k = 0; k < talukaObj.size(); k++) {
                                talukaNames.add(talukaObj.get(k).getName());
                                this.talukas.add(talukaObj.get(k));
                            }

                            setTalukaData(talukaNames);
                        }
                    }
                    break;

                case Constants.MultiSelectSpinnerType.SPINNER_TALUKA:
                    selectedTalukas.clear();
                    for (int i = 0; i < selected.length; i++) {
                        if (selected[i]) {
                            selectedTalukas.add(talukas.get(i));
                        }
                    }

                    if (spVillage.getVisibility() == View.VISIBLE) {
                        if (Util.isConnected(this)) {
                            profilePresenter.getLocationData(selectedTalukas.get(0).getId(),
                                    selectedRole.getProject().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.VILLAGE_LEVEL);
                        } else {
                            List<String> villageNames = new ArrayList<>();
                            UserInfo userInfo = Util.getUserObjectFromPref();
                            List<JurisdictionType> villageObj = userInfo.getUserLocation().getVillageIds();

                            Collections.sort(villageObj, (j1, j2) -> j1.getName().compareTo(j2.getName()));

                            for (int k = 0; k < villageObj.size(); k++) {
                                villageNames.add(villageObj.get(k).getName());
                                this.villages.add(villageObj.get(k));
                            }

                            setVillageData(villageNames);
                        }
                    }
                    break;

                case Constants.MultiSelectSpinnerType.SPINNER_CLUSTER:
                    selectedClusters.clear();
                    for (int i = 0; i < selected.length; i++) {
                        if (selected[i]) {
                            selectedClusters.add(clusters.get(i));
                        }
                    }
                    break;

                case Constants.MultiSelectSpinnerType.SPINNER_VILLAGE:
                    selectedVillages.clear();
                    for (int i = 0; i < selected.length; i++) {
                        if (selected[i]) {
                            selectedVillages.add(villages.get(i));
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "EXCEPTION_IN_ON_VALUE_SELECTED");
        }
    }
}
