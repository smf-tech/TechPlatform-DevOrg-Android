package com.platform;

import android.app.Application;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.platform.utility.Config;
import com.platform.utility.Util;

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
        Util.makeDirectory(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MV/Image");
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
