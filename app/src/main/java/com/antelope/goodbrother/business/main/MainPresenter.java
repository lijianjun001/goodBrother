package com.antelope.goodbrother.business.main;

import android.app.Activity;
import android.util.LruCache;

import com.nirvana.zmkj.base.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter extends BasePresenter {
    public MainPresenter(Activity activity) {
        super(activity);
    }

    public void getGiftIndex(int pageIndex) {
        showMessageProxy.startProgressDialog("正在获取数据");
        try {
            Thread.sleep(1000);
            showMessageProxy.stopProgressDialog();
            MainModel mainModel = new MainModel();
            mainModel.setTotalPage(5);
            List<MainEntity> mainEntities = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                mainEntities.add(new MainEntity("", "菜鸟" + pageIndex, "http://www.runoob.com/"));
            }
            for (int i = 0; i < 10; i++) {
                mainEntities.add(new MainEntity("", "flutter" + pageIndex, ""));
            }
            mainModel.setList(mainEntities);
            EventBus.getDefault().post(mainModel);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
