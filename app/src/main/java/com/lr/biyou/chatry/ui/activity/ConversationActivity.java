package com.lr.biyou.chatry.ui.activity;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.flyco.dialog.utils.CornerUtils;
import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.bean.MessageEvent;
import com.lr.biyou.chatry.common.IntentExtra;
import com.lr.biyou.chatry.common.ThreadManager;
import com.lr.biyou.chatry.db.DbManager;
import com.lr.biyou.chatry.db.dao.GroupMemberDao;
import com.lr.biyou.chatry.db.dao.UserDao;
import com.lr.biyou.chatry.db.model.GroupMemberInfoEntity;
import com.lr.biyou.chatry.im.IMManager;
import com.lr.biyou.chatry.model.Resource;
import com.lr.biyou.chatry.model.ScreenCaptureResult;
import com.lr.biyou.chatry.model.Status;
import com.lr.biyou.chatry.model.TypingInfo;
import com.lr.biyou.chatry.model.UserSimpleInfo;
import com.lr.biyou.chatry.ui.fragment.ConversationFragmentEx;
import com.lr.biyou.chatry.ui.view.AnnouceView;
import com.lr.biyou.chatry.utils.RongGenerate;
import com.lr.biyou.chatry.utils.ScreenCaptureUtil;
import com.lr.biyou.chatry.utils.SearchUtils;
import com.lr.biyou.chatry.utils.log.SLog;
import com.lr.biyou.chatry.viewmodel.ConversationViewModel;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.ui.moudle2.activity.ChatItemActivity;
import com.lr.biyou.ui.moudle2.activity.GroupChatItemActivity;
import com.lr.biyou.ui.moudle2.activity.SelectContractListActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.tool.AnimUtil;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.UtilTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.callkit.util.SPUtils;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongKitIntent;
import io.rong.imkit.mention.RongMentionManager;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * 会话页面
 */
public class ConversationActivity extends BasicActivity {

