package com.antelope.goodbrother.business.printer.scan_detail;

import android.app.Activity;

import com.antelope.goodbrother.business.model.ScanModel;
import com.antelope.goodbrother.business.printer.main.ScanMainHolder;
import com.antelope.goodbrother.http.net.OKHttpProxy;
import com.antelope.goodbrother.http.net.ParamLtyUtil;
import com.cylty.zmkj.utils.GsonUtils;
import com.nirvana.zmkj.base.BasePresenter;

import org.greenrobot.eventbus.EventBus;

public class ScanDetailPresenter extends BasePresenter {
    public ScanDetailPresenter(Activity activity) {
        super(activity);
    }

    private ScanModel scanModel;

    public void getDetail(String code) {
        OKHttpProxy okHttpProxy = new OKHttpProxy(mActivity);
        String param = ParamLtyUtil.LtyScanning(code);
        okHttpProxy.call(param, true, false, "");
        okHttpProxy.setOnResultSuccessListener(resultModel -> {

            if (resultModel.getResult() == 0) {

                scanModel = GsonUtils.fromJson(resultModel.getData(), ScanModel.class);
                EventBus.getDefault().post(scanModel);
            } else {
                showMessageProxy.showMessageDialog(resultModel.getMessage());
            }

        });
    }

    public void receive(String code) {
        OKHttpProxy okHttpProxy = new OKHttpProxy(mActivity);
        String param = ParamLtyUtil.ReceiveLibrary(code);
        okHttpProxy.call(param, true, false, "");
        okHttpProxy.setOnResultSuccessListener(resultModel -> {
            if (resultModel.getResult() == 0) {
                print();
                showMessageProxy.showMessageDialog("打印成功！准备出餐");
            } else {
                showMessageProxy.showMessageDialog(resultModel.getMessage());
            }

        });
    }

    String line2 = "================================";

    public void print() {
//        ScanMainHolder.bluetoothFactory.PrintText("“众牧宝”注册实名小串", 2, 1, 30);
        ScanMainHolder.bluetoothFactory.PrintText("订单号:" + scanModel.getCode(), 1, 1, 20);
        ScanMainHolder.bluetoothFactory.PrintText("手机号:" + scanModel.getPhone(), 1, 1, 20);
        ScanMainHolder.bluetoothFactory.PrintText("创建时间:" + scanModel.getExchangeTime(), 1, 1, 20);
        ScanMainHolder.bluetoothFactory.PrintText(line2, 1, 1, 20);
        ScanMainHolder.bluetoothFactory.Printcolumncontent("项目", 0, "数量", 300, "", 300, "", 300);
        if (scanModel.getCount() == 1) {
            ScanMainHolder.bluetoothFactory.Printcolumncontent("注册-羊小串", 0, "x  1", 300, "", 300, "", 300);
        } else if (scanModel.getCount() == 2) {
            ScanMainHolder.bluetoothFactory.Printcolumncontent("实名-羊小串", 0, "x  2", 300, "", 300, "", 300);
        } else if (scanModel.getCount() == 3) {
            ScanMainHolder.bluetoothFactory.Printcolumncontent("注册-羊小串", 0, "x  1", 300, "", 300, "", 300);
            ScanMainHolder.bluetoothFactory.Printcolumncontent("实名-羊小串", 0, "x  2", 300, "", 300, "", 300);
        }
        ScanMainHolder.bluetoothFactory.PrintText(line2, 1, 1, 20);
        ScanMainHolder.bluetoothFactory.Printcolumncontent("小计", 0, "x   " + scanModel.getCount(), 300, "", 300, "", 300);
        ScanMainHolder.bluetoothFactory.PrintText("服务员:" + scanModel.getWaiter(), 1, 1, 20);
        ScanMainHolder.bluetoothFactory.PrintText(" ", 1, 1, 10);
        ScanMainHolder.bluetoothFactory.PrintText(" ", 1, 1, 30);
    }


    private void printDemo() {

        int interval1 = 0;//First column distance
        int interval2 = 180;//Second column distance
        int interval3 = 250;//Third column distance
        int interval4 = 300;//Fourth column distance
        int right = 100;//Fourth right alignment distance
        ScanMainHolder.bluetoothFactory.PrintText("\r\nSupermarket name", 2, 1, 30);
        ScanMainHolder.bluetoothFactory.PrintText("Serial Number:201809072006200zz7063", 1, 1, 20);
        ScanMainHolder.bluetoothFactory.PrintText("Date：2018-09-07  20:03:06", 1, 1, 20);
        ScanMainHolder.bluetoothFactory.PrintText(line2, 1, 1, 20);

        ScanMainHolder.bluetoothFactory.Printcolumncontent("name", interval1, "price", interval2, "quantity", interval3, " amount", interval4);
        ScanMainHolder.bluetoothFactory.Printcolumncontent("Ice Tea", interval1, "1.5", interval2, "1", interval3, "1.5", interval4, right);
        ScanMainHolder.bluetoothFactory.Printcolumncontent("Wahaha Milk", interval1, "25", interval2, "1", interval3, "25.0", interval4, right);
        ScanMainHolder.bluetoothFactory.Printcolumncontent("Football", interval1, "108", interval2, "1", interval3, "108.0", interval4, right);
        ScanMainHolder.bluetoothFactory.PrintText(line2, 1, 1, 20);
        ScanMainHolder.bluetoothFactory.Printcolumncontent("Payable：63.75", interval1, "", interval2, "Quantity：3", interval3, "", interval4);
        ScanMainHolder.bluetoothFactory.Printcolumncontent("Payment：63.75", interval1, "", interval2, "Send Back：0.00", interval3, "", interval4, right);
        ScanMainHolder.bluetoothFactory.PrintText(line2, 1, 1, 20);
        ScanMainHolder.bluetoothFactory.PrintText("Tel：002-64350618", 1, 1, 20);
        ScanMainHolder.bluetoothFactory.PrintText("", 1, 1, 30);
        ScanMainHolder.bluetoothFactory.PrintText("Reminder：*******xxxx***", 1, 1, 20);
        ScanMainHolder.bluetoothFactory.PrintQR("Supermarket", 6, 2);
        ScanMainHolder.bluetoothFactory.PrintText(" ", 1, 1, 10);
        ScanMainHolder.bluetoothFactory.PrintText("  ", 1, 1, 50);
        ScanMainHolder.bluetoothFactory.PrintText(" ", 1, 1, 30);
    }
}
