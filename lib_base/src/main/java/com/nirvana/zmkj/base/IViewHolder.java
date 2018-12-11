package com.nirvana.zmkj.base;

import android.view.View;
import android.widget.ListView;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public interface IViewHolder extends LifecycleObserver {

    /**
     * @return weather show navigation
     */
    boolean navigationIsShow();

    /**
     * @return the content layout id
     */
    @LayoutRes
    int getLayoutId();

    /**
     * @return the content layout type
     */
    int getLayoutType();

    /**
     * init content
     *
     * @param contentView the content view
     */
    void initContent(View contentView);

    /**
     * @param listView listView of init
     */
    void initListView(ListView listView);


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume();

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause();

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop();

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy();

}
