package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.models.UserInfo;
import com.platform.request.ProfileRequestCall;
import com.platform.view.activities.ProfileActivity;

import java.lang.ref.WeakReference;

public class ProfileActivityPresenter implements PlatformRequestCallListener {

    @SuppressWarnings("CanBeFinal")
    private final String TAG = ProfileActivityPresenter.class.getName();
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
        // {"_id":"5c2ddcaed503a33074095df3","email":"","phone":"7972129849","approve_status":"pending","updated_at":"2019-01-03 14:03:35","created_at":"2019-01-03 09:58:06","dob":"1970-01-01"}
        Log.i(TAG, "Success" + response);
    }

    @Override
    public void onFailureListener(String message) {
        Log.i(TAG, "Fail" + message);
    }

    @Override
    public void onErrorListener(VolleyError error) {
        Log.i(TAG, "Error" + error);
    }
}
