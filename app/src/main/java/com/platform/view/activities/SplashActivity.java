package com.platform.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.platform.R;
import com.platform.utility.Util;

public class SplashActivity extends AppCompatActivity {

    private final static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Util.setApplicationLocale();

        new Handler().postDelayed(() -> {
            Intent intent;
            // Check user profile available or not
            if (Util.getUserObjectFromPref().getId() == null ||
                    Util.getUserObjectFromPref().getId().isEmpty()) {

                // Check user has registered mobile number or not
                if (Util.getLoginObjectFromPref().getLoginData() == null ||
                        Util.getLoginObjectFromPref().getLoginData().getAccessToken().isEmpty()) {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, ProfileActivity.class);
                }
            } else {
                intent = new Intent(SplashActivity.this, HomeActivity.class);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }, SPLASH_TIME_OUT);
    }
}
