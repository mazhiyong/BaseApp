package com.lr.biyou.ui.temporary.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.mywidget.dialog.SureOrNoDialog;
import com.lr.biyou.ui.temporary.adapter.BankCardAdapter2;
import com.lr.biyou.ui.temporary.adapter.BankCardChildAdapter;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
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

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 银行卡   界面
 */
public class BankCardActivity extends BasicActivity implements RequestView,BankCardAdapter2.OnCheckedBankCardListener,ReLoadingData {

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
    @BindView(R.id.refresh_list_view)
    LRecyclerView mRefreshListView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.bottom_lay)
    LinearLayout mBottomLay;
    @BindView(R.id.bind_tv_lay)
    LinearLayout mBindTvLay;

    private String mRequestTag ="";

    //    private BankCardAdapter mBankCardAdapter;
    private BankCardAdapter2 mBankCardAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private int mPage = 1;


    private Map<String,Object> mSelectMap = new HashMap<>();
    private Map<String,Object> mYueMap = new HashMap<>();
    public static List<View> mViews = new ArrayList<View>();

    private int type = -1;

    private Map<String,Object> mUnBindCard = new HashMap<>();


    private Map<String,Object> mCardConfig;


    @Override
    public int getContentView() {
        return R.layout.activity_bank_card;
    }

    @Override
    public void init() {
        ////getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.QIAN_YUE_WY);
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.BANKUPDATE_UPDATE);
        registerReceiver(mBroadcastReceiver,intentFilter);

        mTitleText.setText(getResources().getString(R.string.bank_card_title));

        initView();
        //responseData();
        showProgressDialog();

        cardConfig();
        bankCardAction();

        /**
         * 设置是否仅仅跟踪左侧边缘的滑动返回
         * 因为银行卡里面有嵌套 viewpager  所以要边界返回
         */
        //mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
    }


    /**
     查询资金托管配置
     */
    private void cardConfig() {
        mRequestTag = MethodUrl.supervisionConfig;
        Map<String, String> map = new HashMap<>();
        map.put("accsn","A");
        Map<String, String> mHeadermap = new HashMap<>();
        mRequestPresenterImp.requestGetToMap(mHeadermap, MethodUrl.supervisionConfig, map);
    }

    private void initView() {
        mPageView.setContentView(mContent);
        mPageView.showLoading();
        mPageView.setReLoadingData(this);
        LinearLayoutManager manager = new LinearLayoutManager(BankCardActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        manager.setSmoothScrollbarEnabled(true);
        manager.setInitialPrefetchItemCount(4);
        mRefreshListView.setItemViewCacheSize(20);
        //mRefreshListView.setDrawingCacheEnabled(true);
        //mRefreshListView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRefreshListView.setLayoutManager(manager);
        mRefreshListView.setHasFixedSize(true);

        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                bankCardAction();
            }
        });

        mRefreshListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //responseData();
            }
        });

        mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
            @Override
            public void reload() {
                showProgressDialog();
                bankCardAction();
            }
        });

    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MbsConstans.BroadcastReceiverAction.QIAN_YUE_WY)){
                bankCardAction();
            }else if (action.equals(MbsConstans.BroadcastReceiverAction.BANKUPDATE_UPDATE)){
                bankCardAction();
            }
        }
    };

    /**
     * 银行卡列表
     */
    private void bankCardAction(){
        mRequestTag = MethodUrl.bankCardList;
        Map<String, String> map = new HashMap<>();
        map.put("isdefault",""); //isdefault 是否默认卡（0：否，1：是）
        map.put("accsn",""); //accsn 业务类型(1:提现账户;A充值卡<快捷支付>;)
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.bankCardList, map);
    }

    /**
     * 解绑  提现  充值卡都可以解绑
     */
    private void unBindCard() {
        mRequestTag = MethodUrl.unbindCard;
        Map<String, Object> map = new HashMap<>();
        map.put("accid", mUnBindCard.get("accid") + "");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.unbindCard, map);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (mIsRefresh){
            bankCardAction();
        }
        mIsRefresh = false;
    }


    //二类户查询列表
    private void erLeiHuList() {

        mRequestTag = MethodUrl.erleiHuList;
        Map<String, String> map = new HashMap<>();
        map.put("patncode", mPatncodeMap.get("patncode") + "");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.erleiHuList, map);
    }
    //二类户余额查询
    private void erLeiHuMoney(){

        mRequestTag = MethodUrl.erleiMoney;
        Map<String, String> map = new HashMap<>();
        map.put("patncode",mMoneyMap.get("patncode")+"");
        map.put("crdno",mMoneyMap.get("accid")+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.erleiMoney, map);
    }
    //绑定的I类户信息查询
    private void bindCardInfo(){

        mRequestTag = MethodUrl.erleiMoney;
        Map<String, String> map = new HashMap<>();
        map.put("patncode",mBindMap.get("patncode")+"");
        map.put("crdno",mBindMap.get("accid")+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.bindList, map);
    }


    private void getYueInfo() {
        mRequestTag = MethodUrl.allZichan;
        Map<String, String> map = new HashMap<>();
        map.put("qry_type","card");
        map.put("accid",mSelectMap.get("accid")+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.allZichan, map);

    }







    private void responseData() {
        if (type == 1){ //当前银行卡余额查询
            for (Map<String,Object> map : mDataList){
                if (map.equals(mSelectMap)){
                    map.put("isShow","1");
                    map.put("money",mYueMap.get("bal_amt")+"");
                }
            }
        }

        if (mBankCardAdapter == null) {
            mBankCardAdapter = new BankCardAdapter2(BankCardActivity.this,1);
//            mBankCardAdapter = new BankCardAdapter(BankCardActivity.this);
            mBankCardAdapter.addAll(mDataList);
            // mBankCardAdapter.setOnChangeBankCardListener(this);

            /*AnimationAdapter adapter = new ScaleInAnimationAdapter(mDataAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(.5f));*/

            mLRecyclerViewAdapter = new LRecyclerViewAdapter(mBankCardAdapter);

//            SampleHeader headerView = new SampleHeader(BankCardActivity.this, R.layout.item_bank_bind);
//            mLRecyclerViewAdapter.addHeaderView(headerView);

            mRefreshListView.setAdapter(mLRecyclerViewAdapter);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(false);


            mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Map<String, Object> item = mBankCardAdapter.getDataList().get(position);
                   /* Intent intent = new Intent(getActivity(), ShowDetailPictrue.class);
                    intent.putExtra("jsonData",item.get("url")+"");
                    startActivity(intent);*/
                }

            });

            //查询余额
            mBankCardAdapter.setOnCheckedBankCardListener(this);


        } else {
            if (mPage == 1) {
                mBankCardAdapter.clear();
            }
            mBankCardAdapter.addAll(mDataList);
            mBankCardAdapter.notifyDataSetChanged();
            mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
        }
     /*   //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.colorAccent, R.color.black, android.R.color.white);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.LineScalePulseOut); //设置下拉刷新Progress的样式
        //mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);  //设置下拉刷新箭头
        //设置头部加载颜色
        mRecyclerView.setHeaderViewColor(R.color.colorAccent, R.color.red ,android.R.color.white);
//设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.colorAccent, R.color.red ,android.R.color.white);*/

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
        }

        mRefreshListView.refreshComplete(10);
        mBankCardAdapter.notifyDataSetChanged();
        /*if (mBankCardAdapter.getDataList().size() <= 0){
            mPageView.showEmpty();
        }else {*/
        mPageView.showContent();
