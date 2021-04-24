package com.octopusbjsindia.view.activities.MissionRahat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.MissionRahat.MachineModel;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataValue;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.presenter.MissionRahat.CreateMachineActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;

public class CreateMachineActivity extends AppCompatActivity implements APIDataListener,
        View.OnClickListener, CustomSpinnerListener {

    private RelativeLayout progressBar;
    private CreateMachineActivityPresenter presenter;
    private EditText etState, etDistrict, etModel, etCapacity, etDoner;
    private Button btSubmit;
    private String selectedStateId, selectedState, selectedDistrictId, selectedDistrict, selectedModel,
            selectedCapacity, donerName;
    private ArrayList<CustomSpinnerObject> stateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineModelList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> capacityList = new ArrayList<>();
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
        etDistrict = findViewById(R.id.et_district);
        etModel = findViewById(R.id.et_model);
        etCapacity = findViewById(R.id.et_capacity);
        etDoner = findViewById(R.id.et_doner);
        btSubmit = findViewById(R.id.bt_submit);
        etState.setOnClickListener(this);
        etDistrict.setOnClickListener(this);
        etModel.setOnClickListener(this);
        etCapacity.setOnClickListener(this);
        etDoner.setOnClickListener(this);

        presenter.getLocationData("",
                Util.getUserObjectFromPref().getJurisdictionTypeId(),
                Constants.JurisdictionLevelName.STATE_LEVEL);
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
                        presenter.getLocationData("",
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
            case R.id.et_machine_make_model:
                machineModelList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("structureDept"))
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setSelected(false);
                            machineModelList.add(temp);
                        }
                }
                CustomSpinnerDialogClass csdStructureDept = new CustomSpinnerDialogClass(this, this,
                        "Select Structure owner department", machineModelList, false);
                csdStructureDept.show();
                csdStructureDept.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_capacity:
                capacityList.clear();
                for (int i = 0; i < masterDataLists.size(); i++) {
                    if (masterDataLists.get(i).getField().equalsIgnoreCase("structureSubDept"))
                        for (MasterDataValue obj : masterDataLists.get(i).getData()) {
                            CustomSpinnerObject temp = new CustomSpinnerObject();
                            temp.set_id(obj.getId());
                            temp.setName(obj.getValue());
                            temp.setSelected(false);
                            capacityList.add(temp);
                        }
                }
                CustomSpinnerDialogClass cddCity = new CustomSpinnerDialogClass(this, this,
                        "Select Sub Structure owner department", capacityList, false);
                cddCity.show();
                cddCity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;

        }
    }

    public void setMasterData() {

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

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void closeCurrentActivity() {

    }

    @Override
    public void onCustomSpinnerSelection(String type) {

    }
}