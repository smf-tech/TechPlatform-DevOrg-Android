package com.platform.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.platform.Platform;
import com.platform.listeners.LeavePresenterListener;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Util;

import org.json.JSONObject;

public class LeavesRequestCall {

    private Gson gson;
    private LeavePresenterListener leavePresenterListener;

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

    public void getUsersAllLeavesDetails(String requestID) {

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


    public void postUserLeave(String requestID,JsonObject jsonObject) {

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
                Request.Method.POST,
                getModulesUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                getModulesResponseListener,
                getModulesErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(jsonObject);
        gsonRequest.setShouldCache(false);
        //if(fragmentWeakReference != null) {
        //    fragmentWeakReference.get().showProgressBar();
        //}
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }
}
