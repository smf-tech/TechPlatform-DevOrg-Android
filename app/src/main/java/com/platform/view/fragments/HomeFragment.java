package com.platform.view.fragments;

import android.accounts.Account;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.platform.R;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.home.Home;
import com.platform.models.user.UserInfo;
import com.platform.presenter.HomeActivityPresenter;
import com.platform.syncAdapter.GenericAccountService;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.HomeActivity;
import com.platform.view.adapters.ViewPagerAdapter;

import static com.platform.syncAdapter.SyncAdapterUtils.ACCOUNT;
import static com.platform.syncAdapter.SyncAdapterUtils.ACCOUNT_TYPE;

public class HomeFragment extends Fragment implements PlatformTaskListener {

    private Home homeData;
    private View homeFragmentView;
    private HomeActivityPresenter presenter;
    private AlertDialog dialogNotApproved;
    private Object mSyncObserverHandle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        presenter = new HomeActivityPresenter(this);
        if (Util.isConnected(getActivity())) {
            getUserData();
        }

        homeFragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        return homeFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity()!=null) {
            ((HomeActivity) getActivity()).setActionBarTitle(
                    getActivity().getResources().getString(R.string.app_name_ss));
        }
        dialogNotApproved = new AlertDialog.Builder(getActivity()).create();
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
            dashboardFragment.setArguments(bundle);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(dashboardFragment, "Dashboard");
        adapter.addFragment(new StoriesFragment(), "Stories");
        adapter.addFragment(new ConnectFragment(), "Connect");
        viewPager.setAdapter(adapter);
    }

    private void getUserData() {
        if (presenter != null) {
            UserInfo user = Util.getUserObjectFromPref();
            presenter.getModules(user);
        }

        mSyncStatusObserver.onStatusChanged(0);

        // Watch for sync state changes
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);
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

            if (homeData.getUserApproveStatus().equalsIgnoreCase(Constants.PENDING)) {
                showApprovedDialog();
            }

            ViewPager viewPager = homeFragmentView.findViewById(R.id.home_view_pager);
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

        try {
            // Showing Alert Message
            dialogNotApproved.show();
        } catch (Exception e) {
            Log.e("Error in showing dialog", e.getMessage());
        }
    }

    @SuppressWarnings("CanBeFinal")
    private
    SyncStatusObserver mSyncStatusObserver = which -> {
        Account account = GenericAccountService.GetAccount(ACCOUNT, ACCOUNT_TYPE);

        ContentResolver.isSyncActive(account, SyncAdapterUtils.AUTHORITY);
        ContentResolver.isSyncPending(account, SyncAdapterUtils.AUTHORITY);
    };
}
