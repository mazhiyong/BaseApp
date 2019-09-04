package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 还款详情  界面
 */
public class RepaymentInfoActivity extends BasicActivity implements RequestView, ReLoadingData {

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
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.borrow_bianhao_value)
    TextView mBorrowBianhaoValue;
    @BindView(R.id.repay_bianhao_value)
    TextView mRepayBianhaoValue;
    @BindView(R.id.borrow_man_value)
    TextView mBorrowManValue;
    @BindView(R.id.repay_time_value)
    TextView mRepayTimeValue;
    @BindView(R.id.repay_benjin_value)
    TextView mRepayBenjinValue;
    @BindView(R.id.repay_lixi_value)
    TextView mRepayLixiValue;
    @BindView(R.id.repay_type_value)
    TextView mRepayTypeValue;
    @BindView(R.id.status_value)
    TextView mStatusValue;
    @BindView(R.id.des_value)
    TextView mDesValue;
    @BindView(R.id.borrow_hetong_value)
    TextView mBorrowHetongValue;
    @BindView(R.id.repay_hetong_value)
    TextView mRepayHetongValue;

    private String mRequestTag = "";


    private Map<String, Object> mDataMap;
    private Map<String, Object> mResultMap;

    @Override
    public int getContentView() {
        return R.layout.activity_repayment_info;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mDataMap = (Map<String, Object>) bundle.getSerializable("DATA");
        }
        mTitleText.setText(getResources().getString(R.string.repay_detail_title));
        initView();
        repayDetail();
    }


    private void initView() {
        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();
    }


    private void setValueTv() {

        mPageView.showContent();


        String status = mResultMap.get("checkstate")+"";
        String statusStr = "";
        switch (status){
            case "1":
                statusStr = "已申请";
                break;
            case "2":
                statusStr = "还款成功";
                break;
            case "3":
                statusStr = "还款失败";
                break;
        }

        List<Map<String, Object>> list = SelectDataUtil.getNameCodeByType("repayState");
        Map<String,Object> mm = SelectDataUtil.getMapByKey(status+"",list);
        statusStr = mm.get(status+"")+"";

        String backType = mResultMap.get("backtype")+"";
        String backTypeStr = "";
        //还款账户类型(1：客户结算账户还款;2：客户资金账户还款;3：核心企业资金账户代还;4：借款客户网银还款;5：核心企业网银代
        switch (backType){
            case "1":
                backTypeStr = "客户结算账户还款";
                break;
            case "2":
                backTypeStr = "客户资金账户还款";
                break;
            case "3":
                backTypeStr = "核心企业资金账户代还";
                break;
            case "4":
                backTypeStr = "借款客户网银还款";
                break;
            case "5":
                backTypeStr = "核心企业网银代";
                break;
        }

        List<Map<String, Object>> list2 = SelectDataUtil.getNameCodeByType("repayAcct");
        Map<String,Object> mm2 = SelectDataUtil.getMapByKey(backType+"",list2);
        backTypeStr = mm2.get(backType+"")+"";

        String benjin = UtilTools.getRMBMoney(mResultMap.get("backbejn")+"");
        String lixi = UtilTools.getRMBMoney(mResultMap.get("backlixi")+"");

        String time = UtilTools.getStringFromSting2(mResultMap.get("creatime")+"","yyyyMMddHHmmss","yyyy-MM-dd HH:mm:ss");

        mBorrowBianhaoValue.setText(mResultMap.get("loansqid")+"");
        mRepayBianhaoValue.setText(mResultMap.get("rtnbillid")+"");
        mBorrowManValue.setText(mResultMap.get("zifangnme")+"");
        mRepayTimeValue.setText(time+"");
        mRepayBenjinValue.setText(benjin);
        mRepayLixiValue.setText(lixi);
        mRepayTypeValue.setText(backTypeStr);
        mStatusValue.setText(statusStr);
        //mDesValue.setText(mResultMap.get("loansqid")+"");
        //mBorrowHetongValue.setText(mResultMap.get("loanpath")+"");
        //mRepayHetongValue.setText(mResultMap.get("repaypath")+"");

    }


    private void repayDetail() {

        mRequestTag = MethodUrl.repaymentDetail;
        Map<String, String> map = new HashMap<>();
        map.put("rtnbillid", mDataMap.get("rtnbillid") + "");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.repaymentDetail, map);
    }


    @OnClick({R.id.back_img,R.id.left_back_lay,R.id.borrow_hetong_value,R.id.repay_hetong_value})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.borrow_hetong_value:
                //  mBorrowHetongValue.setText(mResultMap.get("loanpath")+"");
                //        mRepayHetongValue.setText(mResultMap.get("repaypath")+"");
                intent = new Intent(RepaymentInfoActivity.this,PDFLookActivity.class);
                intent.putExtra("id",mResultMap.get("loanpath")+"");
                startActivity(intent);
                break;
            case R.id.repay_hetong_value:
                intent = new Intent(RepaymentInfoActivity.this,PDFLookActivity.class);
                intent.putExtra("id",mResultMap.get("repaypath")+"");
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
            case MethodUrl.repaymentDetail://
                mResultMap = tData;
                setValueTv();
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.repaymentDetail:
                        repayDetail();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        mPageView.showNetworkError();
        dealFailInfo(map,mType);
    }

    @Override
    public void reLoadingData() {
        repayDetail();
    }

}
