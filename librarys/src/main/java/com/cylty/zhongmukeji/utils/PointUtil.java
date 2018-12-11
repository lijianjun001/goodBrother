package com.cylty.zhongmukeji.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;

public class PointUtil {
    private Context context;
    private int screenWidth, screenHeight;

    public PointUtil(Context context) {
        this.context = context;
        this.screenWidth = ScreenUtils.getScreenWidth(context);
        this.screenHeight = ScreenUtils.getScreenHeight(context);
    }

    @SuppressLint("UseValueOf")
    public int getRandomX() {
        int x = new Double(Math.random() * screenWidth).intValue();
        return x;
    }

    @SuppressLint("UseValueOf")
    public int getRandomY() {
        int y = new Double(Math.random() * screenHeight).intValue();
        return y;
    }

    public Point getRandomPoint() {
        Point point = new Point();
        int x = getRandomX();
        int y = getRandomY();
        point.x = x;
        point.y = y;
        if (x > 0 && x < screenWidth * 3 / 8 && y > screenHeight / 4 && y < screenHeight * 5 / 12) {
            return point;
        } else if (x > screenWidth * 3 / 8 && x < screenWidth * 5 / 8 && y > screenHeight / 3 && y < screenHeight / 2) {
            return point;
            //72是小羊视图的宽度
        } else if (x > screenWidth * 5 / 8 && x < screenWidth - DensityUtil.dipToPx(context, 72) && y > screenHeight / 8 && y < screenHeight / 3) {
            return point;
        } else {
            return getRandomPoint();
        }
    }

}
