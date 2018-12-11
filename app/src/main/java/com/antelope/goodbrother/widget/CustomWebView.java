package com.antelope.goodbrother.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.antelope.goodbrother.account.AccountManager;
import com.antelope.goodbrother.account.CookiesManager;


/**
 * Created by lijianjun on 2017-05-24.
 */

public class CustomWebView extends WebView {
    public static final String CLOSE_FLAG = "closewebview";
    public static final String SAVE_IMAGE_FLAG = "saveimg://";

    public CustomWebView(Context context) {
        super(context, null);

    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);// 必须加，不然不能用js
        webSettings.setDatabaseEnabled(true);// 启动数据库
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
//        webSettings.setAppCachePath(Constants.BASE_PATH + "/cache/caches");
        webSettings.setGeolocationEnabled(true);// 启用地理定位
//        webSettings.setGeolocationDatabasePath(Constants.BASE_PATH + "/cache/webdatabase");// 设置定位的数据库路径
        webSettings.setDomStorageEnabled(true);// 启用键值对存储,必须加
//        webSettings.setUseWideViewPort(true);// 设置webview自适应屏幕大小
        webSettings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        CookiesManager.getInstance(context).syncCookie(AccountManager.getInstance().getToken());
    }

    public void loadBody(String body) {
        this.loadDataWithBaseURL(null, buildHtml(body), "text/html", "utf-8", null);
    }

    private String buildHtml(String body) {
        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, minimal-ui\">\n" +
                "  <meta name=\"screen-orientation\" content=\"portrait\" />\n" +
                "  <meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n" +
                "  <meta name=\"format-detection\" content=\"telephone=no\">\n" +
                "  <meta name=\"full-screen\" content=\"yes\">\n" +
                "  <meta name=\"x5-fullscreen\" content=\"true\">\n" +
                "  <script src=\"file:///android_asset/bind.min.js\"></script>\n" +
                "  <script src=\"file:///android_asset/flexible.js\"></script>\n" +
                "  <input type=\"hidden\" value=\"01\" id=\"customerNoticPlatform\"></input>" +
                "  <title>zhongmubao</title>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                body +
                "</body>\n" +
                "\n" +
                "</html>\n";
        return html;
    }
}
