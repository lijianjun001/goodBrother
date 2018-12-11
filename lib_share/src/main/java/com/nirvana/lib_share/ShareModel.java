package com.nirvana.lib_share;

/**
 * Created by Administrator on 2018/3/1.
 */

public class ShareModel {
    private String type;
    private String shareTo;//00微信好友 01微信朋友圈 02 微博 03微信全部 04 微信好友和微博 05微信朋友圈和微博 06全部
    private String shareSuccessLink;
    private String shareSuccessType;
    private String shareSuccessMessage;
    private String shareTitle;
    private String shareContent;
    private String shareIcon;
    private String shareUrl;
    private String img;
    private boolean needReturn;
    private boolean promptlyTrigger;//是否直接送红包
    public boolean isNeedReturn() {
        return needReturn;
    }

    public void setNeedReturn(boolean needReturn) {
        this.needReturn = needReturn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShareTo() {
        return shareTo;
    }

    public void setShareTo(String shareTo) {
        this.shareTo = shareTo;
    }

    public String getShareSuccessLink() {
        return shareSuccessLink;
    }

    public void setShareSuccessLink(String shareSuccessLink) {
        this.shareSuccessLink = shareSuccessLink;
    }

    public String getShareSuccessType() {
        return shareSuccessType;
    }

    public void setShareSuccessType(String shareSuccessType) {
        this.shareSuccessType = shareSuccessType;
    }

    public String getShareSuccessMessage() {
        return shareSuccessMessage;
    }

    public void setShareSuccessMessage(String shareSuccessMessage) {
        this.shareSuccessMessage = shareSuccessMessage;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public String getShareIcon() {
        return shareIcon;
    }

    public void setShareIcon(String shareIcon) {
        this.shareIcon = shareIcon;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isPromptlyTrigger() {
        return promptlyTrigger;
    }

    public void setPromptlyTrigger(boolean promptlyTrigger) {
        this.promptlyTrigger = promptlyTrigger;
    }
}
