package com.platform.view.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.platform.R;
import com.platform.models.Operator.MachineWorklogResponseModel;
import com.platform.models.events.CommonResponse;
import com.platform.presenter.MachineWorkingDataListPresenter;
import com.platform.utility.Util;
import com.platform.view.adapters.MachineWorklogRecyclerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.platform.utility.Constants.DAY_MONTH_YEAR;

public class MachineWorkingDataListActivity extends BaseActivity implements MachineWorklogRecyclerAdapter.OnRequestItemClicked, View.OnClickListener {
    public EditText tv_startdate, tv_enddate;
    public Button btn_apply;
    MachineWorkingDataListPresenter machineWorkingDataListPresenter;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String strRequestObject;
    private String machineId = "5de229c1ca632728f60f19aa";
    private long startDate = 1572692975000L, endDate = 1575284975000L;
    private RecyclerView rv_machinedataworklog;
    private MachineWorklogRecyclerAdapter machineWorklogRecyclerAdapter;

    private ImageView toolbar_back_action;
    private TextView toolbar_title,tv_no_data_msg,tv_complete_total_hours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_worklog);
        rv_machinedataworklog = findViewById(R.id.rv_machinedataworklog);
        toolbar_back_action = findViewById(R.id.toolbar_back_action);
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
        rv_machinedataworklog.setLayoutManager(layoutManager);
        //receive intent data
        Bundle data = getIntent().getExtras();
        toolbar_title.setText("Machine Worklog");

        if (data != null && data.containsKey("machineId")) {
            machineId = data.getString("machineId") != null
                    ? data.getString("machineId") : "null";
            //toolbar_title.setText(machineId);
        }
        if (data != null && data.containsKey("machineName")) {
            machineId = data.getString("machineName") != null
                    ? data.getString("machineName") : "null";
            toolbar_title.setText(machineId);
        }

// inside your activity (if you did not enable transitions in your theme)
        Gson gson = new GsonBuilder().create();
        String paramjson = gson.toJson(getCheckProfileJson(machineId, startDate, endDate));
        machineWorkingDataListPresenter = new MachineWorkingDataListPresenter(MachineWorkingDataListActivity.this);
        machineWorkingDataListPresenter.getMachineWorkData(paramjson);
    }

    public JsonObject getCheckProfileJson(String machineId, long startDate, long endDate) {

        JsonObject requestObject = new JsonObject();
        requestObject.addProperty("machineId", machineId);
        requestObject.addProperty("startDate", Util.getDateInepoch(tv_startdate.getText().toString()));
        requestObject.addProperty("endDate", Util.getDateInepoch(tv_enddate.getText().toString()));

        return requestObject;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void finishwithResult() {

    }

    public void ShowReceivedWorkList(String requestID, String response, int status) {
        Log.d("machineWorklog", requestID + " response Json : " + response);

        if (!TextUtils.isEmpty(response)) {
            Gson gson = new Gson();
            CommonResponse commonResponse = gson.fromJson(response, CommonResponse.class);
            if (commonResponse.getStatus()!=200){
                tv_no_data_msg.setVisibility(View.VISIBLE);
                tv_complete_total_hours.setVisibility(View.GONE);
            }else {
                tv_no_data_msg.setVisibility(View.GONE);
                rv_machinedataworklog.setVisibility(View.VISIBLE);
                tv_complete_total_hours.setVisibility(View.VISIBLE);
                MachineWorklogResponseModel pendingRequestsResponse
                        = new Gson().fromJson(response, MachineWorklogResponseModel.class);
                if (pendingRequestsResponse != null && pendingRequestsResponse.getMachineWorklogList() != null
                        && !pendingRequestsResponse.getMachineWorklogList().isEmpty()
                        && pendingRequestsResponse.getMachineWorklogList().size() > 0) {
                    //fragmentWeakReference.get().showFetchedUserProfileForApproval(pendingRequestsResponse.getData().getApplication());
                    machineWorklogRecyclerAdapter = new MachineWorklogRecyclerAdapter(this, pendingRequestsResponse.getMachineWorklogList(),
                            this);
                    rv_machinedataworklog.setAdapter(machineWorklogRecyclerAdapter);
                    tv_complete_total_hours.setText("Total hours = "+pendingRequestsResponse.getTotalWorkHrs());
                }else {
                    rv_machinedataworklog.setVisibility(View.GONE);
                    tv_complete_total_hours.setVisibility(View.GONE);
                    tv_no_data_msg.setVisibility(View.VISIBLE);
                }
            }
        }


        /*machineWorklogRecyclerAdapter = new MachineWorklogRecyclerAdapter(this, data,
                this, this);
        rv_machinedataworklog.setAdapter(machineWorklogRecyclerAdapter);*/
    }

    @Override
    public void onItemClicked(int pos) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back_action:
                finish();
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
                if (!TextUtils.isEmpty(tv_startdate.getText())&&!TextUtils.isEmpty(tv_enddate.getText())) {
                    Gson gson = new GsonBuilder().create();
                    String paramjson = gson.toJson(getCheckProfileJson(machineId, startDate, endDate));
                    //machineWorkingDataListPresenter = new MachineWorkingDataListPresenter(MachineWorkingDataListActivity.this);
                    machineWorkingDataListPresenter.getMachineWorkData(paramjson);
                }else {
                    Toast.makeText(MachineWorkingDataListActivity.this, "Please select date range.", Toast.LENGTH_LONG).show();
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
                            textview.setText(selectedDateString);
                            tv_enddate.setText("");
                        }
                        //textview.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
}
