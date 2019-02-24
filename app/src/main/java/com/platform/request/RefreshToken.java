package com.platform.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;
import com.platform.BuildConfig;
import com.platform.Platform;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.models.login.Login;
import com.platform.utility.Constants;
import com.platform.utility.TokenRetryPolicy;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONObject;

import java.util.Map;

public class RefreshToken implements PlatformRequestCallListener {

    private static final String TAG = RefreshToken.class.getName();
    private int retryCounter = 0;
    private TokenRetryPolicy tokenRetryPolicy;

    public void refreshAccessToken(final TokenRetryPolicy tokenRetryPolicy) {
        this.tokenRetryPolicy = tokenRetryPolicy;

        Response.Listener<JSONObject> refreshTokenSuccessListener = response -> {
            try {
                Log.e(TAG, "REFRESH_TOKEN_RESP: " + response);
                tokenRetryPolicy.onRefreshTokenUpdate(Constants.SUCCESS, response.toString());
            } catch (Exception e) {
                tokenRetryPolicy.onRefreshTokenUpdate(Constants.FAILURE, "");
            }
        };

        Response.ErrorListener refreshTokenErrorListener = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (retryCounter <= 2) {
                    retryCounter++;
                    retryRefreshToken(refreshTokenSuccessListener, this);
                } else {
                    tokenRetryPolicy.onRefreshTokenUpdate(Constants.FAILURE, "");
                }
            }
        };

        try {
            JsonObjectRequest gsonRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    BuildConfig.BASE_URL + Urls.Login.REFRESH_TOKEN,
                    null,
                    refreshTokenSuccessListener,
                    refreshTokenErrorListener) {

                @Override
                public Map<String, String> getHeaders() {
                    return Util.requestHeader(false);
                }

                @Override
                public byte[] getBody() {
                    return createRefreshTokenBodyParams().toString().getBytes();
                }
            };

            gsonRequest.setShouldCache(false);
            Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
        } catch (Exception e) {
            Log.e(TAG, "Error in refresh token request");
            tokenRetryPolicy.onRefreshTokenUpdate(Constants.FAILURE, "");
        }
    }

    private void retryRefreshToken(Response.Listener<JSONObject> refreshTokenSuccessListener,
                                   Response.ErrorListener refreshTokenErrorListener) {

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.POST,
                BuildConfig.BASE_URL + Urls.Login.REFRESH_TOKEN,
                null,
                refreshTokenSuccessListener,
                refreshTokenErrorListener) {

            @Override
            public Map<String, String> getHeaders() {
                return Util.requestHeader(false);
            }

            @Override
            public byte[] getBody() {
                return createRefreshTokenBodyParams().toString().getBytes();
            }
        };

        jsObjRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(jsObjRequest);
    }

    private JsonObject createRefreshTokenBodyParams() {
        JsonObject bodyParams = new JsonObject();
        bodyParams.addProperty(Constants.App.GRANT_TYPE, Constants.App.REFRESH_TOKEN);
        bodyParams.addProperty(Constants.App.CLIENT_ID, BuildConfig.CLIENT_ID);
        bodyParams.addProperty(Constants.App.CLIENT_SECRET, BuildConfig.CLIENT_SECRET);
        bodyParams.addProperty(Constants.App.SCOPE, "*");

        //use refresh token
        Login login = Util.getLoginObjectFromPref();
        if (login != null) {
            bodyParams.addProperty(Constants.App.REFRESH_TOKEN, login.getLoginData().getRefreshToken());
        }

        return bodyParams;
    }

    @Override
    public void onSuccessListener(String response) {
        if (tokenRetryPolicy != null) {
            tokenRetryPolicy.onRefreshTokenUpdate(Constants.SUCCESS, response);
        }
    }

    @Override
    public void onFailureListener(String message) {
        if (tokenRetryPolicy != null) {
            tokenRetryPolicy.onRefreshTokenUpdate(Constants.FAILURE, "");
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (tokenRetryPolicy != null) {
            tokenRetryPolicy.onRefreshTokenUpdate(Constants.FAILURE, "");
        }
    }
}
