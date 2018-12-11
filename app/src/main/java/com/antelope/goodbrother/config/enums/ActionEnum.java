package com.antelope.goodbrother.config.enums;


/**
 * Created by lijianjun on 2017-08-21.
 */

public enum ActionEnum {
    Wallet("wallet", "");//action 和服务器对应，不能乱改


    private String action, pageName;

    ActionEnum(String action, String pageName) {
        this.action = action;
        this.pageName = pageName;
    }

    public static ActionEnum getActionEnum(String action) {
        ActionEnum actionEnum = null;
        for (ActionEnum temp: ActionEnum.values()) {
            if (temp.getAction().equals(action)) {
                actionEnum = temp;
            }
        }
        return actionEnum;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}
