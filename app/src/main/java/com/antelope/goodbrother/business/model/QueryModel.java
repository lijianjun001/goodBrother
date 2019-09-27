package com.antelope.goodbrother.business.model;

public class QueryModel {

    private int Count;
    private String Phone;
    private String Code;
    private String ExchangeTime;
    private String RegisterTime;
    private String Waiter;
    private boolean IsReceive;

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getExchangeTime() {
        return ExchangeTime;
    }

    public void setExchangeTime(String exchangeTime) {
        ExchangeTime = exchangeTime;
    }

    public String getRegisterTime() {
        return RegisterTime;
    }

    public void setRegisterTime(String registerTime) {
        RegisterTime = registerTime;
    }

    public String getWaiter() {
        return Waiter;
    }

    public void setWaiter(String waiter) {
        Waiter = waiter;
    }

    public boolean isReceive() {
        return IsReceive;
    }

    public void setReceive(boolean receive) {
        IsReceive = receive;
    }
}
