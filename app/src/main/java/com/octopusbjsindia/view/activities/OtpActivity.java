package com.octopusbjsindia.view.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.octopusbjsindia.R;
import com.octopusbjsindia.models.login.LoginInfo;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.view.fragments.NewOtpFragment;

public class OtpActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = OtpActivity.class.getName();
    private LoginInfo loginInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        initView();
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            loginInfo = bundle.getParcelable(Constants.Login.LOGIN_OTP_VERIFY_DATA);
        }

        try {
//            OtpFragment otpFragment = new OtpFragment();
            NewOtpFragment otpFragment = NewOtpFragment.newInstance(loginInfo);
            Bundle data = new Bundle();
            data.putParcelable(Constants.Login.LOGIN_OTP_VERIFY_DATA, loginInfo);
            otpFragment.setArguments(data);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.otp_form_container, otpFragment).commit();
        } catch (Exception e) {
            Log.e(TAG, "Exception :: OtpActivity : initView");
        }
    }

    @Override
    public void onClick(View view) {

    }
    public void killActivity() {
        finish();
        Log.e("killActivity", "killActivity called");
    }
}
