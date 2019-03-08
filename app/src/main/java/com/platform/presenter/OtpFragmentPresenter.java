package com.platform.presenter;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.UserRequestCallListener;
import com.platform.models.login.Login;
import com.platform.models.login.LoginFail;
import com.platform.models.login.LoginInfo;
import com.platform.models.user.User;
import com.platform.request.LoginRequestCall;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.fragments.NewOtpFragment;

import java.lang.ref.WeakReference;

@SuppressWarnings("CanBeFinal")
public class OtpFragmentPresenter implements UserRequestCallListener {

    private boolean isOtpVerifyCall;
    private WeakReference<NewOtpFragment> otpFragment;

    public OtpFragmentPresenter(NewOtpFragment otpFragment) {
        this.otpFragment = new WeakReference<>(otpFragment);
    }

    public void resendOtp(LoginInfo loginInfo) {
        if (otpFragment == null || otpFragment.get() == null) {
            return;
        }

        isOtpVerifyCall = false;
        LoginRequestCall loginRequestCall = new LoginRequestCall();
        loginRequestCall.setListener(this);

        otpFragment.get().showProgressBar();
        loginRequestCall.resendOtp(loginInfo);
    }

    public void getLoginToken(final LoginInfo loginInfo, String otp) {
        if (otpFragment.get().isAllFieldsValid()) {
            loginInfo.setOneTimePassword(otp);

            isOtpVerifyCall = true;
            LoginRequestCall loginRequestCall = new LoginRequestCall();
            loginRequestCall.setListener(this);

            otpFragment.get().showProgressBar();
            loginRequestCall.getToken(loginInfo);
        }
    }

    @Override
    public void onSuccessListener(String response) {
        if (otpFragment == null || otpFragment.get() == null) {
            return;
        }

        Login login = new Gson().fromJson(response, Login.class);
        if (login.getStatus().equalsIgnoreCase(Constants.SUCCESS)) {
            Util.saveLoginObjectInPref(response);
        } else if (login.getStatus().equalsIgnoreCase(Constants.FAILURE)) {
            otpFragment.get().deRegisterOtpSmsReceiver();
        }

        if (isOtpVerifyCall) {
            LoginRequestCall loginRequestCall = new LoginRequestCall();
            loginRequestCall.setListener(this);
            // Get User Profile
            loginRequestCall.getUserProfile();
        } else {
            otpFragment.get().hideProgressBar();
            otpFragment.get().showNextScreen(null);
        }
    }

    @Override
    public void onUserProfileSuccessListener(String response) {
        User user = new Gson().fromJson(response, User.class);
        if (response != null && user.getUserInfo() != null) {
            Util.saveUserObjectInPref(new Gson().toJson(user.getUserInfo()));
        }

        isOtpVerifyCall = false;
        otpFragment.get().hideProgressBar();
        otpFragment.get().showNextScreen(user);
    }

    @Override
    public void onFailureListener(String response) {
        if (otpFragment == null || otpFragment.get() == null) {
            return;
        }

        otpFragment.get().hideProgressBar();
        otpFragment.get().deRegisterOtpSmsReceiver();

        LoginFail loginFail = new Gson().fromJson(response, LoginFail.class);
        otpFragment.get().showErrorMessage(loginFail.getMessage());
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (otpFragment == null || otpFragment.get() == null) {
            return;
        }

        otpFragment.get().hideProgressBar();
        if (error != null) {
            otpFragment.get().showErrorMessage(error.getLocalizedMessage());
        }
    }
}
