package com.platform.view.fragments;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.platform.R;
import com.platform.dao.UserAttendanceDao;
import com.platform.dao.UserCheckOutDao;
import com.platform.database.DatabaseManager;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.attendance.AttendaceCheckOut;
import com.platform.models.attendance.AttendaceData;
import com.platform.models.attendance.CheckIn;
import com.platform.models.home.Modules;
import com.platform.models.leaves.LeaveDetail;
import com.platform.models.planner.SubmoduleData;
import com.platform.models.planner.attendanceData;
import com.platform.presenter.PlannerFragmentPresenter;

import com.platform.presenter.SubmitAttendanceFragmentPresenter;
import com.platform.services.ShowTimerService;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.utility.GPSTracker;
import com.platform.utility.PreferenceHelper;
import com.platform.utility.Util;
import com.platform.view.activities.CreateEventTaskActivity;
import com.platform.view.activities.GeneralActionsActivity;
import com.platform.view.activities.HomeActivity;
import com.platform.view.activities.PlannerDetailActivity;
import com.platform.view.adapters.EventTaskListAdapter;
import com.platform.view.adapters.LeaveBalanceAdapter;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class PlannerFragment extends Fragment implements PlatformTaskListener {

    private View plannerView;
    TextView tvAttendanceDetails;
    Button btCheckIn;
    Button btCheckout;
    AttendaceData attendaceCheckinData, attendaceCheckoutData;

    private RelativeLayout lyEvents;
    private RelativeLayout lyTasks;
    private RelativeLayout lyAttendance;
    private RelativeLayout lyLeaves;

    private PlannerFragmentPresenter plannerFragmentPresenter;
    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;
    private GPSTracker gpsTracker;
    private Long timeStamp = null;
    private UserAttendanceDao userAttendanceDao;
    private String CHECK_IN = "checkin";
    private String CHECK_OUT = "checkout";

    String availableOnServer, totalHours, totalHrs = "", totalMin = "";
    private Context context;
    List<AttendaceData> getUserType;
    private PreferenceHelper preferenceHelper;
    private TextView txt_total_hours;
    BroadcastReceiver getHoursDifferenceReceiver;
    private boolean isCheckOut = false;

    private static String KEY_TOTALHOURS = "totalHours";
    private static String KEY_CHECKINTIME = "checkInTime";

    public static String KEY_CHECKINTEXT = "checkInText";
    public static String KEY_CHECKOUTTEXT = "checkOutText";

    public static String KEY_TOTAL_HRS_CHEKOUT = "totalHoursCheckOut";
    public static String KEY_ISCHECKOUT = "isCheckOut";
    private String checkInText ;
    private String checkOutText ;
    private String prefCheckInTime, userServerCheckInTime, userServerCheckOutTime;
    Timer timer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
            ((HomeActivity) getActivity()).setSyncButtonVisibility(false);

            if ((boolean) getArguments().getSerializable("SHOW_BACK")) {
                ((HomeActivity) getActivity()).showBackArrow();
            }
        }
        timer = new Timer();
        AppEvents.trackAppEvent(getString(R.string.event_meetings_screen_visit));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        plannerView = inflater.inflate(R.layout.fragment_dashboard_planner, container, false);

        return plannerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        preferenceHelper = new PreferenceHelper(getActivity());
        userAttendanceDao = DatabaseManager.getDBInstance(getActivity()).getAttendaceSchema();

        getHoursDifferenceReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                String totlaHours = "";
                if (bundle != null) {
                    totlaHours = bundle.getString("HoursDiff");
                }
                Toast.makeText(context, "Received Hours" + totlaHours, Toast.LENGTH_LONG).show();
            }
        };

        btCheckIn = plannerView.findViewById(R.id.bt_check_in);
        btCheckout = plannerView.findViewById(R.id.bt_checkout);

        tvAttendanceDetails = plannerView.findViewById(R.id.tv_attendance_details);
        txt_total_hours = plannerView.findViewById(R.id.iv_total_hours);

        attendaceCheckinData = new AttendaceData();
        attendaceCheckoutData = new AttendaceData();
        if(Util.isConnected(getContext())) {
            ArrayList<AttendaceData> unsyncAttendanceList = (ArrayList<AttendaceData>) userAttendanceDao.
                    getUnsyncAttendance(false);
            if (unsyncAttendanceList.size() > 0) {
                Util.showSimpleProgressDialog(getActivity(), "Attendance", "Loading...", false);
                for(AttendaceData attendaceData: unsyncAttendanceList){
                    if(attendaceData.getAttendanceType().equals(CHECK_OUT)){

                        AttendaceData attendaceCheckinData = userAttendanceDao.getUserAttendace(attendaceData.getAttendaceDate(), CHECK_IN);
                        attendaceData.setAttendanceId(attendaceCheckinData.getAttendanceId());

                        SubmitAttendanceFragmentPresenter submitAttendanceFragmentPresenter = new SubmitAttendanceFragmentPresenter(this);
                        submitAttendanceFragmentPresenter.markAttendace(attendaceData);

                    } else {
                        SubmitAttendanceFragmentPresenter submitAttendanceFragmentPresenter = new SubmitAttendanceFragmentPresenter(this);
                        submitAttendanceFragmentPresenter.markAttendace(attendaceData);
                    }
                }
                Util.removeSimpleProgressDialog();
            }
        }
        initCardView();
    }

    private void initCardView() {
        if (getActivity() == null) {
            return;
        }

        progressBarLayout = plannerView.findViewById(R.id.profile_act_progress_bar);
        progressBar = plannerView.findViewById(R.id.pb_profile_act);

        checkTodayMarkInOrNot();

        plannerFragmentPresenter = new PlannerFragmentPresenter(this);

        lyEvents = plannerView.findViewById(R.id.ly_events);
        lyTasks = plannerView.findViewById(R.id.ly_task);
        lyAttendance = plannerView.findViewById(R.id.ly_attendance);
        lyLeaves = plannerView.findViewById(R.id.ly_leave);

        List<Modules> approveModules = DatabaseManager.getDBInstance(getActivity().getApplicationContext())
                .getModulesOfStatus(Constants.RequestStatus.APPROVED_MODULE);

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.Planner.KEY_IS_DASHBOARD, true);

        //to display tha submodules
        for (Modules m : approveModules) {
            switch (m.getName().getDefaultValue()) {
                case Constants.Planner.ATTENDANCE_KEY_:
                    lyAttendance.setVisibility(View.VISIBLE);
                    setAttendaceView();
                    break;
                case Constants.Planner.EVENTS_KEY_:
                    lyEvents.setVisibility(View.VISIBLE);
                    setEventView();
                    break;
                case Constants.Planner.TASKS_KEY_:
                    lyTasks.setVisibility(View.VISIBLE);
                    setTaskView();
                    break;
                case Constants.Planner.LEAVES_KEY_:
                    lyLeaves.setVisibility(View.VISIBLE);
                    setLeaveView();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Util.isConnected(getContext())) {
            plannerFragmentPresenter = new PlannerFragmentPresenter(this);
            plannerFragmentPresenter.getPlannerData();
        } else {
            Util.showToast(getString(R.string.msg_no_network), this);
            setOfflineAttendance();
//            // offline total hours calculation
//            getUserType = userAttendanceDao.getUserAttendanceType(CHECK_IN, Util.getTodaysDate(), Util.getUserMobileFromPref());
//            if (getUserType != null && getUserType.size() > 0 && !getUserType.isEmpty()) {
//
//                btCheckIn.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
//                btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));
//                clearCheckInButtonText();
//                btCheckIn.setText("Check in at " + getUserType.get(0).getTime());
////                db_chkin_time = getUserType.get(0).getTime();
////                time = getUserType.get(0).getTime();
//                enableCheckOut();
//            }
//
//            getCheckOut = userCheckOutDao.getCheckOutData(CHECK_OUT, Util.getTodaysDate(), Util.getUserMobileFromPref());
//            if (getCheckOut != null && getCheckOut.size() > 0 && !getCheckOut.isEmpty()) {
//                btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
//                btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
//                clearCheckOutButtonText();
//                btCheckout.setText("Check out at " + getCheckOut.get(0).getTime());
//                db_chkout_time = getCheckOut.get(0).getTime();
//                String totalHrs = getCheckOut.get(0).getTotalHrs();
//                txt_total_hours.setText(totalHrs);
//            }
//            String totalHours = calculateTotalHours(db_chkin_time, db_chkout_time);
//            txt_total_hours.setText(totalHours);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (plannerView != null) {
            plannerView = null;
        }
        super.onDestroy();
    }

    @Override
    public void showProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public <T> void showNextScreen(T data) {

    }

    @Override
    public void showErrorMessage(String result) {
        getActivity().runOnUiThread(() -> Util.showToast(result, this));
        setOfflineAttendance();
    }

    private void setOfflineAttendance(){
        String attendanceDate = Util.getDateFromTimestamp(new Date().getTime(),"yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
        long attnDate= Util.dateTimeToTimeStamp(attendanceDate,"00:00");
//        Date date = null;
//        try {
//            date = formatter.parse(attendanceDate);
//        } catch (ParseException e) {
//            Log.e("TAG", e.getMessage());
//        }
        AttendaceData offlineCheckinAttendaceData = userAttendanceDao.getUserAttendace(attnDate, CHECK_IN);
        AttendaceData offlineCheckoutAttendaceData = userAttendanceDao.getUserAttendace(attnDate, CHECK_OUT);

        if(offlineCheckinAttendaceData!= null && offlineCheckoutAttendaceData!= null){
            txt_total_hours.setText(calculateHoursFromTwoDate(offlineCheckinAttendaceData.getDate(),
                    offlineCheckoutAttendaceData.getDate()));
        } else if(offlineCheckinAttendaceData!= null){
            updateTime(offlineCheckinAttendaceData.getDate(), 0);
        }

        if(offlineCheckoutAttendaceData!=null && offlineCheckoutAttendaceData.getDate()!=null &&
                !offlineCheckoutAttendaceData.getDate().equals("")){
            checkInText = "Check in at " + Util.getDateFromTimestamp
                    (offlineCheckinAttendaceData.getDate(),"hh:mm aa");
            btCheckIn.setText(checkInText);
            checkOutText = "Check out at " + Util.getDateFromTimestamp
                    (offlineCheckoutAttendaceData.getDate(),"hh:mm aa");
            btCheckout.setText(checkOutText);
            enableButton(false, false);
        }else{
            if(offlineCheckinAttendaceData!=null && offlineCheckinAttendaceData.getDate()!= null &&
                    !offlineCheckinAttendaceData.getDate().equals("")){
                checkInText = "Check in at " + Util.getDateFromTimestamp
                        (offlineCheckinAttendaceData.getDate(),"hh:mm aa");
                btCheckIn.setText(checkInText);
                enableButton(false, true);
            } else{
                enableButton(true, false);
            }
        }
    }

    public void showPlannerSummary(ArrayList<SubmoduleData> submoduleList) {

        for (SubmoduleData obj : submoduleList) {
            switch (obj.getSubModule()) {

                case Constants.Planner.ATTENDANCE_KEY:
                    List<attendanceData> attendanceData = obj.getAttendanceData();
                    if (attendanceData.size() > 0) {
                        for (int i = 0; i < attendanceData.size(); i++) {

                            String attendanceDate = Util.getDateFromTimestamp(Long.parseLong(attendanceData.get(i).getCheckIn().getTime()),"yyyy-MM-dd");
//                            String attendanceDate = Util.getDateFromTimestamp(new Date().getTime(),"yyyy-MM-dd");
//                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
                            long attnDate= Util.dateTimeToTimeStamp(attendanceDate,"00:00");

//                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
//                            Date date = null;
//                            try {
//                                date = formatter.parse(attendanceDate);
//                            } catch (ParseException e) {
//                                Log.e("TAG", e.getMessage());
//                            }

                            AttendaceData attendaceCheckinTemp = userAttendanceDao.getUserAttendace(attnDate, CHECK_IN);
                            if(attendaceCheckinTemp!=null) {
                                userAttendanceDao.updateUserAttendace(attendanceData.get(i).get_id(), true, attnDate, CHECK_IN);
                            }else{
                                AttendaceData attendaceCheckinData = new AttendaceData();
                                attendaceCheckinData.setAttendanceId(attendanceData.get(i).get_id());
                                attendaceCheckinData.setLatitude(Double.parseDouble(attendanceData.get(i).getCheckIn().getLat()));
                                attendaceCheckinData.setLongitude(Double.parseDouble(attendanceData.get(i).getCheckIn().getLong()));
                                attendaceCheckinData.setAttendanceType(CHECK_IN);
                                attendaceCheckinData.setDate(Long.parseLong(attendanceData.get(i).getCheckIn().getTime()));
                                attendaceCheckinData.setAttendaceDate(attnDate);
                                userAttendanceDao.insert(attendaceCheckinData);
                            }
                            String attendanceCheckoutDate = Util.getDateFromTimestamp(Long.parseLong(attendanceData.get(i).getCheckOut().getTime()),"yyyy-MM-dd");
                            long attnCheckoutDate= Util.dateTimeToTimeStamp(attendanceCheckoutDate,"00:00");
//                            SimpleDateFormat checkOutformatter = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
//                            Date checkoutDate = null;
//                            try {
//                                checkoutDate = checkOutformatter.parse(attendanceCheckoutDate);
//                            } catch (ParseException e) {
//                                Log.e("TAG", e.getMessage());
//                            }
                            AttendaceData attendaceCheckoutTemp = userAttendanceDao.getUserAttendace
                                    (attnCheckoutDate, CHECK_OUT);

                            if(attendaceCheckoutTemp!=null) {
                                userAttendanceDao.updateUserAttendace(attendanceData.get(i).get_id(), true, attnCheckoutDate, CHECK_OUT);
                            }else{
                                AttendaceData attendaceCheckoutData = new AttendaceData();
                                attendaceCheckoutData.setAttendanceId(attendanceData.get(i).get_id());
                                attendaceCheckoutData.setLatitude(Double.parseDouble(attendanceData.get(i).getCheckOut().getLat()));
                                attendaceCheckoutData.setLongitude(Double.parseDouble(attendanceData.get(i).getCheckOut().getLong()));
                                attendaceCheckoutData.setAttendanceType(CHECK_OUT);
                                attendaceCheckoutData.setDate(Long.parseLong(attendanceData.get(i).getCheckOut().getTime()));
                                attendaceCheckoutData.setAttendaceDate(attnCheckoutDate);
                                userAttendanceDao.insert(attendaceCheckoutData);
                            }

                        }
                        setOfflineAttendance();
                    } else {
                        //enable check-in and disable check-out
                        setOfflineAttendance();
                    }

//                    getUserCheckInTimeFromServer(todayCheckInTime);
//                    getUserCheckOutTimeFromServer(todayCheckOutTime);

//                    updateButtonTextIfUserSaved();

                    break;
                case Constants.Planner.EVENTS_KEY:
                    if (obj.getEventData() != null && obj.getEventData().size() > 0) {
                        RecyclerView.LayoutManager mLayoutManagerEvent = new LinearLayoutManager(getActivity().getApplicationContext());
                        EventTaskListAdapter eventTaskListAdapter = new EventTaskListAdapter(getActivity(),
                                obj.getEventData(), Constants.Planner.EVENTS_LABEL);
                        RecyclerView rvEvents = plannerView.findViewById(R.id.rv_events);
                        rvEvents.setLayoutManager(mLayoutManagerEvent);
                        rvEvents.setAdapter(eventTaskListAdapter);

                        plannerView.findViewById(R.id.rv_events).setVisibility(View.VISIBLE);
                        plannerView.findViewById(R.id.cv_no_event).setVisibility(View.GONE);

                        if (obj.getEventData() != null && obj.getEventData().size() == 1) {
                            final float scale = getContext().getResources().getDisplayMetrics().density;
                            int px = (int) (165 * scale + 0.5f);
                            lyEvents.getLayoutParams().height = px;
                        } else {
                            final float scale = getContext().getResources().getDisplayMetrics().density;
                            int px = (int) (285 * scale + 0.5f);
                            lyEvents.getLayoutParams().height = px;
                        }

                    } else {
                        plannerView.findViewById(R.id.rv_events).setVisibility(View.GONE);
                        plannerView.findViewById(R.id.cv_no_event).setVisibility(View.VISIBLE);
                        final float scale = getContext().getResources().getDisplayMetrics().density;
                        int px = (int) (135 * scale + 0.5f);
                        lyEvents.getLayoutParams().height = px;
                    }
                    break;
                case Constants.Planner.TASKS_KEY:
                    if (obj.getTaskData() != null && obj.getTaskData().size() > 0) {

                        RecyclerView.LayoutManager mLayoutManagerTask = new LinearLayoutManager(getActivity().getApplicationContext());
                        EventTaskListAdapter taskListAdapter = new EventTaskListAdapter(getActivity(),
                                obj.getTaskData(), Constants.Planner.TASKS_LABEL);
                        RecyclerView rvTask = plannerView.findViewById(R.id.rv_task);
                        rvTask.setLayoutManager(mLayoutManagerTask);
                        rvTask.setAdapter(taskListAdapter);

                        plannerView.findViewById(R.id.cv_no_task).setVisibility(View.GONE);
                        plannerView.findViewById(R.id.rv_task).setVisibility(View.VISIBLE);

                        if (obj.getTaskData() != null && obj.getTaskData().size() == 1) {
                            final float scale = getContext().getResources().getDisplayMetrics().density;
                            int px = (int) (165 * scale + 0.5f);
                            lyTasks.getLayoutParams().height = px;
                        } else {
                            final float scale = getContext().getResources().getDisplayMetrics().density;
                            int px = (int) (285 * scale + 0.5f);
                            lyTasks.getLayoutParams().height = px;
                        }

                    } else {
                        plannerView.findViewById(R.id.cv_no_task).setVisibility(View.VISIBLE);
                        plannerView.findViewById(R.id.rv_task).setVisibility(View.GONE);
                        final float scale = getContext().getResources().getDisplayMetrics().density;
                        int px = (int) (135 * scale + 0.5f);
                        lyTasks.getLayoutParams().height = px;
                    }
                    break;
                case Constants.Planner.LEAVES_KEY:
                    if (obj.getLeave() != null) {
                        CircularProgressBar pbTotal = plannerView.findViewById(R.id.pb_total);
                        pbTotal.setProgress((obj.getLeave().getTotal() / obj.getLeave().getTotal()) * 100);
                        TextView tvTotal = plannerView.findViewById(R.id.tv_total);
                        tvTotal.setText(obj.getLeave().getTotal().toString());

                        CircularProgressBar pbUsed = plannerView.findViewById(R.id.pb_used);
                        pbUsed.setProgress((obj.getLeave().getUsed() / obj.getLeave().getTotal()) * 100);
                        TextView tvUsed = plannerView.findViewById(R.id.tv_used);
                        tvUsed.setText(obj.getLeave().getUsed().toString());

                        CircularProgressBar pbBalance = plannerView.findViewById(R.id.pb_balance);
                        pbBalance.setProgress((obj.getLeave().getBalance() / obj.getLeave().getTotal()) * 100);
                        TextView tvbalance = plannerView.findViewById(R.id.tv_balance);
                        tvbalance.setText(obj.getLeave().getBalance().toString());
                    }
                    break;
            }
        }
    }

    private String getUserCheckOutTimeFromServer(String todayCheckOutTime) {

        try {
            String convertedDate = Util.getLongDateInString(Long.valueOf(todayCheckOutTime), "yyyy/MM/dd hh:mm:ss aa");

            String splitDate[] = convertedDate.split(" ");
            String hh = splitDate[0];
            String mm = splitDate[1];
            String timeFormat = splitDate[2];

            String splitMin[] = mm.split(":");
            String hours = splitMin[0];
            String minutes = splitMin[1];

            userServerCheckOutTime = hours + ":" + minutes + " " + timeFormat;

        } catch (Exception e) {

        }
        return userServerCheckOutTime;

    }

    private String getUserCheckInTimeFromServer(String todayCheckInTime) {

        try {
            String convertedDate = Util.getLongDateInString(Long.valueOf(todayCheckInTime), "yyyy/MM/dd hh:mm:ss aa");

            Log.i("CheckInDate", "111" + convertedDate);
            String splitDate[] = convertedDate.split(" ");
            String hh = splitDate[0];
            String mm = splitDate[1];
            String timeFormat = splitDate[2];

            String splitMin[] = mm.split(":");
            String hours = splitMin[0];
            String minutes = splitMin[1];

            userServerCheckInTime = hours + ":" + minutes + " " + timeFormat;

        } catch (Exception e) {

        }
        return userServerCheckInTime;

    }

    public void setAttendaceView() {
        btCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //disable check-in and enable check-out
                markCheckIn();
            }
        });
        btCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //disable check-in and disable check-out
                markOut();
            }
        });

        tvAttendanceDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
                intent.putExtra(Constants.Planner.KEY_IS_DASHBOARD, false);
                intent.putExtra("title", getActivity().getString(R.string.attendance));
                intent.putExtra("switch_fragments", "AttendancePlannerFragment");
                intent.putExtra("TotalHours", txt_total_hours.getText().toString());
                intent.putExtra("userAvailable", availableOnServer);
                intent.putExtra("userCheckInTime", userServerCheckInTime);
                intent.putExtra("userCheckOutTime", userServerCheckOutTime);
                getActivity().startActivity(intent);
            }
        });
    }

    public void setEventView() {

        TextView tvAllEventsDetail = plannerView.findViewById(R.id.tv_all_events_list);
        TextView btAddEvents = plannerView.findViewById(R.id.bt_add_events);

        tvAllEventsDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isConnected(getContext())) {
                    Intent intentEventList = new Intent(getActivity(), PlannerDetailActivity.class);
                    intentEventList.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.EVENTS_LABEL);
                    getActivity().startActivity(intentEventList);
                } else {
                    Util.showToast(getString(R.string.msg_no_network), getContext());
                }
            }
        });
        btAddEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isConnected(getContext())) {
                    Intent intentCreateEvent = new Intent(getActivity(), CreateEventTaskActivity.class);
                    intentCreateEvent.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.EVENTS_LABEL);
                    getActivity().startActivity(intentCreateEvent);
                } else {
                    Util.showToast(getString(R.string.msg_no_network), getContext());
                }
            }
        });

    }

    public void setTaskView() {

        RecyclerView rvEvents = plannerView.findViewById(R.id.rv_task);
        TextView tvAllTaskDetail = plannerView.findViewById(R.id.tv_all_task_list);
        TextView btAddTasks = plannerView.findViewById(R.id.bt_add_task);

        tvAllTaskDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isConnected(getContext())) {
                    Intent intentEventList = new Intent(getActivity(), PlannerDetailActivity.class);
                    intentEventList.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.TASKS_LABEL);
                    getActivity().startActivity(intentEventList);
                } else {
                    Util.showToast(getString(R.string.msg_no_network), getContext());
                }
            }
        });
        btAddTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isConnected(getContext())) {
                    Intent intentCreateEvent = new Intent(getActivity(), CreateEventTaskActivity.class);
                    intentCreateEvent.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.TASKS_LABEL);
                    getActivity().startActivity(intentCreateEvent);
                } else {
                    Util.showToast(getString(R.string.msg_no_network), getContext());
                }
            }
        });
    }

    public void setLeaveView() {
        TextView tvCheckLeaveDetailsLink = plannerView.findViewById(R.id.tv_link_check_leaves);
        TextView imgClickAddLeaves = plannerView.findViewById(R.id.fab_add_leaves);
        if(Util.isConnected(getContext())){
            plannerView.findViewById(R.id.ly_lebal).setVisibility(View.VISIBLE);
            plannerView.findViewById(R.id.ly_circular_progress).setVisibility(View.VISIBLE);
        }
        tvCheckLeaveDetailsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isConnected(getContext())) {
                    Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
                    intent.putExtra("title", getActivity().getString(R.string.leave));
                    intent.putExtra("switch_fragments", "LeaveDetailsFragment");
                    getActivity().startActivity(intent);
                } else {
                    Util.showToast(getString(R.string.msg_no_network), getContext());
                }
            }
        });
        imgClickAddLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Util.isConnected(getContext())) {
                    Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
                    intent.putExtra("title", getActivity().getString(R.string.apply_leave));
                    intent.putExtra("isEdit", false);
                    intent.putExtra("apply_type", "Leave");
                    intent.putExtra("switch_fragments", "LeaveApplyFragment");
                    getActivity().startActivity(intent);
                } else{
                    Util.showToast(getString(R.string.msg_no_network), getContext());
                }
            }
        });
    }

    private void enableButton(boolean b1, boolean b2) {
        if(b1 && !b2){
            btCheckIn.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
            btCheckIn.setTextColor(getResources().getColor(R.color.white));
            btCheckIn.setEnabled(true);
            btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
            btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
            btCheckout.setEnabled(false);
        }
        if(!b1 && b2){
            btCheckIn.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
            btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));
            btCheckIn.setEnabled(false);
            btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
            btCheckout.setTextColor(getResources().getColor(R.color.white));
            btCheckout.setEnabled(true);
        }
        if(!b2 && !b1){
            btCheckIn.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
            btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));
            btCheckIn.setEnabled(false);
            btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
            btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
            btCheckout.setEnabled(false);
        }
    }

    public void markCheckIn() {
        gpsTracker = new GPSTracker(getActivity());
        if (gpsTracker.isGPSEnabled(getActivity(), this)) {
            Date currentDate = new Date();
            timeStamp = currentDate.getTime();
            Location location = gpsTracker.getLocation();
            if (location != null) {
                enableButton(false, true);
                attendaceCheckinData.setLatitude(location.getLatitude());
                attendaceCheckinData.setLongitude(location.getLongitude());
                attendaceCheckinData.setDate(timeStamp);
                attendaceCheckinData.setAttendanceType(CHECK_IN);
                String attendanceDate = Util.getDateFromTimestamp(timeStamp,"dd-MM-YYYY");
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
                Date date = null;
                try {
                    date = formatter.parse(attendanceDate);
                } catch (ParseException e) {
                    Log.e("TAG", e.getMessage());
                }
                attendaceCheckinData.setAttendaceDate(date.getTime());

                //attendaceData.setAttendanceFormattedDate(Util.getTodaysDate());
                //attendaceData.setMobileNumber(Util.getUserMobileFromPref());

                btCheckIn.setText("Check in at " + Util.getDateFromTimestamp(timeStamp,"hh:mm aa"));
                updateTime(timeStamp, 0);
                if (!Util.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), "Checked in offline.", Toast.LENGTH_LONG).show();
                    attendaceCheckinData.setSync(false);
                    userAttendanceDao.insert(attendaceCheckinData);
//                    getUserType = userAttendanceDao.getUserAttendanceType(CHECK_IN, Util.getTodaysDate(), Util.getUserMobileFromPref());
//                    if (getUserType.size() > 0 && !getUserType.isEmpty() && getUserType != null) {
//                        Toast.makeText(getActivity(), "Already check in", Toast.LENGTH_LONG).show();
//                    }
                } else {
                    attendaceCheckinData.setSync(true);
                        userAttendanceDao.insert(attendaceCheckinData);
                        SubmitAttendanceFragmentPresenter submitAttendanceFragmentPresenter = new SubmitAttendanceFragmentPresenter(this);
                        Util.showSimpleProgressDialog(getActivity(), "Attendance", "Loading...", false);
                        submitAttendanceFragmentPresenter.markAttendace(attendaceCheckinData);
                }
                Util.showToast(getResources().getString(R.string.check_in_msg), getActivity());
            } else {
                Util.showToast("Unable to get location", getActivity());
            }

        } else {
            gpsTracker.showSettingsAlert();
        }
        if (!isCheckOut) {
            getDiffBetweenTwoHours();
        }
    }

    private void markOut() {
        gpsTracker = new GPSTracker(getActivity());
        if (gpsTracker.isGPSEnabled(getActivity(), this)) {
            Date currentDate = new Date();
            timeStamp = currentDate.getTime();
            Location location = gpsTracker.getLocation();
            if (location != null) {
                enableButton(false, false);
                String attendanceDate = Util.getDateFromTimestamp(timeStamp,"dd-MM-YYYY");
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
                Date date = null;
                try {
                    date = formatter.parse(attendanceDate);
                } catch (ParseException e) {
                    Log.e("TAG", e.getMessage());
                }
                AttendaceData attendaceCheckinData = userAttendanceDao.getUserAttendace(date.getTime(), CHECK_IN);
                //attendaceCheckoutData = new AttendaceData();
                attendaceCheckoutData.setAttendanceId(attendaceCheckinData.getAttendanceId());
                attendaceCheckoutData.setLatitude(location.getLatitude());
                attendaceCheckoutData.setLongitude(location.getLongitude());
                attendaceCheckoutData.setDate(timeStamp);
                attendaceCheckoutData.setAttendanceType(CHECK_OUT);
                attendaceCheckoutData.setAttendaceDate(date.getTime());

                btCheckout.setText("Check out at " + Util.getDateFromTimestamp(timeStamp,"hh:mm aa"));
                updateTime(attendaceCheckinData.getDate(), timeStamp);
                if (!Util.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), "Checked out offline.", Toast.LENGTH_LONG).show();
                    attendaceCheckoutData.setSync(false);
                    userAttendanceDao.insert(attendaceCheckoutData);
//                    getUserType = userAttendanceDao.getUserAttendanceType(CHECK_IN, Util.getTodaysDate(), Util.getUserMobileFromPref());
//                    if (getUserType.size() > 0 && !getUserType.isEmpty() && getUserType != null) {
//                        Toast.makeText(getActivity(), "Already check in", Toast.LENGTH_LONG).show();
//                    }
                } else {
                    attendaceCheckoutData.setSync(true);
                    userAttendanceDao.insert(attendaceCheckoutData);
                    SubmitAttendanceFragmentPresenter submitAttendanceFragmentPresenter = new SubmitAttendanceFragmentPresenter(this);
                    Util.showSimpleProgressDialog(getActivity(), "Attendance", "Loading...", false);
                    submitAttendanceFragmentPresenter.markAttendace(attendaceCheckoutData);
                }
                Util.showToast(getResources().getString(R.string.check_out_msg), getActivity());
            } else {
                Util.showToast("Unable to get location", getActivity());
            }

        } else {
            gpsTracker.showSettingsAlert();
        }
        if (!isCheckOut) {
            getDiffBetweenTwoHours();
        }
    }



