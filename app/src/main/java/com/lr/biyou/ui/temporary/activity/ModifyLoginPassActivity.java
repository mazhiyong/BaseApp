package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;

import androidx.core.content.ContextCompat;
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

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.utils.secret.RSAUtils;
import com.lr.biyou.utils.tool.RegexUtil;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改登录密码  界面
 */
public class ModifyLoginPassActivity extends BasicActivity implements RequestView {

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
    @BindView(R.id.old_pass_edit)
    EditText mOldPassEdit;
    @BindView(R.id.new_pass_edit)
    EditText mNewPassEdit;
    @BindView(R.id.sure_again_pass_edit)
    EditText mSureAgainPassEdit;
    @BindView(R.id.togglePwd1)
    ToggleButton mTogglePwd1;
    @BindView(R.id.togglePwd2)
    ToggleButton mTogglePwd2;
    @BindView(R.id.togglePwd3)
    ToggleButton mTogglePwd3;
    @BindView(R.id.but_next)
    Button mButNext;

    private String mPhone = "";

    private String mRequestTag = "";

    @Override
    public int getContentView() {
        return R.layout.activity_modify_login_pass;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.modify_login_pass));

        mPhone = SPUtils.get(this, MbsConstans.SharedInfoConstans.LOGIN_ACCOUNT,"")+"";


        mTogglePwd1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //显示密码
                    mOldPassEdit.setTransformationMethod(
                            HideReturnsTransformationMethod.getInstance());
                } else {
                    //隐藏密码
                    mOldPassEdit.setTransformationMethod(
                            PasswordTransformationMethod.getInstance());
                }
            }
        });

        mTogglePwd2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //显示密码
                    mNewPassEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //隐藏密码
                    mNewPassEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        mTogglePwd3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //显示密码
                    mSureAgainPassEdit.setTransformationMethod(
                            HideReturnsTransformationMethod.getInstance());
                } else {
                    //隐藏密码
                    mSureAgainPassEdit.setTransformationMethod(
                            PasswordTransformationMethod.getInstance());
                }
            }
        });
    }


    private void submitData(){

        String oldPass = mOldPassEdit.getText()+"";
        String password = mNewPassEdit.getText() + "";
        String passwordAgain = mSureAgainPassEdit.getText() + "";

        if (UtilTools.isEmpty(mOldPassEdit,getString(R.string.old_pass))){
            return;
        }

        int old = RegexUtil.isLetterDigit(oldPass);
        switch (old){
            case 0:
                break;
            case 1:
                showToastMsg("原密码"+getResources().getString(R.string.must_has_shuzi));
                return;
            case 2:
                showToastMsg("原密码"+getResources().getString(R.string.must_has_zimu));
                return;
            case 3:
                showToastMsg(getResources().getString(R.string.set_new_pass_tip));
                return;
        }


        if (UtilTools.isEmpty(mNewPassEdit,getString(R.string.pass_word))){
            return;
        }
        if (UtilTools.isEmpty(mSureAgainPassEdit,getString(R.string.pass_word))){
            return;
        }
        if (!password.equals(passwordAgain)){
            showToastMsg("两次输入新密码不一致，请重新输入");
            return;
        }

        if (oldPass.equals(password)){
            showToastMsg("新密码不能与原密码相同，请重新输入");
            return;
        }

        int s = RegexUtil.isLetterDigit(password);
        switch (s){
            case 0:
                break;
            case 1:
                showToastMsg("新密码"+getResources().getString(R.string.must_has_shuzi));
                return;
            case 2:
                showToastMsg("新密码"+getResources().getString(R.string.must_has_zimu));
                return;
            case 3:
                showToastMsg(getResources().getString(R.string.set_new_pass_tip));
                return;
        }
        String jiamiOld = RSAUtils.encryptContent(oldPass, RSAUtils.publicKey);
        String jiamiNew = RSAUtils.encryptContent(password, RSAUtils.publicKey);


        mRequestTag = MethodUrl.modifyLoginPass;
        Map<String, String> map = new HashMap<>();
        map.put("oldpass", jiamiOld);
        map.put("newpass", jiamiNew);
        map.put("tel",mPhone);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPutToRes(mHeaderMap, MethodUrl.modifyLoginPass, map);
    }


    @OnClick({R.id.back_img, R.id.but_next, R.id.left_back_lay})
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
                submitData();
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
            case MethodUrl.modifyLoginPass:
                showToastMsg("修改密码成功");
                closeAllActivity();

                intent = new Intent(ModifyLoginPassActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.modifyLoginPass:
                        submitData();
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
