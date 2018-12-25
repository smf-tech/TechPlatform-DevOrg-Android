package com.platform.controller;

import android.util.Log;

import com.android.volley.VolleyError;
import com.platform.listeners.LoginActivityListener;
import com.platform.models.login.Login;
import com.platform.request.LoginRequestCallListener;

import java.lang.ref.WeakReference;

public class LoginActivityController implements LoginRequestCallListener {

    private WeakReference<LoginActivityListener> loginActivity;
    private final String TAG = LoginActivityController.class.getSimpleName();

    public LoginActivityController(LoginActivityListener loginActivity) {
        this.loginActivity = new WeakReference<>(loginActivity);
    }

    public void detachView() {
        detachReferences();
    }

    @Override
    public void onSuccessListener(Login login) {
        if (loginActivity == null || loginActivity.get() == null) {
            Log.e(TAG, "Activity returned null");
            return;
        }

        Log.e(TAG, "Request success :" + login.toString());
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
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (loginActivity == null || loginActivity.get() == null) {
            Log.e(TAG, "Activity returned null");
            return;
        }

        loginActivity.get().hideProgressBar();
        Log.e(TAG, "Login::onErrorResponse " + error.networkResponse.statusCode);
    }

    private void detachReferences() {
        this.loginActivity = null;
    }
}
