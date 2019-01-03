package com.cylty.zmkj.okHttp;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/12/31.
 */

public class RxManager {
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();// 管理订阅者

    public void add(Subscription m) {
        mCompositeSubscription.add(m);
    }

    public void clear() {
        mCompositeSubscription.unsubscribe();// 取消订阅
    }
}
