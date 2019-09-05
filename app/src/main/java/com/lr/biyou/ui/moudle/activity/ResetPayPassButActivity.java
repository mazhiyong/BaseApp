package com.lr.biyou.ui.moudle.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.core.content.ContextCompat;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.ui.moudle1.activity.UserInfoActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.secret.RSAUtils;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.RegexUtil;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;
import com.zhangke.websocket.util.LogUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 重置支付密码  界面
 */
public class ResetPayPassButActivity extends BasicActivity implements RequestView {

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
    @BindView(R.id.but_sure)
    Button mButNext;
    @BindView(R.id.new_pass_edit)
    EditText mNewPassEdit;
    @BindView(R.id.new_pass_again_edit)
    EditText mNewPassAgainEdit;
    @BindView(R.id.togglePwd1)
    ToggleButton mTogglePwd1;
    @BindView(R.id.togglePwd2)
    ToggleButton mTogglePwd2;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password_again)
    EditText etPasswordAgain;
    @BindView(R.id.bt_sure)
    Button btSure;
    @BindView(R.id.divide_line)
    View divideLine;
    @BindView(R.id.et_old_password)
    EditText etOldPassword;
    @BindView(R.id.old_lay)
    LinearLayout oldLay;



    private String mType = "";

    private String mRequestTag = "";

    private  String paycode = "";


    @Override
    public int getContentView() {
        return R.layout.activity_reset_pay_pass_but;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mTitleText.setText("安全中心");
        mTitleText.setCompoundDrawables(null, null, null, null);
        divideLine.setVisibility(View.GONE);

        if (!UtilTools.empty(MbsConstans.PAY_CODE)){
            paycode = MbsConstans.PAY_CODE;
        }else {
            paycode = SPUtils.get(ResetPayPassButActivity.this, MbsConstans.SharedInfoConstans.PAY_CODE, "").toString();
        }

        if (UtilTools.empty(paycode)){ //设置支付密码
            mType = "0";
            oldLay.setVisibility(View.GONE);
        }else {
            mType = "1";
            oldLay.setVisibility(View.VISIBLE); //重置支付密码
        }


        mTogglePwd1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //显示密码
                    mNewPassEdit.setTransformationMethod(
                            HideReturnsTransformationMethod.getInstance());
                } else {
                    //隐藏密码
                    mNewPassEdit.setTransformationMethod(
                            PasswordTransformationMethod.getInstance());
                }
            }
        });
        mTogglePwd2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //显示密码
                    mNewPassAgainEdit.setTransformationMethod(
                            HideReturnsTransformationMethod.getInstance());
                } else {
                    //隐藏密码
                    mNewPassAgainEdit.setTransformationMethod(
                            PasswordTransformationMethod.getInstance());
                }
            }
        });

    }


    /**
     * 获取用户信息
     */
    private void getUserInfoAction() {
        mRequestTag = MethodUrl.USER_INFO;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(ResetPayPassButActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.USER_INFO, map);
    }


    private void submitAction() {
        mRequestTag = MethodUrl.UPDATE_PAYCODE;

        String oldpassword =etOldPassword.getText().toString();
        String password = etPassword.getText().toString();
        String passwordAgain = etPasswordAgain.getText().toString();
        if (mType.equals("1") && UtilTools.empty(oldpassword)) {
            showToastMsg("请输入原支付密码");
            return;
        }

        if (UtilTools.empty(password)) {
            showToastMsg("请输入支付密码");
            return;
        }


        if (UtilTools.empty(passwordAgain)) {
            showToastMsg("请再次输入支付密码");
            return;
        }
        if (!password.equals(passwordAgain)) {
            showToastMsg("两次输入密码不一样，请重新输入");
            return;
        }

        /*int s = RegexUtil.isLetterDigit(password);
        switch (s) {
            case 0:
                break;
            case 1:
                showToastMsg(getResources().getString(R.string.must_has_shuzi));
                return;
            case 2:
                showToastMsg(getResources().getString(R.string.must_has_zimu));
                return;
            case 3:
                showToastMsg(getResources().getString(R.string.set_new_pass_tip));
                return;
        }*/

        // String pass = AESHelper.encrypt(password, AESHelper.password);
        //String pass = RSAUtils.encryptContent(password, RSAUtils.publicKey);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(ResetPayPassButActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("old_password",oldpassword);
        map.put("payment_password",password);
        map.put("repayment_password",passwordAgain);
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.UPDATE_PAYCODE, map);

        paycode = password;

    }

    @OnClick({R.id.back_img, R.id.left_back_lay, R.id.bt_sure})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.bt_sure:
                submitAction();
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
        Intent intent;
        switch (mType) {
            case MethodUrl.UPDATE_PAYCODE:
                switch (tData.get("code")+""){
                    case "0": //请求成功
                        if (mType.equals("0")){
                            showToastMsg("设置成功");
                        }else {
                            showToastMsg("重置成功");
                        }

                        SPUtils.put(ResetPayPassButActivity.this, MbsConstans.SharedInfoConstans.PAY_CODE, paycode);

                        finish();
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(ResetPayPassButActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;

                }
                break;
            case MethodUrl.USER_INFO:
                switch (tData.get("code")+""){
                    case "0": //请求成功
                        MbsConstans.USER_MAP = (Map<String, Object>) tData.get("data");
                        if (!UtilTools.empty(MbsConstans.USER_MAP)){
                            SPUtils.put(ResetPayPassButActivity.this, MbsConstans.SharedInfoConstans.LOGIN_INFO, JSONUtil.getInstance().objectToJson(MbsConstans.USER_MAP));
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(ResetPayPassButActivity.this, LoginActivity.class);
                        startActivity(intent);

                        break;

                }
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.RESET_PASSWORD:
                        submitAction();
                        break;
                }
                break;
        }

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map, mType);
    }


}
