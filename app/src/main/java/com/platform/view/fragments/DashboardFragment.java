package com.platform.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

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
import com.platform.view.customs.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class DashboardFragment extends Fragment {

    private View dashboardView;
    private TabLayout tabLayout;
    private boolean isSyncRequired;
    private boolean isUserApproved;
//    private final int[] tabIcons = {
//            R.drawable.ic_form_icon_db,
//            R.drawable.ic_planner_icon_db,
//            R.drawable.ic_approvals,
//            R.drawable.ic_reports_icon_db,
//            R.drawable.ic_webview_icon_db,
//            R.drawable.ic_webview_icon_db,
//            R.drawable.ic_matrimony_icon_db
//    };
    private final int[] disableTabIcons = {
            R.drawable.ic_lock
    };
    private List<Modules> tabNames = new ArrayList<>();
    private static int mApprovalCount = 0;

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
                int size = tabNames.size();
                for (int i = 0; i < size; i++) {
                    if (tabNames.get(i).getModuleType().equals(Constants.Home.FORMS) ||
                            tabNames.get(i).getModuleType().equals(Constants.Home.PLANNER) ||
                            tabNames.get(i).getModuleType().equals(Constants.Home.APPROVALS) ||
                            tabNames.get(i).getModuleType().equals(Constants.Home.REPORTS) ||
                            tabNames.get(i).getModuleType().equals(Constants.Home.WEBMODULE) ||
                            tabNames.get(i).getModuleType().equals(Constants.Home.CONTENT) ||
                            tabNames.get(i).getModuleType().equals(Constants.Home.MATRIMONY) ||
                            tabNames.get(i).getModuleType().equals(Constants.Home.SUJALAM_SUPHALAM)) {
                        //do nothing
                    } else {
                        tabNames.remove(i);
                        size--;
                        i--;
                    }
                }
                setMenuResourceId();

                if (homeData.getUserApproveStatus().equalsIgnoreCase(Constants.RequestStatus.PENDING) ||
                        homeData.getUserApproveStatus().equalsIgnoreCase(Constants.RequestStatus.REJECTED)) {

                    isUserApproved = false;
                    List<Modules> defaultModules = homeData.getHomeData().getDefaultModules();
                    for (final Modules tabName : tabNames) {
                        for (Modules module : defaultModules) {
                            if (tabName.getName().getLocaleValue().equalsIgnoreCase(module.getName().getLocaleValue())) {
                                tabName.setActive(true);
                            }
                        }
                    }
                } else {
                    isUserApproved = true;
                    for (int i = 0; i < tabNames.size(); i++) {
                        tabNames.get(i).setActive(true);
                    }
                }
            }
        }
        initViews();
    }

    private void setMenuResourceId() {
        for (int i = 0; i < tabNames.size(); i++) {
            switch (tabNames.get(i).getModuleType()) {
                case Constants.Home.FORMS:
                    tabNames.get(i).setResId(R.id.action_menu_forms);
                    break;

                case Constants.Home.PLANNER:
                    tabNames.get(i).setResId(R.id.action_menu_calendar);
                    break;

                case Constants.Home.APPROVALS:
                    tabNames.get(i).setResId(R.id.action_menu_teams);
                    break;

                case Constants.Home.REPORTS:
                    tabNames.get(i).setResId(R.id.action_menu_reports);
                    break;

                case Constants.Home.WEBMODULE:
                    tabNames.get(i).setResId(R.id.action_menu_reports);
                    break;

                case Constants.Home.CONTENT:
                    tabNames.get(i).setResId(R.id.action_menu_reports);
                    break;

                case Constants.Home.MATRIMONY:
                    tabNames.get(i).setResId(R.id.action_menu_reports);
                    break;

                case Constants.Home.SUJALAM_SUPHALAM:
                    tabNames.get(i).setResId(R.id.action_menu_reports);
                    break;
                default:
            }
        }
    }

    private void initViews() {
        CustomViewPager viewPager = dashboardView.findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        tabLayout = dashboardView.findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.setTabMode(TabLayout.MODE_AUTO);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        if (getActivity() != null) {
            ((HomeActivity) getActivity()).hideItem(tabNames);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        DashboardViewPagerAdapter adapter = new DashboardViewPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < tabNames.size(); i++) {
            Modules modules = tabNames.get(i);

            switch (modules.getModuleType()) {
                case Constants.Home.FORMS:
                    Bundle b = new Bundle();
                    b.putBoolean("NEED_SYNC", isSyncRequired);
                    PMFragment fragment = new PMFragment();
                    fragment.setArguments(b);
                    adapter.addFragment(fragment);
                    break;

                case Constants.Home.PLANNER:
                    adapter.addFragment(new PlannerFragment());
                    break;

                case Constants.Home.APPROVALS:
                    adapter.addFragment(new TMUserLandingFragment());
                    break;

                case Constants.Home.REPORTS:
                    adapter.addFragment(new ReportsFragment());
                    break;

                case Constants.Home.WEBMODULE:
                    Bundle webBundle = new Bundle();
                    webBundle.putString("Weblink", modules.getWeblink());
                    webBundle.putString("Webmodule_name", modules.getName().getLocaleValue());
                    WebmoduleFragment webmoduleFragment = new WebmoduleFragment();
                    webmoduleFragment.setArguments(webBundle);
                    adapter.addFragment(webmoduleFragment);
                    break;

                case Constants.Home.CONTENT:
                    ContentManagementFragment cmf = ContentManagementFragment.newInstance("con", "con");
                    adapter.addFragment(cmf);
                    break;

                case Constants.Home.MATRIMONY:
                    adapter.addFragment(new MatrimonyFragment());
                    break;

                case Constants.Home.SUJALAM_SUPHALAM:
                    adapter.addFragment(new SujalamSufalamFragment());
                    break;

                default:
                    adapter.addFragment(new DefaultFragment());
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
                drawTabCount(i, tabOne, tabView, pendingActionsCountView);
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                if (tab != null) {
                    tab.setCustomView(tabOne);
                }
        }

        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        int tabCount = tabStrip.getChildCount() > tabNames.size() ? tabNames.size() : tabStrip.getChildCount();

        for (int i = 0; i < tabCount; i++) {
            View child = tabStrip.getChildAt(i);
            child.setId(i);
            child.setContentDescription(tabNames.get(i).getName().getLocaleValue());
            child.setEnabled(tabNames.get(i).isActive());
            child.setOnClickListener(view -> {
                ((HomeActivity) DashboardFragment.this.getActivity()).setActionBarTitle(child.getContentDescription().toString());
                AppEvents.trackAppEvent(DashboardFragment.this.getString(R.string.event_forms_tab_click));
            });
        }
    }

    private void drawTabCount(int i, View tabOne, TextView tabView, TextView pendingActionsCountView) {
        if (getContext() == null) return;
        if (!tabNames.get(i).isActive()) {
            ((TextView) tabOne.findViewById(R.id.tab))
                    .setCompoundDrawablesWithIntrinsicBounds(disableTabIcons[0], 0, 0, 0);
            pendingActionsCountView.setVisibility(View.GONE);
        } else {
            pendingActionsCountView.setVisibility(View.VISIBLE);
            int resId;
            int pendingActionCount = 0;

            switch (tabNames.get(i).getName().getDefaultValue()) {
                case Constants.Home.FORMS:
//                    resId = tabIcons[0];
                    pendingActionCount = getFormsPendingActionCount();
                    break;

                case Constants.Home.PLANNER:
//                    resId = tabIcons[1];
                    break;

                case Constants.Home.APPROVALS:
//                    resId = tabIcons[2];
                    pendingActionCount = getApprovalCount();
                    break;

                case Constants.Home.REPORTS:
//                    resId = tabIcons[3];
                    break;

                case Constants.Home.WEBMODULE_NAME:
//                    resId = tabIcons[4];
                    break;

                case Constants.Home.CONTENT:
//                    resId = tabIcons[4];
                    break;

                case Constants.Home.MATRIMONY:
                    //resId = tabIcons[6];
                    break;

                case Constants.Home.SUJALAM_SUPHALAM:
                    //resId = tabIcons[6];
                    break;
                default:
//                    resId = R.drawable.bg_circle_default;
                    break;
            }

//            tabView.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
            if (pendingActionCount != 0) {
                pendingActionsCountView.setText(String.valueOf(pendingActionCount));
                pendingActionsCountView.setTextColor(getResources().getColor(R.color.black));
            } else {
                pendingActionsCountView.setVisibility(View.GONE);
            }
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

    public void updateBadgeCount() {
        for (int i = 0; i < tabNames.size(); i++) {
            if (getContext() == null) continue;

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                View tabCustomView = tab.getCustomView();
                if (tabCustomView != null) {
                    TextView tabView = tabCustomView.findViewById(R.id.tab);
                    tabView.setText(tabNames.get(i).getName().getLocaleValue());
                    TextView pendingActionsCountView = tabCustomView.findViewById(R.id.pending_action_count);
                    drawTabCount(i, tabCustomView, tabView, pendingActionsCountView);
                }
            }
        }
    }

    public static int getApprovalCount() {
        return DashboardFragment.mApprovalCount;
    }

    public static void setApprovalCount(int mApprovalCount) {
        DashboardFragment.mApprovalCount = mApprovalCount;
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
