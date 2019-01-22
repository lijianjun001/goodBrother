package com.antelope.goodbrother.account;


import com.antelope.goodbrother.config.Constants;
import com.cylty.zmkj.utils.GsonUtils;
import com.nirvana.zmkj.manager.SharePreferenceManager;

import org.greenrobot.eventbus.EventBus;

public class AccountManager {
    private static volatile AccountManager accountManager;

    private AccountInfo accountInfo;

    public AccountManager() {

    }

    public static AccountManager getInstance() {
        if (accountManager == null) {
            synchronized (AccountManager.class) {
                if (accountManager == null) {
                    accountManager = new AccountManager();
                }
            }
        }
        return accountManager;
    }

    public AccountInfo getAccountInfo() {
        if (accountInfo == null) {
            String accountInfo = SharePreferenceManager.getInstance().getString(Constants.USER_INFO);
            this.accountInfo = GsonUtils.fromJson(accountInfo, AccountInfo.class);
        }

        return this.accountInfo;
    }

    public void saveAccountInfo(AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
        String accountInfoStr = GsonUtils.toJson(accountInfo);
        SharePreferenceManager.getInstance().putString(Constants.USER_INFO, accountInfoStr);
    }

    public String getToken() {
        if (accountInfo == null) {
            getAccountInfo();
        }
        return accountInfo != null ? accountInfo.getToken() : "";
    }

    public boolean currentUserIsLogin() {
        return getInstance().getAccountInfo() != null && getToken() != null;
    }

    public void exit() {
        this.accountInfo = null;
        ExitCurrentAccountEvent exitCurrentAccountEvent = new ExitCurrentAccountEvent();
        EventBus.getDefault().post(exitCurrentAccountEvent);
        SharePreferenceManager.getInstance().putString(Constants.USER_INFO, "");
    }
}
