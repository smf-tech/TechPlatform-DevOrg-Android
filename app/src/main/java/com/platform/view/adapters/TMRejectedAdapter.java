package com.platform.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.tm.PendingRequest;
import com.platform.presenter.RejectedFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.Util;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("CanBeFinal")
public class TMRejectedAdapter extends RecyclerView.Adapter<TMRejectedAdapter.RejectedRequestViewHolder> {

    private List<PendingRequest> pendingRequestList;
    private RejectedFragmentPresenter rejectedFragmentPresenter;

    class RejectedRequestViewHolder extends RecyclerView.ViewHolder {

        TextView txtRequestTitle, txtRequestCreatedAt;
        ImageView ivApprove, ivReject;

        RejectedRequestViewHolder(View view) {
            super(view);

            txtRequestTitle = view.findViewById(R.id.txt_pending_request_title);
            txtRequestCreatedAt = view.findViewById(R.id.txt_pending_request_created_at);
            ivApprove = view.findViewById(R.id.iv_approve_request);
            ivReject = view.findViewById(R.id.iv_reject_request);
        }
    }

    public TMRejectedAdapter(List<PendingRequest> pendingRequestList, RejectedFragmentPresenter presenter) {
        this.pendingRequestList = pendingRequestList;
        this.rejectedFragmentPresenter = presenter;
    }

    @NonNull
    @Override
    public RejectedRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_pending_requests_card_view, parent, false);

        return new RejectedRequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RejectedRequestViewHolder rejectedRequestViewHolder, int position) {

        PendingRequest pendingRequest = pendingRequestList.get(position);

        rejectedRequestViewHolder.txtRequestTitle.setText(String.format("%s",
                pendingRequest.getEntity().getUserInfo().getUserName()));

        rejectedRequestViewHolder.txtRequestCreatedAt.setText(String.format("On %s",
                Util.getDateFromTimestamp(pendingRequest.getUpdatedDateTime())));

        rejectedRequestViewHolder.ivApprove.setVisibility(View.GONE);
        rejectedRequestViewHolder.ivApprove.setOnClickListener(
                v -> rejectedFragmentPresenter
                        .approveRejectRequest(Constants.RequestStatus.APPROVED, pendingRequest));

        rejectedRequestViewHolder.ivReject.setVisibility(View.GONE);
        rejectedRequestViewHolder.ivReject.setOnClickListener(
                v -> rejectedFragmentPresenter
                        .approveRejectRequest(Constants.RequestStatus.REJECTED, pendingRequest));
    }

    @Override
    public int getItemCount() {
        return pendingRequestList.size();
    }
}
