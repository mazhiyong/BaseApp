package com.lr.biyou.ui.temporary.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.MyClickableSpan;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.ui.moudle.activity.MainActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.jaeger.library.StatusBarUtil;




import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 企业申请数字证书   修改后的
 */
public class QiyeCaActivity extends BasicActivity implements RequestView, ReLoadingData {

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.right_text_tv)
    TextView mRightTextTv;
    @BindView(R.id.right_img)
    ImageView mRightImg;
    @BindView(R.id.right_lay)
    LinearLayout mRightLay;
    @BindView(R.id.divide_line)
    View mDivideLine;
    @BindView(R.id.top_layout)
    LinearLayout mTitleBarView;
    @BindView(R.id.my_image)
    ImageView mMyImage;
    @BindView(R.id.xieyi_checkbox)
    CheckBox mXieyiCheckbox;
    @BindView(R.id.soft_xieyi_tv)
    TextView mSoftXieyiTv;
    @BindView(R.id.sushu_value_tv)
    TextView mSushuValueTv;
    @BindView(R.id.user_name_value_tv)
    TextView mUserNameValueTv;
    @BindView(R.id.phone_value_tv)
    TextView mPhoneValueTv;
    @BindView(R.id.apply_date_value_tv)
    TextView mApplyDateValueTv;
    @BindView(R.id.limit_value_tv)
    TextView mLimitValueTv;
    @BindView(R.id.qiye_qianzhang_image)
    ImageView mQiyeQZImage;
    @BindView(R.id.but_checkl)
    Button mButCheck;

    private Map<String,Object> mConfigMap;
    private String mRequestTag = "";

    private boolean mPressBut = false;
    private String mAuthCode = "";

    @Override
    public int getContentView() {
        return R.layout.activity_qiye_ca;
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        IntentFilter filter = new IntentFilter();
        filter.addAction(MbsConstans.BroadcastReceiverAction.CAPAY_SUC);
        registerReceiver(receiver, filter);
        mTitleText.setText("申请数字证书");
        checkCa();
    }

    private void initXieyiView() {

        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("title", "数字证书申请表");
        map.put("content", "数字证书申请表");
        map.put("url",mConfigMap.get("catabpatn")+"");
        list.add(map);

        map = new HashMap<>();
        map.put("url",mConfigMap.get("caauthpath")+"");
        map.put("title", "数字证书使用授权书");
        map.put("content", "数字证书使用授权书");
        list.add(map);

        String xiyiStr = "已阅读并同意签署";
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> mm = list.get(i);
            final String str = mm.get("content") + "";
            if (i == (list.size() - 1)) {
                xiyiStr = xiyiStr + "《" + str + "》";
            } else {
                xiyiStr = xiyiStr + "《" + str + "》和";
            }
        }
        SpannableString sp = new SpannableString(xiyiStr);

        for (final Map xieyiMap : list) {
            final String str = xieyiMap.get("content") + "";
            setClickableSpan(sp, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(QiyeCaActivity.this, PDFLookActivity.class);
                    intent.putExtra("id", xieyiMap.get("url") + "");
                    startActivity(intent);
                }
            }, xiyiStr, "《" + str + "》");
        }
        mSoftXieyiTv.setText(sp);
        //添加点击事件时，必须设置
        mSoftXieyiTv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initValueView(){
        mSushuValueTv.setText(mConfigMap.get("firmname")+"");
        mUserNameValueTv.setText(mConfigMap.get("username")+"");
        mPhoneValueTv.setText(mConfigMap.get("telephone")+"");
        mApplyDateValueTv.setText(mConfigMap.get("reqdate")+"");
        mLimitValueTv.setText(mConfigMap.get("validdate")+"");
        String payStatus = mConfigMap.get("payState")+"";
        if (payStatus.equals("1")) {//证书费用支付状态（0：未支付 1：已支付）
            mButCheck.setText(getString(R.string.apply));
        }else {
            mButCheck.setText(getString(R.string.but_next));
        }
        loadQZImage();

        initXieyiView();

    }

    /**
     * 加载电子签章信息
     */
    private void loadQZImage(){

       // GlideUtils.loadImage(this,"https://www.baidu.com/img/bd_logo1.png",mQiyeQZImage);

        if (mConfigMap == null || mConfigMap.isEmpty()){
            return;
        }
        String loadImgUrl = mConfigMap.get("remotepath")+"";

        LogUtilDebug.i("打印log日志--------------------------------------------------------",loadImgUrl);


       /* if (UtilTools.empty(loadImgUrl)){
            return;
        }*/
        if (loadImgUrl.contains("http")){
        }else {
            loadImgUrl = MbsConstans.PIC_URL+loadImgUrl;
        }
        final String imgUrl = loadImgUrl;

        GlideUtils.loadImage(this,imgUrl,mQiyeQZImage);
//
    }


    private void caNextAction(){
        String payStatus = mConfigMap.get("payState")+"";
        if (payStatus.equals("1")){//证书费用支付状态（0：未支付 1：已支付）
            //  去申请证书
            PermissionsUtils.requsetRunPermission(QiyeCaActivity.this, new RePermissionResultBack() {
                @Override
                public void requestSuccess() {
                    netWorkWarranty();
                }

                @Override
                public void requestFailer() {
                    toast(R.string.failure);
                }
            }, Permission.Group.CAMERA,Permission.Group.STORAGE);

        }else {//去支付费用
            Intent intent = new Intent(this,QiyeCaPayActivity.class);
            startActivity(intent);
        }

        mButCheck.setEnabled(true);
    }


    /**
     * 检测是否已支付money  匹配来账信息
     */
    private void checkCa() {
        mRequestTag = MethodUrl.checkCa;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.checkCa, map);
    }
    /**
     * 获取证书信息
     */
    private void caInfo() {
        mRequestTag = MethodUrl.caConfig;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.caConfig, map);
    }
    /**
     * 安装证书进行的操作
     */
    private void submitInstall(){

        mRequestTag =  MethodUrl.installCerSubmit;
        Map<String, Object> map = new HashMap<>();
        map.put("authcode", mAuthCode);
        map.put("remotepath", mConfigMap.get("remotepath")+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.installCerSubmit, map);
    }


    private SpannableString setClickableSpan(SpannableString sp, View.OnClickListener l, String str, String span) {
        sp.setSpan(new MyClickableSpan(ContextCompat.getColor(this, R.color.blue1), l), str.indexOf(span), str.indexOf(span) + span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }


    @Override
    public void reLoadingData() {

    }

    /**
     * @descriptoin 请求前加载progress
     * @author dc
     * @date 2017/2/16 11:00
     */
    @Override
    public void showProgress() {
        showProgressDialog();
    }

    /**
     * @descriptoin 请求结束之后隐藏progress
     * @author dc
     * @date 2017/2/16 11:01
     */
    @Override
    public void disimissProgress() {
        dismissProgressDialog();
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
        switch (mType) {
            case MethodUrl.installCerSubmit:
                String msg = tData.get("result")+"";
                showToastMsg(msg);
                intent = new Intent();
                intent.setAction(MbsConstans.BroadcastReceiverAction.ZHENGSHU_UPDATE);
                sendBroadcast(intent);
                backTo(MainActivity.class,true);
                break;
            case MethodUrl.caConfig:
                mButCheck.setEnabled(true);
                mConfigMap = tData;
                initValueView();
                if (mPressBut){
                    caNextAction();
                    mPressBut = false;
                }
                break;
            case MethodUrl.checkCa:
                caInfo();
                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.caConfig:
                        caInfo();
                        break;
                    case MethodUrl.checkCa:
                        checkCa();
                        break;
                }
                break;
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
        mButCheck.setEnabled(true);
        dealFailInfo(map, mType);
    }

    @OnClick({R.id.left_back_lay,R.id.but_checkl,R.id.qiye_qianzhang_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.qiye_qianzhang_image:
                loadQZImage();
                break;
            case R.id.but_checkl:
                if (!mXieyiCheckbox.isChecked()){
                    showToastMsg("请您先阅读并同意签署相关协议");
                    return;
                }
                mPressBut = true;
                mButCheck.setEnabled(false);
                checkCa();
                break;
        }
    }

    /**
     * 联网授权
     */
    private void netWorkWarranty() {

//        final String uuid = ConUtil.getUUIDString(this);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Manager manager = new Manager(QiyeCaShenQActivity.this);
//                LivenessLicenseManager licenseManager = new LivenessLicenseManager(QiyeCaShenQActivity.this);
//                manager.registerLicenseManager(licenseManager);
//                manager.takeLicenseFromNetwork(uuid);
//                if (licenseManager.checkCachedLicense() > 0) {
//                    //授权成功
//                    mHandler.sendEmptyMessage(1);
//                }else {
//                    //授权失败
//                    mHandler.sendEmptyMessage(2);
//                }
//            }
//        }).start();
    }




    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    enterNextPage();
                    break;
                case 2:
                    showToastMsg("人脸验证授权失败");
                    mButCheck.setEnabled(true);
                    break;
            }
        }
    };

    private static final int PAGE_INTO_LIVENESS = 101;
    private void enterNextPage() {
        //startActivityForResult(new Intent(this, LivenessActivity.class), PAGE_INTO_LIVENESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        Intent intent = null;
        Bundle bundle;
        if (requestCode == 1 ) {
            switch (resultCode){//通过短信验证码  安装证书
                case MbsConstans.CodeType.CODE_INSTALL:
                    bundle = data.getExtras();
                    if (bundle != null){
                        mAuthCode = bundle.getString("authcode");
                        submitInstall();
                    }else {
                        mButCheck.setEnabled(true);
                    }

                    break;
                case MbsConstans.FaceType.FACE_INSTALL:
                    bundle = data.getExtras();
                    if (bundle != null){
                        mAuthCode = bundle.getString("authcode");
                        submitInstall();
                    }else {
                        mButCheck.setEnabled(true);
                    }
                    break;
                default:
                    mButCheck.setEnabled(true);
                    break;

            }

        }else if (requestCode == PAGE_INTO_LIVENESS){//人脸识别返回来的数据
            if (resultCode == RESULT_OK ) {
                bundle=data.getExtras();
                bundle.putInt(MbsConstans.FaceType.FACE_KEY, MbsConstans.FaceType.FACE_INSTALL);
                intent = new Intent(QiyeCaActivity.this,ApplyAmountActivity.class);
                intent.putExtras(bundle);
                //设置返回数据
                startActivityForResult(intent,1);
            } else {
                mButCheck.setEnabled(true);
            }
        }
    }

    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    //广播监听
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            if (MbsConstans.BroadcastReceiverAction.CAPAY_SUC.equals(action)) {
                mButCheck.setText(getString(R.string.apply));
            }
        }
    };
}
