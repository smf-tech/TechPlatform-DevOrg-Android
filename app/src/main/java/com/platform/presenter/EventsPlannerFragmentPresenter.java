package com.platform.presenter;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.platform.listeners.CreateEventListener;
import com.platform.models.events.EventsResponse;
import com.platform.request.EventRequestCall;
import com.platform.utility.PlatformGson;
import com.platform.utility.Util;
import com.platform.view.fragments.EventsPlannerFragment;
import com.platform.view.fragments.TasksPlannerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class EventsPlannerFragmentPresenter implements CreateEventListener {

    private final WeakReference<EventsPlannerFragment> fragmentWeakReference;

    public EventsPlannerFragmentPresenter(EventsPlannerFragment fragmentWeakReference) {
        this.fragmentWeakReference = new WeakReference<>(fragmentWeakReference);

    }

    public void getEvents(String status) {
        EventRequestCall requestCall = new EventRequestCall();
        requestCall.setCreateEventListener(this);
        fragmentWeakReference.get().showProgressBar();
        requestCall.getEvent(status);
    }


    @Override
    public void onEventsFetched(String response) {
        fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {

            EventsResponse data = PlatformGson.getPlatformGsonInstance().fromJson(response, EventsResponse.class);

            if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
                fragmentWeakReference.get().hideProgressBar();
                fragmentWeakReference.get().showNextScreen(data);
            }
        }
    }

    @Override
    public void onFormsFetched(String response) {
        // not used
    }

    @Override
    public void onEventSubmitted(String response) {

    }

    @Override
    public void onFailureListener(String message) {
        if (fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            fragmentWeakReference.get().showErrorMessage(message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (fragmentWeakReference.get() != null) {
            fragmentWeakReference.get().hideProgressBar();
            if (error != null) {
                fragmentWeakReference.get().showErrorMessage(error.getLocalizedMessage());
            }
        }
    }
}
