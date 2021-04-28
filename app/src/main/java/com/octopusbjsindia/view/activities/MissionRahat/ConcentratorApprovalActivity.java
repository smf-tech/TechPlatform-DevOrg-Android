package com.octopusbjsindia.view.activities.MissionRahat;

import androidx.appcompat.app.AppCompatActivity;

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

import com.android.volley.VolleyError;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.MissionRahat.RequirementDetailsData;
import com.octopusbjsindia.models.MissionRahat.SearchListData;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.presenter.MissionRahat.ConcentratorApprovalActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ConcentratorApprovalActivity extends AppCompatActivity implements View.OnClickListener, CustomSpinnerListener, APIDataListener {

    ConcentratorApprovalActivityPresenter presenter;
    RelativeLayout progressBar;
    private ArrayList<CustomSpinnerObject> machineList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> selectedMachineList = new ArrayList<>();
    String selectedMachineCodes = "", selectedMachineID = "", requestId = "";
    int position;
    EditText etNumberOfConcentratorApproved, etMachines, etDateMachineAllocation, etObservationReason;
    TextView tvHospitalName, tvAddress, tvVisitDate, tvMachineRequired, tvPersonVisited, tvDesignation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentrator_approval);

        position  = getIntent().getIntExtra("position",-1);
        requestId  = getIntent().getStringExtra("RequestId");
        setTitle("Requirement Details");

        progressBar = findViewById(R.id.lyProgressBar);
        presenter = new ConcentratorApprovalActivityPresenter(this);

        tvHospitalName = findViewById(R.id.tvHospitalName);
        tvAddress = findViewById(R.id.tvAddress);
        tvPersonVisited = findViewById(R.id.tvPersonVisited);
        tvDesignation = findViewById(R.id.tvDesignation);
        tvVisitDate = findViewById(R.id.tvVisitDate);
        tvMachineRequired = findViewById(R.id.tvMachineRequired);
        etNumberOfConcentratorApproved = findViewById(R.id.etNumberOfConcentratorApproved);
        etMachines = findViewById(R.id.etMachines);
        etDateMachineAllocation = findViewById(R.id.etDateMachineAllocation);
        etObservationReason = findViewById(R.id.etObservationReason);

        etMachines.setOnClickListener(this);
        etDateMachineAllocation.setOnClickListener(this);
        findViewById(R.id.btnApprove).setOnClickListener(this);
        findViewById(R.id.btReject).setOnClickListener(this);

        if (Util.isConnected(this)) {
            presenter.getDetails(requestId);
            presenter.getMachines();
        } else {
            Util.showToast(this, getResources().getString(R.string.msg_no_network));
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
            case R.id.toolbar_back_action:
                finish();
                break;
            case R.id.etMachines:
                CustomSpinnerDialogClass csdStructerType = new CustomSpinnerDialogClass(this, this,
                        "Select Machines", machineList, true);
                csdStructerType.show();
                csdStructerType.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.etDateMachineAllocation:
                Util.showDateDialogMin(this, etDateMachineAllocation);
                break;
            case R.id.btnApprove:
                if (TextUtils.isEmpty(etNumberOfConcentratorApproved.getText().toString())) {
                    Util.showToast(this, "Please enter Approve quantity of oxygen concentrator");
                } else if (TextUtils.isEmpty(etNumberOfConcentratorApproved.getText().toString())) {
                    Util.showToast(this, "Please enter Approve quantity of oxygen concentrator");
                } else if (TextUtils.isEmpty(etMachines.getText().toString())) {
                    Util.showToast(this, "Please select oxygen concentrator codes");
                } else if (TextUtils.isEmpty(etDateMachineAllocation.getText().toString())) {
                    Util.showToast(this, "Please enter Approve quantity of oxygen concentrator");
                } else if (Integer.parseInt(etNumberOfConcentratorApproved.getText().toString()) >
                        Integer.parseInt(tvMachineRequired.getText().toString())) {
                    Util.showToast(this, "Approve quantity should be less than Required quantity");
                } else if (selectedMachineList.size() != Integer.parseInt(etNumberOfConcentratorApproved.getText().toString())) {
                    Util.showToast(this, "Approve quantity and number of selected oxygen concentrator does not match");
                } else {
                    HashMap request = new HashMap<String,Object>();
                    request.put("requirementId",requestId);
                    request.put("visited_date",new Date().getTime());
                    request.put("approve_quantity_oxygen_concentrator_machines",etNumberOfConcentratorApproved.getText().toString());
                    request.put("status","approved");
                    request.put("praposed_date",Util.dateTimeToTimeStamp(etDateMachineAllocation.getText().toString(),"00:00"));
                    request.put("feedback",etObservationReason.getText().toString());
                    request.put("machines_allotted",selectedMachineID.split(","));

                    presenter.submitRequest(request);
                }
                break;
            case R.id.btReject:
                if (TextUtils.isEmpty(etObservationReason.getText().toString())) {
                    Util.showToast(this, "Please enter reason of rejection");
                } else {
                    HashMap request = new HashMap<String,Object>();
                    request.put("requirementId",requestId);
                    request.put("feedback",etObservationReason.getText().toString());
                    request.put("status","rejected");

                    presenter.submitRequest(request);
                }
                break;

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
        Intent returnIntent = new Intent();
        returnIntent.putExtra("position",position);
        returnIntent.putExtra("status","Approved");
        setResult(Activity.RESULT_OK,returnIntent);
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

    public void setData(RequirementDetailsData data) {
        tvHospitalName.setText(data.getHospitalName());
        tvAddress.setText(data.getAddress());
        tvPersonVisited.setText(data.getVisitedPersonName());
        tvDesignation.setText(data.getDesignation());
        tvMachineRequired.setText(data.getRequestedQuantityConcentratorMachine().toString());
        tvVisitDate.setText(Util.getDateFromTimestamp(new Date().getTime(), Constants.FORM_DATE));
    }

    public void setMachineList(List<SearchListData> data) {
        for (SearchListData obj : data) {
            CustomSpinnerObject sampal = new CustomSpinnerObject();
            sampal.set_id(obj.getId());
            sampal.setName(obj.getValue());
            machineList.add(sampal);

        }
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        selectedMachineCodes= "";
        selectedMachineID = "";
        for (CustomSpinnerObject obj : machineList) {
            if (obj.isSelected()) {
                if(selectedMachineCodes.equals("")){
                    selectedMachineCodes = selectedMachineCodes + obj.getName();
                    selectedMachineID = selectedMachineID + obj.get_id();
                } else {
                    selectedMachineCodes = selectedMachineCodes + "," +obj.getName();
                    selectedMachineID = selectedMachineID + "," +obj.get_id();
                }
                selectedMachineList.add(obj);
            }
        }
        etMachines.setText(selectedMachineCodes);
    }
}