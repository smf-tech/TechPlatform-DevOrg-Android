package com.octopusbjsindia.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.listeners.UserRequestCallListener;
import com.octopusbjsindia.models.login.LoginInfo;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GsonRequestFactory;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;

import org.json.JSONObject;

public class LoginRequestCall {

    private UserRequestCallListener listener;
    private final String TAG = LoginRequestCall.class.getName();

    public void setListener(UserRequestCallListener listener) {
        this.listener = listener;
    }

    public void generateOtp(LoginInfo loginInfo) {
        Response.Listener<JSONObject> generateOTPResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "generateOtp - Resp: " + res);
                    }
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(e.getMessage());
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
                  //  Log.d(TAG, "resendOtp - Resp: " + res);
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(e.getMessage());
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
                    Log.d(TAG, "getToken - Resp: " + res);
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener loginErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getTokenUrl = BuildConfig.BASE_URL + Urls.Login.GENERATE_TOKEN;
        Log.d(TAG, "getToken - url: " + getTokenUrl);
        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.POST,
                getTokenUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                loginSuccessListener,
                loginErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(false));
        gsonRequest.setBodyParams(createBodyParams(loginInfo));
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    private JsonObject createBodyParams(LoginInfo loginInfo) {
        JsonObject body = new JsonObject();
        if (loginInfo != null) {
            try {
                body.addProperty(Constants.Login.USER_PHONE, loginInfo.getMobileNumber());
                body.addProperty(Constants.Login.USER_OTP, loginInfo.getOneTimePassword());
                body.addProperty("device_id", loginInfo.getDeviceId());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        Log.i(TAG, "AUTH_BODY: " + body);
        return body;
    }


    public void getUserProfile() {
        Response.Listener<JSONObject> userProfileSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getUserProfile - Resp: " + res);
                    listener.onUserProfileSuccessListener(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener userProfileErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getProfileUrl = BuildConfig.BASE_URL + Urls.Profile.GET_PROFILE;
        Log.d(TAG, "getUserProfile - url: " + getProfileUrl);
        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getProfileUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                userProfileSuccessListener,
                userProfileErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }
}
