package com.octopusbjsindia.presenter;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.listeners.AddMemberRequestCallListener;
import com.octopusbjsindia.models.events.MemberListResponse;
import com.octopusbjsindia.models.events.ParametersFilterMember;
import com.octopusbjsindia.models.events.Participant;
import com.octopusbjsindia.models.profile.JurisdictionLevelResponse;
import com.octopusbjsindia.models.profile.OrganizationResponse;
import com.octopusbjsindia.models.profile.OrganizationRolesResponse;
import com.octopusbjsindia.request.AddMemeberFilterRequestCall;
import com.octopusbjsindia.request.EventRequestCall;
import com.octopusbjsindia.request.ProfileRequestCall;
import com.octopusbjsindia.view.activities.AddMembersFilterActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AddMemberFilterActivityPresenter implements AddMemberRequestCallListener {

    private final WeakReference<AddMembersFilterActivity> addMemberFilterActivity;

    public AddMemberFilterActivityPresenter(AddMembersFilterActivity addMembersFilterActivity) {
        this.addMemberFilterActivity = new WeakReference<>(addMembersFilterActivity);
    }

//    public void getOrganizations() {
//        ProfileRequestCall requestCall = new ProfileRequestCall();
//        requestCall.setListener(this);
//        addMemberFilterActivity.get().showProgressBar();
//        requestCall.getOrganizations();
//    }

    public void getOrganizationRoles(String orgId, String projectId) {
        AddMemeberFilterRequestCall requestCall = new AddMemeberFilterRequestCall();
        requestCall.setListener(this);
        addMemberFilterActivity.get().showProgressBar();
        requestCall.getOrganizationRoles(orgId, projectId);
    }

    public void getJurisdictionLevelData(String orgId, String jurisdictionTypeId, String levelName) {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);
        addMemberFilterActivity.get().showProgressBar();
        requestCall.getJurisdictionLevelData(orgId, jurisdictionTypeId, levelName);
    }

    public void getFilterMemberList(ParametersFilterMember parametersFilter) {
        EventRequestCall requestCall = new EventRequestCall();
        requestCall.setAddMemberRequestCallListener(this);
        addMemberFilterActivity.get().showProgressBar();
        requestCall.getMemberList(parametersFilter);
    }

    @Override
    public void onProfileUpdated(String response) {

    }

    @Override
    public void onOrganizationsFetched(String response) {
        addMemberFilterActivity.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            OrganizationResponse orgResponse = new Gson().fromJson(response, OrganizationResponse.class);
            if (orgResponse != null && orgResponse.getData() != null
                    && !orgResponse.getData().isEmpty()
                    && orgResponse.getData().size() > 0) {
                if(orgResponse.getStatus()==200){
                    addMemberFilterActivity.get().showOrganizations(orgResponse.getData());
                } else {
                    onFailureListener(orgResponse.getMessage());
                }

            }
        }
    }

    @Override
    public void onOrganizationProjectsFetched(String orgId, String response) {

    }

    @Override
    public void onOrganizationRolesFetched(String orgId, String response) {
        addMemberFilterActivity.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            OrganizationRolesResponse orgRolesResponse
                    = new Gson().fromJson(response, OrganizationRolesResponse.class);
            if (orgRolesResponse != null && orgRolesResponse.getData() != null &&
                    !orgRolesResponse.getData().isEmpty() && orgRolesResponse.getData().size() > 0) {
                addMemberFilterActivity.get().showOrganizationRoles(orgRolesResponse.getData());
            }
        }
    }

    @Override
    public void onJurisdictionFetched(String response, String level) {
        addMemberFilterActivity.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            JurisdictionLevelResponse jurisdictionLevelResponse
                    = new Gson().fromJson(response, JurisdictionLevelResponse.class);

            if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                    && !jurisdictionLevelResponse.getData().isEmpty()
                    && jurisdictionLevelResponse.getData().size() > 0) {

                addMemberFilterActivity.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(), level);
            }
        }
    }

    @Override
    public void onMembersFetched(String response) {
        addMemberFilterActivity.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            MemberListResponse memberListResponse = new Gson().fromJson(response, MemberListResponse.class);
            if (memberListResponse != null) {
                if(memberListResponse.getStatus()==200){
                    addMemberFilterActivity.get().showMember((ArrayList<Participant>) memberListResponse.getData());
                } else {
                    onFailureListener(memberListResponse.getMessage());
                }
            }
        }
    }

    @Override
    public void onFailureListener(String message) {
        if (addMemberFilterActivity.get() != null) {
            addMemberFilterActivity.get().hideProgressBar();
            addMemberFilterActivity.get().showErrorMessage(message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (addMemberFilterActivity.get() != null) {
            addMemberFilterActivity.get().hideProgressBar();
            if (error != null) {
                addMemberFilterActivity.get().showErrorMessage(error.getLocalizedMessage());
            }
        }
    }
}
