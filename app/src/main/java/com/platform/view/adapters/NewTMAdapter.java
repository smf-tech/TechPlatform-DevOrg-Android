package com.platform.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.platform.R;
import com.platform.models.tm.PendingRequest;

import java.util.List;

@SuppressWarnings("CanBeFinal")
public class NewTMAdapter extends RecyclerView.Adapter<NewTMAdapter.MyViewHolder> {
    private List<PendingRequest> pendingRequestList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView prTitle, prCreatedAt;

        MyViewHolder(View view) {
            super(view);
            prTitle = view.findViewById(R.id.txt_pending_request_title);
            prCreatedAt = view.findViewById(R.id.txt_pending_request_created_at);
        }
    }

    public NewTMAdapter(List<PendingRequest> pendingRequestList) {
        this.pendingRequestList = pendingRequestList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_pending_requests_card_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        PendingRequest pendingRequest = pendingRequestList.get(position);
        myViewHolder.prTitle.setText(pendingRequest.getRequesterFirstName() + " " + pendingRequest.getRequesterLastName());
        myViewHolder.prCreatedAt.setText("On " + pendingRequest.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return pendingRequestList.size();
    }
}
