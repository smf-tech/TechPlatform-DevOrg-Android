package com.platform.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ShowTimerService extends Service {
    public static volatile boolean shouldContinue=true;
    private String checkInTime="";
    public MyBinder binder = new MyBinder();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    public class MyBinder extends Binder {
        public ShowTimerService getService() {
            return ShowTimerService.this;
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            checkInTime=bundle.getString("CheckInTime");
        }
        doWork();

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    /*public ShowTimerService() {
        super("timer service");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        doWork(intent);

    }*/

    public void doWork() {

        /*Bundle bundle=intent.getExtras();
        if(bundle!=null){
            checkInTime=bundle.getString("CheckInTime");
        }*/
        // get hourd difference
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");

        if(!checkInTime.isEmpty()){
            Date startDate = null;
            try {
                startDate = simpleDateFormat.parse(checkInTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            Calendar calendar=Calendar.getInstance();
            String CurrentTime=simpleDateFormat.format(calendar.getTime());


            Date endDate = null;
            try {
                endDate = simpleDateFormat.parse(CurrentTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            long difference = endDate.getTime() - startDate.getTime();
            if(difference<0)
            {
                Date dateMax = null;
                try {
                    dateMax = simpleDateFormat.parse("24:00");
                    Date dateMin = simpleDateFormat.parse("00:00");
                    difference=(dateMax.getTime() -startDate.getTime() )+(endDate.getTime()-dateMin.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            int days = (int) (difference / (1000*60*60*24));
            int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
            int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);

        }
        //Log.i("log_tag","Hours: "+hours+", Mins: "+min);

       /* Intent i = new Intent();
        i.putExtra("HoursDiff",hours+min);
        i.setAction("BROADCASTIME");
        sendBroadcast(i);

        if(!shouldContinue){
        stopSelf();
        return;
        }*/

    }


}
