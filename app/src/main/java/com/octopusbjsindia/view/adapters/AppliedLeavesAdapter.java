package com.octopusbjsindia.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.leaves.LeaveData;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;

import java.util.List;

import static com.octopusbjsindia.utility.Constants.DAY_MONTH_YEAR;

@SuppressWarnings("CanBeFinal")
public class AppliedLeavesAdapter extends RecyclerView.Adapter<AppliedLeavesAdapter.ViewHolder> {
    private LeaveAdapterListener leavesListener;
    private List<LeaveData> leavesList;

    public AppliedLeavesAdapter(final List<LeaveData> leavesList, LeaveAdapterListener leavesListener) {
        this.leavesListener = leavesListener;
        this.leavesList = leavesList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout leaveTitleLayout;
        TextView tvLeaveCategory, tvStartdate, tvEnddate, tvStatus, tvLeaveReason, tvType;
        Button deleteClick;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            leaveTitleLayout = itemView.findViewById(R.id.leave_title_layout);
            tvLeaveCategory = itemView.findViewById(R.id.tv_leave_category);
            tvStartdate = itemView.findViewById(R.id.tv_startdate);
            tvEnddate = itemView.findViewById(R.id.tv_enddate);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvLeaveReason = itemView.findViewById(R.id.tv_leave_reason);
            tvType = itemView.findViewById(R.id.tv_leave_type);
            deleteClick = itemView.findViewById(R.id.btn_delete);
        }
    }

    @NonNull
    @Override
    public AppliedLeavesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_leaves_row,
                viewGroup, false);
        return new AppliedLeavesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AppliedLeavesAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tvStartdate.setText(Util.getDateFromTimestamp(leavesList.get(position).getStartdate(), DAY_MONTH_YEAR));
        viewHolder.tvEnddate.setText(Util.getDateFromTimestamp(leavesList.get(position).getEnddate(), DAY_MONTH_YEAR));
        viewHolder.tvStatus.setText(leavesList.get(position).getStatus());
        viewHolder.tvLeaveCategory.setText(leavesList.get(position).getLeaveType());
        viewHolder.tvLeaveReason.setText(leavesList.get(position).getReason());
        viewHolder.tvType.setText("("+leavesList.get(position).getFullHalfDay()+")");

        viewHolder.leaveTitleLayout.setOnClickListener(v ->
                leavesListener.editLeaves(leavesList.get(position)));
        if(leavesList.get(position).getStatus().equalsIgnoreCase(Constants.Leave.PENDING_STATUS)){
            viewHolder.deleteClick.setVisibility(View.VISIBLE);
            viewHolder.deleteClick.setOnClickListener(v -> leavesListener.deleteLeaves(leavesList.get(position).getId()));
        }else{
            viewHolder.deleteClick.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return leavesList.size();
    }

    public interface LeaveAdapterListener {
        void deleteLeaves(String leaveId);
        void editLeaves(LeaveData leaveData);
    }
}