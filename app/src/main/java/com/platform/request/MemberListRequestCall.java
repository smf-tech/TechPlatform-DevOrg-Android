package com.platform.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.platform.BuildConfig;
import com.platform.Platform;
import com.platform.R;
import com.platform.listeners.MemberListListener;
import com.platform.presenter.AddMembersListPresenter;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONException;
import org.json.JSONObject;

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
}
