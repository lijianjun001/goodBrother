package com.nirvana.lib_share;

import android.content.Context;

import com.sina.weibo.sdk.share.WbShareHandler;

/**
 * Created by Administrator on 2018/2/27.
 */

public class ShareTypeWeibo implements ShareInterface {

    private Context context;
    private WbShareHandler wbShareHandler;

    public ShareTypeWeibo(Context context, WbShareHandler wbShareHandler) {
        this.context = context;
        this.wbShareHandler = wbShareHandler;
    }

    @Override
    public void shareLink() {
        ShareThirdPart.shareToWeibo(wbShareHandler, ShareCache.getShareModel().getShareContent(),ShareCache.getShareModel().getShareTitle(),ShareCache.getShareModel().getShareUrl());
    }

    @Override
    public void shareImage() {
        ShareImageTask shareImageTask = new ShareImageTask(context, wbShareHandler);
        shareImageTask.execute();
    }

    @Override
    public void shareLinkAndImage() {
        ShareTextAndImageTask shareTextAndImageTask = new ShareTextAndImageTask(context, wbShareHandler);
        shareTextAndImageTask.execute();
    }
}
