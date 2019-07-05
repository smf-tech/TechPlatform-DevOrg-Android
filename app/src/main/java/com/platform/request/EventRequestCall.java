package com.platform.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.platform.BuildConfig;
import com.platform.Platform;
import com.platform.R;
import com.platform.listeners.AddMemberRequestCallListener;
import com.platform.listeners.CreateEventListener;
import com.platform.models.events.Event;
import com.platform.models.events.ParametersFilterMember;
import com.platform.models.profile.JurisdictionType;
import com.platform.utility.Constants;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.PreferenceHelper;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

        final String getOrgUrl = BuildConfig.BASE_URL + String.format(Urls.Events.GET_EVENTS, status);
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

        final String eventSubmitUrl = BuildConfig.BASE_URL + Urls.Events.SUBMIT_EVENT;
        Gson gson = new GsonBuilder().create();
        String parmjson = gson.toJson(event);
        Log.d(TAG, "SubmitEvents: url" + eventSubmitUrl);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.POST,
                eventSubmitUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                orgSuccessListener,
                orgErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(createBodyParams(parmjson));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void getMemberList(ParametersFilterMember parametersFilter) {
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

//        final String getOrgUrl = BuildConfig.BASE_URL + Urls.Events.GET_MEMBERS_LIST ;
        final String getOrgUrl = BuildConfig.BASE_URL + Urls.Events.GET_MEMBERS_LIST;
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put("org_id", parametersFilter.getOrganizationIds());
        mParams.put("role", parametersFilter.getRoleIds());
        mParams.put("state", parametersFilter.getState());
        mParams.put("district", parametersFilter.getDistrict());
        mParams.put("taluka", parametersFilter.getTaluka());
        mParams.put("cluster", parametersFilter.getCluster());
        mParams.put("village", parametersFilter.getVillage());
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(mParams);
        Log.d(TAG, "GET_MEMBERS_LIST: " + getOrgUrl);

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

    public void getFormData(ArrayList<JurisdictionType> projectIds) {
        Response.Listener<JSONObject> orgSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "GET_FORMS_LIST - Resp: " + res);
                    createEventListener.onFormsFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                createEventListener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener orgErrorListener = error -> createEventListener.onErrorListener(error);

        final String eventSubmitUrl = BuildConfig.BASE_URL + Urls.Events.GET_FORMS_LIST;
        Gson gson = new GsonBuilder().create();
        Map<String,ArrayList<JurisdictionType>> params = new HashMap<>();
        params.put("projectIds",projectIds);
        String json = gson.toJson(params);

        Log.d(TAG, "GET_FORMS_LIST - url:" + eventSubmitUrl);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.POST,
                eventSubmitUrl,
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
