package com.antelope.goodbrother.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

public class PermissionUtil {
    public static void requestPermission(Activity activity, String permissionName, int reqCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasPermission = activity.checkSelfPermission(permissionName);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{permissionName},
                        reqCode);
            }
        }
    }

    public static boolean permissionGranted(Activity activity, String permissionName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasPermission = activity.checkSelfPermission(permissionName);
            return hasPermission == PackageManager.PERMISSION_GRANTED;
        }else{
            return true;
        }
    }
}
