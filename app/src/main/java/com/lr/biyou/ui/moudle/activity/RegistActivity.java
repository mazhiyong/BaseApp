package com.lr.biyou.ui.moudle.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
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
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.KindSelectDialog;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RegistActivity extends BasicActivity implements RequestView, SelectBackListener {


    @BindView(R.id.tv_zhuti)
    TextView mTvZhuti;
    @BindView(R.id.zhuti_lay)
    LinearLayout mZhutiLay;
    @BindView(R.id.et_phone)
    EditText mEtPhone;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.iv_code)
    ImageView mIvCode;
    @BindView(R.id.bt_next)
    Button mBtNext;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.arrow_view)
    ImageView mArrowView;
    @BindView(R.id.divide_line)
    View divideLine;
    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.back_text)
    TextView backText;
    @BindView(R.id.regist_type_tv)
    TextView registTypeTv;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.et_yaocode)
    EditText etYaocode;

    private KindSelectDialog mDialog;
    private String mRequestTag = "";
    private String authcode = "";
    private String type = "";
    private String invcode = "";


    private Map<String, Object> mKindMap;
    private int TYPE = 0; //0 手机号注册   1 邮箱注册

    private TimeCount mTimeCount;


    @Override
    public int getContentView() {
        return R.layout.activity_regist;
    }


    @Override
    public void init() {

        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTimeCount = new TimeCount(1 * 60 * 1000, 1000);

        mTitleText.setText("");
        mTitleText.setCompoundDrawables(null,null,null,null);
        backImg.setVisibility(View.GONE);
        backText.setText("取消");
        divideLine.setVisibility(View.GONE);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            invcode = bundle.getString("result") + "";
        }


        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()>0 && !UtilTools.empty(mEtCode.getText()+"")){
                    mBtNext.setEnabled(true);
                }else {
                    mBtNext.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mEtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length()>0 && !UtilTools.empty(mEtPhone.getText()+"")){
                    mBtNext.setEnabled(true);
                }else {
                    mBtNext.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    /**
     * 获取全局字典配置信息
     */
    public void getNameCodeInfo() {
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.nameCode, map);
    }


    @OnClick({R.id.tv_tip,R.id.tv_code,R.id.regist_type_tv, R.id.zhuti_lay, R.id.iv_code, R.id.bt_next, R.id.left_back_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_tip:
                Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_code:
                //获取短信验证码
                if (UtilTools.empty(mEtPhone.getText().toString())){
                    if (TYPE == 0){
                        showToastMsg("请输入手机号");
                    }else {
                        showToastMsg("请输入邮箱账号");
                    }
                    return;
                }
                mTimeCount.start();
                getMsgcodeAction();
                break;
            case R.id.regist_type_tv:
                if (TYPE == 0) {
                    TYPE = 1;
                    titleTv.setText(getResources().getString(R.string.regist_account_email));
                    registTypeTv.setText(getResources().getString(R.string.regist_account_phone));
                    mEtPhone.setHint(getResources().getString(R.string.regist_account_email_hint));
                } else {
                    TYPE = 0;
                    titleTv.setText(getResources().getString(R.string.regist_account_phone));
                    registTypeTv.setText(getResources().getString(R.string.regist_account_email));
                    mEtPhone.setHint(getResources().getString(R.string.regist_account_phone_hint));
                }
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.zhuti_lay:
                //List<Map<String, Object>> mDataList = SelectDataUtil.getRegisterType();
                List<Map<String, Object>> mDataList = SelectDataUtil.getListByKeyList(SelectDataUtil.getNameCodeByType("firmKind"));
                if (mDataList == null || mDataList.size() == 0) {
                    getNameCodeInfo();
                } else {
                    if (mDataList.size() == 1) {
                        mZhutiLay.setEnabled(false);
                        mArrowView.setVisibility(View.GONE);
                    } else {
                        mZhutiLay.setEnabled(true);
                        mArrowView.setVisibility(View.VISIBLE);
                        mDialog = new KindSelectDialog(this, true, mDataList, 10);
                        mDialog.setSelectBackListener(this);
                        mDialog.showAtLocation(Gravity.BOTTOM, 0, 0);

                    }
                }
                break;
            case R.id.iv_code:
                if (UtilTools.empty(MbsConstans.TEMP_TOKEN)) {
                    getTempTokenAction();
                } else {
                    getImageCodeAction();
                }
                break;
            case R.id.bt_next:
                if (UtilTools.empty(mEtPhone.getText().toString())){
                    if (TYPE == 0){
                        showToastMsg("请输入手机号");
                    }else {
                        showToastMsg("请输入邮箱账号");
                    }
                    return;
                }
                if (UtilTools.empty(mEtCode.getText().toString())){
                    showToastMsg("请输入验证码");
                    return;
                }

                //mBtNext.setEnabled(false);
                intent = new Intent(RegistActivity.this,ResetLoginPassButActivity.class);
                Map<String,Object> map = new HashMap<>();
                map.put("account",mEtPhone.getText().toString());
                map.put("code",mEtCode.getText().toString());
                map.put("invitation_code",etYaocode.getText()+"");
                map.put("type","0");
                intent.putExtra("DATA",(Serializable) map);
                startActivity(intent);




                break;
        }
    }

    private void getTempTokenAction() {
        mRequestTag = MethodUrl.tempToken;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.tempToken, map);
    }


    private void getMsgcodeAction() {
        mRequestTag = MethodUrl.REGIST_SMSCODE;
        Map<String, Object> map = new HashMap<>();
        map.put("account",mEtPhone.getText().toString());
        Map<String, String> mHeaderMap = new HashMap<>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.REGIST_SMSCODE, map);
    }


    private void getImageCodeAction() {
        type = "0";
        mRequestTag = MethodUrl.imageCode;
        Map<String, String> map = new HashMap<>();
        map.put("token", MbsConstans.TEMP_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.imageCode, map);
    }


    private void registAction() {

        if (mKindMap == null || mKindMap.isEmpty()) {
            showToastMsg("请选择账号主体");
            mBtNext.setEnabled(true);
            return;
        }

        if (TextUtils.isEmpty(mEtPhone.getText())) {
            showToastMsg("请编辑手机号码信息");
            mBtNext.setEnabled(true);
            return;
        }

        if (TextUtils.isEmpty(mEtCode.getText())) {
            showToastMsg("请编辑验证码信息");
            mBtNext.setEnabled(true);
            return;
        }

        cheackImageCodeAction();
    }

    private void cheackImageCodeAction() {
        type = "1";
        mRequestTag = MethodUrl.imageCode;
        Map<String, Object> map = new HashMap<>();
        map.put("imgcode", mEtCode.getText().toString());
        map.put("temptoken", MbsConstans.TEMP_TOKEN);
        map.put("tel", mEtPhone.getText().toString());
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.imageCode, map);

    }


    /**
     * @descriptoin 请求前加载progress
     * @author dc
     * @date 2017/2/16 11:00
     */
    @Override
    public void showProgress() {
        showProgressDialog();
    }

    /**
     * @descriptoin 请求结束之后隐藏progress
     * @author dc
     * @date 2017/2/16 11:01
     */
    @Override
    public void disimissProgress() {
        dismissProgressDialog();
    }

    /**
     * @param tData 数据类型
     * @param mType
     * @descriptoin 请求数据成功
     * @author dc
     * @date 2017/2/16 11:01
     */
    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        mBtNext.setEnabled(true);
        Intent intent;
        switch (mType) {
            case MethodUrl.nameCode:
                String result = tData.get("result") + "";
                SPUtils.put(RegistActivity.this, MbsConstans.SharedInfoConstans.NAME_CODE_DATA, result);
                List<Map<String, Object>> mDataList = SelectDataUtil.getListByKeyList(SelectDataUtil.getNameCodeByType("firmKind"));
                if (mDataList == null || mDataList.size() == 0) {
                    showToastMsg("暂无可选择的主体，请联系客服");
                } else {

                    if (mDataList.size() == 1) {
                        mZhutiLay.setEnabled(false);
                        mArrowView.setVisibility(View.GONE);
                    } else {
                        mZhutiLay.setEnabled(true);
                        mArrowView.setVisibility(View.VISIBLE);
                        mDialog = new KindSelectDialog(this, true, mDataList, 10);
                        mDialog.setSelectBackListener(this);
                        mDialog.showAtLocation(Gravity.BOTTOM, 0, 0);

                    }
                }

                break;
            case MethodUrl.tempToken:
                MbsConstans.TEMP_TOKEN = tData.get("temp_token") + "";
                getImageCodeAction();
                break;
            case MethodUrl.imageCode:
                // 获取图片验证码
                if (tData.containsKey("img")) {//获取图片验证码
                    Bitmap bitmap = (Bitmap) tData.get("img");
                    mIvCode.setImageBitmap(bitmap);
                } else {
                    authcode = tData.get("authcode") + ""; //验证图片验证码
                    getSmgCodeAction();
                }
                break;
            case MethodUrl.REGIST_SMSCODE:
                switch (tData.get("code")+""){
                    case "0":
                        showToastMsg(getResources().getString(R.string.code_phone_tip));
                        mEtCode.setText(tData.get("data")+"");
                        break;

                    case "-1":
                        showToastMsg(tData.get("msg")+"");
                        break;

                    case "1":
                        showToastMsg(tData.get("msg")+"");
                        break;
                }

//                intent = new Intent(this, CodeMsgActivity.class);
//                intent.putExtra(MbsConstans.CodeType.CODE_KEY, MbsConstans.CodeType.CODE_PHONE_REGIST);
//                intent.putExtra("phone", mEtPhone.getText().toString());
//                intent.putExtra("authcode", authcode);
//                intent.putExtra("invcode", invcode);
//                intent.putExtra("showPhone", mEtPhone.getText().toString());
//                intent.putExtra("DATA", (Serializable) tData);
//                intent.putExtra("kind", mKindMap.get("code") + "");
//                startActivity(intent);
        }

    }

    private void getSmgCodeAction() {
        mRequestTag = MethodUrl.REGIST_SMSCODE;
        Map<String, Object> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        map.put("tel", mEtPhone.getText().toString());
        map.put("authcode", authcode);
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.REGIST_SMSCODE, map);
    }


    /**
     * @param map
     * @param mType
     * @descriptoin 请求数据错误
     * @date 2017/2/16 11:01
     */
    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        mBtNext.setEnabled(true);
        switch (mType) {
            case MethodUrl.imageCode:
                if (type.equals("0")) { //请求验证码失败
                    mIvCode.setImageResource(R.drawable.default_pic);
                } else { //验证验证码失败
                    getImageCodeAction();
                }
                break;
        }

        dealFailInfo(map, mType);
    }


    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 10:
                String tt = map.get("code") + ""; //选择用户主体类型（个人/企业）
                mTvZhuti.setText(map.get("name") + "");
                mKindMap = map;
                switch (tt) {
                    case "0":
                        mEtPhone.setHint(R.string.please_use_phone_regist2);
                        break;
                    case "1":
                        mEtPhone.setHint(R.string.please_use_phone_regist);
                        break;
                }
                break;
        }
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
