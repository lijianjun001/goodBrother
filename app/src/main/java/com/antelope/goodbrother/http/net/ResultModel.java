package com.antelope.goodbrother.http.net;

public class ResultModel {
    private int Result;//0或者10086成功其他失败
    private String Message;//错误信息
    private String Data;//成功返回的数据

    public int getResult() {
        return Result;
    }

    public void setResult(int result) {
        Result = result;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

}
