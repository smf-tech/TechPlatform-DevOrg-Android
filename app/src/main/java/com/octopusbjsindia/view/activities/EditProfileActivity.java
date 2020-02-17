package com.octopusbjsindia.view.activities;

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
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.listeners.ProfileTaskListener;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.login.LoginInfo;
import com.octopusbjsindia.models.profile.Jurisdiction;
import com.octopusbjsindia.models.profile.JurisdictionLocation;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.models.profile.Organization;
import com.octopusbjsindia.models.profile.OrganizationProject;
import com.octopusbjsindia.models.profile.OrganizationRole;
import com.octopusbjsindia.models.profile.Project;
import com.octopusbjsindia.models.profile.UserLocation;
import com.octopusbjsindia.models.user.RoleData;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.presenter.EditProfileActivityPresenter;
import com.octopusbjsindia.utility.AppEvents;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("CanBeFinal")
public class EditProfileActivity extends BaseActivity implements ProfileTaskListener,
        View.OnClickListener, APIDataListener, CustomSpinnerListener {

    private EditText etUserFirstName;
    private EditText etUserMiddleName;
    private EditText etUserLastName;
    private EditText etUserBirthDate;
    private EditText etUserMobileNumber;
    private EditText etUserEmailId;
    private EditText etUserOrganization;
    private EditText etUserProject;
    private EditText etUserRole;
    private EditText etUserCountry;
    private EditText etUserState;
    private EditText etUserDistrict, etUserCity, etUserTaluka, etUserCluster, etUserVillage, etUserSchool;
    private ImageView imgUserProfilePic;
    RadioGroup radioGroup;
    private ImageView backButton;
    private Button btnProfileSubmit;
    private String userGender = Constants.Login.MALE;
    private ArrayList<CustomSpinnerObject> selectionOrgList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> selectionProjectList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> selectionRolesList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> customSpinnerCountries = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> customSpinnerStates = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> customSpinnerDistricts = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> customSpinnerCities = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> customSpinnerTalukas = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> customSpinnerVillages = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> customSpinnerClusters = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> customSpinnerSchools = new ArrayList<>();

    private List<Organization> organizations = new ArrayList<>();
    private List<OrganizationProject> projects = new ArrayList<>();
    private List<OrganizationRole> roles = new ArrayList<>();

    private ArrayList<JurisdictionType> selectedProjects = new ArrayList<>();
    private ArrayList<String> selectedRoles = new ArrayList<>();

    private ArrayList<JurisdictionType> selectedCountries = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedStates = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedDistricts = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedCities = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedTalukas = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedClusters = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedVillages = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedSchools = new ArrayList<>();
    private Uri outputUri;
    private Uri finalUri;
    private EditProfileActivityPresenter profilePresenter;
    private OrganizationRole selectedRole;
    private Organization selectedOrg;
    private boolean mImageUploaded;
    private String mUploadedImageUrl;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private final String TAG = EditProfileActivity.class.getName();
    private String currentPhotoPath = "";
    private String multiLocationLevel = "";
    private boolean isRoleSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profilePresenter = new EditProfileActivityPresenter(this);

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
        radioGroup = findViewById(R.id.user_gender_group);
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
        etUserOrganization = findViewById(R.id.etUserOrganization);
        etUserProject = findViewById(R.id.etUserProject);
        etUserRole = findViewById(R.id.etUserRole);
        etUserCountry = findViewById(R.id.etUserCountry);
        etUserState = findViewById(R.id.etUserState);
        etUserDistrict = findViewById(R.id.etUserDistrict);
        etUserCity = findViewById(R.id.etUserCity);
        etUserTaluka = findViewById(R.id.etUserTaluka);
        etUserCluster = findViewById(R.id.etUserCluster);
        etUserVillage = findViewById(R.id.etUserVillage);
        etUserSchool = findViewById(R.id.etUserSchool);
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

                    setEditModeUserData();
                    if (Util.isConnected(this)) {
                        profilePresenter.getUserProfile();
                        profilePresenter.getOrganizations();
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
        imgUserProfilePic.setOnClickListener(this);
        btnProfileSubmit.setOnClickListener(this);
        etUserOrganization.setOnClickListener(this);
        etUserProject.setOnClickListener(this);
        etUserRole.setOnClickListener(this);
        etUserCountry.setOnClickListener(this);
        etUserState.setOnClickListener(this);
        etUserDistrict.setOnClickListener(this);
        etUserCity.setOnClickListener(this);
        etUserTaluka.setOnClickListener(this);
        etUserCluster.setOnClickListener(this);
        etUserVillage.setOnClickListener(this);
        etUserSchool.setOnClickListener(this);
    }

    private void setEditModeUserData() {
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

        Organization orgObj = new Organization();
        orgObj.setId(userInfo.getOrgId());
        orgObj.setOrgName(userInfo.getOrgName());
        orgObj.setType(userInfo.getType());
        this.selectedOrg = orgObj;
        etUserOrganization.setText(selectedOrg.getOrgName());

        JurisdictionType project = new JurisdictionType();
        project.setId(userInfo.getProjectIds().get(0).getId());
        project.setName(userInfo.getProjectIds().get(0).getName());
        selectedProjects.add(project);
        etUserProject.setText(userInfo.getProjectIds().get(0).getName());

        multiLocationLevel = userInfo.getMultipleLocationLevel().getName();
        OrganizationRole role = new OrganizationRole();
        role.setId(userInfo.getRoleIds());
        role.setDisplayName(userInfo.getRoleNames());
        Project roleProject = new Project();
        roleProject.setJurisdictionTypeId(userInfo.getJurisdictionTypeId());
        role.setProject(roleProject);
        selectedRole = role;
        selectedRoles.add(selectedRole.getId());
        etUserRole.setText(selectedRole.getDisplayName());

        UserLocation userLocation = userInfo.getUserLocation();
        if (userLocation.getCountryId() != null && userLocation.getCountryId().size() > 0) {
            etUserCountry.setVisibility(View.VISIBLE);
            selectedCountries.clear();
            String countryNames = "";
            if (userInfo.getUserLocation() != null && userInfo.getUserLocation().getCountryId() != null
                    && userInfo.getUserLocation().
                    getCountryId().size() > 0) {
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getCountryId().size(); i++) {
                    JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getCountryId().get(i);
                    selectedCountries.add(j);
                    if (i == 0) {
                        countryNames = j.getName();
                    } else {
                        countryNames = countryNames + "," + j.getName();
                    }
                }
                etUserCountry.setText(userInfo.getUserLocation().getCountryId().get(0).getName());
            }
        }

        if (userLocation.getStateId() != null && userLocation.getStateId().size() > 0) {
            etUserState.setVisibility(View.VISIBLE);
            selectedStates.clear();
            String stateNames = "";
            if (userInfo.getUserLocation() != null && userInfo.getUserLocation().getStateId() != null
                    && userInfo.getUserLocation().getStateId().size() > 0) {
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getStateId().size(); i++) {
                    JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getStateId().get(i);
                    selectedStates.add(j);
                    if (i == 0) {
                        stateNames = j.getName();
                    } else {
                        stateNames = stateNames + "," + j.getName();
                    }
                }
                etUserState.setText(stateNames);
            }
        }

        if (userLocation.getDistrictIds() != null && userLocation.getDistrictIds().size() > 0) {
            etUserDistrict.setVisibility(View.VISIBLE);
            selectedDistricts.clear();
            String districtNames = "";
            if (userInfo.getUserLocation() != null && userInfo.getUserLocation().
                    getDistrictIds() != null && userInfo.getUserLocation().getDistrictIds().size() > 0) {
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size(); i++) {
                    JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(i);
                    selectedDistricts.add(j);
                    if (i == 0) {
                        districtNames = j.getName();
                    } else {
                        districtNames = districtNames + "," + j.getName();
                    }
                }
                etUserDistrict.setText(districtNames);
            }
        }

        if (userLocation.getCityIds() != null && userLocation.getCityIds().size() > 0) {
            etUserCity.setVisibility(View.VISIBLE);
            selectedCities.clear();
            String cityNames = "";
            if (userInfo.getUserLocation() != null && userInfo.getUserLocation().getCityIds() != null
                    && userInfo.getUserLocation().
                    getCityIds().size() > 0) {
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getCityIds().size(); i++) {
                    JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getCityIds().get(i);
                    selectedCities.add(j);
                    if (i == 0) {
                        cityNames = j.getName();
                    } else {
                        cityNames = cityNames + "," + j.getName();
                    }
                }
                etUserCity.setText(cityNames);
//                JurisdictionType selectedCity = new JurisdictionType();
//                selectedCity.setId(userInfo.getUserLocation().getCityIds().get(0).getId());
//                selectedCity.setName(userInfo.getUserLocation().getCityIds().get(0).getName());
//                selectedCities.add(selectedCity);
            }
        }

        if (userLocation.getTalukaIds() != null && userLocation.getTalukaIds().size() > 0) {
            etUserTaluka.setVisibility(View.VISIBLE);
            selectedTalukas.clear();
            String talukaNames = "";
            if (userInfo.getUserLocation() != null && userInfo.getUserLocation().getTalukaIds() != null
                    && userInfo.getUserLocation().
                    getTalukaIds().size() > 0) {
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size(); i++) {
                    JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i);
                    selectedTalukas.add(j);
                    if (i == 0) {
                        talukaNames = j.getName();
                    } else {
                        talukaNames = talukaNames + "," + j.getName();
                    }
                }
                etUserTaluka.setText(talukaNames);
            }
        }

        if (userLocation.getVillageIds() != null && userLocation.getVillageIds().size() > 0) {
            etUserVillage.setVisibility(View.VISIBLE);
            selectedVillages.clear();
            String villageNames = "";
            if (userInfo.getUserLocation() != null && userInfo.getUserLocation().getVillageIds() != null
                    && userInfo.getUserLocation().getVillageIds().size() > 0) {
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getVillageIds().size(); i++) {
                    JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getVillageIds().get(i);
                    selectedVillages.add(j);
                    if (i == 0) {
                        villageNames = j.getName();
                    } else {
                        villageNames = villageNames + "," + j.getName();
                    }
                }
                etUserVillage.setText(villageNames);
            }
        }

        if (userLocation.getClusterIds() != null && userLocation.getClusterIds().size() > 0) {
            etUserCluster.setVisibility(View.VISIBLE);
            selectedClusters.clear();
            String clusterNames = "";
            if (userInfo.getUserLocation() != null && userInfo.getUserLocation().getClusterIds() != null
                    && userInfo.getUserLocation().getClusterIds().size() > 0) {
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getClusterIds().size(); i++) {
                    JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getClusterIds().get(i);
                    selectedClusters.add(j);
                    if (i == 0) {
                        clusterNames = j.getName();
                    } else {
                        clusterNames = clusterNames + "," + j.getName();
                    }
                }
                etUserCluster.setText(clusterNames);
            }
        }

        if (userLocation.getSchoolIds() != null && userLocation.getSchoolIds().size() > 0) {
            etUserSchool.setVisibility(View.VISIBLE);
            selectedSchools.clear();
            String schoolNames = "";
            if (userInfo.getUserLocation() != null && userInfo.getUserLocation().getSchoolIds() != null
                    && userInfo.getUserLocation().getSchoolIds().size() > 0) {
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getSchoolIds().size(); i++) {
                    JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getSchoolIds().get(i);
                    selectedSchools.add(j);
                    if (i == 0) {
                        schoolNames = j.getName();
                    } else {
                        schoolNames = schoolNames + "," + j.getName();
                    }
                }
                etUserSchool.setText(schoolNames);
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
                CustomSpinnerDialogClass cdd = new CustomSpinnerDialogClass(this, this, "Select Organization",
                        selectionOrgList, false);
                cdd.show();
                cdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.etUserProject:
                if (selectionProjectList.size() > 0) {
                    CustomSpinnerDialogClass cddProject = new CustomSpinnerDialogClass(this, this, "Select Project",
                            selectionProjectList, false);
                    cddProject.show();
                    cddProject.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (selectedOrg != null && !TextUtils.isEmpty(selectedOrg.getId())) {
                        profilePresenter.getOrganizationProjects(this.selectedOrg.getId());
                    } else {
                        Toast.makeText(this, getString(R.string.msg_select_org), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.etUserRole:
                if (selectionRolesList.size() > 0) {
                    CustomSpinnerDialogClass cddProject = new CustomSpinnerDialogClass(this, this, "Select Role",
                            selectionRolesList, false);
                    cddProject.show();
                    cddProject.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (selectedProjects != null && selectedProjects.size() > 0 &&
                            !TextUtils.isEmpty(selectedProjects.get(0).getId())) {
                        profilePresenter.getOrganizationRoles(this.selectedOrg.getId(),
                                selectedProjects.get(0).getId());
                    } else {
                        Toast.makeText(this, getString(R.string.msg_select_project), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.etUserCountry:
                if (customSpinnerCountries.size() > 0) {
                    //String level = "";
                    if (isRoleSelected) {
                        Jurisdiction jurisdiction = selectedRole.getProject().getJurisdictions().
                                get(selectedRole.getProject().getJurisdictions().size() - 1);
                        multiLocationLevel = jurisdiction.getLevelName();
                    }
                    if (multiLocationLevel.equals(Constants.JurisdictionLevelName.COUNTRY_LEVEL)) {
                        CustomSpinnerDialogClass csdCountry = new CustomSpinnerDialogClass(this, this,
                                "Select Country", customSpinnerCountries,
                                true);
                        csdCountry.show();
                        csdCountry.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                    } else {
                        CustomSpinnerDialogClass csdCountry = new CustomSpinnerDialogClass(this, this,
                                "Select Country", customSpinnerCountries,
                                false);
                        csdCountry.show();
                        csdCountry.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                    }
                } else {
                    if (selectedProjects != null && selectedProjects.size() > 0 &&
                            !TextUtils.isEmpty(selectedProjects.get(0).getId())) {
                        profilePresenter.getProfileLocationData("",
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.COUNTRY_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    } else {
                        Toast.makeText(this, getString(R.string.msg_select_project), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.etUserState:
                if (customSpinnerStates.size() > 0) {
                    if (isRoleSelected) {
                        Jurisdiction jurisdiction = selectedRole.getProject().getJurisdictions().
                                get(selectedRole.getProject().getJurisdictions().size() - 1);
                        multiLocationLevel = jurisdiction.getLevelName();
                    }
                    if (multiLocationLevel.equals(Constants.JurisdictionLevelName.STATE_LEVEL)) {
                        CustomSpinnerDialogClass csdState = new CustomSpinnerDialogClass(this, this,
                                "Select State", customSpinnerStates,
                                true);
                        csdState.show();
                        csdState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                    } else {
                        CustomSpinnerDialogClass csdState = new CustomSpinnerDialogClass(this, this,
                                "Select State", customSpinnerStates,
                                false);
                        csdState.show();
                        csdState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                    }
                } else {
                    if (selectedCountries.size() > 0 && selectedCountries.size() > 0 &&
                            selectedCountries.get(0).getId().length() > 0) {
                        String selectedCountryIds = "";
                        for (int i = 0; i < selectedCountries.size(); i++) {
                            JurisdictionType j = selectedCountries.get(i);
                            if (i == 0) {
                                selectedCountryIds = j.getId();
                            } else {
                                selectedCountryIds = selectedCountryIds + "," + j.getId();
                            }
                        }
                        profilePresenter.getProfileLocationData(selectedCountryIds,
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.STATE_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    } else {
                        if (etUserCountry.getVisibility() == View.VISIBLE) {
                            Toast.makeText(this, getString(R.string.msg_select_country), Toast.LENGTH_LONG).show();
                        } else {
                            profilePresenter.getProfileLocationData("",
                                    selectedRole.getProject().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.STATE_LEVEL, selectedOrg.getId(),
                                    selectedProjects.get(0).getId(), selectedRole.getId());
                        }
                    }
                }
                break;
            case R.id.etUserCity:
                if (customSpinnerCities.size() > 0) {
                    if (isRoleSelected) {
                        Jurisdiction jurisdiction = selectedRole.getProject().getJurisdictions().
                                get(selectedRole.getProject().getJurisdictions().size() - 1);
                        multiLocationLevel = jurisdiction.getLevelName();
                    }
                    if (multiLocationLevel.equals(Constants.JurisdictionLevelName.CITY_LEVEL)) {
                        CustomSpinnerDialogClass csdState = new CustomSpinnerDialogClass(this, this,
                                "Select City", customSpinnerCities,
                                true);
                        csdState.show();
                        csdState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                    } else {
                        CustomSpinnerDialogClass csdState = new CustomSpinnerDialogClass(this, this,
                                "Select City", customSpinnerCities,
                                false);
                        csdState.show();
                        csdState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                    }
                } else {
                    if (selectedStates != null && selectedStates.size() > 0 &&
                            !TextUtils.isEmpty(selectedStates.get(0).getId())) {
                        String selectedStateIds = "";
                        for (int i = 0; i < selectedStates.size(); i++) {
                            JurisdictionType j = selectedStates.get(i);
                            if (i == 0) {
                                selectedStateIds = j.getId();
                            } else {
                                selectedStateIds = selectedStateIds + "," + j.getId();
                            }
                        }
                        profilePresenter.getProfileLocationData(selectedStateIds,
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.CITY_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    } else {
                        Toast.makeText(this, getString(R.string.msg_select_state), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.etUserDistrict:
                if (customSpinnerDistricts.size() > 0) {
                    if (isRoleSelected) {
                        Jurisdiction jurisdiction = selectedRole.getProject().getJurisdictions().
                                get(selectedRole.getProject().getJurisdictions().size() - 1);
                        multiLocationLevel = jurisdiction.getLevelName();
                    }
                    if (multiLocationLevel.equals(Constants.JurisdictionLevelName.DISTRICT_LEVEL)) {
                        CustomSpinnerDialogClass csdState = new CustomSpinnerDialogClass(this, this,
                                "Select District", customSpinnerDistricts,
                                true);
                        csdState.show();
                        csdState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                    } else {
                        CustomSpinnerDialogClass csdState = new CustomSpinnerDialogClass(this, this,
                                "Select District", customSpinnerDistricts,
                                false);
                        csdState.show();
                        csdState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                    }
                } else {
                    if (selectedStates != null && selectedStates.size() > 0 &&
                            !TextUtils.isEmpty(selectedStates.get(0).getId())) {
                        String selectedStateIds = "";
                        for (int i = 0; i < selectedStates.size(); i++) {
                            JurisdictionType j = selectedStates.get(i);
                            if (i == 0) {
                                selectedStateIds = j.getId();
                            } else {
                                selectedStateIds = selectedStateIds + "," + j.getId();
                            }
                        }
                        profilePresenter.getProfileLocationData(selectedStateIds,
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.DISTRICT_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    } else {
                        Toast.makeText(this, getString(R.string.msg_select_state), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.etUserTaluka:
                if (customSpinnerTalukas.size() > 0) {
                    if (isRoleSelected) {
                        Jurisdiction jurisdiction = selectedRole.getProject().getJurisdictions().
                                get(selectedRole.getProject().getJurisdictions().size() - 1);
                        multiLocationLevel = jurisdiction.getLevelName();
                    }
                    if (multiLocationLevel.equals(Constants.JurisdictionLevelName.TALUKA_LEVEL)) {
                        CustomSpinnerDialogClass csdState = new CustomSpinnerDialogClass(this, this,
                                "Select Taluka", customSpinnerTalukas,
                                true);
                        csdState.show();
                        csdState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                    } else {
                        CustomSpinnerDialogClass csdState = new CustomSpinnerDialogClass(this, this,
                                "Select Taluka", customSpinnerTalukas,
                                false);
                        csdState.show();
                        csdState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                    }
                } else {
                    if (etUserCity.getVisibility() == View.VISIBLE) {
                        if (selectedCities != null && selectedCities.size() > 0 &&
                                !TextUtils.isEmpty(selectedCities.get(0).getId())) {
                            String selectedCityIds = "";
                            for (int i = 0; i < selectedCities.size(); i++) {
                                JurisdictionType j = selectedCities.get(i);
                                if (i == 0) {
                                    selectedCityIds = j.getId();
                                } else {
                                    selectedCityIds = selectedCityIds + "," + j.getId();
                                }
                            }
                            profilePresenter.getProfileLocationData(selectedCityIds,
                                    selectedRole.getProject().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.TALUKA_LEVEL, selectedOrg.getId(),
                                    selectedProjects.get(0).getId(), selectedRole.getId());
                        } else {
                            Toast.makeText(this, getString(R.string.msg_select_city), Toast.LENGTH_LONG).show();
                        }
                    } else if (etUserDistrict.getVisibility() == View.VISIBLE) {
                        if (selectedDistricts != null && selectedDistricts.size() > 0 &&
                                !TextUtils.isEmpty(selectedDistricts.get(0).getId())) {
                            String selectedDistrictIds = "";
                            for (int i = 0; i < selectedDistricts.size(); i++) {
                                JurisdictionType j = selectedDistricts.get(i);
                                if (i == 0) {
                                    selectedDistrictIds = j.getId();
                                } else {
                                    selectedDistrictIds = selectedDistrictIds + "," + j.getId();
                                }
                            }
                            profilePresenter.getProfileLocationData(selectedDistrictIds,
                                    selectedRole.getProject().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.TALUKA_LEVEL, selectedOrg.getId(),
                                    selectedProjects.get(0).getId(), selectedRole.getId());
                        } else {
                            Toast.makeText(this, getString(R.string.msg_select_district), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
            case R.id.etUserVillage:
                if (customSpinnerVillages.size() > 0) {
                    if (isRoleSelected) {
                        Jurisdiction jurisdiction = selectedRole.getProject().getJurisdictions().
                                get(selectedRole.getProject().getJurisdictions().size() - 1);
                        multiLocationLevel = jurisdiction.getLevelName();
                    }
                    if (multiLocationLevel.equals(Constants.JurisdictionLevelName.VILLAGE_LEVEL)) {
                        CustomSpinnerDialogClass csdVillage = new CustomSpinnerDialogClass(this, this,
                                "Select Village", customSpinnerVillages,
                                true);
                        csdVillage.show();
                        csdVillage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                    } else {
                        CustomSpinnerDialogClass csdVillage = new CustomSpinnerDialogClass(this, this,
                                "Select Village", customSpinnerVillages,
                                false);
                        csdVillage.show();
                        csdVillage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                    }
                } else {
                    if (etUserCluster.getVisibility() == View.VISIBLE) {
                        if (selectedClusters != null && selectedClusters.size() > 0 &&
                                !TextUtils.isEmpty(selectedClusters.get(0).getId())) {
                            String selectedClusterIds = "";
                            for (int i = 0; i < selectedClusters.size(); i++) {
                                JurisdictionType j = selectedClusters.get(i);
                                if (i == 0) {
                                    selectedClusterIds = j.getId();
                                } else {
                                    selectedClusterIds = selectedClusterIds + "," + j.getId();
                                }
                            }
                            profilePresenter.getProfileLocationData(selectedClusterIds,
                                    selectedRole.getProject().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.VILLAGE_LEVEL, selectedOrg.getId(),
                                    selectedProjects.get(0).getId(), selectedRole.getId());
                        } else {
                            Toast.makeText(this, getString(R.string.msg_select_cluster), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (selectedTalukas != null && selectedTalukas.size() > 0 &&
                                !TextUtils.isEmpty(selectedTalukas.get(0).getId())) {
                            String selectedTalukaIds = "";
                            for (int i = 0; i < selectedTalukas.size(); i++) {
                                JurisdictionType j = selectedTalukas.get(i);
                                if (i == 0) {
                                    selectedTalukaIds = j.getId();
                                } else {
                                    selectedTalukaIds = selectedTalukaIds + "," + j.getId();
                                }
                            }
                            profilePresenter.getProfileLocationData(selectedTalukaIds,
                                    selectedRole.getProject().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.VILLAGE_LEVEL, selectedOrg.getId(),
                                    selectedProjects.get(0).getId(), selectedRole.getId());
                        } else {
                            Toast.makeText(this, getString(R.string.msg_select_taluka), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
            case R.id.etUserCluster:
                if (customSpinnerClusters.size() > 0) {
                    if (isRoleSelected) {
                        Jurisdiction jurisdiction = selectedRole.getProject().getJurisdictions().
                                get(selectedRole.getProject().getJurisdictions().size() - 1);
                        multiLocationLevel = jurisdiction.getLevelName();
                    }
                    if (multiLocationLevel.equals(Constants.JurisdictionLevelName.CLUSTER_LEVEL)) {
                        CustomSpinnerDialogClass csdCluster = new CustomSpinnerDialogClass(this, this,
                                "Select Cluster", customSpinnerClusters,
                                true);
                        csdCluster.show();
                        csdCluster.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                    } else {
                        CustomSpinnerDialogClass csdCluster = new CustomSpinnerDialogClass(this, this,
                                "Select Cluster", customSpinnerClusters,
                                false);
                        csdCluster.show();
                        csdCluster.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                    }
                } else {
                    if (selectedTalukas != null && selectedTalukas.size() > 0 &&
                            !TextUtils.isEmpty(selectedTalukas.get(0).getId())) {
                        String selectedTalukaIds = "";
                        for (int i = 0; i < selectedTalukas.size(); i++) {
                            JurisdictionType j = selectedTalukas.get(i);
                            if (i == 0) {
                                selectedTalukaIds = j.getId();
                            } else {
                                selectedTalukaIds = selectedTalukaIds + "," + j.getId();
                            }
                        }
                        profilePresenter.getProfileLocationData(selectedTalukaIds,
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.CLUSTER_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    } else {
                        Toast.makeText(this, getString(R.string.msg_select_taluka), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.etUserSchool:
                if (customSpinnerSchools.size() > 0) {
                    if (isRoleSelected) {
                        Jurisdiction jurisdiction = selectedRole.getProject().getJurisdictions().
                                get(selectedRole.getProject().getJurisdictions().size() - 1);
                        multiLocationLevel = jurisdiction.getLevelName();
                    }
                    if (multiLocationLevel.equals(Constants.JurisdictionLevelName.SCHOOL_LEVEL)) {
                        CustomSpinnerDialogClass csdSchool = new CustomSpinnerDialogClass(this, this,
                                "Select School", customSpinnerSchools,
                                true);
                        csdSchool.show();
                        csdSchool.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                    } else {
                        CustomSpinnerDialogClass csdSchool = new CustomSpinnerDialogClass(this, this,
                                "Select School", customSpinnerSchools,
                                false);
                        csdSchool.show();
                        csdSchool.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                    }
                } else {
                    if (selectedVillages != null && selectedVillages.size() > 0 &&
                            !TextUtils.isEmpty(selectedVillages.get(0).getId())) {
                        String selectedVillageIds = "";
                        for (int i = 0; i < selectedVillages.size(); i++) {
                            JurisdictionType j = selectedVillages.get(i);
                            if (i == 0) {
                                selectedVillageIds = j.getId();
                            } else {
                                selectedVillageIds = selectedVillageIds + "," + j.getId();
                            }
                        }
                        profilePresenter.getProfileLocationData(selectedVillageIds,
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.SCHOOL_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    } else {
                        Toast.makeText(this, getString(R.string.msg_select_village), Toast.LENGTH_LONG).show();
                    }
                }
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

            RoleData role = new RoleData();
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
                //userLocation.setCountryId(s);
            }
            userLocation.setCountryId(s);

            s = new ArrayList<>();
            for (JurisdictionType state : selectedStates) {
                JurisdictionType stateObj = new JurisdictionType();
                stateObj.setId(state.getId());
                stateObj.setName(state.getName());
                s.add(stateObj);
            }
            userLocation.setStateId(s);

            s = new ArrayList<>();
            for (JurisdictionType district : selectedDistricts) {
                JurisdictionType districtObj = new JurisdictionType();
                districtObj.setId(district.getId());
                districtObj.setName(district.getName());
                s.add(districtObj);
            }
            userLocation.setDistrictIds(s);

            s = new ArrayList<>();
            for (JurisdictionType city : selectedCities) {
                JurisdictionType cityObj = new JurisdictionType();
                cityObj.setId(city.getId());
                cityObj.setName(city.getName());
                s.add(cityObj);
            }
            userLocation.setCityIds(s);

            s = new ArrayList<>();
            for (JurisdictionType taluka : selectedTalukas) {
                JurisdictionType talukaObj = new JurisdictionType();
                talukaObj.setId(taluka.getId());
                talukaObj.setName(taluka.getName());
                s.add(talukaObj);
            }
            userLocation.setTalukaIds(s);

            s = new ArrayList<>();
            for (JurisdictionType cluster : selectedClusters) {
                JurisdictionType clusterObj = new JurisdictionType();
                clusterObj.setId(cluster.getId());
                clusterObj.setName(cluster.getName());
                s.add(clusterObj);
            }
            userLocation.setClusterIds(s);

            s = new ArrayList<>();
            for (JurisdictionType village : selectedVillages) {
                JurisdictionType villageObj = new JurisdictionType();
                villageObj.setId(village.getId());
                villageObj.setName(village.getName());
                s.add(villageObj);
            }
            userLocation.setVillageIds(s);

            s = new ArrayList<>();
            for (JurisdictionType school : selectedSchools) {
                JurisdictionType schoolObj = new JurisdictionType();
                schoolObj.setId(school.getId());
                schoolObj.setName(school.getName());
                s.add(schoolObj);
            }
            userLocation.setSchoolIds(s);

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
        } else if (etUserBirthDate.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_birth_date);
        } else if (etUserEmailId.getText().toString().trim().length() != 0 &&
                !Patterns.EMAIL_ADDRESS.matcher(etUserEmailId.getText().toString().trim()).matches()) {
            msg = getResources().getString(R.string.msg_enter_valid_email_id);
        } else if (TextUtils.isEmpty(userGender)) {
            msg = getString(R.string.msg_select_gender);
        } else if (selectedOrg == null || TextUtils.isEmpty(selectedOrg.getId())) {
            msg = getString(R.string.msg_select_org);
        } else if (selectedProjects == null || selectedProjects.size() == 0) {
            msg = getString(R.string.msg_select_project);
        } else if (selectedRoles == null || selectedRoles.size() == 0) {
            msg = getString(R.string.msg_select_role);
        } else if ((etUserCountry.getVisibility() == View.VISIBLE) &&
                (selectedCountries == null || selectedCountries.size() == 0)) {
            msg = getString(R.string.msg_select_country);
        } else if ((etUserState.getVisibility() == View.VISIBLE) &&
                (selectedStates == null || selectedStates.size() == 0)) {
            msg = getString(R.string.msg_select_state);
        } else if ((etUserDistrict.getVisibility() == View.VISIBLE) &&
                (selectedDistricts == null || selectedDistricts.size() == 0)) {
            msg = getString(R.string.msg_select_district);
        } else if ((etUserCity.getVisibility() == View.VISIBLE) &&
                (selectedCities == null || selectedCities.size() == 0)) {
            msg = getString(R.string.msg_select_city);
        } else if ((etUserTaluka.getVisibility() == View.VISIBLE) &&
                (selectedTalukas == null || selectedTalukas.size() == 0)) {
            msg = getString(R.string.msg_select_taluka);
        } else if ((etUserCluster.getVisibility() == View.VISIBLE) &&
                (selectedClusters == null || selectedClusters.size() == 0)) {
            msg = getString(R.string.msg_select_cluster);
        } else if ((etUserVillage.getVisibility() == View.VISIBLE) &&
                (selectedVillages == null || selectedVillages.size() == 0)) {
            msg = getString(R.string.msg_select_village);
        } else if ((etUserSchool.getVisibility() == View.VISIBLE) &&
                (selectedSchools == null || selectedSchools.size() == 0)) {
            msg = getString(R.string.msg_select_school);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.CHOOSE_IMAGE_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
            try {
                finalUri = Uri.fromFile(new File(currentPhotoPath));
                Crop.of(finalUri, finalUri).start(this);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    getImageFile();
                    outputUri = data.getData();
                    finalUri = Uri.fromFile(new File(currentPhotoPath));
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
        }
    }

    private void hideJurisdictionLevel() {
        etUserCountry.setVisibility(View.GONE);
        selectedCountries.clear();

        etUserState.setVisibility(View.GONE);
        selectedStates.clear();

        etUserDistrict.setVisibility(View.GONE);
        selectedDistricts.clear();

        etUserCity.setVisibility(View.GONE);
        selectedCities.clear();

        etUserTaluka.setVisibility(View.GONE);
        selectedTalukas.clear();

        etUserCluster.setVisibility(View.GONE);
        selectedClusters.clear();

        etUserVillage.setVisibility(View.GONE);
        selectedVillages.clear();

        etUserSchool.setVisibility(View.GONE);
        selectedSchools.clear();
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
    public void onFailureListener(String requestID, String message) {
        Util.showToast(message, this);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        Util.showToast(error.getMessage(), this);
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
        selectionOrgList.clear();
        for (int i = 0; i < organizations.size(); i++) {
            CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
            customSpinnerObject.set_id(organizations.get(i).getId());
            customSpinnerObject.setName(organizations.get(i).getOrgName());
            selectionOrgList.add(customSpinnerObject);
        }
    }

    @Override
    public void showOrganizationProjects(List<OrganizationProject> organizationProjects) {
        if (organizationProjects != null && !organizationProjects.isEmpty()) {

            this.projects.clear();
            this.projects.addAll(organizationProjects);
            selectionProjectList.clear();
            for (int i = 0; i < organizationProjects.size(); i++) {
                CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                customSpinnerObject.set_id(organizationProjects.get(i).getId());
                customSpinnerObject.setName(organizationProjects.get(i).getOrgProjectName());
                selectionProjectList.add(customSpinnerObject);
            }
        }
    }

    @Override
    public void showOrganizationRoles(List<OrganizationRole> organizationRoles) {
        if (organizationRoles != null && !organizationRoles.isEmpty()) {

            this.roles.clear();
            this.roles.addAll(organizationRoles);
            selectionRolesList.clear();
            for (int i = 0; i < organizationRoles.size(); i++) {
                CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                customSpinnerObject.set_id(organizationRoles.get(i).getId());
                customSpinnerObject.setName(organizationRoles.get(i).getDisplayName());
                selectionRolesList.add(customSpinnerObject);
            }
        }
    }

    private void setJurisdictionLevel(String level) {
        switch (level) {
            case Constants.JurisdictionLevelName.COUNTRY_LEVEL:
                etUserCountry.setVisibility(View.VISIBLE);
                selectedCountries.clear();
                break;

            case Constants.JurisdictionLevelName.STATE_LEVEL:
                etUserState.setVisibility(View.VISIBLE);
                selectedStates.clear();
                break;

            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                etUserDistrict.setVisibility(View.VISIBLE);
                selectedDistricts.clear();
                break;

            case Constants.JurisdictionLevelName.CITY_LEVEL:
                etUserCity.setVisibility(View.VISIBLE);
                selectedCities.clear();
                break;

            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                etUserTaluka.setVisibility(View.VISIBLE);
                selectedTalukas.clear();
                break;

            case Constants.JurisdictionLevelName.VILLAGE_LEVEL:
                etUserVillage.setVisibility(View.VISIBLE);
                selectedVillages.clear();
                break;

            case Constants.JurisdictionLevelName.CLUSTER_LEVEL:
                etUserCluster.setVisibility(View.VISIBLE);
                selectedClusters.clear();
                break;

            case Constants.JurisdictionLevelName.SCHOOL_LEVEL:
                etUserSchool.setVisibility(View.VISIBLE);
                selectedSchools.clear();
                break;
        }
    }

    @Override
    public void showJurisdictionLevel(List<JurisdictionLocation> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.COUNTRY_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    customSpinnerCountries.clear();
                    List<String> countryNames = new ArrayList<>();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        countryNames.add(location.getName());
                        CustomSpinnerObject country = new CustomSpinnerObject();
                        country.set_id(location.getId());
                        country.setName(location.getName());
                        country.setSelected(false);
                        this.customSpinnerCountries.add(country);
                    }
                }
                break;
            case Constants.JurisdictionLevelName.STATE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    customSpinnerStates.clear();
                    List<String> stateNames = new ArrayList<>();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        stateNames.add(location.getName());
                        CustomSpinnerObject state = new CustomSpinnerObject();
                        state.set_id(location.getId());
                        state.setName(location.getName());
                        state.setSelected(false);
                        this.customSpinnerStates.add(state);
                    }
                }
                break;

            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    List<String> districts = new ArrayList<>();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        districts.add(location.getName());
                        CustomSpinnerObject district = new CustomSpinnerObject();
                        district.set_id(location.getId());
                        district.setName(location.getName());
                        district.setSelected(false);
                        this.customSpinnerDistricts.add(district);
                    }
                }
                break;

            case Constants.JurisdictionLevelName.CITY_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    List<String> cities = new ArrayList<>();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        cities.add(location.getName());
                        CustomSpinnerObject city = new CustomSpinnerObject();
                        city.set_id(location.getId());
                        city.setName(location.getName());
                        city.setSelected(false);
                        this.customSpinnerCities.add(city);
                    }
                }
                break;

            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    List<String> talukas = new ArrayList<>();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        talukas.add(location.getName());
                        CustomSpinnerObject taluka = new CustomSpinnerObject();
                        taluka.set_id(location.getId());
                        taluka.setName(location.getName());
                        taluka.setSelected(false);
                        this.customSpinnerTalukas.add(taluka);
                    }
                }
                break;

            case Constants.JurisdictionLevelName.VILLAGE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    List<String> villages = new ArrayList<>();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        villages.add(location.getName());
                        CustomSpinnerObject village = new CustomSpinnerObject();
                        village.set_id(location.getId());
                        village.setName(location.getName());
                        village.setSelected(false);
                        this.customSpinnerVillages.add(village);
                    }
                }
                break;

            case Constants.JurisdictionLevelName.CLUSTER_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    List<String> clusters = new ArrayList<>();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        clusters.add(location.getName());
                        CustomSpinnerObject cluster = new CustomSpinnerObject();
                        cluster.set_id(location.getId());
                        cluster.setName(location.getName());
                        cluster.setSelected(false);
                        this.customSpinnerClusters.add(cluster);
                    }
                }
                break;
            case Constants.JurisdictionLevelName.SCHOOL_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    List<String> schools = new ArrayList<>();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        schools.add(location.getName());
                        CustomSpinnerObject school = new CustomSpinnerObject();
                        school.set_id(location.getId());
                        school.setName(location.getName());
                        school.setSelected(false);
                        this.customSpinnerSchools.add(school);
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
    public void onCustomSpinnerSelection(String type) {
        if (type.equals("Select Organization")) {
            int selectedPosition = -1;
            String selectedOrgName = "";
            for (CustomSpinnerObject customSpinnerObject : selectionOrgList) {
                if (customSpinnerObject.isSelected()) {
                    selectedOrgName = customSpinnerObject.getName();
                    selectedPosition = selectionOrgList.indexOf(customSpinnerObject);
                    break;
                }
            }
            etUserOrganization.setText(selectedOrgName);
            // clear other dependent fields
            hideJurisdictionLevel();
            selectedProjects.clear();
            selectionProjectList.clear();
            etUserProject.setText("");
            selectedRoles.clear();
            selectionRolesList.clear();
            etUserRole.setText("");
            selectedCountries.clear();
            customSpinnerCountries.clear();
            etUserCountry.setText("");
            selectedStates.clear();
            customSpinnerStates.clear();
            etUserState.setText("");
            selectedDistricts.clear();
            customSpinnerDistricts.clear();
            etUserDistrict.setText("");
            selectedCities.clear();
            customSpinnerCities.clear();
            etUserCity.setText("");
            selectedClusters.clear();
            customSpinnerClusters.clear();
            etUserCluster.setText("");
            selectedTalukas.clear();
            customSpinnerTalukas.clear();
            etUserTaluka.setText("");
            selectedVillages.clear();
            customSpinnerVillages.clear();
            etUserVillage.setText("");
            selectedSchools.clear();
            customSpinnerSchools.clear();
            etUserSchool.setText("");

            if (Util.isConnected(this)) {
                if (organizations != null && !organizations.isEmpty() && organizations.get(selectedPosition) != null
                        && !TextUtils.isEmpty(organizations.get(selectedPosition).getId())) {
                    this.selectedOrg = organizations.get(selectedPosition);
                    profilePresenter.getOrganizationProjects(this.selectedOrg.getId());
                }
            }
        } else if (type.equals("Select Project")) {
            int selectedPosition = -1;
            String selectedProjectName = "";//, selectedOrgId;
            for (CustomSpinnerObject customSpinnerObject : selectionProjectList) {
                if (customSpinnerObject.isSelected()) {
                    selectedProjectName = customSpinnerObject.getName();
                    selectedPosition = selectionProjectList.indexOf(customSpinnerObject);
                    break;
                }
            }
            etUserProject.setText(selectedProjectName);
            // clear other dependent fields
            hideJurisdictionLevel();
            selectedRoles.clear();
            selectionRolesList.clear();
            etUserRole.setText("");
            selectedCountries.clear();
            customSpinnerCountries.clear();
            etUserCountry.setText("");
            selectedStates.clear();
            customSpinnerStates.clear();
            etUserState.setText("");
            selectedDistricts.clear();
            customSpinnerDistricts.clear();
            etUserDistrict.setText("");
            selectedCities.clear();
            customSpinnerCities.clear();
            etUserCity.setText("");
            selectedClusters.clear();
            customSpinnerClusters.clear();
            etUserCluster.setText("");
            selectedTalukas.clear();
            customSpinnerTalukas.clear();
            etUserTaluka.setText("");
            selectedVillages.clear();
            customSpinnerVillages.clear();
            etUserVillage.setText("");
            selectedSchools.clear();
            customSpinnerSchools.clear();
            etUserSchool.setText("");
            // clear other dependent fields
            if (Util.isConnected(this)) {
                selectedProjects.clear();
                if (projects != null && !projects.isEmpty() && projects.get(selectedPosition) != null
                        && !TextUtils.isEmpty(projects.get(selectedPosition).getId())) {
                    JurisdictionType project = new JurisdictionType();
                    project.setId(projects.get(selectedPosition).getId());
                    project.setName(projects.get(selectedPosition).getOrgProjectName());
                    selectedProjects.add(project);
                    profilePresenter.getOrganizationRoles(this.selectedOrg.getId(),
                            projects.get(selectedPosition).getId());
                }
            }
        } else if (type.equals("Select Role")) {
            int selectedPosition = -1;
            String selectedRoleName = "";//, selectedOrgId;
            for (CustomSpinnerObject customSpinnerObject : selectionRolesList) {
                if (customSpinnerObject.isSelected()) {
                    selectedRoleName = customSpinnerObject.getName();
                    selectedPosition = selectionRolesList.indexOf(customSpinnerObject);
                    break;
                }
            }
            etUserRole.setText(selectedRoleName);
            // clear other dependent fields
            selectedCountries.clear();
            customSpinnerCountries.clear();
            etUserCountry.setText("");
            selectedStates.clear();
            customSpinnerStates.clear();
            etUserState.setText("");
            selectedDistricts.clear();
            customSpinnerDistricts.clear();
            etUserDistrict.setText("");
            selectedCities.clear();
            customSpinnerCities.clear();
            etUserCity.setText("");
            selectedClusters.clear();
            customSpinnerClusters.clear();
            etUserCluster.setText("");
            selectedTalukas.clear();
            customSpinnerTalukas.clear();
            etUserTaluka.setText("");
            selectedVillages.clear();
            customSpinnerVillages.clear();
            etUserVillage.setText("");
            selectedSchools.clear();
            customSpinnerSchools.clear();
            etUserSchool.setText("");
            if (Util.isConnected(this)) {
                selectedRoles.clear();
                if (roles != null && !roles.isEmpty() && roles.get(selectedPosition) != null
                        && !TextUtils.isEmpty(roles.get(selectedPosition).getId())) {
                    selectedRole = roles.get(selectedPosition);
                    selectedRoles.add(selectedRole.getId());
                    isRoleSelected = true;
                }
            }
            if (selectedRole.getProject() != null) {
                List<Jurisdiction> jurisdictions = selectedRole.getProject().getJurisdictions();
                if (jurisdictions != null && jurisdictions.size() > 0) {
                    hideJurisdictionLevel();
                    for (Jurisdiction j : jurisdictions) {
                        setJurisdictionLevel(j.getLevelName());
                    }
                    if (Util.isConnected(this)) {
                        profilePresenter.getProfileLocationData("", selectedRole.getProject().
                                        getJurisdictionTypeId(), jurisdictions.get(0).getLevelName(), selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    } else {
                        Util.showToast(getResources().getString(R.string.no_data_available), this);
                    }
                }
            }
        } else if (type.equals("Select Country")) {
            selectedCountries.clear();
            for (CustomSpinnerObject country : customSpinnerCountries) {
                if (country.isSelected()) {
                    JurisdictionType selectedCountry = new JurisdictionType();
                    selectedCountry.setName(country.getName());
                    selectedCountry.setId(country.get_id());
                    selectedCountries.add(selectedCountry);
                }
            }
            // clear other dependent fields
            selectedStates.clear();
            customSpinnerStates.clear();
            etUserState.setText("");
            selectedDistricts.clear();
            customSpinnerDistricts.clear();
            etUserDistrict.setText("");
            selectedCities.clear();
            customSpinnerCities.clear();
            etUserCity.setText("");
            selectedClusters.clear();
            customSpinnerClusters.clear();
            etUserCluster.setText("");
            selectedTalukas.clear();
            customSpinnerTalukas.clear();
            etUserTaluka.setText("");
            selectedVillages.clear();
            customSpinnerVillages.clear();
            etUserVillage.setText("");
            selectedSchools.clear();
            customSpinnerSchools.clear();
            etUserSchool.setText("");

            String selectedCountryIds = "";
            String selectedCountryNames = "";
            for (int i = 0; i < selectedCountries.size(); i++) {
                JurisdictionType j = selectedCountries.get(i);
                if (i == 0) {
                    selectedCountryIds = j.getId();
                    selectedCountryNames = j.getName();
                } else {
                    selectedCountryIds = selectedCountryIds + "," + j.getId();
                    selectedCountryNames = selectedCountryNames + "," + j.getName();
                }
            }
            etUserCountry.setText(selectedCountryNames);

            if (selectedCountries != null && selectedCountries.size() > 0) {
                if (Util.isConnected(this)) {
                    if (etUserState.getVisibility() == View.VISIBLE) {
                        profilePresenter.getProfileLocationData(selectedCountryIds,
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.STATE_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    }
                }
            }
        } else if (type.equals("Select State")) {
            selectedStates.clear();
            for (CustomSpinnerObject state : customSpinnerStates) {
                if (state.isSelected()) {
                    JurisdictionType selectedState = new JurisdictionType();
                    selectedState.setName(state.getName());
                    selectedState.setId(state.get_id());
                    selectedStates.add(selectedState);
                }
            }
            // clear other dependent fields
            selectedDistricts.clear();
            customSpinnerDistricts.clear();
            etUserDistrict.setText("");
            selectedCities.clear();
            customSpinnerCities.clear();
            etUserCity.setText("");
            selectedClusters.clear();
            customSpinnerClusters.clear();
            etUserCluster.setText("");
            selectedTalukas.clear();
            customSpinnerTalukas.clear();
            etUserTaluka.setText("");
            selectedVillages.clear();
            customSpinnerVillages.clear();
            etUserVillage.setText("");
            selectedSchools.clear();
            customSpinnerSchools.clear();
            etUserSchool.setText("");

            String selectedStateIds = "";
            String selectedStateNames = "";
            for (int i = 0; i < selectedStates.size(); i++) {
                JurisdictionType j = selectedStates.get(i);
                if (i == 0) {
                    selectedStateIds = j.getId();
                    selectedStateNames = j.getName();
                } else {
                    selectedStateIds = selectedStateIds + "," + j.getId();
                    selectedStateNames = selectedStateNames + "," + j.getName();
                }
            }
            etUserState.setText(selectedStateNames);

            if (selectedStates != null && selectedStates.size() > 0) {
                if (etUserCity.getVisibility() == View.VISIBLE) {
                    if (Util.isConnected(this)) {
                        profilePresenter.getProfileLocationData(selectedStateIds,
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.CITY_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    }
                }
                if (etUserDistrict.getVisibility() == View.VISIBLE) {
                    if (Util.isConnected(this)) {
                        profilePresenter.getProfileLocationData(selectedStateIds,
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.DISTRICT_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    }
                }
            }
        } else if (type.equals("Select City")) {
            selectedCities.clear();
            for (CustomSpinnerObject city : customSpinnerCities) {
                if (city.isSelected()) {
                    JurisdictionType selectedCity = new JurisdictionType();
                    selectedCity.setName(city.getName());
                    selectedCity.setId(city.get_id());
                    selectedCities.add(selectedCity);
                }
            }
            // clear other dependent fields
            selectedClusters.clear();
            customSpinnerClusters.clear();
            etUserCluster.setText("");
            selectedTalukas.clear();
            customSpinnerTalukas.clear();
            etUserTaluka.setText("");
            selectedVillages.clear();
            customSpinnerVillages.clear();
            etUserVillage.setText("");
            selectedSchools.clear();
            customSpinnerSchools.clear();
            etUserSchool.setText("");

            String selectedCityIds = "";
            String selectedCityNames = "";
            if (selectedCities != null && selectedCities.size() > 0) {
                for (int i = 0; i < selectedCities.size(); i++) {
                    JurisdictionType j = selectedCities.get(i);
                    if (i == 0) {
                        selectedCityIds = j.getId();
                        selectedCityNames = j.getName();
                    } else {
                        selectedCityIds = selectedCityIds + "," + j.getId();
                        selectedCityNames = selectedCityNames + "," + j.getName();
                    }
                }
            }
            etUserCity.setText(selectedCityNames);

            if (Util.isConnected(this)) {
                if (etUserTaluka.getVisibility() == View.VISIBLE) {
                    profilePresenter.getProfileLocationData(selectedCityIds,
                            selectedRole.getProject().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.TALUKA_LEVEL, selectedOrg.getId(),
                            selectedProjects.get(0).getId(), selectedRole.getId());
                }
            }
        } else if (type.equals("Select District")) {
            selectedDistricts.clear();
            for (CustomSpinnerObject district : customSpinnerDistricts) {
                if (district.isSelected()) {
                    JurisdictionType selectedDistrict = new JurisdictionType();
                    selectedDistrict.setName(district.getName());
                    selectedDistrict.setId(district.get_id());
                    selectedDistricts.add(selectedDistrict);
                }
            }
            // clear other dependent fields
            selectedClusters.clear();
            customSpinnerClusters.clear();
            etUserCluster.setText("");
            selectedTalukas.clear();
            customSpinnerTalukas.clear();
            etUserTaluka.setText("");
            selectedVillages.clear();
            customSpinnerVillages.clear();
            etUserVillage.setText("");
            selectedSchools.clear();
            customSpinnerSchools.clear();
            etUserSchool.setText("");

            String selectedDistrictIds = "";
            String selectedDistrictNames = "";
            if (selectedDistricts != null && selectedDistricts.size() > 0) {
                for (int i = 0; i < selectedDistricts.size(); i++) {
                    JurisdictionType j = selectedDistricts.get(i);
                    if (i == 0) {
                        selectedDistrictIds = j.getId();
                        selectedDistrictNames = j.getName();
                    } else {
                        selectedDistrictIds = selectedDistrictIds + "," + j.getId();
                        selectedDistrictNames = selectedDistrictNames + "," + j.getName();
                    }
                }
            }
            etUserDistrict.setText(selectedDistrictNames);

            if (Util.isConnected(this)) {
                if (etUserTaluka.getVisibility() == View.VISIBLE) {
                    profilePresenter.getProfileLocationData(selectedDistrictIds,
                            selectedRole.getProject().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.TALUKA_LEVEL, selectedOrg.getId(),
                            selectedProjects.get(0).getId(), selectedRole.getId());
                }
            }
        } else if (type.equals("Select Taluka")) {
            selectedTalukas.clear();
            for (CustomSpinnerObject taluka : customSpinnerTalukas) {
                if (taluka.isSelected()) {
                    JurisdictionType selectedTaluka = new JurisdictionType();
                    selectedTaluka.setName(taluka.getName());
                    selectedTaluka.setId(taluka.get_id());
                    selectedTalukas.add(selectedTaluka);
                }
            }
            // clear other dependent fields
            selectedClusters.clear();
            customSpinnerClusters.clear();
            etUserCluster.setText("");
            selectedVillages.clear();
            customSpinnerVillages.clear();
            etUserVillage.setText("");
            selectedSchools.clear();
            customSpinnerSchools.clear();
            etUserSchool.setText("");

            String selectedTalukaIds = "";
            String selectedTalukaNames = "";
            for (int i = 0; i < selectedTalukas.size(); i++) {
                JurisdictionType j = selectedTalukas.get(i);
                if (i == 0) {
                    selectedTalukaIds = j.getId();
                    selectedTalukaNames = j.getName();
                } else {
                    selectedTalukaIds = selectedTalukaIds + "," + j.getId();
                    selectedTalukaNames = selectedTalukaNames + "," + j.getName();
                }
            }
            etUserTaluka.setText(selectedTalukaNames);

            if (selectedTalukas != null && selectedTalukas.size() > 0) {
                if (Util.isConnected(this)) {
                    if (etUserCluster.getVisibility() == View.VISIBLE) {
                        profilePresenter.getProfileLocationData(selectedTalukaIds,
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.CLUSTER_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    } else if (etUserVillage.getVisibility() == View.VISIBLE) {
                        profilePresenter.getProfileLocationData(selectedTalukaIds,
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.VILLAGE_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    }
                }
            }
        } else if (type.equals("Select Cluster")) {
            selectedClusters.clear();
            for (CustomSpinnerObject cluster : customSpinnerClusters) {
                if (cluster.isSelected()) {
                    JurisdictionType selectedCluster = new JurisdictionType();
                    selectedCluster.setName(cluster.getName());
                    selectedCluster.setId(cluster.get_id());
                    selectedClusters.add(selectedCluster);
                }
            }
            // clear other dependent fields
            selectedVillages.clear();
            customSpinnerVillages.clear();
            etUserVillage.setText("");
            selectedSchools.clear();
            customSpinnerSchools.clear();
            etUserSchool.setText("");

            String selectedClusterIds = "";
            String selectedClusterNames = "";
            for (int i = 0; i < selectedClusters.size(); i++) {
                JurisdictionType j = selectedClusters.get(i);
                if (i == 0) {
                    selectedClusterIds = j.getId();
                    selectedClusterNames = j.getName();
                } else {
                    selectedClusterIds = selectedClusterIds + "," + j.getId();
                    selectedClusterNames = selectedClusterNames + "," + j.getName();
                }
            }
            etUserCluster.setText(selectedClusterNames);

            if (selectedClusters != null && selectedClusters.size() > 0) {
                if (Util.isConnected(this)) {
                    if (etUserVillage.getVisibility() == View.VISIBLE) {
                        profilePresenter.getProfileLocationData(selectedClusterIds,
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.VILLAGE_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    }
                }
            }
        } else if (type.equals("Select Village")) {
            selectedVillages.clear();
            for (CustomSpinnerObject cluster : customSpinnerVillages) {
                if (cluster.isSelected()) {
                    JurisdictionType selectedVillage = new JurisdictionType();
                    selectedVillage.setName(cluster.getName());
                    selectedVillage.setId(cluster.get_id());
                    selectedVillages.add(selectedVillage);
                }
            }
            // clear other dependent fields
            selectedSchools.clear();
            customSpinnerSchools.clear();
            etUserSchool.setText("");

            String selectedVillageIds = "";
            String selectedVillageNames = "";
            for (int i = 0; i < selectedVillages.size(); i++) {
                JurisdictionType j = selectedVillages.get(i);
                if (i == 0) {
                    selectedVillageIds = j.getId();
                    selectedVillageNames = j.getName();
                } else {
                    selectedVillageIds = selectedVillageIds + "," + j.getId();
                    selectedVillageNames = selectedVillageNames + "," + j.getName();
                }
            }
            etUserVillage.setText(selectedVillageNames);

            if (selectedVillages != null && selectedVillages.size() > 0) {
                if (Util.isConnected(this)) {
                    if (etUserSchool.getVisibility() == View.VISIBLE) {
                        profilePresenter.getProfileLocationData(selectedVillageIds,
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.SCHOOL_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    }
                }
            }
        } else if (type.equals("Select School")) {
            selectedSchools.clear();
            for (CustomSpinnerObject school : customSpinnerSchools) {
                if (school.isSelected()) {
                    JurisdictionType selectedSchool = new JurisdictionType();
                    selectedSchool.setName(school.getName());
                    selectedSchool.setId(school.get_id());
                    selectedSchools.add(selectedSchool);
                }
            }
            String selectedSchoolIds = "";
            String selectedSchoolNames = "";
            if (selectedSchools != null && selectedSchools.size() > 0) {
                for (int i = 0; i < selectedSchools.size(); i++) {
                    JurisdictionType j = selectedSchools.get(i);
                    if (i == 0) {
                        selectedSchoolIds = j.getId();
                        selectedSchoolNames = j.getName();
                    } else {
                        selectedSchoolIds = selectedSchoolIds + "," + j.getId();
                        selectedSchoolNames = selectedSchoolNames + "," + j.getName();
                    }
                }
            }
            etUserSchool.setText(selectedSchoolNames);
        }
    }
}