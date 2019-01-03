package com.nirvana.zmkj.widget;

import android.content.Context;
import android.widget.Toast;

import com.cylty.zmkj.utils.StringUtils;
import com.nirvana.ylmc.lib_base.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/12/20.
 */

public class ShowMessageProxy {
    private Context context;
    private static final Object OBJECT=new Object();
    public ShowMessageProxy(Context context) {
        this.context = context;
    }

    public void showMessageDialog(String message) {
        CustomDialog.Builder builder = new CustomDialog.Builder(context);
        final CustomDialog dialog = builder.setMessage(message).setPositive("确定").createSingleButtonDialog();
        dialog.setOnDiaLogClickListener(new CustomDialog.OnDiaLogClickListener() {
            @Override
            public void onPositive() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onNegative() {
            }
        });
        dialog.show();
    }

    private CustomProgressDialog progressDialog;

    public void startProgressDialog(String strMsg) {
        if (progressDialog == null) {
            progressDialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);
        }
        if (StringUtils.isEmpty(strMsg)) {
            strMsg = "页面加载中。。。";
        }
        progressDialog.setMessage(strMsg);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void stopProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private static boolean toastIsShowing = false;

    public void displayToast(String str) {
        if (toastIsShowing) return;
        synchronized (OBJECT) {
            if (!toastIsShowing && !StringUtils.isEmpty(str)) {
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                toastIsShowing = true;
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
}
