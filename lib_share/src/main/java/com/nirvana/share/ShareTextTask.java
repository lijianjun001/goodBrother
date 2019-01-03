package com.nirvana.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cylty.zmkj.utils.BitmapUtil;
import com.cylty.zmkj.utils.FileUtils;

import java.io.File;
import java.lang.ref.SoftReference;

public class ShareTextTask extends AsyncTask<String, Void, String> {
    private SoftReference<Context> contextSf;

    public ShareTextTask(Context context) {
        this.contextSf = new SoftReference<>(context);
    }

    @Override
    protected String doInBackground(String... params) {
        ShareCache.setIsShareInBackGround(true);
        String result = "正在分享中";
        ShareModel shareModel = ShareCache.getShareModel();
        if (shareModel != null) {
            String title = shareModel.getShareTitle();
            String desc = shareModel.getShareContent();
            String imgUrl = shareModel.getShareIcon();
            String url = shareModel.getShareUrl();
            String isTimelineStr = shareModel.getShareTo();
            boolean isLine = ShareConstants.ShareToWXSceneTimeline.equals(isTimelineStr);
            try {
                if (imgUrl != null) {
                    File file = FileUtils.createImgFileByUrl(contextSf.get(), imgUrl);
                    byte[] thumbData = FileUtils.getFileBytesByUrl(file, imgUrl);
                    FileUtils.saveByteToFile(file, thumbData);
                    if (thumbData != null) {
                        if (thumbData.length / 1024 > 20) {
                            Bitmap bitmap = BitmapUtil.bytesToBitmap(thumbData);
                            thumbData = BitmapUtil.compressImageShape(bitmap);
                        }
                        ShareThirdPart.shareContentToWx(url, title, desc, thumbData, isLine);
                    } else {
                        shareUseLocalPic(url, title, desc, isLine);
                    }
                } else {
                    shareUseLocalPic(url, title, desc, isLine);
                }
            } catch (Exception e) {
                shareUseLocalPic(url, title, desc, isLine);
            }
        } else {
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

    private void shareUseLocalPic(String url, String title, String desc, boolean isLine) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(contextSf.get().getResources(), R.drawable.activity_cache);
            byte[] thumbData = BitmapUtil.compressImageQuality(bmp);
            ShareThirdPart.shareContentToWx(url, title, desc, thumbData, isLine);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
