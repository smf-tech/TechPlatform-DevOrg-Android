package com.octopusbjsindia.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
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
import com.octopusbjsindia.models.SujalamSuphalam.Structure;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.JurisdictionLocation;
import com.octopusbjsindia.presenter.CreateStructureActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GPSTracker;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateStructureActivity extends AppCompatActivity implements APIDataListener, View.OnClickListener,
        CustomSpinnerListener {

    private RelativeLayout progressBar;
    private CreateStructureActivityPresenter presenter;

    private EditText etDistrict, etTaluka, etHostVillage, etCatchmentVillage, etHostVillagePopulation, etCatchmentVillagePopulation,
            etGatNo, etWaterShedNo, etArea, etStructureName, etStructureType, etStructureWorkType, etStructureOwnerDepartment,
            etNotaDetail, etSubStructureOwnerDepartment, etAdministrativeApprovalNo, etAdministrativeApprovalDate,
            etTechnicalSanctionNo, etTechnicalSanctionDate, etIntervention, etAdministrativeEstimateAmount, etApproximateWorkingHours, etApproximateDieselConsumptionAmount,
            etApproximateDieselLiters, etApproximateEstimateQuantity, etRemark;
    private Button btSubmit;

    private String selectedDistrictId, selectedDistrict, selectedTalukaId, selectedTaluka, selectedHostVillageId,
            selectedHostVillage, selectedStructureTypeId, selectedIntervention, selectedInterventionId,
            selectedStructureType, selectedStructureWorkTypeId, selectedStructureWorkType, selectedStructureOwnerDepartmentId,
            selectedStructureOwnerDepartment, selectedSubStructureOwnerDepartmentId, selectedSubStructureOwnerDepartment;
    private ArrayList<String> selectedCatchmentVillageId = new ArrayList<String>();
    private ArrayList<String> selectedCatchmentVillage = new ArrayList<String>();
    private ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> talukaList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> villageList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> catchmentVillageList = new ArrayList<>();
    private ArrayList<MasterDataList> masterDataLists = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> structureDepartmentList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> structureSubDepartmentList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> structureTypeList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> structureWorkTypeList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> interventionList = new ArrayList<>();

    private Structure structureData;

    private GPSTracker gpsTracker;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_structure);

        progressBar = findViewById(R.id.ly_progress_bar);
        presenter = new CreateStructureActivityPresenter(this);
        setMasterData();
        initView();
        setTitle("Create Structure");

        gpsTracker = new GPSTracker(this);
        if (gpsTracker.isGPSEnabled(this, this)) {
            location = gpsTracker.getLocation();
        }
    }

    private void initView() {

        structureData = new Structure();

        etDistrict = findViewById(R.id.et_district);
        etTaluka = findViewById(R.id.et_taluka);
        etHostVillage = findViewById(R.id.et_host_village);
        etHostVillagePopulation = findViewById(R.id.et_host_village_population);
        etCatchmentVillage = findViewById(R.id.et_catchment_village);
        etCatchmentVillagePopulation = findViewById(R.id.et_catchment_village_population);
        etGatNo = findViewById(R.id.et_gat_no);
        etWaterShedNo = findViewById(R.id.et_water_shed_no);
        etArea = findViewById(R.id.et_area);
        etStructureName = findViewById(R.id.et_structure_name);
        etStructureOwnerDepartment = findViewById(R.id.et_structure_owner_department);
        etSubStructureOwnerDepartment = findViewById(R.id.et_sub_structure_owner_department);
        etNotaDetail = findViewById(R.id.et_nota_detail);
        etStructureType = findViewById(R.id.et_structure_type);
        etStructureWorkType = findViewById(R.id.et_structure_work_type);
        etAdministrativeApprovalNo = findViewById(R.id.et_administrative_approval_no);
        etAdministrativeApprovalDate = findViewById(R.id.et_administrative_approval_date);
        etTechnicalSanctionNo = findViewById(R.id.et_technical_sanction_no);
        etTechnicalSanctionDate = findViewById(R.id.et_technical_sanction_date);
        etIntervention = findViewById(R.id.et_intervention);
        etAdministrativeEstimateAmount = findViewById(R.id.et_administrative_estimate_amount);
        etApproximateWorkingHours = findViewById(R.id.et_approximate_working_hours);
        etApproximateDieselConsumptionAmount = findViewById(R.id.et_approximate_diesel_consumption_amount);
        etApproximateDieselLiters = findViewById(R.id.et_approximate_diesel_liters);
        etApproximateEstimateQuantity = findViewById(R.id.et_approximate_estimate_quantity);
        etRemark = findViewById(R.id.et_remark);
        btSubmit = findViewById(R.id.bt_submit);

        if (Util.getUserObjectFromPref().getUserLocation().getDistrictIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getDistrictIds().size() > 0) {
            etDistrict.setText(Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName());
            selectedDistrictId = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getId();
            selectedDistrict = Util.getUserObjectFromPref().getUserLocation().getDistrictIds().get(0).getName();
        } else {
            //get District
            presenter.getLocationData(Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId(),
                    Util.getUserObjectFromPref().getJurisdictionTypeId(),
                    Constants.JurisdictionLevelName.DISTRICT_LEVEL);
            etDistrict.setOnClickListener(this);
        }
        if (Util.getUserObjectFromPref().getUserLocation().getTalukaIds() != null &&
                Util.getUserObjectFromPref().getUserLocation().getTalukaIds().size() > 0) {
            etTaluka.setText(Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName());
            selectedTalukaId = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getId();
            selectedTaluka = Util.getUserObjectFromPref().getUserLocation().getTalukaIds().get(0).getName();
        } else {
            etTaluka.setOnClickListener(this);
        }

        etHostVillage.setOnClickListener(this);
        etAdministrativeApprovalDate.setOnClickListener(this);
        etTechnicalSanctionDate.setOnClickListener(this);
        etCatchmentVillage.setOnClickListener(this);
        etIntervention.setOnClickListener(this);
        etStructureType.setOnClickListener(this);
        etStructureWorkType.setOnClickListener(this);
        etStructureOwnerDepartment.setOnClickListener(this);
        etSubStructureOwnerDepartment.setOnClickListener(this);
        btSubmit.setOnClickListener(this);

        //get District
//        presenter.getJurisdictionLevelData(Util.getUserObjectFromPref().getOrgId(),
//                Util.getUserObjectFromPref().getJurisdictionTypeId(),
//                Constants.JurisdictionLevelName.DISTRICT_LEVEL);
        //get Village
        presenter.getLocationData(selectedTalukaId,
                Util.getUserObjectFromPref().getJurisdictionTypeId(),
                Constants.JurisdictionLevelName.VILLAGE_LEVEL);
    }

    public void setTitle(String title) {
        TextView tvTitle = findViewById(R.id.toolbar_title);
        tvTitle.setText(title);
        findViewById(R.id.toolbar_back_action).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.et_district:
                CustomSpinnerDialogClass csdDisttrict = new CustomSpinnerDialogClass(this, this,
                        "Select District", districtList, false);
                csdDisttrict.show();
                csdDisttrict.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_taluka:
                CustomSpinnerDialogClass csdTaluka = new CustomSpinnerDialogClass(this, this,
                        "Select Taluka", talukaList, false);
                csdTaluka.show();
                csdTaluka.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_host_village:
                CustomSpinnerDialogClass csdHostVillage = new CustomSpinnerDialogClass(this, this,
                        "Select Host Village", villageList, false);
                csdHostVillage.show();
                csdHostVillage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_catchment_village:
                CustomSpinnerDialogClass csdCatchmentVillage = new CustomSpinnerDialogClass(this, this,
                        "Select Catchment Village", catchmentVillageList, true);
                csdCatchmentVillage.show();
                csdCatchmentVillage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_structure_owner_department:
                structureDepartmentList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("structureDept"))
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setSelected(false);
                            structureDepartmentList.add(temp);
                        }
                }
                CustomSpinnerDialogClass csdStructureDept = new CustomSpinnerDialogClass(this, this,
                        "Select Structure owner department", structureDepartmentList, false);
                csdStructureDept.show();
                csdStructureDept.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_sub_structure_owner_department:
                structureSubDepartmentList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("structureSubDept"))
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setSelected(false);
                            structureSubDepartmentList.add(temp);
                        }
                }
                CustomSpinnerDialogClass cddCity = new CustomSpinnerDialogClass(this, this,
                        "Select Sub Structure owner department", structureSubDepartmentList, false);
                cddCity.show();
                cddCity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_intervention:
                interventionList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("intervention"))
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setTypeCode(obj.getTypeCode());
                            temp.setSelected(false);
                            interventionList.add(temp);
                        }
                }
                CustomSpinnerDialogClass csdIntervention = new CustomSpinnerDialogClass(this, this,
                        "Select Intervention", interventionList, false);
                csdIntervention.show();
                csdIntervention.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_structure_type:
