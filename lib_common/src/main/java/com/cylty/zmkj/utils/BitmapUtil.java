/*
 * @Title: BitmapUtils.java
 * @Copyright: Corporation. Ltd. Copyright 1998-2018, All rights reserved
 * @Description: TODO<请描述此文件是做什么的>
 * @author: xjp
 * @data: 2014年9月18日 上午10:10:30
 * @version: V1.0
 */
package com.cylty.zmkj.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lijianjun on 2017-06-20.
 */
public class BitmapUtil {

    private static final String TAG = "BitmapUtils";
    private static BitmapFactory.Options options = new BitmapFactory.Options();

    /**
     * 创建倒影图片
     *
     * @param srcBitmap        源图片的bitmap
     * @param reflectionHeight 图片倒影的高度
     * @return Bitmap
     * @throw
     */
    public static Bitmap createReflectedBitmap(Bitmap srcBitmap, int reflectionHeight) {

        if (null == srcBitmap) {
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }

        // The gap between the reflection bitmap and original bitmap.
        final int REFLECTION_GAP = 0;

        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();

        if (0 == srcWidth || srcHeight == 0) {
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }

        // The matrix
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        try {

            // The reflection bitmap, width is same with original's, height is
            // half of original's.
            Bitmap reflectionBitmap = Bitmap.createBitmap(srcBitmap, 0, srcHeight - reflectionHeight, srcWidth,
                    reflectionHeight, matrix, false);

            if (null == reflectionBitmap) {
                Log.e(TAG, "Create the reflectionBitmap is failed");
                return null;
            }

            // Create the bitmap which contains original and reflection bitmap.
            Bitmap bitmapWithReflection = Bitmap.createBitmap(srcWidth, srcHeight + reflectionHeight,
                    Config.ARGB_8888);

            if (null == bitmapWithReflection) {
                return null;
            }

            // Prepare the canvas to draw stuff.
            Canvas canvas = new Canvas(bitmapWithReflection);

            // Draw the original bitmap.
            canvas.drawBitmap(srcBitmap, 0, 0, null);

            // Draw the reflection bitmap.
            canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP, null);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            LinearGradient shader = new LinearGradient(0, srcHeight, 0,
                    bitmapWithReflection.getHeight() + REFLECTION_GAP, 0x70FFFFFF, 0x00FFFFFF, TileMode.MIRROR);
            paint.setShader(shader);
            paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));

            canvas.save();
            // Draw the linear shader.
            canvas.drawRect(0, srcHeight, srcWidth, bitmapWithReflection.getHeight() + REFLECTION_GAP, paint);
            if (!reflectionBitmap.isRecycled()) {
                reflectionBitmap.recycle();
            }

            canvas.restore();

            return bitmapWithReflection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "Create the reflectionBitmap is failed");
        return null;
    }

    /**
     * 图片圆角处理
     *
     * @param srcBitmap 源图片的bitmap
     * @param ret       圆角的度数
     * @return Bitmap
     */
    public static Bitmap getRoundImage(Bitmap srcBitmap, float ret) {

        if (null == srcBitmap) {
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }

        int bitWidth = srcBitmap.getWidth();
        int bitHight = srcBitmap.getHeight();

        BitmapShader bitmapShader = new BitmapShader(srcBitmap, TileMode.CLAMP, TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);

        RectF rectf = new RectF(0, 0, bitWidth, bitHight);

        Bitmap outBitmap = Bitmap.createBitmap(bitWidth, bitHight, Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        canvas.drawRoundRect(rectf, ret, ret, paint);
        canvas.save();
        canvas.restore();

        return outBitmap;
    }

    /**
     * 图片沿着Y轴旋转一定角度
     *
     * @param srcBitmap        源图片的bitmap
     * @param reflectionHeight 图片倒影的高度
     * @param rotate           图片旋转的角度
     * @return Bitmap
     * @throw
     */
    public static Bitmap skewImage(Bitmap srcBitmap, float rotate, int reflectionHeight) {

        if (null == srcBitmap) {
            Log.e(TAG, "the srcBitmap is null");
            return null;
        }

        Bitmap reflecteBitmap = createReflectedBitmap(srcBitmap, reflectionHeight);

        if (null == reflecteBitmap) {
            Log.e(TAG, "failed to createReflectedBitmap");
            return null;
        }

        int wBitmap = reflecteBitmap.getWidth();
        int hBitmap = reflecteBitmap.getHeight();
        float scaleWidth = ((float) 180) / wBitmap;
        float scaleHeight = ((float) 270) / hBitmap;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        reflecteBitmap = Bitmap.createBitmap(reflecteBitmap, 0, 0, wBitmap, hBitmap, matrix, true);
        Camera localCamera = new Camera();
        localCamera.save();
        Matrix localMatrix = new Matrix();
        localCamera.rotateY(rotate);
        localCamera.getMatrix(localMatrix);
        localCamera.restore();
        localMatrix.preTranslate(-reflecteBitmap.getWidth() >> 1, -reflecteBitmap.getHeight() >> 1);
        Bitmap localBitmap2 = Bitmap.createBitmap(reflecteBitmap, 0, 0, reflecteBitmap.getWidth(),
                reflecteBitmap.getHeight(), localMatrix, true);
        Bitmap localBitmap3 = Bitmap.createBitmap(localBitmap2.getWidth(), localBitmap2.getHeight(),
                Config.ARGB_8888);
        Canvas localCanvas = new Canvas(localBitmap3);
        Paint localPaint = new Paint();
        localPaint.setAntiAlias(true);
        localPaint.setFilterBitmap(true);
        localCanvas.drawBitmap(localBitmap2, 0.0F, 0.0F, localPaint);
        if (!reflecteBitmap.isRecycled()) {
            reflecteBitmap.recycle();
        }
        if (!localBitmap2.isRecycled()) {
            localBitmap2.recycle();
        }
        localCanvas.save();
        localCanvas.restore();
        return localBitmap3;
    }

    /**
     * 图片模糊化处理
     *
     * @param bitmap  源图片
     * @param radius  The radius of the blur Supported range 0 < radius <= 25
     * @param context 上下文
     * @return Bitmap
     * @throw
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("NewApi")
    public static Bitmap blurBitmap(Bitmap bitmap, float radius, Context context) {

        // Let's create an empty bitmap with the same size of the bitmap we want
        // to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

        // Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);

        // Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // Create the Allocations (in/out) with the Renderscript and the in/out
        // bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        // Set the radius of the blur
        if (radius > 25) {
            radius = 25.0f;
        } else if (radius <= 0) {
            radius = 1.0f;
        }
        blurScript.setRadius(radius);

        // Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        // recycle the original bitmap
        bitmap.recycle();
        bitmap = null;
        // After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;

    }

    /**
     * 给图片添加指定颜色的边框
     *
     * @param srcBitmap   原图片
     * @param borderWidth 边框宽度
     * @param color       边框的颜色值
     * @return
     */
    public static Bitmap addFrameBitmap(Bitmap srcBitmap, int borderWidth, int color) {
        if (srcBitmap == null) {
            Log.e(TAG, "the srcBitmap or borderBitmap is null");
            return null;
        }

        int newWidth = srcBitmap.getWidth() + borderWidth;
        int newHeight = srcBitmap.getHeight() + borderWidth;

        Bitmap outBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);

        Canvas canvas = new Canvas(outBitmap);

        Rect rec = canvas.getClipBounds();
        rec.bottom--;
        rec.right--;
        Paint paint = new Paint();
        // 设置边框颜色
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        // 设置边框宽度
        paint.setStrokeWidth(borderWidth);
        canvas.drawRect(rec, paint);

        canvas.drawBitmap(srcBitmap, borderWidth / 2, borderWidth / 2, null);
        canvas.save();
        canvas.restore();
        if (!srcBitmap.isRecycled()) {
            srcBitmap.recycle();
        }

        return outBitmap;
    }

    /**
     * convert drawable to bitmap
     *
     * @param drawable 转换的drawable
     * @return 转换成的bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * 回收animationDrawable
     *
     * @param animationDrawable the recycled animationDrawable
     */
    public static void tryRecycleAnimationDrawable(AnimationDrawable animationDrawable) {
        if (animationDrawable != null) {
            animationDrawable.stop();
            for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
                Drawable frame = animationDrawable.getFrame(i);
                if (frame instanceof BitmapDrawable) {
                    ((BitmapDrawable) frame).getBitmap().recycle();
                }
                frame.setCallback(null);
            }
            animationDrawable.setCallback(null);
        }
    }


    /**
     * bitmap 保存为jpg 图片
     *
     * @param mBitmap 图片源
     * @param file    图片路径
     */
    public static void saveBitmap(Bitmap mBitmap, File file) {
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get bytes form inputStream
     *
     * @param inputStream the inputStream
     * @return the byte
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n = 0;
        while ((n = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    /**
     * 压缩 bitmap 质量
     *
     * @param bitmap the bitmap of compress
     * @return
     */
    public static byte[] compressImageQuality(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (byteArrayOutputStream.size() / 1024 > 20) { // 循环判断如果压缩后图片是否大于20kb,大于继续压缩
            byteArrayOutputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, byteArrayOutputStream);// 这里压缩值options，把压缩后的数据存放到baos中
            options = options / 2;// 每次都减少一半
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * the bytes to bitmap
     *
     * @param bytes the bytes of bitmap
     * @return
     */
    public static Bitmap bytesToBitmap(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new RuntimeException("bytes not to null");
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * compress bitmap shape
     *
     * @param bitmap the bitmap to compress
     * @return
     * @throws RuntimeException
     */
    public static byte[] compressImageShape(Bitmap bitmap) throws RuntimeException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 100f;//这里设置高度为100f
        float ww = 100f;//这里设置宽度为100f
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap retBitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        if (retBitmap == null) throw new RuntimeException("compressImageShape is error");
        return compressImageQuality(retBitmap);//压缩好比例大小后再进行质量压缩
    }

    /**
     * convertViewToBitmap
     *
     * @param view         the converted view
     * @param bitmapWidth  the width of returnBitmap
     * @param bitmapHeight the height of returnBitmap
     * @return
     */
    public static Bitmap convertViewToBitmap(View view, int bitmapWidth, int bitmapHeight) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Config.ARGB_8888);
        view.draw(new Canvas(bitmap));

        return bitmap;
    }

    /**
     * 获得GradientDrawable
     *
     * @param radius      设置GradientDrawable圆角度
     * @param fillColor   设置GradientDrawable实体色
     * @param width       设置GradientDrawable边缘宽度
     * @param strokeColor 设置GradientDrawable边缘颜色
     * @return
     */
    public GradientDrawable getDrawable(int radius, int fillColor, int width, int strokeColor) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(radius);
        gradientDrawable.setColor(fillColor);
        gradientDrawable.setStroke(width, strokeColor);
        return gradientDrawable;
    }

    /**
     * 背景选择器
     *
     * @param pressedDraw 按下时背景
     * @param normalDraw  常规背景
     * @return
     */
    public static StateListDrawable getSelector(Drawable normalDraw, Drawable pressedDraw) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{-android.R.attr.state_checked}, normalDraw);
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, pressedDraw);
        return stateListDrawable;
    }

    /**
     * 颜色选择器
     *
     * @param normalColor 按下时颜色
     * @param pressColor  常规颜色
     * @return
     */
    public ColorStateList getColorSelector(int normalColor, int pressColor) {
        int[][] states = {{-android.R.attr.state_selected}, {android.R.attr.state_selected}};
        int[] colors = {normalColor, pressColor};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        return colorStateList;
    }

    @SuppressLint("NewApi")
    public static void setBackground(Context context, View view, int rId) {
        Drawable bitmap = context.getResources().getDrawable(rId);
        view.setBackground(bitmap);
    }

    public static void recycleBackground(View view) {
        BitmapDrawable temp = (BitmapDrawable) view.getBackground();
        if (temp != null) {
            view.setBackgroundResource(0);// 别忘了把背景设为null，避免onDraw刷新背景时候出现used
            temp.setCallback(null);
            temp.getBitmap().recycle();
        }
    }
}