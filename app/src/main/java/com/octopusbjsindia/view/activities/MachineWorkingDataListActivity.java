package com.octopusbjsindia.view.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.Operator.MachineWorklogDetailModel;
import com.octopusbjsindia.models.Operator.MachineWorklogResponseModel;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.home.RoleAccessList;
import com.octopusbjsindia.models.home.RoleAccessObject;
import com.octopusbjsindia.presenter.MachineWorkingDataListPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.MachineWorkDetaillogAdapter;
import com.octopusbjsindia.view.adapters.MachineWorklogRecyclerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.octopusbjsindia.utility.Constants.DAY_MONTH_YEAR;

public class MachineWorkingDataListActivity extends BaseActivity implements MachineWorklogRecyclerAdapter.OnRequestItemClicked, View.OnClickListener {
    private final String GET_APP_CONFIG = "getappconfig";
    private final String GET_WORKLOG_DETAILS = "getworklogdetails";

    public EditText tv_startdate, tv_enddate;
    public static boolean isReadingEditAccess =false;
    public Button btn_apply;
    MachineWorkingDataListPresenter machineWorkingDataListPresenter;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String strRequestObject;
    private String machineId = "5de229c1ca632728f60f19aa";
    private String machineCode ="";
    private long startDate = 1572692975000L, endDate = 1575284975000L;
    private RecyclerView rv_machinedataworklog,rv_machinedetailsworklog;
    private MachineWorklogRecyclerAdapter machineWorklogRecyclerAdapter;
    private MachineWorkDetaillogAdapter machineWorkDetaillogAdapter;
    private MachineWorklogResponseModel pendingRequestsResponse;
    private MachineWorklogDetailModel machineWorklogDetailModel;
    private ImageView toolbar_back_action;
    private ImageView toolbar_edit_action;
    private TextView toolbar_title,tv_no_data_msg,tv_complete_total_hours;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private LinearLayout layout_machine_worklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_worklog);
        layout_machine_worklist = findViewById(R.id.layout_machine_worklist);
        progressBarLayout = findViewById(R.id.profile_act_progress_bar);
        progressBar = findViewById(R.id.pb_profile_act);
        rv_machinedataworklog = findViewById(R.id.rv_machinedataworklog);
        rv_machinedetailsworklog = findViewById(R.id.rv_machinedetailsworklog);
        toolbar_back_action = findViewById(R.id.toolbar_back_action);
        toolbar_edit_action = findViewById(R.id.toolbar_action);
        tv_no_data_msg  = findViewById(R.id.tv_no_data_msg);
        tv_complete_total_hours  = findViewById(R.id.tv_complete_total_hours);
        toolbar_title = findViewById(R.id.toolbar_title);
        btn_apply = findViewById(R.id.btn_apply);
        tv_startdate = findViewById(R.id.tv_startdate);
        tv_enddate = findViewById(R.id.tv_enddate);

        tv_startdate.setOnClickListener(this);
        tv_enddate.setOnClickListener(this);
        btn_apply.setOnClickListener(this);
        tv_startdate.setText(Util.getCurrentDate());
        tv_enddate.setText(Util.getCurrentDate());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        toolbar_back_action.setOnClickListener(this);
        toolbar_edit_action.setOnClickListener(this);
        toolbar_edit_action.setVisibility(View.GONE);
        rv_machinedataworklog.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        rv_machinedetailsworklog.setLayoutManager(layoutManager2);
        //receive intent data
        Bundle data = getIntent().getExtras();
        toolbar_title.setText("Machine Worklog");

        if (data != null && data.containsKey("machineId")) {
            machineId = data.getString("machineId") != null
                    ? data.getString("machineId") : "null";
            //toolbar_title.setText(machineId);
        }
        if (data != null && data.containsKey("machineName")) {
            machineCode = data.getString("machineName") != null
                    ? data.getString("machineName") : "null";
            toolbar_title.setText(machineCode);
        }

