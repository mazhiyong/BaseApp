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
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 交易明细详情   界面
 */
public class TradeDetailActivity extends BasicActivity implements RequestView {

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
    @BindView(R.id.trade_title_value_tv)
    TextView mTradeTitleValueTv;
    @BindView(R.id.trade_money_value_tv)
    TextView mTradeMoneyValueTv;
    @BindView(R.id.trade_status_value_tv)
    TextView mTradeStatusValueTv;
    @BindView(R.id.trade_time_value_tv)
    TextView mTradeTimeValueTv;
    @BindView(R.id.trade_num_value_tv)
    TextView mTradeNumValueTv;
    @BindView(R.id.trade_des_value_tv)
    TextView mTradeDesValueTv;
    @BindView(R.id.trade_zhuangtai_value_tv)
    TextView mTradeZhuangtaiValueTv;
    @BindView(R.id.trade_fenqi_value_tv)
    TextView mTradeFenqiValueTv;

    private String mRequestTag = "";


    private Map<String,Object> mDataMap;

    @Override
    public int getContentView() {
        return R.layout.activity_trade_detail;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mDataMap = (Map<String, Object>) bundle.getSerializable("DATA");
        }

        mTitleText.setText(getResources().getString(R.string.detail_title));
        initView();

    }


    private void initView() {
        if (mDataMap == null){
            return;
        }

        String title = mDataMap.get("target")+"";
        String money = mDataMap.get("amt")+"";
        String time = mDataMap.get("dealtime")+"";
        String tradeNum = mDataMap.get("billno")+"";
        String des = mDataMap.get("abstract")+"";
        String zhaungtai = mDataMap.get("state")+"";
        String fenqi = mDataMap.get("")+"";


        String type = mDataMap.get("billtype")+"";
        String typeShow = "";
        switch (type){
            case "top_up":
                typeShow = "充值";
                break;
            case "withdraw":
                typeShow = "提现";
                break;
            case "borrow":
                typeShow = "借款";
                break;
            case "repayment":
                typeShow = "还款";
                break;
            case "other":
                typeShow = "其他";
                break;
        }

        money = UtilTools.getRMBMoney(money);

        if (UtilTools.empty(title)){
            title = "";
            mTradeTitleValueTv.setVisibility(View.GONE);
        }else {
            mTradeTitleValueTv.setVisibility(View.VISIBLE);
        }
        mTradeTitleValueTv.setText(title);
        mTradeMoneyValueTv.setText(money);
        mTradeStatusValueTv.setText(typeShow+""+zhaungtai);
        mTradeTimeValueTv.setText(time);
        mTradeNumValueTv.setText(tradeNum);
        mTradeDesValueTv.setText(des);
        mTradeZhuangtaiValueTv.setText(zhaungtai);
        mTradeFenqiValueTv.setText(typeShow);
    }


    private void traderListAction() {


        mRequestTag = MethodUrl.bankCardList;
        Map<String, String> map = new HashMap<>();
        map.put("ptncode", "");
        map.put("busi_type", "");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.tradeList, map);
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

    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {
        dealFailInfo(map,mType);
    }

}
