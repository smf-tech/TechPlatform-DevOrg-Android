package com.platform.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.platform.R;
import com.platform.listeners.AddMemberListener;
import com.platform.models.events.ParametersFilterMember;
import com.platform.models.events.Participant;
import com.platform.models.profile.JurisdictionType;
import com.platform.models.profile.Location;
import com.platform.models.profile.Organization;
import com.platform.models.profile.OrganizationProject;
import com.platform.models.profile.OrganizationRole;
import com.platform.presenter.AddMemberFilterActivityPresenter;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.widgets.MultiSelectSpinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("CanBeFinal")
public class AddMembersFilterActivity extends BaseActivity implements AddMemberListener,
        View.OnClickListener, MultiSelectSpinner.MultiSpinnerListener {

    private MultiSelectSpinner spOrganization;
    private MultiSelectSpinner spState;
    private MultiSelectSpinner spRole;

    private MultiSelectSpinner spDistrict;
    private MultiSelectSpinner spTaluka;
    private MultiSelectSpinner spCluster;
    private MultiSelectSpinner spVillage;

    private List<Organization> organizations = new ArrayList<>();
//    private List<OrganizationProject> projects = new ArrayList<>();
    private List<OrganizationRole> roles = new ArrayList<>();

    private List<JurisdictionType> states = new ArrayList<>();
    private List<JurisdictionType> districts = new ArrayList<>();
    private List<JurisdictionType> talukas = new ArrayList<>();
    private List<JurisdictionType> clusters = new ArrayList<>();
    private List<JurisdictionType> villages = new ArrayList<>();

//    private ArrayList<String> selectedProjects = new ArrayList<>();
    private ArrayList<String> selectedOrganizations = new ArrayList<>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_filer);

        addMemberFilerPresenter = new AddMemberFilterActivityPresenter(this);
        addMemberFilerPresenter.getOrganizations();

        initView();
    }

    private void initView() {

        setActionbar(getString(R.string.filter));

        progressBarLayout = findViewById(R.id.profile_act_progress_bar);
        progressBar = findViewById(R.id.pb_profile_act);
        backButton = findViewById(R.id.toolbar_back_action);

        spOrganization = findViewById(R.id.sp_user_organization);
        spOrganization.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_ORGANIZATION);

        spRole = findViewById(R.id.sp_role);
        spRole.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_ROLE);

        spState = findViewById(R.id.sp_user_state);
        spState.setSpinnerName(Constants.MultiSelectSpinnerType.SPINNER_STATE);

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
        btApplyFilters.setOnClickListener(this);
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

        }
    }

    private void setActionbar(String title) {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
    }

    @Override
    public void onValuesSelected(boolean[] selected, String spinnerName) {
        switch (spinnerName) {
//            case Constants.MultiSelectSpinnerType.SPINNER_PROJECT:
//                selectedProjects.clear();
//                for (int i = 0; i < selected.length; i++) {
//                    if (selected[i]) {
//                        selectedProjects.add(projects.get(i).getId());
//                    }
//                }
//                break;
            case Constants.MultiSelectSpinnerType.SPINNER_ORGANIZATION:
                selectedOrganizations.clear();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        selectedOrganizations.add(organizations.get(i).getId());
                    }
                }
                //Change this "selectedOrganizations.get(0)" after API changes
                if(this.selectedOrganizations.size()!=0){
                    addMemberFilerPresenter.getOrganizationRoles(this.selectedOrganizations.get(0));
                }
                break;

            case Constants.MultiSelectSpinnerType.SPINNER_ROLE:
                selectedRolesId.clear();
                selectedRolesJurisdictionTypeId.clear();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        selectedRolesId.add(roles.get(i).getId());
                        selectedRolesJurisdictionTypeId.add(roles.get(i).getProject().getJurisdictionTypeId());
                    }
                }
