package com.zhihu.matisse.internal.utils;

import android.content.Context;

import java.io.File;

public class FileHelper {

    private File cropCacheFolder;

    private static FileHelper mInstance;

    private FileHelper() {
    }

    public static FileHelper getInstance() {
        if (mInstance == null) {
            synchronized (FileHelper.class) {
                if (mInstance == null) {
                    mInstance = new FileHelper();
                }
            }
        }
        return mInstance;
    }

    public File getCropCacheFolder(Context context) {
        if (cropCacheFolder == null) {
            cropCacheFolder = new File(context.getCacheDir() + "/box/cropTemp/");
        }
        return cropCacheFolder;
    }
}
