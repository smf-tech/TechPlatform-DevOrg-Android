package com.platform.presenter;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.platform.listeners.CreateEventListener;
import com.platform.models.events.AddForm;
import com.platform.models.events.AddFormsResponse;
import com.platform.models.events.Event;
import com.platform.models.events.EventsResponse;
import com.platform.models.profile.JurisdictionType;
import com.platform.request.EventRequestCall;
import com.platform.utility.PlatformGson;
import com.platform.utility.Util;
import com.platform.view.activities.CreateEventActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CreateEventActivityPresenter implements CreateEventListener {

    private final WeakReference<CreateEventActivity> createEventActivity;

    public CreateEventActivityPresenter(CreateEventActivity createEventActivity) {
        this.createEventActivity = new WeakReference<>(createEventActivity);
    }

    public void getFormData(ArrayList<JurisdictionType> projectIds) {
        EventRequestCall requestCall = new EventRequestCall();
        requestCall.setCreateEventListener(this);

        createEventActivity.get().showProgressBar();
        requestCall.getFormData(projectIds);
    }

    public void submitEvent(Event event) {
        EventRequestCall requestCall = new EventRequestCall();
        requestCall.setCreateEventListener(this);

        createEventActivity.get().showProgressBar();
        requestCall.submitEvent(event);
    }

    @Override
    public void onEventsFetched(String response) {

    }

    @Override
    public void onFormsFetched(String response) {
        if (createEventActivity.get() != null) {
            createEventActivity.get().hideProgressBar();
            if (!TextUtils.isEmpty(response)) {

                AddFormsResponse data = PlatformGson.getPlatformGsonInstance().fromJson(response, AddFormsResponse.class);

                if (createEventActivity != null && createEventActivity.get() != null) {
                    createEventActivity.get().onFormsListFatched((ArrayList< AddForm > )data.getData());
                }
            }
        }
    }

    @Override
    public void onEventSubmitted(String response) {
        createEventActivity.get().hideProgressBar();

    }

    @Override
    public void onFailureListener(String message) {
        if (createEventActivity.get() != null) {
            createEventActivity.get().hideProgressBar();
            createEventActivity.get().showErrorMessage(message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (createEventActivity.get() != null) {
            createEventActivity.get().hideProgressBar();
            if (error != null) {
                createEventActivity.get().showErrorMessage(error.getLocalizedMessage());
            }
        }
    }
}
