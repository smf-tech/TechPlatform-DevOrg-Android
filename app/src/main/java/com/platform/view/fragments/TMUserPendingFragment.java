package com.platform.view.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;
import com.platform.listeners.TMTaskListener;
import com.platform.models.tm.PendingRequest;
import com.platform.presenter.PendingFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.activities.HomeActivity;
import com.platform.view.adapters.NewTMAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TMUserPendingFragment extends Fragment implements View.OnClickListener,
        TMTaskListener, NewTMAdapter.OnRequestItemClicked {

    private View tmFragmentView;
    private TextView txtNoData;
    private ExpandableListView rvPendingRequests;
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
        rvPendingRequests = tmFragmentView.findViewById(R.id.rv_dashboard_tm);
        rvPendingRequests.setGroupIndicator(null);

        ArrayList<PendingRequest> pendingRequestList = new ArrayList<>();
        PendingRequest pendingRequest = new PendingRequest();
        pendingRequestList.add(pendingRequest);

        Map<String, List<PendingRequest>> map = new HashMap<>();
        map.put("", pendingRequestList);
        rvPendingRequests.setAdapter(new PendingApprovalsListAdapter(getContext(),
                map, pendingFragmentPresenter));

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        rvPendingRequests.setLayoutManager(layoutManager);
//        rvPendingRequests.setItemAnimator(new DefaultItemAnimator());

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
                Util.launchFragment(new TMUserApprovalsFragment(), getContext(),
                        getString(R.string.approvals), true);
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
            DashboardFragment.setApprovalCount(pendingRequestList.size());

            txtNoData.setVisibility(View.GONE);
            rvPendingRequests.setVisibility(View.VISIBLE);

            this.pendingRequestList = pendingRequestList;
//            newTMAdapter = new NewTMAdapter(this.pendingRequestList, pendingFragmentPresenter, this, getContext());

            Map<String, List<PendingRequest>> map = new HashMap<>();
            map.put("", pendingRequestList);
            rvPendingRequests.setAdapter(new PendingApprovalsListAdapter(getContext(), map, pendingFragmentPresenter));
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
        newTMAdapter.notifyDataSetChanged();

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

    private static class PendingApprovalsListAdapter extends BaseExpandableListAdapter {

        private Context mContext;
        private Map<String, List<PendingRequest>> mPendingRequestMap;
        private PendingFragmentPresenter pendingFragmentPresenter;

        public PendingApprovalsListAdapter(final Context context, final Map<String, List<PendingRequest>>
                pendingRequestList, final PendingFragmentPresenter pendingFragmentPresenter) {
            mContext = context;
            mPendingRequestMap = pendingRequestList;
            this.pendingFragmentPresenter = pendingFragmentPresenter;
        }

        @Override
        public int getGroupCount() {
            return mPendingRequestMap.size();
        }

        @Override
        public int getChildrenCount(final int groupPosition) {
            ArrayList<String> list = new ArrayList<>(mPendingRequestMap.keySet());
            String cat = list.get(groupPosition);

            List<PendingRequest> processData = mPendingRequestMap.get(cat);
            if (processData != null) {
                return processData.size();
            }

            return 0;
        }

        @Override
        public Object getGroup(final int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(final int groupPosition, final int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(final int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(final int groupPosition, final int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(final int groupPosition, final boolean isExpanded, final View convertView, final ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pending_approvals_item, parent, false);

            ArrayList<String> list = new ArrayList<>(mPendingRequestMap.keySet());
            String cat = list.get(groupPosition);

            List<PendingRequest> processData = mPendingRequestMap.get(cat);
            int size = 0;
            if (processData != null) {
                size = processData.size();
            }

            ((TextView) view.findViewById(R.id.form_title)).setText(cat);
            ((TextView) view.findViewById(R.id.form_count)).setText(String.format("%s Forms", String.valueOf(size)));

            ImageView v = view.findViewById(R.id.form_image);
            if (isExpanded) {
                Util.rotateImage(180f, v);
            } else {
                Util.rotateImage(0f, v);
            }

            return view;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, final View convertView, final ViewGroup parent) {

            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.row_pending_requests_card_view, parent, false);

            ArrayList<String> list = new ArrayList<>(mPendingRequestMap.keySet());
            String cat = list.get(groupPosition);

            List<PendingRequest> processData = mPendingRequestMap.get(cat);
            if (processData != null) {
                PendingRequest data = processData.get(childPosition);

                if (data != null && data.getEntity() != null && data.getEntity().getUserInfo() != null) {
                    ((TextView) view.findViewById(R.id.txt_pending_request_title))
                            .setText(data.getEntity().getUserInfo().getUserName());
                    ((TextView) view.findViewById(R.id.txt_pending_request_created_at))
                            .setText(Util.getDateFromTimestamp(data.getCreatedDateTime()));
                }

                view.findViewById(R.id.iv_approve_request)
                        .setOnClickListener(v -> approveUserRequest(data));

                view.findViewById(R.id.iv_reject_request)
                        .setOnClickListener(v -> rejectUserRequest(data));
            }

            return view;
        }

        @Override
        public boolean isChildSelectable(final int groupPosition, final int childPosition) {
            return false;
        }

        public void approveUserRequest(PendingRequest pendingRequest) {
            pendingFragmentPresenter.approveRejectRequest(Constants.RequestStatus.APPROVED, pendingRequest);
        }

        public void rejectUserRequest(PendingRequest pendingRequest) {
            showReasonPopUp(pendingRequest);
        }

        private void showReasonPopUp(final PendingRequest pendingRequest) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
            alertDialog.setTitle(mContext.getString(R.string.app_name_ss));
            alertDialog.setMessage(mContext.getString(R.string.msg_rejection_reason));
            alertDialog.setIcon(R.mipmap.app_logo);
            alertDialog.setCancelable(false);

            EditText comment = new EditText(mContext);
            comment.setHint(R.string.msg_rejection_comment);
            comment.setInputType(InputType.TYPE_CLASS_TEXT);
            comment.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40));

            alertDialog.setView(comment);

            alertDialog.setPositiveButton(android.R.string.yes, null);
            alertDialog.setNegativeButton(android.R.string.no, null);

            AlertDialog dialog = alertDialog.create();
            dialog.setOnShowListener(dialogInterface -> {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
                    if (comment.getText().toString().trim().isEmpty()) {
                        comment.setError(mContext.getString(R.string.msg_error_rejection_comment_needed));
                    } else {
                        dialogInterface.dismiss();
                        pendingRequest.setReason(comment.getText().toString());
                        pendingFragmentPresenter.approveRejectRequest(Constants.RequestStatus.REJECTED, pendingRequest);
                    }
                });
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view -> dialogInterface.dismiss());
            });
            dialog.show();
        }

        public interface OnRequestItemClicked {
            void onItemClicked(int pos);
        }
    }
}
