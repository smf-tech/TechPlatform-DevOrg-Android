package com.octopusbjsindia.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class ShowTimerService extends Service {
    public static volatile boolean shouldContinue=true;
    private String checkInTime="";
    public MyBinder binder = new MyBinder();
    private int hours;
    private int min;


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
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public String doWork() {


        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

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
                    hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                    min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
                    
                    

                }

            }
        },1000);


        //Log.i("log_tag","Hours: "+hours+", Mins: "+min);

       /* Intent i = new Intent();
        i.putExtra("HoursDiff",hours+min);
        i.setAction("BROADCASTIME");
        sendBroadcast(i);

        if(!shouldContinue){
        stopSelf();
        return;
        }*/
       return hours+":"+min;

    }


}
