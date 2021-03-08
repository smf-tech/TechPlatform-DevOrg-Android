package com.octopusbjsindia.view.fragments.ssgp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.models.ssgp.VdcBdRequestModel;
import com.octopusbjsindia.models.ssgp.VdcCmRequestModel;
import com.octopusbjsindia.presenter.ssgp.VDCBDFormFragmentPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.ssgp.GPActionsActivity;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.List;

public class VDCBDFormFragment extends Fragment implements View.OnClickListener, APIDataListener, CustomSpinnerListener {
    private View vdcbdFormFragmentView;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private ArrayList<MasterDataList> masterDataLists = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> structureTypeList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> stateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> talukaList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> villageList = new ArrayList<>();

    String selectedStructureTypeId, selectedStructureType, selectedIntervention, selectedInterventionId,
            selectedState = "", selectedStateId = "", selectedDistrict = "", selectedDistrictId = "",
            selectedTaluka = "", selectedTalukaId = "", selectedVillage = "", selectedVillageId = "",
            selectedBeneficiaryType, selectedBeneficiaryTypeId;

    private VDCBDFormFragmentPresenter presenter;
    private EditText etState, etDistrict, etTaluka, etVillage;
    private EditText et_struct_code, et_structure_type, et_beneficiary_name, et_beneficiary_contact,
            et_beneficiary_category, et_irrigation, et_gat_no, et_annual_income, et_numberof_crops, et_type_of_crops, et_remark;
    private Button btn_submit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        vdcbdFormFragmentView = inflater.inflate(R.layout.fragment_v_d_c_bd_form, container, false);
        return vdcbdFormFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        for (int i = 0; i < Util.getUserObjectFromPref().getUserLocation().getStateId().size(); i++) {
            CustomSpinnerObject customState = new CustomSpinnerObject();
            customState.set_id(Util.getUserObjectFromPref().getUserLocation().getStateId().get(i).getId());
            customState.setName(Util.getUserObjectFromPref().getUserLocation().getStateId().get(i).getName());
            stateList.add(customState);
        }

        ((GPActionsActivity) getActivity()).setTitle("Beneficiary detail form");
        progressBarLayout = vdcbdFormFragmentView.findViewById(R.id.profile_act_progress_bar);
        progressBar = vdcbdFormFragmentView.findViewById(R.id.pb_profile_act);
        presenter = new VDCBDFormFragmentPresenter(this);
        etState = vdcbdFormFragmentView.findViewById(R.id.et_state);
        etDistrict = vdcbdFormFragmentView.findViewById(R.id.et_district);
        etTaluka = vdcbdFormFragmentView.findViewById(R.id.et_taluka);
        etVillage = vdcbdFormFragmentView.findViewById(R.id.et_village);
        et_struct_code = vdcbdFormFragmentView.findViewById(R.id.et_struct_code);

        et_structure_type = vdcbdFormFragmentView.findViewById(R.id.et_structure_type);
        et_beneficiary_name = vdcbdFormFragmentView.findViewById(R.id.et_beneficiary_name);
        et_beneficiary_contact = vdcbdFormFragmentView.findViewById(R.id.et_beneficiary_contact);
        et_beneficiary_category = vdcbdFormFragmentView.findViewById(R.id.et_beneficiary_category);
        et_irrigation = vdcbdFormFragmentView.findViewById(R.id.et_irrigation);
        et_gat_no = vdcbdFormFragmentView.findViewById(R.id.et_gat_no);
        et_annual_income = vdcbdFormFragmentView.findViewById(R.id.et_annual_income);
        et_numberof_crops = vdcbdFormFragmentView.findViewById(R.id.et_numberof_crops);
        et_type_of_crops = vdcbdFormFragmentView.findViewById(R.id.et_type_of_crops);
        et_remark = vdcbdFormFragmentView.findViewById(R.id.et_remark);
        btn_submit = vdcbdFormFragmentView.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        etState.setOnClickListener(this);
        etDistrict.setOnClickListener(this);
        etTaluka.setOnClickListener(this);
        etVillage.setOnClickListener(this);

    }




    private boolean isAllInputsValid() {
        String msg = "";

        if (etState.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.select_state);
        } else if (etDistrict.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.select_district);
        } else if (etTaluka.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_select_taluka);
        } else if (et_struct_code.getText().toString().trim().length() == 0) {
            msg =getString(R.string.msg_select_struct_code);
        } else if (et_structure_type.getText().toString().trim().length() == 0) {
            msg = getString(R.string.select_stuct_type);
        } else if (et_beneficiary_name.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.msg_enter_name);
        } else if (et_beneficiary_contact.getText().toString().trim().length() == 10) {
            msg = getResources().getString(R.string.msg_enter_mobile_number);
        } else if (et_beneficiary_category.getText().toString().trim().length() == 0) {
            msg = getResources().getString(R.string.type_of_beneficiary);
        } else if (et_irrigation.getText().toString().trim().length() == 0) {
            msg = getString(R.string.msg_irrigation_source);
        } else if (et_gat_no.getText().toString().trim().length() == 0) {
            msg = getString(R.string.msg_enter_gat_no);
        }else if (et_annual_income.getText().toString().trim().length() == 0) {
            msg = getString(R.string.msg_annual_income);
        }else if (et_numberof_crops.getText().toString().trim().length() == 0) {
            msg = getString(R.string.msg_numberof_crops);
        }else if (et_type_of_crops.getText().toString().trim().length() == 0) {
            msg = "Please enter type of crops taken";
        }else if (et_remark.getText().toString().trim().length() == 0) {
            msg = getString(R.string.msg_enter_remark);
        }

        if (TextUtils.isEmpty(msg)) {
            return true;
        }

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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

            case R.id.btn_submit:
                //   if (isAllInputsValid())
            {
                //Util.showToast(getActivity(),"data is valid call API here");
                VdcBdRequestModel vdcBdRequestModel = new VdcBdRequestModel();
                vdcBdRequestModel.setStateId("test");
                vdcBdRequestModel.setDistrictId("test");
                vdcBdRequestModel.setTalukaId("test");
                vdcBdRequestModel.setVillageId("test");
                vdcBdRequestModel.setStructureId("asdj32");
                vdcBdRequestModel.setStructureType("skjdha");
                vdcBdRequestModel.setBeneficiaryName("dgashd");
                vdcBdRequestModel.setBeneficiaryNumber("1211231231");
                vdcBdRequestModel.setCategoryBeneficiaryFarmer("individual");
                vdcBdRequestModel.setArrigationSurWater("rain");
                vdcBdRequestModel.setGatNumber("23kjkjk");
                vdcBdRequestModel.setAnnualIncome("2300000");
                vdcBdRequestModel.setCropNumberTime("3");
                vdcBdRequestModel.setTypeOfCrop("Rabbi");
                vdcBdRequestModel.setComment("good");

                presenter.submitBdData(vdcBdRequestModel);
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

            /*case "Select Structure Type":
                for (CustomSpinnerObject obj : structureTypeList) {
                    if (obj.isSelected()) {
                        selectedStructureType = obj.getName();
                        selectedStructureTypeId = obj.get_id();
                    }
                }
                et_structure_type.setText(selectedStructureType);
                break;*/


        }
    }

}