// inside your activity (if you did not enable transitions in your theme)
        Gson gson = new GsonBuilder().create();
        String paramjson = gson.toJson(getCheckProfileJson(machineId, startDate, endDate));
        machineWorkingDataListPresenter = new MachineWorkingDataListPresenter(MachineWorkingDataListActivity.this);
        machineWorkingDataListPresenter.getMachineWorkData(paramjson);

        //rOLE aCCESS
        RoleAccessAPIResponse roleAccessAPIResponse = Util.getRoleAccessObjectFromPref();
        RoleAccessList roleAccessList = roleAccessAPIResponse.getData();
        if (roleAccessList != null) {
            List<RoleAccessObject> roleAccessObjectList = roleAccessList.getRoleAccess();
            for (RoleAccessObject roleAccessObject : roleAccessObjectList) {
                if (roleAccessObject.getActionCode().equals(Constants.SSModule.ACCESS_CODE_MACHINE_EDIT_READING)) {
                    isReadingEditAccess = true;
                    continue;
                }
            }
        }
    }

    public JsonObject getCheckProfileJson(String machineId, long startDate, long endDate) {

        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("machineId", machineId);
        requestObject.addProperty("startDate", Util.getDateInepoch(tv_startdate.getText().toString()));
        requestObject.addProperty("endDate", Util.getDateInepoch(tv_enddate.getText().toString()));

        return requestObject;
    }
    public JsonObject getWorkDetailReqJson(String machineId, String workDate, long endDate) {

        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("machineId", machineId);
        requestObject.addProperty("workDate",workDate);

        return requestObject;
    }

    public JsonObject getEditReqJson(String start_id,String editedReading,int flagStartEndReading){
        JsonObject requestObject = new JsonObject();
        if (flagStartEndReading==1) {

            requestObject.addProperty("start_id", start_id);
            requestObject.addProperty("start_meter_reading", editedReading);

        }
        if (flagStartEndReading==2) {

            requestObject.addProperty("end_id", start_id);
            requestObject.addProperty("end_meter_reading", editedReading);

        }

        return requestObject;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void finishwithResult() {

    }

    public void ShowEditedMeterReading(String requestID, String response, int status) {

    }

    public void ShowReceivedWorkList(String requestID, String response, int status) {
        Log.d("machineWorklog", requestID + " response Json : " + response);

        if (GET_WORKLOG_DETAILS.equalsIgnoreCase(requestID)){
            if (!TextUtils.isEmpty(response)) {
                Gson gson = new Gson();
                CommonResponse commonResponse = gson.fromJson(response, CommonResponse.class);
                if (commonResponse.getStatus() != 200) {
                    tv_no_data_msg.setVisibility(View.VISIBLE);
                    tv_complete_total_hours.setVisibility(View.GONE);}else {
                    Log.d("machineLogDetails", requestID + " response Json : " + response);
                    machineWorklogDetailModel = new Gson().fromJson(response, MachineWorklogDetailModel.class);
                    if (machineWorklogDetailModel != null && machineWorklogDetailModel.getMachineWorklogDetails() != null
                            && !machineWorklogDetailModel.getMachineWorklogDetails().isEmpty()
                            && machineWorklogDetailModel.getMachineWorklogDetails().size() > 0) {
                        rv_machinedetailsworklog.setVisibility(View.VISIBLE);
                        toolbar_edit_action.setVisibility(View.GONE);
                        toolbar_edit_action.setImageResource(R.drawable.ic_dialog_close_dark);

                        toolbar_back_action.setVisibility(View.VISIBLE);
                        machineWorkDetaillogAdapter = new MachineWorkDetaillogAdapter(this,machineWorklogDetailModel.getMachineWorklogDetails());
                        rv_machinedetailsworklog.setAdapter(machineWorkDetaillogAdapter);
                        layout_machine_worklist.setVisibility(View.GONE);
                        tv_complete_total_hours.setVisibility(View.GONE);
                        rv_machinedataworklog.setVisibility(View.GONE);
                    }
                }
            }
        }else {
            if (!TextUtils.isEmpty(response)) {
                Gson gson = new Gson();
                CommonResponse commonResponse = gson.fromJson(response, CommonResponse.class);
                if (commonResponse.getStatus() != 200) {
                    tv_no_data_msg.setVisibility(View.VISIBLE);
                    tv_complete_total_hours.setVisibility(View.GONE);
                } else {
                    layout_machine_worklist.setVisibility(View.VISIBLE);
                    tv_complete_total_hours.setVisibility(View.VISIBLE);
                    tv_no_data_msg.setVisibility(View.GONE);
                    rv_machinedataworklog.setVisibility(View.VISIBLE);
                    tv_complete_total_hours.setVisibility(View.VISIBLE);
                    pendingRequestsResponse
                            = new Gson().fromJson(response, MachineWorklogResponseModel.class);
                    if (pendingRequestsResponse != null && pendingRequestsResponse.getMachineWorklogList() != null
                            && !pendingRequestsResponse.getMachineWorklogList().isEmpty()
                            && pendingRequestsResponse.getMachineWorklogList().size() > 0) {
                        //fragmentWeakReference.get().showFetchedUserProfileForApproval(pendingRequestsResponse.getData().getApplication());
                        machineWorklogRecyclerAdapter = new MachineWorklogRecyclerAdapter(this,this, pendingRequestsResponse.getMachineWorklogList(),
                                this);
                        rv_machinedataworklog.setAdapter(machineWorklogRecyclerAdapter);
                        tv_complete_total_hours.setText("Total hours = " + pendingRequestsResponse.getTotalWorkHrs());
                    } else {
                        rv_machinedataworklog.setVisibility(View.GONE);
                        tv_complete_total_hours.setVisibility(View.GONE);
                        tv_no_data_msg.setVisibility(View.VISIBLE);
                    }
                }
            }

        }
        /*machineWorklogRecyclerAdapter = new MachineWorklogRecyclerAdapter(this, data,
                this, this);
        rv_machinedataworklog.setAdapter(machineWorklogRecyclerAdapter);*/
    }

    @Override
    public void onItemClicked(int pos) {
        if (Util.isConnected(this)) {
            String paramjson = new Gson().toJson(getWorkDetailReqJson(pendingRequestsResponse.getMachineWorklogList().get(pos).getMachineId(), pendingRequestsResponse.getMachineWorklogList().get(pos).getWorkDate(), endDate));
            machineWorkingDataListPresenter.getMachineWorklogDetails(paramjson);
        }else {
            Util.snackBarToShowMsg(getWindow().getDecorView()
                            .findViewById(android.R.id.content), "No internet connection.",
                    Snackbar.LENGTH_LONG);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_action:

                if (tv_complete_total_hours.getVisibility()==View.VISIBLE){
                    finish();
                }else {
                    layout_machine_worklist.setVisibility(View.VISIBLE);
                    tv_complete_total_hours.setVisibility(View.VISIBLE);
                    rv_machinedataworklog.setVisibility(View.VISIBLE);
                    rv_machinedetailsworklog.setVisibility(View.GONE);
                    toolbar_edit_action.setVisibility(View.GONE);
                    toolbar_back_action.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.toolbar_action:
                //finish();
                layout_machine_worklist.setVisibility(View.VISIBLE);
                tv_complete_total_hours.setVisibility(View.VISIBLE);
                rv_machinedataworklog.setVisibility(View.VISIBLE);
                rv_machinedetailsworklog.setVisibility(View.GONE);
                toolbar_edit_action.setVisibility(View.GONE);
                toolbar_back_action.setVisibility(View.VISIBLE);
                break;

            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_startdate:
                selectStartDate(tv_startdate, 1);
                break;
            case R.id.tv_enddate:
                selectStartDate(tv_enddate, 2);
                break;
            case R.id.btn_apply:
                if (Util.isConnected(this)) {
                    if (!TextUtils.isEmpty(tv_startdate.getText()) && !TextUtils.isEmpty(tv_enddate.getText())) {
                        Gson gson = new GsonBuilder().create();
                        String paramjson = gson.toJson(getCheckProfileJson(machineId, startDate, endDate));
                        //machineWorkingDataListPresenter = new MachineWorkingDataListPresenter(MachineWorkingDataListActivity.this);
                        machineWorkingDataListPresenter.getMachineWorkData(paramjson);
                    } else {
                        Toast.makeText(MachineWorkingDataListActivity.this, "Please select date range.", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Util.snackBarToShowMsg(getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "No internet connection.",
                            Snackbar.LENGTH_LONG);
                }
                break;

        }
    }

    private void selectStartDate(TextView textview, int flag) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        String selectedDateString = new SimpleDateFormat(DAY_MONTH_YEAR).format(calendar.getTime());
                        if (flag == 2) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat formatter = new SimpleDateFormat(DAY_MONTH_YEAR);//new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                            Date startDate = null;
                            Date endDate = null;
                            Date currentDate = null;
                            try {
                                startDate = formatter.parse(tv_startdate.getText().toString());
                                endDate = formatter.parse(selectedDateString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (startDate.getTime() > endDate.getTime()) {
                                String msg = getResources().getString(R.string.msg_enter_proper_date);
                                Toast.makeText(MachineWorkingDataListActivity.this, msg, Toast.LENGTH_LONG).show();
                            }else {
                                textview.setText(selectedDateString);
                            }

                        }else {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat formatter = new SimpleDateFormat(DAY_MONTH_YEAR);//new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                            Date startDate = null;
                            Date endDate = null;
                            Date currentDate = null;
                            try {
                                startDate = formatter.parse(selectedDateString);
                                endDate = formatter.parse(tv_enddate.getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (startDate.getTime() > endDate.getTime()) {
                                String msg = getResources().getString(R.string.msg_enter_proper_date);
                                Toast.makeText(MachineWorkingDataListActivity.this, msg, Toast.LENGTH_LONG).show();
                            }else {
                                textview.setText(selectedDateString);
                            }
                            //---
                            //textview.setText(selectedDateString);
                            //tv_enddate.setText("");
                        }
                        //textview.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (tv_complete_total_hours.getVisibility()==View.VISIBLE){
            finish();
        }else {
            layout_machine_worklist.setVisibility(View.VISIBLE);
            tv_complete_total_hours.setVisibility(View.VISIBLE);
            rv_machinedataworklog.setVisibility(View.VISIBLE);
            rv_machinedetailsworklog.setVisibility(View.GONE);
            toolbar_edit_action.setVisibility(View.GONE);
            toolbar_back_action.setVisibility(View.VISIBLE);
        }
    }

    public void showProgressBar() {
        runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideProgressBar() {
        runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    public void onReceiveEditedReading(String updatedValue, int pos, int flagStartEndReading) {
        if (flagStartEndReading==1) {
            if (pendingRequestsResponse.getMachineWorklogList().get(pos).getEndReading() != null && !pendingRequestsResponse.getMachineWorklogList().get(pos).getEndReading().equalsIgnoreCase("")) {
                /*if (Float.parseFloat(pendingRequestsResponse.getMachineWorklogList().get(pos).getEndReading()) - Float.parseFloat(updatedValue) <= 12
                        && Float.parseFloat(pendingRequestsResponse.getMachineWorklogList().get(pos).getEndReading()) - Float.parseFloat(updatedValue) >= 0)*/
                if (Float.parseFloat(pendingRequestsResponse.getMachineWorklogList().get(pos).getEndReading()) - Float.parseFloat(updatedValue) > 0)
                {
                    pendingRequestsResponse.getMachineWorklogList().get(pos).setStartReading(updatedValue);

                    if (Util.isConnected(this)) {
                        String paramjson = new Gson().toJson(getEditReqJson(pendingRequestsResponse.getMachineWorklogList().get(pos).getStart_id(), updatedValue, flagStartEndReading));
                        machineWorkingDataListPresenter.editMachineWorklog(paramjson);
                    } else {
                        Util.snackBarToShowMsg(getWindow().getDecorView()
                                        .findViewById(android.R.id.content), "No internet connection.",
                                Snackbar.LENGTH_LONG);
                    }
                } else {
                    if (Float.parseFloat(pendingRequestsResponse.getMachineWorklogList().get(pos).getEndReading()) < Float.parseFloat(updatedValue)) {
                        Util.snackBarToShowMsg(getWindow().getDecorView()
                                        .findViewById(android.R.id.content), "End meter reading should be greater than start meter reading",
                                Snackbar.LENGTH_LONG);
                    } else {
                        Util.snackBarToShowMsg(getWindow().getDecorView()
                                        .findViewById(android.R.id.content), "Reading difference should be more than 0.",
                                Snackbar.LENGTH_LONG);
                    }
                }
            } else {
                pendingRequestsResponse.getMachineWorklogList().get(pos).setStartReading(updatedValue);

                if (Util.isConnected(this)) {
                    String paramjson = new Gson().toJson(getEditReqJson(pendingRequestsResponse.getMachineWorklogList().get(pos).getStart_id(), updatedValue, flagStartEndReading));
                    machineWorkingDataListPresenter.editMachineWorklog(paramjson);
                } else {
                    Util.snackBarToShowMsg(getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "No internet connection.",
                            Snackbar.LENGTH_LONG);
                }
            }


        } else if (flagStartEndReading == 2) {
            /*if (Float.parseFloat(updatedValue) - Float.parseFloat(pendingRequestsResponse.getMachineWorklogList().get(pos).getStartReading()) <= 12
                    && Float.parseFloat(updatedValue) - Float.parseFloat(pendingRequestsResponse.getMachineWorklogList().get(pos).getStartReading()) >= 0)*/
            if (Float.parseFloat(updatedValue) - Float.parseFloat(pendingRequestsResponse.getMachineWorklogList().get(pos).getStartReading()) > 0)
            {
                pendingRequestsResponse.getMachineWorklogList().get(pos).setEndReading(updatedValue);

                if (Util.isConnected(this)) {
                    String paramjson = new Gson().toJson(getEditReqJson(pendingRequestsResponse.getMachineWorklogList().get(pos).getEnd_id(), updatedValue, flagStartEndReading));
                    machineWorkingDataListPresenter.editMachineWorklog(paramjson);
                } else {
                    Util.snackBarToShowMsg(getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "No internet connection.",
                            Snackbar.LENGTH_LONG);
                }
            }else {
                if (Float.parseFloat(updatedValue) < Float.parseFloat(pendingRequestsResponse.getMachineWorklogList().get(pos).getStartReading())) {
                    Util.snackBarToShowMsg(getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Start meter reading should be less than end meter reading.",
                            Snackbar.LENGTH_LONG);
                } else {
                    Util.snackBarToShowMsg(getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Reading difference should be more than 0 hours.",
                            Snackbar.LENGTH_LONG);
                }
            }

        }
        machineWorklogRecyclerAdapter.notifyDataSetChanged();

    }
}
