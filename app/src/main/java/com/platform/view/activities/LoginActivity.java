package com.platform.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.platform.R;
import com.platform.listeners.LoginActivityListener;
import com.platform.models.login.LoginInfo;
import com.platform.controller.LoginActivityController;
import com.platform.utility.Util;
import com.platform.widgets.PlatformEditTextView;

public class LoginActivity extends BaseActivity implements LoginActivityListener {

    private ProgressBar pbVerifyLogin;
    private RelativeLayout pbVerifyLoginLayout;
    private PlatformEditTextView etUserMobileNumber;

    private LoginInfo loginInfo;
    private boolean doubleBackToExitPressedOnce = false;
    private LoginActivityController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        loginInfo = new LoginInfo();
        loginController = new LoginActivityController(this);
    }

    private void initViews() {
        pbVerifyLogin = findViewById(R.id.pb_login);
        pbVerifyLoginLayout = findViewById(R.id.login_progress_bar);
        etUserMobileNumber = findViewById(R.id.edt_mobile_number);

        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);

        TextSwitcher label = findViewById(R.id.enter_mobile_label);
        label.setFactory(factory);
        label.setInAnimation(in);
        label.setOutAnimation(out);
        label.setText(getResources().getString(R.string.msg_enter_mobile));

        Button loginButton = findViewById(R.id.btn_login);
        loginButton.setOnClickListener(view -> onLoginClick());

        TextView resendOTP = findViewById(R.id.tv_resend_otp);
        resendOTP.setOnClickListener(view -> onResendOTPClick());
    }

    final private ViewSwitcher.ViewFactory factory = () -> {
        // Create a new TextView
        TextView t = new TextView(getApplicationContext());
        t.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_Medium);
        t.setTextColor(getApplicationContext().getResources().getColor(R.color.black));
        return t;
    };

    @Override
    public void onStop() {
        super.onStop();
        hideProgressBar();

        if (loginController != null) {
            loginController.cancelNetworkRequest();
        }
    }

    @Override
    protected void onDestroy() {
        if (loginController != null) {
            loginController.detachView();
            loginController = null;
        }

        hideProgressBar();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Runtime.getRuntime().gc();
            startActivity(startMain);
            System.exit(0);

            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.back_string), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    private void onLoginClick() {
        if (isAllInputsValid()) {
            loginInfo.setMobileNumber(String.valueOf(etUserMobileNumber.getText()).trim());
            loginController.loginToSalesForce(loginInfo);
        }
    }

    private void onResendOTPClick() {

    }

    @Override
    public void showProgressBar() {
        if (pbVerifyLogin != null && pbVerifyLoginLayout.getVisibility() == View.GONE) {
            if (pbVerifyLogin != null && !pbVerifyLogin.isShown()) {
                pbVerifyLogin.setVisibility(View.VISIBLE);
                pbVerifyLoginLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void hideProgressBar() {
        if (pbVerifyLoginLayout != null && pbVerifyLoginLayout.getVisibility() == View.VISIBLE) {
            if (pbVerifyLogin != null && pbVerifyLogin.isShown()) {
                pbVerifyLogin.setVisibility(View.GONE);
                pbVerifyLoginLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean isAllInputsValid() {
        return validateAllInputs();
    }

    private String getUserMobileNumber() {
        return String.valueOf(etUserMobileNumber.getText());
    }

    private boolean validateAllInputs() {
        boolean isInputValid = true;
        if (getUserMobileNumber().length() == 0) {
            Util.setError(etUserMobileNumber, getResources().getString(R.string.msg_mobile_number_is_empty));
            isInputValid  = false;
        } else if (getUserMobileNumber().trim().length() < 10) {
            Util.setError(etUserMobileNumber, getResources().getString(R.string.msg_mobile_number_is_invalid));
            isInputValid  = false;
        }

        return isInputValid;
    }
}
