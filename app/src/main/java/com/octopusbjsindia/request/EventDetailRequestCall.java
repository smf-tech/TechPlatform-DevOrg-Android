package com.octopusbjsindia.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.EventDetailListener;
import com.octopusbjsindia.models.events.SetAttendanceCodeRequest;
import com.octopusbjsindia.presenter.EventDetailPresenter;
import com.octopusbjsindia.utility.GsonRequestFactory;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;

import org.json.JSONObject;

public class EventDetailRequestCall {

    private Gson gson;
    private EventDetailListener listener;
    private final String TAG = EventRequestCall.class.getName();

    public void setEventDetailListener(EventDetailPresenter eventDetailPresenter) {
        this.listener=eventDetailPresenter;
    }

    public void getAttendanceCode(String id) {
        Response.Listener<JSONObject> orgSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "GET_ATTENDANCE_CODE - Resp: " + res);
                    listener.onAttendanceCodeFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener orgErrorListener = error -> listener.onErrorListener(error);

        final String getOrgUrl = BuildConfig.BASE_URL + Urls.Events.GET_ATTENDANCE_CODE + "/"+id;
        Log.d(TAG, "GET_ATTENDANCE_CODE: " + getOrgUrl);

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

    public void setAttendanceCode(SetAttendanceCodeRequest request) {
        Response.Listener<JSONObject> orgSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "SET_ATTENDANCE_CODE - Resp: " + res);
                    listener.onAttendanceCodeSubmitted(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener orgErrorListener = error -> listener.onErrorListener(error);

        final String getOrgUrl = BuildConfig.BASE_URL + Urls.Events.SET_ATTENDANCE_CODE;
        Log.d(TAG, "SET_ATTENDANCE_CODE: " + getOrgUrl);
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(request);

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
        gsonRequest.setBodyParams(createBodyParams(json));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    private JSONObject createBodyParams(String json) {

        Log.d(TAG, "Request json: " + json);
        try {
            return  new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getMemberList(String id) {
        Response.Listener<JSONObject> orgSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "GET_ATTENDANCE_CODE - Resp: " + res);
                    listener.onParticipantsListFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener orgErrorListener = error -> listener.onErrorListener(error);

        final String getOrgUrl = BuildConfig.BASE_URL + Urls.Events.GET_EVENT_TASK_MEMBERS_LIST + "/"+id;
        Log.d(TAG, "GET_ATTENDANCE_CODE: " + getOrgUrl);

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

    public void setTaskMarkComplete(String id) {
        Response.Listener<JSONObject> orgSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "SET_TASK_MARK_COMPLETE - Resp: " + res);
                    listener.onTaskMarkComplete(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener orgErrorListener = error -> listener.onErrorListener(error);

        final String getOrgUrl = BuildConfig.BASE_URL + Urls.Events.SET_TASK_MARK_COMPLETE+"/"+id;
        Log.d(TAG, "SET_TASK_MARK_COMPLETE: " + getOrgUrl);

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

    public void delete(String id) {
        Response.Listener<JSONObject> orgSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "DELETE_EVENT_TASK - Resp: " + res);
                    listener.onDeleted(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener orgErrorListener = error -> listener.onErrorListener(error);

        final String getOrgUrl = BuildConfig.BASE_URL + Urls.Events.DELETE_EVENT_TASK +"/"+id;
        Log.d(TAG, "DELETE_EVENT_TASK: " + getOrgUrl);

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
}
