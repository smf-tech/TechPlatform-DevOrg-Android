package com.platform.utility;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.platform.Platform;
import com.platform.R;
import com.platform.database.DatabaseManager;
import com.platform.models.forms.FormResult;
import com.platform.models.login.Login;
import com.platform.models.pm.ProcessData;
import com.platform.models.profile.OrganizationProjectsResponse;
import com.platform.models.profile.OrganizationResponse;
import com.platform.models.profile.OrganizationRolesResponse;
import com.platform.models.profile.UserLocation;
import com.platform.models.user.UserInfo;
import com.platform.view.activities.HomeActivity;
import com.platform.view.fragments.HomeFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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
import java.util.TimeZone;

import static com.platform.utility.Constants.DATE_FORMAT;
import static com.platform.utility.Constants.FORM_DATE_FORMAT;

public class Util {
    private static ProgressDialog mProgressDialog;
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

    public static String getDateFromTimestamp(long date) {
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

    public static String getTimeFromTimeStamp(Long timeStamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timeStamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date currentTimeZone = calendar.getTime();
            return sdf.format(currentTimeZone);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }
        return "";
    }

    public static void launchFragment(Fragment fragment, Context context, String titleName,
                                      final boolean addToBackStack) {
        try {
            Bundle b = new Bundle();
            b.putSerializable("TITLE", titleName);
            b.putBoolean("SHOW_ALL", false);
            if (fragment instanceof HomeFragment) {
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
        deleteCache(Platform.getInstance());

        if (refreshData) {
            DatabaseManager.getDBInstance(Platform.getInstance()).deleteAllSyncedFormResults();
        } else {
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
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
}
