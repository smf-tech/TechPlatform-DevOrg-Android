package com.platform.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.platform.BuildConfig;
import com.platform.Platform;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONObject;

public class PMRequestCall {

    private PlatformRequestCallListener listener;
    private final String TAG = PMRequestCall.class.getName();

    public void setListener(PlatformRequestCallListener listener) {
        this.listener = listener;
    }

    public void getAllProcess() {
        Response.Listener<JSONObject> processResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
                listener.onFailureListener("");
                Log.d(TAG, e.getMessage());
            }
        };

        Response.ErrorListener processErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getProcessUrl = BuildConfig.BASE_URL + Urls.PM.GET_PROCESS;

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getProcessUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                processResponseListener,
                processErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(new JsonObject());
        gsonRequest.setShouldCache(false);

        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }
}
