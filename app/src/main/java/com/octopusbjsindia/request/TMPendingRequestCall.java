package com.octopusbjsindia.request;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.listeners.TMPendingRequestCallListener;
import com.octopusbjsindia.models.tm.PendingRequest;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GsonRequestFactory;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TMPendingRequestCall {

    private TMPendingRequestCallListener listener;
    private final String TAG = TMPendingRequestCall.class.getName();

    public void setListener(TMPendingRequestCallListener listener) {
        this.listener = listener;
    }

    public void getAllPendingRequests(JSONObject jsonObject) {
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
        final String getPendingRequestsUrl = BuildConfig.BASE_URL + Urls.TM.GET_PENDING_APPROVAL_REQUESTS;

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.POST,
                getPendingRequestsUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                pendingRequestsResponseListener,
                pendingRequestsErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(jsonObject);
        gsonRequest.setShouldCache(false);

        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    private JSONObject createBodyParams() {
        JSONObject requestObject = new JSONObject();
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson("");
        Log.d(TAG, "SubmitRequest: " + json);

        try {
            requestObject.put("type","forms");
            requestObject.put("approval_type","pending");
            requestObject.put("filterSet", new JSONArray().put(getFilterObject()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            return requestObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject getFilterObject() {
        JSONObject requestObject =new JSONObject();

        try {
            requestObject.put("filterType","category");

        requestObject.put("id", getidObject());  // "5c6bbf3dd503a3057867cf24");
        requestObject.put("name","Training and Volunteers");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject;
    }

    private JSONArray getidObject() {
        JSONArray requestObject =new JSONArray();

        try {

            requestObject.put("5c6bbf3dd503a3057867cf24");
            requestObject.put("5c6bbf07d503a30a5e724eab");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestObject;
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
        if (requestStatus.equals(Constants.RequestStatus.REJECTED)) {
            gsonRequest.setBodyParams(getRequestStatusObject(requestStatus,
                    (String) pendingRequest.getReason()));
        } else {
            gsonRequest.setBodyParams(getRequestStatusObject(requestStatus, null));
        }
        gsonRequest.setShouldCache(false);

        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    @NonNull
    private JsonObject getRequestStatusObject(String requestStatus, String reason) {
        JsonObject requestObject = new JsonObject();
        requestObject.addProperty(Constants.TM.UPDATE_STATUS, requestStatus);
        if (reason != null) {
            requestObject.addProperty(Constants.TM.REJECTION_REASON, reason);
        }
        return requestObject;
    }
}
