package com.octopusbjsindia.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.listeners.APIPresenterListener;
import com.octopusbjsindia.listeners.AddMemberRequestCallListener;
import com.octopusbjsindia.models.events.MemberListResponse;
import com.octopusbjsindia.models.events.ParametersFilterMember;
import com.octopusbjsindia.models.events.Participant;
import com.octopusbjsindia.models.profile.JurisdictionLevelResponse;
import com.octopusbjsindia.models.profile.OrganizationResponse;
import com.octopusbjsindia.models.profile.OrganizationRolesResponse;
import com.octopusbjsindia.request.APIRequestCall;
import com.octopusbjsindia.request.AddMemeberFilterRequestCall;
import com.octopusbjsindia.request.EventRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Urls;
import com.octopusbjsindia.view.activities.AddMembersFilterActivity;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class AddMemberFilterActivityPresenter implements AddMemberRequestCallListener, APIPresenterListener {

    private final WeakReference<AddMembersFilterActivity> addMemberFilterActivity;
    private static final String KEY_SELECTED_ID = "selected_location_id";
    private static final String KEY_JURIDICTION_TYPE_ID = "jurisdictionTypeId";
    private static final String KEY_LEVEL = "jurisdictionLevel";
    private final String TAG = AddMemberFilterActivityPresenter.class.getName();

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

//    public void getJurisdictionLevelData(String orgId, String jurisdictionTypeId, String levelName) {
//        ProfileRequestCall requestCall = new ProfileRequestCall();
//        requestCall.setListener(this);
//        addMemberFilterActivity.get().showProgressBar();
//        requestCall.getJurisdictionLevelData(orgId, jurisdictionTypeId, levelName);
//    }

/*
    public void getLocationData(String selectedLocationId, String jurisdictionTypeId, String levelName) {
        HashMap<String,String> map=new HashMap<>();
        map.put(KEY_SELECTED_ID, selectedLocationId);
        map.put(KEY_JURIDICTION_TYPE_ID, jurisdictionTypeId);
        map.put(KEY_LEVEL, levelName);

        addMemberFilterActivity.get().showProgressBar();
        final String getLocationUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_LOCATION_DATA);
        Log.d(TAG, "getLocationUrl: url" + getLocationUrl);
        addMemberFilterActivity.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        if(levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.STATE_LEVEL)) {
            requestCall.postDataApiCall(Constants.JurisdictionLevelName.STATE_LEVEL, new JSONObject(map).toString(), getLocationUrl);
        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)) {
            requestCall.postDataApiCall(Constants.JurisdictionLevelName.DISTRICT_LEVEL, new JSONObject(map).toString(), getLocationUrl);
        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)) {
            requestCall.postDataApiCall(Constants.JurisdictionLevelName.TALUKA_LEVEL, new JSONObject(map).toString(), getLocationUrl);
        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.VILLAGE_LEVEL)) {
            requestCall.postDataApiCall(Constants.JurisdictionLevelName.VILLAGE_LEVEL, new JSONObject(map).toString(), getLocationUrl);
        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.CLUSTER_LEVEL)) {
            requestCall.postDataApiCall(Constants.JurisdictionLevelName.CLUSTER_LEVEL, new JSONObject(map).toString(), getLocationUrl);
        }
    }
*/

    public void getLocationDataV3(String stateId, String districtId, String talukaId, String jurisdictionTypeId, String levelName) {
        HashMap<String, String> map = new HashMap<>();
        map.put("state_id", stateId);
        map.put("district_id", districtId);
        map.put("taluka_id", talukaId);
        map.put("cluster_id", "");
        map.put("village_id", "");
        map.put(KEY_JURIDICTION_TYPE_ID, jurisdictionTypeId);
        map.put(KEY_LEVEL, levelName);

        addMemberFilterActivity.get().showProgressBar();
        final String getLocationUrl = BuildConfig.BASE_URL
                + String.format(Urls.Profile.GET_LOCATION_DATA3);
        Log.d(TAG, "getLocationUrl: url" + getLocationUrl);
        addMemberFilterActivity.get().showProgressBar();
        APIRequestCall requestCall = new APIRequestCall();
        requestCall.setApiPresenterListener(this);
        if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.STATE_LEVEL)) {
            requestCall.postDataApiCall(Constants.JurisdictionLevelName.STATE_LEVEL, new JSONObject(map).toString(), getLocationUrl);
        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.DISTRICT_LEVEL)) {
            requestCall.postDataApiCall(Constants.JurisdictionLevelName.DISTRICT_LEVEL, new JSONObject(map).toString(), getLocationUrl);
        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.TALUKA_LEVEL)) {
            requestCall.postDataApiCall(Constants.JurisdictionLevelName.TALUKA_LEVEL, new JSONObject(map).toString(), getLocationUrl);
        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.VILLAGE_LEVEL)) {
            requestCall.postDataApiCall(Constants.JurisdictionLevelName.VILLAGE_LEVEL, new JSONObject(map).toString(), getLocationUrl);
        } else if (levelName.equalsIgnoreCase(Constants.JurisdictionLevelName.CLUSTER_LEVEL)) {
            requestCall.postDataApiCall(Constants.JurisdictionLevelName.CLUSTER_LEVEL, new JSONObject(map).toString(), getLocationUrl);
        }
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
    public void onProfileFetched(String response) {

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

    @Override
    public void onFailureListener(String requestID, String message) {
        if (addMemberFilterActivity != null && addMemberFilterActivity.get() != null) {
            addMemberFilterActivity.get().hideProgressBar();
            addMemberFilterActivity.get().onFailureListener(requestID,message);
        }
    }

    @Override
    public void onErrorListener(String requestID, VolleyError error) {
        if (addMemberFilterActivity != null && addMemberFilterActivity.get() != null) {
            addMemberFilterActivity.get().hideProgressBar();
            if (error != null) {
                addMemberFilterActivity.get().onErrorListener(requestID,error);
            }
        }
    }

    @Override
    public void onSuccessListener(String requestID, String response) {
        if (addMemberFilterActivity == null) {
            return;
        }
        addMemberFilterActivity.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            JurisdictionLevelResponse jurisdictionLevelResponse
                    = new Gson().fromJson(response, JurisdictionLevelResponse.class);

            if (jurisdictionLevelResponse != null && jurisdictionLevelResponse.getData() != null
                    && !jurisdictionLevelResponse.getData().isEmpty()
                    && jurisdictionLevelResponse.getData().size() > 0) {

                addMemberFilterActivity.get().showJurisdictionLevel(jurisdictionLevelResponse.getData(), requestID);
            }
        }
    }
}
