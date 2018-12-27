package com.platform.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.platform.Platform;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.models.UserInfo;
import com.platform.utility.Constants;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

public class ProfileRequestCall {

    private PlatformRequestCallListener listener;

    public void setListener(PlatformRequestCallListener listener) {
        this.listener = listener;
    }

    public void submitUserProfile(UserInfo userInfo) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        GsonRequestFactory<JsonObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.POST,
                Urls.Login.SUBMIT_PROFILE,
                new TypeToken<JsonObject>() {
                }.getType(),
                gson,
                profileSuccessListener,
                profileErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader());
        gsonRequest.setBodyParams(createBodyParams(userInfo));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    private final Response.Listener<JsonObject> profileSuccessListener = new Response.Listener<JsonObject>() {
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

    private final Response.ErrorListener profileErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            listener.onErrorListener(error);

        }
    };

    private JsonObject createBodyParams(UserInfo userInfo) {
        JsonObject body = new JsonObject();
        if (userInfo != null) {
            body.addProperty(Constants.Login.USERNAME, userInfo.getUserFirstName());
            body.addProperty(Constants.Login.USER_GENDER, userInfo.getUserGender());
        }
        return body;
    }
}
