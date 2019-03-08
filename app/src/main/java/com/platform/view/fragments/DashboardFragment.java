package com.platform.view.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.models.forms.FormResult;
import com.platform.models.home.Home;
import com.platform.models.home.Modules;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.view.activities.HomeActivity;
import com.platform.view.adapters.SmartFragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import static com.platform.utility.Constants.UserApprovals.EVENT_APPROVALS_FETCHED;
import static com.platform.utility.Constants.UserApprovals.EXTRA_APPROVALS_COUNT;

@SuppressWarnings("WeakerAccess")
public class DashboardFragment extends Fragment {

    private View dashboardView;
    private TabLayout tabLayout;
    private boolean isSyncRequired;
    private final int[] tabIcons = {
            R.drawable.bg_circle_pink,
            R.drawable.bg_circle_orange,
            R.drawable.bg_circle_yellow,
            R.drawable.bg_circle_green
    };
    private final int[] tabThemeColor = {
            R.color.pink,
            R.color.orange,
            R.color.yellow,
            R.color.green
    };
    private final int[] disableTabIcons = {
            R.drawable.bg_circle_lock
    };
    private List<Modules> tabNames = new ArrayList<>();
    public static int mApprovalCount = 0;

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
            this.isSyncRequired = arguments.getBoolean("NEED_SYNC");
            Home homeData = (Home) arguments.getSerializable(Constants.Home.HOME_DATA);
            if (homeData != null) {
                tabNames = homeData.getHomeData().getOnApproveModules();
                setMenuResourceId();

                if (homeData.getUserApproveStatus().equalsIgnoreCase(Constants.RequestStatus.PENDING) ||
                        homeData.getUserApproveStatus().equalsIgnoreCase(Constants.RequestStatus.REJECTED)) {

                    List<Modules> defaultModules = homeData.getHomeData().getDefaultModules();
                    for (final Modules tabName : tabNames) {
                        for (Modules module : defaultModules) {
                            if (tabName.getName().getLocaleValue().equalsIgnoreCase(module.getName().getLocaleValue())) {
                                tabName.setActive(true);
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

        IntentFilter filter = new IntentFilter();
        filter.addAction(EVENT_APPROVALS_FETCHED);

        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                if (Objects.requireNonNull(intent.getAction()).equals(EVENT_APPROVALS_FETCHED)) {
                    Toast.makeText(context, "User approvals fetched.", Toast.LENGTH_SHORT).show();

                    mApprovalCount = intent.getIntExtra(EXTRA_APPROVALS_COUNT, 0);

                    for (final Modules modules : tabNames) {
                        if (modules.getName().getLocaleValue().equals(getString(R.string.approvals))) {
                            RelativeLayout tabOne = (RelativeLayout) LayoutInflater.from(context)
                                    .inflate(R.layout.layout_custom_tab, tabLayout, false);
                            TextView tabView = tabOne.findViewById(R.id.tab);
                            tabView.setText(modules.getName().getLocaleValue());
                            tabView.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[2], 0, 0);
                            TextView pendingActionsCountView = tabOne.findViewById(R.id.pending_action_count);
                            pendingActionsCountView.setText(String.valueOf(mApprovalCount));
                            pendingActionsCountView.setTextColor(getResources().getColor(tabThemeColor[2],
                                    getContext().getTheme()));

                            TabLayout.Tab tab = tabLayout.getTabAt(2);
                            if (tab != null) {
                                tab.setCustomView(tabOne);
                            }
                        }
                    }
                }
            }
        }, filter);

    }

    private void setMenuResourceId() {
        for (int i = 0; i < tabNames.size(); i++) {
            switch (tabNames.get(i).getName().getLocaleValue()) {
                case Constants.Home.FORMS:
                    tabNames.get(i).setResId(R.id.action_menu_forms);
                    break;

                case Constants.Home.MEETINGS:
                    tabNames.get(i).setResId(R.id.action_menu_calendar);
                    break;

                case Constants.Home.APPROVALS:
                    tabNames.get(i).setResId(R.id.action_menu_teams);
                    break;

                case Constants.Home.REPORTS:
                    tabNames.get(i).setResId(R.id.action_menu_reports);
                    break;
            }
        }
    }

