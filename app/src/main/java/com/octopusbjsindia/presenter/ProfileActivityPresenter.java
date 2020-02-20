package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.models.login.Login;
import com.octopusbjsindia.models.profile.MultyProjectData;
import com.octopusbjsindia.models.profile.MultyProjectResponse;
import com.octopusbjsindia.models.user.User;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.request.ProfileRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.GsonRequestFactory;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.ProfileActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.octopusbjsindia.utility.Util.getAppVersion;
import static com.octopusbjsindia.utility.Util.getLoginObjectFromPref;
import static com.octopusbjsindia.utility.Util.getStringFromPref;

public class ProfileActivityPresenter implements APIPresenterListener {

    ProfileActivity activity;

    public ProfileActivityPresenter(ProfileActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onFailureListener(String requestID, String message) {
        activity.hideProgressBar();
        activity.onFailureListener(requestID, message);
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        activity.hideProgressBar();
        activity.onErrorListener(requestID, error);
        ;
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        activity.hideProgressBar();
        if(requestID.equals("multi_profile")){
            MultyProjectResponse multyProjectResponse = new Gson().fromJson(response, MultyProjectResponse.class);
            activity.displayProjects(multyProjectResponse.getData());
        } else if(requestID.equals("UserProfile")){
            User user = new Gson().fromJson(response, User.class);
            if (response != null && user.getUserInfo() != null) {
                Util.saveUserObjectInPref(new Gson().toJson(user.getUserInfo()));
            }
            activity.onSuccessListener(requestID,"");
        }

    }

    public void getMultProfile() {
        activity.showProgressBar();
        final String checkProfileUrl = BuildConfig.BASE_URL + Urls.Profile.GET_MULTIPAL_PROFILE;
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall("multi_profile", checkProfileUrl);
    }

    public void getUserProfile() {
        activity.showProgressBar();
        final String url = BuildConfig.BASE_URL + Urls.Profile.GET_PROFILE;
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        requestCall.getDataApiCall("UserProfile", url);
    }

    public void getUserDetels(MultyProjectData multyProjectData) {
        activity.showProgressBar();
        Response.Listener<JSONObject> userProfileSuccessListener = response -> {
            activity.hideProgressBar();
            try {
                if (response != null) {
                    String res = response.toString();
                    Log.d("getUserProfile - Resp:", res);
                    activity.updateUI(res);
                }
            } catch (Exception e) {
                Log.e("getUserProfile", e.getMessage());
                activity.hideProgressBar();
                activity.onFailureListener("getUserProfile", e.getMessage());
            }
        };

        Response.ErrorListener userProfileErrorListener = error -> {
            activity.hideProgressBar();
            activity.onErrorListener("requestID", error);
        };


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

        gsonRequest.setHeaderParams(requestHeader(multyProjectData));
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    public static Map<String, String> requestHeader(MultyProjectData data) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json, text/plain, */*");
        headers.put("Content-Type", "application/json;charset=UTF-8");
//        headers.put("orgId", Util.getUserObjectFromPref().getOrgId());

        Login loginObj = getLoginObjectFromPref();
        if (loginObj != null && loginObj.getLoginData() != null &&
                loginObj.getLoginData().getAccessToken() != null) {
            headers.put(Constants.Login.AUTHORIZATION,
                    "Bearer " + loginObj.getLoginData().getAccessToken());
            if (data.getOrgId() != null) {
                headers.put("orgId", data.getOrgId());
            }
            if (data.getProjectId() != null) {
                headers.put("projectId", data.getProjectId());
            }
            if (data.getRoleId() != null) {
                headers.put("roleId", data.getRoleId());
            }
            headers.put("versionName", getAppVersion());
            headers.put("deviceId", getStringFromPref(Constants.App.deviceId));
        }

        return headers;
    }

}
