package com.antelope.goodbrother.business.printer.main;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.antelope.goodbrother.R;
import com.antelope.goodbrother.business.printer.query_detail.QueryDetailActivity;
import com.antelope.goodbrother.config.PermissionConstant;
import com.antelope.goodbrother.utils.PermissionUtil;
import com.cylty.zmkj.utils.StringUtils;
import com.nirvana.zmkj.base.BaseViewHolder;
import com.nirvana.zmkj.manager.SharePreferenceManager;
import com.szzk.dp_nbtlibs.BluetoothFactory;
import com.yzq.zxinglibrary.android.CaptureActivity;

import androidx.appcompat.app.AppCompatActivity;

public class ScanMainHolder extends BaseViewHolder<ScanMainPresenter> implements View.OnClickListener {

    private Handler handler;
    public static BluetoothFactory bluetoothFactory;
    private EditText telPhone;
    private FrameLayout connectFl, testFl;


    public ScanMainHolder(AppCompatActivity activity, Bundle bundle) {
        super(activity, bundle);
    }

    public ScanMainHolder(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    protected ScanMainPresenter createPresenter() {
        return new ScanMainPresenter(activity);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main_scan;
    }

    @Override
    public void initContent(View contentView) {
        handler = new MyHandler();
        bluetoothFactory = BluetoothFactory.getBluetoothFactory(activity, handler);
        connectFl = contentView.findViewById(R.id.connect_fl);
        connectFl.setOnClickListener(this);
        testFl = contentView.findViewById(R.id.test_fl);
        testFl.setOnClickListener(this);
        contentView.findViewById(R.id.scan_fl).setOnClickListener(this);
        contentView.findViewById(R.id.query_btn).setOnClickListener(this);
        telPhone = contentView.findViewById(R.id.tel_et);


    }

    @Override
    public void onResume() {
        super.onResume();
        String mac = SharePreferenceManager.getInstance().getString(ScanMainActivity.connectKey);
        requestPermissionOrStartBlueth(mac);
        if (StringUtils.isEmpty(mac)) {
            connectFl.setVisibility(View.VISIBLE);
//            testFl.setVisibility(View.VISIBLE);
        } else {
            connectFl.setVisibility(View.GONE);
//            testFl.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connect_fl:
                requestPermissionOrOpenBlueth();
                break;

            case R.id.test_fl:
                printDemo();
                break;
            case R.id.scan_fl:
                requestPermissionOrOpenCamera();
                break;
            case R.id.query_btn:
//                bluetoothFactory.getVoltage();
                String tel = telPhone.getText().toString();
                if (!StringUtils.isEmpty(tel)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ScanMainActivity.TELPHONE, tel);
                    Intent intent2 = new Intent(activity, QueryDetailActivity.class);
                    intent2.putExtras(bundle);
                    activity.startActivity(intent2);
                } else {
                    showMessageProxy.displayToast("手机号不能为空");
                }

                break;
        }
    }

    private void requestPermissionOrOpenCamera() {

        PermissionUtil.requestPermissionOrDoAction(() -> goCapture(), PermissionConstant.KEK_HAS_REQ_CAMERA, Manifest.permission.CAMERA, PermissionConstant.REQ_CAMERA_NOTICE, activity, ScanMainActivity.REQUEST_PERMISSION_CAMERA_CODE);
    }


    private void requestPermissionOrOpenBlueth() {
        PermissionUtil.requestPermissionOrDoAction(() -> {
            PermissionUtil.requestPermissionOrDoAction(() -> goConnct(), PermissionConstant.KEK_HAS_REQ_BULETH, Manifest.permission.BLUETOOTH, PermissionConstant.REQ_BULETH_NOTICE, activity, ScanMainActivity.REQUEST_PERMISSION_BLUETH);

        }, PermissionConstant.KEK_HAS_REQ_CAMERA, Manifest.permission.CAMERA, PermissionConstant.REQ_CAMERA_NOTICE, activity, ScanMainActivity.REQUEST_PERMISSION_CAMERA_CODE);

    }

