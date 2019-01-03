package com.antelope.goodbrother.business.main;

import android.view.View;

import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.antelope.goodbrother.R;
import com.antelope.goodbrother.widget.LoadMoreRecycleScrollListener;
import com.antelope.goodbrother.widget.LoadMoreRecycleView;
import com.nirvana.zmkj.base.BaseViewHolder;
import com.nirvana.zmkj.manager.Page;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class MainViewHolder extends BaseViewHolder<MainPresenter> {
    private LoadMoreRecycleView recyclerView;
    private List<MainEntity> mainEntities;
    private MainAdapter mainAdapter;

    public MainViewHolder(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(activity);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    public void initContent(View contentView) {
        initNavigation(R.drawable.white_back, R.color.navigation_bg, "首页", R.color.white);
        recyclerView = contentView.findViewById(R.id.recyclerView);
        mainEntities = new ArrayList<>();
        mainAdapter = new MainAdapter(activity, new LinearLayoutHelper(10), mainEntities);
        recyclerView.setContentAdapter(mainAdapter);
        recyclerView.setOnLoadMoreListener(new LoadMoreRecycleScrollListener.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int pageIndex) {
                presenter.getGiftIndex(pageIndex);
            }

            @Override
            public void onLoadComplete() {

            }
        });
        presenter.getGiftIndex(Page.PAGE_INIT);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void result(MainModel mainModel) {
        recyclerView.setTotalPage(mainModel.getTotalPage());
        mainEntities.addAll(mainModel.getList());
        mainAdapter.notifyDataSetChanged();
    }


}
