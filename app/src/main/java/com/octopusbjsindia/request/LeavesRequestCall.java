package com.octopusbjsindia.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.listeners.LeavePresenterListener;
import com.octopusbjsindia.models.leaves.LeaveData;
import com.octopusbjsindia.utility.GsonRequestFactory;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;

import org.json.JSONObject;

public class LeavesRequestCall {

    private Gson gson;
    private LeavePresenterListener leavePresenterListener;
    private final String TAG = LeavesRequestCall.class.getName();

    public void setLeavePresenterListener(LeavePresenterListener listener) {
        this.leavePresenterListener = listener;
        gson = new GsonBuilder().serializeNulls()
                .create();
    }

    public void getUsersAllLeavesDetails(String requestID, String year, String month) {

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (leavePresenterListener == null) {
                return;
            }

            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getUsersAllLeavesDetails - Resp: " + res);
                    leavePresenterListener.onSuccessListener(requestID,res);
                }
            } catch (Exception e) {
                leavePresenterListener.onFailureListener(requestID,e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> leavePresenterListener.onErrorListener(requestID,error);

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
            if (leavePresenterListener == null) {
                return;
            }
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "ApplyLeave - Resp: " + res);
                    leavePresenterListener.onSuccessListener(requestID,res);
                }
            } catch (Exception e) {
                leavePresenterListener.onFailureListener(requestID,e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> leavePresenterListener.onErrorListener(requestID,error);

        final String applyLeaveUrl = BuildConfig.BASE_URL + String.format(Urls.Leaves.APPLY_LEAVE);
        Gson gson = new GsonBuilder().create();
        String parmjson = gson.toJson(leaveData);
        Log.d(TAG, "ApplyLeave: url" + applyLeaveUrl);
        Log.d(TAG, "ApplyLeave: request" + parmjson);

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
            if (leavePresenterListener == null) {
                return;
            }
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "requestUserCompoff - Resp: " + res);
                    leavePresenterListener.onSuccessListener(requestID,res);
                }
            } catch (Exception e) {
                leavePresenterListener.onFailureListener(requestID,e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> leavePresenterListener.onErrorListener(requestID,error);

        final String requestUserCompoffUrl = BuildConfig.BASE_URL + String.format(Urls.Leaves.REQUEST_COMPOFF_);
        Gson gson = new GsonBuilder().create();
        String parmjson = gson.toJson(leaveData);
        Log.d(TAG, "requestUserCompoff: url" + requestUserCompoffUrl);
        Log.d(TAG, "requestUserCompoff: request" + parmjson);

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
            if (leavePresenterListener == null) {
                return;
            }

            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "deleteUserLeave - Resp: " + res);
                    leavePresenterListener.onSuccessListener(requestID,res);

                }
            } catch (Exception e) {
                leavePresenterListener.onFailureListener(requestID,e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> leavePresenterListener.onErrorListener(requestID,error);

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
            if (leavePresenterListener == null) {
                return;
            }

            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "deleteUserLeave - Resp: " + res);
                    leavePresenterListener.onSuccessListener(requestID,res);

                }
            } catch (Exception e) {
                leavePresenterListener.onFailureListener(requestID,e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> leavePresenterListener.onErrorListener(requestID,error);

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

    public void getLeavesBalance(String requestID) {

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (leavePresenterListener == null) {
                return;
            }
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "LEAVE_BALANCE - Resp: " + res);
                    leavePresenterListener.onSuccessListener(requestID,res);
                }
            } catch (Exception e) {
                leavePresenterListener.onFailureListener(requestID,e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> leavePresenterListener.onErrorListener(requestID,error);

        final String getMontlyLeavesUrl = BuildConfig.BASE_URL
                + String.format(Urls.Leaves.LEAVE_BALANCE);
        Log.d(TAG, "LEAVE_BALANCE: url" + getMontlyLeavesUrl);

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
}
