package com.octopusbjsindia.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.AddMemberListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.events.ParametersFilterMember;
import com.octopusbjsindia.models.events.Participant;
import com.octopusbjsindia.models.profile.JurisdictionLocation;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.models.profile.Organization;
import com.octopusbjsindia.models.profile.OrganizationProject;
import com.octopusbjsindia.models.profile.OrganizationRole;
import com.octopusbjsindia.presenter.AddMemberFilterActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("CanBeFinal")
public class AddMembersFilterActivity extends BaseActivity implements AddMemberListener,
        View.OnClickListener, APIDataListener, CustomSpinnerListener {

    private EditText etUserState, etUserDistrict, etUserTaluka, etUserVillage;
    private EditText etUserRole;
    private ArrayList<CustomSpinnerObject> selectionRolesList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> customSpinnerStates = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> customSpinnerDistricts = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> customSpinnerTalukas = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> customSpinnerVillages = new ArrayList<>();

    private List<OrganizationRole> roles = new ArrayList<>();
    private ArrayList<String> selectedRolesId = new ArrayList<>();
    private ArrayList<String> selectedRolesJurisdictionTypeId = new ArrayList<>();

    private ArrayList<JurisdictionType> selectedStates = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedDistricts = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedTalukas = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedClusters = new ArrayList<>();
    private ArrayList<JurisdictionType> selectedVillages = new ArrayList<>();

    private ImageView backButton;
    private Button btApplyFilters;

    private AddMemberFilterActivityPresenter addMemberFilerPresenter;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;

    private String eventID;
    private ArrayList<Participant> oldMembersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_filer);

        addMemberFilerPresenter = new AddMemberFilterActivityPresenter(this);
        addMemberFilerPresenter.getOrganizationRoles(Util.getUserObjectFromPref().getOrgId(), Util.getUserObjectFromPref().
                getProjectIds().get(0).getId());
        initView();
    }

    private void initView() {
        setActionbar(getString(R.string.filter));
        oldMembersList = (ArrayList<Participant>) getIntent().getSerializableExtra(Constants.Planner.MEMBERS_LIST);
        eventID = getIntent().getStringExtra(Constants.Planner.EVENT_TASK_ID);

        progressBarLayout = findViewById(R.id.profile_act_progress_bar);
        progressBar = findViewById(R.id.pb_profile_act);
        backButton = findViewById(R.id.toolbar_back_action);
        etUserRole = findViewById(R.id.etUserRole);
        etUserState = findViewById(R.id.etUserState);
        etUserDistrict = findViewById(R.id.etUserDistrict);
        etUserTaluka = findViewById(R.id.etUserTaluka);
        etUserVillage = findViewById(R.id.etUserVillage);
        btApplyFilters = findViewById(R.id.bt_apply_filters);
        setListeners();
    }

    private void setListeners() {
        backButton.setOnClickListener(this);
        btApplyFilters.setOnClickListener(this);
        etUserRole.setOnClickListener(this);
        etUserState.setOnClickListener(this);
        etUserDistrict.setOnClickListener(this);
        etUserTaluka.setOnClickListener(this);
        etUserVillage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_action:
                onBackPressed();
                break;

            case R.id.bt_apply_filters:
                submitDetails();
                break;

            case R.id.etUserRole:
                if (selectionRolesList.size() > 0) {
                    CustomSpinnerDialogClass cddProject = new CustomSpinnerDialogClass(this, this, "Select Role",
                            selectionRolesList, false);
                    cddProject.show();
                    cddProject.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
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
                    if(selectedRolesJurisdictionTypeId.size()!=0) {
//                        addMemberFilerPresenter.getLocationData("",
//                                selectedRolesJurisdictionTypeId.get(0),
//                                Constants.JurisdictionLevelName.STATE_LEVEL);
                        addMemberFilerPresenter.getLocationDataV3("",
                                "", "",
                                selectedRolesJurisdictionTypeId.get(0),
                                Constants.JurisdictionLevelName.STATE_LEVEL);
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
                            if(selectedRolesJurisdictionTypeId.size()!=0) {
//                                addMemberFilerPresenter.getLocationData(selectedStates.get(0).getId(),
//                                        selectedRolesJurisdictionTypeId.get(0),
//                                        Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                                addMemberFilerPresenter.getLocationDataV3(selectedStates.get(0).getId(),
                                        "", "",
                                        selectedRolesJurisdictionTypeId.get(0),
                                        Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                            }
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
                        if (selectedDistricts != null && selectedDistricts.size() > 0 &&
                                !TextUtils.isEmpty(selectedDistricts.get(0).getId())) {
                            if (etUserTaluka.getVisibility() == View.VISIBLE) {
                                if(selectedRolesJurisdictionTypeId.size()!=0) {
//                                    addMemberFilerPresenter.getLocationData(selectedDistricts.get(0).getId(),
//                                            selectedRolesJurisdictionTypeId.get(0),
//                                            Constants.JurisdictionLevelName.TALUKA_LEVEL);
                                    addMemberFilerPresenter.getLocationDataV3(selectedStates.get(0).getId(),
                                            selectedDistricts.get(0).getId(), "",
                                            selectedRolesJurisdictionTypeId.get(0),
                                            Constants.JurisdictionLevelName.TALUKA_LEVEL);
                                }
                            }
                        } else {
                            Toast.makeText(this, getString(R.string.msg_select_district), Toast.LENGTH_LONG).show();
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
                    if (selectedTalukas != null && selectedTalukas.size() > 0 &&
                            !TextUtils.isEmpty(selectedTalukas.get(0).getId())) {
                        if (etUserVillage.getVisibility() == View.VISIBLE) {
                            if(selectedRolesJurisdictionTypeId.size()!=0) {
//                                addMemberFilerPresenter.getLocationData(selectedTalukas.get(0).getId(),
//                                        selectedRolesJurisdictionTypeId.get(0),
//                                        Constants.JurisdictionLevelName.VILLAGE_LEVEL);
                                addMemberFilerPresenter.getLocationDataV3(selectedStates.get(0).getId(),
                                        selectedDistricts.get(0).getId(), selectedTalukas.get(0).getId(),
                                        selectedRolesJurisdictionTypeId.get(0),
                                        Constants.JurisdictionLevelName.VILLAGE_LEVEL);
                            }
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.msg_select_taluka), Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void setActionbar(String title) {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
    }

    @Override
    public void showOrganizations(List<Organization> organizations) {

    }

    @Override
    public void showOrganizationProjects(List<OrganizationProject> organizationProjects) {

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
            selectionRolesList.clear();
            for (int i = 0; i < organizationRoles.size(); i++) {
                //org.add(organizations.get(i).getOrgName());
                CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                customSpinnerObject.set_id(organizationRoles.get(i).getId());
                customSpinnerObject.setName(organizationRoles.get(i).getDisplayName());
                selectionRolesList.add(customSpinnerObject);
            }
            //spRole.setItems(roles, getString(R.string.role), this);
        }
    }

    @Override
    public void showJurisdictionLevel(List<JurisdictionLocation> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.STATE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    customSpinnerStates.clear();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
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
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        CustomSpinnerObject district = new CustomSpinnerObject();
                        district.set_id(location.getId());
                        district.setName(location.getName());
                        district.setSelected(false);
                        this.customSpinnerDistricts.add(district);
                    }
                }
                break;

            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
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
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        CustomSpinnerObject village = new CustomSpinnerObject();
                        village.set_id(location.getId());
                        village.setName(location.getName());
                        village.setSelected(false);
                        this.customSpinnerVillages.add(village);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void submitDetails() {
        ParametersFilterMember parametersFilter = new ParametersFilterMember();
        parametersFilter.setOrganizationIds(Util.getUserObjectFromPref().getOrgId());
        parametersFilter.setRoleIds(android.text.TextUtils.join(",", selectedRolesId));
        parametersFilter.setState(commaSeparatedString(selectedStates));
        parametersFilter.setDistrict(commaSeparatedString(selectedDistricts));
        parametersFilter.setTaluka(commaSeparatedString(selectedTalukas));
        parametersFilter.setCluster(commaSeparatedString(selectedClusters));
        parametersFilter.setVillage(commaSeparatedString(selectedVillages));

        addMemberFilerPresenter.getFilterMemberList(parametersFilter);
    }

    public String commaSeparatedString(ArrayList<JurisdictionType> jurisdictionList){
        ArrayList<String> s = new ArrayList<>();
        for (JurisdictionType village : jurisdictionList) {
            s.add(village.getId());
        }
        return android.text.TextUtils.join(",", s);
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

    }

    @Override
    public void showErrorMessage(String result) {
        runOnUiThread(() -> Util.showToast(result, this));
    }

    @Override
    public void showMember(ArrayList<Participant> memberList) {
        if(oldMembersList!=null && oldMembersList.size()>0){
            boolean flagPresent=false;
            boolean isAttended=false;
            int position=0;

            for(int i=0;i<oldMembersList.size();i++){
                for(int j=0;j<memberList.size();j++){
                    flagPresent=false;
                    if(oldMembersList.get(i).getId().equals(memberList.get(j).getId())){
                        position=j;
                        isAttended=oldMembersList.get(i).isAttendedCompleted();
                        flagPresent=true;
                        break;
                    }
                }
                if(flagPresent){
                    memberList.get(position).setMemberSelected(true);
                    memberList.get(position).setAttendedCompleted(isAttended);
                } else {
                    oldMembersList.get(i).setMemberSelected(true);
                    memberList.add(oldMembersList.get(i));
                }
            }
        }

        Intent intent = new Intent(this, AddMembersListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        intent.putExtra(Constants.Planner.IS_NEW_MEMBERS_LIST,true);
        intent.putExtra(Constants.Planner.MEMBERS_LIST,memberList);
        intent.putExtra(Constants.Planner.EVENT_TASK_ID, eventID);
        this.startActivity(intent);
        finish();
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        if (type.equals("Select Role")) {
            selectedRolesId.clear();
            selectedRolesJurisdictionTypeId.clear();
            String selectedRoleName = "";
            for (int i = 0; i < selectionRolesList.size(); i++) {
                if (selectionRolesList.get(i).isSelected()) {
                    selectedRolesId.add(roles.get(i).getId());
                    selectedRoleName = roles.get(i).getDisplayName();
                    selectedRolesJurisdictionTypeId.add(roles.get(i).getProject().getJurisdictionTypeId());
                }
            }
            etUserRole.setText(selectedRoleName);
            if(selectedRolesJurisdictionTypeId.size()!=0) {
//                addMemberFilerPresenter.getLocationData("",
//                        selectedRolesJurisdictionTypeId.get(0),
//                        Constants.JurisdictionLevelName.STATE_LEVEL);
                addMemberFilerPresenter.getLocationDataV3("", "", "",
                        selectedRolesJurisdictionTypeId.get(0),
                        Constants.JurisdictionLevelName.STATE_LEVEL);
            }
        } else if(type.equals("Select State")) {
            selectedStates.clear();
            JurisdictionType selectedState = new JurisdictionType();
            for (CustomSpinnerObject state : customSpinnerStates) {
                if (state.isSelected()) {
                    selectedState.setName(state.getName());
                    selectedState.setId(state.get_id());
                    selectedStates.add(selectedState);
                }
            }
            etUserState.setText(selectedState.getName());
            selectedDistricts.clear();
            customSpinnerDistricts.clear();
            etUserDistrict.setText("");
            selectedTalukas.clear();
            customSpinnerTalukas.clear();
            etUserTaluka.setText("");
            selectedVillages.clear();
            customSpinnerVillages.clear();
            etUserVillage.setText("");
            if(selectedRolesJurisdictionTypeId.size()!=0) {
//                addMemberFilerPresenter.getLocationData(selectedStates.get(0).getId(),
//                        selectedRolesJurisdictionTypeId.get(0),
//                        Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                addMemberFilerPresenter.getLocationDataV3(selectedStates.get(0).getId(), "",
                        "", selectedRolesJurisdictionTypeId.get(0),
                        Constants.JurisdictionLevelName.DISTRICT_LEVEL);
            }
        } else if(type.equals("Select District")) {
            selectedDistricts.clear();
            JurisdictionType selectedDistrict = new JurisdictionType();
            for (CustomSpinnerObject district : customSpinnerDistricts) {
                if (district.isSelected()) {
                    selectedDistrict.setName(district.getName());
                    selectedDistrict.setId(district.get_id());
                    selectedDistricts.add(selectedDistrict);
                }
            }
            etUserDistrict.setText(selectedDistrict.getName());
            selectedTalukas.clear();
            customSpinnerTalukas.clear();
            etUserTaluka.setText("");
            selectedVillages.clear();
            customSpinnerVillages.clear();
            etUserVillage.setText("");
            if(selectedRolesJurisdictionTypeId.size()!=0) {
//                addMemberFilerPresenter.getLocationData(selectedDistricts.get(0).getId(),
//                        selectedRolesJurisdictionTypeId.get(0),
//                        Constants.JurisdictionLevelName.TALUKA_LEVEL);
                addMemberFilerPresenter.getLocationDataV3(selectedStates.get(0).getId(),
                        selectedDistricts.get(0).getId(), "",
                        selectedRolesJurisdictionTypeId.get(0),
                        Constants.JurisdictionLevelName.TALUKA_LEVEL);
            }
        } else if(type.equals("Select Taluka")) {
            selectedTalukas.clear();
            JurisdictionType selectedTaluka = new JurisdictionType();
            for (CustomSpinnerObject taluka : customSpinnerTalukas) {
                if (taluka.isSelected()) {
                    selectedTaluka.setName(taluka.getName());
                    selectedTaluka.setId(taluka.get_id());
                    selectedTalukas.add(selectedTaluka);
                }
            }
            etUserTaluka.setText(selectedTaluka.getName());
            selectedVillages.clear();
            customSpinnerVillages.clear();
            etUserVillage.setText("");
            if(selectedRolesJurisdictionTypeId.size()!=0) {
//                addMemberFilerPresenter.getLocationData(selectedTalukas.get(0).getId(),
//                        selectedRolesJurisdictionTypeId.get(0),
//                        Constants.JurisdictionLevelName.VILLAGE_LEVEL);
                addMemberFilerPresenter.getLocationDataV3(selectedStates.get(0).getId(),
                        selectedDistricts.get(0).getId(), selectedTalukas.get(0).getId(),
                        selectedRolesJurisdictionTypeId.get(0),
                        Constants.JurisdictionLevelName.VILLAGE_LEVEL);
            }
        } else if(type.equals("Select Village")) {
            selectedVillages.clear();
            JurisdictionType selectedVillage = new JurisdictionType();
            for (CustomSpinnerObject village : customSpinnerVillages) {
                if (village.isSelected()) {
                    selectedVillage.setName(village.getName());
                    selectedVillage.setId(village.get_id());
                    selectedVillages.add(selectedVillage);
                }
            }
            etUserVillage.setText(selectedVillage.getName());
        }
    }
}
