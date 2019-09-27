package com.antelope.goodbrother.business.printer.scan_detail;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.antelope.goodbrother.R;
import com.antelope.goodbrother.business.model.ScanModel;
import com.antelope.goodbrother.business.printer.main.ScanMainActivity;
import com.nirvana.zmkj.base.BaseViewHolder;
import com.nirvana.zmkj.widget.CustomDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.appcompat.app.AppCompatActivity;

public class ScanDetailHolder extends BaseViewHolder<ScanDetailPresenter> implements View.OnClickListener {


    private TextView telTv, codeTv, registerTv, changeTv, waiterTv, countTv;
    private RelativeLayout registerRl, realNameRl;
    private String code;

    public ScanDetailHolder(AppCompatActivity activity, Bundle bundle) {
        super(activity, bundle);
    }

    public ScanDetailHolder(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    protected ScanDetailPresenter createPresenter() {
        return new ScanDetailPresenter(activity);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_scan_detail;
    }

    @Override
    public void initContent(View contentView) {
        telTv = contentView.findViewById(R.id.tel_tv);
        codeTv = contentView.findViewById(R.id.code_tv);
        registerTv = contentView.findViewById(R.id.register_tv);
        changeTv = contentView.findViewById(R.id.change_tv);
        waiterTv = contentView.findViewById(R.id.waiter_tv);
        countTv = contentView.findViewById(R.id.count_tv);
        registerRl = contentView.findViewById(R.id.register_rl);
        realNameRl = contentView.findViewById(R.id.realname_rl);
        contentView.findViewById(R.id.close_iv).setOnClickListener(this);
        contentView.findViewById(R.id.print_btn).setOnClickListener(this);
        contentView.findViewById(R.id.back_btn).setOnClickListener(this);

        code = bundle.getString(ScanMainActivity.KEY_SCAN_CODE);
        presenter.getDetail(code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.close_iv:
            case R.id.back_btn:

                CustomDialog.Builder builder = new CustomDialog.Builder(activity);
                final CustomDialog dialog = builder.setMessage("您确定退出订单页面？").setPositive("坚持退出").setNegative("查看订单").createTwoButtonDialog();
                dialog.setOnDiaLogClickListener(new CustomDialog.OnDiaLogClickListener() {
                    @Override
                    public void onPositive() {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                            activity.finish();
                        }
                    }

                    @Override
                    public void onNegative() {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
                break;

            case R.id.print_btn:
                presenter.receive(code);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResult(ScanModel scanModel) {


        if (scanModel.isReceive()) {
            showMessageProxy.showMessageDialog("已经领取");
            return;
        }
        telTv.setText(scanModel.getPhone());
        codeTv.setText(scanModel.getCode());
        registerTv.setText(scanModel.getRegisterTime());
        changeTv.setText(scanModel.getExchangeTime());
        waiterTv.setText(scanModel.getWaiter());
        countTv.setText(scanModel.getCount() + "");

        switch (scanModel.getCount()) {
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

