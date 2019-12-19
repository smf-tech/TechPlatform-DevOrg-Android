package com.octopusbjsindia.utility;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class Permissions {

    public static <T> boolean isCameraPermissionGranted(Activity context, T objectInstance) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED &&
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

    static <T> boolean isLocationPermissionGranted(Activity context, T objectInstance) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED ||
                    context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                if (objectInstance instanceof Fragment) {
                    ((Fragment) objectInstance).requestPermissions(
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            Constants.GPS_REQUEST);
                } else {
                    ActivityCompat.requestPermissions(context,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            Constants.GPS_REQUEST);
                }
                return false;
            }
        } else {
            return true;
        }
    }

    public static <T> boolean isReadExternalStotagePermission(Activity context, T objectInstance) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                if (objectInstance instanceof Fragment) {
                    ((Fragment) objectInstance).requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.READ_EXTERNAL_STORAGE);
                } else {
                    ActivityCompat.requestPermissions(context,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            Constants.READ_EXTERNAL_STORAGE);
                }
                return false;
            }
        } else {
            return true;
        }
    }

    public static <T> boolean isCallPermission(Activity context, T objectInstance) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                if (objectInstance instanceof Fragment) {
                    ((Fragment) objectInstance).requestPermissions(
                            new String[]{Manifest.permission.CALL_PHONE},
                            Constants.CALL_PHONE);
                } else {
                    ActivityCompat.requestPermissions(context,
                            new String[]{Manifest.permission.CALL_PHONE},
                            Constants.CALL_PHONE);
                }
                return false;
            }
        } else {
            return true;
        }
    }

    static <T> boolean isReadPhoneStatePermissionGranted(Activity context, T objectInstance) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                if (objectInstance instanceof Fragment) {
                    ((Fragment) objectInstance).requestPermissions(
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            Constants.READ_PHONE_STORAGE);
                } else {
                    ActivityCompat.requestPermissions(context,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            Constants.READ_PHONE_STORAGE);
                }
                return false;
            }
        } else {
            return true;
        }
    }
}
