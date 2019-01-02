package com.platform.request;

import com.android.volley.Request;
import com.android.volley.Response;
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
        Response.Listener<JsonObject> profileSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
                listener.onFailureListener("");
            }
        };

        Response.ErrorListener profileErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String submitProfileUrl = Urls.BASE_URL
                + String.format(Urls.Login.SUBMIT_PROFILE, userInfo.getUserMobileNumber());

        GsonRequestFactory<JsonObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.PUT,
                submitProfileUrl,
                new TypeToken<JsonObject>() {
                }.getType(),
                gson,
                profileSuccessListener,
                profileErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(createBodyParams(userInfo));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    private JsonObject createBodyParams(UserInfo userInfo) {
        JsonObject body = new JsonObject();
        if (userInfo != null) {
            body.addProperty(Constants.Login.USER_F_NAME, userInfo.getUserFirstName());
            body.addProperty(Constants.Login.USER_M_NAME, userInfo.getUserMiddleName());
            body.addProperty(Constants.Login.USER_L_NAME, userInfo.getUserLastName());
            body.addProperty(Constants.Login.USER_BIRTH_DATE, userInfo.getUserBirthDate());
            body.addProperty(Constants.Login.USER_EMAIL_ID, userInfo.getUserEmailId());
            body.addProperty(Constants.Login.USER_GENDER, userInfo.getUserGender());
        }
        return body;
    }
}
