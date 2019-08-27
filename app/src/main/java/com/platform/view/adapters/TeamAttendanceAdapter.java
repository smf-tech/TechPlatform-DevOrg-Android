package com.platform.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.platform.R;
import com.platform.models.attendance.TeamAttendanceData;
import com.platform.utility.Util;

import java.util.List;

@SuppressWarnings("CanBeFinal")
public class TeamAttendanceAdapter extends RecyclerView.Adapter<TeamAttendanceAdapter.ViewHolder> {

    private Context mContext;
    private List<TeamAttendanceData> leavesList;
    private RequestOptions requestOptions;

    public TeamAttendanceAdapter(final Context context, final List<TeamAttendanceData> leavesList) {
        this.mContext = context;
        this.leavesList = leavesList;
        requestOptions = new RequestOptions().placeholder(R.drawable.ic_user_avatar);
        requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivUserImage, ivStatus;
        TextView tvName, tvRole, tvCheckInTime, tvCheckOutTime;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserImage = itemView.findViewById(R.id.iv_user_profile_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvRole = itemView.findViewById(R.id.tv_role);
            ivStatus = itemView.findViewById(R.id.iv_status);
            tvCheckInTime = itemView.findViewById(R.id.tv_check_in_time);
            tvCheckOutTime = itemView.findViewById(R.id.tv_check_out_time);
        }
    }

    @NonNull
    @Override
    public TeamAttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_attendance_row, viewGroup, false);
        return new TeamAttendanceAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamAttendanceAdapter.ViewHolder viewHolder, int i) {

        if (leavesList.get(i).getImageUrl() != null && !leavesList.get(i).getImageUrl().equals("")) {
            Glide.with(mContext)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(leavesList.get(i).getImageUrl())
                    .into(viewHolder.ivUserImage);
        }
        viewHolder.tvName.setText(leavesList.get(i).getName());
        viewHolder.tvRole.setText(leavesList.get(i).getRoleName());

        if (leavesList.get(i).getCheckIn() != null)
            viewHolder.tvCheckInTime.setText(Util.getDateFromTimestamp(Long.valueOf(leavesList.get(i).getCheckIn().getTime()), "hh:mm aa"));
        if (leavesList.get(i).getCheckOut() != null)
            viewHolder.tvCheckOutTime.setText(Util.getDateFromTimestamp(Long.valueOf(leavesList.get(i).getCheckOut().getTime()), "hh:mm aa"));

        if (leavesList.get(i).getStatus().equals("Absent")) {
            viewHolder.ivStatus.setImageResource(R.drawable.ic_absent);
        } else if (leavesList.get(i).getStatus().equals("Present")) {
            viewHolder.ivStatus.setImageResource(R.drawable.ic_present);
        } else if (leavesList.get(i).getStatus().equals("Leave")) {
            viewHolder.ivStatus.setImageResource(R.drawable.ic_leave);
        }

    }

    @Override
    public int getItemCount() {
        return leavesList.size();
    }
}