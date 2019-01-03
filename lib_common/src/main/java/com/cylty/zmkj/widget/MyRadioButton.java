package com.cylty.zmkj.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.cylty.zmkj.R;


public class MyRadioButton extends RadioButton {

    private int mDrawableWidth, mDrawableHeight;
    private Drawable drawableLeft = null, drawableTop = null, drawableRight = null, drawableBottom = null;
    public MyRadioButton(Context context) {
        this(context, null, 0);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MyRadioButton);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.MyRadioButton_drawableWidth) {
                mDrawableWidth = a.getDimensionPixelSize(R.styleable.MyRadioButton_drawableWidth, 50);
            } else if (attr == R.styleable.MyRadioButton_drawableHeight) {
                mDrawableHeight = a.getDimensionPixelSize(R.styleable.MyRadioButton_drawableHeight, 50);
            } else if (attr == R.styleable.MyRadioButton_drawableTop) {
                drawableTop = a.getDrawable(attr);
            } else if (attr == R.styleable.MyRadioButton_drawableBottom) {
                drawableRight = a.getDrawable(attr);
            } else if (attr == R.styleable.MyRadioButton_drawableRight) {
                drawableBottom = a.getDrawable(attr);
            } else if (attr == R.styleable.MyRadioButton_drawableLeft) {
                drawableLeft = a.getDrawable(attr);
            }
        }
        a.recycle();
        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    public void setDrawableWidth(int drawableWidth) {
        mDrawableWidth = drawableWidth;
        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    public void setDrawableHeight(int drawableHeight) {
        mDrawableHeight = drawableHeight;
        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left,
                                                        Drawable top, Drawable right, Drawable bottom) {
        this.drawableTop=top;
        if (left != null) {
            left.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
        if (right != null) {
            right.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
        if (top != null) {
            top.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, mDrawableWidth, mDrawableHeight);
        }
        setCompoundDrawables(left, top, right, bottom);
    }

}
