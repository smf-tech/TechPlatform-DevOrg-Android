package com.octopusbjsindia.services;

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
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.models.notifications.NotificationData;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.PreferenceHelper;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.EditProfileActivity;
import com.octopusbjsindia.view.activities.GeneralActionsActivity;
import com.octopusbjsindia.view.activities.HomeActivity;
import com.octopusbjsindia.view.activities.LoginActivity;
import com.octopusbjsindia.view.activities.PlannerDetailActivity;
import com.octopusbjsindia.view.activities.SSActionsActivity;
import com.octopusbjsindia.view.activities.TMFiltersListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import static com.octopusbjsindia.utility.Constants.Notification.NOTIFICATION;

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
            sendNotification(remoteMessage);
        } else {
            //contains notification payload.
            saveLocaly(remoteMessage);
            Log.d(TAG, "Message Notification payload: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage);
        }
    }

    public void saveLocaly(RemoteMessage remoteMessage) {
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

    private void sendNotification(RemoteMessage remoteMessage) {
        String messageTitle = remoteMessage.getNotification().getTitle();
        String messageBody = remoteMessage.getNotification().getTitle();
        Intent intent = null;
        if (TextUtils.isEmpty(remoteMessageId)) {
            intent = getIntent(remoteMessage.getData().get("toOpen"));
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
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
            } else {
                notificationBuilder = new NotificationCompat.Builder(this, "")
                        .setSmallIcon(R.mipmap.app_logo)
                        .setContentTitle(messageTitle)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
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

    private Intent getIntent(String toOpen) {
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
                switch (toOpen) {
                    case "formApproval":
                        intent = new Intent(getApplicationContext(), TMFiltersListActivity.class);
                        intent.putExtra("filter_type", "forms");
                        getApplicationContext().startActivity(intent);
                        break;
                    case "userApproval":
                        intent = new Intent(getApplicationContext(), TMFiltersListActivity.class);
                        intent.putExtra("filter_type", "userapproval");
                        getApplicationContext().startActivity(intent);
                        break;
                    case "leaveApproval":
                        intent = new Intent(getApplicationContext(), TMFiltersListActivity.class);
                        intent.putExtra("filter_type", "leave");
                        getApplicationContext().startActivity(intent);
                        break;
                    case "attendanceApproval":
                        intent = new Intent(getApplicationContext(), TMFiltersListActivity.class);
                        intent.putExtra("filter_type", "attendance");
                        getApplicationContext().startActivity(intent);
                        break;
                    case "compoffApproval":
                        intent = new Intent(getApplicationContext(), TMFiltersListActivity.class);
                        intent.putExtra("filter_type", "compoff");
                        getApplicationContext().startActivity(intent);
                        break;
                    case "event":
                        intent = new Intent(getApplicationContext(), PlannerDetailActivity.class);
                        intent.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.EVENTS_LABEL);
                        getApplicationContext().startActivity(intent);
                        break;
                    case "task":
                        intent = new Intent(getApplicationContext(), PlannerDetailActivity.class);
                        intent.putExtra(Constants.Planner.TO_OPEN, Constants.Planner.TASKS_LABEL);
                        getApplicationContext().startActivity(intent);
                        break;
                    case "leave":
                        intent = new Intent(getApplicationContext(), GeneralActionsActivity.class);
                        intent.putExtra("title", getApplicationContext().getString(R.string.leave));
                        intent.putExtra("switch_fragments", "LeaveDetailsFragment");
                        getApplicationContext().startActivity(intent);
                        break;
                    case "attendance":
                        intent = new Intent(getApplicationContext(), GeneralActionsActivity.class);
                        intent.putExtra("title", getApplicationContext().getString(R.string.attendance));
                        intent.putExtra("switch_fragments", "AttendancePlannerFragment");
                        getApplicationContext().startActivity(intent);
                        break;
                    case "structure":
                        intent = new Intent(getApplicationContext(), SSActionsActivity.class);
                        intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
                        intent.putExtra("viewType", 1);
                        intent.putExtra("title", "Structure List");
                        getApplicationContext().startActivity(intent);
                        break;
                    case "machine":
                        intent = new Intent(getApplicationContext(), SSActionsActivity.class);
                        intent.putExtra("SwitchToFragment", "StructureMachineListFragment");
                        intent.putExtra("viewType", 2);
                        intent.putExtra("title", "Machine List");
                        getApplicationContext().startActivity(intent);
                        break;
                    default:
                        intent = new Intent(getApplicationContext(), HomeActivity.class);
                        break;
                }
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
        //Util.updateFirebaseIdRequests(jsonObject);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("firebase_id", s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            Util.updateFirebaseIdRequests(jsonObject);
        }
    }
}
