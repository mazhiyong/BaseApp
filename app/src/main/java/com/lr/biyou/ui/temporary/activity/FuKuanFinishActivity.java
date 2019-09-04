package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class FuKuanFinishActivity extends BasicActivity implements RequestView {

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.right_text_tv)
    TextView mRightTextTv;
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.bt_finish)
    Button mBtFinish;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.tv_message)
    TextView mTvMessage;
    @BindView(R.id.card_type)
    TextView mCardType;
    @BindView(R.id.accid_value_tv)
    TextView mAccidValueTv;
    @BindView(R.id.card_lay)
    LinearLayout mCardLay;
    @BindView(R.id.line1)
    View mLine1;
    @BindView(R.id.iv_icon)
    ImageView mIvIcon;

    private String mAccid = "";
    private String mBankName = "";

    @Override
    public int getContentView() {
        return R.layout.activity_fu_kuan_finish;
    }

    @Override
    public void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mTvMoney.setText(MbsConstans.RMB+" "+UtilTools.getNormalMoney(bundle.getString("money")));
            String type = bundle.getString("type");
            switch (type) {
                case "0":
                    mTitleText.setText("付款");
                    mTvMessage.setText("付款成功");
                    mCardLay.setVisibility(View.GONE);
                    mLine1.setVisibility(View.GONE);
                    break;
                case "1":
                    mTitleText.setText("充值");
                    mTvMessage.setText("充值成功");
                    mAccid = bundle.getString("accid");

                    mCardLay.setVisibility(View.VISIBLE);
                    mLine1.setVisibility(View.VISIBLE);
                    mBankName = bundle.getString("bankName");
                    mAccidValueTv.setText(mBankName + "(" + UtilTools.getCardNoFour(mAccid) + ")");
                    break;
                case "2":
                    mTitleText.setText("提现");
                    mTvMessage.setText("提现成功");
                    mAccid = bundle.getString("accid");
                    mCardLay.setVisibility(View.VISIBLE);
                    mLine1.setVisibility(View.VISIBLE);
                    mBankName = bundle.getString("bankName");
                    mAccidValueTv.setText(mBankName + "(" + UtilTools.getCardNoFour(mAccid) + ")");
                    break;
                case "3":
                    mTitleText.setText("提现");
                    mTvMessage.setText("提现失败");
                    mIvIcon.setImageResource(R.drawable.notice);
                    mAccid = bundle.getString("accid");
                    mCardLay.setVisibility(View.VISIBLE);
                    mLine1.setVisibility(View.VISIBLE);
                    mBankName = bundle.getString("bankName");
                    mAccidValueTv.setText(mBankName + "(" + UtilTools.getCardNoFour(mAccid) + ")");
                    break;

                case "4":
                    mTitleText.setText("充值");
                    mTvMessage.setText("充值失败");
                    mIvIcon.setImageResource(R.drawable.notice);
                    mAccid = bundle.getString("accid");
                    mCardLay.setVisibility(View.VISIBLE);
                    mLine1.setVisibility(View.VISIBLE);
                    mBankName = bundle.getString("bankName");
                    mAccidValueTv.setText(mBankName + "(" + UtilTools.getCardNoFour(mAccid) + ")");
                    break;

                case "5":
                    mTitleText.setText("付款");
                    mTvMessage.setText("付款失败");
                    mIvIcon.setImageResource(R.drawable.notice);
                    mCardLay.setVisibility(View.GONE);
                    mLine1.setVisibility(View.GONE);
                    break;

            }
        }
    }


    @OnClick({R.id.back_img, R.id.bt_finish, R.id.left_back_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.bt_finish:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backTo(AllZiChanActivity.class, false);
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

    }
}
