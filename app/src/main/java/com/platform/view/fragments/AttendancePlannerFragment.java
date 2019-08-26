package com.platform.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.platform.R;
import com.platform.models.attendance.Attendance;
import com.platform.models.attendance.AttendanceDateList;
import com.platform.models.attendance.CheckIn;
import com.platform.models.attendance.CheckOut;
import com.platform.models.attendance.Datum;
import com.platform.models.attendance.HolidayList;
import com.platform.models.attendance.MonthlyAttendance;
import com.platform.models.attendance.TeamAttendanceData;
import com.platform.presenter.MonthlyAttendanceFragmentPresenter;
import com.platform.utility.EventDecorator;
import com.platform.utility.Util;
import com.platform.view.adapters.TeamAttendanceAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AttendancePlannerFragment extends Fragment implements View.OnClickListener,
        OnDateSelectedListener, RadioGroup.OnCheckedChangeListener {

    private View plannerView;
    private boolean isMonth = true;

    private RelativeLayout lyCalender;
    private LinearLayout lyCheckInOutDashboard;
    private LinearLayout lvAttendanceStatus;
    private MaterialCalendarView calendarView;

    ArrayList<String> pendingList;
    ArrayList<String> approveList;
    ArrayList<String> rejectList;


    //private List<AttendanceStatus>attendanceStatusList;
    MonthlyAttendanceFragmentPresenter monthlyAttendanceFragmentPresenter;
    private int year, month, cmonth;

    public String todayAsString;
    private String calendarSelectedDate;

    ImageView ivUserImage;
    TextView tvName, tvRole, tvCheckInTime, tvCheckOutTime, tvStatus;

    private AttendanceDateList attendanceDateList;
    private List<AttendanceDateList> listDateWiseAttendace;
    private CheckIn checkIn;
    private CheckOut checkOut;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private ImageView ivCalendarMode;
    private RadioGroup radioGroup;
    private RecyclerView rvTeamList;
    List<TeamAttendanceData> teamAttendanceList;
    TeamAttendanceAdapter adapter;
    RelativeLayout lyMain;
    Date selectedDate;
    boolean isTeamList;


    public AttendancePlannerFragment() {
    }

    public void onCreate() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        plannerView = inflater.inflate(R.layout.fragment_attendance_planner, container, false);
        return plannerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        Bundle bundle = this.getArguments();

        lyCalender = plannerView.findViewById(R.id.ly_calender);
        lyCheckInOutDashboard = plannerView.findViewById(R.id.ly_check_in_out_dashboard);

        progressBar = plannerView.findViewById(R.id.pb_profile_act);
        progressBarLayout = plannerView.findViewById(R.id.profile_act_progress_bar);
        ivCalendarMode = plannerView.findViewById(R.id.iv_calendar_mode);
        calendarView = plannerView.findViewById(R.id.calendarView);
        calendarView.state().edit().setMaximumDate(Calendar.getInstance().getTime()).commit();

        ivCalendarMode.setOnClickListener(this);

        ivUserImage = plannerView.findViewById(R.id.iv_user_profile_pic);
        tvName = plannerView.findViewById(R.id.tv_name);
        tvRole = plannerView.findViewById(R.id.tv_role);
        tvCheckInTime = plannerView.findViewById(R.id.tv_check_in_time);
        tvCheckOutTime = plannerView.findViewById(R.id.tv_check_out_time);
        tvStatus = plannerView.findViewById(R.id.iv_status);
        lyMain = plannerView.findViewById(R.id.ly_main);
        isTeamList = false;

        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_user_avatar);
        requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());
        Glide.with(this)
                .applyDefaultRequestOptions(requestOptions)
                .load(Util.getUserObjectFromPref().getProfilePic())
                .into(ivUserImage);
        tvName.setText(Util.getUserObjectFromPref().getUserName());
        tvRole.setText(Util.getUserObjectFromPref().getRoleNames());

        teamAttendanceList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvTeamList = plannerView.findViewById(R.id.rv_team_list);
        adapter = new TeamAttendanceAdapter(getContext(), teamAttendanceList);
        rvTeamList.setLayoutManager(mLayoutManager);
        rvTeamList.setAdapter(adapter);

        radioGroup = plannerView.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(this);

        monthlyAttendanceFragmentPresenter = new MonthlyAttendanceFragmentPresenter(AttendancePlannerFragment.this);


        setUIData();
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                selectedDate = date.getDate();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                calendarSelectedDate = df.format(selectedDate);
                showDialogForSelectedDate(calendarSelectedDate);
                if (isTeamList)
                    monthlyAttendanceFragmentPresenter.getTeamAttendance(selectedDate);

            }
        });
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                int month = date.getMonth() + 1;
                int year = date.getYear();

                // call api when swipw month
                if (!Util.isConnected(getActivity())) {
                    Util.showToast("Network not available ! Try again", getActivity());
                } else {
                    showProgressBar();
                    monthlyAttendanceFragmentPresenter.getMonthlyAttendance(year, month);
                    Log.i("MonthlyAttendace", "111" + year + month);
                }
            }
        });
    }

    private void showDialogForSelectedDate(String calendarSelectedDate) {
        if (listDateWiseAttendace != null && listDateWiseAttendace.size() > 0 && !listDateWiseAttendace.isEmpty()) {
            for (AttendanceDateList attendanceDate : listDateWiseAttendace) {
                String checkInDate = attendanceDate.getCreateAt();
                String UserAttendaceDate[] = checkInDate.split(" ");
                String AttendaceCreatedAt = UserAttendaceDate[0];
                if (calendarSelectedDate.equalsIgnoreCase(AttendaceCreatedAt)) {
                    if (!attendanceDate.getCheckInTime().isEmpty()) {
                        tvCheckInTime.setText(Util.getDateFromTimestamp(Long.valueOf(attendanceDate.getCheckInTime()), "hh:mm aa"));
                    }
                    if (!attendanceDate.getCheckOutTime().isEmpty()) {
                        tvCheckOutTime.setText(Util.getDateFromTimestamp(Long.valueOf(attendanceDate.getCheckOutTime()), "hh:mm aa"));
                    }
                    tvStatus.setVisibility(View.VISIBLE);
                    tvStatus.setText(attendanceDate.getStatus());
                } else {
                    tvCheckInTime.setText("");
                    tvCheckOutTime.setText("");
                    tvStatus.setVisibility(View.GONE);
                }
            }
        }
    }

    private void setUIData() {
        CalendarDay calendarDay = calendarView.getCurrentDate();

        selectedDate = calendarDay.getDate();
        year = calendarDay.getYear();
        month = calendarDay.getMonth();
        cmonth = month + 1;

        MonthlyAttendanceFragmentPresenter monthlyAttendanceFragmentPresenter = new MonthlyAttendanceFragmentPresenter(this);
        monthlyAttendanceFragmentPresenter.getMonthlyAttendance(year, cmonth);

        isMonth = !isMonth;
        setCalendar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_calendar_mode:
                isMonth = !isMonth;
                setCalendar();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_my_attendance:
                lyMain.setVisibility(View.VISIBLE);
                rvTeamList.setVisibility(View.GONE);
                isTeamList = false;
                break;

            case R.id.rb_my_team_attendance:
                rvTeamList.setVisibility(View.VISIBLE);
                lyMain.setVisibility(View.GONE);
                isTeamList = true;
                monthlyAttendanceFragmentPresenter.getTeamAttendance(selectedDate);
                break;
        }
    }

    //Month data api response
    public void getAttendanceInfo(MonthlyAttendance data) {

        hideProgressBar();
        Log.i("AttendanceData", "222" + data);
        List<Datum> dataList = data.getData();
        List<Attendance> attendanceList;
        String id, status, subModule;
        String holidayName, leaveDate, holidayDate;
        List<HolidayList> holidayList = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");

        ArrayList<CalendarDay> holidays = new ArrayList<>();
        ArrayList<CalendarDay> leavsDaysList = new ArrayList<>();
        listDateWiseAttendace = new ArrayList<>();

        pendingList = new ArrayList<>();
        approveList = new ArrayList<>();
        rejectList = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            subModule = dataList.get(i).getSubModule();
            if (subModule.equalsIgnoreCase("attendance")) {

                attendanceList = dataList.get(i).getAttendance();
                for (int j = 0; j < attendanceList.size(); j++) {

                    attendanceDateList = new AttendanceDateList();
                    id = attendanceList.get(j).getId();
                    status = attendanceList.get(j).getStatus().getStatus();
                    checkIn = attendanceList.get(j).getCheckIn();
                    checkOut = attendanceList.get(j).getCheckOut();
                    attendanceDateList.setCheckInTime(checkIn.getTime());
                    attendanceDateList.setCheckOutTime(checkOut.getTime());
                    attendanceDateList.setAttendanceDate(attendanceList.get(j).getCreatedOn());
                    attendanceDateList.setStatus(attendanceList.get(j).getStatus().getStatus());
                    attendanceDateList.setTotalHrs(attendanceList.get(j).getTotalHours());
                    attendanceDateList.setCreateAt(attendanceList.get(j).getCreatedAt());
                    listDateWiseAttendace.add(attendanceDateList);

                    leaveDate = attendanceList.get(j).getCheckIn().getTime();

                    // convert this date and add into list
                    Long TimeStamp = Long.parseLong(leaveDate);
                    Date d1 = new Date(TimeStamp);
                    java.text.DateFormat df = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.FULL, java.text.DateFormat.FULL);
                    df.setTimeZone(TimeZone.getTimeZone("UTC"));
                    //String FinalDate=df.format(d1);
                    leavsDaysList.add(CalendarDay.from(d1));
                }

                if (leavsDaysList.size() > 0)
                    calendarView.addDecorator(new EventDecorator(getActivity(),
                            leavsDaysList, getResources().getDrawable(R.drawable.circle_background)));


                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                calendarSelectedDate = df.format(date);
                showDialogForSelectedDate(calendarSelectedDate);
            }

            if (subModule.equalsIgnoreCase("holidayList")) {
                holidayList = dataList.get(i).getHolidayList();
                for (int k = 0; k < holidayList.size(); k++) {
                    id = holidayList.get(k).getId();
                    holidayName = holidayList.get(k).getName();
                    holidayDate = holidayList.get(k).getDate().getDate().getNumberLong();

                    // convert this date and add into list
                    Long TimeStamp = Long.parseLong(holidayDate);
                    Date d1 = new Date(TimeStamp);
                    java.text.DateFormat df = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.FULL, java.text.DateFormat.FULL);
                    df.setTimeZone(TimeZone.getTimeZone("UTC"));
                    //String FinalDate=df.format(d1);
                    holidays.add(CalendarDay.from(d1));
                }
                if (holidays.size() > 0)
                    calendarView.addDecorator(new EventDecorator(getActivity(),
                            holidays, getResources().getDrawable(R.drawable.circle_background)));
            }
        }
    }

    //Team Attancance data api response
    public void setTeamAttendanceData(List<TeamAttendanceData> teamAttList) {
        teamAttendanceList.clear();
        teamAttendanceList.addAll(teamAttList);
        adapter.notifyDataSetChanged();
    }

    private void setCalendar() {
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        Calendar instance = Calendar.getInstance();
        calendarView.setSelectedDate(instance.getTime());

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance.get(Calendar.YEAR), Calendar.JANUARY, 1);

        if (isMonth) {
            calendarView.state().edit()
                    .setMinimumDate(instance1.getTime())
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit();
            ivCalendarMode.setRotation(180);

        } else {
            calendarView.state().edit()
                    .setMinimumDate(instance1.getTime())
                    .setCalendarDisplayMode(CalendarMode.WEEKS)
                    .commit();
            ivCalendarMode.setRotation(0);
        }

        calendarView.setSelectedDate(instance.getTime());
        calendarView.setCurrentDate(instance.getTime());
        highlightDates();

    }

    private void highlightDates() {
        // set the date list to highlight
        ArrayList<CalendarDay> dateList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        //String reg_date = formatter.format(cal.getTime());

        cal.add(Calendar.DATE, 2);
        try {
            dateList.add(CalendarDay.from(formatter.parse(formatter.format(cal.getTime()))));
        } catch (ParseException e) {
            Log.e("TAG", e.getMessage());
        }
        cal.add(Calendar.DATE, 3);
        try {
            dateList.add(CalendarDay.from(formatter.parse(formatter.format(cal.getTime()))));
        } catch (ParseException e) {
            Log.e("TAG", e.getMessage());
        }
        //noinspection deprecation
        calendarView.addDecorator(new EventDecorator(getActivity(),
                dateList, getResources().getDrawable(R.drawable.circle_background)));

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView,
                               @NonNull CalendarDay calendarDay, boolean b) {

        Toast.makeText(getActivity(), "date:" + calendarDay, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void showProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideProgressBar() {
        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    public void showError(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }
}



