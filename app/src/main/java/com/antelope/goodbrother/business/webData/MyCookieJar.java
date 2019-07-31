package com.antelope.goodbrother.business.webData;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MyCookieJar implements CookieJar {

    private static List<Cookie> cookies;

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        this.cookies = cookies;
        for (Cookie cookie : cookies) {
            Log.d("cookie Name:", cookie.name());
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        if (this.cookies == null) {
            Log.d("loadForRequest", "没加载到cookie");
        }
        return this.cookies != null ? this.cookies : new ArrayList<>();
    }
}
