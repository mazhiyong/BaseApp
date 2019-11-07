package com.lr.biyou.ui.moudle2.fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.androidkun.xtablayout.XTabLayout;
import com.flyco.dialog.utils.CornerUtils;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.jaeger.library.StatusBarUtil;
import com.king.zxing.Intents;
import com.lr.biyou.R;
import com.lr.biyou.basic.BasicFragment;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.chatry.task.UserTask;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.ui.moudle2.activity.ChatNoticeListActivity;
import com.lr.biyou.ui.moudle2.adapter.MyFriendListAdapter;
import com.lr.biyou.ui.moudle2.adapter.MyRecentChatListAdapter;
import com.lr.biyou.ui.moudle4.adapter.MyViewPagerAdapter;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.AnimUtil;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.IMConnectionStatusViewModel;
import cn.wildfire.chat.kit.IMServiceStatusViewModel;
import cn.wildfire.chat.kit.WfcScheme;
import cn.wildfire.chat.kit.channel.ChannelViewModel;
import cn.wildfire.chat.kit.common.OperateResult;
import cn.wildfire.chat.kit.contact.ContactListFragment;
import cn.wildfire.chat.kit.contact.ContactViewModel;
import cn.wildfire.chat.kit.contact.newfriend.SearchUserActivity;
import cn.wildfire.chat.kit.conversation.CreateConversationActivity;
import cn.wildfire.chat.kit.conversationlist.ConversationListFragment;
import cn.wildfire.chat.kit.conversationlist.ConversationListViewModel;
import cn.wildfire.chat.kit.group.GroupInfoActivity;
import cn.wildfire.chat.kit.group.GroupListFragment;
import cn.wildfire.chat.kit.qrcode.ScanQRCodeActivity;
import cn.wildfire.chat.kit.search.SearchResult;
import cn.wildfire.chat.kit.search.SearchResultAdapter;
import cn.wildfire.chat.kit.search.SearchViewModel;
import cn.wildfire.chat.kit.search.SearchableModule;
import cn.wildfire.chat.kit.search.module.ContactSearchModule;
import cn.wildfire.chat.kit.search.module.ConversationSearchModule;
import cn.wildfire.chat.kit.search.module.GroupSearchViewModule;
import cn.wildfire.chat.kit.user.ChangeMyNameActivity;
import cn.wildfire.chat.kit.user.UserInfoActivity;
import cn.wildfire.chat.kit.user.UserViewModel;
import cn.wildfirechat.client.ConnectionStatus;
import cn.wildfirechat.remote.ChatManager;
import q.rorbin.badgeview.QBadgeView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

