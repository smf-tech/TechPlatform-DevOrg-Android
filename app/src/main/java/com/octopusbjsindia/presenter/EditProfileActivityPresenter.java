package com.octopusbjsindia.presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.listeners.ImageRequestCallListener;
import com.octopusbjsindia.listeners.ProfileRequestCallListener;
import com.octopusbjsindia.models.profile.JurisdictionLevelResponse;
import com.octopusbjsindia.models.profile.OrganizationProjectsResponse;
import com.octopusbjsindia.models.profile.OrganizationResponse;
import com.octopusbjsindia.models.profile.OrganizationRolesResponse;
import com.octopusbjsindia.models.user.User;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.request.ImageRequestCall;
import com.octopusbjsindia.request.ProfileRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.EditProfileActivity;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;

@SuppressWarnings("CanBeFinal")
public class EditProfileActivityPresenter implements ProfileRequestCallListener,
        ImageRequestCallListener, APIPresenterListener {

    private final String TAG = EditProfileActivityPresenter.class.getName();
    private WeakReference<EditProfileActivity> profileActivity;
//    public static final String GET_COUNTRY = "getCountry";
//    public static final String GET_STATE = "getState";
//    public static final String GET_DISTRICT = "getDistrict";
//    public static final String GET_CITY = "getCity";
//    public static final String GET_TALUKAS = "getTalukas";
//    public static final String GET_VILLAGE = "getVillage";
//    public static final String GET_CLUSTER = "getCluster";
    private static final String KEY_SELECTED_ID = "selected_location_id";
    private static final String KEY_JURIDICTION_TYPE_ID = "jurisdictionTypeId";
    private static final String KEY_LEVEL = "jurisdictionLevel";

    public EditProfileActivityPresenter(EditProfileActivity activity) {
        profileActivity = new WeakReference<>(activity);
    }

    public void submitProfile(final UserInfo userInfo) {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);

        profileActivity.get().showProgressBar();
        requestCall.submitUserProfile(userInfo);
    }

    public void getOrganizations() {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);

        profileActivity.get().showProgressBar();
        requestCall.getOrganizations();
    }

    public void getOrganizationProjects(String orgId) {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);

        profileActivity.get().showProgressBar();
        requestCall.getOrganizationProjects(orgId);
    }

    public void getOrganizationRoles(String orgId, String projectId) {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);

        profileActivity.get().showProgressBar();
        requestCall.getOrganizationRoles(orgId, projectId);
    }

