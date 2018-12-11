package com.cylty.zhongmukeji.loadSkin.loader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;

import com.cylty.zhongmukeji.loadSkin.config.SkinConfig;
import com.cylty.zhongmukeji.loadSkin.listener.ILoaderListener;
import com.cylty.zhongmukeji.loadSkin.listener.ISkinLoader;
import com.cylty.zhongmukeji.loadSkin.listener.ISkinUpdate;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class SkinManager implements ISkinLoader {

    private static Object synchronizedLock = new Object();
    private static SkinManager instance;

    private List<ISkinUpdate> skinObservers;
    private Context context;
    private String skinPackageName;
    private Resources mResources;
    private String skinPath;
    private boolean isDefaultSkin = false;

    /**
     * whether the skin being used is from external .skin file
     *
     * @return is external skin = true
     */
    public boolean isExternalSkin() {
        return !isDefaultSkin && mResources != null;
    }

    /**
     * get current skin path
     *
     * @return current skin path
     */
    public String getSkinPath() {
        return skinPath;
    }

    /**
     * return a global static instance of {@link SkinManager}
     *
     * @return
     */
    public static SkinManager getInstance() {
        if (instance == null) {
            synchronized (synchronizedLock) {
                if (instance == null) {
                    instance = new SkinManager();
                }
            }
        }
        return instance;
    }

    public Resources getResources() {
        return mResources;
    }

    private SkinManager() {


    }

    public void init(Context ctx) {
        context = ctx.getApplicationContext();
        File fileDiv = setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS);
        skinPath = fileDiv.getAbsolutePath() + "/" + SkinConfig.DEFAULT_SKIN_NAME;
    }

    /**
     * Load resources from apk in asyc task
     *
     * @param callback callback to notify user
     */
    public void load(final ILoaderListener callback) {
        new AsyncTask<String, Void, Resources>() {

            protected void onPreExecute() {
                if (callback != null) {
                    callback.onStart();
                }
            }
            @Override
            protected Resources doInBackground(String... params) {
                try {
                    File file = new File(skinPath);
                    if (file == null || !file.exists()) {
                        return null;
                    }
                    SecurityChecker securityChecker = new SecurityChecker(context);
                    if (!securityChecker.verifyApk(file.getAbsoluteFile())) {
                        return null;
                    }
                    PackageManager mPm = context.getPackageManager();
                    PackageInfo mInfo = mPm.getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES);
                    skinPackageName = mInfo.packageName;

                    AssetManager assetManager = AssetManager.class.newInstance();
                    Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                    addAssetPath.invoke(assetManager, skinPath);

                    Resources superRes = context.getResources();
                    Resources skinResource = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
                    isDefaultSkin = false;
                    return skinResource;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            protected void onPostExecute(Resources result) {
                mResources = result;

                if (mResources != null) {
                    if (callback != null) callback.onSuccess();
                    notifySkinUpdate();
                } else {
                    isDefaultSkin = true;
                    if (callback != null) callback.onFailed();
                }
            }

            ;

        }.execute();
    }

    @Override
    public void attach(ISkinUpdate observer) {
        if (skinObservers == null) {
            skinObservers = new ArrayList<ISkinUpdate>();
        }
        if (!skinObservers.contains(skinObservers)) {
            skinObservers.add(observer);
        }
    }

    @Override
    public void detach(ISkinUpdate observer) {
        if (skinObservers == null) return;
        if (skinObservers.contains(observer)) {
            skinObservers.remove(observer);
        }
    }

    @Override
    public void notifySkinUpdate() {
        if (skinObservers == null) return;
        for (ISkinUpdate observer : skinObservers) {
            observer.onThemeUpdate();
        }
    }

    public float getSize(int resId) {
        float size = context.getResources().getDimension(resId);
        if (mResources == null || isDefaultSkin) {
            return size;
        }
        String resName = context.getResources().getResourceEntryName(resId);

        int trueResId = mResources.getIdentifier(resName, "dimen", skinPackageName);
        float trueSize = 0;

        try {
            trueSize = mResources.getDimension(trueResId);
        } catch (NotFoundException e) {
            e.printStackTrace();
            trueSize = size;
        }
        return trueSize;
    }

    public int getColor(int resId) {
        int originColor = context.getResources().getColor(resId);
        if (mResources == null || isDefaultSkin) {
            return originColor;
        }

        String resName = context.getResources().getResourceEntryName(resId);

        int trueResId = mResources.getIdentifier(resName, "color", skinPackageName);
        int trueColor = 0;

        try {
            trueColor = mResources.getColor(trueResId);
        } catch (NotFoundException e) {
            e.printStackTrace();
            trueColor = originColor;
        }

        return trueColor;
    }

    @SuppressLint("NewApi")
    public Drawable getDrawable(int resId) {
        Drawable originDrawable = context.getResources().getDrawable(resId);
        if (mResources == null || isDefaultSkin) {
            return originDrawable;
        }
        String resName = context.getResources().getResourceEntryName(resId);

        int trueResId = mResources.getIdentifier(resName, "drawable", skinPackageName);

        Drawable trueDrawable = null;
        try {
            if (android.os.Build.VERSION.SDK_INT < 22) {
                trueDrawable = mResources.getDrawable(trueResId);
            } else {
                trueDrawable = mResources.getDrawable(trueResId, null);
            }
        } catch (NotFoundException e) {
            e.printStackTrace();
            trueDrawable = originDrawable;
        }

        return trueDrawable;
    }

    /**
     * 加载指定资源颜色drawable,转化为ColorStateList，保证selector类型的Color也能被转换。</br>
     * 无皮肤包资源返回默认主题颜色
     *
     * @param resId
     * @return
     * @author pinotao
     */
    public ColorStateList convertToColorStateList(int resId) {

        boolean isExtendSkin = true;

        if (mResources == null || isDefaultSkin) {
            isExtendSkin = false;
        }

        String resName = context.getResources().getResourceEntryName(resId);
        if (isExtendSkin) {
            int trueResId = mResources.getIdentifier(resName, "color", skinPackageName);
            ColorStateList trueColorList = null;
            if (trueResId == 0) { // 如果皮肤包没有复写该资源，但是需要判断是否是ColorStateList
                try {
                    ColorStateList originColorList = context.getResources().getColorStateList(resId);
                    return originColorList;
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    trueColorList = mResources.getColorStateList(trueResId);
                    return trueColorList;
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                ColorStateList originColorList = context.getResources().getColorStateList(resId);
                return originColorList;
            } catch (NotFoundException e) {
                e.printStackTrace();
            }

        }
        int[][] states = new int[1][1];
        return new ColorStateList(states, new int[]{context.getResources().getColor(resId)});
    }


    private static File setDestinationInExternalFilesDir(Context context, String dirType) {
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
}