package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.utils.tool.ParseTextUtil;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择手机号信息   界面
 */
public class ChoosePhoneActivity extends BasicActivity implements RequestView {


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
    @BindView(R.id.my_image)
    ImageView mMyImage;
    @BindView(R.id.choose_phone_tips)
    TextView mChoosePhoneTips;
    @BindView(R.id.but_new_phone)
    Button mButNewPhone;
    @BindView(R.id.user_old_phone_tv)
    TextView mUserOldPhoneTv;
    @BindView(R.id.personal_scrollView)
    ScrollView mPersonalScrollView;

    private String mRequestTag = "";

    private Map<String,Object> mPhoneMap;

    private ParseTextUtil mParseTextUtil;


    @Override
    public int getContentView() {
        return R.layout.activity_choose_phone;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mParseTextUtil = new ParseTextUtil(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
        }

        String s = "如果启用您的新注册账号：系统将自动注销您原登录账号：";
        Spannable spannableString = mParseTextUtil.parseValueColorNum(s);
        mChoosePhoneTips.setText(spannableString);
        mTitleText.setText(getResources().getString(R.string.activation_phone));
        getPhonesInfo();
    }

    /**
     * 查询新老手机号信息
     */
    private void getPhonesInfo() {

        mRequestTag = MethodUrl.userTelephones;
        Map<String, Object> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.userTelephones, map);
    }
    /**
     * 设置新的手机号  登录
     */
    private void submitNewPhone() {
        if (mPhoneMap == null){
            showToastMsg("获取用户手机号信息失败，请退出重试");
            return;
        }

        mRequestTag = MethodUrl.setNewTel;
        Map<String, Object> map = new HashMap<>();
        map.put("new_tel", mPhoneMap.get("new_tel")+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.setNewTel, map);
    }

    @OnClick({R.id.back_img, R.id.left_back_lay,R.id.but_new_phone,R.id.user_old_phone_tv})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.but_new_phone:
                submitNewPhone();
                break;
            case R.id.user_old_phone_tv:
                showToastMsg("请使用老账号重新登录");
                closeAllActivity();
                intent = new Intent(ChoosePhoneActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
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
            case  MethodUrl.userTelephones:
                mPhoneMap = tData;
                String s = "如果启用您的新注册账号："+mPhoneMap.get("new_tel")+"，系统将自动注销您原登录账号："+mPhoneMap.get("old_tel");
                Spannable spannableString = mParseTextUtil.parseValueColorNum(s);
                mChoosePhoneTips.setText(spannableString);
                break;
            case MethodUrl.setNewTel:
                showToastMsg("激活成功，请重新登录");
                closeAllActivity();
                intent = new Intent(ChoosePhoneActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.userTelephones:
                        getPhonesInfo();
                        break;
                }
                break;
        }

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map,mType);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
