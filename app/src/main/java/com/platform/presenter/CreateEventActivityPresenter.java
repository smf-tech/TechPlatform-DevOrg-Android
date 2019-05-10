package com.platform.presenter;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.platform.listeners.CreateEventListener;
import com.platform.models.events.Event;
import com.platform.request.EventRequestCall;
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

    public void getEventCategory() {
        EventRequestCall requestCall = new EventRequestCall();
        requestCall.setCreateEventListener(this);

        createEventActivity.get().showProgressBar();
        requestCall.getCategory();
    }

    public void submitEvent(Event event) {
        EventRequestCall requestCall = new EventRequestCall();
        requestCall.setCreateEventListener(this);

        createEventActivity.get().showProgressBar();
        requestCall.submitEvent(event);
    }

    @Override
    public void onCategoryFetched(String response) {
        createEventActivity.get().hideProgressBar();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject categoryResponse = new JSONObject(response);
                JSONArray data = categoryResponse.getJSONArray("data");
                ArrayList categoryTypes = new ArrayList();
                for(int i=0; i<data.length();i++){
                    categoryTypes.add(data.getJSONObject(i).getString("name"));
                }
                createEventActivity.get().showCategoryTypes(categoryTypes);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onEventsFetched(String response) {

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
