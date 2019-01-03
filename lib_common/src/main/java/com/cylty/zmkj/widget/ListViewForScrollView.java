package com.cylty.zmkj.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import com.cylty.zmkj.R;

public class ListViewForScrollView extends ListView implements OnScrollListener {
    private OnLoadMoreListener loadMoreListener;
    private boolean isComplete = true;
    private boolean isCanLoadMore = true;
    private int tempFirstVisibleItem;
    private TextView footerTv;

    public ListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ListViewForScrollView(Context context) {
        super(context);
        init(context);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.setOnScrollListener(this);
        initFooter(context);
    }

    private void initFooter(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_footer, null);
        footerTv = view.findViewById(R.id.footer_tv);
        addFooterView(view);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptHoverEvent(ev);
    }

    //滑动最后到一条数据可见时，自动刷新数据
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (isCanLoadMore) {
            if (this.tempFirstVisibleItem != firstVisibleItem) {
                this.tempFirstVisibleItem = firstVisibleItem;
                if ((this.tempFirstVisibleItem + visibleItemCount) == totalItemCount && isComplete) {
                    if (loadMoreListener != null) {
                        isComplete = false;
                        loadMoreListener.onLoadMore();
                    }
                }
            }
        }
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void setCanLoadmore(boolean isCanLoadMore) {
        this.isCanLoadMore = isCanLoadMore;

        if (isCanLoadMore){
            footerTv.setVisibility(VISIBLE);
        }else{
            footerTv.setVisibility(GONE);
        }
    }

    public void setLoadMoreComplete() {
        isComplete = true;
        footerTv.setText("我也是有底线的 ~");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
