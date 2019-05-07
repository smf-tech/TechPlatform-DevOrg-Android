package com.platform.request;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
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
import com.platform.utility.Constants;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.PreferenceHelper;
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

        final String getOrgUrl = BuildConfig.BASE_URL + String.format(Urls.Events.GET_EVENTS, status);
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
        final String getOrgUrl = BuildConfig.BASE_URL + Urls.Events.GET_MEMBERS_LIST + "?" +
                (!TextUtils.isEmpty(parametersFilter.getOrganizationIds()) ? "organization=" + parametersFilter.getOrganizationIds() + "&" : "") +
                (!TextUtils.isEmpty(parametersFilter.getRoleIds()) ? "role=" + parametersFilter.getRoleIds() + "&" : "") +
                (!TextUtils.isEmpty(parametersFilter.getState()) ? "location.state=" + parametersFilter.getState() + "&" : "") +
                (!TextUtils.isEmpty(parametersFilter.getDistrict()) ? "location.district=" + parametersFilter.getDistrict() + "&" : "") +
                (!TextUtils.isEmpty(parametersFilter.getTaluka()) ? "location.taluka=" + parametersFilter.getTaluka() + "&" : "") +
                (!TextUtils.isEmpty(parametersFilter.getCluster()) ? "location.cluster=" + parametersFilter.getCluster() + "&" : "") +
                (!TextUtils.isEmpty(parametersFilter.getVillage()) ? "location.village=" + parametersFilter.getVillage() + "&" : "") +
//                "&page=1" +
                "limit=50";

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

        Log.d(TAG, "SubmitEvents: " + eventSubmitUrl);

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
    //    gsonRequest.setBodyParams(createBodyParams(event));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

//    private JsonObject createBodyParams(Event event) {
//        JsonObject body = new JsonObject();
//        if (event != null) {
//            try {
//                body.addProperty(Constants.Login.USER_NAME, userInfo.getUserName());
//                body.addProperty(Constants.Login.USER_F_NAME, userInfo.getUserFirstName());
//                body.addProperty(Constants.Login.USER_M_NAME, userInfo.getUserMiddleName());
//                body.addProperty(Constants.Login.USER_L_NAME, userInfo.getUserLastName());
//                body.addProperty(Constants.Login.USER_BIRTH_DATE, userInfo.getUserBirthDate());
//                body.addProperty(Constants.Login.USER_EMAIL, userInfo.getUserEmailId());
//                body.addProperty(Constants.Login.USER_GENDER, userInfo.getUserGender());
//                body.addProperty(Constants.Login.USER_ORG_ID, userInfo.getOrgId());
//                body.addProperty(Constants.Login.USER_ASSOCIATE_TYPE, userInfo.getType());
//                body.addProperty(Constants.Login.USER_ROLE_ID, userInfo.getRoleIds());
//                body.addProperty(Constants.Login.USER_PROFILE_PIC, userInfo.getProfilePic());
//
//                // Add project Ids
//                JsonArray projectIdArray = new JsonArray();
//                ArrayList<JurisdictionType> userProjects = userInfo.getProjectIds();
//                for (JurisdictionType project : userProjects) {
//                    projectIdArray.add(project.getId());
//                }
//                body.add(Constants.Login.USER_PROJECTS, projectIdArray);
//
//                // Add user location
//                UserLocation userLocation = userInfo.getUserLocation();
//                JsonObject locationObj = new JsonObject();
//
//                JsonArray locationArray = new JsonArray();
//                if (userLocation.getStateId() != null) {
//                    for (JurisdictionType states : userLocation.getStateId()) {
//                        locationArray.add(states.getId());
//                    }
//                    locationObj.add(Constants.Location.STATE, locationArray);
//                }
//
//                locationArray = new JsonArray();
//                if (userLocation.getDistrictIds() != null) {
//                    for (JurisdictionType districts : userLocation.getDistrictIds()) {
//                        locationArray.add(districts.getId());
//                    }
//                    locationObj.add(Constants.Location.DISTRICT, locationArray);
//                }
//
//                locationArray = new JsonArray();
//                if (userLocation.getTalukaIds() != null) {
//                    for (JurisdictionType talukas : userLocation.getTalukaIds()) {
//                        locationArray.add(talukas.getId());
//                    }
//                    locationObj.add(Constants.Location.TALUKA, locationArray);
//                }
//
//                locationArray = new JsonArray();
//                if (userLocation.getVillageIds() != null) {
//                    for (JurisdictionType villages : userLocation.getVillageIds()) {
//                        locationArray.add(villages.getId());
//                    }
//                    locationObj.add(Constants.Location.VILLAGE, locationArray);
//                }
//
//                locationArray = new JsonArray();
//                if (userLocation.getClusterIds() != null) {
//                    for (JurisdictionType clusters : userLocation.getClusterIds()) {
//                        locationArray.add(clusters.getId());
//                    }
//                    locationObj.add(Constants.Location.CLUSTER, locationArray);
//                }
//
//                body.add(Constants.Login.USER_LOCATION, locationObj);
//
//                PreferenceHelper preferenceHelper = new PreferenceHelper(Platform.getInstance());
//                String token = preferenceHelper.getString(PreferenceHelper.TOKEN);
//                body.addProperty(Constants.Login.USER_FIREBASE_ID, token);
//
//            } catch (Exception e) {
//                Log.e(TAG, e.getMessage());
//            }
//        }
//
//        Log.i(TAG, "BODY: " + body);
//        return body;
//    }
}
