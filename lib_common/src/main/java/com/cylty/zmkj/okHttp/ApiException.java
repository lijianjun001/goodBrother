package com.cylty.zmkj.okHttp;

/**
 * Created by Administrator on 2017/12/6.
 */

public class ApiException extends RuntimeException {
    private int errorCode;

    public ApiException(int code, String msg) {
        super(msg);
        this.errorCode = code;
    }

    public int getErrorCode() {
        return errorCode;
    }

}

