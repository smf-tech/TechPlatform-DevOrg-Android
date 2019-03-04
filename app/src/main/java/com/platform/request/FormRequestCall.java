package com.platform.request;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.platform.BuildConfig;
import com.platform.Platform;
import com.platform.listeners.FormRequestCallListener;
import com.platform.models.forms.Elements;
import com.platform.models.forms.FormData;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FormRequestCall {

    private FormRequestCallListener listener;
    private final String TAG = FormRequestCall.class.getName();

    public void setListener(FormRequestCallListener listener) {
        this.listener = listener;
    }

    public void createFormResponse(final HashMap<String, String> requestObjectMap, final Map<String,
            String> uploadedImageUrlList, String postUrl, final String formId) {
        JsonObject requestObject = getFormRequest(requestObjectMap, uploadedImageUrlList);
        Response.Listener<JSONObject> createFormResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    listener.onFormCreatedUpdated(res, new Gson().toJson(requestObject), formId);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener("");
            }
        };

        Response.ErrorListener createFormErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(Request.Method.POST,
                postUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                createFormResponseListener,
                createFormErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(requestObject);
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void updateFormResponse(final HashMap<String, String> requestObjectMap,
                                   final Map<String, String> uploadedImageUrlList, String postUrl, final String formId, String oid) {

        JsonObject requestObject = getFormRequest(requestObjectMap, uploadedImageUrlList);
        Response.Listener<JSONObject> createFormResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    listener.onFormCreatedUpdated(res, new Gson().toJson(requestObject), formId);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener("");
            }
        };

        Response.ErrorListener createFormErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.PUT,
                postUrl/* + "/" + formId + "/" + oid*/,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                createFormResponseListener,
                createFormErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(requestObject);
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void getChoicesByUrl(final Elements elements, final int pageIndex, final int elementIndex, final FormData formData) {
        Response.Listener<JSONObject> choicesResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.i(TAG, "getChoicesByUrl - Resp: " + res);
                    listener.onChoicesPopulated(res, elements, pageIndex, elementIndex, formData);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener("");
            }
        };

        Response.ErrorListener choicesErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                elements.getChoicesByUrl().getUrl(),
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                choicesResponseListener,
                choicesErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setShouldCache(false);

        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void getProcessDetails(String processId) {
        Response.Listener<JSONObject> processDetailsResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.i(TAG, "getProcessDetails - Resp: " + res);
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener("");
            }
        };

        Response.ErrorListener processDetailsErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getProcessUrl = BuildConfig.BASE_URL + String.format(Urls.PM.GET_PROCESS_DETAILS, processId);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getProcessUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                processDetailsResponseListener,
                processDetailsErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(new JsonObject());
        gsonRequest.setShouldCache(false);

        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void getFormResults(String processId) {
        Response.Listener<JSONObject> processResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    listener.onFormDetailsLoadedListener(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener("");
            }
        };

        Response.ErrorListener processErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getProcessUrl = BuildConfig.BASE_URL + String.format(Urls.PM.GET_FORM, processId);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getProcessUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                processResponseListener,
                processErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(new JsonObject());
        gsonRequest.setShouldCache(false);

        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    @NonNull
    private JsonObject getFormRequest(HashMap<String, String> requestObjectMap, final Map<String, String> imageUrls) {
        JsonObject requestObject = new JsonObject();
        for (Map.Entry<String, String> entry : requestObjectMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            requestObject.addProperty(key, value);
        }

        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (final Map.Entry<String, String> entry : imageUrls.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                requestObject.addProperty(key, value);
            }
        }

        return requestObject;
    }
}
