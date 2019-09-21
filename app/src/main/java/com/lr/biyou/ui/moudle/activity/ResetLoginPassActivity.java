package com.lr.biyou.ui.moudle.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.utils.tool.UtilTools;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 忘记密码  界面
 */
public class ResetLoginPassActivity extends BasicActivity implements RequestView {

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
    @BindView(R.id.phone_edit)
    EditText mPhoneEdit;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.bt_next)
    Button btNext;
    @BindView(R.id.divide_line)
    View divideLine;

    private String mAccount = "";

    private String mRequestTag = "";

    private TimeCount mTimeCount;

    @Override
    public int getContentView() {
        return R.layout.activity_reset_login_pass;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText("");
        mTitleText.setCompoundDrawables(null,null,null,null);
        mBackImg.setVisibility(View.GONE);
        mBackText.setText("取消");
        divideLine.setVisibility(View.GONE);

        mTimeCount = new TimeCount(1 * 60 * 1000, 1000);


        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()>0 && !UtilTools.empty(etCode.getText()+"")){
                    btNext.setEnabled(true);
                }else {
                    btNext.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()>0 && !UtilTools.empty(etPhone.getText()+"")){
                    btNext.setEnabled(true);
                }else {
                    btNext.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }


    private void getMsgCodeAction() {

        mRequestTag = MethodUrl.REGIST_SMSCODE;
        Map<String, Object> map = new HashMap<>();
        map.put("account",  etPhone.getText() + "");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.REGIST_SMSCODE, map);
    }


    @OnClick({R.id.back_img, R.id.bt_next, R.id.left_back_lay, R.id.tv_code})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.bt_next:
                mAccount = etPhone.getText() + "";
                if (UtilTools.empty(mAccount)) {
                    showToastMsg("手机号或邮箱地址不能为空");
                    return;
                }
                if (UtilTools.empty(etCode.getText().toString())) {
                    showToastMsg("请输入验证码");
                    return;
                }

                intent = new Intent(ResetLoginPassActivity.this, ResetLoginPassButActivity.class);
                Map<String,Object> map = new HashMap<>();
                map.put("account",mAccount);
                map.put("code",etCode.getText().toString());
                map.put("invitation_code","");
                map.put("type","1");
                intent.putExtra("DATA",(Serializable) map);
                startActivity(intent);
//                if (!RegexUtil.isPhone(mAccount)) {
//                    showToastMsg("手机号码格式不正确");
//                    return;
//                }
//                if (!RegexUtil.isEmail(mAccount)) {
//                    showToastMsg("邮箱格式格式不正确");
//                    return;
//                }
                break;
            case R.id.tv_code:
                if (UtilTools.empty(etPhone.getText().toString())) {
                    showToastMsg("手机号或邮箱地址不能为空");
                    return;
                }
                mTimeCount.start();
                getMsgCodeAction();
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
            case MethodUrl.REGIST_SMSCODE:
                switch (tData.get("code")+""){
                    case "0":
                        showToastMsg("获取验证码成功");
                        etCode.setText(tData.get("data")+"");
                        break;

                    case "-1":
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "1":
                        showToastMsg(tData.get("msg")+"");
                        break;
                }

//                intent = new Intent(ResetLoginPassActivity.this, CodeMsgActivity.class);
//                intent.putExtra(MbsConstans.CodeType.CODE_KEY, MbsConstans.CodeType.CODE_RESET_LOGIN_PASS);
//                intent.putExtra("DATA", (Serializable) tData);
//                intent.putExtra("phonenum", mAccount + "");
//                intent.putExtra("showPhone", UtilTools.getPhoneXing(mAccount));
//                startActivity(intent);
                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.resetPassCode:
                        getMsgCodeAction();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map, mType);
    }

    /* 定义一个倒计时的内部类 */
    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            tvCode.setText(getResources().getString(R.string.msg_code_again));
            tvCode.setClickable(true);
            MbsConstans.CURRENT_TIME = 0;
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            tvCode.setClickable(false);
            tvCode.setText(millisUntilFinished / 1000 + "秒");
            MbsConstans.CURRENT_TIME = (int) (millisUntilFinished / 1000);
        }
    }
}
