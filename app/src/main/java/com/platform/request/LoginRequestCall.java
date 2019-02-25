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
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.models.login.LoginInfo;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONObject;

public class LoginRequestCall {

    private PlatformRequestCallListener listener;
    private final String TAG = LoginRequestCall.class.getName();

    public void setListener(PlatformRequestCallListener listener) {
        this.listener = listener;
    }

    public void generateOtp(LoginInfo loginInfo) {
        Response.Listener<JSONObject> generateOTPResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener("");
            }
        };

        Response.ErrorListener generateOTPErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String generateOtpUrl = BuildConfig.BASE_URL
                + String.format(Urls.Login.GENERATE_OTP, loginInfo.getMobileNumber());

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                generateOtpUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                generateOTPResponseListener,
                generateOTPErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(false));
        gsonRequest.setBodyParams(new JsonObject());
        gsonRequest.setShouldCache(false);

        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void resendOtp(LoginInfo loginInfo) {

        Response.Listener<JSONObject> resendOTPResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener("");
            }
        };

        Response.ErrorListener resendOTPErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String resendOtpUrl = BuildConfig.BASE_URL
                + String.format(Urls.Login.GENERATE_OTP, loginInfo.getMobileNumber());

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(Request.Method.GET,
                resendOtpUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                resendOTPResponseListener,
                resendOTPErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(false));
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void getToken(LoginInfo loginInfo) {
        Response.Listener<JSONObject> loginSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    // Login loginData = new Gson().fromJson(res, Login.class);
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(response.toString());
            }
        };

        Response.ErrorListener loginErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getTokenUrl = BuildConfig.BASE_URL + String.format(Urls.Login.GENERATE_TOKEN,
                loginInfo.getMobileNumber(), loginInfo.getOneTimePassword());

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getTokenUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                loginSuccessListener,
                loginErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(false));
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }
}
