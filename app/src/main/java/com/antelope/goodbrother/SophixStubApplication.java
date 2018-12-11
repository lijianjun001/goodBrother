package com.antelope.goodbrother;

import android.content.Context;
import android.util.Log;

import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixApplication;
import com.taobao.sophix.SophixEntry;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

import androidx.annotation.Keep;
import androidx.multidex.MultiDex;

/**
 * Sophix入口类，专门用于初始化Sophix，不应包含任何业务逻辑。
 * 此类必须继承自SophixApplication，onCreate方法不需要实现。
 * 此类不应与项目中的其他类有任何互相调用的逻辑，必须完全做到隔离。
 * AndroidManifest中设置application为此类，而SophixEntry中设为原先Application类。
 * 注意原先Application里不需要再重复初始化Sophix，并且需要避免混淆原先Application类。
 * 如有其它自定义改造，请咨询官方后妥善处理。
 */
public class SophixStubApplication extends SophixApplication {
    private final String TAG = "SophixStubApplication";

    // 此处SophixEntry应指定真正的Application，并且保证RealApplicationStub类名不被混淆。
    @Keep
    @SophixEntry(MyApplication.class)
    static class RealApplicationStub {
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//         如果需要使用MultiDex，需要在此处调用。
        MultiDex.install(this);
        initSophix();
    }

    private void initSophix() {
        String appVersion = "0.0.0";
        try {
            appVersion = this.getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0)
                    .versionName;
        } catch (Exception e) {
        }
        final SophixManager instance = SophixManager.getInstance();
        instance.setContext(this)
                .setAppVersion(appVersion)
                .setSecretMetaData("25231195", "e2e1ec6e3ed24ca41ac778433679f406", "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCqGkO5L0La24Z8oVzWF4qyjw48PHbASRpn5eJQ3jYiXX82kHQ2XpvXab0gLpUOooXm9kuuA6t0GUNngF7pcUO96ROJvwXLyDDm7kTqbzNG26xigyrAqpeckBtRlyCjjKRZmCTUm2NmSmerGsPvub8/uveFQTxFNPEtX4x7TOigjRDsU4rDx2UYWK6eIIWYwGvGkpu4OE3mIaC/p7hPmw0ZasvEV8g37pmXJc+3SR+j5We/n4vi7VtScBGrazMjcyfGcEp7tko8aLr1+0pvG+KrMRmyC9aszyFKw3aQqkd6FdudSBzYnUxpVFNvsLbeox+mIk9JJ6Ej/EdGJox9ZY0jAgMBAAECggEAbxHxULUseU3Ilg6jWORMW2WPzQNKIypYxKjVyyodvPjB/TX7vBiwt8rChIYGLGUw++us5PZeeNOaMLYgw1uC+Tl82X/S4Apa7B9+/nkXyVfdSwIM43tAvilWvULqPAC0vA/dVLoWAI/q+03hzOHDoHCL+QXsvd/g71ipmUVRzKrfbsnW38+Jfqjki/S01+38joKFGSebQ/RX3lLjbTKO74HSDVRnVQzk0JFeaNwoRu83LJrB5tbng+ebIF1M75x48Cj7Yph/fwxsT3FMqzGrnvdZs8v67ac5zDZ3U7QS85CQVZ3U6mM1yMx/CVHnq/IpUyNBrQtiG0hlNODRGt1/QQKBgQDSqLGmiJeycx9hloDSfegGvCqtVhcfyK1LaYLpmPPs/nMk6YH8ViI2nyuGM6FEbNzjDqFq1APw9AVbM5MzURYwHNUpVR1wZcPAvmaxyeiDi6YsO2fGxNfcYtmQJmmyX5r7x4WeE5cHU82iHdeCaQOo/fo8rqYR+mezqWCwKpnyoQKBgQDOtupYU1cgEPTeR9YP1qXy+rnXzuCcXYDNLoAokqnaP/IDW6eszSPj64oW6iu2CNXJsrjTyZcXVHAK9MZ4Kv0AlZVv5GRNTicvi0TdGokD3ZgYufkxFIDLEvLbuUOQTUQ6DYN/n7AzoLJGTDanfmZg/8smouw9jdcrItd/G/ztQwKBgHRg+fQU1imh7B5PtU+Ue1DtN26XdyL6DLLy0X4YH/o4AIjN8B/lvfULLlJvx5IzyjwuYwLPqnW9ehAOiC37iBSAeUriWOc1cVXUWouyDiBOUDlzgDuRl+eLPMKkVhRTOR4L9keEbxV/OMVtUxgayAjedxZ7LggUnKHm2yG4B2DBAoGAUHtPCkyIY2e2rsRxR4Fe4xdTO/7D3J1bBIfSRfm0P9EdvMj2o4TLMHYIMZvCoQEXm4wGAg8+0EqDxQBANZEnKA8VO5dEpgSDpTMoAJ3QMr0X9qiRqNY+vtIF1B5xe8FZivCv/gSCaddrEYuiy9ueqK1uvwA30k9jMtxB4q9W7lkCgYAqzkz8VdiuHYDWpzs/lCCSD63w3WMAZPUs8TD4AXMhFdSuqUSF1Fzo8MwomctOfT6sqabehH3dyBk3++58gpyXjG29SRO9sl0rmBfdcQdbinJUQ1L5mutnXt2zdB6geSC7J0ibMKz4nX6S9cxFDsXps+GIAgAG8ZXJZ24tbp4axg==")
                .setEnableDebug(true)
                .setEnableFullLog()
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            Log.i(TAG, "sophix load patch success!");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 如果需要在后台重启，建议此处用SharePreference保存状态。
                            Log.i(TAG, "sophix preload patch success. restart app to make effect.");
                        }
                    }
                }).initialize();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SophixManager.getInstance().queryAndLoadNewPatch();
    }
}