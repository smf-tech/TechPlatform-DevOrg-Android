package com.octopusbjsindia.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.models.attendance.MonthlyAttendance;
import com.octopusbjsindia.models.attendance.TeamAttendanceData;
import com.octopusbjsindia.models.attendance.TeamAttendanceResponse;
import com.octopusbjsindia.presenter.AttendanceSummeryFragmentPresenter;
import com.octopusbjsindia.utility.EventDecorator;
import com.octopusbjsindia.utility.Util;
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
import java.util.Locale;


public class AttendanceSummeryFragment extends Fragment implements APIDataListener {


    private RelativeLayout progressBarLayout;
    private MaterialCalendarView calendarView;
    private TextView tvCheckInTime, tvCheckOutTime, tvStatus, tvHours, tvChechInAddress, tvChechOutAddress;
    private Date selectedDate;
    private CalendarDay selectedCalendarDay;
    private String userId;

    private AttendanceSummeryFragmentPresenter presenter;

    private View view;

    private ArrayList<TeamAttendanceData> attendanceList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_attendance_summery, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new AttendanceSummeryFragmentPresenter(this);
        initView();
    }

    private void initView() {

        Bundle data = getActivity().getIntent().getExtras();
        userId = data.getString("user_id");

        attendanceList = new ArrayList<TeamAttendanceData>();
        tvCheckInTime = view.findViewById(R.id.tv_check_in_time);
        tvCheckOutTime = view.findViewById(R.id.tv_check_out_time);
        tvHours = view.findViewById(R.id.tv_hours);
        tvChechInAddress = view.findViewById(R.id.tv_checkin_address);
        tvChechOutAddress = view.findViewById(R.id.tv_checkout_address);
        tvStatus = view.findViewById(R.id.tv_status);
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_user_avatar);
        requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());
        TextView tvUserName = view.findViewById(R.id.tv_name);
        TextView tvUserRole = view.findViewById(R.id.tv_role);
        ImageView ivUserProfilePic = view.findViewById(R.id.iv_user_profile_pic);
        tvUserName.setText(data.getString("user_name"));
        tvUserRole.setText(data.getString("user_role"));
//        if (data.("profile_pic")) {
        Glide.with(this)
                .applyDefaultRequestOptions(requestOptions)
                .load(data.getString("profile_pic"))
                .into(ivUserProfilePic);
