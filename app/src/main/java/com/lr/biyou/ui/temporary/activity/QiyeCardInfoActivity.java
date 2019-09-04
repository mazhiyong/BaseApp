package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.FileUtils;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;
import com.lr.biyou.mywidget.view.BankCardTextWatcher;
import com.jaeger.library.StatusBarUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 企业账号信息录入   界面
 */
public class QiyeCardInfoActivity extends BasicActivity implements RequestView, SelectBackListener {

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
    @BindView(R.id.name_tv)
    EditText mNameTv;
    @BindView(R.id.zhanghu_eidt)
    EditText mZhanghuEidt;
    @BindView(R.id.kaihu_bank_value_tv)
    TextView mKaihuBankValueTv;
    @BindView(R.id.name_bank_lay)
    CardView mNameBankLay;
    @BindView(R.id.kaihu_bank_dian_tv)
    TextView mKaihuBankDianTv;
    @BindView(R.id.kaihu_bank_lay)
    CardView mKaihuBankLay;
    @BindView(R.id.but_next)
    Button mButNext;
    @BindView(R.id.qiye_xuke_image)
    ImageView mQiyeXukeImage;


    private String mRequestTag = "";

    private String mBankNum = "";
    private String mName = "";

    private Map<String, Object> mHezuoMap;

    private boolean mIsShow = false;

    private Map<String, Object> mBankMap;
    private Map<String, Object> mWangDianMap;
    private Map<String, String> mAddMap;

    @Override
    public int getContentView() {
        return R.layout.activity_qiye_card_info;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mHezuoMap = (Map<String, Object>) bundle.getSerializable("DATA");
        }

        mTitleText.setText(getResources().getString(R.string.qiye_dakuan_check));

        BankCardTextWatcher.bind(mZhanghuEidt);

