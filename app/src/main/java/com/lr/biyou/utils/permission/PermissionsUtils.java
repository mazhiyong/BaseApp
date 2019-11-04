package com.lr.biyou.utils.permission;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.lr.biyou.R;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * Describe：6.0动态权限管理帮助类
 *
 */
public class PermissionsUtils {

    /**
     * 判断权限是否授权
     *
     * @param context     context
     * @param permissions 权限列表
     */
    public static boolean checkPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionsList = new ArrayList<>();
            if (permissions != null && permissions.length != 0) {
                for (String permission : permissions) {
                    if (!isHavePermissions(context, permission)) {
                        permissionsList.add(permission);
                    }
                }
                if (permissionsList.size() > 0) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * 判断设置返回后，权限是否获得授权
     */
    public static boolean checkSettingPermissions(Context context, final List<String> permissions){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionsList = new ArrayList<>();
            if (permissions != null && permissions.size() != 0) {
                for (String permission : permissions) {
                    if (!isHavePermissions(context, permission)) {
                        permissionsList.add(permission);
                    }
                }
                if (permissionsList.size() > 0) {
                    return false;
                }
            }
        }
        return true;

    }


    /**
     * 检查是否授权某权限
     */
    private static boolean isHavePermissions(Context context, String permissions) {
        return ContextCompat.checkSelfPermission(context, permissions) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 动态申请单个权限
     */
    public static void  requsetRunPermission(final Context context, final RePermissionResultBack mSuccessBack, final String permissions){
        LogUtilDebug.i("show","111.....");
        AndPermission.with(context)
                .runtime()
                .permission(permissions)
                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //context.Toasts("授权成功");
                        if(mSuccessBack != null){
                            mSuccessBack.requestSuccess();
                        }
                    }
                }).onDenied(new Action<List<String>>() {
            @Override
            public void onAction(List<String> permissions) {
                LogUtilDebug.i("show","333.....");
                if(AndPermission.hasAlwaysDeniedPermission(context,permissions)){
                    showSettingDialog(context,mSuccessBack,permissions);
                }
            }
        }).start();



    }



    /**
     * 动态申请权限组
     * @param permissions
     */

    public static void  requsetRunPermission(final Context context, final RePermissionResultBack mResultBack, final String[]... permissions){
        AndPermission.with(context)
                .runtime()
                .permission(permissions)
                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //context.Toasts("授权成功");
                        if(mResultBack != null){
                            mResultBack.requestSuccess();
                        }
                    }
                }).onDenied(new Action<List<String>>() {
            @Override
            public void onAction(List<String> permissions) {
                Toast.makeText(context,R.string.failure,Toast.LENGTH_SHORT).show();
                if(AndPermission.hasAlwaysDeniedPermission(context,permissions)){
                    showSettingDialog(context,mResultBack,permissions);
                }
            }
        }).start();
    }

    /**
     * Display setting dialog.
     */
    public static void showSettingDialog(final Context context, final RePermissionResultBack mSuccessBack, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));

        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPermission(context,mSuccessBack,permissions);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mSuccessBack != null){
                            mSuccessBack.requestFailer();
                        }
                    }
                })
                .show();
    }


    protected static void setPermission(final Context context, final RePermissionResultBack mSuccessBack, final List<String> permissons){

        AndPermission.with(context).runtime().setting().start(1234);
        /*AndPermission.with(context)
                .runtime()
                .setting()
                .onComeback(new Setting.Action() {
                    @Override
                    public void onAction() {
                        LogUtilDebug.i("show","从设置返回.....");
                        //检测权限是否设置成功
                        if(PermissionsUtils.checkSettingPermissions(context,permissons)){
                            if(mSuccessBack != null){
                                mSuccessBack.requestSuccess();
                            }
                        }else {
                            if(mSuccessBack != null){
                                mSuccessBack.requestFailer();
                            }
                        }


                    }
                }).start();*/
    }
}
