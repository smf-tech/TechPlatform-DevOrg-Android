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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
    private TrainerBachListResponseModel trainerBachListResponseModel;
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


    public void showReceivedBatchList(TrainerBachListResponseModel trainerBachListResponseModelReceived) {
        Util.logger("Leaves -", "---");
        //tmUserLeaveApplicationsList = data;
        trainerBachListResponseModel = trainerBachListResponseModelReceived;
        trainerBatchListRecyclerAdapter = new TrainerBatchListRecyclerAdapter(this, trainerBachListResponseModel.getTrainerBachListdata(),
                this, this);
        rv_trainerbactchlistview.setAdapter(trainerBatchListRecyclerAdapter);
    }

    @Override
    public void onItemClicked(int pos) {

    }

    @Override
    public void onApproveClicked(int pos) {

        String paramjson = new Gson().toJson(getAddTrainerReqJson(pos));
        presenter.addTrainerToBatch(paramjson);
    }

    @Override
    public void onRejectClicked(int pos) {

    }

    public JsonObject getAddTrainerReqJson(int pos) {
        String batchId = trainerBachListResponseModel.getTrainerBachListdata().get(pos).get_id();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("batch_id", batchId);
        requestObject.addProperty("phone", "9881499768");

        return requestObject;
    }

    public void addTrainerTobatch(int adapterPosition){
        String paramjson = new Gson().toJson(getAddTrainerReqJson(adapterPosition));
        presenter.addTrainerToBatch(paramjson);
    }

    public void addSelfTrainerToBatch(int adapterPosition){
        String paramjson = new Gson().toJson(getTrainerReqJson(adapterPosition));
        presenter.addSelfTrainerToBatch(paramjson);
    }

    public JsonObject getTrainerReqJson(int pos) {
        String batchId = trainerBachListResponseModel.getTrainerBachListdata().get(pos).get_id();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("batch_id", batchId);

        return requestObject;
    }

    public void fillPreTestFormToBatch(int adapterPosition){
        String paramjson = new Gson().toJson(getPreTestReqJson(adapterPosition));
        presenter.submitPreTestFormToBatch(paramjson);
    }

    public JsonObject getPreTestReqJson(int pos) {
        String batchId = trainerBachListResponseModel.getTrainerBachListdata().get(pos).get_id();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("batch_id", batchId);
        requestObject.addProperty("gender", "male");
        requestObject.addProperty("age", "25");
        requestObject.addProperty("education", "BA");
        requestObject.addProperty("trainer_id", "5e4b73ec515e88607566f393");
        requestObject.addProperty("trainer_name", "Kumood S Bongale");
        requestObject.addProperty("trainer_phone", "9881499768");
        requestObject.addProperty("occupation", "Teacher");
        requestObject.addProperty("email", "abc@gmail.com");


        return requestObject;
    }

    public void submitFeedbsckToBatch(int adapterPosition){
        String paramjson = new Gson().toJson(getFeedbackReqJson(adapterPosition));
        presenter.submitFeedbsckToBatch(paramjson);
    }

    public JsonObject getFeedbackReqJson(int pos) {
        String batchId = trainerBachListResponseModel.getTrainerBachListdata().get(pos).get_id();
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("batch_id", batchId);
        requestObject.addProperty("age", "25");
        requestObject.addProperty("gender", "male");
        requestObject.addProperty("education", "BA");
        requestObject.addProperty("trainer_id", "5e4b73ec515e88607566f393");
        requestObject.addProperty("feedback_type", "Post");
        requestObject.addProperty("user_type", "ZP");
        requestObject.addProperty("user_type", "ZP");
        requestObject.addProperty("trainer_name", "Kumood S Bongale");
        requestObject.addProperty("trainer_phone", "9881499768");
        requestObject.addProperty("email", "kbongale@bjsindia.org");
        requestObject.addProperty("name", "Kumood Bongale");
        requestObject.addProperty("work_experience_in_year", "6");
        requestObject.addProperty("training_location", "Pune");
        requestObject.addProperty("training_date_from", "1582796403000");
        requestObject.addProperty("training_date_to", "1582911603000");
        requestObject.addProperty("are_your_teacher", "false");
        requestObject.addProperty("name_of_school_or_college", "");
        requestObject.addProperty("master_trainer_name", "Test master trainer");
        requestObject.addProperty("is_this_program_helpfull_for_girls", "Yes");
        requestObject.addProperty("is_this_program_helpfull_for_parents", "Yes");

        return requestObject;
    }

}
