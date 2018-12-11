package com.nirvana.lib_share;


/**
 * Created by Administrator on 2018/2/23.
 */

public class ShareConstants {
    public static String APP_ID = "wxe32507eb20ecf22b";
    public static final String APP_KEY = "3589342165";
    public static final String REDIRECT_URL = "http://www.sina.com";
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    public static final String ShareTypeLink = "00";//00链接分享
    public static final String ShareTypeImg = "01";//01图片分享
    public static final String ShareTypeLinkAndImg = "02";//链接和图片分享

    public static final String ShareToWXSceneSession = "00";//00是朋友
    public static final String ShareToWXSceneTimeline = "01";//01是朋友圈
    public static final String ShareToWXSceneSessionAndWXSceneTimeline = "02";//所有
    public static final String ShareToWeiBo = "03";//微博
    public static final String ShareToWXSceneSessionAndWeiBo = "04";//朋友+微博
    public static final String ShareToWXSceneTimelineAndWeiBo = "05";// 朋友圈+微博
    public static final String ShareToAll = "";//其他全部

    public static final String SHARE_SUCCESS_TYPE_POP = "00";//00是分享弹框
    public static final String SHARE_SUCCESS_TYPE_LINK = "01";//01是分享链接
    public static final String SHARE_SUCCESS_TYPE_AUTO = "02";//02自由处理

}
