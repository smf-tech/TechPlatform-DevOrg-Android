package com.octopusbjsindia.view.activities.MissionRahat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.R;
import com.octopusbjsindia.databinding.ActivityPatientInfoLayoutBinding;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.MissionRahat.DailyRecordRequestModel;
import com.octopusbjsindia.models.MissionRahat.OxygenMachineList;
import com.octopusbjsindia.models.MissionRahat.PatientInfoRequestModel;
import com.octopusbjsindia.models.MissionRahat.PatientInfoResponseModel;
import com.octopusbjsindia.models.MissionRahat.SearchListData;
import com.octopusbjsindia.models.MissionRahat.SearchListResponse;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataResponse;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataValue;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.events.CommonResponseStatusString;
import com.octopusbjsindia.presenter.MissionRahat.PatientInfoPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PatientInfoActivity extends AppCompatActivity implements APIDataListener, View.OnClickListener,
        CustomSpinnerListener {
    private PatientInfoPresenter presenter;
    private OxygenMachineList oxygenMachineList;
    private ArrayList<CustomSpinnerObject> genderList = new ArrayList<>();
    private ActivityPatientInfoLayoutBinding patientInfoBinding;
    private String selectedSlot = "", selectedSlotId = "";
    private ArrayList<CustomSpinnerObject> reportSlotList = new ArrayList<>();
    private Activity activity;
    private int position, patients_benefited_count = 0;
    private double hours_used_count = 0;
    private String machineCode, selectedGender = "";


    private OxygenMachineList receivedOxygenMachineData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_oxymachine_daily_report);
        patientInfoBinding = ActivityPatientInfoLayoutBinding.inflate(getLayoutInflater());
        View view = patientInfoBinding.getRoot();
        setContentView(view);
        activity = PatientInfoActivity.this;
        presenter = new PatientInfoPresenter(this);
        initView();
    }

    private void initView() {

        setClickListners();
        patientInfoBinding.toolbar.toolbarTitle.setText("Patient Information");
        presenter.getMasterData();
        Gson gson = new Gson();
        if (getIntent().getExtras() != null) {
            String machineDataString = getIntent().getExtras().getString("MachineDataString");
            position = getIntent().getExtras().getInt("position");
            oxygenMachineList = gson.fromJson(machineDataString, OxygenMachineList.class);
            Log.d("machine_code---", oxygenMachineList.getCode());
            machineCode = oxygenMachineList.getId();
            patientInfoBinding.etSelectMachines.setText(machineCode);

        }

        CustomSpinnerObject male = new CustomSpinnerObject();
        male.set_id("1");
        male.setName("Male");
        genderList.add(male);
        CustomSpinnerObject female = new CustomSpinnerObject();
        female.set_id("2");
        female.setName("Female");
        genderList.add(female);

        //TODO need to discuss for api  and status of machine
        if (oxygenMachineList.getStatus().equalsIgnoreCase("working")){
            //preFillData(); or call api for data
            presenter.getPatientInfo(getRequestData());
        }else {
            patientInfoBinding.etOtherRemark.setVisibility(View.GONE);
            patientInfoBinding.etMachineUsedDays.setVisibility(View.GONE);
            patientInfoBinding.etEndSaturation.setVisibility(View.GONE);
            patientInfoBinding.etEndDate.setVisibility(View.GONE);
        }
//        presenter.getPatientInfo(getRequestData());
    }

    private String getRequestData() {
        HashMap request = new HashMap<String, Object>();
        request.put("machine_id", machineCode);
        Gson gson = new GsonBuilder().create();
        String params = gson.toJson(request);
        return params;
    }

    private void preFillData() {

    }



    private void setClickListners() {
        patientInfoBinding.toolbar.toolbarBackAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        patientInfoBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //submitData();
                if (isAllDataValid()) {
                    showDialog(activity, "Alert", "Do you want to submit ?", "No", "Yes");
                }
            }
        });
        patientInfoBinding.etSelectGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderDropDown();
            }
        });

        patientInfoBinding.etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.showDateDialog(PatientInfoActivity.this, patientInfoBinding.etStartDate);
            }
        });
        patientInfoBinding.etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.showDateDialogMin(PatientInfoActivity.this, patientInfoBinding.etEndDate);
            }
        });

    }

    private void showGenderDropDown() {
        CustomSpinnerDialogClass csdGender = new CustomSpinnerDialogClass(PatientInfoActivity.this, this,
                "Select Gender", genderList, false);
        csdGender.show();
        csdGender.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void showSlotsDropDown() {
        CustomSpinnerDialogClass cddCity = new CustomSpinnerDialogClass(this, this,
                "Select report slot", reportSlotList, false);

        cddCity.show();
        cddCity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void callSubmitMethod() {
        presenter.submitDailyReportData(getMouRequestData());
    }

    private String getMouRequestData() {
        PatientInfoRequestModel patientInfoRequestModel = new PatientInfoRequestModel();
        Gson gson = new GsonBuilder().create();
/*

        patientInfoRequestModel.setMachineStartDate(Util.getDateInLong(patientInfoBinding.etStartDate.getText().toString()));
        patientInfoRequestModel.setGender(patientInfoBinding.etSelectGender.getText().toString());
        patientInfoRequestModel.setName(patientInfoBinding.etPatientName.getText().toString());
        patientInfoRequestModel.setAge(Integer.parseInt(patientInfoBinding.etPatientAge.getText().toString()));
        patientInfoRequestModel.setIcmrCode(patientInfoBinding.etIcmrCode.getText().toString());
        patientInfoRequestModel.setAdharCard(patientInfoBinding.etPatientsAadhar.getText().toString());
        patientInfoRequestModel.setMobileNumber(Long.parseLong(patientInfoBinding.etMobileNumber.getText().toString()));
        patientInfoRequestModel.setStartSaturationLevel(Double.parseDouble(patientInfoBinding.etStartSaturation.getText().toString()));
        patientInfoRequestModel.setMachineId(machineCode);
*/

        /*if (oxygenMachineList.getStatus().equalsIgnoreCase("working")) {
            patientInfoRequestModel.setEndSaturationLevel(Double.parseDouble(patientInfoBinding.etEndSaturation.getText().toString()));
        patientInfoRequestModel.setMachineEndDate(Util.getDateInLong(patientInfoBinding.etEndDate.getText().toString()));
            patientInfoRequestModel.setNoOfDays(Integer.parseInt(patientInfoBinding.etMachineUsedDays.getText().toString()));
        patientInfoRequestModel.setRemark(patientInfoBinding.etOtherRemark.getText().toString());

        }*/


        HashMap request = new HashMap<String, Object>();
        request.put("machine_id", machineCode);
        request.put("name", patientInfoBinding.etPatientName.getText().toString());
        request.put("gender", patientInfoBinding.etSelectGender.getText().toString());
        request.put("age",Integer.parseInt(patientInfoBinding.etPatientAge.getText().toString()));
        request.put("icmr_code", patientInfoBinding.etIcmrCode.getText().toString());
        request.put("adhar_card",patientInfoBinding.etPatientsAadhar.getText().toString());
        request.put("mobile_number",Long.parseLong(patientInfoBinding.etMobileNumber.getText().toString()));
        request.put("start_saturation_level",Double.parseDouble(patientInfoBinding.etStartSaturation.getText().toString()));
        request.put("start_date",Util.getDateInLong(patientInfoBinding.etStartDate.getText().toString()));
        //yes no field


        if (oxygenMachineList.getStatus().equalsIgnoreCase("working")) {

            request.put("end_saturation_level", Double.parseDouble(patientInfoBinding.etEndSaturation.getText().toString()));
            request.put("end_date",Util.getDateInLong(patientInfoBinding.etEndDate.getText().toString()));
            request.put("no_of_days",Integer.parseInt(patientInfoBinding.etMachineUsedDays.getText().toString()));
            request.put("remark", patientInfoBinding.etOtherRemark.getText().toString());

        }





        //Gson gson = new GsonBuilder().create();
        String params = gson.toJson(request);

        return params;
    }


    public void setMasterData(MasterDataResponse masterDataResponse) {
        for (MasterDataList obj : masterDataResponse.getData()) {
            if (obj.getForm().equalsIgnoreCase("mission_rahat_report_slot")) {
                if (obj.getField().equalsIgnoreCase("reportSlot")) {
                    for (MasterDataValue data : obj.getData()) {
                        CustomSpinnerObject temp = new CustomSpinnerObject();
                        temp.set_id(data.getId());
                        temp.setName(data.getValue());
                        temp.setSelected(false);
                        reportSlotList.add(temp);
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
        CommonResponseStatusString responseOBJ = new Gson().fromJson(response, CommonResponseStatusString.class);
        /*Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                responseOBJ.getMessage(), Snackbar.LENGTH_LONG);*/
        Util.showToast(responseOBJ.getMessage(), this);
        closeCurrentActivity();
    }

    @Override
    public void showProgressBar() {
        runOnUiThread(() -> {
            if (patientInfoBinding.profileActProgressBar != null && patientInfoBinding.pbProfileAct != null) {
                patientInfoBinding.profileActProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (patientInfoBinding.profileActProgressBar != null && patientInfoBinding.pbProfileAct != null) {
                patientInfoBinding.profileActProgressBar.setVisibility(View.GONE);
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
            case "Select report slot":
                selectedSlot = "";
                selectedSlotId = "";
                for (CustomSpinnerObject capacity : reportSlotList) {
                    if (capacity.isSelected()) {
                        selectedSlot = capacity.getName();
                        selectedSlotId = capacity.get_id();
                    }
                }
                //  patientInfoBinding.etSelectSlot.setText(selectedSlot);
                break;

            case "Select Gender":
                for (CustomSpinnerObject obj : genderList) {
                    if (obj.isSelected()) {
                        selectedGender = obj.getName();
//                        selectedTaskID = obj.get_id();
                        break;
                    }
                }
                patientInfoBinding.etSelectGender.setText(selectedGender);
                break;
        }
    }


    private boolean isAllDataValid() {
        if (patientInfoBinding.etPatientName.getText().toString().trim().length() == 0) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter name of patients.", Snackbar.LENGTH_LONG);
            return false;
        } else if (patientInfoBinding.etSelectGender.getText().toString().trim().length() == 0) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please select gender.", Snackbar.LENGTH_LONG);
            return false;
        } else if (patientInfoBinding.etPatientAge.getText().toString().trim().length() == 0) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter age of patient.", Snackbar.LENGTH_LONG);
            return false;
        } else if (patientInfoBinding.etIcmrCode.getText().toString().trim().length() == 0) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter ICMR code of patient.", Snackbar.LENGTH_LONG);
            return false;
        } else if (patientInfoBinding.etPatientsAadhar.getText().toString().trim().length() != 12) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter Aadhaar number of patient.", Snackbar.LENGTH_LONG);
            return false;
        } else if (patientInfoBinding.etMobileNumber.getText().toString().trim().length() != 10) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter mobile of patients.", Snackbar.LENGTH_LONG);
            return false;
        } else if (patientInfoBinding.etStartSaturation.getText().toString().trim().length() == 0) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter start saturation level of patient.", Snackbar.LENGTH_LONG);
            return false;
        }/* else if (patientInfoBinding.etEndSaturation.getText().toString().trim().length() == 0) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter end saturation level of patient.", Snackbar.LENGTH_LONG);
            return false;
        } else if (patientInfoBinding.etMachineUsedDays.getText().toString().trim().length() == 0) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter number of machine hours used.", Snackbar.LENGTH_LONG);
            return false;
        }*/ else {

        }
        return true;
    }


    @Override
    public void onClick(View v) {
        CustomSpinnerDialogClass cddCity = new CustomSpinnerDialogClass(this, this,
                "Select State",
                reportSlotList,
                false);

        cddCity.show();
        cddCity.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }


    //submit button confirmation
    public void showDialog(Context context, String dialogTitle, String message, String btn1String, String
            btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(context));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        if (!TextUtils.isEmpty(dialogTitle)) {
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(dialogTitle);
            title.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
            text.setText(message);
            text.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(btn1String)) {
            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setText(btn1String);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                // Close dialog
                dialog.dismiss();
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                // Close dialog
                try {
                    dialog.dismiss();
                    callSubmitMethod();
                } catch (IllegalStateException e) {
                    Log.e("TAG", e.getMessage());
                }
            });
        }

        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


    public void setPatientInfo(String get_hospitals, String response) {
        PatientInfoResponseModel patientInfoResponseModel =
                new Gson().fromJson(response, PatientInfoResponseModel.class);

        patientInfoBinding.etSelectMachines.setText(machineCode);
        patientInfoBinding.etPatientName.setText(patientInfoResponseModel.getPatientInfoResponse().getName());
        patientInfoBinding.etSelectGender.setText(patientInfoResponseModel.getPatientInfoResponse().getGender());
        patientInfoBinding.etPatientAge.setText(String.valueOf(patientInfoResponseModel.getPatientInfoResponse().getAge()));
        patientInfoBinding.etIcmrCode.setText(patientInfoResponseModel.getPatientInfoResponse().getIcmrCode());
        patientInfoBinding.etPatientsAadhar.setText(patientInfoResponseModel.getPatientInfoResponse().getAdharCard());
        patientInfoBinding.etMobileNumber.setText(String.valueOf(patientInfoResponseModel.getPatientInfoResponse().getMobileNumber()));
        patientInfoBinding.etStartDate.setText(Util.getFormattedDateFromTimestamp(patientInfoResponseModel.getPatientInfoResponse().getStartDate()));
        patientInfoBinding.etStartSaturation.setText(String.valueOf(patientInfoResponseModel.getPatientInfoResponse().getStartSaturationLevel()));

        patientInfoBinding.etSelectMachines.setClickable(false);
        patientInfoBinding.etPatientName.setClickable(false);
        patientInfoBinding.etSelectGender.setClickable(false);
        patientInfoBinding.etPatientAge.setClickable(false);
        patientInfoBinding.etIcmrCode.setClickable(false);
        patientInfoBinding.etPatientsAadhar.setClickable(false);
        patientInfoBinding.etMobileNumber.setClickable(false);
        patientInfoBinding.etStartDate.setClickable(false);
        patientInfoBinding.etStartSaturation.setClickable(false);

    }
}