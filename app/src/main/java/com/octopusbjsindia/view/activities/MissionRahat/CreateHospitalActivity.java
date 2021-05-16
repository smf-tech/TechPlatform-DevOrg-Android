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
import com.octopusbjsindia.models.MissionRahat.HospitalModel;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataResponse;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataValue;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.JurisdictionLocationV3;
import com.octopusbjsindia.presenter.MissionRahat.CreateHospitalActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.List;

public class CreateHospitalActivity extends AppCompatActivity implements View.OnClickListener,
        APIDataListener, CustomSpinnerListener {

    private RelativeLayout progressBar;
    private CreateHospitalActivityPresenter presenter;
    private EditText etState, etDistrict, etTaluka, etHospitalName, etAddress, etPersonName, etDesignation,
            etMobile, etSelectUserType;
    private Button btSubmit, btback;
    private String selectedStateId, selectedState, selectedDistrictId, selectedDistrict,
            selectedTalukaId, selectedTaluka, selectedHospitalType, selectedHospitalTypeID;
    private ArrayList<CustomSpinnerObject> stateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> talukaList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> hospitalTypeList = new ArrayList<>();
    private HospitalModel hospitalModel;
    private EditText etRegistrationNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_hospital);

        progressBar = findViewById(R.id.ly_progress_bar);
        presenter = new CreateHospitalActivityPresenter(this);
        initView();
        setTitle("Create Hospital");
        presenter.getMasterData();
    }

    private void initView() {
        hospitalModel = new HospitalModel();
        etState = findViewById(R.id.et_state);
        etDistrict = findViewById(R.id.et_district);
        etTaluka = findViewById(R.id.et_taluka);
        etHospitalName = findViewById(R.id.et_hospital_name);
        etAddress = findViewById(R.id.et_hospital_address);
        etPersonName = findViewById(R.id.et_responsible_person_name);
        etDesignation = findViewById(R.id.et_person_designation);
        etMobile = findViewById(R.id.et_mobile_number);
        etSelectUserType  = findViewById(R.id.et_select_user_type);
        etRegistrationNo  = findViewById(R.id.et_registration_no);
        btSubmit = findViewById(R.id.bt_submit);

        etState.setOnClickListener(this);
        etDistrict.setOnClickListener(this);
        etTaluka.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
        etSelectUserType.setOnClickListener(this);
        etRegistrationNo.setOnClickListener(this);
        findViewById(R.id.toolbar_back_action).setOnClickListener(this);

        presenter.getLocationData(Util.getUserObjectFromPref().getUserLocation().getCountryId().get(0).getId(),
                Util.getUserObjectFromPref().getJurisdictionTypeId(),
                Constants.JurisdictionLevelName.STATE_LEVEL);
    }

    public void setTitle(String title) {
        TextView tvTitle = findViewById(R.id.toolbar_title);
        tvTitle.setText(title);
        findViewById(R.id.toolbar_back_action).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.et_select_user_type:
                showSlotsDropDown();
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
            case R.id.et_district:
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
            case R.id.et_taluka:
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
            case R.id.bt_submit:
                if (isAllDataValid()) {
                    presenter.submitHospital(hospitalModel);
                }
                break;
        }
    }

    private void showSlotsDropDown() {
        CustomSpinnerDialogClass cddCity = new CustomSpinnerDialogClass(this, this,
                "Select user type", hospitalTypeList, false);

        cddCity.show();
        cddCity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }


    public void setMasterData(MasterDataResponse masterDataResponse) {
        for (MasterDataList obj : masterDataResponse.getData()) {
            if (obj.getForm().equalsIgnoreCase("mission_rahat_incharge_type")) {
                if (obj.getField().equalsIgnoreCase("inchargeType")) {
                    for (MasterDataValue data : obj.getData()) {
                        CustomSpinnerObject temp = new CustomSpinnerObject();
                        temp.set_id(data.getId());
                        temp.setName(data.getValue());
                        temp.setSelected(false);
                        hospitalTypeList.add(temp);
                    }
                }
            }
        }
    }


    private boolean isAllDataValid() {
        if (TextUtils.isEmpty(selectedStateId)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please select state.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(selectedDistrictId)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please select district.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(selectedTalukaId)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please select taluka.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(selectedHospitalTypeID)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please select user type Hospital/NGO etc.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etHospitalName.getText().toString().trim())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter hospital name.", Snackbar.LENGTH_LONG);
            return false;
        }/* else if (TextUtils.isEmpty(etRegistrationNo.getText().toString().trim())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter Registration number.", Snackbar.LENGTH_LONG);
            return false;
        }*/
        else if (TextUtils.isEmpty(etAddress.getText().toString().trim())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter hospital address.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etPersonName.getText().toString().trim())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter responsible person name.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etDesignation.getText().toString().trim())) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter designation of person.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(etMobile.getText().toString().trim()) ||
                etMobile.getText().toString().trim().length() != 10) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter proper mobile number.", Snackbar.LENGTH_LONG);
            return false;
        } else {
            hospitalModel.setStateId(selectedStateId);
            hospitalModel.setDistrictId(selectedDistrictId);
            hospitalModel.setTaukaId(selectedTalukaId);
            hospitalModel.setHospitalName(etHospitalName.getText().toString().trim());
            hospitalModel.setAddress(etAddress.getText().toString().trim());
            hospitalModel.setPersonName(etPersonName.getText().toString().trim());
            hospitalModel.setDesignation(etDesignation.getText().toString().trim());
            hospitalModel.setMobile_number(etMobile.getText().toString().trim());
            hospitalModel.setHospitalTypeId(selectedHospitalTypeID);
            hospitalModel.setRegistrationNo(etRegistrationNo.getText().toString().trim());
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
                etDistrict.setText("");
                selectedDistrict = "";
                selectedDistrictId = "";
                for (CustomSpinnerObject mState : stateList) {
                    if (mState.isSelected()) {
                        selectedState = mState.getName();
                        selectedStateId = mState.get_id();
                        break;
                    }
                }
                etState.setText(selectedState);
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
                etDistrict.setText(selectedDistrict);
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
                etTaluka.setText(selectedTaluka);
                break;
            case "Select user type":
                for (CustomSpinnerObject district : hospitalTypeList) {
                    if (district.isSelected()) {
                        selectedHospitalType = district.getName();
                        selectedHospitalTypeID = district.get_id();
                        break;
                    }
                }
                etSelectUserType.setText(selectedHospitalType);
                break;

        }
    }
}