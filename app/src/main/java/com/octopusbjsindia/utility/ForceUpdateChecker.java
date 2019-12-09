package com.octopusbjsindia.utility;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

@SuppressWarnings("CanBeFinal")
public class ForceUpdateChecker {

    private static final String TAG = ForceUpdateChecker.class.getSimpleName();

    public static final String KEY_UPDATE_REQUIRED = "Platform_force_update_required";
    public static final String KEY_CURRENT_VERSION = "Platform_force_update_current_version";
    public static final String KEY_UPDATE_URL = "Platform_force_update_store_url";
    private static final String KEY_MIN_REQUIRED_VERSION = "Platform_minimum_required_app_version";

    private OnUpdateNeededListener onUpdateNeededListener;
    private Context context;

    public interface OnUpdateNeededListener {
        void onUpdateNeeded(String updateUrl, boolean isForcefulUpdate);
    }

    public static Builder with(@NonNull Context context) {
        return new Builder(context);
    }

    private ForceUpdateChecker(@NonNull Context context,
                               OnUpdateNeededListener onUpdateNeededListener) {
        this.context = context;
        this.onUpdateNeededListener = onUpdateNeededListener;
    }

    private void check() {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        if (remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)) {
            String currentVersion = remoteConfig.getString(KEY_CURRENT_VERSION);
            String appVersion = getAppVersion(context);
            String updateUrl = remoteConfig.getString(KEY_UPDATE_URL);
            String minVersion = remoteConfig.getString(KEY_MIN_REQUIRED_VERSION);

            float appV = 0, currentV = 0, minimumV = 0;

            if (appVersion != null && appVersion.length() != 0) {
                appV = Float.parseFloat(appVersion);
            }

            if (currentVersion != null && currentVersion.length() != 0) {
                currentV = Float.parseFloat(currentVersion);
            }

            if (minVersion != null && minVersion.length() != 0) {
                minimumV = Float.parseFloat(minVersion);
            }

            if (appV < currentV && onUpdateNeededListener != null) {
                if (appV < minimumV) {
                    onUpdateNeededListener.onUpdateNeeded(updateUrl, true);
                } else {
                    onUpdateNeededListener.onUpdateNeeded(updateUrl, false);
                }
            }
        }
    }

    private String getAppVersion(Context context) {
        String result = "";
        try {
            result = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
            result = result.replaceAll("[a-zA-Z]|-", "");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }

        return result;
    }

    public static class Builder {

        private Context context;
        private OnUpdateNeededListener onUpdateNeededListener;

        Builder(Context context) {
            this.context = context;
        }

        public Builder onUpdateNeeded(OnUpdateNeededListener onUpdateNeededListener) {
            this.onUpdateNeededListener = onUpdateNeededListener;
            return this;
        }

        ForceUpdateChecker build() {
            return new ForceUpdateChecker(context, onUpdateNeededListener);
        }

        public void check() {
            ForceUpdateChecker forceUpdateChecker = build();
            forceUpdateChecker.check();
        }
    }
}
