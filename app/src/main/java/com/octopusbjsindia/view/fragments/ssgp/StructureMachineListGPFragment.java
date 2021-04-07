package com.octopusbjsindia.view.fragments.ssgp;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.SujalamSuphalam.MachineData;
import com.octopusbjsindia.models.SujalamSuphalam.MachineListAPIResponse;
import com.octopusbjsindia.models.SujalamSuphalam.StructureData;
import com.octopusbjsindia.models.SujalamSuphalam.StructureListAPIResponse;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.models.ssgp.StructureListData;
import com.octopusbjsindia.models.ssgp.StructureListRasponce;
import com.octopusbjsindia.presenter.ssgp.StructureMachineListGPFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.ssgp.GPActionsActivity;
import com.octopusbjsindia.view.adapters.MutiselectDialogAdapter;
import com.octopusbjsindia.view.adapters.SSStructureListAdapter;
import com.octopusbjsindia.view.adapters.ssgp.GPMachineListAdapter;
import com.octopusbjsindia.view.adapters.ssgp.GPStructureListAdapter;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StructureMachineListGPFragment extends Fragment implements APIDataListener, View.OnClickListener, CustomSpinnerListener {
    private View structureMachineListFragmentView;
    private int viewType;
    private Context context;
    private RecyclerView rvDataList;
    private final ArrayList<MachineData> ssMachineListData = new ArrayList<>();
    private final ArrayList<MachineData> filteredMachineListData = new ArrayList<>();
    private final ArrayList<StructureListData> ssStructureListData = new ArrayList<>();
    private final ArrayList<StructureListData> filteredStructureListData = new ArrayList<>();

    private GPMachineListAdapter gpMachineListAdapter;
    private GPStructureListAdapter gpStructureListAdapter;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private StructureMachineListGPFragmentPresenter presenter;
    private TextView tvStateFilter, tvDistrictFilter, tvTalukaFilter;
    private ArrayList<CustomSpinnerObject> machineStateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineDistrictList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineTalukaList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineTalukaDeployList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> statusList = new ArrayList<>();
    private int mouAction = 0, selectedStatus = 0, shiftAction = 0;
    public boolean isDallyProgress, isDallyProgressValidation, isStructureMaster;
    private FloatingActionButton fbCreate;
    private boolean isTalukaApiFirstCall;
    private ImageView btnFilterClear;
    private String userStates = "", userStateIds = "", userDistricts = "", userDistrictIds = "",
            userTalukas = "", userTalukaIds = "";
    private String selectedStateId = "", selectedDistrictId = "", selectedTalukaId = "";
    private boolean isFABOpen = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        structureMachineListFragmentView = inflater.inflate(R.layout.fragment_structure_machine_list_gp, container, false);
        context = getActivity();
        return structureMachineListFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        viewType = bundle.getInt("viewType");
        selectedStatus = bundle.getInt("selectedStatus");
        init();
    }

    private void init() {
        progressBarLayout = structureMachineListFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = structureMachineListFragmentView.findViewById(R.id.pb_profile_act);
        fbCreate = structureMachineListFragmentView.findViewById(R.id.fb_create);
        fbCreate.setOnClickListener(this);
        tvStateFilter = structureMachineListFragmentView.findViewById(R.id.tv_state_filter);
        tvDistrictFilter = structureMachineListFragmentView.findViewById(R.id.tv_district_filter);
        tvTalukaFilter = structureMachineListFragmentView.findViewById(R.id.tv_taluka_filter);
        btnFilterClear = structureMachineListFragmentView.findViewById(R.id.btn_filter_clear);
        btnFilterClear.setOnClickListener(this);
        setUserLocation();
        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        if (roleAccessList != null) {
            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_DALLY_PROGRESS)) {
                    isDallyProgress = true;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_DALLY_PROGRESS_VALIDATION)) {
                    isDallyProgressValidation = true;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_STRUCTURE_MASTER_GP)) {
                    isStructureMaster = true;
                }

            }
        }

        rvDataList = structureMachineListFragmentView.findViewById(R.id.rv_data_list);
        rvDataList.setLayoutManager(new LinearLayoutManager(getActivity()));
        gpMachineListAdapter = new GPMachineListAdapter(getActivity(), this, filteredMachineListData);
        rvDataList.setAdapter(gpMachineListAdapter);
        gpStructureListAdapter = new GPStructureListAdapter(getActivity(), filteredStructureListData);
        rvDataList.setAdapter(gpStructureListAdapter);
        presenter = new StructureMachineListGPFragmentPresenter(this);


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


        final SwipeRefreshLayout pullToRefresh = structureMachineListFragmentView.findViewById(R.id.pull_to_refresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onResume();
                pullToRefresh.setRefreshing(false);
            }
        });

        //this api call is given for deploy machine function. if user directly clicks on "Make machine available"
        // option in takeMOUAction function, this api call is needed.
        if (tvDistrictFilter.getText() != null && tvDistrictFilter.getText().toString().length() > 0) {
            isTalukaApiFirstCall = true;
            presenter.getLocationData(userDistrictIds,
                    Util.getUserObjectFromPref().getJurisdictionTypeId(),
                    Constants.JurisdictionLevelName.TALUKA_LEVEL);
        }


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
        if (Util.isConnected(getActivity())) {

            if (viewType == 1) {
                if(isStructureMaster){
                    fbCreate.setVisibility(View.VISIBLE);
                } else {
                    fbCreate.setVisibility(View.GONE);
                }
                presenter.getStrucuresList(
                        (userStateIds != "") ? userStateIds : "",
                        (userDistrictIds != "") ? userDistrictIds : "",
                        (userTalukaIds != "") ? userTalukaIds : "");
            } else {
                if(isDallyProgressValidation){
                    fbCreate.setVisibility(View.VISIBLE);
                } else {
                    fbCreate.setVisibility(View.GONE);
                }
                presenter.getTalukaMachinesList(
                        (userStateIds != "") ? userStateIds : "",
                        (userDistrictIds != "") ? userDistrictIds : "",
                        (userTalukaIds != "") ? userTalukaIds : "");
            }
        } else {
            Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
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
    public void onFailureListener(String requestID, String message) {
        Util.showToast(message, this);
        showNoDataMessage();
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        Util.showToast(error.getMessage(), this);
        showNoDataMessage();
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
        getActivity().finish();
    }

    public void populateMachineData(String requestID, MachineListAPIResponse machineListData) {

        ssMachineListData.clear();
        filteredMachineListData.clear();
        if (machineListData != null) {
            if (requestID.equals(presenter.GET_MACHINE_LIST)) {
                ssMachineListData.addAll(machineListData.getData());
                if (selectedStatus != 0) {
                    for (MachineData machineData : ssMachineListData) {
                        if (machineData.getStatusCode() == selectedStatus) {
                            filteredMachineListData.add(machineData);
                        }
                    }
                } else {
                    filteredMachineListData.addAll(ssMachineListData);
                }
                rvDataList.setAdapter(gpMachineListAdapter);
                gpMachineListAdapter.notifyDataSetChanged();
                ((GPActionsActivity) context).setTitle("Machine List (" + filteredMachineListData.size() + ")");
            }
        }
        showNoDataMessage();
    }


    public void populateStructureData(String requestID, StructureListRasponce structureListData) {

        filteredStructureListData.clear();
        ssStructureListData.clear();
        filteredStructureListData.addAll(structureListData.getData());
        ssStructureListData.addAll(structureListData.getData());
        gpStructureListAdapter.notifyDataSetChanged();
        ((GPActionsActivity) context).setTitle("Structure List(" + filteredStructureListData.size() + ")");
        showNoDataMessage();
    }

    public void showNoDataMessage() {
        if (viewType == 1) {
            if (filteredStructureListData.size() > 0) {
                structureMachineListFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.GONE);
            } else {
                structureMachineListFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
            }
        } else {
            if (filteredMachineListData.size() > 0) {
                structureMachineListFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.GONE);
            } else {
                structureMachineListFragmentView.findViewById(R.id.ly_no_data).setVisibility(View.VISIBLE);
            }
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
                if (!isTalukaApiFirstCall) {
                    CustomSpinnerDialogClass cddTaluka = new CustomSpinnerDialogClass(getActivity(),
                            this, "Select Taluka", machineTalukaList,
                            true);
                    cddTaluka.show();
                    cddTaluka.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                }

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
        }
    }

    public void showResponse(String requestId, String responseStatus, int status) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);

        presenter.getTalukaMachinesList(
                (userStateIds != "") ? userStateIds : "",
                (userDistrictIds != "") ? userDistrictIds : "",
                (userTalukaIds != "") ? userTalukaIds : "");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fb_create) {
            if (viewType == 1) {
                if (Util.isConnected(getActivity())) {
                    Intent intent = new Intent(getActivity(), GPActionsActivity.class);
                    intent.putExtra("SwitchToFragment", "VDCSMFormFragment");
                    getActivity().startActivity(intent);
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                }
            } else {
                if (Util.isConnected(getActivity())) {
                    Intent intent = new Intent(getActivity(), GPActionsActivity.class);
                    intent.putExtra("SwitchToFragment", "VDCDPRValidationFormFragment");
                    getActivity().startActivity(intent);
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                }
            }
        } else if (view.getId() == R.id.tv_state_filter) {
            CustomSpinnerDialogClass cdd = new CustomSpinnerDialogClass(getActivity(), this,
                    "Select State",
                    machineStateList,
                    true);
            cdd.show();
            cdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

        } else if (view.getId() == R.id.tv_taluka_filter) {
//            if (machineTalukaList.size() > 0) {
//                CustomSpinnerDialogClass csdTaluka = new CustomSpinnerDialogClass(getActivity(), this,
//                        "Select Taluka", machineTalukaList, false);
//                csdTaluka.show();
//                csdTaluka.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT);
//            } else {
            if (tvDistrictFilter.getText() != null && tvDistrictFilter.getText().toString().length() > 0) {
                isTalukaApiFirstCall = false;
                presenter.getLocationData((!TextUtils.isEmpty(selectedDistrictId))
                                ? selectedDistrictId : userDistrictIds,
                        Util.getUserObjectFromPref().getJurisdictionTypeId(),
                        Constants.JurisdictionLevelName.TALUKA_LEVEL);

            } else {
                Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                                .findViewById(android.R.id.content), "Please select District first.",
                        Snackbar.LENGTH_LONG);
            }
            //}
        } else if (view.getId() == R.id.tv_district_filter) {
//            if (machineDistrictList.size() > 0) {
//                CustomSpinnerDialogClass csdDisttrict = new CustomSpinnerDialogClass(getActivity(), this,
//                        "Select District", machineDistrictList, false);
//                csdDisttrict.show();
//                csdDisttrict.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.MATCH_PARENT);
//            } else {
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
            //}
        } else if (view.getId() == R.id.btn_filter_clear) {
            if (viewType == 1) {
                filteredStructureListData.clear();
                filteredStructureListData.addAll(ssStructureListData);
                gpStructureListAdapter.notifyDataSetChanged();
                ((GPActionsActivity) context).setActivityTitle("Structure List(" + filteredStructureListData.size() + ")");
            } else {
                filteredMachineListData.clear();
                filteredMachineListData.addAll(ssMachineListData);
                rvDataList.setAdapter(gpMachineListAdapter);
                gpMachineListAdapter.notifyDataSetChanged();
                ((GPActionsActivity) context).setActivityTitle("Machine List(" + filteredMachineListData.size() + ")");
            }
            tvStateFilter.setText("");
            if (Util.getUserObjectFromPref().getUserLocation().getStateId() != null &&
                    Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 0) {
                tvStateFilter.setText("");
                tvStateFilter.setText(Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getName());
            }
            tvDistrictFilter.setText("");
            if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null &&
                    Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 0) {
                tvDistrictFilter.setText(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName());
            }
            tvTalukaFilter.setText("");
            if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                    Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
                tvTalukaFilter.setText(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName());
            }
            btnFilterClear.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        if (type.equals("Select State")) {
            ArrayList<String> filterStateIds = new ArrayList<>();
            String selectedState = "";
            selectedStateId = "";
            for (CustomSpinnerObject mState : machineStateList) {
                if (mState.isSelected()) {
                    if (selectedState.equals("")) {
                        selectedState = mState.getName();
                    } else {
                        selectedState = selectedState + "," + mState.getName();
                    }
                    if (selectedStateId.length() > 0) {
                        selectedStateId = selectedStateId + "," + mState.get_id();
                    } else {
                        selectedStateId = mState.get_id();
                    }
                    filterStateIds.add(mState.get_id());
                }
            }
            tvDistrictFilter.setText("");
            selectedDistrictId = "";
            tvTalukaFilter.setText("");
            selectedTalukaId = "";
            if (!TextUtils.isEmpty(selectedStateId)) {
                tvStateFilter.setText(selectedState);
                if (viewType == 1) {
                    filteredStructureListData.clear();
                    for (StructureListData data : ssStructureListData) {
                        for (String stateId : filterStateIds) {
                            if (data.getStateId().equalsIgnoreCase(stateId)) {
                                if (selectedStatus != 0) {
                                    if (data.getStructureStatusCode() == selectedStatus) {
                                        filteredStructureListData.add(data);
                                    }
                                } else {
                                    filteredStructureListData.add(data);
                                }
                                break;
                            }
                        }
                    }
                    gpStructureListAdapter.notifyDataSetChanged();
                    ((GPActionsActivity) context).setTitle("Structure List (" + filteredStructureListData.size() + ")");
                } else {
                    filteredMachineListData.clear();
                    for (MachineData machineData : ssMachineListData) {
                        for (String stateId : filterStateIds) {
                            if (machineData.getStateId().equalsIgnoreCase(stateId)) {
                                if (selectedStatus != 0) {
                                    if (machineData.getStatusCode() == selectedStatus) {
                                        filteredMachineListData.add(machineData);
                                    }
                                } else {
                                    filteredMachineListData.add(machineData);
                                }
                                break;
                            }
                        }
                    }
                    rvDataList.setAdapter(gpMachineListAdapter);
                    gpMachineListAdapter.notifyDataSetChanged();
                    ((GPActionsActivity) context).setTitle("Machine List (" + filteredMachineListData.size() + ")");
                }
                btnFilterClear.setVisibility(View.VISIBLE);
            }
        } else if (type.equals("Select District")) {
            ArrayList<String> filterDistrictIds = new ArrayList<>();
            String selectedDistrict = "";
            selectedDistrictId = "";
            for (CustomSpinnerObject mDistrict : machineDistrictList) {
                if (mDistrict.isSelected()) {
                    if (selectedDistrict.equals("")) {
                        selectedDistrict = mDistrict.getName();
                    } else {
                        selectedDistrict = selectedDistrict + "," + mDistrict.getName();
                    }
                    if (selectedDistrictId.length() > 0) {
                        selectedDistrictId = selectedDistrictId + "," + mDistrict.get_id();
                    } else {
                        selectedDistrictId = mDistrict.get_id();
                    }
                    filterDistrictIds.add(mDistrict.get_id());
                }
            }
            tvTalukaFilter.setText("");
            selectedTalukaId = "";
            if (!TextUtils.isEmpty(selectedDistrictId)) {
                tvDistrictFilter.setText(selectedDistrict);
                if (viewType == 1) {
                    filteredStructureListData.clear();
                    for (StructureListData data : ssStructureListData) {
                        for (String districtId : filterDistrictIds) {
                            if (data.getDistrictId().equalsIgnoreCase(districtId)) {
                                if (selectedStatus != 0) {
                                    if (data.getStructureStatusCode() == selectedStatus) {
                                        filteredStructureListData.add(data);
                                    }
                                } else {
                                    filteredStructureListData.add(data);
                                }
                                break;
                            }
                        }
                    }
                    gpStructureListAdapter.notifyDataSetChanged();
                    ((GPActionsActivity) context).setTitle("Structure List (" + filteredStructureListData.size() + ")");
                } else {
                    filteredMachineListData.clear();
                    for (MachineData machineData : ssMachineListData) {
                        for (String districtId : filterDistrictIds) {
                            if (machineData.getDistrictId().equalsIgnoreCase(districtId)) {
                                if (selectedStatus != 0) {
                                    if (machineData.getStatusCode() == selectedStatus) {
                                        filteredMachineListData.add(machineData);
                                    }
                                } else {
                                    filteredMachineListData.add(machineData);
                                }
                                break;
                            }
                        }
                    }
                    rvDataList.setAdapter(gpMachineListAdapter);
                    gpMachineListAdapter.notifyDataSetChanged();
                    ((GPActionsActivity) context).setTitle("Machine List (" + filteredMachineListData.size() + ")");
                }
                btnFilterClear.setVisibility(View.VISIBLE);
            }
        } else if (type.equals("Select Taluka")) {
            ArrayList<String> filterTalukaIds = new ArrayList<>();
            String selectedTaluka = "";
            for (CustomSpinnerObject mTaluka : machineTalukaList) {
                if (mTaluka.isSelected()) {
                    if (selectedTaluka.equals("")) {
                        selectedTaluka = mTaluka.getName();
                    } else {
                        selectedTaluka = selectedTaluka + "," + mTaluka.getName();
                    }
                    if (selectedTalukaId.length() > 0) {
                        selectedTalukaId = selectedTalukaId + "," + mTaluka.get_id();
                    } else {
                        selectedTalukaId = mTaluka.get_id();
                    }
                    filterTalukaIds.add(mTaluka.get_id());
                }
            }
            if (!TextUtils.isEmpty(selectedTalukaId)) {
                tvTalukaFilter.setText(selectedTaluka);
                if (viewType == 1) {
                    filteredStructureListData.clear();
                    for (StructureListData data : ssStructureListData) {
                        for (String talukaId : filterTalukaIds) {
                            if (data.getTalukaId().equalsIgnoreCase(talukaId)) {
                                if (selectedStatus != 0) {
                                    if (data.getStructureStatusCode() == selectedStatus) {
                                        filteredStructureListData.add(data);
                                    }
                                } else {
                                    filteredStructureListData.add(data);
                                }
                                break;
                            }
                        }
                    }
                    gpStructureListAdapter.notifyDataSetChanged();
                    ((GPActionsActivity) context).setTitle("Structure List (" + filteredStructureListData.size() + ")");
                } else {
                    filteredMachineListData.clear();
                    for (MachineData machineData : ssMachineListData) {
                        for (String talukaId : filterTalukaIds) {
                            if (machineData.getTalukaId().equalsIgnoreCase(talukaId)) {
                                if (selectedStatus != 0) {
                                    if (machineData.getStatusCode() == selectedStatus) {
                                        filteredMachineListData.add(machineData);
                                    }
                                } else {
                                    filteredMachineListData.add(machineData);
                                }
                                break;
                            }
                        }
                    }
                    rvDataList.setAdapter(gpMachineListAdapter);
                    gpMachineListAdapter.notifyDataSetChanged();
                    ((GPActionsActivity) context).setTitle("Machine List (" + filteredMachineListData.size() + ")");
                }
                btnFilterClear.setVisibility(View.VISIBLE);
            }
        }
    }
}
