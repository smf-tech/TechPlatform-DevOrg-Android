package com.platform.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.platform.R;
import com.platform.listeners.ProfileTaskListener;
import com.platform.models.profile.Jurisdiction;
import com.platform.models.profile.JurisdictionType;
import com.platform.models.profile.Location;
import com.platform.models.profile.Organization;
import com.platform.models.profile.OrganizationProject;
import com.platform.models.profile.OrganizationRole;
import com.platform.models.user.UserInfo;
import com.platform.presenter.AddMemberFilerActivityPresenter;
import com.platform.presenter.ProfileActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.widgets.MultiSelectSpinner;

import java.util.ArrayList;
import java.util.List;

public class AddMemberFilerActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

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

            case R.id.btn_profile_submit:

                break;
        }
    }

    private void setActionbar(String title) {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
        switch (adapterView.getId()) {
            case R.id.sp_user_organization:
//                if (getIntent().getStringExtra(Constants.Login.ACTION) != null
//                        && getIntent().getStringExtra(Constants.Login.ACTION)
//                        .equalsIgnoreCase(Constants.Login.ACTION_EDIT)) {
//
//                    UserInfo userInfo = Util.getUserObjectFromPref();
//                    this.selectedOrg = organizations.get(i);
//
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
//
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
//                        profilePresenter.getOrganizationRoles(this.selectedOrg.getId());
//                    }
//                } else {
//                    if (organizations != null && !organizations.isEmpty() && organizations.get(i) != null
//                            && !TextUtils.isEmpty(organizations.get(i).getId())) {
//                        this.selectedOrg = organizations.get(i);
//                        profilePresenter.getOrganizationProjects(organizations.get(i).getId());
//                        profilePresenter.getOrganizationRoles(organizations.get(i).getId());
//                    }
//                }
                break;

            case R.id.sp_role:
//                if (roles != null && !roles.isEmpty() && roles.get(i) != null) {
//
//                    selectedRoles.clear();
//                    selectedRole = roles.get(i);
//                    selectedRoles.add(selectedRole.getDisplayName());
//
//                    List<Jurisdiction> jurisdictions = selectedRole.getProject().getJurisdictions();
//                    if (jurisdictions != null && jurisdictions.size() > 0) {
//                        hideJurisdictionLevel();
//                        for (Jurisdiction j : jurisdictions) {
//                            switch (j.getLevelName()) {
//                                case Constants.JurisdictionLevelName.STATE_LEVEL:
//                                    spState.setVisibility(View.VISIBLE);
//                                    findViewById(R.id.txt_state).setVisibility(View.VISIBLE);
//                                    profilePresenter.getJurisdictionLevelData(selectedOrg.getId(),
//                                            selectedRole.getProject().getJurisdictionTypeId(), j.getLevelName());
//                                    break;
//
//                                case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
//                                    spDistrict.setVisibility(View.VISIBLE);
//                                    findViewById(R.id.txt_district).setVisibility(View.VISIBLE);
//                                    break;
//
//                                case Constants.JurisdictionLevelName.TALUKA_LEVEL:
//                                    spTaluka.setVisibility(View.VISIBLE);
//                                    findViewById(R.id.txt_taluka).setVisibility(View.VISIBLE);
//                                    break;
//
//                                case Constants.JurisdictionLevelName.VILLAGE_LEVEL:
//                                    spVillage.setVisibility(View.VISIBLE);
//                                    findViewById(R.id.txt_village).setVisibility(View.VISIBLE);
//                                    break;
//                            }
//                        }
//                    }
//                }
                break;

            case R.id.sp_user_state:
                if (states != null && !states.isEmpty() && states.get(i) != null) {

                    selectedStates.clear();
                    selectedStates.add(states.get(i));

                    if (spDistrict.getVisibility() == View.VISIBLE) {
//                        addMemberFilerPresenter.getJurisdictionLevelData(selectedOrg.getId(),
//                                selectedRole.getProject().getJurisdictionTypeId(),
//                                Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                    }
                }
                break;

            case R.id.sp_user_structure:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
