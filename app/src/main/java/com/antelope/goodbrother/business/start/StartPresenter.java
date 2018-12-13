package com.antelope.goodbrother.business.start;

import android.app.Activity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.antelope.goodbrother.config.Constants;
import com.antelope.goodbrother.config.RouterConfig;
import com.antelope.goodbrother.utils.RouterUtil;
import com.nirvana.zmkj.base.BasePresenter;
import com.nirvana.zmkj.manager.SharePreferenceManager;

public class StartPresenter extends BasePresenter {

    public StartPresenter(Activity activity) {
        super(activity);
    }


    public void gotoNextPage() {
        boolean isFirstIn = SharePreferenceManager.getInstance().getBoolean(Constants.IS_FIRST_IN, true);
        if (!isFirstIn) {
            ARouter.getInstance().build(RouterConfig.ACTIVITY_MAIN).navigation();
        } else {
            RouterUtil.openActivity(mActivity, WelcomeActivity.class);
        }
        mActivity.finish();
    }



}
