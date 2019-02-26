package com.platform.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.platform.R;
import com.platform.utility.AppSignatureHelper;
import com.platform.utility.Util;

public class SplashActivity extends AppCompatActivity {

    private final static int SPLASH_TIME_OUT = 2000;
    private final String TAG = SplashActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Util.setApplicationLocale();

        new AppSignatureHelper(this);

        new Handler().postDelayed(() -> {
            Intent intent;

            try {
                // Check user has registered mobile number or not
                if (Util.getLoginObjectFromPref() == null ||
                        Util.getLoginObjectFromPref().getLoginData() == null ||
                        TextUtils.isEmpty(Util.getLoginObjectFromPref().getLoginData().getAccessToken())) {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                } else if (TextUtils.isEmpty(Util.getUserObjectFromPref().getId())) {
                    intent = new Intent(SplashActivity.this, ProfileActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, HomeActivity.class);
                }

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }, SPLASH_TIME_OUT);
    }
}
