package com.platform.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.ProfileRequestCallListener;
import com.platform.models.UserInfo;
import com.platform.models.profile.OrganizationProjectsResponse;
import com.platform.models.profile.OrganizationResponse;
import com.platform.models.profile.OrganizationRolesResponse;
import com.platform.models.profile.StateResponse;
import com.platform.request.ProfileRequestCall;
import com.platform.utility.Util;
import com.platform.view.activities.ProfileActivity;

import java.lang.ref.WeakReference;

public class ProfileActivityPresenter implements ProfileRequestCallListener {

    @SuppressWarnings("CanBeFinal")
    private final String TAG = ProfileActivityPresenter.class.getName();
    @SuppressWarnings("CanBeFinal")
    private WeakReference<ProfileActivity> profileActivity;

    public ProfileActivityPresenter(ProfileActivity activity) {
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
        requestCall.getOrganizations();
    }

    public void getOrganizationProjects(String orgId) {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);
        requestCall.getOrganizationProjects(orgId);
    }

    public void getOrganizationRoles(String orgId) {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);
        requestCall.getOrganizationRoles(orgId);
    }

    public void getStates() {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);
        requestCall.getStates();
    }

    public void getJurisdictionLevelData(String stateId, int level) {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);
        requestCall.getJurisdictionLevelData(stateId, level);
    }

    @Override
    public void onProfileUpdated(String response) {
        UserInfo userInfo = new Gson().fromJson(response, UserInfo.class);

        // Save response
        Util.saveUserObjectInPref(response);

        profileActivity.get().hideProgressBar();
        profileActivity.get().showNextScreen(userInfo);
    }

    @Override
    public void onOrganizationsFetched(String response) {
        if (!TextUtils.isEmpty(response)) {
            OrganizationResponse organizationResponse = new Gson().fromJson(response, OrganizationResponse.class);
            if (organizationResponse != null && organizationResponse.getData() != null && !organizationResponse.getData().isEmpty() && organizationResponse.getData().size() > 0) {
                profileActivity.get().showOrganizations(organizationResponse.getData());
            }
        }
    }

    @Override
    public void onStatesFetched(String response) {
        if (!TextUtils.isEmpty(response)) {
            StateResponse stateResponse = new Gson().fromJson(response, StateResponse.class);
            if (stateResponse != null && stateResponse.getData() != null && !stateResponse.getData().isEmpty() && stateResponse.getData().size() > 0) {
                profileActivity.get().showStates(stateResponse.getData());
            }
        }
    }

    @Override
    public void onJurisdictionFetched(String response) {

    }

    @Override
    public void onOrganizationProjectsFetched(String response) {
        if (!TextUtils.isEmpty(response)) {
            OrganizationProjectsResponse organizationProjectsResponse = new Gson().fromJson(response, OrganizationProjectsResponse.class);
            if (organizationProjectsResponse != null && organizationProjectsResponse.getData() != null && !organizationProjectsResponse.getData().isEmpty() && organizationProjectsResponse.getData().size() > 0) {
                profileActivity.get().showOrganizationProjects(organizationProjectsResponse.getData());
            }
        }
    }

    @Override
    public void onOrganizationRolesFetched(String response) {
        if (!TextUtils.isEmpty(response)) {
            OrganizationRolesResponse organizationRolesResponse = new Gson().fromJson(response, OrganizationRolesResponse.class);
            if (organizationRolesResponse != null && organizationRolesResponse.getData() != null && !organizationRolesResponse.getData().isEmpty() && organizationRolesResponse.getData().size() > 0) {
                profileActivity.get().showOrganizationRoles(organizationRolesResponse.getData());
            }
        }
    }

    @Override
    public void onFailureListener(String message) {
        Log.i(TAG, "Fail" + message);
        profileActivity.get().hideProgressBar();
        profileActivity.get().showErrorMessage(message);
    }

    @Override
    public void onErrorListener(VolleyError error) {
        Log.i(TAG, "Error" + error);
        profileActivity.get().hideProgressBar();

        if (error != null) {
            profileActivity.get().showErrorMessage(error.getLocalizedMessage());
        }
    }
}
