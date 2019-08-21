package com.platform.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.models.notifications.NotificationData;
import com.platform.utility.Constants;
import com.platform.utility.PreferenceHelper;
import com.platform.utility.Util;
import com.platform.view.activities.EditProfileActivity;
import com.platform.view.activities.HomeActivity;
import com.platform.view.activities.LoginActivity;

import java.util.Calendar;
import java.util.Date;

import static com.platform.utility.Constants.Notification.NOTIFICATION;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String remoteMessageId = "";
    private final String TAG = MyFirebaseMessagingService.class.getName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        remoteMessageId = remoteMessage.getData().get("Id");

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            //contains data payload.
            saveLocaly(remoteMessage);
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            sendNotification(remoteMessage.getNotification().getTitle(),
                        remoteMessage.getNotification().getBody());
        } else {
            //contains notification payload.
            saveLocaly(remoteMessage);
            Log.d(TAG, "Message Notification payload: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        }
    }

    public void saveLocaly(RemoteMessage remoteMessage){
        Date crDate = Calendar.getInstance().getTime();
        String strDate = Util.getDateFromTimestamp(crDate.getTime(), Constants.FORM_DATE_FORMAT);
        NotificationData data = new NotificationData();
        data.setDateTime(strDate);
        data.setTitle(remoteMessage.getNotification().getTitle());
        data.setText(remoteMessage.getNotification().getBody());
        data.setToOpen(remoteMessage.getData().get("toOpen"));
        data.setUnread(true);
        DatabaseManager.getDBInstance(Platform.getInstance()).getNotificationDataDeo().insert(data);

        Util.updateNotificationsCount(false);

        Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
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

            NotificationCompat.Builder notificationBuilder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationBuilder = new NotificationCompat.Builder(this, Constants.App.CHANNEL_ID)
                        .setSmallIcon(R.mipmap.app_logo)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
            } else {
                notificationBuilder = new NotificationCompat.Builder(this, "")
                        .setSmallIcon(R.mipmap.app_logo)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
            }


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager != null) {
                notificationManager.notify(0, notificationBuilder.build());
            }

            // Create an explicit intent for an Activity in your app
//            Intent intent = new Intent(this, AlertDetails.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

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
