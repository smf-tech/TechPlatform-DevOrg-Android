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
import com.platform.utility.Constants;
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
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class AttendancePlannerFragment extends Fragment implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener {

    private View plannerView;
    private boolean isMonth = true;

    private TextView tvCheckInTime;
    private TextView tvCheckOutTime;
    private TextView tvUserName;
    private TextView tvUserRole;
    private ImageView ivUserProfilePic;

    private Date selectedDate;
    private boolean isTeamAttendance;
    private RequestOptions requestOptions;

    private RelativeLayout attendanceCardLayout;
    private MaterialCalendarView calendarView;

    private MonthlyAttendanceFragmentPresenter monthlyAttendanceFragmentPresenter;

    private AttendanceDateList attendanceDateList;
    private List<AttendanceDateList> listDateWiseAttendace;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private ImageView ivCalendarMode;
    private RadioGroup radioGroup;
    private RecyclerView rvTeamList;
    private List<TeamAttendanceData> teamAttendanceList;
    private TeamAttendanceAdapter adapter;

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

        requestOptions = new RequestOptions().placeholder(R.drawable.ic_user_avatar);
        requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());

        tvCheckInTime = plannerView.findViewById(R.id.tv_check_in_time);
        tvCheckOutTime = plannerView.findViewById(R.id.tv_check_out_time);
        tvUserName = plannerView.findViewById(R.id.tv_name);
        tvUserRole = plannerView.findViewById(R.id.tv_role);
        ivUserProfilePic = plannerView.findViewById(R.id.iv_user_profile_pic);
        tvUserName.setText(Util.getUserObjectFromPref().getUserName());
        tvUserRole.setText(Util.getUserObjectFromPref().getRoleNames());
        if(Util.getUserObjectFromPref().getProfilePic()!=null){
            Glide.with(this)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(Util.getUserObjectFromPref().getProfilePic())
                    .into(ivUserProfilePic);
        }

        selectedDate = new Date();
        isTeamAttendance=false;
        listDateWiseAttendace=new ArrayList<AttendanceDateList>();

        progressBar = plannerView.findViewById(R.id.pb_profile_act);
        progressBarLayout = plannerView.findViewById(R.id.profile_act_progress_bar);
        ivCalendarMode = plannerView.findViewById(R.id.iv_calendar_mode);
        calendarView = plannerView.findViewById(R.id.calendarView);
        calendarView.state().edit().setMaximumDate(Calendar.getInstance().getTime()).commit();

        monthlyAttendanceFragmentPresenter = new MonthlyAttendanceFragmentPresenter(AttendancePlannerFragment.this);

        ivCalendarMode.setOnClickListener(this);
        attendanceCardLayout = plannerView.findViewById(R.id.ly_main);

        radioGroup = plannerView.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(this);

        teamAttendanceList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvTeamList = plannerView.findViewById(R.id.rv_team_list);
        adapter = new TeamAttendanceAdapter(getContext(), teamAttendanceList);
        rvTeamList.setLayoutManager(mLayoutManager);
        rvTeamList.setAdapter(adapter);

        isMonth = !isMonth;
        setCalendar();

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                selectedDate = date.getDate();
//                String datestr = Util.getDateFromTimestamp(selectedDate.getTime(), "yyyy-MM-dd");
//                showDialogForSelectedDate(datestr);

                String attendanceDate = Util.getDateFromTimestamp(selectedDate.getTime(), "yyyy-MM-dd");
                long attnDate = Util.dateTimeToTimeStamp(attendanceDate, "00:00");

                showDialogForSelectedDate(attnDate);

                if(isTeamAttendance){
                    if(Util.isConnected(getContext())) {
                        monthlyAttendanceFragmentPresenter.getTeamAttendance(selectedDate);
                    }else{
                        Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                    }
                }
            }
        });
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                if (!Util.isConnected(getActivity())) {
                    Util.showToast("Network not available ! Try again", getActivity());
                } else {
                    showProgressBar();
                    if(Util.isConnected(getContext())) {
                        monthlyAttendanceFragmentPresenter.getMonthlyAttendance(date.getYear(), date.getMonth() + 1);
                    }else{
                        Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                    }
                }
            }
        });
    }

    private void showDialogForSelectedDate(long calendarSelectedDate) {
        AttendanceDateList attendanceDateList;
        if (listDateWiseAttendace != null && listDateWiseAttendace.size() > 0 && !listDateWiseAttendace.isEmpty()) {

            for (int i = 0; i < listDateWiseAttendace.size(); i++) {
                attendanceDateList = listDateWiseAttendace.get(i);
                String checkInDate = attendanceDateList.getAttendanceDate();
                Date d=new Date(Long.parseLong(checkInDate));


                String datestr = Util.getDateFromTimestamp(d.getTime(), "yyyy-MM-dd");
                long attnDate = Util.dateTimeToTimeStamp(datestr, "00:00");

                if (calendarSelectedDate==attnDate) {
                    if (!attendanceDateList.getCheckInTime().isEmpty()) {
                        tvCheckInTime.setText( Util.getDateFromTimestamp(Long.valueOf(attendanceDateList.getCheckInTime()), "hh:mm aa"));
                    }
                    if (!attendanceDateList.getCheckOutTime().isEmpty()) {
                        tvCheckOutTime.setText(Util.getDateFromTimestamp(Long.valueOf(attendanceDateList.getCheckOutTime()), "hh:mm aa"));
                    }   //cheack that created at is equal to today date
                } else {
                    tvCheckInTime.setText("");
                    tvCheckOutTime.setText("");
                }
            }
        }
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
                isTeamAttendance=false;
                attendanceCardLayout.setVisibility(View.VISIBLE);
                rvTeamList.setVisibility(View.GONE);
                break;

            case R.id.rb_my_team_attendance:
                if(Util.isConnected(getContext())) {
                    isTeamAttendance = true;
                    rvTeamList.setVisibility(View.VISIBLE);
                    attendanceCardLayout.setVisibility(View.GONE);
                    monthlyAttendanceFragmentPresenter.getTeamAttendance(selectedDate);
                }else{
                    Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                }

                break;
        }
    }

    public void getAttendanceInfo(MonthlyAttendance data) {

        hideProgressBar();
        Log.i("AttendanceData", "222" + data);
        List<Datum> dataList = data.getData();
        List<Attendance> attendanceList;
        String holidayName, holidayDate;
        List<HolidayList> holidayList = null;
        ArrayList<CalendarDay> holidays = new ArrayList<>();
        ArrayList<CalendarDay> attendList = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {

            if (dataList.get(i).getSubModule().equalsIgnoreCase("attendance")) {
                attendanceList = dataList.get(i).getAttendance();
                for (int j = 0; j < attendanceList.size(); j++) {

                    attendanceDateList = new AttendanceDateList();
                    attendanceDateList.setCheckInTime(attendanceList.get(j).getCheckIn().getTime());
                    attendanceDateList.setCheckOutTime(attendanceList.get(j).getCheckOut().getTime());
                    attendanceDateList.setAttendanceDate(attendanceList.get(j).getCreatedOn());
                    attendanceDateList.setStatus(attendanceList.get(j).getStatus().getStatus());
                    attendanceDateList.setTotalHrs(attendanceList.get(j).getTotalHours());
                    attendanceDateList.setCreateAt(attendanceList.get(j).getCreatedAt());
                    listDateWiseAttendace.add(attendanceDateList);

                    Long TimeStamp = Long.parseLong(attendanceList.get(j).getCheckIn().getTime());
                    Date d1 = new Date(TimeStamp);
                    java.text.DateFormat df = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.FULL, java.text.DateFormat.FULL);
                    df.setTimeZone(TimeZone.getTimeZone("UTC"));
                    //String FinalDate=df.format(d1);
                    attendList.add(CalendarDay.from(d1));
                }
                if (attendList.size() > 0)
                    calendarView.addDecorator(new EventDecorator(getActivity(),
                            attendList, getResources().getDrawable(R.drawable.circular_background)));

                String datestr = Util.getDateFromTimestamp(selectedDate.getTime(), "yyyy-MM-dd");
                long attnDate = Util.dateTimeToTimeStamp(datestr, "00:00");
                showDialogForSelectedDate(attnDate);
            } else if (dataList.get(i).getSubModule().equalsIgnoreCase("holidayList")) {
                holidayList = dataList.get(i).getHolidayList();
                for (int k = 0; k < holidayList.size(); k++) {

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

    public void setTeamAttendanceData(List<TeamAttendanceData> dataList) {
        teamAttendanceList.clear();
        teamAttendanceList.addAll(dataList);
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
//        highlightDates();

    }

//    private void highlightDates() {
//        // set the date list to highlight
//        ArrayList<CalendarDay> dateList = new ArrayList<>();
//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        //String reg_date = formatter.format(cal.getTime());
//
//        cal.add(Calendar.DATE, 2);
//        try {
//            dateList.add(CalendarDay.from(formatter.parse(formatter.format(cal.getTime()))));
//        } catch (ParseException e) {
//            Log.e("TAG", e.getMessage());
//        }
//        cal.add(Calendar.DATE, 3);
//        try {
//            dateList.add(CalendarDay.from(formatter.parse(formatter.format(cal.getTime()))));
//        } catch (ParseException e) {
//            Log.e("TAG", e.getMessage());
//        }
//        //noinspection deprecation
//        calendarView.addDecorator(new EventDecorator(getActivity(),
//                dateList, getResources().getDrawable(R.drawable.circle_background)));
//
//    }

    public void showError(String error) {
        Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(selectedDate);
        int year = calendar.get(Calendar.YEAR);

        monthlyAttendanceFragmentPresenter = new MonthlyAttendanceFragmentPresenter(this);

        if(Util.isConnected(getContext())) {
            monthlyAttendanceFragmentPresenter.getMonthlyAttendance(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1);
        }else{
            Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
        }

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

}



