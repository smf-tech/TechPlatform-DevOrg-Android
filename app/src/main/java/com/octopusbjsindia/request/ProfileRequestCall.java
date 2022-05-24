package com.octopusbjsindia.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.ProfileRequestCallListener;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.models.profile.UserLocation;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GsonRequestFactory;
import com.octopusbjsindia.utility.PreferenceHelper;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;

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
                    Log.d(TAG, "getOrganizations - Resp: " + res);
                    listener.onOrganizationsFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener orgErrorListener = error -> listener.onErrorListener(error);

        final String getOrgUrl = BuildConfig.BASE_URL + Urls.Profile.GET_ORGANIZATION;

        Log.d(TAG, "ORGANIZATION API: " + getOrgUrl);

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
                    Log.d(TAG, "getOrganizationProjects - Resp: " + res);
                    listener.onOrganizationProjectsFetched(orgId, res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener orgProjectsErrorListener = error -> listener.onErrorListener(error);

        final String getOrgProjectUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_ORGANIZATION_PROJECTS, orgId);

        Log.d(TAG, "ORGANIZATION_PROJECTS API: " + getOrgProjectUrl);

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

    public void getOrganizationRoles(String orgId, String projectId) {
        Response.Listener<JSONObject> orgRolesSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getOrganizationRoles - Resp: " + res);
                    listener.onOrganizationRolesFetched(orgId, res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener orgRolesErrorListener = error -> listener.onErrorListener(error);

        final String getOrgProjectUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_ORGANIZATION_ROLES, orgId, projectId);

        Log.d(TAG, "ORGANIZATION_ROLES API: " + getOrgProjectUrl);

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
                    Log.d(TAG, "getJurisdictionLevelData - Resp: " + res);
                    listener.onJurisdictionFetched(res, levelName);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener jurisdictionErrorListener = error -> listener.onErrorListener(error);

//        final String getStateUrl = BuildConfig.BASE_URL
//                + String.format(Urls.Profile.GET_JURISDICTION_LEVEL_DATA, orgId, "5c4ab05cd503a372d0391467", levelName);
        final String getStateUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_JURISDICTION_LEVEL_DATA, orgId, jurisdictionTypeId, levelName);

        Log.d(TAG, "JURISDICTION API: " + getStateUrl);

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

    public void getUserProfile() {
        Response.Listener<JSONObject> userProfileSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d(TAG, "getUserProfile - Resp: " + res);
                    listener.onProfileFetched(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(e.getMessage());
            }
        };

        Response.ErrorListener userProfileErrorListener = error -> listener.onErrorListener(error);

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getProfileUrl = BuildConfig.BASE_URL + Urls.Profile.GET_PROFILE;

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.GET,
                getProfileUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                userProfileSuccessListener,
                userProfileErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public void submitUserProfile(UserInfo userInfo) {
        Response.Listener<JSONObject> profileSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();

                    Log.d(TAG, "submitUserProfile - Resp: " + res);
                    listener.onProfileUpdated(res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                listener.onFailureListener(Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener profileErrorListener = error -> listener.onErrorListener(error);

        final String submitProfileUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.SUBMIT_PROFILE, userInfo.getUserMobileNumber());

        //Log.d(TAG, "submitUserProfile - url: " + submitProfileUrl);

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

        String req = createBodyParams(userInfo).toString();
        Log.d(TAG, "submitUserProfile - req: " + req);

        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    private JsonObject createBodyParams(UserInfo userInfo) {
        JsonObject body = new JsonObject();
        if (userInfo != null) {
            try {
                body.addProperty(Constants.Login.USER_NAME, userInfo.getUserName());
                body.addProperty(Constants.Login.USER_F_NAME, userInfo.getUserFirstName());
                body.addProperty(Constants.Login.USER_M_NAME, userInfo.getUserMiddleName());
                body.addProperty(Constants.Login.USER_L_NAME, userInfo.getUserLastName());
                body.addProperty(Constants.Login.USER_BIRTH_DATE, userInfo.getUserBirthDate());
                body.addProperty(Constants.Login.USER_EMAIL, userInfo.getUserEmailId());
                body.addProperty(Constants.Login.USER_GENDER, userInfo.getUserGender());
                body.addProperty(Constants.Login.USER_ORG_ID, userInfo.getOrgId());
                body.addProperty(Constants.Login.USER_ASSOCIATE_TYPE, userInfo.getType());
                body.addProperty(Constants.Login.USER_ROLE_ID, userInfo.getRoleIds());
                body.addProperty(Constants.Login.USER_PROFILE_PIC, userInfo.getProfilePic());

                // Add project Ids
                JsonArray projectIdArray = new JsonArray();
                ArrayList<JurisdictionType> userProjects = userInfo.getProjectIds();
                for (JurisdictionType project : userProjects) {
                    projectIdArray.add(project.getId());
                }
                body.add(Constants.Login.USER_PROJECTS, projectIdArray);

                // Add user location
                UserLocation userLocation = userInfo.getUserLocation();
                JsonObject locationObj = new JsonObject();

                JsonArray locationArray = new JsonArray();
                if (userLocation.getCountryId() != null) {
                    for (JurisdictionType countries : userLocation.getCountryId()) {
                        locationArray.add(countries.getId());
                    }
                    locationObj.add(Constants.Location.COUNTRY, locationArray);
                }

                locationArray = new JsonArray();
                if (userLocation.getStateId() != null) {
                    for (JurisdictionType states : userLocation.getStateId()) {
                        locationArray.add(states.getId());
                    }
                    locationObj.add(Constants.Location.STATE, locationArray);
                }

                locationArray = new JsonArray();
                if (userLocation.getDistrictIds() != null) {
                    for (JurisdictionType districts : userLocation.getDistrictIds()) {
                        locationArray.add(districts.getId());
                    }
                    locationObj.add(Constants.Location.DISTRICT, locationArray);
                }

                locationArray = new JsonArray();
                if (userLocation.getCityIds() != null) {
                    for (JurisdictionType cities : userLocation.getCityIds()) {
                        locationArray.add(cities.getId());
                    }
                    locationObj.add(Constants.Location.CITY, locationArray);
                }

                locationArray = new JsonArray();
                if (userLocation.getTalukaIds() != null) {
                    for (JurisdictionType talukas : userLocation.getTalukaIds()) {
                        locationArray.add(talukas.getId());
                    }
                    locationObj.add(Constants.Location.TALUKA, locationArray);
                }

                locationArray = new JsonArray();
                if (userLocation.getVillageIds() != null) {
                    for (JurisdictionType villages : userLocation.getVillageIds()) {
                        locationArray.add(villages.getId());
                    }
                    locationObj.add(Constants.Location.VILLAGE, locationArray);
                }

                locationArray = new JsonArray();
                if (userLocation.getClusterIds() != null) {
                    for (JurisdictionType clusters : userLocation.getClusterIds()) {
                        locationArray.add(clusters.getId());
                    }
                    locationObj.add(Constants.Location.CLUSTER, locationArray);
                }

                locationArray = new JsonArray();
                if (userLocation.getSchoolIds() != null) {
                    for (JurisdictionType schools : userLocation.getSchoolIds()) {
                        locationArray.add(schools.getId());
                    }
                    locationObj.add(Constants.Location.SCHOOL, locationArray);
                }

                locationArray = new JsonArray();
                if (userLocation.getGranpanchayatIds() != null) {
                    for (JurisdictionType panchayat : userLocation.getGranpanchayatIds()) {
                        locationArray.add(panchayat.getId());
                    }
                    locationObj.add(Constants.Location.GRAM_PANCHAYAT, locationArray);
                }

                locationArray = new JsonArray();
                if (userLocation.getLearningCenterIds() != null) {
                    for (JurisdictionType centers : userLocation.getLearningCenterIds()) {
                        locationArray.add(centers.getId());
                    }
                    locationObj.add(Constants.Location.LEARNING_CENTER, locationArray);
                }

                body.add(Constants.Login.USER_LOCATION, locationObj);

                PreferenceHelper preferenceHelper = new PreferenceHelper(Platform.getInstance());
                String token = preferenceHelper.getString(PreferenceHelper.TOKEN);
                body.addProperty(Constants.Login.USER_FIREBASE_ID, token);

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        Log.i(TAG, "BODY: " + body);
        return body;
    }

}
