package com.platform.view.activities;

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
import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.APIDataListener;
import com.platform.listeners.CustomSpinnerListener;
import com.platform.models.SujalamSuphalam.MasterDataList;
import com.platform.models.SujalamSuphalam.MasterDataResponse;
import com.platform.models.SujalamSuphalam.MasterDataValue;
import com.platform.models.SujalamSuphalam.SSMasterDatabase;
import com.platform.models.SujalamSuphalam.Structure;
import com.platform.models.common.CustomSpinnerObject;
import com.platform.models.profile.JurisdictionLocation;
import com.platform.presenter.CreateStructureActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.GPSTracker;
import com.platform.utility.Util;
import com.platform.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateStructureActivity extends AppCompatActivity implements APIDataListener, View.OnClickListener,
        CustomSpinnerListener {

    private RelativeLayout progressBar;
    private CreateStructureActivityPresenter presenter;

    private EditText etDistrict, etTaluka, etHostVillage, etCatchmentVillage, etHostVillagePopulation, etCatchmentVillagePopulation,
            etGatNo, etWaterShedNo, etArea, etStructureName, etStructureType, etStructureOwnerDepartment,
            etNotaDetail, etSubStructureOwnerDepartment, etAdministrativeApprovalNo, etAdministrativeApprovalDate,
            etTechnicalSanctionNo, etAdministrativeEstimateAmount, etApproximateWorkingHours, etApproximateDieselConsumptionAmount,
            etApproximateDieselLiters, etApproximateEstimateQuantity, etRemark;
    private Button btSubmit;

    private String selectedDistrictId, selectedDistrict, selectedTalukaId, selectedTaluka, selectedHostVillageId,
            selectedHostVillage, selectedStructureTypeId, selectedStructureType, selectedStructureOwnerDepartmentId,
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

        //get District
        presenter.getJurisdictionLevelData(Util.getUserObjectFromPref().getOrgId(), "5dc3f0c75dda7604a85b7b58",
                Constants.JurisdictionLevelName.DISTRICT_LEVEL);

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
        etAdministrativeApprovalNo = findViewById(R.id.et_administrative_approval_no);
        etAdministrativeApprovalDate = findViewById(R.id.et_administrative_approval_date);
        etTechnicalSanctionNo = findViewById(R.id.et_technical_sanction_no);
        etAdministrativeEstimateAmount = findViewById(R.id.et_administrative_estimate_amount);
        etApproximateWorkingHours = findViewById(R.id.et_approximate_working_hours);
        etApproximateDieselConsumptionAmount = findViewById(R.id.et_approximate_diesel_consumption_amount);
        etApproximateDieselLiters = findViewById(R.id.et_approximate_diesel_liters);
        etApproximateEstimateQuantity = findViewById(R.id.et_approximate_estimate_quantity);
        etRemark = findViewById(R.id.et_remark);
        btSubmit = findViewById(R.id.bt_submit);

        etDistrict.setOnClickListener(this);
        etTaluka.setOnClickListener(this);
        etHostVillage.setOnClickListener(this);
        etAdministrativeApprovalDate.setOnClickListener(this);
        etCatchmentVillage.setOnClickListener(this);
        etStructureType.setOnClickListener(this);
        etStructureOwnerDepartment.setOnClickListener(this);
        etSubStructureOwnerDepartment.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
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
            case R.id.et_structure_type:
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
                CustomSpinnerDialogClass csdStructerType = new CustomSpinnerDialogClass(this, this,
                        "Select Structure Type", structureTypeList, false);
                csdStructerType.show();
                csdStructerType.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_administrative_approval_date:
                Util.showDateDialog(this, etAdministrativeApprovalDate);
                break;
            case R.id.bt_submit:
                if (isAllDataValid()) {
                    presenter.submitStructure(structureData);
                }

                break;

        }
    }

    private boolean isAllDataValid() {
        if (TextUtils.isEmpty(selectedDistrict)
                || TextUtils.isEmpty(selectedTaluka)
                || TextUtils.isEmpty(selectedHostVillage)
                || TextUtils.isEmpty(etHostVillagePopulation.getText().toString())
                || TextUtils.isEmpty(android.text.TextUtils.join(",", selectedCatchmentVillage))
                || TextUtils.isEmpty(etCatchmentVillagePopulation.getText().toString())
                || TextUtils.isEmpty(etGatNo.getText().toString())
                || TextUtils.isEmpty(etWaterShedNo.getText().toString())
                || TextUtils.isEmpty(etArea.getText().toString())
                || TextUtils.isEmpty(etStructureName.getText().toString())
                || TextUtils.isEmpty(selectedStructureOwnerDepartmentId)
                || TextUtils.isEmpty(selectedSubStructureOwnerDepartmentId)
                || TextUtils.isEmpty(etNotaDetail.getText().toString())
                || TextUtils.isEmpty(selectedStructureTypeId)
                || TextUtils.isEmpty(etAdministrativeApprovalNo.getText().toString())
                || TextUtils.isEmpty(etAdministrativeApprovalDate.getText().toString())
                || TextUtils.isEmpty(etTechnicalSanctionNo.getText().toString())
                || TextUtils.isEmpty(etAdministrativeEstimateAmount.getText().toString())
                || TextUtils.isEmpty(etApproximateWorkingHours.getText().toString())
                || TextUtils.isEmpty(etApproximateDieselConsumptionAmount.getText().toString())
                || TextUtils.isEmpty(etApproximateDieselLiters.getText().toString())
                || TextUtils.isEmpty(etApproximateEstimateQuantity.getText().toString())) {

            Util.snackBarToShowMsg(this.getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please, feel proper information.",
                    Snackbar.LENGTH_LONG);
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
            structureData.setAdministrativeApprovalNo(etAdministrativeApprovalNo.getText().toString());
            structureData.setAdministrativeApprovalDate(etAdministrativeApprovalDate.getText().toString());
            structureData.setTechnicalSectionNumber(etTechnicalSanctionNo.getText().toString());
            structureData.setAdministrativeEstimateAmount(etAdministrativeEstimateAmount.getText().toString());
            structureData.setApprxWorkingHrs(etApproximateWorkingHours.getText().toString());
            structureData.setApprxDieselConsumptionRs(etApproximateDieselConsumptionAmount.getText().toString());
            structureData.setApprxDieselConsumptionLt(etApproximateDieselLiters.getText().toString());
            structureData.setApprxEstimateQunty(etApproximateEstimateQuantity.getText().toString());
            structureData.setLat(location.getLatitude());
            structureData.setLog(location.getLongitude());
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
                //get Taluka
                presenter.getJurisdictionLevelData(Util.getUserObjectFromPref().getOrgId(), "5dc3f0c75dda7604a85b7b58",
                        Constants.JurisdictionLevelName.TALUKA_LEVEL);
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
                //get Taluka
                presenter.getJurisdictionLevelData(Util.getUserObjectFromPref().getOrgId(), "5dc3f0c75dda7604a85b7b58",
                        Constants.JurisdictionLevelName.VILLAGE_LEVEL);

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

            case "Select Structure Type":
                for (CustomSpinnerObject obj : structureTypeList) {
                    if (obj.isSelected()) {
                        selectedStructureType = obj.getName();
                        selectedStructureTypeId = obj.get_id();
                    }
                }
                etStructureType.setText(selectedStructureType);
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
                    Collections.sort(data, (j1, j2) -> j1.getDistrict().getName().compareTo(j2.getDistrict().getName()));

                    for (int i = 0; i < data.size(); i++) {
                        JurisdictionLocation location = data.get(i);
                        CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                        meetCountry.set_id(location.getDistrictId());
                        meetCountry.setName(location.getDistrict().getName());
                        meetCountry.setSelected(false);
                        districtList.add(meetCountry);
                    }
                }
                break;
            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (data != null && !data.isEmpty()) {
                    talukaList.clear();
                    Collections.sort(data, (j1, j2) -> j1.getTaluka().getName().compareTo(j2.getTaluka().getName()));

                    for (int i = 0; i < data.size(); i++) {
                        if (selectedDistrict.equalsIgnoreCase(data.get(i).getDistrict().getName())) {
                            JurisdictionLocation location = data.get(i);
                            CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                            meetCountry.set_id(location.getTalukaId());
                            meetCountry.setName(location.getTaluka().getName());
                            meetCountry.setSelected(false);
                            talukaList.add(meetCountry);
                        }
                    }
                    //get Village
                    presenter.getJurisdictionLevelData(Util.getUserObjectFromPref().getOrgId(), "5dc3f0c75dda7604a85b7b58",
                            Constants.JurisdictionLevelName.VILLAGE_LEVEL);

                }
                break;
            case Constants.JurisdictionLevelName.VILLAGE_LEVEL:
                if (data != null && !data.isEmpty()) {
                    villageList.clear();
                    catchmentVillageList.clear();
                    Collections.sort(data, (j1, j2) -> j1.getVillage().getName().compareTo(j2.getVillage().getName()));

                    for (int i = 0; i < data.size(); i++) {
                        if (selectedTaluka.equalsIgnoreCase(data.get(i).getTaluka().getName())) {

                            JurisdictionLocation location = data.get(i);
                            CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                            meetCountry.set_id(location.getVillageId());
                            meetCountry.setName(location.getVillage().getName());
                            meetCountry.setSelected(false);
                            villageList.add(meetCountry);
                            catchmentVillageList.add(meetCountry);
                        }
                    }
                }
                break;
        }
    }
}
