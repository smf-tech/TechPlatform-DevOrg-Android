    package com.octopusbjsindia.view.activities.MissionRahat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataResponse;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataValue;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.presenter.MissionRahat.FilterActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener,
        APIDataListener, CustomSpinnerListener {

    private RelativeLayout progressBar;
    private FilterActivityPresenter presenter;

    private boolean isSubmit = false;
    private String selectedStateId, selectedState, selectedDistrictId, selectedDistrict,
            selectedTalukaId, selectedTaluka, selectedModel, selectedModelId, selectedCapacity, selectedCapacityId
            ;
    private ArrayList<CustomSpinnerObject> stateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> talukaList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineModelList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> capacityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        setTitle("Filter");
        progressBar = findViewById(R.id.ly_progress_bar);
        presenter = new FilterActivityPresenter(this);
        getIntent().getSerializableExtra("request");

        findViewById(R.id.etState).setOnClickListener(this);
        findViewById(R.id.etDistrict).setOnClickListener(this);
        findViewById(R.id.etTaluka).setOnClickListener(this);
        findViewById(R.id.etModelType).setOnClickListener(this);
        findViewById(R.id.etCapacity).setOnClickListener(this);
        findViewById(R.id.btSubmit).setOnClickListener(this);


        presenter.getLocationData(Util.getUserObjectFromPref().getUserLocation().getCountryId().get(0).getId(),
                Util.getUserObjectFromPref().getJurisdictionTypeId(),
                Constants.JurisdictionLevelName.STATE_LEVEL);
        presenter.getMasterData();
    }

    public void setTitle(String title) {
        TextView tvTitle = findViewById(R.id.toolbar_title);
        tvTitle.setText(title);
        findViewById(R.id.toolbar_back_action).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent returnIntent;
        switch (v.getId()) {

            case R.id.toolbar_back_action:
                HashMap<String,Object> request = new HashMap<>();
                request.put("state_id",selectedStateId);
                request.put("district_id",selectedDistrictId);
                request.put("taluka_id",selectedTalukaId);
                request.put("model_type_id",selectedModelId);
                request.put("capacity_id",selectedCapacityId);

                returnIntent = new Intent();
                returnIntent.putExtra("request",request);
                returnIntent.putExtra("isSubmit",isSubmit);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
                break;

            case R.id.btSubmit:

                HashMap<String,Object> reques = new HashMap<>();
                reques.put("state_id",selectedStateId);
                reques.put("district_id",selectedDistrictId);
                reques.put("taluka_id",selectedTalukaId);
                reques.put("model_type_id",selectedModelId);
                reques.put("capacity_id",selectedCapacityId);

                returnIntent = new Intent();
                returnIntent.putExtra("reques",reques);
                returnIntent.putExtra("isSubmit",isSubmit);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
                break;

            case R.id.etState:
                if (stateList.size() > 0) {
                    CustomSpinnerDialogClass cdd6 = new CustomSpinnerDialogClass(this, this,
                            "Select State",
                            stateList,
                            false);
                    cdd6.show();
                    cdd6.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (Util.isConnected(this)) {
                        presenter.getLocationData(Util.getUserObjectFromPref().getUserLocation().getCountryId().get(0).getId(),
                                Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.STATE_LEVEL);
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), this);
                    }
                }
                break;
            case R.id.etDistrict:
                if (districtList.size() > 0) {
                    CustomSpinnerDialogClass csdDisttrict = new CustomSpinnerDialogClass(this, this,
                            "Select District", districtList, false);
                    csdDisttrict.show();
                    csdDisttrict.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (Util.isConnected(this)) {
                        if ((!TextUtils.isEmpty(selectedStateId))) {
                            presenter.getLocationData(selectedStateId,
                                    Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                        } else {
                            Util.showToast("Please select state.", this);
                        }
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), this);
                    }
                }
                break;
            case R.id.etTaluka:
                if (talukaList.size() > 0) {
                    CustomSpinnerDialogClass csdTaluka = new CustomSpinnerDialogClass(this, this,
                            "Select Taluka", talukaList, false);
                    csdTaluka.show();
                    csdTaluka.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (Util.isConnected(this)) {
                        if ((!TextUtils.isEmpty(selectedDistrictId))) {
                            presenter.getLocationData(selectedDistrictId,
                                    Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                    Constants.JurisdictionLevelName.TALUKA_LEVEL);
                        } else {
                            Util.showToast("Please select taluka.", this);
                        }
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), this);
                    }
                }
                break;
            case R.id.etModelType:
                CustomSpinnerDialogClass csdStructureDept = new CustomSpinnerDialogClass(this, this,
                        "Select machine model", machineModelList, false);
                csdStructureDept.show();
                csdStructureDept.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.etCapacity:
                CustomSpinnerDialogClass cddCity = new CustomSpinnerDialogClass(this, this,
                        "Select machine capacity", capacityList, false);
                cddCity.show();
                cddCity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
            case R.id.bt_submit:
