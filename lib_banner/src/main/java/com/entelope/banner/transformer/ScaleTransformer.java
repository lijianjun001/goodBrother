package com.entelope.banner.transformer;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

public class ScaleTransformer implements ViewPager.PageTransformer {
    private float MIN_SCALE = 0.88F;

    public ScaleTransformer() {
    }

    public ScaleTransformer(float scale) {
        this.MIN_SCALE = scale;
    }

    public void transformPage(View page, float position) {
        if (position < -1.0F) {
            page.setScaleY(MIN_SCALE);
            page.setScaleX(MIN_SCALE);
        } else if (position <= 1.0F) {

            float scale = MIN_SCALE + (1 - MIN_SCALE) * Math.abs((1.0F - Math.abs(position)));
            page.setScaleY(scale);
            page.setScaleX(scale);
        } else {
            page.setScaleY(MIN_SCALE);
            page.setScaleX(MIN_SCALE);
        }

    }
}