    public void requestPermissionOrStartBlueth(String mac) {
        PermissionUtil.requestPermissionOrDoAction(() -> connect(mac), PermissionConstant.KEK_HAS_REQ_BULETH, Manifest.permission.BLUETOOTH, PermissionConstant.REQ_BULETH_NOTICE, activity, ScanMainActivity.REQUEST_PERMISSION_BLUETH);

    }


    public void goCapture() {
        Intent intent = new Intent(activity, CaptureActivity.class);
        activity.startActivityForResult(intent, ScanMainActivity.REQUEST_CODE_SCAN);
    }

    public void goConnct() {
        Intent intent = new Intent(activity, CaptureActivity.class);
        activity.startActivityForResult(intent, ScanMainActivity.REQUEST_CONNECT_PRINTER);
    }

    public void connect(String mac) {

        if (invald()) {
            bluetoothFactory.connectbt(mac, true);
        }

    }

    private boolean invald() {
        BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
        if (blueadapter == null) {
            showMessageProxy.showMessageDialog("此设备不支持狼牙");
            return false;
        }
        if (!blueadapter.isEnabled()) {
            showMessageProxy.showMessageDialog("请先打开手机狼牙");
            return false;
        }
        return true;
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothFactory.CONNECT_STATE: {  //打印机连接状态
                    if (msg.getData().getString("state").equals("1")) {//连接成功
                        showMessageProxy.displayToast("连接打印机成功");
                    }
                    break;
                }
                case BluetoothFactory.CHECKPAGE_RESULT: {//打印机纸张状态，缺纸，有纸
//                    if (msg.getData().getString("state").equals("1")) {//有纸
//                        showMessageProxy.displayToast("有纸");
//                    }
                    break;
                }
                case BluetoothFactory.VOLTAGE: {//打印机电量
                    String Voltage = msg.getData().getString("Voltage");
                    showMessageProxy.displayToast(Voltage);
                    Log.d("Voltage", Voltage);
                    break;
                }
                case BluetoothFactory.PRINTSTATE: {//打印状态
//                    boolean printstate = msg.getData().getBoolean("printstate");
//                    showMessageProxy.displayToast(printstate + "");
                    break;
                }
            }
        }
    }

    String line2 = "================================";

    private void printDemo() {

        int interval1 = 0;//First column distance
        int interval2 = 180;//Second column distance
        int interval3 = 250;//Third column distance
        int interval4 = 300;//Fourth column distance
        int right = 100;//Fourth right alignment distance
        bluetoothFactory.PrintText("\r\nSupermarket name", 2, 1, 30);
        bluetoothFactory.PrintText("Serial Number:201809072006200zz7063", 1, 1, 20);
        bluetoothFactory.PrintText("Date：2018-09-07  20:03:06", 1, 1, 20);
        bluetoothFactory.PrintText(line2, 1, 1, 20);

        bluetoothFactory.Printcolumncontent("name", interval1, "price", interval2, "quantity", interval3, " amount", interval4);
        bluetoothFactory.Printcolumncontent("Ice Tea", interval1, "1.5", interval2, "1", interval3, "1.5", interval4, right);
        bluetoothFactory.Printcolumncontent("Wahaha Milk", interval1, "25", interval2, "1", interval3, "25.0", interval4, right);
        bluetoothFactory.Printcolumncontent("Football", interval1, "108", interval2, "1", interval3, "108.0", interval4, right);
        bluetoothFactory.PrintText(line2, 1, 1, 20);
        bluetoothFactory.Printcolumncontent("Payable：63.75", interval1, "", interval2, "Quantity：3", interval3, "", interval4);
        bluetoothFactory.Printcolumncontent("Payment：63.75", interval1, "", interval2, "Send Back：0.00", interval3, "", interval4, right);
        bluetoothFactory.PrintText(line2, 1, 1, 20);
        bluetoothFactory.PrintText("Tel：002-64350618", 1, 1, 20);
        bluetoothFactory.PrintText("", 1, 1, 30);
        bluetoothFactory.PrintText("Reminder：*******xxxx***", 1, 1, 20);
        bluetoothFactory.PrintQR("Supermarket", 6, 2);
        bluetoothFactory.PrintText(" ", 1, 1, 10);
        bluetoothFactory.PrintText("  ", 1, 1, 50);
        bluetoothFactory.PrintText(" ", 1, 1, 30);
    }
}

