package com.platform.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.platform.R;
import com.platform.utility.Constants;
import com.platform.utility.PreferenceHelper;
import com.platform.utility.Util;
import com.platform.view.activities.HomeActivity;
import com.platform.view.activities.LoginActivity;
import com.platform.view.activities.EditProfileActivity;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.platform.utility.Constants.Notification.NOTIFICATION;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String remoteMessageId = "";
    private final String TAG = MyFirebaseMessagingService.class.getName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            try {
                remoteMessageId = remoteMessage.getData().get("Id");

                Util.updateNotificationsCount(false);

                sendNotification(remoteMessage.getData().get("Title"),
                        remoteMessage.getData().get("Description"));

                // notify for new notification.
                Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private void sendNotification(String messageTitle, String messageBody) {
        Intent intent = null;
        if (TextUtils.isEmpty(remoteMessageId)) {
            intent = getIntent();
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

    private Intent getIntent() {
        Intent intent;

        try {
            // Check user has registered mobile number or not
            if (Util.getLoginObjectFromPref() == null ||
                    Util.getLoginObjectFromPref().getLoginData() == null ||
                    TextUtils.isEmpty(Util.getLoginObjectFromPref().getLoginData().getAccessToken())) {
                intent = new Intent(getApplicationContext(), LoginActivity.class);
            } else if (TextUtils.isEmpty(Util.getUserObjectFromPref().getId())) {
                intent = new Intent(getApplicationContext(), EditProfileActivity.class);
            } else {
                intent = new Intent(getApplicationContext(), HomeActivity.class);
            }

            intent.putExtra(NOTIFICATION, true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            intent = new Intent(getApplicationContext(), HomeActivity.class);
        }
        return intent;
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        PreferenceHelper preferenceHelper = new PreferenceHelper(getApplicationContext());
        preferenceHelper.insertString(PreferenceHelper.TOKEN, s);
    }
}
