package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.models.UserInfo;
import com.platform.request.ProfileRequestCall;
import com.platform.utility.Util;
import com.platform.view.activities.ProfileActivity;

import java.lang.ref.WeakReference;

public class ProfileActivityPresenter implements PlatformRequestCallListener {

    @SuppressWarnings("CanBeFinal")
    private final String TAG = ProfileActivityPresenter.class.getName();
    @SuppressWarnings("CanBeFinal")
    private WeakReference<ProfileActivity> profileActivity;

    public ProfileActivityPresenter(ProfileActivity activity) {
        profileActivity = new WeakReference<>(activity);
    }

    public void submitProfile(final UserInfo userInfo) {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);

        profileActivity.get().showProgressBar();
        requestCall.submitUserProfile(userInfo);
    }

    public void getOrganizations() {
        ProfileRequestCall requestCall = new ProfileRequestCall();
        requestCall.setListener(this);
        requestCall.getOrganizations();
    }

    @Override
    public void onSuccessListener(String response) {
        UserInfo userInfo = new Gson().fromJson(response, UserInfo.class);

        // Save response
        Util.saveUserObjectInPref(response);

        profileActivity.get().hideProgressBar();
        profileActivity.get().showNextScreen(userInfo);
    }

    @Override
    public void onFailureListener(String message) {
        Log.i(TAG, "Fail" + message);
        profileActivity.get().hideProgressBar();
        profileActivity.get().showErrorMessage(message);
    }

    @Override
    public void onErrorListener(VolleyError error) {
        Log.i(TAG, "Error" + error);
        profileActivity.get().hideProgressBar();

        if (error != null) {
            profileActivity.get().showErrorMessage(error.getLocalizedMessage());
        }
    }
}
