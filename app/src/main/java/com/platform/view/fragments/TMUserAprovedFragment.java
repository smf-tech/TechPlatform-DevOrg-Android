package com.platform.view.fragments;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.listeners.TMTaskListener;
import com.platform.models.tm.PendingApprovalsRequest;
import com.platform.models.tm.PendingRequest;
import com.platform.presenter.RejectedFragmentPresenter;
import com.platform.presenter.TMUserAprovedFragmentPresenter;
import com.platform.utility.Constants;
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

@SuppressWarnings("WeakerAccess")
public class TMUserAprovedFragment extends Fragment implements View.OnClickListener,
        TMTaskListener, TMPendingApprovalPageRecyclerAdapter.OnRequestItemClicked, TMFiltersListActivity.OnFilterSelected {

    private View tmFragmentView;
    private TextView txtNoData;
    //private ExpandableListView rvPendingRequests;
    private RecyclerView rvPendingRequests;
    private TMUserAprovedFragmentPresenter pendingFragmentPresenter;
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

        pendingFragmentPresenter = new TMUserAprovedFragmentPresenter(this);
        //pendingFragmentPresenter.getAllPendingRequests();
        // check for method in NotificationsFragmentPresenter
        init();
    }

    public void init() {
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
            viewAllApprovals.setVisibility(View.GONE);
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
        Util.showToast(getString(R.string.status_update_success), getActivity());
        this.pendingRequestList.remove(pendingRequest);

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
        PendingApprovalsRequest pendingRequest = pendingRequestList.get(pos);
        showActionPopUp(pendingRequest);
    }

    private void showActionPopUp(final PendingApprovalsRequest pendingRequest) {
        if (getFragmentManager() == null) {
            return;
        }
        Toast.makeText(getActivity(), "" + pendingRequest.getType() + " " + pendingRequest.get_id(), Toast.LENGTH_SHORT).show();
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
        preferenceHelper.insertString(PreferenceHelper.IS_PENDING,PreferenceHelper.NON_PENDING);

        startActivity(intent);

    }

    /*@Override
    public void onFilterButtonClicked(JSONObject requestJson) {
        Toast.makeText(getActivity(), "Filter applied in activtiy", Toast.LENGTH_SHORT).show();
    }*/
    public void onFilterButtonApplied(JSONObject requestJson) {
        Toast.makeText(getActivity(), "Filter applied in activtiy", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFilterButtonClicked(JSONObject requestobject) {
        this.pendingRequestList.clear();
        if (mAdapter!=null) {
            mAdapter.notifyDataSetChanged();
        }
        Toast.makeText(getActivity(), "Filter applied in activtiy using interface", Toast.LENGTH_SHORT).show();
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
                        .setText(Util.getDateFromTimestamp(sPendingRequest.getCreatedDateTime(), Constants.FORM_DATE));

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


//old
/*extends Fragment implements TMTaskListener {

    private View tmFragmentView;
    private TextView txtNoData;
    private ExpandableListView elvRejectedRequests;
    private RejectedFragmentPresenter presenter;

    private boolean isVisible;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        tmFragmentView = inflater.inflate(R.layout.fragment_dashboard_tm, container, false);
        return tmFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new RejectedFragmentPresenter(this);
        presenter.getAllRejectedRequests();

        init();
    }

    private void init() {
        txtNoData = tmFragmentView.findViewById(R.id.txt_no_data);
        elvRejectedRequests = tmFragmentView.findViewById(R.id.rv_dashboard_tm);

        tmFragmentView.findViewById(R.id.txt_view_all_approvals).setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isVisible) {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (presenter == null) {
                    presenter = new RejectedFragmentPresenter(TMUserRejectedFragment.this);
                }
                presenter.getAllRejectedRequests();
            }, 100);
        }
    }

    @Override
    public void showPendingRequests(List<PendingRequest> pendingRequestList) {
        if (pendingRequestList != null && !pendingRequestList.isEmpty()) {
            txtNoData.setVisibility(View.GONE);
            elvRejectedRequests.setVisibility(View.VISIBLE);

            HashMap<String, List<PendingRequest>> rejectedRequestMap = new HashMap<>();
            rejectedRequestMap.put(USER_APPROVALS, pendingRequestList);
            TMRejectedAdapter newTMAdapter = new TMRejectedAdapter(getContext(), rejectedRequestMap, presenter);
            elvRejectedRequests.setAdapter(newTMAdapter);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
            elvRejectedRequests.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateRequestStatus(String response, PendingRequest pendingRequest) {

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
}
*/