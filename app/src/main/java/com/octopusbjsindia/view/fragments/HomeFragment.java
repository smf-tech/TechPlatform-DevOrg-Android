package com.octopusbjsindia.view.fragments;

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
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.VolleyError;
import com.google.android.material.tabs.TabLayout;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.listeners.APIDataListener;
import com.octopusbjsindia.listeners.PlatformTaskListener;
import com.octopusbjsindia.models.home.Home;
import com.octopusbjsindia.models.home.HomeData;
import com.octopusbjsindia.models.home.Modules;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.presenter.HomeActivityPresenter;
import com.octopusbjsindia.syncAdapter.GenericAccountService;
import com.octopusbjsindia.syncAdapter.SyncAdapterUtils;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.HomeActivity;
import com.octopusbjsindia.view.adapters.ViewPagerAdapter;

import java.util.List;

import static com.octopusbjsindia.syncAdapter.SyncAdapterUtils.ACCOUNT;
import static com.octopusbjsindia.syncAdapter.SyncAdapterUtils.ACCOUNT_TYPE;
import static com.octopusbjsindia.utility.Constants.RequestStatus.APPROVED_MODULE;
import static com.octopusbjsindia.utility.Constants.RequestStatus.DEFAULT_MODULE;

public class HomeFragment extends Fragment implements PlatformTaskListener, APIDataListener, HomeActivity.OnSyncClicked {

    private final int[] tabIcons = {
            R.drawable.ic_home_24,
            R.drawable.ic_newspaper_24,
            R.drawable.ic_globe_24
    };
    private Home homeData;
    private Context context;
    private View homeFragmentView;
    private HomeActivityPresenter presenter;
    private AlertDialog dialogNotApproved;
    private Object mSyncObserverHandle;
    private boolean isSyncRequired;
    private FragmentManager childFragmentManager;

    @SuppressWarnings("CanBeFinal")
    private
    SyncStatusObserver mSyncStatusObserver = which -> {
        Account account = GenericAccountService.GetAccount(ACCOUNT, ACCOUNT_TYPE);

        ContentResolver.isSyncActive(account, SyncAdapterUtils.AUTHORITY);
        ContentResolver.isSyncPending(account, SyncAdapterUtils.AUTHORITY);
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null) {
            context = getActivity();
            ((HomeActivity) context).setSyncClickListener(this);
        }

        //String str = getResources().getString(R.string.task_title) + Util.getLocaleLanguageCode();
        dialogNotApproved = new AlertDialog.Builder(context).create();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        homeFragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        presenter = new HomeActivityPresenter(this);
        return homeFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        childFragmentManager = getChildFragmentManager();
        getUserData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constants.GET_MODELS) {
            getUserData();
            Constants.GET_MODELS = false;
        }
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

        if(getActivity() != null && isAdded()) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(childFragmentManager);
            adapter.addFragment(dashboardFragment, getString(R.string.tab_dashboard));
            adapter.addFragment(new StoriesFragment(), getString(R.string.tab_stories));
            adapter.addFragment(new ConnectFragment(), getString(R.string.tab_connect));
            viewPager.setAdapter(adapter);
        }
    }

    private void getUserData() {
        mSyncStatusObserver.onStatusChanged(0);

        // Watch for sync state changes
        isSyncRequired = false;
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);

        if (presenter != null && Util.isConnected(context)) {
            isSyncRequired = true;
            UserInfo user = Util.getUserObjectFromPref();
            presenter.getModules(user);
        }
        if(!Util.isConnected(context)) {
            initiateViewPager();
        }
    }

    private List<Modules> getModulesFromDatabase() {
        return DatabaseManager.getDBInstance(context.getApplicationContext()).getAllModules();
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        Util.showToast(getResources().getString(R.string.msg_something_went_wrong), this);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        Util.showToast(getResources().getString(R.string.msg_something_went_wrong), this);
    }

    @Override
    public void onSuccessListener(String requestID, String response) {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void closeCurrentActivity() {

    }

    public void initiateViewPager() {
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

            ViewPager viewPager = homeFragmentView.findViewById(R.id.home_view_pager);
            viewPager.setOffscreenPageLimit(3);
            setupViewPager(viewPager);

            TabLayout tabLayout = homeFragmentView.findViewById(R.id.home_tabs);
            tabLayout.setupWithViewPager(viewPager);

            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            tabLayout.getTabAt(2).setIcon(tabIcons[2]);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            ((HomeActivity) getActivity()).setActionBarTitle(Constants.Home.HOME);
                            break;

                        case 1:
                            ((HomeActivity) getActivity()).setActionBarTitle(Constants.Home.STORIES);
                            break;

                        case 2:
                            ((HomeActivity) getActivity()).setActionBarTitle(Constants.Home.CONNECT);
                            break;

                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
            ((HomeActivity) context).setActionBarTitle(getResources().getString(R.string.app_name_ss));
            //return;
        }
    }

    @Override
    public <T> void showNextScreen(T data) {
        if (data != null) {
            homeData = (Home) data;

            DatabaseManager.getDBInstance(Platform.getInstance()).deleteAllModules();

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
        }
        presenter.getRoleAccess();
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

    @Override
    public void onSyncButtonClicked() {
        if (presenter != null && Util.isConnected(context)) {
            isSyncRequired = true;
            Util.removeDatabaseRecords(true);
            Util.setSubmittedFormsLoaded(false);
            presenter.getUserProfile();
        }
    }

    public void getdynamicLogo() {
        if (Permissions.isCameraPermissionGranted(getActivity(), this)) {
            Util.downloadAndLoadIcon(getActivity());
        }
    }

}
