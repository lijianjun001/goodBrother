package com.antelope.goodbrother.business.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Test extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

//        PendingResult result = goAsync();
//
//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                result.finish();
//            }
//        });

        context.startService(new Intent(context, MyIntentService.class));
    }
}
