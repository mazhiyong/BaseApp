package com.wanou.framelibrary.weight;

import android.app.Activity;
import android.app.Dialog;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.wanou.framelibrary.R;
import com.wanou.framelibrary.utils.UiTools;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wodx521
 * @date on 2018/9/1
 */
public class LoadingDialog {
    private static List<Dialog> dialogList = new ArrayList<>();

    public static void getDialog(Activity activity, String loadingContent) {
        AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialogList.add(dialog);
        View view = UiTools.parseLayout(R.layout.loading_dialog);
        TextView mTvLoadingContent = view.findViewById(R.id.tv_loading_content);
        if (!TextUtils.isEmpty(loadingContent)) {
            mTvLoadingContent.setText(loadingContent);
        } else {
            mTvLoadingContent.setText(R.string.loading);
        }
        dialog.setView(view);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        int deviceWidth = UiTools.getDeviceWidth(activity);
        attributes.width = deviceWidth / 3;
        dialog.getWindow().setAttributes(attributes);
    }

    public static void getDialog(Fragment fragment, String loadingContent) {
        AlertDialog dialog = new AlertDialog.Builder(fragment.getActivity()).create();
        dialogList.add(dialog);
        View view = UiTools.parseLayout(R.layout.loading_dialog);
        TextView mTvLoadingContent = view.findViewById(R.id.tv_loading_content);
        if (!TextUtils.isEmpty(loadingContent)) {
            mTvLoadingContent.setText(loadingContent);
        } else {
            mTvLoadingContent.setText(R.string.loading);
        }
        dialog.setView(view);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        int deviceWidth = UiTools.getDeviceWidth(fragment.getActivity());
        attributes.width = deviceWidth / 3;
        dialog.getWindow().setAttributes(attributes);
    }

    public static void dismiss() {
        if (dialogList.size() > 0) {
            for (Dialog dialog1 : dialogList) {
                if (dialog1 != null && dialog1.isShowing()) {
                    dialog1.dismiss();
                }
            }
        }
    }
}
