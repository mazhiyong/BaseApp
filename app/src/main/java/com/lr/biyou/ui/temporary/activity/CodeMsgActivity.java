package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mywidget.dialog.ImageCodeDialog;
import com.lr.biyou.ui.moudle.activity.MainActivity;
import com.lr.biyou.ui.moudle.activity.ResetLoginPassButActivity;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 */
public class CodeMsgActivity extends BasicActivity implements RequestView{

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
    @BindView(R.id.but_next)
    Button mButNext;
    @BindView(R.id.msg_code_edit)
    EditText mMsgCodeEdit;
    @BindView(R.id.msg_code_tv)
    TextView mGetCodeTv;
    @BindView(R.id.msg_code_tip)
    TextView mGetCodeTip;

    private Map<String,Object> mData = new HashMap<>();
    private int mOpType = 0;

    private String mPhone = "";
    private TimeCount mTimeCount;

    private String mCodeUrl="";
    private String mCodeCheck="";

    private String mNewPhone = "";
    private String mNewAuthCode = "";

    private String mShowPhone = "";

    private String mRequestTag = "";

    private String mAuthCode = "";

    private String mIdNum = "";

    private String mKind ="";
    private String mInvcode = "";



    //绑定银行卡信息
    private String mAccid = "";//卡号
    private String mMobno = "";//银行预留手机号
    private String mOpnbnkid = "";//开户银行编号
    private String mOpnbnknm = "";//开户银行名称
    private String mOpnbnkwdcd = "";//开户网点编号
    private String mOpnbnkwdnm = "";//开户网点名称
    private String mWdprovcd = "";//开户网点地址-省份-编号
    private String mWdprovnm = "";//开户网点地址-省份-名称
    private String mWdcitycd = "";//开户网点地址-城市-编号
    private String mWdcitynm = "";//开户网点地址-城市-名称
    private String mBackType = "";//返回到哪个activity


    //充值金钱信息
    private String mCAccid = "";
    private String mCMoney = "";

    @Override
    public int getContentView() {
        return R.layout.activity_code_msg;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        /*if (MbsConstans.USER_MAP != null){
            mPhone = MbsConstans.USER_MAP.get("tel")+"";
        }else {*/
        mPhone = SPUtils.get(this, MbsConstans.SharedInfoConstans.LOGIN_ACCOUNT,"")+"";
//        }
        mTimeCount = new TimeCount(1 * 60 * 1000, 1000);//构造CountDownTimer对象

        mTitleText.setText(getResources().getString(R.string.msg_code_title));

        mMsgCodeEdit.requestFocus();

        Intent intent  = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mData = (Map<String, Object>) bundle.getSerializable("DATA");
            mOpType = bundle.getInt(MbsConstans.CodeType.CODE_KEY);
            if (bundle.containsKey("phonenum")){
                mPhone = bundle.getString("phonenum")+"";
            }
            mShowPhone = bundle.getString("showPhone")+"";
            mKind = bundle.getString("kind")+"";
            mInvcode = bundle.getString("invcode")+"";
        }

