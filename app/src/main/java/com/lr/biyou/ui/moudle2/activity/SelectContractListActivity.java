package com.lr.biyou.ui.moudle2.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.lr.biyou.rongyun.common.IntentExtra;
import com.lr.biyou.rongyun.im.IMManager;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle2.adapter.SelectContractListAdapter;
import com.lr.biyou.utils.tool.AnimUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * 选择联系人 界面
 */
public class SelectContractListActivity extends BasicActivity implements RequestView, ReLoadingData, SelectBackListener {

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

    private String mRequestTag = "";

    private String mStartTime = "";
    private String mEndTime = "";
    private String mBusiType = "";

    private String mSelectStartTime = "";
    private String mSelectEndTime = "";
    private String mSelectType = "";


    private SelectContractListAdapter mListAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<Map<String, Object>> mDataList = new ArrayList<>();
    private List<Map<String, Object>> mBooleanList = new ArrayList<>();
    private List<Map<String, Object>> mSelectList = new ArrayList<>();
    private int mPage = 1;

    private AnimUtil mAnimUtil;
    private String type;
    private String groupId;

    @Override
    public int getContentView() {
        return R.layout.activity_select_contract_list;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);


        mAnimUtil = new AnimUtil();

        mTitleText.setText("选择联系人");
        mTitleText.setCompoundDrawables(null, null, null, null);
        mRightImg.setVisibility(View.VISIBLE);
        mRightTextTv.setVisibility(View.VISIBLE);
        mRightTextTv.setText("确定");

        String sTime = UtilTools.getFirstDayOfMonthByDate(new Date());
        String eTime = UtilTools.getStringFromDate(new Date(), "yyyyMMdd");

        mSelectStartTime = sTime;
        mSelectEndTime = eTime;
        mStartTime = mSelectStartTime;
        mEndTime = mSelectEndTime;

