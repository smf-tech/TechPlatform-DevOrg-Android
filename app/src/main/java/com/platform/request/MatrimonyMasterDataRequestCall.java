package com.platform.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.platform.BuildConfig;
import com.platform.Platform;
import com.platform.listeners.MatrimonyMasterDataRequestCallListener;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONObject;

import static com.platform.utility.Urls.Matrimony.USER_REGI_MASTER;

public class MatrimonyMasterDataRequestCall {
    private MatrimonyMasterDataRequestCallListener listener;
    private final String TAG = TMUserLandingRequestCall.class.getName();

    public void setListener(MatrimonyMasterDataRequestCallListener listener) {
        this.listener = listener;
    }

    public void getAllMasterDataRequests() {
        Response.Listener<JSONObject> pendingRequestsResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getAll TMLAnding Requests - Resp: " + res);
                    listener.MatrimonyMasterDataRequestsFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener pendingRequestsErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getPendingRequestsUrl = BuildConfig.BASE_URL + USER_REGI_MASTER;

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
