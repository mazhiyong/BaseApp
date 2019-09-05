package com.lr.biyou.ui.moudle4.fragment;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidkun.xtablayout.XTabLayout;
import com.flyco.dialog.utils.CornerUtils;
import com.github.jdsjlzx.ItemDecoration.GridItemDecoration;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicFragment;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.OnChildClickListener;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.dialog.KindSelectDialog;
import com.lr.biyou.mywidget.view.LoadingWindow;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle.activity.MainActivity;
import com.lr.biyou.ui.moudle.activity.ResetPayPassButActivity;
import com.lr.biyou.ui.moudle4.activity.DingDanListActivity;
import com.lr.biyou.ui.temporary.activity.MyShouMoneyActivity;
import com.lr.biyou.ui.moudle4.activity.PayMoneyActivity;
import com.lr.biyou.ui.moudle4.adapter.DingDanListAdapter;
import com.lr.biyou.ui.moudle4.adapter.FBTradeListAdapter;
import com.lr.biyou.utils.tool.AnimUtil;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * 法币交易
 */
public class FBTradeFragment extends BasicFragment implements RequestView, ReLoadingData, SelectBackListener {

    @BindView(R.id.refresh_list_view)
    LRecyclerView mRefreshListView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.tab_layout)
    XTabLayout tabLayout;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tab_child)
    XTabLayout tabChild;
    @BindView(R.id.BiType_tv)
    TextView BiTypeTv;
    @BindView(R.id.bitype_lay)
    LinearLayout bitypeLay;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.etNumber)
    EditText mEtNumber;
    @BindView(R.id.amount_tv)
    TextView amountTv;
    @BindView(R.id.tv_dell)
    TextView tvDell;
    @BindView(R.id.lay)
    LinearLayout lay;

    private int TYPE = 0;
    private LoadingWindow mLoadingWindow;

    private LRecyclerViewAdapter mLRecyclerViewAdapter1 = null;
    private LRecyclerViewAdapter mLRecyclerViewAdapter2 = null;
    private LRecyclerViewAdapter mLRecyclerViewAdapter3 = null;
    private LRecyclerViewAdapter mLRecyclerViewAdapter4 = null;
    private LRecyclerViewAdapter mLRecyclerViewAdapter5 = null;
    private LRecyclerViewAdapter mLRecyclerViewAdapter6 = null;
    private LRecyclerViewAdapter mLRecyclerViewAdapter7 = null;


    private FBTradeListAdapter mBuyAdapter1;
    private FBTradeListAdapter mBuyAdapter2;
    private FBTradeListAdapter mBuyAdapter3;
    private FBTradeListAdapter mBuyAdapter4;
    private FBTradeListAdapter mBuyAdapter5;
    private FBTradeListAdapter mBuyAdapter6;
    private DingDanListAdapter mdingdanAdapter;


    private String mRequestTag = "";

    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private List<Map<String, Object>> mTabsData = new ArrayList<>();

    //    private String mStartTime="";
//    private String mEndTime = "";
    private String mJieKuanStatus = "";

    private String mKeyword = "";

    private String mSelectStartTime = "";
    private String mSelectEndTime = "";
    private String mSelectType = "";


    private String mDataType = "";

    private AnimUtil mAnimUtil;

    private int TAB_POSITION = 0;
    private int tab_POSITION = 0;
    private XTabLayout.Tab tabChild1;
    private XTabLayout.Tab tabChild2;
    private XTabLayout.Tab tabChild3;

    private XTabLayout.Tab tabDialog1;
    private XTabLayout.Tab tabDialog2;

    private KindSelectDialog mDialog;

    private String symbol ="";
    private String direction = "0"; //0 买  1卖

    private int mPage = 1;
    private String id = "";


    public FBTradeFragment() {
        // Required empty public constructor
    }


    /**
     * -------------------------------------懒加载  start
     */
    boolean isViewInitiated;
    boolean isVisibleToUser;
    boolean isDataInitiated;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            //showProgressDialog();
