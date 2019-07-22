package com.platform.view.fragments;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.Parcelable;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.platform.R;
import com.platform.dao.UserAttendanceDao;
import com.platform.dao.UserCheckOutDao;
import com.platform.database.DatabaseManager;
import com.platform.listeners.CreateEventListener;
import com.platform.listeners.PlatformTaskListener;
import com.platform.listeners.SubmitAttendanceListener;
import com.platform.models.attendance.AttendaceCheckOut;
import com.platform.models.attendance.AttendaceData;
import com.platform.models.home.Modules;
import com.platform.models.leaves.LeaveDetail;
import com.platform.models.planner.SubmoduleData;
import com.platform.models.planner.attendanceData;
import com.platform.presenter.MonthlyAttendanceFragmentPresenter;
import com.platform.presenter.PlannerFragmentPresenter;

import com.platform.presenter.SubmitAttendanceFragmentPresenter;
import com.platform.services.AttendanceService;
import com.platform.services.ShowTimerService;
import com.platform.utility.AppEvents;
import com.platform.utility.Config;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.ACTIVITY_SERVICE;
import static androidx.constraintlayout.motion.widget.Debug.getLocation;

public class PlannerFragment extends Fragment implements PlatformTaskListener {

    private View plannerView;

    TextView tvCheckInTime;
    TextView tvCheckOutTime;
    TextView tvAttendanceDetails;
    Button btCheckIn;
    Button btCheckout;

    private PlannerFragmentPresenter plannerFragmentPresenter;
    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;
    private List<LeaveDetail> leaveBalance = new ArrayList<>();
    public static ArrayList<Integer> leaveBackground = new ArrayList<>();
    private GPSTracker gpsTracker;
    private String strLat, strLong;
    private String strAdd = "";
    private Long millis = null;
    private CharSequence time,checkOutTime,currentTime,checkInTime;
    private UserAttendanceDao userAttendanceDao;
    private UserCheckOutDao userCheckOutDao;
    List<AttendaceData> attendaceDataList;
    public String todayAsString;
    private int year, month;
    private String CHECK_IN = "checkin";
    private String CHECK_OUT = "checkout";
    private String attendaceId;

    String availableOnServer=null,totalHours,totalHrs,totalMin;
    // private SubmitAttendanceFragmentPresenter submitAttendanceFragmentPresenter;
    private Context context;
    List<AttendaceData> getUserType;
    //List<AttendaceData> getCheckOut;
    List<AttendaceCheckOut> getCheckOut;
    private boolean checkOutStatus=false;
    private PreferenceHelper preferenceHelper;
    private AlarmManager alarmManager;
    private TextView txt_timer,txt_total_hours;

    private Timer updateTimer;
    BroadcastReceiver getHoursDifferenceReceiver;
    public static  ShowTimerService showTimerService;
    private static boolean mBound;
    private boolean showTotalHours=false,isCheckOut=false;

    private static String KEY_TOTALHOURS="totalHours";
    private static String KEY_CHECKINTIME="checkInTime";

    public static  String KEY_CHECKINTEXT="checkInText";
    public static  String KEY_CHECKOUTTEXT="checkOutText";

    private String checkInText="Check in at";
    private String checkOutText="Check out at";
    private String prefCheckInTime;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
            ((HomeActivity) getActivity()).setSyncButtonVisibility(false);

