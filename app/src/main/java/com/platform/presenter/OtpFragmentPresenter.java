package com.platform.presenter;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.models.login.Login;
import com.platform.models.login.LoginInfo;
import com.platform.request.LoginRequestCall;
import com.platform.utility.Constants;
import com.platform.utility.Util;
import com.platform.view.fragments.NewOtpFragment;

import java.lang.ref.WeakReference;

public class OtpFragmentPresenter implements PlatformRequestCallListener {

    @SuppressWarnings("CanBeFinal")
//    private WeakReference<OtpFragment> otpFragment;
    private WeakReference<NewOtpFragment> otpFragment;

    public OtpFragmentPresenter(NewOtpFragment otpFragment) {
        this.otpFragment = new WeakReference<>(otpFragment);
    }

    public void resendOtp(LoginInfo loginInfo) {
        if (otpFragment == null || otpFragment.get() == null) {
            return;
        }

        LoginRequestCall loginRequestCall = new LoginRequestCall();
        loginRequestCall.setListener(this);

        otpFragment.get().showProgressBar();
        loginRequestCall.resendOtp(loginInfo);
    }

    public void getLoginToken(final LoginInfo loginInfo, String otp) {
        if (otpFragment.get().isAllFieldsValid()) {
            loginInfo.setOneTimePassword(otp);

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

        otpFragment.get().hideProgressBar();
        otpFragment.get().showNextScreen(login);
    }

    @Override
    public void onFailureListener(String message) {
        if (otpFragment == null || otpFragment.get() == null) {
            return;
        }

        otpFragment.get().hideProgressBar();
        otpFragment.get().deRegisterOtpSmsReceiver();
        otpFragment.get().showErrorMessage(message);
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
