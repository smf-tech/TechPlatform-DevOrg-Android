package com.octopusbjsindia.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.SujalamSuphalam.MachineData;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.SujalamSuphalam.SSMasterDatabase;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.models.profile.JurisdictionLocation;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.presenter.MachineMouFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GPSTracker;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.MachineMouActivity;
import com.octopusbjsindia.view.activities.SSActionsActivity;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.List;

public class MachineMouFirstFragment extends Fragment implements APIDataListener, CustomSpinnerListener,
        View.OnClickListener {
    private static final String TAG = "MachineMouFirstFragment";
    private View machineMouFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private MachineMouFragmentPresenter machineMouFragmentPresenter;
    private EditText editOwnerType, etMachineState, etMachineDistrict, etMachineTaluka,
            etMachineType, etYear, etMachineMakeModel, etMeterWorking, etRtoNumber, etChasisNumber,
            etExcavationCapacity, etDieselCapacity, etProviderName, etProviderContact;
    private Button btnFirstPartMou, btnEligilble, btnNotEligible;
    private LinearLayout llEligible;
    private int statusCode;
    private ArrayList<CustomSpinnerObject> ownershipList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineTypesList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> makeModelList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> isMeterWorkingList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineTalukaList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> manufactureYearsList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineStateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineDistrictList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> talukaList = new ArrayList<>();
    private String selectedOwner = "", selectedOwnerId, selectedState, selectedStateId, selectedDistrict, selectedDistrictId,
            selectedTaluka, selectedTalukaId, selectedMachine = "",
            selectedMachineId, selectedMakeModel = "", selectedMakeModelId,
            selectedIsMeterWorking = "", selectedYear, selectedYearId;
    private boolean isMachineEligible, isMachineMou, isStateFilter, isDistrictFilter, isTalukaFilter;
    private GPSTracker gpsTracker;
    private Location location;
    String userStateIds = "";
    String userDistrictIds = "";
    String userTalukaIds = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        machineMouFragmentView = inflater.inflate(R.layout.fragment_machine_mou, container, false);
        return machineMouFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        statusCode = getActivity().getIntent().getIntExtra("statusCode", 0);
        progressBarLayout = machineMouFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = machineMouFragmentView.findViewById(R.id.pb_profile_act);
        editOwnerType = machineMouFragmentView.findViewById(R.id.et_owner_type);
        etMachineState = machineMouFragmentView.findViewById(R.id.et_machine_state);
        etMachineDistrict = machineMouFragmentView.findViewById(R.id.et_machine_district);
        etMachineTaluka = machineMouFragmentView.findViewById(R.id.et_machine_taluka);
        etMachineType = machineMouFragmentView.findViewById(R.id.et_machine_type);
        etYear = machineMouFragmentView.findViewById(R.id.et_year);
        etMachineMakeModel = machineMouFragmentView.findViewById(R.id.et_machine_make_model);
        etMeterWorking = machineMouFragmentView.findViewById(R.id.et_meter_working);
        etRtoNumber = machineMouFragmentView.findViewById(R.id.et_rto_number);
        etChasisNumber = machineMouFragmentView.findViewById(R.id.et_chasis_number);
        etExcavationCapacity = machineMouFragmentView.findViewById(R.id.et_excavation_capacity);
        etDieselCapacity = machineMouFragmentView.findViewById(R.id.et_diesel_capacity);
        etProviderName = machineMouFragmentView.findViewById(R.id.et_provider_name);
        etProviderContact = machineMouFragmentView.findViewById(R.id.et_provider_contact);
        btnFirstPartMou = machineMouFragmentView.findViewById(R.id.btn_first_part_mou);
        llEligible = machineMouFragmentView.findViewById(R.id.ll_eligible);

        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        if (roleAccessList != null) {
            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_ELIGIBLE_MACHINE)) {
                    isMachineEligible = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_MOU_MACHINE)) {
                    isMachineMou = true;
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

        if (Util.getUserObjectFromPref().getUserLocation().getStateId() != null) {
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getStateId().size(); i++) {
                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getStateId().get(i);
                if (i == 0) {
                    userStateIds = j.getId();
                } else {
                    userStateIds = userStateIds + "," + j.getId();
                }
            }
        }

        if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null) {
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size(); i++) {
                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(i);
                if (i == 0) {
                    userDistrictIds = j.getId();
                } else {
                    userDistrictIds = userDistrictIds + "," + j.getId();
                }
            }
        }

        if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null) {
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size(); i++) {
                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i);
                if (i == 0) {
                    userTalukaIds = j.getId();
                } else {
                    userTalukaIds = userTalukaIds + "," + j.getId();
                }
            }
        }

        if (statusCode == Constants.SSModule.MACHINE_NEW_STATUS_CODE) {
            if (isMachineEligible) {
                llEligible.setVisibility(View.VISIBLE);
                btnEligilble = machineMouFragmentView.findViewById(R.id.btn_eligible);
                btnNotEligible = machineMouFragmentView.findViewById(R.id.btn_not_eligible);
                btnEligilble.setOnClickListener(this);
                btnNotEligible.setOnClickListener(this);
                btnFirstPartMou.setVisibility(View.GONE);
            }
        } else if (statusCode == Constants.SSModule.MACHINE_ELIGIBLE_STATUS_CODE) {
            if (isMachineMou) {
                btnFirstPartMou.setOnClickListener(this);
                btnFirstPartMou.setVisibility(View.VISIBLE);
            }
        } else if (statusCode == Constants.SSModule.MACHINE_NON_ELIGIBLE_STATUS_CODE) {
            if (isMachineEligible) {
                llEligible.setVisibility(View.VISIBLE);
                btnEligilble = machineMouFragmentView.findViewById(R.id.btn_eligible);
                btnNotEligible = machineMouFragmentView.findViewById(R.id.btn_not_eligible);
                btnEligilble.setOnClickListener(this);
                btnNotEligible.setVisibility(View.GONE);
                btnFirstPartMou.setVisibility(View.GONE);
            }
        }
        machineMouFragmentPresenter = new MachineMouFragmentPresenter(this);
        if (statusCode == Constants.SSModule.MACHINE_CREATE_STATUS_CODE) {
            btnFirstPartMou.setOnClickListener(this);
            btnFirstPartMou.setVisibility(View.VISIBLE);
            btnFirstPartMou.setText("Create Machine");
            setUIForMachineCreate();
        } else if (statusCode == Constants.SSModule.MACHINE_MOU_EXPIRED_STATUS_CODE) {
            btnFirstPartMou.setOnClickListener(this);
            btnFirstPartMou.setVisibility(View.VISIBLE);
            setMachineFirstData();
        } else {
            setMachineFirstData();
        }
        gpsTracker = new GPSTracker(getActivity());
    }

    private void setMachineFirstData() {
        editOwnerType.setFocusable(false);
        editOwnerType.setLongClickable(false);
        etMachineState.setFocusable(false);
        etMachineState.setLongClickable(false);
        etMachineDistrict.setFocusable(false);
        etMachineDistrict.setLongClickable(false);
        etMachineTaluka.setFocusable(false);
        etMachineTaluka.setLongClickable(false);
        etMachineType.setFocusable(false);
        etMachineType.setLongClickable(false);
        etYear.setFocusable(false);
        etYear.setLongClickable(false);
        etMachineMakeModel.setFocusable(false);
        etMachineMakeModel.setLongClickable(false);
        etMeterWorking.setFocusable(false);
        etMeterWorking.setLongClickable(false);
        etRtoNumber.setFocusable(false);
        etRtoNumber.setLongClickable(false);
        etChasisNumber.setFocusable(false);
        etChasisNumber.setLongClickable(false);
        etExcavationCapacity.setFocusable(false);
        etExcavationCapacity.setLongClickable(false);
        etDieselCapacity.setFocusable(false);
        etDieselCapacity.setLongClickable(false);
        etProviderName.setFocusable(false);
        etProviderName.setLongClickable(false);
        etProviderContact.setFocusable(false);
        etProviderContact.setLongClickable(false);

        editOwnerType.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getOwnedBy());
        //etUniqueIdNumber.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getMachineCode());
        etMachineState.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getState());
        etMachineDistrict.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getDistrict());
        etMachineTaluka.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getTaluka());
        etMachineType.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getMachinetype());
        etYear.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getManufacturedYear());
        etMachineMakeModel.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getMakeModel());
        etMeterWorking.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getIsMeterWorking());
        etRtoNumber.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getRtoNumber());
        etChasisNumber.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getChasisNo());
        etExcavationCapacity.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getExcavationCapacity());
        etDieselCapacity.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getDiselTankCapacity());
        etProviderName.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getProviderName());
        etProviderContact.setText(((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getProviderContactNumber());
    }

    private void setUIForMachineCreate() {
        editOwnerType.setFocusable(false);
        editOwnerType.setLongClickable(false);
        etMachineState.setFocusable(false);
        etMachineState.setLongClickable(false);
        etMachineDistrict.setFocusable(false);
        etMachineDistrict.setLongClickable(false);
        etMachineTaluka.setFocusable(false);
        etMachineTaluka.setLongClickable(false);
        etMachineType.setFocusable(false);
        etMachineType.setLongClickable(false);
        etYear.setFocusable(false);
        etYear.setLongClickable(false);
        etMachineMakeModel.setFocusable(false);
        etMachineMakeModel.setLongClickable(false);
        etMeterWorking.setFocusable(false);
        etMeterWorking.setLongClickable(false);

        editOwnerType.setOnClickListener(this);
        etMachineType.setOnClickListener(this);
        etYear.setOnClickListener(this);
        etMachineMakeModel.setOnClickListener(this);
        etMeterWorking.setOnClickListener(this);

        if (Util.getUserObjectFromPref().getUserLocation().getStateId() != null &&
                Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 0) {
            etMachineState.setText(Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getName());
            selectedStateId = Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId();
        }
        if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 0) {
            etMachineDistrict.setText(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName());
            selectedDistrictId = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId();
        }
        if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
            etMachineTaluka.setText(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName());
            selectedTalukaId = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId();
        }

        if (isStateFilter) {
            etMachineState.setOnClickListener(this);
        } else {
            if (Util.getUserObjectFromPref().getUserLocation().getStateId().size() > 0) {
                etMachineState.setOnClickListener(this);
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
            etMachineDistrict.setOnClickListener(this);
            machineMouFragmentPresenter.getLocationData(userStateIds,
                    Util.getUserObjectFromPref().getJurisdictionTypeId(), Constants.JurisdictionLevelName.DISTRICT_LEVEL);
        } else {
            if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 0) {
                etMachineDistrict.setOnClickListener(this);
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
            etMachineTaluka.setOnClickListener(this);
            machineMouFragmentPresenter.getLocationData(userDistrictIds,
                    Util.getUserObjectFromPref().getJurisdictionTypeId(), Constants.JurisdictionLevelName.TALUKA_LEVEL);
        } else {
            if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
                etMachineTaluka.setOnClickListener(this);
                machineTalukaList.clear();
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size(); i++) {
                    CustomSpinnerObject customTaluka = new CustomSpinnerObject();
                    customTaluka.set_id(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i).getId());
                    customTaluka.setName(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i).getName());
                    machineTalukaList.add(customTaluka);
                }
            }
        }

        List<SSMasterDatabase> list = DatabaseManager.getDBInstance(Platform.getInstance()).
                getSSMasterDatabaseDao().getSSMasterData();
        String masterDbString = list.get(0).getData();

        Gson gson = new Gson();
        TypeToken<ArrayList<MasterDataList>> token = new TypeToken<ArrayList<MasterDataList>>() {
        };
        ArrayList<MasterDataList> masterDataList = gson.fromJson(masterDbString, token.getType());

        for (int i = 0; i < masterDataList.size(); i++) {
            if (masterDataList.get(i).getForm().equals("machine_create") && masterDataList.get(i).
                    getField().equals("ownedBy")) {
                for (int j = 0; j < masterDataList.get(i).getData().size(); j++) {
                    CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                    customSpinnerObject.setName(masterDataList.get(i).getData().get(j).getValue());
                    customSpinnerObject.set_id(masterDataList.get(i).getData().get(j).getId());
                    customSpinnerObject.setSelected(false);
                    ownershipList.add(customSpinnerObject);
                }
            }
            if (masterDataList.get(i).getForm().equals("machine_create") && masterDataList.get(i).
                    getField().equals("machineType")) {
                for (int j = 0; j < masterDataList.get(i).getData().size(); j++) {
                    CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                    customSpinnerObject.setName(masterDataList.get(i).getData().get(j).getValue());
                    customSpinnerObject.set_id(masterDataList.get(i).getData().get(j).getId());
                    customSpinnerObject.setSelected(false);
                    machineTypesList.add(customSpinnerObject);
                }
            }
            if (masterDataList.get(i).getForm().equals("machine_create") && masterDataList.get(i).
                    getField().equals("machineMake")) {
                for (int j = 0; j < masterDataList.get(i).getData().size(); j++) {
                    CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                    customSpinnerObject.setName(masterDataList.get(i).getData().get(j).getValue());
                    customSpinnerObject.set_id(masterDataList.get(i).getData().get(j).getId());
                    customSpinnerObject.setSelected(false);
                    makeModelList.add(customSpinnerObject);
                }
            }
            if (masterDataList.get(i).getForm().equals("machine_mou") && masterDataList.get(i).
                    getField().equals("manufactured_year")) {
                for (int j = 0; j < masterDataList.get(i).getData().size(); j++) {
                    CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                    customSpinnerObject.setName(masterDataList.get(i).getData().get(j).getValue());
                    customSpinnerObject.set_id(masterDataList.get(i).getData().get(j).getId());
                    customSpinnerObject.setSelected(false);
                    manufactureYearsList.add(customSpinnerObject);
                }
            }
        }
        CustomSpinnerObject workingYes = new CustomSpinnerObject();
        workingYes.setName("Yes");
        workingYes.set_id("1");
        workingYes.setSelected(false);
        isMeterWorkingList.add(workingYes);

        CustomSpinnerObject workingNo = new CustomSpinnerObject();
        workingNo.setName("No");
        workingNo.set_id("2");
        workingNo.setSelected(false);
        isMeterWorkingList.add(workingNo);
    }

    private void setCreateMachineData() {
        MachineData machineData = new MachineData();
        ((MachineMouActivity) getActivity()).getMachineDetailData().setMachine(machineData);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setOwnedBy
                (selectedOwnerId);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setOwnedByValue(selectedOwner);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setState(selectedStateId);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setDistrict
                (selectedDistrictId);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setTaluka
                (selectedTalukaId);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setMachinetype
                (selectedMachineId);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setManufacturedYear
                (selectedYearId);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setMakeModel
                (selectedMakeModelId);
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setIsMeterWorking(
                (etMeterWorking.getText().toString().trim()));
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setRtoNumber
                (etRtoNumber.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setChasisNo(
                (etChasisNumber.getText().toString().trim()));
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setExcavationCapacity
                (etExcavationCapacity.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setDiselTankCapacity
                (etDieselCapacity.getText().toString().trim());
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setProviderName(
                (etProviderName.getText().toString().trim()));
        ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().setProviderContactNumber(
                (etProviderContact.getText().toString().trim()));
        if (Util.isConnected(getActivity())) {
            if (gpsTracker.isGPSEnabled(getActivity(), this)) {
                location = gpsTracker.getLocation();
                if (location != null) {
                    ((MachineMouActivity) getActivity()).getMachineDetailData().setFormLat(String.valueOf(location.getLatitude()));
                    ((MachineMouActivity) getActivity()).getMachineDetailData().setFormLong(String.valueOf(location.getLongitude()));
                }
            }
            machineMouFragmentPresenter.createMachine(((MachineMouActivity) getActivity()).getMachineDetailData());
        } else {
            Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (machineMouFragmentPresenter != null) {
            machineMouFragmentPresenter.clearData();
            machineMouFragmentPresenter = null;
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
//        if (getActivity() != null) {
//            getActivity().onBackPressed();
//        }
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_first_part_mou:
                if (statusCode == Constants.SSModule.MACHINE_CREATE_STATUS_CODE) {
                    if (Util.isConnected(getActivity())) {
                        if (isAllDataValid()) {
                            setCreateMachineData();
                        }
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                    }
                } else {
                    ((MachineMouActivity) getActivity()).openFragment("MachineMouSecondFragment");
                }
                break;
            case R.id.btn_eligible:
                if (Util.isConnected(getActivity())) {
                    machineMouFragmentPresenter.updateMachineStructureStatus(((MachineMouActivity)
                                    getActivity()).getMachineDetailData().getMachine().getId(),
                            ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getMachineCode(),
                            Constants.SSModule.MACHINE_ELIGIBLE_STATUS_CODE, Constants.SSModule.MACHINE_TYPE);
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                }
                break;
            case R.id.btn_not_eligible:
                if (Util.isConnected(getActivity())) {
                    machineMouFragmentPresenter.updateMachineStructureStatus(((MachineMouActivity)
                                    getActivity()).getMachineDetailData().getMachine().getId(),
                            ((MachineMouActivity) getActivity()).getMachineDetailData().getMachine().getMachineCode(),
                            Constants.SSModule.MACHINE_NON_ELIGIBLE_STATUS_CODE, Constants.SSModule.MACHINE_TYPE);
                } else {
                    Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                }
                break;
            case R.id.et_owner_type:
                CustomSpinnerDialogClass cdd = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Ownership Type", ownershipList, false);
                cdd.show();
                cdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_machine_state:
                CustomSpinnerDialogClass cdd6 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select State",
                        machineStateList,
                        false);
                cdd6.show();
                cdd6.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_machine_district:
                CustomSpinnerDialogClass cdd7 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select District",
                        machineDistrictList,
                        false);
                cdd7.show();
                cdd7.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_machine_taluka:
                CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Taluka", machineTalukaList, false);
                cdd1.show();
                cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_machine_type:
                CustomSpinnerDialogClass cdd2 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Machine Type", machineTypesList, false);
                cdd2.show();
                cdd2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_machine_make_model:
                CustomSpinnerDialogClass cdd3 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Machine Make-Model",
                        makeModelList,
                        false);
                cdd3.show();
                cdd3.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_meter_working:
                CustomSpinnerDialogClass cdd4 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select option",
                        isMeterWorkingList,
                        false);
                cdd4.show();
                cdd4.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_year:
                CustomSpinnerDialogClass cdd5 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Year",
                        manufactureYearsList,
                        false);
                cdd5.show();
                cdd5.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
        }
    }
    public boolean isAllDataValid() {
        if (selectedOwnerId == null || selectedOwnerId.length() == 0) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.select_ownership_field), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etMachineState.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.msg_select_state), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etMachineDistrict.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.msg_select_district), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etMachineTaluka.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.msg_select_taluka), Snackbar.LENGTH_LONG);
            return false;
        } else if (selectedMachine.length() == 0) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.select_machine_type), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etYear.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.select_year), Snackbar.LENGTH_LONG);
            return false;
        } else if (selectedMakeModel.length() == 0) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.select_machine_make_model), Snackbar.LENGTH_LONG);
            return false;
        } else if (selectedIsMeterWorking.length() == 0) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.select_meter_working_option), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etRtoNumber.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.enter_rto_number), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etChasisNumber.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.enter_chasis), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etExcavationCapacity.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.enter_excavation_capacity), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etDieselCapacity.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.enter_diesel_capacity), Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etProviderName.getText().toString().trim())) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                    getString(R.string.enter_provider_name), Snackbar.LENGTH_LONG);
            return false;
        } else if (etProviderContact.getText().toString().trim().length() != 10) {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), getString(R.string.enter_provider_contact),
                    Snackbar.LENGTH_LONG);
            return false;
        }
        return true;
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Select Ownership Type":
                for (CustomSpinnerObject ownershipType : ownershipList) {
                    if (ownershipType.isSelected()) {
                        selectedOwner = ownershipType.getName();
                        selectedOwnerId = ownershipType.get_id();
                        break;
                    }
                }
                editOwnerType.setText(selectedOwner);
                break;
            case "Select State":
                for (CustomSpinnerObject state : machineStateList) {
                    if (state.isSelected()) {
                        selectedState = state.getName();
                        selectedStateId = state.get_id();
                        break;
                    }
                }
                etMachineState.setText(selectedState);
            case "Select District":
                for (CustomSpinnerObject district : machineDistrictList) {
                    if (district.isSelected()) {
                        selectedDistrict = district.getName();
                        selectedDistrictId = district.get_id();
                        break;
                    }
                }
                etMachineDistrict.setText(selectedDistrict);
                break;
            case "Select Taluka":
                for (CustomSpinnerObject taluka : machineTalukaList) {
                    if (taluka.isSelected()) {
                        selectedTaluka = taluka.getName();
                        selectedTalukaId = taluka.get_id();
                        break;
                    }
                }
                etMachineTaluka.setText(selectedTaluka);
                break;
            case "Select Machine Type":
                for (CustomSpinnerObject accountType : machineTypesList) {
                    if (accountType.isSelected()) {
                        selectedMachine = accountType.getName();
                        selectedMachineId = accountType.get_id();
                        break;
                    }
                }
                etMachineType.setText(selectedMachine);
                break;
            case "Select Machine Make-Model":
                for (CustomSpinnerObject accountType : makeModelList) {
                    if (accountType.isSelected()) {
                        selectedMakeModel = accountType.getName();
                        selectedMakeModelId = accountType.get_id();
                        break;
                    }
                }
                etMachineMakeModel.setText(selectedMakeModel);
                break;
            case "Select option":
                for (CustomSpinnerObject accountType : isMeterWorkingList) {
                    if (accountType.isSelected()) {
                        selectedIsMeterWorking = accountType.getName();
                        break;
                    }
                }
                etMeterWorking.setText(selectedIsMeterWorking);
                break;
            case "Select Year":
                for (CustomSpinnerObject accountType : manufactureYearsList) {
                    if (accountType.isSelected()) {
                        selectedYear = accountType.getName();
                        selectedYearId = accountType.get_id();
                        break;
                    }
                }
                etYear.setText(selectedYear);
                break;
        }
    }

    public void showResponse(String responseStatus, String requestId, int status) {
        Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                        .findViewById(android.R.id.content), responseStatus,
                Snackbar.LENGTH_LONG);
        if (requestId.equals(MachineMouFragmentPresenter.UPDATE_MACHINE_STATUS)) {
            if (status == 200) {
                getActivity().finish();
                Intent intent = new Intent(getActivity(), SSActionsActivity.class);
                intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
                intent.putExtra("viewType", 2);
                intent.putExtra("title", "Machine List");
                getActivity().startActivity(intent);
            }
        }
    }

    public void showJurisdictionLevel(List<JurisdictionLocation> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    machineTalukaList.clear();
                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getTaluka().getName().compareTo(j2.getTaluka().getName()));
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocation location = jurisdictionLevels.get(i);
                        CustomSpinnerObject talukaList = new CustomSpinnerObject();
                        talukaList.set_id(location.getId());
                        talukaList.setName(location.getName());
                        talukaList.setSelected(false);
                        machineTalukaList.add(talukaList);
                    }
                }
                break;
            default:
                break;
        }
    }
}
