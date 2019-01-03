package com.antelope.goodbrother.http.java.service;

import com.antelope.goodbrother.http.java.body.SystemBody;
import com.cylty.zmkj.okHttp.ResultModel;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2017/10/22.
 */

public interface SystemService {
    @POST("system/touTiaoAdv")
    Observable<ResultModel<String>> uploadSyetemInfo(@Body SystemBody systemBody);
}
