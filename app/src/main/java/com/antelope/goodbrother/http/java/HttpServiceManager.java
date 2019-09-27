package com.antelope.goodbrother.http.java;

import com.antelope.goodbrother.account.AccountManager;
import com.antelope.goodbrother.config.Constants;
import com.antelope.goodbrother.http.java.service.CustomService;
import com.antelope.goodbrother.http.java.service.SystemService;
import com.cylty.zmkj.okHttp.converter.MyGsonConverterFactory;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by lijianjun on 2016/12/27.
 */

public class HttpServiceManager {

    private static CustomService customService;
    private static SystemService systemService;


    private static volatile HttpServiceManager serviceManager;

    public static HttpServiceManager getInstance() {
        synchronized (HttpServiceManager.class) {
            if (serviceManager == null) {
                serviceManager = new HttpServiceManager();
            }
        }
        return serviceManager;
    }

    public CustomService getCustomService() {
        if (customService == null) {
            customService = configRetrofit(CustomService.class);
        }
        return customService;
    }

    public SystemService getSystemService() {
        if (systemService == null) {
            systemService = configRetrofit(SystemService.class);
        }
        return systemService;
    }

    private <T> T configRetrofit(Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.getAppUrlClientIpJava())
                .client(configClient())
                .addConverterFactory(MyGsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(service);
    }

    private OkHttpClient configClient() {
        HostnameVerifier hv = (hostname, session) -> hostname.matches("\\S*emubao.com");
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().hostnameVerifier(hv).addInterceptor(chain -> {
            Request request = chain.request()
                    .newBuilder()
                    .header("Authorization", AccountManager.getInstance().getToken())
                    .header("Content-Type", "application/json")
                    .header("Platform", "01").build();
            return chain.proceed(request);
        }).connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS).build();
        return okHttpClient;
    }

}
