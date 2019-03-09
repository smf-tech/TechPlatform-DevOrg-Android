package com.platform.view.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private Context mContext;

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
                        PendingFragmentPresenter pendingFragmentPresenter, OnRequestItemClicked clickListener, final Context context) {

        this.pendingRequestList = pendingRequestList;
        this.pendingFragmentPresenter = pendingFragmentPresenter;
        this.clickListener = clickListener;
        mContext = context;
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

//        alertDialog.setNegativeButton(mContext.getString(R.string.no),
//                (dialogInterface, i) -> );

        /*alertDialog.setPositiveButton(mContext.getString(R.string.yes),
                (dialogInterface, i) -> {
                    if (comment.getText().toString().trim().isEmpty()) {
                        comment.setError(mContext.getString(R.string.msg_error_rejection_comment_needed));
                        dialogInterface.cancel();
                    } else {
                        pendingRequest.setReason(comment.getText().toString());
                        dialogInterface.dismiss();
                        pendingFragmentPresenter.approveRejectRequest(Constants.RequestStatus.REJECTED, pendingRequest);
                    }
                });*/

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
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(view -> {
                dialogInterface.dismiss();
            });
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return pendingRequestList.size();
    }

    public interface OnRequestItemClicked {
        void onItemClicked(int pos);
    }
}
