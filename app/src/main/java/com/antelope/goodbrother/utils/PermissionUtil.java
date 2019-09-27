package com.antelope.goodbrother.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import com.nirvana.zmkj.manager.SharePreferenceManager;
import com.nirvana.zmkj.widget.CustomDialog;

import androidx.core.app.ActivityCompat;

public class PermissionUtil {

    public static boolean permissionGranted(Activity activity, String permissionName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasPermission = ActivityCompat.checkSelfPermission(activity, permissionName);
            return hasPermission == PackageManager.PERMISSION_GRANTED;
        } else {
            return false;
        }
    }

    /**
     * @param permissionAction 需要做的行为
     * @param spPermissionKey  是否请求过权限记录时的key
     * @param permissionName   需要请求的权限
     * @param notShowNotice    请求失败提示语
     * @param reqAct           请求权限的activity
     * @param reqCode          请求权限的code
     */
    public static void requestPermissionOrDoAction(PermissionAction permissionAction, String spPermissionKey, String permissionName, String notShowNotice, Activity reqAct, int reqCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasPermission = reqAct.checkSelfPermission(permissionName);
            boolean hasReq = SharePreferenceManager.getInstance().getBoolean(spPermissionKey, false);
            if (!reqAct.shouldShowRequestPermissionRationale(permissionName) && hasPermission != PackageManager.PERMISSION_GRANTED && hasReq) {//如果拒绝并且不在提醒


                CustomDialog.Builder builder = new CustomDialog.Builder(reqAct);
                CustomDialog dialog = builder.setMessage(notShowNotice).setPositive("确定").createSingleButtonDialog();
                dialog.setOnDiaLogClickListener(new CustomDialog.OnDiaLogClickListener() {
                    @Override
                    public void onPositive() {
                        UriUtil.openSetting(reqAct);
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegative() {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return;
            }

            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                reqAct.requestPermissions(new String[]{permissionName}, reqCode
                );
            } else {
                permissionAction.onAction();
            }
        } else {
            permissionAction.onAction();
        }
    }

    public static void requestPermissionsOrDoAction(PermissionAction permissionAction, String spPermissionKey, String[] permissionNames, String notShowNotice, Activity reqAct, int reqCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean grant = false;
            for (String permissionName : permissionNames) {
                int hasPermission = reqAct.checkSelfPermission(permissionName);
                grant = hasPermission == PackageManager.PERMISSION_GRANTED;
            }

            boolean hasReq = SharePreferenceManager.getInstance().getBoolean(spPermissionKey, false);//是否请求过该权限
            if (!reqAct.shouldShowRequestPermissionRationale(permissionNames[0]) && !grant && hasReq) {//如果拒绝并且不在提醒,第一次请求reqAct.shouldShowRequestPermissionRationale为false


                CustomDialog.Builder builder = new CustomDialog.Builder(reqAct);
                CustomDialog dialog = builder.setMessage(notShowNotice).setPositive("确定").createSingleButtonDialog();
                dialog.setOnDiaLogClickListener(new CustomDialog.OnDiaLogClickListener() {
                    @Override
                    public void onPositive() {
                        UriUtil.openSetting(reqAct);
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegative() {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return;
            }

            if (!grant) {
                reqAct.requestPermissions(permissionNames, reqCode
                );
            } else {
                permissionAction.onAction();
            }
        } else {
            permissionAction.onAction();
        }
    }

    public interface PermissionAction {

        void onAction();
    }

    public static void setPermissionReq(String spPermissionKey) {

        SharePreferenceManager.getInstance().putBoolean(spPermissionKey, true);
    }
}
