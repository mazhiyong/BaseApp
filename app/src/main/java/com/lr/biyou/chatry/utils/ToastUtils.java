package com.lr.biyou.chatry.utils;

import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import com.lr.biyou.basic.BasicApplication;
import com.lr.biyou.chatry.common.ErrorCode;

/**
 * Toast 工具类
 */
public class ToastUtils {
    private static Toast lastToast;

    public static void showErrorToast(ErrorCode errorCode) {
        //根据错误码进行对应错误提示
        String message = errorCode.getMessage();
        if (TextUtils.isEmpty(message)) return;

        if (lastToast != null) {
            lastToast.setText(message);
        } else {

            lastToast = Toast.makeText(BasicApplication.getContext(), message, Toast.LENGTH_SHORT);
        }
        lastToast.show();
    }

    public static void showErrorToast(int errorCode) {
        showErrorToast(ErrorCode.fromCode(errorCode));
    }

    public static void showToast(int resourceId) {
        showToast(resourceId, Toast.LENGTH_SHORT);
    }

    public static void showToast(int resourceId, int duration) {
        showToast(BasicApplication.getContext().getResources().getString(resourceId), duration);
    }

    public static void showToast(String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    public static void showToast(String message, int duration) {
        if (TextUtils.isEmpty(message)) return;

        // 9.0 以上直接用调用即可防止重复的显示的问题，且如果复用 Toast 会出现无法再出弹出对话框问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
             Toast.makeText(BasicApplication.getContext(), message, duration).show();
        } else {
            if (lastToast != null) {
                lastToast.setText(message);
            } else {
                lastToast = Toast.makeText(BasicApplication.getContext(), message, duration);
            }
            lastToast.show();
        }
    }
}
