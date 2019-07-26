package com.platform.view.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.listeners.TMTaskListener;
import com.platform.models.tm.PendingApprovalsRequest;
import com.platform.models.tm.PendingRequest;
import com.platform.presenter.PendingFragmentPresenter;
import com.platform.utility.PreferenceHelper;
import com.platform.utility.Util;
import com.platform.view.activities.TMFiltersListActivity;
import com.platform.view.activities.TMUserProfileListActivity;
import com.platform.view.adapters.PendingApprovalsListAdapter;
import com.platform.view.adapters.TMPendingApprovalPageRecyclerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CanBeFinal")
public class TMUserPendingFragment extends Fragment implements View.OnClickListener,
        TMTaskListener, TMPendingApprovalPageRecyclerAdapter.OnRequestItemClicked, TMFiltersListActivity.OnFilterSelected {
    private ProgressBar progressBar;
    private RelativeLayout progressBarLayout;
    private View tmFragmentView;
    private TextView txtNoData;
    //private ExpandableListView rvPendingRequests;
    private RecyclerView rvPendingRequests;
    private PendingFragmentPresenter pendingFragmentPresenter;
    private boolean mShowAllApprovalsText = true;
    private TMPendingApprovalPageRecyclerAdapter mAdapter;
    private JSONObject filterRequestObject;

    private List<PendingApprovalsRequest> pendingRequestList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() != null && getArguments() != null) {
            String title = (String) getArguments().getSerializable("TITLE");
            if (TextUtils.isEmpty(title)) {
                title = getString(R.string.approvals);
            }
            /*((HomeActivity) getActivity()).setActionBarTitle(title);
            ((HomeActivity) getActivity()).setSyncButtonVisibility(false);*/
            ((TMFiltersListActivity) getActivity()).setFilterClickListener(this);
            mShowAllApprovalsText = getArguments().getBoolean("SHOW_ALL", true);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        tmFragmentView = inflater.inflate(R.layout.fragment_approvals_pending, container, false);
        return tmFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pendingFragmentPresenter = new PendingFragmentPresenter(this);
        //pendingFragmentPresenter.getAllPendingRequests();
        // check for method in NotificationsFragmentPresenter
        init();
    }

    public void init() {
        progressBarLayout = tmFragmentView.findViewById(R.id.gen_frag_progress_bar);
        progressBar = tmFragmentView.findViewById(R.id.pb_gen_form_fragment);
        txtNoData = tmFragmentView.findViewById(R.id.txt_no_data);
        txtNoData.setText(getString(R.string.msg_no_pending_req));

        rvPendingRequests = tmFragmentView.findViewById(R.id.rv_pendingapprovalpageview);
        //rvPendingRequests.setGroupIndicator(null);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        rvPendingRequests.setLayoutManager(layoutManager);
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
    public <T> void showNextScreen(T data) {

    }

    @Override
    public void showErrorMessage(String result) {

    }


    public void showPendingApprovalRequests(List<PendingApprovalsRequest> pendingRequestList) {
        if (!pendingRequestList.isEmpty()) {
            DashboardFragment.setApprovalCount(pendingRequestList.size());

            txtNoData.setVisibility(View.GONE);
            rvPendingRequests.setVisibility(View.VISIBLE);

            this.pendingRequestList.clear();
            this.pendingRequestList.addAll(pendingRequestList);



            /*mAdapter = new TMPendingApprovalPageRecyclerAdapter(getActivity(), pendingRequestList,
                    pendingFragmentPresenter, this);*/
            mAdapter = new TMPendingApprovalPageRecyclerAdapter(getActivity(), this.pendingRequestList,
                    this);
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
    public void showPendingRequests(List<PendingRequest> pendingRequestList) {

    }

    @Override
    public void updateRequestStatus(String response, PendingRequest pendingRequest) {

        this.pendingRequestList.remove(pendingRequest);

        this.mAdapter.notifyDataSetChanged();

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
        PendingApprovalsRequest pendingRequest = pendingRequestList.get(pos);
        showActionPopUp(pendingRequest);
    }

    private void showActionPopUp(final PendingApprovalsRequest pendingRequest) {
        if (getFragmentManager() == null) {
            return;
        }

        /*FragmentTransaction ft = getFragmentManager().beginTransaction();
        ApprovalDialogFragment approvalDialogFragment =
                ApprovalDialogFragment.newInstance(pendingRequest, mAdapter);
        approvalDialogFragment.show(ft, "dialog");*/
        try {
            filterRequestObject.put("user_id", pendingRequest.get_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Intent intent = new Intent(getActivity(), TMUserProfileListActivity.class);
        intent.putExtra("filter_type", pendingFragmentPresenter.getSelectedFiltertype());
        intent.putExtra("filter_type_request", filterRequestObject.toString());
        PreferenceHelper preferenceHelper = new PreferenceHelper(getActivity());
        preferenceHelper.insertString(PreferenceHelper.IS_PENDING, PreferenceHelper.IS_PENDING);
        startActivity(intent);

    }

    public void onFilterButtonApplied(JSONObject requestJson) {

    }

    @Override
    public void onFilterButtonClicked(JSONObject requestobject) {
        this.pendingRequestList.clear();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

        filterRequestObject = requestobject;
        pendingFragmentPresenter.getAllPendingRequests(requestobject);
    }

    public static class ApprovalDialogFragment extends DialogFragment {

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
                        .setText(sPendingRequest.getEntity().getUser().getUserName());
                ((TextView) view.findViewById(R.id.txt_pending_request_created_at))
                        .setText(Util.getDateTimeFromTimestamp(sPendingRequest.getCreatedDateTime()));

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