        initView();
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mDataList = (List<Map<String, Object>>) bundle.getSerializable("DATA");
                if (UtilTools.empty(mDataList)) {
                    showProgressDialog();
                    firendListAction();
                } else {
                    responseData();
                }
                type = bundle.getString("TYPE");
            }

        }

    }


    private void initView() {
        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();
        LinearLayoutManager manager = new LinearLayoutManager(SelectContractListActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(manager);

        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                firendListAction();
            }
        });

        mRefreshListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                firendListAction();
            }
        });
    }

    @OnClick({R.id.back_img, R.id.right_lay, R.id.left_back_lay})
    public void onViewClicked(View view) {
        Intent intent = null;
        List<Map<String, Object>> list = mListAdapter.getBooleanList();
        mSelectList.clear();
        if (list != null && list.size()> 0){
            for (Map map : list) {
                boolean b = (Boolean) map.get("selected");
                Map<String, Object> mSelectMap = (Map<String, Object>) map.get("value");
                if (b) {
                    mSelectList.add(mSelectMap);
                }
            }
        }

       /* if (mSelectList.size() == 0) {
            showToastMsg("请选择联系人");
            return;
        }
*/
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.right_lay:
                switch (type) {
                    case "0"://创建群聊
                        crateGroupAction();
                        break;
                    case "1"://群艾特

                        break;
                    case "2"://邀请加群或删除群员
                    case "3": //增加或删除群管理员
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < mSelectList.size(); i++) {
                            Map<String, Object> map = mSelectList.get(i);
                            if (i + 1 == mSelectList.size()) {
                                sb.append(map.get("id") + "");
                            } else {
                                sb.append(map.get("id") + ",");
                            }
                        }
                        intent = new Intent();
                        intent.putExtra(IntentExtra.LIST_STR_ID_LIST, sb.toString());
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case "4"://转让

                        break;
                    case "5:"://分享名片

                        break;

                }


                break;
        }
    }


    private void firendListAction() {

        mRequestTag = MethodUrl.CHAT_MY_FRIENDS;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(SelectContractListActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_MY_FRIENDS, map);
    }


    private void responseData() {
        for (Map m : mDataList) {
            Map<String, Object> map = new HashMap<>();
            map.put("value", m);
            map.put("selected", false);
            mBooleanList.add(map);
        }


        if (mListAdapter == null) {
            mListAdapter = new SelectContractListAdapter(SelectContractListActivity.this);
            mListAdapter.setBooleanList(mBooleanList);
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
            mListAdapter.setBooleanList(mBooleanList);
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
        if (mListAdapter.getDataList().size() <= 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }


        mListAdapter.setmListener(new OnChildClickListener() {
            @Override
            public void onChildClickListener(View view, int position, Map<String, Object> mParentMap) {
                //agreeOrRefuseAction(position,mParentMap);
                mRefreshListView.post(new Runnable() {
                    @Override
                    public void run() {
                        mListAdapter.notifyDataSetChanged();
                        mLRecyclerViewAdapter.notifyDataSetChanged();//必须调用此方法
                        if (type.equals("4")){
                            Intent intent = new Intent();
                            intent.putExtra(IntentExtra.LIST_STR_ID_LIST, mParentMap.get("id")+"");
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        if (type.equals("5")){
                            Intent intent = new Intent();
                            intent.putExtra("DATA", (Serializable) mParentMap);
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                    }
                });
            }
        });
    }


    private void crateGroupAction() {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mSelectList.size(); i++) {
            Map<String, Object> map = mSelectList.get(i);
            if (i + 1 == mSelectList.size()) {
                sb.append(map.get("id") + "");
            } else {
                sb.append(map.get("id") + ",");
            }
        }

        mRequestTag = MethodUrl.CHAT_CREAT_GROUPS;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(SelectContractListActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("ids", sb.toString() + "");
        map.put("name", "");
        map.put("portrait", "");
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_CREAT_GROUPS, map);
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
        Intent intent;
        switch (mType) {
            case MethodUrl.CHAT_MY_FRIENDS:
                switch (tData.get("code") + "") {
                    case "0":
                        if (UtilTools.empty(tData.get("data") + "")) {
                            mPageView.showEmpty();
                        } else {
                            Map<String, Object> mapData = (Map<String, Object>) tData.get("data");
                            if (!UtilTools.empty(mapData)) {
                                if (UtilTools.empty(mapData.get("friend") + "")) {
                                    mPageView.showEmpty();
                                } else {
                                    mDataList = (List<Map<String, Object>>) mapData.get("friend");
                                    if (UtilTools.empty(mDataList)) {
                                        mPageView.showEmpty();
                                    } else {
                                        mPageView.showContent();
                                        responseData();
                                        mRefreshListView.refreshComplete(10);
                                    }
                                }

                            } else {
                                mPageView.showEmpty();
                            }
                        }
                        break;
                    case "1":
                        closeAllActivity();
                        intent = new Intent(SelectContractListActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        mPageView.showNetworkError();
                        showToastMsg(tData.get("msg") + "");
                        break;
                }
                break;
            case MethodUrl.CHAT_CREAT_GROUPS:
                switch (tData.get("code") + "") {
                    case "0":
                        //showToastMsg(tData.get("msg")+"");
                        LogUtilDebug.i("show", "创建成功");
                        groupId = tData.get("data") + "";
                        getGroupInfoAction(groupId);
                        //RongIM.getInstance().startGroupChat(SelectContractListActivity.this, tData.get("data") + "", "新建群聊");

                        break;
                    case "1":
                        closeAllActivity();
                        intent = new Intent(SelectContractListActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        mPageView.showNetworkError();
                        showToastMsg(tData.get("msg") + "");
                        break;
                }
                break;

            case MethodUrl.CHAT_GROUPS_INFO:
                switch (tData.get("code") + "") {
                    case "0":
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            Map<String, Object> map = (Map<String, Object>) tData.get("data");
                            if (!UtilTools.empty(map)) {
                               // 更新 IMKit 缓存群组数据
                                IMManager.getInstance().updateGroupInfoCache(groupId, map.get("name") + "", Uri.parse(map.get("portrait")+""));
                                RongIM.getInstance().startConversation(this, Conversation.ConversationType.GROUP, groupId, map.get("name") + "");
                                finish();
                            }
                        }

                        break;
                    case "1":
                        closeAllActivity();
                        intent = new Intent(SelectContractListActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
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
    public void loadDataError(Map<String, Object> map, String mType) {

        switch (mType) {
            case MethodUrl.tradeList://
                if (mListAdapter != null) {
                    if (mListAdapter.getDataList().size() <= 0) {
                        mPageView.showNetworkError();
                    } else {
                        mPageView.showContent();
                    }
                    mRefreshListView.refreshComplete(10);
                    mRefreshListView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            firendListAction();
                        }
                    });
                } else {
                    mPageView.showNetworkError();
                }
                break;
        }

        dealFailInfo(map, mType);
    }

    private void getGroupInfoAction(String groupId) {

        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(SelectContractListActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("group_id", groupId);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_GROUPS_INFO, map);

    }


    @Override
    public void reLoadingData() {
        showProgressDialog();
        firendListAction();
    }

    @Override
    public void onSelectBackListener(Map<String, Object> map, int type) {

    }
}
