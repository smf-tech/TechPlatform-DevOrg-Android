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
import com.platform.listeners.LeavePresenterListener;
import com.platform.models.leaves.LeaveData;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

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

    public void getLeavesData(String requestID) {

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (leavePresenterListener == null) {
                return;
            }

            try {
                if (response != null) {
                    String res = response.toString();
                    leavePresenterListener.onSuccessListener(requestID,res);

                }
            } catch (Exception e) {
                leavePresenterListener.onFailureListener(requestID,e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> leavePresenterListener.onErrorListener(requestID,error);


        final String getModulesUrl = ""; //BuildConfig.BASE_URL
        //+ String.format(Urls.Home.GET_MODULES, user.getOrgId(), user.getRoleIds());

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getModulesUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                getModulesResponseListener,
                getModulesErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(new JsonObject());
        gsonRequest.setShouldCache(false);
        //if(fragmentWeakReference != null) {
        //    fragmentWeakReference.get().showProgressBar();
        //}
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void getUsersAllLeavesDetails(String requestID, String year, String month) {

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (leavePresenterListener == null) {
                return;
            }

            try {
                if (response != null) {
                    String res = response.toString();
                    leavePresenterListener.onSuccessListener(requestID,res);

                }
            } catch (Exception e) {
                leavePresenterListener.onFailureListener(requestID,e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> leavePresenterListener.onErrorListener(requestID,error);

        final String getMontlyLeavesUrl = BuildConfig.BASE_URL
        + String.format(Urls.Leaves.GET_MONTHLY_LEAVES, year, month);

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
        //gsonRequest.setBodyParams(new JsonObject());
        gsonRequest.setShouldCache(false);
        //if(fragmentWeakReference != null) {
        //    fragmentWeakReference.get().showProgressBar();
        //}
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

    public void deleteUserLeave(String requestID, String leaveId) {

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (leavePresenterListener == null) {
                return;
            }

            try {
                if (response != null) {
                    String res = response.toString();
                    leavePresenterListener.onSuccessListener(requestID,res);

                }
            } catch (Exception e) {
                leavePresenterListener.onFailureListener(requestID,e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> leavePresenterListener.onErrorListener(requestID,error);

        final String deleteUserLeaveUrl = BuildConfig.BASE_URL
                + String.format(Urls.Leaves.DELETE_USER_LEAVE, leaveId);

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
        //gsonRequest.setBodyParams(new JsonObject());
        gsonRequest.setShouldCache(false);
        //if(fragmentWeakReference != null) {
        //    fragmentWeakReference.get().showProgressBar();
        //}
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
                    leavePresenterListener.onSuccessListener(requestID,res);

                }
            } catch (Exception e) {
                leavePresenterListener.onFailureListener(requestID,e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> leavePresenterListener.onErrorListener(requestID,error);

        final String getMontlyLeavesUrl = BuildConfig.BASE_URL
                + String.format(Urls.Leaves.HOLIDAY_LIST);

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
