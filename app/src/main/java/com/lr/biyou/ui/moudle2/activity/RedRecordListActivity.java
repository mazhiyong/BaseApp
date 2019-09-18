package com.lr.biyou.ui.moudle2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle2.adapter.RedRecordListAdapter;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 红包记录
 */
public class RedRecordListActivity extends BasicActivity implements RequestView, ReLoadingData,SelectBackListener {

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.right_lay)
    LinearLayout rightLay;
    @BindView(R.id.refresh_list_view)
    LRecyclerView refreshListView;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.page_view)
    PageView pageView;
    @BindView(R.id.divide_line)
    View divideLine;

    private LRecyclerViewAdapter mLRecyclerViewAdapter1 = null;
    private RedRecordListAdapter mListAdapter;
    private List<Map<String,Object>> mDataList = new ArrayList<>();
    private String mRequestTag = "";

    @Override
    public int getContentView() {
        return R.layout.activity_red_record_list;
    }

    @Override
    public void init() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setCompoundDrawables(null,null,null,null);
        divideLine.setVisibility(View.GONE);
        rightImg.setVisibility(View.GONE);
        Bundle bundle =getIntent().getExtras();
        if (!UtilTools.empty(bundle)){
            String type = bundle.getString("type");
            LogUtilDebug.i("show","TYPE----------"+type);
            if (!UtilTools.empty(type)){
                if (type.equals("1")){
                    mTitleText.setText("红包历史");
                    getRedRecordAction();
                }else {
                    mTitleText.setText("转账历史");
                    getTransferRecordAction();
                }
            }
        }


        initView();

    }

    private void initView() {
        pageView.setContentView(content);
        pageView.setReLoadingData(this);
        pageView.showLoading();
        LinearLayoutManager manager = new LinearLayoutManager(RedRecordListActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        refreshListView.setLayoutManager(manager);

        refreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (mRequestTag){
                    case MethodUrl.CHAT_RED_RECORD:
                        getRedRecordAction();
                        break;

                    case MethodUrl.CHAT_ZHUANZHANG_RECORD:
                        getTransferRecordAction();
                        break;
                }
            }
        });

    }

    private void getRedRecordAction() {
        mRequestTag = MethodUrl.CHAT_RED_RECORD;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(RedRecordListActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_RED_RECORD, map);
    }

    private void getTransferRecordAction() {
        mRequestTag = MethodUrl.CHAT_ZHUANZHANG_RECORD;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(RedRecordListActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_ZHUANZHANG_RECORD, map);
    }



    @OnClick({R.id.left_back_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back_lay:
                finish();
                break;

        }
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        Intent intent;
        switch (mType){
            case MethodUrl.CHAT_ZHUANZHANG_RECORD:
            case MethodUrl.CHAT_RED_RECORD:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (UtilTools.empty(tData.get("data")+"")){
                            pageView.showEmpty();
                        }else {
                            mDataList = (List<Map<String, Object>>) tData.get("data");
                            if (UtilTools.empty(mDataList)) {
                                pageView.showEmpty();
                            } else {
                                pageView.showContent();
                                responseData();
                                refreshListView.refreshComplete(10);
                            }
                        }

                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        pageView.showNetworkError();
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        intent = new Intent(RedRecordListActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
        }

    }



    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map, mType);
    }


    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {
        switch (type) {
            case 10:
                String s = (String) map.get("name"); //选择账户

                break;
            case 30: //选择币种
                String str = (String) map.get("name"); //选择币种

                break;
        }
    }


    private void responseData() {
        if (mListAdapter == null) {
            mListAdapter = new RedRecordListAdapter(RedRecordListActivity.this);
            mListAdapter.addAll(mDataList);

            /*AnimationAdapter adapter = new ScaleInAnimationAdapter(mDataAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(.5f));*/

            mLRecyclerViewAdapter1 = new LRecyclerViewAdapter(mListAdapter);

//            SampleHeader headerView = new SampleHeader(BankCardActivity.this, R.layout.item_bank_bind);
//            mLRecyclerViewAdapter.addHeaderView(headerView);

            refreshListView.setAdapter(mLRecyclerViewAdapter1);
            refreshListView.setItemAnimator(new DefaultItemAnimator());
            refreshListView.setHasFixedSize(true);
            refreshListView.setNestedScrollingEnabled(false);

            refreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            refreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            refreshListView.setPullRefreshEnabled(true);
            refreshListView.setLoadMoreEnabled(true);


        } else {
            mListAdapter.clear();
            mListAdapter.addAll(mDataList);
            refreshListView.setAdapter(mLRecyclerViewAdapter1);
        }
     /*   //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.colorAccent, R.color.black, android.R.color.white);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.LineScalePulseOut); //设置下拉刷新Progress的样式
        //mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);  //设置下拉刷新箭头
        //设置头部加载颜色
        mRecyclerView.setHeaderViewColor(R.color.colorAccent, R.color.red ,android.R.color.white);
//设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.colorAccent, R.color.red ,android.R.color.white);*/

        refreshListView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");
        if (mDataList.size() < 10) {
            refreshListView.setNoMore(true);
        } else {
            refreshListView.setNoMore(false);
        }

        refreshListView.refreshComplete(10);
        mListAdapter.notifyDataSetChanged();
        if (mListAdapter.getDataList().size() <= 0) {
            pageView.showEmpty();
        } else {
            pageView.showContent();
        }
    }

    @Override
    public void reLoadingData() {
        switch (mRequestTag){
            case MethodUrl.CHAT_RED_RECORD:
                getRedRecordAction();
                break;

            case MethodUrl.CHAT_ZHUANZHANG_RECORD:
                getTransferRecordAction();
                break;
        }
    }
}
