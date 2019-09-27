package com.antelope.goodbrother.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;

public class UriUtil {
    public static void openSetting(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    public static void openBrowser(Activity activity, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }

    public static void openCall(Activity activity, String url) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        activity.startActivity(intent);
    }


    public static void openFileChooser(Activity activity, String type, String title, int reqCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(type);
        activity.startActivityForResult(Intent.createChooser(intent, title), reqCode);
    }

    public static void openPick(Activity activity, int reqCode) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(pickIntent, reqCode);
    }

    public static void openCapture(Activity activity, Uri outputUri, int reqCode) {
        Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                outputUri);
        activity.startActivityForResult(takeIntent, reqCode);
    }

    // 相机——调用系统裁剪
    public static void startPhotoZoom(Activity activity, Uri inputUri, Uri outputUri, int reqCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(inputUri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以裁剪
        intent.putExtra("crop", true);
        // aspectX,aspectY是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY是裁剪图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  //7.0
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.putExtra("scale", true);
        // 设置是否返回数据
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);//这里必须是Uri.fromFile(file)
        activity.startActivityForResult(intent, reqCode);
    }
}
