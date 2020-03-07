package com.octopusbjsindia.view.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.JurisdictionLocation;
import com.octopusbjsindia.models.smartgirl.TrainerBachListResponseModel;
import com.octopusbjsindia.presenter.TrainerBatchListPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.TrainerBatchListRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TrainerBatchListActivity extends AppCompatActivity implements TrainerBatchListRecyclerAdapter.OnRequestItemClicked, TrainerBatchListRecyclerAdapter.OnApproveRejectClicked {
    //--Constant
    final String GET_CATEGORY = "getCategory";
    //------
    public EditText tv_startdate, tv_enddate;
    public EditText et_select_program, et_workshop_category, et_select_state, et_select_district, et_select_city, et_select_venue, et_traner_name, et_traner_additional, et_total_beneficiaries;
    public String et_select_program_str = "", et_workshop_category_str = "", et_select_state_str = "", et_select_district_str = "", et_select_city_str = "", et_select_venue_str = "", et_traner_name_str = "", et_traner_additional_str = "", et_total_beneficiaries__str = "";
    public TrainerBatchListPresenter presenter;
    public RecyclerView rv_trainerbactchlistview;
    public TrainerBatchListRecyclerAdapter trainerBatchListRecyclerAdapter;
    private Button btn_create_batch, btn_cancel;
    //----declaration
    private RelativeLayout progressBar;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> stateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> categoryList = new ArrayList<>();
    private String selectedDistrictId, selectedDistrict, selectedStateId, selectedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_batchlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        presenter = new TrainerBatchListPresenter(this);
        //setMasterData();
        //---
        progressBar = findViewById(R.id.ly_progress_bar);
        rv_trainerbactchlistview = findViewById(R.id.rv_trainerbactchlistview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        rv_trainerbactchlistview.setLayoutManager(layoutManager);
        //-------

        presenter.getBatchList();

    }


//progress loaders

    public void showProgressBar() {
        runOnUiThread(() -> {
            if (progressBar != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (progressBar != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void showJurisdictionLevel(List<JurisdictionLocation> data, String levelName) {
        switch (levelName) {
            case Constants.JurisdictionLevelName.DISTRICT_LEVEL:
                if (data != null && !data.isEmpty()) {
                    districtList.clear();
                    //Collections.sort(data, (j1, j2) -> j1.getDistrict().getName().compareTo(j2.getDistrict().getName()));

                    for (int i = 0; i < data.size(); i++) {
//                        if (Util.getUserObjectFromPref().getUserLocation().getStateId().get(0).getId()
//                                .equalsIgnoreCase(data.get(i).getStateId())) {
                        JurisdictionLocation location = data.get(i);
                        CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                        meetCountry.set_id(location.getId());
                        meetCountry.setName(location.getName());
                        meetCountry.setSelected(false);
                        districtList.add(meetCountry);
                        //    }
                    }
                }
                break;

            case Constants.JurisdictionLevelName.STATE_LEVEL:
                if (data != null && !data.isEmpty()) {
                    stateList.clear();
                    //Collections.sort(data, (j1, j2) -> j1.getTaluka().getName().compareTo(j2.getTaluka().getName()));

                    for (int i = 0; i < data.size(); i++) {
                        //if (selectedDistrict.equalsIgnoreCase(data.get(i).getDistrict().getName())) {
                        JurisdictionLocation location = data.get(i);
                        CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                        meetCountry.set_id(location.getId());
                        meetCountry.setName(location.getName());
                        meetCountry.setSelected(false);
                        stateList.add(meetCountry);
                        //}
                    }
                }
                break;
        }
    }


    public void showReceivedBatchList(TrainerBachListResponseModel trainerBachListResponseModel) {
        Util.logger("Leaves -", "---");
        //tmUserLeaveApplicationsList = data;
        trainerBatchListRecyclerAdapter = new TrainerBatchListRecyclerAdapter(this, trainerBachListResponseModel.getTrainerBachListdata(),
                this, this);
        rv_trainerbactchlistview.setAdapter(trainerBatchListRecyclerAdapter);
    }

    @Override
    public void onItemClicked(int pos) {

    }

    @Override
    public void onApproveClicked(int pos) {

    }

    @Override
    public void onRejectClicked(int pos) {

    }
}
