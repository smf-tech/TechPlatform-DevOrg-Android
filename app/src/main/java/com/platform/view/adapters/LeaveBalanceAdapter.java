package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.leaves.LeaveDetail;
import com.platform.utility.Constants;
import com.platform.view.fragments.LeaveApplyFragment;

import java.util.ArrayList;
import java.util.List;

public class LeaveBalanceAdapter extends RecyclerView.Adapter<LeaveBalanceAdapter.MyViewHolder> {
    LeaveApplyFragment context;
    List<LeaveDetail> leave;
    String adapterType;
    ArrayList<Integer> leaveBackground;

    public LeaveBalanceAdapter(List<LeaveDetail> leave, String adapterType) {
        this.leave = leave;
        this.adapterType = adapterType;
    }

    public LeaveBalanceAdapter(LeaveApplyFragment Context, List<LeaveDetail> leave, ArrayList<Integer> leaveBackground, String adapterType) {
        this.context = Context;
        this.leave = leave;
        this.adapterType = adapterType;
        this.leaveBackground = leaveBackground;
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
        if(adapterType.equalsIgnoreCase("LeaveBalance")){
            holder.tvLeaveCount.setText(leave.get(position).getBalance());
        }
        holder.tvLeaveType.setText(leave.get(position).getType());
        if(adapterType.equalsIgnoreCase("Category")){
            holder.tvLeaveType.setTextColor(context.getResources().getColor(R.color.leave_form_text_color));
            holder.tvLeaveCount.setVisibility(View.GONE);

            if(leaveBackground!=null && leaveBackground.size()==leave.size()) {
                holder.leaveBalanceLayout.setBackgroundResource(leaveBackground.get(position));
                if(leaveBackground.get(position)==(R.drawable.leave_form_view_focused)){
                    holder.tvLeaveType.setTextColor(context.getResources().getColor(R.color.white));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return leave.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leaveBalanceLayout;
        TextView tvLeaveCount,tvLeaveType;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            leaveBalanceLayout = itemView.findViewById(R.id.leave_balance_layout);
            tvLeaveCount = itemView.findViewById(R.id.tv_leaves_count);
            tvLeaveType = itemView.findViewById(R.id.tv_leaves_type);
            if(adapterType.equalsIgnoreCase("Category")) {
                if (!context.isEdit){
                    leaveBalanceLayout.setOnClickListener(v -> {
                        for (int i = 0; i < leaveBackground.size(); i++) {
                            if (i == getAdapterPosition()) {
                                leaveBackground.remove(i);
                                leaveBackground.add(i, R.drawable.leave_form_view_focused);
                                context.selectedLeaveCatgory = leave.get(i).getType();
                            } else {
                                leaveBackground.remove(i);
                                leaveBackground.add(i, R.drawable.leave_form_view_unfocused);
                            }
                        }
                        notifyDataSetChanged();
                    });
                }
            }
        }
    }
}
