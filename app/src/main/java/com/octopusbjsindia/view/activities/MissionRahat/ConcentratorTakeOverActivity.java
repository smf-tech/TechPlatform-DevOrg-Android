package com.octopusbjsindia.view.activities.MissionRahat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.MissionRahat.SearchListData;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataResponse;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataValue;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.events.CommonResponseStatusString;
import com.octopusbjsindia.presenter.MissionRahat.ConcentratorRequirementActivityPresenter;
import com.octopusbjsindia.presenter.MissionRahat.ConcentratorTakeOverActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ConcentratorTakeOverActivity extends AppCompatActivity implements View.OnClickListener, APIDataListener, CustomSpinnerListener {

    ConcentratorTakeOverActivityPresenter presenter;
    private int actionType = -1;
    RelativeLayout progressBar;
    String type = "";
    String receivedRequirementId = "";
    ArrayList<SearchListData> hospitalList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> yesNoList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> machineCapacityList = new ArrayList<>();
    private String selectedOption = "", selectedOptionId = "",selectedCapacityId = "";

    EditText etNumberOfMachines, etNumberOfPowerCables, etNumberOfConnectors, etNasalConula, etDisplayFunctioning, etOxymeterFunctioning,
            etRemoteFunctioning, etUserManual, etCapacity, etUnidentifiedNoise, etSafelyPackaged;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentrator_takeover);

        //boolean isForTakeOver = getIntent().getBooleanExtra("isForTakeOver", false);
        actionType = getIntent().getIntExtra("actionType", -1);
        receivedRequirementId  = getIntent().getStringExtra("RequirementId");
        progressBar = findViewById(R.id.lyProgressBar);
        presenter = new ConcentratorTakeOverActivityPresenter(this);
        if (Util.isConnected(this)) {
            presenter.getMasterData();
        } else {
            Util.showToast(this, getResources().getString(R.string.msg_no_network));
        }

        etNumberOfMachines = findViewById(R.id.etNumberOfMachines);
        etNumberOfPowerCables = findViewById(R.id.etNumberOfPowerCables);
        etNumberOfConnectors = findViewById(R.id.etNumberOfConnectors);
        etNasalConula = findViewById(R.id.etNasalConula);
        etDisplayFunctioning = findViewById(R.id.etDisplayFunctioning);
        etOxymeterFunctioning = findViewById(R.id.etOxymeterFunctioning);
        etRemoteFunctioning = findViewById(R.id.etRemoteFunctioning);
        etUserManual = findViewById(R.id.etUserManual);
        etCapacity = findViewById(R.id.etCapacity);
        etUnidentifiedNoise = findViewById(R.id.etUnidentifiedNoise);
        etSafelyPackaged = findViewById(R.id.etSafelyPackaged);


        etDisplayFunctioning.setOnClickListener(this);
        etOxymeterFunctioning.setOnClickListener(this);
        etRemoteFunctioning.setOnClickListener(this);
        etUserManual.setOnClickListener(this);
        etCapacity.setOnClickListener(this);
        etUnidentifiedNoise.setOnClickListener(this);
        etSafelyPackaged.setOnClickListener(this);

        findViewById(R.id.btSubmit).setOnClickListener(this);

        CustomSpinnerObject yes = new CustomSpinnerObject();
        yes.set_id("1");
        yes.setName("yes");
        yesNoList.add(yes);
        CustomSpinnerObject no = new CustomSpinnerObject();
        no.set_id("2");
        no.setName("no");
        yesNoList.add(no);
        if (actionType == Constants.MissionRahat.TAKEOVER) {
            type = "take_over";
            setTitle("Take over form");
        }else if (actionType == Constants.MissionRahat.HANDOVER) {
            type = "hand_over";
            setTitle("Hand over");
        }
    }

    public void setTitle(String title) {
        TextView tvTitle = findViewById(R.id.toolbar_title);
        ImageView back = findViewById(R.id.toolbar_back_action);
        tvTitle.setText(title);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.etDisplayFunctioning:
                CustomSpinnerDialogClass csdGender = new CustomSpinnerDialogClass(ConcentratorTakeOverActivity.this, this,
                        "Display Functioning", yesNoList, false);
                csdGender.show();
                csdGender.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.etOxymeterFunctioning:
                CustomSpinnerDialogClass csdGender1 = new CustomSpinnerDialogClass(ConcentratorTakeOverActivity.this, this,
                        "Oxymeter Functioning", yesNoList, false);
                csdGender1.show();
                csdGender1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.etRemoteFunctioning:
                CustomSpinnerDialogClass csdGender2 = new CustomSpinnerDialogClass(ConcentratorTakeOverActivity.this, this,
                        "Remote Functioning", yesNoList, false);
                csdGender2.show();
                csdGender2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.etUserManual:
                CustomSpinnerDialogClass csdGender3 = new CustomSpinnerDialogClass(ConcentratorTakeOverActivity.this, this,
                        "User Manual Available", yesNoList, false);
                csdGender3.show();
                csdGender3.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.etCapacity:
                CustomSpinnerDialogClass csdGender4 = new CustomSpinnerDialogClass(ConcentratorTakeOverActivity.this, this,
                        "Select Capacity", machineCapacityList, false);
                csdGender4.show();
                csdGender4.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.etUnidentifiedNoise:
                CustomSpinnerDialogClass csdGender5 = new CustomSpinnerDialogClass(ConcentratorTakeOverActivity.this, this,
                        "Noise Observed", yesNoList, false);
                csdGender5.show();
                csdGender5.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.etSafelyPackaged:
                CustomSpinnerDialogClass csdGender6 = new CustomSpinnerDialogClass(ConcentratorTakeOverActivity.this, this,
                        "Safely Packaged", yesNoList, false);
                csdGender6.show();
                csdGender6.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;


            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.btSubmit:
                if(TextUtils.isEmpty(etNumberOfMachines.getText().toString())){
                    Util.showToast(this,"Please enter number of machine");
                } else if(TextUtils.isEmpty(etCapacity.getText().toString())){
                    Util.showToast(this,"Please select capacity");
                } else if(TextUtils.isEmpty(etNumberOfPowerCables.getText().toString())){
                    Util.showToast(this,"Please enter number of power cables.");
                } else if(TextUtils.isEmpty(etNumberOfConnectors.getText().toString())){
                    Util.showToast(this,"Please enter number of connectors.");
                } else if(TextUtils.isEmpty(etNasalConula.getText().toString())){
                    Util.showToast(this,"Please enter number of nasal cannula.");
                } else if(TextUtils.isEmpty(etDisplayFunctioning.getText().toString())){
                    Util.showToast(this,"Please select is display functioning.");
                } else if(TextUtils.isEmpty(etOxymeterFunctioning.getText().toString())){
                    Util.showToast(this,"Please select is oxy meter functioning.");
                } else if(TextUtils.isEmpty(etRemoteFunctioning.getText().toString())){
                    Util.showToast(this,"Please select is oxy remote functioning.");
                } else if(TextUtils.isEmpty(etUserManual.getText().toString())){
                    Util.showToast(this,"Please select is user manual available.");
                }  else if(TextUtils.isEmpty(etUnidentifiedNoise.getText().toString())){
                    Util.showToast(this,"Please select is unidentified noise observed.");
                }  else if(TextUtils.isEmpty(etSafelyPackaged.getText().toString())){
                    Util.showToast(this,"Please select is safely packaged for take over.");
                } else
            {


                HashMap request = new HashMap<String, Object>();
                request.put("requirement_id", receivedRequirementId);
                request.put("form_type", type);
                request.put("numof_machine", etNumberOfMachines.getText().toString());
                request.put("capacity", selectedCapacityId);
                request.put("numof_power_cable",etNumberOfPowerCables.getText().toString());
                request.put("numof_connector", etNumberOfConnectors.getText().toString());
                request.put("numof_nasal_cannula", etNasalConula.getText().toString());
                //yes no field
                request.put("is_display_working", etDisplayFunctioning.getText().toString());
                request.put("is_oxymeter_working", etOxymeterFunctioning.getText().toString());
                request.put("is_remote_working", etRemoteFunctioning.getText().toString());
                request.put("is_usermanual_availble", etUserManual.getText().toString());
                request.put("is_unidentifies_noise", etUnidentifiedNoise.getText().toString());
                request.put("is_safely_packaged", etSafelyPackaged.getText().toString());


                presenter.submitRequest(request);
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == 1001) {
            if (resultCode == Activity.RESULT_OK) {
                int pos = data.getIntExtra("result",-1);
                if(pos != -1){
                    selectedHospitalId = hospitalList.get(pos).getId();
                    etHospitalName.setText(hospitalList.get(pos).getValue());
                    etAddress.setText(hospitalList.get(pos).getAddress());
                    etOwnerName.setText(hospitalList.get(pos).getPersonName());
                    etContactNumber.setText(hospitalList.get(pos).getMobileNumber());
                }
            }
        }
        if (requestCode == 1002) {
            if (resultCode == Activity.RESULT_OK) {
                int pos = data.getIntExtra("result",-1);
                if(pos != -1){
                    selectedHospitalId = hospitalList.get(pos).getId();
                    etHospitalName.setText(hospitalList.get(pos).getValue());
                    etAddress.setText(hospitalList.get(pos).getAddress());
                    etOwnerName.setText(hospitalList.get(pos).getPersonName());
                    etContactNumber.setText(hospitalList.get(pos).getMobileNumber());
                    assignHospitaltoIncharge(selectedHospitalId);
                }
            }
        }*/
    }

    /*public void assignHospitaltoIncharge(String selectedHospitalId) {
        HashMap request = new HashMap<String, Object>();
        request.put("hospital_id", selectedHospitalId);
        presenter.assignHospitaltoIncharge(request);
    }*/

    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(this));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        TextView title = dialog.findViewById(R.id.tv_dialog_title);
        title.setText(getResources().getString(R.string.alert));
        title.setVisibility(View.VISIBLE);

        TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
        text.setText("Are you sure, want to discard");
        text.setVisibility(View.VISIBLE);

        Button button = dialog.findViewById(R.id.btn_dialog);
        button.setText("Yes");
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(v -> {
            // Close dialog
            dialog.dismiss();
            super.onBackPressed();
        });

        Button button1 = dialog.findViewById(R.id.btn_dialog_1);
        button1.setText("No");
        button1.setVisibility(View.VISIBLE);
        button1.setOnClickListener(v -> {
            // Close dialog
            dialog.dismiss();
        });
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(this, message);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        Util.showToast(this, error.getMessage());
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        CommonResponseStatusString responseOBJ = new Gson().fromJson(response, CommonResponseStatusString.class);
        Util.showToast(responseOBJ.getMessage(), this);
        finish();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void closeCurrentActivity() {

    }

    public void setHostpitalList(String get_hospitals, List<SearchListData> data) {
        hospitalList = (ArrayList<SearchListData>) data;
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Display Functioning":
                for (CustomSpinnerObject obj : yesNoList) {
                    if (obj.isSelected()) {
                        selectedOption= obj.getName();
//                        selectedTaskID = obj.get_id();
                        break;
                    }
                }
                etDisplayFunctioning.setText(selectedOption);
                break;

            case "Oxymeter Functioning":
                for (CustomSpinnerObject obj : yesNoList) {
                    if (obj.isSelected()) {
                        selectedOption= obj.getName();
//                        selectedTaskID = obj.get_id();
                        break;
                    }
                }
                etOxymeterFunctioning.setText(selectedOption);
                break;

            case "Remote Functioning":
                for (CustomSpinnerObject obj : yesNoList) {
                    if (obj.isSelected()) {
                        selectedOption= obj.getName();
//                        selectedTaskID = obj.get_id();
                        break;
                    }
                }
                etRemoteFunctioning.setText(selectedOption);
                break;

            case "User Manual Available":
                for (CustomSpinnerObject obj : yesNoList) {
                    if (obj.isSelected()) {
                        selectedOption= obj.getName();
//                        selectedTaskID = obj.get_id();
                        break;
                    }
                }
                etUserManual.setText(selectedOption);
                break;

            case "Select Capacity":
                for (CustomSpinnerObject obj : machineCapacityList) {
                    if (obj.isSelected()) {
                        selectedOption= obj.getName();
                        selectedCapacityId = obj.get_id();
                        break;
                    }
                }
                etCapacity.setText(selectedOption);
                break;

            case "Noise Observed":
                for (CustomSpinnerObject obj : yesNoList) {
                    if (obj.isSelected()) {
                        selectedOption= obj.getName();
//                        selectedTaskID = obj.get_id();
                        break;
                    }
                }
                etUnidentifiedNoise.setText(selectedOption);
                break;

            case "Safely Packaged":
                for (CustomSpinnerObject obj : yesNoList) {
                    if (obj.isSelected()) {
                        selectedOption= obj.getName();
//                        selectedTaskID = obj.get_id();
                        break;
                    }
                }
                etSafelyPackaged.setText(selectedOption);
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
                        machineCapacityList.add(temp);
                    }
                }
            }
        }
    }
}