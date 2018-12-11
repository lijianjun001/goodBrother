package com.antelope.goodbrother.http.net;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.JSON;
import com.antelope.goodbrother.R;
import com.antelope.goodbrother.config.Constants;
import com.antelope.goodbrother.config.RouterConfig;
import com.cylty.zhongmukeji.utils.NetUtil;
import com.nirvana.zmkj.widget.ShowMessageProxy;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by lijianjun on 2017-04-17.
 */

public class OKHttpProxy {
    private final static Object object = new Object();
    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS).build();
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.APP_URL_CLIENT_IP_PORT)//必须以"/"结尾
            .client(okHttpClient)
            .build();
    private static APIService service = retrofit.create(APIService.class);
    private Context context;
    private OnResultSuccessListener onResultSuccessListener;
    private OnResultFailListener onResultFailListener;
    private OnResultSuccessToStringListener onResultSuccessToStringListener;
    private ShowMessageProxy showMessageProxy;

    public OKHttpProxy(Context context) {
        this.context = context;
        showMessageProxy = new ShowMessageProxy(context);
    }

    public void setOnResultSuccessToStringListener(OnResultSuccessToStringListener onResultSuccessToStringListener) {
        this.onResultSuccessToStringListener = onResultSuccessToStringListener;
    }

    public void setOnResultSuccessListener(OnResultSuccessListener onResultSuccessListener) {
        this.onResultSuccessListener = onResultSuccessListener;
    }

    public void setOnResultFailListener(OnResultFailListener onResultFailListener) {
        this.onResultFailListener = onResultFailListener;
    }

    public void call(String data, boolean checkNet, boolean showDialog, String dialogText) {
        if (checkNet && !NetUtil.isNetworkAvaiable(context)) {
            showMessageProxy.displayToast(context.getResources().getString(R.string.network_error));
            return;
        }
        if (showDialog) {
            showMessageProxy.startProgressDialog(dialogText);
        }
        call(data);
    }

    public void call(String data) {
        Call<ResponseBody> responseBodyCall = service.loadData(data);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                ResultModel resultModel;
                String responseStr;
                if (response.isSuccessful() && responseBody != null) {
                    try {
                        responseStr = responseBody.string();
                        if (Constants.DEBUG) {
                            Log.d("call", responseStr);
                        }
                        showMessageProxy.stopProgressDialog();
                        resultModel = JSON.parseObject(responseStr, ResultModel.class);
                        String message = resultModel.getMessage();
                        if (message != null && context.getResources().getString(R.string.re_login).equals(message)) {
                            synchronized (object) {
                                ARouter.getInstance().build(RouterConfig.ACTIVITY_LOGIN_BY_PASSWORD).navigation();
                                showMessageProxy.displayToast(message);
                            }
                            return;
                        }
                        if (onResultSuccessListener != null) {
                            onResultSuccessListener.onSuccess(resultModel);
                        }
                        if (onResultSuccessToStringListener != null) {
                            onResultSuccessToStringListener.onSuccess(responseStr);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    responseBody.close();
                } else {
                    if (onResultFailListener != null) {
                        showMessageProxy.stopProgressDialog();
                        onResultFailListener.onFailure();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                if (onResultFailListener != null) {
                    showMessageProxy.stopProgressDialog();
                    showMessageProxy.displayToast(context.getResources().getString(R.string.failure_str));
                    onResultFailListener.onFailure();
                }
            }
        });

    }

    public interface OnResultSuccessListener {
        /**
         * @desc 获取数据成功
         * @author lijianjun
         * create at 2017-04-13 11:30
         */
        void onSuccess(ResultModel resultModel);
    }


    public interface OnResultSuccessToStringListener {
        /**
         * @desc 获取数据成功
         * @author lijianjun
         * create at 2017-04-13 11:30
         */
        void onSuccess(String str);
    }


    public interface OnResultFailListener {
        /**
         * @author lijianjun
         * create at 2017-04-13 11:28
         * @desc 获取数据失败，超时等情况下触发
         */
        void onFailure();
    }
}
