package com.octopusbjsindia.view.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.CustomSpinnerListener;
import com.octopusbjsindia.models.SujalamSuphalam.MasterDataValue;
import com.octopusbjsindia.models.common.CustomSpinnerObject;
import com.octopusbjsindia.models.profile.JurisdictionLocation;
import com.octopusbjsindia.models.smartgirl.SmartGirlCategoryResponseModel;
import com.octopusbjsindia.presenter.CreateTrainerWorkshopPresenter;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.customs.CustomSpinnerDialogClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.octopusbjsindia.utility.Constants.DAY_MONTH_YEAR;

public class CreateTrainerWorkshop extends AppCompatActivity implements View.OnClickListener, CustomSpinnerListener {
    //--Constant
    final String GET_CATEGORY = "getCategory";
    //------
    public EditText tv_startdate, tv_enddate;
    public EditText et_select_program,et_workshop_category,et_select_state, et_select_district, et_select_city, et_select_venue, et_traner_name, et_traner_additional,et_total_beneficiaries;
    public CreateTrainerWorkshopPresenter presenter;
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
        setContentView(R.layout.activity_create_trainer_workshop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        presenter = new CreateTrainerWorkshopPresenter(this);
        //setMasterData();
        //---
        progressBar = findViewById(R.id.ly_progress_bar);
        //-------
        tv_startdate = findViewById(R.id.tv_startdate);
        tv_enddate = findViewById(R.id.tv_enddate);
        tv_startdate.setOnClickListener(this);
        tv_enddate.setOnClickListener(this);
        tv_startdate.setText(Util.getCurrentDate());
        tv_enddate.setText(Util.getCurrentDate());
        et_total_beneficiaries = findViewById(R.id.et_total_beneficiaries);
        et_select_program  = findViewById(R.id.et_select_program);

        et_workshop_category = findViewById(R.id.et_workshop_category);
        et_select_state = findViewById(R.id.et_select_state);
        et_select_district = findViewById(R.id.et_select_district);
        et_select_city = findViewById(R.id.et_select_city);
        et_select_venue = findViewById(R.id.et_select_venue);
        et_traner_name = findViewById(R.id.et_traner_name);
        et_traner_additional = findViewById(R.id.et_traner_additional);

        et_select_state.setOnClickListener(this);
        et_select_district.setOnClickListener(this);
        et_workshop_category.setOnClickListener(this);

        // calling states first-

        presenter.getLocationData("",
                Util.getUserObjectFromPref().getJurisdictionTypeId(),
                Constants.JurisdictionLevelName.STATE_LEVEL);
        presenter.getBatchCategory();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_startdate:
                selectStartDate(tv_startdate, 1);
                break;
            case R.id.tv_enddate:
                selectStartDate(tv_enddate, 2);
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


        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateTrainerWorkshop.this,
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
                                Toast.makeText(CreateTrainerWorkshop.this, "Start date should be less than end date.", Toast.LENGTH_LONG).show();
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
                                Toast.makeText(CreateTrainerWorkshop.this, "End date should be greater than start date.", Toast.LENGTH_LONG).show();
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

    public void showReceivedCategories(SmartGirlCategoryResponseModel jurisdictionLevelResponse)
    {
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
            case "Select District":
                for (CustomSpinnerObject obj : districtList) {
                    if (obj.isSelected()) {
                        selectedDistrict = obj.getName();
                        selectedDistrictId = obj.get_id();
                        break;
                    }
                }
                et_select_district.setText(selectedDistrict);
                /*etDistrict.setText(selectedDistrict);
                etTaluka.setText("");
                selectedTaluka = "";
                selectedTalukaId = "";
                etHostVillage.setText("");
                selectedHostVillage = "";
                selectedHostVillageId = "";*/
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

    public void createBatch(){
        String paramjson = new Gson().toJson(getCreateBatchReqJson());
        //presenter.createBatch(paramjson);
        presenter.createBatch(paramjson);
    }

    public JsonObject getCreateBatchReqJson() {


        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("machineId", "");
        requestObject.addProperty("workDate","");

        return requestObject;
    }
}
