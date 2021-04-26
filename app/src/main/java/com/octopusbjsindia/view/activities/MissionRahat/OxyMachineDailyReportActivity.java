package com.octopusbjsindia.view.activities.MissionRahat;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.databinding.ActivityOxymachineDailyReportBinding;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.MissionRahat.DailyRecordRequestModel;
import com.octopusbjsindia.presenter.MissionRahat.OxyMachineDailyReportPresenter;
import com.octopusbjsindia.utility.Util;

public class OxyMachineDailyReportActivity extends AppCompatActivity implements APIDataListener,
        CustomSpinnerListener {
    private OxyMachineDailyReportPresenter presenter;
    ActivityOxymachineDailyReportBinding dailyReportBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_oxymachine_daily_report);
        dailyReportBinding = ActivityOxymachineDailyReportBinding.inflate(getLayoutInflater());
        View view = dailyReportBinding.getRoot();
        setContentView(view);
        presenter = new OxyMachineDailyReportPresenter(this);
        initView();
    }

    private void initView() {

        setClickListners();
        dailyReportBinding.toolbar.toolbarTitle.setText("Daily Oxygen usage report");


    }

    private void setClickListners() {
        dailyReportBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //submitData();
                /*if (dailyReportBinding.checkBoxTerms.isChecked()){
                    callSubmitMethod();
                }else {
                    Util.showToast(OxyMachineDailyReportActivity.this,"Please check terms and conditions before submit.");
                }*/
            }
        });
        dailyReportBinding.etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setStartDate();
                Util.showDateDialog(OxyMachineDailyReportActivity.this, dailyReportBinding.etStartDate);
            }
        });
/*        dailyReportBinding.etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setEndDate();
            }
        });*/
        dailyReportBinding.toolbar.toolbarBackAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    }

    public void showSuccessResponse(String response) {
    }
}