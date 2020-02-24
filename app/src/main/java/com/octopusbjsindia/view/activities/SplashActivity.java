package com.octopusbjsindia.view.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.models.appconfig.AppConfigResponseModel;
import com.octopusbjsindia.models.content.Url;
import com.octopusbjsindia.models.notifications.NotificationData;
import com.octopusbjsindia.presenter.SplashActivityPresenter;
import com.octopusbjsindia.syncAdapter.SyncAdapterUtils;
import com.octopusbjsindia.utility.AppSignatureHelper;
import com.octopusbjsindia.utility.Constants;
import com.octopusbjsindia.utility.Permissions;
import com.octopusbjsindia.utility.PreferenceHelper;
import com.octopusbjsindia.utility.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.octopusbjsindia.utility.Util.getUserObjectFromPref;

public class SplashActivity extends AppCompatActivity {

    private RequestOptions requestOptions;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private final static int SPLASH_TIME_OUT = 3000;
    private final String TAG = SplashActivity.class.getName();
    PreferenceHelper preferenceHelper;
    private SplashActivityPresenter splashActivityPresenter;
    private TextView tv_powered,tv_app_version;
    private String appVersion = "";
    String toOpen;
    ImageView img_logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferences = Platform.getInstance().getSharedPreferences(
                "AppData", Context.MODE_PRIVATE);
        editor = Platform.getInstance().getSharedPreferences(
                "AppData", Context.MODE_PRIVATE).edit();

        tv_powered = findViewById(R.id.powered);
        tv_app_version  = findViewById(R.id.tv_app_version);
        img_logo  =findViewById(R.id.img_logo);
        //img_logo.setImageResource(R.drawable.ic_splash);

        if (getUserObjectFromPref() != null) {

            if (getUserObjectFromPref().getCurrent_project_logo() != null && !TextUtils.isEmpty(getUserObjectFromPref().getCurrent_project_logo())) {
                requestOptions = new RequestOptions().placeholder(R.drawable.ic_splash);
                requestOptions = requestOptions.apply(RequestOptions.noTransformation());
                Glide.with(this)
                        .applyDefaultRequestOptions(requestOptions)
                        .load(getUserObjectFromPref().getCurrent_project_logo())
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(img_logo);
            }else {
                img_logo.setImageResource(R.drawable.ic_splash);
            }
        }else {
            img_logo.setImageResource(R.drawable.ic_splash);
        }


        toOpen = getIntent().getStringExtra("toOpen");
        if(toOpen != null){
            Date crDate = Calendar.getInstance().getTime();
            String strDate = Util.getDateFromTimestamp(crDate.getTime(), Constants.FORM_DATE_FORMAT);
            NotificationData data = new NotificationData();
            data.setDateTime(strDate);
            data.setTitle(getIntent().getStringExtra("title"));
            data.setText(getIntent().getStringExtra("message"));
            data.setToOpen(toOpen);
            data.setUnread(false);
            DatabaseManager.getDBInstance(Platform.getInstance()).getNotificationDataDeo().insert(data);
        }

        try {
            appVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            tv_powered.setText("Powered By");
            tv_app_version.setText("Version -"+appVersion);



        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        preferenceHelper = new PreferenceHelper(SplashActivity.this);
        SyncAdapterUtils.createSyncAccount(getApplicationContext());
        splashActivityPresenter = new SplashActivityPresenter(SplashActivity.this);
        Util.setApplicationLocale();

        new AppSignatureHelper(this);
        if (Util.isConnected(SplashActivity.this)) {
            splashActivityPresenter.getAppConfig("");
        } else {
            GotoNextScreen();
        }
//        splashActivityPresenter.getAppConfig("");

    }

    public String getSize(Context context, Uri uri) {
        String fileSize = null;
        Cursor cursor = context.getContentResolver()
                .query(uri, null, null, null, null, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {

                // get file size
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (!cursor.isNull(sizeIndex)) {
                    fileSize = cursor.getString(sizeIndex);
                }
            }
        } finally {
            cursor.close();
        }
        return fileSize;
    }

    public void checkForceUpdate(String requestID, String message, int code) {
        //if (requestID.equalsIgnoreCase(presenter.GET_APP_CONFIG))
        {
            Log.e("RESPOSEN COFn", "@@@" + message);
            if (!TextUtils.isEmpty(message)) {

                try {
                    {
                        AppConfigResponseModel appConfigResponseModel
                                = new Gson().fromJson(message, AppConfigResponseModel.class);

                        editor.putString(Constants.OperatorModule.APP_CONFIG_RESPONSE,message);
                        editor.apply();

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
                            } else {
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

    public void GotoNextScreen() {
        new Handler().postDelayed(() -> {
            Intent intent;

            try {
                // Check user has registered mobile number or not
                if (Util.getLoginObjectFromPref() == null ||
                        Util.getLoginObjectFromPref().getLoginData() == null ||
                        Util.getUserObjectFromPref() == null ||
                        TextUtils.isEmpty(Util.getLoginObjectFromPref().getLoginData().getAccessToken())) {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                } else if (TextUtils.isEmpty(Util.getUserObjectFromPref().getId())||
                        TextUtils.isEmpty(Util.getUserObjectFromPref().getOrgId())) {
                    intent = new Intent(SplashActivity.this, EditProfileActivity.class);
                } else {
                    if (Util.getUserObjectFromPref().getRoleCode() == Constants.SSModule.ROLE_CODE_SS_OPERATOR) {
                        intent = new Intent(SplashActivity.this, OperatorMeterReadingActivity.class);
                        intent.putExtra("meetid", "5d6f90c25dda765c2f0b5dd4");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        intent = new Intent(SplashActivity.this, HomeActivity.class);
                        intent.putExtra("toOpen",toOpen);
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
