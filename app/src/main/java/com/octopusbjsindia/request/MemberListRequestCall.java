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
import com.octopusbjsindia.listeners.MemberListListener;
import com.octopusbjsindia.models.events.Participant;
import com.octopusbjsindia.utility.GsonRequestFactory;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MemberListRequestCall {

    private Gson gson;
    MemberListListener listener;
    private final String TAG = MemberListRequestCall.class.getName();

    public MemberListRequestCall() {
        gson = new GsonBuilder().serializeNulls().create();
    }

    public void setListener(MemberListListener listener) {
        this.listener=listener;
    }

    public void deleteMember(String eventTaskID, String userId) {
        Response.Listener<JSONObject> profileSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "onMembersDeleted - Resp: " + res);
                    listener.onMembersDeleted(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener profileErrorListener = error -> listener.onErrorListener(error);

        final String submitProfileUrl = BuildConfig.BASE_URL + Urls.Events.DELETE_EVENT_TASK_MEMBERS;

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("eventId", eventTaskID);
            jsonObj.put("memberId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "onMembersDeleted - URL: " + submitProfileUrl);
        Log.d(TAG, "onMembersDeleted - Req: " + jsonObj);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.POST,
                submitProfileUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                profileSuccessListener,
                profileErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(jsonObj);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);

    }

    public void getTaskMemberList() {
        Response.Listener<JSONObject> orgSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "GET_TASK_MEMBERS_LIST - Resp: " + res);
                    listener.onTaskMembersFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener orgErrorListener = error -> listener.onErrorListener(error);

        final String getOrgUrl = BuildConfig.BASE_URL + Urls.Events.GET_TASK_MEMBERS_LIST;
        Log.d(TAG, "GET_TASK_MEMBERS_LIST: " + getOrgUrl);

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


    public void setMemberToEventTask(String eventTaskID, ArrayList<Participant> list) {
        Response.Listener<JSONObject> orgSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "GET_TASK_MEMBERS_LIST - Resp: " + res);
                    listener.onMemberListUpdated(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener orgErrorListener = error -> listener.onErrorListener(error);

        final String getOrgUrl = BuildConfig.BASE_URL + Urls.Events.UPDATE_MEMBER_LIST;

        Map<String, Object> request = new HashMap<String, Object>();
        request.put("_id",eventTaskID);
        request.put("participants",list);
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(request);

        Log.d(TAG, "UPDATE_MEMBER_LIST: url" + getOrgUrl);
        Log.d(TAG, "UPDATE_MEMBER_LIST: req" + json);

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
}