//            if (getUserVisibleHint()) {
//                setWebsocketListener();
//                handler.post(runnable);
//            }
            //请求数据 只在进入时加载数据，不进行预加载数据
            mLoadingWindow.showView();
            //获取交易区
            get0tcTradeAction();

            //获取买卖挂单记录
            getEntrustListAction();


            LogUtilDebug.i("show", "FB懒加载数据");
            isDataInitiated = true;
            return true;
        }
        return false;
    }


    /**
     * --------------------------------------懒加载     end
     */


    @Override
    public int getLayoutId() {
        return R.layout.fragment_listdata;
    }

    @Override
    public void init() {
        mLoadingWindow = new LoadingWindow(getActivity(), R.style.Dialog);
        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);

        mAnimUtil = new AnimUtil();

        tabLayout.addTab(tabLayout.newTab().setText("我要买"));
        tabLayout.addTab(tabLayout.newTab().setText("我要卖"));
        tabLayout.addTab(tabLayout.newTab().setText("发布"));
        tabLayout.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                //mViewPager.setCurrentItem(tab.getPosition());
                TAB_POSITION = tab.getPosition();
                switch (TAB_POSITION) {
                    case 0://我要买
                        tabChild.removeAllTabs();
                        if (!UtilTools.empty(mTabsData) && mTabsData.size()> 0){
                            for (Map<String,Object> map : mTabsData){
                                tabChild.addTab(tabChild.newTab().setText(map.get("symbol")+""));
                            }


                            symbol = mTabsData.get(tab_POSITION).get("symbol")+"";
                            direction = "0";

                            mPageView.setVisibility(View.VISIBLE);
                            lay.setVisibility(View.GONE);

                            //获取买卖挂单记录
                            getEntrustListAction();

                        }
                        /*switch (tab_POSITION) {
                            case 0:
                                mPageView.setVisibility(View.VISIBLE);
                                lay.setVisibility(View.GONE);
                                initData1();
                                break;
                            case 1:
                                mPageView.setVisibility(View.VISIBLE);
                                lay.setVisibility(View.GONE);
                                initData2();
                                break;
                            case 2:
                                mPageView.setVisibility(View.VISIBLE);
                                lay.setVisibility(View.GONE);
                                initData3();
                                break;
                        }*/

                        break;
                    case 1: //我要卖
                        tabChild.removeAllTabs();
                        if (!UtilTools.empty(mTabsData) && mTabsData.size()> 0){
                            for (Map<String,Object> map : mTabsData){
                                tabChild.addTab(tabChild.newTab().setText(map.get("symbol")+""));
                            }

                            symbol = mTabsData.get(tab_POSITION).get("symbol")+"";
                            direction = "1";

                            mPageView.setVisibility(View.VISIBLE);
                            lay.setVisibility(View.GONE);

                            //获取买卖挂单记录
                            getEntrustListAction();

                        }
                       /* switch (tab_POSITION) {
                            case 0:
                                mPageView.setVisibility(View.VISIBLE);
                                lay.setVisibility(View.GONE);
                                initData4();
                                break;

                            case 1:
                                mPageView.setVisibility(View.VISIBLE);
                                lay.setVisibility(View.GONE);
                                initData5();
                                break;

                            case 2:
                                mPageView.setVisibility(View.VISIBLE);
                                lay.setVisibility(View.GONE);
                                initData6();
                                break;
                        }*/
                        break;
                    case 2: //发布
                        tabChild.removeAllTabs();

                        tabChild1 = tabChild.newTab();
                        tabChild2 = tabChild.newTab();
                        tabChild3 = tabChild.newTab();

                        tabChild1.setText("购买");
                        tabChild2.setText("出售");
                        tabChild3.setText("挂单记录");

                        tabChild.addTab(tabChild1);
                        tabChild.addTab(tabChild2);
                        tabChild.addTab(tabChild3);


                        switch (tab_POSITION) {
                            case 0:
                                mPageView.setVisibility(View.GONE);
                                lay.setVisibility(View.VISIBLE);
                                tvDell.setText("发布购买");
                                tvDell.setBackgroundResource(R.drawable.btn_next_green);
                                break;

                            case 1:
                                mPageView.setVisibility(View.GONE);
                                lay.setVisibility(View.VISIBLE);
                                tvDell.setText("发布出售");
                                tvDell.setBackgroundResource(R.drawable.btn_next_red);
                                break;

                            case 2:
                                mPageView.setVisibility(View.VISIBLE);
                                lay.setVisibility(View.GONE);
                                //获取用户挂单记录
                                getUserTradeList();
                                break;
                        }
                        break;
                }

            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });

        tabChild.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                //mViewPager.setCurrentItem(tab.getPosition());
                tab_POSITION = tab.getPosition();
                switch (TAB_POSITION) {
                    case 0://我要买
                        symbol = mTabsData.get(tab_POSITION).get("symbol")+"";
                        direction = "0";

                        mPageView.setVisibility(View.VISIBLE);
                        lay.setVisibility(View.GONE);

                        //获取买卖挂单记录
                        getEntrustListAction();

                      /*  switch (tab_POSITION) {
                            case 0: //USDT
                                mPageView.setVisibility(View.VISIBLE);
                                lay.setVisibility(View.GONE);
                                initData1();
                                break;
                            case 1: //BTC
                                mPageView.setVisibility(View.VISIBLE);
                                lay.setVisibility(View.GONE);
                                initData2();
                                break;
                            case 2: //ETH
                                mPageView.setVisibility(View.VISIBLE);
                                lay.setVisibility(View.GONE);
                                initData3();
                                break;
                        }*/
                        break;
                    case 1: //我要卖
                        symbol = mTabsData.get(tab_POSITION).get("symbol")+"";
                        direction = "1";

                        mPageView.setVisibility(View.VISIBLE);
                        lay.setVisibility(View.GONE);

                        //获取买卖挂单记录
                        getEntrustListAction();
                       /* switch (tab_POSITION) {
                            case 0: //USDT
                                mPageView.setVisibility(View.VISIBLE);
                                lay.setVisibility(View.GONE);
                                initData4();
                                break;
                            case 1: //BTC
                                mPageView.setVisibility(View.VISIBLE);
                                lay.setVisibility(View.GONE);
                                initData5();
                                break;
                            case 2: //ETH
                                mPageView.setVisibility(View.VISIBLE);
                                lay.setVisibility(View.GONE);
                                initData6();
                                break;
                        }*/
                        break;
                    case 2: //发布
                        switch (tab_POSITION) {
                            case 0: //购买
                                mPageView.setVisibility(View.GONE);
                                lay.setVisibility(View.VISIBLE);
                                tvDell.setText("发布购买");
                                tvDell.setBackgroundResource(R.drawable.btn_next_green);
                                break;
                            case 1: //出售
                                mPageView.setVisibility(View.GONE);
                                lay.setVisibility(View.VISIBLE);
                                tvDell.setText("发布出售");
                                tvDell.setBackgroundResource(R.drawable.btn_next_red);
                                break;
                            case 2: //挂单记录
                                mPageView.setVisibility(View.VISIBLE);
                                lay.setVisibility(View.GONE);
                                //获取用户挂单记录
                                getUserTradeList();
                                break;
                        }
                        break;
                }
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(linearLayoutManager);

        //刷新
        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                switch (mRequestTag){
                    case MethodUrl.FB_ENTRUSTLIST:
                        getEntrustListAction();
                        break;

                    case MethodUrl.USER_ENTRUSTLIST:
                        getUserTradeList();
                        break;
                }

            }
        });


        //加载更多
        mRefreshListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPage = mPage + 1;
                switch (mRequestTag){
                    case MethodUrl.FB_ENTRUSTLIST:
                        getEntrustListAction();
                        break;

                    case MethodUrl.USER_ENTRUSTLIST:
                        getUserTradeList();
                        break;
                }
            }
        });

        setBarTextColor();

        //默认查询最近一周

        mDataType = "1";

        String sTime = UtilTools.getWeekAgo(new Date(), -6);
        String eTime = UtilTools.getStringFromDate(new Date(), "yyyyMMdd");

        mSelectStartTime = sTime;
        mSelectEndTime = eTime;


        List<Map<String,Object>> mDataList2 = SelectDataUtil.getBiType();
        mDialog=new KindSelectDialog(getActivity(),true,mDataList2,30);
        mDialog.setSelectBackListener(this);
    }


    private void get0tcTradeAction() {
        mRequestTag = MethodUrl.OTC_TRADE;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getParentFragment().getActivity(),MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token",MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.OTC_TRADE, map);
    }

    private void  getEntrustListAction() {
        mRequestTag = MethodUrl.FB_ENTRUSTLIST;
        Map<String, Object> map = new HashMap<>();
        map.put("direction",direction);
        map.put("symbol",symbol);
        map.put("page",mPage+"");
        map.put("size","10");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.FB_ENTRUSTLIST, map);
    }


    private void getUserTradeList() {
        mRequestTag = MethodUrl.USER_ENTRUSTLIST;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getActivity(), MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token",MbsConstans.ACCESS_TOKEN);
        map.put("page",mPage+"");
        map.put("size","10");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.USER_ENTRUSTLIST, map);
    }





    @OnClick({R.id.iv_search ,R.id.tv_dell,R.id.bitype_lay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.iv_search:
                intent = new Intent(getParentFragment().getActivity(), DingDanListActivity.class);
                startActivity(intent);
                break;

            case R.id.bitype_lay:
                mDialog.showAtLocation(Gravity.BOTTOM,0,0);
                break;

            case R.id.tv_dell:

                break;


        }

    }

    @Override
    public void showProgress() {
        mLoadingWindow.showView();
    }

    @Override
    public void disimissProgress() {
        mLoadingWindow.cancleView();
    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        mLoadingWindow.cancleView();
        Intent intent;
        switch (mType) {
            case MethodUrl.OTC_TRADE:
                switch (tData.get("code")+""){
                    case "0":
                        Map<String,Object> mapData= (Map<String, Object>) tData.get("data");
                        if (!UtilTools.empty(mapData)){
                           mTabsData = (List<Map<String, Object>>) mapData.get("pair");
                           if (!UtilTools.empty(mTabsData) && mTabsData.size()> 0){
                               for (Map<String,Object> map : mTabsData){
                                   tabChild.addTab(tabChild.newTab().setText(map.get("symbol")+""));
                               }
                               symbol = mTabsData.get(0).get("symbol")+"";
                           }
                        }
                        break;
                    case "1":
                        if (getParentFragment().getActivity() != null){
                            getParentFragment().getActivity().finish();
                        }
                        intent = new Intent(getParentFragment().getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg")+"");
                        break;

                }
                break;
            case MethodUrl.FB_ENTRUSTLIST:
                switch (tData.get("code")+""){
                    case "0":
                        Map<String,Object> mapData= (Map<String, Object>) tData.get("data");
                        if (!UtilTools.empty(mapData)){
                            mDataList = (List<Map<String, Object>>) mapData.get("list");
                            if (mDataList != null && mDataList.size()>0){
                                for (Map<String,Object> map : mDataList){
                                    map.put("direction",direction);
                                    map.put("symbol",symbol);
                                }
                                mPageView.showContent();
                                responseData1();
                                mRefreshListView.refreshComplete(10);
                            }else {
                                mPageView.showEmpty();
                            }
                        }
                        break;
                    case "1":
                        if (getParentFragment().getActivity() != null){
                            getParentFragment().getActivity().finish();
                        }
                        intent = new Intent(getParentFragment().getActivity(), LoginActivity.class);
                        startActivity(intent);

                        break;
                    case "-1":
                        showToastMsg(tData.get("msg")+"");
                        mPageView.showNetworkError();
                        break;

                }
                break;

            case MethodUrl.USER_BUY_DEAL:
                switch (tData.get("code")+""){
                    case "0":
                        showToastMsg(tData.get("msg")+"");
                        switch (TAB_POSITION) {
                            case 0:
                                intent = new Intent(getParentFragment().getActivity(), PayMoneyActivity.class);
                                intent.putExtra("kind", "0"); //购买
                                intent.putExtra("id",tData.get("data")+"");//订单ID
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(getParentFragment().getActivity(), PayMoneyActivity.class);
                                intent.putExtra("kind", "1"); //出售
                                intent.putExtra("id",tData.get("data")+"");//订单ID
                                startActivity(intent);
                                break;
                            case 2:

                                break;
                        }
                        break;
                    case "1":
                        if (getParentFragment().getActivity() != null){
                            getParentFragment().getActivity().finish();
                        }
                        intent = new Intent(getParentFragment().getActivity(), LoginActivity.class);
                        startActivity(intent);

                        break;
                    case "-1":
                        showToastMsg(tData.get("msg")+"");
                        break;

                }
                break;

            case MethodUrl.USER_ENTRUSTLIST:
                switch (tData.get("code")+""){
                    case "0":
                        Map<String,Object> mapData= (Map<String, Object>) tData.get("data");
                        if (!UtilTools.empty(mapData)){
                            mDataList = (List<Map<String, Object>>) mapData.get("list");
                            if (mDataList != null && mDataList.size()>0){
                               /* for (Map<String,Object> map : mDataList){
                                    map.put("direction",direction);
                                    map.put("symbol",symbol);
                                }*/
                                mPageView.showContent();
                                responseData7();
                                mRefreshListView.refreshComplete(10);
                            }else {
                                mPageView.showEmpty();
                            }
                        }
                        break;
                    case "1":
                        if (getParentFragment().getActivity() != null){
                            getParentFragment().getActivity().finish();
                        }
                        intent = new Intent(getParentFragment().getActivity(), LoginActivity.class);
                        startActivity(intent);

                        break;
                    case "-1":
                        showToastMsg(tData.get("msg")+"");
                        mPageView.showNetworkError();
                        break;

                }
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.shoumoneyList:
                        //getShouMoneyInfoList();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        switch (mType) {
            case MethodUrl.borrowList:
                if (mBuyAdapter1 != null) {
                    if (mBuyAdapter1.getDataList().size() <= 0) {
                        mPageView.showNetworkError();
                    } else {
                        mPageView.showContent();
                    }
                    mRefreshListView.refreshComplete(10);
                    mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {

                        }
                    });
                } else {
                    mPageView.showNetworkError();
                }

                break;
        }


        mLoadingWindow.cancleView();
        dealFailInfo(map, mType);
    }

    @Override
    public void reLoadingData() {
        switch (mRequestTag){
            case MethodUrl.FB_ENTRUSTLIST:
                getEntrustListAction();
                break;

            case MethodUrl.USER_ENTRUSTLIST:
                getUserTradeList();
                break;
        }
        mLoadingWindow.showView();



    }


    public void setBarTextColor() {
        StatusBarUtil.setLightMode(getActivity());
    }


    private void responseData1() {
        if (mBuyAdapter1 == null) {
            mBuyAdapter1 = new FBTradeListAdapter(getActivity());
            mBuyAdapter1.addAll(mDataList);

            AnimationAdapter adapter1 = new ScaleInAnimationAdapter(mBuyAdapter1);
            adapter1.setFirstOnly(false);
            adapter1.setDuration(400);
            adapter1.setInterpolator(new OvershootInterpolator(0.8f));


            AnimationAdapter adapter = new AlphaInAnimationAdapter(adapter1);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(1f));


            mLRecyclerViewAdapter1 = new LRecyclerViewAdapter(adapter);
            //SampleHeader headerView = new SampleHeader(getActivity(), R.layout.fragment_home_head_view);
            //mLRecyclerViewAdapter1.addHeaderView(headerView);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter1);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            //int spacing = getResources().getDimensionPixelSize(R.dimen.divide_hight);
            //mRefreshListView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, gridLayoutManager.getSpanCount(), Color.GRAY));
            //根据需要选择使用GridItemDecoration还是SpacesItemDecoration
            GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                    .setHorizontal(R.dimen.divide_hight)
                    .setVertical(R.dimen.divide_hight)
                    .setColorResource(R.color.divide_line)
                    .build();
            //mRefreshListView.addItemDecoration(divider);

//            DividerDecoration divider2 = new DividerDecoration.Builder(getActivity())
//                    .setHeight(R.dimen.dp_10)
//                    .setPadding(R.dimen.dp_10)
//                    .setColorResource(R.color.body_bg)
//                    .build();
//            mRefreshListView.addItemDecoration(divider2);
        } else {
           /* if (mPage == 1) {
                mRepaymentAdapter.clear();
            }*/
//            mBuyAdapter1.clear();
//            mBuyAdapter1.addAll(mDataList);
//            mBuyAdapter1.notifyDataSetChanged();
//            mLRecyclerViewAdapter1.notifyDataSetChanged();//必须调用此方法

            mBuyAdapter1.clear();
            mBuyAdapter1.addAll(mDataList);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter1);

        }

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
        }

        if (mBuyAdapter1.getDataList().size() <= 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }

        mBuyAdapter1.setmCallBack(new OnChildClickListener() {
            @Override
            public void onChildClickListener(View view, int position, Map<String, Object> mParentMap) {
                id = mParentMap.get("id")+"";
                if (direction.equals("0")){
                    showDialog("购买 "+mParentMap.get("symbol"),
                            mParentMap.get("price")+"", "交易数量 0 USDT",
                            "按价格购买", "按数量购买");
                }else {
                    showDialog("出售 "+mParentMap.get("symbol"),
                            mParentMap.get("price")+"", "交易数量 0 USDT",
                            "按价格出售", "按数量出售");
                }

            }
        });
    }


    /*private void responseData2() {
        if (mBuyAdapter2 == null) {
            mBuyAdapter2 = new FBTradeListAdapter(getActivity());
            mBuyAdapter2.addAll(mDataList);

            AnimationAdapter adapter1 = new ScaleInAnimationAdapter(mBuyAdapter2);
            adapter1.setFirstOnly(false);
            adapter1.setDuration(400);
            adapter1.setInterpolator(new OvershootInterpolator(0.8f));


            AnimationAdapter adapter = new AlphaInAnimationAdapter(adapter1);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(1f));


            mLRecyclerViewAdapter2 = new LRecyclerViewAdapter(adapter);
            //SampleHeader headerView = new SampleHeader(getActivity(), R.layout.fragment_home_head_view);
            //mLRecyclerViewAdapter1.addHeaderView(headerView);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter2);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            //int spacing = getResources().getDimensionPixelSize(R.dimen.divide_hight);
            //mRefreshListView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, gridLayoutManager.getSpanCount(), Color.GRAY));
            //根据需要选择使用GridItemDecoration还是SpacesItemDecoration
            GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                    .setHorizontal(R.dimen.divide_hight)
                    .setVertical(R.dimen.divide_hight)
                    .setColorResource(R.color.divide_line)
                    .build();
            //mRefreshListView.addItemDecoration(divider);

//            DividerDecoration divider2 = new DividerDecoration.Builder(getActivity())
//                    .setHeight(R.dimen.dp_10)
//                    .setPadding(R.dimen.dp_10)
//                    .setColorResource(R.color.body_bg)
//                    .build();
//            mRefreshListView.addItemDecoration(divider2);

        } else {
           *//* if (mPage == 1) {
                mRepaymentAdapter.clear();
            }*//*
//            mBuyAdapter1.clear();
//            mBuyAdapter1.addAll(mDataList);
//            mBuyAdapter1.notifyDataSetChanged();
//            mLRecyclerViewAdapter1.notifyDataSetChanged();//必须调用此方法

            mBuyAdapter2.clear();
            mBuyAdapter2.addAll(mDataList);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter2);

        }

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
        }

        if (mBuyAdapter2.getDataList().size() <= 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }

        mBuyAdapter2.setmCallBack(new OnChildClickListener() {
            @Override
            public void onChildClickListener(View view, int position, Map<String, Object> mParentMap) {
                showDialog("购买 BTC", "￥ 90.00", "交易数量 900.00 USDT", "按价格购买", "按数量购买");
            }
        });

    }


    private void responseData3() {
        if (mBuyAdapter3 == null) {
            mBuyAdapter3 = new FBTradeListAdapter(getActivity());
            mBuyAdapter3.addAll(mDataList);

            AnimationAdapter adapter1 = new ScaleInAnimationAdapter(mBuyAdapter3);
            adapter1.setFirstOnly(false);
            adapter1.setDuration(400);
            adapter1.setInterpolator(new OvershootInterpolator(0.8f));


            AnimationAdapter adapter = new AlphaInAnimationAdapter(adapter1);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(1f));


            mLRecyclerViewAdapter3 = new LRecyclerViewAdapter(adapter);
            //SampleHeader headerView = new SampleHeader(getActivity(), R.layout.fragment_home_head_view);
            //mLRecyclerViewAdapter1.addHeaderView(headerView);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter3);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            //int spacing = getResources().getDimensionPixelSize(R.dimen.divide_hight);
            //mRefreshListView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, gridLayoutManager.getSpanCount(), Color.GRAY));
            //根据需要选择使用GridItemDecoration还是SpacesItemDecoration
            GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                    .setHorizontal(R.dimen.divide_hight)
                    .setVertical(R.dimen.divide_hight)
                    .setColorResource(R.color.divide_line)
                    .build();
            //mRefreshListView.addItemDecoration(divider);

//            DividerDecoration divider2 = new DividerDecoration.Builder(getActivity())
//                    .setHeight(R.dimen.dp_10)
//                    .setPadding(R.dimen.dp_10)
//                    .setColorResource(R.color.body_bg)
//                    .build();
//            mRefreshListView.addItemDecoration(divider2);


        } else {
           *//* if (mPage == 1) {
                mRepaymentAdapter.clear();
            }*//*
//            mBuyAdapter1.clear();
//            mBuyAdapter1.addAll(mDataList);
//            mBuyAdapter1.notifyDataSetChanged();
//            mLRecyclerViewAdapter1.notifyDataSetChanged();//必须调用此方法

            mBuyAdapter3.clear();
            mBuyAdapter3.addAll(mDataList);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter3);

        }

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
        }

        if (mBuyAdapter3.getDataList().size() <= 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }

        mBuyAdapter3.setmCallBack(new OnChildClickListener() {
            @Override
            public void onChildClickListener(View view, int position, Map<String, Object> mParentMap) {
                showDialog("购买 ETH", "￥ 90.00", "交易数量 900.00 USDT", "按价格购买", "按数量购买");
            }
        });

    }

    private void responseData4() {
        if (mBuyAdapter4 == null) {
            mBuyAdapter4 = new FBTradeListAdapter(getActivity());
            mBuyAdapter4.addAll(mDataList);

            AnimationAdapter adapter1 = new ScaleInAnimationAdapter(mBuyAdapter4);
            adapter1.setFirstOnly(false);
            adapter1.setDuration(400);
            adapter1.setInterpolator(new OvershootInterpolator(0.8f));


            AnimationAdapter adapter = new AlphaInAnimationAdapter(adapter1);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(1f));


            mLRecyclerViewAdapter4 = new LRecyclerViewAdapter(adapter);
            //SampleHeader headerView = new SampleHeader(getActivity(), R.layout.fragment_home_head_view);
            //mLRecyclerViewAdapter1.addHeaderView(headerView);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter4);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            //int spacing = getResources().getDimensionPixelSize(R.dimen.divide_hight);
            //mRefreshListView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, gridLayoutManager.getSpanCount(), Color.GRAY));
            //根据需要选择使用GridItemDecoration还是SpacesItemDecoration
            GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                    .setHorizontal(R.dimen.divide_hight)
                    .setVertical(R.dimen.divide_hight)
                    .setColorResource(R.color.divide_line)
                    .build();
            //mRefreshListView.addItemDecoration(divider);

//            DividerDecoration divider2 = new DividerDecoration.Builder(getActivity())
//                    .setHeight(R.dimen.dp_10)
//                    .setPadding(R.dimen.dp_10)
//                    .setColorResource(R.color.body_bg)
//                    .build();
//            mRefreshListView.addItemDecoration(divider2);


        } else {
           *//* if (mPage == 1) {
                mRepaymentAdapter.clear();
            }*//*
//            mBuyAdapter1.clear();
//            mBuyAdapter1.addAll(mDataList);
//            mBuyAdapter1.notifyDataSetChanged();
//            mLRecyclerViewAdapter1.notifyDataSetChanged();//必须调用此方法

            mBuyAdapter4.clear();
            mBuyAdapter4.addAll(mDataList);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter4);

        }

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
        }

        if (mBuyAdapter4.getDataList().size() <= 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }

        mBuyAdapter4.setmCallBack(new OnChildClickListener() {
            @Override
            public void onChildClickListener(View view, int position, Map<String, Object> mParentMap) {
                showDialog("出售 USDT", "￥ 90.00", "交易数量 900.00 USDT", "按价格出售", "按数量出售");
            }
        });

    }


    private void responseData5() {
        if (mBuyAdapter5 == null) {
            mBuyAdapter5 = new FBTradeListAdapter(getActivity());
            mBuyAdapter5.addAll(mDataList);

            AnimationAdapter adapter1 = new ScaleInAnimationAdapter(mBuyAdapter5);
            adapter1.setFirstOnly(false);
            adapter1.setDuration(400);
            adapter1.setInterpolator(new OvershootInterpolator(0.8f));


            AnimationAdapter adapter = new AlphaInAnimationAdapter(adapter1);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(1f));


            mLRecyclerViewAdapter5 = new LRecyclerViewAdapter(adapter);
            //SampleHeader headerView = new SampleHeader(getActivity(), R.layout.fragment_home_head_view);
            //mLRecyclerViewAdapter1.addHeaderView(headerView);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter5);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            //int spacing = getResources().getDimensionPixelSize(R.dimen.divide_hight);
            //mRefreshListView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, gridLayoutManager.getSpanCount(), Color.GRAY));
            //根据需要选择使用GridItemDecoration还是SpacesItemDecoration
            GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                    .setHorizontal(R.dimen.divide_hight)
                    .setVertical(R.dimen.divide_hight)
                    .setColorResource(R.color.divide_line)
                    .build();
            //mRefreshListView.addItemDecoration(divider);

//            DividerDecoration divider2 = new DividerDecoration.Builder(getActivity())
//                    .setHeight(R.dimen.dp_10)
//                    .setPadding(R.dimen.dp_10)
//                    .setColorResource(R.color.body_bg)
//                    .build();
//            mRefreshListView.addItemDecoration(divider2);

        } else {
           *//* if (mPage == 1) {
                mRepaymentAdapter.clear();
            }*//*
//            mBuyAdapter1.clear();
//            mBuyAdapter1.addAll(mDataList);
//            mBuyAdapter1.notifyDataSetChanged();
//            mLRecyclerViewAdapter1.notifyDataSetChanged();//必须调用此方法

            mBuyAdapter5.clear();
            mBuyAdapter5.addAll(mDataList);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter5);

        }

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
        }

        if (mBuyAdapter5.getDataList().size() <= 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }

        mBuyAdapter5.setmCallBack(new OnChildClickListener() {
            @Override
            public void onChildClickListener(View view, int position, Map<String, Object> mParentMap) {
                showDialog("出售 ETC", "￥ 90.00", "交易数量 900.00 USDT", "按价格出售", "按数量出售");
            }
        });

    }


    private void responseData6() {
        if (mBuyAdapter6 == null) {
            mBuyAdapter6 = new FBTradeListAdapter(getActivity());
            mBuyAdapter6.addAll(mDataList);

            AnimationAdapter adapter1 = new ScaleInAnimationAdapter(mBuyAdapter6);
            adapter1.setFirstOnly(false);
            adapter1.setDuration(400);
            adapter1.setInterpolator(new OvershootInterpolator(0.8f));


            AnimationAdapter adapter = new AlphaInAnimationAdapter(adapter1);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(1f));


            mLRecyclerViewAdapter6 = new LRecyclerViewAdapter(adapter);
            //SampleHeader headerView = new SampleHeader(getActivity(), R.layout.fragment_home_head_view);
            //mLRecyclerViewAdapter1.addHeaderView(headerView);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter6);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            //int spacing = getResources().getDimensionPixelSize(R.dimen.divide_hight);
            //mRefreshListView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, gridLayoutManager.getSpanCount(), Color.GRAY));
            //根据需要选择使用GridItemDecoration还是SpacesItemDecoration
            GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                    .setHorizontal(R.dimen.divide_hight)
                    .setVertical(R.dimen.divide_hight)
                    .setColorResource(R.color.divide_line)
                    .build();
            //mRefreshListView.addItemDecoration(divider);

//            DividerDecoration divider2 = new DividerDecoration.Builder(getActivity())
//                    .setHeight(R.dimen.dp_10)
//                    .setPadding(R.dimen.dp_10)
//                    .setColorResource(R.color.body_bg)
//                    .build();
//            mRefreshListView.addItemDecoration(divider2);
        } else {
           *//* if (mPage == 1) {
                mRepaymentAdapter.clear();
            }*//*
//            mBuyAdapter1.clear();
//            mBuyAdapter1.addAll(mDataList);
//            mBuyAdapter1.notifyDataSetChanged();
//            mLRecyclerViewAdapter1.notifyDataSetChanged();//必须调用此方法

            mBuyAdapter6.clear();
            mBuyAdapter6.addAll(mDataList);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter6);

        }

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
        }

        if (mBuyAdapter6.getDataList().size() <= 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }


        mBuyAdapter6.setmCallBack(new OnChildClickListener() {
            @Override
            public void onChildClickListener(View view, int position, Map<String, Object> mParentMap) {
                showDialog("出售 ETH", "￥ 90.00", "交易数量 900.00 USDT", "按价格出售", "按数量出售");
            }
        });

    }*/

    private void responseData7() {
        if (mdingdanAdapter == null) {
            mdingdanAdapter = new DingDanListAdapter(getActivity());
            mdingdanAdapter.addAll(mDataList);

            AnimationAdapter adapter1 = new ScaleInAnimationAdapter(mdingdanAdapter);
            adapter1.setFirstOnly(false);
            adapter1.setDuration(400);
            adapter1.setInterpolator(new OvershootInterpolator(0.8f));


            AnimationAdapter adapter = new AlphaInAnimationAdapter(adapter1);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(1f));


            mLRecyclerViewAdapter7 = new LRecyclerViewAdapter(adapter);
            //SampleHeader headerView = new SampleHeader(getActivity(), R.layout.fragment_home_head_view);
            //mLRecyclerViewAdapter1.addHeaderView(headerView);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter7);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            //int spacing = getResources().getDimensionPixelSize(R.dimen.divide_hight);
            //mRefreshListView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, gridLayoutManager.getSpanCount(), Color.GRAY));
            //根据需要选择使用GridItemDecoration还是SpacesItemDecoration
            GridItemDecoration divider = new GridItemDecoration.Builder(getActivity())
                    .setHorizontal(R.dimen.divide_hight)
                    .setVertical(R.dimen.divide_hight)
                    .setColorResource(R.color.divide_line)
                    .build();
            //mRefreshListView.addItemDecoration(divider);

//            DividerDecoration divider2 = new DividerDecoration.Builder(getActivity())
//                    .setHeight(R.dimen.dp_10)
//                    .setPadding(R.dimen.dp_10)
//                    .setColorResource(R.color.body_bg)
//                    .build();
//            mRefreshListView.addItemDecoration(divider2);
        } else {
           /* if (mPage == 1) {
                mRepaymentAdapter.clear();
            }*/
//            mBuyAdapter1.clear();
//            mBuyAdapter1.addAll(mDataList);
//            mBuyAdapter1.notifyDataSetChanged();
//            mLRecyclerViewAdapter1.notifyDataSetChanged();//必须调用此方法

            mdingdanAdapter.clear();
            mdingdanAdapter.addAll(mDataList);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter7);

        }

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
        }

        if (mdingdanAdapter.getDataList().size() <= 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }

    }




    private View popView;
    private PopupWindow mConditionDialog;
    private boolean bright = false;

    private TextView tvTitle;
    private TextView tvPrice;
    private TextView tvType;
    private TextView tvBuy;
    private TextView tvNumber;
    private TextView tvMoney;
    private TextView tvCancel;
    private TextView tvOrder;
    private XTabLayout diolog_tabLayout;
    private EditText etNumber;

    private int tradeType = 0; //0 价格购买  1 数量购买
    private String tradePrice = "";
    private String tradeNumber = "";


    public void showDialog(String type, String price, String number, String tab1, String tab2) {
        initPopupWindow(type, price, number, tab1, tab2);
    }

    private void initPopupWindow(String type, String price, String number, String tab1, String tab2) {//
        int nH = UtilTools.getNavigationBarHeight(getActivity());
        LinearLayout mNagView;
        if (mConditionDialog == null) {
            popView = LayoutInflater.from(getActivity()).inflate(R.layout.dialong_trade_layout, null);
            mConditionDialog = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //mConditionDialog.setClippingEnabled(false);
            initConditionDialog(popView);
            mConditionDialog.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), UtilTools.dip2px(getParentFragment().getActivity(), 5)));
            mConditionDialog.setOutsideTouchable(false);// 设置可允许在外点击消失
            mConditionDialog.update();
            mConditionDialog.setTouchable(true);
            mConditionDialog.setFocusable(true);
            mConditionDialog.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            mConditionDialog.showAtLocation(getParentFragment().getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
            initViewData(type, price, number, tab1, tab2);
            toggleBright();
            mConditionDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    toggleBright();
                }
            });
        } else {
            mConditionDialog.showAtLocation(getParentFragment().getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
            initViewData(type, price, number, tab1, tab2);
            toggleBright();
        }
    }


    private void toggleBright() {
        //三个参数分别为： 起始值 结束值 时长 那么整个动画回调过来的值就是从0.5f--1f的
        mAnimUtil.setValueAnimator(0.7f, 1f, 500);
        mAnimUtil.addUpdateListener(new AnimUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                //此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                float bgAlpha = bright ? progress : (1.7f - progress);//三目运算，应该挺好懂的。
                //bgAlpha = progress;//三目运算，应该挺好懂的。
                bgAlpha(bgAlpha);//在此处改变背景，这样就不用通过Handler去刷新了。
            }
        });
        mAnimUtil.addEndListner(new AnimUtil.EndListener() {
            @Override
            public void endUpdate(Animator animator) {
                //在一次动画结束的时候，翻转状态
                bright = !bright;
            }
        });
        mAnimUtil.startAnimator();
    }

    private void bgAlpha(float alpha) {
        WindowManager.LayoutParams lp = ((Activity) getActivity()).getWindow().getAttributes();
        lp.alpha = alpha;// 0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }


    private void initConditionDialog(View view) {

        tvTitle = view.findViewById(R.id.title_tv);
        tvPrice = view.findViewById(R.id.price_tv);
        tvType = view.findViewById(R.id.type_tv);
        tvBuy = view.findViewById(R.id.buy_tv);
        tvNumber = view.findViewById(R.id.number_tv);
        tvMoney = view.findViewById(R.id.money_tv);
        tvCancel = view.findViewById(R.id.cancel);
        tvOrder = view.findViewById(R.id.order);
        diolog_tabLayout = view.findViewById(R.id.tab_child);
        etNumber = view.findViewById(R.id.etNumber);

        tabDialog1 = diolog_tabLayout.newTab();
        tabDialog2 = diolog_tabLayout.newTab();

        diolog_tabLayout.addTab(tabDialog1);
        diolog_tabLayout.addTab(tabDialog2);

        diolog_tabLayout.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                etNumber.setText("");
                etNumber.setHint("请输入");
                tvNumber.setText("交易数量 0 USDT");
                tvMoney.setText("¥ 0.00");
                tradeType = tab.getPosition();
                switch (tab.getPosition()) {
                    case 0:
                        tvType.setText("CNY");
                        break;
                    case 1:
                        tvType.setText("USDT");
                        break;
                }
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });


        //取消
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConditionDialog.dismiss();
            }
        });

        //下单
        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //买卖下单操作
                postDingdanAction();
            }
        });

        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0){
                    switch (tradeType){
                        case 0: //按价格够买
                            tradePrice = s.toString();

                            tradeNumber = Float.parseFloat(tradePrice)/Float.parseFloat(tvPrice.getText().toString().replaceAll(",","").trim())+"";
                            tvNumber.setText("交易数量 "+UtilTools.getNormalMoney(tradeNumber)+" USDT");
                            tvMoney.setText(UtilTools.getRMBMoney(tradePrice));

                            break;
                        case 1:  //按数量够买
                            tradeNumber = s.toString();
                            tradePrice = Float.parseFloat(tradeNumber)*Float.parseFloat(tvPrice.getText().toString().replaceAll(",","").trim())+"";
                            tvNumber.setText("交易数量 "+UtilTools.getNormalMoney(tradeNumber)+" USDT");
                            tvMoney.setText(UtilTools.getRMBMoney(tradePrice));

                            break;

                    }
                }else {
                    etNumber.setHint("请输入");
                    tvNumber.setText("交易数量 0 USDT");
                    tvMoney.setText("¥ 0.00");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    private void postDingdanAction() {
        mRequestTag = MethodUrl.USER_BUY_DEAL;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getActivity(), MbsConstans.SharedInfoConstans.ACCESS_TOKEN,"").toString();
        }
        //map.put("token","a147ff5721babca2e7c7b976023af933");
        map.put("token",MbsConstans.ACCESS_TOKEN);
        map.put("id", id);
        map.put("number", tradeNumber);
        //传参交易密码
        String paycode = "";
        if (!UtilTools.empty(MbsConstans.PAY_CODE)){
            paycode = MbsConstans.PAY_CODE;
        }else {
            paycode = SPUtils.get(getParentFragment().getActivity(), MbsConstans.SharedInfoConstans.PAY_CODE, "").toString();
        }

        map.put("payment_password",paycode);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.USER_BUY_DEAL, map);
    }

    public void initViewData(String type, String price, String number, String tab1, String tab2) {
        tvTitle.setText(type);
        tvPrice.setText(UtilTools.getNormalMoney(price));
        tvNumber.setText(number);
        tabDialog1.setText(tab1);
        tabDialog2.setText(tab2);
        switch (TAB_POSITION) {
            case 0:
                tvBuy.setText("买入");
                break;
            case 1:
                tvBuy.setText("出售");
                break;
        }

        etNumber.setText("");
        etNumber.setHint("请输入");
        tvNumber.setText("交易数量 0 USDT");
        tvMoney.setText("¥ 0.00");
    }


    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 21:
                mSelectStartTime = map.get("date") + "";
                String startShow = UtilTools.getStringFromSting2(mSelectStartTime, "yyyyMMdd", "yyyy-MM-dd");

                break;
            case 22:
                mSelectEndTime = map.get("date") + "";
                String endShow = UtilTools.getStringFromSting2(mSelectEndTime, "yyyyMMdd", "yyyy-MM-dd");
                break;

            case 30:
                String str= (String) map.get("name");
                BiTypeTv.setText(str);
                break;

        }
    }

}
