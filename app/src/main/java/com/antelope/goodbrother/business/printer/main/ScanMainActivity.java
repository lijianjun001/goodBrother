package com.antelope.goodbrother.business.printer.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.antelope.goodbrother.business.printer.scan_detail.ScanDetailActivity;
import com.nirvana.zmkj.base.BaseActivity;
import com.nirvana.zmkj.manager.SharePreferenceManager;
import com.yzq.zxinglibrary.common.Constant;

import androidx.annotation.NonNull;

public class ScanMainActivity extends BaseActivity {

    public static final int REQUEST_CODE_SCAN = 1;
    public static final int REQUEST_PERMISSION_CAMERA_CODE = 2;
    public static final int REQUEST_CONNECT_PRINTER = 3;
    public static final int REQUEST_PERMISSION_BLUETH = 4;
    public static final String connectKey = "connectKey";
    public static final String TELPHONE = "tel";
    public static final String KEY_SCAN_CODE = "keyScanCode";
    private ScanMainHolder scanMainHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scanMainHolder = new ScanMainHolder(this);
        setContentView(scanMainHolder.getRootView());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                Log.d("扫描结果为", content);
                Intent intent = new Intent(this, ScanDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(KEY_SCAN_CODE, content);
                intent.putExtras(bundle);
                this.startActivity(intent);
            }
        }

        if (requestCode == REQUEST_CONNECT_PRINTER && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                Log.d("扫描结果为", content);

                SharePreferenceManager.getInstance().putString(connectKey, content);


                scanMainHolder.requestPermissionOrStartBlueth(content);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_CAMERA_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanMainHolder.goCapture();
            }
        }
    }
}

