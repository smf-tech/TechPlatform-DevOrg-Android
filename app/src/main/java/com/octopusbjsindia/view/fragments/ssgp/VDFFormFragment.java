package com.octopusbjsindia.view.fragments.ssgp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.SujalamSuphalam.SSMasterDatabase;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.presenter.ssgp.VDFFormFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.ssgp.GPActionsActivity;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.List;

public class VDFFormFragment extends Fragment implements APIDataListener, CustomSpinnerListener,
        View.OnClickListener {

    private View vdfFormFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private EditText etState,etDistrict, etTaluka, etVillage, etStructureType1, etStructCount1, etStructureType2,
            etStructCount2, etStructureType3, etStructCount3, etStructureType4, etStructCount4,
            etStructureType5, etStructCount5, etHours, etMachineType, etMachineCount, etNodalName,
            etNodalContact, etMachineTransport, etFeasibility, etReason, etFutureWorkTime,
            etWorkableStructCount, etRemark, etHoRemark;
    private Button btnSubmit;
    private ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> talukaList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> villageList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> structureTypeList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineTypeList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> transportAgreeOptionsList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> workImmediateOptionsList = new ArrayList<>();
    private VDFFormFragmentPresenter presenter;
    private String userDistrictIds = "";
    private String userTalukaIds = "";
    private String userVillageIds = "";
    private boolean isStateFilter, isDistrictFilter, isTalukaFilter, isVillageFilter;
    private String selectedDistrictId = "", selectedTalukaId, selectedVillageId, selectedTaluka, selectedVillage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vdfFormFragmentView = inflater.inflate(R.layout.fragment_v_d_f_form, container, false);
        return vdfFormFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        ((GPActionsActivity) getActivity()).setTitle("VDF Form");
        progressBarLayout = vdfFormFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = vdfFormFragmentView.findViewById(R.id.pb_profile_act);
        presenter = new VDFFormFragmentPresenter(this);
        etDistrict = vdfFormFragmentView.findViewById(R.id.et_district);
        etTaluka = vdfFormFragmentView.findViewById(R.id.et_taluka);
        etVillage = vdfFormFragmentView.findViewById(R.id.et_village);
        etStructureType1 = vdfFormFragmentView.findViewById(R.id.et_structure_type1);
        etStructCount1 = vdfFormFragmentView.findViewById(R.id.et_struct_count1);
        etStructureType2 = vdfFormFragmentView.findViewById(R.id.et_structure_type2);
        etStructCount2 = vdfFormFragmentView.findViewById(R.id.et_struct_count2);
        etStructureType3 = vdfFormFragmentView.findViewById(R.id.et_structure_type3);
        etStructCount3 = vdfFormFragmentView.findViewById(R.id.et_struct_count3);
        etStructureType4 = vdfFormFragmentView.findViewById(R.id.et_structure_type4);
        etStructCount4 = vdfFormFragmentView.findViewById(R.id.et_struct_count4);
        etStructureType5 = vdfFormFragmentView.findViewById(R.id.et_structure_type5);
        etStructCount5 = vdfFormFragmentView.findViewById(R.id.et_struct_count5);
        etHours = vdfFormFragmentView.findViewById(R.id.et_hours);
        etMachineType = vdfFormFragmentView.findViewById(R.id.et_machine_type);
        etMachineCount = vdfFormFragmentView.findViewById(R.id.et_machine_count);
        etNodalName = vdfFormFragmentView.findViewById(R.id.et_nodal_name);
        etNodalContact = vdfFormFragmentView.findViewById(R.id.et_nodal_contact);
        etMachineTransport = vdfFormFragmentView.findViewById(R.id.et_machine_transport);
        etFeasibility = vdfFormFragmentView.findViewById(R.id.et_feasibility);
        etReason = vdfFormFragmentView.findViewById(R.id.et_reason);
        etFutureWorkTime = vdfFormFragmentView.findViewById(R.id.et_future_work_time);
        etWorkableStructCount = vdfFormFragmentView.findViewById(R.id.et_workable_struct_count);
        etRemark = vdfFormFragmentView.findViewById(R.id.et_remark);
        etHoRemark = vdfFormFragmentView.findViewById(R.id.et_ho_remark);

        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        if (roleAccessList != null) {
            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_STATE)) {
                    isStateFilter = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_DISTRICT)) {
                    isDistrictFilter = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_TALUKA)) {
                    isTalukaFilter = true;
                    continue;
                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_VILLAGE)) {
                    isVillageFilter = true;
                    continue;
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

        if (Util.getUserObjectFromPref().getUserLocation().getVillageIds() != null) {
            for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getVillageIds().size(); i++) {
                JurisdictionType j = Util.getUserObjectFromPref().getUserLocation().getVillageIds().get(i);
                if (i == 0) {
                    userVillageIds = j.getId();
                } else {
                    userVillageIds = userVillageIds + "," + j.getId();
                }
            }
        }

        etDistrict.setFocusable(false);
        etDistrict.setLongClickable(false);
        etTaluka.setFocusable(false);
        etTaluka.setLongClickable(false);
        etVillage.setFocusable(false);
        etVillage.setLongClickable(false);
        etStructureType1.setFocusable(false);
        etStructureType1.setLongClickable(false);
        etStructCount1.setFocusable(false);
        etStructCount1.setLongClickable(false);
        etStructCount2.setFocusable(false);
        etStructCount2.setLongClickable(false);
        etStructCount3.setFocusable(false);
        etStructCount3.setLongClickable(false);
        etStructCount4.setFocusable(false);
        etStructCount4.setLongClickable(false);
        etStructCount5.setFocusable(false);
        etStructCount5.setLongClickable(false);
        etHours.setFocusable(false);
        etHours.setLongClickable(false);
        etMachineType.setFocusable(false);
        etMachineType.setLongClickable(false);
        etMachineCount.setFocusable(false);
        etMachineCount.setLongClickable(false);
        etNodalName.setFocusable(false);
        etNodalName.setLongClickable(false);
        etNodalContact.setFocusable(false);
        etNodalContact.setLongClickable(false);
        etMachineTransport.setFocusable(false);
        etMachineTransport.setLongClickable(false);
        etFeasibility.setLongClickable(false);
        etFeasibility.setFocusable(false);
        etReason.setLongClickable(false);
        etReason.setFocusable(false);
        etFutureWorkTime.setLongClickable(false);
        etFutureWorkTime.setFocusable(false);
        etWorkableStructCount.setLongClickable(false);
        etWorkableStructCount.setFocusable(false);
        etRemark.setLongClickable(false);
        etRemark.setLongClickable(false);
        etHoRemark.setLongClickable(false);
        etHoRemark.setFocusable(false);

        etDistrict.setOnClickListener(this);
        etTaluka.setOnClickListener(this);
        etVillage.setOnClickListener(this);
        etStructureType1.setOnClickListener(this);
        etStructCount1.setOnClickListener(this);
        etStructureType2.setOnClickListener(this);
        etStructCount2.setOnClickListener(this);
        etStructureType3.setOnClickListener(this);
        etStructCount3.setOnClickListener(this);
        etStructureType4.setOnClickListener(this);
        etStructCount4.setOnClickListener(this);
        etStructureType5.setOnClickListener(this);
        etStructCount5.setOnClickListener(this);
        etHours.setOnClickListener(this);
        etMachineType.setOnClickListener(this);
        etMachineCount.setOnClickListener(this);
        etNodalName.setOnClickListener(this);
        etNodalContact.setOnClickListener(this);
        etMachineTransport.setOnClickListener(this);
        etFeasibility.setOnClickListener(this);
        etReason.setOnClickListener(this);
        etFutureWorkTime.setOnClickListener(this);
        etWorkableStructCount.setOnClickListener(this);
        etRemark.setOnClickListener(this);
        etHoRemark.setOnClickListener(this);

        if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 0) {
            etDistrict.setText(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName());
            selectedDistrictId = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId();
        }
        if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
            etTaluka.setText(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName());
            selectedTalukaId = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId();
        }
        if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
            etVillage.setText(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName());
            selectedVillageId = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId();
        }

        if (isDistrictFilter) {
            etDistrict.setOnClickListener(this);
        } else {
            if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 1) {
                etDistrict.setOnClickListener(this);
                districtList.clear();
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size(); i++) {
                    CustomSpinnerObject customDistrict = new CustomSpinnerObject();
                    customDistrict.set_id(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(i).getId());
                    customDistrict.setName(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(i).getName());
                    districtList.add(customDistrict);
                }
            }
        }

        if (isTalukaFilter) {
            etTaluka.setOnClickListener(this);
        } else {
            if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 1) {
                etTaluka.setOnClickListener(this);
                talukaList.clear();
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size(); i++) {
                    CustomSpinnerObject customTaluka = new CustomSpinnerObject();
                    customTaluka.set_id(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i).getId());
                    customTaluka.setName(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(i).getName());
                    talukaList.add(customTaluka);
                }
            }
        }

        if (isVillageFilter) {
            etVillage.setOnClickListener(this);
        } else {
            if (Util.getUserObjectFromPref().getUserLocation().getVillageIds().size() > 1) {
                etVillage.setOnClickListener(this);
                villageList.clear();
                for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getVillageIds().size(); i++) {
                    CustomSpinnerObject customTaluka = new CustomSpinnerObject();
                    customTaluka.set_id(Util.getUserObjectFromPref().getUserLocation().getVillageIds().get(i).getId());
                    customTaluka.setName(Util.getUserObjectFromPref().getUserLocation().getVillageIds().get(i).getName());
                    villageList.add(customTaluka);
                }
            }
        }

        List<SSMasterDatabase> list = DatabaseManager.getDBInstance(Platform.getInstance()).
                getSSMasterDatabaseDao().getSSMasterData("GP");
        String masterDbString = list.get(0).getData();

        Gson gson = new Gson();
        TypeToken<ArrayList<MasterDataList>> token = new TypeToken<ArrayList<MasterDataList>>() {
        };
        ArrayList<MasterDataList> masterDataList = gson.fromJson(masterDbString, token.getType());

        for (int i = 0; i < masterDataList.size(); i++) {
            if (masterDataList.get(i).getForm().equals("machine_create") && masterDataList.get(i).
                    getField().equals("machineType")) {
                for (int j = 0; j < masterDataList.get(i).getData().size(); j++) {
                    CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                    customSpinnerObject.setName(masterDataList.get(i).getData().get(j).getValue());
                    customSpinnerObject.set_id(masterDataList.get(i).getData().get(j).getId());
                    customSpinnerObject.setSelected(false);
                    machineTypeList.add(customSpinnerObject);
                }
            }
            if (masterDataList.get(i).getForm().equals("machine_mou") && masterDataList.get(i).
                    getField().equals("structureType")) {
                for (int j = 0; j < masterDataList.get(i).getData().size(); j++) {
                    CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                    customSpinnerObject.setName(masterDataList.get(i).getData().get(j).getValue());
                    customSpinnerObject.set_id(masterDataList.get(i).getData().get(j).getId());
                    customSpinnerObject.setSelected(false);
                    structureTypeList.add(customSpinnerObject);
                }
            }
        }

        CustomSpinnerObject workingYes = new CustomSpinnerObject();
        workingYes.setName("Yes");
        workingYes.set_id("1");
        workingYes.setSelected(false);
        transportAgreeOptionsList.add(workingYes);

        CustomSpinnerObject workingNo = new CustomSpinnerObject();
        workingNo.setName("No");
        workingNo.set_id("2");
        workingNo.setSelected(false);
        transportAgreeOptionsList.add(workingNo);

        if (!Util.isConnected(getActivity())) {
            Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.et_district:
//                if(districtList.size()>0) {
//                    CustomSpinnerDialogClass cdd6 = new CustomSpinnerDialogClass(getActivity(), this,
//                            "Select District",
//                            districtList,
//                            false);
//                    cdd6.show();
//                    cdd6.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.MATCH_PARENT);
//                } else {
////                    if (Util.isConnected(getActivity())) {
////                        if (etState.getText() != null && etState.getText().toString().length() > 0) {
////                            presenter.getLocationData((!TextUtils.isEmpty(selectedStateId))
////                                            ? selectedStateId : userStateIds, Util.getUserObjectFromPref().getJurisdictionTypeId(),
////                                    Constants.JurisdictionLevelName.DISTRICT_LEVEL);
////                        }
////                    } else {
////                        Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
////                    }
//                }
//                break;
            case R.id.et_taluka:
                if (talukaList.size() > 0) {
                    CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(getActivity(), this,
                            "Select Taluka", talukaList, false);
                    cdd1.show();
                    cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (Util.isConnected(getActivity())) {
                        if (etDistrict.getText() != null && etDistrict.getText().toString().length() > 0) {
                            presenter.getLocationData((!TextUtils.isEmpty(selectedDistrictId))
                                            ? selectedDistrictId : userDistrictIds,
                                    Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.TALUKA_LEVEL);
                        }
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                    }
                }
                break;
            case R.id.et_village:
                if (villageList.size() > 0) {
                    CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(getActivity(), this,
                            "Select village", villageList, false);
                    cdd1.show();
                    cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (Util.isConnected(getActivity())) {
                        if (etTaluka.getText() != null && etTaluka.getText().toString().length() > 0) {
                            presenter.getLocationData((!TextUtils.isEmpty(selectedTalukaId))
                                            ? selectedTalukaId : userTalukaIds,
                                    Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.VILLAGE_LEVEL);
                        }
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                    }
                }
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

    public void showJurisdictionLevel(List<JurisdictionLocationV3> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    districtList.clear();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject district = new CustomSpinnerObject();
                        district.set_id(location.getId());
                        district.setName(location.getName());
                        district.setSelected(false);
                        districtList.add(district);
                    }
                }
                CustomSpinnerDialogClass cdd7 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select District",
                        districtList,
                        false);
                cdd7.show();
                cdd7.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    talukaList.clear();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject talukaList = new CustomSpinnerObject();
                        talukaList.set_id(location.getId());
                        talukaList.setName(location.getName());
                        talukaList.setSelected(false);
                        this.talukaList.add(talukaList);
                    }
                }
                CustomSpinnerDialogClass cdd1 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Taluka", talukaList, false);
                cdd1.show();
                cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case Constants.JurisdictionLevelName.VILLAGE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    villageList.clear();
                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject villageList = new CustomSpinnerObject();
                        villageList.set_id(location.getId());
                        villageList.setName(location.getName());
                        villageList.setSelected(false);
                        this.villageList.add(villageList);
                    }
                }
                CustomSpinnerDialogClass cdd2 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Village", villageList, false);
                cdd2.show();
                cdd2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            default:
                break;
        }
    }

    @Override
    public void closeCurrentActivity() {
        getActivity().finish();
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Select Taluka":
                for (CustomSpinnerObject taluka : talukaList) {
                    if (taluka.isSelected()) {
                        selectedTaluka = taluka.getName();
                        selectedTalukaId = taluka.get_id();
                        break;
                    }
                }
                etTaluka.setText(selectedTaluka);
                break;
            case "Select Village":
                for (CustomSpinnerObject village : talukaList) {
                    if (village.isSelected()) {
                        selectedVillage = village.getName();
                        selectedVillageId = village.get_id();
                        break;
                    }
                }
                etVillage.setText(selectedVillage);
                break;
        }
    }
}