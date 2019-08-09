package com.platform.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.platform.BuildConfig;
import com.platform.Platform;
import com.platform.listeners.APIPresenterListener;
import com.platform.models.leaves.LeaveData;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONObject;

public class LeavesRequestCall {

    private Gson gson;
    private APIPresenterListener apiPresenterListener;
    private final String TAG = LeavesRequestCall.class.getName();

    public void setApiPresenterListener(APIPresenterListener listener) {
        this.apiPresenterListener = listener;
        gson = new GsonBuilder().serializeNulls()
                .create();
    }

    public void getUsersAllLeavesDetails(String requestID, String year, String month) {

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (apiPresenterListener == null) {
                return;
            }

            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getUsersAllLeavesDetails - Resp: " + res);
                    apiPresenterListener.onSuccessListener(requestID,res);
                }
            } catch (Exception e) {
                apiPresenterListener.onFailureListener(requestID,e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> apiPresenterListener.onErrorListener(requestID,error);

        final String getMontlyLeavesUrl = BuildConfig.BASE_URL
        + String.format(Urls.Leaves.GET_MONTHLY_LEAVES, year, month);

        Log.d(TAG, "getMontlyLeaves: url" + getMontlyLeavesUrl);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getMontlyLeavesUrl,
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


    public void postUserLeave(String requestID, LeaveData leaveData) {

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (apiPresenterListener == null) {
                return;
            }
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "ApplyLeave - Resp: " + res);
                    apiPresenterListener.onSuccessListener(requestID,res);
                }
            } catch (Exception e) {
                apiPresenterListener.onFailureListener(requestID,e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> apiPresenterListener.onErrorListener(requestID,error);

        final String applyLeaveUrl = BuildConfig.BASE_URL + String.format(Urls.Leaves.APPLY_LEAVE);
        Gson gson = new GsonBuilder().create();
        String parmjson = gson.toJson(leaveData);
        Log.d(TAG, "ApplyLeave: url" + applyLeaveUrl);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.POST,
                applyLeaveUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                getModulesResponseListener,
                getModulesErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(createBodyParams(parmjson));
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void requestUserCompoff(String requestID, LeaveData leaveData) {

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (apiPresenterListener == null) {
                return;
            }
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "requestUserCompoff - Resp: " + res);
                    apiPresenterListener.onSuccessListener(requestID,res);
                }
            } catch (Exception e) {
                apiPresenterListener.onFailureListener(requestID,e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> apiPresenterListener.onErrorListener(requestID,error);

        final String requestUserCompoffUrl = BuildConfig.BASE_URL + String.format(Urls.Leaves.REQUEST_COMPOFF_);
        Gson gson = new GsonBuilder().create();
        String parmjson = gson.toJson(leaveData);
        Log.d(TAG, "requestUserCompoff: url" + requestUserCompoffUrl);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.POST,
                requestUserCompoffUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                getModulesResponseListener,
                getModulesErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(createBodyParams(parmjson));
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void deleteUserLeave(String requestID, String leaveId) {

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (apiPresenterListener == null) {
                return;
            }

            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "deleteUserLeave - Resp: " + res);
                    apiPresenterListener.onSuccessListener(requestID,res);

                }
            } catch (Exception e) {
                apiPresenterListener.onFailureListener(requestID,e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> apiPresenterListener.onErrorListener(requestID,error);

        final String deleteUserLeaveUrl = BuildConfig.BASE_URL
                + String.format(Urls.Leaves.DELETE_USER_LEAVE, leaveId);
        Log.d(TAG, "deleteUserLeaveUrl: url" + deleteUserLeaveUrl);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                deleteUserLeaveUrl,
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

    public void getHolidayList(String requestID) {

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (apiPresenterListener == null) {
                return;
            }

            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "deleteUserLeave - Resp: " + res);
                    apiPresenterListener.onSuccessListener(requestID,res);

                }
            } catch (Exception e) {
                apiPresenterListener.onFailureListener(requestID,e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> apiPresenterListener.onErrorListener(requestID,error);

        final String getMontlyLeavesUrl = BuildConfig.BASE_URL
                + String.format(Urls.Leaves.HOLIDAY_LIST);
        Log.d(TAG, "getMontlyLeavesUrl: url" + getMontlyLeavesUrl);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getMontlyLeavesUrl,
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
