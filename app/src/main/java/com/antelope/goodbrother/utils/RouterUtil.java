package com.antelope.goodbrother.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class RouterUtil {

    public static void openActivity(Context context, String pAction, Bundle bundle) {
        Intent intent = new Intent(pAction);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void openActivity(Context context, Class<?> mClass) {
        Intent intent = new Intent(context, mClass);
        context.startActivity(intent);
    }


    public static void openActivity(Context context, Class<?> mClass, Bundle bundle) {
        Intent intent = new Intent(context, mClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static void openActivityNewTask(Context context, Class<?> mClass, Bundle bundle) {
        Intent intent = new Intent(context, mClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
