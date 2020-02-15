package com.octopusbjsindia.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.octopusbjsindia.R;
import com.octopusbjsindia.models.attendance.TeamAttendanceData;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.GeneralActionsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        TextView tvName, tvRole, tvCheckInTime, tvCheckOutTime, tvTotalHours;
        RelativeLayout lyMain;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            lyMain = itemView.findViewById(R.id.ly_main);
            ivUserImage = itemView.findViewById(R.id.iv_user_profile_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTotalHours = itemView.findViewById(R.id.tv_total_hours);
            tvRole = itemView.findViewById(R.id.tv_role);
            ivStatus = itemView.findViewById(R.id.iv_status);
            tvCheckInTime = itemView.findViewById(R.id.tv_check_in_time);
            tvCheckOutTime = itemView.findViewById(R.id.tv_check_out_time);
            lyMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, GeneralActionsActivity.class);
                    intent.putExtra("title", "Team User Attendance");
                    intent.putExtra("switch_fragments", "AttendanceSummeryFragment");
                    intent.putExtra("user_id", leavesList.get(getAdapterPosition()).getUser_id());
                    intent.putExtra("user_role", leavesList.get(getAdapterPosition()).getRoleName());
                    intent.putExtra("user_name", leavesList.get(getAdapterPosition()).getName());
                    if (leavesList.get(getAdapterPosition()).getImageUrl() != null && !leavesList.get(getAdapterPosition()).getImageUrl().equals("")) {
                        intent.putExtra("profile_pic", leavesList.get(getAdapterPosition()).getImageUrl());
                    }
                    mContext.startActivity(intent);
                }
            });

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
        } else {
            viewHolder.ivUserImage.setImageResource(R.drawable.ic_user_avatar);
        }
        viewHolder.tvName.setText(leavesList.get(i).getName());
        viewHolder.tvRole.setText(leavesList.get(i).getRoleName());

        if (leavesList.get(i).getCheckIn() != null)
            viewHolder.tvCheckInTime.setText(
                    Util.getDateFromTimestamp(Long.valueOf(leavesList.get(i).getCheckIn().getTime()), "hh:mm aa")
            );
        if (leavesList.get(i).getCheckOut() != null)
            viewHolder.tvCheckOutTime.setText(
                    Util.getDateFromTimestamp(Long.valueOf(leavesList.get(i).getCheckOut().getTime()), "hh:mm aa")
            );

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");

        Date date1 = null;
        Date date2 = null;
        try {
            if (leavesList.get(i).getCheckIn() != null) {
                date1 = simpleDateFormat.parse(
                        Util.getDateFromTimestamp(Long.valueOf(leavesList.get(i).getCheckIn().getTime()), "hh:mm aa")
                );
            }
            if (leavesList.get(i).getCheckOut() != null) {
                date2 = simpleDateFormat.parse(
                        Util.getDateFromTimestamp(Long.valueOf(leavesList.get(i).getCheckOut().getTime()), "hh:mm aa")
                );
            }

            if (date1 != null && date2 != null) {
                long difference = date2.getTime() - date1.getTime();
                int days = (int) (difference / (1000 * 60 * 60 * 24));
                int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
                int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                hours = (hours < 0 ? -hours : hours);
                viewHolder.tvTotalHours.setText(hours + ":" + min);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (leavesList.get(i).getStatus().equalsIgnoreCase("absent")) {
            viewHolder.ivStatus.setImageResource(R.drawable.ic_absent);
        } else if (leavesList.get(i).getStatus().equalsIgnoreCase("present")) {
            viewHolder.ivStatus.setImageResource(R.drawable.ic_present);
        } else if (leavesList.get(i).getStatus().equalsIgnoreCase("leave")) {
            viewHolder.ivStatus.setImageResource(R.drawable.ic_leave);
        }

    }

    @Override
    public int getItemCount() {
        return leavesList.size();
    }
}