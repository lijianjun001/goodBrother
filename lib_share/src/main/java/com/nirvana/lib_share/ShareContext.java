package com.nirvana.lib_share;

/**
 * Created by Administrator on 2018/2/27.
 */

public class ShareContext {
    private ShareInterface shareInterface;

    public ShareContext(ShareInterface shareInterface) {
        this.shareInterface = shareInterface;
    }

    public void shareLink() {
        shareInterface.shareLink();
    }

    public void shareImg() {
        shareInterface.shareImage();
    }

    public void shareLinkAndImg() {
        shareInterface.shareLinkAndImage();
    }

    public void shareByType(String shareType) {
        if (ShareConstants.ShareTypeLink.equals(shareType)) {//链接
            shareLink();
        } else if (ShareConstants.ShareTypeImg.equals(shareType)) {//图片
            shareImg();
        } else {//链接和图片
            shareLinkAndImg();
        }
    }
}
