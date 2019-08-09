package com.platform.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.platform.BuildConfig;
import com.platform.Platform;
import com.platform.R;
import com.platform.listeners.APIPresenterListener;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONObject;

public class MatrimonyMeetRequestCall {

    private Gson gson;
    private APIPresenterListener apiPresenterListener;
    private final String TAG = LeavesRequestCall.class.getName();

    public void setApiPresenterListener(APIPresenterListener listener) {
        this.apiPresenterListener = listener;
        gson = new GsonBuilder().serializeNulls()
                .create();
    }

    public void getMatrimonyMeetTypes(String requestID) {

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (apiPresenterListener == null) {
                return;
            }
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "matrimonyMeetTypes - Resp: " + res);
                    apiPresenterListener.onSuccessListener(requestID,res);
                }
            } catch (Exception e) {
                apiPresenterListener.onFailureListener(requestID,e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> apiPresenterListener.onErrorListener(requestID,error);

        final String getMatrimonyMeetTypesUrl = BuildConfig.BASE_URL
                + String.format(Urls.Matrimony.MATRIMONY_MEET_TYPES);
        Log.d(TAG, "getMatrimonyMeetTypesUrl: url" + getMatrimonyMeetTypesUrl);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getMatrimonyMeetTypesUrl,
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

    public void getJurisdictionLevelData(String orgId, String jurisdictionTypeId, String levelName, String requestID) {
        Response.Listener<JSONObject> jurisdictionSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getJurisdictionLevelData - Resp: " + res);
                    apiPresenterListener.onSuccessListener(res, requestID);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                apiPresenterListener.onFailureListener(requestID,Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener jurisdictionErrorListener = error -> apiPresenterListener.onErrorListener(requestID,error);

        final String getStateUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_JURISDICTION_LEVEL_DATA, orgId, jurisdictionTypeId, levelName);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getStateUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                jurisdictionSuccessListener,
                jurisdictionErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }
}
