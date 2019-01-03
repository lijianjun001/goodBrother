package com.antelope.goodbrother.http.java.service;


import com.antelope.goodbrother.http.java.body.PersonalBody;
import com.cylty.zmkj.okHttp.ResultModel;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 综合service
 * Created by Administrator on 2017/12/11.
 */

public interface MyService {
    @POST("/my/menu/list")
    Observable<ResultModel<String>> personalList(@Body PersonalBody personalBody);

}
