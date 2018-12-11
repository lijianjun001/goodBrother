package com.nirvana.zmkj.base;

import android.os.Bundle;

/**
 * Created by lijianjun on 2017-06-13.
 */

public abstract class BasePresenterActivity<P extends BasePresenter> extends BaseActivity {

    protected P mPresenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPresenter = createPresenter();
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.clear();
        }
        super.onDestroy();
    }

    protected abstract P createPresenter();

}
