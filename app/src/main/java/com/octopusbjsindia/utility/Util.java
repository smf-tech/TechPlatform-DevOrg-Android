package com.octopusbjsindia.utility;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.octopusbjsindia.BuildConfig;
import com.octopusbjsindia.Platform;
import com.octopusbjsindia.R;
import com.octopusbjsindia.database.DatabaseManager;
import com.octopusbjsindia.models.forms.FormResult;
import com.octopusbjsindia.models.home.RoleAccessAPIResponse;
import com.octopusbjsindia.models.login.Login;
import com.octopusbjsindia.models.pm.ProcessData;
import com.octopusbjsindia.models.profile.OrganizationProjectsResponse;
import com.octopusbjsindia.models.profile.OrganizationResponse;
import com.octopusbjsindia.models.profile.OrganizationRolesResponse;
import com.octopusbjsindia.models.profile.UserLocation;
import com.octopusbjsindia.models.tm.TMApprovalRequestModel;
import com.octopusbjsindia.models.user.UserInfo;
import com.octopusbjsindia.view.activities.HomeActivity;
import com.octopusbjsindia.view.activities.LoginActivity;
import com.octopusbjsindia.view.fragments.HomeFragment;
import com.octopusbjsindia.view.fragments.PlannerFragment;
import com.octopusbjsindia.view.fragments.ReportsFragment;
import com.octopusbjsindia.view.fragments.TMUserAttendanceApprovalFragment;
import com.octopusbjsindia.view.fragments.TMUserFormsApprovalFragment;
import com.octopusbjsindia.view.fragments.TMUserLeavesApprovalFragment;
import com.octopusbjsindia.view.fragments.TMUserProfileApprovalFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.octopusbjsindia.utility.Constants.DATE_FORMAT;
import static com.octopusbjsindia.utility.Constants.DATE_TIME_FORMAT;
import static com.octopusbjsindia.utility.Constants.DAY_MONTH_YEAR;
import static com.octopusbjsindia.utility.Constants.FORM_DATE_FORMAT;

public class Util {
    private static ProgressDialog mProgressDialog;
    private static final String TAG = Util.class.getSimpleName();
    private String todayAsString;
    private ProgressDialog pd;

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
        headers.put("deviceId", getStringFromPref(Constants.App.deviceId));
//        headers.put("orgId", Util.getUserObjectFromPref().getOrgId());

        if (isTokenPresent) {
            Login loginObj = getLoginObjectFromPref();
            if (loginObj != null && loginObj.getLoginData() != null &&
                    loginObj.getLoginData().getAccessToken() != null) {
                headers.put(Constants.Login.AUTHORIZATION,
                        "Bearer " + loginObj.getLoginData().getAccessToken());
                if (getUserObjectFromPref() != null) {
                    if (getUserObjectFromPref().getOrgId() != null) {
                        headers.put("orgId", getUserObjectFromPref().getOrgId());
                    }
                    if (getUserObjectFromPref().getProjectIds() != null) {
                        headers.put("projectId", getUserObjectFromPref().getProjectIds().get(0).getId());
                    }
                    if (getUserObjectFromPref().getRoleIds() != null) {
                        headers.put("roleId", getUserObjectFromPref().getRoleIds());
                    }
                }
                headers.put("versionName",getAppVersion());
            }
        }

