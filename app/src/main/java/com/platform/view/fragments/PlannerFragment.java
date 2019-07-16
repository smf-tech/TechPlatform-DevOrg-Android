package com.platform.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.home.Modules;
import com.platform.models.leaves.LeaveDetail;
import com.platform.models.planner.SubmoduleData;
import com.platform.presenter.PlannerFragmentPresenter;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.CreateEventTaskActivity;
import com.platform.view.activities.GeneralActionsActivity;
import com.platform.view.activities.HomeActivity;
import com.platform.view.activities.PlannerDetailActivity;
import com.platform.view.adapters.EventTaskListAdapter;
import com.platform.view.adapters.LeaveBalanceAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        return plannerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initCardView();
    }

    private void initCardView() {
        if (getActivity() == null) {
            return;
        }

        progressBarLayout = plannerView.findViewById(R.id.profile_act_progress_bar);
        progressBar = plannerView.findViewById(R.id.pb_profile_act);
        plannerFragmentPresenter = new PlannerFragmentPresenter(this);
        plannerFragmentPresenter.getPlannerData();

        RelativeLayout rl_events = plannerView.findViewById(R.id.ly_events);
        RelativeLayout rl_tasks = plannerView.findViewById(R.id.ly_task);
        RelativeLayout rl_attendance = plannerView.findViewById(R.id.ly_attendance);
        RelativeLayout rl_leaves = plannerView.findViewById(R.id.ly_leave);

//        Date d = new Date();
//        CharSequence date = DateFormat.format(Constants.MONTH_DAY_FORMAT, d.getTime());
//        TextView todayDate = plannerView.findViewById(R.id.tv_today_date);
//        todayDate.setText(date);

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

        for(SubmoduleData obj:submoduleList){
            switch (obj.getSubModule()) {
                case Constants.Planner.ATTENDANCE_KEY:
                    obj.getAttendanceData();
//                ((ViewHolderAttendace)holder).

                    break;
                case Constants.Planner.EVENTS_KEY:
                    if(obj.getEventTaskData()!=null && obj.getEventTaskData().size()>0){

                        RecyclerView.LayoutManager mLayoutManagerEvent = new LinearLayoutManager(getActivity().getApplicationContext());
                        EventTaskListAdapter eventTaskListAdapter = new EventTaskListAdapter(getActivity(),
                                obj.getEventTaskData(), Constants.Planner.EVENTS_LABEL);
                        RecyclerView rvEvents = plannerView.findViewById(R.id.rv_events);
                        rvEvents.setLayoutManager(mLayoutManagerEvent);
                        rvEvents.setAdapter(eventTaskListAdapter);
                    }

                    break;
                case Constants.Planner.TASKS_KEY:
                    if(obj.getTaskData()!=null && obj.getTaskData().size()>0) {

                        RecyclerView.LayoutManager mLayoutManagerTask = new LinearLayoutManager(getActivity().getApplicationContext());
                        EventTaskListAdapter taskListAdapter = new EventTaskListAdapter(getActivity(),
                                obj.getTaskData(), Constants.Planner.EVENTS_LABEL);
                        RecyclerView rvTask = plannerView.findViewById(R.id.rv_events);
                        rvTask.setLayoutManager(mLayoutManagerTask);
                        rvTask.setAdapter(taskListAdapter);
                    }
                    break;
                case Constants.Planner.LEAVES_KEY:
                    if(obj.getLeave()!=null && obj.getLeave().size()>0){
                        leaveBalance.addAll(obj.getLeave());
                        RecyclerView.LayoutManager mLayoutManagerLeave = new LinearLayoutManager(getActivity(),
                                LinearLayoutManager.HORIZONTAL, true);
                        LeaveBalanceAdapter LeaveAdapter = new LeaveBalanceAdapter(
                                obj.getLeave(),"LeaveBalance");
                        RecyclerView rvLeaveBalance = plannerView.findViewById(R.id.rv_leave_balance);
                        rvLeaveBalance.setLayoutManager(mLayoutManagerLeave);
                        rvLeaveBalance.setAdapter(LeaveAdapter);
                    }
                    break;
            }
        }

    }

    public void setAttendaceView() {
        btCheckIn = plannerView.findViewById(R.id.bt_check_in);
        btCheckout = plannerView.findViewById(R.id.bt_checkout);
        tvCheckInTime = plannerView.findViewById(R.id.tv_check_in_time);
        tvCheckOutTime = plannerView.findViewById(R.id.tv_check_out_time);
        tvAttendanceDetails = plannerView.findViewById(R.id.tv_attendance_details);
        btCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCheckInTime.setText(DateFormat.format(Constants.TIME_FORMAT, new Date().getTime()));
                tvCheckInTime.setVisibility(View.VISIBLE);
                btCheckIn.setBackground(getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));
                btCheckIn.setClickable(false);
            }
        });
        btCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvCheckOutTime.setText(DateFormat.format(Constants.TIME_FORMAT, new Date().getTime()));
                tvCheckOutTime.setVisibility(View.VISIBLE);
                btCheckout.setBackground(getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
                btCheckout.setClickable(false);
            }
        });

        tvAttendanceDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GeneralActionsActivity.class);
                intent.putExtra(Constants.Planner.KEY_IS_DASHBOARD, false);
                intent.putExtra("title", getActivity().getString(R.string.attendance));
                intent.putExtra("switch_fragments", "AttendancePlannerFragment");
                getActivity().startActivity(intent);
            }
        });


    }

    public void setEventView() {

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
                intent.putExtra("switch_fragments", "LeaveApplyFragment");
                intent.putExtra("leaveBalance", (Serializable) leaveBalance);
                getActivity().startActivity(intent);
            }
        });


    }

}
