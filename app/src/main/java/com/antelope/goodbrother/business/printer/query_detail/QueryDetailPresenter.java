package com.antelope.goodbrother.business.printer.query_detail;

import android.app.Activity;

import com.antelope.goodbrother.business.model.QueryModel;
import com.antelope.goodbrother.http.net.OKHttpProxy;
import com.antelope.goodbrother.http.net.ParamLtyUtil;
import com.cylty.zmkj.utils.GsonUtils;
import com.nirvana.zmkj.base.BasePresenter;

import org.greenrobot.eventbus.EventBus;

public class QueryDetailPresenter extends BasePresenter {
    public QueryDetailPresenter(Activity activity) {
        super(activity);
    }

    public void receiveLibraryLog(String phone) {
        OKHttpProxy okHttpProxy = new OKHttpProxy(mActivity);
        String param = ParamLtyUtil.ReceiveLibraryLog(phone);
        okHttpProxy.call(param, true, false, "");
        okHttpProxy.setOnResultSuccessListener(resultModel -> {

            QueryModel queryModel = GsonUtils.fromJson(resultModel.getData(), QueryModel.class);
            EventBus.getDefault().post(queryModel);

        });


    }
}
