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
import com.platform.models.attendance.TeamUser;
import com.platform.utility.Util;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@SuppressWarnings("CanBeFinal")
public class TeamAttendanceAdapter extends RecyclerView.Adapter<TeamAttendanceAdapter.ViewHolder> {

    private Context mContext;
    private List<TeamUser> leavesList;
    private RequestOptions requestOptions;

    public TeamAttendanceAdapter(final Context context, final List<TeamUser> leavesList) {
        this.mContext = context;
        this.leavesList = leavesList;
        requestOptions = new RequestOptions().placeholder(R.drawable.ic_user_avatar);
        requestOptions = requestOptions.apply(RequestOptions.circleCropTransform());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivUserImage, ivStatus;
        TextView tvName, tvRole;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserImage = itemView.findViewById(R.id.iv_user_profile_pic);
            tvName = itemView.findViewById(R.id.tv_name);
            tvRole = itemView.findViewById(R.id.tv_role);
            ivStatus = itemView.findViewById(R.id.iv_status);
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

        if(!leavesList.get(i).getImageUrl().equals("")){
            Glide.with(mContext)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(leavesList.get(i).getImageUrl())
                    .into(viewHolder.ivUserImage);
        }
        viewHolder.tvName.setText(leavesList.get(i).getName());
        viewHolder.tvRole.setText(leavesList.get(i).getRole());

        if(!leavesList.get(i).getStatus().equals("a")){
            viewHolder.ivStatus.setImageResource(R.drawable.ic_search);
        } else if(!leavesList.get(i).getStatus().equals("p")){
            viewHolder.ivStatus.setImageResource(R.drawable.ic_close);
        }

    }

    @Override
    public int getItemCount() {
            return leavesList.size();
    }
}