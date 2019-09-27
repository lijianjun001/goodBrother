package com.antelope.goodbrother.business.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.nirvana.zmkj.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private ServiceConnection serviceConnection;

    private TestService testService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MainViewHolder(this).getRootView());

        Intent intent = new Intent(MainActivity.this, TestService.class);
        startService(intent);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                testService = ((TestService.MyBinder) service).getService();

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    public void getNum() {

        testService.getNum();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
