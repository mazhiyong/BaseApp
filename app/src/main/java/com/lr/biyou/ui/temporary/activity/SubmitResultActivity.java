package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.basic.MbsConstans;
import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.ui.moudle.activity.LoginActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提交结果   界面  申请额度   更换手机号
 */
public class SubmitResultActivity extends BasicActivity implements RequestView {

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
    @BindView(R.id.my_image)
    ImageView mMyImage;
    @BindView(R.id.submit_result_tv)
    TextView mSubmitResultTv;
    @BindView(R.id.submit_tip_tv)
    TextView mSubmitTipTv;
    @BindView(R.id.but_back)
    Button mButBack;


    private int mOpType = 0;

    @Override
    public int getContentView() {
        return R.layout.activity_submit_result;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intentBroast = new Intent();
        intentBroast.setAction(MbsConstans.BroadcastReceiverAction.SHOUXIN_UPDATE);
        sendBroadcast(intentBroast);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mOpType = bundle.getInt(MbsConstans.ResultType.RESULT_KEY);
        }

        switch (mOpType) {
            case MbsConstans.ResultType.RESULT_APPLY_MONEY:
                mTitleText.setText(getResources().getString(R.string.get_my_num));
                mMyImage.setImageResource(R.drawable.wait);
                mButBack.setText(getResources().getString(R.string.but_back));
                mSubmitResultTv.setText(getResources().getString(R.string.submit_success));
                mSubmitTipTv.setText(getResources().getString(R.string.applay_wait_tip));
                break;
            case MbsConstans.ResultType.RESULT_PHONE_CHANGE:
                mTitleText.setText(getResources().getString(R.string.change_phone_num));
                mMyImage.setImageResource(R.drawable.finish);
                mButBack.setText(getResources().getString(R.string.but_sure));
                mSubmitResultTv.setText(getResources().getString(R.string.change_phone_success));
                mSubmitTipTv.setText(getResources().getString(R.string.new_phone_login));
                break;
        }
    }

    /**
     * 网络连接请求
     */
    private void submitInstall() {

        Map<String, Object> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.installCerSubmit, map);
    }

    @OnClick({R.id.back_img, R.id.but_back,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.left_back_lay:
            case R.id.back_img:
                switch (mOpType) {
                    case MbsConstans.ResultType.RESULT_APPLY_MONEY:
                        finish();
                        break;
                    case MbsConstans.ResultType.RESULT_PHONE_CHANGE:
                        closeAllActivity();
                        intent = new Intent(SubmitResultActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
            case R.id.but_back:
                switch (mOpType) {
                    case MbsConstans.ResultType.RESULT_APPLY_MONEY:
                        finish();
                        break;
                    case MbsConstans.ResultType.RESULT_PHONE_CHANGE:
                        closeAllActivity();
                        intent = new Intent(SubmitResultActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
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
