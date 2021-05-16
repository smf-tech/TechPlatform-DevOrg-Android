package com.octopusbjsindia.view.activities.MissionRahat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.MissionRahat.MachineModel;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataResponse;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataValue;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.presenter.MissionRahat.CreateMachineActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.List;

public class CreateMachineActivity extends AppCompatActivity implements APIDataListener,
        View.OnClickListener, CustomSpinnerListener {
    private RelativeLayout progressBar;
    private CreateMachineActivityPresenter presenter;
    private EditText etState, etModel, etCapacity, etDonor; //etDistrict
    private Button btSubmit, btback;
    private String selectedStateId, selectedState, selectedModel,
            selectedModelId, selectedCapacity, selectedCapacityId, selectedDonorName, selectedDonorId; //selectedDistrictId, selectedDistrict
    private ArrayList<CustomSpinnerObject> stateList = new ArrayList<>();
    //private ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineModelList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> capacityList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> donorList = new ArrayList<>();
    private MachineModel machineModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_machine);

        progressBar = findViewById(R.id.ly_progress_bar);
        presenter = new CreateMachineActivityPresenter(this);
        initView();
        setTitle("Create Machine");
    }

    private void initView() {
        machineModel = new MachineModel();
        etState = findViewById(R.id.et_state);
        //etDistrict = findViewById(R.id.et_district);
        etModel = findViewById(R.id.et_model);
        etCapacity = findViewById(R.id.et_capacity);
        etDonor = findViewById(R.id.et_donor);
        btSubmit = findViewById(R.id.bt_submit);
        etState.setOnClickListener(this);
        //etDistrict.setOnClickListener(this);
        etModel.setOnClickListener(this);
        etCapacity.setOnClickListener(this);
        etDonor.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        findViewById(R.id.toolbar_back_action).setOnClickListener(this);

        presenter.getLocationData(Util.getUserObjectFromPref().getUserLocation().getCountryId().get(0).getId(),
                Util.getUserObjectFromPref().getJurisdictionTypeId(),
                Constants.JurisdictionLevelName.STATE_LEVEL);

        presenter.getMasterData();
    }

    public void setTitle(String title) {
        TextView tvTitle = findViewById(R.id.toolbar_title);
        tvTitle.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.et_state:
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
//            case R.id.et_district:
//                if (districtList.size() > 0) {
//                    CustomSpinnerDialogClass csdDisttrict = new CustomSpinnerDialogClass(this, this,
//                            "Select District", districtList, false);
//                    csdDisttrict.show();
//                    csdDisttrict.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
//                            ViewGroup.LayoutParams.MATCH_PARENT);
//                } else {
//                    if (Util.isConnected(this)) {
//                        if ((!TextUtils.isEmpty(selectedStateId))) {
//                            presenter.getLocationData(selectedStateId,
//                                    Util.getUserObjectFromPref().getJurisdictionTypeId(),
//                                    Constants.JurisdictionLevelName.DISTRICT_LEVEL);
//                        } else {
//                            Util.showToast("Please select state.", this);
//                        }
//                    } else {
//                        Util.showToast(getResources().getString(R.string.msg_no_network), this);
//                    }
//                }
//                break;
            case R.id.et_model:
                CustomSpinnerDialogClass csdStructureDept = new CustomSpinnerDialogClass(this, this,
                        "Select machine model", machineModelList, false);
                csdStructureDept.show();
                csdStructureDept.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_capacity:
                CustomSpinnerDialogClass cddCity = new CustomSpinnerDialogClass(this, this,
                        "Select machine capacity", capacityList, false);
                cddCity.show();
                cddCity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_donor:
                CustomSpinnerDialogClass cddDonor = new CustomSpinnerDialogClass(this, this,
                        "Select machine donor", donorList, false);
                cddDonor.show();
                cddDonor.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.bt_submit:
                if (isAllDataValid()) {
                    presenter.submitMachine(machineModel);
                }
                break;
        }
    }

    private boolean isAllDataValid() {
        if (TextUtils.isEmpty(selectedStateId)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please select state.", Snackbar.LENGTH_LONG);
            return false;
        }
//        else if (TextUtils.isEmpty(selectedDistrictId)) {
//            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
//                    "Please select District.", Snackbar.LENGTH_LONG);
//            return false;
//        }
        else if (TextUtils.isEmpty(selectedModelId)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please select machine model.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(selectedCapacityId)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please select machine capacity.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(selectedDonorId)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please select donor.", Snackbar.LENGTH_LONG);
            return false;
        } else {
            machineModel.setStateId(selectedStateId);
            machineModel.setStateName(selectedState);
//            machineModel.setDistrictId(selectedDistrictId);
//            machineModel.setDistrictName(selectedDistrict);
            machineModel.setModelTypeId(selectedModelId);
            machineModel.setMachineModel(selectedModel);
            machineModel.setMahineCapacityId(selectedCapacityId);
            machineModel.setMahineCapacity(selectedCapacity);
            machineModel.setDonerId(selectedDonorId);
            machineModel.setDonorName(selectedDonorName);
        }
        return true;
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
//            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
//                if (jurisdictionLevels != null && !jurisdictionLevels.isEmpty()) {
//                    districtList.clear();
//                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getCity().getName().compareTo(j2.getCity().getName()));
//
//                    for (int i = 0; i < jurisdictionLevels.size(); i++) {
//                        JurisdictionLocationV3 location = jurisdictionLevels.get(i);
//                        CustomSpinnerObject district = new CustomSpinnerObject();
//                        district.set_id(location.getId());
//                        district.setName(location.getName());
//                        district.setSelected(false);
//                        districtList.add(district);
//                    }
//                }
//                break;
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
                } else if (obj.getField().equalsIgnoreCase("donor_list")) {
                    for (MasterDataValue data : obj.getData()) {
                        CustomSpinnerObject temp = new CustomSpinnerObject();
                        temp.set_id(data.getId());
                        temp.setName(data.getValue());
                        temp.setSelected(false);
                        donorList.add(temp);
                    }
                }
            }
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
        finish();
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Select State":
//                etDistrict.setText("");
//                selectedDistrict = "";
//                selectedDistrictId = "";
                for (CustomSpinnerObject mState : stateList) {
                    if (mState.isSelected()) {
                        selectedState = mState.getName();
                        selectedStateId = mState.get_id();
                        break;
                    }
                }
                etState.setText(selectedState);
                if (selectedState != "" && selectedState != "State") {
                    presenter.getLocationData(selectedStateId,
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                }
                break;
//            case "Select District":
//                for (CustomSpinnerObject district : districtList) {
//                    if (district.isSelected()) {
//                        selectedDistrict = district.getName();
//                        selectedDistrictId = district.get_id();
//                        break;
//                    }
//                }
//                etDistrict.setText(selectedDistrict);
//                break;
            case "Select machine model":
                selectedModel = "";
                selectedModelId = "";
                for (CustomSpinnerObject model : machineModelList) {
                    if (model.isSelected()) {
                        selectedModel = model.getName();
                        selectedModelId = model.get_id();
                    }
                }
                etModel.setText(selectedModel);
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
                etCapacity.setText(selectedCapacity);
                break;
            case "Select machine donor":
                selectedDonorName = "";
                selectedDonorId = "";
                for (CustomSpinnerObject donor : donorList) {
                    if (donor.isSelected()) {
                        selectedDonorName = donor.getName();
                        selectedDonorId = donor.get_id();
                    }
                }
                etDonor.setText(selectedDonorName);
                break;
        }
    }
}