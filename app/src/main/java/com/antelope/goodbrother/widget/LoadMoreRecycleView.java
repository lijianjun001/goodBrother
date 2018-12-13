package com.antelope.goodbrother.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.nirvana.zmkj.base.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class LoadMoreRecycleView extends RecyclerView {
    private List<String> loadData;
    private DelegateAdapter delegateAdapter;
    private LoadMoreRecycleScrollListener.OnLoadMoreListener onLoadMoreListener;
    private LoadMoreRecycleScrollListener loadMoreRecycleScrollListener;

    public void setOnLoadMoreListener(LoadMoreRecycleScrollListener.OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public LoadMoreRecycleView(Context context) {
        super(context);
        init();
    }

    public LoadMoreRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    public void init() {
        loadData = new ArrayList<>();
        loadData.add(FootViewAdapter.LOADING);
        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(getContext());
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        this.setLayoutManager(virtualLayoutManager);
        this.setAdapter(delegateAdapter);
        loadMoreRecycleScrollListener = new LoadMoreRecycleScrollListener(new LoadMoreRecycleScrollListener.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int pageIndex) {
                if (onLoadMoreListener != null) {
                    onLoadMoreListener.onLoadMore(pageIndex);
                }
            }

            @Override
            public void onLoadComplete() {
                complete();
            }
        });
        addOnScrollListener(loadMoreRecycleScrollListener);
    }

    public void addContentAdapter(BaseRecyclerAdapter baseRecyclerAdapter) {
        delegateAdapter.addAdapter(baseRecyclerAdapter);
        delegateAdapter.notifyDataSetChanged();
    }

    public void setTotalPage(int totalPage) {
        loadMoreRecycleScrollListener.setTotalPage(totalPage);
    }

    FootViewAdapter footViewAdapter;

    public void addFooter(Activity activity) {
        footViewAdapter = new FootViewAdapter(activity, new LinearLayoutHelper(0), loadData);
        delegateAdapter.addAdapter(footViewAdapter);
    }

    private void complete() {
        loadData.clear();
        loadData.add(FootViewAdapter.LOAD_COMPLETE);
        footViewAdapter.notifyDataSetChanged();
        delegateAdapter.addAdapter(footViewAdapter);
    }
}
