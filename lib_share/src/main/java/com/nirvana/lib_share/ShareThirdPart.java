package com.nirvana.lib_share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

public class ShareThirdPart {
    public static IWXAPI wxapi;

    public static void register(Context context) {
        //微信
        wxapi = WXAPIFactory.createWXAPI(context, ShareConstants.APP_ID, true);
        //微博
        AuthInfo mAuthInfo = new AuthInfo(context, ShareConstants.APP_KEY, ShareConstants.REDIRECT_URL, ShareConstants.SCOPE);
        WbSdk.install(context, mAuthInfo);
    }

    // 微信分享 缩略图（thumb）：最大64KB，支持JPG格式
    static void shareContentToWx(String url, String title, String description, byte[] thumbData,
                                 boolean isTimeLine) {
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = url;
        WXMediaMessage mediaMessage = new WXMediaMessage(webPage);
        mediaMessage.title = title;
        mediaMessage.description = description;

        mediaMessage.thumbData = thumbData;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = mediaMessage;
        req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        wxapi.sendReq(req);
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    static void shareImgToWx(String filePath, boolean isTimeLine) {
        try {
            WXImageObject imgObj = new WXImageObject();
            imgObj.setImagePath(filePath);

            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObj;

            Bitmap bmp = BitmapFactory.decodeFile(filePath);
            int THUMB_SIZE = 100;
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
            bmp.recycle();
            msg.thumbData = bmpToByteArray(thumbBmp, true);

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
            wxapi.sendReq(req);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void shareMiniProgramToWx(String webpageUrl, String username, String path, String thumbPath, String title, String description) {
        try {
            WXMiniProgramObject wxMiniProgramObject = new WXMiniProgramObject();
            wxMiniProgramObject.webpageUrl = webpageUrl;//兼容老版本的网页链接
            wxMiniProgramObject.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
            wxMiniProgramObject.userName = username;//小程序的原始id
            wxMiniProgramObject.path = path;//小程序页面路径

            WXMediaMessage wxMediaMessage = new WXMediaMessage(wxMiniProgramObject);
            wxMediaMessage.title = title;
            wxMediaMessage.description = description;
            Bitmap bmp = BitmapFactory.decodeFile(thumbPath);
            int THUMB_SIZE = 100;
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
            bmp.recycle();
            wxMediaMessage.thumbData = bmpToByteArray(thumbBmp, true);// 小程序消息封面图片，小于128k
            wxMediaMessage.mediaObject = wxMiniProgramObject;
            wxMediaMessage.thumbData = null;
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");
            req.message = wxMediaMessage;
            req.scene = SendMessageToWX.Req.WXSceneSession;  // 目前支持会话
            wxapi.sendReq(req);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void shareImgToWeibo(WbShareHandler shareHandler, Bitmap bitmap) {
        try {
            WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
            ImageObject imageObject = new ImageObject();
            imageObject.setImageObject(bitmap);
            weiboMultiMessage.imageObject = imageObject;
            shareHandler.shareMessage(weiboMultiMessage, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void shareTextAndImgToWeibo(WbShareHandler shareHandler, String title, String content, String url, Bitmap bitmap) {
        try {
            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
            TextObject textObject = new TextObject();
            textObject.text = title + content + url;
            weiboMessage.textObject = textObject;
            ImageObject imageObject = new ImageObject();
            imageObject.setImageObject(bitmap);
            weiboMessage.imageObject = imageObject;
            shareHandler.shareMessage(weiboMessage, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shareToWeibo(WbShareHandler shareHandler, String content, String title, String url) {
        try {
            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
            TextObject textObject = new TextObject();
            textObject.text = title + "\n" + content + "\n" + url;
            textObject.title = title;
            textObject.actionUrl = url;
            weiboMessage.textObject = textObject;
            shareHandler.shareMessage(weiboMessage, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
