package com.platform.view.fragments;

import android.accounts.Account;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.home.Home;
import com.platform.models.home.HomeData;
import com.platform.models.home.Modules;
import com.platform.models.user.UserInfo;
import com.platform.presenter.HomeActivityPresenter;
import com.platform.syncAdapter.GenericAccountService;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.HomeActivity;
import com.platform.view.adapters.ViewPagerAdapter;

import java.util.List;

import static com.platform.syncAdapter.SyncAdapterUtils.ACCOUNT;
import static com.platform.syncAdapter.SyncAdapterUtils.ACCOUNT_TYPE;
import static com.platform.utility.Constants.RequestStatus.APPROVED_MODULE;
import static com.platform.utility.Constants.RequestStatus.DEFAULT_MODULE;

public class HomeFragment extends Fragment implements PlatformTaskListener, HomeActivity.OnSyncClicked {

    private Home homeData;
    private Context context;
    private View homeFragmentView;
    private HomeActivityPresenter presenter;
    private AlertDialog dialogNotApproved;
    private Object mSyncObserverHandle;
    private boolean isSyncRequired;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null) {
            context = getActivity();
            ((HomeActivity) context).setActionBarTitle(getResources().getString(R.string.app_name_ss));
            ((HomeActivity) context).setSyncClickListener(this);
        }

        String str=getResources().getString(R.string.task_title) + Util.getLocaleLanguageCode();
        dialogNotApproved = new AlertDialog.Builder(context).create();
        AppEvents.trackAppEvent(getString(R.string.event_home_screen_visit));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        homeFragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        presenter = new HomeActivityPresenter(this);
        getUserData();
        return homeFragmentView;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        DashboardFragment dashboardFragment = new DashboardFragment();
        if (homeData != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.Home.HOME_DATA, homeData);
            bundle.putBoolean("NEED_SYNC", isSyncRequired);
            dashboardFragment.setArguments(bundle);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(dashboardFragment, getString(R.string.tab_dashboard));
        adapter.addFragment(new StoriesFragment(), getString(R.string.tab_stories));
        adapter.addFragment(new ConnectFragment(), getString(R.string.tab_connect));
        viewPager.setAdapter(adapter);
    }

    private void getUserData() {
        mSyncStatusObserver.onStatusChanged(0);

        // Watch for sync state changes
        isSyncRequired = false;
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);

        List<Modules> modulesFromDatabase = getModulesFromDatabase();
        if (modulesFromDatabase != null && !modulesFromDatabase.isEmpty()) {

            List<Modules> defaultModules = DatabaseManager.getDBInstance(context.getApplicationContext())
                    .getModulesOfStatus(Constants.RequestStatus.DEFAULT_MODULE);

            List<Modules> approveModules = DatabaseManager.getDBInstance(context.getApplicationContext())
                    .getModulesOfStatus(Constants.RequestStatus.APPROVED_MODULE);

            HomeData homeData = new HomeData();
            homeData.setDefaultModules(defaultModules);
            homeData.setOnApproveModules(approveModules);

            this.homeData = new Home();
            this.homeData.setHomeData(homeData);

            UserInfo userInfo = Util.getUserObjectFromPref();
            this.homeData.setUserApproveStatus(
                    (userInfo.getApproveStatus().equalsIgnoreCase(Constants.RequestStatus.PENDING) ||
                            userInfo.getApproveStatus().equalsIgnoreCase(Constants.RequestStatus.REJECTED)) ?
                            Constants.RequestStatus.PENDING : Constants.RequestStatus.APPROVED);

            if (this.homeData.getUserApproveStatus().equalsIgnoreCase(Constants.RequestStatus.PENDING) ||
                    this.homeData.getUserApproveStatus().equalsIgnoreCase(Constants.RequestStatus.REJECTED)) {
                if (!dialogNotApproved.isShowing()) {
                    showApprovedDialog();
                }
            }

            ViewPager viewPager = homeFragmentView.findViewById(R.id.home_view_pager);
            viewPager.setOffscreenPageLimit(3);
            setupViewPager(viewPager);


            TabLayout tabLayout = homeFragmentView.findViewById(R.id.home_tabs);
            tabLayout.setupWithViewPager(viewPager);
            return;
        }

        if (presenter != null && Util.isConnected(context)) {
            isSyncRequired = true;
            UserInfo user = Util.getUserObjectFromPref();
            presenter.getModules(user);
        }
    }

    private List<Modules> getModulesFromDatabase() {
        return DatabaseManager.getDBInstance(context.getApplicationContext()).getAllModules();
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public <T> void showNextScreen(T data) {
        if (data != null) {
            homeData = (Home) data;

            List<Modules> defaultModules = homeData.getHomeData().getDefaultModules();
            for (final Modules module : defaultModules) {
                module.setModule(DEFAULT_MODULE);
                DatabaseManager.getDBInstance(context.getApplicationContext()).insertModule(module);
            }

            List<Modules> approveModules = this.homeData.getHomeData().getOnApproveModules();
            for (final Modules module : approveModules) {
                module.setModule(APPROVED_MODULE);
                DatabaseManager.getDBInstance(context.getApplicationContext()).insertModule(module);
            }

            HomeData homeData = new HomeData();
            homeData.setDefaultModules(defaultModules);
            homeData.setOnApproveModules(approveModules);

            this.homeData.setHomeData(homeData);
            this.homeData.setUserApproveStatus(
                    (this.homeData.getUserApproveStatus().equalsIgnoreCase(Constants.RequestStatus.PENDING) ||
                            this.homeData.getUserApproveStatus().equalsIgnoreCase(Constants.RequestStatus.REJECTED)) ?
                            Constants.RequestStatus.PENDING : Constants.RequestStatus.APPROVED);

            if (this.homeData.getUserApproveStatus().equalsIgnoreCase(Constants.RequestStatus.PENDING) ||
                    this.homeData.getUserApproveStatus().equalsIgnoreCase(Constants.RequestStatus.REJECTED)) {

                if (!dialogNotApproved.isShowing()) {
                    showApprovedDialog();
                }
            }

            ViewPager viewPager = homeFragmentView.findViewById(R.id.home_view_pager);
            viewPager.setOffscreenPageLimit(3);
            setupViewPager(viewPager);

            TabLayout tabLayout = homeFragmentView.findViewById(R.id.home_tabs);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    @Override
    public void showErrorMessage(String result) {
        Util.showToast(result, this);
    }

    private void showApprovedDialog() {
        if (dialogNotApproved != null) {
            try {
                if (dialogNotApproved.isShowing()) {
                    dialogNotApproved.dismiss();
                }

                dialogNotApproved.setTitle(getString(R.string.app_name_ss));
                String message = getString(R.string.approve_profile);
                dialogNotApproved.setMessage(message);

                // Setting Icon to Dialog
                dialogNotApproved.setIcon(R.mipmap.app_logo);

                // Setting OK Button
                dialogNotApproved.setButton(Dialog.BUTTON_POSITIVE, getString(android.R.string.ok),
                        (dialog, which) -> dialogNotApproved.dismiss());

                // Showing Alert Message
                dialogNotApproved.show();
            } catch (Exception e) {
                Log.e("Error in showing dialog", e.getMessage());
            }
        }
    }

    @SuppressWarnings("CanBeFinal")
    private
    SyncStatusObserver mSyncStatusObserver = which -> {
        Account account = GenericAccountService.GetAccount(ACCOUNT, ACCOUNT_TYPE);

        ContentResolver.isSyncActive(account, SyncAdapterUtils.AUTHORITY);
        ContentResolver.isSyncPending(account, SyncAdapterUtils.AUTHORITY);
    };

    @Override
    public void onSyncButtonClicked() {
        if (presenter != null && Util.isConnected(context)) {
            isSyncRequired = true;
            Util.removeDatabaseRecords(true);
            Util.setSubmittedFormsLoaded(false);
            presenter.getUserProfile();
        }
    }
}
