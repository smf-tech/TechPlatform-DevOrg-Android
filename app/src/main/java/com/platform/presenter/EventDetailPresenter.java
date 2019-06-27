package com.platform.presenter;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.platform.listeners.EventDetailListener;
import com.platform.request.EventDetailRequestCall;
import com.platform.view.activities.EventDetailActivity;

public class EventDetailPresenter implements EventDetailListener {
    EventDetailActivity activity;

    public EventDetailPresenter(EventDetailActivity eventDetailActivity) {
        this.activity=eventDetailActivity;
    }

    public void getAttendanceCode() {
        EventDetailRequestCall requestCall = new EventDetailRequestCall();
        requestCall.setEventDetailListener(this);

        activity.showProgressBar();
        requestCall.getAttendanceCode();
    }

    @Override
    public void onAttendanceCodeFetched(String response) {
        if (activity != null) {
            activity.hideProgressBar();
            if (!TextUtils.isEmpty(response)) {

//                AddFormsResponse data = PlatformGson.getPlatformGsonInstance().fromJson(response, AddFormsResponse.class);
//
//                if (activity != null) {
//                    activity.onFormsListFatched((ArrayList< AddForm > )data.getData());
//                }
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
