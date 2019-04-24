package com.platform.view.fragments;

import android.content.res.Resources;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.platform.R;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.view.activities.HomeActivity;
import java.util.Date;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
public class PlannerFragment extends Fragment {

    private View plannerView;

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

        Date d = new Date();
        CharSequence date  = DateFormat.format(Constants.MONTH_DAY_FORMAT, d.getTime());

        TextView todayDate=plannerView.findViewById(R.id.tv_today_date);
        todayDate.setText(date);

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.Planner.KEY_IS_DASHBOARD, true);

        Fragment attendancePlannerFragment = new AttendancePlannerFragment();
        attendancePlannerFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fly_attendance, attendancePlannerFragment, attendancePlannerFragment
                        .getClass().getSimpleName()).commit();

        Fragment eventsPlannerFragment = new EventsPlannerFragment();
        eventsPlannerFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fly_events, eventsPlannerFragment, eventsPlannerFragment.getClass()
                        .getSimpleName()).commit();

        Fragment tasksPlannerFragment = new TasksPlannerFragment();
        tasksPlannerFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fly_tasks, tasksPlannerFragment, tasksPlannerFragment.getClass()
                        .getSimpleName()).commit();

        Fragment leavePlannerFragment = new LeavePlannerFragment();
        leavePlannerFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fly_leave, leavePlannerFragment, leavePlannerFragment.getClass()
                        .getSimpleName()).commit();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (plannerView != null) {
            plannerView = null;
        }
        super.onDestroy();
    }

}
