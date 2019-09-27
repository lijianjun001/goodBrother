package com.antelope.goodbrother.business.printer.query_detail;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.antelope.goodbrother.R;
import com.antelope.goodbrother.business.model.QueryModel;
import com.antelope.goodbrother.business.printer.main.ScanMainActivity;
import com.cylty.zmkj.utils.StringUtils;
import com.nirvana.zmkj.base.BaseViewHolder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.appcompat.app.AppCompatActivity;

public class QueryDetailHolder extends BaseViewHolder<QueryDetailPresenter> implements View.OnClickListener {


    private TextView telTv, changeTimeTv, waiterTv;
    private RelativeLayout registerRl, realNameRl;

    public QueryDetailHolder(AppCompatActivity activity, Bundle bundle) {
        super(activity, bundle);
    }

    public QueryDetailHolder(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    protected QueryDetailPresenter createPresenter() {
        return new QueryDetailPresenter(activity);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_query_detail;
    }

    @Override
    public void initContent(View contentView) {

        String tel = bundle.getString(ScanMainActivity.TELPHONE);
        presenter.receiveLibraryLog(tel);
        contentView.findViewById(R.id.close_iv).setOnClickListener(this);
        telTv = contentView.findViewById(R.id.tel_tv);
        changeTimeTv = contentView.findViewById(R.id.change_time_tv);
        waiterTv = contentView.findViewById(R.id.waiter_tv);
        registerRl = contentView.findViewById(R.id.register_rl);
        realNameRl = contentView.findViewById(R.id.realname_rl);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_iv:
                activity.finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResult(QueryModel queryModel) {

        telTv.setText(queryModel.getPhone());
        String exTime = queryModel.getExchangeTime();
        String[] extimes;
        if (exTime.contains("|")) {
            extimes = exTime.split("\\|");
            if (extimes.length == 2 && !StringUtils.isEmpty(extimes[1])) {
                exTime = extimes[0] + "\n" + extimes[1];
            } else {
                exTime = extimes[0];
            }
        }
        changeTimeTv.setText(exTime);
        waiterTv.setText(queryModel.getWaiter());

        switch (queryModel.getCount()) {
            case 1:
                registerRl.setVisibility(View.VISIBLE);
                break;
            case 2:
                realNameRl.setVisibility(View.VISIBLE);
                break;
            case 3:
                registerRl.setVisibility(View.VISIBLE);
                realNameRl.setVisibility(View.VISIBLE);
                break;
        }
    }
}

