package com.octopus.view.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.octopus.Platform;
import com.octopus.R;
import com.octopus.database.DatabaseManager;
import com.octopus.models.notifications.NotificationData;
import com.octopus.utility.Util;
import com.octopus.view.adapters.NotificationAdapter;

import java.util.Collections;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtNoData;
    private RecyclerView rvNotification;
    NotificationAdapter adapter;
    List<NotificationData> notificationList;
//    private ProgressBar progressBar;
//    private RelativeLayout progressBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        intiView();
    }

    private void intiView() {

        findViewById(R.id.tv_clear).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);

        Util.updateNotificationsCount(true);


//        List<NotificationData> notificationList=new ArrayList<NotificationData>();
        notificationList= DatabaseManager.getDBInstance(Platform.getInstance())
                .getNotificationDataDeo().getAllNotifications();

        if(!(notificationList.size()>0)){
            findViewById(R.id.txt_no_data).setVisibility(View.VISIBLE);
        }

        Collections.reverse(notificationList);

        rvNotification = findViewById(R.id.notifications_list);

        adapter = new NotificationAdapter(this, notificationList);
        rvNotification.setLayoutManager(new LinearLayoutManager(this));
        rvNotification.setItemAnimator(new DefaultItemAnimator());
        rvNotification.setAdapter(adapter);

        //mark all notification as read onens open this activity
        //mark all notification as read onens open this activity
        List<NotificationData> unRearNotificationList=DatabaseManager.getDBInstance(Platform.getInstance())
                .getNotificationDataDeo().getUnRearNotifications(true);
        for(NotificationData obj:unRearNotificationList){
            obj.setUnread(false);
            DatabaseManager.getDBInstance(Platform.getInstance())
                    .getNotificationDataDeo().update(obj);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_clear:
                clearAll();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void clearAll() {

        AlertDialog alertDialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Clear alert")
                .setMessage("Are you sure you want to clear all")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Clear list
                        DatabaseManager.getDBInstance(Platform.getInstance())
                                .getNotificationDataDeo().deleteAllNotifications();
                        notificationList.clear();
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        alertDialog = builder.create();
        alertDialog.show();
    }

}
