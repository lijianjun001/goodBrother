package com.antelope.goodbrother.business.main;

import android.view.View;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.antelope.goodbrother.R;
import com.antelope.goodbrother.widget.FootViewAdapter;
import com.antelope.goodbrother.widget.LoadMoreRecycleScrollListener;
import com.nirvana.zmkj.base.BaseViewHolder;
import com.nirvana.zmkj.manager.Page;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


public class MainViewHolder2 extends BaseViewHolder<MainPresenter> {
    private RecyclerView recyclerView;
    private DelegateAdapter delegateAdapter;
    private List<MainEntity> mainEntities;
    private MainAdapter mainAdapter;
    private FootViewAdapter footViewAdapter;
    private List<String>footerData;
    private LoadMoreRecycleScrollListener loadMoreRecycleScrollListener;

    public MainViewHolder2(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(activity);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initContent(View contentView) {
        initNavigation(R.drawable.white_back, R.color.navigation_bg, "首页", R.color.white);
        recyclerView = contentView.findViewById(R.id.recyclerView);
        VirtualLayoutManager linearLayoutManager = new VirtualLayoutManager(activity);
        delegateAdapter = new DelegateAdapter(linearLayoutManager);
        recyclerView.setLayoutManager(linearLayoutManager);
        mainEntities = new ArrayList<>();
        mainAdapter = new MainAdapter(activity, new LinearLayoutHelper(0), mainEntities);
        delegateAdapter.addAdapter(mainAdapter);
        footerData=new ArrayList<>();
        footerData.add(FootViewAdapter.LOADING);
        footViewAdapter=new FootViewAdapter(activity,new LinearLayoutHelper(0),footerData);
        delegateAdapter.addAdapter(footViewAdapter);
        recyclerView.setAdapter(delegateAdapter);
        presenter.getGiftIndex(Page.PAGE_INIT);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void result(MainModel mainModel) {

        if (loadMoreRecycleScrollListener == null) {
            loadMoreRecycleScrollListener = new LoadMoreRecycleScrollListener(new LoadMoreRecycleScrollListener.OnLoadMoreListener() {
                @Override
                public void onLoadMore(int pageIndex) {
                    presenter.getGiftIndex(pageIndex);
                }

                @Override
                public void onLoadComplete() {
                    footerData.clear();
                    footerData.add(FootViewAdapter.LOAD_COMPLETE);
                    footViewAdapter.notifyDataSetChanged();
                }
            });
            loadMoreRecycleScrollListener.setTotalPage(mainModel.getTotalPage());
            recyclerView.addOnScrollListener(loadMoreRecycleScrollListener);
        }

        mainEntities.addAll(mainModel.getList());
        mainAdapter.notifyDataSetChanged();
    }


}
