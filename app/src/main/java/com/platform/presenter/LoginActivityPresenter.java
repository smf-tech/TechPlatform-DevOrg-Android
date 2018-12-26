package com.platform.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.platform.models.login.Login;
import com.platform.listeners.PlatformRequestCallListener;
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
