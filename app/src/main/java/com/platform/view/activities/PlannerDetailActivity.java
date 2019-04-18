package com.platform.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import com.platform.R;
import com.platform.utility.Constants;
import com.platform.view.fragments.EventsPlannerFragment;

public class PlannerDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_detail);
        initView();
    }

    private void initView() {

        String toOpen = getIntent().getStringExtra(Constants.Planner.TO_OPEN);

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.Planner.KEY_IS_DASHBOARD, false);

        if (toOpen.equals("ATTENDANCE")) {

        } else if (toOpen.equals("EVENTS")) {
            Fragment eventsPlannerFragment = new EventsPlannerFragment();
            eventsPlannerFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fly_events_list, eventsPlannerFragment, eventsPlannerFragment.getClass()
                            .getSimpleName()).commit();
        } else if (toOpen.equals("TASKS")) {

        } else if (toOpen.equals("LEAVES")) {

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
