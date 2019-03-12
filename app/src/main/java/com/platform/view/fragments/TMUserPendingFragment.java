package com.platform.view.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.platform.R;
import com.platform.listeners.TMTaskListener;
import com.platform.models.tm.PendingRequest;
import com.platform.presenter.PendingFragmentPresenter;
import com.platform.utility.Util;
import com.platform.view.activities.HomeActivity;
import com.platform.view.adapters.NewTMAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.platform.view.fragments.DashboardFragment.mApprovalCount;

public class TMUserPendingFragment extends Fragment implements View.OnClickListener, TMTaskListener, NewTMAdapter.OnRequestItemClicked {

    private View tmFragmentView;
    private TextView txtNoData;
    private RecyclerView rvPendingRequests;
    private PendingFragmentPresenter pendingFragmentPresenter;
    private NewTMAdapter newTMAdapter;
    private boolean mShowAllApprovalsText = true;

    private List<PendingRequest> pendingRequestList;

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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPendingRequests = tmFragmentView.findViewById(R.id.rv_dashboard_tm);
        rvPendingRequests.setLayoutManager(layoutManager);
        rvPendingRequests.setItemAnimator(new DefaultItemAnimator());

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
        switch (v.getId()) {
            case R.id.txt_view_all_approvals:
                Util.launchFragment(new TMUserApprovalsFragment(), getContext(), getString(R.string.approvals));
                break;
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
            mApprovalCount = pendingRequestList.size();

            txtNoData.setVisibility(View.GONE);
            rvPendingRequests.setVisibility(View.VISIBLE);

            this.pendingRequestList = pendingRequestList;
            newTMAdapter = new NewTMAdapter(this.pendingRequestList, pendingFragmentPresenter, this, getContext());
            rvPendingRequests.setAdapter(newTMAdapter);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
            txtNoData.setText(getString(R.string.msg_no_pending_req));
            rvPendingRequests.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateRequestStatus(String response, PendingRequest pendingRequest) {
        Util.showToast(getString(R.string.status_update_success), getActivity());
        this.pendingRequestList.remove(pendingRequest);
        newTMAdapter.notifyDataSetChanged();

        if (pendingRequestList != null && !pendingRequestList.isEmpty()) {
            txtNoData.setVisibility(View.GONE);
        } else {
            rvPendingRequests.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
            txtNoData.setText(getString(R.string.msg_no_pending_req));
        }
    }

    @Override
    public void onItemClicked(int pos) {
        PendingRequest pendingRequest = pendingRequestList.get(pos);
        showActionPopUp(pendingRequest);
    }

    private void showActionPopUp(final PendingRequest pendingRequest) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        // Setting Dialog Title
        alertDialog.setTitle("Approve Request");
        // Setting Dialog Message
        alertDialog.setMessage("Approve request for " + pendingRequest.getEntity().getUserInfo().getUserName());
        // Setting Icon to Dialog
        alertDialog.setIcon(R.mipmap.app_logo);
        // Setting CANCEL Button
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Reject",
                (dialog, which) -> newTMAdapter.rejectUserRequest(pendingRequest));
        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Approve",
                (dialog, which) -> newTMAdapter.approveUserRequest(pendingRequest));

        // Showing Alert Message
        alertDialog.show();
    }
}
