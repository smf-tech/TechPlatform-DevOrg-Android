package com.platform.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.platform.R;
import com.platform.utility.Constants;
import com.platform.utility.PreferenceHelper;
import com.platform.view.activities.SplashActivity;

@SuppressWarnings("FieldCanBeLocal")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String remoteMessageId = "";
    private PreferenceHelper preferenceHelper;
    private final String TAG = MyFirebaseMessagingService.class.getName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            try {
                preferenceHelper = new PreferenceHelper(getApplicationContext());
                remoteMessageId = remoteMessage.getData().get("Id");

                if (preferenceHelper != null) {
                    // notify for new notification.
                    Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                } else {
                    sendNotification(remoteMessage.getData().get("Title"),
                            remoteMessage.getData().get("Description"));

                    // notify for new notification.
                    Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendNotification(String messageTitle, String messageBody) {
        Intent intent = null;
        if (TextUtils.isEmpty(remoteMessageId)) {
            intent = new Intent(this, SplashActivity.class);
        } else {
            Log.i(TAG, "Create message" + messageTitle + messageBody);
        }

        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "")
                    .setSmallIcon(R.mipmap.app_logo)
                    .setContentTitle(messageTitle)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.notify(0, notificationBuilder.build());
            }
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        PreferenceHelper preferenceHelper = new PreferenceHelper(getApplicationContext());
        preferenceHelper.insertString(PreferenceHelper.TOKEN, s);
    }
}
