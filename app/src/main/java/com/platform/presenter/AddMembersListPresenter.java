package com.platform.presenter;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.AddMemberRequestCallListener;
import com.platform.listeners.MemberListListener;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.events.CommonResponse;
import com.platform.request.MemberListRequestCall;
import com.platform.view.activities.AddMembersListActivity;

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

    @Override
    public void onMembersDeleted(String response) {
        activity.hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            CommonResponse orgResponse = new Gson().fromJson(response, CommonResponse.class);
            activity.onMembersDeleted(orgResponse);
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
