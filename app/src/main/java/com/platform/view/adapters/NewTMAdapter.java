package com.platform.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.tm.PendingRequest;
import com.platform.presenter.TMFragmentPresenter;
import com.platform.utility.Constants;

import java.util.List;

@SuppressWarnings("CanBeFinal")
public class NewTMAdapter extends RecyclerView.Adapter<NewTMAdapter.PendingRequestViewHolder> {
    private List<PendingRequest> pendingRequestList;
    private TMFragmentPresenter tmFragmentPresenter;

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

    public NewTMAdapter(List<PendingRequest> pendingRequestList, TMFragmentPresenter tmFragmentPresenter) {
        this.pendingRequestList = pendingRequestList;
        this.tmFragmentPresenter = tmFragmentPresenter;
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
        pendingRequestViewHolder.txtRequestTitle.setText(String.format("%s %s",
                pendingRequest.getRequesterFirstName(), pendingRequest.getRequesterLastName()));
        pendingRequestViewHolder.txtRequestCreatedAt.setText(String.format("On %s", pendingRequest.getCreatedAt()));

        pendingRequestViewHolder.ivApprove.setOnClickListener(
                v -> tmFragmentPresenter.approveRejectRequest(Constants.RequestStatus.APPROVED, pendingRequest));

        pendingRequestViewHolder.ivReject.setOnClickListener(
                v -> tmFragmentPresenter.approveRejectRequest(Constants.RequestStatus.REJECTED, pendingRequest));
    }

    @Override
    public int getItemCount() {
        return pendingRequestList.size();
    }
}
