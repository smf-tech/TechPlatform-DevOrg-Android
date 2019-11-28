package com.platform.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.platform.R;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.AppSignatureHelper;
import com.platform.utility.Constants;
import com.platform.utility.PreferenceHelper;
import com.platform.utility.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    private final static int SPLASH_TIME_OUT = 2000;

    private final String TAG = SplashActivity.class.getName();
    PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferenceHelper = new PreferenceHelper(SplashActivity.this);
        SyncAdapterUtils.createSyncAccount(getApplicationContext());

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
                    intent = new Intent(SplashActivity.this, EditProfileActivity.class);
                } else {
                    if (Util.getUserObjectFromPref().getRoleCode()== Constants.SSModule.ROLE_CODE_SS_OPERATOR){
                         intent = new Intent(SplashActivity.this, OperatorMeterReadingActivity.class);
                        intent.putExtra("meetid","5d6f90c25dda765c2f0b5dd4");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }else {
                        intent = new Intent(SplashActivity.this, HomeActivity.class);
                    }
                }

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }, SPLASH_TIME_OUT);

        /*if (!preferenceHelper.getString(preferenceHelper.TOKEN).isEmpty()) {
            if (!preferenceHelper.getString(preferenceHelper.TOKEN).equalsIgnoreCase(Util.getUserObjectFromPref().getFirebaseId())) {
                checkAndUpdateFirebase();
            }
        }*/
    }

    /*public void checkAndUpdateFirebase() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("firebase_id", preferenceHelper.getString(preferenceHelper.TOKEN));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            Util.updateFirebaseIdRequests(jsonObject);
        }
    }*/

}
