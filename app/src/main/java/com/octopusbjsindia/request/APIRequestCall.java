package com.octopusbjsindia.request;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.utility.GsonRequestFactory;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.utility.VolleyMultipartRequest;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class APIRequestCall {

    private Gson gson;
    private APIPresenterListener apiPresenterListener;
    private final String TAG = APIRequestCall.class.getName();

    public void setApiPresenterListener(APIPresenterListener listener) {
        this.apiPresenterListener = listener;
        gson = new GsonBuilder().serializeNulls()
                .create();
    }

    public void postDataApiCall(String requestID, String paramJson, String url) {

        Log.d(TAG, requestID + " url : " + url);
        Log.d(TAG, requestID + " request Json : " + paramJson);

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (apiPresenterListener == null) {
                return;
            }
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, requestID + " Resp: " + res);
                    apiPresenterListener.onSuccessListener(requestID, res);
                }
            } catch (Exception e) {
                apiPresenterListener.onFailureListener(requestID, e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> apiPresenterListener.onErrorListener(requestID, error);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.POST,
                url,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                getModulesResponseListener,
                getModulesErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(createBodyParams(paramJson));
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void postDataCustomizeHeaderApiCall(String requestID, String paramJson, String url, String orgId,
                                               String projectId, String roleId) {

        Log.d(TAG, requestID + " url : " + url);
        Log.d(TAG, requestID + " request Json : " + paramJson);

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (apiPresenterListener == null) {
                return;
            }
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, requestID + " Resp: " + res);
                    apiPresenterListener.onSuccessListener(requestID, res);
                }
            } catch (Exception e) {
                apiPresenterListener.onFailureListener(requestID, e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> apiPresenterListener.onErrorListener(requestID, error);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.POST,
                url,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                getModulesResponseListener,
                getModulesErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestCustomHeader(true, orgId, projectId, roleId));
        gsonRequest.setBodyParams(createBodyParams(paramJson));
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void getDataApiCall(String requestID, String url) {

        Log.d(TAG, requestID + " url : " + url);

        Response.Listener<JSONObject> getModulesResponseListener = response -> {
            if (apiPresenterListener == null) {
                return;
            }
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, requestID + " Resp: " + res);
                    apiPresenterListener.onSuccessListener(requestID, res);
                }
            } catch (Exception e) {
                Log.d(TAG, requestID + " Exp: " + e.getMessage());
                apiPresenterListener.onFailureListener(requestID, e.getMessage());
            }
        };

        Response.ErrorListener getModulesErrorListener = error -> {
            apiPresenterListener.onErrorListener(requestID, error);
        };

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                url,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                getModulesResponseListener,
                getModulesErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    private JSONObject createBodyParams(String json) {
        try {
            return new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void multipartApiCall(String requestID, String paramJson, HashMap<String, Bitmap> imageHashmap,
                                 Resources resources, String url) {
        Log.d(requestID + " URL:", url);
        Log.d(requestID + " req:", paramJson);

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
//                        rQueue.getCache().clear();
                        try {
                            String res = response.toString();
                            Log.d(requestID + " res:", res);
                            apiPresenterListener.onSuccessListener(requestID, res);
                        } catch (Exception e) {
                            apiPresenterListener.onFailureListener(requestID, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        apiPresenterListener.onErrorListener(requestID, error);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("formData", paramJson);
                params.put("imageArraySize", String.valueOf(imageHashmap.size()));//add string parameters
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                Drawable drawable = null;
                Iterator myVeryOwnIterator = imageHashmap.keySet().iterator();
                for (int i = 0; i < imageHashmap.size(); i++) {
                    String key = (String) myVeryOwnIterator.next();
                    drawable = new BitmapDrawable(resources, imageHashmap.get(key));
                    params.put(key, new DataPart(key, getFileDataFromDrawable(drawable),
                            "image/jpeg"));
                }
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Platform.getInstance().getVolleyRequestQueue().add(volleyMultipartRequest);
//        rQueue = Volley.newRequestQueue(this);
//        rQueue.add(volleyMultipartRequest);
    }

    private byte[] getFileDataFromDrawable(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
