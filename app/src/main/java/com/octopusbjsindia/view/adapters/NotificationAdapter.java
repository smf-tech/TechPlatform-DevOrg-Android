package com.octopusbjsindia.view.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.models.notifications.NotificationData;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.view.activities.GeneralActionsActivity;
import com.octopusbjsindia.view.activities.NotificationsActivity;
import com.octopusbjsindia.view.activities.PlannerDetailActivity;
import com.octopusbjsindia.view.activities.TMFiltersListActivity;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private NotificationsActivity context;
    private List<NotificationData> notificationList;

    public NotificationAdapter(NotificationsActivity context, List<NotificationData> notificationList) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_notification, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        holder.tvTitle.setText(notificationList.get(position).getTitle());
        holder.tvDetal.setText(notificationList.get(position).getText());
        holder.tvDateTime.setText(notificationList.get(position).getDateTime());


        //Check unread message..
//        if(notificationList.get(position).getUnread()){
//            holder.imgDelete.setBackground(context.getResources().getDrawable(R.drawable.circle_background));
//        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDetal, tvDateTime;
        ImageView imgDelete;
        RelativeLayout layNotification;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDetal = itemView.findViewById(R.id.tv_detals);
            tvDateTime = itemView.findViewById(R.id.tv_date_time);
            imgDelete = itemView.findViewById(R.id.row_img);
            layNotification = itemView.findViewById(R.id.row_layout);

            layNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(notificationList.get(getAdapterPosition()).getToOpen()!=null){

                        Intent intent;

                        switch (notificationList.get(getAdapterPosition()).getToOpen()) {
                            case "formApproval":
                                intent = new Intent(context, TMFiltersListActivity.class);
                                intent.putExtra("filter_type","forms");
                                context.startActivity(intent);
                                break;
                            case "userApproval":
                                intent = new Intent(context, TMFiltersListActivity.class);
                                intent.putExtra("filter_type","userapproval");
                                context.startActivity(intent);
                                break;
                            case "leaveApproval":
                                intent = new Intent(context, TMFiltersListActivity.class);
                                intent.putExtra("filter_type","leave");
                                context.startActivity(intent);
                                break;
                            case "attendanceApproval":
                                intent = new Intent(context, TMFiltersListActivity.class);
                                intent.putExtra("filter_type","attendance");
                                context.startActivity(intent);
                                break;
                            case "compoffApproval":
                                intent = new Intent(context, TMFiltersListActivity.class);
                                intent.putExtra("filter_type","compoff");
                                context.startActivity(intent);
                                break;
                            case "event":
                                intent = new Intent(context, PlannerDetailActivity.class);
                                intent.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.EVENTS_LABEL);
                                context.startActivity(intent);
                                break;
                            case "task":
                                intent = new Intent(context, PlannerDetailActivity.class);
                                intent.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.TASKS_LABEL);
                                context.startActivity(intent);
                                break;
                            case "leave":
                                intent= new Intent(context, GeneralActionsActivity.class);
                                intent.putExtra("title", context.getString(R.string.leave));
                                intent.putExtra("switch_fragments", "LeaveDetailsFragment");
                                context.startActivity(intent);
                                break;
                            case "attendance":
                                intent = new Intent(context, GeneralActionsActivity.class);
                                intent.putExtra("title", context.getString(R.string.attendance));
                                intent.putExtra("switch_fragments", "AttendancePlannerFragment");
                                context.startActivity(intent);
                                break;

                            default:
                                context.finish();
                        }
                    }
                }
            });
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseManager.getDBInstance(Platform.getInstance())
                            .getNotificationDataDeo().deleteNotification(notificationList.get(getAdapterPosition()).getId());
                    notificationList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

        }
    }
}
