package com.cylty.zmkj.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView {
    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCallbacks != null) {
            mCallbacks.onScrollChanged(t, oldt);
        }
    }

    public interface Callbacks {
        void onScrollChanged(int scrollY, int oldY);
    }

    private Callbacks mCallbacks;

    public void setCallbacks(Callbacks callbacks) {
        mCallbacks = callbacks;
    }


    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }
}
