package com.lr.biyou.ui.temporary.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.ItemDecoration.GridItemDecoration;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.ui.temporary.adapter.HeTongSelectAdapter;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.ParseTextUtil;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 选择合同
 */
public class HeTongSelectActivity extends BasicActivity implements RequestView, ReLoadingData {

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.right_text_tv)
    TextView mRightTextTv;
    @BindView(R.id.right_img)
    ImageView mRightImg;
    @BindView(R.id.right_lay)
    LinearLayout mRightLay;
    @BindView(R.id.divide_line)
    View mDivideLine;
    @BindView(R.id.top_layout)
    LinearLayout mTitleBarView;
    @BindView(R.id.refresh_list_view)
    LRecyclerView mRefreshListView;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.all_money_tv)
    TextView mAllMoneyTv;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private HeTongSelectAdapter mAdapter;
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private List<Map<String,Object>> mBooleanList = new ArrayList<>();
    private Map<String,Object> mSelectMap = new HashMap<>();


    //选择合同下的凭证列表信息
    private List<Map<String,Object>> mPingZhengList = new ArrayList<>();
    private List<Map<String,Object>> mSelectPingZhengList = new ArrayList<>();
    private List<Map<String, Object>> mBooleanPingZhengList = new ArrayList<>();

    private Map<String, Object> mHeTongMap = new HashMap<>();


    private String mRequestTag = "";
    private String mPayCompayName = "";
    private String mPaycustid = "";

    private Map<String,Object> mSxMap = new HashMap<>();


    @Override
    public int getContentView() {
        return R.layout.activity_select_hetong;
    }


    private String mResponseType = "";
    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mTitleText.setText(getResources().getString(R.string.hetong_title));


        IntentFilter filter = new IntentFilter();
        filter.addAction(MbsConstans.BroadcastReceiverAction.HTONGUPDATE);
        registerReceiver(receiver, filter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mPayCompayName = bundle.getString("payfirmname");
            mPaycustid = bundle.getString("paycustid");
            mSxMap =(Map<String, Object>) bundle.getSerializable("DATA");
        }


        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(linearLayoutManager);

        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                htList();

            }
        });

        mRefreshListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //payHistoryAction();
            }
        });

        mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
            @Override
            public void reload() {
            }
        });

        showProgressDialog();
        htList();
    }
    private void htList() {
        mRequestTag = MethodUrl.hetongInfo;
        Map<String, String> map = new HashMap<>();
        map.put("paycustid",mPaycustid);
        map.put("payfirmname",mPayCompayName);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.hetongInfo, map);
    }

    private void ruchiAction() {
        //List<Map<String,Object>> mapList = (List<Map<String, Object>>) DataHolder.getInstance().retrieve("moneySelect");
        List<Map<String,Object>> list = new ArrayList<>();
        for (Map mm:mSelectPingZhengList){
            Map<String,Object> moneyMap = new HashMap<>();
            moneyMap.put("flowdate",mm.get("flowdate")+"");
            moneyMap.put("flowid",mm.get("flowid")+"");
            moneyMap.put("sgndt",mm.get("sgndt")+"");

            //入池状态：0：未入池  2：已入池
            String status = mm.get("poolsta")+"";
            if (status.equals("0")){
                list.add(moneyMap);
            }
        }
        mRequestTag = MethodUrl.ruchiAction;
        Map<String, Object> map = new HashMap<>();
        map.put("contno",mHeTongMap.get("contno")+"");//贸易合同编号
        map.put("settid",mHeTongMap.get("settid")+"");//贸易合同业务主键
        map.put("receivables",list);//应收账款列表
        map.put("vchtrdtype","0"); //账款类型(0:应收账款 1:发票 )
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        LogUtilDebug.i("入池操作传递的参数",map);
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.ruchiAction, map);
    }


    private void yszkList() {
        mRequestTag = MethodUrl.yszkList;
        Map<String, String> map = new HashMap<>();
        map.put("flowdate",mSxMap.get("flowdate")+"");
        map.put("flowid",mSxMap.get("flowid")+"");
        map.put("autoid",mSxMap.get("autoid")+"");
        map.put("settid",mHeTongMap.get("settid")+"");
        map.put("contno",mHeTongMap.get("contno")+"");
        map.put("paycustid",mPaycustid);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.yszkList, map);
    }

    private void initData() {

        String mm = "合计:500000元";

        ParseTextUtil parseTextUtil = new ParseTextUtil(this);

        SpannableString ms = parseTextUtil.getTextSpan(mm,":");
        mAllMoneyTv.setText(ms);
    }


    @Override
    public void reLoadingData() {
        showProgress();
        htList();
    }

    /**
     * @descriptoin 请求前加载progress
     * @author dc
     * @date 2017/2/16 11:00
     */
    @Override
    public void showProgress() {

    }

    /**
     * @descriptoin 请求结束之后隐藏progress
     * @author dc
     * @date 2017/2/16 11:01
     */
    @Override
    public void disimissProgress() {
        dismissProgressDialog();
    }

    /**
     * @param tData 数据类型
     * @param mType
     * @descriptoin 请求数据成功
     * @author dc
     * @date 2017/2/16 11:01
     */
    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        Intent intent;
        switch (mType){
            case MethodUrl.ruchiAction: //申请入池

                showToastMsg(tData.get("result")+"");
                mBtnNext.setEnabled(true);

                backTo(BorrowMoneyActivity.class,true);
                /*intent = new Intent(HeTongSelectActivity.this, BorrowMoneyActivity.class);
                intent.putExtra("DATA",(Serializable)mSxMap);
                startActivity(intent);*/
                break;
            case MethodUrl.hetongInfo: //合同列表
                List<Map<String,Object>> list = (List<Map<String, Object>>) tData.get("tradecontList");

                if (list != null) {
                    mDataList.clear();
                    mDataList.addAll(list);
                }
                mResponseType = "1";
                responseData();
                mRefreshListView.refreshComplete(10);

                break;

            case MethodUrl.yszkList: //应收账款列表
                List<Map<String,Object>> listData  =  (List<Map<String, Object>>) tData.get("yszkInfoList");
               /* Map<String,Object> map = new HashMap<>();
                map.put("flowdate","20190511");
                map.put("poolsta","0");
                map.put("paymoney","10000");
                map.put("billid","201596276");
                map.put("paycustid","1905713000000191");
                map.put("payfirmname","测试");
                map.put("flowid","22");
                map.put("paydate","20190811");
                map.put("sgndt","20190511");
                listData.add(map);


                Map<String,Object> map1 = new HashMap<>();
                map1.put("flowdate","20190511");
                map1.put("poolsta","0");
                map1.put("paymoney","10000");
                map1.put("billid","201596276");
                map1.put("paycustid","1905713000000191");
                map1.put("payfirmname","测试222");
                map1.put("flowid","22");
                map1.put("paydate","20190811");
                map1.put("sgndt","20190511");
                listData.add(map1);*/
                if (listData != null){
                    mPingZhengList.clear();
                    mPingZhengList.addAll(listData);
                }
                mResponseType = "2";
                responseData();
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                showProgressDialog();
                switch (mRequestTag) {
                    case MethodUrl.hetongInfo:
                        htList();
                        break;
                    case MethodUrl.ruchiAction:
                        ruchiAction();
                        break;
                    case MethodUrl.yszkList:
                        yszkList();
                        break;
                }
                break;
        }
    }

    /**
     * @param map
     * @param mType
     * @descriptoin 请求数据错误
     * @date 2017/2/16 11:01
     */
    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        switch (mType) {
            case MethodUrl.ruchiAction:
                mBtnNext.setEnabled(true);
            case MethodUrl.hetongInfo://
                if(mAdapter!=null){
                    if(mDataList.size()<=0){
                        mPageView.showNetworkError();
                    }else {
                        mPageView.showContent();
                    }
                    mRefreshListView.refreshComplete(10);
                    mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            showProgressDialog();
                            htList();
                        }
                    });

                }else {
                    mPageView.showNetworkError();
                }
                break;
        }
        dealFailInfo(map,mType);
    }


    private void responseData() {
        LogUtilDebug.i("show","mPingZheng:"+mPingZhengList.size());

        if (mBooleanList != null) {
            mBooleanList.clear();
        }

        if (mResponseType.equals("2")){ //请求凭证列表
            for (Map m: mDataList){
                Map<String,Object> map = new HashMap<>();
                map.put("value",m);
                if (m.equals(mHeTongMap)){
                    map.put("list",mPingZhengList);
                    map.put("selected",true);
                }else {
                    List<Map<String,Object>> mPZList = new ArrayList<>();
                    map.put("selected",false);
                    map.put("list",mPZList);
                }

                mBooleanList.add(map);
            }
        }else {

            for (Map m: mDataList){
                Map<String,Object> map = new HashMap<>();
                map.put("value",m);
                List<Map<String,Object>> mPZList = new ArrayList<>();
                map.put("selected",false);
                map.put("list",mPZList);

                mBooleanList.add(map);
            }
        }



        if (mAdapter == null) {
            mAdapter = new HeTongSelectAdapter(this, mDataList, new HeTongSelectAdapter.OnChangeBankCardListener() {
                @Override
                public void onButClickListener(String type, Map<String, Object> map) {
                    mRefreshListView.smoothScrollToPosition(0);
                    Intent intent;
                    switch (type){
                        case "1":
                            intent = new Intent(HeTongSelectActivity.this,HeTongAddActivity.class);
                            intent.putExtra("payfirmname",mPayCompayName);
                            intent.putExtra("paycustid",mPaycustid);
                            startActivity(intent);
                            break;
                        case "2":
                            //请求当前合同下应收账款列表
                            mHeTongMap = map;
                            showProgressDialog();
                            yszkList();
                            List<Map<String, Object>> list = mAdapter.getBooleanList();
                            for (Map mm : list) {
                                boolean b = (Boolean) mm.get("selected");
                                mSelectMap = (Map<String, Object>) mm.get("value");
                            }

                            mRefreshListView.post(new Runnable(){
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                    mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
                                }
                            });

                           /* intent = new Intent(HeTongSelectActivity.this, BorrowMoneyActivity.class);
                            intent.putExtra("payfirmname",mPayCompayName);
                            intent.putExtra("paycustid",mPaycustid);
                            startActivity(intent);*/
                            break;
                    }
                }
            });

            mAdapter.setBooleanList(mBooleanList);
            View view = LayoutInflater.from(this).inflate(R.layout.item_hetong_add, mRefreshListView, false);
            //View view = LayoutInflater.from(this).inflate(R.layout.item_bank_bind, null);

            mAdapter.addFooterView(view);

            mAdapter.setClickListener(new HeTongSelectAdapter.OnItemClickListener() {
                @Override
                public void OnMyItemClickListener(List<Map<String, Object>> list,Map<String,Object> mParentMap) {
                    mBooleanPingZhengList = list;
                    mHeTongMap = mParentMap;
                }
            });

            /*mAdapter.setBooleanList(mBooleanList);
            mAdapter.addAll(mDataList);*/

            mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdapter);
            //SampleHeader headerView = new SampleHeader(getActivity(), R.layout.fragment_home_head_view);
            //mLRecyclerViewAdapter.addHeaderView(headerView);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            mRefreshListView.setFocusableInTouchMode(false);

            //int spacing = getResources().getDimensionPixelSize(R.dimen.divide_hight);
            //mRefreshListView.addItemDecoration(SpacesItemDecoration.newInstance(spacing, spacing, gridLayoutManager.getSpanCount(), Color.GRAY));
            //根据需要选择使用GridItemDecoration还是SpacesItemDecoration
            GridItemDecoration divider = new GridItemDecoration.Builder(this)
                    .setHorizontal(R.dimen.divide_hight)
                    .setVertical(R.dimen.divide_hight)
                    .setColorResource(R.color.divide_line)
                    .build();
            //mRefreshListView.addItemDecoration(divider);

            DividerDecoration divider2 = new DividerDecoration.Builder(this)
                    .setHeight(R.dimen.divide_hight)
                    //.setPadding(R.dimen.dp_10)
                    .setColorResource(R.color.divide_line)
                    .build();
            mRefreshListView.addItemDecoration(divider2);



        } else {
           /* if (mPage == 1) {
                mRepaymentAdapter.clear();
            }*/
            mAdapter.setBooleanList(mBooleanList);
            mAdapter.notifyDataSetChanged();
            mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
        }

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
        }

        if (mDataList.size() <= 0) {
            mPageView.showEmpty();

        } else {
            mPageView.showContent();
        }
    }








    @OnClick({R.id.left_back_lay, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.btn_next:
                LogUtilDebug.i("show","total_size:"+ mBooleanPingZhengList.size());

                mSelectPingZhengList.clear();
                if (mBooleanPingZhengList.size() == 0){ //默认全选状态 用户无点击取消勾选操作
                    for (Map map : mPingZhengList) {
                        if ((map.get("poolsta")+"").equals("0") ){
                            mSelectPingZhengList.add(map);
                        }
                    }
                }else {
                    for (Map map : mBooleanPingZhengList) {
                        boolean b = (Boolean) map.get("selected");
                        Map<String, Object> mSelectMap = (Map<String, Object>) map.get("value");
                        if (b && (mSelectMap.get("poolsta")+"").equals("0")) {
                            mSelectPingZhengList.add(mSelectMap);
                        }
                    }
                }


                LogUtilDebug.i("show","select_size:"+ mSelectPingZhengList.size());


                if (mSelectPingZhengList.size()>0){
                    mBtnNext.setEnabled(false);
                    ruchiAction();
                }else {
                    showToastMsg("请先选择收款凭证");
                }
                break;
        }
    }




    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            if (MbsConstans.BroadcastReceiverAction.HTONGUPDATE.equals(action)) {
                showProgressDialog();
                htList();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}
