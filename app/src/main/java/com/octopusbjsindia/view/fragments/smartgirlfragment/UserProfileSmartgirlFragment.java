package com.octopusbjsindia.view.fragments.smartgirlfragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.models.profile.Organization;
import com.octopusbjsindia.models.profile.OrganizationProject;
import com.octopusbjsindia.models.profile.OrganizationRole;
import com.octopusbjsindia.models.profile.Project;
import com.octopusbjsindia.models.profile.UserLocation;
import com.octopusbjsindia.models.user.User;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.presenter.EditProfileActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.EditProfileActivity;
import com.octopusbjsindia.view.adapters.DropDownSelectionAdapter;

import java.util.ArrayList;
import java.util.List;

public class UserProfileSmartgirlFragment extends Fragment implements View.OnClickListener {
    private final String TAG = EditProfileActivity.class.getName();
    private final List<CustomSpinnerObject> filterOrgList = new ArrayList<>();
    private final List<CustomSpinnerObject> filterProjectList = new ArrayList<>();
    View view;
    User user;
    RadioGroup radioGroup;
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
    private String currentPhotoPath = "";
    private String multiLocationLevel = "";
    private String jurisdictionId = "";
    private boolean isRoleSelected;
    private RecyclerView rvOrg;
    private DropDownSelectionAdapter orgDropDownAdapter;
    private String selectedOrgName = "";
    private RecyclerView rvProject;
    private DropDownSelectionAdapter projectDropDownAdapter;
    private String selectedProjectName = "";
    private boolean isOrgSelected = false, isProjectSelected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_userprofile_layout, container, false);

        initViews();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            //   batchId = getArguments().getString("batch_id");
            //     Log.d("batch_id received","-> "+batchId);

        }
    }


    private void initViews() {
        rvOrg = view.findViewById(R.id.rv_org_list);
        /*orgDropDownAdapter = new DropDownSelectionAdapter(filterOrgList, "Organization", this);
        rvOrg.setLayoutManager(new LinearLayoutManager(this));
        rvOrg.setAdapter(orgDropDownAdapter);*/

        rvProject = view.findViewById(R.id.rv_project_list);
        /*projectDropDownAdapter = new DropDownSelectionAdapter(filterProjectList, "Project", this);
        rvProject.setLayoutManager(new LinearLayoutManager(this));
        rvProject.setAdapter(projectDropDownAdapter);*/


        progressBarLayout = view.findViewById(R.id.profile_act_progress_bar);
        progressBar = view.findViewById(R.id.pb_profile_act);
        backButton = view.findViewById(R.id.toolbar_back_action);
        etUserFirstName = view.findViewById(R.id.et_user_first_name);
        etUserMiddleName = view.findViewById(R.id.et_user_middle_name);
        etUserLastName = view.findViewById(R.id.et_user_last_name);
        etUserBirthDate = view.findViewById(R.id.et_user_birth_date);
        etUserMobileNumber = view.findViewById(R.id.et_user_mobile_number);
        etUserEmailId = view.findViewById(R.id.et_user_email_id);
        radioGroup = view.findViewById(R.id.user_gender_group);
        /*radioGroup.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
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
        });*/
        radioGroup.setClickable(false);
        etUserOrganization = view.findViewById(R.id.etUserOrganization);

        etUserProject = view.findViewById(R.id.etUserProject);

        etUserRole = view.findViewById(R.id.etUserRole);
        etUserCountry = view.findViewById(R.id.etUserCountry);
        etUserCountry.setVisibility(View.GONE);
        etUserState = view.findViewById(R.id.etUserState);
        etUserDistrict = view.findViewById(R.id.etUserDistrict);
        etUserCity = view.findViewById(R.id.etUserCity);
        etUserCity.setVisibility(View.GONE);
        etUserTaluka = view.findViewById(R.id.etUserTaluka);
        etUserTaluka.setVisibility(View.GONE);
        etUserCluster = view.findViewById(R.id.etUserCluster);
        etUserCluster.setVisibility(View.GONE);
        etUserVillage = view.findViewById(R.id.etUserVillage);
        etUserVillage.setVisibility(View.GONE);
        etUserSchool = view.findViewById(R.id.etUserSchool);
        etUserSchool.setVisibility(View.GONE);
        imgUserProfilePic = view.findViewById(R.id.user_profile_pic);
        btnProfileSubmit = view.findViewById(R.id.btn_profile_submit);


        if (Platform.getInstance().getAppMode().equals(Constants.App.BJS_MODE)) {
            view.findViewById(R.id.user_geo_location_view).setVisibility(View.GONE);
            view.findViewById(R.id.input_user_address).setVisibility(View.GONE);
        }

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {

            String response = getArguments().getString("memberList");
            user = new Gson().fromJson(response, User.class);
            if (response != null && user.getUserInfo() != null) {
                setEditModeUserData();
            }
        } else {
            etUserMobileNumber.setText(Util.getUserMobileFromPref());
        }
        etUserMobileNumber.setEnabled(false);
        etUserMobileNumber.setFocusable(false);
        etUserMobileNumber.setClickable(false);
    }

    private void setEditModeUserData() {

        UserInfo userInfo = user.getUserInfo();
        String userName = userInfo.getUserName().trim();

        if (userName.split(" ").length == 3) {
            etUserFirstName.setText(userName.split(" ")[0]);
            etUserMiddleName.setText(userName.split(" ")[1]);
            etUserLastName.setText(userName.split(" ")[2]);
        } else if (userName.split(" ").length == 2) {
            etUserFirstName.setText(userName.split(" ")[0]);
            etUserMiddleName.setVisibility(View.GONE);
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
        selectedOrgName = selectedOrg.getOrgName();
        etUserOrganization.setText(selectedOrgName);

        JurisdictionType project = new JurisdictionType();
        project.setId(userInfo.getProjectIds().get(0).getId());
        project.setName(userInfo.getProjectIds().get(0).getName());
        selectedProjects.add(project);

//        CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
//        customSpinnerObject.set_id(userInfo.getProjectIds().get(0).getId());
//        customSpinnerObject.setName(userInfo.getProjectIds().get(0).getName());
//        selectionProjectList.add(customSpinnerObject);

        selectedProjectName = userInfo.getProjectIds().get(0).getName();
        etUserProject.setText(selectedProjectName);

        multiLocationLevel = userInfo.getMultipleLocationLevel().getName();
        jurisdictionId = userInfo.getJurisdictionTypeId();
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
                /*for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getCountryId().size(); i++) {
                    JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getCountryId().get(i);
                    selectedCountries.add(j);
                    if (i == 0) {
                        countryNames = j.getName();
                    } else {
                        countryNames = countryNames + "," + j.getName();
                    }
                }*/
                if (TextUtils.isEmpty(userInfo.getUserLocation().getCountryId().get(0).getName())){
                    etUserCountry.setVisibility(View.GONE);
                }else {
                    etUserCountry.setText(userInfo.getUserLocation().getCountryId().get(0).getName());
                }
            }
        }

        if (userLocation.getStateId() != null && userLocation.getStateId().size() > 0) {
            etUserState.setVisibility(View.VISIBLE);
            selectedStates.clear();
            String stateNames = "";
            if (userInfo.getUserLocation() != null && userInfo.getUserLocation().getStateId() != null
                    && userInfo.getUserLocation().getStateId().size() > 0) {
                /*for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getStateId().size(); i++) {
                    JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getStateId().get(i);
                    selectedStates.add(j);
                    if (i == 0) {
                        stateNames = j.getName();
                    } else {
                        stateNames = stateNames + "," + j.getName();
                    }
                }*/
                etUserState.setText(userInfo.getUserLocation().getStateId().get(0).getName());
            }
        }

        if (userLocation.getDistrictIds() != null && userLocation.getDistrictIds().size() > 0) {
            etUserDistrict.setVisibility(View.VISIBLE);
            selectedDistricts.clear();
            String districtNames = "";
            if (userInfo.getUserLocation() != null && userInfo.getUserLocation().
                    getDistrictIds() != null && userInfo.getUserLocation().getDistrictIds().size() > 0) {
                /*for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size(); i++) {
                    JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(i);
                    selectedDistricts.add(j);
                    if (i == 0) {
                        districtNames = j.getName();
                    } else {
                        districtNames = districtNames + "," + j.getName();
                    }
                }*/
                districtNames = userInfo.getUserLocation().getDistrictIds().get(0).getName();
                if (TextUtils.isEmpty(districtNames)){
                    etUserDistrict.setVisibility(View.GONE);
                }else {
                    etUserDistrict.setText(districtNames);
                }
            }
        }

        if (userLocation.getCityIds() != null && userLocation.getCityIds().size() > 0) {
            etUserCity.setVisibility(View.VISIBLE);
            selectedCities.clear();
            String cityNames = "";
            if (userInfo.getUserLocation() != null && userInfo.getUserLocation().getCityIds() != null
                    && userInfo.getUserLocation().
                    getCityIds().size() > 0) {
                /*for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getCityIds().size(); i++) {
                    JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getCityIds().get(i);
                    selectedCities.add(j);
                    if (i == 0) {
                        cityNames = j.getName();
                    } else {
                        cityNames = cityNames + "," + j.getName();
                    }
                }*/
                cityNames = userInfo.getUserLocation().getCityIds().get(0).getName();
                if (TextUtils.isEmpty(cityNames)){
                    etUserCity.setVisibility(View.GONE);
                }else {
                    etUserCity.setText(cityNames);
                }

            }
        }

        if (userLocation.getTalukaIds() != null && userLocation.getTalukaIds().size() > 0) {
            etUserTaluka.setVisibility(View.VISIBLE);
            selectedTalukas.clear();
            String talukaNames = "";
            if (userInfo.getUserLocation() != null && userInfo.getUserLocation().getTalukaIds() != null
                    && userInfo.getUserLocation().
                    getTalukaIds().size() > 0) {
                /*for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size(); i++) {
                    JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i);
                    selectedTalukas.add(j);
                    if (i == 0) {
                        talukaNames = j.getName();
                    } else {
                        talukaNames = talukaNames + "," + j.getName();
                    }
                }*/
                if (TextUtils.isEmpty(userInfo.getUserLocation().getTalukaIds().get(0).getName())){
                    etUserTaluka.setVisibility(View.GONE);
                }else {
                    etUserTaluka.setText(userInfo.getUserLocation().getTalukaIds().get(0).getName());
                }

            }
        }

        if (userLocation.getVillageIds() != null && userLocation.getVillageIds().size() > 0) {
            etUserVillage.setVisibility(View.VISIBLE);
            selectedVillages.clear();
            String villageNames = "";
            if (userInfo.getUserLocation() != null && userInfo.getUserLocation().getVillageIds() != null
                    && userInfo.getUserLocation().getVillageIds().size() > 0) {
                /*for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getVillageIds().size(); i++) {
                    JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getVillageIds().get(i);
                    selectedVillages.add(j);
                    if (i == 0) {
                        villageNames = j.getName();
                    } else {
                        villageNames = villageNames + "," + j.getName();
                    }
                }*/

                if (TextUtils.isEmpty(userInfo.getUserLocation().getVillageIds().get(0).getName())){
                    etUserVillage.setVisibility(View.GONE);
                }else {
                    etUserVillage.setText(userInfo.getUserLocation().getVillageIds().get(0).getName());
                }

            }
        }

        if (userLocation.getClusterIds() != null && userLocation.getClusterIds().size() > 0) {
            etUserCluster.setVisibility(View.VISIBLE);
            selectedClusters.clear();
            String clusterNames = "";
            if (userInfo.getUserLocation() != null && userInfo.getUserLocation().getClusterIds() != null
                    && userInfo.getUserLocation().getClusterIds().size() > 0) {
                /*for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getClusterIds().size(); i++) {
                    JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getClusterIds().get(i);
                    selectedClusters.add(j);
                    if (i == 0) {
                        clusterNames = j.getName();
                    } else {
                        clusterNames = clusterNames + "," + j.getName();
                    }
                }*/
                if (TextUtils.isEmpty(userInfo.getUserLocation().getClusterIds().get(0).getName())){
                    etUserCluster.setVisibility(View.GONE);
                }else {
                    etUserCluster.setText(userInfo.getUserLocation().getClusterIds().get(0).getName());
                }
            }
        }

        if (userLocation.getSchoolIds() != null && userLocation.getSchoolIds().size() > 0) {
            etUserSchool.setVisibility(View.VISIBLE);
            selectedSchools.clear();
            String schoolNames = "";
            if (userInfo.getUserLocation() != null && userInfo.getUserLocation().getSchoolIds() != null
                    && userInfo.getUserLocation().getSchoolIds().size() > 0) {
                /*for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getSchoolIds().size(); i++) {
                    JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getSchoolIds().get(i);
                    selectedSchools.add(j);
                    if (i == 0) {
                        schoolNames = j.getName();
                    } else {
                        schoolNames = schoolNames + "," + j.getName();
                    }
                }*/
                if (TextUtils.isEmpty(schoolNames)){
                    etUserSchool.setVisibility(View.GONE);
                }else {
                    etUserSchool.setText(schoolNames);
                }

            }
        }

        if (!TextUtils.isEmpty(userInfo.getProfilePic())) {
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_user_avatar);
            requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());

            Glide.with(this)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(userInfo.getProfilePic())
                    .into(imgUserProfilePic);

            /*((TextView) view.findViewById(R.id.user_profile_pic_label))
                    .setText(getString(R.string.update_profile_pic));*/
        }
//        profilePresenter.getOrganizationProjects(this.selectedOrg.getId());
    }


    @Override
    public void onClick(View view) {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
