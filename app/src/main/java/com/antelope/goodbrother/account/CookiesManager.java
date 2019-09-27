package com.antelope.goodbrother.account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.antelope.goodbrother.config.Constants;

public class CookiesManager {

    private static volatile CookiesManager cookiesManager;
    private CookieManager cookieManager;

    public static CookiesManager getInstance(Context context) {
        if (cookiesManager == null) {
            synchronized (CookiesManager.class) {
                if (cookiesManager == null) {
                    cookiesManager = new CookiesManager(context);
                }
            }

        }
        return cookiesManager;
    }

    public CookiesManager(Context context) {
        CookieSyncManager.createInstance(context);
        cookieManager = CookieManager.getInstance();
    }

    public void syncCookie(String token) {
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(Constants.getAppUrlClientIpPort(), "EmubaoToken=" + token + ";");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush();
        } else {
            CookieSyncManager.getInstance().sync();
        }
    }


    public String getCookies(String url) {
        String CookieStr = cookieManager.getCookie(url);
        return CookieStr;
    }


    @SuppressLint("NewApi")
    public void removeCookie() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.removeAllCookie();
            } else {
                cookieManager.removeAllCookies(value -> {
                    Log.d("Cookie", value + "");
                });
            }
        } catch (NoSuchMethodError e) {
            // TODO: handle exception
        }
    }
}
