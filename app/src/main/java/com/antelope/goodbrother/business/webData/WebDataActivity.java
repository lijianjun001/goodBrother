package com.antelope.goodbrother.business.webData;

import android.os.Bundle;

import com.nirvana.zmkj.base.BaseActivity;

public class WebDataActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new WebDataViewHolder(this).getRootView());
    }

}
