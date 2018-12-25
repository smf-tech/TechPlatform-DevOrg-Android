package com.platform.utility;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class Util {

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
