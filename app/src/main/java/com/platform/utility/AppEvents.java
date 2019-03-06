package com.platform.utility;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.platform.Platform;
//import com.platform.models.user.UserInfo;

public class AppEvents {

    private static final String TAG = AppEvents.class.getName();

    public static void trackAppEvent(String message) {
        try {
            FirebaseAnalytics firebaseAnalytics = Platform.getInstance().getFirebaseAnalyticsInstance();
            //registerSuperProperties();

            firebaseAnalytics.logEvent(message, new Bundle());
        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
        }
    }

//    private static void registerSuperProperties() {
//        UserInfo userInfo = Util.getUserObjectFromPref();
//        FirebaseAnalytics firebaseAnalytics = Platform.getInstance().getFirebaseAnalyticsInstance();
//        firebaseAnalytics.setUserProperty("User_Id", userInfo.getUserMobileNumber());
//        firebaseAnalytics.setUserProperty("Username", userInfo.getUserName());
//        firebaseAnalytics.setUserProperty("User_Email", userInfo.getUserEmailId());
//    }
}
