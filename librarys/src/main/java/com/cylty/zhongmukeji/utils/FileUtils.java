package com.cylty.zhongmukeji.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FileUtils {

    // 读/写检查 
    static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static File createDestinationDirInExternalFilesDir(Context context, String dirType) {
        final File file = context.getExternalFilesDir(dirType);
        if (file == null) {
            throw new IllegalStateException("Failed to get external storage files directory");
        } else if (file.exists()) {
            if (!file.isDirectory()) {
                throw new IllegalStateException(file.getAbsolutePath() +
                        " already exists and is not a directory");
            }
        } else {
            if (!file.mkdirs()) {
                throw new IllegalStateException("Unable to create directory: " +
                        file.getAbsolutePath());
            }
        }
        return file;
    }

    /**
     * 创建目录
     */
    public static File mkdirs(String path) throws Exception {
        if (!isExternalStorageWritable()) {
            throw new IllegalStateException("the sdcard is not available");
        }
        File file = new File(path);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return file;
            } else {
                throw new IllegalStateException("file is not mkdirs");
            }
        }
        return file;
    }


    public static File createImgFileByUrl(Context context, String fileUrl) {
        String imageName = getFileNameByUrl(fileUrl);
        if (imageName == null) throw new IllegalArgumentException("fileUrl error");
        File fileDiv = FileUtils.createDestinationDirInExternalFilesDir(context, Environment.DIRECTORY_PICTURES);
        return new File(fileDiv.getAbsolutePath(), imageName);
    }

    /**
     * @param file    if file is exists,byte from file
     * @param fileUrl file url
     * @return
     * @throws IOException
     */
    public static byte[] getFileBytesByUrl(File file, String fileUrl) throws IOException {
        InputStream inputStream;
        if (file.exists()) {
            inputStream = new FileInputStream(file);
        } else {
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder().get()
                    .url(fileUrl)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                inputStream = response.body().byteStream();
            } else {
                throw new IOException("get file data error");
            }
        }
        byte[] data = null;
        if (inputStream != null) {
            data = BitmapUtil.toByteArray(inputStream);
            inputStream.close();
        }
        return data;
    }

    public static byte[] getFileBytes(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        byte[] data;
        data = BitmapUtil.toByteArray(inputStream);
        inputStream.close();
        return data;
    }


    /**
     * @param context the content
     * @param fileUrl file url
     * @return
     * @throws Exception
     */
    public static File saveImageByUrl(Context context, String fileUrl) throws Exception {
        File file = createImgFileByUrl(context, fileUrl);
        byte[] bytes = getFileBytesByUrl(file, fileUrl);
        saveByteToFile(file, bytes);
        return file;
    }

    /**
     * @param file the file save bytes
     * @param data bytes
     * @throws IOException
     */
    public static void saveByteToFile(File file, byte[] data) throws IOException {
        FileOutputStream outStream;
        if (!file.exists()) {
            outStream = new FileOutputStream(file);
            outStream.write(data);
            outStream.flush();
            outStream.close();
        }
    }

    /**
     * 获取目录名称
     *
     * @param url file's url
     * @return FileName
     */
    public static String getFileNameByUrl(String url) {
        int lastIndexStart = url.lastIndexOf("/");
        if (lastIndexStart != -1) {
            return url.substring(lastIndexStart + 1, url.length());
        } else {
            return null;
        }
    }


    /**
     * 删除该目录下的文件
     *
     * @param path file's path
     */
    public static void delFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                if (!file.delete()) {
                    throw new IllegalStateException("the file is not delete");
                }

            } else {
                throw new IllegalStateException("the file is not exists");
            }
        }
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    public static String changePath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * 解压缩一个文件
     *
     * @param zipFile 压缩文件
     * @throws IOException 当解压缩过程出错时抛出
     */

    public static void unzip(String zipFile, String targetDir) throws IOException {
        int BUFFER = 4096; //这里缓冲区我们使用4KB，
        String strEntry; //保存每个zip的条目名称
        BufferedOutputStream dest = null; //缓冲输出流
        FileInputStream fis = new FileInputStream(zipFile);
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
        ZipEntry entry; //每个zip条目的实例
        while ((entry = zis.getNextEntry()) != null) {
            try {
                int count;
                byte data[] = new byte[BUFFER];
                strEntry = entry.getName();
                File entryFile = new File(targetDir + strEntry);
                File entryDir = new File(entryFile.getParent());
                if (!entryDir.exists()) {
                    entryDir.mkdirs();
                }
                if (strEntry.endsWith("/")) {
                    entryFile.mkdirs();
                    continue;
                }
                FileOutputStream fos = new FileOutputStream(entryFile);
                dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = zis.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        zis.close();
    }

}
