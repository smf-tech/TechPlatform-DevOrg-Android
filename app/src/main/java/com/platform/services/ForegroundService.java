package com.platform.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.platform.Platform;
import com.platform.R;
import com.platform.view.activities.OperatorMeterReadingActivity;

import java.util.concurrent.TimeUnit;

import static androidx.core.app.NotificationCompat.Builder;


public class ForegroundService extends Service {

    public static final String
            ACTION_LOCATION_BROADCAST = ForegroundService.class.getName() + "LocationBroadcast";
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    public String mTimeLeftString = "";
    public int mTimeLeft = 0;
    long hours;
    NotificationManager manager;
    Handler mHandler;
    int Starttime = 0;
    long startTime = SystemClock.elapsedRealtime();
    int time = (int) (System.currentTimeMillis()) / 1000;
    int currentSystemTime = 0;
    int currentClockTime = 0;
    SharedPreferences preferences;

    private int currentHours  = 0;
    int totalHours = 0;

    //Handler mHandler new ha;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        preferences = Platform.getInstance().getSharedPreferences(
                "AppData", Context.MODE_PRIVATE);
        mHandler = new Handler();
        Intent notificationIntent = new Intent(this, OperatorMeterReadingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        final Builder builder = new Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.mipmap.app_logo)
                .setContentIntent(pendingIntent);

        startForeground(R.string.app_name, builder.build());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do your stuff - don't create a new runnable here!
                //if (!mStopHandler)
                {
                    mHandler.postDelayed(this, 1000);
                    /*int time = (int) (System.currentTimeMillis());
                    Timestamp tsTemp = new Timestamp(time);
                    String ts =  tsTemp.toString();
                    builder.setContentText("Time "+ts);
                    Notification notification = builder.getNotification();
                    notification.flags = Notification.FLAG_ONGOING_EVENT;
                    manager.notify(R.string.app_name, builder.build());*/

                    sendBroadcastMessage();
                }
            }
        };

// start it with:
        mHandler.post(runnable);

        //do heavy work on a background thread

        //stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        Log.d(ACTION_LOCATION_BROADCAST, "on destroy called");
        Log.e("System--DESTROYED", "--" + currentClockTime);
        saveLoginObjectInPref();
    }


    public void saveLoginObjectInPref() {


        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("systemTime", currentSystemTime);
        editor.putInt("systemClockTime", currentClockTime);
        editor.putInt("totalHours", currentClockTime);
        editor.apply();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void sendBroadcastMessage() {
        {
            Intent intent = new Intent(ACTION_LOCATION_BROADCAST);


            int systemTime = preferences.getInt("systemTime", 0);
            int systemClockTime = preferences.getInt("systemClockTime", 0);
            Log.e("System---", "--" + systemTime);
            Log.e("System--clock-", "--" + systemClockTime);


// Calculate the time interval when the task is done
            int timeInterval = (int) (SystemClock.elapsedRealtime() - startTime) / 1000;
            currentClockTime = systemClockTime + timeInterval;
            currentSystemTime = ((int) System.currentTimeMillis() / 1000);
            currentHours = timeInterval;
           // Log.e("currentHours", "--" + currentHours);
            totalHours = timeInterval+preferences.getInt("totalHours", 0);
            //getFormattedTime(currentClockTime);

            //intent.putExtra("STR_TIME", currentSystemTime - time + " " + "\n " + getFormattedTime(currentClockTime));
            intent.putExtra("STR_TIME", getFormattedTime(currentClockTime));
            intent.putExtra("TOTAL_HOURS", totalHours);
            intent.putExtra("CURRENT_HOURS", currentHours);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            /*Log.e("------------", "------------" );
            Log.e("timeInterval", "" + timeInterval);
            Log.e("preference", "" + preferences.getInt("totalHours", 0));

            Log.e("current Time", getFormattedTime(currentClockTime));
            Log.e("Total_hours", "" + totalHours);
            Log.e("current_hours", "" + currentHours);

            Log.e("timeInterval", "" + timeInterval);*/

        }

    }


    public String getFormattedTime(int time) {
        //  mTimeLeft = (int)time/1000;
        mTimeLeft = time;
        //mTimeLeftFromPrevious = (int)millisUntilFinished;
        //int day = (int) TimeUnit.SECONDS.toDays(mTimeLeft);
        hours = TimeUnit.SECONDS.toHours(mTimeLeft); //- (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(mTimeLeft) - (TimeUnit.SECONDS.toHours(mTimeLeft) * 60);
        long second = TimeUnit.SECONDS.toSeconds(mTimeLeft) - (TimeUnit.SECONDS.toMinutes(mTimeLeft) * 60);
        String hrsString = "hrs", minString = "mins", secStrind = "secs";
        if (hours == 1) {
            hrsString = "Hr";
        } else {
            hrsString = "Hrs";
        }
        if (minute == 1) {
            minString = "Mins";
        } else {
            minString = "Min";
        }
        if (second == 1) {
            secStrind = "Secs";
        } else {
            secStrind = "Sec";
        }
//                mTimeLeftString = ""+day+" Days "+hours+" Hours "+minute+" Minutes "+second+" Seconds ";
        mTimeLeftString = hours + hrsString + minute + minString + second + " " + secStrind;
        if (hours > 0) {
            mTimeLeftString = hours + " " + hrsString + " " + minute + " " + minString + " " + second + " " + secStrind;
            //btn_join_quiz.setBackgroundColor(getResources().getColor(R.color.quiz_prizegray_color));//getColor(getActivity(),R.color.quiz_prizegray_color));
        } else if (minute > 0) {
            mTimeLeftString = minute + " " + minString + " " + second + " " + secStrind;
        } else if (second > 0) {
            mTimeLeftString = second + " " + secStrind;
        }
        return mTimeLeftString;
    }
}