package com.octopusbjsindia.view.activities;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.octopusbjsindia.R;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.view.fragments.EventsTaskLandingFragment;

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
            case "Event":
                bundle.putString(Constants.Planner.TO_OPEN, Constants.Planner.EVENTS_LABEL);
                Fragment eventsPlannerFragment = new EventsTaskLandingFragment();
                eventsPlannerFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fly_events_list, eventsPlannerFragment, eventsPlannerFragment.getClass()
                                .getSimpleName()).commit();
                break;
            case "Task":
                bundle.putString(Constants.Planner.TO_OPEN, Constants.Planner.TASKS_LABEL);
//                Fragment tasksPlannerFragment = new TasksPlannerFragment();
                Fragment tasksPlannerFragment = new EventsTaskLandingFragment();
                tasksPlannerFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fly_events_list, tasksPlannerFragment, tasksPlannerFragment.getClass()
                                .getSimpleName()).commit();
                break;
//            case "LEAVES":

//                break;
        }
    }
}
