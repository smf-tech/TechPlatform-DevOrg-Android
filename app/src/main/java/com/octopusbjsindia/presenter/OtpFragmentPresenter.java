package com.octopusbjsindia.presenter;

import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.UserRequestCallListener;
import com.octopusbjsindia.models.login.Login;
import com.octopusbjsindia.models.login.LoginInfo;
import com.octopusbjsindia.models.user.User;
import com.octopusbjsindia.request.LoginRequestCall;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.fragments.NewOtpFragment;

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
        if (login.getCode() == 400) {
            onFailureListener(login.getMessage());
        } else {
            if (login.getCode() == 200 || login.getCode() == 300) {
                Util.saveLoginObjectInPref(response);
                if (login.getCode() == 200) {
                    Util.saveIsDeviceMatchInPref("Matched");
//                    UserInfo userInfo = Util.getUserObjectFromPref();
//                    userInfo.setIsDeviceMatched(1);
//                    Util.saveUserObjectInPref(new Gson().toJson(userInfo));
                } else {
                    Util.saveIsDeviceMatchInPref("MisMatched");
//                    UserInfo userInfo = Util.getUserObjectFromPref();
//                    userInfo.setIsDeviceMatched(0);
//                    Util.saveUserObjectInPref(new Gson().toJson(userInfo));
                    //Util.getUserObjectFromPref().setIsDeviceMatched(0);
                }
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

        if (response != null && response.length() > 0) {
            otpFragment.get().showErrorMessage(response);
        } else {
            otpFragment.get().showErrorMessage(Platform.getInstance().getString(R.string.msg_failure));
        }
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (otpFragment == null || otpFragment.get() == null) {
            return;
        }

        otpFragment.get().hideProgressBar();
        if (error != null && error.networkResponse != null) {
            if (error.networkResponse.statusCode == Constants.TIMEOUT_ERROR_CODE) {
                if (error.networkResponse.data != null) {
                    String json = new String(error.networkResponse.data);
                    json = Util.trimMessage(json);
                    if (json != null) {
                        Util.showToast(json, otpFragment.get().getActivity());
                    } else {
                        Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                                otpFragment.get().getActivity());
                    }
                } else {
                    Util.showToast(Platform.getInstance().getString(R.string.msg_slow_network),
                            otpFragment.get().getActivity());
                }
            } else {
                otpFragment.get().showErrorMessage(error.getLocalizedMessage());
                Log.e("onErrorListener",
                        "Unexpected response code " + error.networkResponse.statusCode);
            }
        }
    }
}
