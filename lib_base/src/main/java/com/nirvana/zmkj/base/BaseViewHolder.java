package com.nirvana.zmkj.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nirvana.ylmc.lib_base.R;
import com.nirvana.zmkj.receiver.NetModel;
import com.nirvana.zmkj.receiver.NetworkReceiver;
import com.nirvana.zmkj.widget.Navigation;
import com.nirvana.zmkj.widget.ShowMessageProxy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseViewHolder<P extends BasePresenter> implements IViewHolder {

    protected AppCompatActivity activity;
    protected P presenter;
    private View rootView;
    private IViewHolder iViewHolder;
    private Navigation navigation;
    public static final int LAYOUT_TYPE_COMMON = 1;
    public static final int LAYOUT_TYPE_LIST = 2;

    @IntDef({LAYOUT_TYPE_COMMON, LAYOUT_TYPE_LIST})
    @Retention(RetentionPolicy.SOURCE)
    @interface LayoutType {
    }

    private ListView listView;
    private LinearLayout contentLl;
    public ShowMessageProxy showMessageProxy;
    private TextView noNetTv;
    protected Bundle bundle;

    public BaseViewHolder(final AppCompatActivity activity, Bundle bundle) {
        this.activity = activity;
        this.bundle = bundle;
        init();
    }

    public BaseViewHolder(final AppCompatActivity activity) {
        this.activity = activity;
        init();
    }

    private void init() {
        activity.getLifecycle().addObserver(this);
        EventBus.getDefault().register(this);
        presenter = createPresenter();
        iViewHolder = this;
        showMessageProxy = new ShowMessageProxy(activity);
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        rootView = layoutInflater.inflate(R.layout.base_layout, null);
        navigation = rootView.findViewById(R.id.navigation);
        navigation.setOnBackListener(new Navigation.OnBackListener() {
            @Override
            public void OnBack() {

                activity.finish();
            }
        });
        if (iViewHolder.navigationIsShow()) {
            navigation.setVisibility(View.VISIBLE);
        } else {
            navigation.setVisibility(View.GONE);
        }
        noNetTv = rootView.findViewById(R.id.no_net_tv);
        if (iViewHolder.getLayoutType() == LAYOUT_TYPE_COMMON || iViewHolder.getLayoutType() == 0) {
            contentLl = rootView.findViewById(R.id.content_ll);
            contentLl.setVisibility(View.VISIBLE);
            View contentView = layoutInflater.inflate(iViewHolder.getLayoutId(), null);
            contentLl.addView(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            iViewHolder.initContent(contentView);
        } else if (iViewHolder.getLayoutType() == LAYOUT_TYPE_LIST) {
            listView = rootView.findViewById(R.id.listView);
            listView.setVisibility(View.VISIBLE);
            initListView(listView);
        }
    }


    public View getRootView() {
        return rootView;
    }


    protected void setNavigationTitle(String title) {
        if (navigation != null) {
            navigation.setTitle(title);
        }
    }

    protected void setNavigationBackBg(int drawable) {
        if (navigation != null) {
            navigation.setBackBg(drawable);
        }
    }

    public void setNavigationBg(int color) {
        navigation.setBackground(color);
    }

    public void setNavigationTitleColor(int color) {
        navigation.setNavigationTitleColor(color);
    }

    protected void setNavigationVisible(boolean visibility) {
        if (visibility) {
            navigation.setVisibility(View.VISIBLE);
        } else {
            navigation.setVisibility(View.GONE);
        }
    }

    protected void setNoNetTvVisible(boolean visibility) {
        if (visibility) {
            noNetTv.setVisibility(View.VISIBLE);
        } else {
            noNetTv.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void netAvisible(NetModel netModel) {
        if (netModel.getState() == NetworkReceiver.UNAVAILABLE) {
            setNoNetTvVisible(true);
        } else {
            setNoNetTvVisible(false);
        }
    }

    //用于创建Presenter和判断是否使用MVP模式(由子类实现)
    protected abstract P createPresenter();

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        if (presenter != null) {
            presenter.clear();
        }
        EventBus.getDefault().unregister(this);
    }


    @Override
    public boolean navigationIsShow() {
        return false;
    }

    @Override
    public @LayoutType
    int getLayoutType() {
        return LAYOUT_TYPE_COMMON;
    }

    @Override
    public void initListView(ListView listview) {

    }

    public void setRightIv(int src, View.OnClickListener onClickListener) {
        navigation.setRightIvSrc(src);
        navigation.setRightIvOnclickListener(onClickListener);
    }


    protected void initNavigation(int BackBg, int bg, String title, int titleColor) {
        setNavigationVisible(true);
        setNavigationBackBg(BackBg);
        setNavigationBg(bg);
        setNavigationTitle(title);
        setNavigationTitleColor(titleColor);
    }
}
