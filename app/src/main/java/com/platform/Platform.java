package com.platform;

import android.app.Application;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Platform extends Application {

    private static Platform mPlatformInstance;
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();

        mPlatformInstance = this;
    }

    public static Platform getInstance() {
        return mPlatformInstance;
    }

    @NonNull
    public RequestQueue getVolleyRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
}
