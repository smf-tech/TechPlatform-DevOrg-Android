package com.platform.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.tm.PendingRequest;
import com.platform.presenter.ApprovedFragmentPresenter;
import com.platform.utility.Constants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("CanBeFinal")
public class TMApprovedAdapter extends RecyclerView.Adapter<TMApprovedAdapter.PendingRequestViewHolder> {

    private List<PendingRequest> pendingRequestList;
    private ApprovedFragmentPresenter approvedFragmentPresenter;

    class PendingRequestViewHolder extends RecyclerView.ViewHolder {
        TextView txtRequestTitle, txtRequestCreatedAt;
        ImageView ivApprove, ivReject;

        PendingRequestViewHolder(View view) {
            super(view);
            txtRequestTitle = view.findViewById(R.id.txt_pending_request_title);
            txtRequestCreatedAt = view.findViewById(R.id.txt_pending_request_created_at);
            ivApprove = view.findViewById(R.id.iv_approve_request);
            ivReject = view.findViewById(R.id.iv_reject_request);
        }
    }

    public TMApprovedAdapter(List<PendingRequest> pendingRequestList, ApprovedFragmentPresenter presenter) {
        this.pendingRequestList = pendingRequestList;
        this.approvedFragmentPresenter = presenter;
    }

    @NonNull
    @Override
    public PendingRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_pending_requests_card_view, parent, false);

        return new PendingRequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingRequestViewHolder pendingRequestViewHolder, int position) {

        PendingRequest pendingRequest = pendingRequestList.get(position);

        pendingRequestViewHolder.txtRequestTitle.setText(String.format("%s",
                pendingRequest.getEntity().getUserInfo().getUserName()));

        pendingRequestViewHolder.txtRequestCreatedAt.setText(String.format("On %s",
                pendingRequest.getCreatedDateTime()));

        pendingRequestViewHolder.ivApprove.setVisibility(View.GONE);
        pendingRequestViewHolder.ivApprove.setOnClickListener(
                v -> approvedFragmentPresenter.approveRejectRequest(Constants.RequestStatus.APPROVED, pendingRequest));

        pendingRequestViewHolder.ivReject.setVisibility(View.GONE);
        pendingRequestViewHolder.ivReject.setOnClickListener(
                v -> approvedFragmentPresenter.approveRejectRequest(Constants.RequestStatus.REJECTED, pendingRequest));
    }

    @Override
    public int getItemCount() {
        return pendingRequestList.size();
    }
}
