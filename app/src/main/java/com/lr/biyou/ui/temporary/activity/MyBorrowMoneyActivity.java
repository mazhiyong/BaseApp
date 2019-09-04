package com.lr.biyou.ui.temporary.activity;

import android.content.Intent;
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
import com.lr.biyou.mywidget.view.TipsToast;
import com.lr.biyou.ui.temporary.adapter.MyBorrowMoneyAdapter;
import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.ui.moudle.activity.MainActivity;

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
 * 我的借款
 */
public class MyBorrowMoneyActivity extends BasicActivity implements RequestView, ReLoadingData {
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
    MyBorrowMoneyAdapter mBorrowMoneyAdapter;
    List<Map<String, Object>> mDataList = new ArrayList<>();

    private String mRequestTag = "";


    @Override
    public int getContentView() {
        return R.layout.activity_brorow_my_money;
    }


    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mTitleText.setText(getResources().getString(R.string.borrow_money_my));

        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(linearLayoutManager);
        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshListView.refreshComplete(10);
            }
        });

        //showProgressDialog();
        initData();
        responseData();

    }

    private void initData() {
        for (int i = 0; i <5 ; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("type","应收账款融资");
            map.put("state","审核中");
            map.put("date","2019-04-27");
            map.put("money","2180000");
            map.put("rate","0.05%");
            map.put("qixian","6个月");
            map.put("number","1891029190219029121");
            map.put("peopel","郑州银行花卉银行");
            mDataList.add(map);
        }

    }


    //借款前的配置信息获取   借款前选择的合作方   合作方列表
    private void jiekuanCheck() {
        if (MbsConstans.USER_MAP != null) {//是否完善信息（1：已完善 0：未完善）
            String ss = MbsConstans.USER_MAP.get("cmpl_info") + "";
            if (ss.equals("1")) {
            } else {
                Intent intent = new Intent(this, PerfectInfoActivity.class);
                startActivity(intent);
            }

        } else {
            MainActivity.mInstance.getUserInfoAction();
            TipsToast.showToastMsg("获取用户基本信息失败,请重新获取");
            return;
        }

        mRequestTag = MethodUrl.jiekuanSxList;
        Map<String, String> map = new HashMap<>();
        map.put("creditstate", "1");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.jiekuanSxList, map);
    }


    @OnClick(R.id.left_back_lay)
    public void onViewClicked() {
        finish();
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
            case MethodUrl.jiekuanSxList:
                List<Map<String, Object>> jkSxList =(List<Map<String,Object>>) tData.get("creditList");
                if (jkSxList != null ){
                    mDataList.addAll(jkSxList);
                    responseData();
                }else {
                    TipsToast.showToastMsg(getResources().getString(R.string.exception_info));
                }

                mRefreshListView.refreshComplete(10);
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.jiekuanSxList:
                        jiekuanCheck();
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
            case MethodUrl.jiekuanSxList:
                if(mBorrowMoneyAdapter!=null){
                    if(mBorrowMoneyAdapter.getDataList().size()<=0){
                        mPageView.showNetworkError();
                    }else {
                        mPageView.showContent();
                    }
                    mRefreshListView.refreshComplete(10);
                    mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            jiekuanCheck();
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
        if (mBorrowMoneyAdapter== null) {
            mBorrowMoneyAdapter = new MyBorrowMoneyAdapter(this);
            mBorrowMoneyAdapter.addAll(mDataList);

            AnimationAdapter adapter1 = new ScaleInAnimationAdapter(mBorrowMoneyAdapter);
            adapter1.setFirstOnly(false);
            adapter1.setDuration(400);
            adapter1.setInterpolator(new OvershootInterpolator(0.8f));


            AnimationAdapter adapter = new AlphaInAnimationAdapter(adapter1);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(1f));


            mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
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

            mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                }

            });


        } else {

           /* if (mPage == 1) {
                mRepaymentAdapter.clear();
            }*/
            mBorrowMoneyAdapter.clear();
            mBorrowMoneyAdapter.addAll(mDataList);
            mBorrowMoneyAdapter.notifyDataSetChanged();
            mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
        }

        mRefreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
        }

        if (mBorrowMoneyAdapter.getDataList().size() <= 0) {
            mPageView.showEmpty();

        } else {
            mPageView.showContent();
        }
    }





    @Override
    public void reLoadingData() {
        jiekuanCheck();
        showProgress();
    }



}
