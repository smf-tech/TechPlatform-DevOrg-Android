package com.platform.view.fragments;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.CreateEventListener;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.home.Modules;
import com.platform.models.planner.SubmoduleData;
import com.platform.presenter.PlannerFragmentPresenter;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.view.activities.HomeActivity;
import com.platform.view.adapters.PlannerFragmentAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlannerFragment extends Fragment implements PlatformTaskListener {

    private View plannerView;
    private RecyclerView rvSubmodules;
    private PlannerFragmentAdapter plannerFragmentAdapter;
    private PlannerFragmentPresenter plannerFragmentPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            ((HomeActivity) getActivity()).setActionBarTitle(title);
            ((HomeActivity) getActivity()).setSyncButtonVisibility(false);

            if ((boolean) getArguments().getSerializable("SHOW_BACK")) {
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

        plannerFragmentPresenter = new PlannerFragmentPresenter(this);
        plannerFragmentPresenter.getPlannerData();

//        RelativeLayout rl_events = plannerView.findViewById(R.id.events_card);
//        RelativeLayout rl_tasks = plannerView.findViewById(R.id.tasks_card);
//        RelativeLayout rl_attendance = plannerView.findViewById(R.id.attendance_card);
//        RelativeLayout rl_leaves = plannerView.findViewById(R.id.leave_card);
//
//
//        Date d = new Date();
//        CharSequence date = DateFormat.format(Constants.MONTH_DAY_FORMAT, d.getTime());
//
//        TextView todayDate = plannerView.findViewById(R.id.tv_today_date);
//        todayDate.setText(date);
//
//        List<Modules> approveModules = DatabaseManager.getDBInstance(getActivity().getApplicationContext())
//                .getModulesOfStatus(Constants.RequestStatus.APPROVED_MODULE);
//
//        Bundle bundle = new Bundle();
//        bundle.putBoolean(Constants.Planner.KEY_IS_DASHBOARD, true);
//
//        for (Modules m:approveModules) {
//            switch (m.getName().getDefaultValue()){
//                case Constants.Planner.EVENTS_KEY:
//                    rl_events.setVisibility(View.VISIBLE);
//                    Fragment eventsPlannerFragment = new EventsPlannerFragment();
//                    eventsPlannerFragment.setArguments(bundle);
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fly_events, eventsPlannerFragment, eventsPlannerFragment.getClass()
//                                    .getSimpleName()).commit();
//                    break;
//                case Constants.Planner.TASKS_KEY:
//                    rl_tasks.setVisibility(View.VISIBLE);
//                    Fragment tasksPlannerFragment = new TasksPlannerFragment();
//                    tasksPlannerFragment.setArguments(bundle);
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fly_tasks, tasksPlannerFragment, tasksPlannerFragment.getClass()
//                                    .getSimpleName()).commit();
//                    break;
//                case Constants.Planner.ATTENDANCE_KEY:
//                    rl_attendance.setVisibility(View.VISIBLE);
//                    Fragment attendancePlannerFragment = new AttendancePlannerFragment();
//                    attendancePlannerFragment.setArguments(bundle);
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fly_attendance, attendancePlannerFragment, attendancePlannerFragment
//                                    .getClass().getSimpleName()).commit();
//                    break;
//                case Constants.Planner.LEAVES_KEY:
//                    rl_leaves.setVisibility(View.VISIBLE);
//                    Fragment leavePlannerFragment = new LeavePlannerFragment();
//                    leavePlannerFragment.setArguments(bundle);
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fly_leave, leavePlannerFragment, leavePlannerFragment.getClass()
//                                    .getSimpleName()).commit();
//                    break;
//                default:
//                    break;
//            }
//        }
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

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public <T> void showNextScreen(T data) {

    }

    @Override
    public void showErrorMessage(String result) {

    }

    public void showPlannerSummer(ArrayList<SubmoduleData> submoduleList) {
//        ArrayList<SubmoduleData> templist=new ArrayList<>();
//        SubmoduleData obj1=new SubmoduleData("attendance");
//        SubmoduleData obj2=new SubmoduleData("event");
//        SubmoduleData obj3=new SubmoduleData("task");
//        SubmoduleData obj4=new SubmoduleData("leave");
//        templist.add(obj1);
//        templist.add(obj2);
//        templist.add(obj3);
//        templist.add(obj4);
        plannerFragmentAdapter=new PlannerFragmentAdapter(getActivity(),submoduleList);

        rvSubmodules = plannerView.findViewById(R.id.rv_submodules);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvSubmodules.setLayoutManager(mLayoutManager);
        rvSubmodules.setAdapter(plannerFragmentAdapter);
    }
}
