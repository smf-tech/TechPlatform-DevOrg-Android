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

public class NotificatioAdapter extends RecyclerView.Adapter<NotificatioAdapter.MyViewHolder> {

    private NotificationsFragment context;
    private List<NotificationData> notificationList;

    public NotificatioAdapter(NotificationsFragment notificationsFragment, List<NotificationData> notificationList) {
        this.notificationList = notificationList;
        this.context = notificationsFragment;
    }

    @NonNull
    @Override
    public NotificatioAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_notification, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificatioAdapter.MyViewHolder holder, int position) {
        holder.tvTitle.setText(notificationList.get(position).getTitle());
        holder.tvDetel.setText(notificationList.get(position).getText());
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
                    switch (notificationList.get(getAdapterPosition()).getToOpen()) {
                        case "":
                            break;
                        default:
                    }
                }
            });
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseManager.getDBInstance(Platform.getInstance())
                            .getNotificationDataDeo().deleteNotification(notificationList.get(getAdapterPosition()).getId());
//                    DatabaseManager.getDBInstance(Platform.getInstance())
//                            .getNotifications().deleteAllNotifications();
                    notificationList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

        }
    }
}
