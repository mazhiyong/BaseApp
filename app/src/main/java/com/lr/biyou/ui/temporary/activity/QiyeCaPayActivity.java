package com.lr.biyou.ui.temporary.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.QiyeCaPayAdapter;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class QiyeCaPayActivity extends BasicActivity implements RequestView,ReLoadingData {
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.title_bar_view)
    LinearLayout mTitleBarView;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.tv_money)
    TextView mMoneyText;
    @BindView(R.id.tv_money2)
    TextView mMoneyText2;
    @BindView(R.id.tv_gongyingshang)
    TextView mGongyingshangText;
    @BindView(R.id.bt_pay)
    Button mButton;
    @BindView(R.id._rcv_pay_way)
    RecyclerView mRecyclerView;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.zhekou_tv)
    TextView mZhekouTv;
    @BindView(R.id.bottom_money_tv)
    TextView mBottomMoneyTv;


    QiyeCaPayAdapter mPayWayAdapter;
    private List<Map<String,Object>> mDataList = new ArrayList<>();//支付方式总列表

    private List<Map<String,Object>> mBankList ;//银行列表

    private Map<String,Object> mPayTypeMap ;//选中的支付方式

    private String mRequestTag = "";

    private String mMoney = "0";
    private String mMemo = "";
    private String mRclno = "";//收款人客户号
    private String name = "";

    private Map<String,Object> mPayInfo;

    @Override
    public int getContentView() {
        return R.layout.activity_qiye_ca_pay;
    }

    private Map<String,Object> mSetTiXianBank;

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        StatusBarUtil.setTranslucentForImageView(this, MbsConstans.ALPHA, null);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.title_item_height) + UtilTools.getStatusHeight2(this));
        mTitleBarView.setLayoutParams(layoutParams);
        mTitleBarView.setPadding(0, UtilTools.getStatusHeight2(this), 0, 0);

        mTitleText.setText(getResources().getString(R.string.qiye_ca_money));
        mTitleText.setTextColor(ContextCompat.getColor(this,R.color.white));

        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();

        IntentFilter filter = new IntentFilter();
        filter.addAction(MbsConstans.BroadcastReceiverAction.CAPAY_SUC);
        registerReceiver(receiver, filter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mMoney = bundle.getString("money");
            mMemo = bundle.getString("memo");
            mRclno = bundle.getString("rclno");
            name = bundle.getString("name");
        }

        String ss =  UtilTools.getRMBMoney(mMoney);
       /* ParseTextUtil parseTextUtil = new ParseTextUtil(this);
        Spannable spannable =  parseTextUtil.getDianType(ss);*/
        mMoneyText.setText(ss);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getDefaultPay();
        initRecycleView();
        payInfo();

    }
    @Override
    public void setBarTextColor(){
        StatusBarUtil.setDarkMode(this);
    }

    private void getDefaultPay(){
        Map<String,Object> map = new HashMap<>();
        map = new HashMap<>();
        map.put("opnbnknm","微信");
        map.put("type","12");
        map.put("icon",R.drawable.pay_weixin);
        mDataList.add(map);

        map = new HashMap<>();
        map.put("opnbnknm","支付宝");
        map.put("type","13");
        map.put("icon",R.drawable.pay_zhifubao);
        mDataList.add(map);

        map = new HashMap<>();
        map.put("opnbnknm","网银转账");
        map.put("type","15");
        map.put("icon",R.drawable.pay_zhuanzhang);
        mDataList.add(map);


    }


    /**
     * 证书费用信息   和转账信息
     */
    private void payInfo() {
        mRequestTag = MethodUrl.zsMoneyInfo;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.zsMoneyInfo, map);
    }


    /**
     获取用户银行卡列表
     */
    private void bankCardInfoAction() {
        mRequestTag = MethodUrl.bankCardList;

        Map<String, String> map = new HashMap<>();
        map.put("accsn", "2");
        Map<String, String> mHeadermap = new HashMap<>();
        mRequestPresenterImp.requestGetToRes(mHeadermap, MethodUrl.bankCardList, map);
    }



    private void initRecycleView(){
        if (mPayWayAdapter == null){
            mPayWayAdapter=new QiyeCaPayAdapter(this,mDataList);
            mRecyclerView.setAdapter(mPayWayAdapter);
            mPayWayAdapter.setItemClickListener(new OnMyItemClickListener() {
                @Override
                public void OnMyItemClickListener(View view, int position) {
                    mPayTypeMap = mDataList.get(position);

                }
            });
        }else {
            mPayWayAdapter.notifyDataSetChanged();
        }
        if (mDataList.size() < 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }

    }

    private void butAction(){
        if (mPayTypeMap == null || mPayTypeMap.isEmpty()){
            showToastMsg("请选择支付方式");
            mButton.setEnabled(true);
            return;
        }
        Intent intent;
        String payType = mPayTypeMap.get("type")+"";
        switch (payType){
            case "12"://微信
                showToastMsg("暂未开通");
                mButton.setEnabled(true);
                break;
            case "13"://支付宝
                showToastMsg("暂未开通");
                mButton.setEnabled(true);
                break;
            case "15"://转账
                mButton.setEnabled(false);
                intent = new Intent(QiyeCaPayActivity.this,QiyeCaZhuanZhangActivity.class);
                intent.putExtra("DATA",(Serializable) mPayInfo);
                startActivity(intent);
                mButton.setEnabled(true);
                break;
        }
    }


    @Override
    protected void onResume(){
        super.onResume();
    }

    @OnClick({R.id.back_img,R.id.bt_pay,R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.bt_pay://付款
                mButton.setEnabled(false);
                butAction();
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
        dismissProgressDialog();
        Intent intent;
        mButton.setEnabled(true);
        switch (mType){
            case MethodUrl.zsMoneyInfo:
                mPayInfo = tData;

                String money = mPayInfo.get("charge")+"";
                //折扣比例、
                String bili = mPayInfo.get("rebate")+"";
                mMoneyText2.setText(UtilTools.getRMBMoney( money));
                mMoneyText2.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);

                double moneyReally = UtilTools.mul(Double.valueOf(money),Double.valueOf(bili));
                Spannable moneyStr = UtilTools.getDianType2( QiyeCaPayActivity.this,UtilTools.getRMBMoney(moneyReally+""));
                mMoneyText.setText(moneyStr);

                mBottomMoneyTv.setText(UtilTools.getRMBMoney(moneyReally+""));

                double biliNum = UtilTools.mul(100,Double.valueOf(bili));

                String biliStr = UtilTools.getlilv(biliNum+"");

                mZhekouTv.setText("折扣:"+biliStr);
                break;
            case MethodUrl.bankCardList:
                String result = tData.get("result") + "";
                if (UtilTools.empty(result)) {
                } else {
                    mBankList =   JSONUtil.getInstance().jsonToList(result);
                    if (mBankList != null && mBankList.size() > 0) {
                        for (Map<String,Object> map : mDataList){
                            String s = map.get("accsn")+"";
                            if (s.equals("1") || s.equals("3")){
                                mSetTiXianBank = map;
                            }
                        }
                    } else {

                    }
                }
                mDataList.clear();
                getDefaultPay();
                initRecycleView();
                break;
            case MethodUrl.REFRESH_TOKEN:
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken= false;
                switch (mRequestTag) {
                    case MethodUrl.bankCardList:
                        bankCardInfoAction();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dismissProgressDialog();
        mButton.setEnabled(true);
        dealFailInfo(map,mType);
    }

    @Override
    public void reLoadingData() {
        bankCardInfoAction();
    }

    //广播监听
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            if (MbsConstans.BroadcastReceiverAction.CAPAY_SUC.equals(action)) {
                finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}
