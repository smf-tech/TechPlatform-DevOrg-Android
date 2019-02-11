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
import com.platform.listeners.FormStatusCallListener;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONObject;

public class FormStatusRequestCall {

    private FormStatusCallListener listener;
    private final String TAG = FormStatusRequestCall.class.getName();

    public void setListener(FormStatusCallListener listener) {
        this.listener = listener;
    }

    public void getProcessDetails(String processId) {
        Response.Listener<JSONObject> processDetailsResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.i(TAG, "getProcessDetails - Resp: " + res);
                    listener.onFormsLoaded(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
                listener.onFailureListener(e.getMessage());
                Log.d(TAG, e.getMessage());
            }
        };

        Response.ErrorListener processDetailsErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getProcessUrl = BuildConfig.BASE_URL + String.format(Urls.PM.GET_PROCESS_DETAILS, processId);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getProcessUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                processDetailsResponseListener,
                processDetailsErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(new JsonObject());
        gsonRequest.setShouldCache(false);

        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void getAllProcesses() {
        Response.Listener<JSONObject> processDetailsResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.i(TAG, "getProcessDetails - Resp: " + res);
                    listener.onFormsLoaded(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
                listener.onFailureListener(e.getMessage());
                Log.d(TAG, e.getMessage());
            }
        };

        Response.ErrorListener processDetailsErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getProcessUrl = BuildConfig.BASE_URL + Urls.PM.GET_PROCESS;

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getProcessUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                processDetailsResponseListener,
                processDetailsErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(new JsonObject());
        gsonRequest.setShouldCache(false);

        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void getAllProcess(String processId) {
        Response.Listener<JSONObject> processResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    listener.onFormsLoaded(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
                listener.onFailureListener("");
                Log.d(TAG, e.getMessage());
            }
        };

        Response.ErrorListener processErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getProcessUrl = BuildConfig.BASE_URL + String.format(Urls.PM.GET_FORM, processId);

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
