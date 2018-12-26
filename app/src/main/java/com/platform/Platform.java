package com.platform;

import android.app.Application;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.platform.utility.Config;

public class Platform extends Application {

    private static Platform mPlatformInstance;
    private RequestQueue mRequestQueue;

    public static Platform getInstance() {
        return mPlatformInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mPlatformInstance = this;
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
