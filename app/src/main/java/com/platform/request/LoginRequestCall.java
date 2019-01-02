package com.platform.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.platform.Platform;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.models.Model;
import com.platform.models.login.LoginInfo;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

public class LoginRequestCall {

    private PlatformRequestCallListener listener;

    public void setListener(PlatformRequestCallListener listener) {
        this.listener = listener;
    }

    public void generateOtp(LoginInfo loginInfo) {
        Response.Listener<Model> generateOTPResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    // Login loginData = new Gson().fromJson(res, Login.class);
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
                listener.onFailureListener("");
            }
        };

        Response.ErrorListener generateOTPErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String generateOtpUrl = Urls.BASE_URL
                + String.format(Urls.Login.GENERATE_OTP, loginInfo.getMobileNumber());

        GsonRequestFactory<Model> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                generateOtpUrl,
                new TypeToken<Model>() {
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

        Response.Listener<Model> resendOTPResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    // Login loginData = new Gson().fromJson(res, Login.class);
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
                listener.onFailureListener("");
            }
        };

        Response.ErrorListener resendOTPErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String resendOtpUrl = Urls.BASE_URL
                + String.format(Urls.Login.GENERATE_OTP, loginInfo.getMobileNumber());

        GsonRequestFactory<Model> gsonRequest = new GsonRequestFactory<>(Request.Method.GET,
                resendOtpUrl,
                new TypeToken<Model>() {
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
        Response.Listener<Model> loginSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    // Login loginData = new Gson().fromJson(res, Login.class);
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
                listener.onFailureListener("");
            }
        };

        Response.ErrorListener loginErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getTokenUrl = Urls.BASE_URL + String.format(Urls.Login.GENERATE_TOKEN,
                loginInfo.getMobileNumber(), loginInfo.getOneTimePassword());

        GsonRequestFactory<Model> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getTokenUrl,
                new TypeToken<Model>() {
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
