package com.lr.biyou.manage;

import android.content.Context;
import android.view.View;

import com.lr.biyou.R;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mywidget.dialog.UpdateDialog;
import com.lr.biyou.service.DownloadService;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.yanzhenjie.permission.Permission;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * 版本更新管理
 */
public class UpdateManager {
    public static  UpdateManager manager;
    private WeakReference<Context> mReference;
    private UpdateDialog mUpdateDialog;
    public Context mContext;

    public UpdateManager(Context context) {
        mReference = new WeakReference<>(context);
        mContext = context;
    }

    public static synchronized UpdateManager getInstance(Context context) {
        if (manager == null) {
            manager = new UpdateManager(context);
        }
        return manager;
    }

    /**
     * 获取后台版本号，判断是否需要更新
     * @param
     * @return
     */
    public void  update(Map<String,Object> tData){
        String  versonCode=tData.get("versionCode")+"";
        String  versonName=tData.get("versionName")+"";
        String  versonMsg=tData.get("versionUpdateMsg")+"";
        String  versonDownUrl=tData.get("downUrl")+"";
        String  versonMd5Code=tData.get("md5Code")+"";
        String  versonForceUpdate=tData.get("forceUpdate")+"";
        String  versonOsType=tData.get("osType")+"";
        String  versonId=tData.get("id")+"";

        MbsConstans.UpdateAppConstans.VERSION_NET_CODE=Integer.parseInt(versonCode);
        MbsConstans.UpdateAppConstans.VERSION_NET_APK_NAME="boss";
        MbsConstans.UpdateAppConstans.VERSION_APK_ID=versonId;
        MbsConstans.UpdateAppConstans.VERSION_NET_CODE_NAME=versonName;
        MbsConstans.UpdateAppConstans.VERSION_NET_APK_URL=versonDownUrl;
        MbsConstans.UpdateAppConstans.VERSION_NET_UPDATE_MSG=versonMsg;
        MbsConstans.UpdateAppConstans.VERSION_MD5_CODE=versonMd5Code;
        if(versonForceUpdate.equals("true")){
            MbsConstans.UpdateAppConstans.VERSION_UPDATE_FORCE=true;
        }
        //升级
        String [] requset =new String[]{
                Permission.READ_EXTERNAL_STORAGE,
                Permission.WRITE_EXTERNAL_STORAGE,
                Permission.CAMERA
        };
        if( MbsConstans.UpdateAppConstans.VERSION_NET_CODE > MbsConstans.UpdateAppConstans.VERSION_APP_CODE){
            mUpdateDialog = new UpdateDialog(mReference.get(),true);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.cancel:
                            if(MbsConstans.UpdateAppConstans.VERSION_UPDATE_FORCE){
                            }else {
                                mUpdateDialog.dismiss();
                            }
                            break;
                        case R.id.confirm:
                            PermissionsUtils.requsetRunPermission(mReference.get(), new RePermissionResultBack() {
                                @Override
                                public void requestSuccess() {
                                    //授权成功，下载更新
                                    mUpdateDialog.getProgressLay().setVisibility(View.VISIBLE);
                                    DownloadService.downNewFile(MbsConstans.UpdateAppConstans.VERSION_NET_APK_URL, 1003,
                                            MbsConstans. UpdateAppConstans.VERSION_NET_APK_NAME,MbsConstans.UpdateAppConstans.VERSION_MD5_CODE,mReference.get());

                                }

                                @Override
                                public void requestFailer() {

                                }

                            }, Permission.Group.STORAGE);
                            break;
                    }
                }
            };
            mUpdateDialog.setCanceledOnTouchOutside(false);
            mUpdateDialog.setCancelable(false);
            String ss = "";
            if (MbsConstans.UpdateAppConstans.VERSION_UPDATE_FORCE){
                ss = "必须更新";
            }else {
                ss = "建议更新";
            }
            mUpdateDialog.setOnClickListener(onClickListener);
            mUpdateDialog.initValue("检查新版本"+"("+ss+")","更新内容:\n"+MbsConstans.UpdateAppConstans.VERSION_NET_UPDATE_MSG);
            mUpdateDialog.show();

            if (MbsConstans.UpdateAppConstans.VERSION_UPDATE_FORCE){
                mUpdateDialog.setCancelable(false);
                mUpdateDialog.tv_cancel.setEnabled(false);
                mUpdateDialog.tv_cancel.setVisibility(View.GONE);
            }else {
                mUpdateDialog.setCancelable(true);
                mUpdateDialog.tv_cancel.setEnabled(true);
                mUpdateDialog.tv_cancel.setVisibility(View.VISIBLE);
            }
            mUpdateDialog.getProgressLay().setVisibility(View.GONE);
            DownloadService.mProgressBar = mUpdateDialog.getProgressBar();
            DownloadService.mTextView = mUpdateDialog.getPrgText();
        }
        }


    }


