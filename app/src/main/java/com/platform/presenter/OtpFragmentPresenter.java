package com.platform.presenter;

import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.platform.Platform;
import com.platform.listeners.PlatformRequestCallListener;
import com.platform.models.Model;
import com.platform.models.login.LoginInfo;
import com.platform.request.LoginRequestCall;
import com.platform.utility.GsonRequestFactory;
import com.platform.utility.Urls;
import com.platform.utility.Util;
import com.platform.view.activities.OtpActivity;
import com.platform.view.activities.ProfileActivity;
import com.platform.view.fragments.OtpFragment;

import java.lang.ref.WeakReference;

public class OtpFragmentPresenter implements PlatformRequestCallListener {

    private Gson gson;
    private WeakReference<OtpFragment> otpFragment;
    private final String TAG = OtpFragmentPresenter.class.getSimpleName();

    public OtpFragmentPresenter(OtpFragment otpFragment) {
        this.otpFragment = new WeakReference<>(otpFragment);
        this.gson = new GsonBuilder().serializeNulls().create();
    }

    public void getOtp(LoginInfo loginInfo) {
        if (otpFragment == null || otpFragment.get() == null) {
            return;
        }

        Response.Listener<Model> resendOTPResponseListener = response -> {

            if (otpFragment == null || otpFragment.get() == null) {
                return;
            }

            otpFragment.get().hideProgressBar();

            if (response.getResultCode().equalsIgnoreCase("Success")) {
                otpFragment.get().startOtpTimer();
            } else if (response.getResultCode().equalsIgnoreCase("Failure")) {
                otpFragment.get().deRegisterOtpSmsReceiver();
            }
        };

        Response.ErrorListener resendOTPErrorListener = error -> {

            if (otpFragment == null || otpFragment.get() == null) {
                return;
            }

            otpFragment.get().hideProgressBar();
            otpFragment.get().deRegisterOtpSmsReceiver();
        };

        otpFragment.get().showProgressBar();

        final GsonRequestFactory<Model> gsonRequest = new GsonRequestFactory<>(Request.Method.GET,
                Urls.BASE_URL + Urls.Login.RESEND_OTP + loginInfo.getMobileNumber(),
                new TypeToken<Model>() {
                }.getType(),
                gson,
                resendOTPResponseListener,
                resendOTPErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader());
        gsonRequest.setBodyParams(new JsonObject());
        gsonRequest.setShouldCache(false);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

    private void login(final LoginInfo loginInfo) {
        LoginRequestCall loginRequestCall = new LoginRequestCall();
        loginRequestCall.setListener(this);

        otpFragment.get().showProgressBar();
        loginRequestCall.login(loginInfo);
    }

    public void loginUser(final LoginInfo loginInfo, String otp) {
        if (otpFragment.get().isAllFieldsValid()) {
            loginInfo.setOneTimePassword(otp);
            login(loginInfo);
        }
}

    @Override
    public void onSuccessListener(String response) {
        if (otpFragment == null || otpFragment.get() == null) {
            return;
        }

        otpFragment.get().hideProgressBar();
        Log.e(TAG, "Request success :" + response);

        //TODO: Removed once API integration done
        launchProfileScreen();
    }

    @Override
    public void onFailureListener(String message) {
        if (otpFragment == null || otpFragment.get() == null) {
            return;
        }

        otpFragment.get().hideProgressBar();
    }

    @Override
    public void onErrorListener(VolleyError error) {
        if (otpFragment == null || otpFragment.get() == null) {
            return;
        }

        otpFragment.get().hideProgressBar();

        //TODO: Removed once API integration done
        launchProfileScreen();
    }

    private void launchProfileScreen() {
        Intent intent = new Intent(otpFragment.get().getActivity(), ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        OtpActivity activity = (OtpActivity) otpFragment.get().getActivity();
        if (activity != null) {
            activity.startActivity(intent);
        }
    }
}
