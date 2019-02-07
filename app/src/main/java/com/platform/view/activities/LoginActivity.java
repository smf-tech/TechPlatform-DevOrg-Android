package com.platform.view.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.platform.R;
import com.platform.listeners.PlatformTaskListener;
import com.platform.models.login.Login;
import com.platform.models.login.LoginInfo;
import com.platform.presenter.LoginActivityPresenter;
import com.platform.utility.Constants;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.Util;
import com.platform.widgets.PlatformEditTextView;

public class LoginActivity extends BaseActivity implements PlatformTaskListener,
        View.OnClickListener, TextView.OnEditorActionListener {

    private final String TAG = LoginActivity.class.getSimpleName();

    private ProgressBar pbVerifyLogin;
    private RelativeLayout pbVerifyLoginLayout;
    private PlatformEditTextView etUserMobileNumber;

    private LoginInfo loginInfo;
    private boolean doubleBackToExitPressedOnce = false;
    private LoginActivityPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SyncAdapterUtils.createSyncAccount(this);

        initViews();

        loginInfo = new LoginInfo();
        loginPresenter = new LoginActivityPresenter(this);
    }

    private void initViews() {
        pbVerifyLogin = findViewById(R.id.pb_login);
        pbVerifyLoginLayout = findViewById(R.id.login_progress_bar);

        etUserMobileNumber = findViewById(R.id.edt_mobile_number);
        etUserMobileNumber.setOnEditorActionListener(this);

        TextView label = findViewById(R.id.enter_mobile_label);
        label.setText(getResources().getString(R.string.msg_enter_mobile));

        Button loginButton = findViewById(R.id.btn_login);
        loginButton.setOnClickListener(this);

        TextView resendOTP = findViewById(R.id.tv_resend_otp);
        resendOTP.setOnClickListener(this);

        if (Util.isFirstTimeLaunch(true)) {
            showLanguageSelectionDialog();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressBar();
    }

    @Override
    protected void onDestroy() {
        if (loginPresenter != null) {
            loginPresenter.detachView();
            loginPresenter = null;
        }

        hideProgressBar();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

            try {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startMain);
                System.exit(0);
            } catch (Exception e) {
                Log.e(TAG, "Exception :: LoginActivity : onBackPressed");
                e.printStackTrace();
            }

            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.back_string), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    private void showLanguageSelectionDialog() {
        final String[] items = {"English", "मराठी", "हिंदी"};

        AlertDialog languageSelectionDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.select_lang))
                .setCancelable(false)
                .setSingleChoiceItems(items, 0, (dialogInterface, i) -> {
                })
                .setPositiveButton(R.string.ok, (dialog, id) -> {

                    ListView listView = ((AlertDialog) dialog).getListView();
                    switch (listView.getCheckedItemPosition()) {
                        case 0:
                            Util.setLocaleLanguageCode(Constants.App.LANGUAGE_ENGLISH);
                            break;

                        case 1:
                            Util.setLocaleLanguageCode(Constants.App.LANGUAGE_MARATHI);
                            break;

                        case 2:
                            Util.setLocaleLanguageCode(Constants.App.LANGUAGE_HINDI);
                            break;
                    }

                    Util.setFirstTimeLaunch(false);
                    dialog.dismiss();
                    finish();
                    startActivity(getIntent());
                }).create();

        languageSelectionDialog.show();
    }

    private void onLoginClick() {
        if (isAllInputsValid()) {
            goToVerifyOtpScreen();
        }
    }

    private void goToVerifyOtpScreen() {
        if (etUserMobileNumber != null) {
            loginInfo = new LoginInfo();
            loginInfo.setMobileNumber(String.valueOf(etUserMobileNumber.getText()).trim());
            loginPresenter.getOtp(loginInfo);
        }
    }

    private void onResendOTPClick() {
        Log.i(TAG, "Handle resend OTP event");
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
    public <T> void showNextScreen(T data) {
        if (data != null && ((Login) data).getLoginData() != null) {
            loginInfo.setOneTimePassword(String.valueOf(((Login) data).getLoginData().getOtp()));

            try {
                Intent intent = new Intent(this, OtpActivity.class);
                intent.putExtra(Constants.Login.LOGIN_OTP_VERIFY_DATA, loginInfo);
                startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "Exception :: LoginActivity : showNextScreen");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showErrorMessage(String result) {
        Util.showToast(result, this);
    }

    private String getUserMobileNumber() {
        if (etUserMobileNumber != null) {
            return String.valueOf(etUserMobileNumber.getText());
        }

        return "";
    }

    private boolean isAllInputsValid() {
        boolean isInputValid = true;
        if (TextUtils.isEmpty(getUserMobileNumber())) {
            Util.setError(etUserMobileNumber, getResources().getString(R.string.msg_mobile_number_is_empty));
            isInputValid = false;
        } else if (getUserMobileNumber().trim().length() < 10) {
            Util.setError(etUserMobileNumber, getResources().getString(R.string.msg_mobile_number_is_invalid));
            isInputValid = false;
        }

        return isInputValid;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                onLoginClick();
                break;

            case R.id.tv_resend_otp:
                onResendOTPClick();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            Util.hideKeyboard (etUserMobileNumber);

            if (isAllInputsValid()) {
                goToVerifyOtpScreen();
            }
            return true;
        }
        return false;
    }
}
