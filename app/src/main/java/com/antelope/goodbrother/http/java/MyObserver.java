package com.antelope.goodbrother.http.java;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.antelope.goodbrother.BuildConfig;
import com.antelope.goodbrother.config.Constants;
import com.antelope.goodbrother.config.RouterConfig;
import com.cylty.zmkj.okHttp.ApiException;
import com.cylty.zmkj.utils.StringUtils;
import com.nirvana.zmkj.widget.CustomDialog;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import rx.Observer;

/**
 * Created by lijianjun on 2017/9/7.
 */

public abstract class MyObserver<T> implements Observer<T> {
    private Context context;

    public MyObserver(Context context) {
        this.context = context;
    }


    @Override
    public void onError(Throwable e) {
        if (BuildConfig.DEBUG) {
            Type genType = getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            Log.e(params[0].toString(), e.toString());
        }
        if (e.toString().contains(Constants.TOKEN_INVALID_CODE)) {
            ARouter.getInstance().build(RouterConfig.ACTIVITY_LOGIN_BY_PASSWORD).navigation();
        } else if (e instanceof ApiException) {
            CustomDialog.Builder builder = new CustomDialog.Builder(context);
            CustomDialog dialog = builder.setMessage(e.getMessage()).setPositive("确定").createSingleButtonDialog();
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
        } else {
            if (!StringUtils.isEmpty(e.getMessage())) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCompleted() {

    }
}
