package com.cylty.zhongmukeji.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context
     * @param dpValue dp值
     * @return 返回像素值
     */
    public static int dipToPx(Context context, float dpValue) {
        if (context != null) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            final float scale = displayMetrics.density;
            return (int) (dpValue * scale + 0.5f);
        } else {
            return 0;
        }
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param context
     * @param pxValue 像素值
     * @return 返回dp值
     */
    public static int pxTodip(Context context, float pxValue) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (pxValue / scale + 0.5f);
        } else {
            return 0;
        }

    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将px值转换为ds值
     *
     * @param pxValue
     * @return
     */
    public static int px2ds(Context context, float pxValue) {
        int screenWidth = ScreenUtils.getScreenWidth(context);
        int totalPart = 800;//把屏幕宽度分成800份
        int perPartPx = screenWidth / totalPart;
        return (int) (pxValue / perPartPx + 0.5f);
    }

    /**
     * 将ds值转换为px值
     *
     * @param psValue
     * @return
     */
    public static int ds2Px(Context context, int psValue) {
        int screenWidth = ScreenUtils.getScreenWidth(context);
        int totalPart = 800;
        int perPartPx = screenWidth / totalPart;
        return (int)(psValue * perPartPx+0.5f);
    }

}
