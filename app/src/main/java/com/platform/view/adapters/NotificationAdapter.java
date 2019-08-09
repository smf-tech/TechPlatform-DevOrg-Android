package com.platform.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.models.notifications.NotificationData;
import com.platform.view.fragments.NotificationsFragment;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private NotificationsFragment context;
    private List<NotificationData> notificationList;

    public NotificationAdapter(NotificationsFragment notificationsFragment, List<NotificationData> notificationList) {
        this.notificationList = notificationList;
        this.context = notificationsFragment;
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
        holder.tvDetel.setText(notificationList.get(position).getText());
        if(notificationList.get(position).getUnread()){
            holder.imgDelete.setBackground(context.getResources().getDrawable(R.drawable.circle_background));
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDetel;
        ImageView imgDelete;
        RelativeLayout layNotification;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDetel = itemView.findViewById(R.id.tv_Detels);
            imgDelete = itemView.findViewById(R.id.row_img);
            layNotification = itemView.findViewById(R.id.row_layout);

            layNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(notificationList.get(getAdapterPosition()).getToOpen()!=null){
                        switch (notificationList.get(getAdapterPosition()).getToOpen()) {
                            case "":
                                break;
                            default:
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
