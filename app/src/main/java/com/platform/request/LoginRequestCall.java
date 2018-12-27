package com.platform.request;

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
import com.platform.models.login.LoginInfo;
import com.platform.utility.Constants;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

public class LoginRequestCall {

    private PlatformRequestCallListener listener;

    private final Response.Listener<JsonObject> loginSuccessListener = new Response.Listener<JsonObject>() {
        @Override
        public void onResponse(JsonObject response) {
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
        }
    };

    private final Response.ErrorListener loginErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
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
                Urls.Login.LOGIN,
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
                bodyParams.addProperty(Constants.Login.OTP, loginInfo.getOneTimePassword());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bodyParams;
    }
}
