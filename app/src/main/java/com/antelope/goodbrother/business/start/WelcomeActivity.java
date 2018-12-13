package com.antelope.goodbrother.business.start;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.antelope.goodbrother.R;
import com.antelope.goodbrother.config.Constants;
import com.antelope.goodbrother.config.RouterConfig;
import com.cylty.zhongmukeji.utils.BitmapUtil;
import com.nirvana.zmkj.base.BaseActivity;
import com.nirvana.zmkj.manager.SharePreferenceManager;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;

public class WelcomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private ViewPager vp;
    private List<View> views;
    private int[] pics = {R.drawable.nav_1, R.drawable.nav_2, R.drawable.nav_3, R.drawable.nav_4};
    private Button startButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_welcome);
        super.onCreate(savedInstanceState);
        vp = findViewById(R.id.viewpager);
        startButton = findViewById(R.id.start_btn);
        initView();
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        changeStartButton(arg0);
    }


    protected void initView() {
        views = new ArrayList<>();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        for (int pic: pics) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            BitmapUtil.setBackground(this, iv, pic);
            views.add(iv);
        }
        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);
        vp.addOnPageChangeListener(this);
    }

    private void changeStartButton(int position) {
        if (position == 3) {
            startButton.setVisibility(View.VISIBLE);
            startButton.setOnClickListener(v -> {
                setGuided();
                ARouter.getInstance().build(RouterConfig.ACTIVITY_MAIN).navigation();
                WelcomeActivity.this.finish();
            });
        } else {
            startButton.setVisibility(View.GONE);
        }
    }


    private void setGuided() {
        SharePreferenceManager.getInstance().putBoolean(Constants.IS_FIRST_IN, false);
    }


    @Override
    protected void onDestroy() {
        for (View view: views) {
            BitmapUtil.recycleBackground(view);
        }
        super.onDestroy();
    }
}
