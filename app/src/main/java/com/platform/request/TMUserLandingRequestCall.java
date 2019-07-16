package com.platform.request;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.platform.BuildConfig;
import com.platform.Platform;

import com.platform.listeners.TMUserLandingRequestCallListener;
import com.platform.models.tm.PendingRequest;
import com.platform.presenter.TMUserLandingFragmentPresenter;
import com.platform.utility.Constants;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONObject;

public class TMUserLandingRequestCall {

    private TMUserLandingRequestCallListener listener;
    private final String TAG = TMUserLandingRequestCall.class.getName();

    public void setListener(TMUserLandingRequestCallListener listener) {
        this.listener = listener;
    }

    public void getAllPendingRequests() {
        Response.Listener<JSONObject> pendingRequestsResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getAll TMLAnding Requests - Resp: " + res);
                    listener.onTMUserLandingRequestsFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener pendingRequestsErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getPendingRequestsUrl = BuildConfig.BASE_URL + Urls.TM.GET_APPROVALS_SUMMARY_REQUESTS;

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
