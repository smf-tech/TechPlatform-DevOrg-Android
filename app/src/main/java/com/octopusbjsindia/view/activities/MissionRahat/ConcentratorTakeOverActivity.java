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
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.MissionRahat.SearchListData;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
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
    ArrayList<SearchListData> hospitalList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> yesNoList = new ArrayList<>();

    EditText etNumberOfMachines, etNumberOfPowerCables, etNumberOfConnectors, etNasalConula, etDisplayFunctioning, etOxymeterFunctioning,
            etRemoteFunctioning, etUserManual, etCapacity, etUnidentifiedNoise, etSafelyPackaged;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentrator_takeover);
        setTitle("Concentrator Requirement");
        //boolean isForTakeOver = getIntent().getBooleanExtra("isForTakeOver", false);
        actionType = getIntent().getIntExtra("actionType", -1);
        progressBar = findViewById(R.id.lyProgressBar);
        presenter = new ConcentratorTakeOverActivityPresenter(this);
        if (Util.isConnected(this)) {
        //    presenter.getHospitals();
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
        yes.setName("YES");
        yesNoList.add(yes);
        CustomSpinnerObject no = new CustomSpinnerObject();
        no.set_id("2");
        no.setName("NO");
        yesNoList.add(no);
        if (actionType == Constants.MissionRahat.TAKEOVER) {
            type = "takeover";
        }else if (actionType == Constants.MissionRahat.HANDOVER) {
            type = "handover";
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
                        "Select YesNo", yesNoList, false);
                csdGender.show();
                csdGender.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.etOxymeterFunctioning:

                break;
            case R.id.etRemoteFunctioning:

                break;
            case R.id.etUserManual:

                break;
            case R.id.etCapacity:

                break;
            case R.id.etUnidentifiedNoise:

                break;
            case R.id.etSafelyPackaged:

                break;


            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.btSubmit:
                /*if(TextUtils.isEmpty(selectedHospitalId)){
                    Util.showToast(this,"Please select Hospital");
                } else if(TextUtils.isEmpty(etOwnerName.getText().toString())){
                    Util.showToast(this,"Please enter owner name of hospital");
                } else if(TextUtils.isEmpty(etContactNumber.getText().toString())){
                    Util.showToast(this,"Please enter owner contact");
                }  else if(etContactNumber.getText().toString().length()<10){
                    Util.showToast(this,"Please enter valid owner contact");
                } else if(TextUtils.isEmpty(etPermissionOxygenBed.getText().toString())){
                    Util.showToast(this,"Please enter no. of permission granted for Oxygen bed");
                } else if(TextUtils.isEmpty(etPermissionGeneralBed.getText().toString())){
                    Util.showToast(this,"Please enter no. of permission granted for General Bed");
                } else if(TextUtils.isEmpty(etExistingOxygenBed.getText().toString())){
                    Util.showToast(this,"Please enter no. of Existing Oxygen Bed");
                } else if(TextUtils.isEmpty(etExistingGeneralBed.getText().toString())){
                    Util.showToast(this,"Please enter no. of Existing General Bed");
                } else if(TextUtils.isEmpty(etNumberOfConcentratorRequired.getText().toString())){
                    Util.showToast(this,"Please enter number of concentrator required");
                }  else if(1 > Integer.parseInt(etNumberOfConcentratorRequired.getText().toString())){
                    Util.showToast(this,"Please enter valid number of concentrator required");
                } else*/
            {


                HashMap request = new HashMap<String, Object>();

                request.put("requirementId", "example");
                request.put("type", type);
                request.put("numof_machine", "10");
                request.put("capacity", "5L");
                request.put("numof_power_cable", "10");
                request.put("numof_connector", "10");
                request.put("numof_nasal_cannula", "10");
                request.put("is_display_working", "YES");
                request.put("is_oxymeter_working", "YES");
                request.put("is_remote_working", "YES");
                request.put("is_usermanual_availble", "YES");
                request.put("is_unidentifies_noise", "YES");
                request.put("is_safely_packaged", "YES");


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
        Util.showToast(this, response);
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

            case "Select Gender":
                /*for (CustomSpinnerObject obj : genderList) {
                    if (obj.isSelected()) {
                        selectedGender = obj.getName();
//                        selectedTaskID = obj.get_id();
                        break;
                    }
                }
                patientInfoBinding.etSelectGender.setText(selectedGender);*/
                break;
        }
    }
}