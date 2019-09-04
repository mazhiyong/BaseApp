package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.ItemDecoration.GridItemDecoration;
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
import com.lr.biyou.ui.temporary.adapter.FukuanFangSelectAdapter;
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
 * 选择付款方
 */
public class SelectFukuanFangActivity extends BasicActivity implements RequestView, ReLoadingData {
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
    @BindView(R.id.date_tv)
    TextView mDateTv;
    @BindView(R.id.refresh_list_view)
    LRecyclerView mRefreshListView;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;

    LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    FukuanFangSelectAdapter mAdapter;
    List<Map<String, Object>> mDataList = new ArrayList<>();

    private String mRequestTag = "";

    private Map<String,Object> mSxMap = new HashMap<>();


    @Override
    public int getContentView() {
        return R.layout.activity_select_fukuan_fang;
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            mSxMap  = (Map<String, Object>) bundle.getSerializable("DATA");
        }

        mTitleText.setText(getResources().getString(R.string.shouldshou_money));

        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(linearLayoutManager);
        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                payList();
            }
        });

        showProgressDialog();
        payList();
    }

    private void payList() {
        mRequestTag = MethodUrl.payCompanyList;
        Map<String, String> map = new HashMap<>();
        map.put("flowdate",mSxMap.get("flowdate")+"");
        map.put("flowid",mSxMap.get("flowid")+"");
        map.put("autoid",mSxMap.get("autoid")+"");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.payCompanyList, map);
    }


    @OnClick(R.id.left_back_lay)
    public void onViewClicked() {
        finish();
    }



    @Override
    public void reLoadingData() {
        payList();
        showProgressDialog();

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
        switch (mType){
            case MethodUrl.payCompanyList:
                List<Map<String,Object>> list = (List<Map<String, Object>>) tData.get("payFirmInfoList");
                if (list != null) {
                    mDataList.clear();
                    mDataList.addAll(list);
                    responseData();
                }

                mRefreshListView.refreshComplete(10);
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.payCompanyList:
                        payList();
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
            case MethodUrl.payCompanyList:
                if(mAdapter!=null){
                    if(mAdapter.getDataList().size()<=0){
                        mPageView.showNetworkError();
                    }else {
                        mPageView.showContent();
                    }
                    mRefreshListView.refreshComplete(10);
                    mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            payList();
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
        if (mAdapter == null) {
            mAdapter = new FukuanFangSelectAdapter(this);
            mAdapter.addAll(mDataList);

            AnimationAdapter adapter1 = new ScaleInAnimationAdapter(mAdapter);
            adapter1.setFirstOnly(false);
            adapter1.setDuration(400);
            adapter1.setInterpolator(new OvershootInterpolator(0.8f));


            AnimationAdapter adapter = new AlphaInAnimationAdapter(adapter1);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(1f));


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
                    // .setHeight(R.dimen.dp_10)
                    //.setPadding(R.dimen.dp_10)
                    .setColorResource(R.color.body_bg)
                    .build();
            mRefreshListView.addItemDecoration(divider2);


            mAdapter.setOnItemClickListener(new com.lr.biyou.listener.OnItemClickListener() {
                @Override
                public void onItemClickListener(View view, int position, Map<String, Object> map) {
                /*    Intent intent = new Intent(SelectFukuanFangActivity.this, ShouldShouMoneyActivity.class);
                    intent.putExtra("payfirmname",map.get("payfirmname")+"");
                    intent.putExtra("paycustid",map.get("paycustid")+"");
                    intent.putExtra("DATA",(Serializable) mSxMap);
                    startActivity(intent);*/
                    Intent intent = new Intent(SelectFukuanFangActivity.this, BorrowMoneyActivity.class);
                    intent.putExtra("payfirmname",map.get("payfirmname")+"");
                    intent.putExtra("paycustid",map.get("paycustid")+"");
                    intent.putExtra("DATA",(Serializable) mSxMap);
                    startActivity(intent);
                }
            });


        } else {

           /* if (mPage == 1) {
                mRepaymentAdapter.clear();
            }*/
            mAdapter.clear();
            mAdapter.addAll(mDataList);
            mAdapter.notifyDataSetChanged();
            mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
        }

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
        }

        if (mAdapter.getDataList().size() <= 0) {
            mPageView.showEmpty();

        } else {
            mPageView.showContent();
        }
    }


    @OnClick({R.id.left_back_lay, R.id.btn_next, R.id.content, R.id.page_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back_lay:
                break;
            case R.id.btn_next:
                break;
            case R.id.content:
                break;
            case R.id.page_view:
                break;
        }
    }




}
