package com.platform.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.platform.R;
import com.platform.utility.Constants;
import com.platform.utility.EventDecorator;
import com.platform.view.activities.GeneralActionsActivity;
import com.platform.view.adapters.AppliedLeavesAdapter;
import com.platform.view.adapters.AttendanceAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AttendancePlannerFragment extends Fragment implements View.OnClickListener,OnDateSelectedListener {

    private View plannerView;
    private boolean isDashboard;
    private Button  btCheckin,btCheckout;
    private TextView tvClickPending;
    private TextView tvClickApproved;
    private TextView tvClickRejected;

    private LinearLayout lvAttendaceStatus;
    private RecyclerView rvAttendanceList;
    private int tabClicked =-1;

    private RelativeLayout lyCalender;
   // private RelativeLayout lyWorkingHours;
    private LinearLayout lyCheckInOutDashboard;
    private TextView tvAttendanceDetails;
    private ImageView ivCalendarMode;
    private MaterialCalendarView calendarView ;
    boolean isMonth = true;
    private TextView tvCheckInTime,tvCheckOutTime;

    public AttendancePlannerFragment() {
        // Required empty public constructor
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
        if (bundle != null) {
            isDashboard = bundle.getBoolean(Constants.Planner.KEY_IS_DASHBOARD);
        }

        lyCalender = plannerView.findViewById(R.id.ly_calender);
        tvCheckInTime = plannerView.findViewById(R.id.tv_check_in_time);
        tvCheckOutTime = plannerView.findViewById(R.id.tv_check_out_time);
       // lyWorkingHours = plannerView.findViewById(R.id.ly_working_hours);
        lyCheckInOutDashboard = plannerView.findViewById(R.id.ly_checkin_out_dashboard);
        lvAttendaceStatus = plannerView.findViewById(R.id.lv_attendance_status);
        rvAttendanceList = plannerView.findViewById(R.id.rv_attendance_list);
        tvClickPending = plannerView.findViewById(R.id.tv_tb_pending);
        tvClickPending.setOnClickListener(this);
        tvClickApproved = plannerView.findViewById(R.id.tv_tb_approved);
        tvClickApproved.setOnClickListener(this);
        tvClickRejected = plannerView.findViewById(R.id.tv_tb_rejected);
        tvClickRejected.setOnClickListener(this);
        btCheckin = plannerView.findViewById(R.id.bt_checkin);
        btCheckin.setOnClickListener(this);
        btCheckout = plannerView.findViewById(R.id.bt_checkout);
        btCheckout.setOnClickListener(this);
        tvAttendanceDetails = plannerView.findViewById(R.id.tv_attendance_details);
        tvAttendanceDetails.setOnClickListener(this);

        ivCalendarMode = plannerView.findViewById(R.id.iv_calendar_mode);
        calendarView = plannerView.findViewById(R.id.calendarView);
        ivCalendarMode.setOnClickListener(this);
        setUIData();

    }

    private void setUIData(){
        if(isDashboard) {
            lvAttendaceStatus.setVisibility(View.GONE);
            rvAttendanceList.setVisibility(View.GONE);
            lyCalender.setVisibility(View.GONE);
           // lyWorkingHours.setVisibility(View.VISIBLE);
            lyCheckInOutDashboard.setVisibility(View.VISIBLE);

        } else {
            rvAttendanceList.setVisibility(View.VISIBLE);
            lvAttendaceStatus.setVisibility(View.VISIBLE);

            lyCalender.setVisibility(View.VISIBLE);
            //lyWorkingHours.setVisibility(View.VISIBLE);
            tvAttendanceDetails.setVisibility(View.GONE);
            attendaceListData();

        }

        isMonth = !isMonth;
        setCalendar();


    }

    private void attendaceListData(){

        AttendanceAdapter adapter = new AttendanceAdapter(getActivity(), new ArrayList<>());
        rvAttendanceList.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvAttendanceList.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {
        Date d = new Date();
        CharSequence time  = DateFormat.format(Constants.TIME_FORMAT, d.getTime());
        switch (v.getId()){
            case R.id.bt_checkin:
                tvCheckInTime.setText("@9:30 AM");
                btCheckin.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                btCheckin.setTextColor(getActivity().getResources().getColor(R.color.attendance_text_color));
                break;
            case R.id.bt_checkout:
                tvCheckOutTime.setText("@9:30 AM");
                btCheckout.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                btCheckout.setTextColor(getActivity().getResources().getColor(R.color.attendance_text_color));
                break;
            case R.id.tv_attendance_details:
                Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
                intent.putExtra(Constants.Planner.KEY_IS_DASHBOARD, false);
                intent.putExtra("title", "Attendance");
                intent.putExtra("switch_fragments", "AttendancePlannerFragment");
                startActivity(intent);
                break;
            case R.id.tv_tb_pending:

                if(tabClicked != 1) {
                    tabClicked = 1;
                    tvClickPending.setTextColor(getResources().getColor(R.color.black_green));
                    tvClickApproved.setTextColor(getResources().getColor(R.color.blur_tab));
                    tvClickRejected.setTextColor(getResources().getColor(R.color.blur_tab));
                }
                break;
            case R.id.tv_tb_approved:
                if(tabClicked != 2) {
                    tabClicked = 2;
                    tvClickPending.setTextColor(getResources().getColor(R.color.blur_tab));
                    tvClickApproved.setTextColor(getResources().getColor(R.color.black_green));
                    tvClickRejected.setTextColor(getResources().getColor(R.color.blur_tab));
                }
                break;
            case R.id.tv_tb_rejected:
                if(tabClicked != 3) {
                    tabClicked = 3;
                    tvClickPending.setTextColor(getResources().getColor(R.color.blur_tab));
                    tvClickApproved.setTextColor(getResources().getColor(R.color.blur_tab));
                    tvClickRejected.setTextColor(getResources().getColor(R.color.black_green));
                }
                break;

            case R.id.iv_calendar_mode:
                isMonth = !isMonth;
                setCalendar();
                break;
        }
    }

    private void setCalendar() {
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        Calendar instance = Calendar.getInstance();
        calendarView.setSelectedDate(instance.getTime());

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance.get(Calendar.YEAR), Calendar.JANUARY, 1);
        if(isMonth){
            calendarView.state().edit()
                    .setMinimumDate(instance1.getTime())
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit();
        } else {
            calendarView.state().edit()
                    .setMinimumDate(instance1.getTime())
                    .setCalendarDisplayMode(CalendarMode.WEEKS)
                    .commit();
        }
        calendarView.setSelectedDate(instance.getTime());
        calendarView.setCurrentDate(instance.getTime());
        highliteDates();
    }

    private void highliteDates() {
        // set the date list to highlight
        ArrayList<CalendarDay> dateList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss");
        String reg_date = formatter.format(cal.getTime());

        cal.add(Calendar.DATE, 2);
        try {
            dateList.add(CalendarDay.from(formatter.parse(formatter.format(cal.getTime()))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add(Calendar.DATE, 3);
        try {
            dateList.add(CalendarDay.from(formatter.parse(formatter.format(cal.getTime()))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendarView.addDecorator(new EventDecorator(getActivity(),
                dateList, getResources().getDrawable(R.drawable.circle_background)));
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {
        Toast.makeText(getActivity(),"date:"+calendarDay,Toast.LENGTH_SHORT).show();
    }
}
