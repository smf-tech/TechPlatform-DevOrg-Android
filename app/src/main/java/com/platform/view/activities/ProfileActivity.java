package com.platform.view.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.bumptech.glide.Glide;
import com.platform.Platform;
import com.platform.R;
import com.platform.listeners.ProfileTaskListener;
import com.platform.models.UserInfo;
import com.platform.models.login.LoginInfo;
import com.platform.models.profile.Jurisdiction;
import com.platform.models.profile.JurisdictionLevel;
import com.platform.models.profile.Organization;
import com.platform.models.profile.OrganizationProject;
import com.platform.models.profile.OrganizationRole;
import com.platform.models.profile.State;
import com.platform.models.profile.UserLocation;
import com.platform.presenter.ProfileActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Permissions;
import com.platform.utility.Util;
import com.platform.widgets.ClusterMultiSelectSpinner;
import com.platform.widgets.DistrictMultiSelectSpinner;
import com.platform.widgets.ProjectMultiSelectSpinner;
import com.platform.widgets.RoleMultiSelectSpinner;
import com.platform.widgets.TalukaMultiSelectSpinner;
import com.platform.widgets.VillageMultiSelectSpinner;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressWarnings("CanBeFinal")
public class ProfileActivity extends BaseActivity implements ProfileTaskListener,
        View.OnClickListener, AdapterView.OnItemSelectedListener,
        ProjectMultiSelectSpinner.ProjectMultiSpinnerListener, RoleMultiSelectSpinner.RoleMultiSpinnerListener,
        DistrictMultiSelectSpinner.DistrictMultiSpinnerListener, TalukaMultiSelectSpinner.TalukaMultiSpinnerListener,
        ClusterMultiSelectSpinner.ClusterMultiSpinnerListener, VillageMultiSelectSpinner.VillageMultiSpinnerListener {

    private EditText etUserFirstName;
    private EditText etUserMiddleName;
    private EditText etUserLastName;
    private EditText etUserBirthDate;
    private EditText etUserMobileNumber;
    private EditText etUserEmailId;

    private Spinner spOrganization;
    private Spinner spState;
    private Spinner spStructure;

    private ProjectMultiSelectSpinner spProject;
    private RoleMultiSelectSpinner spRole;
    private DistrictMultiSelectSpinner spDistrict;
    private TalukaMultiSelectSpinner spTaluka;
    private ClusterMultiSelectSpinner spCluster;
    private VillageMultiSelectSpinner spVillage;

    private ImageView imgUserProfilePic;
    private ImageView backButton;
    private Button btnProfileSubmit;

    private String userGender = "Male";

    private ArrayList<OrganizationProject> projects = new ArrayList<>();
    private ArrayList<OrganizationRole> roles = new ArrayList<>();
    private ArrayList<JurisdictionLevel> districts = new ArrayList<>();
    private ArrayList<JurisdictionLevel> talukas = new ArrayList<>();
    private ArrayList<JurisdictionLevel> clusters = new ArrayList<>();
    private ArrayList<JurisdictionLevel> villages = new ArrayList<>();

    private ArrayList<String> projectIds = new ArrayList<>();
    private ArrayList<String> roleIds = new ArrayList<>();
    private ArrayList<String> districtIds = new ArrayList<>();
    private ArrayList<String> talukaIds = new ArrayList<>();
    private ArrayList<String> clusterIds = new ArrayList<>();
    private ArrayList<String> villageIds = new ArrayList<>();

    private Uri outputUri;
    private Uri finalUri;
    private ProfileActivityPresenter profilePresenter;

    private List<Organization> organizations = new ArrayList<>();
    private List<State> states = new ArrayList<>();

    private State selectedState;
    private Organization selectedOrg;

    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePresenter = new ProfileActivityPresenter(this);

        if (getIntent().getStringExtra(Constants.Login.ACTION) == null ||
                !getIntent().getStringExtra(Constants.Login.ACTION)
                        .equalsIgnoreCase(Constants.Login.ACTION_EDIT)) {
            profilePresenter.getOrganizations();
            profilePresenter.getStates();
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
        spState = findViewById(R.id.sp_user_state);
        spStructure = findViewById(R.id.sp_user_structure);

        spProject = findViewById(R.id.sp_project);
        spRole = findViewById(R.id.sp_role);
        spDistrict = findViewById(R.id.sp_district);
        spTaluka = findViewById(R.id.sp_taluka);
        spCluster = findViewById(R.id.sp_cluster);
        spVillage = findViewById(R.id.sp_village);

        imgUserProfilePic = findViewById(R.id.user_profile_pic);
        btnProfileSubmit = findViewById(R.id.btn_profile_submit);

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
                    etUserFirstName.setText(userInfo.getUserFirstName());
                    etUserMiddleName.setText(userInfo.getUserMiddleName());
                    etUserLastName.setText(userInfo.getUserLastName());
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
                }
            }
        } else {
            etUserMobileNumber.setText(Util.getUserMobileFromPref());
        }
    }

    private void setListeners() {
        backButton.setOnClickListener(this);
        etUserBirthDate.setOnClickListener(this);

        spOrganization.setOnItemSelectedListener(this);
        spState.setOnItemSelectedListener(this);
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
        Util.hideKeyboard (btnProfileSubmit);

        if (isAllInputsValid()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserFirstName(String.valueOf(etUserFirstName.getText()).trim());
            userInfo.setUserMiddleName(String.valueOf(etUserMiddleName.getText()).trim());
            userInfo.setUserLastName(String.valueOf(etUserLastName.getText()).trim());
            userInfo.setUserBirthDate(String.valueOf(etUserBirthDate.getText()).trim());
            userInfo.setUserMobileNumber(String.valueOf(etUserMobileNumber.getText()).trim());
            userInfo.setUserEmailId(String.valueOf(etUserEmailId.getText()).trim());
            userInfo.setUserGender(userGender);

            userInfo.setOrgId(selectedOrg.getId());
            userInfo.setProjectIds(projectIds);
            userInfo.setRoleIds(roleIds.get(0));

            UserLocation userLocation = new UserLocation();
            userLocation.setStateId(selectedState.getId());
            userLocation.setDistrictIds(districtIds);
            userLocation.setTalukaIds(talukaIds);
            userLocation.setClusterIds(clusterIds);
            userLocation.setVillageIds(villageIds);

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
        } else if (projectIds == null || projectIds.size() == 0) {
            msg = getString(R.string.msg_select_project);
        } else if (roleIds == null || roleIds.size() == 0) {
            msg = getString(R.string.msg_select_role);
        } else if (selectedState == null || TextUtils.isEmpty(selectedState.getId())) {
            msg = getString(R.string.msg_select_state);
        } else if ((spDistrict.getVisibility() == View.VISIBLE) && (districtIds == null || districtIds.size() == 0)) {
            msg = getString(R.string.msg_select_district);
        } else if ((spTaluka.getVisibility() == View.VISIBLE) && (talukaIds == null || talukaIds.size() == 0)) {
            msg = getString(R.string.msg_select_taluka);
        } else if ((spCluster.getVisibility() == View.VISIBLE) && (clusterIds == null || clusterIds.size() == 0)) {
            msg = getString(R.string.msg_select_cluster);
        } else if ((spVillage.getVisibility() == View.VISIBLE) && (villageIds == null || villageIds.size() == 0)) {
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
                String imageFilePath = "/MV/Image/picture_crop.jpg";
                finalUri = Util.getUri(imageFilePath);
                Crop.of(outputUri, finalUri).asSquare().start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    String imageFilePath = "/MV/Image/picture_crop.jpg";
                    outputUri = data.getData();
                    finalUri = Util.getUri(imageFilePath);
                    Crop.of(outputUri, finalUri).asSquare().start(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            Glide.with(this)
                    .load(finalUri)
                    .into(imgUserProfilePic);
        }
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

            case R.id.sp_user_state:
                if (states != null && !states.isEmpty() && states.get(i) != null
                        && states.get(i).getJurisdictions() != null
                        && !states.get(i).getJurisdictions().isEmpty()
                        && states.get(i).getJurisdictions().size() > 0) {

                    selectedState = states.get(i);

                    spDistrict.setVisibility(View.GONE);
                    spTaluka.setVisibility(View.GONE);
                    spCluster.setVisibility(View.GONE);
                    spVillage.setVisibility(View.GONE);

                    findViewById(R.id.txt_district).setVisibility(View.GONE);
                    findViewById(R.id.txt_taluka).setVisibility(View.GONE);
                    findViewById(R.id.txt_cluster).setVisibility(View.GONE);
                    findViewById(R.id.txt_village).setVisibility(View.GONE);

                    Util.saveUserLocationJurisdictionLevel(0);

                    for (Jurisdiction jurisdiction : states.get(i).getJurisdictions()) {
                        profilePresenter.getJurisdictionLevelData(jurisdiction.getStateId(),
                                jurisdiction.getLevel());
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
            spProject.setItems(organizationProjects, getString(R.string.project), this);
            this.projects.clear();
            this.projects.addAll(organizationProjects);
        }
    }

    @Override
    public void showOrganizationRoles(List<OrganizationRole> organizationRoles) {
        if (organizationRoles != null && !organizationRoles.isEmpty()) {
            spRole.setItems(organizationRoles, getString(R.string.role), this);
            this.roles.clear();
            this.roles.addAll(organizationRoles);
        }
    }

    @Override
    public void showStates(List<State> states) {
        this.states = states;
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
    public void showJurisdictionLevel(List<JurisdictionLevel> jurisdictionLevels, int level, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    spDistrict.setItems(jurisdictionLevels, getString(R.string.district), this);
                    this.districts.clear();
                    this.districts.addAll(jurisdictionLevels);
                    findViewById(R.id.txt_district).setVisibility(View.VISIBLE);
                    spDistrict.setVisibility(View.VISIBLE);
                }
                break;

            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    spTaluka.setItems(jurisdictionLevels, getString(R.string.taluka), this);
                    this.talukas.clear();
                    this.talukas.addAll(jurisdictionLevels);
                    findViewById(R.id.txt_taluka).setVisibility(View.VISIBLE);
                    spTaluka.setVisibility(View.VISIBLE);
                }
                break;

            case Constants.JurisdictionLevelName.CLUSTER_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    spCluster.setItems(jurisdictionLevels, getString(R.string.cluster), this);
                    this.clusters.clear();
                    this.clusters.addAll(jurisdictionLevels);
                    findViewById(R.id.txt_cluster).setVisibility(View.VISIBLE);
                    spCluster.setVisibility(View.VISIBLE);
                }
                break;

            case Constants.JurisdictionLevelName.VILLAGE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    spVillage.setItems(jurisdictionLevels, getString(R.string.village), this);
                    this.villages.clear();
                    this.villages.addAll(jurisdictionLevels);
                    findViewById(R.id.txt_village).setVisibility(View.VISIBLE);
                    spVillage.setVisibility(View.VISIBLE);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void showErrorMessage(String result) {
        Util.showToast(result, this);
    }

    @Override
    public void onBackPressed() {
        try {
            hideProgressBar();
            setResult(RESULT_CANCELED);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRolesSelected(boolean[] selected) {
        roleIds.clear();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                roleIds.add(roles.get(i).getId());
                Log.d("TAG", "Selected role " + roles.get(i).getDisplayName());
            }
        }
    }

    @Override
    public void onClustersSelected(boolean[] selected) {
        clusterIds.clear();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                clusterIds.add(clusters.get(i).getId());
                Log.d("TAG", "Selected cluster " + clusters.get(i).getJurisdictionLevelName());
            }
        }
    }

    @Override
    public void onDistrictsSelected(boolean[] selected) {
        districtIds.clear();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                districtIds.add(districts.get(i).getId());
                Log.d("TAG", "Selected district " + districts.get(i).getJurisdictionLevelName());
            }
        }
    }

    @Override
    public void onTalukasSelected(boolean[] selected) {
        talukaIds.clear();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                talukaIds.add(talukas.get(i).getId());
                Log.d("TAG", "Selected taluka " + talukas.get(i).getJurisdictionLevelName());
            }
        }
    }

    @Override
    public void onVillagesSelected(boolean[] selected) {
        villageIds.clear();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                villageIds.add(villages.get(i).getId());
                Log.d("TAG", "Selected village " + villages.get(i).getJurisdictionLevelName());
            }
        }
    }

    @Override
    public void onProjectsSelected(boolean[] selected) {
        projectIds.clear();
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                projectIds.add(projects.get(i).getId());
                Log.d("TAG", "Selected project " + projects.get(i).getOrgProjectName());
            }
        }
    }
}
