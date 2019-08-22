package com.platform.view.fragments;

import android.app.AlarmManager;
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
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
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
    TextView tvCheckInTime;
    TextView tvCheckOutTime;
    TextView tvAttendanceDetails;
    Button btCheckIn;
    Button btCheckout;

    private RelativeLayout lyEvents;
    private RelativeLayout lyTasks;
    private RelativeLayout lyAttendance;
    private RelativeLayout lyLeaves;

    private PlannerFragmentPresenter plannerFragmentPresenter;
    private RelativeLayout progressBarLayout;
    private ProgressBar progressBar;
    public static ArrayList<Integer> leaveBackground = new ArrayList<>();
    private GPSTracker gpsTracker;
    private String strLat, strLong;
    private String strAdd = "";
    private Long millis = null;
    private CharSequence time="",checkOutTime="",currentTime,checkInTime;
    private UserAttendanceDao userAttendanceDao;
    private UserCheckOutDao userCheckOutDao;
    List<AttendaceData> attendaceDataList;
    public String todayAsString;
    private int year, month;
    private String CHECK_IN = "checkin";
    private String CHECK_OUT = "checkout";
    private String attendaceId;

    String availableOnServer,totalHours,totalHrs="",totalMin="";
    // private SubmitAttendanceFragmentPresenter submitAttendanceFragmentPresenter;
    private Context context;
    List<AttendaceData> getUserType;
    //List<AttendaceData> getCheckOut;
    List<AttendaceCheckOut> getCheckOut;
    private boolean checkOutStatus=false;
    private PreferenceHelper preferenceHelper;
    private AlarmManager alarmManager;
    private TextView txt_timer,txt_total_hours;

    BroadcastReceiver getHoursDifferenceReceiver;
    public ShowTimerService showTimerService;
    private static boolean mBound;
    private boolean showTotalHours=false,isCheckOut=false;

    private static String KEY_TOTALHOURS="totalHours";
    private static String KEY_CHECKINTIME="checkInTime";

    public static  String KEY_CHECKINTEXT="checkInText";
    public static  String KEY_CHECKOUTTEXT="checkOutText";

    public static String KEY_TOTAL_HRS_CHEKOUT="totalHoursCheckOut";
    public static String KEY_ISCHECKOUT="isCheckOut";
    private String checkInText;
    private String checkOutText;
    private String CheckInDate="",CheckOutDate="",totalHoursFromTwonDate="";
    private String prefCheckInTime,todayCheckInTime,todayCheckOutTime,userServerCheckInTime,userServerCheckOutTime;
    private attendanceData.CheckIn checkInObj;
    private attendanceData.CheckOut checkOutObj;
    private Location location;
    String db_chkin_time="",db_chkout_time="";
    public static Timer timer;
    BroadcastReceiver _broadcastReceiver;
    private final SimpleDateFormat _sdfWatchTime = new SimpleDateFormat("HH:mm");
    private long checkInDateInLong,checkOutDateInLong;
    private TextView txt_total_hours_view;

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
        // start time
        return plannerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        preferenceHelper=new PreferenceHelper(getActivity());
        userAttendanceDao=DatabaseManager.getDBInstance(getActivity()).getAttendaceSchema();
        userCheckOutDao=DatabaseManager.getDBInstance(getActivity()).getCheckOutAttendaceSchema();

        checkInText=getString(R.string.check_in_at);
        checkOutText=getString(R.string.check_out_it);

        timer=new Timer();
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
       /* alarmManager= (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar=Util.setHours(17,03);
        if(calendar.getTimeInMillis()<System.currentTimeMillis()){

        }else {
                Intent callAlarmReceiver=new Intent(context,AttendanceService.class);
                callAlarmReceiver.putExtra("requestJson","");
                callAlarmReceiver.putExtra("messenger",new Messenger(new MessageHandler()));
                PendingIntent pendingIntent=PendingIntent.getService(getActivity(),0,callAlarmReceiver,PendingIntent.FLAG_CANCEL_CURRENT);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

        }*/

        btCheckIn = plannerView.findViewById(R.id.bt_check_in);
        btCheckout = plannerView.findViewById(R.id.bt_checkout);


        //tvCheckInTime = plannerView.findViewById(R.id.tv_check_in_time);
        //tvCheckOutTime = plannerView.findViewById(R.id.tv_check_out_time);
        tvAttendanceDetails = plannerView.findViewById(R.id.tv_attendance_details);
        txt_total_hours=plannerView.findViewById(R.id.iv_total_hours);

        txt_total_hours_view=plannerView.findViewById(R.id.tv_date);
        txt_total_hours_view.setText(getString(R.string.total_hours));

        txt_timer=plannerView.findViewById(R.id.txt_timer);

        if(!isCheckOut){
            getDiffBetweenTwoHours();
        }

        initCardView();
        deleteSharedPreferece();
    }

    private void initCardView() {
        if (getActivity() == null) {
            return;
        }



        progressBarLayout = plannerView.findViewById(R.id.profile_act_progress_bar);
        progressBar = plannerView.findViewById(R.id.pb_profile_act);


        checkTodayMarkInOrNot();

        // comment by deepak on 1-07-2019
        plannerFragmentPresenter = new PlannerFragmentPresenter(this);

        lyEvents= plannerView.findViewById(R.id.ly_events);
        lyTasks= plannerView.findViewById(R.id.ly_task);
        lyAttendance= plannerView.findViewById(R.id.ly_attendance);
        lyLeaves= plannerView.findViewById(R.id.ly_leave);

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
    public void onDestroy() {
        super.onDestroy();
        if (plannerView != null) {
            plannerView = null;
        }
        /*if (timer!=null) {
            timer.cancel();
        }*/
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
                        checkInObj=attendanceData.get(i).getCheckIn();
                        checkOutObj=attendanceData.get(i).getCheckOut();

                        todayCheckInTime=checkInObj.getTime();
                        todayCheckOutTime=checkOutObj.getTime();

                    }

                    getUserCheckInTimeFromServer(todayCheckInTime);
                    getUserCheckOutTimeFromServer(todayCheckOutTime);


                    if(availableOnServer!=null&&
                            !availableOnServer.isEmpty())
                    {
                        //preferenceHelper.insertString(Constants.KEY_ATTENDANCDE,availableOnServer);
                        Log.i("AttendanceId", "111" + availableOnServer);
                    }

                    updateButtonTextIfUserSaved();
                    if(todayCheckInTime!=null){
                        // start timer form current dsate
                        if (todayCheckOutTime!=null&&!todayCheckOutTime.isEmpty()){
                            if (timer!=null){
                                timer.cancel();
                                timer =null;
                            }

                        }

                        if(timer!=null){
                            updateTime(Util.getDateFromTimestamp(Long.valueOf(todayCheckInTime),"yyyy/MM/dd HH:mm:ss a"),"");
                        }
                    }




//((ViewHolderAttendace)holder).
                    break;
                case Constants.Planner.EVENTS_KEY:
                    if(obj.getEventData()!=null && obj.getEventData().size()>0){
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
                    if(obj.getLeave()!=null){
                        CircularProgressBar pbTotal = plannerView.findViewById(R.id.pb_total);
                        pbTotal.setProgress((obj.getLeave().getTotal()/obj.getLeave().getTotal())*100);
                        TextView tvTotal=plannerView.findViewById(R.id.tv_total);
                        tvTotal.setText(obj.getLeave().getTotal().toString());

                        CircularProgressBar pbUsed = plannerView.findViewById(R.id.pb_used);
                        pbUsed.setProgress((obj.getLeave().getUsed()/obj.getLeave().getTotal())*100);
                        TextView tvUsed=plannerView.findViewById(R.id.tv_used);
                        tvUsed.setText(obj.getLeave().getUsed().toString());

                        CircularProgressBar pbBalance = plannerView.findViewById(R.id.pb_balance);
                        pbBalance.setProgress((obj.getLeave().getBalance()/obj.getLeave().getTotal())*100);
                        TextView tvbalance=plannerView.findViewById(R.id.tv_balance);
                        tvbalance.setText(obj.getLeave().getBalance().toString());
                    }
                    break;
            }
        }
    }

    private String getUserCheckOutTimeFromServer(String todayCheckOutTime) {

        try
        {
            String convertedDate=Util.getLongDateInString(Long.valueOf(todayCheckOutTime),"yyyy/MM/dd HH:mm:ss aa");
            // 2019/08/01 12:52:59
            Log.i("CheckOutDate","111"+convertedDate);

            String splitDate[]=convertedDate.split(" ");
            String hh=splitDate[0];
            String mm=splitDate[1];
            String timeFormat=splitDate[2];

            String splitMin[]=mm.split(":");
            String hours=splitMin[0];
            String minutes=splitMin[1];

            userServerCheckOutTime= hours+":"+minutes+" "+timeFormat;

        }catch (Exception e){

        }
        return userServerCheckOutTime;

    }

    private String getUserCheckInTimeFromServer(String todayCheckInTime) {

        try
        {
            String convertedDate=Util.getLongDateInString(Long.valueOf(todayCheckInTime),"yyyy/MM/dd HH:mm:ss aa");
            // 2019/08/01 12:52:59

            Log.i("CheckInDate","111"+convertedDate);
            String splitDate[]=convertedDate.split(" ");
            String hh=splitDate[0];
            String mm=splitDate[1];
            String timeFormat=splitDate[2];

            String splitMin[]=mm.split(":");
            String hours=splitMin[0];
            String minutes=splitMin[1];

            userServerCheckInTime= hours+":"+minutes+" "+timeFormat;

        }catch (Exception e){

        }
        return userServerCheckInTime;

    }

    public void setAttendaceView () {


            btCheckIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //tvCheckInTime.setText(DateFormat.format(Constants.TIME_FORMAT, new Date().getTime()));
                    //tvCheckInTime.setVisibility(View.VISIBLE);
                /*btCheckIn.setBackground(getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));*/

                    // 12 hrs format



                    //start a service
                    //startTimerService();


                    // save check in time in DB in both way






                    getUserType = userAttendanceDao.getUserAttendanceType(CHECK_IN,Util.getTodaysDate(),Util.getUserMobileFromPref());
                    if (getUserType.size() > 0 && !getUserType.isEmpty() && getUserType != null) {
                        Util.showToast("User already checked in",getActivity());

                        //statTimer();
                        //startTimerService();
                        //Util.showDelayInCheckInDialog(getActivity(),getResources().getString(R.string.check_in_msg),getResources().getString(R.string.delayin_checkin_title),true);

                    } else if(availableOnServer!=null&&!availableOnServer.isEmpty()){
                        Util.showToast("User already checked in",getActivity());

                    }else {

                        markCheckIn();
                    }

                }
            });
            btCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getCheckOut=userCheckOutDao.getCheckOutData(CHECK_OUT,Util.getTodaysDate(),Util.getUserMobileFromPref());
                    if(getCheckOut != null && getCheckOut.size() > 0 && !getCheckOut.isEmpty())
                    {
                        Util.showToast("User already checked out",getActivity());
                    } else {
                        markOut();
                    }

                }
            });

            tvAttendanceDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
                    intent.putExtra(Constants.Planner.KEY_IS_DASHBOARD, false);
                    intent.putExtra("title", getActivity().getString(R.string.attendance));
                    intent.putExtra("switch_fragments", "AttendancePlannerFragment");
                    intent.putExtra("TotalHours",txt_total_hours.getText().toString());
                    intent.putExtra("userAvailable",availableOnServer);
                    intent.putExtra("userCheckInTime",userServerCheckInTime);
                    intent.putExtra("userCheckOutTime",userServerCheckOutTime);

                    getActivity().startActivity(intent);
                }
            });

        }

        private void markOut () {

            checkOutTime = DateFormat.format("HH:mm aa", new Date().getTime());
            millis = getLongFromDate();

            gpsTracker = new GPSTracker(getActivity());
            if (gpsTracker.isGPSEnabled(getActivity(), this)) {
                ///getCompleteAddressString(Double.parseDouble(strLat), Double.parseDouble(strLong));


                if (!Util.isConnected(getActivity())) {
                    // offline storage
                    //userAttendanceDao=DatabaseManager.getDBInstance(getActivity()).getAttendaceSchema();
                    // offline save
                    getCheckOut = userCheckOutDao.getCheckOutData(CHECK_OUT, Util.getTodaysDate(), Util.getUserMobileFromPref());
                    if (getCheckOut.size() > 0 && !getCheckOut.isEmpty() && getCheckOut != null) {
                        Toast.makeText(getActivity(), "Already check out", Toast.LENGTH_LONG).show();
                        btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
                        btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
                        //tvCheckOutTime.setText(getCheckOut.get(0).getTime());
                    } else {

                        if(getLocation()!=null){

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

                            // old code
                           /* try {
                                attendaceData.setTotalHrs(getTotalHours());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
*/
                            attendaceData.setAttendanceFormattedDate(Util.getTodaysDate());
                            attendaceData.setMobileNumber(Util.getUserMobileFromPref());
                            //tvCheckOutTime.setText(checkOutTime);
                           /* try {
                                txt_total_hours.setText(getTotalHours());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }*/

                            userCheckOutDao.insert(attendaceData);
                            btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
                            btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
                            checkOutText=checkOutText+" "+checkOutTime;
                            btCheckout.setText(checkOutText);
                            preferenceHelper.saveCheckOutText(KEY_CHECKOUTTEXT,checkOutText);

                            String pattern = "yyyy/MM/dd HH:mm:ss a";
                            SimpleDateFormat df = new SimpleDateFormat(pattern,Locale.getDefault());
                            Date today = Calendar.getInstance().getTime();
                            CheckOutDate = df.format(today);

                            if(timer!=null){

                                if(CheckInDate.isEmpty()){
                                    CheckInDate=Util.getDateFromTimestamp(checkInDateInLong,"yyyy/MM/dd HH:mm:ss a");
                                }
                                updateTime(CheckInDate,CheckOutDate);
                            }
                            else {
                                Log.i("TotalHours","333"+totalHoursFromTwonDate);
                            }
                            txt_total_hours.setText(totalHoursFromTwonDate);
                            /*if(!isCheckOut){
                                getDiffBetweenTwoHours();
                            }
                            isCheckOut=true;
                            //setButtonText();
                            preferenceHelper.totalHours(KEY_TOTALHOURS,false);*/
                            Util.showToast(getResources().getString(R.string.check_out_msg),getActivity());
                            Log.i("OfflineMarkOutData", "111" + attendaceData);

                        }
                        else {
                            Util.showToast("Unable to get location",getActivity());
                        }

                    }


                } else {

                    if(getLocation()!=null){

                        Util.showSimpleProgressDialog(getActivity(), "Attendance", "Loading...", false);
                        attendaceId = userAttendanceDao.getUserId(Util.getTodaysDate(), Util.getUserMobileFromPref());
                        String diffInCheckInandCheckout="";
                        try {
                            diffInCheckInandCheckout=totalHoursAfterCheckOut();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // in case clear data user user available server variable

                        if(attendaceId!=null){
                            SubmitAttendanceFragmentPresenter submitAttendanceFragmentPresenter = new SubmitAttendanceFragmentPresenter(this);
                            submitAttendanceFragmentPresenter.markOutAttendance(attendaceId,CHECK_OUT, millis, strLat, strLong,txt_total_hours.getText().toString());

                        }else {
                            SubmitAttendanceFragmentPresenter submitAttendanceFragmentPresenter = new SubmitAttendanceFragmentPresenter(this);
                            submitAttendanceFragmentPresenter.markOutAttendance(availableOnServer,CHECK_OUT, millis, strLat, strLong,txt_total_hours.getText().toString());
                        }

                    }else {
                        Util.showToast("Unable to get location",getActivity());
                    }

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
                if(Util.isConnected(getContext())) {
                    Intent intentEventList = new Intent(getActivity(), PlannerDetailActivity.class);
                    intentEventList.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.EVENTS_LABEL);
                    getActivity().startActivity(intentEventList);
                }else{
                    Util.showToast(getString(R.string.msg_no_network), getContext());
                }
            }
        });
        btAddEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Util.isConnected(getContext())) {
                    Intent intentCreateEvent = new Intent(getActivity(), CreateEventTaskActivity.class);
                    intentCreateEvent.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.EVENTS_LABEL);
                    getActivity().startActivity(intentCreateEvent);
                }else{
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
                if(Util.isConnected(getContext())) {
                    Intent intentEventList = new Intent(getActivity(), PlannerDetailActivity.class);
                    intentEventList.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.TASKS_LABEL);
                    getActivity().startActivity(intentEventList);
                }else{
                    Util.showToast(getString(R.string.msg_no_network), getContext());
                }
            }
        });
        btAddTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Util.isConnected(getContext())) {
                    Intent intentCreateEvent = new Intent(getActivity(), CreateEventTaskActivity.class);
                    intentCreateEvent.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.TASKS_LABEL);
                    getActivity().startActivity(intentCreateEvent);
                }else{
                    Util.showToast(getString(R.string.msg_no_network), getContext());
                }
            }
        });

    }

    public void setLeaveView() {
        TextView tvCheckLeaveDetailsLink = plannerView.findViewById(R.id.tv_link_check_leaves);
        TextView imgClickAddLeaves = plannerView.findViewById(R.id.fab_add_leaves);

        tvCheckLeaveDetailsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Util.isConnected(getContext())) {
                    Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
                    intent.putExtra("title", getActivity().getString(R.string.leave));
                    intent.putExtra("switch_fragments", "LeaveDetailsFragment");
                    getActivity().startActivity(intent);
                }else{
                    Util.showToast(getString(R.string.msg_no_network), getContext());
                }
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
                getActivity().startActivity(intent);
            }
        });
    }

        public void markCheckIn () {

            time = DateFormat.format("HH:mm aa",new Date().getTime());
            millis = getLongFromDate();

            gpsTracker = new GPSTracker(getActivity());
            if (gpsTracker.isGPSEnabled(getActivity(), this)) {

                if (!Util.isConnected(getActivity())) {
                    getUserType = userAttendanceDao.getUserAttendanceType(CHECK_IN, Util.getTodaysDate(), Util.getUserMobileFromPref());
                    if (getUserType.size() > 0 && !getUserType.isEmpty() && getUserType != null) {
                        Toast.makeText(getActivity(), "Already check in", Toast.LENGTH_LONG).show();



                    } else {

                        if (getLocation() != null) {

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

                            btCheckIn.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
                            btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));
                            btCheckIn.setText(checkInText+" "+time);
                            //tvCheckInTime.setText(time);
                            checkInTime = DateFormat.format(Constants.TIME_FORMAT_, new Date().getTime());

                            preferenceHelper.saveCheckInTime(KEY_CHECKINTIME,checkInTime);
                            preferenceHelper.totalHours(KEY_TOTALHOURS,true);
                            checkInText = checkInText+" "+time;
                            preferenceHelper.saveCheckInButtonText(KEY_CHECKINTEXT, checkInText);
                            //setButtonText();
                            // show check in Time

                            String pattern = "yyyy/MM/dd HH:mm:ss a";
                            SimpleDateFormat df = new SimpleDateFormat(pattern,Locale.getDefault());
                            Date today = Calendar.getInstance().getTime();
                            CheckInDate = df.format(today);
                            updateTime(CheckInDate,"");

                            enableCheckOut();
                            Util.showToast(getResources().getString(R.string.check_in_msg), getActivity());
                        } else {
                            Util.showToast("Unable to get location", getActivity());
                        }

                    }

                } else {

                    if (getLocation()!= null) {

                        SubmitAttendanceFragmentPresenter submitAttendanceFragmentPresenter = new SubmitAttendanceFragmentPresenter(this);
                        Util.showSimpleProgressDialog(getActivity(), "Attendance", "Loading...", false);
                        submitAttendanceFragmentPresenter.markAttendace(CHECK_IN,millis,time.toString(), "", strLat, strLong,strAdd);
                    } else {
                        Util.showToast("Unable to get location", getActivity());
                    }
                }
            } else {
                gpsTracker.showSettingsAlert();
            }

            if(!isCheckOut){
                getDiffBetweenTwoHours();
            }
        }

    private void startTimerService() {

        // get check in time 24 hrs;
        Intent timerService=new Intent(getActivity(),ShowTimerService.class);
        timerService.putExtra("CheckInTime",(String)time);
        getActivity().startService(timerService);
        getActivity().bindService(timerService,mConnection,Context.BIND_AUTO_CREATE);
        mBound=true;
    }

    private void stopTimerService(){
        if (mBound) {
            getActivity().unbindService(mConnection);
            mBound = false;
        }
        Intent intent = new Intent(getActivity(),
                ShowTimerService.class);
        getActivity().stopService(intent);

    }

    public Location  getLocation () {

            location = gpsTracker.getLocation();
            if (location != null) {
                strLat = String.valueOf(location.getLatitude());
                strLong = String.valueOf(location.getLongitude());
            } else {
                Log.i("latLong", "222" + strLat + ":-" + strLong);


            }
            return location;
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
            String pattern = "yyyy/MM/dd HH:mm:ss a";

// Create an instance of SimpleDateFormat used for formatting
// the string representation of date according to the chosen pattern
            SimpleDateFormat df = new SimpleDateFormat(pattern,Locale.getDefault());

// Get the today date using Calendar object.
            Date today = Calendar.getInstance().getTime();


// Using DateFormat format method we can create a string
// representation of a date with the defined format.
            String todayAsString = df.format(today);

// Print it!
            System.out.println("Today is: " + todayAsString);

            try {
                millis = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ",Locale.getDefault()).parse(todayAsString).getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return millis;
        }

        public void checkInResponse(String response){
            String attendanceId;
            int status;
            Util.removeSimpleProgressDialog();
            getUserType = userAttendanceDao.getUserAttendanceType(CHECK_IN,Util.getTodaysDate(), Util.getUserMobileFromPref());
            if (getUserType != null && getUserType.size() > 0 && !getUserType.isEmpty()) {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();

            } else {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    String msg=jsonObject.getString("message");
                    if (status ==300) {
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    } else {

                        // start a timer and calculate date difference

                        String pattern = "yyyy/MM/dd HH:mm:ss a";
                        SimpleDateFormat df = new SimpleDateFormat(pattern,Locale.getDefault());
                        Date today = Calendar.getInstance().getTime();
                        CheckInDate = df.format(today);
                        updateTime(CheckInDate,"");

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

                        btCheckIn.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));

                        btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));

                        //tvCheckInTime.set
                        // Text(time);

                        checkInTime=DateFormat.format(Constants.TIME_FORMAT_,new Date().getTime());
                        preferenceHelper.saveCheckInTime(KEY_CHECKINTIME,checkInTime);
                        preferenceHelper.totalHours(KEY_TOTALHOURS,true);
                        checkInText=checkInText+" "+time;
                        btCheckIn.setText(checkInText);
                        preferenceHelper.saveCheckInButtonText(KEY_CHECKINTEXT,checkInText);
                        //setButtonText();
                        //statTimer();
                        enableCheckOut();
                        Util.showToast(getResources().getString(R.string.check_in_msg),getActivity());
                        // start a timer here


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

        public void checkOutResponse (String response)
        {
            Log.i("checkOut", "111" + response);

            int status =0;
            String id = "";
            try {
                JSONObject jsonObject=new JSONObject(response);
                status=jsonObject.getInt("status");
                JSONObject jsonData=jsonObject.getJSONObject("data");
                id=jsonData.getString("_id");

            } catch (JSONException e) {
                e.printStackTrace();
            }



            Util.removeSimpleProgressDialog();
            getCheckOut = userCheckOutDao.getCheckOutData(CHECK_OUT, Util.getTodaysDate(), Util.getUserMobileFromPref());
            if (getCheckOut != null && getCheckOut.size() > 0 && !getCheckOut.isEmpty()) {
                Toast.makeText(getActivity(), "User Already Check Out", Toast.LENGTH_LONG).show();
            } else if(status==300){
                   Util.showToast("User Already check out",getActivity());
                }
                else
                {

                    String pattern = "yyyy/MM/dd HH:mm:ss a";
                    SimpleDateFormat df = new SimpleDateFormat(pattern,Locale.getDefault());
                    Date today = Calendar.getInstance().getTime();
                    CheckOutDate = df.format(today);

                    if(timer!=null){
                        updateTime(CheckInDate,CheckOutDate);
                    }
                    else {
                        Log.i("TotalHours","333"+totalHoursFromTwonDate);
                    }

                AttendaceCheckOut attendaceData = new AttendaceCheckOut();
                attendaceData.setUid(id);
                Double lat = Double.parseDouble(strLat);
                Double log = Double.parseDouble(strLong);

                attendaceData.setLatitude(lat);
                attendaceData.setLongitude(log);
                attendaceData.setAddress(strAdd);
                attendaceData.setAttendaceDate(millis);
                attendaceData.setAttendanceType(CHECK_OUT);
                attendaceData.setTime(String.valueOf(checkOutTime));
                attendaceData.setSync(true);
                attendaceData.setTotalHrs(totalHoursFromTwonDate);
                txt_total_hours.setText(totalHoursFromTwonDate);

                // old code
               /* try {
                    attendaceData.setTotalHrs(getTotalHours());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    txt_total_hours.setText(getTotalHours());
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/

                attendaceData.setAttendanceFormattedDate(Util.getTodaysDate());
                attendaceData.setMobileNumber(Util.getUserMobileFromPref());

                try {

                    userCheckOutDao.insert(attendaceData);
                    btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
                    btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
                    checkOutText=checkOutText+" "+checkOutTime;
                    btCheckout.setText(checkOutText);
                    preferenceHelper.saveCheckOutText(KEY_CHECKOUTTEXT,checkOutText);
                    //setButtonText();
                    // old code
                   /* if(!isCheckOut){
                        getDiffBetweenTwoHours();
                    }
                    isCheckOut=true;
                    preferenceHelper.totalHours(KEY_TOTALHOURS,false);*/
                    Util.showToast(getResources().getString(R.string.check_out_msg),getActivity());
                   /* preferenceHelper.isCheckOut(KEY_ISCHECKOUT,true);
                    try {
                        String checkOutTotalHours=totalHoursAfterCheckOut();
                        if(checkOutTotalHours!=null){
                            preferenceHelper.insertTotalHoursAfterCheckOut(KEY_TOTAL_HRS_CHEKOUT,checkOutTotalHours);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
*/
                    //tvCheckOutTime.setText(checkOutTime);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }


            }

        }

        public void checkInError (String response){
            Toast.makeText(context, response, Toast.LENGTH_LONG).show();
            Log.i("ErrorData","111"+response);
            Util.removeSimpleProgressDialog();

        }
        public void checkOutError (String response){
            Toast.makeText(context, response, Toast.LENGTH_LONG).show();
            Util.removeSimpleProgressDialog();
        }

        public void enableCheckOut ()
        {
            btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));
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





    public ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {

            showTimerService = ((ShowTimerService.MyBinder)service).getService();
            mBound=true;

        }

        public void onServiceDisconnected(ComponentName className)
        {
            mBound=false;
        }
    };

    @Override
    public void onStart(){
        super.onStart();

       /* _broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context ctx, Intent intent) {

                if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0)

                    txt_total_hours.setText(_sdfWatchTime.format(new Date()));

            }
        };

        getActivity().registerReceiver(_broadcastReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));*/

    }

    @Override
    public void onStop() {
        super.onStop();
        /*if (_broadcastReceiver != null)
            getActivity().unregisterReceiver(_broadcastReceiver);*/
        //getActivity().unbindService(mConnection);
        //mBound=false;
    }


    public String getDiffBetweenTwoHours()
    {
        if(preferenceHelper.showTotalHours(KEY_TOTALHOURS)){
            try {
                totalHours=getTotalHours();
                //txt_total_hours.setText(totalHours);

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

        if(Util.isConnected(getContext())) {

            plannerFragmentPresenter = new PlannerFragmentPresenter(this);
            plannerFragmentPresenter.getPlannerData();

        }else {

            Util.showToast(getString(R.string.msg_no_network), this);
            // offline total hours calculation
            getUserType = userAttendanceDao.getUserAttendanceType(CHECK_IN,Util.getTodaysDate(),Util.getUserMobileFromPref());
            if(getUserType != null && getUserType.size() > 0 && !getUserType.isEmpty()) {

                btCheckIn.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
                btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));
                clearCheckInButtonText();
                btCheckIn.setText(checkInText+" "+getUserType.get(0).getTime());
                db_chkin_time=getUserType.get(0).getTime();
                time = getUserType.get(0).getTime();
                userServerCheckInTime=(String)time;
                checkInDateInLong=getUserType.get(0).getAttendaceDate();

                enableCheckOut();
            }

            getCheckOut=userCheckOutDao.getCheckOutData(CHECK_OUT,Util.getTodaysDate(),Util.getUserMobileFromPref());
            if (getCheckOut != null && getCheckOut.size() > 0 && !getCheckOut.isEmpty())
            {
                //btCheckout.setBackgroundResource(R.drawable.bg_grey_box_with_border);
                //btCheckout.setBackgroundColor(getResources().getColor(R.color.card_gray_bg));
                //btCheckout.setBackground(getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                /*btCheckout.getBackground().setColorFilter(getResources().getColor(R.color.card_gray_bg), PorterDuff.Mode.MULTIPLY);*/
                btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
                btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
                clearCheckOutButtonText();
                btCheckout.setText(checkOutText+" "+getCheckOut.get(0).getTime());
                db_chkout_time=getCheckOut.get(0).getTime();
                checkOutTime=getCheckOut.get(0).getTime();
                userServerCheckOutTime=(String)checkOutTime;
                checkOutDateInLong=getCheckOut.get(0).getAttendaceDate();
                String totalHrs=getCheckOut.get(0).getTotalHrs();
                txt_total_hours.setText(totalHrs);
            }
            //txt_total_hours.setText(totalHoursFromTwonDate);
            // show total hrs

            if(userServerCheckInTime!=null){
                // start timer form current dsate
                if (userServerCheckOutTime!=null){
                    if (timer!=null){
                        timer.cancel();
                        timer =null;
                        // stop the timer
                    }

                }

                String totalHours=calculateTotalHours((String)time,(String)checkOutTime);
                txt_total_hours.setText(totalHours);

                    if(timer!=null){
                        updateTime(Util.getDateFromTimestamp(Long.valueOf(checkInDateInLong),"yyyy/MM/dd HH:mm:ss a"),"");

                    }
            }


        }



    }

    private void updateButtonTextIfUserSaved() {

        getUserType = userAttendanceDao.getUserAttendanceType(CHECK_IN,Util.getTodaysDate(),Util.getUserMobileFromPref());
        if(getUserType != null && getUserType.size() > 0 && !getUserType.isEmpty()) {

            btCheckIn.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
            btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));
            clearCheckInButtonText();
            btCheckIn.setText(checkInText+" "+getUserType.get(0).getTime());
            db_chkin_time=getUserType.get(0).getTime();
            time = getUserType.get(0).getTime();
            enableCheckOut();

            //set background
           /* btCheckout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btCheckout.setTextColor(getResources().getColor(R.color.white));
            btCheckout.setEnabled(true);*/
        }
        getCheckOut=userCheckOutDao.getCheckOutData(CHECK_OUT,Util.getTodaysDate(),Util.getUserMobileFromPref());
        if (getCheckOut != null && getCheckOut.size() > 0 && !getCheckOut.isEmpty())
        {

            btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
            btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
            clearCheckOutButtonText();
            btCheckout.setText(checkOutText+" "+getCheckOut.get(0).getTime());
            db_chkout_time=getCheckOut.get(0).getTime();
            checkOutTime=getCheckOut.get(0).getTime();

            //String totalHrs=getCheckOut.get(0).getTotalHrs();
            //txt_total_hours.setText(totalHrs);
            //
        }

        //  check that user time is available on server
        if(userServerCheckInTime!=null){
            makeCheckInButtonGray();
            clearCheckInButtonText();
            enableCheckOut();
            btCheckIn.setText(checkInText+" "+userServerCheckInTime);
            time = userServerCheckInTime;
        }
        if(userServerCheckOutTime!=null){
            makeCheckOutButtonGray();
            clearCheckOutButtonText();
            btCheckout.setText(checkOutText+" "+userServerCheckOutTime);
            checkOutTime=userServerCheckOutTime;
        }

        if(availableOnServer!=null&& !availableOnServer.isEmpty()){
            //String totalHrs=calculateTotalHours(userServerCheckInTime,userServerCheckOutTime);
            //txt_total_hours.setText(totalHrs);

        }else {
            //String totalHrs=calculateTotalHours(db_chkin_time,db_chkout_time);
            //txt_total_hours.setText(totalHrs);
        }

        //txt_total_hours.setText(totalHoursFromTwonDate);
           String totalHours=calculateTotalHours((String)time,(String)checkOutTime);
           txt_total_hours.setText(totalHours);


      /*  if(timer!=null){
            timer.cancel();
            timer=null;
            txt_total_hours.setText(totalHoursFromTwonDate);
        }*/



        /*if(deleteUserAttendanceData()){
            String totalHrs=showTotalHrsAfterClearData(userServerCheckInTime,userServerCheckOutTime);
            txt_total_hours.setText(totalHrs);
        }else {
            if(!isCheckOut){
                getDiffBetweenTwoHours();
            }

        }
*/
        // show total hours after user clear data
    }

    private String calculateTotalHours(String userServerCheckInTime, String userServerCheckOutTime) {
        int hours = 0,min=0;
        long ss=0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm aa");
        Date startDate = null;
        try {
            startDate = simpleDateFormat.parse(userServerCheckInTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date endDate = null;
        try {

            if(userServerCheckOutTime==null||userServerCheckOutTime==""){
                Date d=new Date();
                SimpleDateFormat sdf=new SimpleDateFormat("HH:mm aa");
                userServerCheckOutTime = sdf.format(d);
                endDate=sdf.parse(userServerCheckOutTime);
            }else {
                endDate = simpleDateFormat.parse(userServerCheckOutTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(startDate!=null&&endDate!=null){
            long difference = endDate.getTime() - startDate.getTime();

            int days = (int) (difference / (1000*60*60*24));
            hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
            ss=(int)(difference / (24 * 60 * 60 * 1000));


            //hours = (hours < 0 ? -hours : hours);

            //hours = difference/(1000 * 60 * 60);
            //min = (difference/(1000*60)) % 60;

        }

        Log.i("======= Hours"," :: "+hours);
        Log.i("======= Minutes"," :: "+min);

        return  hours+":"+min+":"+ss;
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

    public boolean deleteUserAttendanceData(){

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(PreferenceHelper.PREFER_NAME,Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(KEY_CHECKINTIME).apply();
        sharedPreferences.edit().remove(KEY_CHECKINTEXT).apply();
        sharedPreferences.edit().remove(KEY_CHECKOUTTEXT).apply();
        sharedPreferences.edit().remove(KEY_TOTALHOURS).apply();
        sharedPreferences.edit().remove(KEY_ISCHECKOUT).apply();
        sharedPreferences.edit().remove(KEY_TOTAL_HRS_CHEKOUT).apply();
        return true;
    }

    public void checkTodayMarkInOrNot()
    {
        getUserType = userAttendanceDao.getUserAttendanceType(CHECK_IN,Util.getTodaysDate(),Util.getUserMobileFromPref());
        if (getUserType != null && getUserType.size() > 0 && !getUserType.isEmpty()) {
            setButtonText();
            btCheckIn.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
            btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));

        }else {

            btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
            btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
            btCheckout.setEnabled(false);

        }

        getCheckOut=userCheckOutDao.getCheckOutData(CHECK_OUT,Util.getTodaysDate(),Util.getUserMobileFromPref());
        if (getCheckOut != null && getCheckOut.size() > 0 && !getCheckOut.isEmpty())
        {
            btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
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

    public String  totalHoursAfterCheckOut() throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date startDate = null;


        try {

            startDate = simpleDateFormat.parse((String)time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date endDate = simpleDateFormat.parse((String)checkOutTime);
        if(endDate!=null&&startDate!=null){

            long difference = endDate.getTime() - startDate.getTime();
            int days = (int) (difference / (1000*60*60*24));
            int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
            Log.i("log_tag", "Hours: " + hours + ", Mins: " + min);
            totalHrs = String.valueOf(hours);
            totalMin = String.valueOf(min);

        }
        if(totalHrs.isEmpty()){
            totalHrs="0";
        }
        if(totalMin.isEmpty()){
            totalMin="0";
        }
        return totalHrs + " :" + totalMin;
    }


    public void makeCheckInButtonGray(){
        btCheckIn.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
        btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));
    }

    public void makeCheckOutButtonGray(){
        btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.button_gray_color));
        btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
    }

    public void makeCheckInButttonDisable(){
        btCheckIn.setEnabled(false);
    }
    public void makeCheckOutButttonDisable(){
        btCheckout.setEnabled(false);
    }

    public void clearCheckInButtonText(){
       btCheckIn.setText("");
    }
    public void clearCheckOutButtonText(){
        btCheckout.setText("");
    }


    public void updateTime(String checkIntime,String checkOutTime){



        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Message msg = mHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString(KEY_TOTALHOURS,calculateHoursFromTwoDate(checkIntime,checkOutTime));
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }

        },0,1000);
    }


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            totalHoursFromTwonDate= bundle.getString(KEY_TOTALHOURS);
            txt_total_hours.setText(totalHoursFromTwonDate);

        }
    };



    public String calculateHoursFromTwoDate(String checkInTime,String checkOutTime){

        int hours = 0,min=0;
        long ss=0;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss a");
        Date startDate = null;
        try {
            startDate = simpleDateFormat.parse(checkInTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date endDate = null;
        try {

            if(checkOutTime==null||checkOutTime==""){
                Date d=new Date();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss a");
                checkOutTime = sdf.format(d);
                endDate=sdf.parse(checkOutTime);
            }else {
                endDate = simpleDateFormat.parse(checkOutTime);
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

        return  hours+":"+min+":"+ss;
    }

}

