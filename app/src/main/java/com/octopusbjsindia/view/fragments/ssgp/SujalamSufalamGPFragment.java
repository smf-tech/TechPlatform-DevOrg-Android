package com.octopusbjsindia.view.fragments.ssgp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataResponse;
import com.octopusbjsindia.models.SujalamSuphalam.SSAnalyticsAPIResponse;
import com.octopusbjsindia.models.SujalamSuphalam.SSAnalyticsData;
import com.octopusbjsindia.models.SujalamSuphalam.SSMasterDatabase;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.presenter.ssgp.SujalamSuphalamGPPresenter;
import com.octopusbjsindia.utility.AppEvents;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.HomeActivity;
import com.octopusbjsindia.view.activities.SSActionsActivity;
import com.octopusbjsindia.view.activities.ssgp.GPActionsActivity;
import com.octopusbjsindia.view.adapters.SSAnalyticsAdapter;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SujalamSufalamGPFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener, APIDataListener,
        CustomSpinnerListener {

    private View sujalamSufalamFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private TextView tvStructureView, tvMachineView, tvToggle;
    private Button btnSsView, btnVdfForm;
    private RecyclerView rvSSAnalytics;
    private int viewType = 1;
    private SSAnalyticsAdapter structureAnalyticsAdapter, machineAnalyticsAdapter;
    private ArrayList<SSAnalyticsData> structureAnalyticsDataList = new ArrayList<>();
    private ArrayList<SSAnalyticsData> machineAnalyticsDataList = new ArrayList<>();
    private ArrayList<MasterDataList> masterDataList = new ArrayList<>();
    private SujalamSuphalamGPPresenter presenter;
    private boolean isStructureView, isMachineView, isStateFilter, isDistrictFilter, isTalukaFilter;
    private TextView tvStateFilter, tvDistrictFilter, tvTalukaFilter;
    private ImageView btnFilter;
    private String userStates = "", userStateIds = "", userDistricts = "", userDistrictIds = "",
            userTalukas = "", userTalukaIds = "";
    private ArrayList<CustomSpinnerObject> machineStateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineDistrictList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineTalukaList = new ArrayList<>();
    private boolean isFilterApplied;
    private String selectedStateId = "", selectedDistrictId = "", selectedTalukaId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
            ((HomeActivity) getActivity()).setSyncButtonVisibility(false);

            if ((boolean) getArguments().getSerializable("SHOW_BACK")) {
                ((HomeActivity) getActivity()).showBackArrow();
            }
        }
        AppEvents.trackAppEvent(getString(R.string.ss_screen_visit));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sujalamSufalamFragmentView = inflater.inflate(R.layout.fragment_sujalam_sufalam_gp, container, false);
        return sujalamSufalamFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
        }
        init();
    }

    private void init(){
        progressBarLayout = sujalamSufalamFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = sujalamSufalamFragmentView.findViewById(R.id.pb_profile_act);
        tvStructureView = sujalamSufalamFragmentView.findViewById(R.id.tv_structure_view);
        tvMachineView = sujalamSufalamFragmentView.findViewById(R.id.tv_machine_view);
        tvToggle = sujalamSufalamFragmentView.findViewById(R.id.tv_toggle);
        tvStructureView.setOnClickListener(this);
        tvMachineView.setOnClickListener(this);
        btnSsView = sujalamSufalamFragmentView.findViewById(R.id.btn_ss_view);
        btnSsView.setOnClickListener(this);
        btnSsView.setOnLongClickListener(this);
        btnVdfForm = sujalamSufalamFragmentView.findViewById(R.id.btn_vdf_form);
        btnVdfForm.setOnClickListener(this);
        tvStateFilter = sujalamSufalamFragmentView.findViewById(R.id.tv_state_filter);
        tvDistrictFilter = sujalamSufalamFragmentView.findViewById(R.id.tv_district_filter);
        tvTalukaFilter = sujalamSufalamFragmentView.findViewById(R.id.tv_taluka_filter);
        btnFilter = sujalamSufalamFragmentView.findViewById(R.id.btn_filter);
        btnFilter.setOnClickListener(this);
        rvSSAnalytics = sujalamSufalamFragmentView.findViewById(R.id.rv_ss_analytics);
        rvSSAnalytics.setLayoutManager(new GridLayoutManager(getContext(), 2));

        structureAnalyticsAdapter = new SSAnalyticsAdapter(getActivity(), structureAnalyticsDataList, 1, "Structure List");
        machineAnalyticsAdapter = new SSAnalyticsAdapter(getActivity(), machineAnalyticsDataList, 2, "Machine List");

        setUserLocation();

        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        if(roleAccessList != null) {
            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_VIEW_STRUCTURES)) {
                    isStructureView = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_VIEW_MACHINES)) {
                    isMachineView = true;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_STATE)) {
                    isStateFilter = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_DISTRICT)) {
                    isDistrictFilter = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_TALUKA)) {
                    isTalukaFilter = true;
                    continue;
                }
            }
        }

        if (isStateFilter) {
            tvStateFilter.setOnClickListener(this);
        } else {
            if (Util.getUserObjectFromPref().getUserLocation().getStateId() != null &&
                    Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 1) {
                tvStateFilter.setOnClickListener(this);
                machineStateList.clear();
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getStateId().size(); i++) {
                    CustomSpinnerObject customState = new CustomSpinnerObject();
                    customState.set_id(Util.getUserObjectFromPref().getUserLocation().getStateId().get(i).getId());
                    customState.setName(Util.getUserObjectFromPref().getUserLocation().getStateId().get(i).getName());
                    machineStateList.add(customState);
                }
            }
        }
        if (isDistrictFilter) {
            tvDistrictFilter.setOnClickListener(this);
        } else {
            if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null &&
                    Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 1) {
                tvDistrictFilter.setOnClickListener(this);
                machineDistrictList.clear();
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size(); i++) {
                    CustomSpinnerObject customDistrict = new CustomSpinnerObject();
                    customDistrict.set_id(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(i).getId());
                    customDistrict.setName(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(i).getName());
                    machineDistrictList.add(customDistrict);
                }
            }
        }
        if (isTalukaFilter) {
            tvTalukaFilter.setOnClickListener(this);
        } else {
            if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                    Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 1) {
                tvTalukaFilter.setOnClickListener(this);
                machineTalukaList.clear();
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size(); i++) {
                    CustomSpinnerObject customTaluka = new CustomSpinnerObject();
                    customTaluka.set_id(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i).getId());
                    customTaluka.setName(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i).getName());
                    machineTalukaList.add(customTaluka);
                }
            }
        }
        setStructureView();
    }

    private void setUserLocation() {
        if (Util.getUserObjectFromPref().getUserLocation().getStateId() != null &&
                Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 0) {
            userStates = "";
            userStateIds = "";
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getStateId().size(); i++) {
                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getStateId().get(i);
                if (i == 0) {
                    userStates = j.getName();
                    userStateIds = j.getId();
                } else {
                    userStates = userStates + "," + j.getName();
                    userStateIds = userStateIds + "," + j.getId();
                }
            }
            tvStateFilter.setText(userStates);

        } else {
            tvStateFilter.setText("");
        }

        if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 0) {
            userDistricts = "";
            userDistrictIds = "";
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size(); i++) {
                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(i);
                if (i == 0) {
                    userDistricts = j.getName();
                    userDistrictIds = j.getId();
                } else {
                    userDistricts = userDistricts + "," + j.getName();
                    userDistrictIds = userDistrictIds + "," + j.getId();
                }
            }
            tvDistrictFilter.setText(userDistricts);
        } else {
            tvDistrictFilter.setText("");
        }

        if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
            userTalukas = "";
            userTalukaIds = "";
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size(); i++) {
                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i);
                if (i == 0) {
                    userTalukas = j.getName();
                    userTalukaIds = j.getId();
                } else {
                    userTalukas = userTalukas + "," + j.getName();
                    userTalukaIds = userTalukaIds + "," + j.getId();
                }
            }
            tvTalukaFilter.setText(userTalukas);
        } else {
            tvTalukaFilter.setText("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserLocation();
        btnSsView.setEnabled(true);
        btnVdfForm.setEnabled(true);
        isFilterApplied = false;
        btnFilter.setImageResource(R.drawable.ic_filter);
        presenter = new SujalamSuphalamGPPresenter(this);
        if(Util.isConnected(getActivity())) {
            presenter.getAnalyticsData(presenter.GET_STRUCTURE_ANALYTICS,
                    "", "", "");
            presenter.getAnalyticsData(presenter.GET_MACHINE_ANALYTICS,
                    "", "", "");
            presenter.getSSMasterData();
        } else {
            Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_structure_view:
                setStructureView();
                break;
            case R.id.tv_machine_view:
                setMachineView();
                break;
            case R.id.btn_ss_view:
                btnSsView.setEnabled(false);
                intent = new Intent(getActivity(), GPActionsActivity.class);
                intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
                if (viewType == 1) {
                    intent.putExtra("viewType", 1);
                    intent.putExtra("title", "Structure List");
                } else {
                    intent.putExtra("viewType", 2);
                    intent.putExtra("title", "Machine List");
                }
                getActivity().startActivity(intent);
                break;
            case R.id.btn_vdf_form:
                btnVdfForm.setEnabled(false);
                intent = new Intent(getActivity(), SSActionsActivity.class);
                intent.putExtra("SwitchToFragment", "VDFFormFragment");
                getActivity().startActivity(intent);
                break;
            case R.id.tv_state_filter:
                CustomSpinnerDialogClass cdd = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select State",
                        machineStateList,
                        false);
                cdd.show();
                cdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.tv_district_filter:
                if (Util.isConnected(getActivity())) {
                    if (tvStateFilter.getText() != null && tvStateFilter.getText().toString().length() > 0) {
                        presenter.getLocationData((!TextUtils.isEmpty(selectedStateId))
                                        ? selectedStateId : userStateIds, Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                    } else {
                        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                        .findViewById(android.R.id.content), "Your State is not available in your profile." +
                                        "Please update your profile.",
                                Snackbar.LENGTH_LONG);
                    }
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                }
                break;
            case R.id.tv_taluka_filter:
                if (Util.isConnected(getActivity())) {
                    if (tvDistrictFilter.getText() != null && tvDistrictFilter.getText().toString().length() > 0) {
                        presenter.getLocationData((!TextUtils.isEmpty(selectedDistrictId))
                                        ? selectedDistrictId : userDistrictIds,
                                Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.TALUKA_LEVEL);
                    } else {
                        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                        .findViewById(android.R.id.content), "Please select District first.",
                                Snackbar.LENGTH_LONG);
                    }
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                }
                break;
            case R.id.btn_filter:
                if ((!selectedStateId.equals("")) || (!selectedDistrictId.equals("")) || (!selectedTalukaId.equals(""))) {
                    if (!isFilterApplied) {
                        isFilterApplied = true;
                        btnFilter.setImageResource(R.drawable.ic_cancel_filter);

                        presenter.getAnalyticsData(presenter.GET_STRUCTURE_ANALYTICS,
                                selectedStateId, selectedDistrictId, selectedTalukaId);

                        presenter.getAnalyticsData(presenter.GET_MACHINE_ANALYTICS,
                                selectedStateId, selectedDistrictId, selectedTalukaId);
                    } else {
                        isFilterApplied = false;
                        btnFilter.setImageResource(R.drawable.ic_filter);
                        selectedStateId = "";
                        selectedDistrictId = "";
                        selectedTalukaId = "";
                        setUserLocation();
                        presenter.getAnalyticsData(presenter.GET_STRUCTURE_ANALYTICS,
                                "", "", "");

                        presenter.getAnalyticsData(presenter.GET_MACHINE_ANALYTICS,
                                "", "", "");
                    }
                } else {
                    Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Please select filter.",
                            Snackbar.LENGTH_LONG);
                }
                break;
        }
    }

    private void setMachineView(){
        viewType = 2;
        tvMachineView.setTextColor(getResources().getColor(R.color.dark_grey));
        tvMachineView.setTypeface(tvMachineView.getTypeface(), Typeface.BOLD);
        tvStructureView.setTextColor(getResources().getColor(R.color.text_lite_grey));
        tvStructureView.setTypeface(tvStructureView.getTypeface(), Typeface.NORMAL);
        tvToggle.setBackgroundResource(R.drawable.ic_toggle_machine_view);
        rvSSAnalytics.setAdapter(machineAnalyticsAdapter);
        machineAnalyticsAdapter.notifyDataSetChanged();
        if(isMachineView) {
            btnSsView.setVisibility(View.VISIBLE);
            btnSsView.setText("Machine View");
        } else {
            btnSsView.setVisibility(View.INVISIBLE);
        }
        manageUI();
    }

    private void setStructureView(){
        viewType = 1;
        tvStructureView.setTextColor(getResources().getColor(R.color.dark_grey));
        tvStructureView.setTypeface(tvStructureView.getTypeface(), Typeface.BOLD);
        tvMachineView.setTextColor(getResources().getColor(R.color.text_lite_grey));
        tvMachineView.setTypeface(tvMachineView.getTypeface(), Typeface.NORMAL);
        tvToggle.setBackgroundResource(R.drawable.ic_toggle_structure_view);
        rvSSAnalytics.setAdapter(structureAnalyticsAdapter);
        structureAnalyticsAdapter.notifyDataSetChanged();
        if(isStructureView) {
            btnSsView.setVisibility(View.VISIBLE);
            btnSsView.setText("Structure View");
        } else {
            btnSsView.setVisibility(View.INVISIBLE);
        }
        manageUI();
    }

    private void manageUI() {
        if (viewType == 1) {
            if (structureAnalyticsDataList.size() > 0) {
                sujalamSufalamFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.GONE);
            } else {
                sujalamSufalamFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
            }
            rvSSAnalytics.setAdapter(structureAnalyticsAdapter);
        } else {
            if (machineAnalyticsDataList.size() > 0) {
                sujalamSufalamFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.GONE);
            } else {
                sujalamSufalamFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
            }
            rvSSAnalytics.setAdapter(machineAnalyticsAdapter);
        }
        structureAnalyticsAdapter.notifyDataSetChanged();
    }

    public void emptyResponse(String requestCode) {
        if (requestCode.equals(presenter.GET_STRUCTURE_ANALYTICS)) {
            structureAnalyticsDataList.clear();
            manageUI();
        } else if (requestCode.equals(presenter.GET_MACHINE_ANALYTICS)) {
            machineAnalyticsDataList.clear();
            manageUI();
        }
    }

    public void populateAnalyticsData(String requestCode, SSAnalyticsAPIResponse analyticsData) {
        if (analyticsData != null) {
            if(requestCode.equals(presenter.GET_STRUCTURE_ANALYTICS)) {
                structureAnalyticsDataList.clear();
                for (SSAnalyticsData data : analyticsData.getData()) {
                    if (data != null) {
                        structureAnalyticsDataList.add(data);
                    }
                }

            } else if(requestCode.equals(presenter.GET_MACHINE_ANALYTICS)) {
                machineAnalyticsDataList.clear();
                for (SSAnalyticsData data : analyticsData.getData()) {
                    if (data != null) {
                        machineAnalyticsDataList.add(data);
                    }
                }
            }
            manageUI();
        }
    }

    public void setMasterData(MasterDataResponse masterDataResponse) {
        if(masterDataResponse.getStatus()==1000){
            Util.logOutUser(getActivity());
        } else {
            DatabaseManager.getDBInstance(Platform.getInstance()).getSSMasterDatabaseDao().deleteSSMasterData();
            Gson gson = new GsonBuilder().create();
            String ssMasterDataList = gson.toJson(masterDataResponse.getData());
            Date date = Calendar.getInstance().getTime();
            String strDate = Util.getDateFromTimestamp(date.getTime(), Constants.FORM_DATE_FORMAT);
            SSMasterDatabase ssMasterDatabase = new SSMasterDatabase();
            ssMasterDatabase.setDateTime(strDate);
            ssMasterDatabase.setData(ssMasterDataList);
            DatabaseManager.getDBInstance(Platform.getInstance()).getSSMasterDatabaseDao().insert(ssMasterDatabase);
        }
    }

    public void showJurisdictionLevel(List<JurisdictionLocationV3> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    machineTalukaList.clear();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject talukaList = new CustomSpinnerObject();
                        talukaList.set_id(location.getId());
                        talukaList.setName(location.getName());
                        talukaList.setSelected(false);
                        machineTalukaList.add(talukaList);
                    }
                }
                //if (!isTalukaApiFirstCall) {

                CustomSpinnerDialogClass cddTaluka = new CustomSpinnerDialogClass(getActivity(),
                        this, "Select Taluka", machineTalukaList,
                        true);
                cddTaluka.show();
                cddTaluka.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                //}
                break;
            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    machineDistrictList.clear();

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject districtList = new CustomSpinnerObject();
                        districtList.set_id(location.getId());
                        districtList.setName(location.getName());
                        districtList.setSelected(false);
                        machineDistrictList.add(districtList);
                    }
                }
                CustomSpinnerDialogClass cddDistrict = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select District", machineDistrictList,
                        true);
                cddDistrict.show();
                cddDistrict.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);

                break;
            default:
                break;
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (presenter != null) {
            presenter.clearData();
            presenter = null;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        Util.showToast("long click",this);
        return false;
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        if (type.equals("Select State")) {
            String selectedState = "";
            selectedStateId = "";
            for (CustomSpinnerObject state : machineStateList) {
                if (state.isSelected()) {
                    if (selectedState.length() > 0) {
                        selectedState = selectedState + "," + state.getName();
                    } else {
                        selectedState = state.getName();
                    }
                    if (selectedStateId.length() > 0) {
                        selectedStateId = selectedStateId + "," + state.get_id();
                    } else {
                        selectedStateId = state.get_id();
                    }
                }
            }
            tvStateFilter.setText(selectedState);
            tvDistrictFilter.setText("");
            selectedDistrictId = "";
            tvTalukaFilter.setText("");
            selectedTalukaId = "";
            if (isFilterApplied) {
                isFilterApplied = false;
                btnFilter.setImageResource(R.drawable.ic_filter);
            }

        } else if (type.equals("Select District")) {
            String selectedDistrict = "";
            selectedDistrictId = "";
            for (CustomSpinnerObject mDistrict : machineDistrictList) {
                if (mDistrict.isSelected()) {
                    if (selectedDistrict.length() > 0) {
                        selectedDistrict = selectedDistrict + "," + mDistrict.getName();
                    } else {
                        selectedDistrict = mDistrict.getName();
                    }
                    if (selectedDistrictId.length() > 0) {
                        selectedDistrictId = selectedDistrictId + "," + mDistrict.get_id();
                    } else {
                        selectedDistrictId = mDistrict.get_id();
                    }
                }
            }
            tvDistrictFilter.setText(selectedDistrict);
            tvTalukaFilter.setText("");
            selectedTalukaId = "";
            if (isFilterApplied) {
                isFilterApplied = false;
                btnFilter.setImageResource(R.drawable.ic_filter);
            }

        } else if (type.equals("Select Taluka")) {
            String selectedTaluka = "";
            selectedTalukaId = "";
            for (CustomSpinnerObject mTaluka : machineTalukaList) {
                if (mTaluka.isSelected()) {
                    if (selectedTaluka.length() > 0) {
                        selectedTaluka = selectedTaluka + "," + mTaluka.getName();
                    } else {
                        selectedTaluka = mTaluka.getName();
                    }
                    if (selectedTalukaId.length() > 0) {
                        selectedTalukaId = selectedTalukaId + "," + mTaluka.get_id();
                    } else {
                        selectedTalukaId = mTaluka.get_id();
                    }
                }
            }
            tvTalukaFilter.setText(selectedTaluka);
            if (isFilterApplied) {
                isFilterApplied = false;
                btnFilter.setImageResource(R.drawable.ic_filter);
            }
        }
    }
}
