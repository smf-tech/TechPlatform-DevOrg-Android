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
import com.octopusbjsindia.databinding.ActivityOxymachineDailyReportBinding;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.MissionRahat.DailyRecordRequestModel;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataList;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataResponse;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataValue;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.events.CommonResponseStatusString;
import com.octopusbjsindia.presenter.MissionRahat.OxyMachineDailyReportPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.util.ArrayList;
import java.util.Objects;

public class OxyMachineDailyReportActivity extends AppCompatActivity implements APIDataListener,View.OnClickListener,
        CustomSpinnerListener{
    private OxyMachineDailyReportPresenter presenter;
    private ActivityOxymachineDailyReportBinding dailyReportBinding;
    private String selectedSlot="", selectedSlotId="";
    private ArrayList<CustomSpinnerObject> reportSlotList = new ArrayList<>();
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_oxymachine_daily_report);
        dailyReportBinding = ActivityOxymachineDailyReportBinding.inflate(getLayoutInflater());
        View view = dailyReportBinding.getRoot();
        setContentView(view);
        activity = OxyMachineDailyReportActivity.this;
        presenter = new OxyMachineDailyReportPresenter(this);
        initView();
    }

    private void initView() {

        setClickListners();
        dailyReportBinding.toolbar.toolbarTitle.setText("Daily Oxygen usage report");

        presenter.getMasterData();
    }

    private void setClickListners() {
        dailyReportBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //submitData();
                if (isAllDataValid()) {
                    showDialog(activity, "Alert", "Do you want to submit ?", "No", "Yes");
                }
            }
        });
        dailyReportBinding.etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setStartDate();
                Util.showDateDialog(OxyMachineDailyReportActivity.this, dailyReportBinding.etStartDate);
            }
        });
        dailyReportBinding.etSelectSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSlotsDropDown();
            }
        });
        dailyReportBinding.toolbar.toolbarBackAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        DailyRecordRequestModel dailyReportRequestModel = new DailyRecordRequestModel();
        Gson gson = new GsonBuilder().create();
        //dailyReportRequestModel.set
        String paramjson = gson.toJson(dailyReportRequestModel);
        return paramjson;
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

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        CommonResponseStatusString responseOBJ = new Gson().fromJson(response, CommonResponseStatusString.class);
        Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                responseOBJ.getMessage(), Snackbar.LENGTH_LONG);
    }

    @Override
    public void showProgressBar() {
        runOnUiThread(() -> {
            if (dailyReportBinding.profileActProgressBar != null && dailyReportBinding.pbProfileAct != null) {
                dailyReportBinding.profileActProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (dailyReportBinding.profileActProgressBar != null && dailyReportBinding.pbProfileAct != null) {
                dailyReportBinding.profileActProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void closeCurrentActivity() {

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
                dailyReportBinding.etSelectSlot.setText(selectedSlot);
                break;

        }
    }




    private boolean isAllDataValid() {
        if (dailyReportBinding.etMachineHours.getText().toString().trim().length() == 0) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter number of machine hours used.", Snackbar.LENGTH_LONG);
            return false;
        } else if (dailyReportBinding.etNumberofPatients.getText().toString().trim().length() == 0) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please enter number of patients.", Snackbar.LENGTH_LONG);
            return false;
        } else if (TextUtils.isEmpty(selectedSlotId)) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please select slot of report.", Snackbar.LENGTH_LONG);
            return false;
        } else if (dailyReportBinding.etStartDate.getText().toString().trim().length() == 0) {
            Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                    "Please select date of report.", Snackbar.LENGTH_LONG);
            return false;
        } else {

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








}