@SuppressLint("ValidFragment")
public class ChatViewFragment extends BasicFragment implements RequestView, ReLoadingData,ViewPager.OnPageChangeListener {


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
    RecyclerView mRefreshListView;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.title)
    TextView Title;


    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    private boolean mIsback = false;

    private String mRequestTag = "";
    private String isShow="-1";

    private List<Fragment> mFragments = new ArrayList<>();
    //会话界面
    private ConversationListFragment conversationListFragment ;
    //联系人界面
    private ContactListFragment contactListFragment;
    //群组界面
    private GroupListFragment groupListFragment;

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


    private UserTask userTask;
    private String friendName;

    //检索
    private  List<SearchableModule> modules = new ArrayList<>();
    private SearchResultAdapter adapter;
    private SearchViewModel searchViewModel;
    private Observer<SearchResult> searchResultObserver = this::onSearchResult;



    //未读消息
    private QBadgeView unreadMessageUnreadBadgeView;
    //好友申请消息
    private QBadgeView unreadFriendRequestBadgeView;

    private static final int REQUEST_CODE_SCAN_QR_CODE = 100;
    private static final int REQUEST_IGNORE_BATTERY_CODE = 101;

    private boolean isInitialized = false;

    //频道
    private ChannelViewModel channelViewModel;
    private boolean isFollowed = false;

    private ContactViewModel contactViewModel;
    private ConversationListViewModel conversationListViewModel;

    private Observer<Boolean> imStatusLiveDataObserver = status -> {
        if (status && !isInitialized) {
            //init();
            isInitialized = true;
        }
    };

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

        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        searchViewModel.getResultLiveData().observeForever(searchResultObserver);

        //搜索联系人Module
        SearchableModule module = new ContactSearchModule();
        modules.add(module);

        //搜索群组Module
        module = new GroupSearchViewModule();
        modules.add(module);

        //搜索会话列表Module
        module = new ConversationSearchModule();
        modules.add(module);
    }

    @Override
    public void onResume() {
        super.onResume();
        isConnncted();
        if (contactViewModel != null) {
            contactViewModel.reloadFriendRequestStatus();
            conversationListViewModel.reloadConversationUnreadStatus();
        }

        if (channelViewModel == null){
            channelViewModel = ViewModelProviders.of(this).get(ChannelViewModel.class);
        }

        isFollowed = channelViewModel.isListenedChannel("EjELELbb");
        if (isFollowed){
            LogUtilDebug.i("show","已经订阅当前频道");
        }else {
            LogUtilDebug.i("show","未订阅当前频道");
            channelViewModel.listenChannel("EjELELbb", true).observe(this, new Observer<OperateResult<Boolean>>() {
                @Override
                public void onChanged(@Nullable OperateResult<Boolean> booleanOperateResult) {
                    if (booleanOperateResult.isSuccess()) {
                        LogUtilDebug.i("show","成功订阅当前频道");
                    } else {
                        LogUtilDebug.i("show","订阅当前频道失败");
                    }
                }
            });
        }



    }

    private void isConnncted() {
        IMServiceStatusViewModel imServiceStatusViewModel = ViewModelProviders.of(this).get(IMServiceStatusViewModel.class);
        imServiceStatusViewModel.imServiceStatusLiveData().observe(this, imStatusLiveDataObserver);
        IMConnectionStatusViewModel connectionStatusViewModel = ViewModelProviders.of(this).get(IMConnectionStatusViewModel.class);
        connectionStatusViewModel.connectionStatusLiveData().observe(this, status -> {
            if (status == ConnectionStatus.ConnectionStatusTokenIncorrect || status == ConnectionStatus.ConnectionStatusSecretKeyMismatch || status == ConnectionStatus.ConnectionStatusRejected || status == ConnectionStatus.ConnectionStatusLogout) {
                LogUtilDebug.i("show","重新连接聊天服务器");
                ChatManager.Instance().disconnect(true);
                //重新连接登录
                if (UtilTools.empty(MbsConstans.RONGYUN_MAP)) {
                    String s = SPUtils.get(getActivity(), MbsConstans.SharedInfoConstans.RONGYUN_DATA,"").toString();
                    MbsConstans.RONGYUN_MAP = JSONUtil.getInstance().jsonMap(s);
                }
                ChatManagerHolder.gChatManager.connect(MbsConstans.RONGYUN_MAP.get("id")+"", MbsConstans.RONGYUN_MAP.get("token")+"");
            }else {
                LogUtilDebug.i("show","已经连接聊天服务器");
            }
        });
    }

    private void initView() {

        mPageView.setContentView(mContent);
        mPageView.showContent();

        //tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("近期聊天"));
        tabLayout.addTab(tabLayout.newTab().setText("我的好友"));
        tabLayout.addTab(tabLayout.newTab().setText("我的群组"));

        conversationListFragment = new ConversationListFragment();
        contactListFragment = new ContactListFragment();
        groupListFragment = new GroupListFragment();


        //mFragments.clear();
        mFragments.add(conversationListFragment);
        mFragments.add(contactListFragment);
        mFragments.add(groupListFragment);

        viewPager.setAdapter(new MyViewPagerAdapter(getChildFragmentManager(), mFragments));

        viewPager.addOnPageChangeListener(new XTabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence sequence, int start, int before, int count) {
                //输入检索内容
                if (sequence.length() > 0) {
                    if (adapter != null) {
                        adapter.reset();
                    }
                    searchViewModel.search(sequence.toString(), modules);
                }else {
                    mPageView.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (checkDisplayName()) {
            ignoreBatteryOption();
        }



    }


    private void onSearchResult(SearchResult result) {
        if (result == null) {
            mPageView.setVisibility(View.GONE);
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
        } else {
            mPageView.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            if (adapter == null) {
                adapter = new SearchResultAdapter(this);
                mRefreshListView.setAdapter(adapter);
                mRefreshListView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
            adapter.submitSearResult(result);
        }

    }



    private boolean checkDisplayName() {
       /* UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        SharedPreferences sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        cn.wildfirechat.model.UserInfo userInfo = userViewModel.getUserInfo(userViewModel.getUserId(), false);
        if (userInfo != null && TextUtils.equals(userInfo.displayName, userInfo.mobile)) {
            if (!sp.getBoolean("updatedDisplayName", false)) {
                sp.edit().putBoolean("updatedDisplayName", true).apply();
                updateDisplayName();
                return false;
            }
        }*/
        return true;
    }

    private void updateDisplayName() {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .content("修改个人昵称？")
                .positiveText("修改")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(getActivity(), ChangeMyNameActivity.class);
                        startActivity(intent);
                    }
                }).build();
        dialog.show();
    }

    private void ignoreBatteryOption() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Intent intent = new Intent();
                String packageName = getActivity().getPackageName();
                PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivityForResult(intent, REQUEST_IGNORE_BATTERY_CODE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showUnreadMessageBadgeView(int count) {
       /* if (unreadMessageUnreadBadgeView == null) {
            BottomNavigationMenuView bottomNavigationMenuView = ((BottomNavigationMenuView) bottomNavigationView.getChildAt(0));
            View view = bottomNavigationMenuView.getChildAt(0);
            unreadMessageUnreadBadgeView = new QBadgeView(getActivity());
            unreadMessageUnreadBadgeView.bindTarget(view);
        }
        unreadMessageUnreadBadgeView.setBadgeNumber(count);*/
    }

    private void hideUnreadMessageBadgeView() {
        if (unreadMessageUnreadBadgeView != null) {
            unreadMessageUnreadBadgeView.hide(true);
            unreadFriendRequestBadgeView = null;
        }
    }


    private void showUnreadFriendRequestBadgeView(int count) {
       /* if (unreadFriendRequestBadgeView == null) {
            BottomNavigationMenuView bottomNavigationMenuView = ((BottomNavigationMenuView) bottomNavigationView.getChildAt(0));
            View view = bottomNavigationMenuView.getChildAt(1);
            unreadFriendRequestBadgeView = new QBadgeView(getActivity());
            unreadFriendRequestBadgeView.bindTarget(view);
        }
        unreadFriendRequestBadgeView.setBadgeNumber(count);*/
    }

    public void hideUnreadFriendRequestBadgeView() {
        if (unreadFriendRequestBadgeView != null) {
            unreadFriendRequestBadgeView.hide(true);
            unreadFriendRequestBadgeView = null;
        }
    }

    public void setBarTextColor() {
        StatusBarUtil.setLightMode(getActivity());
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        LogUtilDebug.i("show","onPageScrollStateChanged()....");
        if (state != ViewPager.SCROLL_STATE_IDLE) {
            //滚动过程中隐藏快速导航条
            contactListFragment.showQuickIndexBar(false);
        } else {
            contactListFragment.showQuickIndexBar(true);
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

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map, mType);
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
                    case R.id.group_chat_lay: //创建群聊
                        mConditionDialog.dismiss();
                        intent = new Intent(getActivity(), CreateConversationActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.add_friend_lay://添加好友
                        mConditionDialog.dismiss();
                        intent = new Intent(getActivity(), SearchUserActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.scan_lay://扫一扫
                        mConditionDialog.dismiss();
                        PermissionsUtils.requsetRunPermission(getActivity(), new RePermissionResultBack() {
                            @Override
                            public void requestSuccess() {
                                startActivityForResult(new Intent(getActivity(), ScanQRCodeActivity.class), REQUEST_CODE_SCAN_QR_CODE);
                            }

                            @Override
                            public void requestFailer() {
                                Toast.makeText(getActivity(), "相机权限授权失败", Toast.LENGTH_LONG).show();
                            }
                        }, Permission.Group.STORAGE, Permission.Group.CAMERA);

                        break;
                }
            }
        };

        group_chat_lay.setOnClickListener(onClickListener);
        add_friend_lay.setOnClickListener(onClickListener);
        sacn_lay.setOnClickListener(onClickListener);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SCAN_QR_CODE:
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra(Intents.Scan.RESULT);
                    onScanPcQrCode(result);
                }
                break;
            case REQUEST_IGNORE_BATTERY_CODE:
                if (resultCode == RESULT_CANCELED) {

                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void onScanPcQrCode(String qrcode) {
        String prefix = qrcode.substring(0, qrcode.lastIndexOf('/') + 1);
        String value = qrcode.substring(qrcode.lastIndexOf("/") + 1);
        switch (prefix) {
            case WfcScheme.QR_CODE_PREFIX_PC_SESSION:
                // pcLogin(value); //pc登录
                break;
            case WfcScheme.QR_CODE_PREFIX_USER: //扫码加好友
                showUser(value);
                break;
            case WfcScheme.QR_CODE_PREFIX_GROUP: //扫码入群
                joinGroup(value);
                break;
            case WfcScheme.QR_CODE_PREFIX_CHANNEL://扫码加入频道
                //subscribeChannel(value);
                break;
            default:

                break;
        }
    }

    private void showUser(String uid) {

        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        cn.wildfirechat.model.UserInfo userInfo = userViewModel.getUserInfo(uid, true);
        if (userInfo == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        intent.putExtra("userInfo", userInfo);
        startActivity(intent);
    }

    private void joinGroup(String groupId) {
        Intent intent = new Intent(getActivity(), GroupInfoActivity.class);
        intent.putExtra("groupId", groupId);
        startActivity(intent);
    }



    @Override
    public void reLoadingData() {


    }

    @Override
    public void onDestroy() {
        searchViewModel.getResultLiveData().removeObserver(searchResultObserver);
        super.onDestroy();
    }

}
