package com.octopus.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.octopus.BuildConfig;
import com.octopus.Platform;
import com.octopus.listeners.PlatformRequestCallListener;
import com.octopus.utility.GsonRequestFactory;
import com.octopus.utility.Urls;
import com.octopus.utility.Util;

import org.json.JSONObject;

public class ReportsRequestCall {

    private PlatformRequestCallListener listener;
    private final String TAG = ReportsRequestCall.class.getName();

    public void setListener(PlatformRequestCallListener listener) {
        this.listener = listener;
    }

    public void getAllReports() {

        Response.Listener<JSONObject> reportsResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getAllReports - Resp: " + res);
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener reportsErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getAllReportsUrl = BuildConfig.BASE_URL + Urls.Report.GET_ALL_REPORTS;

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getAllReportsUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                reportsResponseListener,
                reportsErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(new JsonObject());
        gsonRequest.setShouldCache(false);

        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }
}
