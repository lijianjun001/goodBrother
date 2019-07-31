package com.antelope.goodbrother.business.webData;

import java.io.Serializable;

public class ProjectModel2 implements Serializable {
    private String Id;//购羊计划Id
    private String Title;//标题
    private double Price;//每一只羊的价格
    private String Rate;//年华收益
    private String EffectiveTime;//入栏日期
    private String ProjectPhoto;
    private String Period;//养殖周期
    private String State;//状态,（抢购，新人，预售，养殖中，已赎回，售馨）
    private String SoldState;//可售状态,（00-可售，01-未开始，02-已结束，03-售馨）
    private long Countdown;//倒计时
    private String ActivityId;//活动Id
    private String Type;//03专属供应链体系全程监控 04专属供应链体系全程监控 其他羊只全部由中国人寿投保
    private String Link;//链接地址
    private String Shorthand;//牧场名
    private boolean countDownMis = false;
    private int PurchaseCount;
    private String RateImg;
    private String DaoJiShiXianShiLeiXing;
    private String DaoJiShiStr;
    private int XuYuState;

    public ProjectModel2() {

    }

    public int getXuYuState() {
        return XuYuState;
    }

    public void setXuYuState(int xuYuState) {
        XuYuState = xuYuState;
    }

    public String getDaoJiShiXianShiLeiXing() {
        return DaoJiShiXianShiLeiXing;
    }

    public void setDaoJiShiXianShiLeiXing(String daoJiShiXianShiLeiXing) {
        DaoJiShiXianShiLeiXing = daoJiShiXianShiLeiXing;
    }

    public String getDaoJiShiStr() {
        return DaoJiShiStr;
    }

    public void setDaoJiShiStr(String daoJiShiStr) {
        DaoJiShiStr = daoJiShiStr;
    }

    public String getRateImg() {
        return RateImg;
    }

    public void setRateImg(String rateImg) {
        RateImg = rateImg;
    }

    public int getPurchaseCount() {
        return PurchaseCount;
    }

    public void setPurchaseCount(int purchaseCount) {
        PurchaseCount = purchaseCount;
    }

    public String getShorthand() {
        return Shorthand;
    }

    public void setShorthand(String shorthand) {
        Shorthand = shorthand;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getEffectiveTime() {
        return EffectiveTime;
    }

    public void setEffectiveTime(String effectiveTime) {
        EffectiveTime = effectiveTime;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getSoldState() {
        return SoldState;
    }

    public void setSoldState(String soldState) {
        SoldState = soldState;
    }

    public long getCountdown() {
        return Countdown;
    }

    public void setCountdown(long countdown) {
        Countdown = countdown;
    }

    public String getActivityId() {
        return ActivityId;
    }

    public void setActivityId(String activityId) {
        ActivityId = activityId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getProjectPhoto() {
        return ProjectPhoto;
    }

    public void setProjectPhoto(String projectPhoto) {
        ProjectPhoto = projectPhoto;
    }

    public boolean isCountDownMis() {
        return countDownMis;
    }

    public void setCountDownMis(boolean countDownMis) {
        this.countDownMis = countDownMis;
    }

}