    private void initViews() {
        ViewPager viewPager = dashboardView.findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(4);
        setupViewPager(viewPager);

        tabLayout = dashboardView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        if (getActivity() != null) {
            ((HomeActivity) getActivity()).hideItem(tabNames);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void setupViewPager(ViewPager viewPager) {
        DashboardViewPagerAdapter adapter = new DashboardViewPagerAdapter(getChildFragmentManager());
        for (Modules modules : tabNames) {
            switch (modules.getName().getLocaleValue()) {
                case Constants.Home.FORMS:
                    Bundle b = new Bundle();
                    b.putBoolean("NEED_SYNC", isSyncRequired);
                    PMFragment fragment = new PMFragment();
                    fragment.setArguments(b);
                    adapter.addFragment(fragment);
                    break;

                case Constants.Home.MEETINGS:
                    adapter.addFragment(new MeetingsFragment());
                    break;

                case Constants.Home.APPROVALS:
                    adapter.addFragment(new TMUserPendingFragment());
                    break;

                case Constants.Home.REPORTS:
                    adapter.addFragment(new ReportsFragment());
                    break;
            }
        }

        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        for (int i = 0; i < tabNames.size(); i++) {
            if (getContext() == null) continue;

            RelativeLayout tabOne = (RelativeLayout) LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_custom_tab, tabLayout, false);
            TextView tabView = tabOne.findViewById(R.id.tab);
            tabView.setText(tabNames.get(i).getName().getLocaleValue());

            TextView pendingActionsCountView = tabOne.findViewById(R.id.pending_action_count);

            if (!tabNames.get(i).isActive()) {
                ((TextView) tabOne.findViewById(R.id.tab))
                        .setCompoundDrawablesWithIntrinsicBounds(0, disableTabIcons[0], 0, 0);
                pendingActionsCountView.setVisibility(View.GONE);
            } else {
                pendingActionsCountView.setVisibility(View.VISIBLE);
                int resId = tabIcons[0];
                int resColor = tabThemeColor[0];
                int pendingActionCount = 0;

                switch (tabNames.get(i).getName().getLocaleValue()) {
                    case Constants.Home.FORMS:
                        resId = tabIcons[0];
                        resColor = tabThemeColor[0];
                        pendingActionCount = getFormsPendingActionCount();
                        break;

                    case Constants.Home.MEETINGS:
                        resId = tabIcons[1];
                        resColor = tabThemeColor[1];
                        break;

                    case Constants.Home.APPROVALS:
                        resId = tabIcons[2];
                        resColor = tabThemeColor[2];
                        pendingActionCount = mApprovalCount;
                        break;

                    case Constants.Home.REPORTS:
                        resId = tabIcons[3];
                        resColor = tabThemeColor[3];
                        break;
                }

                tabView.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
                if (pendingActionCount != 0) {
                    pendingActionsCountView.setText(String.valueOf(pendingActionCount));
                    pendingActionsCountView.setTextColor(getResources().getColor(resColor,
                            getContext().getTheme()));
                } else {
                    pendingActionsCountView.setVisibility(View.GONE);
                }
            }

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(tabOne);
            }
        }

        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            View child = tabStrip.getChildAt(i);
            child.setId(i);
            child.setEnabled(tabNames.get(i).isActive());
            child.setOnClickListener(view -> {
                switch (view.getId()) {
                    case 0:
                        AppEvents.trackAppEvent(getString(R.string.event_forms_tab_click));
                        break;

                    case 1:
                        AppEvents.trackAppEvent(getString(R.string.event_meetings_tab_click));
                        break;

                    case 2:
                        AppEvents.trackAppEvent(getString(R.string.event_approvals_tab_click));
                        break;

                    case 3:
                        AppEvents.trackAppEvent(getString(R.string.event_reports_tab_click));
                        break;
                }
            });
        }
    }

    private int getFormsPendingActionCount() {
        DatabaseManager dbInstance = DatabaseManager.getDBInstance(getContext());
        int count = 0;

        List<FormResult> partiallySavedForms = dbInstance.getAllPartiallySavedForms();
        if (partiallySavedForms != null) count = partiallySavedForms.size();

        return count;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateBadgeCount();
    }

    private void updateBadgeCount() {
        for (int i = 0; i < tabNames.size(); i++) {
            if (getContext() == null) continue;

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View tabCustomView = tab.getCustomView();
                if (tabCustomView != null) {
                    TextView tabView = tabCustomView.findViewById(R.id.tab);
                    tabView.setText(tabNames.get(i).getName().getLocaleValue());

                    TextView pendingActionsCountView = tabCustomView.findViewById(R.id.pending_action_count);

                    if (!tabNames.get(i).isActive()) {
                        ((TextView) tabCustomView.findViewById(R.id.tab))
                                .setCompoundDrawablesWithIntrinsicBounds(0, disableTabIcons[0], 0, 0);
                        pendingActionsCountView.setVisibility(View.GONE);
                    } else {
                        pendingActionsCountView.setVisibility(View.VISIBLE);
                        int resId = tabIcons[0];
                        int resColor = tabThemeColor[0];
                        int pendingActionCount = 0;

                        switch (tabNames.get(i).getName().getLocaleValue()) {
                            case Constants.Home.FORMS:
                                resId = tabIcons[0];
                                resColor = tabThemeColor[0];
                                pendingActionCount = getFormsPendingActionCount();
                                break;

                            case Constants.Home.MEETINGS:
                                resId = tabIcons[1];
                                resColor = tabThemeColor[1];
                                break;

                            case Constants.Home.APPROVALS:
                                resId = tabIcons[2];
                                resColor = tabThemeColor[2];
                                pendingActionCount = mApprovalCount;
                                break;

                            case Constants.Home.REPORTS:
                                resId = tabIcons[3];
                                resColor = tabThemeColor[3];
                                break;
                        }

                        tabView.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
                        if (pendingActionCount != 0) {
                            pendingActionsCountView.setText(String.valueOf(pendingActionCount));
                            pendingActionsCountView.setTextColor(getResources().getColor(resColor,
                                    getContext().getTheme()));
                        } else {
                            pendingActionsCountView.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    }

    class DashboardViewPagerAdapter extends SmartFragmentStatePagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        DashboardViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }
}
