package com.platform.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.platform.Platform;

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

    public static <T> boolean isSMSPermissionGranted(Activity context, T objectInstance) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                if (objectInstance instanceof Fragment) {
                    ((Fragment) objectInstance).requestPermissions(
                            new String[]{Manifest.permission.RECEIVE_SMS},
                            Constants.SMS_RECEIVE_REQUEST);
                } else {
                    ActivityCompat.requestPermissions(context,
                            new String[]{Manifest.permission.RECEIVE_SMS},
                            Constants.SMS_RECEIVE_REQUEST);
                }
                return false;
            }
        } else {
            return true;
        }
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

    public static Map<String, String> requestHeader() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json, text/plain, */*");
        headers.put("Content-Type", "application/json;charset=UTF-8");

        return headers;
    }

    public static String getTwoDigit(int i) {
        if (i < 10) {
            return "0" + i;
        }
        return "" + i;
    }
}
