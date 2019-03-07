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
import com.platform.listeners.TMPendingRequestCallListener;
import com.platform.models.tm.PendingRequest;
import com.platform.utility.Constants;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONObject;

import androidx.annotation.NonNull;

public class TMPendingRequestCall {

    private TMPendingRequestCallListener listener;
    private final String TAG = TMPendingRequestCall.class.getName();

    public void setListener(TMPendingRequestCallListener listener) {
        this.listener = listener;
    }

    public void getAllPendingRequests() {
        Response.Listener<JSONObject> pendingRequestsResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getAllPendingRequests - Resp: " + res);
                    listener.onPendingRequestsFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener pendingRequestsErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getPendingRequestsUrl = BuildConfig.BASE_URL + Urls.TM.GET_PENDING_REQUESTS;

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getPendingRequestsUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                pendingRequestsResponseListener,
                pendingRequestsErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setShouldCache(false);

        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void approveRejectRequest(String requestStatus, PendingRequest pendingRequest) {
        Response.Listener<JSONObject> approveRejectRequestsResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "approveRejectRequest - Resp: " + res);
                    listener.onRequestStatusChanged(res, pendingRequest);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener approveRejectRequestsErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String approveRejectUrl = BuildConfig.BASE_URL + String.format(Urls.TM.APPROVE_REJECT_REQUEST,
                pendingRequest.getId());

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.PUT,
                approveRejectUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                approveRejectRequestsResponseListener,
                approveRejectRequestsErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(getRequestStatusObject(requestStatus));
        gsonRequest.setShouldCache(false);

        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    @NonNull
    private JsonObject getRequestStatusObject(String requestStatus) {
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty(Constants.TM.UPDATE_STATUS, requestStatus);
        return requestObject;
    }
}
