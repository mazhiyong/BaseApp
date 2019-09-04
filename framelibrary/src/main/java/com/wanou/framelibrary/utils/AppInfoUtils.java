package com.wanou.framelibrary.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.wanou.framelibrary.GlobalApplication;

public class AppInfoUtils {
    private static long getLocalVersion() {
        long longVersionCode = 0;
        try {
            PackageInfo packageInfo = GlobalApplication.getContext().getPackageManager()
                    .getPackageInfo(GlobalApplication.getContext().getPackageName(), 0);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                longVersionCode = packageInfo.getLongVersionCode();
            } else {
                longVersionCode = packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return longVersionCode;
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName() {
        String localVersionName = "";
        try {
            PackageInfo packageInfo = GlobalApplication.getContext().getPackageManager()
                    .getPackageInfo(GlobalApplication.getContext().getPackageName(), 0);
            localVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersionName;
    }

    public static boolean isUpdate(int newVersion) {
        return newVersion > getLocalVersion();
    }
}
