package com.octopusbjsindia.view.fragments.ssgp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataValue;
import com.octopusbjsindia.models.SujalamSuphalam.SSMasterDatabase;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.models.ssgp.BeneficiaryDetail;
import com.octopusbjsindia.models.ssgp.VACStructureMasterRequest;
import com.octopusbjsindia.presenter.ssgp.VDCSMFormFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.ssgp.GPActionsActivity;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.List;

public class VDCSMFormFragment extends Fragment implements APIDataListener, CustomSpinnerListener, View.OnClickListener {
    private View view;
    private RelativeLayout progressBar;
    private ArrayList<MasterDataList> masterDataLists = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> structureTypeList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> beneficiaryTypeList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> interventionList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> stateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> talukaList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> villageList = new ArrayList<>();

    String selectedStructureTypeId, selectedStructureType, selectedIntervention, selectedInterventionId,
            selectedState = "", selectedStateId = "", selectedDistrict = "", selectedDistrictId = "",
            selectedTaluka = "", selectedTalukaId = "", selectedVillage = "", selectedVillageId = "",
            selectedBeneficiaryType, selectedBeneficiaryTypeId;
    private VDCSMFormFragmentPresenter presenter;
    private EditText etState, etDistrict, etTaluka, etVillage, et_structure_name, et_type_of_beneficiary,
            et_type_intervention, et_structure_type, et_scope_of_work, et_working_hours, et_diesel_quantity,
            et_approximate_working_days, et_neighbor_farmer_consent, et_silt_transported, et_ff_name, et_ff_mobile,
            et_structure_length, et_structure_Width, et_structure_depth, et_beneficiary_name, et_beneficiary_contact,
            et_beneficiary_category, et_irrigation, et_gat_no, et_Annual_income, et_numberof_crops, et_type_of_crops,
            et_beneficiary_name1, et_beneficiary_contact1, et_beneficiary_category1, et_beneficiary_name2,
            et_beneficiary_contact2, et_beneficiary_category2, et_beneficiary_name3, et_beneficiary_contact3,
            et_beneficiary_category3, et_beneficiary_name4, et_beneficiary_contact4, et_beneficiary_category4,
            et_beneficiary_name5, et_beneficiary_contact5, et_beneficiary_category5, et_responsible_person_name,
            et_responsible_person_contact, et_ho_remark;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_v_d_c_sm_form, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {

        setMasterData();

        for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getStateId().size(); i++) {
            CustomSpinnerObject customState = new CustomSpinnerObject();
            customState.set_id(Util.getUserObjectFromPref().getUserLocation().getStateId().get(i).getId());
            customState.setName(Util.getUserObjectFromPref().getUserLocation().getStateId().get(i).getName());
            stateList.add(customState);
        }

