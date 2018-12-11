package com.cylty.zhongmukeji.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;

public class GridViewForScrollView extends GridView{

    public GridViewForScrollView(Context pContext, AttributeSet pAttrs) {
        super(pContext, pAttrs);
    }

    public GridViewForScrollView(Context pContext) {
        super(pContext);
    }

    public GridViewForScrollView(Context pContext, AttributeSet pAttrs, int pDefStyle) {
        super(pContext, pAttrs, pDefStyle);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
