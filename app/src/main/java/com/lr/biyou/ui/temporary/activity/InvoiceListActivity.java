package com.lr.biyou.ui.temporary.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.db.FaPiaoData;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.ui.moudle.activity.TestScanActivity;
import com.lr.biyou.ui.temporary.adapter.InvoiceListAdapter;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 已经导入的发票列表   界面
 */
public class InvoiceListActivity extends BasicActivity implements RequestView, ReLoadingData {

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
    @BindView(R.id.right_text_tv)
    TextView mRightTextTv;
    @BindView(R.id.top_layout)
    LinearLayout mTitleBarView;
    @BindView(R.id.right_lay)
    LinearLayout mRightLay;
    @BindView(R.id.refresh_list_view)
    LRecyclerView mRefreshListView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.tv_message)
    TextView mTvMessage;
    @BindView(R.id.tv_message2)
    TextView mTvMessage2;


    private String mRequestTag = "";

    private InvoiceListAdapter mInvoiceListAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private int mPage = 1;


    @Override
    public int getContentView() {
        return R.layout.activity_invoice_list;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mTitleText.setText(getResources().getString(R.string.sao_result));

        mRightImg.setVisibility(View.VISIBLE);
        mRightImg.setImageResource(R.drawable.shuaixuan);
        mRightTextTv.setVisibility(View.VISIBLE);
        mRightLay.setVisibility(View.GONE);

        initView();

        //获取数据库发票信息
        mDataList = FaPiaoData.getInstance().selectDB();
        responseData();
//        showProgressDialog();
//        samePeopleList();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

            }
        }
        super.onNewIntent(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //获取数据库发票信息
        mDataList = FaPiaoData.getInstance().selectDB();
        responseData();
    }

    private void initView() {
        mPageView.setContentView(mContent);
        mPageView.showLoading();
        mPageView.setReLoadingData(this);
        LinearLayoutManager manager = new LinearLayoutManager(InvoiceListActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(manager);

        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                mDataList.clear();
                mDataList = FaPiaoData.getInstance().selectDB();
                responseData();
            }
        });

        mRefreshListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPage++;
                mHandler.sendEmptyMessageDelayed(1, 3000);
            }
        });
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            responseData();
        }
    };


    private void samePeopleList() {

        mRequestTag = MethodUrl.peopleList;
        Map<String, String> map = new HashMap<>();
        map.put("creditstate", "");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToRes(mHeaderMap, MethodUrl.peopleList, map);
    }


    private void responseData() {

       /* for (int i = 0;i<10;i++){
            mDataList.add(new HashMap<>());
        }
*/
        mTvMessage.setText("已成功扫描 "+mDataList.size()+" 张发票");
        mTvMessage2.setText("已成功扫描 "+mDataList.size()+" 张发票");

        if (mInvoiceListAdapter == null) {
            mInvoiceListAdapter = new InvoiceListAdapter(InvoiceListActivity.this);
            mInvoiceListAdapter.addAll(mDataList);

            /*AnimationAdapter adapter = new ScaleInAnimationAdapter(mDataAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(.5f));*/

            mLRecyclerViewAdapter = new LRecyclerViewAdapter(mInvoiceListAdapter);


            mRefreshListView.setAdapter(mLRecyclerViewAdapter);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(true);


            mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Map<String, Object> item = mInvoiceListAdapter.getDataList().get(position);
                   /* Intent intent = new Intent(getActivity(), ShowDetailPictrue.class);
                    intent.putExtra("jsonData",item.get("url")+"");
                    startActivity(intent);*/
                }

            });


        } else {
            if (mPage == 1) {
                mInvoiceListAdapter.clear();
            }
            mInvoiceListAdapter.addAll(mDataList);
            mInvoiceListAdapter.notifyDataSetChanged();
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
        mInvoiceListAdapter.notifyDataSetChanged();
        if (mInvoiceListAdapter.getDataList().size() < 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }

    }


    @OnClick({R.id.back_img, R.id.right_lay, R.id.left_back_lay, R.id.save_tv, R.id.next_tv})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.right_lay:
                break;
            case R.id.next_tv:
                intent = new Intent(this, TestScanActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
                break;
            case R.id.save_tv:
                intent = new Intent(this, InvoiceImportActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);

                //清空数据库信息
                FaPiaoData.getInstance().clearData();
                break;
        }
    }

    @Override
    public void showProgress() {
        // showProgressDialog();
    }

    @Override
    public void disimissProgress() {
        dismissProgressDialog();
    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {

        Intent intent;
        switch (mType) {
            case MethodUrl.peopleList://
                String result = tData.get("result") + "";
                if (UtilTools.empty(result)) {
                    List<Map<String, Object>> list = JSONUtil.getInstance().jsonToList(result);
                    responseData();
                } else {
                    List<Map<String, Object>> list = JSONUtil.getInstance().jsonToList(result);
                    if (list != null) {
                        mDataList.clear();
                        mDataList.addAll(list);
                        responseData();
                    } else {

                    }
                }
                mRefreshListView.refreshComplete(10);

                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.peopleList:
                        samePeopleList();
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

        switch (mType) {
            case MethodUrl.peopleList://

                if (mInvoiceListAdapter != null) {
                    if (mInvoiceListAdapter.getDataList().size() <= 0) {
                        mPageView.showNetworkError();
                    } else {
                        mPageView.showContent();
                    }
                    mRefreshListView.refreshComplete(10);
                    mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            samePeopleList();
                        }
                    });
                } else {
                    mPageView.showNetworkError();
                }
                break;
        }
        dealFailInfo(map, mType);
    }

    @Override
    public void reLoadingData() {
        showProgressDialog();
        samePeopleList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