            if ((boolean)getArguments().getSerializable("SHOW_BACK")) {
                ((HomeActivity) getActivity()).showBackArrow();
            }
        }

        AppEvents.trackAppEvent(getString(R.string.event_meetings_screen_visit));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        plannerView = inflater.inflate(R.layout.fragment_dashboard_planner, container, false);
        // start timer
        updateTimer = new Timer();

        return plannerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        preferenceHelper=new PreferenceHelper(getActivity());



        /* if(checkServiceRunning()){
            Toast.makeText(context,"Service Running",Toast.LENGTH_LONG).show();
            showTimerService.doWork();
        }
        else {
            Toast.makeText(context,"Service Stopped",Toast.LENGTH_LONG).show();
        }
*/
        getHoursDifferenceReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle=intent.getExtras();
                String totlaHours="";
                if(bundle!=null){
                    totlaHours=bundle.getString("HoursDiff");
                }
                Toast.makeText(context,"Received Hours"+totlaHours,Toast.LENGTH_LONG).show();
            }
        };
        alarmManager= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar=Util.setHours(17,03);
        if(calendar.getTimeInMillis()<System.currentTimeMillis()){

        }else {
                Intent callAlarmReceiver=new Intent(context,AttendanceService.class);
                callAlarmReceiver.putExtra("requestJson","");
                callAlarmReceiver.putExtra("messenger",new Messenger(new MessageHandler()));
                PendingIntent pendingIntent=PendingIntent.getService(getActivity(),0,callAlarmReceiver,PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

        }

        btCheckIn = plannerView.findViewById(R.id.bt_check_in);
        btCheckout = plannerView.findViewById(R.id.bt_checkout);


        //tvCheckInTime = plannerView.findViewById(R.id.tv_check_in_time);
        //tvCheckOutTime = plannerView.findViewById(R.id.tv_check_out_time);
        tvAttendanceDetails = plannerView.findViewById(R.id.tv_attendance_details);
        txt_total_hours=plannerView.findViewById(R.id.iv_total_hours);
        txt_timer=plannerView.findViewById(R.id.txt_timer);

        if(!isCheckOut){
            getDiffBetweenTwoHours();
        }

        initCardView();
        deleteSharedPreferece();

        Toast.makeText(context, "I am in OnCreate", Toast.LENGTH_SHORT).show();



    }

    private void initCardView() {
        if (getActivity() == null) {
            return;
        }


        userAttendanceDao = DatabaseManager.getDBInstance(getActivity()).getAttendaceSchema();
        userCheckOutDao=DatabaseManager.getDBInstance(getActivity()).getCheckOutAttendaceSchema();

        progressBarLayout = plannerView.findViewById(R.id.profile_act_progress_bar);
        progressBar = plannerView.findViewById(R.id.pb_profile_act);

        checkTodayMarkInOrNot();



        // comment by deepak on 1-07-2019
        plannerFragmentPresenter = new PlannerFragmentPresenter(this);
        plannerFragmentPresenter.getPlannerData();

        RelativeLayout rl_events = plannerView.findViewById(R.id.ly_events);
        RelativeLayout rl_tasks = plannerView.findViewById(R.id.ly_task);
        RelativeLayout rl_attendance = plannerView.findViewById(R.id.ly_attendance);
        RelativeLayout rl_leaves = plannerView.findViewById(R.id.ly_leave);


        List<Modules> approveModules = DatabaseManager.getDBInstance(getActivity().getApplicationContext())
                .getModulesOfStatus(Constants.RequestStatus.APPROVED_MODULE);

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.Planner.KEY_IS_DASHBOARD, true);

        //to display tha submodules
        for (Modules m : approveModules) {
            switch (m.getName().getDefaultValue()) {
                case Constants.Planner.ATTENDANCE_KEY_:
                    rl_attendance.setVisibility(View.VISIBLE);
                    setAttendaceView();
                    break;
                case Constants.Planner.EVENTS_KEY_:
                    rl_events.setVisibility(View.VISIBLE);
                    setEventView();
                    break;
                case Constants.Planner.TASKS_KEY_:
                    rl_tasks.setVisibility(View.VISIBLE);
                    setTaskView();
                    break;
                case Constants.Planner.LEAVES_KEY_:
                    rl_leaves.setVisibility(View.VISIBLE);
                    setLeaveView();
                    break;
                default:
                    break;
            }
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
    }

    public void showPlannerSummary(ArrayList<SubmoduleData> submoduleList) {

        for (SubmoduleData obj : submoduleList) {
            switch (obj.getSubModule()) {
                case Constants.Planner.ATTENDANCE_KEY:

                    List<attendanceData> attendanceData = obj.getAttendanceData();
                    for (int i = 0; i < attendanceData.size(); i++) {
                        availableOnServer = attendanceData.get(i).get_id();

                    }
                    preferenceHelper.insertString(Constants.KEY_ATTENDANCDE, availableOnServer);
                    Log.i("AttendanceId", "111" + availableOnServer);

//((ViewHolderAttendace)holder).
                    break;
                case Constants.Planner.EVENTS_KEY:
                    if (obj.getEventTaskData() != null && obj.getEventTaskData().size() > 0) {

                        RecyclerView.LayoutManager mLayoutManagerEvent = new LinearLayoutManager(getActivity().getApplicationContext());
                        EventTaskListAdapter eventTaskListAdapter = new EventTaskListAdapter(getActivity(),
                                obj.getEventTaskData(), Constants.Planner.EVENTS_LABEL);
                        RecyclerView rvEvents = plannerView.findViewById(R.id.rv_events);
                        rvEvents.setLayoutManager(mLayoutManagerEvent);
                        rvEvents.setAdapter(eventTaskListAdapter);
                    }


                    break;
                case Constants.Planner.TASKS_KEY:
                    if (obj.getTaskData() != null && obj.getTaskData().size() > 0) {

                        RecyclerView.LayoutManager mLayoutManagerTask = new LinearLayoutManager(getActivity().getApplicationContext());
                        EventTaskListAdapter taskListAdapter = new EventTaskListAdapter(getActivity(),
                                obj.getTaskData(), Constants.Planner.EVENTS_LABEL);
                        RecyclerView rvTask = plannerView.findViewById(R.id.rv_events);
                        rvTask.setLayoutManager(mLayoutManagerTask);
                        rvTask.setAdapter(taskListAdapter);
                    }
                    break;
                case Constants.Planner.LEAVES_KEY:
                    if (obj.getLeave() != null && obj.getLeave().size() > 0) {
                        leaveBalance.addAll(obj.getLeave());
                        RecyclerView.LayoutManager mLayoutManagerLeave = new LinearLayoutManager(getActivity(),
                                LinearLayoutManager.HORIZONTAL, true);
                        LeaveBalanceAdapter LeaveAdapter = new LeaveBalanceAdapter(
                                obj.getLeave(), "LeaveBalance");
                        RecyclerView rvLeaveBalance = plannerView.findViewById(R.id.rv_leave_balance);
                        rvLeaveBalance.setLayoutManager(mLayoutManagerLeave);
                        rvLeaveBalance.setAdapter(LeaveAdapter);
                    }
                    break;
            }
        }

    }
        public void setAttendaceView () {


            btCheckIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //tvCheckInTime.setText(DateFormat.format(Constants.TIME_FORMAT, new Date().getTime()));
                    //tvCheckInTime.setVisibility(View.VISIBLE);
                /*btCheckIn.setBackground(getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));*/

                    time = DateFormat.format(Constants.TIME_FORMAT,new Date().getTime());



                    if (availableOnServer != null && availableOnServer.length() > 0) {
                        //statTimer();
                        //startTimerService();
                        Toast.makeText(getActivity(), "User already check in", Toast.LENGTH_LONG).show();
                        //Util.showDelayInCheckInDialog(getActivity(),getResources().getString(R.string.delayedin_checkin),getResources().getString(R.string.delayin_checkin_title),true);

                    } else {
                         markCheckIn();


                    }


                }
            });
            btCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //tvCheckOutTime.setText(DateFormat.format(Constants.TIME_FORMAT, new Date().getTime()));
                    //tvCheckOutTime.setVisibility(View.VISIBLE);
                    checkOutTime = DateFormat.format(Constants.TIME_FORMAT, new Date().getTime());
                    markOut();

                    //preferenceHelper.totalHours(KEY_TOTALHOURS,false);




                }
            });

            tvAttendanceDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
                    intent.putExtra(Constants.Planner.KEY_IS_DASHBOARD, false);
                    intent.putExtra("title", getActivity().getString(R.string.attendance));
                    intent.putExtra("switch_fragments", "AttendancePlannerFragment");
                    intent.putExtra("TotalHours",getDiffBetweenTwoHours());
                    getActivity().startActivity(intent);
                }
            });


        }

        private void markOut () {
            gpsTracker = new GPSTracker(getActivity());

            if (gpsTracker.isGPSEnabled(getActivity(), this)) {

                if (!gpsTracker.canGetLocation()) {
                    Toast.makeText(getActivity(), "Unable to get lat and log", Toast.LENGTH_LONG).show();
                }
                getLocation();
                getCompleteAddressString(Double.parseDouble(strLat), Double.parseDouble(strLong));
                millis = getLongFromDate();
                if (!Util.isConnected(getActivity())) {

                    // offline storage
                    //userAttendanceDao=DatabaseManager.getDBInstance(getActivity()).getAttendaceSchema();
                    // offline save
                    getCheckOut = userCheckOutDao.getCheckOutData(CHECK_OUT, Util.getTodaysDate(), Util.getUserMobileFromPref());


                    if (getCheckOut.size() > 0 && !getCheckOut.isEmpty() && getCheckOut != null) {
                        Toast.makeText(getActivity(), "Already check out", Toast.LENGTH_LONG).show();
                        btCheckout.setBackground(getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                        btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
                        //tvCheckOutTime.setText(getCheckOut.get(0).getTime());
                    } else {

                        AttendaceCheckOut attendaceData = new AttendaceCheckOut();
                        attendaceData.setUid("000");
                        Double lat = Double.parseDouble(strLat);
                        Double log = Double.parseDouble(strLong);
                        attendaceData.setLatitude(lat);
                        attendaceData.setLongitude(log);
                        attendaceData.setAddress(strAdd);
                        attendaceData.setAttendaceDate(millis);
                        attendaceData.setAttendanceType(CHECK_OUT);
                        attendaceData.setTime(String.valueOf(checkOutTime));
                        attendaceData.setSync(false);

                        try {
                            attendaceData.setTotalHrs(getTotalHours());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        attendaceData.setAttendanceFormattedDate(Util.getTodaysDate());
                        attendaceData.setMobileNumber(Util.getUserMobileFromPref());

                        //tvCheckOutTime.setText(checkOutTime);
                        try {
                            txt_total_hours.setText(getTotalHours());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        userCheckOutDao.insert(attendaceData);
                        btCheckout.setBackground(getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                        btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));

                        checkOutText=checkOutText+ checkOutTime;
                        preferenceHelper.saveCheckOutText(KEY_CHECKOUTTEXT,checkOutText);
                        if(!isCheckOut){
                            getDiffBetweenTwoHours();
                        }
                        isCheckOut=true;
                        setButtonText();
                        preferenceHelper.totalHours(KEY_TOTALHOURS,false);

                        // update total houes
                        Log.i("OfflineMarkOutData", "111" + attendaceData);
                    }


                } else {

                    Util.showSimpleProgressDialog(getActivity(), "Attendance", "Loading...", false);
                    attendaceId = userAttendanceDao.getUserId(Util.getTodaysDate(), Util.getUserMobileFromPref());
                    SubmitAttendanceFragmentPresenter submitAttendanceFragmentPresenter = new SubmitAttendanceFragmentPresenter(this);
                    submitAttendanceFragmentPresenter.markOutAttendance(attendaceId, CHECK_OUT, millis, strLat, strLong);

                }


            } else {
                gpsTracker.showSettingsAlert();
            }

            if(!isCheckOut){
                getDiffBetweenTwoHours();
            }
            isCheckOut=true;
            preferenceHelper.totalHours(KEY_TOTALHOURS,false);


        }

        public void setEventView () {

        TextView tvAllEventsDetail = plannerView.findViewById(R.id.tv_all_events_list);
        TextView btAddEvents = plannerView.findViewById(R.id.bt_add_events);

        tvAllEventsDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEventList = new Intent(getActivity(), PlannerDetailActivity.class);
                intentEventList.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.EVENTS_LABEL);
                getActivity().startActivity(intentEventList);
            }
        });
        btAddEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCreateEvent = new Intent(getActivity(), CreateEventTaskActivity.class);
                intentCreateEvent.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.EVENTS_LABEL);
                getActivity().startActivity(intentCreateEvent);
            }
        });

    }

    public void setTaskView() {

        RecyclerView rvEvents = plannerView.findViewById(R.id.rv_task);
        TextView tvAllEventsDetail = plannerView.findViewById(R.id.tv_all_task_list);
        TextView btAddEvents = plannerView.findViewById(R.id.bt_add_task);

        tvAllEventsDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentEventList = new Intent(getActivity(), PlannerDetailActivity.class);
                intentEventList.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.TASKS_LABEL);
                getActivity().startActivity(intentEventList);
            }
        });
        btAddEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCreateEvent = new Intent(getActivity(), CreateEventTaskActivity.class);
                intentCreateEvent.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.TASKS_LABEL);
                getActivity().startActivity(intentCreateEvent);
            }
        });

    }

    public void setLeaveView() {
        TextView tvCheckLeaveDetailsLink = plannerView.findViewById(R.id.tv_link_check_leaves);
        TextView imgClickAddLeaves = plannerView.findViewById(R.id.fab_add_leaves);

        tvCheckLeaveDetailsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
                intent.putExtra("title", getActivity().getString(R.string.leave));
                intent.putExtra("switch_fragments", "LeaveDetailsFragment");
                intent.putExtra("leaveBalance", (Serializable) leaveBalance);
                getActivity().startActivity(intent);
            }
        });
        imgClickAddLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
                intent.putExtra("title", getActivity().getString(R.string.apply_leave));
                intent.putExtra("isEdit", false);
                intent.putExtra("apply_type", "Leave");
                intent.putExtra("switch_fragments", "LeaveApplyFragment");
                intent.putExtra("leaveBalance", (Serializable) leaveBalance);
                getActivity().startActivity(intent);
            }
        });


    }

        public void markCheckIn () {

            gpsTracker = new GPSTracker(getActivity());

            if (gpsTracker.isGPSEnabled(getActivity(), this)) {

                getLocation();
                getCompleteAddressString(Double.parseDouble(strLat), Double.parseDouble(strLong));
                millis = getLongFromDate();
                if (!Util.isConnected(getActivity())) {

                    getUserType = userAttendanceDao.getUserAttendanceType(CHECK_IN, Util.getTodaysDate(), Util.getUserMobileFromPref());
                    if (getUserType.size() > 0 && !getUserType.isEmpty() && getUserType != null) {
                        Toast.makeText(getActivity(), "Already check in", Toast.LENGTH_LONG).show();
                        startTimerService();
                    } else {


                        AttendaceData attendaceData = new AttendaceData();
                        attendaceData.setUid("000");
                        Double lat = Double.parseDouble(strLat);
                        Double log = Double.parseDouble(strLong);
                        attendaceData.setLatitude(lat);
                        attendaceData.setLongitude(log);
                        attendaceData.setAddress(strAdd);
                        attendaceData.setAttendaceDate(millis);
                        attendaceData.setAttendanceType(CHECK_IN);
                        attendaceData.setTime(String.valueOf(time));
                        attendaceData.setSync(false);
                        attendaceData.setAttendanceFormattedDate(Util.getTodaysDate());
                        attendaceData.setMobileNumber(Util.getUserMobileFromPref());
                        userAttendanceDao.insert(attendaceData);

                        btCheckIn.setBackground(getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                        btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));
                        btCheckIn.setText("Check in at"+ time);
                        //tvCheckInTime.setText(time);
                        checkInTime=DateFormat.format(Constants.TIME_FORMAT_,new Date().getTime());

                        preferenceHelper.saveCheckInTime(KEY_CHECKINTIME,checkInTime);
                        preferenceHelper.totalHours(KEY_TOTALHOURS,true);
                        checkInText="Check in at"+ time;
                        preferenceHelper.saveCheckInButtonText(KEY_CHECKINTEXT,checkInText);
                        setButtonText();

                        // show check in Time
                        enableCheckOut();
                        //statTimer();

                        Log.i("OfflineData", "111" + attendaceData);
                    }


                } else {

                    SubmitAttendanceFragmentPresenter submitAttendanceFragmentPresenter = new SubmitAttendanceFragmentPresenter(this);
                    Util.showSimpleProgressDialog(getActivity(),"Attendance", "Loading...", false);
                    submitAttendanceFragmentPresenter.markAttendace(CHECK_IN, millis, time.toString(), "", strLat, strLong, strAdd);

                }

            } else {
                gpsTracker.showSettingsAlert();
            }
            if(!isCheckOut){
                getDiffBetweenTwoHours();
            }

            //Intent intent = new Intent(getActivity(),ShowTimerService.class);
            //intent.putExtra("CheckInTime",checkInTime);
            //getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }

    private void startTimerService() {


        // get check in time 24 hrs;
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("kk:mm:ss");
        String CheckInTime24Hrs=sdf.format(calendar.getTime());

        Intent timerService=new Intent(getContext(), ShowTimerService.class);
        timerService.putExtra("CheckInTime",CheckInTime24Hrs);
        getContext().startService(timerService);
        getActivity().bindService(timerService, mConnection, Context.BIND_AUTO_CREATE);
        mBound=true;
        /* Intent intent = new Intent(getActivity(),ShowTimerService.class);
        getActivity().startService(intent);
        getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);*/

    }

    public void getLocation () {
            Location location = gpsTracker.getLocation();

            if (location != null) {
                strLat = String.valueOf(location.getLatitude());
                strLong = String.valueOf(location.getLongitude());
            } else {
                strLat = gpsTracker.getLatitude();
                Log.i("latLong", "222" + strLat + ":-" + strLong);


            }
        }
        private String getCompleteAddressString ( double LATITUDE, double LONGITUDE){

                strLong = gpsTracker.getLongitude();

            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
                if (addresses != null) {
                    Address returnedAddress = addresses.get(0);
                    StringBuilder strReturnedAddress = new StringBuilder("");

                    for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    }
                    strAdd = strReturnedAddress.toString();
                    Log.w("My address", strReturnedAddress.toString());
                } else {
                    Log.w("My address", "No Address returned!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.w("My address", "Canont get Address!");
            }
            Log.i("Address", "222" + strAdd);
            return strAdd;
        }

        public Long getLongFromDate () {
            Long millis = null;
            String pattern = "yyyy/MM/dd HH:mm:ss";

// Create an instance of SimpleDateFormat used for formatting
// the string representation of date according to the chosen pattern
            SimpleDateFormat df = new SimpleDateFormat(pattern);

// Get the today date using Calendar object.
            Date today = Calendar.getInstance().getTime();


// Using DateFormat format method we can create a string
// representation of a date with the defined format.
            String todayAsString = df.format(today);

// Print it!
            System.out.println("Today is: " + todayAsString);

            try {
                millis = new SimpleDateFormat("yyyy/MM/dd").parse(todayAsString).getTime();
                Log.i("Epoch", "111" + millis);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return millis;


        }


        public void checkInResponse(String response){
            String attendanceId;
            int status;
            Util.removeSimpleProgressDialog();
            getUserType = userAttendanceDao.getUserAttendanceType(CHECK_IN, Util.getTodaysDate(), Util.getUserMobileFromPref());
            if (getUserType != null && getUserType.size() > 0 && !getUserType.isEmpty()) {
                Toast.makeText(getActivity(), "User Already Check In", Toast.LENGTH_LONG).show();
            } else {


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    if (status == 300) {
                        Toast.makeText(getActivity(), "Today is holiday you cant login", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        attendanceId = jsonData.getString("attendanceId");
                        // save this attendanceIS in SP
                        // online save
                        AttendaceData attendaceData = new AttendaceData();
                        attendaceData.setUid(attendanceId);
                        Double lat = Double.parseDouble(strLat);
                        Double log = Double.parseDouble(strLong);
                        attendaceData.setLatitude(lat);
                        attendaceData.setLongitude(log);
                        attendaceData.setAddress(strAdd);
                        attendaceData.setAttendaceDate(millis);
                        attendaceData.setAttendanceType(CHECK_IN);
                        attendaceData.setTime(String.valueOf(time));
                        attendaceData.setSync(true);
                        attendaceData.setAttendanceFormattedDate(Util.getTodaysDate());
                        attendaceData.setMobileNumber(Util.getUserMobileFromPref());

                        //tvCheckInTime.setText(time);
                        userAttendanceDao.insert(attendaceData);



                        btCheckIn.setBackground(getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                        btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));
                        //btCheckIn.setText("Check in at "+time);
                        //tvCheckInTime.set
                        // Text(time);

                        checkInTime=DateFormat.format(Constants.TIME_FORMAT_,new Date().getTime());
                        preferenceHelper.saveCheckInTime(KEY_CHECKINTIME,checkInTime);
                        preferenceHelper.totalHours(KEY_TOTALHOURS,true);
                        checkInText="Check in at"+ time;
                        preferenceHelper.saveCheckInButtonText(KEY_CHECKINTEXT,checkInText);
                        setButtonText();
                        //statTimer();
                        enableCheckOut();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

        public void checkOutResponse (String response)
        {
            Log.i("checkOut", "111" + response);
            Util.removeSimpleProgressDialog();
            getCheckOut = userCheckOutDao.getCheckOutData(CHECK_OUT, Util.getTodaysDate(), Util.getUserMobileFromPref());
            if (getCheckOut != null && getCheckOut.size() > 0 && !getCheckOut.isEmpty()) {
                Toast.makeText(getActivity(), "User Already Check Out", Toast.LENGTH_LONG).show();
            } else {
                AttendaceCheckOut attendaceData = new AttendaceCheckOut();
                attendaceData.setUid(attendaceId);
                Double lat = Double.parseDouble(strLat);
                Double log = Double.parseDouble(strLong);

                attendaceData.setLatitude(lat);
                attendaceData.setLongitude(log);
                attendaceData.setAddress(strAdd);
                attendaceData.setAttendaceDate(millis);
                attendaceData.setAttendanceType(CHECK_OUT);
                attendaceData.setTime(String.valueOf(checkOutTime));
                attendaceData.setSync(true);

                try {
                    attendaceData.setTotalHrs(getTotalHours());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    txt_total_hours.setText(getTotalHours());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                attendaceData.setAttendanceFormattedDate(Util.getTodaysDate());
                attendaceData.setMobileNumber(Util.getUserMobileFromPref());

                try {
                    userCheckOutDao.insert(attendaceData);
                    btCheckout.setBackground(getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                    btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
                    checkOutText=checkOutText+ checkOutTime;
                    preferenceHelper.saveCheckOutText(KEY_CHECKOUTTEXT,checkOutText);
                    setButtonText();

                    if(!isCheckOut){
                        getDiffBetweenTwoHours();
                    }
                    isCheckOut=true;
                    preferenceHelper.totalHours(KEY_TOTALHOURS,false);

                    //tvCheckOutTime.setText(checkOutTime);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }


            }

        }

        public void checkInError (String response){
            Toast.makeText(context, "User Already CheckIn", Toast.LENGTH_LONG).show();
            Util.removeSimpleProgressDialog();

        }
        public void checkOutError (String response){
            Toast.makeText(context, response, Toast.LENGTH_LONG).show();
            Util.removeSimpleProgressDialog();
        }

        public void enableCheckOut ()
        {
            btCheckout.setBackground(getResources().getDrawable(R.drawable.bg_button));
            btCheckout.setTextColor(getResources().getColor(R.color.white));
            btCheckout.setEnabled(true);
        }

        public static class MessageHandler extends Handler {
            @Override
            public void handleMessage(Message message) {
                Log.i("Handler Response", "111" + message);
                Bundle bundle = message.getData();
                Log.i("Bundle after service", "" + bundle);
            }
        }

        public String getTotalHours () throws ParseException {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date startDate = null;
            prefCheckInTime=preferenceHelper.getCheckInTime(KEY_CHECKINTIME);

            try {
                if(prefCheckInTime!=null){
                    startDate = simpleDateFormat.parse((String)prefCheckInTime);
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            Date endDate = simpleDateFormat.parse((String) DateFormat.format(Constants.TIME_FORMAT_,new Date().getTime()));
            if(endDate!=null&&startDate!=null){

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

        public void statTimer()
        {

            Handler handler=new Handler();

                updateTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {


                        DateFormat.format(Constants.TIME_FORMAT, new Date().getTime());
                        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
                        try {

                            Date date1 = format.parse((String)time);
                            long millisecond=date1.getTime();
                            int hours = (int) (millisecond/(1000 * 60 * 60));
                            int mins = (int) (millisecond/(1000*60)) % 60;

                            String checkInTime=hours+":"+mins;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    txt_timer.setText(time);
                                }
                            });


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }



                    }
                },1000);
        }

        public void stopTimer()
        {

        }


    public boolean checkServiceRunning(){
        ActivityManager manager = (ActivityManager)getActivity().getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if ("com.platform.services.ShowTimerService"
                    .equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }


    public ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            showTimerService = ((ShowTimerService.MyBinder) service).getService();
            mBound=true;
            if(mBound){

                showTimerService.doWork();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            mBound=false;
        }
    };

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


    public String getDiffBetweenTwoHours()
    {
        if(preferenceHelper.showTotalHours(KEY_TOTALHOURS)){
            try {
                totalHours=getTotalHours();
                txt_total_hours.setText(totalHours);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return totalHours;
    }


    @Override
    public void onResume() {
        super.onResume();
        setButtonText();

        getUserType = userAttendanceDao.getUserAttendanceType(CHECK_IN,Util.getTodaysDate(),Util.getUserMobileFromPref());
        if (getUserType != null && getUserType.size() > 0 && !getUserType.isEmpty()) {

            btCheckIn.setBackground(getResources().getDrawable(R.drawable.bg_grey_box_with_border));
            btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));

            //set background
            btCheckout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btCheckout.setTextColor(getResources().getColor(R.color.white));
            btCheckout.setEnabled(true);
        }
        getCheckOut=userCheckOutDao.getCheckOutData(CHECK_OUT,Util.getTodaysDate(),Util.getUserMobileFromPref());
        if (getCheckOut != null && getCheckOut.size() > 0 && !getCheckOut.isEmpty())
        {
            btCheckout.setBackground(getResources().getDrawable(R.drawable.bg_grey_box_with_border));
            btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));

        }

       /* Intent intent=getActivity().getIntent();
        String totHrsFromPrvFrag=null;
        if(intent!=null){
            Bundle bundle=intent.getExtras();
            totHrsFromPrvFrag=bundle.getString("TotalHours","00:00");
            txt_total_hours.setText(totHrsFromPrvFrag);
        }*/

        if(!isCheckOut){
            getDiffBetweenTwoHours();
        }

        //Toast.makeText(context, "I am in Resume", Toast.LENGTH_SHORT).show();

    }

    public void setButtonText(){

        String checkIn=getCheckInButtonText(KEY_CHECKINTEXT);
        String checkOut=getCheckOutButtonText(KEY_CHECKOUTTEXT);

        btCheckIn.setText(checkIn);
        btCheckout.setText(checkOut);

    }
    public String getCheckInButtonText(String key){
       return preferenceHelper.getCheckInButtonText(key);
    }

    public String getCheckOutButtonText(String key){
        return preferenceHelper.getCheckOutText(key);
    }

    public void deleteUserAttendanceData(){

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(PreferenceHelper.PREFER_NAME,Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(KEY_CHECKINTIME).apply();
        sharedPreferences.edit().remove(KEY_CHECKINTEXT).apply();
        sharedPreferences.edit().remove(KEY_CHECKOUTTEXT).apply();
        sharedPreferences.edit().remove(KEY_TOTALHOURS).apply();

    }

    public void checkTodayMarkInOrNot()
    {
        getUserType = userAttendanceDao.getUserAttendanceType(CHECK_IN,Util.getTodaysDate(),Util.getUserMobileFromPref());
        if (getUserType != null && getUserType.size() > 0 && !getUserType.isEmpty()) {

            btCheckIn.setBackground(getResources().getDrawable(R.drawable.bg_grey_box_with_border));
            btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));
            setButtonText();
        }else {

            btCheckout.setBackground(getResources().getDrawable(R.drawable.bg_grey_box_with_border));
            btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
            btCheckout.setEnabled(false);

        }

        getCheckOut=userCheckOutDao.getCheckOutData(CHECK_OUT,Util.getTodaysDate(),Util.getUserMobileFromPref());
        if (getCheckOut != null && getCheckOut.size() > 0 && !getCheckOut.isEmpty())
        {
            btCheckout.setBackground(getResources().getDrawable(R.drawable.bg_grey_box_with_border));
            btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
            String checkInTime = getCheckOut.get(0).getTime();
            setButtonText();
            //tvCheckOutTime.setText(checkInTime);
            //tvCheckOutTime.setVisibility(View.VISIBLE);
            //txt_total_hours.setText(getCheckOut.get(0).getTotalHrs());
        }

    }

    public void deleteSharedPreferece()
    {
        getUserType = userAttendanceDao.getUserAttendanceType(CHECK_IN,Util.getTodaysDate(),Util.getUserMobileFromPref());
        if(getUserType != null && getUserType.size() > 0 && !getUserType.isEmpty()) {

        }else {
            deleteUserAttendanceData();
        }

    }




}


