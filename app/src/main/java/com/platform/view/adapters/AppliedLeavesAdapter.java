package com.platform.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.models.leaves.LeaveData;

import java.util.List;

import static com.platform.utility.Constants.DAY_MONTH_YEAR;
import static com.platform.utility.Util.getDateFromTimestamp;
import static com.platform.utility.Util.getDateTimeFromTimestamp;

@SuppressWarnings("CanBeFinal")
public class AppliedLeavesAdapter extends RecyclerView.Adapter<AppliedLeavesAdapter.ViewHolder> {
    private LeaveAdapterListener leavesListener;
    private List<LeaveData> leavesList;

    public AppliedLeavesAdapter(final List<LeaveData> leavesList, LeaveAdapterListener leavesListener) {
        this.leavesListener = leavesListener;
        this.leavesList = leavesList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout leaveTitleLayout;
        TextView leaveHeader, leaveSubHeader;
        ImageView deleteClick;
        ImageView editClick;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            leaveTitleLayout = itemView.findViewById(R.id.leave_title_layout);
            leaveHeader = itemView.findViewById(R.id.tv_leave_header);
            leaveSubHeader = itemView.findViewById(R.id.tv_leave_sub_header);
            deleteClick = itemView.findViewById(R.id.img_delete);
            editClick = itemView.findViewById(R.id.img_edit);
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
        viewHolder.leaveSubHeader.setText("from "+ getDateFromTimestamp
                (leavesList.get(position).getStartdate(), DAY_MONTH_YEAR)+ " to " + getDateFromTimestamp(leavesList.get(position).getEnddate(), DAY_MONTH_YEAR));
        viewHolder.leaveTitleLayout.setOnClickListener(v -> leavesListener.editLeaves(leavesList.get(position)));
        viewHolder.deleteClick.setOnClickListener(v -> leavesListener.deleteLeaves(leavesList.get(position).getId()));
        viewHolder.editClick.setOnClickListener(v -> leavesListener.editLeaves(leavesList.get(position)));
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