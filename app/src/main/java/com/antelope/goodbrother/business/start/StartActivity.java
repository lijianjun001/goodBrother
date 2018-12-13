package com.antelope.goodbrother.business.start;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.antelope.goodbrother.R;
import com.nirvana.zmkj.base.BasePresenterActivity;

import androidx.annotation.NonNull;

public class StartActivity extends BasePresenterActivity<StartPresenter> {
    private TextView timeTv;
    private RelativeLayout skipRl;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_start);
        super.onCreate(savedInstanceState);
        timeTv = findViewById(R.id.skip_time);
        skipRl = findViewById(R.id.skip_rl);
        skipRl.setOnClickListener(v -> mPresenter.gotoNextPage());
        initView();
    }


    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        super.onDestroy();
    }

    @Override
    protected StartPresenter createPresenter() {
        return new StartPresenter(this);
    }


    protected void initView() {

        countDownTimer = new CountDownTimer(3 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                skipRl.setVisibility(View.VISIBLE);
                timeTv.setText(millisUntilFinished / 1000 + " 跳过");
            }

            @Override
            public void onFinish() {
                mPresenter.gotoNextPage();
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

}