package com.platform.view.fragments;


import android.content.Intent;
import android.content.IntentFilter;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewOtpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewOtpFragment extends Fragment implements View.OnClickListener, PlatformTaskListener,
        SmsReceiver.OtpSmsReceiverListener {

    private static LoginInfo sLoginInfo;
    private String mobileNumber;
    private long currentSec = 0;

    private boolean isSmsReceiverRegistered;
    private boolean isSmSPermissionNotDenied;

    private CountDownTimer timer;
    private OtpFragmentPresenter otpPresenter;
    private SmsReceiver smsReceiver;
    private Button mBtnVerify;
    private TextView tvOtpTimer;
    private TextView tvOtpMessage;
    private EditText mOtp1, mOtp2, mOtp3, mOtp4;

    private ProgressBar pbVerifyLogin;
    private RelativeLayout pbVerifyLoginLayout;
    private final String TAG = NewOtpFragment.class.getSimpleName();
    private String mOTP;

    public NewOtpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewOtpFragment.
     * @param loginInfo
     */
    public static NewOtpFragment newInstance(final LoginInfo loginInfo) {
        sLoginInfo = loginInfo;
        return new NewOtpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.otp_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvOtpMessage = view.findViewById(R.id.enter_mobile_label);

        mOtp1 = view.findViewById(R.id.otp_1);
        mOtp2 = view.findViewById(R.id.otp_2);
        mOtp3 = view.findViewById(R.id.otp_3);
        mOtp4 = view.findViewById(R.id.otp_4);

        TextView tvResendOtp = view.findViewById(R.id.tv_resend_otp);
        tvResendOtp.setVisibility(View.VISIBLE);
        tvResendOtp.setOnClickListener(this);

        tvOtpTimer = view.findViewById(R.id.tv_otp_timer);
        tvOtpTimer.setVisibility(View.VISIBLE);

        mBtnVerify = view.findViewById(R.id.btn_verify);
        mBtnVerify.setOnClickListener(this);

        pbVerifyLogin = view.findViewById(R.id.pb_login);
        pbVerifyLoginLayout = view.findViewById(R.id.login_progress_bar);

        otpPresenter = new OtpFragmentPresenter(this);

        if (sLoginInfo != null && !sLoginInfo.getMobileNumber().contentEquals("")) {
            mobileNumber = sLoginInfo.getMobileNumber();

            tvOtpMessage.append(mobileNumber);

            if (!sLoginInfo.getOneTimePassword().isEmpty()) {
                mOTP = sLoginInfo.getOneTimePassword();
                Log.e(TAG, "onViewCreated: " + mOTP);
                char[] chars = mOTP.toCharArray();
//                String[] otp = oneTimePassword.split("");
                if (chars.length >= 4) {
                    mOtp1.setText(String.valueOf(chars[0]));
                    mOtp2.setText(String.valueOf(chars[1]));
                    mOtp3.setText(String.valueOf(chars[2]));
                    mOtp4.setText(String.valueOf(chars[3]));
                }
                tvOtpTimer.setVisibility(View.GONE);
            } else {
                checkSmsPermission();
                tvOtpTimer.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_verify:
                if (timer != null) {
                    timer.cancel();
                }

                tvOtpTimer.setText("");
                tvOtpTimer.setVisibility(View.GONE);
                String otp = getOtp();
                otpPresenter.getLoginToken(sLoginInfo, otp);
                break;

        }
    }

    private String getOtp() {
//        return mOtp1.getText().toString() +
//                mOtp2.getText().toString() +
//                mOtp3.getText().toString() +
//                mOtp4.getText().toString() +
//                mOtp5.getText().toString() +
//                mOtp6.getText().toString();

        return mOTP;
    }

    public boolean isAllFieldsValid() {
//        if (String.valueOf(etOtp.getText()).trim().length() != 6) {
//            Util.setError(etOtp, getResources().getString(R.string.check_otp));
//            return false;
//        }

        return true;
    }

    @Override
    public void smsReceive(String otp) {

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
//                startOtpTimer();
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

    private void checkSmsPermission() {
        if (Permissions.isSMSPermissionGranted(getActivity(), this)) {
            isSmSPermissionNotDenied = true;
            registerOtpSmsReceiver();
        }
    }

    @Override
    public void showProgressBar() {
        mBtnVerify.setClickable(false);
        if (pbVerifyLogin != null && pbVerifyLoginLayout.getVisibility() == View.GONE) {
            if (pbVerifyLogin != null && !pbVerifyLogin.isShown()) {
                pbVerifyLogin.setVisibility(View.VISIBLE);
                pbVerifyLoginLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void hideProgressBar() {
        mBtnVerify.setClickable(true);
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
                Util.saveUserMobileInPref(sLoginInfo.getMobileNumber());
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constants.Login.LOGIN_OTP_VERIFY_DATA, sLoginInfo);

                OtpActivity activity = (OtpActivity) getActivity();
                if (activity != null) {
                    activity.startActivity(intent);
                    activity.finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("NewOTPFragment", "Exception :: OtpFragment : showNextScreen");
            }
        }
    }

    @Override
    public void showErrorMessage(String result) {
        Util.showToast(result, this);
    }
}
