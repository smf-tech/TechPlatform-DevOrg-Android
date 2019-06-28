package com.platform.presenter;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.platform.listeners.CreateEventListener;
import com.platform.models.events.EventParams;
import com.platform.models.events.EventsResponse;
import com.platform.models.events.EventsResponseOfMonth;
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

    public void getEventsOfMonth(EventParams eventParams) {
        EventRequestCall requestCall = new EventRequestCall();
        requestCall.setCreateEventListener(this);
        fragmentWeakReference.get().showProgressBar();
        requestCall.getEventOfMonth(eventParams);
    }

    public void getEventsOfDay(EventParams eventParams) {
        EventRequestCall requestCall = new EventRequestCall();
        requestCall.setCreateEventListener(this);
        fragmentWeakReference.get().showProgressBar();
        requestCall.getEventOfDay(eventParams);
    }


    @Override
    public void onEventsFetchedOfDay(String response) {
        fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            EventsResponse data = PlatformGson.getPlatformGsonInstance().fromJson(response, EventsResponse.class);

            if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
                fragmentWeakReference.get().hideProgressBar();
                fragmentWeakReference.get().displayEventsListOfDay(data.getData());
            }
        }
    }

    @Override
    public void onEventsFetchedOfMonth(String response) {
        fragmentWeakReference.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {

            EventsResponseOfMonth data = PlatformGson.getPlatformGsonInstance().fromJson(response, EventsResponseOfMonth.class);

            if (fragmentWeakReference != null && fragmentWeakReference.get() != null) {
                fragmentWeakReference.get().hideProgressBar();
                fragmentWeakReference.get().displayEventsListOfMonth(data.getData());
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
