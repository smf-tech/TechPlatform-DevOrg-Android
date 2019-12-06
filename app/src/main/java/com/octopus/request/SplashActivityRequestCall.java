package com.octopus.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.octopus.Platform;
import com.octopus.listeners.APIDataListener;
import com.octopus.utility.GsonRequestFactory;
import com.octopus.utility.Util;

import org.json.JSONObject;

public class SplashActivityRequestCall {

    private final String TAG = SplashActivityRequestCall.class.getName();
    private Gson gson;
    private APIDataListener listener;

    public void setApiPresenterListener(APIDataListener listener) {
        this.listener = listener;
        gson = new GsonBuilder().serializeNulls()
                .create();
    }


    public void getApiCall(final String requestID, String url) {

        Log.d(TAG, requestID + " URL: " + url);

        Response.Listener<JSONObject> getModulesResponseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response != null) {
                        String res = response.toString();
                        Log.d(TAG, requestID + " Resp: " + res);
                        listener.onSuccessListener(requestID, res);
                    }
                } catch (Exception e) {
                    listener.onFailureListener(requestID, e.getMessage());
                }
            }
        };

        Response.ErrorListener getModulesErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onErrorListener(requestID, error);
            }
        };

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                url,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                getModulesResponseListener,
                getModulesErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void login(final String requestID, String url) {

        Log.d(TAG, requestID + " URL: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (response != null) {
                                String res = response.toString();
                                Log.d(TAG, requestID + " Resp: " + res);
                                listener.onSuccessListener(requestID, res);
                            }
                        } catch (Exception e) {
                            listener.onFailureListener(requestID, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onErrorListener(requestID, error);
                    }
                });

        Platform.getInstance().getVolleyRequestQueue().add(stringRequest);

    }


    private JSONObject createBodyParams(String json) {
        Log.d(TAG, "Request json: " + json);
        try {
            return new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
