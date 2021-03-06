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
import com.octopusbjsindia.models.ssgp.StructureWorkType;
import com.octopusbjsindia.models.ssgp.VACStructureMasterRequest;
import com.octopusbjsindia.models.ssgp.VDFFRequest;
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
    private EditText etState, etDistrict, etTaluka, etVillage, etStructureType1, etStructCount1, etStructureType2,
            etStructCount2, etStructureType3, etStructCount3, etStructureType4, etStructCount4,
            etStructureType5, etStructCount5, etHours, etMachineType, etMachineCount, etNodalName,
            etNodalContact, etMachineTransport, etFeasibility, etReason, etFutureWorkTime,
            etWorkableStructCount, etRemark, etHoRemark, selectedEt;
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
    private String selectedDistrictId, selectedTalukaId, selectedVillageId, selectedStructureType1, selectedStructureTypeId1,
            selectedStructureType2, selectedStructureTypeId2, selectedStructureType3, selectedStructureTypeId3,
            selectedStructureType4, selectedStructureTypeId4, selectedStructureType5, selectedStructureTypeId5;

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

        etStructureType1.setOnClickListener(this);
        etStructureType2.setOnClickListener(this);
        etStructureType3.setOnClickListener(this);
        etStructureType4.setOnClickListener(this);
        etStructureType5.setOnClickListener(this);

