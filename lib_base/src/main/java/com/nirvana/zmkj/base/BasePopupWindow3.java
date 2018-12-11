package com.nirvana.zmkj.base;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.nirvana.zmkj.widget.ShowMessageProxy;

/**
 * Created by Administrator on 2016/12/5.
 */

public abstract class BasePopupWindow3<T> extends PopupWindow {


    protected Activity activity;
    public ShowMessageProxy showMessageProxy;
    public T model;
    private OnDismissListener onDismissListener;
    private static int mashCount;

    public void addOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public BasePopupWindow3(Activity activity) {
        super(activity);
        this.activity = activity;
        showMessageProxy = new ShowMessageProxy(activity);
        init(null);
    }

    public BasePopupWindow3(Activity activity, T model) {
        super(activity);
        this.activity = activity;
        showMessageProxy = new ShowMessageProxy(activity);
        this.model = model;
        init(model);
    }

    private void init(T model) {
        View contentView = LayoutInflater.from(activity).inflate(getLayout(),null);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        if (maskLLShow()) {
            mashCount++;
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.alpha = 0.3f;
            activity.getWindow().setAttributes(lp);
        }
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (maskLLShow()) {
                    mashCount--;
                    if (mashCount == 0) {
                        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                        lp.alpha = 1f;
                        activity.getWindow().setAttributes(lp);
                    }
                }
                if (onDismissListener != null) {
                    onDismissListener.onDismiss();
                }
            }
        });
        initView(contentView, model);
    }

    protected abstract int getLayout();

    protected abstract void initView(View contentView, T model);

    protected abstract boolean maskLLShow();
}
