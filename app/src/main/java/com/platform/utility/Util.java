package com.platform.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.google.gson.Gson;
import com.platform.Platform;
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
                    "Bearer " + loginObj.getData().getAccessToken());
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

    private static Login getLoginObjectFromPref() {
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
}
