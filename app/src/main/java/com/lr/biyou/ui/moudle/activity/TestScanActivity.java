package com.lr.biyou.ui.moudle.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.db.FaPiaoData;
import com.lr.biyou.rongyun.common.IntentExtra;
import com.lr.biyou.ui.moudle2.activity.AddFriendActivity;
import com.lr.biyou.ui.moudle1.activity.HtmlActivity;
import com.lr.biyou.ui.temporary.activity.InvoiceListActivity;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.UtilTools;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class TestScanActivity extends BasicActivity implements QRCodeView.Delegate {

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.light_view)
    ImageView mLightView;
    @BindView(R.id.select_image_tv)
    TextView mSelectImageTv;
    @BindView(R.id.zxingview)
    ZXingView mZXingView;
    @BindView(R.id.top_layout)
    LinearLayout mTopLayout;
    @BindView(R.id.flashLightIv)
    AppCompatImageView mFlashLightIv;
    @BindView(R.id.flashLightTv)
    TextView mFlashLightTv;
    @BindView(R.id.flashLightLayout)
    LinearLayoutCompat mFlashLightLayout;
    @BindView(R.id.albumIv)
    AppCompatImageView mAlbumIv;
    @BindView(R.id.albumLayout)
    LinearLayoutCompat mAlbumLayout;


    private boolean mIsLight = false;

    private static final String TAG = TestScanActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;

    private String mType = "";

    private String groupId= "";
    private String Id= "";

    @Override
    public int getContentView() {
        return R.layout.activity_test_scan;
    }

    @Override
    public void init() {
        /**
         * 设置是否仅仅跟踪左侧边缘的滑动返回
         * 因为银行卡里面有嵌套 viewpager  所以要边界返回
         */
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        //StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        //StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, R.color.transparent), 225);
        StatusBarUtil.setTranslucentForImageView(this, MbsConstans.ALPHA, null);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mType = bundle.getString("type");
        }

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) this.getResources().getDimension(R.dimen.title_item_height) + UtilTools.getStatusHeight2(this));
        mTopLayout.setLayoutParams(layoutParams);
        mTopLayout.setPadding(0, UtilTools.getStatusHeight2(this), 0, 0);

        mZXingView.setDelegate(this);
        mTitleText.setText("二维码");
        mTitleText.setTextColor(ContextCompat.getColor(this, R.color.white));



    }


    @Override
    protected void onStart() {
        super.onStart();
        //mZXingView.getScanBoxView().setOnlyDecodeScanBoxArea(true); // 仅识别扫描框中的码

        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别

        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别

    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mZXingView.startSpot(); // 开始识别
        StatusBarUtil.setDarkMode(this);
    }


    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "!!!!!!!!!!!!!!!!!!!!!!!result:" + result);
        //setTitle("扫描结果为：" + result);

        vibrate();
        if (UtilTools.empty(result)) {
            showToastMsg("未发现二维码");
            return;
        }
        mZXingView.stopSpot();  //
        mZXingView.closeFlashlight(); // 打开闪光灯
        mIsLight = false;
        mFlashLightIv.setImageResource(R.drawable.ic_close);
        mFlashLightTv.setText("打开闪光灯");
        Intent intent ;
        switch (mType){
            case "0": //首页的扫一扫
                Log.i("show","result:"+result);
                if (result.contains("u=") && !result.contains("g=")){ //扫一扫加好友
                    intent = new Intent(this, AddFriendActivity.class);
                    intent.putExtra(IntentExtra.STR_TARGET_ID, result.substring(result.indexOf("u=")+2));
                    startActivity(intent);
                }

                if(result.contains("g=")){ //扫码入群
                    groupId = result.substring(result.indexOf("g=")+2,result.indexOf("&u="));
                    //String rc_id = result.substring(result.indexOf("&u=")+3); //生成二维码的用户的id
                    intent = new Intent(this, AddFriendActivity.class);
                    intent.putExtra(IntentExtra.STR_GROUP_ID, groupId);
                    startActivity(intent);

                }

                break;
            case "1":
                intent = new Intent(this, HtmlActivity.class);
                result = result +"&os=android";
                intent.putExtra("id", result);
                startActivity(intent);
                break;
            case "2":  //扫描发票
                //将发票信息录入数据库
                String fp_code = "100120123";
                String fp_number = "100120111301";
                String fp_money = "100.00";
                String fp_date = "2019-04-19";

                 if (FaPiaoData.getInstance().dataExist(fp_code,fp_number)){
                     Toast.makeText(TestScanActivity.this,"当前发票信息已存在,请换一张试试",Toast.LENGTH_LONG).show();
                 }else {
                     FaPiaoData.getInstance().insertDB(fp_code,fp_number,fp_money,fp_date);
                 }
                intent = new Intent(this, InvoiceListActivity.class);
                startActivity(intent);

                break;
            case "3": //注册
                //Uri uri = Uri.parse(result);
                if (result.contains("i=")){
                    String i = result.substring(result.indexOf("i=")+2);
                    intent = new Intent(TestScanActivity.this, RegistActivity.class);
                    intent.putExtra("result",i);
                    startActivity(intent);
                }else {
                    intent = new Intent(this, HtmlActivity.class);
                    result = result +"&os=android";
                    intent.putExtra("id", result);
                    startActivity(intent);
                }
                break;
            case "4": //提币地址 扫一扫
                intent = new Intent();
                intent.putExtra("result",result);
                setResult(RESULT_OK,intent);
                 break;
            default:
                intent = new Intent(this, HtmlActivity.class);
                result = result +"&os=android";
                intent.putExtra("id", result);
                startActivity(intent);
                break;
        }
        finish();
    }




    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZXingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
            mLightView.setVisibility(View.VISIBLE);
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZXingView.getScanBoxView().setTipText(tipText);
            }
            if (mIsLight) {
                mLightView.setVisibility(View.VISIBLE);
            } else {
                mLightView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }


    @OnClick({R.id.back_img, R.id.left_back_lay, R.id.select_image_tv, R.id.light_view,R.id.flashLightLayout, R.id.albumLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.select_image_tv:
                PermissionsUtils.requsetRunPermission(TestScanActivity.this, new RePermissionResultBack() {
                    @Override
                    public void requestSuccess() {
                        selectPic();
                    }

                    @Override
                    public void requestFailer() {
                        toast(R.string.failure);
                    }
                },Permission.Group.STORAGE);


                //mZXingView.getScanBoxView().setOnlyDecodeScanBoxArea(true); // 仅识别扫描框中的码
                break;
            case R.id.light_view:
                if (mIsLight) {
                    mZXingView.closeFlashlight(); // 打开闪光灯
                    mIsLight = false;
                    mLightView.setImageResource(R.drawable.ic_close);
                } else {
                    mZXingView.openFlashlight(); // 打开闪光灯
                    mIsLight = true;
                    mLightView.setImageResource(R.drawable.ic_open);
                }
                break;
            case R.id.flashLightLayout:
                if (mIsLight) {
                    mZXingView.closeFlashlight(); // 打开闪光灯
                    mIsLight = false;
                    mFlashLightIv.setImageResource(R.drawable.ic_close);
                    mFlashLightTv.setText("打开闪光灯");
                } else {
                    mZXingView.openFlashlight(); // 打开闪光灯
                    mIsLight = true;
                    mFlashLightIv.setImageResource(R.drawable.ic_open);
                    mFlashLightTv.setText("关闭闪光灯");
                }
                break;
            case R.id.albumLayout:
                selectPic();
                break;
        }
    }

    private final int mSelectPicCode = 1002;
    private List<LocalMedia> selectList = new ArrayList<>();

    private void selectPic() {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(TestScanActivity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                // .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(0)// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(false)// 是否可预览图片 true or false
                .previewVideo(false)// 是否可预览视频 true or false
                .enablePreviewAudio(true) // 是否可播放音频 true or false
                .isCamera(false)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .glideOverride(160, 160)
                //.sizeMultiplier(0.4f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(false)// 是否压缩 true or false
                //.glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                // .withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                //  .hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .compressSavePath(MbsConstans.UPLOAD_PATH)//压缩图片保存地址
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


    /*    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_preview:
                mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
                break;
            case R.id.stop_preview:
                mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
                break;
            case R.id.start_spot:
                mZXingView.startSpot(); // 开始识别
                break;
            case R.id.stop_spot:
                mZXingView.stopSpot(); // 停止识别
                break;
            case R.id.start_spot_showrect:
                mZXingView.startSpotAndShowRect(); // 显示扫描框，并且开始识别
                break;
            case R.id.stop_spot_hiddenrect:
                mZXingView.stopSpotAndHiddenRect(); // 停止识别，并且隐藏扫描框
                break;
            case R.id.show_scan_rect:
                mZXingView.showScanRect(); // 显示扫描框
                break;
            case R.id.hidden_scan_rect:
                mZXingView.hiddenScanRect(); // 隐藏扫描框
                break;
            case R.id.decode_scan_box_area:
                mZXingView.getScanBoxView().setOnlyDecodeScanBoxArea(true); // 仅识别扫描框中的码
                break;
            case R.id.decode_full_screen_area:
                mZXingView.getScanBoxView().setOnlyDecodeScanBoxArea(false); // 识别整个屏幕中的码
                break;
            case R.id.open_flashlight:
                mZXingView.openFlashlight(); // 打开闪光灯
                break;
            case R.id.close_flashlight:
                mZXingView.closeFlashlight(); // 关闭闪光灯
                break;
            case R.id.scan_one_dimension:
                mZXingView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
                mZXingView.setType(BarcodeType.ONE_DIMENSION, null); // 只识别一维条码
                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                break;
            case R.id.scan_two_dimension:
                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
                mZXingView.setType(BarcodeType.TWO_DIMENSION, null); // 只识别二维条码
                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                break;
            case R.id.scan_qr_code:
                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
                mZXingView.setType(BarcodeType.ONLY_QR_CODE, null); // 只识别 QR_CODE
                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                break;
            case R.id.scan_code128:
                mZXingView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
                mZXingView.setType(BarcodeType.ONLY_CODE_128, null); // 只识别 CODE_128
                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                break;
            case R.id.scan_ean13:
                mZXingView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
                mZXingView.setType(BarcodeType.ONLY_EAN_13, null); // 只识别 EAN_13
                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                break;
            case R.id.scan_high_frequency:
                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
                mZXingView.setType(BarcodeType.HIGH_FREQUENCY, null); // 只识别高频率格式，包括 QR_CODE、UPC_A、EAN_13、CODE_128
                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                break;
            case R.id.scan_all:
                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
                mZXingView.setType(BarcodeType.ALL, null); // 识别所有类型的码
                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                break;
            case R.id.scan_custom:
                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式

                Map<DecodeHintType, Object> hintMap = new EnumMap<>(DecodeHintType.class);
                List<BarcodeFormat> formatList = new ArrayList<>();
                formatList.add(BarcodeFormat.QR_CODE);
                formatList.add(BarcodeFormat.UPC_A);
                formatList.add(BarcodeFormat.EAN_13);
                formatList.add(BarcodeFormat.CODE_128);
                hintMap.put(DecodeHintType.POSSIBLE_FORMATS, formatList); // 可能的编码格式
                hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE); // 花更多的时间用于寻找图上的编码，优化准确性，但不优化速度
                hintMap.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 编码字符集
                mZXingView.setType(BarcodeType.CUSTOM, hintMap); // 自定义识别的类型

                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
                break;
            case R.id.choose_qrcde_from_gallery:
                *//*
                从相册选取二维码图片，这里为了方便演示，使用的是
                https://github.com/bingoogolapple/BGAPhotoPicker-Android
                这个库来从图库中选择二维码图片，这个库不是必须的，你也可以通过自己的方式从图库中选择图片
                 *//*
                Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                        .cameraFileDir(null)
                        .maxChooseCount(1)
                        .selectedPhotos(null)
                        .pauseOnScroll(false)
                        .build();
                startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
                break;
        }
    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别

        if (resultCode == Activity.RESULT_OK && requestCode == mSelectPicCode) {
            // 图片选择结果回调
            selectList = PictureSelector.obtainMultipleResult(data);
            LogUtilDebug.i("已选择的图片", selectList + "");
            // 例如 LocalMedia 里面返回三种path
            // 1.media.getPath(); 为原图path
            // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
            // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
            // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
            LocalMedia localMedia = selectList.get(0);
            mZXingView.decodeQRCode(localMedia.getPath());
        }
    }



    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * @descriptoin 请求前加载progress
     * @author dc
     * @date 2017/2/16 11:00
     */
    @Override
    public void showProgress() {

    }

    /**
     * @descriptoin 请求结束之后隐藏progress
     * @author dc
     * @date 2017/2/16 11:01
     */
    @Override
    public void disimissProgress() {

    }

    /**
     * @param tData 数据类型
     * @param mType
     * @descriptoin 请求数据成功
     * @author dc
     * @date 2017/2/16 11:01
     */
    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        Intent intent;
        switch (mType){
            case MethodUrl.CHAT_QUERY_ID:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            Id = tData.get("data") + "";
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(TestScanActivity.this, com.lr.biyou.ui.moudle.activity.LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
            case MethodUrl.CHAT_GROUP_ADD:
                switch (tData.get("code")+"") {
                    case "0":
                        showToastMsg(tData.get("msg") + "");
                        break;
                    case "1":
                        closeAllActivity();
                        intent = new Intent(TestScanActivity.this, com.lr.biyou.ui.moudle.activity.LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
                        break;

                }
        }
    }

    /**
     * @param map
     * @param mType
     * @descriptoin 请求数据错误
     * @date 2017/2/16 11:01
     */
    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map,mType);
    }
}