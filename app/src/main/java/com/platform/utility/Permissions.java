package com.platform.utility;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

public class Permissions {

    public static <T> boolean isSMSPermissionGranted(Activity context, T objectInstance) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.RECEIVE_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
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

    public static <T> boolean isCameraPermissionGranted(Activity context, T objectInstance) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED ||
                    context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                if (objectInstance instanceof Fragment) {
                    ((Fragment) objectInstance).requestPermissions(
                            new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            Constants.CAMERA_REQUEST);
                } else {
                    ActivityCompat.requestPermissions(context,
                            new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            Constants.CAMERA_REQUEST);
                }
                return false;
            }
        } else {
            return true;
        }
    }
}
