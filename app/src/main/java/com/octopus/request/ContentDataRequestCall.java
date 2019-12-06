package com.octopus.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.octopus.BuildConfig;
import com.octopus.Platform;
import com.octopus.listeners.ContentDataListener;
import com.octopus.utility.GsonRequestFactory;
import com.octopus.utility.Urls;
import com.octopus.utility.Util;

import org.json.JSONObject;

public class ContentDataRequestCall {

    ContentDataListener contentDataListener;
    private String TAG=ContentDataRequestCall.class.getSimpleName();

    public void setContentDataListener(ContentDataListener contentDataListener)
    {
        this.contentDataListener=contentDataListener;
    }

    public void getContentData(){
        Response.Listener<JSONObject> contentDataSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "Content - Resp: " + res);
                    contentDataListener.onSuccess(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        };

        Response.ErrorListener contentDataErrorListener=new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Content - Error: " +error);
                contentDataListener.onError(error.toString());
            }
        };



        Gson gson = new GsonBuilder().serializeNulls().create();

        final String CONTENT_DATA = BuildConfig.BASE_URL+ Urls.ContentManagement.GET_CONTENT_DATA;

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                CONTENT_DATA,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                contentDataSuccessListener,
                contentDataErrorListener
        );

        // if we send token with ur

        try{
            gsonRequest.setHeaderParams(Util.requestHeader(true));
            gsonRequest.setShouldCache(false);
            Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
        }catch (Exception e){
            Log.i("NetworkException","111"+e.toString());
        }



    }
}
