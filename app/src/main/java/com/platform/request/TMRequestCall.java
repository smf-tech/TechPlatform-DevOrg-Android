package com.platform.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.platform.Platform;
import com.platform.listeners.TMRequestCallListener;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONObject;

public class TMRequestCall {

    private TMRequestCallListener listener;

    public void setListener(TMRequestCallListener listener) {
        this.listener = listener;
    }

    public void getAllPendingRequests() {
        Response.Listener<JSONObject> pendingRequestsResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    listener.onPendingRequestsFetched(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
                listener.onFailureListener("");
            }
        };

        Response.ErrorListener pendingRequestsErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getPendingRequestsUrl = Urls.BASE_URL + Urls.TM.GET_PENDING_REQUESTS;

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
}