//        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
//        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
//        if (roleAccessList != null) {
//            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
//            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
//                if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_STATE)) {
//                    isStateFilter = true;
//                    continue;
//                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_DISTRICT)) {
//                    isDistrictFilter = true;
//                    continue;
//                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_TALUKA)) {
//                    isTalukaFilter = true;
//                    continue;
//                } else if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_VILLAGE)) {
//                    isVillageFilter = true;
//                    continue;
//                }
//            }
//        }

        if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 0) {
            etDistrict.setText(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName());
            selectedDistrictId = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId();
        } else {
            etDistrict.setOnClickListener(this);
        }
        if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
            etTaluka.setText(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName());
            selectedTalukaId = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId();
        } else {
            etTaluka.setOnClickListener(this);
        }
        if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
            etVillage.setText(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName());
            selectedVillageId = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId();
        } else {
            etVillage.setOnClickListener(this);
        }

        List<SSMasterDatabase> list = DatabaseManager.getDBInstance(Platform.getInstance()).
                getSSMasterDatabaseDao().getSSMasterData();
        String masterDbString = list.get(0).getData();

        Gson gson = new Gson();
        TypeToken<ArrayList<MasterDataList>> token = new TypeToken<ArrayList<MasterDataList>>() {
        };
        ArrayList<MasterDataList> masterDataList = gson.fromJson(masterDbString, token.getType());

        for (int i = 0; i < masterDataList.size(); i++) {
//            if (masterDataList.get(i).getForm().equals("machine_create") && masterDataList.get(i).
//                    getField().equals("machineType")) {
//                for (int j = 0; j < masterDataList.get(i).getData().size(); j++) {
//                    CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
//                    customSpinnerObject.setName(masterDataList.get(i).getData().get(j).getValue());
//                    customSpinnerObject.set_id(masterDataList.get(i).getData().get(j).getId());
//                    customSpinnerObject.setSelected(false);
//                    machineTypeList.add(customSpinnerObject);
//                }
//            }
            if (masterDataList.get(i).getForm().equals("structure_create") && masterDataList.get(i).
                    getField().equals("structureType")) {
                for (int j = 0; j < masterDataList.get(i).getData().size(); j++) {
                    CustomSpinnerObject customSpinnerObject = new CustomSpinnerObject();
                    customSpinnerObject.setName(masterDataList.get(i).getData().get(j).getValue());
                    customSpinnerObject.set_id(masterDataList.get(i).getData().get(j).getId());
                    customSpinnerObject.setSelected(false);
                    structureTypeList.add(customSpinnerObject);
                    break;
                }
            }
        }

        if (!Util.isConnected(getActivity())) {
            Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_district:
                CustomSpinnerDialogClass cdd6 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select District",
                        districtList,
                        false);
                cdd6.show();
                cdd6.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
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
            case R.id.et_structure_type1:
                selectedEt = etStructureType1;
                selectStructureType();
                break;
            case R.id.et_structure_type2:
                selectedEt = etStructureType2;
                selectStructureType();
                break;
            case R.id.et_structure_type3:
                selectedEt = etStructureType3;
                selectStructureType();
                break;
            case R.id.et_structure_type4:
                selectedEt = etStructureType4;
                selectStructureType();
                break;
            case R.id.et_structure_type5:
                selectedEt = etStructureType5;
                selectStructureType();
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(selectedDistrictId)) {
                    Util.showToast(getActivity(), "Please selected district");
                } else if (TextUtils.isEmpty(selectedTalukaId)) {
                    Util.showToast(getActivity(), "Please selected taluka");
                } else if (TextUtils.isEmpty(selectedVillageId)) {
                    Util.showToast(getActivity(), "Please selected village");
                } else {
                    VDFFRequest request = new VDFFRequest();
                    request.setDistrictId(selectedDistrictId);
                    request.setTalukaId(selectedTalukaId);
                    request.setVillageId(selectedVillageId);

                    List<StructureWorkType> structureWorkTypeList = new ArrayList<StructureWorkType>();
                    StructureWorkType structure1 = new StructureWorkType();
                    structure1.setStructureType(etStructureType1.getText().toString().trim());
                    structure1.setNumberStructureType(etStructCount1.getText().toString().trim());
                    structureWorkTypeList.add(structure1);

                    StructureWorkType structure2 = new StructureWorkType();
                    structure2.setStructureType(etStructureType2.getText().toString().trim());
                    structure2.setNumberStructureType(etStructCount2.getText().toString().trim());
                    structureWorkTypeList.add(structure2);

                    StructureWorkType structure3 = new StructureWorkType();
                    structure3.setStructureType(etStructureType3.getText().toString().trim());
                    structure3.setNumberStructureType(etStructCount3.getText().toString().trim());
                    structureWorkTypeList.add(structure3);

                    StructureWorkType structure4 = new StructureWorkType();
                    structure4.setStructureType(etStructureType4.getText().toString().trim());
                    structure4.setNumberStructureType(etStructCount4.getText().toString().trim());
                    structureWorkTypeList.add(structure4);

                    StructureWorkType structure5 = new StructureWorkType();
                    structure5.setStructureType(etStructureType5.getText().toString().trim());
                    structure5.setNumberStructureType(etStructCount5.getText().toString().trim());
                    structureWorkTypeList.add(structure5);

                    request.setTypeNWorkStructure(structureWorkTypeList);
                    request.setMachineDemandHr(etHours.getText().toString().trim());
                    request.setMachineDemandType(etMachineType.getText().toString().trim());
                    request.setMachineDemandNumbers(etMachineCount.getText().toString().trim());
                    request.setNodalPersonName(etNodalName.getText().toString().trim());
                    request.setNodalPersonNumber(etNodalContact.getText().toString().trim());
                    request.setMachineTransportation(etMachineTransport.getText().toString().trim());
                    request.setIsStartWorkImmediately(etFeasibility.getText().toString().trim());
                    request.setReasonNotStart(etReason.getText().toString().trim());
                    request.setFutureDate(etFutureWorkTime.getText().toString().trim());
//                    request.set(etWorkableStructCount.getText().toString().trim()); // TODO not shure
                    request.setComment(etRemark.getText().toString().trim()); //TODO not shure
                    request.setComment(etHoRemark.getText().toString().trim());
                    presenter.submitVDFF(request);
                }
                break;
        }
    }

    private void selectStructureType() {
        CustomSpinnerDialogClass csdStructerType = new CustomSpinnerDialogClass(getActivity(), this,
                "Select Structure Type", structureTypeList, false);
        csdStructerType.show();
        csdStructerType.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
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
            case "Select District":
                for (CustomSpinnerObject state : districtList) {
                    if (state.isSelected()) {
                        etDistrict.setText(state.getName());
                        selectedDistrictId = state.get_id();
                        break;
                    }
                }
                etTaluka.setText("");
                selectedTalukaId = "";
                etVillage.setText("");
                selectedVillageId = "";
                break;
            case "Select Taluka":
                for (CustomSpinnerObject state : talukaList) {
                    if (state.isSelected()) {
                        etTaluka.setText(state.getName());
                        selectedTalukaId = state.get_id();
                        break;
                    }
                }
                etVillage.setText("");
                selectedVillageId = "";
                break;
            case "Select Village":
                for (CustomSpinnerObject state : districtList) {
                    if (state.isSelected()) {
                        etVillage.setText(state.getName());
                        selectedVillageId = state.get_id();
                        break;
                    }
                }

                break;
            case "Select Structure Type":
                for (CustomSpinnerObject obj : structureTypeList) {
                    if (obj.isSelected()) {
                        switch (selectedEt.getId()) {
                            case R.id.et_structure_type1:
                                selectedStructureType1 = obj.getName();
                                selectedStructureTypeId1 = obj.get_id();
                                selectedEt.setText(selectedStructureType1);
                                break;
                            case R.id.et_structure_type2:
                                selectedStructureType2 = obj.getName();
                                selectedStructureTypeId2 = obj.get_id();
                                selectedEt.setText(selectedStructureType2);
                                break;
                            case R.id.et_structure_type3:
                                selectedStructureType3 = obj.getName();
                                selectedStructureTypeId3 = obj.get_id();
                                selectedEt.setText(selectedStructureType3);
                                break;
                            case R.id.et_structure_type4:
                                selectedStructureType4 = obj.getName();
                                selectedStructureTypeId4 = obj.get_id();
                                selectedEt.setText(selectedStructureType4);
                                break;
                            case R.id.et_structure_type5:
                                selectedStructureType5 = obj.getName();
                                selectedStructureTypeId5 = obj.get_id();
                                selectedEt.setText(selectedStructureType5);
                                break;
                        }

                        break;
                    }
                }
                break;
        }
    }
}