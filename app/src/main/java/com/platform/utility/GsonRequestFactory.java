package com.platform.utility;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.Map;

public class GsonRequestFactory<T> extends JsonRequest<T> {

    private final Gson gson;
    private final Type type;
    private final Response.Listener<T> listener;
    private final Response.ErrorListener errorListener;
    private final String TAG = GsonRequestFactory.class.getSimpleName();

    private Object bodyParams;
    private Map<String, String> headerParams;

    final int currentTimeout = 60 * 1000; //1 min
    final int maxNumRetries = DefaultRetryPolicy.DEFAULT_MAX_RETRIES;

    public GsonRequestFactory(final int requestMethod,
                              @NonNull final String url,
                              @NonNull final Type type,
                              @NonNull final Gson gson,
                              @NonNull final Response.Listener<T> listener,
                              @NonNull final Response.ErrorListener errorListener) {

        super(requestMethod, url, null, listener, errorListener);

        this.type = type;
        this.gson = gson;
        this.listener = listener;
        this.errorListener = errorListener;

        setRetryPolicy(new TokenRetryPolicy(this));
    }

    public void setHeaderParams(Map<String, String> headerParams) {
        this.headerParams = headerParams;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headerParams;
    }

    public void setBodyParams(Object bodyParams) {
        this.bodyParams = bodyParams;
    }

    @Override
    public byte[] getBody() {
        try {
            Log.v(TAG, "Body " + this.bodyParams.toString());
            return this.bodyParams.toString().getBytes(getParamsEncoding());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encoding not supported: " + e);
        } catch (Exception e) {
            throw new RuntimeException("Exception is: " + e);
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        try {
            if (error != null && error.networkResponse != null
                    && error.networkResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.e(TAG, "Unauthorized");
            } else {

                errorListener.onErrorResponse(error);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            errorListener.onErrorResponse(new ParseError(e));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonResponse = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            Class classType = getClassType();
            if (classType == null) {
                throw new NullPointerException("Class type is null...");
            }

            return (Response<T>) Response.success((classType.newInstance() instanceof JSONObject)
                            ? new JSONObject(jsonResponse) : gson.fromJson(jsonResponse, type),
                    HttpHeaderParser.parseCacheHeaders(response)
            );
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException | InstantiationException | IllegalAccessException e) {
            Log.e(TAG, e.getMessage());
            return Response.error(new ParseError(e));
        }
    }

    private Class getClassType() {
        try {
            return Class.forName(type.toString().split(" ")[1]);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
}