        return headers;
    }

    public static Map<String, String> requestCustomHeader(boolean isTokenPresent, String orgId,
                                                          String projectId, String roleId) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json, text/plain, */*");
        headers.put("Content-Type", "application/json;charset=UTF-8");
//        headers.put("orgId", Util.getUserObjectFromPref().getOrgId());

        if (isTokenPresent) {
            Login loginObj = getLoginObjectFromPref();
            if (loginObj != null && loginObj.getLoginData() != null &&
                    loginObj.getLoginData().getAccessToken() != null) {
                headers.put(Constants.Login.AUTHORIZATION,
                        "Bearer " + loginObj.getLoginData().getAccessToken());
                headers.put("orgId", orgId);
                headers.put("projectId", projectId);
                headers.put("roleId", roleId);
                headers.put("versionName", getAppVersion());
                headers.put("deviceId", getStringFromPref(Constants.App.deviceId));
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

    public static String getIsDeviceMatchFromPref() {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences
                (Constants.App.APP_DATA, Context.MODE_PRIVATE);
        return preferences.getString(Constants.Login.USER_DEVICE_MATCH, "");
    }

    public static void saveIsDeviceMatchInPref(String isDeviceMatch) {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences(
                Constants.App.APP_DATA, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.Login.USER_DEVICE_MATCH, isDeviceMatch);
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

    public static RoleAccessAPIResponse getRoleAccessObjectFromPref() {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences
                (Constants.App.APP_DATA, Context.MODE_PRIVATE);
        String obj = preferences.getString(Constants.Login.USER_ROLE_ACCESS_OBJ, "{}");

        return new Gson().fromJson(obj, RoleAccessAPIResponse.class);
    }

    public static void saveRoleAccessObjectInPref(String roleAccessData) {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences(
                Constants.App.APP_DATA, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.Login.USER_ROLE_ACCESS_OBJ, roleAccessData);
        editor.apply();
    }

    public static void logOutUser(Activity activity) {
        // remove user related shared pref data
//        Util.saveLoginObjectInPref("");
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences(
                Constants.App.APP_DATA, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(Constants.Login.USER_OBJ);
        editor.remove(Constants.Login.LOGIN_OBJ);
        editor.apply();
        try {
            Intent startMain = new Intent(activity, LoginActivity.class);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(startMain);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
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

    public static OrganizationRolesResponse getUserRoleFromPref(String orgId) {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences
                (Constants.Login.USER_ROLE, Context.MODE_PRIVATE);
        String obj = preferences.getString(orgId, "{}");

        return new Gson().fromJson(obj, OrganizationRolesResponse.class);
    }

    public static void saveUserRoleInPref(String orgId, String userData) {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences(
                Constants.Login.USER_ROLE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(orgId, userData);
        editor.apply();
    }

    public static OrganizationProjectsResponse getUserProjectsFromPref(String orgId) {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences
                (Constants.Login.USER_PROJECT, Context.MODE_PRIVATE);
        String obj = preferences.getString(orgId, "{}");

        return new Gson().fromJson(obj, OrganizationProjectsResponse.class);
    }

    public static void saveUserProjectsInPref(String orgId, String userData) {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences(
                Constants.Login.USER_PROJECT, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(orgId, userData);
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

    private static void clearAllUserRoleData() {
        try {
            SharedPreferences preferences = Platform.getInstance().getSharedPreferences
                    (Constants.Login.USER_ROLE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void setStringInPref(String key, String value) {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences(
                Constants.App.APP_DATA, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringFromPref(String key) {
        SharedPreferences preferences = Platform.getInstance().getSharedPreferences
                (Constants.App.APP_DATA, Context.MODE_PRIVATE);
        String value = preferences.getString(key, "");

        return value;
    }

    public static <T> void showToast(String msg, T context) {
        try {
            if (TextUtils.isEmpty(msg)) {
                msg = Platform.getInstance().getString(R.string.msg_something_went_wrong);
            }

            if (msg.contains("ConnectException")) {
                msg = Platform.getInstance().getString(R.string.msg_no_network);
            }

            if (context instanceof Fragment) {
                Toast.makeText(((Fragment) context).getActivity(), msg, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(((Activity) context), msg, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void logger(String tag, String msg) {
        Log.e(tag, "@@@@@2" + msg);
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
            SimpleDateFormat sdf = new SimpleDateFormat(Constants.FORM_DATE, Locale.getDefault());
            Date date = sdf.parse(dateString);
            return date.getTime();

        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }

        return 0L;
    }

    public static long getDateInepoch(String dateString) {
        if (TextUtils.isEmpty(dateString)) {
            return getDateInepoch(new Date().toString());
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DAY_MONTH_YEAR, Locale.getDefault());
            Date date = sdf.parse(dateString);
            long epoch = date.getTime();
            int test = (int) (epoch / 1000);
            return epoch;

        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }

        return 0;
    }

    public static String getLongDateInString(long date, String dateFormat) {
        if (date > 0) {
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
            DateFormat outputFormat = new SimpleDateFormat(Constants.LIST_DATE_FORMAT, Locale.getDefault());
            DateFormat inputFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

            Date date1 = inputFormat.parse(date);
            return outputFormat.format(date1);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return date;
    }

    private static String getFormattedDate(String date, String dateFormat) {
        if (date == null || date.isEmpty()) {
            return getFormattedDate(new Date().toString());
        }

        try {
            String strLocale = getLocaleLanguageCode();
            Locale.setDefault(new Locale(strLocale));
            DateFormat outputFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
            DateFormat inputFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

            Date date1 = inputFormat.parse(date);
            return String.format(Locale.getDefault(), "%s", outputFormat.format(date1));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return String.format(Locale.getDefault(), "%s", date);
    }

    public static long getCurrentTimeStamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }

    public static String getCurrentDate() {

        String currentDateString = new SimpleDateFormat(DAY_MONTH_YEAR).format(new Date());

        return currentDateString;
    }

    public static String getCurrentDatePreviousMonth() {

        SimpleDateFormat format = new SimpleDateFormat(DAY_MONTH_YEAR);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        System.out.println(format.format(cal.getTime()));
        String currentDateString = format.format(cal.getTime());

        return currentDateString;
    }

    public static String getDateTimeFromTimestamp(long date) {
        if (date > 0) {
            try {
                int length = (int) (Math.log10(date) + 1);
                if (length == 10) {
                    date = date * 1000;
                }
                Date d = new Timestamp(date);
                return getFormattedDate(d.toString(), FORM_DATE_FORMAT);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    public static Long dateTimeToTimeStamp(String strDate, String strTime) {
        Date date;
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
        try {
            date = formatter.parse(strDate + " " + strTime);
            return date.getTime();
        } catch (ParseException e) {
            Log.e("TAG", e.getMessage());
        }

        return 0L;
    }


    public static String getDateFromTimestamp(Long timeStamp, String dateTimeFormat) {
        if (timeStamp > 0) {
            try {
                int length = (int) (Math.log10(timeStamp) + 1);
                if (length == 10) {
                    timeStamp = timeStamp * 1000;
                }
                Date d = new Timestamp(timeStamp);

                return getFormattedDate(d.toString(), dateTimeFormat);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    public static String getAmPmTimeStringFromTimeString(String time) {

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            final Date dateObj = sdf.parse(time);
            return new SimpleDateFormat("hh:mm aaa").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    private int getDaysBetween(String start, String end) {
        int days = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);

            long diff = endDate.getTime() - startDate.getTime();
            days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }

    public static void launchFragment(Fragment fragment, Context context, String titleName,
                                      final boolean addToBackStack) {
        try {
            Bundle b = new Bundle();
            b.putSerializable("TITLE", titleName);
            b.putBoolean("SHOW_ALL", false);
            if (fragment instanceof HomeFragment || fragment instanceof PlannerFragment || fragment instanceof ReportsFragment) {
                b.putBoolean("SHOW_BACK", false);
            } else {
                b.putBoolean("SHOW_BACK", true);
            }
            fragment.setArguments(b);

            FragmentTransaction fragmentTransaction = ((HomeActivity) Objects
                    .requireNonNull(context))
                    .getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.home_page_container, fragment, titleName);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            if (addToBackStack)
                fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void removeDatabaseRecords(final boolean refreshData) {
        clearAllUserRoleData();
        DatabaseManager.getDBInstance(Platform.getInstance()).deleteAllProcesses();
        DatabaseManager.getDBInstance(Platform.getInstance()).deleteAllModules();
        DatabaseManager.getDBInstance(Platform.getInstance()).deleteAllReports();
        DatabaseManager.getDBInstance(Platform.getInstance()).deleteAllFormSchema();
        DatabaseManager.getDBInstance(Platform.getInstance()).getNotificationDataDeo().deleteAllNotifications();

        deleteCache(Platform.getInstance());

        if (refreshData) {
            DatabaseManager.getDBInstance(Platform.getInstance()).deleteAllSyncedFormResults();
        } else {
            DatabaseManager.getDBInstance(Platform.getInstance()).deleteAllFormResults();
            DatabaseManager.getDBInstance(Platform.getInstance()).getAttendaceSchema().deleteAllAttendance();
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

    public static boolean isSubmittedFormsLoaded() {
        SharedPreferences preferences = Platform.getInstance()
                .getSharedPreferences(Constants.Form.GET_SUBMITTED_FORMS_FIRST_TIME, Context.MODE_PRIVATE);

        return preferences.getBoolean(Constants.Form.GET_SUBMITTED_FORMS_FIRST_TIME, false);
    }

    public static void setSubmittedFormsLoaded(final boolean loaded) {
        SharedPreferences preferences = Platform.getInstance()
                .getSharedPreferences(Constants.Form.GET_SUBMITTED_FORMS_FIRST_TIME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.Form.GET_SUBMITTED_FORMS_FIRST_TIME, loaded);
        editor.apply();
    }

    public static List<FormResult> sortFormResultListByCreatedDate(final List<FormResult> savedForms) {
        Collections.sort(savedForms, (o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));

        return savedForms;
    }

    public static void sortProcessDataListByCreatedDate(final List<ProcessData> savedForms) {
        Collections.sort(savedForms, (o1, o2) -> {
            if (o2.getMicroservice().getUpdatedAt() != null && o1.getMicroservice().getUpdatedAt() != null)
                return o2.getMicroservice().getUpdatedAt().compareTo(o1.getMicroservice().getUpdatedAt());

            return 0;
        });
    }

    public static String encrypt(String rowString) {
        try {
            String encodedStr = Base64.encodeToString(rowString.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
            return encodedStr.replace("\n", "");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return "";
    }

    public static void rotateImage(float degree, ImageView image) {
        final RotateAnimation rotateAnim = new RotateAnimation(0.0f, degree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setDuration(0);
        rotateAnim.setFillAfter(true);
        image.startAnimation(rotateAnim);
    }

    public static String writeToInternalStorage(Context context, String fileName, String content) {
        File file = new File(context.getCacheDir(), fileName + Constants.App.FILE_EXTENSION);

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(content.getBytes());
            outputStream.close();
            return file.getPath();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return "";
    }

    public static String readFromInternalStorage(Context context, String fileName) {
        File cacheDir = context.getCacheDir();
        File tempFile = new File(cacheDir.getPath() + "/" + fileName + Constants.App.FILE_EXTENSION);

        String nextLine;
        StringBuilder completeText = new StringBuilder();

        try (FileReader fReader = new FileReader(tempFile); BufferedReader bReader = new BufferedReader(fReader)) {
            while ((nextLine = bReader.readLine()) != null) {
                completeText.append(nextLine).append("\n");
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        return completeText.toString();
    }

    private static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static String setFieldAsMandatory(boolean isRequired) {
        return (isRequired ? " *" : "");
    }

    public static void setInputType(Context context, String type, EditText textInputField) {
        if (!TextUtils.isEmpty(type)) {
            switch (type) {
                case Constants.FormInputType.INPUT_TYPE_DATE:
                    textInputField.setFocusable(false);
                    textInputField.setClickable(false);
                    textInputField.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
                    textInputField.setOnClickListener(
                            view -> showDateDialog(context, textInputField));
                    break;

                case Constants.FormInputType.INPUT_TYPE_TIME:
                    textInputField.setFocusable(false);
                    textInputField.setClickable(false);
                    textInputField.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
                    textInputField.setOnClickListener(
                            view -> showTimeDialog(context, textInputField));
                    break;

                case Constants.FormInputType.INPUT_TYPE_TELEPHONE:
                    textInputField.setInputType(InputType.TYPE_CLASS_NUMBER);
                    break;

                case Constants.FormInputType.INPUT_TYPE_NUMERIC:
                case Constants.FormInputType.INPUT_TYPE_NUMBER:
                case Constants.FormInputType.INPUT_TYPE_DECIMAL:
                    textInputField.setInputType(InputType.TYPE_CLASS_NUMBER |
                            InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    break;

                case Constants.FormInputType.INPUT_TYPE_ALPHABETS:
                case Constants.FormInputType.INPUT_TYPE_TEXT:
                    textInputField.setMaxLines(3);
                    textInputField.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean isStartDateIsBeforeEndDate(String startDate, String endDate) {
        try {
            DateFormat formatter;
            Date fromDate, toDate;
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            fromDate = formatter.parse(startDate);
            toDate = formatter.parse(endDate);

            if (fromDate.before(toDate) || fromDate.equals(toDate)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void showAllDateDialog(Context context, final EditText editText) {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog
                = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {

            String date = String.format(Locale.getDefault(), "%s", year) + "-" +
                    String.format(Locale.getDefault(), "%s", Util.getTwoDigit(monthOfYear + 1)) + "-" +
                    String.format(Locale.getDefault(), "%s", Util.getTwoDigit(dayOfMonth));

            editText.setText(date);
        }, mYear, mMonth, mDay);

        dateDialog.setTitle(context.getString(R.string.select_date_title));
        dateDialog.show();
    }

    public static void showDateDialogMin(Context context, final EditText editText) {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog
                = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {

            String date = String.format(Locale.getDefault(), "%s", year) + "-" +
                    String.format(Locale.getDefault(), "%s", Util.getTwoDigit(monthOfYear + 1)) + "-" +
                    String.format(Locale.getDefault(), "%s", Util.getTwoDigit(dayOfMonth));

            editText.setText(date);
        }, mYear, mMonth, mDay);

        dateDialog.setTitle(context.getString(R.string.select_date_title));
        dateDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dateDialog.show();
    }


    public static void showDateDialog(Context context, final EditText editText) {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dateDialog
                = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {

            String date = String.format(Locale.getDefault(), "%s", year) + "-" +
                    String.format(Locale.getDefault(), "%s", Util.getTwoDigit(monthOfYear + 1)) + "-" +
                    String.format(Locale.getDefault(), "%s", Util.getTwoDigit(dayOfMonth));

            editText.setText(date);
        }, mYear, mMonth, mDay);

        dateDialog.setTitle(context.getString(R.string.select_date_title));
        dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dateDialog.show();
    }

    public static void showDateDialogEnableBeforeDefinedDate(Context context, final EditText editText, String definedDate) {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        long definedDateLong = getDateInLong(definedDate);
        DatePickerDialog dateDialog
                = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {

            String date = String.format(Locale.getDefault(), "%s", year) + "-" +
                    String.format(Locale.getDefault(), "%s", Util.getTwoDigit(monthOfYear + 1)) + "-" +
                    String.format(Locale.getDefault(), "%s", Util.getTwoDigit(dayOfMonth));

            editText.setText(date);
        }, mYear, mMonth, mDay);

        dateDialog.setTitle(context.getString(R.string.select_date_title));
        dateDialog.getDatePicker().setMaxDate(definedDateLong);
        dateDialog.show();
    }

    public static void showDateDialogEnableBetweenMinMax(Context context, final EditText editText, String minDate, String maxDate) {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        long minDateLong = getDateInLong(minDate);
        long maxDateLong = getDateInLong(maxDate);
        DatePickerDialog dateDialog
                = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {

            String date = String.format(Locale.getDefault(), "%s", year) + "-" +
                    String.format(Locale.getDefault(), "%s", Util.getTwoDigit(monthOfYear + 1)) + "-" +
                    String.format(Locale.getDefault(), "%s", Util.getTwoDigit(dayOfMonth));

            editText.setText(date);
        }, mYear, mMonth, mDay);

        dateDialog.setTitle(context.getString(R.string.select_date_title));
        dateDialog.getDatePicker().setMinDate(minDateLong);
        dateDialog.getDatePicker().setMaxDate(maxDateLong);
        dateDialog.show();
    }

    public static void showDateDialogEnableAfterMin(Context context, final EditText editText, String minDate) {
        final Calendar c = Calendar.getInstance();
        final int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        final int mDay = c.get(Calendar.DAY_OF_MONTH);

        long minDateLong = getDateInLong(minDate);
        DatePickerDialog dateDialog
                = new DatePickerDialog(context, (view, year, monthOfYear, dayOfMonth) -> {

            String date = String.format(Locale.getDefault(), "%s", year) + "-" +
                    String.format(Locale.getDefault(), "%s", Util.getTwoDigit(monthOfYear + 1)) + "-" +
                    String.format(Locale.getDefault(), "%s", Util.getTwoDigit(dayOfMonth));

            editText.setText(date);
        }, mYear, mMonth, mDay);

        dateDialog.setTitle(context.getString(R.string.select_date_title));
        dateDialog.getDatePicker().setMinDate(minDateLong);
        dateDialog.show();
    }

    public static void showTimeDialog(Context context, final EditText editText) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(context,
                (timePicker1, selectedHour, selectedMinute) -> editText.setText(
                        MessageFormat.format("{0}:{1}",
                                String.format(Locale.getDefault(), "%02d", selectedHour),
                                String.format(Locale.getDefault(), "%02d", selectedMinute))),
                hour, minute, false);

        timePicker.setTitle(context.getString(R.string.select_time_title));
        timePicker.show();
    }

    public static void showTimeDialogTwelveHourFormat(Context context, final EditText editText) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePicker = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker1, int selectedHour, int selectedMinute) {
                        String amPm = "AM";
                        if (selectedHour >= 12) {
                            amPm = "PM";
                            selectedHour = selectedHour - 12;
                        } else {
                            amPm = "AM";
                        }
                        editText.setText(
                                String.format("%s%s", MessageFormat.format("{0}:{1}",
                                        String.format(Locale.getDefault(), "%02d", selectedHour),
                                        String.format(Locale.getDefault(), "%02d", selectedMinute)), amPm));
                    }
                },
                hour, minute, false);

        timePicker.setTitle(context.getString(R.string.select_time_title));
        timePicker.show();
    }

    public static boolean validateStartEndTime(String startTime, String endTime) {

        String pattern = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(startTime);
            Date date2 = sdf.parse(endTime);

            if (date1.before(date2)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void showDialog(Context context, String dialogTitle, String message, String btn1String, String
            btn2String) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(context));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogs_leave_layout);

        if (!TextUtils.isEmpty(dialogTitle)) {
            TextView title = dialog.findViewById(R.id.tv_dialog_title);
            title.setText(dialogTitle);
            title.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(message)) {
            TextView text = dialog.findViewById(R.id.tv_dialog_subtext);
            text.setText(message);
            text.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(btn1String)) {
            Button button = dialog.findViewById(R.id.btn_dialog);
            button.setText(btn1String);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                // Close dialog
                dialog.dismiss();
            });
        }

        if (!TextUtils.isEmpty(btn2String)) {
            Button button1 = dialog.findViewById(R.id.btn_dialog_1);
            button1.setText(btn2String);
            button1.setVisibility(View.VISIBLE);
            button1.setOnClickListener(v -> {
                // Close dialog
                dialog.dismiss();
            });
        }

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        } catch (IllegalArgumentException ie) {
            Log.e(TAG, "IllegalArgumentException");
        } catch (RuntimeException re) {
            Log.e(TAG, "RuntimeException");
        } catch (Exception e) {
            Log.e(TAG, "Exception");
        }
    }

    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            Log.e(TAG, "IllegalArgumentException");
        } catch (RuntimeException re) {
            Log.e(TAG, "RuntimeException");
        } catch (Exception e) {
            Log.e(TAG, "Exception");
        }
    }

    public static void snackBarToShowMsg(@NonNull View view, @NonNull CharSequence text, int duration) {
        Snackbar snackbar = Snackbar.make(view, text, duration);
        snackbar.show();
        //return snackbar;
    }

    public static boolean isUserApproved() {
        UserInfo userInfo = getUserObjectFromPref();
//        return true;
        return !userInfo.getApproveStatus().equalsIgnoreCase(Constants.RequestStatus.PENDING) &&
                !userInfo.getApproveStatus().equalsIgnoreCase(Constants.RequestStatus.REJECTED);
    }

    public static String trimMessage(String json) {
        String trimmedString;

        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString("message");
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }

        return trimmedString;
    }

    public static Bitmap compressImageToBitmap(File f) {
        Bitmap b = null;
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int IMAGE_MAX_SIZE = 1024;
        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }
        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
//            String imageFilePath = Util.getImageName();
//            File imageFile = new File(imageFilePath);
            FileOutputStream out = new FileOutputStream(f);
            b.compress(Bitmap.CompressFormat.PNG, 40, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    public static String getImageName() {
        long time = new Date().getTime();
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + Constants.Image.IMAGE_STORAGE_DIRECTORY);
        if (!dir.exists()) {
            if (!dir.mkdir()) {
                Log.e(TAG, "Failed to create directory!");
                return null;
            }
        }
        return Constants.Image.IMAGE_STORAGE_DIRECTORY + Constants.Image.FILE_SEP
                + Constants.Image.IMAGE_PREFIX + time + Constants.Image.IMAGE_SUFFIX;
    }

    public static boolean isValidImageSize(File f) {
        if (f != null) {
            try {
                // Get length of file in bytes
                long fileSizeInBytes = f.length();
                // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                long fileSizeInKB = fileSizeInBytes / 1024;
                //  Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                long fileSizeInMB = fileSizeInKB / 1024;

                return fileSizeInMB <= 5;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        return false;
    }

    public static String getFormattedDateFromTimestamp(long date) {
        if (date > 0) {
            try {
                int length = (int) (Math.log10(date) + 1);
                if (length == 10) {
                    date = date * 1000;
                }
                Date d = new Timestamp(date);
                return getFormattedDate(d.toString(), DAY_MONTH_YEAR);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return "";
    }

    public static File compressFile(File f) {
        Bitmap b = null;
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        FileInputStream fis;

        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int IMAGE_MAX_SIZE = 1024;
        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;

        try {
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream out = new FileOutputStream(f);
            Objects.requireNonNull(b).compress(Bitmap.CompressFormat.PNG, 40, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    }

    /*private static final float maxHeight = 1280.0f;
    private static final float maxWidth = 1280.0f;

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    public static File compressImage(File file) {
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(file.getPath(), options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inTempStorage = new byte[16 * 1024];
        try {
            bmp = BitmapFactory.decodeFile(file.getPath(), options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(Objects.requireNonNull(scaledBitmap));
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - (float) bmp.getWidth() / 2, middleY - (float) bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        bmp.recycle();
        ExifInterface exif;
        try {
            exif = new ExifInterface(file.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out;
        try {
            //new File(imageFilePath).delete();
            out = new FileOutputStream(file);

            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }*/


    public static void showSuccessFailureToast(String response, Context mContext, View view) {
        try {
            JSONObject json = new JSONObject(response);
            String str_value = json.getString("message");
            Snackbar snackbar = Snackbar.make(view, str_value, Snackbar.LENGTH_LONG);
            snackbar.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //pojo to json string
    public static String modelToJson(TMApprovalRequestModel tmApprovalRequestModel) {
        Gson gson = new Gson();
        String jsonInString = gson.toJson(tmApprovalRequestModel);
        return jsonInString;
    }


    public static String showReasonDialog(final Activity context, int pos, Fragment fragment) {
        Dialog dialog;
        Button btnSubmit, btn_cancel;
        EditText edt_reason;
        Activity activity = context;

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_reason_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        edt_reason = dialog.findViewById(R.id.edt_reason);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btnSubmit = dialog.findViewById(R.id.btn_submit);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   /*Intent loginIntent = new Intent(context, LoginActivity.class);
                   loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                   loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   context.startActivity(loginIntent);*/
                String strReason = edt_reason.getText().toString();

                if (TextUtils.isEmpty(strReason)) {
                    Util.logger("Empty Reason", "Reason Can not be blank");
                    Util.snackBarToShowMsg(activity.getWindow().getDecorView()
                                    .findViewById(android.R.id.content), "Reason Can not be blank",
                            Snackbar.LENGTH_LONG);
                } else {
                    if (fragment instanceof TMUserLeavesApprovalFragment) {
                        ((TMUserLeavesApprovalFragment) fragment).onReceiveReason(strReason, pos);
                    }
                    if (fragment instanceof TMUserAttendanceApprovalFragment) {
                        ((TMUserAttendanceApprovalFragment) fragment).onReceiveReason(strReason, pos);
                    }
                    if (fragment instanceof TMUserProfileApprovalFragment) {
                        ((TMUserProfileApprovalFragment) fragment).onReceiveReason(strReason, pos);
                    }
                    if (fragment instanceof TMUserFormsApprovalFragment) {
                        ((TMUserFormsApprovalFragment) fragment).onReceiveReason(strReason, pos);
                    }

                    dialog.dismiss();
                }
            }
        });
        dialog.show();


        return "";
    }

    public static String getTodaysDate() {
        Date d = new Date();
        String pattern = "MM/dd/yyyy";
        SimpleDateFormat df = new SimpleDateFormat(pattern);
// Get the today date using Calendar object.
        Date today = Calendar.getInstance().getTime();
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
        return df.format(today);

    }

    public void startProgressDialog(Activity activity, String message) {
        pd = new ProgressDialog(activity);
        pd.setMessage(message);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    public void stopProgressDialog() {
        if (pd.isShowing()) {
            pd.dismiss();
        }

    }

    public static void showDelayInCheckInDialog(Context context, String header, String msg, boolean b) {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(b);
        dialog.setContentView(R.layout.attendance_dialog);

        AppCompatTextView textHeader = dialog.findViewById(R.id.txt_delaycheck_header);
        textHeader.setText(header);

        AppCompatTextView textMsg = dialog.findViewById(R.id.txt_delaycheck_title);
        textMsg.setText(msg);

        AppCompatButton button = dialog.findViewById(R.id.btn_delaycheckin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static Calendar setHours(int hours, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, min);
        return calendar;
    }


    //---------------------------
    public static void updateFirebaseIdRequests(JSONObject requestObject) {
        Response.Listener<JSONObject> pendingRequestsResponseListener = response -> {
            try {
                if (response != null) {
                    PreferenceHelper preferenceHelper = new PreferenceHelper(Platform.getInstance());
                    preferenceHelper.isCheckOut(PreferenceHelper.TOKEN_KEY,false);
                    String res = response.toString();
                    Log.d(TAG, "updateFBTokan - Resp: " + res);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        };

        Response.ErrorListener pendingRequestsErrorListener = error -> {
            Log.d(TAG, "updateFBTokan - Resp error: " + error.getMessage());
        };

        Gson gson = new GsonBuilder().serializeNulls().create();
        final String getPendingRequestsUrl = BuildConfig.BASE_URL + Urls.TM.PUT_UPDATE_FIREBASEID_TO_SERVER;

        Log.d(TAG, "updateFBTokan - url: " + getPendingRequestsUrl);

        GsonRequestFactory<JSONObject> gsonRequest = new GsonRequestFactory<>(
                Request.Method.PUT,
                getPendingRequestsUrl,
                new TypeToken<JSONObject>() {
                }.getType(),
                gson,
                pendingRequestsResponseListener,
                pendingRequestsErrorListener
        );

        gsonRequest.setHeaderParams(Util.requestHeader(true));
        gsonRequest.setShouldCache(false);
        gsonRequest.setBodyParams(requestObject);
        Platform.getInstance().getVolleyRequestQueue().add(gsonRequest);
    }

   //download and save project specific logo
   public static  void downloadAndLoadIcon(Context context){
       /*SharedPreferences preferences;
       SharedPreferences.Editor editor;
       preferences = Platform.getInstance().getSharedPreferences(
               "AppData", Context.MODE_PRIVATE);
       editor = Platform.getInstance().getSharedPreferences(
               "AppData", Context.MODE_PRIVATE).edit();

       String message = preferences.getString(Constants.OperatorModule.APP_CONFIG_RESPONSE,"");
       if (!TextUtils.isEmpty(message)) {
           AppConfigResponseModel appConfigResponseModel
                   = new Gson().fromJson(message, AppConfigResponseModel.class);

           UserInfo info = Util.getUserObjectFromPref();

           if (info.getCurrent_project_logo()!=null && !TextUtils.isEmpty(info.getCurrent_project_logo())) {
               editor.putString(Constants.OperatorModule.PROJECT_RELEVENT_LOGO,info.getCurrent_project_logo());
               editor.apply();
               *//*Glide.with(context)
                       .asBitmap()
                       .load(info.getCurrent_project_logo())
                       .into(new SimpleTarget<Bitmap>() {
                           @Override
                           public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition transition) {
                               String logoPath = saveImage(resource, editor, preferences, context);
                           }
                       });*//*
           }else {
               editor.putString(Constants.OperatorModule.PROJECT_RELEVENT_LOGO,"");
               editor.apply();
           }
       }*/
   }
    private static String saveImage(Bitmap image, SharedPreferences.Editor editor, SharedPreferences preferences, Context context) {
        String currentPhotoPath = null;
        File storageDir = getImageFile(); // 1
        currentPhotoPath = storageDir.getPath();
        boolean success = true;

        if (success) {
            try {
                OutputStream fOut = new FileOutputStream(storageDir);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Add the image to the system gallery
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID.concat(".file_provider"), storageDir);
            /*editor.putString(Constants.OperatorModule.PROJECT_RELEVENT_LOGO,uri.toString());
            editor.apply();

            String path = preferences.getString(Constants.OperatorModule.PROJECT_RELEVENT_LOGO, "");*/
            /*if (path.equalsIgnoreCase("")){

            }else {
                img_logo.setImageURI(null);
                img_logo.setImageURI(Uri.parse(path));
            }*/

        }
        return currentPhotoPath;
    }
    //-------
    private static File getImageFile() {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constants.Image.IMAGE_STORAGE_DIRECTORY);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File file;
        file = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + "logo" + ".png");

        return file;
    }

    public static byte[] getFileDataFromDrawable(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
