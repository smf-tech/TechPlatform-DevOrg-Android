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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("CanBeFinal")
public class NewTMAdapter extends RecyclerView.Adapter<NewTMAdapter.PendingRequestViewHolder> {

    private OnRequestItemClicked clickListener;
    private List<PendingRequest> pendingRequestList;
    private PendingFragmentPresenter pendingFragmentPresenter;

    class PendingRequestViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
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

    public NewTMAdapter(List<PendingRequest> pendingRequestList,
                        PendingFragmentPresenter pendingFragmentPresenter, OnRequestItemClicked clickListener) {

        this.pendingRequestList = pendingRequestList;
        this.pendingFragmentPresenter = pendingFragmentPresenter;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public PendingRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_pending_requests_card_view, parent, false);

        return new PendingRequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingRequestViewHolder holder, int position) {

        PendingRequest pendingRequest = pendingRequestList.get(position);
        holder.txtRequestTitle.setText(String.format("%s", pendingRequest.getEntity().getUserInfo().getUserName()));
        holder.txtRequestCreatedAt.setText(String.format("On %s", pendingRequest.getCreatedDateTime()));
        holder.cardView.setOnClickListener(view1 -> clickListener.onItemClicked(holder.getAdapterPosition()));

        holder.ivApprove.setOnClickListener(v -> approveUserRequest(pendingRequest));

        holder.ivReject.setOnClickListener(v -> rejectUserRequest(pendingRequest));
    }

    public void approveUserRequest(PendingRequest pendingRequest) {
        pendingFragmentPresenter.approveRejectRequest(Constants.RequestStatus.APPROVED, pendingRequest);
    }

    public void rejectUserRequest(PendingRequest pendingRequest) {
        pendingFragmentPresenter.approveRejectRequest(Constants.RequestStatus.REJECTED, pendingRequest);
    }

    @Override
    public int getItemCount() {
        return pendingRequestList.size();
    }

    public interface OnRequestItemClicked {
        void onItemClicked(int pos);
    }
}
