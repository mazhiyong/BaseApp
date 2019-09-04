package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
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
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 录入企业信息  界面
 */
public class QiyeInfoActivity extends BasicActivity implements RequestView {

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.qiye_name_edit)
    EditText mQiyeNameEdit;
    @BindView(R.id.qiye_shxy_edit)
    EditText mQiyeShxyEdit;
    @BindView(R.id.user_name_edit)
    EditText mUserNameEdit;
    @BindView(R.id.idcard_edit)
    EditText mIdcardEdit;
    @BindView(R.id.phone_edit)
    TextView mPhoneEdit;
    @BindView(R.id.but_next)
    Button mButNext;
    private String mOpType = "";

    private String mRequestTag = "";

    private String mUrl = "";

    @Override
    public int getContentView() {
        return R.layout.activity_qiye_info;
    }

    @Override
    public void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mOpType = bundle.getString("type");
        }
        mTitleText.setText(getResources().getString(R.string.qiye_info));
        String mPhone = SPUtils.get(this,MbsConstans.SharedInfoConstans.LOGIN_ACCOUNT,"")+"";
        mPhoneEdit.setText(mPhone);

    }


    private void submitAction() {

        String qiyeName = mQiyeNameEdit.getText()+"";
        String qiyeXyDaima = mQiyeShxyEdit.getText()+"";
        String name = mUserNameEdit.getText()+"";
        String num = mIdcardEdit.getText()+"";

        if (mOpType.equals("1")){
            if (UtilTools.isEmpty(mQiyeNameEdit,"企业名称")){
                showToastMsg("企业名称不能为空");
                mButNext.setEnabled(true);
                return;
            }
            if (UtilTools.isEmpty(mQiyeShxyEdit,"统一社会信用代码")){
                showToastMsg("统一社会信用代码不能为空");
                mButNext.setEnabled(true);
                return;
            }

        }
        if (UtilTools.isEmpty(mUserNameEdit,"姓名")){
            showToastMsg("姓名不能为空");
            mButNext.setEnabled(true);
            return;
        }
        if (UtilTools.isEmpty(mIdcardEdit,"身份证号")){
            showToastMsg("身份证号不能为空");
            mButNext.setEnabled(true);
            return;
        }


        mRequestTag = MethodUrl.companyCheck;

        Map<String, String> map = new HashMap<>();

        map.put("firmname",qiyeName);//企业名称
        map.put("yingyezz",qiyeXyDaima);//社会统一信用代码证
        map.put("farnname",name);//法人名称
        map.put("farnzjno",num);//法人证件号码


        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.companyCheck, map);
    }

    //重写返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //点击完返回键，执行的动作
            finish();
        }
        return true;
    }

    @OnClick({R.id.back_img, R.id.but_next,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                backTo(LoginActivity.class,false);
                break;
            case R.id.left_back_lay:
                finish();
                backTo(LoginActivity.class,false);
                break;
            case R.id.but_next:
                mButNext.setEnabled(false);
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
        mButNext.setEnabled(true);
        Intent intent ;
        switch (mType){
            case MethodUrl.companyCheck:
                mButNext.setEnabled(true);
                intent = new Intent(QiyeInfoActivity.this,QiyeInfoShowActivity.class);
                startActivity(intent);
                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken= false;
                switch (mRequestTag) {
                    case MethodUrl.companyCheck:
                        submitAction();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        mButNext.setEnabled(true);
        dealFailInfo(map,mType);
    }

}
