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
import com.platform.listeners.UserRequestCallListener;
import com.platform.models.user.UserInfo;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONObject;

public class HomeRequestCall {

    private UserRequestCallListener listener;
    private final String TAG = HomeRequestCall.class.getName();

    public void setListener(UserRequestCallListener listener) {
        this.listener = listener;
    }

    public void getHomeModules(UserInfo user) {
        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getHomeModules - Resp: " + res);
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getModulesUrl = BuildConfig.BASE_URL
                + String.format(Urls.Home.GET_MODULES, user.getOrgId(), user.getRoleIds());

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

        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }
}
