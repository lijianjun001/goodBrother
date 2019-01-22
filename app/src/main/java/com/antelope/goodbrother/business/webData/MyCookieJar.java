package com.antelope.goodbrother.business.webData;

import com.antelope.goodbrother.account.AccountManager;
import com.cylty.zmkj.utils.Base64;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MyCookieJar implements CookieJar {

    private List<Cookie> cookies;

    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {
        this.cookies = cookies;
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        if (null != cookies) {
            return cookies;
        } else {
            cookies = new ArrayList<>();
            Cookie.Builder builder = new Cookie.Builder();
            builder.name("CURRENT_USER_TICKET").value(Base64.decode(AccountManager.getInstance().getToken(), Base64.DEFAULT).toString())
                    .domain("192.168.31.200").expiresAt(24*60*60*1000);
            Cookie.Builder builder2 = new Cookie.Builder();
            builder2.name("CURRENT_USER_Sign").value(AccountManager.getInstance().getAccountInfo().getSign())
                    .domain("192.168.31.200").expiresAt(24*60*60*1000);
            cookies.add(builder.build());
            cookies.add(builder2.build());
            return cookies;
        }
    }

}
