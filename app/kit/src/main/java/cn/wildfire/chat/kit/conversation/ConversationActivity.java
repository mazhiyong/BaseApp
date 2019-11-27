package cn.wildfire.chat.kit.conversation;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.manage.ActivityManager;
import com.lr.biyou.mvp.presenter.RequestPresenterImp;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.HashMap;
import java.util.Map;

import cn.wildfire.chat.kit.IMServiceStatusViewModel;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.remote.ChatManager;
import io.rong.callkit.util.SPUtils;

public class ConversationActivity extends WfcBaseActivity implements RequestView {

    public TextView tvTitle;

    private boolean isInitialized = false;
    private ConversationFragment conversationFragment;
    private Conversation conversation;

    private Handler handler2 = new Handler();

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
        RequestPresenterImp mRequestPresenterImp = new RequestPresenterImp(this,this);
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_TIME_LONG, map);
    }


    @Override
    protected void onResume() {
        super.onResume();
        handler2.post(cnyRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler2 != null && cnyRunnable != null) {
            handler2.removeCallbacks(cnyRunnable);
        }
    }


    @Override
    protected int contentLayout() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        return R.layout.fragment_container_activity;
    }

    private void setConversationBackground() {
        // you can setup your conversation background here
        // getWindow().setBackgroundDrawableResource(R.mipmap.splash);
    }

    @Override
    protected void afterViews() {
        tvTitle = findViewById(R.id.tv_title);

        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        IMServiceStatusViewModel imServiceStatusViewModel = ViewModelProviders.of(this).get(IMServiceStatusViewModel.class);
        imServiceStatusViewModel.imServiceStatusLiveData().observe(this, aBoolean -> {
            if (!isInitialized && aBoolean) {
                init();
                isInitialized = true;
            }
        });
        conversationFragment = new ConversationFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.containerFrameLayout, conversationFragment, "content")
                .commit();

        setConversationBackground();
    }

    @Override
    protected int menu() {
        return R.menu.conversation;
    }

    public ConversationFragment getConversationFragment() {
        return conversationFragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_conversation_info) {
            showConversationInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!conversationFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    private void showConversationInfo() {
        Intent intent = new Intent(this, ConversationInfoActivity.class);
        intent.putExtra("conversationInfo", ChatManager.Instance().getConversation(conversation));
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        conversation = intent.getParcelableExtra("conversation");
        long initialFocusedMessageId = intent.getLongExtra("toFocusMessageId", -1);
        String channelPrivateChatUser = intent.getStringExtra("channelPrivateChatUser");
        conversationFragment.setupConversation(conversation, null, initialFocusedMessageId, channelPrivateChatUser);
    }


    private void init() {

        Intent intent = getIntent();
        conversation = intent.getParcelableExtra("conversation");
        String conversationTitle = intent.getStringExtra("conversationTitle");
        long initialFocusedMessageId = intent.getLongExtra("toFocusMessageId", -1);
        if (conversation == null) {
            finish();
        }

        conversationFragment.setupConversation(conversation, conversationTitle, initialFocusedMessageId, null);
    }

    public static Intent buildConversationIntent(Context context, Conversation.ConversationType type, String target, int line) {
        return buildConversationIntent(context, type, target, line, -1);
    }

    public static Intent buildConversationIntent(Context context, Conversation.ConversationType type, String target, int line, long toFocusMessageId) {
        Conversation conversation = new Conversation(type, target, line);
        return buildConversationIntent(context, conversation, null, toFocusMessageId);
    }

    public static Intent buildConversationIntent(Context context, Conversation.ConversationType type, String target, int line, String channelPrivateChatUser) {
        Conversation conversation = new Conversation(type, target, line);
        return buildConversationIntent(context, conversation, null, -1);
    }

    public static Intent buildConversationIntent(Context context, Conversation conversation, String channelPrivateChatUser, long toFocusMessageId) {
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra("conversation", conversation);
        intent.putExtra("toFocusMessageId", toFocusMessageId);
        intent.putExtra("channelPrivateChatUser", channelPrivateChatUser);
        return intent;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        LogUtilDebug.i("show","统计聊天时长成功");
        switch (mType) {
            case MethodUrl.CHAT_TIME_LONG:
                switch (tData.get("code") + "") {
                    case "0": //请求成功
                        break;
                    case "-1": //请求失败
                        //showToastMsg(tData.get("msg") + "");
                        break;

                    case "1": //token过期
                        ActivityManager activityManager = ActivityManager.getInstance();
                        activityManager.close();
                        finish();
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }
                break;

        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

    }
}
