package com.octopusbjsindia.utility;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.permissionx.guolindev.PermissionX;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuzongheng on 2016/11/5.
 */

public class PermissionHelperWebView {

    public static boolean hasPermissions(Context context, String... permissionName) {
        for (String permission : permissionName) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static List<String> getGrantedPermissions(Context context, String... permissionName) {
        List<String> permissionsGranted = new ArrayList<>();
        for (String permission : permissionName) {
            if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                permissionsGranted.add(permission);
            }
        }
        return permissionsGranted;
    }

    public interface CheckPermissionListener {
        void onAllGranted(boolean sync);

        /**
         * Partly granted(deniedPermissions.size() >= 0) or all denied
         */
        void onPartlyGranted(List<String> permissionsDenied, boolean sync);
    }

    public static void CheckPermissions(final Context context, final CheckPermissionListener checkPermissionListener, String... permissionName) {

        if (hasPermissions(context, permissionName)) {
            if (checkPermissionListener != null) {
                checkPermissionListener.onAllGranted(true);
            }
        } else {
            PermissionX.init((FragmentActivity) context)
                    .permissions(permissionName)
                    .onExplainRequestReason((scope, deniedList) -> {
                        scope.showRequestReasonDialog(deniedList, "Core fundamental are based on these permissions", "OK", "Cancel");
                    })
                    .onForwardToSettings((scope, deniedList) -> {
                        scope.showForwardToSettingsDialog(deniedList, "You need to allow necessary permissions in Settings manually", "OK", "Cancel");
                    })
                    .request((allGranted, grantedList, deniedList) -> {
                        if (allGranted) {
                            if (checkPermissionListener != null) {
                                checkPermissionListener.onAllGranted(false);
                            }
                        }
                        else {
                            if (checkPermissionListener != null) {
                                checkPermissionListener.onPartlyGranted(deniedList, false);
                            }
                        }
                    });
        }
    }

}
