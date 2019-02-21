package com.platform;

import android.app.Application;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.platform.utility.Config;
import com.platform.utility.Constants;
import com.platform.utility.ForceUpdateChecker;
import com.platform.utility.Util;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class Platform extends Application {

    private final String TAG = Platform.class.getName();
    private static Platform mPlatformInstance;
    private RequestQueue mRequestQueue;

    public static Platform getInstance() {
        return mPlatformInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        initFireBase();

        mPlatformInstance = this;
        Util.makeDirectory(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MV/Image");
    }

    private void initFireBase() {
        Fabric.with(this, new Crashlytics());

        Map<String, Object> remoteConfigDefaults = new HashMap<>();
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, "1.0");
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL, Constants.playStoreLink);

        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setDefaults(remoteConfigDefaults);
        firebaseRemoteConfig.fetch(1000 * 60) // fetch every minutes
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "remote config is fetched.");
                        firebaseRemoteConfig.activateFetched();
                    }
                }).addOnFailureListener(exception -> {
            Log.d(TAG, "Fetch failed");
            // Do whatever should be done on failure
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        if (task.getResult() != null) {
                            String token = task.getResult().getToken();
                            Log.d(TAG, "TOKEN: " + token);
                        }
                    }
                });
    }

    @NonNull
    public RequestQueue getVolleyRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public String getAppMode() {
        return Config.getInstance().getAppMode();
    }
}
