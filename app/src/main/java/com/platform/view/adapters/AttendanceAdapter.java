package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.platform.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

@SuppressWarnings("CanBeFinal")
public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private Context mContext;
    private List<String> leavesList;

    public AttendanceAdapter(final Context context, final List<String> leavesList) {
        this.mContext = context;
        this.leavesList = leavesList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView userImage;
        TextView leaveDesc;
        //TextView leaveStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            leaveDesc = itemView.findViewById(R.id.tv_leave_desc);
            //leaveStatus = itemView.findViewById(R.id.tv_leave_status);
            userImage = itemView.findViewById(R.id.img_user_leaves);
            itemView.findViewById(R.id.img_delete).setVisibility(View.GONE);
            itemView.findViewById(R.id.img_edit).setVisibility(View.GONE);

        }
    }

    @NonNull
    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_leaves_row, viewGroup, false);
        return new AttendanceAdapter.ViewHolder(v);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBindViewHolder(@NonNull AttendanceAdapter.ViewHolder viewHolder, int i) {

        viewHolder.userImage.setBackgroundResource(R.drawable.ic_add_img);
        viewHolder.leaveDesc.setText("You have applied leaves from 8 march to 11 march");
        //viewHolder.leaveStatus.setText("Request status :"+"Not yet approved");
    }

    @Override
    public int getItemCount() {
        return 2;//leavesList.size();
    }
}