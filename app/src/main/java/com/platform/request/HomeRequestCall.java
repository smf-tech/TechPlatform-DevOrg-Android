package com.platform.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.platform.Platform;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONObject;

public class HomeRequestCall {

    private PlatformRequestCallListener listener;

    public void setListener(PlatformRequestCallListener listener) {
        this.listener = listener;
    }

    public void getHomeModules() {
        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
                listener.onFailureListener("");
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getModulesUrl = Urls.BASE_URL
                + String.format(Urls.Home.GET_MODULES, "5c1b940ad503a31f360e1252", "5c1b9523d503a31f9e6ffc23");

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
