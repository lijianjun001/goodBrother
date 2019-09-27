package com.antelope.goodbrother.business.printer.scan_detail;

import android.os.Bundle;

import com.nirvana.zmkj.base.BaseActivity;

public class ScanDetailActivity extends BaseActivity {

    public static final int REQUEST_CODE_SCAN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        setContentView(new ScanDetailHolder(this, bundle).getRootView());
    }
}

