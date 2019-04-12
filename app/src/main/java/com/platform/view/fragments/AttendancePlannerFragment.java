package com.platform.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.platform.R;
import com.platform.utility.Constants;

import java.util.Date;


public class AttendancePlannerFragment extends Fragment implements View.OnClickListener {

    private View plannerView;
    private boolean isDashboard;
    private Button  btCheckin,btCheckout;

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

        RelativeLayout lyCalender = plannerView.findViewById(R.id.ly_calender);
        RelativeLayout lyWorkingHours = plannerView.findViewById(R.id.ly_working_hours);
        RelativeLayout lyCheckInOutDashboard = plannerView.findViewById(R.id.ly_checkin_out_dashboard);
        RelativeLayout lyCheckInOutDetail = plannerView.findViewById(R.id.ly_checkin_out_detail);
        btCheckin = plannerView.findViewById(R.id.bt_checkin);
        btCheckin.setOnClickListener(this);
        btCheckout = plannerView.findViewById(R.id.bt_checkout);
        btCheckout.setOnClickListener(this);
        TextView tvAttendanceDetails = plannerView.findViewById(R.id.tv_attendance_details);
        tvAttendanceDetails.setOnClickListener(this);

        if(isDashboard) {
            lyCalender.setVisibility(View.GONE);
            lyWorkingHours.setVisibility(View.VISIBLE);
            lyCheckInOutDashboard.setVisibility(View.VISIBLE);
            lyCheckInOutDetail.setVisibility(View.GONE);
        } else {
            lyCalender.setVisibility(View.VISIBLE);
            lyWorkingHours.setVisibility(View.VISIBLE);
            lyCheckInOutDashboard.setVisibility(View.GONE);
            lyCheckInOutDetail.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        Date d = new Date();
        CharSequence time  = DateFormat.format(Constants.TIME_FORMAT, d.getTime());
        switch (v.getId()){
            case R.id.bt_checkin:
                btCheckin.setText("Check in\n@ "+time);
                break;
            case R.id.bt_checkout:
                btCheckout.setText("Check out\n@ "+time);
                break;
            case R.id.tv_attendance_details:

                break;
        }
    }
}
