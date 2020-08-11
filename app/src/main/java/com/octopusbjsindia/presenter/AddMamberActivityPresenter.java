package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.login.Login;
import com.octopusbjsindia.models.profile.JurisdictionType;
import com.octopusbjsindia.models.profile.UserLocation;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GsonRequestFactory;
import com.octopusbjsindia.utility.PreferenceHelper;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.activities.AddMemberActivity;
import org.json.JSONObject;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.google.firebase.remoteconfig.FirebaseRemoteConfig.TAG;
import static com.octopusbjsindia.utility.Util.getAppVersion;
import static com.octopusbjsindia.utility.Util.getLoginObjectFromPref;
import static com.octopusbjsindia.utility.Util.getStringFromPref;
import static com.octopusbjsindia.utility.Util.getUserObjectFromPref;

public class AddMamberActivityPresenter implements APIPresenterListener {

    private WeakReference<AddMemberActivity> mContext;

    public AddMamberActivityPresenter(AddMemberActivity activity) {
        mContext = new WeakReference<>(activity);
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        mContext.get().hideProgressBar();
        mContext.get().onFailureListener(requestID,message);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        mContext.get().hideProgressBar();
        mContext.get().onFailureListener(requestID,error.getMessage());
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        mContext.get().hideProgressBar();
        mContext.get().onSuccessListener(requestID,response);
    }

    public void submitProfile(final UserInfo userInfo) {
        mContext.get().showProgressBar();
        requestCall("submitProfile", userInfo);
    }

    public void requestCall(String requestID,UserInfo userInfo) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        Response.Listener<JSONObject> profileSuccessListener = response -> {
            try {
                if (response != null) {
                    String res = response.toString();

                    Log.d(TAG, "submitUserProfile - Resp: " + res);
                    onSuccessListener(requestID,res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                onFailureListener(requestID, Platform.getInstance().getString(R.string.msg_failure));
            }
        };

        Response.ErrorListener profileErrorListener = error -> onErrorListener(requestID, error);

        final String submitProfileUrl = BuildConfig.BASE_URL+Urls.Matrimony.CREATE_SUBORDINATE;

        //Log.d(TAG, "submitUserProfile - url: " + submitProfileUrl);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.POST,
                submitProfileUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                profileSuccessListener,
                profileErrorListener
        );

        String req = createBodyParams(userInfo).toString();
        Log.d(TAG, "submitUserProfile - req: " + req);
        gsonRequest.setHeaderParams(requestHeader(true , userInfo.getRoleIds()));
        gsonRequest.setBodyParams(req);

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
                body.addProperty(Constants.Login.USER_PHONE, userInfo.getUserMobileNumber());
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


    public Map<String, String> requestHeader(boolean isTokenPresent, String roleId) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json, text/plain, */*");
        headers.put("Content-Type", "application/json;charset=UTF-8");
        headers.put("deviceId", getStringFromPref(Constants.App.deviceId));
//        headers.put("orgId", Util.getUserObjectFromPref().getOrgId());

        if (isTokenPresent) {
            Login loginObj = getLoginObjectFromPref();
            if (loginObj != null && loginObj.getLoginData() != null &&
                    loginObj.getLoginData().getAccessToken() != null) {
                headers.put(Constants.Login.AUTHORIZATION,
                        "Bearer " + loginObj.getLoginData().getAccessToken());
                if (getUserObjectFromPref() != null) {
                    if (getUserObjectFromPref().getOrgId() != null) {
                        headers.put("orgId", getUserObjectFromPref().getOrgId());
                    }
                    if (getUserObjectFromPref().getProjectIds() != null) {
                        headers.put("projectId", getUserObjectFromPref().getProjectIds().get(0).getId());
                    }
                    if (getUserObjectFromPref().getRoleIds() != null) {
                        headers.put("roleId", roleId);
                    }
                }
                headers.put("versionName",getAppVersion());
            }
        }

        return headers;
    }

}
