package com.antelope.goodbrother.business.webData;

import android.app.Activity;
import android.util.Log;

import com.antelope.goodbrother.account.AccountInfo;
import com.antelope.goodbrother.account.AccountManager;
import com.cylty.zmkj.utils.GsonUtils;
import com.cylty.zmkj.utils.StringUtils;
import com.nirvana.zmkj.base.BasePresenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class WebDataPresenter extends BasePresenter {
    public WebDataPresenter(Activity activity) {
        super(activity);
    }

    public void getMainData() {
        OkHttpClient.Builder mOkHttpClientBuilder = new OkHttpClient.Builder();
        mOkHttpClientBuilder.cookieJar(new MyCookieJar());
        OkHttpClient mOkHttpClient = mOkHttpClientBuilder.build();
        Headers.Builder headersBuilder = new Headers.Builder()
                .add("Accept", "text/html, application/xhtml+xml, image/jxr, */*")
                .add("Accept-Language", "zh-Hans-CN,zh-Hans,en-US,en;q=0.5")
                .add("Accept-Encoding", "deflate, gzip;q=1.0, *;q=0.5")
                .add("Connection", "Keep-Alive");
        Headers requestHeaders = headersBuilder.build();
        Request request = new Request.Builder().url("http://192.168.31.200:46018/Emubao/WebApp#/Index").headers(requestHeaders).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();

                InputStream inputStream = responseBody.byteStream();
                String str = StringUtils.getString(inputStream);
                Document document = Jsoup.parse(str);
                Log.d("document.head", document.html());
            }
        });
    }

    public void login(String tel, String pass) {
        OkHttpClient.Builder mOkHttpClientBuilder = new OkHttpClient.Builder();
        mOkHttpClientBuilder.cookieJar(new MyCookieJar());
        OkHttpClient mOkHttpClient = mOkHttpClientBuilder.build();
        Request request = new Request.Builder().url("http://192.168.31.200:46018/Account/Login?").build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();

                InputStream inputStream = responseBody.byteStream();
                String str = StringUtils.getString(inputStream);
                Document document = Jsoup.parse(str);
                Log.d("document.head", document.html());
            }
        });
    }


    public void postData(String tel, String pass) {
        OkHttpClient.Builder mOkHttpClientBuilder = new OkHttpClient.Builder();
        final OkHttpClient mOkHttpClient = mOkHttpClientBuilder.build();
//        Headers.Builder headersBuilder = new Headers.Builder()
//                .add("headerKey", "headerValue");
//        Headers requestHeaders = headersBuilder.build();

        FormBody.Builder formBodyBuilder = new FormBody.Builder()
                .add("account", "13260213625")
                .add("r", Math.random() + "")
                .add("password", "123456");
        RequestBody requestBody = formBodyBuilder.build();

        Request request = new Request.Builder()
                .url("http://192.168.31.200:46018/Account/Login?")
//                .headers(requestHeaders)
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();

                InputStream inputStream = responseBody.byteStream();
                String str = StringUtils.getString(inputStream);
                Log.d("document.head", str);
//                Document document = Jsoup.parse(str);
//
//                String body=document.body().toString();
//                Log.d("document.head", document.html());
                AccountInfo accountInfo = GsonUtils.fromJson(str, AccountInfo.class);
                AccountManager.getInstance().saveAccountInfo(accountInfo);
                getMainData();
            }
        });

    }

    public void getWebData() {
        new Thread(() -> {
            Document document = null;
            try {
                document = Jsoup.connect("https://www.baidu.com").get();
                Log.d("document.head", document.html());
//                Log.d("document.head",document.getElementsByClass("wrap").html());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
