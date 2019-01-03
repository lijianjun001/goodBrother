package com.antelope.goodbrother.widget;

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
    private LoadMoreRecycleScrollListener.OnLoadMoreListener onLoadMoreListener;//外部传入的
    private LoadMoreRecycleScrollListener loadMoreRecycleScrollListener;//内部实例化的

    public void setOnLoadMoreListener(LoadMoreRecycleScrollListener.OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public LoadMoreRecycleView(Context context) {
        super(context);
        init(context);
    }

    public LoadMoreRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadMoreRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    private void init(Context context) {
        loadData = new ArrayList<>();
        loadData.add(FootViewAdapter.LOADING);
        footViewAdapter = new FootViewAdapter(context, new LinearLayoutHelper(0), loadData);
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

    public void setContentAdapter(BaseRecyclerAdapter baseRecyclerAdapter) {
        delegateAdapter.addAdapter(baseRecyclerAdapter);
        delegateAdapter.notifyDataSetChanged();
    }

    boolean hasAddFooter = false;

    public void setTotalPage(int totalPage) {
        loadMoreRecycleScrollListener.setTotalPage(totalPage);
        if (totalPage > 0 && !hasAddFooter) {
            hasAddFooter = true;
            delegateAdapter.addAdapter(footViewAdapter);
        }
    }

    private FootViewAdapter footViewAdapter;

    private void complete() {
        if (footViewAdapter != null) {
            loadData.clear();
            loadData.add(FootViewAdapter.LOAD_COMPLETE);
            footViewAdapter.notifyDataSetChanged();
        }
    }

    public void resetPage() {
        loadMoreRecycleScrollListener.resetPage();
        loadData.clear();
        loadData.add(FootViewAdapter.LOADING);
        delegateAdapter.removeAdapter(footViewAdapter);
    }
}
