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
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提交结果   借款申请
 */
public class ResultMoneyActivity extends BasicActivity implements RequestView {

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
    @BindView(R.id.title_tv1)
    TextView mTitleTv1;
    @BindView(R.id.value_tv1)
    TextView mValueTv1;
    @BindView(R.id.title_lay1)
    LinearLayout mTitleLay1;
    @BindView(R.id.title_tv2)
    TextView mTitleTv2;
    @BindView(R.id.value_tv2)
    TextView mValueTv2;
    @BindView(R.id.title_lay2)
    LinearLayout mTitleLay2;
    @BindView(R.id.line2)
    View mLine2;
    @BindView(R.id.title_tv3)
    TextView mTitleTv3;
    @BindView(R.id.value_tv3)
    TextView mValueTv3;
    @BindView(R.id.title_lay3)
    LinearLayout mTitleLay3;
    @BindView(R.id.line3)
    View mLine3;


    private int mOpType = 0;

    private Map<String,Object> mDataMap;

    @Override
    public int getContentView() {
        return R.layout.activity_money_result;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intentBroadcast = new Intent();
        intentBroadcast.setAction(MbsConstans.BroadcastReceiverAction.JIE_HUAN_UPDATE);
        sendBroadcast(intentBroadcast);

        intentBroadcast = new Intent();
        intentBroadcast.setAction(MbsConstans.BroadcastReceiverAction.DAIBAN_INFO_UPDATE);
        sendBroadcast(intentBroadcast);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mOpType = bundle.getInt(MbsConstans.ResultType.RESULT_KEY);
            mDataMap = (Map<String, Object>) bundle.getSerializable("DATA");
        }

        switch (mOpType) {
            case MbsConstans.ResultType.RESULT_JIEKUAN:
                mTitleText.setText(getResources().getString(R.string.borrow_money_title));
                mMyImage.setImageResource(R.drawable.wait);
                mButBack.setText(getResources().getString(R.string.but_back));
                mSubmitResultTv.setText(getResources().getString(R.string.submit_success));
                initJikuan();
                break;
            case MbsConstans.ResultType.RESULT_HUANKUAN:
                mTitleText.setText(getResources().getString(R.string.repay_title));
                mMyImage.setImageResource(R.drawable.wait);
                mButBack.setText(getResources().getString(R.string.but_back));
                mSubmitResultTv.setText(getResources().getString(R.string.repay_success));
                initHuanKuan();
                break;
        }
    }

    private void initJikuan(){
        mTitleTv1.setText("借款金额");
        mTitleTv2.setText("借款期限");
        mValueTv1.setText(MbsConstans.RMB+" "+UtilTools.getNormalMoney(mDataMap.get("reqmoney")+""));

        String danwei = mDataMap.get("limitunit")+"";//借款期限单位
        //Map<String,Object> mm = SelectDataUtil.getMap(danwei,SelectDataUtil.getQixianDw());
        Map<String,Object> mm = SelectDataUtil.getMap(danwei,SelectDataUtil.getNameCodeByType("limitUnit"));

        mValueTv2.setText(mDataMap.get("loanlimit")+""+mm.get("name"));

        mTitleLay3.setVisibility(View.GONE);
        mLine3.setVisibility(View.GONE);
    }
    private void initHuanKuan(){
        mTitleTv1.setText("还款本金");
        mTitleTv2.setText("还款利息");
        mTitleTv3.setText("支付方式");

        mValueTv1.setText(MbsConstans.RMB+" "+UtilTools.getNormalMoney(mDataMap.get("backbejn")+""));
        mValueTv2.setText(MbsConstans.RMB+" "+UtilTools.getNormalMoney(mDataMap.get("backlixi")+""));

        String payType = mDataMap.get("backtype")+"";
        //还款账户类型(1：结算账户还款;2：资金账户还款)
        if (payType.equals("2")){
            mValueTv3.setText("资金账户还款");
        }else {
            mValueTv3.setText("结算账户还款");
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

    @OnClick({R.id.back_img, R.id.but_back, R.id.left_back_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back_lay:
            case R.id.back_img:
                finish();
                break;
            case R.id.but_back:
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
