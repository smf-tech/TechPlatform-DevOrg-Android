package com.platform.view.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.platform.R;
import com.platform.utility.Constants;
import com.platform.view.fragments.AttendancePlannerFragment;
import com.platform.view.fragments.HolidayListFragment;
import com.platform.view.fragments.LeaveApplyFragment;
import com.platform.view.fragments.LeaveDetailsFragment;

/**
 * A class for handling the fragment switching.
 * This class will mainly be used for deciding <b>WHICH</b> fragment of the 'General Actions' tab should be invoked.
 */
public class GeneralActionsActivity extends BaseActivity implements View.OnClickListener {

    /**
     * Debug log tag.
     */
    private final String strTAG = getClass().getSimpleName();

    /**
     * Application fragment manager.
     */
    private FragmentManager fManager;

    private Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * A bundle that stores all the incoming intent data
         */
        Bundle data = getIntent().getExtras();
        setContentView(R.layout.activity_general_actions);
        /*
         * Get a reference to the {@link FragmentManager}.
         */
        fManager = getSupportFragmentManager();

        if (data != null && data.containsKey("switch_fragments")) {

            /*
             * Get the incoming string. If value corresponding to constant is null, use 'null'.
             * */
            String switchToFragment = data.getString("switch_fragments") != null
                    ? data.getString("switch_fragments") : "null";

            String title = data.getString("title") != null
                    ? data.getString("title") : "";
            Log.d(strTAG, "switchToFragment " + switchToFragment);

            TextView toolBar = findViewById(R.id.toolbar_title);
            ImageView toolBarMenu = findViewById(R.id.toolbar_edit_action);
            toolBar.setText(title);

            ImageView toolBarBack = findViewById(R.id.toolbar_back_action);
            toolBarBack.setOnClickListener(this);

            if (!TextUtils.isEmpty(switchToFragment)) {

                switch (switchToFragment) {
                    case "HolidayListFragment":
                        fragment = new HolidayListFragment();
                        toolBarBack.setBackgroundResource(R.drawable.ic_dialog_close_dark);
                        toolBarMenu.setVisibility(View.GONE);
                        fragment.setArguments(data);
                        openFragment();
                        break;

                    case "LeaveDetailsFragment":
                        fragment = new LeaveDetailsFragment();
                        toolBarMenu.setVisibility(View.VISIBLE);
                        fragment.setArguments(data);
                        openFragment();
                        break;

                    case "LeaveApplyFragment":
                        fragment = new LeaveApplyFragment();

                        toolBarMenu.setVisibility(View.GONE);
                        fragment.setArguments(data);
                        openFragment();
                        break;

                    case "AttendancePlannerFragment":
                        fragment = new AttendancePlannerFragment();

                        data.putBoolean(Constants.Planner.KEY_IS_DASHBOARD, false);
                        toolBarMenu.setVisibility(View.GONE);
                        fragment.setArguments(data);
                        openFragment();
                        break;
                }
            }
        }
    }

    private void openFragment() {
        // Begin transaction.
        FragmentTransaction fTransaction = fManager.beginTransaction();
        fTransaction.replace(R.id.general_actions_fragment, fragment).addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (shouldHandleBackPress()) {
            Log.d(strTAG, "shouldHandleBackPress returned true onBackPressed GAA "
                    + fManager.getBackStackEntryCount() + " " + fManager.getFragments());

            try {
                fManager.popBackStackImmediate();
            } catch (IllegalStateException e) {
                Log.e("TAG", e.getMessage());
            }

            Log.d(strTAG, "onBackPressed GAA " + fManager.getBackStackEntryCount()
                    + " " + fManager.getFragments());

            if (fManager.getBackStackEntryCount() == 0) {
                Log.d(strTAG, "onBackPressed GAA count 0 finishing GAA with result code 550");
                finish();
            }
        }
    }

    private boolean shouldHandleBackPress() {
        if (fManager != null && fManager.getFragments().size() > 0) {
            Log.d(strTAG, "shouldHandleBackPress inside IF  " + fManager.getFragments());

            Fragment fragment = fManager.getFragments().get(0);
            if (fragment != null) {
                if (fragment instanceof HolidayListFragment) {
                    // return !(Utils.isProgressShowing());
                    return true;
                }
            }
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == -1) {
            finish();
        } else if (view.getId() == R.id.toolbar_back_action) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        fragment = null;
        super.onDestroy();
    }
}

