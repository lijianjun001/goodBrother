package com.nirvana.zmkj.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.cylty.zmkj.utils.StringUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/12/20.
 */

public class ShowMessageProxy {
    private Context context;
    private final static Object object = new Object();

    public ShowMessageProxy(Context context) {
        this.context = context;
    }

    private static boolean toastIsShowing = false;

    public void showMessageDialog(String message) {
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        final CustomDialog dialog = builder.setMessage(message).setPositive("确定").createSingleButtonDialog();
        dialog.setOnDiaLogClickListener(new SureOnDiaLogClickListener() {
            @Override
            public void onPositive() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    public void showMessageDialog(String message, final SureOnDiaLogClickListener sureOnDiaLogClickListener) {
        if (context instanceof Activity) {

            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        final CustomDialog dialog = builder.setMessage(message).setPositive("确定").createSingleButtonDialog();
        dialog.setOnDiaLogClickListener(new SureOnDiaLogClickListener() {
            @Override
            public void onPositive() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                sureOnDiaLogClickListener.onPositive();
            }
        });
        dialog.show();
    }

    private CustomProgressDialog progressDialog;

    public void startProgressDialog(String strMsg) {
        if (context instanceof Activity) {

            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        if (StringUtils.isEmpty(strMsg)) {
            strMsg = "";
        }
        CustomProgressDialog.Builder builder = new CustomProgressDialog.Builder(context);
        if (progressDialog == null) {
            progressDialog = builder.setMessage(strMsg).create();
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void stopProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
            if (context instanceof Activity) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    Activity activity = (Activity) context;
                    if (!activity.isDestroyed()) {
                        progressDialog.dismiss();
                    }
                } else {
                    progressDialog.dismiss();
                }
            } else {
                progressDialog.dismiss();
            }
        }
    }

    public void displayToast(String str) {//俩秒之间最多弹一次
        if (toastIsShowing) return;
        synchronized (object) {
            if (!toastIsShowing && !StringUtils.isEmpty(str)) {
                toastIsShowing = true;
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        toastIsShowing = false;
                    }
                }, 2 * 1000);
            }
        }
    }

    public abstract static class SureOnDiaLogClickListener implements CustomDialog.OnDiaLogClickListener {

        @Override
        public void onNegative() {

        }
    }
}
