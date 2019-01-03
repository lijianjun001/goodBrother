package com.antelope.goodbrother.business.main;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.antelope.goodbrother.config.RouterConfig;
import com.nirvana.zmkj.base.BaseActivity;

@Route(path = RouterConfig.ACTIVITY_MAIN)
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MainViewHolder(this).getRootView());
    }
}
