package com.platform.presenter;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.platform.listeners.CreateEventListener;
import com.platform.models.events.EventParams;
import com.platform.models.events.EventsResponse;
import com.platform.models.events.EventsResponseOfMonth;
import com.platform.request.EventRequestCall;
import com.platform.utility.PlatformGson;
import com.platform.view.fragments.EventsTaskLandingFragment;

import java.lang.ref.WeakReference;

public class EventsTaskLandingFragmentPresenter implements CreateEventListener {

    private final WeakReference<EventsTaskLandingFragment> fragmentWeakReference;

    public EventsTaskLandingFragmentPresenter(EventsTaskLandingFragment fragmentWeakReference) {
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
//                if(data.getStatus()==200){
                    fragmentWeakReference.get().displayEventsListOfDay(data);
//                } else {
//                    onFailureListener(data.getMessage());
//                }

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
                if(data.getStatus()==200){
                    fragmentWeakReference.get().displayEventsListOfMonth(data.getData());
                } else {
//                    onFailureListener(data.getMessage());
                }
            }
        }
    }

    @Override
    public void onFormsFetched(String response) {
        // not used
    }

    @Override
    public void onTaskMembersFetched(String response) {
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
