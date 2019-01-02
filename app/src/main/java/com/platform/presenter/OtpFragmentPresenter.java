package com.platform.presenter;

import com.android.volley.VolleyError;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.models.login.LoginInfo;
import com.platform.request.LoginRequestCall;
import com.platform.view.fragments.OtpFragment;

import java.lang.ref.WeakReference;

public class OtpFragmentPresenter implements PlatformRequestCallListener {

    @SuppressWarnings("CanBeFinal")
    private WeakReference<OtpFragment> otpFragment;

    public OtpFragmentPresenter(OtpFragment otpFragment) {
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

        otpFragment.get().hideProgressBar();

        if (response.equalsIgnoreCase("Success")) {
            otpFragment.get().startOtpTimer();
        } else if (response.equalsIgnoreCase("Failure")) {
            otpFragment.get().deRegisterOtpSmsReceiver();
        }

        otpFragment.get().gotoNextScreen(response);
    }

    @Override
    public void onFailureListener(String message) {
        if (otpFragment == null || otpFragment.get() == null) {
            return;
        }

        otpFragment.get().hideProgressBar();
        otpFragment.get().deRegisterOtpSmsReceiver();
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (otpFragment == null || otpFragment.get() == null) {
            return;
        }

        otpFragment.get().hideProgressBar();

        //TODO: Removed once API integration done
        otpFragment.get().gotoNextScreen(error.getMessage());
    }
}