//    public void getJurisdictionLevelData(String orgId, String jurisdictionTypeId, String levelName) {
//        ProfileRequestCall requestCall = new ProfileRequestCall();
//        requestCall.setListener(this);
//
//        profileActivity.get().showProgressBar();
//        requestCall.getJurisdictionLevelData(orgId, jurisdictionTypeId, levelName);
//    }

    public void getLocationData(String selectedLocationId, String jurisdictionTypeId, String levelName) {
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_SELECTED_ID, selectedLocationId);
        map.put(KEY_JURIDICTION_TYPE_ID, jurisdictionTypeId);
        map.put(KEY_LEVEL, levelName);

        profileActivity.get().showProgressBar();
        final String getLocationUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_LOCATION_DATA);
        Log.d(TAG, "getLocationUrl: url" + getLocationUrl);
        profileActivity.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.COUNTRY_LEVEL)) {
            requestCall.postDataApiCall(Constants.JurisdictionLevelName.COUNTRY_LEVEL, new JSONObject(map).toString(), getLocationUrl);
        } else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.STATE_LEVEL)) {
            requestCall.postDataApiCall(Constants.JurisdictionLevelName.STATE_LEVEL, new JSONObject(map).toString(), getLocationUrl);
        } else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)){
            requestCall.postDataApiCall(Constants.JurisdictionLevelName.DISTRICT_LEVEL, new JSONObject(map).toString(), getLocationUrl);
        } else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.CITY_LEVEL)){
            requestCall.postDataApiCall(Constants.JurisdictionLevelName.CITY_LEVEL, new JSONObject(map).toString(), getLocationUrl);
        } else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)){
            requestCall.postDataApiCall(Constants.JurisdictionLevelName.TALUKA_LEVEL, new JSONObject(map).toString(), getLocationUrl);
        } else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.VILLAGE_LEVEL)){
            requestCall.postDataApiCall(Constants.JurisdictionLevelName.VILLAGE_LEVEL, new JSONObject(map).toString(), getLocationUrl);
        } else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.CLUSTER_LEVEL)){
            requestCall.postDataApiCall(Constants.JurisdictionLevelName.CLUSTER_LEVEL, new JSONObject(map).toString(), getLocationUrl);
        }
    }

    public void getProfileLocationData(String selectedLocationId, String jurisdictionTypeId, String levelName, String orgId,
                                       String projectId, String roleId) {
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_SELECTED_ID, selectedLocationId);
        map.put(KEY_JURIDICTION_TYPE_ID, jurisdictionTypeId);
        map.put(KEY_LEVEL, levelName);

        profileActivity.get().showProgressBar();
        final String getLocationUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_LOCATION_DATA);
        Log.d(TAG, "getLocationUrl: url" + getLocationUrl);
        profileActivity.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.COUNTRY_LEVEL)) {
            requestCall.postDataCustomizeHeaderApiCall(Constants.JurisdictionLevelName.COUNTRY_LEVEL,
                    new JSONObject(map).toString(), getLocationUrl, orgId, projectId, roleId);
        } else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.STATE_LEVEL)) {
            requestCall.postDataCustomizeHeaderApiCall(Constants.JurisdictionLevelName.STATE_LEVEL,
                    new JSONObject(map).toString(), getLocationUrl, orgId, projectId, roleId);
        } else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)){
            requestCall.postDataCustomizeHeaderApiCall(Constants.JurisdictionLevelName.DISTRICT_LEVEL,
                    new JSONObject(map).toString(), getLocationUrl, orgId, projectId, roleId);
        } else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.CITY_LEVEL)){
            requestCall.postDataCustomizeHeaderApiCall(Constants.JurisdictionLevelName.CITY_LEVEL,
                    new JSONObject(map).toString(), getLocationUrl, orgId, projectId, roleId);
        } else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)){
            requestCall.postDataCustomizeHeaderApiCall(Constants.JurisdictionLevelName.TALUKA_LEVEL,
                    new JSONObject(map).toString(), getLocationUrl, orgId, projectId, roleId);
        } else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.VILLAGE_LEVEL)){
            requestCall.postDataCustomizeHeaderApiCall(Constants.JurisdictionLevelName.VILLAGE_LEVEL,
                    new JSONObject(map).toString(), getLocationUrl, orgId, projectId, roleId);
        } else if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.CLUSTER_LEVEL)){
            requestCall.postDataCustomizeHeaderApiCall(Constants.JurisdictionLevelName.CLUSTER_LEVEL,
                    new JSONObject(map).toString(), getLocationUrl, orgId, projectId, roleId);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void uploadProfileImage(File file, String type) {
        ImageRequestCall requestCall = new ImageRequestCall();
        requestCall.setListener(this);

        profileActivity.get().showProgressBar();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... voids) {
                requestCall.uploadImageUsingHttpURLEncoded(file, type, null,null,null);
                return null;
            }
        }.execute();
    }

    @Override
    public void onProfileUpdated(String response) {
        User user = new Gson().fromJson(response, User.class);

        // Save response
        if (response != null && user.getUserInfo() != null) {
            Util.saveUserObjectInPref(new Gson().toJson(user.getUserInfo()));
        }

        profileActivity.get().hideProgressBar();
        profileActivity.get().showNextScreen(user);
    }

    @Override
    public void onOrganizationsFetched(String response) {
        profileActivity.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            Util.saveUserOrgInPref(response);
            OrganizationResponse orgResponse = new Gson().fromJson(response, OrganizationResponse.class);
            if (orgResponse != null && orgResponse.getData() != null
                    && !orgResponse.getData().isEmpty()
                    && orgResponse.getData().size() > 0) {
                profileActivity.get().showOrganizations(orgResponse.getData());
            }
        }
    }

    @Override
    public void onJurisdictionFetched(String response, String level) {
        profileActivity.get().hideProgressBar();

        if (!TextUtils.isEmpty(response)) {
            JurisdictionLevelResponse jurisdictionLevelResponse
                    = new Gson().fromJson(response, JurisdictionLevelResponse.class);

            if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                    && !jurisdictionLevelResponse.getData().isEmpty()
                    && jurisdictionLevelResponse.getData().size() > 0) {

                profileActivity.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(), level);
            }
        }
    }

    @Override
    public void onOrganizationProjectsFetched(String orgId, String response) {
        profileActivity.get().hideProgressBar();

        if (!TextUtils.isEmpty(response)) {
            Util.saveUserProjectsInPref(orgId, response);
            OrganizationProjectsResponse projectsResponse
                    = new Gson().fromJson(response, OrganizationProjectsResponse.class);

            if (projectsResponse != null && projectsResponse.getData() != null
                    && !projectsResponse.getData().isEmpty() && projectsResponse.getData().size() > 0) {
                profileActivity.get().showOrganizationProjects(projectsResponse.getData());
            }
        }
    }

    @Override
    public void onOrganizationRolesFetched(String orgId, String response) {
        profileActivity.get().hideProgressBar();

        if (!TextUtils.isEmpty(response)) {
            Util.saveUserRoleInPref(orgId, response);
            OrganizationRolesResponse orgRolesResponse
                    = new Gson().fromJson(response, OrganizationRolesResponse.class);

            if (orgRolesResponse != null && orgRolesResponse.getData() != null &&
                    !orgRolesResponse.getData().isEmpty() && orgRolesResponse.getData().size() > 0) {
                profileActivity.get().showOrganizationRoles(orgRolesResponse.getData());
            }
        }
    }

    @Override
    public void onFailureListener(String message) {
        if (profileActivity != null && profileActivity.get() != null) {
            profileActivity.get().hideProgressBar();
            profileActivity.get().showErrorMessage(message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (profileActivity != null && profileActivity.get() != null) {
            profileActivity.get().hideProgressBar();
            if (error != null && error.networkResponse != null) {
                if (error.networkResponse.statusCode == Constants.TIMEOUT_ERROR_CODE) {
                    if (error.networkResponse.data != null) {
                        String json = new String(error.networkResponse.data);
                        json = Util.trimMessage(json);
                        if (json != null) {
                            Util.showToast(json, profileActivity);
                        } else {
                            Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                                    profileActivity);
                        }
                    } else {
                        Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                                profileActivity);
                    }
                } else {
                    profileActivity.get().showErrorMessage(error.getLocalizedMessage());
                    Log.e("onErrorListener",
                            "Unexpected response code " + error.networkResponse.statusCode);
                }
            }
        }
    }

    @Override
    public void onImageUploadedListener(final String response, final String formName) {

        Log.e(TAG, "onImageUploadedListener:\n" + response);
//        profileActivity.get().runOnUiThread(() -> Util.showToast(
//                profileActivity.get().getResources().getString(R.string.image_upload_success), profileActivity.get()));
        profileActivity.get().hideProgressBar();

        try {
            if (new JSONObject(response).has("data")) {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                String url = (String) data.get("url");
                Log.e(TAG, "onPostExecute: Url: " + url);

                profileActivity.get().onImageUploaded(url);
            } else {
                Log.e(TAG, "onPostExecute: Invalid response");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        if (profileActivity != null && profileActivity.get() != null) {
            profileActivity.get().hideProgressBar();
            profileActivity.get().onFailureListener(requestID,message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (profileActivity != null && profileActivity.get() != null) {
            profileActivity.get().hideProgressBar();
            if (error != null) {
                profileActivity.get().onErrorListener(requestID,error);
            }
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (profileActivity == null) {
            return;
        }
        profileActivity.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            JurisdictionLevelResponse jurisdictionLevelResponse
                    = new Gson().fromJson(response, JurisdictionLevelResponse.class);

            if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                    && !jurisdictionLevelResponse.getData().isEmpty()
                    && jurisdictionLevelResponse.getData().size() > 0) {

                profileActivity.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(), requestID);
            }
        }
    }
}
