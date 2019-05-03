package com.platform.presenter;


import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.platform.Platform;
import com.platform.listeners.LeaveDataListener;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Util;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class LeavesPresenter {

    private WeakReference<LeaveDataListener> fragmentWeakReference;
    private Gson gson;

    public LeavesPresenter(LeaveDataListener tmFragment) {
        fragmentWeakReference = new WeakReference<>(tmFragment);
        gson = new GsonBuilder().serializeNulls()
                .create();
    }

    public void clearData() {
        fragmentWeakReference = null;
        gson = null;

    }

    public void getLeavesData() {

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (fragmentWeakReference == null) {
                return;
            }
            fragmentWeakReference.get().hideProgressBar();
            try {
                if (response != null) {
                    String res = response.toString();
                    fragmentWeakReference.get().onSuccessListener(res);

                }
            } catch (Exception e) {
                fragmentWeakReference.get().onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> fragmentWeakReference.get().onErrorListener(error);


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

    public void getUsersAllLeavesDetails() {

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (fragmentWeakReference == null) {
                return;
            }
            fragmentWeakReference.get().hideProgressBar();
            try {
                if (response != null) {
                    String res = response.toString();
                    fragmentWeakReference.get().onSuccessListener(res);

                }
            } catch (Exception e) {
                fragmentWeakReference.get().onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> fragmentWeakReference.get().onErrorListener(error);


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


    public void postUserLeave(JsonObject jsonObject) {

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (fragmentWeakReference == null) {
                return;
            }
            fragmentWeakReference.get().hideProgressBar();
            try {
                if (response != null) {
                    String res = response.toString();
                    fragmentWeakReference.get().onSuccessListener(res);

                }
            } catch (Exception e) {
                fragmentWeakReference.get().onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> fragmentWeakReference.get().onErrorListener(error);


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
