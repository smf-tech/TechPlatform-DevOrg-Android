package com.platform.presenter;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.platform.listeners.CreateEventListener;
import com.platform.models.events.Event;
import com.platform.request.EventRequestCall;
import com.platform.utility.Util;
import com.platform.view.activities.CreateEventActivity;

import java.lang.ref.WeakReference;

public class CreateEventActivityPresenter implements CreateEventListener {

    private WeakReference<CreateEventActivity> createEventActivity;

    public CreateEventActivityPresenter(CreateEventActivity createEventActivity) {
        this.createEventActivity=new WeakReference<> (createEventActivity);
    }

    public void getEventCategory(){
        EventRequestCall requestCall=new EventRequestCall();
        requestCall.setCreateEventListener(this);

        createEventActivity.get().showProgressBar();
        requestCall.getCategory();
    }

    public void submitEvent(Event event) {
        EventRequestCall requestCall=new EventRequestCall();
        requestCall.setCreateEventListener(this);

        createEventActivity.get().showProgressBar();
        requestCall.submitEvent(event);
    }

    @Override
    public void onCategoryFetched(String response) {
        createEventActivity.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            Util.saveUserOrgInPref(response);
//            OrganizationResponse orgResponse = new Gson().fromJson(response, OrganizationResponse.class);
//            if (orgResponse != null && orgResponse.getData() != null
//                    && !orgResponse.getData().isEmpty()
//                    && orgResponse.getData().size() > 0) {
//                createEventActivity.get().showOrganizations(orgResponse.getData());
//            }
        }
    }

    @Override
    public void onEventsFetched(String response) {

    }

    @Override
    public void onEventSubmitted(String response) {
        createEventActivity.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            Util.saveUserOrgInPref(response);
//            OrganizationResponse orgResponse = new Gson().fromJson(response, OrganizationResponse.class);
//            if (orgResponse != null && orgResponse.getData() != null
//                    && !orgResponse.getData().isEmpty()
//                    && orgResponse.getData().size() > 0) {
//                createEventActivity.get().showOrganizations(orgResponse.getData());
//            }
        }
    }

    @Override
    public void onFailureListener(String message) {
        if (createEventActivity != null && createEventActivity.get() != null) {
            createEventActivity.get().hideProgressBar();
            createEventActivity.get().showErrorMessage(message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (createEventActivity != null && createEventActivity.get() != null) {
            createEventActivity.get().hideProgressBar();
            if (error != null) {
                createEventActivity.get().showErrorMessage(error.getLocalizedMessage());
            }
        }
    }



}
