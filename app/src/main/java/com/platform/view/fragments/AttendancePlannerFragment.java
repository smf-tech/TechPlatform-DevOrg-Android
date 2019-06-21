package com.platform.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.attendance.AttendanceStatus;
import com.platform.models.attendance.Datum;
import com.platform.models.attendance.MonthlyAttendance;
import com.platform.presenter.MonthlyAttendanceFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.EventDecorator;
import com.platform.utility.Urls;
import com.platform.view.activities.GeneralActionsActivity;
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
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AttendancePlannerFragment extends Fragment implements View.OnClickListener,
        OnDateSelectedListener {

    private View plannerView;
    private boolean isDashboard;
    private boolean isMonth = true;
    private int tabClicked = -1;

    private TextView tvClickPending;
    private TextView tvClickApproved;
    private TextView tvClickRejected;
    private TextView tvAttendanceDetails;
    private TextView tvCheckInTime;
    private TextView tvCheckOutTime;
    private Button btCheckIn, btCheckout;

    private RecyclerView rvAttendanceList;
    private RelativeLayout attendanceCardLayout;
    private RelativeLayout lyCalender;
    private LinearLayout lyCheckInOutDashboard;
    private LinearLayout lvAttendanceStatus;
    private MaterialCalendarView calendarView;



    private String status,dateTimeStamp;
    public static final String APPROVED="Approve";
    public static final String PENDING="Pending";
    public static final String REJECTED="Rejected";

    ArrayList<String>pendingList;
    ArrayList<String>approveList;
    ArrayList<String>rejectList;


    private List<AttendanceStatus>attendanceStatusList;
    String TAG=AttendancePlannerFragment.class.getSimpleName();
    MonthlyAttendanceFragmentPresenter monthlyAttendanceFragmentPresenter;
    private int year,month;

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
        lyCheckInOutDashboard = plannerView.findViewById(R.id.ly_check_in_out_dashboard);
        lvAttendanceStatus = plannerView.findViewById(R.id.lv_attendance_status);
        rvAttendanceList = plannerView.findViewById(R.id.rv_attendance_list);
        tvClickPending = plannerView.findViewById(R.id.tv_tb_pending);
        tvClickPending.setOnClickListener(this);
        tvClickApproved = plannerView.findViewById(R.id.tv_tb_approved);
        tvClickApproved.setOnClickListener(this);
        tvClickRejected = plannerView.findViewById(R.id.tv_tb_rejected);
        tvClickRejected.setOnClickListener(this);
        btCheckIn = plannerView.findViewById(R.id.bt_check_in);
        btCheckIn.setOnClickListener(this);
        btCheckout = plannerView.findViewById(R.id.bt_checkout);
        btCheckout.setOnClickListener(this);
        tvAttendanceDetails = plannerView.findViewById(R.id.tv_attendance_details);
        tvAttendanceDetails.setOnClickListener(this);

        ImageView ivCalendarMode = plannerView.findViewById(R.id.iv_calendar_mode);
        calendarView = plannerView.findViewById(R.id.calendarView);
        ivCalendarMode.setOnClickListener(this);

        attendanceCardLayout = plannerView.findViewById(R.id.rv_card_attendance);

        setUIData();

    }

    private void setUIData() {
        if (isDashboard) {
            lvAttendanceStatus.setVisibility(View.GONE);
            rvAttendanceList.setVisibility(View.GONE);
            lyCalender.setVisibility(View.GONE);
            // lyWorkingHours.setVisibility(View.VISIBLE);
            lyCheckInOutDashboard.setVisibility(View.VISIBLE);
        } else {
            rvAttendanceList.setVisibility(View.VISIBLE);
            lvAttendanceStatus.setVisibility(View.VISIBLE);

            lyCalender.setVisibility(View.VISIBLE);
            //lyWorkingHours.setVisibility(View.VISIBLE);
            tvAttendanceDetails.setVisibility(View.GONE);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(70, 50, 70, 50);
            attendanceCardLayout.setLayoutParams(lp);


            CalendarDay calendarDay=calendarView.getCurrentDate();
            year=calendarDay.getYear();
            month=calendarDay.getMonth()+1;

            MonthlyAttendanceFragmentPresenter monthlyAttendanceFragmentPresenter=new MonthlyAttendanceFragmentPresenter(this);
            monthlyAttendanceFragmentPresenter.getMonthlyAttendance(year,month);


            //attendanceListData();
        }

        isMonth = !isMonth;
        setCalendar();
    }

    public void getAttendanceInfo(MonthlyAttendance data) {
     Log.i("AttendanceData","222"+data);
     // get dates and status
       List<Datum>listAttDate=data.getData();
       // create list depend on status
       attendanceStatusList=new ArrayList<>();

       pendingList=new ArrayList<>();
       approveList=new ArrayList<>();
       rejectList=new ArrayList<>();


       for(int i=0;i<listAttDate.size();i++){

           status=listAttDate.get(i).getStatus();

           if(status.equalsIgnoreCase("Approve"))
           {
               dateTimeStamp=listAttDate.get(i).getCreatedOn().getDate().getNumberLong();
               approveList.add(dateTimeStamp);

               /*AttendanceStatus attendanceStatus=new AttendanceStatus();
               attendanceStatus.setStatus(status);
               attendanceStatus.setDatetimestamp(dateTimeStamp);
               attendanceStatusList.add(attendanceStatus);*/


           }
           if(status.equalsIgnoreCase("Pending"))
           {
               dateTimeStamp=listAttDate.get(i).getCreatedOn().getDate().getNumberLong();
               pendingList.add(dateTimeStamp);
             /*  AttendanceStatus attendanceStatus=new AttendanceStatus();
               attendanceStatus.setStatus(status);
               attendanceStatus.setDatetimestamp(dateTimeStamp);
               attendanceStatusList.add(attendanceStatus);*/

           }

           if(status.equalsIgnoreCase("Rejected"))
           {
               dateTimeStamp=listAttDate.get(i).getCreatedOn().getDate().getNumberLong();
               rejectList.add(dateTimeStamp);
               /*AttendanceStatus attendanceStatus=new AttendanceStatus();
               attendanceStatus.setStatus(status);
               attendanceStatus.setDatetimestamp(dateTimeStamp);
               attendanceStatusList.add(attendanceStatus);*/
           }

       }
       attendanceListData(PENDING);


    }

    public void attendanceListData(String type) {



        if(type.equalsIgnoreCase(PENDING))
        {
            AttendanceAdapter adapter = new AttendanceAdapter(getActivity(),pendingList,type);
            rvAttendanceList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvAttendanceList.setAdapter(adapter);
        }
        if(type.equalsIgnoreCase(APPROVED))
        {
            AttendanceAdapter adapter = new AttendanceAdapter(getActivity(),approveList,type);
            rvAttendanceList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvAttendanceList.setAdapter(adapter);
        }
        if(type.equalsIgnoreCase(REJECTED))
        {
            AttendanceAdapter adapter = new AttendanceAdapter(getActivity(),rejectList,type);
            rvAttendanceList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvAttendanceList.setAdapter(adapter);
        }

    }

    @Override
    public void onClick(View v) {
        Date d = new Date();
        CharSequence time = DateFormat.format(Constants.TIME_FORMAT, d.getTime());
        switch (v.getId()) {
            case R.id.bt_check_in:
                tvCheckInTime.setText(time);
                tvCheckInTime.setVisibility(View.VISIBLE);
                //noinspection deprecation
                btCheckIn.setBackground(Objects.requireNonNull(getActivity())
                        .getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                btCheckIn.setTextColor(getActivity().getResources().getColor(R.color.attendance_text_color));
                break;

            case R.id.bt_checkout:
                tvCheckOutTime.setText(time);
                //noinspection deprecation
                btCheckout.setBackground(Objects.requireNonNull(getActivity())
                        .getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                btCheckout.setTextColor(getActivity().getResources().getColor(R.color.attendance_text_color));
                break;

            case R.id.tv_attendance_details:
                Intent intent = new Intent(getActivity(),GeneralActionsActivity.class);
                intent.putExtra(Constants.Planner.KEY_IS_DASHBOARD, false);
                intent.putExtra("title", getString(R.string.attendance));
                intent.putExtra("switch_fragments", "AttendancePlannerFragment");
                startActivity(intent);
                break;

            case R.id.tv_tb_pending:
                if (tabClicked != 1) {
                    tabClicked = 1;
                    tvClickPending.setTextColor(getResources().getColor(R.color.black_green));
                    tvClickApproved.setTextColor(getResources().getColor(R.color.blur_tab));
                    tvClickRejected.setTextColor(getResources().getColor(R.color.blur_tab));
                }
                attendanceListData(PENDING);


                break;

            case R.id.tv_tb_approved:
                if (tabClicked != 2) {
                    tabClicked = 2;
                    tvClickPending.setTextColor(getResources().getColor(R.color.blur_tab));
                    tvClickApproved.setTextColor(getResources().getColor(R.color.black_green));
                    tvClickRejected.setTextColor(getResources().getColor(R.color.blur_tab));

                }
                attendanceListData(APPROVED);
                break;

            case R.id.tv_tb_rejected:
                if (tabClicked != 3) {
                    tabClicked = 3;
                    tvClickPending.setTextColor(getResources().getColor(R.color.blur_tab));
                    tvClickApproved.setTextColor(getResources().getColor(R.color.blur_tab));
                    tvClickRejected.setTextColor(getResources().getColor(R.color.black_green));
                }
                attendanceListData(REJECTED);
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

        if (isMonth) {
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
        // list for holiday

        ArrayList<CalendarDay> holidayList = new ArrayList<>();
        try {
            holidayList.add(CalendarDay.from(formatter.parse("2019/06/19")));
            holidayList.add(CalendarDay.from(formatter.parse("2019/06/21")));
        } catch (ParseException e) {
            e.printStackTrace();
        }



        calendarView.addDecorator(new EventDecorator(getActivity(),
                holidayList, getResources().getDrawable(R.drawable.circle_background)));




    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView,
                               @NonNull CalendarDay calendarDay, boolean b) {

        Toast.makeText(getActivity(), "date:" + calendarDay, Toast.LENGTH_SHORT).show();
    }

    public void showError(String error){
        Toast.makeText(getActivity(),"error"+error,Toast.LENGTH_LONG).show();

    }

}
