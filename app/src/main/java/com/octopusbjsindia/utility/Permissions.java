package com.octopusbjsindia.utility;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class Permissions {

    public static <T> boolean isCameraPermissionGranted(Activity context, T objectInstance) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        context.checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED &&
                        context.checkSelfPermission(Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                } else {
                    if (objectInstance instanceof Fragment) {
                        ((Fragment) objectInstance).requestPermissions(permissions_33, Constants.CAMERA_REQUEST);
                    } else {
                        ActivityCompat.requestPermissions(context, permissions_33, Constants.CAMERA_REQUEST);
                    }
                    return false;
                }
            }

            if (context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                if (objectInstance instanceof Fragment) {
                    ((Fragment) objectInstance).requestPermissions(permissions, Constants.CAMERA_REQUEST);
                } else {
                    ActivityCompat.requestPermissions(context, permissions, Constants.CAMERA_REQUEST);
                }
                return false;
            }
        } else {
            return true;
        }
    }

    public static <T> boolean isLocationPermissionGranted(Activity context, T objectInstance) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&
                    context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                if (objectInstance instanceof Fragment) {
                    ((Fragment) objectInstance).requestPermissions(
                            new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION},
                            Constants.GPS_REQUEST);
                } else {
                    ActivityCompat.requestPermissions(context,
                            new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION},
                            Constants.GPS_REQUEST);
                }
                return false;
            }
        } else {
            return true;
        }
    }

    public static <T> boolean isWriteExternalStoragePermission(Activity context, T objectInstance) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                if (objectInstance instanceof Fragment) {
                    ((Fragment) objectInstance).requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            Constants.WRITE_EXTERNAL_STORAGE);
                } else {
                    ActivityCompat.requestPermissions(context,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            Constants.WRITE_EXTERNAL_STORAGE);
                }
                return false;
            }
        } else {
            return true;
        }
    }

    public static <T> boolean isCallPermission(Activity context, T objectInstance) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
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


    public static <T> boolean checkAndRequestStorageCameraPermissions(Activity activity, T objectInstance) {
        // Check which permissions are granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> listPermissionsNeeded = new ArrayList<>();
            for (String perm : storagePermissionsList()) {
                if (ContextCompat.checkSelfPermission(activity, perm) != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(perm);
                }
            }
            // Ask for non-granted permissions
            if (!listPermissionsNeeded.isEmpty()) {
                if (objectInstance instanceof Fragment) {
                    ((Fragment) objectInstance).requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                            Constants.CAMERA_REQUEST);
                } else {
                    ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                            Constants.CAMERA_REQUEST);
                }
                return false;
            }
        }
        // App has all permissions. Proceed ahead
        return true;
    }

    private static String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private static String[] permissions_33 = new String[]{
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.CAMERA};

    public static String[] storagePermissionsList() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = permissions_33;
        } else {
            p = permissions;
        }
        return p;
    }
}