        ((GPActionsActivity) getActivity()).setTitle("VDC SM Form");
        progressBar = view.findViewById(R.id.progress_bar);
        presenter = new VDCSMFormFragmentPresenter(this);
        etState = view.findViewById(R.id.et_state);
        etDistrict = view.findViewById(R.id.et_district);
        etTaluka = view.findViewById(R.id.et_taluka);
        etVillage = view.findViewById(R.id.et_village);
        et_structure_name = view.findViewById(R.id.et_structure_name);
        et_type_of_beneficiary = view.findViewById(R.id.et_type_of_beneficiary);
        et_type_intervention = view.findViewById(R.id.et_type_intervention);
        et_structure_type = view.findViewById(R.id.et_structure_type);
        et_scope_of_work = view.findViewById(R.id.et_scope_of_work);
        et_working_hours = view.findViewById(R.id.et_working_hours);
        et_diesel_quantity = view.findViewById(R.id.et_diesel_quantity);
        et_approximate_working_days = view.findViewById(R.id.et_approximate_working_days);
        et_neighbor_farmer_consent = view.findViewById(R.id.et_neighbor_farmer_consent);
        et_silt_transported = view.findViewById(R.id.et_silt_transported);
        et_ff_name = view.findViewById(R.id.et_ff_name);
        et_ff_mobile = view.findViewById(R.id.et_ff_mobile);
        et_structure_length = view.findViewById(R.id.et_structure_length);
        et_structure_Width = view.findViewById(R.id.et_structure_Width);
        et_structure_depth = view.findViewById(R.id.et_structure_depth);
        et_beneficiary_name = view.findViewById(R.id.et_beneficiary_name);
        et_beneficiary_contact = view.findViewById(R.id.et_beneficiary_contact);
        et_beneficiary_category = view.findViewById(R.id.et_beneficiary_category);
        et_irrigation = view.findViewById(R.id.et_irrigation);
        et_gat_no = view.findViewById(R.id.et_gat_no);
        et_Annual_income = view.findViewById(R.id.et_Annual_income);
        et_numberof_crops = view.findViewById(R.id.et_numberof_crops);
        et_type_of_crops = view.findViewById(R.id.et_type_of_crops);
        et_beneficiary_name1 = view.findViewById(R.id.et_beneficiary_name1);
        et_beneficiary_contact1 = view.findViewById(R.id.et_beneficiary_contact1);
        et_beneficiary_category1 = view.findViewById(R.id.et_beneficiary_category1);
        et_beneficiary_name2 = view.findViewById(R.id.et_beneficiary_name2);
        et_beneficiary_contact2 = view.findViewById(R.id.et_beneficiary_contact2);
        et_beneficiary_category2 = view.findViewById(R.id.et_beneficiary_category2);
        et_beneficiary_name3 = view.findViewById(R.id.et_beneficiary_name3);
        et_beneficiary_contact3 = view.findViewById(R.id.et_beneficiary_contact3);
        et_beneficiary_category3 = view.findViewById(R.id.et_beneficiary_category3);
        et_beneficiary_name4 = view.findViewById(R.id.et_beneficiary_name4);
        et_beneficiary_contact4 = view.findViewById(R.id.et_beneficiary_contact4);
        et_beneficiary_category4 = view.findViewById(R.id.et_beneficiary_category4);
        et_beneficiary_name5 = view.findViewById(R.id.et_beneficiary_name5);
        et_beneficiary_contact5 = view.findViewById(R.id.et_beneficiary_contact5);
        et_beneficiary_category5 = view.findViewById(R.id.et_beneficiary_category5);
        et_responsible_person_name = view.findViewById(R.id.et_responsible_person_name);
        et_responsible_person_contact = view.findViewById(R.id.et_responsible_person_contact);
        et_ho_remark = view.findViewById(R.id.et_ho_remark);
        view.findViewById(R.id.btn_submit).setOnClickListener(this);
        etState.setOnClickListener(this);
        etDistrict.setOnClickListener(this);
        etTaluka.setOnClickListener(this);
        etVillage.setOnClickListener(this);
        et_type_intervention.setOnClickListener(this);
        et_structure_type.setOnClickListener(this);
        et_type_of_beneficiary.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_state:
                CustomSpinnerDialogClass cdd6 = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select State",
                        stateList,
                        false);
                cdd6.show();
                cdd6.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_district:
                if (districtList.size() > 0) {
                    CustomSpinnerDialogClass cdd7 = new CustomSpinnerDialogClass(getActivity(), this,
                            "Select District",
                            districtList,
                            false);
                    cdd7.show();
                    cdd7.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (Util.isConnected(getActivity())) {
                        if (etState.getText() != null && etState.getText().toString().length() > 0) {
                            presenter.getLocationData((!TextUtils.isEmpty(selectedStateId))
                                            ? selectedStateId : selectedStateId, Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                        }
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                    }
                }
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
                                            ? selectedDistrictId : selectedDistrictId,
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
                            "Select Village", villageList, false);
                    cdd1.show();
                    cdd1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (Util.isConnected(getActivity())) {
                        if (etTaluka.getText() != null && etTaluka.getText().toString().length() > 0) {
                            presenter.getLocationData((!TextUtils.isEmpty(selectedTalukaId))
                                            ? selectedTalukaId : selectedTalukaId,
                                    Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.VILLAGE_LEVEL);
                        }
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                    }
                }
                break;
            case R.id.et_type_intervention:
                interventionList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("interventionType"))
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setTypeCode(obj.getTypeCode());
                            temp.setSelected(false);
                            interventionList.add(temp);
                        }
                }
                CustomSpinnerDialogClass csdIntervention = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Intervention", interventionList, false);
                csdIntervention.show();
                csdIntervention.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_structure_type:
                structureTypeList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("structureType"))
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setSelected(false);
                            structureTypeList.add(temp);
                        }
                }
                CustomSpinnerDialogClass csdStructerType = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Structure Type", structureTypeList, false);
                csdStructerType.show();
                csdStructerType.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_type_of_beneficiary:
                beneficiaryTypeList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("structureBeneficiary"))
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setTypeCode(obj.getTypeCode());
                            temp.setSelected(false);
                            beneficiaryTypeList.add(temp);
                        }
                }
                CustomSpinnerDialogClass csdBeneficiary = new CustomSpinnerDialogClass(getActivity(), this,
                        "Select Type of Beneficiary", beneficiaryTypeList, false);
                csdBeneficiary.show();
                csdBeneficiary.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(selectedStateId)) {
                    Util.showToast(getActivity(), "Please selected state");
                } else if (TextUtils.isEmpty(selectedDistrictId)) {
                    Util.showToast(getActivity(), "Please selected district");
                } else if (TextUtils.isEmpty(selectedTalukaId)) {
                    Util.showToast(getActivity(), "Please selected taluka");
                } else if (TextUtils.isEmpty(selectedVillageId)) {
                    Util.showToast(getActivity(), "Please selected village");
                } else if (TextUtils.isEmpty(et_structure_name.getText())) {
                    Util.showToast(getActivity(), "Please enter structure name");
                } else {
                    VACStructureMasterRequest request = new VACStructureMasterRequest();
                    request.setStateId(selectedStateId);
                    request.setDistrictId(selectedDistrictId);
                    request.setTalukaId(selectedTalukaId);
                    request.setVillageId(selectedVillageId);
                    request.setName(et_structure_name.getText().toString().trim());
                    request.setBeneficiaryId(selectedBeneficiaryTypeId);
                    request.setInterventionId(selectedInterventionId);
                    request.setTypeId(selectedStructureTypeId);
                    request.setScopeOfWork(et_scope_of_work.getText().toString().trim());
                    request.setWorkHours(et_working_hours.getText().toString().trim());
                    request.setDieselRequired(et_diesel_quantity.getText().toString().trim());
                    request.setWorkingDaysRequired(et_approximate_working_days.getText().toString().trim());
                    request.setConsentNghTranWork(et_neighbor_farmer_consent.getText().toString().trim());
                    request.setIsSiltTrasported(et_silt_transported.getText().toString().trim());
                    request.setFfName(et_ff_name.getText().toString().trim());
                    request.setFfMobileNumber(et_ff_mobile.getText().toString().trim());
                    request.setStructureLenght(et_structure_length.getText().toString().trim());
                    request.setStructureWidth(et_structure_Width.getText().toString().trim());
                    request.setStructureDepth(et_structure_depth.getText().toString().trim());
                    request.setBeneficiaryName(et_beneficiary_name.getText().toString().trim());
                    request.setBeneficiaryContactNumber(et_beneficiary_contact.getText().toString().trim());
                    request.setCategoryBeneficiaryFarmer(et_beneficiary_category.getText().toString().trim());
//                request.set(et_irrigation.getText().toString().trim());
                    request.setGatNumber(et_gat_no.getText().toString().trim());
                    request.setIncomeFormFarm(et_Annual_income.getText().toString().trim());
                    request.setCropsYears(et_numberof_crops.getText().toString().trim());
                    request.setCropType(et_type_of_crops.getText().toString().trim());
                    request.setResponsiblePersonName(et_responsible_person_name.getText().toString().trim());
                    request.setResponsiblePersonNumber(et_responsible_person_contact.getText().toString().trim());
                    request.setComment(et_ho_remark.getText().toString().trim());

                    ArrayList<BeneficiaryDetail> beneficiaryDetails = new ArrayList<BeneficiaryDetail>();
                    BeneficiaryDetail beneficiary1 = new BeneficiaryDetail();
                    beneficiary1.setName(et_beneficiary_name1.getText().toString().trim());
                    beneficiary1.setNumber(et_beneficiary_contact1.getText().toString().trim());
                    beneficiary1.setCategory(et_beneficiary_category1.getText().toString().trim());
                    beneficiaryDetails.add(beneficiary1);

                    BeneficiaryDetail beneficiary2 = new BeneficiaryDetail();
                    beneficiary2.setName(et_beneficiary_name2.getText().toString().trim());
                    beneficiary2.setNumber(et_beneficiary_contact2.getText().toString().trim());
                    beneficiary2.setCategory(et_beneficiary_category2.getText().toString().trim());
                    beneficiaryDetails.add(beneficiary2);

                    BeneficiaryDetail beneficiary3 = new BeneficiaryDetail();
                    beneficiary3.setName(et_beneficiary_name3.getText().toString().trim());
                    beneficiary3.setNumber(et_beneficiary_contact3.getText().toString().trim());
                    beneficiary3.setCategory(et_beneficiary_category3.getText().toString().trim());
                    beneficiaryDetails.add(beneficiary3);

                    BeneficiaryDetail beneficiary4 = new BeneficiaryDetail();
                    beneficiary4.setName(et_beneficiary_name4.getText().toString().trim());
                    beneficiary4.setNumber(et_beneficiary_contact4.getText().toString().trim());
                    beneficiary4.setCategory(et_beneficiary_category4.getText().toString().trim());
                    beneficiaryDetails.add(beneficiary4);

                    BeneficiaryDetail beneficiary5 = new BeneficiaryDetail();
                    beneficiary5.setName(et_beneficiary_name5.getText().toString().trim());
                    beneficiary5.setNumber(et_beneficiary_contact5.getText().toString().trim());
                    beneficiary5.setCategory(et_beneficiary_category5.getText().toString().trim());
                    beneficiaryDetails.add(beneficiary5);

                    request.setBeneficiaryDetails(beneficiaryDetails);

                    presenter.submitSM(request);

                }
                break;
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(getActivity(),message);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String message) {
        getActivity().finish();
        Util.showToast(getActivity(),message);
    }

    @Override
    public void showProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void setMasterData() {

        List<SSMasterDatabase> list = DatabaseManager.getDBInstance(Platform.getInstance()).
                getSSMasterDatabaseDao().getSSMasterData("GP");
        String masterDbString = list.get(0).getData();

        Gson gson = new Gson();
        TypeToken<ArrayList<MasterDataList>> token = new TypeToken<ArrayList<MasterDataList>>() {
        };
        ArrayList<MasterDataList> masterDataList = gson.fromJson(masterDbString, token.getType());

        for (MasterDataList obj : masterDataList) {
            /*if (obj.getForm().equalsIgnoreCase("structure_create") ||
                    obj.getForm().equalsIgnoreCase("structure_preparation"))*/
            {
                masterDataLists.add(obj);
            }
        }
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
            case "Select State":
                for (CustomSpinnerObject state : stateList) {
                    if (state.isSelected()) {
                        selectedState = state.getName();
                        selectedStateId = state.get_id();
                        break;
                    }
                }
                etState.setText(selectedState);
                etDistrict.setText("");
                selectedDistrict = "";
                selectedDistrictId = "";
                etTaluka.setText("");
                selectedTaluka = "";
                selectedTalukaId = "";
                etVillage.setText("");
                selectedVillage = "";
                selectedVillageId = "";
                break;
            case "Select District":
                for (CustomSpinnerObject state : districtList) {
                    if (state.isSelected()) {
                        selectedDistrict = state.getName();
                        selectedDistrictId = state.get_id();
                        break;
                    }
                }
                etDistrict.setText(selectedDistrict);
                etTaluka.setText("");
                selectedTaluka = "";
                selectedTalukaId = "";
                etVillage.setText("");
                selectedVillage = "";
                selectedVillageId = "";
                break;
            case "Select Taluka":
                for (CustomSpinnerObject state : talukaList) {
                    if (state.isSelected()) {
                        selectedTaluka = state.getName();
                        selectedTalukaId = state.get_id();
                        break;
                    }
                }
                etTaluka.setText(selectedTaluka);
                etVillage.setText("");
                selectedVillage = "";
                selectedVillageId = "";
                break;
            case "Select Village":
                for (CustomSpinnerObject state : villageList) {
                    if (state.isSelected()) {
                        selectedVillage = state.getName();
                        selectedVillageId = state.get_id();
                        break;
                    }
                }
                etVillage.setText(selectedVillage);
                break;
            case "Select Intervention":
                int selectedType = -1;
                for (CustomSpinnerObject obj : interventionList) {
                    if (obj.isSelected()) {
                        selectedIntervention = obj.getName();
                        selectedInterventionId = obj.get_id();
                        selectedType = obj.getTypeCode();
                    }
                }
                selectedStructureType = "";
                selectedStructureTypeId = "";
            /*    et_structure_type.setText("");
                structureTypeList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("structureType")
                            && masterDataLists.get(i).getStructureTypeCode() == selectedType) {
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setSelected(false);
                            structureTypeList.add(temp);
                        }
                        break;
                    }

                }*/
                et_type_intervention.setText(selectedIntervention);
                break;
            case "Select Structure Type":
                for (CustomSpinnerObject obj : structureTypeList) {
                    if (obj.isSelected()) {
                        selectedStructureType = obj.getName();
                        selectedStructureTypeId = obj.get_id();
                    }
                }
                et_structure_type.setText(selectedStructureType);
                break;
            case "Select Type of Beneficiary":
                for (CustomSpinnerObject obj : beneficiaryTypeList) {
                    if (obj.isSelected()) {
                        selectedBeneficiaryType = obj.getName();
                        selectedBeneficiaryTypeId = obj.get_id();
                    }
                }
                et_type_of_beneficiary.setText(selectedBeneficiaryType);
                break;

        }
    }


}

