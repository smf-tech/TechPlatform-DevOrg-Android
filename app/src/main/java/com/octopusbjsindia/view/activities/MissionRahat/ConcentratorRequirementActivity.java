package com.octopusbjsindia.view.activities.MissionRahat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.MissionRahat.SearchListData;
import com.octopusbjsindia.presenter.MissionRahat.ConcentratorRequirementActivityPresenter;
import com.octopusbjsindia.utility.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ConcentratorRequirementActivity extends AppCompatActivity implements View.OnClickListener, APIDataListener {

    ConcentratorRequirementActivityPresenter presenter;
    RelativeLayout progressBar;
    ArrayList<SearchListData> hospitalList;

    EditText etHospitalName, etAddress, etOwnerName, etContactNumber, etInChargeName, etContactNumberInCharge,
            etPermissionOxygenBed, etPermissionGeneralBed, etExistingOxygenBed, etExistingGeneralBed,
            etNumberOfConcentratorRequired;
    String selectedHospitalId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentrator_requirement);
        setTitle("Concentrator Requirement");

        progressBar = findViewById(R.id.lyProgressBar);
        presenter = new ConcentratorRequirementActivityPresenter(this);
        if (Util.isConnected(this)) {
            presenter.getHospitals();
        } else {
            Util.showToast(this, getResources().getString(R.string.msg_no_network));
        }

        etHospitalName = findViewById(R.id.etHospitalName);
        etAddress = findViewById(R.id.etAddress);
        etOwnerName = findViewById(R.id.etOwnerName);
        etContactNumber = findViewById(R.id.etContactNumber);
        etInChargeName = findViewById(R.id.etInChargeName);
        etContactNumberInCharge = findViewById(R.id.etContactNumberInCharge);
        etPermissionOxygenBed = findViewById(R.id.etPermissionOxygenBed);
        etPermissionGeneralBed = findViewById(R.id.etPermissionGeneralBed);
        etExistingOxygenBed = findViewById(R.id.etExistingOxygenBed);
        etExistingGeneralBed = findViewById(R.id.etExistingGeneralBed);
        etNumberOfConcentratorRequired = findViewById(R.id.etNumberOfConcentratorRequired);

        etInChargeName.setText(Util.getUserObjectFromPref().getUserName());
        etContactNumberInCharge.setText(Util.getUserObjectFromPref().getUserMobileNumber());

        etHospitalName.setOnClickListener(this);
        findViewById(R.id.btSubmit).setOnClickListener(this);
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
            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.etHospitalName:
                Intent intent = new Intent(this, SearchListActivity.class);
                intent.putExtra("List", hospitalList);
                startActivityForResult(intent, 1001);
                break;
            case R.id.btSubmit:
                if(TextUtils.isEmpty(selectedHospitalId)){
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
                } else {
                    HashMap request = new HashMap<String,Object>();
                    request.put("hospital_id",selectedHospitalId);
                    request.put("owner_name",etOwnerName.getText().toString().trim());
                    request.put("owner_contact_details",etContactNumber.getText().toString().trim());
                    request.put("incharge_name",etInChargeName.getText().toString().trim());
                    request.put("incharge_contact_details",etContactNumberInCharge.toString().trim());

                    HashMap granted = new HashMap<String,Object>();
                    granted.put("oxygen_bed",etPermissionOxygenBed.getText().toString().trim());
                    granted.put("general_bed",etPermissionGeneralBed.getText().toString().trim());
                    request.put("permission_granted",granted);

                    HashMap existing = new HashMap<String,Object>();
                    existing.put("oxygen_bed",etExistingOxygenBed.getText().toString().trim());
                    existing.put("general_bed",etExistingGeneralBed.getText().toString().trim());
                    request.put("existing_bed_capacity",existing);
                    request.put("required_machine",etNumberOfConcentratorRequired.getText().toString().trim());

                    presenter.submitRequest(request);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (resultCode == Activity.RESULT_OK) {
                int pos = data.getIntExtra("result",-1);
                if(pos != -1){
                    selectedHospitalId = hospitalList.get(pos).getId();
                    etHospitalName.setText(hospitalList.get(pos).getValue());
                    etAddress.setText(hospitalList.get(pos).getAddress());
                }
            }
        }
    }

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
}