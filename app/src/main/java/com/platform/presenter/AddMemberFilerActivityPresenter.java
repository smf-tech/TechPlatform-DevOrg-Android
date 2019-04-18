package com.platform.presenter;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.ProfileRequestCallListener;
import com.platform.models.profile.JurisdictionLevelResponse;
import com.platform.models.profile.OrganizationResponse;
import com.platform.models.profile.OrganizationRolesResponse;
import com.platform.request.ProfileRequestCall;
import com.platform.utility.Util;
import com.platform.view.activities.AddMemberFilerActivity;

import java.lang.ref.WeakReference;

public class AddMemberFilerActivityPresenter implements ProfileRequestCallListener {
    private final String TAG = AddMemberFilerActivityPresenter.class.getName();
    private WeakReference<AddMemberFilerActivity> addMemberFilerActivity;


    public AddMemberFilerActivityPresenter(AddMemberFilerActivity addMemberFilerActivity) {
        this.addMemberFilerActivity = new WeakReference<> (addMemberFilerActivity);
    }

    public void getOrganizations() {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);

        addMemberFilerActivity.get().showProgressBar();
        requestCall.getOrganizations();
    }

    public void getOrganizationRoles(String orgId) {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);

//        addMemberFilerActivity.get().showProgressBar();
        requestCall.getOrganizationRoles(orgId);
    }

    public void getJurisdictionLevelData(String orgId, String jurisdictionTypeId, String levelName) {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);

        addMemberFilerActivity.get().showProgressBar();
        requestCall.getJurisdictionLevelData(orgId, jurisdictionTypeId, levelName);
    }

    @Override
    public void onProfileUpdated(String response) {

    }

    @Override
    public void onOrganizationsFetched(String response) {
        addMemberFilerActivity.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            Util.saveUserOrgInPref(response);
            OrganizationResponse orgResponse = new Gson().fromJson(response, OrganizationResponse.class);
            if (orgResponse != null && orgResponse.getData() != null
                    && !orgResponse.getData().isEmpty()
                    && orgResponse.getData().size() > 0) {
                addMemberFilerActivity.get().showOrganizations(orgResponse.getData());
            }
        }
    }

    @Override
    public void onOrganizationProjectsFetched(String orgId, String response) {

    }

    @Override
    public void onOrganizationRolesFetched(String orgId, String response) {
        addMemberFilerActivity.get().hideProgressBar();

        if (!TextUtils.isEmpty(response)) {
            Util.saveUserRoleInPref(orgId, response);
            OrganizationRolesResponse orgRolesResponse
                    = new Gson().fromJson(response, OrganizationRolesResponse.class);

            if (orgRolesResponse != null && orgRolesResponse.getData() != null &&
                    !orgRolesResponse.getData().isEmpty() && orgRolesResponse.getData().size() > 0) {
                addMemberFilerActivity.get().showOrganizationRoles(orgRolesResponse.getData());
            }
        }
    }

    @Override
    public void onJurisdictionFetched(String response, String level) {
        addMemberFilerActivity.get().hideProgressBar();

        if (!TextUtils.isEmpty(response)) {
            JurisdictionLevelResponse jurisdictionLevelResponse
                    = new Gson().fromJson(response, JurisdictionLevelResponse.class);

            if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                    && !jurisdictionLevelResponse.getData().isEmpty()
                    && jurisdictionLevelResponse.getData().size() > 0) {

                addMemberFilerActivity.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(), level);
            }
        }
    }

    @Override
    public void onFailureListener(String message) {
        if (addMemberFilerActivity != null && addMemberFilerActivity.get() != null) {
            addMemberFilerActivity.get().hideProgressBar();
            addMemberFilerActivity.get().showErrorMessage(message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (addMemberFilerActivity != null && addMemberFilerActivity.get() != null) {
            addMemberFilerActivity.get().hideProgressBar();
            if (error != null) {
                addMemberFilerActivity.get().showErrorMessage(error.getLocalizedMessage());
            }
        }
    }
}
