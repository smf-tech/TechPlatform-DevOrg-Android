package com.octopusbjsindia.view.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.octopusbjsindia.R;
import com.octopusbjsindia.dao.UserAttendanceDao;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.PlatformTaskListener;
import com.octopusbjsindia.models.attendance.AttendaceData;
import com.octopusbjsindia.models.home.Modules;
import com.octopusbjsindia.models.planner.SubmoduleData;
import com.octopusbjsindia.models.planner.attendanceData;
import com.octopusbjsindia.presenter.PlannerFragmentPresenter;

import com.octopusbjsindia.presenter.SubmitAttendanceFragmentPresenter;
import com.octopusbjsindia.receivers.ConnectivityReceiver;
import com.octopusbjsindia.utility.AppEvents;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GPSTracker;
import com.octopusbjsindia.utility.PreferenceHelper;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.CreateEventTaskActivity;
import com.octopusbjsindia.view.activities.GeneralActionsActivity;
import com.octopusbjsindia.view.activities.HomeActivity;
import com.octopusbjsindia.view.activities.LoginActivity;
import com.octopusbjsindia.view.activities.PlannerDetailActivity;
import com.octopusbjsindia.view.adapters.EventTaskListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PlannerFragment extends Fragment implements PlatformTaskListener,
        ConnectivityReceiver.ConnectivityReceiverListener {

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

     private String checkInText;
    private String checkOutText;
    private String prefCheckInTime;
    Timer timer;
    private final String TAG = this.getClass().getSimpleName();

    Activity mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = (Activity)context;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mContext != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) mContext).setActionBarTitle(title);
            ((HomeActivity) mContext).setSyncButtonVisibility(false);

            if ((boolean) getArguments().getSerializable("SHOW_BACK")) {
                ((HomeActivity) mContext).showBackArrow();
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
        context = mContext;
        preferenceHelper = new PreferenceHelper(mContext);
        userAttendanceDao = DatabaseManager.getDBInstance(mContext).getAttendaceSchema();

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
        if (Util.isConnected(getContext())) {
            ArrayList<AttendaceData> unsyncAttendanceList = (ArrayList<AttendaceData>) userAttendanceDao.
                    getUnsyncAttendance(false);
            if (unsyncAttendanceList.size() > 0) {
                Util.showSimpleProgressDialog(mContext, "Attendance", "Loading...", false);
                for (AttendaceData attendaceData : unsyncAttendanceList) {
                    if (attendaceData.getAttendanceType().equals(CHECK_OUT)) {

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
        if (mContext == null) {
            return;
        }

        progressBarLayout = plannerView.findViewById(R.id.profile_act_progress_bar);
        progressBar = plannerView.findViewById(R.id.pb_profile_act);

        plannerFragmentPresenter = new PlannerFragmentPresenter(this);

        lyEvents = plannerView.findViewById(R.id.ly_events);
        lyTasks = plannerView.findViewById(R.id.ly_task);
        lyAttendance = plannerView.findViewById(R.id.ly_attendance);
        lyLeaves = plannerView.findViewById(R.id.ly_leave);

        List<Modules> approveModules = DatabaseManager.getDBInstance(mContext.getApplicationContext())
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
        mContext.runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        mContext.runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public <T> void showNextScreen(T data) {

    }

    public void logOutUser() {
        // remove user related shared pref data
        Util.saveLoginObjectInPref("");
        try {
            Intent startMain = new Intent(mContext, LoginActivity.class);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startMain);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void showErrorMessage(String result) {
        mContext.runOnUiThread(() -> Util.showToast(result, this));
        setOfflineAttendance();
    }

    private void setOfflineAttendance() {
        String attendanceDate = Util.getDateFromTimestamp(new Date().getTime(), "yyyy-MM-dd");
        long attnDate = Util.dateTimeToTimeStamp(attendanceDate, "00:00");

        AttendaceData offlineCheckinAttendaceData = userAttendanceDao.getUserAttendace(attnDate, CHECK_IN);
        AttendaceData offlineCheckoutAttendaceData = userAttendanceDao.getUserAttendace(attnDate, CHECK_OUT);

        if (offlineCheckinAttendaceData != null && offlineCheckoutAttendaceData != null) {
            txt_total_hours.setText(calculateHoursFromTwoDate(offlineCheckinAttendaceData.getDate(),
                    offlineCheckoutAttendaceData.getDate()));

        } else if (offlineCheckinAttendaceData != null) {
            updateTime(offlineCheckinAttendaceData.getDate(), 0);
        }

        if (offlineCheckoutAttendaceData != null && offlineCheckoutAttendaceData.getDate() != null &&
                !offlineCheckoutAttendaceData.getDate().equals("")) {
            checkInText = "Check in at " + Util.getDateFromTimestamp
                    (offlineCheckinAttendaceData.getDate(), "hh:mm aa");
            btCheckIn.setText(checkInText);
            checkOutText = "Check out at " + Util.getDateFromTimestamp
                    (offlineCheckoutAttendaceData.getDate(), "hh:mm aa");
            btCheckout.setText(checkOutText);
            enableButton(false, false);
        } else {
            if (offlineCheckinAttendaceData != null && offlineCheckinAttendaceData.getDate() != null &&
                    !offlineCheckinAttendaceData.getDate().equals("")) {
                checkInText = "Check in at " + Util.getDateFromTimestamp
                        (offlineCheckinAttendaceData.getDate(), "hh:mm aa");
                btCheckIn.setText(checkInText);
                enableButton(false, true);
            } else {
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
//                        for (int i = 0; i < attendanceData.size(); i++) {

                        if (attendanceData.get(0).getCheckIn() != null) {
                            String attendanceDate = Util.getDateFromTimestamp(Long.parseLong(attendanceData.get(0).getCheckIn().getTime()), "yyyy-MM-dd");
                            long attnDate = Util.dateTimeToTimeStamp(attendanceDate, "00:00");

                            AttendaceData attendaceCheckinTemp = userAttendanceDao.getUserAttendace(attnDate, CHECK_IN);
                            if (attendaceCheckinTemp != null) {
                                userAttendanceDao.updateUserAttendace(attendanceData.get(0).get_id(), true, attnDate, CHECK_IN);
                            } else {
                                AttendaceData attendaceCheckinData = new AttendaceData();
                                attendaceCheckinData.setAttendanceId(attendanceData.get(0).get_id());
                                attendaceCheckinData.setLatitude(Double.parseDouble(attendanceData.get(0).getCheckIn().getLat()));
                                attendaceCheckinData.setLongitude(Double.parseDouble(attendanceData.get(0).getCheckIn().getLong()));
                                attendaceCheckinData.setAttendanceType(CHECK_IN);
                                attendaceCheckinData.setDate(Long.parseLong(attendanceData.get(0).getCheckIn().getTime()));
                                attendaceCheckinData.setAttendaceDate(attnDate);
                                userAttendanceDao.insert(attendaceCheckinData);
                            }
                        }

                        if (attendanceData.get(0).getCheckOut() != null) {
                            String attendanceCheckoutDate = Util.getDateFromTimestamp(Long.parseLong(attendanceData.get(0).getCheckOut().getTime()), "yyyy-MM-dd");
                            long attnCheckoutDate = Util.dateTimeToTimeStamp(attendanceCheckoutDate, "00:00");

                            AttendaceData attendaceCheckoutTemp = userAttendanceDao.getUserAttendace(attnCheckoutDate, CHECK_OUT);
                            if (attendaceCheckoutTemp != null) {
                                userAttendanceDao.updateUserAttendace(attendanceData.get(0).get_id(), true, attnCheckoutDate, CHECK_OUT);
                            } else {
                                AttendaceData attendaceCheckoutData = new AttendaceData();
                                attendaceCheckoutData.setAttendanceId(attendanceData.get(0).get_id());
                                attendaceCheckoutData.setLatitude(Double.parseDouble(attendanceData.get(0).getCheckOut().getLat()));
                                attendaceCheckoutData.setLongitude(Double.parseDouble(attendanceData.get(0).getCheckOut().getLong()));
                                attendaceCheckoutData.setAttendanceType(CHECK_OUT);
                                attendaceCheckoutData.setDate(Long.parseLong(attendanceData.get(0).getCheckOut().getTime()));
                                attendaceCheckoutData.setAttendaceDate(attnCheckoutDate);
                                userAttendanceDao.insert(attendaceCheckoutData);
                            }
                        }
//                        }
                        setOfflineAttendance();
                    } else {
                        //enable check-in and disable check-out
                        setOfflineAttendance();
                    }

                    break;
                case Constants.Planner.EVENTS_KEY:
                    if (obj.getEventData() != null && obj.getEventData().size() > 0) {
                        RecyclerView.LayoutManager mLayoutManagerEvent = new LinearLayoutManager(mContext.getApplicationContext());
                        EventTaskListAdapter eventTaskListAdapter = new EventTaskListAdapter(mContext,
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

                        RecyclerView.LayoutManager mLayoutManagerTask = new LinearLayoutManager(mContext.getApplicationContext());
                        EventTaskListAdapter taskListAdapter = new EventTaskListAdapter(mContext,
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
                if(Util.isConnected(getContext())) {
                    Intent intent = new Intent(mContext, GeneralActionsActivity.class);
                    intent.putExtra("title", mContext.getString(R.string.attendance));
                    intent.putExtra("switch_fragments", "AttendancePlannerFragment");
                    mContext.startActivity(intent);
                }else{
                    Util.showToast(mContext.getResources().getString(R.string.msg_no_network), mContext);
                }

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
                    Intent intentEventList = new Intent(mContext, PlannerDetailActivity.class);
                    intentEventList.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.EVENTS_LABEL);
                    mContext.startActivity(intentEventList);
                } else {
                    Util.showToast(getString(R.string.msg_no_network), getContext());
                }
            }
        });
        btAddEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isConnected(getContext())) {
                    Intent intentCreateEvent = new Intent(mContext, CreateEventTaskActivity.class);
                    intentCreateEvent.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.EVENTS_LABEL);
                    mContext.startActivity(intentCreateEvent);
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
                    Intent intentEventList = new Intent(mContext, PlannerDetailActivity.class);
                    intentEventList.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.TASKS_LABEL);
                    mContext.startActivity(intentEventList);
                } else {
                    Util.showToast(getString(R.string.msg_no_network), getContext());
                }
            }
        });
        btAddTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isConnected(getContext())) {
                    Intent intentCreateEvent = new Intent(mContext, CreateEventTaskActivity.class);
                    intentCreateEvent.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.TASKS_LABEL);
                    mContext.startActivity(intentCreateEvent);
                } else {
                    Util.showToast(getString(R.string.msg_no_network), getContext());
                }
            }
        });
    }

    public void setLeaveView() {
        TextView tvCheckLeaveDetailsLink = plannerView.findViewById(R.id.tv_link_check_leaves);
        TextView imgClickAddLeaves = plannerView.findViewById(R.id.fab_add_leaves);
        if (Util.isConnected(getContext())) {
            plannerView.findViewById(R.id.ly_lebal).setVisibility(View.VISIBLE);
            plannerView.findViewById(R.id.ly_circular_progress).setVisibility(View.VISIBLE);
        }
        tvCheckLeaveDetailsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isConnected(getContext())) {
                    Intent intent = new Intent(mContext, GeneralActionsActivity.class);
                    intent.putExtra("title", mContext.getString(R.string.leave));
                    intent.putExtra("switch_fragments", "LeaveDetailsFragment");
                    mContext.startActivity(intent);
                } else {
                    Util.showToast(getString(R.string.msg_no_network), getContext());
                }
            }
        });
        imgClickAddLeaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isConnected(getContext())) {
                    Intent intent = new Intent(mContext, GeneralActionsActivity.class);
                    intent.putExtra("title", mContext.getString(R.string.apply_leave));
                    intent.putExtra("isEdit", false);
                    intent.putExtra("apply_type", "Leave");
                    intent.putExtra("switch_fragments", "LeaveApplyFragment");
                    mContext.startActivity(intent);
                } else {
                    Util.showToast(getString(R.string.msg_no_network), getContext());
                }
            }
        });
    }

    private void enableButton(boolean b1, boolean b2) {
        if (b1 && !b2) {
            btCheckIn.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.colorPrimary));
            btCheckIn.setTextColor(mContext.getResources().getColor(R.color.white));
            btCheckIn.setEnabled(true);
            btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.button_gray_color));
            btCheckout.setTextColor(mContext.getResources().getColor(R.color.attendance_text_color));
            btCheckout.setEnabled(false);
        }
        if (!b1 && b2) {
            btCheckIn.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.button_gray_color));
            btCheckIn.setTextColor(mContext.getResources().getColor(R.color.attendance_text_color));
            btCheckIn.setEnabled(false);
            btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.colorPrimary));
            btCheckout.setTextColor(mContext.getResources().getColor(R.color.white));
            btCheckout.setEnabled(true);
        }
        if (!b2 && !b1) {
            btCheckIn.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.button_gray_color));
            btCheckIn.setTextColor(mContext.getResources().getColor(R.color.attendance_text_color));
            btCheckIn.setEnabled(false);
            btCheckout.setBackgroundTintList(ContextCompat.getColorStateList(mContext, R.color.button_gray_color));
            btCheckout.setTextColor(mContext.getResources().getColor(R.color.attendance_text_color));
            btCheckout.setEnabled(false);
        }
    }

    public void markCheckIn() {
        gpsTracker = new GPSTracker(mContext);
        if (gpsTracker.isGPSEnabled(mContext, this)) {
            Date currentDate = new Date();
            timeStamp = currentDate.getTime();
            Location location = gpsTracker.getLocation();
            if (location != null) {
                enableButton(false, true);
                attendaceCheckinData.setLatitude(location.getLatitude());
                attendaceCheckinData.setLongitude(location.getLongitude());
                attendaceCheckinData.setDate(timeStamp);
                attendaceCheckinData.setAttendanceType(CHECK_IN);

                String attendanceDate = Util.getDateFromTimestamp(new Date().getTime(), "yyyy-MM-dd");
                long attnDate = Util.dateTimeToTimeStamp(attendanceDate, "00:00");

                attendaceCheckinData.setAttendaceDate(attnDate);

                btCheckIn.setText("Check in at " + Util.getDateFromTimestamp(timeStamp, "hh:mm aa"));
                updateTime(timeStamp, 0);
                if (!Util.isConnected(mContext)) {
                    Toast.makeText(mContext, "Checked in offline.", Toast.LENGTH_LONG).show();
                    attendaceCheckinData.setSync(false);
                    userAttendanceDao.insert(attendaceCheckinData);
                } else {
                    attendaceCheckinData.setSync(true);
                    userAttendanceDao.insert(attendaceCheckinData);
                    SubmitAttendanceFragmentPresenter submitAttendanceFragmentPresenter = new SubmitAttendanceFragmentPresenter(this);
                    Util.showSimpleProgressDialog(mContext, "Attendance", "Loading...", false);
                    submitAttendanceFragmentPresenter.markAttendace(attendaceCheckinData);
                }
//                Util.showToast(getResources().getString(R.string.check_in_msg), mContext);
            } else {
                Util.showToast("Unable to get location", mContext);
            }

        } else {
            gpsTracker.showSettingsAlert();
        }
        if (!isCheckOut) {
            getDiffBetweenTwoHours();
        }
    }

    private void markOut() {
        gpsTracker = new GPSTracker(mContext);
        if (gpsTracker.isGPSEnabled(mContext, this)) {
            Date currentDate = new Date();
            timeStamp = currentDate.getTime();
            Location location = gpsTracker.getLocation();
            if (location != null) {
                enableButton(false, false);
                String attendanceDate = Util.getDateFromTimestamp(new Date().getTime(), "yyyy-MM-dd");
                long attnDate = Util.dateTimeToTimeStamp(attendanceDate, "00:00");
                AttendaceData attendaceCheckinData = userAttendanceDao.getUserAttendace(attnDate, CHECK_IN);

                //attendaceCheckoutData = new AttendaceData();
                attendaceCheckoutData.setAttendanceId(attendaceCheckinData.getAttendanceId());
                attendaceCheckoutData.setLatitude(location.getLatitude());
                attendaceCheckoutData.setLongitude(location.getLongitude());
                attendaceCheckoutData.setDate(timeStamp);
                attendaceCheckoutData.setAttendanceType(CHECK_OUT);
                attendaceCheckoutData.setAttendaceDate(attnDate);

                btCheckout.setText("Check out at " + Util.getDateFromTimestamp(timeStamp, "hh:mm aa"));
                updateTime(attendaceCheckinData.getDate(), timeStamp);
                if (!Util.isConnected(mContext)) {
                    Toast.makeText(mContext, "Checked out offline.", Toast.LENGTH_LONG).show();
                    attendaceCheckoutData.setSync(false);
                    userAttendanceDao.insert(attendaceCheckoutData);
                } else {
                    attendaceCheckoutData.setSync(true);
                    userAttendanceDao.insert(attendaceCheckoutData);
                    SubmitAttendanceFragmentPresenter submitAttendanceFragmentPresenter = new SubmitAttendanceFragmentPresenter(this);
                    Util.showSimpleProgressDialog(mContext, "Attendance", "Loading...", false);
                    submitAttendanceFragmentPresenter.markAttendace(attendaceCheckoutData);
                }
//                Util.showToast(getResources().getString(R.string.check_out_msg), mContext);
            } else {
                Util.showToast("Unable to get location", mContext);
            }

        } else {
            gpsTracker.showSettingsAlert();
        }
        if (!isCheckOut) {
            getDiffBetweenTwoHours();
        }
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

                    String attendanceDate = Util.getDateFromTimestamp(new Date().getTime(), "yyyy-MM-dd");
                    long attnDate = Util.dateTimeToTimeStamp(attendanceDate, "00:00");

                    userAttendanceDao.updateUserAttendace(attendanceId, true, attnDate, CHECK_IN);

                    Util.showToast(mContext.getResources().getString(R.string.check_in_msg), mContext);
                } else {
                    Toast.makeText(mContext, "Checked in offline.", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkOutResponse(String response, AttendaceData attendaceData) {
        Util.showToast(mContext.getResources().getString(R.string.check_out_msg), mContext);

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

        String attendanceDate = Util.getDateFromTimestamp(new Date().getTime(), "yyyy-MM-dd");
        long attnDate = Util.dateTimeToTimeStamp(attendanceDate, "00:00");

        userAttendanceDao.updateUserAttendace(attendanceId, true, attnDate, CHECK_OUT);

        Util.removeSimpleProgressDialog();

    }

    public void updateTime(long checkIntime, long checkOutTime) {
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

    public String calculateHoursFromTwoDate(long checkInTime, long checkOutTime) {

        int hours = 0, min = 0;
        long ss = 0;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss a");
        Date startDate = null;
        try {
            startDate = simpleDateFormat.parse(Util.getDateFromTimestamp(checkInTime, "yyyy/MM/dd HH:mm:ss a"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date endDate = null;
        try {

            if (checkOutTime == 0) {
                Date d = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss a");
                endDate = sdf.parse(sdf.format(d.getTime()));
//              endDate=sdf.parse(checkOutTime);
            } else {
                endDate = simpleDateFormat.parse(Util.getDateFromTimestamp(checkOutTime, "yyyy/MM/dd HH:mm:ss a"));
//              stop timer when got checkOutDate
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (startDate != null && endDate != null) {
            long difference = endDate.getTime() - startDate.getTime();

            int days = (int) (difference / (1000 * 60 * 60 * 24));
            hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            ss = (int) (difference / 1000 % 60);

        }

        return hours + ":" + min + ":" + ss;
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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if(isConnected) {
            Util.showToast("Yes", this);
        } else {
            Util.showToast("No", this);
        }

    }
}

