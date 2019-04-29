package com.platform.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.platform.R;
import com.platform.listeners.TMTaskListener;
import com.platform.models.tm.PendingRequest;
import com.platform.presenter.PendingFragmentPresenter;
import com.platform.utility.Util;
import com.platform.view.activities.HomeActivity;
import com.platform.view.adapters.PendingApprovalsListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.platform.utility.Constants.TM.USER_APPROVALS;

@SuppressWarnings("CanBeFinal")
public class TMUserPendingFragment extends Fragment implements View.OnClickListener,
        TMTaskListener, PendingApprovalsListAdapter.OnRequestItemClicked {

    private View tmFragmentView;
    private TextView txtNoData;
    private ExpandableListView rvPendingRequests;
    private PendingFragmentPresenter pendingFragmentPresenter;
    private boolean mShowAllApprovalsText = true;
    private PendingApprovalsListAdapter mAdapter;

    private Map<String, List<PendingRequest>> map = new HashMap<>();
    private List<PendingRequest> pendingRequestList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            if (TextUtils.isEmpty(title)) {
                title = getString(R.string.approvals);
            }
            ((HomeActivity) getActivity()).setActionBarTitle(title);
            ((HomeActivity) getActivity()).setSyncButtonVisibility(false);

            mShowAllApprovalsText = getArguments().getBoolean("SHOW_ALL", true);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        tmFragmentView = inflater.inflate(R.layout.fragment_dashboard_tm, container, false);
        return tmFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pendingFragmentPresenter = new PendingFragmentPresenter(this);
        pendingFragmentPresenter.getAllPendingRequests();

        init();
    }

    private void init() {
        txtNoData = tmFragmentView.findViewById(R.id.txt_no_data);
        txtNoData.setText(getString(R.string.msg_no_pending_req));

        rvPendingRequests = tmFragmentView.findViewById(R.id.rv_dashboard_tm);
        rvPendingRequests.setGroupIndicator(null);

        final TextView viewAllApprovals = tmFragmentView.findViewById(R.id.txt_view_all_approvals);
        viewAllApprovals.setOnClickListener(this);

        if (!mShowAllApprovalsText) {
            viewAllApprovals.setVisibility(View.GONE);
        } else {
            viewAllApprovals.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt_view_all_approvals) {
            Util.launchFragment(new TMUserApprovalsFragment(), getContext(),
                    getString(R.string.approvals), true);
        }
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public <T> void showNextScreen(T data) {

    }

    @Override
    public void showErrorMessage(String result) {

    }

    @Override
    public void showPendingRequests(List<PendingRequest> pendingRequestList) {
        if (!pendingRequestList.isEmpty()) {
            DashboardFragment.setApprovalCount(pendingRequestList.size());

            txtNoData.setVisibility(View.GONE);
            rvPendingRequests.setVisibility(View.VISIBLE);

            this.pendingRequestList.clear();
            this.pendingRequestList.addAll(pendingRequestList);

            map.clear();
            map.put(USER_APPROVALS, pendingRequestList);

            mAdapter = new PendingApprovalsListAdapter(getContext(), map,
                    pendingFragmentPresenter, this);
            rvPendingRequests.setAdapter(mAdapter);
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
    public void updateRequestStatus(String response, PendingRequest pendingRequest) {
        Util.showToast(getString(R.string.status_update_success), getActivity());
        this.pendingRequestList.remove(pendingRequest);

        map.clear();
        map.put(USER_APPROVALS, pendingRequestList);
        mAdapter.notifyDataSetChanged();

        if (pendingRequestList != null && !pendingRequestList.isEmpty()) {
            DashboardFragment.setApprovalCount(this.pendingRequestList.size());
            txtNoData.setVisibility(View.GONE);
        } else {
            DashboardFragment.setApprovalCount(0);
            rvPendingRequests.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
            txtNoData.setText(getString(R.string.msg_no_pending_req));
        }

        if (getParentFragment() != null && getParentFragment() instanceof DashboardFragment) {
            ((DashboardFragment) getParentFragment()).updateBadgeCount();
        }
    }

    @Override
    public void onItemClicked(int pos) {
        PendingRequest pendingRequest = pendingRequestList.get(pos);
        showActionPopUp(pendingRequest);
    }

    private void showActionPopUp(final PendingRequest pendingRequest) {
        if (getFragmentManager() == null) {
            return;
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ApprovalDialogFragment approvalDialogFragment =
                ApprovalDialogFragment.newInstance(pendingRequest, mAdapter);
        approvalDialogFragment.show(ft, "dialog");

    }

    static class ApprovalDialogFragment extends DialogFragment {

        private static PendingRequest sPendingRequest;
        @SuppressLint("StaticFieldLeak")
        private static PendingApprovalsListAdapter sAdapter;

        private static ApprovalDialogFragment newInstance(PendingRequest pendingRequest,
                                                          PendingApprovalsListAdapter adapter) {
            sPendingRequest = pendingRequest;
            sAdapter = adapter;
            return new ApprovalDialogFragment();
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_pending_approval, container, false);

            if (sPendingRequest != null) {
                ((TextView) view.findViewById(R.id.txt_pending_request_title))
                        .setText(sPendingRequest.getEntity().getUserInfo().getUserName());
                ((TextView) view.findViewById(R.id.txt_pending_request_created_at))
                        .setText(Util.getDateFromTimestamp(sPendingRequest.getCreatedDateTime()));

                view.findViewById(R.id.iv_approve_request).setOnClickListener(v1 -> {
                    sAdapter.approveUserRequest(sPendingRequest);
                    dismiss();
                });
                view.findViewById(R.id.iv_reject_request).setOnClickListener(v1 -> {
                    sAdapter.rejectUserRequest(sPendingRequest);
                    dismiss();
                });
            }
            return view;
        }
    }
}
