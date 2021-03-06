package com.octopusbjsindia.presenter;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.listeners.MemberListListener;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.events.EventMemberLestResponse;
import com.octopusbjsindia.models.events.Participant;
import com.octopusbjsindia.request.MemberListRequestCall;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.view.activities.AddMembersListActivity;

import java.util.ArrayList;

public class AddMembersListPresenter implements MemberListListener {

    AddMembersListActivity activity;

    public AddMembersListPresenter(AddMembersListActivity activity) {
        this.activity=activity;
    }

    public void deleteMember(String userId, String eventTaskID) {
        MemberListRequestCall requestCall = new MemberListRequestCall();
        requestCall.setListener(this);
        activity.showProgressBar();
        requestCall.deleteMember(eventTaskID,userId);
    }

    public void taskMemberList() {
        MemberListRequestCall requestCall = new MemberListRequestCall();
        requestCall.setListener(this);

        activity.showProgressBar();
        requestCall.getTaskMemberList();
    }

    public void addMemberToEventTask(String eventTaskID, ArrayList<Participant> list) {
        MemberListRequestCall requestCall = new MemberListRequestCall();
        requestCall.setListener(this);

        activity.showProgressBar();
        requestCall.setMemberToEventTask(eventTaskID,list);
    }

    @Override
    public void onMembersDeleted(String response) {
        activity.hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            CommonResponse orgResponse = new Gson().fromJson(response, CommonResponse.class);
            activity.onMembersDeleted(orgResponse);
        }
    }

    @Override
    public void onTaskMembersFetched(String response) {
        if (activity != null) {
            activity.hideProgressBar();
            if (!TextUtils.isEmpty(response)) {
                EventMemberLestResponse data = PlatformGson.getPlatformGsonInstance().fromJson(response, EventMemberLestResponse.class);
                if (activity != null) {
                    if(data.getStatus()==200){
                        activity.showMemberList(data.getData());
                    } else {
                        onFailureListener(data.getMessage());
                    }

                }
            }
        }
    }

    @Override
    public void onMemberListUpdated(String response) {
        if (activity != null) {
            activity.hideProgressBar();
            if (!TextUtils.isEmpty(response)) {
                EventMemberLestResponse data = PlatformGson.getPlatformGsonInstance().fromJson(response, EventMemberLestResponse.class);
                if (activity != null) {
                    if(data.getStatus()==200){
                        activity.showNextScreen(data.getData());
                    } else {
                        onFailureListener(data.getMessage());
                    }

                }
            }
        }
    }

    @Override
    public void onFailureListener(String message) {
        if (activity != null) {
            activity.hideProgressBar();
            activity.showErrorMessage(message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (activity != null) {
            activity.hideProgressBar();
            if (error != null) {
                activity.showErrorMessage(error.getLocalizedMessage());
            }
        }
    }



}
