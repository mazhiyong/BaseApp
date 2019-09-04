package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
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
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 企业打款填写金额认证  界面
 */
public class QiyeDakuanCheckActivity extends BasicActivity implements RequestView {

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
    @BindView(R.id.money_edit)
    EditText mMoneyEdit;
    @BindView(R.id.qiye_modify_tv)
    TextView mQiyeModifyTv;
    @BindView(R.id.qiye_dakuan_top_tv)
    TextView mQiyeTopTipTv;
    @BindView(R.id.qiye_time_tv)
    TextView mQiyeTimeTv;

    private String mPhone = "";

    private String mRequestTag = "";

    private String mLiushui = "";

    @Override
    public int getContentView() {
        return R.layout.activity_qiye_dakuan_check;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mLiushui = bundle.getString("remitid");

                LogUtilDebug.i("-----------------打款认证流水号",mLiushui);
            }
        }
        super.onNewIntent(intent);
    }

        @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText(getResources().getString(R.string.qiye_dakuan_check));
        UtilTools.setMoneyEdit(mMoneyEdit,0);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mLiushui = bundle.getString("remitid");
        }


        String s = getString(R.string.qiye_modify_zhanghu);
        if (s.contains("?")){
            int i = s.lastIndexOf("?");
            i=i+1;
            SpannableString ss = new SpannableString(s);
            ss.setSpan(new QiyeDakuanCheckActivity.TextSpanClick(false), i, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.grey)), 0, i, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.blue1)), i, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mQiyeModifyTv.setText(ss);
            //添加点击事件时，必须设置
            mQiyeModifyTv.setMovementMethod(LinkMovementMethod.getInstance());
        }else {
            mQiyeModifyTv.setText(s);
        }
        String topXml = getResources().getString(R.string.qiye_dakuan_tip);
        String topStr = String.format(topXml,"认证的企业");
        mQiyeTopTipTv.setText(topStr);

        String qiyeTimeXml = getResources().getString(R.string.qiye_money_time);
        String timeStr = String.format(qiyeTimeXml,"2019-01-01 18:00:00");
        mQiyeTimeTv.setText(timeStr);
    }
    private final class TextSpanClick extends ClickableSpan {
        private boolean status;

        public TextSpanClick(boolean status) {
            this.status = status;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);//取消下划线false
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(QiyeDakuanCheckActivity.this,QiyeCardInfoActivity.class);
            startActivity(intent);

        }
    }


    private void moneyCheck() {//{"remitid":"1910700000291882"}
        String money = mMoneyEdit.getText() + "";
        if (UtilTools.isEmpty(mMoneyEdit,"收款金额")){
            showToastMsg("收款金额不能为空");
            return;
        }
        mRequestTag = MethodUrl.companyPayVerify;
        Map<String, String> map = new HashMap<>();
        map.put("remitid", mLiushui);//打款申请流水
        map.put("amount", money);//打款金额
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap,MethodUrl.companyPayVerify, map);
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
                mButNext.setEnabled(false);
                moneyCheck();
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
        switch (mType){
            case MethodUrl.companyPayVerify:
                mButNext.setEnabled(true);
                intent = new Intent(QiyeDakuanCheckActivity.this,IdCardSuccessActivity.class);
                intent.putExtra(MbsConstans.FaceType.FACE_KEY,MbsConstans.FaceType.FACE_AUTH);
                startActivity(intent);
                finish();
                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.companyPayVerify:
                        moneyCheck();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        switch (mType){
            case MethodUrl.companyPayVerify:
                mButNext.setEnabled(true);
                break;
        }
        dealFailInfo(map,mType);
    }
}
