package com.octopusbjsindia.presenter;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.octopusbjsindia.listeners.EventDetailListener;
import com.octopusbjsindia.models.events.CommonResponse;
import com.octopusbjsindia.models.events.EventMemberLestResponse;
import com.octopusbjsindia.models.events.GetAttendanceCodeResponse;
import com.octopusbjsindia.models.events.SetAttendanceCodeRequest;
import com.octopusbjsindia.request.EventDetailRequestCall;
import com.octopusbjsindia.utility.PlatformGson;
import com.octopusbjsindia.view.activities.EventDetailActivity;

public class EventDetailPresenter implements EventDetailListener {
    EventDetailActivity activity;

    public EventDetailPresenter(EventDetailActivity eventDetailActivity) {
        this.activity=eventDetailActivity;
    }

    public void getAttendanceCode(String eventId) {
        EventDetailRequestCall requestCall = new EventDetailRequestCall();
        requestCall.setEventDetailListener(this);

        activity.showProgressBar();
        requestCall.getAttendanceCode(eventId);
    }

    public void setAttendanceCode(SetAttendanceCodeRequest request) {
        EventDetailRequestCall requestCall = new EventDetailRequestCall();
        requestCall.setEventDetailListener(this);

        activity.showProgressBar();
        requestCall.setAttendanceCode(request);

    }

    public void memberList(String id) {
        EventDetailRequestCall requestCall = new EventDetailRequestCall();
        requestCall.setEventDetailListener(this);

        activity.showProgressBar();
        requestCall.getMemberList(id);
    }

    public void setTaskMarkComplete(String id) {
        EventDetailRequestCall requestCall = new EventDetailRequestCall();
        requestCall.setEventDetailListener(this);

        activity.showProgressBar();
        requestCall.setTaskMarkComplete(id);
    }

    public void delete(String id) {
        EventDetailRequestCall requestCall = new EventDetailRequestCall();
        requestCall.setEventDetailListener(this);

        activity.showProgressBar();
        requestCall.delete(id);
    }


    @Override
    public void onAttendanceCodeFetched(String response) {
        if (activity != null) {
            activity.hideProgressBar();
            if (!TextUtils.isEmpty(response)) {
                GetAttendanceCodeResponse data = PlatformGson.getPlatformGsonInstance().fromJson(response, GetAttendanceCodeResponse.class);
                if (activity != null) {
                    activity.getAttendanceCode(data);
                }
            }
        }
    }

    @Override
    public void onAttendanceCodeSubmitted(String response) {
        if (activity != null) {
            activity.hideProgressBar();
            if (!TextUtils.isEmpty(response)) {
                GetAttendanceCodeResponse data = PlatformGson.getPlatformGsonInstance().fromJson(response, GetAttendanceCodeResponse.class);
                if(data!= null){
                    onFailureListener(data.getMessage());
                }
            }
        }
    }

    @Override
    public void onParticipantsListFetched(String response) {
        if (activity != null) {
            activity.hideProgressBar();
            if (!TextUtils.isEmpty(response)) {
                EventMemberLestResponse data = PlatformGson.getPlatformGsonInstance().fromJson(response, EventMemberLestResponse.class);
                if (activity != null) {
                    activity.showMemberList(data.getData());
                }
            }
        }
    }

    @Override
    public void onTaskMarkComplete(String response) {
        if (activity != null) {
            activity.hideProgressBar();
            if (!TextUtils.isEmpty(response)) {
                CommonResponse data = PlatformGson.getPlatformGsonInstance().fromJson(response, CommonResponse.class);
                if (activity != null) {
                    onFailureListener(data.getMessage());
                }
            }
        }
    }

    @Override
    public void onDeleted(String response) {
        if (activity != null) {
            activity.hideProgressBar();
            if (!TextUtils.isEmpty(response)) {
                CommonResponse data = PlatformGson.getPlatformGsonInstance().fromJson(response, CommonResponse.class);
                if (activity != null) {
                    activity.showNextScreen(data.getMessage());
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