//                if (isAllDataValid()) {
//                    presenter.submitHospital(hospitalModel);
//                }
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
        Util.showToast(response, this);
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

    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Select State":
                ((EditText)(findViewById(R.id.etState))).setText("");
                selectedDistrict = "";
                selectedDistrictId = "";
                for (CustomSpinnerObject mState : stateList) {
                    if (mState.isSelected()) {
                        selectedState = mState.getName();
                        selectedStateId = mState.get_id();
                        break;
                    }
                }
                ((EditText)(findViewById(R.id.etState))).setText(selectedState);
                if (selectedState != "") {
                    presenter.getLocationData(selectedStateId,
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                }
                break;
            case "Select District":
                for (CustomSpinnerObject district : districtList) {
                    if (district.isSelected()) {
                        selectedDistrict = district.getName();
                        selectedDistrictId = district.get_id();
                        break;
                    }
                }
                ((EditText)(findViewById(R.id.etDistrict))).setText(selectedDistrict);
                if (selectedDistrict != "") {
                    presenter.getLocationData(selectedDistrictId,
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.TALUKA_LEVEL);
                }
                break;
            case "Select Taluka":
                for (CustomSpinnerObject taluka : talukaList) {
                    if (taluka.isSelected()) {
                        selectedTaluka = taluka.getName();
                        selectedTalukaId = taluka.get_id();
                        break;
                    }
                }
                ((EditText)(findViewById(R.id.etTaluka))).setText(selectedTaluka);
                break;
            case "Select machine model":
                selectedModel = "";
                selectedModelId = "";
                for (CustomSpinnerObject model : machineModelList) {
                    if (model.isSelected()) {
                        selectedModel = model.getName();
                        selectedModelId = model.get_id();
                    }
                }
                ((EditText)(findViewById(R.id.etModelType))).setText(selectedModel);
                break;
            case "Select machine capacity":
                selectedCapacity = "";
                selectedCapacityId = "";
                for (CustomSpinnerObject capacity : capacityList) {
                    if (capacity.isSelected()) {
                        selectedCapacity = capacity.getName();
                        selectedCapacityId = capacity.get_id();
                    }
                }
                ((EditText)(findViewById(R.id.etCapacity))).setText(selectedCapacity);
                break;
        }
    }

    public void showJurisdictionLevel(List<JurisdictionLocationV3> jurisdictionLevels, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.STATE_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    stateList.clear();
                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getState().getName().compareTo(j2.getState().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject meetState = new CustomSpinnerObject();
                        meetState.set_id(location.getId());
                        meetState.setName(location.getName());
                        meetState.setSelected(false);
                        stateList.add(meetState);
                    }
                }
                break;
            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    districtList.clear();
                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getCity().getName().compareTo(j2.getCity().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject district = new CustomSpinnerObject();
                        district.set_id(location.getId());
                        district.setName(location.getName());
                        district.setSelected(false);
                        districtList.add(district);
                    }
                }
                break;
            case Constants.JurisdictionLevelName.TALUKA_LEVEL:
                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
                    talukaList.clear();
                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getCity().getName().compareTo(j2.getCity().getName()));

                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
                        CustomSpinnerObject taluka = new CustomSpinnerObject();
                        taluka.set_id(location.getId());
                        taluka.setName(location.getName());
                        taluka.setSelected(false);
                        talukaList.add(taluka);
                    }
                }
                break;
            default:
                break;
        }
    }


    public void setMasterData(MasterDataResponse masterDataResponse) {
        for (MasterDataList obj : masterDataResponse.getData()) {
            if (obj.getForm().equalsIgnoreCase("mr_machine_create")) {
                if (obj.getField().equalsIgnoreCase("machineCapacity")) {
                    for (MasterDataValue data : obj.getData()) {
                        CustomSpinnerObject temp = new CustomSpinnerObject();
                        temp.set_id(data.getId());
                        temp.setName(data.getValue());
                        temp.setSelected(false);
                        capacityList.add(temp);
                    }
                } else if (obj.getField().equalsIgnoreCase("machineModel")) {
                    for (MasterDataValue data : obj.getData()) {
                        CustomSpinnerObject temp = new CustomSpinnerObject();
                        temp.set_id(data.getId());
                        temp.setName(data.getValue());
                        temp.setSelected(false);
                        machineModelList.add(temp);
                    }
                }
            }
        }
    }

}