package com.octopusbjsindia.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.listeners.PlatformRequestCallListener;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.utility.GsonRequestFactory;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;

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
                    Log.d(TAG, "getAllProcess - Resp: " + res);
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(e.getMessage());
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

    public void getAllFormsCount(UserInfo user) {
        Response.Listener<JSONObject> processResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getAllFormsCount - Resp: " + res);
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener processErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getFormsStatusCountUrl = BuildConfig.BASE_URL + String.format(Urls.PM.GET_DASHBOARD_DETAILS, user.getId(),user.getOrgId()) ;

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getFormsStatusCountUrl,
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
