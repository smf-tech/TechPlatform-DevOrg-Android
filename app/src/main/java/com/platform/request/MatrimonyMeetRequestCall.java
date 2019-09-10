package com.platform.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.platform.Platform;
import com.platform.listeners.APIPresenterListener;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Util;

import org.json.JSONObject;

public class MatrimonyMeetRequestCall {

    private Gson gson;
    private APIPresenterListener apiPresenterListener;
    private final String TAG = MatrimonyMeetRequestCall.class.getName();

    public void setApiPresenterListener(APIPresenterListener listener) {
        this.apiPresenterListener = listener;
        gson = new GsonBuilder().serializeNulls()
                .create();
    }

    public void postDataApiCall(String requestID, String paramJson, String url) {

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (apiPresenterListener == null) {
                return;
            }
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, requestID + " Resp: " + res);
                    apiPresenterListener.onSuccessListener(requestID,res);
                }
            } catch (Exception e) {
                apiPresenterListener.onFailureListener(requestID,e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> apiPresenterListener.onErrorListener(requestID,error);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.POST,
                url,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                getModulesResponseListener,
                getModulesErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(createBodyParams(paramJson));
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void getDataApiCall(String requestID, String url) {

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (apiPresenterListener == null) {
                return;
            }

            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, requestID + " Resp: " + res);
                    apiPresenterListener.onSuccessListener(requestID,res);
                }
            } catch (Exception e) {
                apiPresenterListener.onFailureListener(requestID,e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> apiPresenterListener.onErrorListener(requestID,error);

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

    private JSONObject createBodyParams(String json) {
        Log.d(TAG, "Request json: " + json);
        try {
            return  new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
