package com.antelope.goodbrother.widget;

import com.alibaba.android.vlayout.VirtualLayoutManager;

import androidx.recyclerview.widget.RecyclerView;


public class LoadMoreRecycleScrollListener extends RecyclerView.OnScrollListener {
    private boolean hasMore = true;
    private int totalPage;
    private int currentPage = 1;

    public int getTotalPage() {
        return totalPage;
    }

    private OnLoadMoreListener onLoadMoreListener;

    public LoadMoreRecycleScrollListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public void resetPage() {
        this.currentPage = 1;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (hasMore) {
            VirtualLayoutManager lm = (VirtualLayoutManager) recyclerView.getLayoutManager();
            int last, total;
            last = lm.findLastVisibleItemPosition();
            total = recyclerView.getAdapter().getItemCount();
            if (last > 0 && last >= total - 1) {
                recyclerView.post(() -> {
                    if (currentPage <= totalPage) {
                        currentPage++;
                        onLoadMoreListener.onLoadMore(currentPage);
                    } else {
                        hasMore = false;
                        onLoadMoreListener.onLoadComplete();
                    }
                });

            }
        }
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int pageIndex);

        void onLoadComplete();
    }
}
