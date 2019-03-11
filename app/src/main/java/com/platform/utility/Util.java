package com.platform.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.models.login.Login;
import com.platform.models.profile.OrganizationProjectsResponse;
import com.platform.models.profile.OrganizationResponse;
import com.platform.models.profile.OrganizationRolesResponse;
import com.platform.models.profile.UserLocation;
import com.platform.models.user.UserInfo;
import com.platform.view.activities.HomeActivity;

import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static com.platform.utility.Constants.DATE_FORMAT;
import static com.platform.utility.Constants.FORM_DATE_FORMAT;

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

        return preferences.getString(Constants.App.LANGUAGE_CODE, "");
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
            if (loginObj != null && loginObj.getLoginData() != null &&
                    loginObj.getLoginData().getAccessToken() != null) {
                headers.put(Constants.Login.AUTHORIZATION,
                        "Bearer " + loginObj.getLoginData().getAccessToken());
            }
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

    public static OrganizationResponse getUserOrgFromPref() {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences
                (Constants.App.APP_DATA, Context.MODE_PRIVATE);
        String obj = preferences.getString(Constants.Login.USER_ORG, "{}");

        return new Gson().fromJson(obj, OrganizationResponse.class);
    }

    public static void saveUserOrgInPref(String userData) {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences(
                Constants.App.APP_DATA, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.Login.USER_ORG, userData);
        editor.apply();
    }

    public static OrganizationRolesResponse getUserRoleFromPref() {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences
                (Constants.App.APP_DATA, Context.MODE_PRIVATE);
        String obj = preferences.getString(Constants.Login.USER_ROLE, "{}");

        return new Gson().fromJson(obj, OrganizationRolesResponse.class);
    }

    public static void saveUserRoleInPref(String userData) {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences(
                Constants.App.APP_DATA, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.Login.USER_ROLE, userData);
        editor.apply();
    }

    public static OrganizationProjectsResponse getUserProjectsFromPref() {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences
                (Constants.App.APP_DATA, Context.MODE_PRIVATE);
        String obj = preferences.getString(Constants.Login.USER_PROJECT, "{}");

        return new Gson().fromJson(obj, OrganizationProjectsResponse.class);
    }

    public static void saveUserProjectsInPref(String userData) {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences(
                Constants.App.APP_DATA, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.Login.USER_PROJECT, userData);
        editor.apply();
    }

//    public static UserLocation getUserLocationFromPref() {
//        SharedPreferences preferences = Platform.getInstance().getSharedPreferences
//                (Constants.App.APP_DATA, Context.MODE_PRIVATE);
//
//        String obj = preferences.getString(Constants.App.USER_LOC_OBJ, "{}");
//        return new Gson().fromJson(obj, UserLocation.class);
//    }

    public static void saveUserLocationInPref(UserLocation location) {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences(
                Constants.App.APP_DATA, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(location);
        editor.putString(Constants.App.USER_LOC_OBJ, json);
        editor.apply();
    }

//    public static void clearAllUserData() {
//        try {
//            SharedPreferences preferences = Platform.getInstance().getSharedPreferences
//                    (Constants.App.APP_DATA, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.clear();
//            editor.apply();
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
//        }
//    }

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

    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            return cm.getActiveNetworkInfo();
        }

        return null;
    }

    public static boolean isConnected(Context context) {
        NetworkInfo activeNetwork = getNetworkInfo(context);
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    public static void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                Platform.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static Long getDateInLong(String dateString) {
        if (TextUtils.isEmpty(dateString)) {
            return getDateInLong(new Date().toString());
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = sdf.parse(dateString);

            return date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0L;
    }

    public static String getLongDateInString(Long date, String dateFormat) {
        if (date != null) {
            try {
                Date d = new Timestamp(date);
                return getFormattedDate(d.toString(), dateFormat);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    private static String getFormattedDate(String date) {
        if (date == null || date.isEmpty()) {
            return getFormattedDate(new Date().toString());
        }

        try {
            DateFormat outputFormat = new SimpleDateFormat(Constants.LIST_DATE_FORMAT, Locale.US);
            DateFormat inputFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);

            Date date1 = inputFormat.parse(date);
            return outputFormat.format(date1);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return date;
    }

    public static String getFormattedDate(String date, String dateFormat) {
        if (date == null || date.isEmpty()) {
            return getFormattedDate(new Date().toString());
        }

        try {
            DateFormat outputFormat = new SimpleDateFormat(dateFormat, Locale.US);
            DateFormat inputFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);

            Date date1 = inputFormat.parse(date);
            return outputFormat.format(date1);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return date;
    }

    public static long getCurrentTimeStamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }

    public static String getDateFromTimestamp(Long date) {
        if (date != null) {
            try {
                Date d = new Timestamp(date);
                return getFormattedDate(d.toString(), FORM_DATE_FORMAT);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    public static void launchFragment(Fragment fragment, Context context, String titleName) {
        try {
            Bundle b = new Bundle();
            b.putSerializable("TITLE", titleName);
            b.putBoolean("SHOW_ALL", false);
            fragment.setArguments(b);

            FragmentTransaction fragmentTransaction = ((HomeActivity) Objects
                    .requireNonNull(context))
                    .getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.home_page_container, fragment, titleName);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void removeDatabaseRecords(final boolean refreshData) {
        DatabaseManager.getDBInstance(Platform.getInstance()).deleteAllProcesses();
        DatabaseManager.getDBInstance(Platform.getInstance()).deleteAllModules();
        DatabaseManager.getDBInstance(Platform.getInstance()).deleteAllReports();

        if (refreshData) {
            DatabaseManager.getDBInstance(Platform.getInstance()).deleteAllSyncedFormResults();
        } else {
            DatabaseManager.getDBInstance(Platform.getInstance()).deleteAllFormSchema();
            DatabaseManager.getDBInstance(Platform.getInstance()).deleteAllFormResults();
        }
    }

    public static int getUnreadNotificationsCount() {
        SharedPreferences preferences = Platform.getInstance()
                .getSharedPreferences(Constants.App.UNREAD_NOTIFICATION_COUNT, Context.MODE_PRIVATE);

        return preferences.getInt(Constants.App.UNREAD_NOTIFICATION_COUNT, 0);
    }

    public static void updateNotificationsCount(boolean clearNotifications) {
        SharedPreferences preferences = Platform.getInstance()
                .getSharedPreferences(Constants.App.UNREAD_NOTIFICATION_COUNT, Context.MODE_PRIVATE);

        int count = preferences.getInt(Constants.App.UNREAD_NOTIFICATION_COUNT, 0);

        SharedPreferences.Editor editor = preferences.edit();
        if (clearNotifications) {
            editor.putInt(Constants.App.UNREAD_NOTIFICATION_COUNT, 0);
        } else {
            editor.putInt(Constants.App.UNREAD_NOTIFICATION_COUNT, ++count);
        }
        editor.apply();
    }
}