    @BindView(R.id.back_img)
    ImageView backImg;
    @BindView(R.id.back_text)
    TextView backText;
    @BindView(R.id.left_back_lay)
    LinearLayout leftBackLay;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_text_tv)
    TextView rightTextTv;
    @BindView(R.id.right_img)
    ImageView rightImg;
    @BindView(R.id.right_lay)
    LinearLayout rightLay;
    @BindView(R.id.divide_line)
    View divideLine;
    @BindView(R.id.rl_lay)
    RelativeLayout rlLay;
    private String TAG = ConversationActivity.class.getSimpleName();
    private ConversationFragmentEx fragment;
    private AnnouceView annouceView;
    private ConversationViewModel conversationViewModel;
    private String title;
    private int screenCaptureStatus;

    /**
     * 对方id
     */

    public String targetId;
    public String Id;

    public String targetId2;
    private Map<Object, Object> mapMessage;

    //艾特群成员
    private final int REQUEST_AI_TE = 99;

    //群组成员
    private List<Map<String, Object>> memberList;
    /**
     * 会话类型
     */
    public Conversation.ConversationType conversationType;
    private ScreenCaptureUtil screenCaptureUtil;

    private Handler handler2 = new Handler();

    private String mRequestTag = "";

    private AnimUtil mAnimUtil;

    //HTTP请求  轮询
    private Runnable cnyRunnable = new Runnable() {
        @Override
        public void run() {
            // 统计聊天时长
            tongjiChatTimeAction();
            handler2.postDelayed(this, MbsConstans.SECOND_TIME_30);
        }
    };


    private void tongjiChatTimeAction() {
        mRequestTag = MethodUrl.CHAT_TIME_LONG;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(ConversationActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_TIME_LONG, map);
    }


    @Override
    public int getContentView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        return R.layout.conversation_activity_conversation;
    }


    @Override
    public void init() {
        // 没有intent 的则直接返回
        Intent intent = getIntent();
        if (intent == null || intent.getData() == null) {
            finish();
            return;
        }

        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }

        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        targetId = intent.getData().getQueryParameter("targetId");
        IMManager.getInstance().tarId = targetId;

        conversationType = Conversation.ConversationType.valueOf(intent.getData()
                .getLastPathSegment().toUpperCase(Locale.US));
        title = intent.getData().getQueryParameter("title");

        titleText.setText(title);
        titleText.setCompoundDrawables(null, null, null, null);

        rightImg.setVisibility(View.VISIBLE);
        rightImg.setImageResource(R.drawable.icon2_gengduo);

        initView();
        //initViewModel();

        mAnimUtil = new AnimUtil();

        LogUtilDebug.i("show", "状态栏高度:" + UtilTools.getStatusBarHeight());


        //根据rc_id查询用户本地id
        getidFromRcidAction();

        if (conversationType == Conversation.ConversationType.GROUP) {
            //如果是群聊,获取群聊用户信息并刷新
            getGroupInfoAction();
        }



    }











    @Override
    protected void onResume() {
        super.onResume();
        /*getTitleStr(targetId, conversationType, title);
        refreshScreenCaptureStatus();*/

        handler2.post(cnyRunnable);

        IMManager.getInstance().isShowTitleWindow = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (screenCaptureUtil != null) {
            screenCaptureUtil.unRegister();
        }

        if (handler2 != null && cnyRunnable != null) {
            handler2.removeCallbacks(cnyRunnable);
        }
    }

    @OnClick({R.id.left_back_lay, R.id.right_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back_lay:
                finish();
                break;
            case R.id.right_lay:

                if (conversationType == Conversation.ConversationType.PUBLIC_SERVICE
                        || conversationType == Conversation.ConversationType.APP_PUBLIC_SERVICE) {

                    RongIM.getInstance().startPublicServiceProfile(this, conversationType, targetId);
                } else if (conversationType == Conversation.ConversationType.PRIVATE) {
                    //私聊设置
                    Intent intent = new Intent(ConversationActivity.this, ChatItemActivity.class);
                    intent.putExtra(IntentExtra.STR_TARGET_ID, targetId);
                    intent.putExtra(IntentExtra.SERIA_CONVERSATION_TYPE, Conversation.ConversationType.PRIVATE);
                    startActivity(intent);
                } else if (conversationType == Conversation.ConversationType.GROUP) {
                    //群聊设置
                    Intent intent = new Intent(this, GroupChatItemActivity.class);
                    intent.putExtra(IntentExtra.STR_TARGET_ID, targetId);
                    intent.putExtra(IntentExtra.SERIA_CONVERSATION_TYPE, Conversation.ConversationType.GROUP);
                    startActivity(intent);
                } else if (conversationType == Conversation.ConversationType.DISCUSSION) {
                    //讨论组
                }

                break;
        }
    }


    private void refreshScreenCaptureStatus() {
        //私聊或者群聊才开启功能
        if (conversationType.equals(Conversation.ConversationType.PRIVATE) || conversationType.equals(Conversation.ConversationType.GROUP)) {
            int cacheType = (int) SPUtils.get(this, "ScreenCaptureStatus", 0);
            //1为开启，0为关闭
            if (cacheType == 1) {
                if (screenCaptureUtil == null) {
                    initScreenShotListener();
                } else {
                    screenCaptureUtil.register();
                }
            } else if (cacheType == 0) {
                if (screenCaptureUtil != null) {
                    screenCaptureUtil.unRegister();
                }
            }
        }
    }

    private void initScreenShotListener() {
        screenCaptureUtil = new ScreenCaptureUtil(this);
        screenCaptureUtil.setScreenShotListener(new ScreenCaptureUtil.ScreenShotListener() {
            @Override
            public void onScreenShotComplete(String data, long dateTaken) {
                SLog.d(TAG, "onScreenShotComplete===");
                ThreadManager.getInstance().runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        //在主线程注册 observeForever 因为截屏时候可能使得 activity 处于 pause 状态，无法发送消息
                        LiveData<Resource<Void>> result = conversationViewModel.sendScreenShotMsg(conversationType.getValue(), targetId);
                        result.observeForever(new Observer<Resource<Void>>() {
                            @Override
                            public void onChanged(Resource<Void> voidResource) {
                                if (voidResource.status == Status.SUCCESS) {
                                    result.removeObserver(this);
                                    SLog.d(TAG, "sendScreenShotMsg===Success");
                                } else if (voidResource.status == Status.ERROR) {
                                    result.removeObserver(this);
                                    SLog.d(TAG, "sendScreenShotMsg===Error");
                                }
                            }
                        });
                    }
                });
            }
        });
        screenCaptureUtil.register();
    }

    private void initViewModel() {
        conversationViewModel = ViewModelProviders.of(this, new ConversationViewModel.Factory(targetId, conversationType, title, this.getApplication())).get(ConversationViewModel.class);

        conversationViewModel.getTitleStr().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String title) {
                if (TextUtils.isEmpty(title)) {
                    if (conversationType == null) {
                        return;
                    }
                    int titleResId;
                    if (conversationType.equals(Conversation.ConversationType.DISCUSSION)) {
                        titleResId = R.string.seal_conversation_title_discussion_group;
                    } else if (conversationType.equals(Conversation.ConversationType.SYSTEM)) {
                        titleResId = R.string.seal_conversation_title_system;
                    } else if (conversationType.equals(Conversation.ConversationType.CUSTOMER_SERVICE)) {
                        titleResId = R.string.seal_conversation_title_feedback;
                    } else {
                        titleResId = R.string.seal_conversation_title_defult;
                    }
                    // getTitleBar().setTitle(titleResId);

                } else {
                    //getTitleBar().setTitle(title);
                }
            }
        });

        // 正在输入状态
        conversationViewModel.getTypingStatusInfo().observe(this, new Observer<TypingInfo>() {
            @Override
            public void onChanged(TypingInfo typingInfo) {
                if (typingInfo == null) {
                    return;
                }

                if (typingInfo.conversationType == conversationType && typingInfo.targetId.equals(targetId)) {
                    if (typingInfo.typingList == null) {
                        //getTitleBar().setType(SealTitleBar.Type.NORMAL);
                        titleText.setText(title);
                    } else {
                        TypingInfo.Typing typing = typingInfo.typingList.get(typingInfo.typingList.size() - 1);
                        // getTitleBar().setType(SealTitleBar.Type.TYPING);
                        if (typing.type == TypingInfo.Typing.Type.text) {
                            //getTitleBar().setTyping(R.string.seal_conversation_remote_side_is_typing);
                            titleText.setText(R.string.seal_conversation_remote_side_is_typing);
                        } else if (typing.type == TypingInfo.Typing.Type.voice) {
                            //getTitleBar().setTyping(R.string.seal_conversation_remote_side_speaking);
                            titleText.setText(R.string.seal_conversation_remote_side_speaking);
                        }
                    }
                }
            }
        });

        // 群 @ 跳转
        conversationViewModel.getGroupAt().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                // 跳转选择界面
                Intent intent = new Intent(RongContext.getInstance(), SelectContractListActivity.class);
                intent.putExtra("TYPE", "1");
                intent.putExtra("DATA", (Serializable) memberList);
                startActivityForResult(intent, REQUEST_AI_TE);
                startActivity(intent);


            }
        });

        conversationViewModel.getScreenCaptureStatus(conversationType.getValue(), targetId).observe(this, new Observer<Resource<ScreenCaptureResult>>() {
            @Override
            public void onChanged(Resource<ScreenCaptureResult> screenCaptureResultResource) {
                if (screenCaptureResultResource.status == Status.SUCCESS) {
                    // 0 关闭 1 打开
                    refreshScreenCaptureStatus();
                }
            }
        });
    }

    /**
     * 获取 title
     *
     * @param targetId
     * @param conversationType
     * @param title
     */
    private void getTitleStr(String targetId, Conversation.ConversationType conversationType, String title) {
       /* if (conversationViewModel != null) {
            LogUtilDebug.i("show","tarid:"+targetId);
            conversationViewModel.getTitleByConversation(targetId, conversationType, title);
        }else {
            LogUtilDebug.i("show","null  &&&&&&tarid:"+targetId);
        }*/
    }


    private void initView() {
        initTitleBar(conversationType, targetId);
        initAnnouceView();
        initConversationFragment();
    }

    private void initConversationFragment() {
        /**
         * 加载会话界面 。 ConversationFragmentEx 继承自 ConversationFragment
         */
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment existFragment = fragmentManager.findFragmentByTag(ConversationFragmentEx.class.getCanonicalName());
        if (existFragment != null) {
            fragment = (ConversationFragmentEx) existFragment;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.show(fragment);
            transaction.commitAllowingStateLoss();
        } else {
            fragment = new ConversationFragmentEx();
            // 自定义的服务才会有通知监听
            if (conversationType.equals(Conversation.ConversationType.CUSTOMER_SERVICE)) {
                // 设置通知监听
                fragment.setOnShowAnnounceBarListener(new ConversationFragmentEx.OnShowAnnounceListener() {
                    @Override
                    public void onShowAnnounceView(String announceMsg, final String announceUrl) {
                        annouceView.setVisibility(View.VISIBLE);
                        annouceView.setAnnounce(announceMsg, announceUrl);
                    }
                });
            }

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.rong_content, fragment, ConversationFragmentEx.class.getCanonicalName());
            transaction.commitAllowingStateLoss();
        }

    }

    /**
     * 通知布局
     */
    private void initAnnouceView() {
        // 初始化通知布局
        annouceView = findViewById(R.id.view_annouce);
        annouceView.setVisibility(View.GONE);
        annouceView.setOnAnnounceClickListener(new AnnouceView.OnAnnounceClickListener() {
            @Override
            public void onClick(View v, String url) {
                String str = url.toLowerCase();
                if (!TextUtils.isEmpty(str)) {
                    if (!str.startsWith("http") && !str.startsWith("https")) {
                        str = "http://" + str;
                    }
                    Intent intent = new Intent(RongKitIntent.RONG_INTENT_ACTION_WEBVIEW);
                    intent.setPackage(v.getContext().getPackageName());
                    intent.putExtra("url", str);
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    /**
     * 初始化 title
     *
     * @param conversationType
     * @param targetId
     */
    private void initTitleBar(Conversation.ConversationType conversationType, String targetId) {
        // title 布局设置
        // 左边返回按钮
        /*getTitleBar().setOnBtnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment != null && !fragment.onBackPressed()) {
                    if (fragment.isLocationSharing()) {
                        fragment.showQuitLocationSharingDialog(ConversationActivity.this);
                        return;
                    }
                    hintKbTwo();
                }
                finish();
            }
        });*/


       /* getTitleBar().getBtnRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDetailActivity(conversationType, targetId);
            }
        });*/

       /* if (conversationType.equals(Conversation.ConversationType.GROUP)) {
            getTitleBar().getBtnRight().setImageDrawable(getResources().getDrawable(R.drawable.seal_detail_group));
        } else if (conversationType.equals(Conversation.ConversationType.PRIVATE)
                || conversationType.equals(Conversation.ConversationType.PUBLIC_SERVICE)
                || conversationType.equals(Conversation.ConversationType.APP_PUBLIC_SERVICE)
                || conversationType.equals(Conversation.ConversationType.DISCUSSION)) {
            getTitleBar().getBtnRight().setImageDrawable(getResources().getDrawable(R.drawable.seal_detail_single));
        } else {
            getTitleBar().getBtnRight().setVisibility(View.GONE);
        }*/
    }

    /**
     * 根据 targetid 和 ConversationType 进入到设置页面
     */
    private void toDetailActivity(Conversation.ConversationType conversationType, String targetId) {

        if (conversationType == Conversation.ConversationType.PUBLIC_SERVICE
                || conversationType == Conversation.ConversationType.APP_PUBLIC_SERVICE) {

            RongIM.getInstance().startPublicServiceProfile(this, conversationType, targetId);
        } else if (conversationType == Conversation.ConversationType.PRIVATE) {
            Intent intent = new Intent(this, PrivateChatSettingActivity.class);
            intent.putExtra(IntentExtra.STR_TARGET_ID, targetId);
            intent.putExtra(IntentExtra.SERIA_CONVERSATION_TYPE, Conversation.ConversationType.PRIVATE);
            startActivity(intent);
        } else if (conversationType == Conversation.ConversationType.GROUP) {
            Intent intent = new Intent(this, GroupDetailActivity.class);
            intent.putExtra(IntentExtra.STR_TARGET_ID, targetId);
            intent.putExtra(IntentExtra.SERIA_CONVERSATION_TYPE, Conversation.ConversationType.GROUP);
            startActivity(intent);
        } else if (conversationType == Conversation.ConversationType.DISCUSSION) {

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
            if (fragment != null && !fragment.onBackPressed()) {
                if (fragment.isLocationSharing()) {
                    fragment.showQuitLocationSharingDialog(this);
                    return true;
                }
                finish();
            }
        }
        return false;
    }


    /**
     * 查询id
     */
    public void getidFromRcidAction() {
        mRequestTag = MethodUrl.CHAT_QUERY_ID;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = com.lr.biyou.utils.tool.SPUtils.get(ConversationActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("rc_id", targetId);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_QUERY_ID, map);
    }

    /**
     * 获取群聊信息
     */
    public void getGroupInfoAction() {
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = com.lr.biyou.utils.tool.SPUtils.get(ConversationActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("group_id", targetId);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_GROUPS_INFO, map);
    }


    /**
     * 获取用户信息
     */
    public void getFriendInfoAction() {
        mRequestTag = MethodUrl.CHAT_FRIEDN_INFO;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = com.lr.biyou.utils.tool.SPUtils.get(ConversationActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("id", Id);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_FRIEDN_INFO, map);
    }


    String conText;
    String conId;
    Conversation.ConversationType conType;
    public void sendMessageAction(String text, String ConId, Conversation.ConversationType ConType) {
        conText = text;
        conId = ConId;
        conType = ConType;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = com.lr.biyou.utils.tool.SPUtils.get(ConversationActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("type", "1");
        map.put("content", text + "");
        map.put("receiver_id", Id);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_SEND_NEWS, map);
    }


    public void sendGroupMessageAction(String text, String ConId, Conversation.ConversationType ConType) {
        conText = text;
        conId = ConId;
        conType = ConType;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = com.lr.biyou.utils.tool.SPUtils.get(ConversationActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("content", text);
        map.put("group_id", targetId);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_GROUP_SEND_NEWS, map);
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        switch (mType) {
            case MethodUrl.CHAT_SEND_NEWS:
              /*  switch (tData.get("code") + "") {
                    case "0": //请求成功
                        //发送消息(文本)
                        if (!UtilTools.empty(tData.get("data"))){
                            TextMessage textMessage = TextMessage.obtain(tData.get("data")+"");
                            MentionedInfo mentionedInfo = RongMentionManager.getInstance().onSendButtonClick();

                            if (mentionedInfo != null) {
                                if (mentionedInfo.getMentionedUserIdList().contains("-1")) {
                                    mentionedInfo.setType(MentionedInfo.MentionedType.ALL);
                                } else {
                                    mentionedInfo.setType(MentionedInfo.MentionedType.PART);
                                }
                                textMessage.setMentionedInfo(mentionedInfo);
                            }

                            io.rong.imlib.model.Message message = io.rong.imlib.model.Message.obtain(conId, conType, textMessage);
                            RongIM.getInstance().sendMessage(message, null, null, (IRongCallback.ISendMessageCallback) null);
                        }

                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        Intent intent = new Intent(ConversationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;*/

            case MethodUrl.CHAT_GROUP_SEND_NEWS:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data"))){
                            TextMessage textMessage = TextMessage.obtain(tData.get("data")+"");
                            MentionedInfo mentionedInfo = RongMentionManager.getInstance().onSendButtonClick();
                            if (mentionedInfo != null) {
                                if (mentionedInfo.getMentionedUserIdList().contains("-1")) {
                                    mentionedInfo.setType(MentionedInfo.MentionedType.ALL);
                                } else {
                                    mentionedInfo.setType(MentionedInfo.MentionedType.PART);
                                }
                                textMessage.setMentionedInfo(mentionedInfo);
                            }

                            Message message = Message.obtain(conId, conType, textMessage);
                            RongIM.getInstance().sendMessage(message, null, null, (IRongCallback.ISendMessageCallback) null);
                        }

                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        Intent intent = new Intent(ConversationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
            case MethodUrl.CHAT_QUERY_ID:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            Id = tData.get("data") + "";
                            getFriendInfoAction();
                        }


                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        Intent intent = new Intent(ConversationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;
            case MethodUrl.CHAT_FRIEDN_INFO:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            Map<String, Object> map = (Map<String, Object>) tData.get("data");
                            if (!UtilTools.empty(map)) {
                                IMManager.getInstance().updateUserInfoCache(map.get("rc_id") + "",
                                        map.get("name") + "",
                                        Uri.parse(map.get("portrait") + ""));
                            }
                        }

                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        Intent intent = new Intent(ConversationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }

                break;
            case MethodUrl.CHAT_TIME_LONG:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        break;
                    case "-1": //请求失败
                        //showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        Intent intent = new Intent(ConversationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;

            case MethodUrl.CHAT_QUERY_USERINFO:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            Map<String, Object> map = (Map<String, Object>) tData.get("data");
                            mapMessage.put("name", map.get("name") + "");
                            mapMessage.put("image", map.get("portrait") + "");
                        }
                        initPopupWindow();
                        break;
                    case "-1": //请求失败
                        showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        closeAllActivity();
                        Intent intent = new Intent(ConversationActivity.this, LoginActivity.class);
                        startActivity(intent);

                        break;

                }
                break;
            case MethodUrl.CHAT_GROUPS_INFO:
                switch (tData.get("code") + "") {
                    case "0":
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            Map<String, Object> map = (Map<String, Object>) tData.get("data");
                            if (!UtilTools.empty(map) && !UtilTools.empty(map.get("member") + "")) {
                                // 更新 IMKit 缓存群组成员数据
                                memberList = (List<Map<String, Object>>) map.get("member");
                               /* if (memberList != null && memberList.size()> 0){
                                    for (Map<String,Object> mapMember:memberList){
                                        IMManager.getInstance().updateGroupMemberInfoCache(targetId, mapMember.get("rc_id")+"", mapMember.get("name")+"");
                                    }
                                }*/
                                DbManager dbManager = DbManager.getInstance(ConversationActivity.this);
                                GroupMemberDao groupMemberDao = dbManager.getGroupMemberDao();
                                UserDao userDao = dbManager.getUserDao();


                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 获取新数据前清除掉原成员信息
                                        if (groupMemberDao != null) {
                                            groupMemberDao.deleteGroupMember(targetId);
                                        }
                                    }
                                }).start();


                                List<GroupMemberInfoEntity> groupEntityList = new ArrayList<>();
                                List<com.lr.biyou.chatry.db.model.UserInfo> newUserList = new ArrayList<>();
                                if (memberList != null && memberList.size() > 0) {
                                    for (Map<String, Object> mapMember : memberList) {
                                        UserSimpleInfo user = new UserSimpleInfo();
                                        user.setId(mapMember.get("rc_id") + "");
                                        user.setName(mapMember.get("name") + "");
                                        user.setPortraitUri(mapMember.get("portrait") + "");

                                        GroupMemberInfoEntity groupEntity = new GroupMemberInfoEntity();
                                        groupEntity.setGroupId(targetId);

                                        groupEntity.setNickName("");
                                        groupEntity.setUserId(user.getId());
                                        //groupEntity.setRole("");
                                        //groupEntity.setCreateTime(info.getCreatedTime());
                                        //groupEntity.setUpdateTime(info.getUpdatedTime());
                                        //groupEntity.setJoinTime(info.getTimestamp());
                                        groupEntityList.add(groupEntity);


                                        // 更新 IMKit 缓存群组成员数据
                                        IMManager.getInstance().updateGroupMemberInfoCache(targetId, user.getId(), user.getName());
                                        IMManager.getInstance().updateUserInfoCache(user.getId(), user.getName(), Uri.parse(user.getPortraitUri()));


                                        if (userDao != null) {
                                            // 更新已存在的用户信息
                                            String portraitUri = user.getPortraitUri();
                                            Log.i("show", "头像Uri:" + portraitUri);

                                            // 当没有头像时生成默认头像
                                            if (TextUtils.isEmpty(portraitUri)) {
                                                portraitUri = RongGenerate.generateDefaultAvatar(ConversationActivity.this, user.getId(), user.getName());
                                                user.setPortraitUri(portraitUri);
                                            }

                                        } else {
                                            Log.i("show", "头像Uri&&&" + user.getPortraitUri());
                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    com.lr.biyou.chatry.db.model.UserInfo userInfo = new com.lr.biyou.chatry.db.model.UserInfo();
                                                    userInfo.setId(user.getId());
                                                    userInfo.setName(user.getName());
                                                    userInfo.setNameSpelling(SearchUtils.fullSearchableString(user.getName()));
                                                    userInfo.setPortraitUri(user.getPortraitUri());
                                                }
                                            }).start();
                                        }
                                    }

                                    //IMManager.getInstance().updateGroupMemberInfoCache(targetId, mapMember.get("rc_id")+"", mapMember.get("name")+"");
                                }
                                // 更新群组成员

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (groupMemberDao != null) {
                                            groupMemberDao.insertGroupMemberList(groupEntityList);
                                        }
                                        if (userDao != null) {
                                            // 插入新的用户信息
                                            userDao.insertUserListIgnoreExist(newUserList);
                                        }
                                    }
                                }).start();


                            }

                        }
                        break;
                    case "1":
                        closeAllActivity();
                        Intent intent = new Intent(ConversationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
                        break;
                }

                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map, mType);
    }

    /**
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUI(MessageEvent event) {
        switch (event.getType()) {
            case 2:
                finish();
                break;
            case 3://弹窗
                mapMessage = event.getMessage();
                targetId2 = mapMessage.get("id") + "";
                getUserInfoAction(targetId2);
                break;

        }
    }

    /**
     * 查询用户信息
     */
    public void getUserInfoAction(String targetId) {
        mRequestTag = MethodUrl.CHAT_QUERY_USERINFO;
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = com.lr.biyou.utils.tool.SPUtils.get(ConversationActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("rc_id", targetId);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_QUERY_USERINFO, map);
    }


    private View popView;
    private PopupWindow mConditionDialog;
    private boolean bright = false;

    private void initPopupWindow() {

        if (mConditionDialog == null) {
            popView = LayoutInflater.from(ConversationActivity.this).inflate(R.layout.item_head_news, null);
            mConditionDialog = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mConditionDialog.setClippingEnabled(false);
            initConditionDialog(popView);
            int screenWidth = UtilTools.getScreenWidth(ConversationActivity.this);
            //int screenHeight=UtilTools.getScreenHeight(getActivity());
            mConditionDialog.setWidth((int) (0.8f * screenWidth));
            mConditionDialog.setHeight(UtilTools.dip2px(ConversationActivity.this, 55));

            //设置background后在外点击才会消失
            mConditionDialog.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), UtilTools.dip2px(ConversationActivity.this, 5)));
            //mConditionDialog.setOutsideTouchable(true);// 设置可允许在外点击消失
            //自定义动画
            mConditionDialog.setAnimationStyle(R.style.popWindowStyle);
            //mConditionDialog.setAnimationStyle(android.R.style.Animation_Activity);//使用系统动画
            mConditionDialog.update();
            mConditionDialog.setTouchable(true);
            mConditionDialog.setFocusable(true);
            //popView.requestFocus();//pop设置不setBackgroundDrawable情况，把焦点给popView，添加popView.setOnKeyListener。可实现点击外部不消失，点击反键才消失
            //			mConditionDialog.showAtLocation(mCityTv, Gravity.TOP|Gravity.RIGHT, 0, 0); //设置layout在PopupWindow中显示的位置
            //mConditionDialog.showAtLocation(getActivity().getWindow().getDecorView(),  Gravity.TOP|Gravity.RIGHT, 0, 0);
            int offX = (UtilTools.getScreenWidth(ConversationActivity.this) - mConditionDialog.getWidth()) / 2;
            mConditionDialog.showAsDropDown(divideLine, offX, UtilTools.dip2px(ConversationActivity.this, 10), Gravity.LEFT);
            //toggleBright();
            mConditionDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    //toggleBright();
                }
            });
        } else {

            //mConditionDialog.showAtLocation(getActivity().getWindow().getDecorView(),  Gravity.TOP|Gravity.RIGHT, 0, 0);
            int offX = (UtilTools.getScreenWidth(ConversationActivity.this) - mConditionDialog.getWidth()) / 2;
            mConditionDialog.showAsDropDown(divideLine, offX, UtilTools.dip2px(ConversationActivity.this, 10), Gravity.LEFT);
            //toggleBright();
        }

        updata(mapMessage);
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
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;// 0.0-1.0
        getWindow().setAttributes(lp);
    }


    TextView seeTV;
    TextView nameTV;
    TextView contentTV;
    ImageView headIv;

    private void initConditionDialog(View view) {
        seeTV = view.findViewById(R.id.see_tv);
        nameTV = view.findViewById(R.id.name_tv);
        contentTV = view.findViewById(R.id.content_tv);
        headIv = view.findViewById(R.id.head_iv);

       /* final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (v.getId()) {
                    case R.id.see_tv:
                        mConditionDialog.dismiss();
                        showToastMsg("查看详情");

                        RongIM.getInstance().startPrivateChat(ConversationActivity.this,targetId2,"弹窗");
                        break;

                }
            }
        };*/


        //seeTV.setOnClickListener(onClickListener);
    }

    private CountTimer countTimer;

    private void updata(Map<Object, Object> mapMessage) {
        GlideUtils.loadCircleImage(ConversationActivity.this, mapMessage.get("image") + "", headIv);
        nameTV.setText(mapMessage.get("name") + "");
        LogUtilDebug.i("show", "content:" + mapMessage.get("content") + "");
        switch (mapMessage.get("type") + "") {
            case "0": //text
                if (!UtilTools.empty(mapMessage.get("content") + "")) {
                    Map<String, Object> mapContent = JSONUtil.getInstance().jsonMap(mapMessage.get("content") + "");
                    if (!UtilTools.empty(mapContent)) {
                        LogUtilDebug.i("show", "mapContent22:" + mapContent.toString());
                        contentTV.setText(mapContent.get("content") + "");
                        LogUtilDebug.i("show", "Content:" + mapContent.get("content") + "");
                    }
                }
                break;
            case "1": //imag
                contentTV.setText("向您发送了一条图片消息");
                break;
            case "2": //红包
                contentTV.setText("向您发送了一条红包消息");
                break;
            case "3": //其他
                contentTV.setText("向您发送了一条新消息");
                break;
        }
        cancelTimer();
        countTimer = new CountTimer(2000, 1500);
        countTimer.start();

    }



    private class CountTimer extends CountDownTimer {
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            cancel();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            LogUtilDebug.i("show", "milll:" + millisUntilFinished);
            if (millisUntilFinished < 1000 && mConditionDialog.isShowing()) {
                mConditionDialog.dismiss();
            }
        }
    }


    public void cancelTimer() {
        if (countTimer != null) {
            countTimer.cancel();
            countTimer = null;
        }
       /* if (mConditionDialog != null && mConditionDialog.isShowing()) {
            mConditionDialog.dismiss();
        }*/
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_AI_TE && resultCode == Activity.RESULT_OK) {
            if (data.getExtras() != null) {
                Map<String, Object> mapMember = (Map<String, Object>) data.getExtras().get("DATA");
                if (!UtilTools.empty(mapMember)) {
                    UserInfo userInfo = new UserInfo(mapMember.get("rc_id") + "", mapMember.get("name") + "", Uri.parse(mapMember.get("portrait") + ""));
                    RongMentionManager.getInstance().mentionMember(userInfo);
                }


            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus eventBus = EventBus.getDefault();
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }

        IMManager.getInstance().isShowTitleWindow = false;

        cancelTimer();
    }


}
