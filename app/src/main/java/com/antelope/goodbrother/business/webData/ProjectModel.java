package com.antelope.goodbrother.business.webData;

import java.io.Serializable;

public class ProjectModel implements Serializable {
    private String CustomerId;
    private String Sign;
    private java.util.List<ProjectModel2> List;

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public java.util.List<ProjectModel2> getList() {
        return List;
    }

    public void setList(java.util.List<ProjectModel2> list) {
        List = list;
    }
}
