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

    public void getSubmittedForms(final String formID, String url) {
        Response.Listener<JSONObject> processDetailsResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getSubmittedForms - Resp: " + res);
                    listener.onMastersFormsLoaded(res, formID);
                }
            } catch (Exception e) {
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener processDetailsErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                url,
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

    public void getProcesses() {
        Response.Listener<JSONObject> processDetailsResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getProcesses - Resp: " + res);
                    listener.onFormsLoaded(res);
                }
            } catch (Exception e) {
                listener.onFailureListener(e.getMessage());
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

}
