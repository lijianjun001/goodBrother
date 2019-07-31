package com.antelope.goodbrother.business.webData;

/**
 * Created by Administrator on 2016/11/28.
 */

public class BonusModel {
    private String TotalAmount;
    private String RedPackageList;
    private String MaxSelectedCount;

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getRedPackageList() {
        return RedPackageList;
    }

    public void setRedPackageList(String redPackageList) {
        RedPackageList = redPackageList;
    }

    public String getMaxSelectedCount() {
        return MaxSelectedCount;
    }

    public void setMaxSelectedCount(String maxSelectedCount) {
        MaxSelectedCount = maxSelectedCount;
    }
}
