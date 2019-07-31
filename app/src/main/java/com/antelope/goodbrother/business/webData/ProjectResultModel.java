package com.antelope.goodbrother.business.webData;

public class ProjectResultModel {
    private String Result;
    private String Message;
    private ProjectModel Data;

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ProjectModel getData() {
        return Data;
    }

    public void setData(ProjectModel data) {
        Data = data;
    }
}
