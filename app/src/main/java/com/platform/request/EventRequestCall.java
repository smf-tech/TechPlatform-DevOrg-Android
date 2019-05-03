package com.platform.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.platform.BuildConfig;
import com.platform.Platform;
import com.platform.R;
import com.platform.listeners.AddMemberRequestCallListener;
import com.platform.listeners.CreateEventListener;
import com.platform.models.events.Event;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONObject;

public class EventRequestCall {

    private Gson gson;
    private CreateEventListener createEventListener;
    private AddMemberRequestCallListener addMemberRequestCallListener;
    private final String TAG = EventRequestCall.class.getName();

    public void setCreateEventListener(CreateEventListener listener) {
        this.createEventListener = listener;
    }

    public void setAddMemberRequestCallListener(AddMemberRequestCallListener listener) {
        this.addMemberRequestCallListener = listener;
    }

    public void getCategory() {

        Response.Listener<JSONObject> orgSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getCategory - Resp: " + res);
                    createEventListener.onCategoryFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                createEventListener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener orgErrorListener = error -> createEventListener.onErrorListener(error);

        final String getOrgUrl = BuildConfig.BASE_URL + Urls.Events.GET_CATEGORY;

        Log.d(TAG, "getCategory: " + getOrgUrl);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getOrgUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                orgSuccessListener,
                orgErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void getEvent(String status) {
        Response.Listener<JSONObject> orgSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getEvents - Resp: " + res);
                    createEventListener.onEventsFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                createEventListener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener orgErrorListener = error -> createEventListener.onErrorListener(error);

        final String getOrgUrl = BuildConfig.BASE_URL + Urls.Events.GET_EVENTS + "?status=" + status;
//        final String getOrgUrl = BuildConfig.BASE_URL + String.format(Urls.Events.GET_EVENTS,status);
        Log.d(TAG, "getEvents: " + getOrgUrl);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getOrgUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                orgSuccessListener,
                orgErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void getMemberList() {
        Response.Listener<JSONObject> orgSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getEvents - Resp: " + res);
                    addMemberRequestCallListener.onMembersFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                addMemberRequestCallListener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener orgErrorListener = error -> addMemberRequestCallListener.onErrorListener(error);

        final String getOrgUrl = BuildConfig.BASE_URL + Urls.Events.GET_MEMBERS_LIST;
        Log.d(TAG, "getEvents: " + getOrgUrl);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getOrgUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                orgSuccessListener,
                orgErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void submitEvent(Event event) {
        Response.Listener<JSONObject> orgSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "SubmitEvents - Resp: " + res);
                    createEventListener.onEventSubmitted(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                createEventListener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener orgErrorListener = error -> createEventListener.onErrorListener(error);

        final String getOrgUrl = BuildConfig.BASE_URL + Urls.Events.SUBMIT_EVENT;
        Log.d(TAG, "SubmitEvents: " + getOrgUrl);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.POST,
                getOrgUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                orgSuccessListener,
                orgErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }
}
