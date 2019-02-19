package com.platform.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.platform.R;
import com.platform.listeners.TMTaskListener;
import com.platform.models.tm.PendingRequest;
import com.platform.presenter.TMFragmentPresenter;
import com.platform.utility.Util;
import com.platform.view.activities.HomeActivity;
import com.platform.view.adapters.NewTMAdapter;

import java.util.List;

public class TMFragment extends Fragment implements View.OnClickListener, TMTaskListener {

    private boolean mShowAllApprovalsText;
    private View tmFragmentView;
    private RecyclerView rvPendingRequests;
    private TMFragmentPresenter tmFragmentPresenter;
    private NewTMAdapter newTMAdapter;
    private List<PendingRequest> pendingRequestList;
    private TextView txtNoData;

    public static TMFragment newInstance(boolean showAllApprovalsText) {
        Bundle args = new Bundle();
        args.putBoolean("showAllApprovalsText", showAllApprovalsText);
        TMFragment fragment = new TMFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mShowAllApprovalsText = getArguments()
                    .getBoolean("showAllApprovalsText", true);
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

        tmFragmentPresenter = new TMFragmentPresenter(this);
        tmFragmentPresenter.getAllPendingRequests();

        if (getActivity() != null) {
            ((HomeActivity) getActivity()).setActionBarTitle(
                    getActivity().getResources().getString(R.string.team_management));
        }

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
                Util.launchFragment(TMFragment.newInstance(false), getContext(), "teamsFragment");
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
        if (pendingRequestList != null && !pendingRequestList.isEmpty()) {
            txtNoData.setVisibility(View.GONE);
            rvPendingRequests.setVisibility(View.VISIBLE);
            this.pendingRequestList = pendingRequestList;
            newTMAdapter = new NewTMAdapter(this.pendingRequestList, tmFragmentPresenter);
            rvPendingRequests.setAdapter(newTMAdapter);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
            rvPendingRequests.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateRequestStatus(String response, PendingRequest pendingRequest) {
        Util.showToast("Request status updated successfully", getActivity());
        this.pendingRequestList.remove(pendingRequest);
        newTMAdapter.notifyDataSetChanged();

        if (pendingRequestList != null && !pendingRequestList.isEmpty()) {
            txtNoData.setVisibility(View.GONE);
        } else {
            rvPendingRequests.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
        }
    }
}