//                structureTypeList.clear();
//                for (int i = 0; i < masterDataLists.size(); i++) {
//                    if (masterDataLists.get(i).getField().equalsIgnoreCase("structureType"))
//                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
//                            CustomSpinnerObject temp = new CustomSpinnerObject();
//                            temp.set_id(obj.getId());
//                            temp.setName(obj.getValue());
//                            temp.setSelected(false);
//                            structureTypeList.add(temp);
//                        }
//                }
                CustomSpinnerDialogClass csdStructerType = new CustomSpinnerDialogClass(this, this,
                        "Select Structure Type", structureTypeList, false);
                csdStructerType.show();
                csdStructerType.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_structure_work_type:
                structureWorkTypeList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("work_type"))
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setSelected(false);
                            structureWorkTypeList.add(temp);
                        }
                }
                CustomSpinnerDialogClass csdStructerWorkType = new CustomSpinnerDialogClass(this, this,
                        "Select Structure Work Type", structureWorkTypeList, false);
                csdStructerWorkType.show();
                csdStructerWorkType.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_administrative_approval_date:
                Util.showDateDialog(this, etAdministrativeApprovalDate);
                break;
            case R.id.et_technical_sanction_date:
                Util.showDateDialog(this, etTechnicalSanctionDate);
                break;
            case R.id.bt_submit:
                if (isAllDataValid()) {
                    presenter.submitStructure(structureData);
                }
                break;

        }
    }

    private boolean isAllDataValid() {
        if (TextUtils.isEmpty(selectedDistrict)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, selected District.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(selectedTaluka)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, selected Taluka.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(selectedHostVillage)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, selected Host Village.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etHostVillagePopulation.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, feel Host Village Population.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(android.text.TextUtils.join(",", selectedCatchmentVillage))) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, select Catchment Village.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etCatchmentVillagePopulation.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, feel Catchment Village Population.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etGatNo.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, feel Gat No.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etWaterShedNo.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, feel Water Shed No.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etArea.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, feel Area.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etStructureName.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, feel proper information.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(selectedStructureOwnerDepartmentId)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, feel Structure Name.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(selectedSubStructureOwnerDepartmentId)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, select Sub-structure Owner DepartmentId.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etNotaDetail.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, feel Nota Detail.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(selectedStructureTypeId)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, select Structure Type.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etAdministrativeApprovalNo.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, feel Administrative Approval No.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etAdministrativeApprovalDate.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, feel Administrative Approval Date.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etTechnicalSanctionNo.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, feel Technical Sanction No.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etAdministrativeEstimateAmount.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, feel Administrative Estimate Amount.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etApproximateWorkingHours.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, feel Approximate Working Hours.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etApproximateDieselConsumptionAmount.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, feel Approximate Diesel Consumption Amount.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etApproximateDieselLiters.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, feel proper information.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etApproximateEstimateQuantity.getText().toString())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please, feel Approximate Estimate Quantity.", Snackbar.LENGTH_LONG);
            return false;
        } else {
            structureData.setStateId(Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId());
            structureData.setDistrictId(selectedDistrictId);
            structureData.setDistrict(selectedDistrict);
            structureData.setTalukaId(selectedTalukaId);
            structureData.setTaluka(selectedTaluka);
            structureData.setVillageId(selectedHostVillageId);
            structureData.setVillage(selectedHostVillage);
            structureData.setVillagePopulation(etHostVillagePopulation.getText().toString());
            structureData.setCatchmentVillagesIds(android.text.TextUtils.join(",", selectedCatchmentVillageId));
            structureData.setCatchmentVillages(android.text.TextUtils.join(",", selectedCatchmentVillage));
            structureData.setTotalPopulation(etCatchmentVillagePopulation.getText().toString());
            structureData.setGatNo(etGatNo.getText().toString());
            structureData.setWaterShedNo(etWaterShedNo.getText().toString());
            structureData.setArea(etArea.getText().toString());
            structureData.setName(etStructureName.getText().toString());
            structureData.setDepartmentId(selectedStructureOwnerDepartmentId);
            structureData.setSubDepartmentId(selectedSubStructureOwnerDepartmentId);
            structureData.setNotaDetail(etNotaDetail.getText().toString());
            structureData.setStructureType(selectedStructureTypeId);
            structureData.setWorkType(selectedStructureWorkTypeId);
            structureData.setAdministrativeApprovalNo(etAdministrativeApprovalNo.getText().toString());
            structureData.setAdministrativeApprovalDate(etAdministrativeApprovalDate.getText().toString());
            structureData.setTechnicalSectionNumber(etTechnicalSanctionNo.getText().toString());
            structureData.setTechnicalSectionDate(etTechnicalSanctionDate.getText().toString());
            structureData.setInterventionId(selectedInterventionId);
            structureData.setAdministrativeEstimateAmount(etAdministrativeEstimateAmount.getText().toString());
            structureData.setApprxWorkingHrs(etApproximateWorkingHours.getText().toString());
            structureData.setApprxDieselConsumptionRs(etApproximateDieselConsumptionAmount.getText().toString());
            structureData.setApprxDieselConsumptionLt(etApproximateDieselLiters.getText().toString());
            structureData.setApprxEstimateQunty(etApproximateEstimateQuantity.getText().toString());
            if (location != null) {
                structureData.setLat(location.getLatitude());
                structureData.setLog(location.getLongitude());
            }
            structureData.setFfId(Util.getUserObjectFromPref().getId());
            structureData.setRemark(etRemark.getText().toString());
        }
        return true;
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Select District":
                for (CustomSpinnerObject obj : districtList) {
                    if (obj.isSelected()) {
                        selectedDistrict = obj.getName();
                        selectedDistrictId = obj.get_id();
                        break;
                    }
                }
                etDistrict.setText(selectedDistrict);
                etTaluka.setText("");
                selectedTaluka = "";
                selectedTalukaId = "";
                etHostVillage.setText("");
                selectedHostVillage = "";
                selectedHostVillageId = "";
                //get Taluka
                if (!TextUtils.isEmpty(selectedDistrictId)) {
                    presenter.getLocationData(selectedDistrictId,
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.TALUKA_LEVEL);
                }
                break;
            case "Select Taluka":
                for (CustomSpinnerObject obj : talukaList) {
                    if (obj.isSelected()) {
                        selectedTaluka = obj.getName();
                        selectedTalukaId = obj.get_id();
                        break;
                    }
                }
                etTaluka.setText(selectedTaluka);
                etHostVillage.setText("");
                selectedHostVillage = "";
                selectedHostVillageId = "";
                //get Taluka
                if (!TextUtils.isEmpty(selectedTalukaId)) {
                    presenter.getLocationData(selectedTalukaId,
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.VILLAGE_LEVEL);
                }

                break;
            case "Select Host Village":
                for (CustomSpinnerObject obj : villageList) {
                    if (obj.isSelected()) {
                        selectedHostVillage = obj.getName();
                        selectedHostVillageId = obj.get_id();
                        break;
                    }
                }
                etHostVillage.setText(selectedHostVillage);
                break;
            case "Select Catchment Village":
                selectedCatchmentVillage.clear();
                selectedCatchmentVillageId.clear();
                for (CustomSpinnerObject obj : catchmentVillageList) {
                    if (obj.isSelected()) {
                        selectedCatchmentVillage.add(obj.getName());
                        selectedCatchmentVillageId.add(obj.get_id());
                    }
                }
                etCatchmentVillage.setText(android.text.TextUtils.join(",", selectedCatchmentVillage));
                break;
            case "Select Structure owner department":
                for (CustomSpinnerObject obj : structureDepartmentList) {
                    if (obj.isSelected()) {
                        selectedStructureOwnerDepartment = obj.getName();
                        selectedStructureOwnerDepartmentId = obj.get_id();
                    }
                }
                etStructureOwnerDepartment.setText(selectedStructureOwnerDepartment);
                break;
            case "Select Sub Structure owner department":
                for (CustomSpinnerObject obj : structureSubDepartmentList) {
                    if (obj.isSelected()) {
                        selectedSubStructureOwnerDepartment = obj.getName();
                        selectedSubStructureOwnerDepartmentId = obj.get_id();
                    }
                }
                etSubStructureOwnerDepartment.setText(selectedSubStructureOwnerDepartment);
                break;
            case "Select Intervention":
                int selectedType=-1;
                for (CustomSpinnerObject obj : interventionList) {
                    if (obj.isSelected()) {
                        selectedIntervention = obj.getName();
                        selectedInterventionId = obj.get_id();
                        selectedType = obj.getTypeCode();
                    }
                }
                selectedStructureType = "";
                selectedStructureTypeId = "";
                etStructureType.setText("");
                structureTypeList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("structureType")
                            && masterDataLists.get(i).getStructureTypeCode()== selectedType){
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setSelected(false);
                            structureTypeList.add(temp);
                        }
                        break;
                    }

                }
                etIntervention.setText(selectedIntervention);
                break;
            case "Select Structure Type":
                for (CustomSpinnerObject obj : structureTypeList) {
                    if (obj.isSelected()) {
                        selectedStructureType = obj.getName();
                        selectedStructureTypeId = obj.get_id();
                    }
                }
                etStructureType.setText(selectedStructureType);
                break;
            case "Select Structure Work Type":
                for (CustomSpinnerObject obj : structureWorkTypeList) {
                    if (obj.isSelected()) {
                        selectedStructureWorkType = obj.getName();
                        selectedStructureWorkTypeId = obj.get_id();
                    }
                }
                etStructureWorkType.setText(selectedStructureWorkType);
                break;
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(message, this);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        Util.showToast(error.getMessage(), this);
    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {
        runOnUiThread(() -> {
            if (progressBar != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (progressBar != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {
        finish();
    }

    public void setMasterData() {

        List<SSMasterDatabase> list = DatabaseManager.getDBInstance(Platform.getInstance()).
                getSSMasterDatabaseDao().getSSMasterData();
        String masterDbString = list.get(0).getData();

        Gson gson = new Gson();
        TypeToken<ArrayList<MasterDataList>> token = new TypeToken<ArrayList<MasterDataList>>() {
        };
        ArrayList<MasterDataList> masterDataList = gson.fromJson(masterDbString, token.getType());

        for (MasterDataList obj : masterDataList) {
            if (obj.getForm().equalsIgnoreCase("structure_create")) {
                masterDataLists.add(obj);
            }
        }
    }

    public void logOutUser() {
        // remove user related shared pref data

        Util.saveLoginObjectInPref("");

        try {
            Intent startMain = new Intent(this, LoginActivity.class);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startMain);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    public void showJurisdictionLevel(List<JurisdictionLocation> data, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                if (data != null && !data.isEmpty()) {
                    districtList.clear();
                    //Collections.sort(data, (j1, j2) -> j1.getDistrict().getName().compareTo(j2.getDistrict().getName()));

                    for (int i = 0; i < data.size(); i++) {
//                        if (Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId()
//                                .equalsIgnoreCase(data.get(i).getStateId())) {
                            JurisdictionLocation location = data.get(i);
                            CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                            meetCountry.set_id(location.getId());
                            meetCountry.setName(location.getName());
                            meetCountry.setSelected(false);
                            districtList.add(meetCountry);
                    //    }
                    }
                }
                break;
            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (data != null && !data.isEmpty()) {
                    talukaList.clear();
                    //Collections.sort(data, (j1, j2) -> j1.getTaluka().getName().compareTo(j2.getTaluka().getName()));

                    for (int i = 0; i < data.size(); i++) {
                        //if (selectedDistrict.equalsIgnoreCase(data.get(i).getDistrict().getName())) {
                            JurisdictionLocation location = data.get(i);
                            CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                            meetCountry.set_id(location.getId());
                            meetCountry.setName(location.getName());
                            meetCountry.setSelected(false);
                            talukaList.add(meetCountry);
                        //}
                    }
                }
                break;
            case Constants.JurisdictionLevelName.VILLAGE_LEVEL:
                if (data != null && !data.isEmpty()) {
                    villageList.clear();
                    catchmentVillageList.clear();
                    //Collections.sort(data, (j1, j2) -> j1.getVillage().getName().compareTo(j2.getVillage().getName()));

                    for (int i = 0; i < data.size(); i++) {
                        //if (selectedTaluka.equalsIgnoreCase(data.get(i).getTaluka().getName())) {

                            JurisdictionLocation location = data.get(i);
                            CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                            meetCountry.set_id(location.getId());
                            meetCountry.setName(location.getName());
                            meetCountry.setSelected(false);
                            villageList.add(meetCountry);
                            catchmentVillageList.add(meetCountry);
                        //}
                    }
                }
                break;
        }
    }
}
