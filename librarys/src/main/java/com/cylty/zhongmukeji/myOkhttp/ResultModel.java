package com.cylty.zhongmukeji.myOkhttp;

public class ResultModel<T> {
    private int code;
    private String message;//错误信息
    private T data;//成功返回的数据

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
