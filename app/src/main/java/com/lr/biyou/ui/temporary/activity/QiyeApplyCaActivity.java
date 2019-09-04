package com.lr.biyou.ui.temporary.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.AppUtil;
import com.lr.biyou.utils.tool.FileUtils;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
import com.jaeger.library.StatusBarUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;


/**
 * 企业申请证书  界面
 */
public class QiyeApplyCaActivity extends BasicActivity implements RequestView {

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.right_img)
    ImageView mRightImg;
    @BindView(R.id.top_layout)
    LinearLayout mTitleBarView;

    @BindView(R.id.qiye_down_tv1)
    TextView mQiyeDownTv1;
    @BindView(R.id.qiye_down_tv2)
    TextView mQiyeDownTv2;
    @BindView(R.id.qiye_image1)
    ImageView mQiyeImage1;
    @BindView(R.id.qiye_pdf_cardView1)
    CardView mQiyePdfCardView1;
    @BindView(R.id.qiye_image2)
    ImageView mQiyeImage2;
    @BindView(R.id.qiye_pdf_cardView2)
    CardView mQiyePdfCardView2;
    @BindView(R.id.qiye_qianzhang_image)
    ImageView mQiyeQianzhangImage;
    @BindView(R.id.qiye_qianzhang_lay)
    CardView mQiyeQianzhangLay;
    @BindView(R.id.but_next)
    Button mButNext;
    @BindView(R.id.open1)
    Button mOpen1;
    @BindView(R.id.open2)
    Button mOpen2;


    private String mName = "";
    private String mIdNum = "";

    private String mRequestTag = "";

    private int mCurrentPosition = 0;

    @Override
    public int getContentView() {
        return R.layout.activity_qiye_apply_ca;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mName = bundle.getString("idname");
            mIdNum = bundle.getString("idno");
        }
        mTitleText.setText(getResources().getString(R.string.qiye_apply_ca));
    }

    private void submitInfoAction() {

        mRequestTag = MethodUrl.peopleAuth;
        Map<String, Object> mSignMap = new HashMap<>();

        Map<String, Object> map = new HashMap<>();
        map.put("idno", mIdNum);
        map.put("idname", mName);
        Map<String, Object> fileMap = new HashMap<>();

        fileMap.put("img_front", mQiyeImage1.getTag());
        fileMap.put("img_back", mQiyeImage2.getTag());
        fileMap.put("img_hand", mQiyeQianzhangImage.getTag());
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.postFileToMap(mHeaderMap, MethodUrl.peopleAuth, mSignMap , map, fileMap);
    }
    private void downFileAction(){
        String url = "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk";
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.downloadFile("bytes=0" +"-",mHeaderMap, url, map);
    }

    private boolean isAvilible( Context context, String packageName )
    {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for ( int i = 0; i < pinfo.size(); i++ )
        {
            if(pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    @OnClick({R.id.back_img,R.id.left_back_lay,R.id.open1,R.id.open2,
            R.id.qiye_down_tv1, R.id.qiye_down_tv2, R.id.qiye_pdf_cardView1, R.id.qiye_pdf_cardView2, R.id.qiye_qianzhang_lay, R.id.but_next})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.open1:

               /* if (mOpen1.getTag() == null){

                }else {
                    File file = (File)mOpen1.getTag();
                     intent = new Intent("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    if (isAvilible(QiyeApplyCaActivity.this, "cn.wps.moffice_eng")) {
                        intent.setClassName("cn.wps.moffice_eng",
                                "cn.wps.moffice.documentmanager.PreStartActivity2");
                    } else {
                        intent.addCategory("android.intent.category.DEFAULT");
                    }
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri uri;
                    //下面这句指定调用相机拍照后的照片存储的路径
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(QiyeApplyCaActivity.this,
                                AppUtil.getAppProcessName(QiyeApplyCaActivity.this)+".fileProvider", file);
                    } else {
                        uri = Uri.fromFile(file);
                    }
                    intent.setDataAndType(uri, "application/pdf");
                    startActivity(intent);

                    System.out.println(AppUtil.getAppProcessName(QiyeApplyCaActivity.this)+" "+file.getAbsolutePath());
                }
*/
                break;
            case R.id.open2:

                break;
            case R.id.qiye_down_tv1:
               /* intent = new Intent(QiyeApplyCaActivity.this,PDFLookActivity.class);
                intent.putExtra("id","https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk");
                startActivity(intent);*/
                downFileAction();
                break;
            case R.id.qiye_down_tv2:
                intent = new Intent(QiyeApplyCaActivity.this,PDFLookActivity.class);
                intent.putExtra("id","https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk");
                startActivity(intent);
                break;
            case R.id.qiye_pdf_cardView1:
                mCurrentPosition = 1;

                PermissionsUtils.requsetRunPermission(QiyeApplyCaActivity.this, new RePermissionResultBack() {
                    @Override
                    public void requestSuccess() {
                        selectPic();
                    }

                    @Override
                    public void requestFailer() {
                        toast(R.string.failure);
                    }
                },Permission.Group.CAMERA,Permission.Group.STORAGE);
                break;
            case R.id.qiye_pdf_cardView2:
                mCurrentPosition = 2;
                PermissionsUtils.requsetRunPermission(QiyeApplyCaActivity.this, new RePermissionResultBack() {
                    @Override
                    public void requestSuccess() {
                        selectPic();
                    }

                    @Override
                    public void requestFailer() {
                        toast(R.string.failure);
                    }
                },Permission.Group.CAMERA,Permission.Group.STORAGE);
                break;
            case R.id.qiye_qianzhang_lay:
                mCurrentPosition = 3;
                PermissionsUtils.requsetRunPermission(QiyeApplyCaActivity.this, new RePermissionResultBack() {
                    @Override
                    public void requestSuccess() {
                        selectPic();
                    }

                    @Override
                    public void requestFailer() {
                        toast(R.string.failure);
                    }
                },Permission.Group.CAMERA,Permission.Group.STORAGE);
                break;
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.but_next:
//                submitInfoAction();
                intent = new Intent(QiyeApplyCaActivity.this,QiyeCaPayActivity.class);
                startActivity(intent);
                break;

        }
    }


    private void downFile(Map<String,Object> map){
        LogUtilDebug.i("下载文件","这里执行么没有");
        Response<ResponseBody> response = (Response<ResponseBody>)map.get("file");
        if (response.body() == null) {
            System.out.println("资源错误");
            return;
        }

        OutputStream os = null;
        InputStream is = response.body().byteStream();

        long totalLength = response.body().contentLength();
        long currentLength = 0;

        File rootFile=new File(MbsConstans.APP_DOWN_PATH);
        if(!rootFile.exists()&&!rootFile.isDirectory())
            rootFile.mkdirs();
        File file = new File(MbsConstans.APP_DOWN_PATH+"/"+System.currentTimeMillis()+".apk");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            os = new FileOutputStream(file);
            int len;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
                currentLength += len;
                LogUtilDebug.i("下载文件", "当前进度: " + currentLength);
                LogUtilDebug.i("下载文件",(int) (100 * currentLength / totalLength)+"  %");
                if ((int) (100 * currentLength / totalLength) == 100) {
                    LogUtilDebug.i("下载文件", "下载完成");
                }
            }
        } catch (FileNotFoundException e) {
            LogUtilDebug.i("下载文件","未找到文件！");
            e.printStackTrace();
        } catch (IOException e) {
            LogUtilDebug.i("下载文件","IO错误！");
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        mOpen1.setTag(file);
    }



    private List<LocalMedia> selectList = new ArrayList<>();
    private void selectPic(){
        File path = new File( MbsConstans.UPLOAD_PATH);
        if (!path.exists()) {
            path.mkdirs();
        }
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(QiyeApplyCaActivity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                // .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(0)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(true)// 是否可预览视频 true or false
                .enablePreviewAudio(true) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .glideOverride(160, 160)
                //.sizeMultiplier(0.4f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                //.glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                // .withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                //  .hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(true)// 是否显示gif图片 true or false
                .compressSavePath( MbsConstans.UPLOAD_PATH)//压缩图片保存地址
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                //.openClickSound(true)// 是否开启点击声音 true or false
                //.selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
                //.previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                // .cropCompressQuality(90)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                //.rotateEnabled() // 裁剪是否可旋转图片 true or false
                //.scaleEnabled()// 裁剪是否可放大缩小图片 true or false
                // .videoQuality()// 视频录制质量 0 or 1 int
                //.videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                //.videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                //.recordVideoSecond()//视频秒数录制 默认60s int
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                //.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                .forResult(mCurrentPosition);//结果回调onActivityResult code
        //注意  这个  mCurrentPosition   是标记后台返回的文件类别类型的索引
    }



    private static final int TAKE_PICTURE = 0x000001;

    private String saveP = "";

    public void photo() {
        PermissionsUtils.requsetRunPermission(QiyeApplyCaActivity.this, new RePermissionResultBack() {
            @Override
            public void requestSuccess() {
                selectPic();
            }

            @Override
            public void requestFailer() {
                toast(R.string.failure);
            }
        },Permission.Group.CAMERA,Permission.Group.STORAGE);
    }

    private void getCamera() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = String.valueOf(System.currentTimeMillis());
        String savePath = MbsConstans.PHOTO_PATH;
        try {
            FileUtils.createSDDir("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        saveP = savePath + File.separator + fileName + ".jpg";

        Uri uri = Uri.fromFile(new File(saveP));

        //下面这句指定调用相机拍照后的照片存储的路径
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(QiyeApplyCaActivity.this, AppUtil.getAppProcessName(QiyeApplyCaActivity.this) + ".fileProvider", new File(savePath, fileName + ".jpg"));
        } else {
            uri = Uri.fromFile(new File(savePath, fileName + ".jpg"));
        }
        // 指定存储路径，这样就可以保存原图了
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        // 拍照返回图片
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == mCurrentPosition){
                selectList = PictureSelector.obtainMultipleResult(data);
                LogUtilDebug.i("已选择的图片",selectList+"");
                showProgressDialog("正在上传，请稍后...");
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                for (int i = 0;i<selectList.size();i++){
                    LocalMedia localMedia = selectList.get(i);
                    uploadFile(localMedia,mCurrentPosition+"");
                }
            }
        }
    }


    private void uploadFile(LocalMedia localMedia,String code){

        // mRequestTag = MethodUrl.creUploadFile;
        Map<String,Object> mSignMap = new HashMap<>();
        mSignMap.put("file",localMedia);
        mSignMap.put("code",code);

        Map<String,Object> mParamMap = new HashMap<>();

        String oldPath = localMedia.getPath();
        Map<String, Object> map = new HashMap<>();
        map.put("file",localMedia.getCompressPath());
        //map.put("fileName",mDataMap.get("precreid")+""+System.currentTimeMillis()+""+i);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.postFileToMap(mHeaderMap, MethodUrl.creUploadFile,mSignMap,mParamMap,map);
    }


    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void disimissProgress() {
        dismissProgressDialog();
    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {

        Intent intent;
        switch (mType) {
            case MethodUrl.creUploadFile:
                dismissProgressDialog();
                String code = tData.get("code")+"";
                LocalMedia localMedia = (LocalMedia) tData.get("file");
                String path = "";
                if (localMedia.isCut() && !localMedia.isCompressed()) {
                    // 裁剪过
                    path = localMedia.getCutPath();
                } else if (localMedia.isCompressed() || (localMedia.isCut() && localMedia.isCompressed())) {
                    // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                    path = localMedia.getCompressPath();
                } else {
                    // 原图
                    path = localMedia.getPath();
                }
                switch (code){
                    case "1":
                        GlideUtils.loadImage(QiyeApplyCaActivity.this,path,mQiyeImage1);
                        break;
                    case "2":
                        GlideUtils.loadImage(QiyeApplyCaActivity.this,path,mQiyeImage2);
                        break;
                    case "3":
                        GlideUtils.loadImage(QiyeApplyCaActivity.this,path,mQiyeQianzhangImage);
                        break;
                }
                break;
            case "https://qd.myapp.com/myapp/qqteam/AndroidQQ/mobileqq_android.apk":
                downFile(tData);
                break;
            case MethodUrl.peopleAuth:
                FileUtils.deleteDir(MbsConstans.PHOTO_PATH);

                intent = new Intent(QiyeApplyCaActivity.this, CheckWatiActivity.class);
                startActivity(intent);
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.peopleAuth:
                        submitInfoAction();
                        break;
                }
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        switch (mType) {
            case MethodUrl.creUploadFile://
                dismissProgressDialog();
                break;
        }
        dealFailInfo(map, mType);
    }



    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        super.finish();
        FileUtils.deleteDir(MbsConstans.UPLOAD_PATH);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            PictureFileUtils.deleteCacheDirFile(this);
            PictureFileUtils.deleteExternalCacheDirFile(this);
        }catch (Exception e){

        }
    }

}
