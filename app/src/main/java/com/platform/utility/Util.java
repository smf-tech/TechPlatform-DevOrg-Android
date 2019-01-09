package com.platform.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.platform.Platform;
import com.platform.R;
import com.platform.models.UserInfo;
import com.platform.models.login.Login;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Util {

    private static final String TAG = Util.class.getSimpleName();

    public static void setError(final EditText inputEditText, String errorMessage) {
        final int padding = 10;
        inputEditText.setCompoundDrawablePadding(padding);
        inputEditText.setError(errorMessage);
        inputEditText.requestFocus();

        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    inputEditText.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static boolean isFirstTimeLaunch(boolean flag) {
        SharedPreferences preferences = Platform.getInstance()
                .getSharedPreferences(Constants.App.FIRST_TIME_KEY, Context.MODE_PRIVATE);
        return preferences.getBoolean(Constants.App.FIRST_TIME_CODE, flag);
    }

    public static void setFirstTimeLaunch(boolean flag) {
        SharedPreferences preferences = Platform.getInstance()
                .getSharedPreferences(Constants.App.FIRST_TIME_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.App.FIRST_TIME_CODE, flag);
        editor.apply();
    }

    public static String getLocaleLanguageCode() {
        SharedPreferences preferences = Platform.getInstance()
                .getSharedPreferences(Constants.App.LANGUAGE_LOCALE, Context.MODE_PRIVATE);

        String languageCode = preferences.getString(Constants.App.LANGUAGE_CODE, "");
        Log.i(TAG, "App language code: " + languageCode);
        return languageCode;
    }

    public static void setLocaleLanguageCode(String languageCode) {
        // Set locale with selected language
        Locale locale;
        if ("en".equalsIgnoreCase(languageCode)) {
            locale = new Locale(languageCode, "US");
        } else {
            locale = new Locale(languageCode);
        }
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        // Save language code in db
        SharedPreferences preferences = Platform.getInstance()
                .getSharedPreferences(Constants.App.LANGUAGE_LOCALE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.App.LANGUAGE_CODE, languageCode);
        editor.apply();
    }

    public static void setApplicationLocale() {
        SharedPreferences preferences = Platform.getInstance()
                .getSharedPreferences(Constants.App.LANGUAGE_LOCALE, Context.MODE_PRIVATE);

        String languageCode = preferences.getString(Constants.App.LANGUAGE_CODE, "");
        if (languageCode.contentEquals("")) {
            setLocaleLanguageCode("en");
        } else {
            setLocaleLanguageCode(languageCode);
        }
    }

    public static Map<String, String> requestHeader(boolean isTokenPresent) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json, text/plain, */*");
        headers.put("Content-Type", "application/json;charset=UTF-8");

        if (isTokenPresent) {
            Login loginObj = getLoginObjectFromPref();
            headers.put(Constants.Login.AUTHORIZATION,
                    "Bearer " + loginObj.getLoginData().getAccessToken());
        }

        return headers;
    }

    public static String getTwoDigit(int i) {
        if (i < 10) {
            return "0" + i;
        }
        return "" + i;
    }

    public static void makeDirectory(String dir) {
        File tempDirectory = new File(dir);
        if (!tempDirectory.exists()) {
            boolean mkdirs = tempDirectory.mkdirs();
            Log.i(TAG, "" + mkdirs);
        }
    }

    private static String getFilePath(String fileName) {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + fileName;
    }

    public static Uri getUri(String fileName) {
        File file = new File(getFilePath(fileName));
        return Uri.fromFile(file);
    }

    public static String getUserMobileFromPref() {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences
                (Constants.App.APP_DATA, Context.MODE_PRIVATE);
        return preferences.getString(Constants.Login.USER_MOBILE_NO, "");
    }

    public static void saveUserMobileInPref(String phone) {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences(
                Constants.App.APP_DATA, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.Login.USER_MOBILE_NO, phone);
        editor.apply();
    }

    public static Login getLoginObjectFromPref() {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences
                (Constants.App.APP_DATA, Context.MODE_PRIVATE);
        String obj = preferences.getString(Constants.Login.LOGIN_OBJ, "{}");

        return new Gson().fromJson(obj, Login.class);
    }

    public static void saveLoginObjectInPref(String loginData) {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences(
                Constants.App.APP_DATA, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.Login.LOGIN_OBJ, loginData);
        editor.apply();
    }

    public static UserInfo getUserObjectFromPref() {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences
                (Constants.App.APP_DATA, Context.MODE_PRIVATE);
        String obj = preferences.getString(Constants.Login.USER_OBJ, "{}");

        return new Gson().fromJson(obj, UserInfo.class);
    }

    public static void saveUserObjectInPref(String userData) {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences(
                Constants.App.APP_DATA, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.Login.USER_OBJ, userData);
        editor.apply();
    }

    public static <T> void showToast(String msg, T context) {
        if (TextUtils.isEmpty(msg)) {
            msg = Platform.getInstance().getString(R.string.msg_something_went_wrong);
        }

        if (context instanceof Fragment) {
            Toast.makeText(((Fragment) context).getActivity(), msg, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(((Activity) context), msg, Toast.LENGTH_LONG).show();
        }
    }

    public static String getAppVersion() {
        String result = "";
        try {
            result = Platform.getInstance().getPackageManager()
                    .getPackageInfo(Platform.getInstance().getPackageName(), 0)
                    .versionName;
            result = result.replaceAll("[a-zA-Z]|-", "");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("TAG", e.getMessage());
        }

        return result;
    }
}
