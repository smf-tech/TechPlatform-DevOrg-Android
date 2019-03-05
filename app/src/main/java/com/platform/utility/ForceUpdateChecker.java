package com.platform.utility;

import android.content.Context;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

@SuppressWarnings("CanBeFinal")
public class ForceUpdateChecker {

    private static final String TAG = ForceUpdateChecker.class.getSimpleName();
    public static final String KEY_UPDATE_REQUIRED = "SS_force_update_required";
    public static final String KEY_CURRENT_VERSION = "SS_force_update_current_version";
    public static final String KEY_UPDATE_URL = "SS_force_update_store_url";

    private OnUpdateNeededListener onUpdateNeededListener;
    private Context context;

    public interface OnUpdateNeededListener {
        void onUpdateNeeded(String updateUrl);
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
            float appV = 0, currentV = 0;

            if (appVersion != null && appVersion.length() != 0) {
                appV = Float.parseFloat(appVersion);
            }

            if (currentVersion != null && currentVersion.length() != 0) {
                currentV = Float.parseFloat(currentVersion);
            }

            if (appV < currentV && onUpdateNeededListener != null) {
                onUpdateNeededListener.onUpdateNeeded(updateUrl);
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
