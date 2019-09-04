package com.lr.biyou.ui.temporary.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.ItemDecoration.GridItemDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
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
import com.lr.biyou.ui.temporary.adapter.AdvanceListAdapter;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.UtilTools;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
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
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 *  预付款 合同   列表
 */
public class AdvancePayListActivity extends BasicActivity implements RequestView, ReLoadingData {
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
    @BindView(R.id.brorow_tip_tv)
    TextView mTipTv;
    @BindView(R.id.refresh_list_view)
    LRecyclerView mRefreshListView;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.top_layout)
    LinearLayout mTitleBarView;
    @BindView(R.id.divide_line)
    View mDivideLine;
    @BindView(R.id.content)
    LinearLayout mContent;


    LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    AdvanceListAdapter mAdvanceListAdapter;
    List<Map<String, Object>> mDataList = new ArrayList<>();

    private String mRequestTag = "";

    private Map<String,Object> mHezuoMap = new HashMap<>();

    @Override
    public int getContentView() {
        return R.layout.activity_advance_list;
    }


    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);


        IntentFilter filter = new IntentFilter();
        filter.addAction(MbsConstans.BroadcastReceiverAction.HTONGUPDATE);
        registerReceiver(receiver, filter);

        mTitleText.setText(getResources().getString(R.string.apply_brorow));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mHezuoMap = (Map<String, Object>) bundle.getSerializable("DATA");
        }


        LogUtilDebug.i("--------------------------------------------------------------",mHezuoMap);


        mRightTextTv.setText("新增");
        mRightTextTv.setTextColor(ContextCompat.getColor(this,R.color.font_c));
        mRightTextTv.setVisibility(View.VISIBLE);
        mRightTextTv.setPadding(0,0,UtilTools.dip2px(this,15),0);

        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(linearLayoutManager);
        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                billInfoList();
            }
        });

        showProgressDialog();
        billInfoList();
    }


    private void billInfoList() {
        mRequestTag = MethodUrl.billinfolist;
        Map<String, String> map = new HashMap<>();
        map.put("patncode", mHezuoMap.get("patncode")+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.billinfolist, map);
    }



    @OnClick({R.id.right_lay, R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.right_lay:
                intent = new Intent(this,AdvanceHtAddActivity.class);
                intent.putExtra("DATA",(Serializable) mHezuoMap);
                startActivity(intent);
                break;
            case R.id.left_back_lay:
                finish();
                break;
        }
    }


    /**
     * @descriptoin 请求前加载progress
     * @author dc
     * @date 2017/2/16 11:00
     */
    @Override
    public void showProgress() {
        //dismissProgressDialog();
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

        switch (mType){
            case MethodUrl.billinfolist:
                String result = tData.get("result")+"";
                List<Map<String, Object>> infoList = JSONUtil.getInstance().jsonToList(result);
                if (infoList != null ){
                    mDataList.clear();
                    mDataList.addAll(infoList);
                }
                responseData();
                mRefreshListView.refreshComplete(10);
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.billinfolist:
                        billInfoList();
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

        switch (mType){
            case MethodUrl.billinfolist:
                if(mAdvanceListAdapter !=null){
                    if(mAdvanceListAdapter.getDataList().size()<=0){
                        mPageView.showNetworkError();
                    }else {
                        mPageView.showContent();
                    }
                    mRefreshListView.refreshComplete(10);
                    mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            billInfoList();
                            showProgressDialog();
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
        if (mAdvanceListAdapter == null) {
            mAdvanceListAdapter = new AdvanceListAdapter(this);
            mAdvanceListAdapter.addAll(mDataList);

            AnimationAdapter adapter1 = new ScaleInAnimationAdapter(mAdvanceListAdapter);
            adapter1.setFirstOnly(false);
            adapter1.setDuration(400);
            adapter1.setInterpolator(new OvershootInterpolator(0.8f));


            AnimationAdapter adapter = new AlphaInAnimationAdapter(adapter1);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(1f));


            mLRecyclerViewAdapter = new LRecyclerViewAdapter(mAdvanceListAdapter);
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
                    .setHeight(R.dimen.dp_10)
                    .setPadding(R.dimen.dp_10)
                    .setColorResource(R.color.body_bg)
                    .build();
            mRefreshListView.addItemDecoration(divider2);

            mAdvanceListAdapter.setOnItemClickListener(new com.lr.biyou.listener.OnItemClickListener() {
                @Override
                public void onItemClickListener(View view, int position, Map<String, Object> map) {
                    switch (view.getId()){
                        case R.id.go_borrow:
                            Intent intent = new Intent(AdvancePayListActivity.this, BorrowMoneyActivity.class);
                            mHezuoMap.putAll(map);
                            intent.putExtra("DATA",(Serializable) mHezuoMap);
                            startActivity(intent);
                            break;
                    }
                }
            });

            mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                   /* Intent intent = new Intent(AdvancePayListActivity.this, BorrowMoneyActivity.class);
                    intent.putExtra("DATA",(Serializable) mHezuoMap);
                    startActivity(intent);*/
                }

            });


        } else {

           /* if (mPage == 1) {
                mRepaymentAdapter.clear();
            }*/
            mAdvanceListAdapter.clear();
            mAdvanceListAdapter.addAll(mDataList);
            mAdvanceListAdapter.notifyDataSetChanged();
            mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
        }

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
        }

        if (mAdvanceListAdapter.getDataList().size() <= 0) {
            mPageView.showEmpty();

        } else {
            mPageView.showContent();
        }
    }

    @Override
    public void reLoadingData() {
        billInfoList();
        showProgress();
    }

    private List<Map<String, Object>> mFileList = new ArrayList<>();
    private int mFileNum = 0;
    //广播监听
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            if (action.equals(MbsConstans.BroadcastReceiverAction.HTONGUPDATE)) {
               billInfoList();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

}
