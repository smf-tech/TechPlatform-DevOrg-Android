package com.octopusbjsindia.view.activities.MissionRahat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octopusbjsindia.R;
import com.octopusbjsindia.adapter.OxyMachineListAdapter;
import com.octopusbjsindia.databinding.LayoutMouOxymachineBinding;
import com.octopusbjsindia.databinding.LayoutOxymachineListBinding;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.MissionRahat.MouRequestModel;
import com.octopusbjsindia.models.MissionRahat.OxygenMachineList;
import com.octopusbjsindia.models.MissionRahat.OxygenMachineListModel;
import com.octopusbjsindia.models.events.CommonResponseStatusString;
import com.octopusbjsindia.presenter.MissionRahat.OxyMachineListActivityPresenter;
import com.octopusbjsindia.presenter.MissionRahat.OxyMachineMouActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;

import java.util.ArrayList;
import java.util.Objects;

public class OxyMachineListActivity extends AppCompatActivity implements OxyMachineListAdapter.OnRequestItemClicked, APIDataListener,
        CustomSpinnerListener {
    private OxyMachineListActivityPresenter presenter;
    private LayoutOxymachineListBinding layoutOxymachineListBinding;
    private Activity activity;
    private ArrayList<OxygenMachineList>oxygenMachineLists = new ArrayList<>();
    private OxyMachineListAdapter oxyMachineListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.layout_mou_oxymachine);
        layoutOxymachineListBinding = LayoutOxymachineListBinding.inflate(getLayoutInflater());
        View view = layoutOxymachineListBinding.getRoot();
        setContentView(view);
        activity = OxyMachineListActivity.this;
        presenter = new OxyMachineListActivityPresenter(this);
        initView();
    }

    private void initView() {

        setClickListners();
        layoutOxymachineListBinding.toolbar.toolbarTitle.setText("Oxygen Concentrator List");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        layoutOxymachineListBinding.rvTrainerbactchlistview.setLayoutManager(layoutManager);

        oxyMachineListAdapter = new OxyMachineListAdapter(this,oxygenMachineLists, this);
        layoutOxymachineListBinding.rvTrainerbactchlistview.setAdapter(oxyMachineListAdapter);
        presenter.getOxyMachineList();

    }

    private void setClickListners() {


        layoutOxymachineListBinding.toolbar.toolbarBackAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        hideProgressBar();
        CommonResponseStatusString responseOBJ = new Gson().fromJson(response, CommonResponseStatusString.class);
        Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                responseOBJ.getMessage(), Snackbar.LENGTH_LONG);
        OxygenMachineListModel oxygenMachineListModel = new Gson().fromJson(response, OxygenMachineListModel.class);
        oxygenMachineLists.addAll(oxygenMachineListModel.getOxygenMachineLists());
        oxyMachineListAdapter.notifyDataSetChanged();


    }

    @Override
    public void showProgressBar() {
        runOnUiThread(() -> {
            if (layoutOxymachineListBinding.lyProgressBar != null && layoutOxymachineListBinding.pbProfileAct != null) {
                layoutOxymachineListBinding.lyProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (layoutOxymachineListBinding.lyProgressBar != null && layoutOxymachineListBinding.pbProfileAct != null) {
                layoutOxymachineListBinding.lyProgressBar.setVisibility(View.GONE);
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
        CommonResponseStatusString responseOBJ = new Gson().fromJson(response, CommonResponseStatusString.class);
        Util.snackBarToShowMsg(this.getWindow().getDecorView().findViewById(android.R.id.content),
                responseOBJ.getMessage(), Snackbar.LENGTH_LONG);
    }


    @Override
    public void onItemClicked(int pos) {
        if (Util.getUserObjectFromPref().getRoleCode() == Constants.SSModule.ROLE_CODE_MR_HOSPITAL_INCHARGE) {
            Intent intent1 = new Intent(this, OxyMachineDailyReportActivity.class);
            Gson gson = new Gson();
            String machineDataString = gson.toJson(oxygenMachineLists.get(pos));
            intent1.putExtra("MachineDataString", machineDataString);
            startActivity(intent1);
        }
    }
}