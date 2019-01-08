package com.platform.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.platform.utility.Constants;

public class SmsReceiver extends BroadcastReceiver {

    private OtpSmsReceiverListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().contentEquals(
                Constants.SMS_RECEIVE_IDENTIFIER)) {

            Bundle data = intent.getExtras();
            if (data != null) {
                Object[] objects = (Object[]) data.get("get_msg");
                if (objects != null) {
                    final String MESSAGE_TEMPLATE = "";
                    for (Object obj : objects) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                        String message = smsMessage.getDisplayMessageBody();
                        if (message.contains(MESSAGE_TEMPLATE)) {
                            if (listener != null) {
                                listener.smsReceive(message);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public void setListener(OtpSmsReceiverListener listener) {
        this.listener = listener;
    }

    public void deRegisterListener() {
        this.listener = null;
    }

    public interface OtpSmsReceiverListener {
        void smsReceive(String otp);
    }
}
