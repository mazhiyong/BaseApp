package com.lr.biyou.ui.temporary.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.ui.moudle.activity.MainActivity;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.SPUtils;
import com.jaeger.library.StatusBarUtil;





import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 安装证书   界面
 */
public class IdCardSuccessActivity extends BasicActivity implements RequestView{

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
    @BindView(R.id.my_image)
    ImageView mMyImage;
    @BindView(R.id.but_checkl)
    Button mInstallBut;
    @BindView(R.id.zan_no_anzhuang)
    TextView mNoAnzhuangTv;

    @BindView(R.id.anzhuang_text_tv)
    TextView mAnZhuangTextTv;
    @BindView(R.id.dianzi_zhang_lay)
    LinearLayout mDianziZhangLay;


    private String mVerifyType= "";
    private String mAuthCode = "";
    private int mOpType = 0;

    private String mRequestTag = "";

    @Override
    public int getContentView() {
        return R.layout.activity_idcard_success;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mVerifyType = bundle.getString("verify_type");
            mAuthCode = bundle.getString("authcode");
            mOpType = bundle.getInt(MbsConstans.FaceType.FACE_KEY);
        }
        switch (mOpType){
            case MbsConstans.FaceType.FACE_AUTH://直接一路认证过来的操作
                mTitleText.setText(getResources().getString(R.string.id_card_check));
                mAnZhuangTextTv.setText(getResources().getString(R.string.check_success));
                break;
            case MbsConstans.FaceType.FACE_INSTALL://认证后第一时间并没有安装证书   更多设置里面点击安装证书进来进行的操作
                mTitleText.setText(getResources().getString(R.string.install_cer));
                mAnZhuangTextTv.setText(getResources().getString(R.string.anzuang_card_text));
                break;
        }

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

