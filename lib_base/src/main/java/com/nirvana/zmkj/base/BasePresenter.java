package com.nirvana.zmkj.base;

import android.app.Activity;

import com.cylty.zhongmukeji.myOkhttp.RxManager;
import com.nirvana.zmkj.widget.ShowMessageProxy;

public class BasePresenter {
    public Activity mActivity;
    protected RxManager mRxManager = new RxManager();
    public ShowMessageProxy showMessageProxy;

    public BasePresenter(Activity activity) {
        this.mActivity = activity;
        showMessageProxy = new ShowMessageProxy(activity);
    }


    void clear() {
        mRxManager.clear();
    }
}
