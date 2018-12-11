package com.nirvana.zmkj.base;

import android.os.Bundle;

/**
 * Created by lijianjun on 2017-06-15.
 */

public abstract class BasePresenterFragment<P extends BasePresenter> extends BaseFragment {
    protected P presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
    }

    @Override
    public void onDestroy() {
        if (presenter != null) {
            presenter.clear();
        }
        super.onDestroy();
    }

    protected abstract P createPresenter();
}
