package com.nirvana.zmkj.manager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CustomActivityManager {
    private static List<Activity> activities = new LinkedList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void exit(Context context) {
        try {
            for (Activity activity: activities) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MobclickAgent.onKillProcess(context);
            System.exit(0);
        }
    }

    public static boolean isRunningForeground(Context context) {
        android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am == null) {
            throw new RuntimeException("activity service is not available");
        }
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        return !TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName());
    }

    public static Activity getTop() {
        if (activities.isEmpty()) {
            return null;
        }
        return activities.get(activities.size() - 1);
    }


    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    public static void exitBy2Click(Context context) {
        Timer timer;
        if (!isExit) {
            isExit = true;
            Toast.makeText(context, "再按一次退出程序", Toast.LENGTH_LONG).show();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            exit(context);
        }
    }
}
