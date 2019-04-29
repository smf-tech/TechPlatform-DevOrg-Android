package com.platform.presenter;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.platform.listeners.CreateEventListener;
import com.platform.request.EventRequestCall;
import com.platform.utility.Util;
import com.platform.view.fragments.EventsPlannerFragment;

import java.lang.ref.WeakReference;

public class EventsPlannerFragmentPresenter implements CreateEventListener {

    private final String TAG = this.getClass().getName();
    private WeakReference<EventsPlannerFragment> fragmentWeakReference;

    public EventsPlannerFragmentPresenter(EventsPlannerFragment fragmentWeakReference) {
        this.fragmentWeakReference=new WeakReference<>(fragmentWeakReference);
    }

    public void getEvents(String status) {
        EventRequestCall requestCall=new EventRequestCall();
        requestCall.setListener(this);

        fragmentWeakReference.get().showProgressBar();
        requestCall.getEvent(status);
    }

    @Override
    public void onCategoryFetched(String response) {

    }

    @Override
    public void onEventsFetched(String response) {
        fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            Util.saveUserOrgInPref(response);
//            OrganizationResponse orgResponse = new Gson().fromJson(response, OrganizationResponse.class);
//            if (orgResponse != null && orgResponse.getData() != null
//                    && !orgResponse.getData().isEmpty()
//                    && orgResponse.getData().size() > 0) {
//                fragmentWeakReference.get().showOrganizations(orgResponse.getData());
//            }
        }
    }

    @Override
    public void onFailureListener(String message) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            fragmentWeakReference.get().showErrorMessage(message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            if (error != null) {
                fragmentWeakReference.get().showErrorMessage(error.getLocalizedMessage());
            }
        }
    }
}
