package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.leaves.LeaveDetail;

import java.util.List;

public class LeaveBalanceAdapter extends RecyclerView.Adapter<LeaveBalanceAdapter.MyViewHolder> {
    Context context;
    List<LeaveDetail> leave;
    String eventsLabel;

    public LeaveBalanceAdapter(Context context, List<LeaveDetail> leave) {
        this.context = context;
        this.leave = leave;

    }

    @NonNull
    @Override
    public LeaveBalanceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_each_leave, parent, false);
        return new LeaveBalanceAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveBalanceAdapter.MyViewHolder holder, int position) {
        holder.tvLeaveCount.setText(leave.get(position).getBalance());
        holder.tvLeaveType.setText(leave.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return leave.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLeaveCount,tvLeaveType;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeaveCount=itemView.findViewById(R.id.tv_leaves_count);
            tvLeaveType=itemView.findViewById(R.id.tv_leaves_type);
        }
    }
}
