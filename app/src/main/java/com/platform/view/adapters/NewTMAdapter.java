package com.platform.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.tm.PendingRequest;
import com.platform.presenter.PendingFragmentPresenter;
import com.platform.utility.Constants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("CanBeFinal")
public class NewTMAdapter extends RecyclerView.Adapter<NewTMAdapter.PendingRequestViewHolder> {
    private List<PendingRequest> pendingRequestList;
    private PendingFragmentPresenter pendingFragmentPresenter;

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

    public NewTMAdapter(List<PendingRequest> pendingRequestList, PendingFragmentPresenter pendingFragmentPresenter) {
        this.pendingRequestList = pendingRequestList;
        this.pendingFragmentPresenter = pendingFragmentPresenter;
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
        pendingRequestViewHolder.txtRequestTitle.setText(String.format("%s", pendingRequest.getRequesterName()));
        pendingRequestViewHolder.txtRequestCreatedAt.setText(String.format("On %s", pendingRequest.getCreatedAt()));

        pendingRequestViewHolder.ivApprove.setOnClickListener(
                v -> pendingFragmentPresenter.approveRejectRequest(Constants.RequestStatus.APPROVED, pendingRequest));

        pendingRequestViewHolder.ivReject.setOnClickListener(
                v -> pendingFragmentPresenter.approveRejectRequest(Constants.RequestStatus.REJECTED, pendingRequest));
    }

    @Override
    public int getItemCount() {
        return pendingRequestList.size();
    }
}
