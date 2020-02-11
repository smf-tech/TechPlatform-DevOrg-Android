package com.octopusbjsindia.receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.octopusbjsindia.R;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.OperatorMeterReadingActivity;

import static com.octopusbjsindia.utility.Constants.App.CHANNEL_ID;


public class AlarmReceiver extends BroadcastReceiver {
    private Context mContext;
    NotificationManager manager;

    private static final int NOTIFICATION_ID = 0;


    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        Log.e("AlarmReceiver","AlarmReceiver");
        Intent newIntent = new Intent(context, OperatorMeterReadingActivity.class);

        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        newIntent.putExtra("fromAlarm","alarm");
        Log.e("AlarmReceiver", "AlarmReceiver.onReceive");

        context.startActivity(newIntent);

        /*NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.e("alarm Receiver","Receiver Called");
        //Create the content intent for the notification, which launches this activity
        Intent contentIntent = new Intent(context, OperatorMeterReadingActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_alarm_clock)
                .setContentTitle("TEST")
                .setContentText("This is test.")
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        //Deliver the notification
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        */

/*
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        MediaPlayer player = MediaPlayer.create(context, alarmUri);
        player.setLooping(true);
        player.start();
        *//*Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();*//*
        createNotificationChannel();
        Log.e("alarm notification","Receiver Called");
        Intent notificationIntent = new Intent(mContext, OperatorMeterReadingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm_clock)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setFullScreenIntent(pendingIntent, true);
        manager.notify(1010, builder.build());*/
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            manager = mContext.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}