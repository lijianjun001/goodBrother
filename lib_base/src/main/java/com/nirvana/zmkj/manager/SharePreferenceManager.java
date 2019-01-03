package com.nirvana.zmkj.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.cylty.zmkj.utils.StringUtils;
import com.nirvana.zmkj.base.BaseApplication;


public class SharePreferenceManager {

    private static volatile SharePreferenceManager instance;
    private SharedPreferences sharedPreferences;

    private SharePreferenceManager() {
        Context context = BaseApplication.getInstance();
        if (context == null) {
            return;
        }
        sharedPreferences = context.getSharedPreferences("zmkj_zhongmubao3.1.0", Context.MODE_PRIVATE);
    }

    public static SharePreferenceManager getInstance() {
        if (instance == null) {
            synchronized (SharePreferenceManager.class) {
                if (instance == null) {
                    instance = new SharePreferenceManager();
                }
            }
        }
        return instance;
    }

    public void putString(String key, String value) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        if (sharedPreferences == null) {
            return null;
        }
        return sharedPreferences.getString(key, "");
    }

    public String getString(String key, String defaultValue) {
        if (sharedPreferences == null) {
            return null;
        }
        return sharedPreferences.getString(key, defaultValue);
    }

    public Boolean getBoolean(String key, boolean defaultValue) {
        if (sharedPreferences == null) {
            return null;
        }
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean isExist(String key) {
        if (sharedPreferences == null) {
            return false;
        }
        String str = sharedPreferences.getString(key, "");
        return !StringUtils.isEmpty(str);
    }

    public void deleteStr(String key) {
        if (sharedPreferences == null) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }


}
