package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.lr.biyou.utils.tool.RegexUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 更换手机号  界面
 */
public class ChangePhoneActivity extends BasicActivity implements RequestView{

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
    @BindView(R.id.new_phone_edit)
    EditText mNewPhoneEdit;


    private String mAuthCode="";

    private String mRequestTag = "";
    private String mPhone = "";

    @Override
    public int getContentView() {
        return R.layout.activity_change_phone;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        Intent intent  = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mAuthCode = bundle.getString("authcode");
        }
        mTitleText.setText(getResources().getString(R.string.login_phone_num));
    }

    /**
     * 获取短信验证码
     */
    private void getMsgCodeAction() {

        mRequestTag = MethodUrl.changePhoneMsgCode;
        Map<String, Object> map = new HashMap<>();
        map.put("tel", mPhone);
        map.put("authcode", mAuthCode);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap,MethodUrl.changePhoneMsgCode, map);
    }
    @OnClick({R.id.back_img, R.id.but_next,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.but_next:

                mPhone = mNewPhoneEdit.getText()+"";

                if (UtilTools.isEmpty(mNewPhoneEdit,"手机号码")){
                    showToastMsg("手机号码不能为空");
                    return;
                }
                if (!RegexUtil.isPhone(mPhone)){
                    showToastMsg("手机号码格式不正确");
                    return;
                }
                getMsgCodeAction();
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
        //{smstoken=sms_token@3948602038bb8ecf912e0ede4a577ebd, send_tel=151****3298}
        Intent intent;
        switch (mType){
            case  MethodUrl.changePhoneMsgCode:
                showToastMsg("获取验证码成功");
                intent = new Intent(ChangePhoneActivity.this,CodeMsgActivity.class);
                intent.putExtra("DATA",(Serializable) tData);
                intent.putExtra(MbsConstans.CodeType.CODE_KEY, MbsConstans.CodeType.CODE_NEW_PHONE_CHANGE);
                intent.putExtra("authcode",mAuthCode);
                intent.putExtra("phone",mPhone);
                intent.putExtra("showPhone", mPhone);
                startActivity(intent);
                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.changePhoneMsgCode:
                        getMsgCodeAction();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {
        dealFailInfo(map,mType);
    }
}