//    private void startTimerService() {
//
//
//        // get check in time 24 hrs;
//        Calendar calendar=Calendar.getInstance();
//        SimpleDateFormat sdf=new SimpleDateFormat("kk:mm:ss");
//        String CheckInTime24Hrs=sdf.format(calendar.getTime());
//
//        Intent timerService=new Intent(getContext(), ShowTimerService.class);
//        timerService.putExtra("CheckInTime",CheckInTime24Hrs);
//        getContext().startService(timerService);
//        getActivity().bindService(timerService, mConnection, Context.BIND_AUTO_CREATE);
//        mBound=true;
//        /* Intent intent = new Intent(getActivity(),ShowTimerService.class);
//        getActivity().startService(intent);
//        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);*/
//
//    }

//    public Location getLocation() {
//
//        location = gpsTracker.getLocation();
//        if (location != null) {
//            strLat = String.valueOf(location.getLatitude());
//            strLong = String.valueOf(location.getLongitude());
//        }
//        return location;
//    }
//        private String getCompleteAddressString ( double LATITUDE, double LONGITUDE){
//
//                strLong = gpsTracker.getLongitude();
//
//            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
//            try {
//                List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
//                if (addresses != null) {
//                    Address returnedAddress = addresses.get(0);
//                    StringBuilder strReturnedAddress = new StringBuilder("");
//
//                    for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
//                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
//                    }
//                    strAdd = strReturnedAddress.toString();
//                    Log.w("My address", strReturnedAddress.toString());
//                } else {
//                    Log.w("My address", "No Address returned!");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.w("My address", "Canont get Address!");
//            }
//            Log.i("Address", "222" + strAdd);
//            return strAdd;
//        }

    public Long getLongFromDate() {
        Long millis = null;
        String pattern = "yyyy/MM/dd HH:mm:ss";
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        Date today = Calendar.getInstance().getTime();
        String todayAsString = df.format(today);
        System.out.println("Today is: " + todayAsString);
        try {
            millis = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).parse(todayAsString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millis;
    }

    public void checkInResponse(String response, AttendaceData attendaceData) {
        String attendanceId;
        int status;
        Util.removeSimpleProgressDialog();
        getUserType = userAttendanceDao.getUserAttendanceType(CHECK_IN, Util.getTodaysDate(), Util.getUserMobileFromPref());
        if (getUserType != null && getUserType.size() > 0 && !getUserType.isEmpty()) {
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        } else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                status = jsonObject.getInt("status");
                String msg = jsonObject.getString("message");
                if (status == 200) {
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    attendanceId = jsonData.getString("attendanceId");
//                    attendaceData.setAttendanceId(attendanceId);
//                    attendaceData.setSync(true);
                    //userAttendanceDao.update(attendaceCheckinData);

                    String attendanceDate = Util.getDateFromTimestamp(attendaceData.getDate(),"dd-MM-YYYY");
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
                    Date date = null;
                    try {
                        date = formatter.parse(attendanceDate);
                    } catch (ParseException e) {
                        Log.e("TAG", e.getMessage());
                    }
//                            temp.setAttendaceDate(date.getTime());

                    userAttendanceDao.updateUserAttendace(attendanceId,true, date.getTime(),CHECK_IN);

                    Util.showToast(getResources().getString(R.string.check_in_msg), getActivity());
                } else {
                    Toast.makeText(getActivity(), "Checked in offline.", Toast.LENGTH_LONG).show();
                    //Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkOutResponse(String response, AttendaceData attendaceData) {
        Util.showToast(getResources().getString(R.string.check_out_msg), getActivity());

        int status = 0;
        String attendanceId = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.getInt("status");
            JSONObject jsonData = jsonObject.getJSONObject("data");
            attendanceId = jsonData.getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String attendanceDate = Util.getDateFromTimestamp(attendaceData.getDate(),"dd-MM-YYYY");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
        Date date = null;
        try {
            date = formatter.parse(attendanceDate);
        } catch (ParseException e) {
            Log.e("TAG", e.getMessage());
        }

        userAttendanceDao.updateUserAttendace(attendanceId,true, date.getTime(),CHECK_OUT);

        Util.removeSimpleProgressDialog();
//        getCheckOut = userCheckOutDao.getCheckOutData(CHECK_OUT, Util.getTodaysDate(), Util.getUserMobileFromPref());
//        if (getCheckOut != null && getCheckOut.size() > 0 && !getCheckOut.isEmpty()) {
//            Toast.makeText(getActivity(), "User Already Check Out", Toast.LENGTH_LONG).show();
//        } else if (status == 300) {
//            Util.showToast("User Already check out", getActivity());
//        } else {
//            AttendaceCheckOut attendaceData = new AttendaceCheckOut();
//            attendaceData.setUid(id);
//            Double lat = Double.parseDouble(strLat);
//            Double log = Double.parseDouble(strLong);
//
//            attendaceData.setLatitude(lat);
//            attendaceData.setLongitude(log);
//            attendaceData.setAddress(strAdd);
//            attendaceData.setAttendaceDate(timeStamp);
//            attendaceData.setAttendanceType(CHECK_OUT);
//            attendaceData.setTime(String.valueOf(checkOutTime));
//            attendaceData.setSync(true);
//
//            try {
//                attendaceData.setTotalHrs(getTotalHours());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                txt_total_hours.setText(getTotalHours());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            attendaceData.setAttendanceFormattedDate(Util.getTodaysDate());
//            attendaceData.setMobileNumber(Util.getUserMobileFromPref());
//
//            try {
//                userCheckOutDao.insert(attendaceData);
//                btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
//                btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
//                checkOutText = checkOutText + checkOutTime;
//                btCheckout.setText(checkOutText);
//                preferenceHelper.saveCheckOutText(KEY_CHECKOUTTEXT, checkOutText);
//                if (!isCheckOut) {
//                    getDiffBetweenTwoHours();
//                }
//                isCheckOut = true;
//                preferenceHelper.totalHours(KEY_TOTALHOURS, false);
//                Util.showToast(getResources().getString(R.string.check_out_msg), getActivity());
//            } catch (Exception e) {
//                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
//            }
//        }
    }

    public void updateTime(long checkIntime,long checkOutTime) {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Message msg = mHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_TOTALHOURS, calculateHoursFromTwoDate(checkIntime, checkOutTime));
                    msg.setData(bundle);
                    mHandler.sendMessage(msg);
                }
            }, 0, 1000);
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            //totalHoursFromTwonDate= bundle.getString(KEY_TOTALHOURS);
            txt_total_hours.setText(bundle.getString(KEY_TOTALHOURS));
        }
    };

    public String calculateHoursFromTwoDate(long checkInTime, long checkOutTime){

        int hours = 0,min=0;
        long ss=0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss a");
        Date startDate = null;
        try {
            startDate = simpleDateFormat.parse(Util.getDateFromTimestamp(checkInTime, "yyyy/MM/dd HH:mm:ss a"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date endDate = null;
        try {

            if(checkOutTime==0){
                Date d=new Date();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss a");
                endDate = sdf.parse(sdf.format(d.getTime()));
//                endDate=sdf.parse(checkOutTime);
            }else {
                endDate = simpleDateFormat.parse(Util.getDateFromTimestamp(checkOutTime, "yyyy/MM/dd HH:mm:ss a"));
// stop timer when got checkOutDate
                if(timer!=null){
                    timer.cancel();
                    timer=null;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(startDate!=null&&endDate!=null){
            long difference = endDate.getTime() - startDate.getTime();

            int days = (int) (difference / (1000*60*60*24));
            hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
            ss=(int)(difference /1000%60);

        }

/* Log.i("======= Hours"," :: "+hours);
Log.i("======= Minutes"," :: "+min);*/

        return hours+":"+min+":"+ss;
    }

    public void checkInError(String response) {
        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
        Log.i("ErrorData", "111" + response);
        Util.removeSimpleProgressDialog();
    }

    public void checkOutError(String response) {
        Toast.makeText(context, response, Toast.LENGTH_LONG).show();
        Util.removeSimpleProgressDialog();
    }

    public void enableCheckOut() {
//        btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
//        btCheckout.setTextColor(getResources().getColor(R.color.white));
//        btCheckout.setEnabled(true);
    }

//        public static class MessageHandler extends Handler {
//            @Override
//            public void handleMessage(Message message) {
//                Log.i("Handler Response", "111" + message);
//                Bundle bundle = message.getData();
//                Log.i("Bundle after service", "" + bundle);
//            }
//        }

    public String getTotalHours() throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date startDate = null;
        prefCheckInTime = preferenceHelper.getCheckInTime(KEY_CHECKINTIME);

        try {
            if (prefCheckInTime != null) {
                startDate = simpleDateFormat.parse((String) prefCheckInTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date endDate = simpleDateFormat.parse((String) DateFormat.format(Constants.TIME_FORMAT_, new Date().getTime()));
        if (endDate != null && startDate != null) {

            long difference = endDate.getTime() - startDate.getTime();
            if (difference < 0) {
                Date dateMax = simpleDateFormat.parse("24:00");
                Date dateMin = simpleDateFormat.parse("00:00");
                difference = (dateMax.getTime() - startDate.getTime()) + (endDate.getTime() - dateMin.getTime());
            }
            int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            Log.i("log_tag", "Hours: " + hours + ", Mins: " + min);
            totalHrs = String.valueOf(hours);
            totalMin = String.valueOf(min);
        }
        return totalHrs + " :" + totalMin;
    }

//    public ServiceConnection mConnection = new ServiceConnection() {
//
//        public void onServiceConnected(ComponentName className, IBinder service) {
//            showTimerService = ((ShowTimerService.MyBinder) service).getService();
//            mBound=true;
//            if(mBound){
//
//                showTimerService.doWork();
//            }
//        }
//
//        public void onServiceDisconnected(ComponentName className) {
//            mBound=false;
//        }
//    };

    @Override
    public void onStart() {
        super.onStart();
        /*Intent intent = new Intent(getActivity(),ShowTimerService.class);
        intent.putExtra("CheckInTime","");
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);*/
    }

    @Override
    public void onStop() {
        super.onStop();
        //getActivity().unbindService(mConnection);
        //mBound=false;
    }


    public String getDiffBetweenTwoHours() {
        if (preferenceHelper.showTotalHours(KEY_TOTALHOURS)) {
            try {
                totalHours = getTotalHours();
                txt_total_hours.setText(totalHours);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return totalHours;
    }


    private void updateButtonTextIfUserSaved() {

//        getUserType = userAttendanceDao.getUserAttendanceType(CHECK_IN, Util.getTodaysDate(), Util.getUserMobileFromPref());
//        if (getUserType != null && getUserType.size() > 0 && !getUserType.isEmpty()) {
//
//            btCheckIn.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
//            //btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));
//            clearCheckInButtonText();
//            //btCheckIn.setText("Check in at " + getUserType.get(0).getTime());
////            db_chkin_time = getUserType.get(0).getTime();
////            time = getUserType.get(0).getTime();
//            enableCheckOut();
//
//        }
//        getCheckOut = userCheckOutDao.getCheckOutData(CHECK_OUT, Util.getTodaysDate(), Util.getUserMobileFromPref());
//        if (getCheckOut != null && getCheckOut.size() > 0 && !getCheckOut.isEmpty()) {
//            btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
//            btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
//            clearCheckOutButtonText();
//            btCheckout.setText("Check out at " + getCheckOut.get(0).getTime());
//            db_chkout_time = getCheckOut.get(0).getTime();
//            String totalHrs = getCheckOut.get(0).getTotalHrs();
//            txt_total_hours.setText(totalHrs);

       // }

        //  check that user time is available on server
//        if (userServerCheckInTime != null) {
//            makeCheckInButtonGray();
//            clearCheckInButtonText();
//            enableCheckOut();
//            //btCheckIn.setText("Check in at" + userServerCheckInTime);
//            time = userServerCheckInTime;
//        }
//        if (userServerCheckOutTime != null) {
//            makeCheckOutButtonGray();
//            clearCheckOutButtonText();
//            btCheckout.setText("Check out at " + userServerCheckOutTime);
//        }
//
//        if (availableOnServer != null && !availableOnServer.isEmpty()) {
//            String totalHrs = calculateTotalHours(userServerCheckInTime, userServerCheckOutTime);
//            txt_total_hours.setText(totalHrs);
//
//        } else {
//            String totalHrs = calculateTotalHours(db_chkin_time, db_chkout_time);
//            txt_total_hours.setText(totalHrs);
//        }
    }

    private String calculateTotalHours(String userServerCheckInTime, String userServerCheckOutTime) {
        long hours = 0, min = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm a");
        Date startDate = null;
        try {

            startDate = simpleDateFormat.parse(userServerCheckInTime);


        } catch (ParseException e) {
            e.printStackTrace();
        }


        Date endDate = null;
        try {

            if (userServerCheckOutTime == null || userServerCheckOutTime == "") {
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
                userServerCheckOutTime = sdf.format(d);
                endDate = sdf.parse(userServerCheckOutTime);
            } else {
                endDate = simpleDateFormat.parse(userServerCheckOutTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (startDate != null && endDate != null) {
            long difference = endDate.getTime() - startDate.getTime();

            int days = (int) (difference / (1000 * 60 * 60 * 24));
            hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);

        }

        Log.i("======= Hours", " :: " + hours);
        Log.i("======= Minutes", " :: " + min);

        return hours + ":" + min;
    }

    public void setButtonText() {

//        String checkIn = getCheckInButtonText(KEY_CHECKINTEXT);
//        String checkOut = getCheckOutButtonText(KEY_CHECKOUTTEXT);
//
//        btCheckIn.setText(checkIn);
//        btCheckout.setText(checkOut);

    }

    public String getCheckInButtonText(String key) {
        return preferenceHelper.getCheckInButtonText(key);
    }

    public String getCheckOutButtonText(String key) {
        return preferenceHelper.getCheckOutText(key);
    }

    public boolean deleteUserAttendanceData() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PreferenceHelper.PREFER_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(KEY_CHECKINTIME).apply();
        sharedPreferences.edit().remove(KEY_CHECKINTEXT).apply();
        sharedPreferences.edit().remove(KEY_CHECKOUTTEXT).apply();
        sharedPreferences.edit().remove(KEY_TOTALHOURS).apply();
        sharedPreferences.edit().remove(KEY_ISCHECKOUT).apply();
        sharedPreferences.edit().remove(KEY_TOTAL_HRS_CHEKOUT).apply();
        return true;
    }

    public void checkTodayMarkInOrNot() {
//        getUserType = userAttendanceDao.getUserAttendanceType(CHECK_IN, Util.getTodaysDate(), Util.getUserMobileFromPref());
//        if (getUserType != null && getUserType.size() > 0 && !getUserType.isEmpty()) {
//            setButtonText();
//            btCheckIn.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
//            btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));
//
//        } else {
//
//            btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
//            btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
//            btCheckout.setEnabled(false);
//
//        }
//
//        getCheckOut = userCheckOutDao.getCheckOutData(CHECK_OUT, Util.getTodaysDate(), Util.getUserMobileFromPref());
//        if (getCheckOut != null && getCheckOut.size() > 0 && !getCheckOut.isEmpty()) {
//            btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
//            btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
//            setButtonText();
//        }

    }

    public void deleteSharedPreferece() {
        getUserType = userAttendanceDao.getUserAttendanceType(CHECK_IN, Util.getTodaysDate(), Util.getUserMobileFromPref());
        if (getUserType != null && getUserType.size() > 0 && !getUserType.isEmpty()) {

        } else {
            deleteUserAttendanceData();
        }
    }

    public void makeCheckInButtonGray() {
//        btCheckIn.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
//        btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));
    }

    public void makeCheckOutButtonGray() {
//        btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
//        btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
    }

    public void clearCheckInButtonText() {
        //btCheckIn.setText("");
    }

    public void clearCheckOutButtonText() {
        //btCheckout.setText("");
    }

}

