package com.platform.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.platform.BuildConfig;
import com.platform.Platform;
import com.platform.R;
import com.platform.utility.AppEvents;
import com.platform.utility.Constants;

public class SmsReceiver extends BroadcastReceiver {

    private IOtpSmsReceiverListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null && intent.getAction() != null &&
                intent.getAction().contentEquals(Constants.SMS_RECEIVE_IDENTIFIER)) {

            Bundle data = intent.getExtras();
            if (data != null) {
                Object[] pdus = (Object[]) data.get("pdus");
                if (pdus != null) {
                    final String MESSAGE_TEMPLATE = "<#> The password is:";

                    for (Object pdu : pdus) {
                        SmsMessage smsMessage;
                        if (Build.VERSION.SDK_INT >= 23)
                            smsMessage = SmsMessage.createFromPdu((byte[]) pdu, "3gpp");
                        else
                            smsMessage = SmsMessage.createFromPdu((byte[]) pdu);

                        String message = smsMessage.getDisplayMessageBody();
                        Log.d("TAG", "SMS: " + message);

                        if (message.contains(MESSAGE_TEMPLATE) &&
                                message.contains(BuildConfig.SMS_VERIFICATION_CODE)) {

                            String otp = message.replaceAll("[^0-9]+", "");
                            if (listener != null) {
                                listener.smsReceive(otp.substring(0, 6));
                                return;
                            }
                        }
                    }
                } else {
                    AppEvents.trackAppEvent(Platform.getInstance().getString(R.string.event_auto_read_failure));
                }
            } else {
                AppEvents.trackAppEvent(Platform.getInstance().getString(R.string.event_auto_read_failure));
            }
        } else {
            AppEvents.trackAppEvent(Platform.getInstance().getString(R.string.event_auto_read_failure));
        }
    }

    public void setListener(IOtpSmsReceiverListener listener) {
        this.listener = listener;
    }

    public interface IOtpSmsReceiverListener {
        void smsReceive(String otp);
    }
}