        getUserInfoAction();
    }

    /**
     * 获取用户基本信息
     */
    public void getUserInfoAction() {
        mRequestTag = MethodUrl.USER_INFO;
        Map<String,Object> map = new HashMap<>();
        Map<String,String> mHeaderMap = new HashMap<String,String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.USER_INFO, map);
    }



    private void uploadFile(LocalMedia localMedia,String code){

        // mRequestTag = MethodUrl.creUploadFile;
        Map<String,Object> mSignMap = new HashMap<>();
        mSignMap.put("file",localMedia);
        mSignMap.put("code",code);
        String oldPath = localMedia.getPath();

        Map<String, Object> mParamMap = new HashMap<>();

        Map<String, Object> map = new HashMap<>();
        map.put("file",localMedia.getCompressPath());
        //map.put("fileName",mDataMap.get("precreid")+""+System.currentTimeMillis()+""+i);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.postFileToMap(mHeaderMap, MethodUrl.creUploadFile,mSignMap,mParamMap, map);
    }



    private void submitAction() {
        /*if (UtilTools.isEmpty(mNameTv, "户名")) {
            showToastMsg("户名不能为空");
            mButNext.setEnabled(true);
            return;
        }*/
        if (UtilTools.isEmpty(mZhanghuEidt, "账户")) {
            showToastMsg("账户不能为空");
            mButNext.setEnabled(true);
            return;
        }
        if (UtilTools.isEmpty(mKaihuBankValueTv, "开户行")) {
            showToastMsg("开户行不能为空");
            mButNext.setEnabled(true);
            return;
        }
       /* if (UtilTools.isEmpty(mKaihuBankDianTv, "开户网点")) {
            showToastMsg("开户网点不能为空");
            mButNext.setEnabled(true);
            return;
        }*/

        mBankNum = mZhanghuEidt.getText() + "";
        mBankNum = mBankNum.replaceAll(" ", "");

       /* boolean b = RegexUtil.isSiCard(mBankNum);
        if (!b) {
            showToastMsg("请输入合法的银行卡号");
            mButNext.setEnabled(true);
            return;
        }*/
        mName = mNameTv.getText() + "";

        mRequestTag = MethodUrl.companyPay;
        mAddMap = new HashMap<>();
        //mAddMap.put("accname", mName);//户名
        mAddMap.put("accid", mBankNum);//帐号
        mAddMap.put("opnbnkid", mBankMap.get("bankid") + "");//开户行ID
        mAddMap.put("opnbnknm", mBankMap.get("bankname") + "");//开户行名称
       /* // mAddMap.put("crossmark",mBankMap.get("crossmark")+"");//跨行标识（1 本行 2 跨行）
        mAddMap.put("wdcode", mWangDianMap.get("opnbnkwdcd") + "");//开户网点编号
        mAddMap.put("wdname", mWangDianMap.get("opnbnkwdnm") + "");//开户网点名称*/

        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.companyPay, mAddMap);
    }


    @OnClick({R.id.kaihu_bank_lay, R.id.but_next, R.id.back_img, R.id.name_bank_lay, R.id.left_back_lay,R.id.qiye_xuke_lay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.kaihu_bank_lay:
                if (mBankMap == null) {
                    showToastMsg("请先填写银行卡信息");
                } else {
                    intent = new Intent(QiyeCardInfoActivity.this, ChoseBankAddActivity.class);
                    intent.putExtra("bankid", mBankMap.get("bankid") + "");
                    startActivityForResult(intent, 200);
                }
                break;
            case R.id.but_next:

               /* intent = new Intent(QiyeCardInfoActivity.this,QiyeDakuanCheckActivity.class);
                startActivity(intent);*/

                mButNext.setEnabled(false);
                submitAction();
                break;
            case R.id.name_bank_lay:
                intent = new Intent(QiyeCardInfoActivity.this, BankNameListActivity.class);
                startActivityForResult(intent, 300);
                break;
            case R.id.qiye_xuke_lay:
                PermissionsUtils.requsetRunPermission(QiyeCardInfoActivity.this, new RePermissionResultBack() {
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
        }
    }


    private final int mSelectPicCode = 1002;
    private List<LocalMedia> selectList = new ArrayList<>();
    private void selectPic(){
        File path = new File( MbsConstans.UPLOAD_PATH);
        if (!path.exists()) {
            path.mkdirs();
        }
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(QiyeCardInfoActivity.this)
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
                .forResult(mSelectPicCode);//结果回调onActivityResult code
        //注意  这个  mCurrentPosition   是标记后台返回的文件类别类型的索引
    }

    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 30:


                break;
        }
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
        switch (mType) {
            case MethodUrl.USER_INFO://用户信息 //{auth=1, firm_kind=0, head_pic=default, name=刘英超, tel=151****3298, idno=4107****3616, cmpl_info=0}
                MbsConstans.USER_MAP = tData;
                SPUtils.put(QiyeCardInfoActivity.this, MbsConstans.SharedInfoConstans.LOGIN_INFO,   JSONUtil.getInstance().objectToJson(MbsConstans.USER_MAP));
                mNameTv.setText(tData.get("comname")+"");
                //showUpdateDialog();
                break;
            //上传文件成功
            case MethodUrl.creUploadFile:
                dismissProgressDialog();
                String code = tData.get("code")+"";
                if (code.equals("1")){
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
                    GlideUtils.loadImage(QiyeCardInfoActivity.this,path,mQiyeXukeImage);
                }
                break;
            case MethodUrl.companyPay://{custid=null}
                Intent intent = new Intent(QiyeCardInfoActivity.this,QiyeDakuanCheckActivity.class);
                intent.putExtra("remitid",tData.get("remitid")+"");
                startActivity(intent);
                mButNext.setEnabled(true);
                break;

            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.companyPay:
                        submitAction();
                        break;
                    case MethodUrl.USER_INFO://用户信息 //{auth=1, firm_kind=0, head_pic=default, name=刘英超, tel=151****3298, idno=4107****3616, cmpl_info=0}
                        getUserInfoAction();
                        break;
                }
                break;
        }

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        switch (mType) {
            case MethodUrl.creUploadFile:
                dismissProgressDialog();
                break;
            case MethodUrl.companyPay://{custid=null}
                mButNext.setEnabled(true);
                break;
            case MethodUrl.checkBankCard://{"bank_same":"0","bankid":"305","accid":"6226203000645932","bankname":"民生银行","card_type":"1"}
                break;
        }
        dealFailInfo(map, mType);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case mSelectPicCode:
                    // 图片选择结果回调
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
                        // UPLOAD_FILE(localMedia,code);
                        LogUtilDebug.i("打印log日志","@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@开始上传");
                        uploadFile(localMedia,"1");
                    }

                    break;
                case 100:

                    break;
                case 200:
                    bundle = data.getExtras();
                    if (bundle != null) { //{"opnbnkwdnm":"南洋商业银行（中国）有限公司北京分行","opnbnkwdcd":"503100000015"}
                        mWangDianMap = (Map<String, Object>) bundle.getSerializable("DATA");
                        mKaihuBankDianTv.setText(mWangDianMap.get("opnbnkwdnm") + "");
                        mKaihuBankDianTv.setError(null, null);
                    }
                    break;
                case 300:
                    bundle = data.getExtras();
                    if (bundle != null) {
                        mBankMap = (Map<String, Object>) bundle.getSerializable("DATA");
                        mBankMap.put("bankid", mBankMap.get("opnbnkid") + "");
                        mBankMap.put("bankname", mBankMap.get("opnbnknm") + "");
                        mBankMap.put("logopath", mBankMap.get("logopath") + "");
                        mKaihuBankValueTv.setText(mBankMap.get("opnbnknm") + "");
                        mKaihuBankValueTv.setError(null, null);
                    }
                    break;
            }
        }
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
            e.printStackTrace();
            LogUtilDebug.i("PictureFileUtils删除文件","没有申请权限");
        }

    }
}
