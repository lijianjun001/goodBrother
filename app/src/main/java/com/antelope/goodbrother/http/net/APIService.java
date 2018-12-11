package com.antelope.goodbrother.http.net;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by lijianjun on 2017-04-14.
 * post括号中是url，loadData后是参数
 * 此类不能混淆
 */

public interface APIService {
    @POST("client")
    @FormUrlEncoded
    Call<ResponseBody> loadData(@Field("Data") String data);

}