//        }


        progressBarLayout = view.findViewById(R.id.profile_act_progress_bar);

        calendarView = view.findViewById(R.id.calendarView);
        calendarView.state().edit().setMaximumDate(Calendar.getInstance().getTime()).commit();

        setCalendar();
        calendarView.setSelectedDate(Calendar.getInstance().getTime());

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedCalendarDay = date;
                selectedDate = date.getDate();

                String attendanceDate = Util.getDateFromTimestamp(selectedDate.getTime(), "yyyy-MM-dd");
                long attnDate = Util.dateTimeToTimeStamp(attendanceDate, "00:00");

                showSelectedDate(attnDate);

            }
        });
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                if (!Util.isConnected(getActivity())) {
                    Util.showToast("Network not available ! Try again", getActivity());
                } else {
                    if (Util.isConnected(getContext())) {
                        presenter.getAttendanceSummery(userId, date.getYear(), date.getMonth() + 1);
                    } else {
                        Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                    }
                }
            }
        });
        if (Util.isConnected(getContext())) {
            SimpleDateFormat MMFormat = new SimpleDateFormat("MM", Locale.ENGLISH);
            SimpleDateFormat yyyyFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);

            Date d = new Date();
            int month = Integer.parseInt(MMFormat.format(d.getTime()));
            int year = Integer.parseInt(yyyyFormat.format(d.getTime()));
            presenter.getAttendanceSummery(userId, year, month);
        } else {
            Util.showToast(getActivity().getResources().getString(R.string.msg_no_network), getActivity());
        }
    }

    private void setCalendar() {
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        Calendar instance = Calendar.getInstance();

        Calendar instance1 = Calendar.getInstance();
        instance1.set(2019, Calendar.JANUARY, 1);
//        if (isMonth) {
        calendarView.state().edit()
                .setMinimumDate(instance1.getTime())
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
//            ivCalendarMode.setRotation(180);
//        } else {
//            calendarView.state().edit()
//                    .setMinimumDate(instance1.getTime())
//                    .setCalendarDisplayMode(CalendarMode.WEEKS)
//                    .commit();
//            ivCalendarMode.setRotation(0);
//        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onFailureListener(String requestID, String message) {

    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {

    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void closeCurrentActivity() {

    }

    public void onSuccessUserAtendance(String requestID, MonthlyAttendance monthlyAttendance) {
    }

    public void onSuccessUserAtendance(TeamAttendanceResponse data) {

        TextView tv = view.findViewById(R.id.tv_total_hours);
        tv.setText(data.getTotalWorkingHours());
        attendanceList.clear();
        attendanceList.addAll(data.getData());
        displayEventsListOfMonth();

        String attendanceDate = Util.getDateFromTimestamp(new Date().getTime(), "yyyy-MM-dd");
        long attnDate = Util.dateTimeToTimeStamp(attendanceDate, "00:00");
        showSelectedDate(attnDate);

    }

    public void displayEventsListOfMonth() {
        ArrayList<CalendarDay> presentList = new ArrayList<>();
        ArrayList<CalendarDay> absentList = new ArrayList<>();
        ArrayList<CalendarDay> leaveList = new ArrayList<>();
        ArrayList<CalendarDay> holidayList = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        for (TeamAttendanceData obj : attendanceList) {
            try {

//                cal.setTime(formatter.parse(Util.getDateFromTimestamp(Long.parseLong(obj.getCheckIn().getTime()),
//                        "yyyy-MM-dd")));
                cal.setTime(formatter.parse(obj.getDate()));
                if (obj.getStatus().equalsIgnoreCase("absent")) {
                    absentList.add(CalendarDay.from(formatter.parse(formatter.format(cal.getTime()))));
                } else if (obj.getStatus().equalsIgnoreCase("present")) {
                    presentList.add(CalendarDay.from(formatter.parse(formatter.format(cal.getTime()))));
                } else if (obj.getStatus().equalsIgnoreCase("leave")) {
                    leaveList.add(CalendarDay.from(formatter.parse(formatter.format(cal.getTime()))));
                } else if (obj.getStatus().equalsIgnoreCase("holiday")) {
                    holidayList.add(CalendarDay.from(formatter.parse(formatter.format(cal.getTime()))));
                }

                calendarView.addDecorator(new EventDecorator(getActivity(),
                        absentList, getResources().getDrawable(R.drawable.circle_background_absent)));
                calendarView.addDecorator(new EventDecorator(getActivity(),
                        holidayList, getResources().getDrawable(R.drawable.circle_background_holiday)));
                calendarView.addDecorator(new EventDecorator(getActivity(),
                        presentList, getResources().getDrawable(R.drawable.circle_background_present)));
                calendarView.addDecorator(new EventDecorator(getActivity(),
                        leaveList, getResources().getDrawable(R.drawable.circle_background_leave)));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void showSelectedDate(long calendarSelectedDate) {

        for (TeamAttendanceData attendance : attendanceList) {
            if (attendance.getCheckIn() != null) {
                String checkInDate = attendance.getCheckIn().getTime();
                Date d = new Date(Long.parseLong(checkInDate));

                String datestr = Util.getDateFromTimestamp(d.getTime(), "yyyy-MM-dd");
                long attnDate = Util.dateTimeToTimeStamp(datestr, "00:00");

                if (calendarSelectedDate == attnDate) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
                    Date date1 = null;
                    Date date2 = null;
                    try {
                        if (!attendance.getCheckIn().getTime().isEmpty()) {
                            String timeCheckIn = Util.getDateFromTimestamp(Long.valueOf(attendance.getCheckIn().getTime()), "hh:mm aa");
                            tvCheckInTime.setText(timeCheckIn);
                            date1 = simpleDateFormat.parse(timeCheckIn);


                            tvChechInAddress.setText(attendance.getCheckIn().getAddress());
                            tvChechOutAddress.setText(attendance.getCheckOut().getAddress());

                        } else {
                            tvCheckInTime.setText("");
                        }
                        if (!attendance.getCheckOut().getTime().isEmpty()) {
                            String timeCheckOut = Util.getDateFromTimestamp(Long.valueOf(attendance.getCheckOut().getTime()), "hh:mm aa");
                            tvCheckOutTime.setText(timeCheckOut);
                            date2 = simpleDateFormat.parse(timeCheckOut);

                        } else {
                            tvCheckOutTime.setText("");
                        }

                        if (date1 != null && date2 != null) {
                            long difference = date2.getTime() - date1.getTime();
                            int days = (int) (difference / (1000 * 60 * 60 * 24));
                            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                            hours = (hours < 0 ? -hours : hours);
//                            viewHolder.tvTotalHours.setText(hours + ":" + min);

                            tvHours.setText(hours + ":" + min);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            } else {
                tvCheckInTime.setText("");
                tvCheckOutTime.setText("");
                tvHours.setText("");
                tvChechInAddress.setText("");
                tvChechOutAddress.setText("");

                tvStatus.setVisibility(View.GONE);
            }
        }

    }
}
