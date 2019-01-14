package com.platform.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.platform.R;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.login.LoginInfo;
import com.platform.presenter.OtpFragmentPresenter;
import com.platform.receivers.SmsReceiver;
import com.platform.utility.Constants;
import com.platform.utility.Permissions;
import com.platform.utility.Util;
import com.platform.view.activities.OtpActivity;
import com.platform.view.activities.ProfileActivity;

public class OtpFragment extends Fragment implements View.OnClickListener, PlatformTaskListener,
        SmsReceiver.OtpSmsReceiverListener {

    private final String TAG = OtpFragment.class.getName();
    private TextView tvOtpTimer;
    private TextView tvOtpMessage;
    private Button btnLogin;
    private EditText etOtp;
    private ProgressBar pbVerifyLogin;
    private RelativeLayout pbVerifyLoginLayout;

    private LoginInfo loginInfo;
    private String mobileNumber;
    private long currentSec = 0;

    private boolean isSmsReceiverRegistered;
    private boolean isSmSPermissionNotDenied;

    private CountDownTimer timer;
    private OtpFragmentPresenter otpPresenter;
    private SmsReceiver smsReceiver;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        smsReceiver = new SmsReceiver();
        smsReceiver.setListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_login, container, false);
        view.findViewById(R.id.edt_mobile_number).setVisibility(View.GONE);

        tvOtpMessage = view.findViewById(R.id.enter_mobile_label);
        tvOtpMessage.setText(getString(R.string.msg_manual_otp));

        TextView tvResendOtp = view.findViewById(R.id.tv_resend_otp);
        tvResendOtp.setVisibility(View.VISIBLE);
        tvResendOtp.setOnClickListener(this);

        tvOtpTimer = view.findViewById(R.id.tv_otp_timer);
        tvOtpTimer.setVisibility(View.VISIBLE);

        btnLogin = view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        etOtp = view.findViewById(R.id.edt_otp);
        etOtp.setVisibility(View.VISIBLE);

        pbVerifyLogin = view.findViewById(R.id.pb_login);
        pbVerifyLoginLayout = view.findViewById(R.id.login_progress_bar);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            loginInfo = bundle.getParcelable(Constants.Login.LOGIN_OTP_VERIFY_DATA);
        }

        otpPresenter = new OtpFragmentPresenter(this);

        if (loginInfo != null && !loginInfo.getMobileNumber().contentEquals("")) {
            mobileNumber = loginInfo.getMobileNumber();

            if (!loginInfo.getOneTimePassword().isEmpty()) {
                etOtp.setText(loginInfo.getOneTimePassword());
                tvOtpTimer.setVisibility(View.GONE);
            } else {
                checkSmsPermission();
                tvOtpTimer.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        if (smsReceiver != null) {
            smsReceiver.deRegisterListener();
        }

        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (otpPresenter != null) {
            otpPresenter = null;
        }
    }

    private void checkSmsPermission() {
        if (Permissions.isSMSPermissionGranted(getActivity(), this)) {
            isSmSPermissionNotDenied = true;
            registerOtpSmsReceiver();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (timer != null) {
                    timer.cancel();
                }

                tvOtpTimer.setText("");
                tvOtpTimer.setVisibility(View.GONE);
                otpPresenter.getLoginToken(loginInfo, String.valueOf(etOtp.getText()).trim());
                break;

            case R.id.tv_resend_otp:
                etOtp.setText("");

                if (timer != null) {
                    timer.cancel();
                }

                if (!isSmsReceiverRegistered) {
                    registerOtpSmsReceiver();
                }

                if (mobileNumber.equalsIgnoreCase(loginInfo.getMobileNumber())) {
                    loginInfo.setOneTimePassword("");
                    otpPresenter.resendOtp(loginInfo);
                }
                break;
        }
    }

    public boolean isAllFieldsValid() {
        if (String.valueOf(etOtp.getText()).trim().length() != 6) {
            Util.setError(etOtp, getResources().getString(R.string.check_otp));
            return false;
        }

        return true;
    }

    @Override
    public void smsReceive(String otp) {
        etOtp.setText(otp);
        deRegisterOtpSmsReceiver();

        if (timer != null) {
            timer.cancel();
        }

        tvOtpTimer.setText("");
        tvOtpTimer.setVisibility(View.GONE);
        currentSec = 0;
    }

    public void startOtpTimer() {
        if (timer != null) {
            timer.cancel();
            tvOtpTimer.setText("");
            tvOtpTimer.setVisibility(View.GONE);
        }

        timer = new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long l) {
                if (getActivity() != null) {
                    currentSec = l / 1000;
                    tvOtpTimer.setText(getString(R.string.otp_timeout_text, "" + currentSec));
                    if (isSmSPermissionNotDenied) {
                        tvOtpMessage.setText(R.string.msg_waiting_for_otp_text);
                    }
                }
            }

            @Override
            public void onFinish() {
                if (getActivity() != null) {
                    if (isSmSPermissionNotDenied) {
                        tvOtpMessage.setText(R.string.msg_otp_verification_timeout);
                    }
                    resetTimer();
                }
            }
        }.start();
    }

    private void resetTimer() {
        tvOtpTimer.setText("");
        tvOtpTimer.setVisibility(View.GONE);
        deRegisterOtpSmsReceiver();
        currentSec = 0;
    }

    private void registerOtpSmsReceiver() {
        try {
            if (getActivity() != null) {
                startOtpTimer();
                getActivity().registerReceiver(smsReceiver,
                        new IntentFilter(Constants.SMS_RECEIVE_IDENTIFIER));
                isSmsReceiverRegistered = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deRegisterOtpSmsReceiver() {
        if (getActivity() != null && smsReceiver != null && isSmsReceiverRegistered) {
            try {
                getActivity().unregisterReceiver(smsReceiver);
                isSmsReceiverRegistered = false;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.SMS_RECEIVE_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    registerOtpSmsReceiver();
                    isSmSPermissionNotDenied = true;
                } else {
                    isSmSPermissionNotDenied = false;
                }
                break;
        }
    }

    @Override
    public void showProgressBar() {
        btnLogin.setClickable(false);
        if (pbVerifyLogin != null && pbVerifyLoginLayout.getVisibility() == View.GONE) {
            if (pbVerifyLogin != null && !pbVerifyLogin.isShown()) {
                pbVerifyLogin.setVisibility(View.VISIBLE);
                pbVerifyLoginLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void hideProgressBar() {
        btnLogin.setClickable(true);
        if (pbVerifyLoginLayout != null && pbVerifyLoginLayout.getVisibility() == View.VISIBLE) {
            if (pbVerifyLogin != null && pbVerifyLogin.isShown()) {
                pbVerifyLogin.setVisibility(View.GONE);
                pbVerifyLoginLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public <T> void showNextScreen(T data) {
        if (data != null) {
            try {
                Util.saveUserMobileInPref(loginInfo.getMobileNumber());
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.Login.LOGIN_OTP_VERIFY_DATA, loginInfo);

                OtpActivity activity = (OtpActivity) getActivity();
                if (activity != null) {
                    activity.startActivity(intent);
                    activity.finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Exception :: OtpFragment : showNextScreen");
            }
        }
    }

    @Override
    public void showErrorMessage(String result) {
        Util.showToast(result, this);
    }
}
