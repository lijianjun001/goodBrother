package com.nirvana.lib_share;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cylty.zhongmukeji.utils.BitmapUtil;
import com.cylty.zhongmukeji.utils.FileUtils;
import com.sina.weibo.sdk.share.WbShareHandler;

import java.io.File;
import java.lang.ref.SoftReference;

public class ShareTextAndImageTask extends AsyncTask<String, Void, String> {
    private SoftReference<Context> contextSf;
    private WbShareHandler wbShareHandler;

    public ShareTextAndImageTask(Context context, WbShareHandler wbShareHandler) {
        this.contextSf = new SoftReference<>(context);
        this.wbShareHandler = wbShareHandler;
    }

    @Override
    protected String doInBackground(String... params) {
        ShareCache.setIsShareInBackGround(true);
        ShareModel shareModel = ShareCache.getShareModel();
        String result;
        try {
            if (shareModel != null) {
                String imgUrl = shareModel.getImg();
                File file = FileUtils.createImgFileByUrl(contextSf.get(), imgUrl);
                byte[] data = FileUtils.getFileBytesByUrl(file, imgUrl);
                FileUtils.saveByteToFile(file, data);
                ShareThirdPart.shareTextAndImgToWeibo(wbShareHandler, shareModel.getShareTitle(), shareModel.getShareContent(), shareModel.getShareUrl(), BitmapUtil.bytesToBitmap(data));
                result = "正在分享中";
            } else {
                result = "分享失败";
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
