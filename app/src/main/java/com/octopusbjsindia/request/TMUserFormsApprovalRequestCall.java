package com.octopusbjsindia.request;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.listeners.TMUserProfileApprovalRequestCallListener;
import com.octopusbjsindia.models.tm.PendingRequest;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GsonRequestFactory;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class TMUserFormsApprovalRequestCall {

    private TMUserProfileApprovalRequestCallListener listener;
    private final String TAG = TMUserFormsApprovalRequestCall.class.getName();

    public void setListener(TMUserProfileApprovalRequestCallListener listener) {
        this.listener = listener;
    }

    public void getAllPendingRequests(JSONObject requestObject) {
        Response.Listener<JSONObject> pendingRequestsResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getAll TMLAnding Requests - Resp: " + res);
                    listener.TMUserProfileApprovalRequestsFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener pendingRequestsErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getPendingRequestsUrl = BuildConfig.BASE_URL + Urls.TM.GET_TM_USER_DETAILS_REQUESTS;

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
        gsonRequest.setShouldCache(false);
        gsonRequest.setBodyParams(requestObject);
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
    private JSONObject getRequestStatusObject(String requestStatus, String reason) {
        JSONObject requestObject = null;
        try {
            requestObject = new JSONObject(requestStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*  JsonObject requestObject = new JsonObject();
        requestObject.addProperty(Constants.TM.UPDATE_STATUS, requestStatus);
        if (reason != null) {
            requestObject.addProperty(Constants.TM.REJECTION_REASON, reason);
        }*/
        return requestObject;
    }


    //new with position
    public void approveRejectRequest(String requestStatus,int position) {
        Response.Listener<JSONObject> approveRejectRequestsResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "approveRejectRequest - Resp: " + res);
                    listener.onRequestStatusChanged(res, position);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener approveRejectRequestsErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String approveRejectUrl = BuildConfig.BASE_URL + Urls.TM.GET_TM_USER_APPROVE_REJECT_REQUEST;

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.POST,
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
                    "Reason -test approval"));
        } else {
            gsonRequest.setBodyParams(getRequestStatusObject(requestStatus, null));
        }
        gsonRequest.setShouldCache(false);

        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    //get form details
    public void getFormDetailsRequestAPI(JSONObject requestObject,String type) {
        Response.Listener<JSONObject> pendingRequestsResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "form details Requests - Resp: " + res);
                    listener.onSuccessListener(res,type);

                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener pendingRequestsErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getPendingRequestsUrl = BuildConfig.BASE_URL + Urls.TM.GET_USER_FORM_RECORD;

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
        gsonRequest.setShouldCache(false);
        gsonRequest.setBodyParams(requestObject);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }
}
