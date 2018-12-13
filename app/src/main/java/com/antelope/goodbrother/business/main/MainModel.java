package com.antelope.goodbrother.business.main;

import java.util.List;

public class MainModel {
    private int totalPage;
    private List<MainEntity> list;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<MainEntity> getList() {
        return list;
    }

    public void setList(List<MainEntity> list) {
        this.list = list;
    }
}
