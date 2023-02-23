package com.octopusbjsindia.view.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.listeners.PlatformTaskListener;
import com.octopusbjsindia.models.login.Login;
import com.octopusbjsindia.models.login.LoginInfo;
import com.octopusbjsindia.presenter.LoginActivityPresenter;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Util;
import com.octopusbjsindia.widgets.PlatformEditTextView;

import static com.octopusbjsindia.utility.Util.getUserObjectFromPref;

public class LoginActivity extends BaseActivity implements PlatformTaskListener,
        View.OnClickListener, TextView.OnEditorActionListener {
    SharedPreferences preferences;
    private RequestOptions requestOptions;
    private final String TAG = LoginActivity.class.getSimpleName();
    private ProgressBar pbVerifyLogin;
    private RelativeLayout pbVerifyLoginLayout;

    private TextInputEditText etMobileNo;
    private TextInputLayout lytMobileNo;
    private ScrollView scrollView;
    TextView txtTermService;
    private ImageView img_logo;
    private LoginInfo loginInfo;
    private boolean doubleBackToExitPressedOnce = false;
    private LoginActivityPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Transparant);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = Platform.getInstance().getSharedPreferences(
                "AppData", Context.MODE_PRIVATE);
        initViews();
        loginInfo = new LoginInfo();
        loginPresenter = new LoginActivityPresenter(this);
    }

    private void initViews() {
        scrollView = findViewById(R.id.scrollView);
        etMobileNo = findViewById(R.id.et_mobile_no);
        lytMobileNo = findViewById(R.id.ly_mobile_no);

        pbVerifyLogin = findViewById(R.id.pb_login);
        pbVerifyLoginLayout = findViewById(R.id.login_progress_bar);

        txtTermService = findViewById(R.id.txtTermService);
        //img_logo  =findViewById(R.id.img_logo);
        findViewById(R.id.txtTermService).setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtTermService.setText(Html.fromHtml("By continuing you agree to the<br><a href=\"http://bjsindia.org/PrivacyPolicy.html\">Terms of Service and Privacy Policy</a>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtTermService.setText(Html.fromHtml("By continuing you agree to the<br><a href=\"http://bjsindia.org/PrivacyPolicy.html\">Terms of Service and Privacy Policy</a>"));
        }

        Button loginButton = findViewById(R.id.bt_login);
        loginButton.setOnClickListener(this);
       /* TextView resendOTP = findViewById(R.id.tv_resend_otp);
        resendOTP.setOnClickListener(this);*/
        if (Util.isFirstTimeLaunch(true)) {
            showLanguageSelectionDialog();
        }

        etMobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lytMobileNo.setError(null);
                lytMobileNo.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String number = editable.toString();
                if (number.length() == 10) {
                    Util.hideKeyboardWithView(LoginActivity.this, etMobileNo);
                    lytMobileNo.clearFocus();
                }
            }
        });


        /*if (getUserObjectFromPref() != null) {
            if (getUserObjectFromPref().getCurrent_project_logo() != null && !TextUtils.isEmpty(getUserObjectFromPref().getCurrent_project_logo())) {
                requestOptions = new RequestOptions().placeholder(R.drawable.login_hero_image);
                requestOptions = requestOptions.apply(RequestOptions.noTransformation());
                Glide.with(this)
                        .applyDefaultRequestOptions(requestOptions)
                        .load(getUserObjectFromPref().getCurrent_project_logo())
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(img_logo);
            } else {
                img_logo.setImageResource(R.drawable.login_hero_image);
            }
        } else {
            img_logo.setImageResource(R.drawable.login_hero_image);
        }*/

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
            }
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.back_string), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    private void showLanguageSelectionDialog() {
        AlertDialog languageSelectionDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.select_lang))
                .setCancelable(false)
                .setSingleChoiceItems(Constants.App.APP_LANGUAGE, 0, (dialogInterface, i) -> {
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
        if (etMobileNo != null) {
            loginInfo = new LoginInfo();
            loginInfo.setMobileNumber(String.valueOf(etMobileNo.getText()).trim());
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Constants.READ_PHONE_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addDeviceId();
            } else {
                Util.snackBarToShowMsg(this.getWindow().getDecorView()
                                .findViewById(android.R.id.content), "Please allow - Read Phone State permission.",
                        Snackbar.LENGTH_LONG);
            }
            return;
        }

    }

    @Override
    public <T> void showNextScreen(T data) {
        if (data != null && ((Login) data).getLoginData() != null) {
            loginInfo.setOneTimePassword(((Login) data).getLoginData().getOtp());
            addDeviceId();
            try {
                Intent intent = new Intent(this, OtpActivity.class);
                intent.putExtra(Constants.Login.LOGIN_OTP_VERIFY_DATA, loginInfo);
                startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "Exception :: LoginActivity : showNextScreen");
            }
        }
    }

    private void addDeviceId() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.
                TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details. else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        Constants.READ_PHONE_STORAGE);
            } else {
                @SuppressLint("HardwareIds")
                String deviceId = Settings.Secure.getString(this.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                loginInfo.setDeviceId(deviceId);
            }
        }
    }

    @Override
    public void showErrorMessage(String result) {
        Util.showToast(result, this);
    }

    private String getUserMobileNumber() {
        if (etMobileNo != null) {
            return String.valueOf(etMobileNo.getText());
        }
        return "";
    }

    private boolean isAllInputsValid() {
        boolean isInputValid = true;
        if (TextUtils.isEmpty(getUserMobileNumber())) {
            Util.setError(etMobileNo, getResources().getString(R.string.msg_mobile_number_is_empty));
            isInputValid = false;
        } else if (getUserMobileNumber().trim().length() < 10) {
            Util.setError(etMobileNo, getResources().getString(R.string.msg_mobile_number_is_invalid));
            isInputValid = false;
        }
        return isInputValid;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                /*if(Util.isConnected(this)) {
                    onLoginClick();
                }else{
                    Util.showToast(getString(R.string.msg_no_network), this);
                }*/

                if (etMobileNo.getText().toString().equals("")) {
                    lytMobileNo.setError("Please enter mobile number");
                } else if (etMobileNo.getText().toString().trim().length() != 10) {
                    lytMobileNo.setError("Please enter valid mobile number");
                } else {
                    if (Util.isConnected(this)) {
                        Util.hideKeyboardWithView(this, this.getCurrentFocus());
                        goToVerifyOtpScreen();
                    } else {
                        Util.showToast(getString(R.string.msg_no_network), this);
                    }
                }
                break;

           /* case R.id.tv_resend_otp:
                onResendOTPClick();
                break;*/

            case R.id.txtTermService:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://13.235.105.204/privacypolicy.html"));
                startActivity(browserIntent);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            Util.hideKeyboard(etMobileNo);
            if (isAllInputsValid()) {
                goToVerifyOtpScreen();
            }
            return true;
        }
        return false;
    }

    public void getdynamicLogo() {
    }
}