//                addMemberFilerPresenter.getJurisdictionLevelData(this.selectedOrganizations.get(0),
//                        selectedRoles.get(0),selectedRole.getProject().getJurisdictionTypeId(),
//                        Constants.JurisdictionLevelName.STATE_LEVEL);
                addMemberFilerPresenter.getJurisdictionLevelData(this.selectedOrganizations.get(0),
                        selectedRolesJurisdictionTypeId.get(0),
                        Constants.JurisdictionLevelName.STATE_LEVEL);
                break;

            case Constants.MultiSelectSpinnerType.SPINNER_STATE:
                selectedStates.clear();
                for (int i = 0; i < selected.length; i++) {
                    if (selected[i]) {
                        selectedStates.add(states.get(i));
                    }
                }
                if (spDistrict.getVisibility() == View.VISIBLE) {
                    addMemberFilerPresenter.getJurisdictionLevelData(this.selectedOrganizations.get(0),
                            selectedRolesJurisdictionTypeId.get(0),
                            Constants.JurisdictionLevelName.DISTRICT_LEVEL);
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
                    addMemberFilerPresenter.getJurisdictionLevelData(this.selectedOrganizations.get(0),
                            selectedRolesJurisdictionTypeId.get(0),
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
                    addMemberFilerPresenter.getJurisdictionLevelData(this.selectedOrganizations.get(0),
                            selectedRolesJurisdictionTypeId.get(0),
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

//    private void hideJurisdictionLevel() {
//        spState.setVisibility(View.GONE);
//        findViewById(R.id.txt_state).setVisibility(View.GONE);
//        states.clear();
//        selectedStates.clear();
//
//        spDistrict.setVisibility(View.GONE);
//        findViewById(R.id.txt_district).setVisibility(View.GONE);
//        districts.clear();
//        selectedDistricts.clear();
//
//        spTaluka.setVisibility(View.GONE);
//        findViewById(R.id.txt_taluka).setVisibility(View.GONE);
//        talukas.clear();
//        selectedTalukas.clear();
//
//        spCluster.setVisibility(View.GONE);
//        findViewById(R.id.txt_cluster).setVisibility(View.GONE);
//        clusters.clear();
//        selectedClusters.clear();
//
//        spVillage.setVisibility(View.GONE);
//        findViewById(R.id.txt_village).setVisibility(View.GONE);
//        villages.clear();
//        selectedVillages.clear();
//    }

    @Override
    public void showOrganizations(List<Organization> organizations) {
        List<String> org = new ArrayList<>();
        for (int i = 0; i < organizations.size(); i++) {
            org.add(organizations.get(i).getOrgName());
        }

        spOrganization.setItems(org, getString(R.string.organization), this);

//        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddMemberFilerActivity.this,
//                android.R.layout.simple_spinner_item, org);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spOrganization.setAdapter(adapter);

        this.organizations.clear();
        this.organizations.addAll(organizations);

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

            spRole.setItems(roles, getString(R.string.role), this);

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

                    spState.setItems(stateNames, getString(R.string.state), this);

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

                }
                break;

            default:
                break;
        }
    }



    private void submitDetails() {

        ParametersFilterMember parametersFilter = new ParametersFilterMember();
        parametersFilter.setOrganizationIds(android.text.TextUtils.join(",", selectedOrganizations));
        parametersFilter.setRoleIds(android.text.TextUtils.join(",", selectedRolesId));

        ArrayList<String> s = new ArrayList<>();
        for (JurisdictionType state : selectedStates) {
            s.add(state.getId());
        }
        parametersFilter.setState(android.text.TextUtils.join(",", s));

        s = new ArrayList<>();
        for (JurisdictionType district : selectedDistricts) {
            s.add(district.getId());
        }
        parametersFilter.setDistrict(android.text.TextUtils.join(",", s));

        s = new ArrayList<>();
        for (JurisdictionType taluka : selectedTalukas) {
            s.add(taluka.getId());
        }
        parametersFilter.setTaluka(android.text.TextUtils.join(",", s));

        s = new ArrayList<>();
        for (JurisdictionType cluster : selectedClusters) {
            s.add(cluster.getId());
        }
        parametersFilter.setCluster(android.text.TextUtils.join(",", s));

        s = new ArrayList<>();
        for (JurisdictionType village : selectedVillages) {
            s.add(village.getId());
        }
        parametersFilter.setVillage(android.text.TextUtils.join(",", s));

        addMemberFilerPresenter.getFilterMemberList(parametersFilter);
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
    public void showMember(ArrayList<Participant> memberList) {
        Intent intentAddMembersListActivity = new Intent(this, AddMembersListActivity.class);
        intentAddMembersListActivity.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        intentAddMembersListActivity.putExtra(Constants.Planner.IS_NEW_MEMBERS_LIST,true);
        intentAddMembersListActivity.putExtra(Constants.Planner.MEMBERS_LIST,memberList);
        this.startActivity(intentAddMembersListActivity);
        finish();
    }
}
