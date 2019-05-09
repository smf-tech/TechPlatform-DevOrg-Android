package com.platform.view.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.tm.PendingRequest;
import com.platform.presenter.PendingFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("CanBeFinal")
public class PendingApprovalsListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private Map<String, List<PendingRequest>> mPendingRequestMap;
    private PendingFragmentPresenter pendingFragmentPresenter;
    private OnRequestItemClicked clickListener;

    public PendingApprovalsListAdapter(final Context context, final Map<String, List<PendingRequest>>
            pendingRequestList, final PendingFragmentPresenter pendingFragmentPresenter,
                                       final OnRequestItemClicked clickListener) {

        mContext = context;
        mPendingRequestMap = pendingRequestList;
        this.pendingFragmentPresenter = pendingFragmentPresenter;
        this.clickListener = clickListener;
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
    public View getGroupView(final int groupPosition, final boolean isExpanded,
                             final View convertView, final ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_pending_approvals_item,
                parent, false);

        ArrayList<String> list = new ArrayList<>(mPendingRequestMap.keySet());
        String cat = list.get(groupPosition);

        List<PendingRequest> processData = mPendingRequestMap.get(cat);
        int size = 0;
        if (processData != null) {
            size = processData.size();
        }

        ((TextView) view.findViewById(R.id.form_title)).setText(cat);
        ((TextView) view.findViewById(R.id.form_count))
                .setText(String.format("%s %s", String.valueOf(size), mContext.getString(R.string.requests)));

        ImageView v = view.findViewById(R.id.form_image);
        if (isExpanded) {
            Util.rotateImage(180f, v);
        } else {
            Util.rotateImage(0f, v);
        }

        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             final boolean isLastChild, final View convertView, final ViewGroup parent) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.row_pending_requests_card_view, parent, false);

        ArrayList<String> list = new ArrayList<>(mPendingRequestMap.keySet());
        String cat = list.get(groupPosition);

        List<PendingRequest> requests = mPendingRequestMap.get(cat);
        if (requests != null) {
            PendingRequest data = requests.get(childPosition);

            if (data != null && data.getEntity() != null && data.getEntity().getUser() != null) {
                ((TextView) view.findViewById(R.id.txt_pending_request_title))
                        .setText(data.getEntity().getUser().getUserName());
                ((TextView) view.findViewById(R.id.txt_pending_request_created_at))
                        .setText(Util.getDateFromTimestamp(data.getCreatedDateTime()));
            }

            view.setOnClickListener(v -> clickListener.onItemClicked(childPosition));

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