    /**
     * 安装证书进行的操作
     */
    private void submitInstall(){

        mRequestTag =  MethodUrl.installCerSubmit;
        Map<String, Object> map = new HashMap<>();
        map.put("authcode", mAuthCode);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.installCerSubmit, map);
    }

    @OnClick({R.id.back_img, R.id.but_checkl,R.id.left_back_lay,R.id.zan_no_anzhuang})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.zan_no_anzhuang:
                switch (mOpType){
                    case MbsConstans.FaceType.FACE_AUTH://直接一路认证过来的操作
                        finish();
                        break;
                    case MbsConstans.FaceType.FACE_INSTALL://认证后第一时间并没有安装证书   更多设置里面点击安装证书进来进行的操作
                        finish();
                        break;
                }
                break;
            case R.id.but_checkl:
                mInstallBut.setEnabled(false);
                switch (mOpType){
                    case MbsConstans.FaceType.FACE_AUTH://直接一路认证过来的操作

                        if (MbsConstans.USER_MAP != null){
                            String firmKind = MbsConstans.USER_MAP.get("firm_kind")+"";
                            if (firmKind.equals("0")){
                                //submitInstall();

                                mInstallBut.setEnabled(true);
                                intent = new Intent(IdCardSuccessActivity.this, QiyeCaActivity.class);
                                startActivity(intent);
                            }else {
                                mInstallBut.setEnabled(true);
                                intent = new Intent(IdCardSuccessActivity.this, QiyeCaActivity.class);
                                startActivity(intent);
                            }
                        }else {
                            mInstallBut.setEnabled(true);
                        }

                        break;
                    case MbsConstans.FaceType.FACE_INSTALL://认证后第一时间并没有安装证书   更多设置里面点击安装证书进来进行的操作
                        //if (mVerifyType.equals("FACE")){//如果是人脸识别的话，需要再次验证一下人脸
                        //netWorkWarranty();


                        if (MbsConstans.USER_MAP != null){
                            String firmKind = MbsConstans.USER_MAP.get("firm_kind")+"";
                            if (firmKind.equals("0")){
                               /* PermissionsUtils.requsetRunPermission(IdCardSuccessActivity.this, new RePermissionResultBack() {
                                    @Override
                                    public void requestSuccess() {
                                        netWorkWarranty();
                                    }

                                    @Override
                                    public void requestFailer() {
                                        toast(R.string.failure);
                                    }
                                },Permission.Group.CAMERA,Permission.Group.STORAGE);*/

                                mInstallBut.setEnabled(true);
                                intent = new Intent(IdCardSuccessActivity.this, QiyeCaActivity.class);
                                startActivity(intent);
                            }else {
                                mInstallBut.setEnabled(true);
                                intent = new Intent(IdCardSuccessActivity.this, QiyeCaActivity.class);
                                startActivity(intent);
                            }
                        }else {
                            mInstallBut.setEnabled(true);
                        }


                       /* }else if (mVerifyType.equals("SMS")){//如果是人工验证的话    需要输入身份证号   后续操作短信验证码
                            intent = new Intent(IdCardSuccessActivity.this,IdCardEditActivity.class);
                            intent.putExtra(MbsConstans.CodeType.CODE_KEY, MbsConstans.CodeType.CODE_INSTALL);
                            //startActivity(intent);
                            startActivityForResult(intent,1);
                        }*/
                        break;
                }
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
//                Manager manager = new Manager(IdCardSuccessActivity.this);
//                LivenessLicenseManager licenseManager = new LivenessLicenseManager(IdCardSuccessActivity.this);
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
                    mInstallBut.setEnabled(true);
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
                        mInstallBut.setEnabled(true);
                    }

                    break;
                case MbsConstans.FaceType.FACE_INSTALL:
                    bundle = data.getExtras();
                    if (bundle != null){
                        mAuthCode = bundle.getString("authcode");
                        submitInstall();
                    }else {
                        mInstallBut.setEnabled(true);
                    }
                    break;
                default:
                    mInstallBut.setEnabled(true);
                    break;

            }

        }else if (requestCode == PAGE_INTO_LIVENESS){//人脸识别返回来的数据
            if (resultCode == RESULT_OK ) {
                bundle=data.getExtras();
                bundle.putInt(MbsConstans.FaceType.FACE_KEY, MbsConstans.FaceType.FACE_INSTALL);
                intent = new Intent(IdCardSuccessActivity.this,ApplyAmountActivity.class);
                intent.putExtras(bundle);
                //设置返回数据
                startActivityForResult(intent,1);
            } else {
                mInstallBut.setEnabled(true);
            }
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
        Intent intent ;
        switch (mType){
            case MethodUrl.USER_INFO://用户信息 //{auth=1, firm_kind=0, head_pic=default, name=刘英超, tel=151****3298, idno=4107****3616, cmpl_info=0}
                MbsConstans.USER_MAP = tData;
                SPUtils.put(IdCardSuccessActivity.this, MbsConstans.SharedInfoConstans.LOGIN_INFO,   JSONUtil.getInstance().objectToJson(MbsConstans.USER_MAP));
                //showUpdateDialog();
                break;
            case MethodUrl.installCerSubmit:
                showToastMsg("安装证书成功");
                mInstallBut.setEnabled(true);
                switch (mOpType){
                    case MbsConstans.FaceType.FACE_AUTH://直接一路认证过来的操作
                        intent = new Intent(IdCardSuccessActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case MbsConstans.FaceType.FACE_INSTALL://认证后第一时间并没有安装证书   更多设置里面点击安装证书进来进行的操作
                        intent = new Intent();
                        intent.setAction(MbsConstans.BroadcastReceiverAction.ZHENGSHU_UPDATE);
                        sendBroadcast(intent);
                        finish();
                        break;
                }
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.installCerSubmit:
                        submitInstall();
                        break;
                    case MethodUrl.USER_INFO:
                        getUserInfoAction();
                        break;
                }
                break;
        }

    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {
        mInstallBut.setEnabled(true);
        dealFailInfo(map,mType);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        switch (mOpType){
            case MbsConstans.FaceType.FACE_AUTH://直接一路认证过来的操作
                Intent intent = new Intent(IdCardSuccessActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case MbsConstans.FaceType.FACE_INSTALL://认证后第一时间并没有安装证书   更多设置里面点击安装证书进来进行的操作
                break;
        }
    }


    protected void toast(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
