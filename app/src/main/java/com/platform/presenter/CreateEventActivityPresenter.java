package com.platform.presenter;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.platform.R;
import com.platform.listeners.CreateEventListener;
import com.platform.listeners.ImageRequestCallListener;
import com.platform.models.events.AddForm;
import com.platform.models.events.AddFormsResponse;
import com.platform.models.events.Event;
import com.platform.models.events.EventsResponse;
import com.platform.models.profile.JurisdictionType;
import com.platform.request.EventRequestCall;
import com.platform.request.ImageRequestCall;
import com.platform.utility.PlatformGson;
import com.platform.utility.Util;
import com.platform.view.activities.CreateEventActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CreateEventActivityPresenter implements CreateEventListener, ImageRequestCallListener {

    private final WeakReference<CreateEventActivity> createEventActivity;
    private final String TAG = ProfileActivityPresenter.class.getName();
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

    public void uploadProfileImage(File imageFile, String imageTypeProfile) {
        ImageRequestCall requestCall = new ImageRequestCall();
        requestCall.setListener(this);

        createEventActivity.get().showProgressBar();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... voids) {
                requestCall.uploadImageUsingHttpURLEncoded(imageFile, imageTypeProfile, null);
                return null;
            }
        }.execute();
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
        createEventActivity.get().showNextScreen("Finish");
    }

    @Override
    public void onImageUploadedListener(String response, String formName) {
        Log.e(TAG, "onImageUploadedListener:\n" + response);
        createEventActivity.get().runOnUiThread(() -> Util.showToast(
                createEventActivity.get().getResources().getString(R.string.image_upload_success), createEventActivity));
        createEventActivity.get().hideProgressBar();

        try {
            if (new JSONObject(response).has("data")) {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                String url = (String) data.get("url");
                Log.e(TAG, "onPostExecute: Url: " + url);

                createEventActivity.get().onImageUploaded(url);
            } else {
                Log.e(TAG, "onPostExecute: Invalid response");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
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
