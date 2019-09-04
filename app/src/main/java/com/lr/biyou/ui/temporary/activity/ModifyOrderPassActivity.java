package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;

import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mywidget.view.CustomerKeyboard;
import com.lr.biyou.mywidget.view.PasswordEditText;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.secret.RSAUtils;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改密码  界面
 */
public class ModifyOrderPassActivity extends BasicActivity implements CustomerKeyboard.CustomerKeyboardClickListener, PasswordEditText.PasswordFullListener{

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
    @BindView(R.id.password_edit_text)
    PasswordEditText mPasswordEditText;
    @BindView(R.id.custom_key_board)
    CustomerKeyboard mCustomKeyBoard;

    @BindView(R.id.order_pass_tip_tv)
    TextView mOrderPassTipTv;

    private String mRequestTag = "";

    private String mActionType = "";
    private String mBackType = "";

    private String mOldPass = "";
    private String mNewPass = "";
    private String mAgainPass = "";

    private String mEditPass = "";

    private String mAuthCode= "";


    @Override
    public int getContentView() {
        return R.layout.activity_modify_order_pass;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mActionType = bundle.getString("TYPE");
            mBackType = bundle.getString("BACK_TYPE","");//返回到哪个界面   提现界面  和  设置界面
            mAuthCode = bundle.getString("authcode");//短信校验后的authcode
            switch (mActionType){
                case "1"://设置新交易密码  第一次进来
                    mTitleText.setText(getResources().getString(R.string.ser_order_pass_title));
                    mOrderPassTipTv.setText(getResources().getString(R.string.order_new_pass));
                    break;
                case "11"://设置新交易密码  第二次进来 再确认输入一遍 和第一次输入的新的密码进行验证  最终提交
                    mTitleText.setText(getResources().getString(R.string.ser_order_pass_title));
                    mOrderPassTipTv.setText(getResources().getString(R.string.order_sure_pass));
                    mNewPass = bundle.getString("PASS");
                    break;
                case "2"://修改交易密码  第一次进来 输入旧的交易密码验证身份
                    mTitleText.setText(getResources().getString(R.string.modify_order_pass_title));
                    mOrderPassTipTv.setText(getResources().getString(R.string.order_old_pass));
                    break;
                case "21"://修改交易密码  第二次进来 输入新的交易密码
                    mTitleText.setText(getResources().getString(R.string.modify_order_pass_title));
                    mOrderPassTipTv.setText(getResources().getString(R.string.order_new_pass));
                    mOldPass = bundle.getString("PASS");
                    break;
                case "22"://修改交易密码  第三次进来 再次输入新的交易密码 和上一次输入新的交易密码进行验证  最终提交
                    mTitleText.setText(getResources().getString(R.string.modify_order_pass_title));
                    mOrderPassTipTv.setText(getResources().getString(R.string.order_sure_pass));
                    mNewPass = bundle.getString("PASS");
                    mOldPass = bundle.getString("OLDPASS");
                    break;
                case "3"://忘记交易密码  第一次进来
                    mTitleText.setText(getResources().getString(R.string.ser_order_pass_title));
                    mOrderPassTipTv.setText(getResources().getString(R.string.order_new_pass));
                    break;
                case "31"://忘记交易密码  第二次进来
                    mTitleText.setText(getResources().getString(R.string.ser_order_pass_title));
                    mOrderPassTipTv.setText(getResources().getString(R.string.order_sure_pass));
                    mNewPass = bundle.getString("PASS");
                    break;
            }
        }
        mCustomKeyBoard.setOnCustomerKeyboardClickListener(this);
        mPasswordEditText.setEnabled(false);
        mPasswordEditText.setOnPasswordFullListener(this);
        mButNext.setEnabled(false);
    }


    /**
     * 检查交易密码是否正确
     */
    private void checkPass() {
        mRequestTag = MethodUrl.checkTradePass;
        Map<String, String> map = new HashMap<>();
        map.put("trd_pwd", RSAUtils.encryptContent(mEditPass,RSAUtils.publicKey));
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.checkTradePass, map);
    }
    /**
     * 设置交易密码
     */
    private void setPass() {
        mRequestTag = MethodUrl.setTradePass;
        Map<String, String> map = new HashMap<>();

        switch (mActionType){
            case "11"://设置新交易密码  第二次进来 再确认输入一遍 和第一次输入的新的密码进行验证  最终提交
                break;
            case "22"://修改交易密码  第三次进来 再次输入新的交易密码 和上一次输入新的交易密码进行验证  最终提交
                map.put("trd_pwd_old", RSAUtils.encryptContent(mOldPass,RSAUtils.publicKey));
                break;
            case "31"://忘记交易密码  第二次进来 再次输入新的交易密码  和第一次验证判断提交   （需加上验证短信成功后的authcode）
                map.put("authcode",mAuthCode);
                break;
        }
        map.put("trd_pwd", RSAUtils.encryptContent(mNewPass,RSAUtils.publicKey));
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPutToMap(mHeaderMap, MethodUrl.setTradePass, map);
    }


    @OnClick({R.id.back_img, R.id.but_next,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.but_next:

                switch (mActionType){
                    case "1"://设置新交易密码  第一次进来
                        intent = new Intent(ModifyOrderPassActivity.this, ModifyOrderPassActivity.class);
                        intent.putExtra("TYPE","11");
                        intent.putExtra("PASS",mEditPass);
                        intent.putExtra("BACK_TYPE",mBackType);
                        startActivity(intent);
                        break;
                    case "11"://设置新交易密码  第二次进来 再确认输入一遍 和第一次输入的新的密码进行验证  最终提交
                        if (mNewPass.equals(mEditPass)){
                            setPass();
                        }else {
                            showToastMsg("两次输入密码不同");
                        }
                        break;
                    case "2"://修改交易密码  第一次进来 输入旧的交易密码验证身份
                        //请求后台验证老密码是否正确   验证成功后设置新密码
                        //-------------------------
                        checkPass();
                        break;
                    case "21"://修改交易密码  第二次进来 输入新的交易密码
                        intent = new Intent(ModifyOrderPassActivity.this, ModifyOrderPassActivity.class);
                        intent.putExtra("TYPE","22");
                        intent.putExtra("PASS",mEditPass);
                        intent.putExtra("OLDPASS",mOldPass);
                        intent.putExtra("BACK_TYPE",mBackType);
                        startActivity(intent);
                        break;
                    case "22"://修改交易密码  第三次进来 再次输入新的交易密码 和上一次输入新的交易密码进行验证  最终提交

                        if (mNewPass.equals(mEditPass)){
                            setPass();
                        }else {
                            showToastMsg("两次输入密码不同");
                        }
                        break;
                    case "3"://忘记交易密码  第一次进来
                        intent = new Intent(ModifyOrderPassActivity.this, ModifyOrderPassActivity.class);
                        intent.putExtra("TYPE","31");
                        intent.putExtra("PASS",mEditPass);
                        intent.putExtra("BACK_TYPE",mBackType);
                        intent.putExtra("authcode",mAuthCode);
                        startActivity(intent);
                        break;
                    case "31"://忘记交易密码  第二次进来
                        if (mNewPass.equals(mEditPass)){
                            setPass();
                        }else {
                            showToastMsg("两次输入密码不同");
                        }
                        break;
                }


                break;
        }
    }

    @Override
    public void click(String number) {
        mPasswordEditText.addPassword(number);
    }

    @Override
    public void delete() {
        mPasswordEditText.deleteLastPassword();
        mButNext.setEnabled(false);
    }

    @Override
    public void passwordFull(String password) {
        // Toast.makeText(this,"密码输入完毕->"+password,Toast.LENGTH_LONG).show();
        mButNext.setEnabled(true);
        mEditPass = password;
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void disimissProgress() {
        dismissProgressDialog();
    }

    /**
     */
    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        Intent intent;
        switch (mType){
            case MethodUrl.setTradePass:
                showToastMsg(tData.get("result")+"");
                if (mBackType.equals("2")){
                    intent = new Intent();
                    intent.setAction(MbsConstans.BroadcastReceiverAction.TRADE_PASS_UPDATE);
                    sendBroadcast(intent);
                    backTo(TiXianActivity.class,false);
                }else if(mBackType.equals("3")){
                    intent = new Intent();
                    intent.setAction(MbsConstans.BroadcastReceiverAction.TRADE_PASS_UPDATE);
                    sendBroadcast(intent);
                    backTo(TiXianActivity.class,false);
                }else {
                    backTo(MoreSetActivity.class,true);
                }
                break;
            //老交易密码 检测
            case MethodUrl.checkTradePass:

                String st = tData.get("check_rst")+"";
                if (st.equals("1")){
                    //现在假如验证成功了
                    intent = new Intent(ModifyOrderPassActivity.this, ModifyOrderPassActivity.class);
                    intent.putExtra("TYPE","21");
                    intent.putExtra("PASS",mEditPass);
                    intent.putExtra("BACK_TYPE",mBackType);
                    startActivity(intent);
                }else {
                    showToastMsg("密码错误，请重新输入");
                }

                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.checkTradePass:
                        checkPass();
                        break;
                    case MethodUrl.setTradePass:
                        setPass();
                        break;
                }
                break;
        }
    }

    /**
     */
    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map,mType);
    }
}
