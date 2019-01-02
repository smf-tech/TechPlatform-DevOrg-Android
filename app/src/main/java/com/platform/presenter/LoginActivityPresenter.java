package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.models.login.LoginInfo;
import com.platform.request.LoginRequestCall;
import com.platform.view.activities.LoginActivity;

import java.lang.ref.WeakReference;

public class LoginActivityPresenter implements PlatformRequestCallListener {

    private final String TAG = LoginActivityPresenter.class.getSimpleName();
    private WeakReference<LoginActivity> loginActivity;

    public LoginActivityPresenter(LoginActivity loginActivity) {
        this.loginActivity = new WeakReference<>(loginActivity);
    }

    public void detachView() {
        detachReferences();
    }

    public void getOtp(LoginInfo loginInfo) {
        LoginRequestCall loginRequestCall = new LoginRequestCall();
        loginRequestCall.setListener(this);

        loginActivity.get().showProgressBar();
        loginRequestCall.generateOtp(loginInfo);
    }

    @Override
    public void onSuccessListener(String response) {
        if (loginActivity == null || loginActivity.get() == null) {
            Log.e(TAG, "Activity returned null");
            return;
        }

        loginActivity.get().hideProgressBar();
        loginActivity.get().gotoNextScreen(response);
    }

    @Override
    public void onFailureListener(String message) {
        if (loginActivity == null || loginActivity.get() == null) {
            Log.e(TAG, "Activity returned null");
            return;
        }

        loginActivity.get().hideProgressBar();
        if (message != null) {
            Log.e(TAG, "Request failed :" + message);
            // TODO: testing
            loginActivity.get().gotoNextScreen(message);
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (loginActivity == null || loginActivity.get() == null) {
            Log.e(TAG, "Activity returned null");
            return;
        }

        loginActivity.get().hideProgressBar();
        Log.e(TAG, "Login::onErrorResponse " + error);
        // TODO: testing
        loginActivity.get().gotoNextScreen(error.getMessage());
    }

    private void detachReferences() {
        this.loginActivity = null;
    }
}
