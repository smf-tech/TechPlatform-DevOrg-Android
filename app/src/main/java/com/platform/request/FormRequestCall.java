package com.platform.request;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
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
import java.util.List;
import java.util.Map;

public class FormRequestCall {

    private FormRequestCallListener listener;
    private final String TAG = FormRequestCall.class.getName();

    public void setListener(FormRequestCallListener listener) {
        this.listener = listener;
    }

    public void createFormResponse(final HashMap<String, String> requestObjectMap, HashMap<String,
            List<HashMap<String, String>>> matrixDynamicValuesMap, final List<Map<String,
            String>> uploadedImageUrlList, String postUrl, final String formId, final String oId, String callType) {

        JsonObject requestObject = getFormRequest(requestObjectMap, matrixDynamicValuesMap, uploadedImageUrlList);

        Log.d(TAG, "createFormResponse - url: " + postUrl);
        Log.d(TAG, "createFormResponse - req: " + requestObject);

        Response.Listener<JSONObject> createFormResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "createFormResponse - Resp: " + res);
                    listener.onFormCreatedUpdated(res, new Gson().toJson(requestObject), formId, callType, oId);
                }
            } catch (Exception e) {
                listener.onFailureListener(e.getMessage());
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

    public void updateFormResponse(final HashMap<String, String> requestObjectMap, HashMap<String,
            List<HashMap<String, String>>> matrixDynamicValuesMap, final List<Map<String,
            String>> uploadedImageUrlList, String postUrl, final String formId, String oid, String callType) {

        JsonObject requestObject = getFormRequest(requestObjectMap, matrixDynamicValuesMap, uploadedImageUrlList);

        Log.d(TAG, "updateFormResponse - url: " + postUrl);
        Log.d(TAG, "updateFormResponse - req: " + requestObject);

        Response.Listener<JSONObject> createFormResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "updateFormResponse - Resp: " + res);
                    listener.onFormCreatedUpdated(res, new Gson().toJson(requestObject), formId, callType, null);
                }
            } catch (Exception e) {
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener createFormErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.PUT,
                postUrl + "/" + oid,
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

    public void getChoicesByUrl(final Elements elements, final int pageIndex, final int elementIndex,
                                final int columnIndex, long rowIndex, final FormData formData, final String url,
                                HashMap<String, String> matrixDynamicInnerMap) {

        Log.d(TAG, "getChoicesByUrl - url: " + url);

        Response.Listener<JSONObject> choicesResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getChoicesByUrl - Resp: " + res);
                    listener.onChoicesPopulated(res, elements, pageIndex, elementIndex, columnIndex, rowIndex,
                            formData, matrixDynamicInnerMap);
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception in getChoicesByUrl()");
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener choicesErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                url,
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
                    Log.d(TAG, "getProcessDetails - Resp: " + res);
                    listener.onSuccessListener(res);
                }
            } catch (Exception e) {
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener processDetailsErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getProcessUrl = BuildConfig.BASE_URL + String.format(Urls.PM.GET_PROCESS_DETAILS, processId);
        Log.e(TAG, "getProcessUrl :" + getProcessUrl);

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

    public void getFormResults(String url) {
        Response.Listener<JSONObject> processResponseListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getFormResults - Resp: " + res);
                    listener.onFormDetailsLoadedListener(res);
                }
            } catch (Exception e) {
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener processErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                url,
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
    private JsonObject getFormRequest(HashMap<String, String> requestObjectMap, HashMap<String,
            List<HashMap<String, String>>> matrixDynamicValuesMap, final List<Map<String, String>> imageUrls) {

        JsonObject requestObject = new JsonObject();
        for (Map.Entry<String, String> entry : requestObjectMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            requestObject.addProperty(key, value);
        }

        for (Map.Entry<String, List<HashMap<String, String>>> entry : matrixDynamicValuesMap.entrySet()) {
            String outerKey = entry.getKey();
            JsonArray jsonArray = new JsonArray();
            for (int index = 0; index < entry.getValue().size(); index++) {
                JsonObject innerObject = new JsonObject();
                for (Map.Entry<String, String> innerEntry : entry.getValue().get(index).entrySet()) {
                    String key = innerEntry.getKey();
                    String value = innerEntry.getValue();
                    innerObject.addProperty(key, value);
                }
                jsonArray.add(innerObject);
            }
            requestObject.add(outerKey, jsonArray);
        }

        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (final Map<String, String> map : imageUrls) {
                for (final Map.Entry<String, String> entry : map.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    requestObject.addProperty(key, value);
                }
            }
        }

        Log.d(TAG, "FORM_BODY" + requestObject.toString());
        return requestObject;
    }
}
