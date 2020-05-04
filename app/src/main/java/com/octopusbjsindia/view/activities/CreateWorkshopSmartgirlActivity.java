package com.octopusbjsindia.view.activities;

import android.app.ActionBar;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.JurisdictionLocation;
import com.octopusbjsindia.models.profile.OrganizationRolesResponse;
import com.octopusbjsindia.models.smartgirl.AdditionalTrainerListResponseModel;
import com.octopusbjsindia.models.smartgirl.SmartGirlCategoryResponseModel;
import com.octopusbjsindia.models.smartgirl.WorkshopBachList;
import com.octopusbjsindia.presenter.CreateTrainerWorkshopPresenter;
import com.octopusbjsindia.presenter.CreateWorkshopSmartgirlPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.octopusbjsindia.utility.Constants.DAY_MONTH_YEAR;

public class CreateWorkshopSmartgirlActivity extends AppCompatActivity implements View.OnClickListener, CustomSpinnerListener {
    //--Constant
    final String GET_CATEGORY = "getCategory";
    boolean isforEdit = false;
    //-------
    SmartGirlCategoryResponseModel smartGirlCategoryResponseModel;
    //------
    public EditText tv_startdate, tv_enddate;
    public EditText et_select_state_trainer, et_select_district_trainer,et_title_workshop;
    public EditText et_select_program, et_workshop_category, et_select_state, et_select_district, et_select_city, et_select_venue, et_traner_name, et_traner_additional, et_total_beneficiaries;
    public String et_select_program_str = "", et_workshop_category_str = "", et_select_state_str = "", et_select_district_str = "", et_select_city_str = "", et_select_venue_str = "", et_traner_name_str = "", et_traner_additional_id = "", et_total_beneficiaries__str = "";
    public String et_select_state_str_trainer, et_select_district_str_trainer;
    public CreateWorkshopSmartgirlPresenter presenter;
    private Button btn_create_batch, btn_cancel;
    //----declaration
    private RelativeLayout progressBar;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private ArrayList<CustomSpinnerObject> districtList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> stateList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> categoryList = new ArrayList<>();
    private ArrayList<CustomSpinnerObject> TrainerList = new ArrayList<>();
    private String selectedDistrictId, selectedDistrict, selectedStateId, selectedState;
    private String selectedTrainerDistrictId,selectedTrainerStateId;
    private String strJsonObjectString;
    private TextView tvTitle;
    WorkshopBachList workshopBachList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workshop_smartgirl);

        tvTitle = findViewById(R.id.toolbar_title);
        tvTitle.setText("Create Workshop");
        presenter = new CreateWorkshopSmartgirlPresenter(this);

        progressBar = findViewById(R.id.ly_progress_bar);
        btn_create_batch = findViewById(R.id.btn_create_batch);
        btn_cancel = findViewById(R.id.btn_cancel);
        tv_startdate = findViewById(R.id.tv_startdate);
        tv_enddate = findViewById(R.id.tv_enddate);
        tv_startdate.setOnClickListener(this);
        tv_enddate.setOnClickListener(this);
        tv_startdate.setText(Util.getCurrentDate());
        tv_enddate.setText(Util.getCurrentDate());
        et_total_beneficiaries = findViewById(R.id.et_total_beneficiaries);
        et_select_program = findViewById(R.id.et_select_program);

        et_workshop_category = findViewById(R.id.et_workshop_category);
        et_select_state = findViewById(R.id.et_select_state);
        et_select_district = findViewById(R.id.et_select_district);

        et_select_state_trainer = findViewById(R.id.et_select_state_trainer);
        et_select_district_trainer = findViewById(R.id.et_select_district_trainer);
        et_title_workshop  = findViewById(R.id.et_title_workshop);
        et_select_city = findViewById(R.id.et_select_city);
        et_select_venue = findViewById(R.id.et_select_venue);
        et_traner_name = findViewById(R.id.et_traner_name);
        et_traner_name.setText(Util.getUserObjectFromPref().getUserName());
        et_traner_additional = findViewById(R.id.et_traner_additional);

        et_traner_additional.setOnClickListener(this);
        et_select_state.setOnClickListener(this);
        et_select_district.setOnClickListener(this);

        et_select_state_trainer.setOnClickListener(this);
        et_select_district_trainer.setOnClickListener(this);

        et_workshop_category.setOnClickListener(this);
        btn_create_batch.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        // calling states first-

        presenter.getLocationData("",
                Util.getUserObjectFromPref().getJurisdictionTypeId(),
                Constants.JurisdictionLevelName.STATE_LEVEL);
        presenter.getBatchCategory();


        if (getIntent().getStringExtra(Constants.Login.ACTION_EDIT) != null
                && getIntent().getStringExtra(Constants.Login.ACTION_EDIT)
                .equalsIgnoreCase(Constants.Login.ACTION_EDIT)) {
          //  Toast.makeText(CreateWorkshopSmartgirlActivity.this, "request to edit batch.", Toast.LENGTH_LONG).show();
            isforEdit = true;

            if (getIntent().getExtras() != null) {
                strJsonObjectString = getIntent().getExtras().getString("jsonObjectString");
                 workshopBachList
                        = new Gson().fromJson(strJsonObjectString, WorkshopBachList.class);
                Log.d("venue---",workshopBachList.getVenue());
                setEditDataToFields(workshopBachList);
            }

        } else {
            isforEdit = false;
        }
    }

    private void setEditDataToFields(WorkshopBachList workshopBachList) {
        if (workshopBachList.getTitle()!=null) {
            et_title_workshop.setText(workshopBachList.getTitle());
        }else {
            et_title_workshop.setText("");
        }
        if (workshopBachList.getCategory()!=null) {
            et_workshop_category.setText("" + workshopBachList.getCategory().getCategoryName().getDefault());
            et_workshop_category_str = workshopBachList.getCategory().get_id();
        }else {
            et_workshop_category.setText("" + workshopBachList.getBatch_category_id());
            et_workshop_category_str = workshopBachList.getBatch_category_id();
        }
        et_select_state.setText(workshopBachList.getState().getName());
        et_select_state_str = workshopBachList.getState_id();
        et_select_district.setText(workshopBachList.getDistrict().getName());
        et_select_district_str = workshopBachList.getDistrict_id();
        et_select_city.setText(workshopBachList.getCity());
        et_select_venue.setText(workshopBachList.getVenue());
        if (workshopBachList.getCreated_by()!=null){
            et_traner_name.setText(workshopBachList.getCreated_by().get(0).getName());
        }

        et_select_state_trainer.setText(workshopBachList.getAdditional_master_trainer().getState_name());
        et_select_state_str_trainer = workshopBachList.getAdditional_master_trainer().getState_id();
        et_select_district_trainer.setText(workshopBachList.getAdditional_master_trainer().getDistrict_name());
        et_select_district_str_trainer = workshopBachList.getAdditional_master_trainer().getDistrict_id();

        et_traner_additional.setText(workshopBachList.getAdditional_master_trainer().getUser_name());
        tv_startdate.setText(Util.getFormattedDateFromTimestamp(workshopBachList.getWorkShopSchedule().getStartDate()));
        tv_enddate.setText(Util.getFormattedDateFromTimestamp(workshopBachList.getWorkShopSchedule().getEndDate()));
        et_total_beneficiaries.setText(workshopBachList.getTotal_praticipants());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_create_batch:
                if (isAllInputsValid()) {
                    if (isforEdit) {
                        editBatch();
                    } else {
                        createBatch();
                    }
                }
                break;
            case R.id.btn_cancel:
                onBackPressed();
                break;

            case R.id.tv_startdate:
                selectStartDate(tv_startdate, 1);
                break;
            case R.id.tv_enddate:
                selectStartDate(tv_enddate, 2);
                break;
            case R.id.et_traner_additional:
                CustomSpinnerDialogClass csdTrainer = new CustomSpinnerDialogClass(this, this,
                        "Select Trainer", TrainerList, false);
                csdTrainer.show();
                csdTrainer.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;


            case R.id.et_select_state:
                CustomSpinnerDialogClass csdState = new CustomSpinnerDialogClass(this, this,
                        "Select State", stateList, false);
                csdState.show();
                csdState.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_select_district:

                CustomSpinnerDialogClass csdDisttrict = new CustomSpinnerDialogClass(this, this,
                        "Select District", districtList, false);
                csdDisttrict.show();
                csdDisttrict.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            //for trainer-
            case R.id.et_select_state_trainer:
                CustomSpinnerDialogClass csdState_trainer = new CustomSpinnerDialogClass(this, this,
                        "Select State trainer", stateList, false);
                csdState_trainer.show();
                csdState_trainer.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case R.id.et_select_district_trainer:

                CustomSpinnerDialogClass csdDisttrict_trainer = new CustomSpinnerDialogClass(this, this,
                        "Select District trainer", districtList, false);
                csdDisttrict_trainer.show();
                csdDisttrict_trainer.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;

            case R.id.et_workshop_category:

                CustomSpinnerDialogClass csdCategory = new CustomSpinnerDialogClass(this, this,
                        "Select Category", categoryList, false);
                csdCategory.show();
                csdCategory.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                break;

            default:
                break;
        }
    }

    //select start date and end date for workshop
    private void selectStartDate(TextView textview, int flagDateStartEnd) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateWorkshopSmartgirlActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        String selectedDateString = new SimpleDateFormat(DAY_MONTH_YEAR).format(calendar.getTime());
                        // textview.setText(selectedDateString);
                        //textview.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        //check for Date-->
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat formatter = new SimpleDateFormat(DAY_MONTH_YEAR);//new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        Date startDate = null;
                        Date endDate = null;

                        if (flagDateStartEnd == 1) {
                            try {
                                startDate = formatter.parse(selectedDateString);
                                endDate = formatter.parse(tv_enddate.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (startDate.getTime() > endDate.getTime()) {
                                Toast.makeText(CreateWorkshopSmartgirlActivity.this, "Start date should be less than end date.", Toast.LENGTH_LONG).show();
                            } else {
                                textview.setText(selectedDateString);
                            }
                        } else {
                            try {
                                startDate = formatter.parse(selectedDateString);
                                endDate = formatter.parse(tv_enddate.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (startDate.getTime() < endDate.getTime()) {
                                Toast.makeText(CreateWorkshopSmartgirlActivity.this, "End date should be greater than start date.", Toast.LENGTH_LONG).show();
                            } else {
                                textview.setText(selectedDateString);
                            }
                        }

                        //-----
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
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
            /*case GET_CATEGORY:
                if (data != null && !data.isEmpty()) {
                    categoryList.clear();
                    //Collections.sort(data, (j1, j2) -> j1.getTaluka().getName().compareTo(j2.getTaluka().getName()));

                    for (int i = 0; i < data.size(); i++) {
                        //if (selectedDistrict.equalsIgnoreCase(data.get(i).getDistrict().getName())) {
                        JurisdictionLocation location = data.get(i);
                        CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                        meetCountry.set_id(location.getId());
                        meetCountry.setName(location.getName());
                        meetCountry.setSelected(false);
                        categoryList.add(meetCountry);
                        //}
                    }
                }
                break;*/
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

    public void showReceivedCategories(SmartGirlCategoryResponseModel jurisdictionLevelResponse) {
        smartGirlCategoryResponseModel = jurisdictionLevelResponse;
        for (int i = 0; i < jurisdictionLevelResponse.getData().size(); i++) {
            // categoryList.add(jurisdictionLevelResponse.getData().get(i).getName().getDefault());
            CustomSpinnerObject meetCountry = new CustomSpinnerObject();
            meetCountry.set_id(jurisdictionLevelResponse.getData().get(i).get_id());
            meetCountry.setName(jurisdictionLevelResponse.getData().get(i).getName().getDefault());
            meetCountry.setSelected(false);
            categoryList.add(meetCountry);
        }
    }

    @Override
    public void onCustomSpinnerSelection(String type) {
        switch (type) {
            case "Select District trainer":
                for (CustomSpinnerObject obj : districtList) {
                    if (obj.isSelected()) {
                        selectedDistrict = obj.getName();
                        selectedDistrictId = obj.get_id();
                        break;
                    }
                }
                et_select_district_trainer.setText(selectedDistrict);
                et_select_district_str_trainer = selectedDistrictId;

                //get Taluka
                if (!TextUtils.isEmpty(selectedDistrictId)) {
                    presenter.getLocationData(selectedDistrictId,
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.TALUKA_LEVEL);
                }
                if (!TextUtils.isEmpty(selectedStateId)) {
                    String paramjson = new Gson().toJson(getAdditionalTrainerReqJson());
                    presenter.getAdditionalTrainer(paramjson);
                }
                break;
            case "Select State trainer":
                for (CustomSpinnerObject obj : stateList) {
                    if (obj.isSelected()) {
                        selectedState = obj.getName();
                        selectedStateId = obj.get_id();
                        break;
                    }
                }
                et_select_state_trainer.setText(selectedState);
                et_select_state_str_trainer = selectedStateId;
                //get District
                if (!TextUtils.isEmpty(selectedStateId)) {
                    presenter.getLocationData(selectedStateId,
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                }


                break;


            case "Select District":
                for (CustomSpinnerObject obj : districtList) {
                    if (obj.isSelected()) {
                        selectedDistrict = obj.getName();
                        selectedDistrictId = obj.get_id();
                        break;
                    }
                }
                et_select_district.setText(selectedDistrict);
                et_select_district_str = selectedDistrictId;
                selectedTrainerDistrictId = selectedDistrictId;

                //get Taluka
                if (!TextUtils.isEmpty(selectedDistrictId)) {
                    presenter.getLocationData(selectedDistrictId,
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.TALUKA_LEVEL);
                }
                break;
            case "Select State":
                for (CustomSpinnerObject obj : stateList) {
                    if (obj.isSelected()) {
                        selectedState = obj.getName();
                        selectedStateId = obj.get_id();
                        break;
                    }
                }
                et_select_state.setText(selectedState);
                et_select_state_str = selectedStateId;
                selectedTrainerStateId  = selectedStateId;
                //get District
                if (!TextUtils.isEmpty(selectedStateId)) {
                    presenter.getLocationData(selectedStateId,
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                }

                break;
            case "Select Category":
                for (CustomSpinnerObject obj : categoryList) {
                    if (obj.isSelected()) {
                        selectedState = obj.getName();
                        selectedStateId = obj.get_id();
                        break;
                    }
                }
                et_workshop_category.setText(selectedState);
                et_workshop_category_str = selectedStateId;
                //get District
                /*if (!TextUtils.isEmpty(selectedStateId)) {
                    presenter.getLocationData(selectedStateId,
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                }*/

                break;
            case "Select Trainer":
                for (CustomSpinnerObject obj : TrainerList) {
                    if (obj.isSelected()) {
                        selectedState = obj.getName();
                        selectedStateId = obj.get_id();
                        break;
                    }
                }
                et_traner_additional.setText(selectedState);
                et_traner_additional_id = selectedStateId;
                //get District
                /*if (!TextUtils.isEmpty(selectedStateId)) {
                    presenter.getLocationData(selectedStateId,
                            Util.getUserObjectFromPref().getJurisdictionTypeId(),
                            Constants.JurisdictionLevelName.DISTRICT_LEVEL);
                }*/

                break;



        }

    }

    /*public void getBatchCategory() {
        final String getRoleAccessUrl = BuildConfig.BASE_URL
                + String.format(Urls.Home.GET_ROLE_ACCESS);
        Log.d("TAG", "getRoleAccessUrl: url" + getRoleAccessUrl);
        //homeFragment.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall(GET_CATEGORY, getRoleAccessUrl);
    }*/


    public void editBatch() {
        String paramjson = new Gson().toJson(getCreateBatchReqJson());
        //presenter.createBatch(paramjson);
        presenter.editBatch(paramjson);
    }

    public void createBatch() {
        String paramjson = new Gson().toJson(getCreateBatchReqJson());
        //creating workshop here
        presenter.createBatch(paramjson);
    }

    public JsonObject getCreateBatchReqJson() {

        JsonObject requestObject = new JsonObject();
        if (isforEdit) {
            requestObject.addProperty("workshop_id",workshopBachList.get_id() );
        } else {
            requestObject.addProperty("batch_id", "");
        }
        requestObject.addProperty("title", et_title_workshop.getText().toString());
        requestObject.addProperty("batch_category_id", et_workshop_category_str);
        requestObject.addProperty("state_id", et_select_state_str);
        requestObject.addProperty("district_id", et_select_district_str);
        requestObject.addProperty("city", et_select_city.getText().toString());
        requestObject.addProperty("venue", et_select_venue.getText().toString());
        requestObject.addProperty("startDate", Util.getDateInepoch(tv_startdate.getText().toString()));
        requestObject.addProperty("endDate", Util.getDateInepoch(tv_enddate.getText().toString()));
        requestObject.addProperty("total_praticipants", et_total_beneficiaries.getText().toString());
        JsonObject trainerObject = new JsonObject();
        /*trainerObject.addProperty("state_id", "5e2eb9b6385c23393400741a");
        trainerObject.addProperty("district_id", "5e2eb9e6385c23393400741d");
        trainerObject.addProperty("user_id", "5e2ebdce42d73f03fe6ab142");*/

        trainerObject.addProperty("state_id", et_select_state_str_trainer);
        trainerObject.addProperty("district_id", et_select_district_str_trainer);
        trainerObject.addProperty("user_id", et_traner_additional_id);


        requestObject.add("additional_master_trainer", trainerObject);


        return requestObject;
    }

    public JsonObject getAdditionalTrainerReqJson() {

        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("project_id", "5e2eb798385c233934007414");
        requestObject.addProperty("state_id", et_select_state_str_trainer);
        requestObject.addProperty("district_id", et_select_district_str_trainer);// et_select_district_str_trainer);

        return requestObject;
    }


    //Validations
    private boolean isAllInputsValid() {
        String msg = "";

        if (TextUtils.isEmpty(et_title_workshop.getText().toString())) {
            msg = "Please enter workshop title";//getResources().getString(R.string.msg_enter_name);
        } else
        if (TextUtils.isEmpty(et_workshop_category_str)) {
            msg = "Please select batch category";//getResources().getString(R.string.msg_enter_name);
        } else if (TextUtils.isEmpty(et_select_state_str)) {
            msg = "Please select state";//getResources().getString(R.string.msg_enter_name);
        } else if (TextUtils.isEmpty(et_select_district_str)) {
            msg = "Please select district.";//getResources().getString(R.string.msg_enter_proper_date);
        } else if (et_select_city.getText().toString().trim().length() == 0) {
            msg = "Please enter the city.";//getResources().getString(R.string.msg_enter_proper_date);
        } else if (et_select_venue.getText().toString().trim().length() == 0) {
            msg = "Please enter the venue.";//getResources().getString(R.string.msg_enter_proper_date);
        } else if (tv_startdate.getText().toString().trim().length() == 0) {
            msg = "Please select the start date.";//getResources().getString(R.string.msg_enter_proper_date);
        } else if (tv_enddate.getText().toString().trim().length() == 0) {
            msg = "Please select the end date.";//getResources().getString(R.string.msg_enter_proper_date);
        } else if (et_total_beneficiaries.getText().toString().trim().length() == 0) {
            msg = "Please enter total beneficiaries.";//getResources().getString(R.string.msg_enter_proper_date);
        }
        /*else if (et_education.getText().toString().trim().length() == 0) {
            msg = "Please enter the qualification.";//getResources().getString(R.string.msg_enter_proper_date);
        }*/

        if (TextUtils.isEmpty(msg)) {
            return true;
        }

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        return false;
    }

    public void showTrainerList(String response) {
        Log.d("TAG", "TrainerList response:- " + response);
        AdditionalTrainerListResponseModel additionalTrainerListResponseModel
                = new Gson().fromJson(response, AdditionalTrainerListResponseModel.class);
        if (additionalTrainerListResponseModel != null && !additionalTrainerListResponseModel.getTrainerListResponseList().isEmpty()) {
            TrainerList.clear();
            //Collections.sort(data, (j1, j2) -> j1.getTaluka().getName().compareTo(j2.getTaluka().getName()));

            for (int i = 0; i < additionalTrainerListResponseModel.getTrainerListResponseList().size(); i++) {
                //if (selectedDistrict.equalsIgnoreCase(data.get(i).getDistrict().getName())) {
                //JurisdictionLocation location = additionalTrainerListResponseModel.getTrainerListResponseList().get(i);
                CustomSpinnerObject meetCountry = new CustomSpinnerObject();
                meetCountry.set_id(additionalTrainerListResponseModel.getTrainerListResponseList().get(i).get_id());
                meetCountry.setName(additionalTrainerListResponseModel.getTrainerListResponseList().get(i).getName());
                meetCountry.setSelected(false);
                TrainerList.add(meetCountry);
                //}
            }
        }
    }

    @Override
    public void onBackPressed() {
        showDialog(this, "Alert", "Do you want really want to close ?", "No", "Yes");
    }
    //back button confirmation
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
                    finish();
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
    public void workshopCreatedSuccess(String response){
        Toast.makeText(CreateWorkshopSmartgirlActivity.this,"Workshop created successfully",Toast.LENGTH_LONG).show();
        finish();
    }
}