//        }
        mRefreshListView.post(new Runnable() {
            @Override
            public void run() {
                mBankCardAdapter.notifyDataSetChanged();
                mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
            }
        });
    }

    @OnClick({R.id.back_img,R.id.left_back_lay,R.id.bind_tv_lay})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.bind_tv_lay:
                intent = new Intent(BankCardActivity.this,ChongZhiCardAddActivity.class);
                intent.putExtra("backtype","10");
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
        //showProgressDialog();
    }

    @Override
    public void disimissProgress() {
        dismissProgressDialog();
    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {

        Intent intent ;
        switch (mType){
            case MethodUrl.supervisionConfig://{"obankSup":"0","bankSup":"1","fastSup":"0"}
                mCardConfig = tData;
                //是否支持银商入金（0：不支持 1：支持）-暂不支持 不需要调试            bankSup
                //是否支持快捷入金（0：不支持 1：支持                   fastSup
                //是否支持跨行转账（0：不支持 1：支持）                 obankSup

                if (mCardConfig == null || mCardConfig.isEmpty()) {
                    mBottomLay.setVisibility(View.GONE);
                } else {
                    String fastSup = mCardConfig.get("fastSup") + "";
                    if (fastSup.equals("1")) {
                        mBottomLay.setVisibility(View.VISIBLE);
                    }else {
                        mBottomLay.setVisibility(View.GONE);
                    }
                }

                break;
            case MethodUrl.allZichan:
                type = 1;
                mYueMap = tData;
                responseData();
                break;
            case MethodUrl.bindList:
                List<Map<String, Object>> mBindList =   JSONUtil.getInstance().jsonToList(tData.get("result") + "");

                if (mBindList == null || mBindList.size() == 0){
                    mBindList = new ArrayList<>();
                    mBindMap.put("bindShow","0");
                    //mBindMap.put("bindCard",mBindList);
                    showToastMsg(getResources().getString(R.string.bind_card_no));
                }else {
                    mBindMap.put("bindCard",mBindList);
                    mBindMap.put("bindShow","1");
                    if (mBankCardChildAdapter != null){
                        mBankCardChildAdapter.notifyDataSetChanged();
                    }
                }
                break;
            case MethodUrl.erleiMoney:
                mMoneyMap.put("money",tData.get("acctbal")+"");
                mMoneyMap.put("isShow","1");
                if (mBankCardChildAdapter != null){
                    mBankCardChildAdapter.notifyDataSetChanged();
                }

                /*mRefreshListView.post(new Runnable(){
                    @Override
                    public void run() {
                        //mLRecyclerViewAdapter.notifyItemChanged(mLRecyclerViewAdapter.getAdapterPosition(false,(int)mMoneyMap.get("indexPos")) , "jdsjlzx");
                    }
                });*/
                break;
            case MethodUrl.erleiHuList:
                List<Map<String, Object>> mList =   JSONUtil.getInstance().jsonToList(tData.get("result") + "");
                if (mList != null && mList.size() > 0) {
                    intent = new Intent(BankCardActivity.this, SelectBankListActivity.class);
                    intent.putExtra("TYPE","1");
                    intent.putExtra("patncode",mPatncodeMap.get("patncode")+"");
                    startActivity(intent);
                } else {
                    intent = new Intent(BankCardActivity.this, BankOpenActivity.class);
                    intent.putExtra("DATA",(Serializable) mPatncodeMap);
                    startActivity(intent);
                }
                break;
            case MethodUrl.bankCardList://
                type = 0;
                String result = tData.get("result")+"";
                if (UtilTools.empty(result)){
                    mDataList=   JSONUtil.getInstance().jsonToList(result);
                    responseData();
                }else {
                    mDataList = JSONUtil.getInstance().jsonToList(result);
                    if (mDataList != null && mDataList.size() > 0){
                        for (Map<String,Object> map : mDataList){
                            map.put("isShow","0");
                        }

                    }
                    responseData();
                }
                mRefreshListView.refreshComplete(10);
                break;
            case MethodUrl.unbindCard:
                showToastMsg(tData.get("result")+"");
                bankCardAction();
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                showProgressDialog();

                for (String mReqTag : mRequestTagList){
                    switch (mReqTag) {
                        case MethodUrl.bankCardList:
                            bankCardAction();
                            break;
                        case MethodUrl.erleiHuList:
                            erLeiHuList();
                            break;
                        case MethodUrl.erleiMoney:
                            erLeiHuMoney();
                            break;
                        case MethodUrl.allZichan:
                            getYueInfo();
                            break;
                        case MethodUrl.unbindCard:
                            unBindCard();
                            break;
                    }
                }

                mRequestTagList = new ArrayList<>();

                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {
        switch (mType) {
            case MethodUrl.erleiMoney:
                if (mBankCardChildAdapter != null){
                    mBankCardChildAdapter.notifyDataSetChanged();
                }
            case MethodUrl.bankCardList://
                if (mBankCardAdapter != null){
                    if (mBankCardAdapter.getDataList().size() <= 0){
                        mPageView.showNetworkError();
                    }else {
                        mPageView.showContent();
                    }
                    mRefreshListView.refreshComplete(10);
                    mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            showProgressDialog();
                            bankCardAction();
                        }
                    });
                }else {
                    mPageView.showNetworkError();
                }
                break;
        }
        dealFailInfo(map,mType);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void reLoadingData() {
        showProgressDialog();
        bankCardAction();
    }


    private Map<String,Object> mPatncodeMap ;
    private Map<String,Object> mMoneyMap ;
    private Map<String,Object> mBindMap ;
    private BankCardChildAdapter mBankCardChildAdapter;
    @Override
    public void onButClickListener(String type, Map<String, Object> map, BankCardAdapter2 bankCardChildAdapter) {
        switch (type){
            case "1":
                mPatncodeMap = map;
                showProgressDialog();
                erLeiHuList();
                break;
            case "2":
                mMoneyMap = map;
                showProgressDialog();
                erLeiHuMoney();
                break;
            case "3":
               /* mRefreshListView.post(new Runnable(){
                    @Override
                    public void run() {
                       // mLRecyclerViewAdapter.notifyItemChanged(mLRecyclerViewAdapter.getAdapterPosition(false,(int)mMoneyMap.get("indexPos")) , "jdsjlzx");
                    }
                });*/
                break;
            case "4":
                showProgressDialog();
                mBindMap = map;
                bindCardInfo();
                break;
            case "5"://解绑

                SureOrNoDialog sureOrNoDialog = new SureOrNoDialog(BankCardActivity.this,true);
                sureOrNoDialog.initValue("提示","解除绑定后，将无法使用该银行卡办理业务,确定要解除绑定吗？");
                sureOrNoDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.cancel:
                                sureOrNoDialog.dismiss();
                                break;
                            case R.id.confirm:
                                showProgressDialog();
                                mUnBindCard = map;
                                unBindCard();
                                sureOrNoDialog.dismiss();
                                break;
                        }
                    }
                });
                sureOrNoDialog.show();
                sureOrNoDialog.setCanceledOnTouchOutside(false);
                sureOrNoDialog.setCancelable(true);


                break;
            case "6"://查询余额
                mSelectMap = map;
                showProgressDialog();
                getYueInfo();
                break;
        }
    }
}
