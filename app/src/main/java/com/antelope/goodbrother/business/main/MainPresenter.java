package com.antelope.goodbrother.business.main;

import android.app.Activity;

import com.nirvana.zmkj.base.BasePresenter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter extends BasePresenter {
    public MainPresenter(Activity activity) {
        super(activity);
    }

    public void getGiftIndex(int pageIndex) {
//        showMessageProxy.startProgressDialog("正在获取数据");
        MainModel mainModel = new MainModel();
        mainModel.setTotalPage(5);
        List<MainEntity> mainEntities = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mainEntities.add(new MainEntity("", "菜鸟" + pageIndex, "http://www.runoob.com/"));
        }
        mainModel.setList(mainEntities);
        EventBus.getDefault().post(mainModel);
    }
}
