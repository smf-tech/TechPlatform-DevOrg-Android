package com.platform.request;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.platform.Platform;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.utility.Constants;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FormCreateRequestCall {

    private PlatformRequestCallListener listener;

    public void setListener(PlatformRequestCallListener listener) {
        this.listener = listener;
    }


    public void createFormResponse(String formId, HashMap<String, String> requestObjectMap) {

        Response.Listener<JSONObject> createFormResponseListener = response -> {
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

        Response.ErrorListener createFormErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String createFormUrl = Urls.BASE_URL
                + String.format(Urls.PM.CREATE_FORM, formId);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(Request.Method.POST,
                createFormUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                createFormResponseListener,
                createFormErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(getFormRequest(requestObjectMap));
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    @NonNull
    private JsonObject getFormRequest(HashMap<String, String> requestObjectMap) {
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, String> entry : requestObjectMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.d("TAG", "Request object key " + key + " value " + value);
            jsonObject.addProperty(key, value);
        }

        JsonObject response = new JsonObject();
        response.add(Constants.PM.RESPONSE, jsonObject);
        return response;
    }


}
