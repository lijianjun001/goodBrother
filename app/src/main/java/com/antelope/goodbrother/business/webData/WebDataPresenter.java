package com.antelope.goodbrother.business.webData;

import android.app.Activity;
import android.util.Log;

import com.antelope.goodbrother.account.AccountInfo;
import com.antelope.goodbrother.account.AccountManager;
import com.cylty.zmkj.utils.GsonUtils;
import com.cylty.zmkj.utils.StringUtils;
import com.google.gson.Gson;
import com.nirvana.zmkj.base.BasePresenter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class WebDataPresenter extends BasePresenter {
    public WebDataPresenter(Activity activity) {
        super(activity);
    }

    private String hostUrl = "https://wap.emubao.com/";
    private int sheepCount = 100;
//    private String hostUrl="http://192.168.31.200:46018/";


    public void getPersonalCenter() {
        OkHttpClient.Builder mOkHttpClientBuilder = new OkHttpClient.Builder();
        mOkHttpClientBuilder.cookieJar(new MyCookieJar());
        OkHttpClient mOkHttpClient = mOkHttpClientBuilder.build();
        Request request = new Request.Builder().url("http://192.168.31.200:46018/Customer/PersonalCenter").build();
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
                Log.d("document.head", document.head().toString());
            }
        });
    }

    public void login(String tel, String pass) {
        OkHttpClient.Builder mOkHttpClientBuilder = new OkHttpClient.Builder();
        mOkHttpClientBuilder.cookieJar(new MyCookieJar());
        final OkHttpClient mOkHttpClient = mOkHttpClientBuilder.build();

        FormBody.Builder formBodyBuilder = new FormBody.Builder()
                .add("account", tel)
                .add("r", Math.random() + "")
                .add("password", pass);
        RequestBody requestBody = formBodyBuilder.build();

        Request request = new Request.Builder()
                .url("https://wap.emubao.com/Account/Login?")
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
//                cookieStr = response.header("Set-Cookie");
//                Log.d("document.head", cookieStr);
                InputStream inputStream = responseBody.byteStream();
                String str = StringUtils.getString(inputStream);
                AccountInfo accountInfo = GsonUtils.fromJson(str, AccountInfo.class);
                AccountManager.getInstance().saveAccountInfo(accountInfo);
            }
        });
    }

    public void getProducts() {
        OkHttpClient.Builder mOkHttpClientBuilder = new OkHttpClient.Builder();
        mOkHttpClientBuilder.cookieJar(new MyCookieJar());
        final OkHttpClient mOkHttpClient = mOkHttpClientBuilder.build();

        FormBody.Builder formBodyBuilder = new FormBody.Builder()
                .add("r", Math.random() + "");
        RequestBody requestBody = formBodyBuilder.build();

        Request request = new Request.Builder()
                .url(hostUrl + "Sheep/ProjectListV2?")
//                .headers(buildHeader())
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
                ProjectResultModel resultModel = new Gson().fromJson(str, ProjectResultModel.class);

                System.out.println("getProducts" + resultModel.getData().getList().toString());
//                Document document = Jsoup.parse(str);
                chooseRedPackets(resultModel.getData().getList().get(2));

            }
        });
    }


    public void createOrder(ProjectModel2 projectModel2, BonusModel bonusModel) {
        OkHttpClient.Builder mOkHttpClientBuilder = new OkHttpClient.Builder();
        mOkHttpClientBuilder.cookieJar(new MyCookieJar());
        final OkHttpClient mOkHttpClient = mOkHttpClientBuilder.build();

        FormBody.Builder formBodyBuilder = new FormBody.Builder()
                .add("r", Math.random() + "")
                .add("id", projectModel2.getId())
                .add("libraryCount", 0 + "")
                .add("bouns", bonusModel.getTotalAmount() + "")
                .add("redPacketList", bonusModel.getRedPackageList() + "")
                .add("count", sheepCount + "");
        RequestBody requestBody = formBodyBuilder.build();

        Request request = new Request.Builder()
                .url(hostUrl + "Sheep/CreateOrder?")
//                .headers(buildHeader())
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
                System.out.println(str);
                CreateResultModel resultModel = new Gson().fromJson(str, CreateResultModel.class);

                if (resultModel.getResult() == 0) {
                    System.out.println("createOrder" + "成功");
                }

//                Document document = Jsoup.parse(str);
            }
        });
    }

    public void chooseRedPackets(ProjectModel2 projectModel2) {
        OkHttpClient.Builder mOkHttpClientBuilder = new OkHttpClient.Builder();
        mOkHttpClientBuilder.cookieJar(new MyCookieJar());
        final OkHttpClient mOkHttpClient = mOkHttpClientBuilder.build();

        FormBody.Builder formBodyBuilder = new FormBody.Builder()
                .add("r", Math.random() + "")
                .add("ProjectId", projectModel2.getId())
                .add("SheepCount", sheepCount + "")
                .add("SortType", 0 + "");
        RequestBody requestBody = formBodyBuilder.build();

        Request request = new Request.Builder()
                .url(hostUrl + "Sheep/DefaultChooseRedPackage?")
//                .headers(buildHeader())
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
                System.out.println(str);
                BonusResultModel resultModel = new Gson().fromJson(str, BonusResultModel.class);

                if (resultModel.getResult() == 0) {
                    System.out.println("chooseRedPackets" + resultModel.getData());
                }
                createOrder(projectModel2, resultModel.getData());
            }
        });
    }

}
