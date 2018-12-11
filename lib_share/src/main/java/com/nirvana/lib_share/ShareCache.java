package com.nirvana.lib_share;

public class ShareCache {
    /**
     * weather share in backGround
     */
    private static boolean isShareInBackGround;//是否正在后台分享

    public static boolean isIsShareInBackGround() {
        return isShareInBackGround;
    }

    public static void setIsShareInBackGround(boolean isShareInBackGround) {
        ShareCache.isShareInBackGround = isShareInBackGround;
    }

    /**
     * share model
     */
    private static ShareModel shareModel;

    public static ShareModel getShareModel() {
        return shareModel;
    }

    public static void setShareModel(ShareModel shareModel) {
        ShareCache.shareModel = shareModel;
    }

}
