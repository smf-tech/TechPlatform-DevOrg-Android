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
import com.octopusbjsindia.models.MissionRahat.AssignMachinesModel;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataResponse;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataValue;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.presenter.MissionRahat.OxyMachinesAssignActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.List;

public class OxyMachinesAssignActivity extends AppCompatActivity implements View.OnClickListener,
        APIDataListener, CustomSpinnerListener {

    private RelativeLayout progressBar;
    private OxyMachinesAssignActivityPresenter presenter;
    private EditText etDistrict, etTaluka, etCapacity, etCount;
    private Button btSubmit, btBack;
    private String selectedDistrict, selectedDistrictId, selectedTaluka, selectedTalukaId,
            selectedCapacity, selectedCapacityId, machineCount;
    private ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> talukaList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> capacityList = new ArrayList<>();
    private AssignMachinesModel assignMachinesModel;
    private boolean isAssignMachinesToDistrictAllowed, isAssignMachinesToTalukaAllowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oxy_machines_assign);

        progressBar = findViewById(R.id.ly_progress_bar);
        presenter = new OxyMachinesAssignActivityPresenter(this);
        isAssignMachinesToDistrictAllowed = getIntent().getBooleanExtra(
                "isAssignToDistrictAllowed", false);
        isAssignMachinesToTalukaAllowed = getIntent().getBooleanExtra(
                "isAssignToTalukaAllowed", false);
        initView();
        setTitle("Assign Machines");
    }

    private void initView() {
        assignMachinesModel = new AssignMachinesModel();
        etDistrict = findViewById(R.id.et_district);
        etTaluka = findViewById(R.id.et_taluka);
        etCapacity = findViewById(R.id.et_capacity);
        etCount = findViewById(R.id.et_count);
        btSubmit = findViewById(R.id.bt_submit);
        etCapacity.setOnClickListener(this);
        etCount.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        findViewById(R.id.toolbar_back_action).setOnClickListener(this);

        TextView tvTitle = findViewById(R.id.toolbar_title);
        tvTitle.setText("Assign Machines");

        if (isAssignMachinesToDistrictAllowed) {
            etDistrict.setVisibility(View.VISIBLE);
            etDistrict.setOnClickListener(this);
        } else {
            etDistrict.setVisibility(View.GONE);
        }
        if (isAssignMachinesToTalukaAllowed) {
            etTaluka.setVisibility(View.VISIBLE);
            etTaluka.setOnClickListener(this);
        } else {
            etTaluka.setVisibility(View.GONE);
        }

        presenter.getLocationData(Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId(),
                Util.getUserObjectFromPref().getJurisdictionTypeId(),
                Constants.JurisdictionLevelName.DISTRICT_LEVEL);

        presenter.getMasterData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.et_district:
                if (districtList.size() > 0) {
                    CustomSpinnerDialogClass csdDisttrict = new CustomSpinnerDialogClass(this, this,
                            "Select District", districtList, false);
                    csdDisttrict.show();
                    csdDisttrict.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
                } else {
                    if (Util.isConnected(this)) {
                        presenter.getLocationData(Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId(),
                                Util.getUserObjectFromPref().getJurisdictionTypeId(),
                                Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), this);
                    }
                }
                break;
            case R.id.et_taluka:
                if (talukaList.size() > 0) {
                    CustomSpinnerDialogClass cdd6 = new CustomSpinnerDialogClass(this, this,
                            "Select Taluka",
                            talukaList,
                            false);
                    cdd6.show();
                    cdd6.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
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
            case R.id.et_capacity:
                CustomSpinnerDialogClass cddCity = new CustomSpinnerDialogClass(this, this,
                        "Select machine capacity", capacityList, false);
                cddCity.show();
                cddCity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.bt_submit:
                machineCount = etCount.getText().toString();
                if (isAllDataValid()) {
                    presenter.assignMachine(assignMachinesModel);
                }
                break;
        }
    }

    private boolean isAllDataValid() {
        if (TextUtils.isEmpty(selectedDistrictId)) {
            if (isAssignMachinesToDistrictAllowed) {
                Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                        "Please select district.", Snackbar.LENGTH_LONG);
                return false;
            }
        } else if (TextUtils.isEmpty(selectedTalukaId)) {
            if (isAssignMachinesToTalukaAllowed) {
                Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                        "Please select taluka.", Snackbar.LENGTH_LONG);
                return false;
            }
        } else if (TextUtils.isEmpty(selectedCapacityId)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please select machine capacity.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(machineCount)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter machine count to assign.", Snackbar.LENGTH_LONG);
            return false;
        } else {
            if (isAssignMachinesToDistrictAllowed) {
                assignMachinesModel.setDistrictId(selectedDistrictId);
            }
            if (isAssignMachinesToTalukaAllowed) {
                assignMachinesModel.setTalukaId(selectedTalukaId);
            }
            assignMachinesModel.setMahineCapacityId(selectedCapacityId);
            assignMachinesModel.setMachineCount(machineCount);
        }
        return true;
    }

    public void showJurisdictionLevel(List<JurisdictionLocationV3> jurisdictionLevels, String levelName) {
        switch (levelName) {
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
                    //Collections.sort(jurisdictionLevels, (j1, j2) -> j1.getState().getName().compareTo(j2.getState().getName()));

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
            case "Select District":
                etTaluka.setText("");
                selectedTaluka = "";
                selectedTalukaId = "";
                for (CustomSpinnerObject cDistrict : districtList) {
                    if (cDistrict.isSelected()) {
                        selectedDistrict = cDistrict.getName();
                        selectedDistrictId = cDistrict.get_id();
                        break;
                    }
                }
                etDistrict.setText(selectedDistrict);
                if (selectedDistrict != "" && selectedDistrictId != "") {
                    presenter.getLocationData(selectedDistrictId,
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.TALUKA_LEVEL);
                }
                break;
            case "Select Taluka":
                for (CustomSpinnerObject cTaluka : talukaList) {
                    if (cTaluka.isSelected()) {
                        selectedTaluka = cTaluka.getName();
                        selectedTalukaId = cTaluka.get_id();
                        break;
                    }
                }
                etTaluka.setText(selectedTaluka);
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
        }
    }
}