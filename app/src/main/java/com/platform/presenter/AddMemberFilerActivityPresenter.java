package com.platform.presenter;

import com.android.volley.VolleyError;
import com.platform.listeners.ProfileRequestCallListener;
import com.platform.request.ProfileRequestCall;
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

//        addMemberFilerActivity.get().showProgressBar();
        requestCall.getOrganizations();
    }

    @Override
    public void onProfileUpdated(String response) {

    }

    @Override
    public void onOrganizationsFetched(String response) {

    }

    @Override
    public void onJurisdictionFetched(String response, String level) {

    }

    @Override
    public void onOrganizationProjectsFetched(String orgId, String response) {

    }

    @Override
    public void onOrganizationRolesFetched(String orgId, String response) {

    }

    @Override
    public void onFailureListener(String message) {

    }

    @Override
    public void onErrorListener(VolleyError error) {

    }
}
