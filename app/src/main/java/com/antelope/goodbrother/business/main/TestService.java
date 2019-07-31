package com.antelope.goodbrother.business.main;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class TestService extends Service {
    private String TAG = this.getClass().getName();
    private MyBinder myBinder;
    private int num;
    private boolean flag;
    private MyReceiver receiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        myBinder = new MyBinder();
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("start");
        intentFilter.addAction("stop");
        registerReceiver(receiver, intentFilter);
    }

    /**
     * @param intent  startservice 传入的intent
     * @param flags   startservice时候为0，启动失败重新启动flag为START_REDELIVER_INTENT onStartCommand方法未被调用或者没有正常返回的异常情况下， 再次尝试创建，传入的flags就为START_FLAG_RETRY 。
     * @param startId startId A unique integer representing this specific request to start
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        new Thread(() -> {
            while (flag) {
                num++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public int getNum() {

        return num;
    }


    public void startAdd() {
        flag = true;
    }

    public void stopAdd() {
        flag = false;
    }

    public class MyBinder extends Binder {


        public TestService getService() {
            return TestService.this;
        }
    }


    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("start")) {
                startAdd();
            } else if (intent.getAction().equals("stop")) {
                stopAdd();
            }
        }
    }


}
