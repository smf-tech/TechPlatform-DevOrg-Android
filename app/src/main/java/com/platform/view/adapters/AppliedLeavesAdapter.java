package com.platform.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;

import java.util.List;

@SuppressWarnings("CanBeFinal")
public class AppliedLeavesAdapter extends RecyclerView.Adapter<AppliedLeavesAdapter.ViewHolder> {
    private LeaveAdapterListener leavesListener;
    private List<String> leavesList;

    public AppliedLeavesAdapter(final List<String> leavesList, LeaveAdapterListener leavesListener) {
        this.leavesListener = leavesListener;
        this.leavesList = leavesList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView leaveDesc;
        ImageView deleteClick;
        ImageView editClick;

        //TextView leaveStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            leaveDesc = itemView.findViewById(R.id.tv_leave_desc);
            userImage = itemView.findViewById(R.id.img_user_leaves);
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
    public void onBindViewHolder(@NonNull AppliedLeavesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.userImage.setBackgroundResource(R.drawable.ic_add_img);
        viewHolder.leaveDesc.setText("You have applied leaves from 8 march to 11 march");
        viewHolder.deleteClick.setOnClickListener(v -> leavesListener.deleteLeaves());
        viewHolder.editClick.setOnClickListener(v -> leavesListener.editLeaves());
    }

    @Override
    public int getItemCount() {
        return leavesList.size();
    }

    public interface LeaveAdapterListener {
        void deleteLeaves();

        void editLeaves();
    }
}