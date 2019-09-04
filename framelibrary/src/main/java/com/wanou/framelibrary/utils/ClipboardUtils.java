package com.wanou.framelibrary.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import com.wanou.framelibrary.GlobalApplication;
import com.wanou.framelibrary.R;


/**
 * @author wodx521
 * @date on 2018/8/15
 */
public class ClipboardUtils {

    public static void copyText(String copyText) {
        //获取剪贴板管理器
        ClipboardManager cm = (ClipboardManager) GlobalApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        if (!TextUtils.isEmpty(copyText)) {
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyText);
            // 将ClipData内容放到系统剪贴板里。
            if (cm != null) {
                cm.setPrimaryClip(mClipData);
                UiTools.showToast(UiTools.getString(R.string.copy_success));
            }
        } else {
            UiTools.showToast(UiTools.getString(R.string.copy_fail));
        }
    }
}
