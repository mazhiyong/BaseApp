package com.lr.biyou.ui.moudle2.fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidkun.xtablayout.XTabLayout;
import com.flyco.dialog.utils.CornerUtils;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicFragment;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.listener.CallBackTotal;
import com.lr.biyou.listener.OnChildClickListener;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.LoadingWindow;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.rongyun.db.model.UserInfo;
import com.lr.biyou.rongyun.im.IMManager;
import com.lr.biyou.rongyun.model.Resource;
import com.lr.biyou.rongyun.task.UserTask;
import com.lr.biyou.rongyun.ui.fragment.MainConversationListFragment;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle.activity.TestScanActivity;
import com.lr.biyou.ui.moudle2.activity.AddFriendActivity;
import com.lr.biyou.ui.moudle2.activity.ChatNoticeListActivity;
import com.lr.biyou.ui.moudle2.activity.SelectContractListActivity;
import com.lr.biyou.ui.moudle2.adapter.MyFriendListAdapter;
import com.lr.biyou.ui.moudle2.adapter.MyRecentChatListAdapter;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.AnimUtil;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

@SuppressLint("ValidFragment")
public class ChatViewFragment extends BasicFragment implements RequestView, ReLoadingData {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_text_tv)
    TextView rightTextTv;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.right_lay)
    LinearLayout rightLay;
    @BindView(R.id.top_layout)
    LinearLayout topLayout;
    @BindView(R.id.divide_line)
    View divideLine;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tab_layout)
    XTabLayout tabLayout;
    @BindView(R.id.notice_iv)
    ImageView noticeIv;
    @BindView(R.id.notice_title_tv)
    TextView noticeTitleTv;
    @BindView(R.id.notice_number_tv)
    TextView noticeNumberTv;
    @BindView(R.id.notice_layout)
    LinearLayout noticeLayout;
    @BindView(R.id.refresh_list_view)
    LRecyclerView mRefreshListView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.title)
    TextView Title;


    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    private boolean mIsback = false;

    private String mRequestTag = "";
    private String isShow="-1";

    private List<Fragment> mFragments = new ArrayList<>();

    private AnimUtil mAnimUtil;
    private LRecyclerViewAdapter mLRecyclerViewAdapter1 = null;
    private MyFriendListAdapter mListAdapter;
    private List<Map<String, Object>> mFriendList;

    private LRecyclerViewAdapter mLRecyclerViewAdapter2 = null;
    private MyRecentChatListAdapter mListAdapter2;
    private List<Map<String, Object>> mRecentlyList;


    private LRecyclerViewAdapter mLRecyclerViewAdapter3 = null;
    private MyRecentChatListAdapter mListAdapter3;
    private List<Map<String, Object>> mGroupList;

    private int mPage = 1;
    private MainConversationListFragment conversationListFragment ;


    private LoadingWindow mLoadingWindow;
    private UserTask userTask;
    private String friendName;

    public ChatViewFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_chat_view;
    }


    @Override
    public void init() {
        setBarTextColor();
        initView();
        mAnimUtil = new AnimUtil();

        getMyFriendsAction();
        getMyGroupsAction();

      /*  RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                Log.i("show","获取会话列表成功");
                if (conversations != null && conversations.size() > 0) {
                    Log.i("show","获取会话列表数:"+conversations.size());
                    for (Conversation c : conversations) {
                        Log.i("show","获取会话列表Title:"+c.getConversationTitle());
                        Log.i("show","获取会话列表:id"+c.getTargetId());
                        Log.i("show","获取会话列表:message"+c.getLatestMessage().toString());
                        Log.i("show","获取会话列表:url"+c.getPortraitUrl());



                        //RongIM.getInstance().clearMessagesUnreadStatus(c.getConversationType(), c.getTargetId(), null);
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
                Log.i("show","获取会话列表失败");
            }
        });
*/
        conversationListFragment = new MainConversationListFragment();
        getChildFragmentManager().beginTransaction().add(R.id.fragment_container, conversationListFragment)
                .show(conversationListFragment)
                .commitAllowingStateLoss();


    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtilDebug.i("show", "chatFragment 可见");
        switch (mRequestTag) {
            case MethodUrl.CHAT_RECENTLY_LIST:
                showProgress();
                getRecentChatAction();
                break;
            case MethodUrl.CHAT_MY_FRIENDS:
                showProgress();
                getMyFriendsAction();
                break;
            case MethodUrl.CHAT_MY_GROUPS:
                showProgress();
                getMyGroupsAction();
                break;
        }


    }


    private void initView() {

        mPageView.setContentView(mContent);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        mRefreshListView.setLayoutManager(manager);

        mRefreshListView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                switch (mRequestTag) {
                   /* case MethodUrl.CHAT_RECENTLY_LIST:
                        //showProgress();
                        //getRecentChatAction();
                        fragmentContainer.setVisibility(View.VISIBLE);
                        mPageView.setVisibility(View.GONE);
                        break;*/
                    case MethodUrl.CHAT_MY_FRIENDS:
                        showProgress();
                        getMyFriendsAction();
                        fragmentContainer.setVisibility(View.GONE);
                        mPageView.setVisibility(View.VISIBLE);
                        break;

                    case MethodUrl.CHAT_MY_GROUPS:
                        showProgress();
                        getMyGroupsAction();
                        fragmentContainer.setVisibility(View.GONE);
                        mPageView.setVisibility(View.VISIBLE);
                        break;

                }
            }
        });

        mRefreshListView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("近期聊天"));
        tabLayout.addTab(tabLayout.newTab().setText("我的好友"));
        tabLayout.addTab(tabLayout.newTab().setText("我的群组"));

        ConversationListFragment conListFragment = new ConversationListFragment();
        mFragments.add(conListFragment);



        tabLayout.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        isShow ="0";
                        Title.setText("最近聊天");
                        //近期聊天
                        noticeLayout.setVisibility(View.GONE);
                        //getRecentChatAction();
                        fragmentContainer.setVisibility(View.VISIBLE);
                        mPageView.setVisibility(View.GONE);
                        break;
                    case 1:
                        isShow ="1";
                        Title.setText("好友列表");
                        fragmentContainer.setVisibility(View.GONE);
                        mPageView.setVisibility(View.VISIBLE);
                        noticeLayout.setVisibility(View.VISIBLE);
                        //好友列表
                        getMyFriendsAction();
                        break;

                    case 2:
                        isShow ="2";
                        noticeLayout.setVisibility(View.GONE);
                        fragmentContainer.setVisibility(View.GONE);
                        mPageView.setVisibility(View.VISIBLE);
                        Title.setText("我加入的群组");
                        //我的群组
                        getMyGroupsAction();
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
        mLoadingWindow = new LoadingWindow(getActivity(), R.style.Dialog);
        mLoadingWindow.showView();

        Title.setText("最近聊天");
        //近期聊天
        noticeLayout.setVisibility(View.GONE);
        mRequestTag = MethodUrl.CHAT_RECENTLY_LIST;
        //getRecentChatAction();


        userTask = new UserTask(getActivity());

        //融云Democode 获取用户信息
        if (UtilTools.empty(MbsConstans.RONGYUN_MAP)) {
            String s = SPUtils.get(getActivity(), MbsConstans.SharedInfoConstans.RONGYUN_DATA, "").toString();
            MbsConstans.RONGYUN_MAP = JSONUtil.getInstance().jsonMap(s);
        }
        userTask.getUserInfo(MbsConstans.RONGYUN_MAP.get("id") + "").observe(this, new Observer<Resource<UserInfo>>() {
            @Override
            public void onChanged(Resource<UserInfo> resource) {
                if (resource.data != null) {
                    UserInfo info = resource.data;
                }
            }
        });


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence sequence, int start, int before, int count) {
                if (sequence.toString().length() > 0) {
                    switch (mRequestTag) {
                        case MethodUrl.CHAT_RECENTLY_LIST:
                            if (mListAdapter2 != null) {
                                mListAdapter2.getFilter().filter(sequence.toString());
                                mListAdapter2.setBackTotal(new CallBackTotal() {
                                    @Override
                                    public void setTotal(int size) {
                                        Log.i("show", "size:" + size);
                                        if (size == 0) {
                                            mPageView.showEmpty();
                                        } else {
                                            mPageView.showContent();
                                        }
                                    }
                                });
                            }
                            break;
                        case MethodUrl.CHAT_MY_FRIENDS:
                            if (mListAdapter != null) {
                                mListAdapter.getFilter().filter(sequence.toString());
                                mListAdapter.setBackTotal(new CallBackTotal() {
                                    @Override
                                    public void setTotal(int size) {
                                        if (size == 0) {
                                            mPageView.showEmpty();
                                        } else {
                                            mPageView.showContent();
                                        }
                                    }
                                });
                            }
                            break;
                        case MethodUrl.CHAT_MY_GROUPS:
                            if (mListAdapter3 != null) {
                                mListAdapter3.getFilter().filter(sequence.toString());
                                mListAdapter3.setBackTotal(new CallBackTotal() {
                                    @Override
                                    public void setTotal(int size) {
                                        if (size == 0) {
                                            mPageView.showEmpty();
                                        } else {
                                            mPageView.showContent();
                                        }
                                    }
                                });

                            }
                            break;

                    }
                } else {
                    switch (mRequestTag) {
                        case MethodUrl.CHAT_RECENTLY_LIST:
                            if (mRecentlyList != null && mRecentlyList.size() > 0) {
                                responseData2();
                            }
                            break;
                        case MethodUrl.CHAT_MY_FRIENDS:
                            if (mFriendList != null && mFriendList.size() > 0) {
                                responseData();
                            }
                            break;
                        case MethodUrl.CHAT_MY_GROUPS:
                            if (mGroupList != null && mGroupList.size() > 0) {
                                responseData3();
                            }
                            break;

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public void setBarTextColor() {
        StatusBarUtil.setLightMode(getActivity());
    }


    private void getRecentChatAction() {

        mRequestTag = MethodUrl.CHAT_RECENTLY_LIST;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getActivity(), MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_RECENTLY_LIST, map);
    }


    private void getMyFriendsAction() {

        mRequestTag = MethodUrl.CHAT_MY_FRIENDS;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getActivity(), MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_MY_FRIENDS, map);
    }


    private void getMyGroupsAction() {

        mRequestTag = MethodUrl.CHAT_MY_GROUPS;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getActivity(), MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_MY_GROUPS, map);
    }


    @OnClick({R.id.right_img, R.id.right_lay, R.id.notice_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_lay:
                initPopupWindow();
                break;
            case R.id.right_img:
                initPopupWindow();
                break;
            case R.id.notice_layout:
                Intent intent = new Intent(getActivity(), ChatNoticeListActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void showProgress() {
        //mLoadingWindow.show();
    }

    @Override
    public void disimissProgress() {
        //mLoadingWindow.cancleView();
    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        mLoadingWindow.cancleView();
        Intent intent;
        switch (mType) {
            case MethodUrl.CHAT_QUERY_RCID:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        //启动聊天
                        if (RongIM.getInstance() != null) {
                            RongIM.getInstance().startPrivateChat(getActivity(), tData.get("data") + "", friendName);
                        }
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
            case MethodUrl.CHAT_MY_FRIENDS:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (UtilTools.empty(tData.get("data") + "")) {
                            mPageView.showEmpty();
                        } else {
                            Map<String, Object> mapData = (Map<String, Object>) tData.get("data");
                            if (!UtilTools.empty(mapData)) {
                                if (isShow.equals("1")){
                                    if (UtilTools.empty(mapData.get("apply") + "")) {
                                        noticeLayout.setVisibility(View.GONE);
                                    } else {
                                        noticeLayout.setVisibility(View.VISIBLE);
                                        if ((mapData.get("apply")+"").equals("0")){
                                            noticeNumberTv.setVisibility(View.GONE);
                                        }else {
                                            noticeNumberTv.setVisibility(View.VISIBLE);
                                        }
                                        noticeNumberTv.setText(mapData.get("apply") + "");
                                    }
                                }

                                if (UtilTools.empty(mapData.get("friend") + "")) {
                                    mPageView.showEmpty();
                                } else {
                                    mFriendList = (List<Map<String, Object>>) mapData.get("friend");
                                    if (UtilTools.empty(mFriendList)) {
                                        mPageView.showEmpty();
                                    } else {
                                        for (Map<String,Object> map :mFriendList){
                                            //更新好友信息
                                            IMManager.getInstance().updateUserInfoCache(map.get("rc_id") + "", map.get("name") + "", Uri.parse(map.get("portrait")+""));
                                        }

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
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        mPageView.showNetworkError();
                        break;

                    case "1": //token过期
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;

            case MethodUrl.CHAT_RECENTLY_LIST:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (UtilTools.empty(tData.get("data") + "")) {
                            mPageView.showEmpty();
                        } else {
                            mRecentlyList = (List<Map<String, Object>>) tData.get("data");
                            if (UtilTools.empty(mRecentlyList)) {
                                mPageView.showEmpty();
                            } else {
                                List<Map<String, Object>> topList = new ArrayList<>();
                                List<Map<String, Object>> normalList = new ArrayList<>();
                                for (Map<String, Object> map : mRecentlyList) {
                                    map.put("account", "0");

                                    if ((map.get("top") + "").equals("1")) {
                                        topList.add(map);
                                    } else {
                                        normalList.add(map);
                                    }
                                    if ((map.get("type") + "").equals("1")) {
                                        RongIM.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, map.get("rc_id") + "", new RongIMClient.ResultCallback<Integer>() {
                                            @Override
                                            public void onSuccess(Integer integer) {
                                                if (integer > 99) {
                                                    map.put("account", "99+");
                                                } else {
                                                    map.put("account", integer + "");
                                                }

                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mListAdapter2.notifyDataSetChanged();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onError(RongIMClient.ErrorCode errorCode) {

                                            }
                                        });
                                    } else {
                                        //异步回调
                                        RongIM.getInstance().getUnreadCount(Conversation.ConversationType.GROUP, map.get("id") + "", new RongIMClient.ResultCallback<Integer>() {
                                            @Override
                                            public void onSuccess(Integer integer) {
                                                if (integer > 99) {
                                                    map.put("account", "99+");
                                                } else {
                                                    map.put("account", integer + "");
                                                }

                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mListAdapter2.notifyDataSetChanged();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onError(RongIMClient.ErrorCode errorCode) {

                                            }
                                        });
                                    }
                                }
                                mRecentlyList.clear();
                                mRecentlyList.addAll(topList);
                                mRecentlyList.addAll(normalList);

                                mPageView.showContent();
                                responseData2();
                                mRefreshListView.refreshComplete(10);
                            }
                        }

                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;


            case MethodUrl.CHAT_MY_GROUPS:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (UtilTools.empty(tData.get("data") + "")) {
                            mPageView.showEmpty();
                        } else {
                            mGroupList = (List<Map<String, Object>>) tData.get("data");
                            if (UtilTools.empty(mGroupList)) {
                                mPageView.showEmpty();
                            } else {
                                for (Map<String, Object> map : mGroupList) {
                                    map.put("account", "0");
                                    // 更新 IMKit 缓存群组数据
                                    IMManager.getInstance().updateGroupInfoCache(map.get("id") + "", map.get("name") + "", Uri.parse(map.get("portrait")+""));
                                }
                                mPageView.showContent();
                                responseData3();
                                mRefreshListView.refreshComplete(10);
                            }
                        }

                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                        intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        mLoadingWindow.cancleView();
        dealFailInfo(map, mType);
    }


    //朋友列表
    private void responseData() {
        if (mListAdapter == null) {
            mListAdapter = new MyFriendListAdapter(getActivity());
            mListAdapter.addAll(mFriendList);

            /*AnimationAdapter adapter = new ScaleInAnimationAdapter(mDataAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(.5f));*/

            mLRecyclerViewAdapter1 = new LRecyclerViewAdapter(mListAdapter);

//            SampleHeader headerView = new SampleHeader(BankCardActivity.this, R.layout.item_bank_bind);
//            mLRecyclerViewAdapter.addHeaderView(headerView);

            mRefreshListView.setAdapter(mLRecyclerViewAdapter1);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(true);


        } else {
            mListAdapter.clear();
            mListAdapter.addAll(mFriendList);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter1);
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
        if (mFriendList.size() < 10) {
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


        mListAdapter.setSourceList(mListAdapter.getDataList());

        mListAdapter.setmListener(new OnChildClickListener() {
            @Override
            public void onChildClickListener(View view, int position, Map<String, Object> mParentMap) {
                //启动聊天
                if (RongIM.getInstance() != null) {
                    RongIM.getInstance().startPrivateChat(getActivity(), mParentMap.get("rc_id") + "", mParentMap.get("name") + "");
                }
            }
        });
    }


    //最近聊天
    private void responseData2() {
        if (mListAdapter2 == null) {
            mListAdapter2 = new MyRecentChatListAdapter(getActivity());
            mListAdapter2.addAll(mRecentlyList);

            /*AnimationAdapter adapter = new ScaleInAnimationAdapter(mDataAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(.5f));*/

            mLRecyclerViewAdapter2 = new LRecyclerViewAdapter(mListAdapter2);

//            SampleHeader headerView = new SampleHeader(BankCardActivity.this, R.layout.item_bank_bind);
//            mLRecyclerViewAdapter.addHeaderView(headerView);

            mRefreshListView.setAdapter(mLRecyclerViewAdapter2);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(true);


        } else {
            mListAdapter2.clear();
            mListAdapter2.addAll(mRecentlyList);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter2);
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
        if (mRecentlyList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
            mPage++;
        }

        mRefreshListView.refreshComplete(10);
        mListAdapter2.notifyDataSetChanged();
        if (mListAdapter2.getDataList().size() <= 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }

        mListAdapter2.setSourceList(mListAdapter2.getDataList());

        mListAdapter2.setmListener(new OnChildClickListener() {
            @Override
            public void onChildClickListener(View view, int position, Map<String, Object> mParentMap) {
                if ((mParentMap.get("type") + "").equals("1")) {
                    //根据用户id获取rcid
                    friendName = mParentMap.get("name") + "";
                    //getRcIdAccordingIdAction(mParentMap.get("id")+"");
                    //启动聊天
                    RongIM.getInstance().startPrivateChat(getActivity(), mParentMap.get("rc_id") + "", friendName);
                } else {
                    RongIM.getInstance().startGroupChat(getActivity(), mParentMap.get("id") + "", mParentMap.get("name") + "");
                }
            }
        });
    }

    private void getRcIdAccordingIdAction(String id) {
        mRequestTag = MethodUrl.CHAT_QUERY_RCID;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(getActivity(), MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("id", id);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_QUERY_RCID, map);

    }


    private void responseData3() {
        if (mListAdapter3 == null) {
            mListAdapter3 = new MyRecentChatListAdapter(getActivity());
            mListAdapter3.addAll(mGroupList);

            /*AnimationAdapter adapter = new ScaleInAnimationAdapter(mDataAdapter);
            adapter.setFirstOnly(false);
            adapter.setDuration(500);
            adapter.setInterpolator(new OvershootInterpolator(.5f));*/

            mLRecyclerViewAdapter3 = new LRecyclerViewAdapter(mListAdapter3);

//            SampleHeader headerView = new SampleHeader(BankCardActivity.this, R.layout.item_bank_bind);
//            mLRecyclerViewAdapter.addHeaderView(headerView);

            mRefreshListView.setAdapter(mLRecyclerViewAdapter3);
            mRefreshListView.setItemAnimator(new DefaultItemAnimator());
            mRefreshListView.setHasFixedSize(true);
            mRefreshListView.setNestedScrollingEnabled(false);

            mRefreshListView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
            mRefreshListView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

            mRefreshListView.setPullRefreshEnabled(true);
            mRefreshListView.setLoadMoreEnabled(true);


        } else {
            mListAdapter3.clear();
            mListAdapter3.addAll(mGroupList);
            mRefreshListView.setAdapter(mLRecyclerViewAdapter3);
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
        if (mGroupList.size() < 10) {
            mRefreshListView.setNoMore(true);
        } else {
            mRefreshListView.setNoMore(false);
            mPage++;
        }

        mRefreshListView.refreshComplete(10);
        mListAdapter3.notifyDataSetChanged();
        if (mListAdapter3.getDataList().size() <= 0) {
            mPageView.showEmpty();
        } else {
            mPageView.showContent();
        }

        mListAdapter3.setSourceList(mListAdapter3.getDataList());

        mListAdapter3.setmListener(new OnChildClickListener() {
            @Override
            public void onChildClickListener(View view, int position, Map<String, Object> mParentMap) {

                //RongIM.getInstance().startGroupChat(getActivity(), mParentMap.get("id") + "", mParentMap.get("name") + "");

                // 更新 IMKit 缓存群组数据
                IMManager.getInstance().updateGroupInfoCache(mParentMap.get("id") + "", mParentMap.get("name") + "", Uri.parse(mParentMap.get("portrait")+""));
                RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.GROUP, mParentMap.get("id") + "", mParentMap.get("name") + "");
            }
        });
    }


    private View popView;
    private PopupWindow mConditionDialog;
    private boolean bright = false;

    private void initPopupWindow() {

        int nH = UtilTools.getNavigationBarHeight(getActivity());
        LinearLayout mNagView;

        if (mConditionDialog == null) {

            popView = LayoutInflater.from(getActivity()).inflate(R.layout.chat_add_dialog, null);
            mConditionDialog = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mConditionDialog.setClippingEnabled(false);
            initConditionDialog(popView);


            //int screenWidth=UtilTools.getScreenWidth(getActivity());
            //int screenHeight=UtilTools.getScreenHeight(getActivity());
            mConditionDialog.setWidth(UtilTools.dip2px(getActivity(), 150));
            mConditionDialog.setHeight(UtilTools.dip2px(getActivity(), 180));

            //设置background后在外点击才会消失
            mConditionDialog.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), UtilTools.dip2px(getActivity(), 5)));
            //mConditionDialog.setOutsideTouchable(true);// 设置可允许在外点击消失
            //自定义动画
            //mConditionDialog.setAnimationStyle(R.style.PopupAnimation);
            mConditionDialog.setAnimationStyle(android.R.style.Animation_Activity);//使用系统动画
            mConditionDialog.update();
            mConditionDialog.setTouchable(true);
            mConditionDialog.setFocusable(true);
            //popView.requestFocus();//pop设置不setBackgroundDrawable情况，把焦点给popView，添加popView.setOnKeyListener。可实现点击外部不消失，点击反键才消失
            //			mConditionDialog.showAtLocation(mCityTv, Gravity.TOP|Gravity.RIGHT, 0, 0); //设置layout在PopupWindow中显示的位置
            //mConditionDialog.showAtLocation(getActivity().getWindow().getDecorView(),  Gravity.TOP|Gravity.RIGHT, 0, 0);
            mConditionDialog.showAsDropDown(divideLine, -UtilTools.dip2px(getActivity(), 20), 0, Gravity.RIGHT);
            toggleBright();
            mConditionDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    toggleBright();
                }
            });
        } else {

            //mConditionDialog.showAtLocation(getActivity().getWindow().getDecorView(),  Gravity.TOP|Gravity.RIGHT, 0, 0);
            mConditionDialog.showAsDropDown(divideLine, -UtilTools.dip2px(getActivity(), 20), 0, Gravity.RIGHT);
            toggleBright();
        }


    }

    private void toggleBright() {
        //三个参数分别为： 起始值 结束值 时长 那么整个动画回调过来的值就是从0.5f--1f的
        mAnimUtil.setValueAnimator(0.7f, 1f, 300);
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
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alpha;// 0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }


    LinearLayout group_chat_lay;
    LinearLayout add_friend_lay;
    LinearLayout sacn_lay;

    private void initConditionDialog(View view) {
        group_chat_lay = view.findViewById(R.id.group_chat_lay);
        add_friend_lay = view.findViewById(R.id.add_friend_lay);
        sacn_lay = view.findViewById(R.id.scan_lay);

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (v.getId()) {
                    case R.id.group_chat_lay:
                        mConditionDialog.dismiss();
                        /*intent = new Intent(getActivity(), SelectCreateGroupActivity.class);
                        startActivityForResult(intent, REQUEST_START_GROUP);*/
                        intent = new Intent(getActivity(), SelectContractListActivity.class);
                        intent.putExtra("TYPE", "0");
                        startActivity(intent);
                        break;
                    case R.id.add_friend_lay:
                        mConditionDialog.dismiss();
                        intent = new Intent(getActivity(), AddFriendActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.scan_lay:
                        mConditionDialog.dismiss();
                        PermissionsUtils.requsetRunPermission(getActivity(), new RePermissionResultBack() {
                            @Override
                            public void requestSuccess() {
                                Intent intent = new Intent(getActivity(), TestScanActivity.class);
                                intent.putExtra("type", "0");
                                startActivity(intent);
                            }

                            @Override
                            public void requestFailer() {
                                Toast.makeText(getActivity(), "相机权限授权失败", Toast.LENGTH_LONG).show();
                            }
                        }, Permission.Group.STORAGE, Permission.Group.CAMERA);
                        /*intent = new Intent(getActivity(), ScanActivity.class);
                        startActivity(intent);*/
                        break;
                }
            }
        };

        group_chat_lay.setOnClickListener(onClickListener);
        add_friend_lay.setOnClickListener(onClickListener);
        sacn_lay.setOnClickListener(onClickListener);
    }


    @Override
    public void reLoadingData() {
        switch (mRequestTag) {
          /*  case MethodUrl.CHAT_RECENTLY_LIST:
                showProgress();
                getRecentChatAction();
                break;*/
            case MethodUrl.CHAT_MY_FRIENDS:
                showProgress();
                getMyFriendsAction();
                break;
            case MethodUrl.CHAT_MY_GROUPS:
                showProgress();
                getMyGroupsAction();
                break;

        }
    }


}
