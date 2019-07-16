package com.platform.presenter;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.platform.R;
import com.platform.listeners.CreateEventListener;
import com.platform.listeners.ImageRequestCallListener;
import com.platform.models.events.AddForm;
import com.platform.models.events.AddFormsResponse;
import com.platform.models.events.CommonResponse;
import com.platform.models.events.EventTask;
import com.platform.models.events.EventMemberLestResponse;
import com.platform.models.profile.JurisdictionType;
import com.platform.request.EventRequestCall;
import com.platform.request.ImageRequestCall;
import com.platform.utility.PlatformGson;
import com.platform.utility.Util;
import com.platform.view.activities.CreateEventTaskActivity;

import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class CreateEventActivityPresenter implements CreateEventListener, ImageRequestCallListener {

    private final WeakReference<CreateEventTaskActivity> activity;
    private final String TAG = ProfileActivityPresenter.class.getName();
    public CreateEventActivityPresenter(CreateEventTaskActivity activity) {
        this.activity = new WeakReference<>(activity);
    }

    public void getFormData(ArrayList<JurisdictionType> projectIds) {
        EventRequestCall requestCall = new EventRequestCall();
        requestCall.setCreateEventListener(this);

        activity.get().showProgressBar();
        requestCall.getFormData(projectIds);
    }

    public void submitEvent(EventTask eventTask) {
        EventRequestCall requestCall = new EventRequestCall();
        requestCall.setCreateEventListener(this);

        activity.get().showProgressBar();
        requestCall.submitEvent(eventTask);
    }

    public void taskMemberList() {
        EventRequestCall requestCall = new EventRequestCall();
        requestCall.setCreateEventListener(this);

        activity.get().showProgressBar();
        requestCall.getTaskMemberList();
    }

    public void uploadProfileImage(File imageFile, String imageTypeProfile) {
        ImageRequestCall requestCall = new ImageRequestCall();
        requestCall.setListener(this);

        activity.get().showProgressBar();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... voids) {
                requestCall.uploadImageUsingHttpURLEncoded(imageFile, imageTypeProfile, null);
                return null;
            }
        }.execute();
    }

    @Override
    public void onEventsFetchedOfDay(String response) {

    }

    @Override
    public void onEventsFetchedOfMonth(String response) {

    }

    @Override
    public void onFormsFetched(String response) {
        if (activity.get() != null) {
            activity.get().hideProgressBar();
            if (!TextUtils.isEmpty(response)) {

                AddFormsResponse data = PlatformGson.getPlatformGsonInstance().fromJson(response, AddFormsResponse.class);

                if (activity != null && activity.get() != null) {
                    activity.get().onFormsListFatched((ArrayList< AddForm > )data.getData());
                }
            }
        }
    }

    @Override
    public void onTaskMembersFetched(String response) {
        if (activity != null) {
            activity.get().hideProgressBar();
            if (!TextUtils.isEmpty(response)) {
                EventMemberLestResponse data = PlatformGson.getPlatformGsonInstance().fromJson(response, EventMemberLestResponse.class);
                if (activity != null) {
                    if(data.getStatus()==200){
                        activity.get().showMemberList(data.getData());
                    } else {
                        onFailureListener(data.getMessage());
                    }

                }
            }
        }
    }

    @Override
    public void onEventSubmitted(String response) {
//        activity.get().hideProgressBar();
//        activity.get().showNextScreen("Finish");
        if (activity != null) {
            activity.get().hideProgressBar();
            if (!TextUtils.isEmpty(response)) {
                CommonResponse data = PlatformGson.getPlatformGsonInstance().fromJson(response, CommonResponse.class);
                if (activity != null) {
                    activity.get().showNextScreen(data.getMessage());
                }
            }
        }
    }

    @Override
    public void onImageUploadedListener(String response, String formName) {
        Log.e(TAG, "onImageUploadedListener:\n" + response);
        activity.get().runOnUiThread(() -> Util.showToast(
                activity.get().getResources().getString(R.string.image_upload_success), activity));
        activity.get().hideProgressBar();

        try {
            if (new JSONObject(response).has("data")) {
                JSONObject data = new JSONObject(response).getJSONObject("data");
                String url = (String) data.get("url");
                Log.e(TAG, "onPostExecute: Url: " + url);

                activity.get().onImageUploaded(url);
            } else {
                Log.e(TAG, "onPostExecute: Invalid response");
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onFailureListener(String message) {
        if (activity.get() != null) {
            activity.get().hideProgressBar();
            activity.get().showErrorMessage(message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (activity.get() != null) {
            activity.get().hideProgressBar();
            if (error != null) {
                activity.get().showErrorMessage(error.getLocalizedMessage());
            }
        }
    }
}