        switch (mOpType){
            case MbsConstans.CodeType.CODE_PHONE_CHANGE://更换手机号 老的手机号
                mCodeUrl = MethodUrl.changePhoneMsgCode;
                mCodeCheck = MethodUrl.checkCode;
                break;
            case MbsConstans.CodeType.CODE_NEW_PHONE_CHANGE://更换手机号 新的手机号
                mCodeUrl = MethodUrl.changePhoneMsgCode;
                mCodeCheck = MethodUrl.checkCode;
                if (bundle != null){
                    mNewPhone = bundle.getString("phone");
                    mNewAuthCode = bundle.getString("authcode");
                }
                mPhone = mNewPhone;
                break;
            case MbsConstans.CodeType.CODE_RESET_LOGIN_PASS://重置登录密码
                mCodeUrl = MethodUrl.resetPassCode;
                mCodeCheck = MethodUrl.checkCode;
                break;
            case MbsConstans.CodeType.CODE_INSTALL://通过短信验证码  安装安全证书
                if (bundle != null){
                    mIdNum = bundle.getString("idno");
                }
                mCodeUrl = MethodUrl.installCode;
                mCodeCheck = MethodUrl.checkCode;
                mPhone = mIdNum;
                break;
            case MbsConstans.CodeType.CODE_WANGYIN://开通网银
                mCodeUrl = MethodUrl.normalSms;
                mCodeCheck = MethodUrl.checkCode;
                break;
            case MbsConstans.CodeType.CODE_MODIFY_ORDER_PASS://修改交易密码
                break;
            case MbsConstans.CodeType.CODE_PHONE_REGIST: //注册
                mCodeUrl = MethodUrl.REGIST_SMSCODE;
                mCodeCheck = MethodUrl.checkCode;
                mPhone = mShowPhone;
                break;
            case MbsConstans.CodeType.CODE_CARD_CHONGZHI://绑定充值卡
                mCodeUrl = MethodUrl.bankCardSms;
                mCodeCheck = MethodUrl.checkBankSms;
                if (bundle != null){
                    mAccid = bundle.getString("accid");//卡号
                    mMobno = bundle.getString("mobno");//银行预留手机号
                    mOpnbnkid = bundle.getString("opnbnkid");//开户银行编号
                    mOpnbnknm = bundle.getString("opnbnknm");//开户银行名称

                    mOpnbnkwdcd = bundle.getString("opnbnkwdcd");//开户网点编号
                    mOpnbnkwdnm = bundle.getString("opnbnkwdnm");//开户网点名称
                    mWdprovcd = bundle.getString("wdprovcd");//开户网点地址-省份-编号
                    mWdprovnm = bundle.getString("wdprovnm");//开户网点地址-省份-名称
                    mWdcitycd = bundle.getString("wdcitycd");//开户网点地址-城市-编号
                    mWdcitynm = bundle.getString("wdcitynm");//开户网点地址-城市-名称
                    mBackType = bundle.getString("backtype");//返回到哪个activity
                    mShowPhone = mMobno;
                }
                break;
            case MbsConstans.CodeType.CODE_CHONGZHI_MONEY:  //充值金额  获取短信验证码
                mCodeUrl = MethodUrl.chongzhiSubmit;
                mCodeCheck = MethodUrl.chongzhiMoneyCodeCheck;//
                if (bundle != null){
                    mCAccid = bundle.getString("accid");//卡号
                    mCMoney = bundle.getString("amount");//金额
                    mOpnbnknm = bundle.getString("bankName");//银行名称
                    mPhone = bundle.getString("phone");//银行预留手机号码
                    mShowPhone = mPhone;
                }
                break;
            case MbsConstans.CodeType.CODE_TRADE_PASS:  //忘记交易密码获取短信验证码
                mCodeUrl = MethodUrl.forgetTradePass;
                mCodeCheck = MethodUrl.checkCode;
                break;
        }

        mGetCodeTip.setText(mGetCodeTip.getText()+" "+UtilTools.getPhoneXing(mPhone));

