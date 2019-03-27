package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.tm.PendingRequest;
import com.platform.presenter.RejectedFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("CanBeFinal")
public class TMRejectedAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private Map<String, List<PendingRequest>> mApprovedRequestMap;
    private RejectedFragmentPresenter approvedFragmentPresenter;

    public TMRejectedAdapter(final Context context, final Map<String,
            List<PendingRequest>> approvedRequestMap, RejectedFragmentPresenter presenter) {

        this.mContext = context;
        this.mApprovedRequestMap = approvedRequestMap;
        this.approvedFragmentPresenter = presenter;
    }

    @Override
    public int getGroupCount() {
        return mApprovedRequestMap.size();
    }

    @Override
    public int getChildrenCount(final int groupPosition) {
        ArrayList<String> list = new ArrayList<>(mApprovedRequestMap.keySet());
        String cat = list.get(groupPosition);

        List<PendingRequest> processData = mApprovedRequestMap.get(cat);
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

        ArrayList<String> list = new ArrayList<>(mApprovedRequestMap.keySet());
        String cat = list.get(groupPosition);

        List<PendingRequest> processData = mApprovedRequestMap.get(cat);
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

        ArrayList<String> list = new ArrayList<>(mApprovedRequestMap.keySet());
        String cat = list.get(groupPosition);

        List<PendingRequest> requests = mApprovedRequestMap.get(cat);
        if (requests != null) {
            PendingRequest data = requests.get(childPosition);

            if (data != null && data.getEntity() != null && data.getEntity().getUserInfo() != null) {
                ((TextView) view.findViewById(R.id.txt_pending_request_title))
                        .setText(data.getEntity().getUserInfo().getUserName());
                ((TextView) view.findViewById(R.id.txt_pending_request_created_at))
                        .setText(String.format("On %s",
                                Util.getDateFromTimestamp(data.getUpdatedDateTime())));
            }

            view.findViewById(R.id.iv_approve_request).setVisibility(View.GONE);
            view.findViewById(R.id.iv_approve_request)
                    .setOnClickListener(v -> approvedFragmentPresenter
                            .approveRejectRequest(Constants.RequestStatus.APPROVED, data));

            view.findViewById(R.id.iv_reject_request).setVisibility(View.GONE);
            view.findViewById(R.id.iv_reject_request)
                    .setOnClickListener(v -> approvedFragmentPresenter
                            .approveRejectRequest(Constants.RequestStatus.REJECTED, data));
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(final int groupPosition, final int childPosition) {
        return false;
    }
}

