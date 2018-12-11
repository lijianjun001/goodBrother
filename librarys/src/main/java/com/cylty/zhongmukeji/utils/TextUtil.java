package com.cylty.zhongmukeji.utils;

import android.graphics.Rect;
import android.text.TextPaint;

/**
 * Created by Administrator on 2018/3/16.
 */

public class TextUtil {
    public static int getWidth(String text, TextPaint textPaint) {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        int width = bounds.width();
        return width;
    }
}
