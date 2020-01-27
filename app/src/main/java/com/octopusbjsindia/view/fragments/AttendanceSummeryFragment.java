package com.octopusbjsindia.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.attendance.AttendanceDateList;
import com.octopusbjsindia.presenter.MonthlyAttendanceFragmentPresenter;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.adapters.TeamAttendanceAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AttendanceSummeryFragment extends Fragment {


    private RelativeLayout progressBarLayout;
    private MaterialCalendarView calendarView;
    private Date selectedDate;

    View view;

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
        
    }

    private void initView() {
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_user_avatar);
        requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());
        TextView tvUserName = view.findViewById(R.id.tv_name);
        TextView tvUserRole = view.findViewById(R.id.tv_role);
        ImageView ivUserProfilePic = view.findViewById(R.id.iv_user_profile_pic);
        tvUserName.setText(Util.getUserObjectFromPref().getUserName());
        tvUserRole.setText(Util.getUserObjectFromPref().getRoleNames());
        if(Util.getUserObjectFromPref().getProfilePic()!=null){
            Glide.with(this)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(Util.getUserObjectFromPref().getProfilePic())
                    .into(ivUserProfilePic);
        }



        progressBarLayout = view.findViewById(R.id.profile_act_progress_bar);

        calendarView.state().edit().setMaximumDate(Calendar.getInstance().getTime()).commit();


//        monthlyAttendanceFragmentPresenter = new MonthlyAttendanceFragmentPresenter(AttendancePlannerFragment.this);


        setCalendar();
        calendarView.setSelectedDate(Calendar.getInstance().getTime());

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                selectedDate = date.getDate();

                String attendanceDate = Util.getDateFromTimestamp(selectedDate.getTime(), "yyyy-MM-dd");
                long attnDate = Util.dateTimeToTimeStamp(attendanceDate, "00:00");

//                showSelectedDate(attnDate);

            }
        });
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                if (!Util.isConnected(getActivity())) {
                    Util.showToast("Network not available ! Try again", getActivity());
                } else {
                    if(Util.isConnected(getContext())) {
//                        monthlyAttendanceFragmentPresenter.getMonthlyAttendance(date.getYear(), date.getMonth() + 1);
                    }else{
                        Util.showToast(getResources().getString(R.string.msg_no_network), getActivity());
                    }
                }
            }
        });
    }

    private void setCalendar() {
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        Calendar instance = Calendar.getInstance();

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance.get(Calendar.YEAR), Calendar.JANUARY, 1);

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

//        calendarView.setSelectedDate(instance.getTime());
//        calendarView.setCurrentDate(instance.getTime());

    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
