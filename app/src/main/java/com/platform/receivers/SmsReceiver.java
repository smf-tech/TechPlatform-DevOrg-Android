package com.platform.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import android.os.Bundle;

//import com.google.android.gms.auth.api.phone.SmsRetriever;
//import com.google.android.gms.common.api.CommonStatusCodes;
//import com.google.android.gms.common.api.Status;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
//        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
//            Bundle extras = intent.getExtras();
//
//            Status status = null;
//            if (extras != null) {
//                status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
//            }
//
//            if (status != null) {
//                switch (status.getStatusCode()) {
//                    case CommonStatusCodes.SUCCESS:
//                        // Get SMS message contents
//                        String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
//                        // Extract one-time code from the message and complete verification
//                        // by sending the code back to your server.
//                        Intent in = new Intent("SmsMessage.intent.MAIN").
//                                putExtra("get_msg", message);
//
//                        //You can place your check conditions here(on the SMS or the sender)
//                        //and then send another broadcast
//                        context.sendBroadcast(in);
//                        break;
//                    case CommonStatusCodes.TIMEOUT:
//                        // Waiting for SMS timed out (5 minutes)
//                        // Handle the error ...
//                        break;
//                }
//            }
//        }
    }
}
