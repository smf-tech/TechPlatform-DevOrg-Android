package com.platform.view.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.platform.R;
import com.platform.utility.AppEvents;
import com.platform.utility.Util;
import com.platform.view.activities.HomeActivity;

public class PlannerFragment extends Fragment implements View.OnClickListener {

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

        initTabView();
    }

    private void initTabView() {
        ViewPager viewPager = plannerView.findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));

        TabLayout tabs = plannerView.findViewById(R.id.tab_layout);
        tabs.setupWithViewPager(viewPager);
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
    public void onClick(View v) {
        if (v.getId() == R.id.txt_view_all_approvals) {
            Util.launchFragment(new PlannerFragment(), getContext(),
                    getString(R.string.planner), true);
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = EventsPlannerFragment.newInstance();
            switch (position) {
                case 0:
                    fragment = EventsPlannerFragment.newInstance();
                    break;

                case 1:
                    fragment = TasksPlannerFragment.newInstance();
                    break;

                case 2:
                    fragment = LeavePlannerFragment.newInstance();
                    break;

                case 3:
                    fragment = AttendancePlannerFragment.newInstance();
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            try {
                switch (position) {
                    case 0:
                        title = getResources().getString(R.string.events);
                        break;

                    case 1:
                        title = getResources().getString(R.string.tasks);
                        break;

                    case 2:
                        title = getResources().getString(R.string.leave);
                        break;

                    case 3:
                        title = getResources().getString(R.string.Attendance);
                        break;
                }
            } catch (Resources.NotFoundException | IllegalStateException e) {
                Log.e("TAG", e.getMessage());
            }

            return title;
        }
    }
}
