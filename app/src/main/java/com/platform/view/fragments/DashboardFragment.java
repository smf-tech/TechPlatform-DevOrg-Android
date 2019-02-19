package com.platform.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.home.Home;
import com.platform.models.home.Modules;
import com.platform.utility.Constants;
import com.platform.view.adapters.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private View dashboardView;
    private TabLayout tabLayout;
    private final int[] tabIcons = {
            R.drawable.bg_circle_pink,
            R.drawable.bg_circle_orange,
            R.drawable.bg_circle_yellow,
            R.drawable.bg_circle_green
    };
    private final int[] disableTabIcons = {
            R.drawable.bg_circle_lock
    };
    private List<Modules> tabNames = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dashboardView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return dashboardView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            Home homeData = (Home) arguments.getSerializable(Constants.Home.HOME_DATA);
            if (homeData != null) {
                tabNames = homeData.getHomeData().getOnApproveModules();

                if (homeData.getUserApproveStatus().equalsIgnoreCase(Constants.PENDING) ||
                        homeData.getUserApproveStatus().equalsIgnoreCase(Constants.REJECTED)) {

                    List<Modules> defaultModules = homeData.getHomeData().getDefaultModules();
                    for (int i = 0; i < tabNames.size(); i++) {
                        for (Modules module : defaultModules) {
                            if (tabNames.get(i).getName().equalsIgnoreCase(module.getName())) {
                                tabNames.get(i).setActive(true);
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < tabNames.size(); i++) {
                        tabNames.get(i).setActive(true);
                    }
                }
            }
        }

        initViews();
    }

    private void initViews() {
        ViewPager viewPager = dashboardView.findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        tabLayout = dashboardView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    @SuppressWarnings("ConstantConditions")
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        for (Modules modules : tabNames) {
            switch (modules.getName()) {
                case Constants.Home.FORMS:
                    adapter.addFragment(new PMFragment(), "Forms");
                    break;

                case Constants.Home.MEETINGS:
                    adapter.addFragment(new MeetingsFragment(), "Meetings");
                    break;

                case Constants.Home.APPROVALS:
                    adapter.addFragment(TMFragment.newInstance(true), "Teams");
                    break;

                case Constants.Home.REPORTS:
                    adapter.addFragment(ReportsFragment.newInstance(true), "Reports");
                    break;
            }
        }

        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        for (int i = 0; i < tabNames.size(); i++) {
            TextView tabOne = (TextView) LayoutInflater.from(getActivity())
                    .inflate(R.layout.layout_custom_tab, tabLayout, false);
            tabOne.setText(tabNames.get(i).getName());

            if (!tabNames.get(i).isActive()) {
                tabOne.setEnabled(false);
                tabOne.setCompoundDrawablesWithIntrinsicBounds(0, disableTabIcons[0], 0, 0);
            } else {
                tabOne.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[i], 0, 0);
            }

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(tabOne);
            }
        }

        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener((v, event) -> v.isEnabled());
        }
    }
}
