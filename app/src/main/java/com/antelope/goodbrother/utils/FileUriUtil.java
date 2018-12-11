package com.antelope.goodbrother.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import com.antelope.goodbrother.BuildConfig;

import java.io.File;

import androidx.core.content.FileProvider;

public class FileUriUtil {
    /**
     * @param context the content
     * @param file    file
     * @return the file uri
     */
    public static Uri getFileUriByFile(Context context, File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }
}
