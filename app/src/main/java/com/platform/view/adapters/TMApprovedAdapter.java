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
public class TMApprovedAdapter extends RecyclerView.Adapter<TMApprovedAdapter.ApprovedRequestViewHolder> {

    private List<PendingRequest> pendingRequestList;
    private ApprovedFragmentPresenter approvedFragmentPresenter;

    class ApprovedRequestViewHolder extends RecyclerView.ViewHolder {

        TextView txtRequestTitle, txtRequestCreatedAt;
        ImageView ivApprove, ivReject;

        ApprovedRequestViewHolder(View view) {
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
    public ApprovedRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_pending_requests_card_view, parent, false);

        return new ApprovedRequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ApprovedRequestViewHolder approvedRequestViewHolder, int position) {

        PendingRequest pendingRequest = pendingRequestList.get(position);

        approvedRequestViewHolder.txtRequestTitle.setText(String.format("%s",
                pendingRequest.getEntity().getUserInfo().getUserName()));

        approvedRequestViewHolder.txtRequestCreatedAt.setText(String.format("On %s",
                pendingRequest.getCreatedDateTime()));

        approvedRequestViewHolder.ivApprove.setVisibility(View.GONE);
        approvedRequestViewHolder.ivApprove.setOnClickListener(
                v -> approvedFragmentPresenter
                        .approveRejectRequest(Constants.RequestStatus.APPROVED, pendingRequest));

        approvedRequestViewHolder.ivReject.setVisibility(View.GONE);
        approvedRequestViewHolder.ivReject.setOnClickListener(
                v -> approvedFragmentPresenter
                        .approveRejectRequest(Constants.RequestStatus.REJECTED, pendingRequest));
    }

    @Override
    public int getItemCount() {
        return pendingRequestList.size();
    }
}
