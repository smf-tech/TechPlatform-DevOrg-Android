package com.platform.presenter;

import com.android.volley.VolleyError;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.models.UserInfo;
import com.platform.request.ProfileRequestCall;
import com.platform.view.activities.ProfileActivity;

import java.lang.ref.WeakReference;

public class ProfileActivityPresenter implements PlatformRequestCallListener {

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

    @Override
    public void onSuccessListener(String response) {

    }

    @Override
    public void onFailureListener(String message) {

    }

    @Override
    public void onErrorListener(VolleyError error) {

    }
}
