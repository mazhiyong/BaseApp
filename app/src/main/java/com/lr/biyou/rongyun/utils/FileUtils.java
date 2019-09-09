package com.lr.biyou.rongyun.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import com.lr.biyou.basic.BasicApplication;
import com.lr.biyou.rongyun.common.LogTag;
import com.lr.biyou.rongyun.utils.log.SLog;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtils {
    public static String saveBitmapToFile(Bitmap bitmap, File toFile) {
        try {
            FileOutputStream fos = new FileOutputStream(toFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        SLog.e(LogTag.FILE, "save image to path:" + toFile.getPath());

        return toFile.getPath();
    }

    public static String saveBitmapToPublicPictures(Bitmap bitmap, String fileName) {
        File saveFileDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!saveFileDirectory.exists()) {
            saveFileDirectory.mkdirs();
        }

        File saveFile = new File(saveFileDirectory, fileName);
        return saveBitmapToFile(bitmap, saveFile);
    }

    public static String saveBitmapToCache(Bitmap bitmap, String fileName) {
        File saveFileDirectory = BasicApplication.getContext().getExternalCacheDir();
        if(saveFileDirectory == null){
            saveFileDirectory = BasicApplication.getContext().getCacheDir();
        }
        if (!saveFileDirectory.exists()) {
            saveFileDirectory.mkdirs();
        }

        File saveFile = new File(saveFileDirectory, fileName);
        return saveBitmapToFile(bitmap, saveFile);
    }
}
