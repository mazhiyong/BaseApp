package com.lr.biyou.ui.temporary.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mywidget.dialog.ZhangDialog;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle.activity.SettingActivity;
import com.lr.biyou.utils.imageload.GlideCacheUtil;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MoreSetActivity extends BasicActivity implements RequestView{


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
    @BindView(R.id.login_phone_lay)
    CardView mLoginPhoneLay;
    @BindView(R.id.reset_loginpass_lay)
    CardView mResetLoginpassLay;
    @BindView(R.id.modify_loginpass_lay)
    CardView mModifyLoginPassLay;
    @BindView(R.id.modify_pass_lay)
    CardView mModifyPassLay;
    @BindView(R.id.finger_login_lay)
    CardView mFingerLoginLay;
    @BindView(R.id.shoushi_login_lay)
    CardView mShoushiLoginLay;
    @BindView(R.id.safe_lay)
    CardView mSafeLay;
    @BindView(R.id.dianzi_zhang_lay)
    CardView mDianziZhangLay;
    @BindView(R.id.recevie_msg_lay)
    CardView mRecevieMsgLay;
    @BindView(R.id.clear_cache_lay)
    CardView mClearCacheLay;
    @BindView(R.id.about_us_lay)
    CardView mAboutUsLay;
    @BindView(R.id.my_phone_tv)
    TextView mMyPhoneTv;
    @BindView(R.id.is_install_tv)
    TextView mIsInstallTv;
    @BindView(R.id.app_version_tv)
    TextView mAppVersionTv;
    @BindView(R.id.cash_size_tv)
    TextView mCashSizeTv;
    @BindView(R.id.dianzi_qz_line)
    View mDianziQzLine;
    @BindView(R.id.modify_trade_pass_tv)
    TextView mModifyTradePassTv;


    private String mRequestTag = "";

    private String mZhangUrl = "";

    private Map<String,Object> mInstallMap;


    private Map<String,Object> mTradeStateMap;

    @Override
    public int getContentView() {
        return R.layout.activity_more_set;
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mTitleText.setText(getResources().getString(R.string.set_title));

        if (MbsConstans.USER_MAP != null){
            mMyPhoneTv.setText(MbsConstans.USER_MAP.get("tel")+"");
        }
        mAppVersionTv.setText(MbsConstans.UpdateAppConstans.VERSION_APP_NAME);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.ZHENGSHU_UPDATE);
        registerReceiver(mBroadcastReceiver,intentFilter);

        mCashSizeTv.setText(GlideCacheUtil.getInstance().getCacheSize(this));

        isInstallCer();
        tradePassState();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (mIsRefresh){
            tradePassState();
            mIsRefresh = false;
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MbsConstans.BroadcastReceiverAction.ZHENGSHU_UPDATE)){
                isInstallCer();
            }
        }
    };

    private void getMsgCodeAction() {

        mRequestTag = MethodUrl.resetPassCode;
        Map<String, Object> map = new HashMap<>();
        String  mPhone = SPUtils.get(this, MbsConstans.SharedInfoConstans.LOGIN_ACCOUNT,"")+"";

        if (UtilTools.empty(mPhone)){
            showToastMsg(getResources().getString(R.string.toast_login_again));
            closeAllActivity();
            MbsConstans.USER_MAP = null;
            MbsConstans.REFRESH_TOKEN = "";
            MbsConstans.ACCESS_TOKEN = "";
            SPUtils.put(MoreSetActivity.this, MbsConstans.SharedInfoConstans.LOGIN_OUT, true);
            Intent intent = new Intent(MoreSetActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        map.put("tel", mPhone+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap,MethodUrl.resetPassCode, map);
    }


    @OnClick({R.id.back_img, R.id.login_phone_lay,R.id.reset_loginpass_lay, R.id.modify_pass_lay, R.id.finger_login_lay,R.id.modify_loginpass_lay,
            R.id.shoushi_login_lay, R.id.safe_lay, R.id.recevie_msg_lay, R.id.clear_cache_lay, R.id.about_us_lay,R.id.left_back_lay,
            R.id.dianzi_zhang_lay})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.login_phone_lay:
                intent = new Intent(MoreSetActivity.this,LoginPhoneInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.modify_loginpass_lay:
                intent = new Intent(MoreSetActivity.this,ModifyLoginPassActivity.class);
                startActivity(intent);
                break;
            case R.id.reset_loginpass_lay:
                getMsgCodeAction();
                break;
            case R.id.modify_pass_lay:
                if (mTradeStateMap == null || mTradeStateMap.isEmpty()){
                    tradePassState();
                }else {
                    intent = new Intent(MoreSetActivity.this,ModifyOrderPassActivity.class);
                    String tradeState = mTradeStateMap.get("trd_pwd_state")+"";
                    if (tradeState.equals("0")){//交易密码状态（0：未设置，1：已设置）
                        intent.putExtra("TYPE","1");//1设置新交易密码   2修改交易密码
                    }else {
                        intent.putExtra("TYPE","2");//1设置新交易密码   2修改交易密码
                    }
                    startActivity(intent);
                }

                break;
            case R.id.finger_login_lay:
                intent = new Intent(MoreSetActivity.this,FingermentActivity.class);
                startActivity(intent);
                break;
            case R.id.shoushi_login_lay:
                intent = new Intent(MoreSetActivity.this,PatternActivity.class);
                startActivity(intent);
                break;
            case R.id.safe_lay://安装证书
                String ss = mInstallMap.get("state")+"";
                if (ss.equals("0")){
                    intent = new Intent(MoreSetActivity.this,IdCardSuccessActivity.class);
                    intent.putExtra("verify_type",mInstallMap.get("verify_type")+"");
                    intent.putExtra(MbsConstans.FaceType.FACE_KEY,MbsConstans.FaceType.FACE_INSTALL);
                    startActivity(intent);
                }
                break;
            case R.id.dianzi_zhang_lay:
//                showZhangDialog();
                dzQzImage();
                break;
            case R.id.recevie_msg_lay://开启消息通知服务
                intent=new Intent(MoreSetActivity.this,ServicesRemindActivity.class);
                startActivity(intent);
                break;
            case R.id.clear_cache_lay:
                //clearCache();
                GlideCacheUtil.getInstance().clearImageAllCache(this);
                showToastMsg("清除成功");
                //mCashSizeTv.setText(GlideCacheUtil.getmContext().getCacheSize(this));
                mCashSizeTv.setText("0.0Byte");
                String mPhone = SPUtils.get(this, MbsConstans.SharedInfoConstans.LOGIN_ACCOUNT,"")+"";
                String mConfig = SPUtils.get(this, MbsConstans.SharedInfoConstans.NAME_CODE_DATA,"")+"";
                SPUtils.clear(this);
                SPUtils.put(this, MbsConstans.SharedInfoConstans.LOGIN_ACCOUNT, mPhone);
                SPUtils.put(this, MbsConstans.SharedInfoConstans.NAME_CODE_DATA, mConfig);
                break;
            case R.id.about_us_lay:
                intent=new Intent(MoreSetActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void clearCache() {

        mRequestTag = MethodUrl.clearCache;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.clearCache, map);
    }

    /**
     * 是否设置交易密码
     */
    private void tradePassState() {
        mRequestTag = MethodUrl.isTradePass;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.isTradePass, map);
    }
    /**
     * 是否安装证书信息
     */
    private void isInstallCer() {
        mRequestTag = MethodUrl.isInstallCer;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.isInstallCer, map);
    }

    /**
     * 电子签章信息
     */
    private void dzQzImage() {
        mRequestTag = MethodUrl.qzImage;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.qzImage, map);
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
        switch (mType){
            //交易密码状态
            case MethodUrl.isTradePass:
                mTradeStateMap = tData;
                String tradeState = tData.get("trd_pwd_state")+"";
                if (tradeState.equals("0")){//交易密码状态（0：未设置，1：已设置）
                    mModifyTradePassTv.setText(getResources().getString(R.string.ser_order_pass_title));
                }else {
                    mModifyTradePassTv.setText(getResources().getString(R.string.modify_order_pass_title));
                }

                break;
            case MethodUrl.qzImage:
                mZhangUrl = tData.get("remotepath")+"";
                showZhangDialog();
                break;
            case MethodUrl.resetPassCode:
                showToastMsg("获取验证码成功");
                intent = new Intent(MoreSetActivity.this,CodeMsgActivity.class);
                intent.putExtra("DATA",(Serializable) tData);
                intent.putExtra(MbsConstans.CodeType.CODE_KEY, MbsConstans.CodeType.CODE_RESET_LOGIN_PASS);
                intent.putExtra("showPhone", MbsConstans.USER_MAP.get("tel")+"");
                startActivity(intent);
                break;
            case MethodUrl.isInstallCer: //{verify_type=FACE, state=0}
                mInstallMap = tData;
                String ss = mInstallMap.get("state")+"";
                if (ss.equals("0")){
                    mIsInstallTv.setText(getResources().getString(R.string.no_setip));
                    mDianziZhangLay.setVisibility(View.GONE);
                    mDianziQzLine.setVisibility(View.GONE);
                } else {
                    mIsInstallTv.setText(getResources().getString(R.string.has_setup));
                    mDianziZhangLay.setVisibility(View.VISIBLE);
                    mDianziQzLine.setVisibility(View.VISIBLE);
                }
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                for (String mTag : mRequestTagList){
                    switch (mTag) {
                        case MethodUrl.isTradePass:
                            tradePassState();
                            break;
                        case MethodUrl.qzImage:
                            dzQzImage();
                            break;
                        case MethodUrl.isInstallCer:
                            isInstallCer();
                            break;
                        case MethodUrl.resetPassCode:
                            getMsgCodeAction();
                            break;
                    }
                }
                mRequestTagList = new ArrayList<>();
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {
        dealFailInfo(map,mType);
    }



    private ZhangDialog mZhangDialog;
    private void showZhangDialog(){
        mZhangDialog = new ZhangDialog(this,true);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.cancel:
                        mZhangDialog.dismiss();
                        break;
                    case R.id.confirm:
                        mZhangDialog.dismiss();
                        break;
                    case R.id.tv_right:
                        mZhangDialog.dismiss();
                        break;
                }
            }
        };
        mZhangDialog.setCanceledOnTouchOutside(false);
        mZhangDialog.setCancelable(true);
        mZhangDialog.setOnClickListener(onClickListener);
        mZhangDialog.initValue("电子印章","");
        mZhangDialog.show();





       /* if (UtilTools.empty(loadImgUrl)){
            return;
        }*/
        if (mZhangUrl.contains("http")){
        }else {
            mZhangUrl = MbsConstans.PIC_URL + mZhangUrl;
        }

        //GlideUtils.loadImage(MoreSetActivity.this,"http://pic15.nipic.com/20110628/1369025_192645024000_2.jpg",mZhangDialog.mContentTv);
        GlideUtils.loadImage(MoreSetActivity.this,mZhangUrl,mZhangDialog.mContentTv);
        mZhangDialog.tv_cancel.setEnabled(true);
        mZhangDialog.tv_cancel.setVisibility(View.VISIBLE);
        mZhangDialog.tv_exit.setVisibility(View.GONE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
