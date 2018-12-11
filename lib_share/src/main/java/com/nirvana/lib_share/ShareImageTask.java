package com.nirvana.lib_share;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cylty.zhongmukeji.utils.BitmapUtil;
import com.cylty.zhongmukeji.utils.FileUtils;
import com.sina.weibo.sdk.share.WbShareHandler;

import java.io.File;
import java.lang.ref.SoftReference;

public class ShareImageTask extends AsyncTask<String, Void, String> {
    private SoftReference<Context> contextSf;
    private WbShareHandler wbShareHandler;

    public ShareImageTask(Context context, WbShareHandler wbShareHandler) {
        this.contextSf = new SoftReference<>(context);
        this.wbShareHandler = wbShareHandler;
    }

    @Override
    protected String doInBackground(String... params) {
        ShareCache.setIsShareInBackGround(true);
        ShareModel shareModel = ShareCache.getShareModel();
        String result;
        String isTimelineStr = ShareCache.getShareModel().getShareTo();
        final boolean isLine = ShareConstants.ShareToWXSceneTimeline.equals(isTimelineStr);
        try {
            String imgPath = shareModel.getImg();
            File file;
            byte data[];
            if (imgPath.startsWith("http")) {
                file = FileUtils.createImgFileByUrl(contextSf.get(), imgPath);
                data = FileUtils.getFileBytesByUrl(file, imgPath);
                FileUtils.saveByteToFile(file, data);
            } else {
                file = new File(imgPath);
                data = FileUtils.getFileBytes(file);
            }
            if (data != null) {
                if (ShareConstants.ShareToWeiBo.equals(ShareCache.getShareModel().getShareTo())) {
                    ShareThirdPart.shareImgToWeibo(wbShareHandler, BitmapUtil.bytesToBitmap(data));
                } else {
                    ShareThirdPart.shareImgToWx(file.getAbsolutePath(), isLine);
                }
                result = "正在分享中";
            } else {
                result = "图片获取失败，分享失败";
            }
        } catch (Exception e) {
            result = "分享失败";
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        ShareCache.setIsShareInBackGround(false);
        Context context = contextSf.get();
        if (context != null) {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        }
    }
}
