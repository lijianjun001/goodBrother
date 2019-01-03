package com.cylty.zmkj.okHttp;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxSchedulerHelper {

    public static <T> Observable.Transformer<T, T> io_main() {

        return tObservable -> tObservable
                // 生产线程
                .subscribeOn(Schedulers.io())
                // 消费线程
                .observeOn(AndroidSchedulers.mainThread());
    }
}