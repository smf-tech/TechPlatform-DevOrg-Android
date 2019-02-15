package com.platform.request;

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
import com.platform.listeners.ProfileRequestCallListener;
import com.platform.models.user.UserInfo;
import com.platform.models.profile.UserLocation;
import com.platform.utility.Constants;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;

import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileRequestCall {

    @SuppressWarnings("CanBeFinal")
    private Gson gson;
    private ProfileRequestCallListener listener;
    private final String TAG = ProfileRequestCall.class.getName();

    public ProfileRequestCall() {
        gson = new GsonBuilder().serializeNulls().create();
    }

    public void setListener(ProfileRequestCallListener listener) {
        this.listener = listener;
    }

    public void getOrganizations() {
        Response.Listener<JSONObject> orgSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.i(TAG, "API Org Response:" + res);
                    listener.onOrganizationsFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        };

        Response.ErrorListener orgErrorListener = error -> listener.onErrorListener(error);

        final String getOrgUrl = BuildConfig.BASE_URL + Urls.Profile.GET_ORGANIZATION;
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

    public void getOrganizationProjects(String orgId) {
        Response.Listener<JSONObject> orgProjectsSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.i(TAG, "API Org projects Response:" + res);
                    listener.onOrganizationProjectsFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        };

        Response.ErrorListener orgProjectsErrorListener = error -> listener.onErrorListener(error);

        final String getOrgProjectUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_ORGANIZATION_PROJECTS, orgId);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getOrgProjectUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                orgProjectsSuccessListener,
                orgProjectsErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void getOrganizationRoles(String orgId) {
        Response.Listener<JSONObject> orgRolesSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.i(TAG, "API Org roles Response:" + res);
                    listener.onOrganizationRolesFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        };

        Response.ErrorListener orgRolesErrorListener = error -> listener.onErrorListener(error);

        final String getOrgProjectUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_ORGANIZATION_ROLES, orgId);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getOrgProjectUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                orgRolesSuccessListener,
                orgRolesErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void getJurisdictionLevelData(String orgId, String jurisdictionTypeId, String levelName) {
        Response.Listener<JSONObject> jurisdictionSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.i(TAG, "API Jurisdiction Response:" + res);
                    listener.onJurisdictionFetched(res, levelName);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        };

        Response.ErrorListener jurisdictionErrorListener = error -> listener.onErrorListener(error);

        final String getStateUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_JURISDICTION_LEVEL_DATA, orgId, jurisdictionTypeId, levelName);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getStateUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                jurisdictionSuccessListener,
                jurisdictionErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void submitUserProfile(UserInfo userInfo) {
        Response.Listener<JSONObject> profileSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.i(TAG, "API submit profile Response:" + res);
                    listener.onProfileUpdated(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener("");
            }
        };

        Response.ErrorListener profileErrorListener = error -> listener.onErrorListener(error);

        final String submitProfileUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.SUBMIT_PROFILE, userInfo.getUserMobileNumber());

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.PUT,
                submitProfileUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                profileSuccessListener,
                profileErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setBodyParams(createBodyParams(userInfo));
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    private JsonObject createBodyParams(UserInfo userInfo) {
        JsonObject body = new JsonObject();
        if (userInfo != null) {
            try {
                body.addProperty(Constants.Login.USER_F_NAME, userInfo.getUserFirstName());
                body.addProperty(Constants.Login.USER_M_NAME, userInfo.getUserMiddleName());
                body.addProperty(Constants.Login.USER_L_NAME, userInfo.getUserLastName());
                body.addProperty(Constants.Login.USER_BIRTH_DATE, userInfo.getUserBirthDate());
                body.addProperty(Constants.Login.USER_EMAIL, userInfo.getUserEmailId());
                body.addProperty(Constants.Login.USER_GENDER, userInfo.getUserGender());
                body.addProperty(Constants.Login.USER_ORG_ID, userInfo.getOrgId());

//            JsonArray roleIdArray = new JsonArray();
//            ArrayList<String> userRoleIds = userInfo.getRoleIds();
//            for (String roleId : userRoleIds) {
//                roleIdArray.add(roleId);
//            }
                body.addProperty(Constants.Login.USER_ROLE_ID, userInfo.getRoleIds());

                // Add project Ids
                JsonArray projectIdArray = new JsonArray();
                ArrayList<String> userProjectIds = userInfo.getProjectIds();
                for (String projectId : userProjectIds) {
                    projectIdArray.add(projectId);
                }
                body.add(Constants.Login.USER_PROJECTS, projectIdArray);

                // Add user location
                UserLocation userLocation = userInfo.getUserLocation();
                JsonObject locationObj = new JsonObject();

                JsonArray locationArray = new JsonArray();
                if (userLocation.getStateId() != null) {
                    for (String stateId : userLocation.getStateId()) {
                        locationArray.add(stateId);
                    }
                    locationObj.add(Constants.Location.STATE, locationArray);
                }

                locationArray = new JsonArray();
                if (userLocation.getDistrictIds() != null) {
                    for (String districtId : userLocation.getDistrictIds()) {
                        locationArray.add(districtId);
                    }
                    locationObj.add(Constants.Location.DISTRICT, locationArray);
                }

                locationArray = new JsonArray();
                if (userLocation.getTalukaIds() != null) {
                    for (String talukaId : userLocation.getTalukaIds()) {
                        locationArray.add(talukaId);
                    }
                    locationObj.add(Constants.Location.TALUKA, locationArray);
                }

                locationArray = new JsonArray();
                if (userLocation.getVillageIds() != null) {
                    for (String villageId : userLocation.getVillageIds()) {
                        locationArray.add(villageId);
                    }
                    locationObj.add(Constants.Location.VILLAGE, locationArray);
                }

                locationArray = new JsonArray();
                if (userLocation.getClusterIds() != null) {
                    for (String clusterId : userLocation.getClusterIds()) {
                        locationArray.add(clusterId);
                    }
                    locationObj.add(Constants.Location.CLUSTER, locationArray);
                }

                body.add(Constants.Login.USER_LOCATION, locationObj);

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        Log.i(TAG, "BODY: " + body);
        return body;
    }
}
