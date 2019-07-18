package com.platform.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.tm.PendingRequest;
import com.platform.utility.Util;

import java.util.List;

@SuppressWarnings({"CanBeFinal", "SameParameterValue"})
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.PendingRequestViewHolder> {

    private NotificationsAdapter.OnRequestItemClicked clickListener;
    private List<PendingRequest> pendingRequestList;

    class PendingRequestViewHolder extends RecyclerView.ViewHolder {

        LinearLayout cardView;
        TextView txtRequestTitle, txtRequestCreatedAt;
        ImageView ivApprove, ivReject;

        PendingRequestViewHolder(View view) {
            super(view);

            cardView = view.findViewById(R.id.cv_pending_requests);
            txtRequestTitle = view.findViewById(R.id.txt_pending_request_title);
            txtRequestCreatedAt = view.findViewById(R.id.txt_pending_request_created_at);
            ivApprove = view.findViewById(R.id.iv_approve_request);
            ivReject = view.findViewById(R.id.iv_reject_request);
        }
    }

    public NotificationsAdapter(List<PendingRequest> pendingRequestList, OnRequestItemClicked clickListener) {
        this.pendingRequestList = pendingRequestList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public NotificationsAdapter.PendingRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_pending_requests_card_view, parent, false);

        return new NotificationsAdapter.PendingRequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.PendingRequestViewHolder holder, int position) {

        PendingRequest pendingRequest = pendingRequestList.get(position);
        holder.txtRequestTitle.setText(String.format("%s", pendingRequest.getEntity().getUser().getUserName()));
        holder.txtRequestCreatedAt.setText(String.format("%s",
                Util.getDateTimeFromTimestamp(pendingRequest.getCreatedDateTime())));
        holder.cardView.setOnClickListener(view1 -> clickListener.onItemClicked());

        holder.ivApprove.setVisibility(View.GONE);
        holder.ivReject.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return pendingRequestList.size();
    }

    public interface OnRequestItemClicked {
        void onItemClicked();
    }
}