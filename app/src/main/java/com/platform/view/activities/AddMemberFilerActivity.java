package com.platform.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.platform.R;
import com.platform.listeners.ProfileTaskListener;
import com.platform.models.events.Member;
import com.platform.models.profile.Jurisdiction;
import com.platform.models.profile.JurisdictionType;
import com.platform.models.profile.Location;
import com.platform.models.profile.Organization;
import com.platform.models.profile.OrganizationProject;
import com.platform.models.profile.OrganizationRole;
import com.platform.models.user.UserInfo;
import com.platform.presenter.AddMemberFilerActivityPresenter;
import com.platform.presenter.ProfileActivityPresenter;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.widgets.MultiSelectSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddMemberFilerActivity extends AppCompatActivity implements ProfileTaskListener,
        View.OnClickListener, AdapterView.OnItemSelectedListener,
        MultiSelectSpinner.MultiSpinnerListener {

    private Spinner spOrganization;
    private Spinner spState;
    private Spinner spRole;

    private MultiSelectSpinner spDistrict;
    private MultiSelectSpinner spTaluka;
    private MultiSelectSpinner spCluster;
    private MultiSelectSpinner spVillage;

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

    private OrganizationRole selectedRole;
    private Organization selectedOrg;

    private ImageView backButton;
    private Button btApplyFilters;

    private AddMemberFilerActivityPresenter addMemberFilerPresenter;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_filer);

        addMemberFilerPresenter = new AddMemberFilerActivityPresenter(this);
        addMemberFilerPresenter.getOrganizations();

        initView();
    }

    private void initView() {

        setActionbar(getString(R.string.filter));

        progressBarLayout = findViewById(R.id.profile_act_progress_bar);
        progressBar = findViewById(R.id.pb_profile_act);
        backButton = findViewById(R.id.toolbar_back_action);

        spOrganization = findViewById(R.id.sp_user_organization);

        spRole = findViewById(R.id.sp_role);
        spState = findViewById(R.id.sp_user_state);

        spDistrict = findViewById(R.id.sp_district);
        spDistrict.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_DISTRICT);

        spTaluka = findViewById(R.id.sp_taluka);
        spTaluka.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_TALUKA);

        spCluster = findViewById(R.id.sp_cluster);
        spCluster.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_CLUSTER);

        spVillage = findViewById(R.id.sp_village);
        spVillage.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_VILLAGE);

        btApplyFilters = findViewById(R.id.bt_apply_filters);

        setListeners();
    }

    private void setListeners() {
        backButton.setOnClickListener(this);

        spOrganization.setOnItemSelectedListener(this);
        spState.setOnItemSelectedListener(this);
        spRole.setOnItemSelectedListener(this);

        btApplyFilters.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_action:
                onBackPressed();
                break;

            case R.id.bt_apply_filters:
                ArrayList<Member> membersList = new ArrayList<>();
                membersList.add(new Member("1", "Sagar Mahajan", "DM",true));
                membersList.add(new Member("2", "Kishor Shevkar", "TC",false));
                membersList.add(new Member("3", "Jagruti Devare", "MT",true));
                membersList.add(new Member("4", "Sachin Kakade", "FA",false));
                Intent intentAddMembersListActivity = new Intent(this, AddMembersListActivity.class);
                intentAddMembersListActivity.putExtra(Constants.Planner.IS_NEW_MEMBERS_LIST,true);
                intentAddMembersListActivity.putExtra(Constants.Planner.MEMBERS_LIST,membersList);
                this.startActivity(intentAddMembersListActivity);
                break;



        }
    }

    private void setActionbar(String title) {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.sp_user_organization:
                if (getIntent().getStringExtra(Constants.Login.ACTION) != null
                        && getIntent().getStringExtra(Constants.Login.ACTION)
                        .equalsIgnoreCase(Constants.Login.ACTION_EDIT)) {

                    UserInfo userInfo = Util.getUserObjectFromPref();
                    this.selectedOrg = organizations.get(i);
//                    List<OrganizationProject> projectData = Util.getUserProjectsFromPref(this.selectedOrg.getId()).getData();
//                    if (projectData != null && projectData.size() > 0) {
//                        showOrganizationProjects(projectData);
//
//                        boolean[] selectedValues = new boolean[projectData.size()];
//                        for (int projectIndex = 0; projectIndex < projectData.size(); projectIndex++) {
//                            selectedValues[projectIndex]
//                                    = userInfo.getProjectIds().contains(projectData.get(projectIndex).getId());
//                        }
//
//                        spProject.setSelectedValues(selectedValues);
//                        spProject.setPreFilledText();
//                    } else {
//                        profilePresenter.getOrganizationProjects(this.selectedOrg.getId());
//                    }

                    int id = 0;
                    List<OrganizationRole> roleData = Util.getUserRoleFromPref(this.selectedOrg.getId()).getData();
                    if (roleData != null && roleData.size() > 0) {
                        showOrganizationRoles(roleData);
                        for (int roleIndex = 0; roleIndex < roleData.size(); roleIndex++) {
                            if (userInfo.getRoleIds().equals(roleData.get(roleIndex).getId())) {
                                id = roleIndex;
                            }
                        }
                        spRole.setSelection(id);
                    } else {
                        addMemberFilerPresenter.getOrganizationRoles(this.selectedOrg.getId());
                    }
                } else {
                    if (organizations != null && !organizations.isEmpty() && organizations.get(i) != null
                            && !TextUtils.isEmpty(organizations.get(i).getId())) {
                        this.selectedOrg = organizations.get(i);
//                        profilePresenter.getOrganizationProjects(organizations.get(i).getId());
                        addMemberFilerPresenter.getOrganizationRoles(organizations.get(i).getId());
                    }
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
                                    addMemberFilerPresenter.getJurisdictionLevelData(selectedOrg.getId(),
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

                    if (spDistrict.getVisibility() == View.VISIBLE) {
                        addMemberFilerPresenter.getJurisdictionLevelData(selectedOrg.getId(),
                                selectedRole.getProject().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                    }
                }
                break;

        }
    }

    private void hideJurisdictionLevel() {
        spState.setVisibility(View.GONE);
        findViewById(R.id.txt_state).setVisibility(View.GONE);
        states.clear();
        selectedStates.clear();

        spDistrict.setVisibility(View.GONE);
        findViewById(R.id.txt_district).setVisibility(View.GONE);
        districts.clear();
        selectedDistricts.clear();

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
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void showOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
        List<String> org = new ArrayList<>();
        for (int i = 0; i < organizations.size(); i++) {
            org.add(organizations.get(i).getOrgName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddMemberFilerActivity.this,
                android.R.layout.simple_spinner_item, org);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrganization.setAdapter(adapter);
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

            ArrayAdapter<String> adapter = new ArrayAdapter<>(AddMemberFilerActivity.this,
                    android.R.layout.simple_spinner_item, roles);
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

    @Override
    public void showJurisdictionLevel(List<Location> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.STATE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.states.clear();
                    List<String> stateNames = new ArrayList<>();

                    Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getState().getName().compareTo(j2.getState().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        Location location = jurisdictionLevels.get(i);
                        stateNames.add(location.getState().getName());
                        this.states.add(location.getState());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddMemberFilerActivity.this,
                            android.R.layout.simple_spinner_item, stateNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spState.setAdapter(adapter);

                    if (getIntent().getStringExtra(Constants.Login.ACTION) != null
                            && getIntent().getStringExtra(Constants.Login.ACTION)
                            .equalsIgnoreCase(Constants.Login.ACTION_EDIT)) {

                        int id = 0;
                        UserInfo userInfo = Util.getUserObjectFromPref();
                        List<String> stateId = userInfo.getUserLocation().getStateId();
                        for (int i = 0; i < states.size(); i++) {
                            if (stateId.get(0).equals(states.get(i).getId())) {
                                id = i;
                            }
                        }
                        spState.setSelection(id);
                    }
                }
                break;

            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.districts.clear();
                    List<String> districts = new ArrayList<>();

                    Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getDistrict().getName().compareTo(j2.getDistrict().getName()));

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

                    if (Util.getUserObjectFromPref().getUserLocation() != null) {
                        List<String> districtIds = Util.getUserObjectFromPref().getUserLocation().getDistrictIds();
                        if (districtIds != null && districtIds.size() > 0) {
                            boolean[] selectedValues = new boolean[this.districts.size()];
                            for (int districtIndex = 0; districtIndex < this.districts.size(); districtIndex++) {
                                for (int districtIdIndex = 0; districtIdIndex < districtIds.size(); districtIdIndex++) {
                                    if (this.districts.get(districtIndex).getId().equals(districtIds.get(districtIdIndex))) {
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
                break;

            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.talukas.clear();
                    List<String> talukas = new ArrayList<>();

                    Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getTaluka().getName().compareTo(j2.getTaluka().getName()));

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

                    if (Util.getUserObjectFromPref().getUserLocation() != null) {
                        List<String> talukaIds = Util.getUserObjectFromPref().getUserLocation().getTalukaIds();
                        if (talukaIds != null && talukaIds.size() > 0) {
                            boolean[] selectedValues = new boolean[this.talukas.size()];
                            for (int talukaIndex = 0; talukaIndex < this.talukas.size(); talukaIndex++) {
                                for (int talukaIdIndex = 0; talukaIdIndex < talukaIds.size(); talukaIdIndex++) {
                                    if (this.talukas.get(talukaIndex).getId().equals(talukaIds.get(talukaIdIndex))) {
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
                break;

            case Constants.JurisdictionLevelName.VILLAGE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.villages.clear();
                    List<String> villages = new ArrayList<>();

                    Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getVillage().getName().compareTo(j2.getVillage().getName()));

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

                    if (Util.getUserObjectFromPref().getUserLocation() != null) {
                        List<String> villageIds = Util.getUserObjectFromPref().getUserLocation().getVillageIds();
                        if (villageIds != null && villageIds.size() > 0) {
                            boolean[] selectedValues = new boolean[this.villages.size()];
                            for (int villageIndex = 0; villageIndex < this.villages.size(); villageIndex++) {
                                for (int villageIdIndex = 0; villageIdIndex < villageIds.size(); villageIdIndex++) {
                                    if (this.villages.get(villageIndex).getId().equals(villageIds.get(villageIdIndex))) {
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
                break;

            case Constants.JurisdictionLevelName.CLUSTER_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    this.clusters.clear();
                    List<String> clusters = new ArrayList<>();

                    Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getCluster().getName().compareTo(j2.getCluster().getName()));

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

                    if (Util.getUserObjectFromPref().getUserLocation() != null) {
                        List<String> clusterIds = Util.getUserObjectFromPref().getUserLocation().getClusterIds();
                        if (clusterIds != null && clusterIds.size() > 0) {
                            boolean[] selectedValues = new boolean[this.clusters.size()];
                            for (int clusterIndex = 0; clusterIndex < this.clusters.size(); clusterIndex++) {
                                for (int clusterIdIndex = 0; clusterIdIndex < clusterIds.size(); clusterIdIndex++) {
                                    if (this.clusters.get(clusterIndex).getId().equals(clusterIds.get(clusterIdIndex))) {
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

    }

    @Override
    public void showErrorMessage(String result) {
        AppEvents.trackAppEvent(getString(R.string.event_update_profile_fail));
        runOnUiThread(() -> Util.showToast(result, this));
    }

    @Override
    public void onValuesSelected(boolean[] selected, String spinnerName) {
        switch (spinnerName) {
            case Constants.MultiSelectSpinnerType.SPINNER_PROJECT:
                selectedProjects.clear();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        selectedProjects.add(projects.get(i).getId());
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

            case Constants.MultiSelectSpinnerType.SPINNER_DISTRICT:
                selectedDistricts.clear();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        selectedDistricts.add(districts.get(i));
                    }
                }

                if (spTaluka.getVisibility() == View.VISIBLE) {
                    addMemberFilerPresenter.getJurisdictionLevelData(selectedOrg.getId(),
                            selectedRole.getProject().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.TALUKA_LEVEL);
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
                    addMemberFilerPresenter.getJurisdictionLevelData(selectedOrg.getId(),
                            selectedRole.getProject().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.VILLAGE_LEVEL);
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
    }
}
