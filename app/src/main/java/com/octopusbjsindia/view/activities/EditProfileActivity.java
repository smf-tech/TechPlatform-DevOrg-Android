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
import android.view.ViewGroup;
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
import com.octopusbjsindia.models.profile.UserLocation;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.presenter.ProfileActivityPresenter;
import com.octopusbjsindia.utility.AppEvents;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;
import com.octopusbjsindia.widgets.MultiSelectSpinner;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("CanBeFinal")
public class EditProfileActivity extends BaseActivity implements ProfileTaskListener,
        View.OnClickListener, AdapterView.OnItemSelectedListener,
        MultiSelectSpinner.MultiSpinnerListener, APIDataListener, CustomSpinnerListener {

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

    //private Spinner spOrganization;
//    private Spinner spCountry;
//    private Spinner spState;
    //private Spinner spRole;
    //private Spinner spStructure;
    //private Spinner spProject;

    //private MultiSelectSpinner spProject;
    //private MultiSelectSpinner spDistrict;
    //private MultiSelectSpinner spCity;
    //private MultiSelectSpinner spTaluka;
    //private MultiSelectSpinner spCluster;
    //private MultiSelectSpinner spVillage;

    private ImageView imgUserProfilePic;
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

    private List<JurisdictionType> countries = new ArrayList<>();
    //private List<JurisdictionType> states = new ArrayList<>();
    private List<JurisdictionType> districts = new ArrayList<>();
    private List<JurisdictionType> cities = new ArrayList<>();
    private List<JurisdictionType> talukas = new ArrayList<>();
    private List<JurisdictionType> clusters = new ArrayList<>();
    private List<JurisdictionType> villages = new ArrayList<>();
    private List<JurisdictionType> schools = new ArrayList<>();

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
    private ProfileActivityPresenter profilePresenter;

    private JurisdictionType selectedProject;
    private OrganizationRole selectedRole;
    private Organization selectedOrg;

    private boolean mImageUploaded;
    private String mUploadedImageUrl;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private final String TAG = EditProfileActivity.class.getName();
    private String currentPhotoPath = "";

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

        //spOrganization = findViewById(R.id.sp_user_organization);
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
        //etUserTaluka = findViewById(R.id.etUserTaluka);
        //spProject = findViewById(R.id.sp_project);
        //spProject.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_PROJECT);
        //spRole = findViewById(R.id.sp_role);
//        spCountry = findViewById(R.id.sp_user_country);
//        spState = findViewById(R.id.sp_user_state);

//        spDistrict = findViewById(R.id.sp_district);
//        spDistrict.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_DISTRICT);

//        spCity = findViewById(R.id.sp_city);
//        spCity.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_CITY);
//
//        spTaluka = findViewById(R.id.sp_taluka);
//        spTaluka.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_TALUKA);
//
//        spCluster = findViewById(R.id.sp_cluster);
//        spCluster.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_CLUSTER);

        //spStructure = findViewById(R.id.sp_user_structure);

//        spVillage = findViewById(R.id.sp_village);
//        spVillage.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_VILLAGE);

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

//                    List<Organization> orgData = Util.getUserOrgFromPref().getData();
//                    if (orgData != null && orgData.size() > 0) {
//                        int id = 0;
//                        showOrganizations(orgData);
//
//                        for (int i = 0; i < orgData.size(); i++) {
//                            if (userInfo.getOrgId().equals(orgData.get(i).getId())) {
//                                id = i;
//                                this.selectedOrg = orgData.get(i);
//                            }
//                        }
//                        //spOrganization.setSelection(id);
//                    } else {
                        if (Util.isConnected(this)) {
                            profilePresenter.getOrganizations();
                        } else {
//                            List<String> org = new ArrayList<>();
//                            org.add(userInfo.getOrgName());
//                            setOrganizationData(org);
                            selectionOrgList.clear();
                            CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                            customSpinnerObject.set_id(userInfo.getId());
                            customSpinnerObject.setName(userInfo.getOrgName());
                            selectionOrgList.add(customSpinnerObject);

                            List<Organization> orgList = new ArrayList<>();
                            Organization orgObj = new Organization();
                            orgObj.setId(userInfo.getOrgId());
                            orgObj.setOrgName(userInfo.getOrgName());
                            orgList.add(orgObj);
                            this.organizations = orgList;
                        }
                    //}
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

        //spOrganization.setOnItemSelectedListener(this);
//        spCountry.setOnItemSelectedListener(this);
//        spState.setOnItemSelectedListener(this);
        //spProject.setOnItemSelectedListener(this);
        //spRole.setOnItemSelectedListener(this);
        //spStructure.setOnItemSelectedListener(this);

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
                //spOrganization.performClick();
                CustomSpinnerDialogClass cdd = new CustomSpinnerDialogClass(this, this, "Select Organization",
                        selectionOrgList, false);
                cdd.show();
                cdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.etUserProject:
                //spOrganization.performClick();
                if (selectionProjectList.size() > 0) {
                    CustomSpinnerDialogClass cddProject = new CustomSpinnerDialogClass(this, this, "Select Project",
                            selectionProjectList, false);
                    cddProject.show();
                    cddProject.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (selectedOrg != null && TextUtils.isEmpty(selectedOrg.getId())) {
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
                    if (selectedProjects != null && selectedProjects.size()>0 &&
                            !TextUtils.isEmpty(selectedProjects.get(0).getId())) {
                        profilePresenter.getOrganizationRoles(this.selectedOrg.getId(),
                                selectedProjects.get(0).getId());
                    } else {
                        Toast.makeText(this, getString(R.string.msg_select_project), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.etUserCountry:
                if(customSpinnerCountries.size()>0) {
                    CustomSpinnerDialogClass csdCountry = new CustomSpinnerDialogClass(this, this,
                            "Select Country", customSpinnerCountries,
                            false);
                    csdCountry.show();
                    csdCountry.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (selectedProjects != null && selectedProjects.size()>0 &&
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
                if(customSpinnerStates.size()>0) {
                    CustomSpinnerDialogClass csdState = new CustomSpinnerDialogClass(this, this,
                            "Select State", customSpinnerStates,
                            false);
                    csdState.show();
                    csdState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                        if (selectedCountries.size() > 0 && selectedCountries.size()>0 &&
                                selectedCountries.get(0).getId().length() > 0) {
                            profilePresenter.getProfileLocationData(selectedCountries.get(0).getId(),
                                    selectedRole.getProject().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.STATE_LEVEL, selectedOrg.getId(),
                                    selectedProjects.get(0).getId(), selectedRole.getId());
                        } else {
                            profilePresenter.getProfileLocationData("",
                                    selectedRole.getProject().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.STATE_LEVEL, selectedOrg.getId(),
                                    selectedProjects.get(0).getId(), selectedRole.getId());
                        }
                }
                break;
            case R.id.etUserCity:
                if(customSpinnerCities.size()>0) {
                    CustomSpinnerDialogClass csdState = new CustomSpinnerDialogClass(this, this,
                            "Select City", customSpinnerCities,
                            false);
                    csdState.show();
                    csdState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (selectedStates != null && selectedStates.size() > 0 &&
                            !TextUtils.isEmpty(selectedStates.get(0).getId())) {
                        profilePresenter.getProfileLocationData(selectedStates.get(0).getId(),
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.CITY_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    } else {
                        Toast.makeText(this, getString(R.string.msg_select_state), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.etUserDistrict:
                if(customSpinnerDistricts.size()>0) {
                    CustomSpinnerDialogClass csdState = new CustomSpinnerDialogClass(this, this,
                            "Select District", customSpinnerDistricts,
                            false);
                    csdState.show();
                    csdState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (selectedStates != null && selectedStates.size() > 0 &&
                            !TextUtils.isEmpty(selectedStates.get(0).getId())) {
                        profilePresenter.getProfileLocationData(selectedStates.get(0).getId(),
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.DISTRICT_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    } else {
                        Toast.makeText(this, getString(R.string.msg_select_state), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.etUserTaluka:
                if(customSpinnerTalukas.size()>0) {
                    CustomSpinnerDialogClass csdState = new CustomSpinnerDialogClass(this, this,
                            "Select Taluka", customSpinnerTalukas,
                            false);
                    csdState.show();
                    csdState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if(etUserCity.getVisibility() == View.VISIBLE) {
                        if (selectedCities != null && selectedCities.size()>0 &&
                                !TextUtils.isEmpty(selectedCities.get(0).getId())) {
                            profilePresenter.getProfileLocationData(selectedCities.get(0).getId(),
                                    selectedRole.getProject().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.TALUKA_LEVEL, selectedOrg.getId(),
                                    selectedProjects.get(0).getId(), selectedRole.getId());
                        } else {
                            Toast.makeText(this, getString(R.string.msg_select_city), Toast.LENGTH_LONG).show();
                        }
                    } else if(etUserDistrict.getVisibility() == View.VISIBLE) {
                        if (selectedDistricts != null && selectedDistricts.size() > 0 &&
                                !TextUtils.isEmpty(selectedDistricts.get(0).getId())) {
                            profilePresenter.getProfileLocationData(selectedDistricts.get(0).getId(),
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
                if(customSpinnerVillages.size()>0) {
                    CustomSpinnerDialogClass csdVillage = new CustomSpinnerDialogClass(this, this,
                            "Select Village", customSpinnerVillages,
                            false);
                    csdVillage.show();
                    csdVillage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (selectedClusters != null && selectedClusters.size()>0 &&
                    !TextUtils.isEmpty(selectedClusters.get(0).getId())) {
                        profilePresenter.getProfileLocationData(selectedClusters.get(0).getId(),
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.VILLAGE_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    } else {
                        Toast.makeText(this, getString(R.string.msg_select_village), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.etUserCluster:
                if(customSpinnerClusters.size()>0) {
                    CustomSpinnerDialogClass csdCluster = new CustomSpinnerDialogClass(this, this,
                            "Select Cluster", customSpinnerClusters,
                            false);
                    csdCluster.show();
                    csdCluster.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (selectedTalukas != null && selectedTalukas.size()>0 &&
                            !TextUtils.isEmpty(selectedTalukas.get(0).getId())) {
                        profilePresenter.getProfileLocationData(selectedTalukas.get(0).getId(),
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.CLUSTER_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    } else {
                        Toast.makeText(this, getString(R.string.msg_select_state), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.etUserSchool:
                if(customSpinnerSchools.size()>0) {
                    CustomSpinnerDialogClass csdSchool = new CustomSpinnerDialogClass(this, this,
                            "Select School", customSpinnerSchools,
                            false);
                    csdSchool.show();
                    csdSchool.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (selectedVillages != null && selectedVillages.size()>0 &&
                            !TextUtils.isEmpty(selectedVillages.get(0).getId())) {
                        profilePresenter.getProfileLocationData(selectedVillages.get(0).getId(),
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.SCHOOL_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    } else {
                        Toast.makeText(this, getString(R.string.msg_select_state), Toast.LENGTH_LONG).show();
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
            if (getDeviceId().length() > 0) {
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
        startActivityForResult(pictureIntent,Constants.CHOOSE_IMAGE_FROM_CAMERA);
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
                 finalUri=Uri.fromFile(new File(currentPhotoPath));
                Crop.of(finalUri, finalUri).start(this);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else if (requestCode == Constants.CHOOSE_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                try {
                    getImageFile();
                    outputUri = data.getData();
                    finalUri=Uri.fromFile(new File(currentPhotoPath));
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
            if (requestCode == Constants.READ_PHONE_STORAGE) {
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
        etUserCountry.setVisibility(View.GONE);
        //findViewById(R.id.txt_country).setVisibility(View.GONE);
        countries.clear();
        selectedCountries.clear();

        etUserState.setVisibility(View.GONE);
        //findViewById(R.id.txt_state).setVisibility(View.GONE);
        //states.clear();
        selectedStates.clear();

        etUserDistrict.setVisibility(View.GONE);
        //findViewById(R.id.txt_district).setVisibility(View.GONE);
        districts.clear();
        selectedDistricts.clear();

        etUserCity.setVisibility(View.GONE);
        //findViewById(R.id.txt_city).setVisibility(View.GONE);
        cities.clear();
        selectedCities.clear();

        etUserTaluka.setVisibility(View.GONE);
        //findViewById(R.id.txt_taluka).setVisibility(View.GONE);
        talukas.clear();
        selectedTalukas.clear();

        etUserCluster.setVisibility(View.GONE);
        //findViewById(R.id.txt_cluster).setVisibility(View.GONE);
        clusters.clear();
        selectedClusters.clear();

        etUserVillage.setVisibility(View.GONE);
        //findViewById(R.id.txt_village).setVisibility(View.GONE);
        villages.clear();
        selectedVillages.clear();

        etUserSchool.setVisibility(View.GONE);
        //findViewById(R.id.txt_village).setVisibility(View.GONE);
        schools.clear();
        selectedSchools.clear();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            //case R.id.sp_user_organization:
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

//                        if (Util.isConnected(this)) {
//                            if (organizations != null && !organizations.isEmpty() && organizations.get(i) != null
//                            && !TextUtils.isEmpty(organizations.get(i).getId())) {
//                                this.selectedOrg = organizations.get(i);
//                                profilePresenter.getOrganizationProjects(this.selectedOrg.getId());
//                            }
//                        }

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
            //break;

            //case R.id.sp_project:
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

//                        if (Util.isConnected(this)) {
//                            if (projects != null && !projects.isEmpty() && projects.get(i) != null
//                                    && !TextUtils.isEmpty(projects.get(i).getId())) {
//                                profilePresenter.getOrganizationRoles(this.selectedOrg.getId(),
//                                        projects.get(i).getId());
//                            }
//                        }

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

            //selectedProjects.clear();
//                for (int i = 0; i < selected.length; i++) {
//                    if (selected[i]) {
//                        JurisdictionType project = new JurisdictionType();
//                        project.setId(projects.get(i).getId());
//                        project.setName(projects.get(i).getOrgProjectName());
//                        selectedProjects.add(project);

//
//                profilePresenter.getOrganizationRoles(organizations.get(i).getId(),
//                        projects.get(i).getId());
//                    }
//                }
            //break;

//            case R.id.sp_role:
//                if (roles != null && !roles.isEmpty() && roles.get(i) != null) {
//
//                    selectedRoles.clear();
//                    selectedRole = roles.get(i);
//                    selectedRoles.add(selectedRole.getDisplayName());
//
//                    if (selectedRole.getProject() != null) {
//                        List<Jurisdiction> jurisdictions = selectedRole.getProject().getJurisdictions();
//                        if (jurisdictions != null && jurisdictions.size() > 0) {
//                            hideJurisdictionLevel();
//                            for (Jurisdiction j : jurisdictions) {
//                                setJurisdictionLevel(j.getLevelName());
//                            }
//                        }
//                    } else {
//                        UserInfo userInfo = Util.getUserObjectFromPref();
//                        UserLocation userLocation = userInfo.getUserLocation();
//                        if (userLocation.getCountryId() != null && userLocation.getCountryId().size() > 0) {
//                            setJurisdictionLevel(Constants.JurisdictionLevelName.COUNTRY_LEVEL);
//                        }
//
//                        if (userLocation.getStateId() != null && userLocation.getStateId().size() > 0) {
//                            setJurisdictionLevel(Constants.JurisdictionLevelName.STATE_LEVEL);
//                        }
//
//                        if (userLocation.getDistrictIds() != null && userLocation.getDistrictIds().size() > 0) {
//                            setJurisdictionLevel(Constants.JurisdictionLevelName.DISTRICT_LEVEL);
//                        }
//
//                        if (userLocation.getCityIds() != null && userLocation.getCityIds().size() > 0) {
//                            setJurisdictionLevel(Constants.JurisdictionLevelName.CITY_LEVEL);
//                        }
//
//                        if (userLocation.getTalukaIds() != null && userLocation.getTalukaIds().size() > 0) {
//                            setJurisdictionLevel(Constants.JurisdictionLevelName.TALUKA_LEVEL);
//                        }
//
//                        if (userLocation.getVillageIds() != null && userLocation.getVillageIds().size() > 0) {
//                            setJurisdictionLevel(Constants.JurisdictionLevelName.VILLAGE_LEVEL);
//                        }
//
//                        if (userLocation.getClusterIds() != null && userLocation.getClusterIds().size() > 0) {
//                            setJurisdictionLevel(Constants.JurisdictionLevelName.CLUSTER_LEVEL);
//                        }
//                    }
//                }
//                break;

//            case R.id.sp_user_country:
//                if (countries != null && !countries.isEmpty() && countries.get(i) != null) {
//                    selectedCountries.clear();
//                    selectedCountries.add(countries.get(i));
//
//                    if (etUserState.getVisibility() == View.VISIBLE) {
//                        if (Util.isConnected(this)) {
//                            profilePresenter.getLocationData(selectedCountries.get(0).getId(),
//                                    selectedRole.getProject().getJurisdictionTypeId(),
//                                    Constants.JurisdictionLevelName.STATE_LEVEL);
//                        }
//                    }
//                }

//            case R.id.sp_user_state:
//                if (states != null && !states.isEmpty() && states.get(i) != null) {
//
//                    selectedStates.clear();
//                    selectedStates.add(states.get(i));
//
//                    if (spCity.getVisibility() == View.VISIBLE) {
//                        if (Util.isConnected(this)) {
//                            profilePresenter.getLocationData(selectedStates.get(0).getId(),
//                                    selectedRole.getProject().getJurisdictionTypeId(),
//                                    Constants.JurisdictionLevelName.CITY_LEVEL);
//                        }

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
                    //}

//                    if (spDistrict.getVisibility() == View.VISIBLE) {
//                        if (Util.isConnected(this)) {
//                            profilePresenter.getLocationData(selectedStates.get(0).getId(),
//                                    selectedRole.getProject().getJurisdictionTypeId(),
//                                    Constants.JurisdictionLevelName.DISTRICT_LEVEL);
//                        }

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
//                    }
//                }
//                break;

//            case R.id.sp_user_structure:
//                break;
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
        //List<String> org = new ArrayList<>();
        selectionOrgList.clear();
        for (int i = 0; i < organizations.size(); i++) {
            //org.add(organizations.get(i).getOrgName());
            CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
            customSpinnerObject.set_id(organizations.get(i).getId());
            customSpinnerObject.setName(organizations.get(i).getOrgName());
            selectionOrgList.add(customSpinnerObject);
        }
        if (getIntent().getStringExtra(Constants.Login.ACTION) != null
                && getIntent().getStringExtra(Constants.Login.ACTION)
                .equalsIgnoreCase(Constants.Login.ACTION_EDIT)) {

            UserInfo userInfo = Util.getUserObjectFromPref();
            if (organizations != null && !organizations.isEmpty()) {
                for (Organization organization : organizations) {
                    if (organization.getId().equals(userInfo.getOrgId())) {
                        this.selectedOrg = organization;
                        etUserOrganization.setText(selectedOrg.getOrgName());
                        profilePresenter.getOrganizationProjects(this.selectedOrg.getId());
                        break;
                    }
                }
            }
        }
        //setOrganizationData(org);
    }

    @Override
    public void showOrganizationProjects(List<OrganizationProject> organizationProjects) {
        if (organizationProjects != null && !organizationProjects.isEmpty()) {

            //Collections.sort(organizationProjects, (j1, j2) -> j1.getOrgProjectName().compareTo(j2.getOrgProjectName()));

//            this.projects.clear();
//            this.projects.addAll(organizationProjects);

            //List<String> projects = new ArrayList<>();
//            List<OrganizationProject> projects = new ArrayList<>();
//            for (OrganizationProject organizationProject : organizationProjects) {
//                //projects.add(organizationProject.getOrgProjectName());
//                projects.add(organizationProject.getOrgProjectName());
//            }

            this.projects.clear();
            this.projects.addAll(organizationProjects);
            selectionProjectList.clear();
            for (int i = 0; i < organizationProjects.size(); i++) {
                //org.add(organizations.get(i).getOrgName());
                CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                customSpinnerObject.set_id(organizationProjects.get(i).getId());
                customSpinnerObject.setName(organizationProjects.get(i).getOrgProjectName());
                selectionProjectList.add(customSpinnerObject);
            }

//            ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProfileActivity.this,
//                    R.layout.layout_spinner_item, projects);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spProject.setAdapter(adapter);

            //spProject.setItems(projects, getString(R.string.project), this);

            if (getIntent().getStringExtra(Constants.Login.ACTION) != null
                    && getIntent().getStringExtra(Constants.Login.ACTION)
                    .equalsIgnoreCase(Constants.Login.ACTION_EDIT)) {

                //int projectId = 0;
                UserInfo userInfo = Util.getUserObjectFromPref();

                //boolean[] selectedValues = new boolean[organizationProjects.size()];
//                for(int userProjectIndex = 0; userProjectIndex< userInfo.getProjectIds().size(); userProjectIndex++ ){
//                    for (int projectIndex = 0; projectIndex < organizationProjects.size(); projectIndex++) {
//                        if (userInfo.getProjectIds().get(userProjectIndex).getId().
//                                equals(organizationProjects.get(projectIndex).getId())) {
//                            projectId = projectIndex;
//                        }
//                    }
//                }
                selectedProjects.clear();
                if (organizationProjects != null && !organizationProjects.isEmpty()) {
                    for (OrganizationProject organizationProject : organizationProjects) {
                        if (organizationProject.getId().equals(userInfo.getProjectIds().get(0).getId())) {
                            JurisdictionType project = new JurisdictionType();
                            project.setId(organizationProject.getId());
                            project.setName(organizationProject.getOrgProjectName());
                            selectedProjects.add(project);
                            etUserProject.setText(organizationProject.getOrgProjectName());
                            profilePresenter.getOrganizationRoles(this.selectedOrg.getId(),
                                    project.getId());
                            break;
                        }
                    }
                }
                //spProject.setSelectedValues(selectedValues);
                //spProject.setSelection(projectId);
                //spProject.setPreFilledText();
            }
        }
    }

    @Override
    public void showOrganizationRoles(List<OrganizationRole> organizationRoles) {
        if (organizationRoles != null && !organizationRoles.isEmpty()) {

            //Collections.sort(organizationRoles, (j1, j2) -> j1.getDisplayName().compareTo(j2.getDisplayName()));

//            List<String> roles = new ArrayList<>();
//            for (OrganizationRole organizationRole : organizationRoles) {
//                roles.add(organizationRole.getDisplayName());
//            }

            this.roles.clear();
            this.roles.addAll(organizationRoles);
            selectionRolesList.clear();
            for (int i = 0; i < organizationRoles.size(); i++) {
                //org.add(organizations.get(i).getOrgName());
                CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                customSpinnerObject.set_id(organizationRoles.get(i).getId());
                customSpinnerObject.setName(organizationRoles.get(i).getDisplayName());
                selectionRolesList.add(customSpinnerObject);
            }

//            ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProfileActivity.this,
//                    R.layout.layout_spinner_item, roles);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spRole.setAdapter(adapter);

            if (getIntent().getStringExtra(Constants.Login.ACTION) != null
                    && getIntent().getStringExtra(Constants.Login.ACTION)
                    .equalsIgnoreCase(Constants.Login.ACTION_EDIT)) {

//                int id = 0;
//                UserInfo userInfo = Util.getUserObjectFromPref();
//                for (int roleIndex = 0; roleIndex < organizationRoles.size(); roleIndex++) {
//                    if (userInfo.getRoleIds().equals(organizationRoles.get(roleIndex).getId())) {
//                        id = roleIndex;
//                    }
//                }
                //spRole.setSelection(id);
                UserInfo userInfo = Util.getUserObjectFromPref();
                for(OrganizationRole organizationRole: organizationRoles) {
                    if(organizationRole.getId().equals(userInfo.getRoleIds())) {
                        selectedRole = organizationRole;
                        selectedRoles.add(selectedRole.getId());
                        etUserRole.setText(selectedRole.getDisplayName());
                        break;
                    }
                }
                if (selectedRole != null && selectedRole.getProject() != null) {
                    List<Jurisdiction> jurisdictions = selectedRole.getProject().getJurisdictions();
                    if (jurisdictions != null && jurisdictions.size() > 0) {
                        hideJurisdictionLevel();
                        for (Jurisdiction j : jurisdictions) {
                            setJurisdictionLevel(j.getLevelName());
                        }
                    }
                }
            }
        }
    }

    private void setJurisdictionLevel(String level) {
        UserInfo userInfo = Util.getUserObjectFromPref();
        switch (level) {
            case Constants.JurisdictionLevelName.COUNTRY_LEVEL:
                etUserCountry.setVisibility(View.VISIBLE);
                //findViewById(R.id.txt_country).setVisibility(View.VISIBLE);
                selectedCountries.clear();
                if(userInfo.getUserLocation()!= null && userInfo.getUserLocation().getCountryId()!=null
                        && userInfo.getUserLocation().
                        getCountryId().size()>0) {
                    etUserCountry.setText(userInfo.getUserLocation().getCountryId().get(0).getName());
                    JurisdictionType selectedCountry = new JurisdictionType();
                    selectedCountry.setId(userInfo.getUserLocation().getCountryId().get(0).getId());
                    selectedCountry.setName(userInfo.getUserLocation().getCountryId().get(0).getName());
                    selectedCountries.add(selectedCountry);
                }
                if (Util.isConnected(this)) {
                    profilePresenter.getProfileLocationData("",
                            selectedRole.getProject().getJurisdictionTypeId(), level, selectedOrg.getId(),
                            selectedProjects.get(0).getId(), selectedRole.getId());
                } else {
                    Util.showToast(getResources().getString(R.string.no_data_available), this);
//                    List<String> countryNames = new ArrayList<>();
//                    UserInfo userInfo = Util.getUserObjectFromPref();
//                    List<JurisdictionType> countriesObj = userInfo.getUserLocation().getCountryId();
//
//                    Collections.sort(countriesObj, (j1, j2) -> j1.getName().compareTo(j2.getName()));
//
//                    for (int k = 0; k < countriesObj.size(); k++) {
//                        countryNames.add(countriesObj.get(k).getName());
//                        this.countries.add(countriesObj.get(k));
//                    }
//                    setCountryData(countryNames);

                }
                break;

            case Constants.JurisdictionLevelName.STATE_LEVEL:
                etUserState.setVisibility(View.VISIBLE);
                //findViewById(R.id.txt_state).setVisibility(View.VISIBLE);
                selectedStates.clear();
                if(userInfo.getUserLocation()!= null && userInfo.getUserLocation().getStateId()!=null && userInfo.getUserLocation().
                        getStateId().size()>0) {
                    etUserState.setText(userInfo.getUserLocation().getStateId().get(0).getName());
                    JurisdictionType selectedState = new JurisdictionType();
                    selectedState.setId(userInfo.getUserLocation().getStateId().get(0).getId());
                    selectedState.setName(userInfo.getUserLocation().getStateId().get(0).getName());
                    selectedStates.add(selectedState);
                }
                if (Util.isConnected(this)) {
                    if (selectedCountries.size() > 0 && selectedCountries.get(0).getId().length() > 0) {
                        profilePresenter.getProfileLocationData(selectedCountries.get(0).getId(),
                                selectedRole.getProject().getJurisdictionTypeId(), level, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    } else {
                        profilePresenter.getProfileLocationData("",
                                selectedRole.getProject().getJurisdictionTypeId(), level, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    }
                } else {
                    Util.showToast(getResources().getString(R.string.no_data_available), this);
//                    List<String> stateNames = new ArrayList<>();
//                    UserInfo userInfo = Util.getUserObjectFromPref();
//                    List<JurisdictionType> statesObj = userInfo.getUserLocation().getStateId();
//
//                    Collections.sort(statesObj, (j1, j2) -> j1.getName().compareTo(j2.getName()));
//
//                    for (int k = 0; k < statesObj.size(); k++) {
//                        stateNames.add(statesObj.get(k).getName());
//                        this.states.add(statesObj.get(k));
//                    }
//                    setStateData(stateNames);
                }
                break;

            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                etUserDistrict.setVisibility(View.VISIBLE);
                //findViewById(R.id.txt_district).setVisibility(View.VISIBLE);
                selectedDistricts.clear();
                if (userInfo.getUserLocation()!= null && userInfo.getUserLocation().
                        getDistrictIds() != null && userInfo.getUserLocation().getDistrictIds().size()>0) {
                    etUserDistrict.setText(userInfo.getUserLocation().getDistrictIds().get(0).getName());
                    JurisdictionType selectedDistrict = new JurisdictionType();
                    selectedDistrict.setId(userInfo.getUserLocation().getDistrictIds().get(0).getId());
                    selectedDistrict.setName(userInfo.getUserLocation().getDistrictIds().get(0).getName());
                    selectedDistricts.add(selectedDistrict);
                }
                break;

            case Constants.JurisdictionLevelName.CITY_LEVEL:
                etUserCity.setVisibility(View.VISIBLE);
                //findViewById(R.id.txt_city).setVisibility(View.VISIBLE);
                selectedCities.clear();
                if(userInfo.getUserLocation()!= null && userInfo.getUserLocation().getCityIds()!=null
                        && userInfo.getUserLocation().
                        getCityIds().size()>0) {
                    etUserCity.setText(userInfo.getUserLocation().getCityIds().get(0).getName());
                    JurisdictionType selectedCity = new JurisdictionType();
                    selectedCity.setId(userInfo.getUserLocation().getCityIds().get(0).getId());
                    selectedCity.setName(userInfo.getUserLocation().getCityIds().get(0).getName());
                    selectedCities.add(selectedCity);
                }
                break;

            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                etUserTaluka.setVisibility(View.VISIBLE);
                //findViewById(R.id.txt_taluka).setVisibility(View.VISIBLE);
                selectedTalukas.clear();
                if(userInfo.getUserLocation()!= null && userInfo.getUserLocation().getTalukaIds()!= null
                        && userInfo.getUserLocation().
                        getTalukaIds().size()>0) {
                    etUserTaluka.setText(userInfo.getUserLocation().getTalukaIds().get(0).getName());
                    JurisdictionType selectedTaluka = new JurisdictionType();
                    selectedTaluka.setId(userInfo.getUserLocation().getTalukaIds().get(0).getId());
                    selectedTaluka.setName(userInfo.getUserLocation().getTalukaIds().get(0).getName());
                    selectedTalukas.add(selectedTaluka);
                }
                break;

            case Constants.JurisdictionLevelName.VILLAGE_LEVEL:
                etUserVillage.setVisibility(View.VISIBLE);
                //findViewById(R.id.txt_village).setVisibility(View.VISIBLE);
                selectedVillages.clear();
                if(userInfo.getUserLocation()!= null && userInfo.getUserLocation().getVillageIds()!= null
                        && userInfo.getUserLocation().getVillageIds().size()>0) {
                    etUserVillage.setText(userInfo.getUserLocation().getVillageIds().get(0).getName());
                    JurisdictionType selectedVillage = new JurisdictionType();
                    selectedVillage.setId(userInfo.getUserLocation().getVillageIds().get(0).getId());
                    selectedVillage.setName(userInfo.getUserLocation().getVillageIds().get(0).getName());
                    selectedVillages.add(selectedVillage);
                }

                break;

            case Constants.JurisdictionLevelName.CLUSTER_LEVEL:
                etUserCluster.setVisibility(View.VISIBLE);
                //findViewById(R.id.txt_village).setVisibility(View.VISIBLE);
                selectedClusters.clear();
                if(userInfo.getUserLocation()!= null && userInfo.getUserLocation().getClusterIds()!=null
                        && userInfo.getUserLocation().getClusterIds().size()>0) {
                    etUserCluster.setText(userInfo.getUserLocation().getClusterIds().get(0).getName());
                    JurisdictionType selectedCluster = new JurisdictionType();
                    selectedCluster.setId(userInfo.getUserLocation().getClusterIds().get(0).getId());
                    selectedCluster.setName(userInfo.getUserLocation().getClusterIds().get(0).getName());
                    selectedClusters.add(selectedCluster);
                }
                break;

            case Constants.JurisdictionLevelName.SCHOOL_LEVEL:
                etUserSchool.setVisibility(View.VISIBLE);
                //findViewById(R.id.txt_village).setVisibility(View.VISIBLE);
                selectedSchools.clear();
                if(userInfo.getUserLocation()!= null && userInfo.getUserLocation().getSchoolIds()!=null
                        && userInfo.getUserLocation().getSchoolIds().size()>0) {
                    etUserSchool.setText(userInfo.getUserLocation().getSchoolIds().get(0).getName());
                    JurisdictionType selectedSchool = new JurisdictionType();
                    selectedSchool.setId(userInfo.getUserLocation().getSchoolIds().get(0).getId());
                    selectedSchool.setName(userInfo.getUserLocation().getSchoolIds().get(0).getName());
                    selectedSchools.add(selectedSchool);
                }
                break;
        }
    }

//    private void setOrganizationData(List<String> org) {
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProfileActivity.this,
//                R.layout.layout_spinner_item, org);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spOrganization.setAdapter(adapter);
//    }

//    private void setCountryData(List<String> countryNames) {
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProfileActivity.this,
//                R.layout.layout_spinner_item, countryNames);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spCountry.setAdapter(adapter);
//
//        if (getIntent().getStringExtra(Constants.Login.ACTION) != null
//                && getIntent().getStringExtra(Constants.Login.ACTION)
//                .equalsIgnoreCase(Constants.Login.ACTION_EDIT)) {
//
//            int id = 0;
//            UserInfo userInfo = Util.getUserObjectFromPref();
//            List<JurisdictionType> countryId = userInfo.getUserLocation().getCountryId();
//            for (int i = 0; i < countries.size(); i++) {
//                if(countryId != null) {
//                    if (countryId.get(0).getId().equals(countries.get(i).getId())) {
//                        id = i;
//                    }
//                }
//            }
//            spCountry.setSelection(id);
//        }
//    }

//    private void setStateData(List<String> stateNames) {
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProfileActivity.this,
//                R.layout.layout_spinner_item, stateNames);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spState.setAdapter(adapter);
//
//        if (getIntent().getStringExtra(Constants.Login.ACTION) != null
//                && getIntent().getStringExtra(Constants.Login.ACTION)
//                .equalsIgnoreCase(Constants.Login.ACTION_EDIT)) {
//
//            int id = 0;
//            UserInfo userInfo = Util.getUserObjectFromPref();
//            List<JurisdictionType> stateId = userInfo.getUserLocation().getStateId();
//            for (int i = 0; i < states.size(); i++) {
//                if(stateId != null) {
//                    if (stateId.get(0).getId().equals(states.get(i).getId())) {
//                        id = i;
//                    }
//                }
//            }
//            spState.setSelection(id);
//        }
//    }

//    private void setDistrictData(List<String> districts) {
//        spDistrict.setItems(districts, getString(R.string.district), this);
//
//        if (Util.getUserObjectFromPref().getUserLocation() != null) {
//            List<JurisdictionType> districtIds = Util.getUserObjectFromPref().getUserLocation().getDistrictIds();
//            if (districtIds != null && districtIds.size() > 0) {
//                boolean[] selectedValues = new boolean[this.districts.size()];
//                for (int districtIndex = 0; districtIndex < this.districts.size(); districtIndex++) {
//                    for (int districtIdIndex = 0; districtIdIndex < districtIds.size(); districtIdIndex++) {
//                        if (this.districts.get(districtIndex).getId().equals(districtIds.get(districtIdIndex).getId())) {
//                            selectedValues[districtIndex] = true;
//                            break;
//                        } else {
//                            selectedValues[districtIndex] = false;
//                        }
//                    }
//                }
//                spDistrict.setSelectedValues(selectedValues);
//                spDistrict.setPreFilledText();
//            }
//        }
//    }

//    private void setCityData(List<String> cities) {
//        spCity.setItems(cities, getString(R.string.city), this);
//
//        if (Util.getUserObjectFromPref().getUserLocation() != null) {
//            List<JurisdictionType> cityIds = Util.getUserObjectFromPref().getUserLocation().getCityIds();
//            if (cityIds != null && cityIds.size() > 0) {
//                boolean[] selectedValues = new boolean[this.cities.size()];
//                for (int cityIndex = 0; cityIndex < this.cities.size(); cityIndex++) {
//                    for (int cityIdIndex = 0; cityIdIndex < cityIds.size(); cityIdIndex++) {
//                        if (this.cities.get(cityIndex).getId().equals(cityIds.get(cityIdIndex).getId())) {
//                            selectedValues[cityIndex] = true;
//                            break;
//                        } else {
//                            selectedValues[cityIndex] = false;
//                        }
//                    }
//                }
//
//                spCity.setSelectedValues(selectedValues);
//                spCity.setPreFilledText();
//            }
//        }
//    }

//    private void setTalukaData(List<String> talukas) {
//        spTaluka.setItems(talukas, getString(R.string.taluka), this);
//
//        if (Util.getUserObjectFromPref().getUserLocation() != null) {
//            List<JurisdictionType> talukaIds = Util.getUserObjectFromPref().getUserLocation().getTalukaIds();
//            if (talukaIds != null && talukaIds.size() > 0) {
//                boolean[] selectedValues = new boolean[this.talukas.size()];
//                for (int talukaIndex = 0; talukaIndex < this.talukas.size(); talukaIndex++) {
//                    for (int talukaIdIndex = 0; talukaIdIndex < talukaIds.size(); talukaIdIndex++) {
//                        if (this.talukas.get(talukaIndex).getId().equals(talukaIds.get(talukaIdIndex).getId())) {
//                            selectedValues[talukaIndex] = true;
//                            break;
//                        } else {
//                            selectedValues[talukaIndex] = false;
//                        }
//                    }
//                }
//
//                spTaluka.setSelectedValues(selectedValues);
//                spTaluka.setPreFilledText();
//            }
//        }
//    }

//    private void setVillageData(List<String> villages) {
//        spVillage.setItems(villages, getString(R.string.village), this);
//
//        if (Util.getUserObjectFromPref().getUserLocation() != null) {
//            List<JurisdictionType> villageIds = Util.getUserObjectFromPref().getUserLocation().getVillageIds();
//            if (villageIds != null && villageIds.size() > 0) {
//                boolean[] selectedValues = new boolean[this.villages.size()];
//                for (int villageIndex = 0; villageIndex < this.villages.size(); villageIndex++) {
//                    for (int villageIdIndex = 0; villageIdIndex < villageIds.size(); villageIdIndex++) {
//                        if (this.villages.get(villageIndex).getId().equals(villageIds.get(villageIdIndex).getId())) {
//                            selectedValues[villageIndex] = true;
//                            break;
//                        } else {
//                            selectedValues[villageIndex] = false;
//                        }
//                    }
//                }
//                spVillage.setSelectedValues(selectedValues);
//                spVillage.setPreFilledText();
//            }
//        }
//    }

    @Override
    public void showJurisdictionLevel(List<JurisdictionLocation> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.COUNTRY_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.countries.clear();
                    customSpinnerCountries.clear();
                    List<String> countryNames = new ArrayList<>();

                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getCountry().getName().compareTo(j2.getCountry().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        countryNames.add(location.getName());
//                        JurisdictionType jurisdictionType = new JurisdictionType();
//                        jurisdictionType.setId(location.getId());
//                        jurisdictionType.setName(location.getName());
//                        this.countries.add(jurisdictionType);
                        CustomSpinnerObject country = new CustomSpinnerObject();
                        country.set_id(location.getId());
                        country.setName(location.getName());
                        country.setSelected(false);
                        this.customSpinnerCountries.add(country);
                    }
                    //setCountryData(countryNames);
                }
                break;
            case Constants.JurisdictionLevelName.STATE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    //this.states.clear();
                    customSpinnerStates.clear();
                    List<String> stateNames = new ArrayList<>();

                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getState().getName().compareTo(j2.getState().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        stateNames.add(location.getName());
//                        JurisdictionType jurisdictionType = new JurisdictionType();
//                        jurisdictionType.setId(location.getId());
//                        jurisdictionType.setName(location.getName());
                        //this.states.add(jurisdictionType);
                        CustomSpinnerObject state = new CustomSpinnerObject();
                        state.set_id(location.getId());
                        state.setName(location.getName());
                        state.setSelected(false);
                        this.customSpinnerStates.add(state);
                    }
                    //setStateData(stateNames);
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
//                        JurisdictionType jurisdictionType = new JurisdictionType();
//                        jurisdictionType.setId(location.getId());
//                        jurisdictionType.setName(location.getName());
//                        this.districts.add(jurisdictionType);
                        CustomSpinnerObject district = new CustomSpinnerObject();
                        district.set_id(location.getId());
                        district.setName(location.getName());
                        district.setSelected(false);
                        this.customSpinnerDistricts.add(district);
                        //}
                        //}
                    }
                    //setDistrictData(districts);
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
//                        JurisdictionType jurisdictionType = new JurisdictionType();
//                        jurisdictionType.setId(location.getId());
//                        jurisdictionType.setName(location.getName());
//                        this.cities.add(jurisdictionType);
                        CustomSpinnerObject city = new CustomSpinnerObject();
                        city.set_id(location.getId());
                        city.setName(location.getName());
                        city.setSelected(false);
                        this.customSpinnerCities.add(city);
                        //}
                        //}
                    }
                    //setCityData(cities);
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
//                        JurisdictionType jurisdictionType = new JurisdictionType();
//                        jurisdictionType.setId(location.getId());
//                        jurisdictionType.setName(location.getName());
//                        this.talukas.add(jurisdictionType);
                        CustomSpinnerObject taluka = new CustomSpinnerObject();
                        taluka.set_id(location.getId());
                        taluka.setName(location.getName());
                        taluka.setSelected(false);
                        this.customSpinnerTalukas.add(taluka);
                        //}
                        //}
//                            }
//                        }
                    }
                    //setTalukaData(talukas);
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
//                        JurisdictionType jurisdictionType = new JurisdictionType();
//                        jurisdictionType.setId(location.getId());
//                        jurisdictionType.setName(location.getName());
//                        this.villages.add(jurisdictionType);
                        CustomSpinnerObject village = new CustomSpinnerObject();
                        village.set_id(location.getId());
                        village.setName(location.getName());
                        village.setSelected(false);
                        this.customSpinnerVillages.add(village);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
                    }
                    //setVillageData(villages);
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
//                        JurisdictionType jurisdictionType = new JurisdictionType();
//                        jurisdictionType.setId(location.getId());
//                        jurisdictionType.setName(location.getName());
//                        this.clusters.add(jurisdictionType);
                        CustomSpinnerObject cluster = new CustomSpinnerObject();
                        cluster.set_id(location.getId());
                        cluster.setName(location.getName());
                        cluster.setSelected(false);
                        this.customSpinnerClusters.add(cluster);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
                        //}
                    }
//                    spCluster.setItems(clusters, getString(R.string.cluster), this);
//
//                    if (Util.getUserObjectFromPref().getUserLocation() != null) {
//                        List<JurisdictionType> clusterIds = Util.getUserObjectFromPref().getUserLocation().getClusterIds();
//                        if (clusterIds != null && clusterIds.size() > 0) {
//                            boolean[] selectedValues = new boolean[this.clusters.size()];
//                            for (int clusterIndex = 0; clusterIndex < this.clusters.size(); clusterIndex++) {
//                                for (int clusterIdIndex = 0; clusterIdIndex < clusterIds.size(); clusterIdIndex++) {
//                                    if (this.clusters.get(clusterIndex).getId().equals(clusterIds.get(clusterIdIndex).getId())) {
//                                        selectedValues[clusterIndex] = true;
//                                        break;
//                                    } else {
//                                        selectedValues[clusterIndex] = false;
//                                    }
//                                }
//                            }
//                            spCluster.setSelectedValues(selectedValues);
//                            spCluster.setPreFilledText();
//                        }
//                    }
                }
                break;
            case Constants.JurisdictionLevelName.SCHOOL_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.schools.clear();
                    List<String> schools = new ArrayList<>();

                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getVillage().getName().compareTo(j2.getVillage().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
//                        for (JurisdictionType state : selectedStates) {
//                            if (state.getName().equalsIgnoreCase(location.getState().getName())) {
//                                for (JurisdictionType district : selectedDistricts) {
//                                    if (district.getName().equalsIgnoreCase(location.getDistrict().getName())) {
//                                        for (JurisdictionType taluka : selectedTalukas) {
//                                            if (taluka.getName().equalsIgnoreCase(location.getTaluka().getName())) {
                        schools.add(location.getName());
//                        JurisdictionType jurisdictionType = new JurisdictionType();
//                        jurisdictionType.setId(location.getId());
//                        jurisdictionType.setName(location.getName());
//                        this.villages.add(jurisdictionType);
                        CustomSpinnerObject school = new CustomSpinnerObject();
                        school.set_id(location.getId());
                        school.setName(location.getName());
                        school.setSelected(false);
                        this.customSpinnerSchools.add(school);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
                    }
                    //setVillageData(villages);
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
//        try {
//            switch (spinnerName) {
//                case Constants.MultiSelectSpinnerType.SPINNER_PROJECT:
//                    selectedProjects.clear();
//                    for (int i = 0; i < selected.length; i++) {
//                        if (selected[i]) {
//                            JurisdictionType project = new JurisdictionType();
//                            project.setId(projects.get(i).getId());
//                            project.setName(projects.get(i).getOrgProjectName());
//                            selectedProjects.add(project);
//                        }
//                    }
//                    break;

//                case Constants.MultiSelectSpinnerType.SPINNER_ROLE:
//                    selectedRoles.clear();
//                    for (int i = 0; i < selected.length; i++) {
//                        if (selected[i]) {
//                            selectedRoles.add(roles.get(i).getId());
//                        }
//                    }
//                    break;

//                case Constants.MultiSelectSpinnerType.SPINNER_CITY:
//                    selectedCities.clear();
//                    for (int i = 0; i < selected.length; i++) {
//                        if (selected[i]) {
//                            selectedCities.add(cities.get(i));
//                        }
//                    }
//                    break;

//                case Constants.MultiSelectSpinnerType.SPINNER_DISTRICT:
//                    selectedDistricts.clear();
//                    for (int i = 0; i < selected.length; i++) {
//                        if (selected[i]) {
//                            selectedDistricts.add(districts.get(i));
//                        }
//                    }
//
//                    if (spTaluka.getVisibility() == View.VISIBLE) {
//                        if (Util.isConnected(this)) {
//                            profilePresenter.getLocationData(selectedDistricts.get(0).getId(),
//                                    selectedRole.getProject().getJurisdictionTypeId(),
//                                    Constants.JurisdictionLevelName.TALUKA_LEVEL);
//                        } else {
//                            List<String> talukaNames = new ArrayList<>();
//                            UserInfo userInfo = Util.getUserObjectFromPref();
//                            List<JurisdictionType> talukaObj = userInfo.getUserLocation().getTalukaIds();
//
//                            Collections.sort(talukaObj, (j1, j2) -> j1.getName().compareTo(j2.getName()));
//
//                            for (int k = 0; k < talukaObj.size(); k++) {
//                                talukaNames.add(talukaObj.get(k).getName());
//                                this.talukas.add(talukaObj.get(k));
//                            }
//
//                            setTalukaData(talukaNames);
//                        }
//                    }
//                    break;
//
//                case Constants.MultiSelectSpinnerType.SPINNER_TALUKA:
//                    selectedTalukas.clear();
//                    for (int i = 0; i < selected.length; i++) {
//                        if (selected[i]) {
//                            selectedTalukas.add(talukas.get(i));
//                        }
//                    }
//
//                    if (spVillage.getVisibility() == View.VISIBLE) {
//                        if (Util.isConnected(this)) {
//                            profilePresenter.getLocationData(selectedTalukas.get(0).getId(),
//                                    selectedRole.getProject().getJurisdictionTypeId(),
//                                    Constants.JurisdictionLevelName.VILLAGE_LEVEL);
//                        } else {
//                            List<String> villageNames = new ArrayList<>();
//                            UserInfo userInfo = Util.getUserObjectFromPref();
//                            List<JurisdictionType> villageObj = userInfo.getUserLocation().getVillageIds();
//
//                            Collections.sort(villageObj, (j1, j2) -> j1.getName().compareTo(j2.getName()));
//
//                            for (int k = 0; k < villageObj.size(); k++) {
//                                villageNames.add(villageObj.get(k).getName());
//                                this.villages.add(villageObj.get(k));
//                            }
//
//                            setVillageData(villageNames);
//                        }
//                    }
//                    break;
//
//                case Constants.MultiSelectSpinnerType.SPINNER_CLUSTER:
//                    selectedClusters.clear();
//                    for (int i = 0; i < selected.length; i++) {
//                        if (selected[i]) {
//                            selectedClusters.add(clusters.get(i));
//                        }
//                    }
//                    break;
//
//                case Constants.MultiSelectSpinnerType.SPINNER_VILLAGE:
//                    selectedVillages.clear();
//                    for (int i = 0; i < selected.length; i++) {
//                        if (selected[i]) {
//                            selectedVillages.add(villages.get(i));
//                        }
//                    }
//                    break;
//            }
//        } catch (Exception e) {
//            Log.e(TAG, "EXCEPTION_IN_ON_VALUE_SELECTED");
//        }
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        if (type.equals("Select Organization")) {
            int selectedPosition = -1;
            String selectedOrgName = "";//, selectedOrgId;
            for (CustomSpinnerObject customSpinnerObject : selectionOrgList) {
                if (customSpinnerObject.isSelected()) {
                    selectedOrgName = customSpinnerObject.getName();
                    //selectedOrgId = customSpinnerObject.get_id();
                    selectedPosition = selectionOrgList.indexOf(customSpinnerObject);
                    break;
                }
            }
            etUserOrganization.setText(selectedOrgName);
            // clear other dependent fields
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
                }
            }
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
        } else if (type.equals("Select Country")) {
            selectedCountries.clear();
            JurisdictionType selectedCountry = new JurisdictionType();
            for (CustomSpinnerObject country : customSpinnerCountries) {
                if (country.isSelected()) {
                    selectedCountry.setName(country.getName());
                    selectedCountry.setId(country.get_id());
                    selectedCountries.add(selectedCountry);
                    break;
                }
            }
            etUserCountry.setText(selectedCountry.getName());
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
            if (selectedCountry != null && selectedCountry.getId() != null) {
                if (Util.isConnected(this)) {
                    if (etUserState.getVisibility() == View.VISIBLE) {
                        profilePresenter.getProfileLocationData(selectedCountries.get(0).getId(),
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.STATE_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    }
                }
            }
        } else if (type.equals("Select State")) {
            selectedStates.clear();
            JurisdictionType selectedState = new JurisdictionType();
            for (CustomSpinnerObject state : customSpinnerStates) {
                if (state.isSelected()) {
                    selectedState.setName(state.getName());
                    selectedState.setId(state.get_id());
                    selectedStates.add(selectedState);
                    break;
                }
            }
            etUserState.setText(selectedState.getName());
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
            if (selectedState != null && selectedState.getId() != null) {
                if (etUserCity.getVisibility() == View.VISIBLE) {
                    if (Util.isConnected(this)) {
                        profilePresenter.getProfileLocationData(selectedStates.get(0).getId(),
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.CITY_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    }
                }
                if (etUserDistrict.getVisibility() == View.VISIBLE) {
                    if (Util.isConnected(this)) {
                        profilePresenter.getProfileLocationData(selectedStates.get(0).getId(),
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.DISTRICT_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    }
                }
            }
        } else if (type.equals("Select City")) {
            selectedCities.clear();
            JurisdictionType selectedCity = new JurisdictionType();
            for (CustomSpinnerObject city : customSpinnerCities) {
                if (city.isSelected()) {
                    selectedCity.setName(city.getName());
                    selectedCity.setId(city.get_id());
                    selectedCities.add(selectedCity);
                    break;
                }
            }
            etUserCity.setText(selectedCity.getName());
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
            if (selectedCity != null && selectedCity.getId() != null) {
                if (Util.isConnected(this)) {
                    if (etUserTaluka.getVisibility() == View.VISIBLE) {
                        profilePresenter.getProfileLocationData(selectedCities.get(0).getId(),
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.TALUKA_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    }
                }
            }
        } else if (type.equals("Select District")) {
            selectedDistricts.clear();
            JurisdictionType selectedDistrict = new JurisdictionType();
            for (CustomSpinnerObject district : customSpinnerDistricts) {
                if (district.isSelected()) {
                    selectedDistrict.setName(district.getName());
                    selectedDistrict.setId(district.get_id());
                    selectedDistricts.add(selectedDistrict);
                    break;
                }
            }
            etUserDistrict.setText(selectedDistrict.getName());
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
            if (selectedDistrict != null && selectedDistrict.getId() != null) {
                if (Util.isConnected(this)) {
                    if (etUserTaluka.getVisibility() == View.VISIBLE) {
                        profilePresenter.getProfileLocationData(selectedDistricts.get(0).getId(),
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.TALUKA_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    }
                }
            }
        } else if (type.equals("Select Taluka")) {
            selectedTalukas.clear();
            JurisdictionType selectedTaluka = new JurisdictionType();
            for (CustomSpinnerObject taluka : customSpinnerTalukas) {
                if (taluka.isSelected()) {
                    selectedTaluka.setName(taluka.getName());
                    selectedTaluka.setId(taluka.get_id());
                    selectedTalukas.add(selectedTaluka);
                    break;
                }
            }
            etUserTaluka.setText(selectedTaluka.getName());
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
            if (selectedTaluka != null && selectedTaluka.getId() != null) {
                if (Util.isConnected(this)) {
                    if (etUserCluster.getVisibility() == View.VISIBLE) {
                        profilePresenter.getProfileLocationData(selectedTalukas.get(0).getId(),
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.CLUSTER_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    } else if (etUserVillage.getVisibility() == View.VISIBLE) {
                        profilePresenter.getProfileLocationData(selectedTalukas.get(0).getId(),
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.VILLAGE_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    }
                }
            }
        } else if (type.equals("Select Cluster")) {
            selectedClusters.clear();
            JurisdictionType selectedCluster = new JurisdictionType();
            for (CustomSpinnerObject cluster : customSpinnerClusters) {
                if (cluster.isSelected()) {
                    selectedCluster.setName(cluster.getName());
                    selectedCluster.setId(cluster.get_id());
                    selectedClusters.add(selectedCluster);
                    break;
                }
            }
            etUserCluster.setText(selectedCluster.getName());
            // clear other dependent fields
            selectedVillages.clear();
            customSpinnerVillages.clear();
            etUserVillage.setText("");
            selectedSchools.clear();
            customSpinnerSchools.clear();
            etUserSchool.setText("");
            if (selectedCluster != null && selectedCluster.getId() != null) {
                if (Util.isConnected(this)) {
                    if (etUserVillage.getVisibility() == View.VISIBLE) {
                        profilePresenter.getProfileLocationData(selectedClusters.get(0).getId(),
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.VILLAGE_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    }
                }
            }
        } else if (type.equals("Select Village")) {
            selectedVillages.clear();
            JurisdictionType selectedVillage = new JurisdictionType();
            for (CustomSpinnerObject cluster : customSpinnerVillages) {
                if (cluster.isSelected()) {
                    selectedVillage.setName(cluster.getName());
                    selectedVillage.setId(cluster.get_id());
                    selectedVillages.add(selectedVillage);
                    break;
                }
            }
            etUserVillage.setText(selectedVillage.getName());
            // clear other dependent fields
            selectedSchools.clear();
            customSpinnerSchools.clear();
            etUserSchool.setText("");
            if (selectedVillage != null && selectedVillage.getId() != null) {
                if (Util.isConnected(this)) {
                    if (etUserSchool.getVisibility() == View.VISIBLE) {
                        profilePresenter.getProfileLocationData(selectedVillages.get(0).getId(),
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.SCHOOL_LEVEL, selectedOrg.getId(),
                                selectedProjects.get(0).getId(), selectedRole.getId());
                    }
                }
            }
        } else if (type.equals("Select School")) {
            selectedSchools.clear();
            JurisdictionType selectedSchool = new JurisdictionType();
            for (CustomSpinnerObject school : customSpinnerSchools) {
                if (school.isSelected()) {
                    selectedSchool.setName(school.getName());
                    selectedSchool.setId(school.get_id());
                    selectedSchools.add(selectedSchool);
                    break;
                }
            }
            etUserSchool.setText(selectedSchool.getName());
        }
    }
}
