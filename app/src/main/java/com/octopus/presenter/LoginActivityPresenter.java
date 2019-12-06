package com.octopus.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopus.Platform;
import com.octopus.R;
import com.octopus.listeners.UserRequestCallListener;
import com.octopus.models.login.Login;
import com.octopus.models.login.LoginInfo;
import com.octopus.request.LoginRequestCall;
import com.octopus.utility.Constants;
import com.octopus.utility.Util;
import com.octopus.view.activities.LoginActivity;

import java.lang.ref.WeakReference;

public class LoginActivityPresenter implements UserRequestCallListener {

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

        Login login = new Gson().fromJson(response, Login.class);
        loginActivity.get().hideProgressBar();
        loginActivity.get().showNextScreen(login);
    }

    @Override
    public void onUserProfileSuccessListener(String response) {

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
            loginActivity.get().showErrorMessage(Platform.getInstance().getString(R.string.msg_auth_fail));
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (loginActivity == null || loginActivity.get() == null) {
            Log.e(TAG, "Activity returned null");
            return;
        }

        loginActivity.get().hideProgressBar();

        if (error != null && error.networkResponse != null) {
            if (error.networkResponse.statusCode == Constants.TIMEOUT_ERROR_CODE) {
                if (error.networkResponse.data != null) {
                    String json = new String(error.networkResponse.data);
                    json = Util.trimMessage(json);
                    if (json != null) {
                        Util.showToast(json, loginActivity);
                    } else {
                        Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                                loginActivity);
                    }
                } else {
                    Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                            loginActivity);
                }
            } else {
                loginActivity.get().showErrorMessage(error.getLocalizedMessage());
                Log.e("onErrorListener",
                        "Unexpected response code " + error.networkResponse.statusCode);
            }
        }
    }

    private void detachReferences() {
        this.loginActivity = null;
    }
}
