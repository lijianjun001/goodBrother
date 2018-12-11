package com.cylty.zhongmukeji.chart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.cylty.zhongmukeji.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijianjun on 2016/11/29.
 */
public class PieChart extends View implements GestureDetector.OnGestureListener {


    /**
     * view的宽高
     */
    private int mWidth, mHeight;
    /**
     * 饼状图的半径、内部空白圆的半径
     */
    private float mRadius, mInnerRadius;
    /**
     * 饼状图的外切
     */
    private RectF mPieRect;
    /**
     * 各种画笔
     */
    private Paint mPiePaint, mBlankPaint, mLinePaint, mTextPaint;
    /**
     * 实体类集合
     */
    private List<IPieElement> mElements;
    /**
     * 各个元素的角度
     */
    private List<Float> mAngles = new ArrayList<>();
    /**
     * 元素的颜色
     */
    private List<String> mColors = new ArrayList<>();
    /**
     * 元素的占比
     */
    private List<String> mPercents = new ArrayList<>();
    /**
     * 中心文字
     */

    private SparseArray<double[]> angles = new SparseArray<>();
    private float pathTimes = 1.1f;

    private float textSize;
    private int textColor;
    float horizontalLineLength = 40;
    private GestureDetector mDetector;

    public PieChart(Context context) {
        this(context, null);
    }

    public PieChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PieChart);
        textSize = typedArray.getDimensionPixelSize(R.styleable.PieChart_textSize, 30);
        textColor = typedArray.getColor(R.styleable.PieChart_textColor, Color.BLACK);
        horizontalLineLength = typedArray.getDimensionPixelSize(R.styleable.PieChart_horizontalLineLength, 40);
        init();
    }

    private void init() {
        mDetector = new GestureDetector(getContext(), this);
        mPieRect = new RectF();
        mPiePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPiePaint.setColor(Color.RED);

        mBlankPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBlankPaint.setColor(Color.WHITE);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(4);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0, height = 0;
        if (widthMode == MeasureSpec.AT_MOST) {
            width = (int) getSize();
        } else {
            width = widthSize;
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = (int) getSize();
        } else {
            height = heightSize;
        }
//        int size= Math.min(width,height);
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w - getPaddingLeft() - getPaddingRight();
        mHeight = h - getPaddingTop() - getPaddingBottom();
        mRadius = (float) (mWidth / 2);
        mPieRect.left = -mRadius;
        mPieRect.top = -mRadius;
        mPieRect.right = mRadius;
        mPieRect.bottom = mRadius;
        mInnerRadius = (float) (mRadius * 0.6);
    }

    private Path mPath = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2, mHeight / 2);
        //从12点方向开始画
        float sweepAngle = -90;
        for (int i = 0; mElements != null && mElements.size() > i; i++) {
            //设置扇形的颜色
            mPiePaint.setColor(Color.parseColor(mColors.get(i)));
            mLinePaint.setColor(Color.parseColor(mColors.get(i)));
            //画扇形
            canvas.drawArc(mPieRect, sweepAngle, mAngles.get(i), true, mPiePaint);
            sweepAngle += mAngles.get(i);
        }
        canvas.drawCircle(0, 0, mInnerRadius, mBlankPaint);
    }


    /**
     * 把文字分两行，并画在圆内接正方形内，依此计算画笔的textSize
     *
     * @param text
     */
    private void calculateTextPaint(String text) {
        if (!TextUtils.isEmpty(text)) {
            measureText(text, 100);
        }
    }

    /**
     * 递归调用，计算testSize
     *
     * @param text
     * @param textSize
     */
    private void measureText(String text, int textSize) {
        mTextPaint.setTextSize(textSize);
        float width = getTextWidth(mTextPaint, text);
        float height = getTextHeight(mTextPaint, text);
        if (width > mInnerRadius * 1.41421) {
            textSize--;
            measureText(text, textSize);
            return;
        }
        if (height * 2.5 > mInnerRadius * 1.41421) {
            textSize--;
            measureText(text, textSize);
        }
    }

    private float getTextHeight(Paint paint, String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    private float getTextWidth(Paint paint, String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.width();
    }

    /**
     * 获取圆弧中点的x轴坐标
     *
     * @param angle      圆弧对应的角度
     * @param sweepAngle 扫过的角度
     * @return 圆弧中点的x轴坐标
     */
    private float getXCoordinate(float angle, float sweepAngle) {
        float x = (float) (mRadius * Math.cos(Math.toRadians(sweepAngle - angle / 2)));
        return x;

    }

    /**
     * 获取圆弧中点的y轴坐标
     *
     * @param angle        圆弧对应的角度
     * @param sweepedAngle 扫过的角度
     * @return 圆弧中点的y轴坐标
     */
    private float getYCoordinate(float angle, float sweepedAngle) {
        float y = (float) (mRadius * Math.sin(Math.toRadians(sweepedAngle - angle / 2)));
        return y;

    }

    private float getSize() {
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        float width = display.getWidth();
        float height = display.getHeight();
        return Math.min(width, height);
    }

    public void setData(List<IPieElement> elements) {
        mElements = elements;
        setValuesAndColors();
        refreshDrawableState();
        invalidate();
    }


    /**
     * 计算角度值和各个值的占比
     */
    private void setValuesAndColors() {
        mColors.clear();
        mAngles.clear();
        mPercents.clear();
        float sum = 0;
        if (mElements != null && mElements.size() > 0) {
            for (IPieElement ele: mElements) {
                sum += ele.getValue();
                mColors.add(ele.getColor());
            }
            BigDecimal totalAngel = BigDecimal.valueOf(360);
            for (int i = 0; i < mElements.size(); i++) {
                IPieElement ele = mElements.get(i);
                BigDecimal bigDecimal = new BigDecimal(String.valueOf(ele.getValue()));
                BigDecimal sumBigDecimal = BigDecimal.valueOf(sum);
                BigDecimal res = bigDecimal.divide(sumBigDecimal, 5, BigDecimal.ROUND_HALF_UP);
                //计算角度
                BigDecimal angle = res.multiply(totalAngel);
                mAngles.add(angle.floatValue());
                //计算百分比保留两位小数并保存
                mPercents.add(bigDecimal.multiply(new BigDecimal(100)).divide(sumBigDecimal, 2, BigDecimal.ROUND_HALF_UP).toPlainString());
            }
        }

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;

        // 判断象限
        // 第一象限
        double angle = 0;

        if (x > centerX && y < centerY) {
            angle = Math.toDegrees(Math.atan((Math.abs(x - centerX)) / (Math.abs(centerY - y))));

        } else if (x > centerX && y > centerY) {//第二象限
            angle = Math.toDegrees(Math.atan(((y - centerY) / (x - centerX))));
            angle += 90;
        } else if (x < centerX && y > centerY) {//第三象限
            angle = Math.toDegrees(Math.atan(((centerX - x) / (y - centerY))));
            angle += 180;
        } else if (x < centerX && y < centerY) {//第四象限
            angle = Math.toDegrees(Math.atan(((centerY - y) / (centerX - x))));
            angle += 270;
        }

        for (int i = 0; i < angles.size(); i++) {
            double[] angs = angles.get(i);
            if (angle >= angs[0] && angle <= angs[1]) {
                Log.e("click", String.valueOf(mElements.get(i).getValue()));
            }
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}
