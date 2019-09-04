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
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 借款信息   界面
 */
public class BorrowInfoActivity extends BasicActivity implements RequestView ,ReLoadingData{

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
    @BindView(R.id.money_tv)
    TextView mMoneyTv;
    @BindView(R.id.borrow_bianhao_value)
    TextView mBorrowBianhaoValue;
    @BindView(R.id.borrow_payman_value)
    TextView mBorrowPaymanValue;
    @BindView(R.id.borrow_getman_value)
    TextView mBorrowGetmanValue;
    @BindView(R.id.borrow_givetime_value)
    TextView mBorrowGivetimeValue;
    @BindView(R.id.borrow_daoqi_value)
    TextView mBorrowDaoqiValue;
    @BindView(R.id.borrow_limit_value)
    TextView mBorrowLimitValue;
    @BindView(R.id.borrow_lilv_value)
    TextView mBorrowLilvValue;
    @BindView(R.id.borrow_givetype_value)
    TextView mBorrowGivetypeValue;
    @BindView(R.id.borrow_use_value)
    TextView mBorrowUseValue;
    @BindView(R.id.borrow_benjin_value)
    TextView mBorrowBenjinValue;
    @BindView(R.id.borrow_lixi_value)
    TextView mBorrowLixiValue;
    @BindView(R.id.borrow_yue_value)
    TextView mBorrowYueValue;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;

    @BindView(R.id.jiekuan_fafang_lay)
    LinearLayout mJieKuanFaFangLay;
    @BindView(R.id.jiekuan_daoqi_lay)
    LinearLayout mJieKuanDaoqiLay;

    private String mRequestTag = "";


    private Map<String, Object> mDataMap;
    private Map<String, Object> mResultMap;

    @Override
    public int getContentView() {
        return R.layout.activity_borrow_info;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
           // mDataMap = (Map<String, Object>) bundle.getSerializable("DATA");
            mResultMap = (Map<String, Object>) bundle.getSerializable("DATA");
        }
        mTitleText.setText(getResources().getString(R.string.detail_title));
        initView();
        setValueTv();
        //borrowDetail();
    }


    private void initView() {
        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();
    }


    private void setValueTv(){

        mPageView.showContent();
        //期限单位(1:年,2:月,3:日)
        String type = mResultMap.get("limitunit")+"";
        String chuzhang = mResultMap.get("vrtacct")+"";//出账品种(0:流动资金贷款;1:银行承兑汇票)
        String use = mResultMap.get("loanuse")+"";//出账品种(0:流动资金贷款;1:银行承兑汇票)

        //期限单位
        //Map<String,Object> danweiMap = SelectDataUtil.getMap(type,SelectDataUtil.getQixianDw());
        Map<String,Object> danweiMap = SelectDataUtil.getMap(type,SelectDataUtil.getNameCodeByType("limitUnit"));
        String danwei = danweiMap.get("name")+"";

        //出账类型
        String chuzhangStr = "";
        Map<String,Object> chuzhangMap = SelectDataUtil.getMap(chuzhang,SelectDataUtil.getChuzhangType());
        chuzhangStr = chuzhangMap.get("name")+"";

        //借款用途  贷款用途
        String jiekuanUser = use;


        String mm =  UtilTools.getRMBMoney(mResultMap.get("reqmoney")+"");
        String benjin = UtilTools.getRMBMoney(mResultMap.get("backmoney")+"");
        String lixi = UtilTools.getRMBMoney(mResultMap.get("backlixi")+"");

        String lilv = UtilTools.getlilv(mResultMap.get("loanlilv")+"");

        String loandate = mResultMap.get("loandate")+"";
        loandate = UtilTools.getStringFromSting(loandate,"yyyyMMdd");
        String stopdate = mResultMap.get("stopdate")+"";
        stopdate = UtilTools.getStringFromSting(stopdate,"yyyyMMdd");

        mMoneyTv.setText(mm);
        mBorrowBenjinValue.setText(benjin);
        mBorrowLixiValue.setText(lixi);
        mBorrowLilvValue.setText(lilv);
        mBorrowGivetimeValue.setText(loandate);
        mBorrowDaoqiValue.setText(stopdate);

        mBorrowBianhaoValue.setText(mResultMap.get("loansqid")+"");
        mBorrowPaymanValue.setText(mResultMap.get("zifangnme")+"");
        mBorrowGetmanValue.setText(mResultMap.get("firmname")+"");

        mBorrowLimitValue.setText(mResultMap.get("loanlimit")+""+danwei);

        mBorrowGivetypeValue.setText(chuzhangStr);
        mBorrowUseValue.setText(jiekuanUser+"");


        double allm = Double.valueOf(mResultMap.get("reqmoney")+"");
        double backm = Double.valueOf(mResultMap.get("backmoney")+"");
        double leftm = UtilTools.sub(allm,backm);
        String ss = UtilTools.getRMBMoney(leftm + "");
        mBorrowYueValue.setText(ss);




        //借款状态（1：放款中 2：还款中 3：已结清 4：已驳回）  loanstate
        String status = mResultMap.get("loanstate") + "";
        LogUtilDebug.i("show","status:"+status);
        switch (status) {
            case "1":
                mJieKuanFaFangLay.setVisibility(View.GONE);
                mJieKuanDaoqiLay.setVisibility(View.GONE);
                break;
            case "2":
                break;
            case "3":
                break;
            case "4":
                mJieKuanFaFangLay.setVisibility(View.GONE);
                mJieKuanDaoqiLay.setVisibility(View.GONE);
                break;
            default:
                mJieKuanFaFangLay.setVisibility(View.GONE);
                mJieKuanDaoqiLay.setVisibility(View.GONE);
                break;
        }

    }


    private void borrowDetail() {

        mRequestTag = MethodUrl.borrowDetail;
        Map<String, String> map = new HashMap<>();
        map.put("loansqid", mDataMap.get("loansqid")+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.borrowDetail, map);
    }


    @OnClick({R.id.back_img,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
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
        Intent intent ;
        switch (mType){
            case MethodUrl.borrowDetail://
                mResultMap = tData;
                setValueTv();
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.borrowDetail:
                        borrowDetail();
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
        borrowDetail();
    }
}
