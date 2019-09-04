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
import com.lr.biyou.utils.secret.RSAUtils;
import com.lr.biyou.utils.tool.RegexUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

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


    private String mAuthCode = "";
    private String mPhone = "";
    private String mType = "";
    private String mKind = "";
    private String mInvcode = "";

    private String mRequestTag = "";


    @Override
    public int getContentView() {
        return R.layout.activity_reset_pay_pass_but;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mAuthCode = bundle.getString("authcode") + "";
            mPhone = bundle.getString("phone") + "";
            mType = bundle.getString("type") + "";
            mKind = bundle.getString("kind") + "";
            mInvcode = bundle.getString("invcode") + "";
        }

        mTitleText.setText("安全中心");
        mTitleText.setCompoundDrawables(null, null, null, null);
        divideLine.setVisibility(View.GONE);

//        //重置密码
//        if (mType.equals("0")) {
//            mTitleText.setText(getResources().getString(R.string.reset_login_pass));
//        } else { //设置密码
//            mTitleText.setText(getResources().getString(R.string.set_login_pass));
//        }


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


    private void submitAction() {
        if (mType.equals("0")) {
            mRequestTag = MethodUrl.RESET_PASSWORD;
        } else {
            mRequestTag = MethodUrl.REGIST_ACTION;
        }
        String password = mNewPassEdit.getText() + "";
        String passwordAgain = mNewPassAgainEdit.getText() + "";


        if (UtilTools.isEmpty(mNewPassEdit, getString(R.string.pass_word))) {
            return;
        }


        if (UtilTools.isEmpty(mNewPassAgainEdit, getString(R.string.pass_word))) {
            return;
        }
        if (!password.equals(passwordAgain)) {
            showToastMsg("两次输入密码不一样，请重新输入");
            return;
        }

        int s = RegexUtil.isLetterDigit(password);
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
        }

        // String pass = AESHelper.encrypt(password, AESHelper.password);
        String pass = RSAUtils.encryptContent(password, RSAUtils.publicKey);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        //注册
        if (mType.equals("1")) {
            Map<String, Object> map = new HashMap<>();
            map.put("authcode", mAuthCode);
            map.put("password", pass);
            map.put("telephone", mPhone);
            map.put("firmkind", mKind);
            map.put("invcode", mInvcode);
            mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.REGIST_ACTION, map);
        } else { //重置密码
            Map<String, String> map = new HashMap<>();
            map.put("authcode", mAuthCode);
            map.put("password", pass);
            map.put("tel", mPhone);
            mRequestPresenterImp.requestPutToRes(mHeaderMap, MethodUrl.RESET_PASSWORD, map);
        }

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
                //submitAction();
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
            case MethodUrl.RESET_PASSWORD:
                showToastMsg("重置密码成功");
                //backTo(LoginActivity.class, false);
                closeAllActivity();

                intent = new Intent(ResetPayPassButActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case MethodUrl.REGIST_ACTION:
                showToastMsg("注册成功");
                closeAllActivity();

                intent = new Intent(ResetPayPassButActivity.this, LoginActivity.class);
                startActivity(intent);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