        mTimeCount.start();
        // getMsgCodeAction();
    }


    private void getMsgCodeAction() {
        mRequestTag = mCodeUrl;
        Map<String,Object> map = new HashMap<>();
        switch (mOpType){
            case MbsConstans.CodeType.CODE_PHONE_CHANGE://更换手机号 老的手机号
                break;
            case MbsConstans.CodeType.CODE_NEW_PHONE_CHANGE://更换手机号 新的手机号
                map.put("tel", mNewPhone);
                map.put("authcode", mNewAuthCode);
                break;
            case MbsConstans.CodeType.CODE_RESET_LOGIN_PASS://重置登录密码
                map.put("tel", mPhone);
                break;
            case MbsConstans.CodeType.CODE_INSTALL://安装证书  需要的短信验证码
                map.put("idno", mIdNum);
                break;
            case MbsConstans.CodeType.CODE_WANGYIN://开通网银
                map.put("busi", "COMMON");
                break;
            case MbsConstans.CodeType.CODE_MODIFY_ORDER_PASS://修改交易密码
                break;
            case MbsConstans.CodeType.CODE_PHONE_REGIST: //注册
                map.put("tel", mPhone);
                map.put("authcode", mAuthCode);
                break;
            case MbsConstans.CodeType.CODE_CARD_CHONGZHI://绑定充值卡获取短信验证码
                map.put("accid", mAccid);
                map.put("mobno", mMobno);
                map.put("opnbnkid", mOpnbnkid);
                map.put("opnbnknm", mOpnbnknm);

                map.put("opnbnkwdcd",mOpnbnkwdcd);//开户网点编号
                map.put("opnbnkwdnm",mOpnbnkwdnm);//开户网点名称
                map.put("wdprovcd",mWdprovcd );//开户网点地址-省份-编号
                map.put("wdprovnm",mWdprovnm);//开户网点地址-省份-名称
                map.put("wdcitycd",mWdcitycd);//开户网点地址-城市-编号
                map.put("wdcitynm",mWdcitynm );//开户网点地址-城市-名称
                break;
            case MbsConstans.CodeType.CODE_CHONGZHI_MONEY://充值钱操作  获取短信验证码
                map.put("amount", mCMoney);
                map.put("accid", mCAccid);
                break;
            case MbsConstans.CodeType.CODE_TRADE_PASS://忘记交易密码   获取短信验证码
                map.put("tel", mPhone);
                break;
        }

        Map<String, String> mHeaderMap = new HashMap<String, String>();
        //mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.resetPassCode, map);
        mRequestPresenterImp.requestPostToMap(mHeaderMap,mCodeUrl, map);
    }

    private void checkMsgCode() {

        mRequestTag = mCodeCheck;
        String msgCode = mMsgCodeEdit.getText()+"";

        if (UtilTools.empty(msgCode)){
            mButNext.setEnabled(true);
            showToastMsg("请输入验证码");
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("smstoken", mData.get("smstoken")+"");
        map.put("smscode",msgCode );
        map.put("busi", "COMMON");
        switch (mOpType){
            case MbsConstans.CodeType.CODE_PHONE_CHANGE://更换手机号 老的手机号
                map.put("tel", mPhone);
                map.put("busi", "MODTEL_OLD");
                break;
            case MbsConstans.CodeType.CODE_NEW_PHONE_CHANGE://更换手机号 新的手机号
                map.put("tel", mNewPhone);
                map.put("busi", "MODTEL_NEW");
                break;
            case MbsConstans.CodeType.CODE_RESET_LOGIN_PASS://重置登录密码
                map.put("tel", mPhone);
                map.put("busi", "MODPWD");
                break;
            case MbsConstans.CodeType.CODE_INSTALL:// 安装安全证书
                map.put("tel", mPhone);
                map.put("busi", "CA_APPLY");
                break;
            case MbsConstans.CodeType.CODE_WANGYIN://网银短信验证
                map.put("tel", mPhone);
                break;
            case MbsConstans.CodeType.CODE_MODIFY_ORDER_PASS://修改交易密码
                break;
            case MbsConstans.CodeType.CODE_PHONE_REGIST://注册
                map.put("busi", "REGISTER");
                map.put("tel", mPhone);
                break;
            case MbsConstans.CodeType.CODE_CARD_CHONGZHI://绑定充值卡
                map.clear();
                map.put("bindNo", mData.get("bindNo")+"");
                map.put("smsCode", msgCode);
                map.put("accid", mAccid);
                break;

            case MbsConstans.CodeType.CODE_CHONGZHI_MONEY://充值钱操作
                map.clear();
                map.put("serialno", mData.get("serialno")+"");
                map.put("smscode", msgCode);
                break;
            case MbsConstans.CodeType.CODE_TRADE_PASS://忘记交易密码 验证短信验证码
                map.put("tel", mPhone);
                map.put("busi", "FORGET_TRDPWD");
                break;

        }
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        //mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.checkCode, map);
        mRequestPresenterImp.requestPostToMap(mHeaderMap, mCodeCheck, map);
    }



    private void submitAction() {

        mRequestTag =  MethodUrl.changePhoneSubmit;
        Map<String, String> map = new HashMap<>();
        map.put("authcode", mAuthCode);
        map.put("tel_new", mNewPhone);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPutToRes(mHeaderMap, MethodUrl.changePhoneSubmit, map);
    }


    @OnClick({R.id.back_img, R.id.but_next, R.id.msg_code_tv,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.msg_code_tv:
                if (mOpType == MbsConstans.CodeType.CODE_PHONE_REGIST){
                    getImageCode();
                }else {
                    mTimeCount.start();
                    getMsgCodeAction();
                }
                break;
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.but_next:
                mButNext.setEnabled(false);
                checkMsgCode();
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
        mButNext.setEnabled(true);
        Intent intent ;
        switch (mType){
            case MethodUrl.forgetTradePass:
                mData = tData;
                mTimeCount.start();
                showToastMsg("获取验证码成功");
                break;
            case MethodUrl.imageCode:

                // 获取图片验证码
                if (tData.containsKey("img")){//获取图片验证码
                    mBitmap =(Bitmap) tData.get("img");

                    if (mImageCodeDialog == null){
                        showZhangDialog();
                    }else {
                        mImageCodeDialog.show();
                        mImageCodeDialog.mContentTv.setImageBitmap(mBitmap);
                    }

                }else {
                    mAuthCode=  tData.get("authcode")+""; //验证图片验证码
                    getMsgCodeAction();
                }
                break;
            case MethodUrl.normalSms:
                mData = tData;
                mTimeCount.start();
                showToastMsg("获取验证码成功");
                break;
            //重置密码获取短信验证码
            case MethodUrl.resetPassCode://{smstoken=sms_token@d72f4338e1d05c746b36316e6a67f552, send_tel=151****3298}
                mData = tData;
                mTimeCount.start();
                showToastMsg("获取验证码成功");
                break;
            //更改手机号获取短信验证码
            case MethodUrl.changePhoneMsgCode://{smstoken=sms_token@d72f4338e1d05c746b36316e6a67f552, send_tel=151****3298}
                mData = tData;
                mTimeCount.start();
                showToastMsg("获取验证码成功");
                break;
            //注册
            case MethodUrl.REGIST_SMSCODE:
                mData = tData;
                mTimeCount.start();
                showToastMsg("获取验证码成功");
                break;
            //绑卡短信验证码
            case MethodUrl.bankCardSms:
                mData = tData;
                showToastMsg("获取短信验证码成功");
                break;
            case MethodUrl.checkCode://{"authcode":"auth_code@cbfb271f02bc96baf91ae104a741320a"}

                UtilTools.hideSoftInput(CodeMsgActivity.this,mMsgCodeEdit);
                switch (mOpType){
                    case MbsConstans.CodeType.CODE_PHONE_CHANGE://更换手机号 老的手机号
                        intent = new Intent(CodeMsgActivity.this,ChangePhoneActivity.class);
                        intent.putExtra("authcode",tData.get("authcode")+"");
                        startActivity(intent);
                        finish();
                        break;
                    case MbsConstans.CodeType.CODE_NEW_PHONE_CHANGE://更换手机号 新的手机号
                        mAuthCode = tData.get("authcode")+"";
                        // submitAction(tData.get("authcode")+"");
                        submitAction();
                        break;
                    case MbsConstans.CodeType.CODE_RESET_LOGIN_PASS://重置登录密码
                        intent = new Intent(CodeMsgActivity.this,ResetLoginPassButActivity.class);
                        intent.putExtra("phone",mPhone);
                        intent.putExtra("authcode",tData.get("authcode")+"");
                        intent.putExtra("type","0");
                        startActivity(intent);
                        finish();
                        break;
                    case MbsConstans.CodeType.CODE_INSTALL:// 安装安全证书
                        intent = new Intent();
                        intent.putExtra("authcode",tData.get("authcode")+"");
                        setResult(MbsConstans.CodeType.CODE_INSTALL,intent);
                        finish();
                        break;
                    case MbsConstans.CodeType.CODE_WANGYIN://开通网银
                        intent = new Intent();
                        intent.putExtra("authcode",tData.get("authcode")+"");
                        setResult(MbsConstans.CodeType.CODE_WANGYIN,intent);
                        finish();
                        break;
                    case MbsConstans.CodeType.CODE_MODIFY_ORDER_PASS://修改交易密码
                        break;
                    case MbsConstans.CodeType.CODE_PHONE_REGIST://注册
                        mTimeCount.start();
                        intent = new Intent(CodeMsgActivity.this, ResetLoginPassButActivity.class);
                        intent.putExtra("phone",mPhone);
                        intent.putExtra("authcode",tData.get("authcode")+"");
                        intent.putExtra("type","1");
                        intent.putExtra("kind",mKind);
                        intent.putExtra("invcode",mInvcode);
                        startActivity(intent);
                        finish();
                        break;
                    case MbsConstans.CodeType.CODE_TRADE_PASS:
                        mTimeCount.start();
                        intent = new Intent(CodeMsgActivity.this,ModifyOrderPassActivity.class);
                        intent.putExtra("TYPE","3");
                        intent.putExtra("BACK_TYPE","3");
                        intent.putExtra("authcode",tData.get("authcode")+"");
                        startActivity(intent);
                        finish();
                        break;


                }
                break;
            case MethodUrl.changePhoneSubmit:
                showToastMsg("修改手机号成功");
                intent = new Intent(CodeMsgActivity.this,SubmitResultActivity.class);
                intent.putExtra(MbsConstans.ResultType.RESULT_KEY,MbsConstans.ResultType.RESULT_PHONE_CHANGE);
                startActivity(intent);
                finish();
                break;
            case MethodUrl.checkBankSms://绑充值卡卡验证短信验证码
                showToastMsg("绑卡成功");
                sendBrodcast2();
                if (mBackType.equals("10")){
                    backTo(BankCardActivity.class,false);
                }else if (mBackType.equals("20")){
                    backTo(TiXianActivity.class,true);
                }else if (mBackType.equals("30")){
                    backTo(ChongzhiTestActivity.class,true);
                }else if (mBackType.equals("40")){
                    //backTo(PayMoneyChoseWayActivity.class,true);
                    finish();
                }else if (mBackType.equals("110")){//个人账户 提现界面添加银行卡  跳转
                    backTo(TiXianActivity.class,true);
                    finish();
                }else if (mBackType.equals("100")){//个人账户 首页进来申请额度  检测没有提现卡的时候
                    backTo(MainActivity.class,true);
                }else {
                    finish();
                }
                break;
            case MethodUrl.chongzhiSubmit:
                mData = tData;
                showToastMsg("获取短信验证码成功");
                break;
            case MethodUrl.chongzhiMoneyCodeCheck://充值钱验证短信验证码
                intent = new Intent(CodeMsgActivity.this,FuKuanFinishActivity.class);
                intent.putExtra("type","1");
                intent.putExtra("money",mCMoney);
                intent.putExtra("accid",mCAccid);
                intent.putExtra("bankName",mOpnbnknm);
                startActivity(intent);
                sendBrodcast();
                finish();
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.forgetTradePass:
                        getMsgCodeAction();
                        break;
                    case MethodUrl.checkBankSms:
                        checkMsgCode();
                        break;
                    case MethodUrl.resetPassCode:
                        getMsgCodeAction();
                        break;
                    case MethodUrl.changePhoneMsgCode:
                        getMsgCodeAction();
                        break;
                    case MethodUrl.checkCode:
                        checkMsgCode();
                        break;
                    case MethodUrl.changePhoneSubmit:
                        submitAction();
                        break;
                    case MethodUrl.chongzhiMoneyCodeCheck:
                        checkMsgCode();
                        break;

                }
                break;

        }
    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {
        mButNext.setEnabled(true);
        String msg = map.get("errmsg")+"";
        String errcodeStr = map.get("errcode")+"";
        int errorCode = -1;
        try {
            errorCode = Double.valueOf(errcodeStr).intValue();
        }catch (Exception e){
            e.printStackTrace();
            LogUtilDebug.i("打印log日志","这里出现异常了"+e.getMessage());
        }

        switch (mType){

            //重置密码获取短信验证码
            case MethodUrl.resetPassCode://{smstoken=sms_token@d72f4338e1d05c746b36316e6a67f552, send_tel=151****3298}
                // finish();
                break;
            //更改手机号获取短信验证码
            case MethodUrl.changePhoneMsgCode://{smstoken=sms_token@d72f4338e1d05c746b36316e6a67f552, send_tel=151****3298}
                //finish();
                break;
            case MethodUrl.checkCode://{"authcode":"auth_code@cbfb271f02bc96baf91ae104a741320a"}
                break;
            case MethodUrl.changePhoneSubmit:
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                break;
            case MethodUrl.checkBankSms://绑定充值卡  验证短信验证码操作
                /*if (errorCode == ErrorHandler.REFRESH_TOKEN_DATE_CODE){
                }else if (errorCode == ErrorHandler.ACCESS_TOKEN_DATE_CODE){
                }else if(errorCode == ErrorHandler.PHONE_NO_ACTIVE){
                } else {

                    showToastMsg(msg);
                    finish();
                    return;
                }*/
                break;
        }

        //1006 access_token过期需要重新登录      1007 refresh_token  过期需要重新获取refresh_token
        dealFailInfo(map,mType);
    }



    private void getImageCode() {

        mRequestTag =  MethodUrl.imageCode;
        Map<String, String> map = new HashMap<>();
        map.put("token",MbsConstans.TEMP_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap,MethodUrl.imageCode,map);
    }

    private void checkImageCode(){

        mRequestTag =  MethodUrl.imageCode;
        Map<String, Object> map = new HashMap<>();
        map.put("temptoken",MbsConstans.TEMP_TOKEN);
        map.put("imgcode",mImageCode);
        map.put("tel",mPhone);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.imageCode, map);
    }



    private String mImageCode = "";
    private ImageCodeDialog mImageCodeDialog;

    private ImageView mImageView;
    private Bitmap mBitmap;
    private void showZhangDialog(){
        mImageCodeDialog = new ImageCodeDialog(this,true);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.cancel:
                        mImageCodeDialog.dismiss();
                        break;
                    case R.id.confirm:

                        mImageCode = mImageCodeDialog.mEditText.getText()+"";

                        if (UtilTools.empty(mImageCode)){

                            showToastMsg("请输入验证码");
                            return;
                        }


                        checkImageCode();
                        mImageCodeDialog.dismiss();
                        break;
                    case R.id.tv_right:
                        mImageCodeDialog.dismiss();
                        break;
                }
            }
        };
        mImageCodeDialog.setCanceledOnTouchOutside(false);
        mImageCodeDialog.setCancelable(true);
        mImageCodeDialog.setOnClickListener(onClickListener);
        mImageCodeDialog.initValue("图形验证码","");
        mImageCodeDialog.show();

        mImageCodeDialog.mContentTv.setImageBitmap(mBitmap);

        mImageCodeDialog.mContentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageCode();
            }
        });


        mImageCodeDialog.tv_cancel.setEnabled(false);
        mImageCodeDialog.tv_cancel.setVisibility(View.GONE);
    }





    /* 定义一个倒计时的内部类 */
    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            mGetCodeTv.setText(getResources().getString(R.string.msg_code_again));
            mGetCodeTv.setClickable(true);
            MbsConstans.CURRENT_TIME = 0;
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            mGetCodeTv.setClickable(false);
            mGetCodeTv.setText(millisUntilFinished / 1000 + "秒");
            MbsConstans.CURRENT_TIME = (int) (millisUntilFinished / 1000);
        }
    }

    private void sendBrodcast(){
        Intent intent = new Intent();
        intent.setAction(MbsConstans.BroadcastReceiverAction.MONEY_UPDATE);
        sendBroadcast(intent);
    }
    private void sendBrodcast2(){
        Intent intent = new Intent();
        intent.setAction(MbsConstans.BroadcastReceiverAction.BANKUPDATE_UPDATE);
        sendBroadcast(intent);
    }

}
