package com.antelope.goodbrother;

import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.antelope.goodbrother.config.Constants;
import com.nirvana.lib_share.ShareThirdPart;
import com.nirvana.zmkj.base.BaseApplication;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import androidx.multidex.MultiDex;
import cn.jpush.android.api.JPushInterface;

public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.openActivityDurationTrack(false);
        JPushInterface.init(this);
        ShareThirdPart.register(getApplicationContext());
        //加载apk换资源
        if (Constants.DEBUG) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();
        }
        ARouter.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
