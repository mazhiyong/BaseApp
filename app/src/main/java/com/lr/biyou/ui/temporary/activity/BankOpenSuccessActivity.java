package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;

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
import com.jaeger.library.StatusBarUtil;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 开户成功  界面
 */
public class BankOpenSuccessActivity extends BasicActivity implements RequestView {

    private String TAG = "BankOpenSuccessActivity";

    @BindView(R.id.card_num_tv)
    EditText mCardNumTv;
    @BindView(R.id.but_next)
    Button mButNext;
    @BindView(R.id.containerLayout)
    LinearLayout mContainerLayout;
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


    private String mRequestTag = "";

     @Override
     public int getContentView() {
         return R.layout.activity_bank_open_success;
     }
    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
//        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white), 60);

        mTitleText.setText(getResources().getString(R.string.bank_card_open_title));
        mCardNumTv.setFocusable(true);
        mCardNumTv.requestFocus();
        mCardNumTv.setEnabled(false);

    }




    @OnClick({R.id.back_img, R.id.but_next,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.but_next:
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
        switch (mType) {
            case  MethodUrl.serverRandom://
                break;
            case MethodUrl.erLeihuPass:

                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";                 mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.serverRandom:
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

        dealFailInfo(map,mType);
    }

}
