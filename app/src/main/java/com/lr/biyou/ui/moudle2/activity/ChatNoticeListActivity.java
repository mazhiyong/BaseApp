package com.lr.biyou.ui.moudle2.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.lr.biyou.listener.OnChildClickListener;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.listener.SelectBackListener;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle2.adapter.ChatNoticeListAdapter;
import com.lr.biyou.utils.tool.AnimUtil;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 消息通知 界面
 */
public class ChatNoticeListActivity extends BasicActivity implements RequestView,ReLoadingData,SelectBackListener{

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

    private String mRequestTag ="";

    private String mStartTime="";
    private String mEndTime = "";
    private String mBusiType = "";

    private String mSelectStartTime = "";
    private String mSelectEndTime = "";
    private String mSelectType = "";


    private ChatNoticeListAdapter mListAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private int mPage = 1;

    private AnimUtil mAnimUtil;

    @Override
    public int getContentView() {
        return R.layout.activity_chat_notice_list;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);


        mAnimUtil = new AnimUtil();

        mTitleText.setText("新朋友");
        mTitleText.setCompoundDrawables(null,null,null,null);
        mRightImg.setVisibility(View.GONE);
        mRightTextTv.setVisibility(View.GONE);

        String sTime = UtilTools.getFirstDayOfMonthByDate(new Date());


        String eTime = UtilTools.getStringFromDate(new Date(),"yyyyMMdd");

        mSelectStartTime = sTime;
        mSelectEndTime = eTime;
        mStartTime = mSelectStartTime;
        mEndTime = mSelectEndTime;


        initView();
        showProgressDialog();
        noticeListAction();
    }


    private void initView() {
        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();
        LinearLayoutManager manager = new LinearLayoutManager(ChatNoticeListActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(manager);

        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                noticeListAction();
            }
        });

        mRefreshListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                noticeListAction();
            }
        });
    }

    private void noticeListAction(){

        mRequestTag = MethodUrl.CHAT_FRIEND_APPLY;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            MbsConstans.ACCESS_TOKEN = SPUtils.get(ChatNoticeListActivity.this,MbsConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token",MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_FRIEND_APPLY, map);
    }



    private void responseData() {
        if (mListAdapter == null) {
            mListAdapter = new ChatNoticeListAdapter(ChatNoticeListActivity.this);
            mListAdapter.addAll(mDataList);

            /*AnimationAdapter adapter = new ScaleInAnimationAdapter(mDataAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(.5f));*/

            mLRecyclerViewAdapter = new LRecyclerViewAdapter(mListAdapter);

//            SampleHeader headerView = new SampleHeader(BankCardActivity.this, R.layout.item_bank_bind);
//            mLRecyclerViewAdapter.addHeaderView(headerView);

            mRefreshListView.setAdapter(mLRecyclerViewAdapter);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(true);



        } else {
            if (mPage == 1) {
                mListAdapter.clear();
            }
            mListAdapter.addAll(mDataList);
            mListAdapter.notifyDataSetChanged();
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
            mPage++;
        }

        mRefreshListView.refreshComplete(10);
        mListAdapter.notifyDataSetChanged();
        if (mListAdapter.getDataList().size() <= 0){
            mPageView.showEmpty();
        }else {
            mPageView.showContent();
        }



        mListAdapter.setmListener(new OnChildClickListener() {
            @Override
            public void onChildClickListener(View view, int position, Map<String, Object> mParentMap) {
                agreeOrRefuseAction(position,mParentMap);
            }
        });
    }


    private void agreeOrRefuseAction(int status,Map<String,Object> mParentMap){

        mRequestTag = MethodUrl.CHAT_AGREE_REFUSE;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)){
            MbsConstans.ACCESS_TOKEN = SPUtils.get(ChatNoticeListActivity.this,MbsConstans.ACCESS_TOKEN,"").toString();
        }
        map.put("token",MbsConstans.ACCESS_TOKEN);
        map.put("id",mParentMap.get("id")+"");
        map.put("status",status);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_AGREE_REFUSE, map);
    }



    @OnClick({R.id.back_img,R.id.right_lay,R.id.left_back_lay})
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
            case MethodUrl.CHAT_FRIEND_APPLY:
                switch (tData.get("code")+""){
                    case "0":
                        if (UtilTools.empty(tData.get("data")+"")){
                            mPageView.showEmpty();
                        }else {
                            List<Map<String,Object>> list = (List<Map<String, Object>>) tData.get("data");
                            if (UtilTools.empty(list)){
                                mPageView.showEmpty();
                            }else {
                                mPageView.showContent();
                                mDataList.clear();
                                mDataList.addAll(list);
                                responseData();
                                mRefreshListView.refreshComplete(10);
                            }

                        }

                        break;
                    case "1":
                        closeAllActivity();
                        intent = new Intent(ChatNoticeListActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        mPageView.showNetworkError();
                        showToastMsg(tData.get("msg")+"");
                        break;
                }
                break;
            case MethodUrl.CHAT_AGREE_REFUSE:
                switch (tData.get("code")+""){
                    case "0":
                        showToastMsg(tData.get("msg")+"");
                        noticeListAction();
                        break;
                    case "1":
                        closeAllActivity();
                        intent = new Intent(ChatNoticeListActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg")+"");
                        break;
                }
                break;


            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.NOTICE_LIST:

                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map,String mType) {

        switch (mType) {
            case MethodUrl.tradeList://
                if (mListAdapter != null){
                    if (mListAdapter.getDataList().size() <= 0){
                        mPageView.showNetworkError();
                    }else {
                        mPageView.showContent();
                    }
                    mRefreshListView.refreshComplete(10);
                    mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            noticeListAction();
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
    public void reLoadingData() {
        showProgressDialog();
        noticeListAction();
    }

    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {

    }
}
