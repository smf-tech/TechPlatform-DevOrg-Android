package com.octopusbjsindia.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.octopusbjsindia.listeners.SubmitAttendanceListener;

public class AttendanceService extends IntentService {


    private String finalUrl;
    public AttendanceService(){
        super("IntentService");
    }
    private SubmitAttendanceListener submitAttendanceListener;

    @Override
    protected void onHandleIntent(Intent intent) {

        runTaskInNewThread(intent);

    }

    private void runTaskInNewThread(Intent intent) {
        doBackWork(intent);

    }

    private void doBackWork(Intent intent) {

        // upload all todays scan id


        final AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent=PendingIntent.getService(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        final Bundle bundle=intent.getExtras();
        String jsonBody=null;
        if(bundle!=null){
            jsonBody=bundle.getString("requestJson");
            final Messenger messenger= (Messenger) bundle.get("messenger");
            final Message message =Message.obtain();
            bundle.putString("success","Service executed successfully");
            message.setData(bundle);
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }


            //Singleton.getSingleton().showProgressBar(getApplicationContext());


            /*finalUrl= NetworkUtility.BASE_URL+NetworkUtility.METHOD_SEND_PURCHASE;
            new MakeRequest().getDataFromUrl(finalUrl,jsonBody, new MakeRequest.responseListener() {
                @Override
                public void onSuccess(String success) {
                    Log.i("Sucess","111"+success);
                    bundle.putString("success",success);
                    message.setData(bundle);
                    try{
                     messenger.send(message);
                    }catch (Exception e){

                    }
                }

                @Override
                public void onError(String error) {
                    Log.i("Error","111"+error);
                    //Singleton.getSingleton().stopProgressBar();
                }
            });
*/
        }

        manager.cancel(pendingIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
