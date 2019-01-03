package com.nirvana.zmkj.base;

import android.app.Activity;

import com.cylty.zmkj.okHttp.RxManager;
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
