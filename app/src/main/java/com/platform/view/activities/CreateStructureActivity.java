package com.platform.view.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.platform.R;
import com.platform.listeners.APIDataListener;
import com.platform.listeners.CustomSpinnerListener;
import com.platform.models.SujalamSuphalam.MasterDataList;
import com.platform.models.SujalamSuphalam.MasterDataResponse;
import com.platform.models.SujalamSuphalam.MasterDataValue;
import com.platform.models.common.CustomSpinnerObject;
import com.platform.models.profile.Location;
import com.platform.models.user.UserInfo;
import com.platform.presenter.CreateStructureActivityPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateStructureActivity extends AppCompatActivity implements APIDataListener, View.OnClickListener, CustomSpinnerListener {


    private RelativeLayout progressBar;
    private CreateStructureActivityPresenter presenter;

    TextView etDistrict,etTaluka,etHostVillage,etCatchmentVillage,
            etStructureType,etStructureOwnerDepartment,etSubStructureOwnerDepartment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_structure);

        progressBar = findViewById(R.id.ly_progress_bar);
        presenter = new CreateStructureActivityPresenter(this);
        presenter.getMaster();
        initView();
        setTitle("Create Structure");
    }

    private void initView() {

        //get District
        presenter.getJurisdictionLevelData(Util.getUserObjectFromPref().getOrgId(), "5c4ab05cd503a372d0391467",
                Constants.JurisdictionLevelName.DISTRICT_LEVEL);

        etDistrict = findViewById(R.id.et_district);
        etTaluka = findViewById(R.id.et_taluka);
        etHostVillage = findViewById(R.id.et_host_village);
        etCatchmentVillage = findViewById(R.id.et_catchment_village);
        etStructureType = findViewById(R.id.et_structure_type);
        etStructureOwnerDepartment = findViewById(R.id.et_structure_owner_department);
        etSubStructureOwnerDepartment = findViewById(R.id.et_sub_structure_owner_department);

        etDistrict.setOnClickListener(this);
        etTaluka.setOnClickListener(this);
        etHostVillage.setOnClickListener(this);
        etCatchmentVillage.setOnClickListener(this);
        etStructureType.setOnClickListener(this);
        etStructureOwnerDepartment.setOnClickListener(this);
        etSubStructureOwnerDepartment.setOnClickListener(this);
    }

    public void setTitle(String title){
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
                for(int i=0; i<masterDataLists.size();i++){
                    if(masterDataLists.get(i).getField().equalsIgnoreCase("structureDept"))
                        for (MasterDataValue obj: masterDataLists.get(i).getData()) {
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
                for(int i=0; i<masterDataLists.size();i++){
                    if(masterDataLists.get(i).getField().equalsIgnoreCase("structureSubDept"))
                        for (MasterDataValue obj: masterDataLists.get(i).getData()) {
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
                for(int i=0; i<masterDataLists.size();i++){
                    if(masterDataLists.get(i).getField().equalsIgnoreCase("structureType"))
                    for (MasterDataValue obj: masterDataLists.get(i).getData()) {
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

        }
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type){
            case "Select District":
                for(CustomSpinnerObject obj: districtList){
                    if(obj.isSelected()){
                        selectedDistrict = obj.getName();
                        selectedDistrictId = obj.get_id();
                        break;
                    }
                }
                etDistrict.setText(selectedDistrict);
                //get Taluka
                presenter.getJurisdictionLevelData(Util.getUserObjectFromPref().getOrgId(), "5c4ab05cd503a372d0391467",
                        Constants.JurisdictionLevelName.TALUKA_LEVEL);
                break;
            case "Select Taluka":
                for(CustomSpinnerObject obj: talukaList){
                    if(obj.isSelected()){
                        selectedTaluka = obj.getName();
                        selectedTalukaId = obj.get_id();
                        break;
                    }
                }
                etTaluka.setText(selectedTaluka);
                //get Taluka
                presenter.getJurisdictionLevelData(Util.getUserObjectFromPref().getOrgId(), "5c4ab05cd503a372d0391467",
                        Constants.JurisdictionLevelName.VILLAGE_LEVEL);

                break;
            case "Select Host Village":
                for(CustomSpinnerObject obj: villageList){
                    if(obj.isSelected()){
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
                for(CustomSpinnerObject obj: catchmentVillageList){
                    if(obj.isSelected()){
                        selectedCatchmentVillage.add(obj.getName());
                        selectedCatchmentVillageId.add(obj.get_id());
                    }
                }
                etCatchmentVillage.setText(android.text.TextUtils.join(",", selectedCatchmentVillage));
                break;
            case "Select Structure owner department":
                for(CustomSpinnerObject obj: structureDepartmentList){
                    if(obj.isSelected()){
                        selectedStructureOwnerDepartment=obj.getName();
                        selectedStructureOwnerDepartmentId=obj.get_id();
                    }
                }
                etStructureOwnerDepartment.setText(selectedStructureType);
                break;
            case "Select Sub Structure owner department":
                for(CustomSpinnerObject obj: structureSubDepartmentList){
                    if(obj.isSelected()){
                        selectedSubStructureOwnerDepartment=obj.getName();
                        selectedSubStructureOwnerDepartmentId=obj.get_id();
                    }
                }
                etSubStructureOwnerDepartment.setText(selectedStructureType);
                break;

            case "Select Structure Type":
                for(CustomSpinnerObject obj: structureTypeList){
                    if(obj.isSelected()){
                        selectedStructureType=obj.getName();
                        selectedStructureTypeId=obj.get_id();
                    }
                }
                etStructureType.setText(selectedStructureType);
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

    }

    public void setMasterData(MasterDataResponse masterDataResponse) {
        if(masterDataResponse.getStatus()==1000){
            logOutUser();
        } else {
            for(MasterDataList obj:masterDataResponse.getData()){
                if(obj.getForm().equalsIgnoreCase("structure_create")){
                    masterDataLists.add(obj);
                }
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

    public void showJurisdictionLevel(List<Location> data, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                if (data != null && !data.isEmpty()) {
                    districtList.clear();
                    Collections.sort(data, (j1, j2) -> j1.getDistrict().getName().compareTo(j2.getDistrict().getName()));

                    for (int i = 0; i < data.size(); i++) {
                        Location location = data.get(i);
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
                            Location location = data.get(i);
                            CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                            meetCountry.set_id(location.getTalukaId());
                            meetCountry.setName(location.getTaluka().getName());
                            meetCountry.setSelected(false);
                            talukaList.add(meetCountry);
                        }
                    }
                    //get Village
                    presenter.getJurisdictionLevelData(Util.getUserObjectFromPref().getOrgId(), "5c4ab05cd503a372d0391467",
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

                            Location location = data.get(i);
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
