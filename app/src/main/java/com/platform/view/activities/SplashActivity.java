package com.platform.view.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.utils.Utils;
import com.google.gson.Gson;
import com.platform.R;
import com.platform.models.appconfig.AppConfigResponseModel;
import com.platform.presenter.SplashActivityPresenter;
import com.platform.syncAdapter.SyncAdapterUtils;
import com.platform.utility.AppSignatureHelper;
import com.platform.utility.Constants;
import com.platform.utility.PreferenceHelper;
import com.platform.utility.Util;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {
    private SplashActivityPresenter splashActivityPresenter;
    private final static int SPLASH_TIME_OUT = 2000;

    private final String TAG = SplashActivity.class.getName();
    PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferenceHelper = new PreferenceHelper(SplashActivity.this);
        SyncAdapterUtils.createSyncAccount(getApplicationContext());
        splashActivityPresenter = new SplashActivityPresenter(SplashActivity.this);
        Util.setApplicationLocale();

        new AppSignatureHelper(this);
        if (Util.isConnected(SplashActivity.this)){
            splashActivityPresenter.getAppConfig("");
        }else {
            GotoNextScreen();
        }
        splashActivityPresenter.getAppConfig("");
/*        new Handler().postDelayed(() -> {
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
        }, SPLASH_TIME_OUT);*/

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


    public void checkForceUpdate(String requestID, String message, int code) {
        //if (requestID.equalsIgnoreCase(presenter.GET_APP_CONFIG))
        {
            Log.e("RESPOSEN COFn", "@@@" + message);
            if (!TextUtils.isEmpty(message)) {

                try {
                    {
                        AppConfigResponseModel appConfigResponseModel
                                = new Gson().fromJson(message, AppConfigResponseModel.class);
                        Gson gson = new Gson();
                        //Utils.setStringPref(Constants.Pref.PROFILE_CATEGORY, gson.toJson(appConfigResponseModel));

                        //String str = Utils.getStringPref(Constants.Pref.PROFILE_CATEGORY);

                        if (appConfigResponseModel != null && appConfigResponseModel.getAppConfigResponse() != null) {
//                            {
//                                {
                            String currentVersion = appConfigResponseModel.getAppConfigResponse().getAppUpdate().getOctopusAppVersion();
                            String appVersion = "";
                            try {
                                appVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }


                            float appV = 0, currentV = 0;
                            if (appVersion != null && appVersion.length() != 0)
                                appV = Float.parseFloat(appVersion);
                            if (currentVersion != null && currentVersion.length() != 0)
                                currentV = Float.parseFloat(currentVersion);
                            if (appV < currentV) {
                                if (appConfigResponseModel.getAppConfigResponse().getAppUpdate().getOctopusForceUpdateRequired()) {
                                    showForceupdateDialog(SplashActivity.this, 1);
                                } else {
                                    showForceupdateDialog(SplashActivity.this, 0);
                                }
                            }else {
                                GotoNextScreen();
                            }
//                                }
//                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String showForceupdateDialog(final Activity context, int pos) {

        Dialog dialog;
        Button btnSubmit, btn_cancel;
        TextView edt_reason;
        Activity activity = context;

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_forceupdate_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        edt_reason = dialog.findViewById(R.id.tv_reason);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btnSubmit = dialog.findViewById(R.id.btn_submit);
        if (pos == 1) {
            btn_cancel.setVisibility(View.GONE);
            dialog.setCancelable(false);
        } else {
            dialog.setCancelable(true);
        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                GotoNextScreen();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   /*Intent loginIntent = new Intent(context, LoginActivity.class);
                   loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   context.startActivity(loginIntent);*/
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                finish();
            }
        });
        dialog.show();
        return "";
    }

    public void GotoNextScreen(){
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
    }
}
