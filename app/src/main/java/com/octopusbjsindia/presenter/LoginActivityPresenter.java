package com.octopusbjsindia.presenter;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.UserRequestCallListener;
import com.octopusbjsindia.models.login.Login;
import com.octopusbjsindia.models.login.LoginInfo;
import com.octopusbjsindia.request.LoginRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.LoginActivity;

import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;

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
        loginActivity.get().getdynamicLogo();
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
