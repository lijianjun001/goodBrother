package com.antelope.goodbrother.http.java.body;


/**
 * Created by Administrator on 2017/9/7.
 */

public class BaseBody {
    private String version;
    private String platform;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
