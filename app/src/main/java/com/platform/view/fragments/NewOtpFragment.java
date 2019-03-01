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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.platform.models.user.User;
import com.platform.presenter.OtpFragmentPresenter;
import com.platform.receivers.SmsReceiver;
import com.platform.utility.Constants;
import com.platform.utility.Permissions;
import com.platform.utility.Util;
import com.platform.view.activities.HomeActivity;
import com.platform.view.activities.OtpActivity;
import com.platform.view.activities.ProfileActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewOtpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("FieldCanBeLocal")
public class NewOtpFragment extends Fragment implements View.OnClickListener, PlatformTaskListener,
        SmsReceiver.IOtpSmsReceiverListener {

    private static LoginInfo sLoginInfo;
    private long currentSec = 0;

    private boolean isResendOtpRequest;
    private boolean isSmsReceiverRegistered;
    private boolean isSmsPermissionNotDenied;

    private SmsReceiver smsReceiver;
    private OtpFragmentPresenter otpPresenter;

    private CountDownTimer timer;
    private Button mBtnVerify;
    private TextView tvOtpTimer;
    private TextView tvOtpMessage;
    private EditText mOtp1, mOtp2, mOtp3, mOtp4, mOtp5, mOtp6;
    private ProgressBar pbVerifyLogin;
    private RelativeLayout pbVerifyLoginLayout;

    private StringBuilder sb;
    private String mMobileNumber;

    private final String TAG = NewOtpFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        smsReceiver = new SmsReceiver();
        smsReceiver.setListener(this);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param loginInfo : login info object
     * @return A new instance of fragment NewOtpFragment.
     */
    public static NewOtpFragment newInstance(final LoginInfo loginInfo) {
        sLoginInfo = loginInfo;
        return new NewOtpFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvOtpMessage = view.findViewById(R.id.enter_mobile_label);

        mOtp1 = view.findViewById(R.id.otp_1);
        mOtp2 = view.findViewById(R.id.otp_2);
        mOtp3 = view.findViewById(R.id.otp_3);
        mOtp4 = view.findViewById(R.id.otp_4);
        mOtp5 = view.findViewById(R.id.otp_5);
        mOtp6 = view.findViewById(R.id.otp_6);

        sb = new StringBuilder();
        mOtp1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (sb.length() == 0 & mOtp1.length() == 1) {
                    sb.append(s);
                    mOtp1.clearFocus();
                    mOtp2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (sb.length() == 0) {
                    mOtp1.requestFocus();
                }

                if (TextUtils.isEmpty(s) && sb.length() > 0) {
                    sb.deleteCharAt(0);
                    mOtp1.requestFocus();
                }
            }
        });

        mOtp2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (sb.length() == 1 & mOtp2.length() == 1) {
                    sb.append(s);
                    mOtp2.clearFocus();
                    mOtp3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (sb.length() == 1) {
                    mOtp2.requestFocus();
                }

                if (TextUtils.isEmpty(s) && sb.length() > 1) {
                    sb.deleteCharAt(1);
                    mOtp2.clearFocus();
                    mOtp1.requestFocus();
                }
            }
        });

        mOtp3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (sb.length() == 2 & mOtp3.length() == 1) {
                    sb.append(s);
                    mOtp3.clearFocus();
                    mOtp4.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (sb.length() == 2) {
                    mOtp3.requestFocus();
                }

                if (TextUtils.isEmpty(s) && sb.length() > 2) {
                    sb.deleteCharAt(2);
                    mOtp3.clearFocus();
                    mOtp2.requestFocus();
                }
            }
        });

        mOtp4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (sb.length() == 3 & mOtp4.length() == 1) {
                    sb.append(s);
                    mOtp4.clearFocus();
                    mOtp5.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (sb.length() == 3) {
                    mOtp4.requestFocus();
                }

                if (TextUtils.isEmpty(s) && sb.length() > 3) {
                    sb.deleteCharAt(3);
                    mOtp4.clearFocus();
                    mOtp3.requestFocus();
                }
            }
        });

        mOtp5.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (sb.length() == 4 & mOtp5.length() == 1) {
                    sb.append(s);
                    mOtp5.clearFocus();
                    mOtp6.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (sb.length() == 4) {
                    mOtp5.requestFocus();
                }

                if (TextUtils.isEmpty(s) && sb.length() > 4) {
                    sb.deleteCharAt(4);
                    mOtp5.clearFocus();
                    mOtp4.requestFocus();
                }
            }
        });

        mOtp6.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (sb.length() == 5 & mOtp6.length() == 1) {
                    sb.append(s);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                if (sb.length() == 5) {
                    mOtp6.requestFocus();
                }

                if (TextUtils.isEmpty(s) && sb.length() > 5) {
                    sb.deleteCharAt(5);
                    mOtp6.clearFocus();
                    mOtp5.requestFocus();
                }
            }
        });

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
            mMobileNumber = sLoginInfo.getMobileNumber();
            tvOtpMessage.setText(getString(R.string.please_type_the_verification_code_n_sent_to, mMobileNumber));
            checkSmsPermission();
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

            case R.id.tv_resend_otp:
                clearOtp();

                if (timer != null) {
                    timer.start();
                }

                if (!isSmsReceiverRegistered) {
                    registerOtpSmsReceiver();
                }

                if (mMobileNumber.equalsIgnoreCase(sLoginInfo.getMobileNumber())) {
                    sLoginInfo.setOneTimePassword("");
                    isResendOtpRequest = true;
                    otpPresenter.resendOtp(sLoginInfo);
                }
                break;
        }
    }

    private void setOtp(final String msg) {
        try {
            char[] otpChars = msg.toCharArray();
            if (otpChars.length == 6) {
                mOtp1.setText(String.valueOf(otpChars[0]));
                mOtp2.setText(String.valueOf(otpChars[1]));
                mOtp3.setText(String.valueOf(otpChars[2]));
                mOtp4.setText(String.valueOf(otpChars[3]));
                mOtp5.setText(String.valueOf(otpChars[4]));
                mOtp6.setText(String.valueOf(otpChars[5]));
            }

            if (timer != null) {
                timer.cancel();
            }

            hideProgressBar();
            tvOtpMessage.setText(getResources().getString(R.string.msg_verify_otp_text));
            tvOtpTimer.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void clearOtp() {
        mOtp1.setText("");
        mOtp2.setText("");
        mOtp3.setText("");
        mOtp4.setText("");
        mOtp5.setText("");
        mOtp6.setText("");

        mOtp1.requestFocus();
        if (sb != null) {
            sb = new StringBuilder();
        }
    }

    private String getOtp() {
        return mOtp1.getText().toString() +
                mOtp2.getText().toString() +
                mOtp3.getText().toString() +
                mOtp4.getText().toString() +
                mOtp5.getText().toString() +
                mOtp6.getText().toString();
    }

    public boolean isAllFieldsValid() {
        return String.valueOf(getOtp()).trim().length() == 6;
    }

    private void startOtpTimer() {
        if (timer != null) {
            timer.cancel();
            tvOtpTimer.setText("");
            showProgressBar();
            tvOtpTimer.setVisibility(View.GONE);
            timer = null;
        }

        timer = new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long l) {
                if (getActivity() != null) {
                    currentSec = l / 1000;
                    tvOtpTimer.setText(getString(R.string.otp_timeout_text, "" + currentSec));
                    if (isSmsPermissionNotDenied) {
                        tvOtpMessage.setText(R.string.msg_waiting_for_otp_text);
                    }
                }
            }

            @Override
            public void onFinish() {
                if (getActivity() != null) {
                    if (isSmsPermissionNotDenied) {
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
                getActivity().registerReceiver(smsReceiver, new IntentFilter(Constants.SMS_RECEIVE_IDENTIFIER));
                startOtpTimer();
                isSmsReceiverRegistered = true;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void deRegisterOtpSmsReceiver() {
        if (getActivity() != null && smsReceiver != null && isSmsReceiverRegistered) {
            try {
                getActivity().unregisterReceiver(smsReceiver);
                isSmsReceiverRegistered = false;
            } catch (IllegalArgumentException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private void checkSmsPermission() {
        if (Permissions.isSMSPermissionGranted(getActivity(), this)) {
            isSmsPermissionNotDenied = true;
            registerOtpSmsReceiver();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constants.SMS_RECEIVE_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isSmsPermissionNotDenied = true;
                    registerOtpSmsReceiver();
                }
                break;
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
        if (isResendOtpRequest) {
            isResendOtpRequest = false;
            return;
        }

        User userObj = (User) data;
        Intent intent;
        if (userObj != null && !TextUtils.isEmpty(userObj.getUserInfo().getOrgId())) {
            intent = new Intent(getActivity(), HomeActivity.class);
        } else {
            intent = new Intent(getActivity(), ProfileActivity.class);
        }

        try {
            Util.saveUserMobileInPref(sLoginInfo.getMobileNumber());

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.Login.LOGIN_OTP_VERIFY_DATA, sLoginInfo);

            OtpActivity activity = (OtpActivity) getActivity();
            if (activity != null) {
                activity.startActivity(intent);
                activity.finish();
            }
        } catch (Exception e) {
            Log.e("NewOTPFragment", "Exception :: OtpFragment : showNextScreen");
        }
    }

    @Override
    public void showErrorMessage(String result) {
        Util.showToast(result, this);
    }

    @Override
    public void smsReceive(String otp) {
        Log.d(TAG, "OTP: " + otp);
        setOtp(otp);
    }
}
