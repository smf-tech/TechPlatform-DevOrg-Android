package com.platform.view.fragments;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.platform.R;
import com.platform.listeners.TMTaskListener;
import com.platform.models.tm.PendingRequest;
import com.platform.presenter.NotificationsFragmentPresenter;
import com.platform.utility.AppEvents;
import com.platform.utility.Util;
import com.platform.view.activities.HomeActivity;
import com.platform.view.adapters.NotificationsAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment implements TMTaskListener,
        NotificationsAdapter.OnRequestItemClicked {

    private TextView txtNoData;
    private RecyclerView rvPendingRequests;
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            Context context = getActivity();
            String title = (String) getArguments().getSerializable("TITLE");
            if (TextUtils.isEmpty(title)) {
                title = getString(R.string.notifications);
            }

            if ((boolean) getArguments().getSerializable("SHOW_BACK")) {
                ((HomeActivity) getActivity()).showBackArrow();
            }

            ((HomeActivity) context).setActionBarTitle(title);
            ((HomeActivity) context).setSyncButtonVisibility(false);
        }

        AppEvents.trackAppEvent(getString(R.string.event_approvals_screen_visit));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtNoData = view.findViewById(R.id.txt_no_data);
        progressBarLayout = view.findViewById(R.id.notifications_progress_bar);
        progressBar = view.findViewById(R.id.pb_gen_notifications);

        rvPendingRequests = view.findViewById(R.id.notifications_list);
        rvPendingRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPendingRequests.setItemAnimator(new DefaultItemAnimator());

        NotificationsFragmentPresenter presenter = new NotificationsFragmentPresenter(this);
        presenter.getAllPendingRequests();
    }

    public void showPendingRequests(List<PendingRequest> pendingRequestList) {
        if (!pendingRequestList.isEmpty()) {
            DashboardFragment.setApprovalCount(pendingRequestList.size());

            txtNoData.setVisibility(View.GONE);
            rvPendingRequests.setVisibility(View.VISIBLE);

            NotificationsAdapter newTMAdapter = new NotificationsAdapter(pendingRequestList, this);
            rvPendingRequests.setAdapter(newTMAdapter);
        } else {
            DashboardFragment.setApprovalCount(0);
            txtNoData.setVisibility(View.VISIBLE);
            txtNoData.setText(getString(R.string.msg_no_pending_req));
            rvPendingRequests.setVisibility(View.GONE);
        }

        if (getParentFragment() != null && getParentFragment() instanceof DashboardFragment) {
            ((DashboardFragment) getParentFragment()).updateBadgeCount();
        }
    }

    @Override
    public void updateRequestStatus(final String response, final PendingRequest pendingRequest) {

    }

    @Override
    public void showProgressBar() {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideProgressBar() {
        if (getActivity() == null) return;

        getActivity().runOnUiThread(() -> {
            if (progressBarLayout != null && progressBar != null && progressBar.isShown()) {
                progressBar.setVisibility(View.GONE);
                progressBarLayout.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public <T> void showNextScreen(final T data) {

    }

    @Override
    public void showErrorMessage(final String result) {
        if (!TextUtils.isEmpty(result)) {
            Log.e("TAG", "showErrorMessage: " + result);
        }
    }

    @Override
    public void onItemClicked() {
        Util.launchFragment(new TMUserApprovalsFragment(), getActivity(),
                getString(R.string.approvals), true);
    }
}
