package com.octopusbjsindia.view.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.PlatformTaskListener;
import com.octopusbjsindia.models.login.LoginInfo;
import com.octopusbjsindia.models.user.User;
import com.octopusbjsindia.presenter.OtpFragmentPresenter;
import com.octopusbjsindia.utility.AppEvents;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.view.activities.EditProfileActivity;
import com.octopusbjsindia.view.activities.HomeActivity;
import com.octopusbjsindia.view.activities.OtpActivity;

public class NewOtpFragment extends Fragment implements View.OnClickListener, PlatformTaskListener {

    private static LoginInfo sLoginInfo;
    private long currentSec = 0;

    private boolean isResendOtpRequest;
    private boolean isSmsReceiverRegistered;
    private boolean isSmsPermissionNotDenied;

    private OtpFragmentPresenter otpPresenter;
    private BroadcastReceiver mIntentReceiver;

    private CountDownTimer timer;
    private Button mBtnVerify;
    private TextView tvOtpTimer;
    private TextView tvOtpMessage;
    private TextView tvResendOtp;
    private EditText mOtp1, mOtp2, mOtp3, mOtp4, mOtp5, mOtp6;
    private ProgressBar pbVerifyLogin;
    private RelativeLayout pbVerifyLoginLayout;

    private StringBuilder sb;
    private String mMobileNumber;

    private final String TAG = NewOtpFragment.class.getSimpleName();

    private String deviceId = "";

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
        AppEvents.trackAppEvent(getString(R.string.event_otp_screen_visit));

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

        tvResendOtp = view.findViewById(R.id.tv_resend_otp);
        tvResendOtp.setVisibility(View.GONE);
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
            enableSmsReceiver();
        }
        getDeviceId();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_verify:
                if (Util.isConnected(getActivity())) {
                    verifyUser();
                } else {
                    Util.showToast(getString(R.string.msg_no_network), this);
                }

                break;

            case R.id.tv_resend_otp:
                clearOtp();
                startOtpTimer();
                enableSmsReceiver();

                AppEvents.trackAppEvent(getString(R.string.event_resend_opt_click));
                if (mMobileNumber.equalsIgnoreCase(sLoginInfo.getMobileNumber())) {
                    sLoginInfo.setOneTimePassword("");
                    isResendOtpRequest = true;
                    otpPresenter.resendOtp(sLoginInfo);
                }
                break;
        }
    }

    private void verifyUser() {
        String otp = getOtp();
        if (deviceId.length() > 0) {
            sLoginInfo.setDeviceId(deviceId);
            Util.setStringInPref(Constants.App.deviceId, deviceId);
            otpPresenter.getLoginToken(sLoginInfo, Util.encrypt(otp));
        } else {
            Util.snackBarToShowMsg(getActivity().getWindow().getDecorView()
                            .findViewById(android.R.id.content), "Please allow - Read Phone State permission.",
                    Snackbar.LENGTH_LONG);
            getDeviceId();
            if (deviceId.length() > 0) {
                sLoginInfo.setDeviceId(deviceId);
                Util.setStringInPref(Constants.App.deviceId, deviceId);
                otpPresenter.getLoginToken(sLoginInfo, Util.encrypt(otp));
            }
        }
    }

    //get device id for token api
    private void getDeviceId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        Constants.READ_PHONE_STORAGE);
            } else {
                deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        } else {
            deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
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
            tvResendOtp.setVisibility(View.VISIBLE);

            AppEvents.trackAppEvent(getString(R.string.event_auto_read_success));
            tvOtpMessage.setText(getResources().getString(R.string.msg_verify_otp_text));
            tvOtpTimer.setVisibility(View.GONE);

            // Verify user automatically once OTP received
            verifyUser();

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
        return getOtp().trim().length() == 6;
    }

    private void startOtpTimer() {
        if (timer != null) {
            timer.cancel();

            showProgressBar();
            tvOtpTimer.setVisibility(View.VISIBLE);
            tvResendOtp.setVisibility(View.GONE);
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
                        tvResendOtp.setVisibility(View.VISIBLE);
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
                smsRetrieval();
                startOtpTimer();
                isSmsReceiverRegistered = true;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void deRegisterOtpSmsReceiver() {
        if (getActivity() != null && isSmsReceiverRegistered) {
            try {
                isSmsReceiverRegistered = false;
            } catch (IllegalArgumentException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private void enableSmsReceiver() {
        isSmsPermissionNotDenied = true;
        registerOtpSmsReceiver();
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

        if (timer != null) {
            timer.cancel();
        }

        User userObj = (User) data;
        Intent intent;
        if (userObj != null && !TextUtils.isEmpty(userObj.getUserInfo().getOrgId())) {
            intent = new Intent(getActivity(), HomeActivity.class);
        } else {
            intent = new Intent(getActivity(), EditProfileActivity.class);
        }

        try {
            Util.saveUserMobileInPref(sLoginInfo.getMobileNumber());

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Constants.Login.LOGIN_OTP_VERIFY_DATA, sLoginInfo);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(Constants.Login.LOGIN_OTP_VERIFY_DATA, sLoginInfo);

            OtpActivity activity = (OtpActivity) getActivity();
            if (activity != null) {
                activity.startActivity(intent);
                ((OtpActivity) getActivity()).killActivity();

            }
        } catch (Exception e) {
            Log.e("NewOTPFragment", "Exception :: OtpFragment : showNextScreen");
        }
    }

    @Override
    public void showErrorMessage(String result) {
        Util.showToast(result, this);
    }

    private void smsRetrieval() {
        if (getActivity() == null) {
            return;
        }

        SmsRetrieverClient client = SmsRetriever.getClient(Platform.getInstance());
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(aVoid -> {
            IntentFilter intentFilter = new IntentFilter("SmsMessage.intent.MAIN");
            mIntentReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String msg = intent.getStringExtra("get_msg");
                    if (msg != null && !msg.isEmpty()) {
                        msg = msg.replace("\n", "");

                        String body = msg.substring(msg.lastIndexOf(":") + 1);
                        body = body.substring(0, body.lastIndexOf(" "));
                        Log.d("PL_SMS_Receive_OTP", body);
                        setOtp(body);
                    }
                }
            };

            Platform.getInstance().registerReceiver(mIntentReceiver, intentFilter);
        });
    }

    @Override
    public void onDestroy() {
        if (mIntentReceiver != null)
            Platform.getInstance().unregisterReceiver(mIntentReceiver);
        super.onDestroy();
    }
}
