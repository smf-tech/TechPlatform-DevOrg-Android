package com.platform.request;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.platform.BuildConfig;
import com.platform.Platform;
import com.platform.listeners.ProfileDetailRequestCallListener;
import com.platform.listeners.TMFilterListRequestCallListener;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class MatrimonyProfileDetailRequestCall {

    private ProfileDetailRequestCallListener listener;
    private final String TAG = MatrimonyProfileDetailRequestCall.class.getName();

    public void setListener(ProfileDetailRequestCallListener listener) {
        this.listener = listener;
    }

    /*public void getAllPendingRequests() {
        Response.Listener<JSONObject> pendingRequestsResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "Matrimony Profiles Request - Resp: " + res);
                    listener.onFilterListRequestsFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener pendingRequestsErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
       // final String getPendingRequestsUrl = BuildConfig.BASE_URL + Urls.Matrimony.GET_MATRIMONY_PROFILE_API;
        final String getPendingRequestsUrl = BuildConfig.BASE_URL
                    + String.format(Urls.Matrimony.GET_MATRIMONY_PROFILE_API,"5d6f90c25dda765c2f0b5dd4");

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
        //gsonRequest.setBodyParams(createBodyParams());
        gsonRequest.setShouldCache(false);

        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }*/

   /* private JSONObject createBodyParams() {
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
    }*/

   /* private JSONObject getFilterObject() {
        JSONObject requestObject =new JSONObject();

        try {
            requestObject.put("filterType","category");

        requestObject.put("id", getidObject());  // "5c6bbf3dd503a3057867cf24");
        requestObject.put("name","Training and Volunteers");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestObject;
    }*/

    /*private JSONArray getidObject() {
        JSONArray requestObject =new JSONArray();

        try {

            requestObject.put("5c6bbf3dd503a3057867cf24");
            requestObject.put("5c6bbf07d503a30a5e724eab");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestObject;
    }*/


    public void approveRejectRequest(JSONObject requestObject,int position,String requestType) {
        Response.Listener<JSONObject> approveRejectRequestsResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "approveRejectRequest - Resp: " + res);
                    listener.onRequestStatusChanged(res, position,requestType);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener approveRejectRequestsErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String approveRejectUrl = BuildConfig.BASE_URL + Urls.Matrimony.MARK_ATTENDANCE_INTERVIEW_API;

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
            gsonRequest.setBodyParams(requestObject);
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

        /*requestObject.addProperty(Constants.TM.UPDATE_STATUS, requestStatus);
        if (reason != null) {
            requestObject.addProperty(Constants.TM.REJECTION_REASON, reason);
        }*/

        return requestObject;
    }
}
