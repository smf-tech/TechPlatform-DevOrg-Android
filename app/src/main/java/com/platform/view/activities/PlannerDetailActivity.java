package com.platform.view.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.platform.R;
import com.platform.utility.Constants;
import com.platform.view.fragments.EventsPlannerFragment;
import com.platform.view.fragments.LeavePlannerFragment;
import com.platform.view.fragments.TasksPlannerFragment;

public class PlannerDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_detail);
        initView();
    }

    private void initView() {

        String toOpen = getIntent().getStringExtra(Constants.Planner.TO_OPEN);

        Bundle bundle = new Bundle();
        switch (toOpen) {
            case "ATTENDANCE":

                break;
            case "EventTask":
                bundle.putString(Constants.Planner.TO_OPEN, Constants.Planner.EVENTS_LABEL);
                Fragment eventsPlannerFragment = new EventsPlannerFragment();
                eventsPlannerFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fly_events_list, eventsPlannerFragment, eventsPlannerFragment.getClass()
                                .getSimpleName()).commit();
                break;
            case "Task":
                bundle.putString(Constants.Planner.TO_OPEN, Constants.Planner.TASKS_LABEL);
//                Fragment tasksPlannerFragment = new TasksPlannerFragment();
                Fragment tasksPlannerFragment = new EventsPlannerFragment();
                tasksPlannerFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fly_events_list, tasksPlannerFragment, tasksPlannerFragment.getClass()
                                .getSimpleName()).commit();
                break;
            case "LEAVES":
                Fragment leavePlannerFragment = new LeavePlannerFragment();
                leavePlannerFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fly_events_list, leavePlannerFragment, leavePlannerFragment.getClass()
                                .getSimpleName()).addToBackStack(null).commit();
                break;
        }
    }
}
