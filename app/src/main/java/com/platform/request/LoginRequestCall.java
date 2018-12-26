package com.platform.request;

import android.util.Log;

import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.platform.BuildConfig;
import com.platform.Platform;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.models.login.Login;
import com.platform.models.login.LoginInfo;
import com.platform.utility.Constants;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Util;

public class LoginRequestCall {

    private final String TAG = this.getClass().getSimpleName();

    private Login loginData;
    private PlatformRequestCallListener listener;

    private final Response.Listener<JsonObject> loginSuccessListener = new Response.Listener<JsonObject>() {
        @Override
        public void onResponse(JsonObject response) {
            try {
                if (response != null) {
                    String res = response.toString();
                    loginData = new Gson().fromJson(res, Login.class);
                    listener.onSuccessListener(loginData);
                }
            } catch (Exception e) {
                e.printStackTrace();
                listener.onFailureListener("");
            }
        }
    };

    private final Response.ErrorListener loginErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            listener.onErrorListener(error);
        }
    };

    private final Response.Listener<JsonObject> successNotificationListener = new Response.Listener<JsonObject>() {
        @Override
        public void onResponse(JsonObject response) {
            if (response != null) {
                Log.d(TAG, response.getAsString());
            }

            listener.onSuccessListener(loginData);
        }
    };

    private final Response.ErrorListener errorNotificationListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            // Deal with the uploadFileError here
            Log.e(TAG, new ParseError(error).getMessage());
            listener.onSuccessListener(loginData);
            listener.onErrorListener(error);
        }
    };

    public void setListener(PlatformRequestCallListener listener) {
        this.listener = listener;
    }

    public void login(LoginInfo loginInfo) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        GsonRequestFactory<JsonObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.POST,
                BuildConfig.LOGIN_URL,
                new TypeToken<JsonObject>() {
                }.getType(),
                gson,
                loginSuccessListener,
                loginErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader());
        gsonRequest.setBodyParams(createBodyParams(loginInfo));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    private JsonObject createBodyParams(LoginInfo loginInfo) {
        JsonObject bodyParams = new JsonObject();
        try {
            bodyParams.addProperty(Constants.App.CLIENT_SECRET, BuildConfig.CLIENT_SECRET);
            bodyParams.addProperty(Constants.App.CLIENT_ID, BuildConfig.CLIENT_ID);
            bodyParams.addProperty(Constants.App.GRANT_TYPE, Constants.App.PASSWORD);

            if (loginInfo != null) {
                bodyParams.addProperty(Constants.Login.USERNAME, loginInfo.getMobileNumber());
                bodyParams.addProperty(Constants.Login.OTP, loginInfo.getOtp());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bodyParams;
    }
}
