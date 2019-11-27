package cn.wildfire.chat.kit.conversation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.flyco.dialog.utils.CornerUtils;
import com.lr.biyou.R;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.WfcUIKit;
import cn.wildfire.chat.kit.channel.ChannelViewModel;
import cn.wildfire.chat.kit.chatroom.ChatRoomViewModel;
import cn.wildfire.chat.kit.common.OperateResult;
import cn.wildfire.chat.kit.conversation.ext.core.ConversationExtension;
import cn.wildfire.chat.kit.conversation.mention.MentionSpan;
import cn.wildfire.chat.kit.conversation.message.model.UiMessage;
import cn.wildfire.chat.kit.conversation.multimsg.MultiMessageAction;
import cn.wildfire.chat.kit.conversation.multimsg.MultiMessageActionManager;
import cn.wildfire.chat.kit.group.GroupViewModel;
import cn.wildfire.chat.kit.third.utils.TimeUtils;
import cn.wildfire.chat.kit.third.utils.UIUtils;
import cn.wildfire.chat.kit.user.UserInfoActivity;
import cn.wildfire.chat.kit.user.UserViewModel;
import cn.wildfire.chat.kit.viewmodel.MessageViewModel;
import cn.wildfire.chat.kit.viewmodel.SettingViewModel;
import cn.wildfire.chat.kit.widget.InputAwareLayout;
import cn.wildfire.chat.kit.widget.KeyboardAwareLinearLayout;
import cn.wildfirechat.message.MessageContent;
import cn.wildfirechat.message.TextMessageContent;
import cn.wildfirechat.message.TypingMessageContent;
import cn.wildfirechat.message.core.MessageDirection;
import cn.wildfirechat.message.core.PersistFlag;
import cn.wildfirechat.message.notification.TipNotificationContent;
import cn.wildfirechat.model.ChannelInfo;
import cn.wildfirechat.model.ChatRoomInfo;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.model.GroupInfo;
import cn.wildfirechat.model.GroupMember;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;
import cn.wildfirechat.remote.UserSettingScope;

public class ConversationFragment extends Fragment implements
        KeyboardAwareLinearLayout.OnKeyboardShownListener,
        KeyboardAwareLinearLayout.OnKeyboardHiddenListener,
        ConversationMessageAdapter.OnPortraitClickListener,
        ConversationMessageAdapter.OnPortraitLongClickListener,
        ConversationInputPanel.OnConversationInputPanelStateChangeListener,
        ConversationMessageAdapter.OnMessageCheckListener {

    public static final int REQUEST_PICK_MENTION_CONTACT = 100;

    private Conversation conversation;
    private boolean loadingNewMessage;
    private boolean shouldContinueLoadNewMessage = false;

    private static final int MESSAGE_LOAD_COUNT_PER_TIME = 20;
    private static final int MESSAGE_LOAD_AROUND = 10;

    @BindView(R.id.rootLinearLayout)
    InputAwareLayout rootLinearLayout;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.msgRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.inputPanelFrameLayout)
    ConversationInputPanel inputPanel;

    @BindView(R.id.multiMessageActionContainerLinearLayout)
    LinearLayout multiMessageActionContainerLinearLayout;

    private ConversationMessageAdapter adapter;
    private boolean moveToBottom = true;
    private ConversationViewModel conversationViewModel;
    private SettingViewModel settingViewModel;
    private MessageViewModel messageViewModel;
    private UserViewModel userViewModel;
    private boolean isInitialized = false;
    private ChatRoomViewModel chatRoomViewModel;

    private Handler handler;
    private long initialFocusedMessageId;
    // 用户channel主发起，针对某个用户的会话
    private String channelPrivateChatUser;
    private String conversationTitle = "";
    private LinearLayoutManager layoutManager;

    private GroupInfo groupInfo;
    private boolean showGroupMemberName = false;

    private Observer<UiMessage> messageLiveDataObserver = new Observer<UiMessage>() {
        @Override
        public void onChanged(@Nullable UiMessage uiMessage) {
            if (!isMessageInCurrentConversation(uiMessage)) {
                //不是当前聊天的消息,弹窗
                if (uiMessage.message.content.getType() == 91){
                    //正在输入
                    return;
                }else {
                    initPopupWindow(uiMessage);
                }


                return;
            }
            MessageContent content = uiMessage.message.content;
            if (isDisplayableMessage(uiMessage)) {
                // 消息定位时，如果收到新消息、或者发送消息，需要重新加载消息列表
                if (shouldContinueLoadNewMessage) {
                    shouldContinueLoadNewMessage = false;
                    reloadMessage();
                    return;
                }

                adapter.addNewMessage(uiMessage);
                if (moveToBottom || uiMessage.message.sender.equals(ChatManager.Instance().getUserId())) {
                    UIUtils.postTaskDelay(() -> {

                                int position = adapter.getItemCount() - 1;
                                if (position < 0) {
                                    return;
                                }
                                recyclerView.scrollToPosition(position);
                            },
                            100);
                }
            }
            if (content instanceof TypingMessageContent && uiMessage.message.direction == MessageDirection.Receive) {
                updateTypingStatusTitle((TypingMessageContent) content);
            } else {
                resetConversationTitle();
            }

            if (uiMessage.message.direction == MessageDirection.Receive) {
                conversationViewModel.clearUnreadStatus(conversation);
            }
        }
    };
    private Observer<UiMessage> messageUpdateLiveDatObserver = new Observer<UiMessage>() {
        @Override
        public void onChanged(@Nullable UiMessage uiMessage) {
            if (!isMessageInCurrentConversation(uiMessage)) {
                return;
            }
            if (isDisplayableMessage(uiMessage)) {
                adapter.updateMessage(uiMessage);
            }
        }
    };

    private Observer<UiMessage> messageRemovedLiveDataObserver = new Observer<UiMessage>() {
        @Override
        public void onChanged(@Nullable UiMessage uiMessage) {
            if (!isMessageInCurrentConversation(uiMessage)) {
                return;
            }
            if (isDisplayableMessage(uiMessage)) {
                adapter.removeMessage(uiMessage);
            }
        }
    };

    private boolean isDisplayableMessage(UiMessage uiMessage) {
        MessageContent content = uiMessage.message.content;
        Log.i("show","content.getPersistFlag():"+content.getPersistFlag());
        if (content.getPersistFlag() == PersistFlag.Persist
                || content.getPersistFlag() == PersistFlag.Persist_And_Count) {
            return true;
        }
        return false;
    }

    private Observer<Map<String, String>> mediaUploadedLiveDataObserver = new Observer<Map<String, String>>() {
        @Override
        public void onChanged(@Nullable Map<String, String> stringStringMap) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sticker", Context.MODE_PRIVATE);
            for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {
                sharedPreferences.edit()
                        .putString(entry.getKey(), entry.getValue())
                        .apply();
            }

        }
    };

    private Observer<Conversation> clearConversationMessageObserver = new Observer<Conversation>() {
        @Override
        public void onChanged(Conversation conversation) {
            if (conversation.equals(ConversationFragment.this.conversation)) {
                adapter.setMessages(null);
                adapter.notifyDataSetChanged();
            }
        }
    };

    private Observer<List<UserInfo>> userInfoUpdateLiveDataObserver = new Observer<List<UserInfo>>() {
        @Override
        public void onChanged(@Nullable List<UserInfo> userInfos) {
            if (conversation.type == Conversation.ConversationType.Single) {
                conversationTitle = null;
                setTitle();
            }
            int start = layoutManager.findFirstVisibleItemPosition();
            int end = layoutManager.findLastVisibleItemPosition();
            adapter.notifyItemRangeChanged(start, end - start + 1, userInfos);
        }
    };

    private boolean isMessageInCurrentConversation(UiMessage message) {
        if (conversation == null || message == null || message.message == null) {
            return false;
        }
        return conversation.equals(message.message.conversation);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.conversation_activity, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public void setupConversation(Conversation conversation, String title, long focusMessageId, String target) {
        this.conversation = conversation;
        this.conversationTitle = title;
        this.initialFocusedMessageId = focusMessageId;
        this.channelPrivateChatUser = target;
        setupConversation(conversation);
    }

    private void initView() {
        handler = new Handler();
        rootLinearLayout.addOnKeyboardShownListener(this);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (adapter.getMessages() == null || adapter.getMessages().isEmpty()) {
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            loadMoreOldMessages();
        });

        // message list
        adapter = new ConversationMessageAdapter(this);
        adapter.setOnPortraitClickListener(this);
        adapter.setOnPortraitLongClickListener(this);
        adapter.setOnMessageCheckListener(this);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 向上滑动，不在底部，收到消息时，不滑动到底部, 发送消息时，可以强制置为true
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    return;
                }
                if (!recyclerView.canScrollVertically(1)) {
                    moveToBottom = true;
                    if (initialFocusedMessageId != -1 && !loadingNewMessage && shouldContinueLoadNewMessage) {
                        int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                        if (lastVisibleItem > adapter.getItemCount() - 3) {
                            loadMoreNewMessages();
                        }
                    }
                } else {
                    moveToBottom = false;
                }
            }
        });

        inputPanel.init(this, rootLinearLayout);
        inputPanel.setOnConversationInputPanelStateChangeListener(this);
    }

    private void setupConversation(Conversation conversation) {
        if (conversationViewModel == null) {
            settingViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
            conversationViewModel = WfcUIKit.getAppScopeViewModel(ConversationViewModel.class);
            conversationViewModel.clearConversationMessageLiveData().observeForever(clearConversationMessageObserver);
            messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
            messageViewModel.messageLiveData().observeForever(messageLiveDataObserver);
            messageViewModel.messageUpdateLiveData().observeForever(messageUpdateLiveDatObserver);
            messageViewModel.messageRemovedLiveData().observeForever(messageRemovedLiveDataObserver);
            messageViewModel.mediaUpdateLiveData().observeForever(mediaUploadedLiveDataObserver);




            userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
            userViewModel.userInfoLiveData().observeForever(userInfoUpdateLiveDataObserver);
        }

        if (conversation.type == Conversation.ConversationType.Group) {
            GroupViewModel groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
            groupInfo = groupViewModel.getGroupInfo(conversation.target, false);
            groupViewModel.groupInfoUpdateLiveData().observe(this, groupInfos -> {
                for (GroupInfo info : groupInfos) {
                    if (info.target.equals(groupInfo.target)) {
                        groupInfo = info;
                        if (groupInfo.mute == 1) {
                            GroupMember groupMember = groupViewModel.getGroupMember(groupInfo.target, userViewModel.getUserId());
                            if (groupMember.type != GroupMember.GroupMemberType.Owner && groupMember.type != GroupMember.GroupMemberType.Manager) {
                                inputPanel.disableInput("全员禁言中");
                            } else {
                                inputPanel.enableInput();
                            }
                        } else {
                            inputPanel.enableInput();
                        }
                        setTitle();
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            showGroupMemberName = "1".equals(userViewModel.getUserSetting(UserSettingScope.GroupHideNickname, groupInfo.target));
            settingViewModel.settingUpdatedLiveData().observe(this, o -> {
                boolean showGroupMemberName = "1".equals(userViewModel.getUserSetting(UserSettingScope.GroupHideNickname, groupInfo.target));
                if (this.showGroupMemberName != showGroupMemberName) {
                    this.showGroupMemberName = showGroupMemberName;
                    adapter.notifyDataSetChanged();
                }
            });

            if (groupInfo.mute == 1) {
                GroupMember groupMember = groupViewModel.getGroupMember(groupInfo.target, userViewModel.getUserId());
                if (groupMember.type != GroupMember.GroupMemberType.Owner && groupMember.type != GroupMember.GroupMemberType.Manager) {
                    inputPanel.disableInput("全员禁言中");
                }
            }

            ChatManager.Instance().getWorkHandler().post(() -> {
                List<GroupMember> groupMembers = ChatManager.Instance().getGroupMembers(conversation.target, false);
                if (groupMembers != null) {
                    List<String> memberIds = new ArrayList<>();
                    for (GroupMember member : groupMembers) {
                        memberIds.add(member.memberId);
                    }
                    ChatManager.Instance().getUserInfos(memberIds, conversation.target);
                }
            });
        }

        inputPanel.setupConversation(conversation);

        MutableLiveData<List<UiMessage>> messages;
        if (initialFocusedMessageId != -1) {
            shouldContinueLoadNewMessage = true;
            messages = conversationViewModel.loadAroundMessages(conversation, channelPrivateChatUser, initialFocusedMessageId, MESSAGE_LOAD_AROUND);
        } else {
            messages = conversationViewModel.getMessages(conversation, channelPrivateChatUser);
        }

        // load message
        swipeRefreshLayout.setRefreshing(true);
        messages.observe(this, uiMessages -> {
            swipeRefreshLayout.setRefreshing(false);
            adapter.setMessages(uiMessages);
            adapter.notifyDataSetChanged();

            if (adapter.getItemCount() > 1) {
                int initialMessagePosition;
                if (initialFocusedMessageId != -1) {
                    initialMessagePosition = adapter.getMessagePosition(initialFocusedMessageId);
                    if (initialMessagePosition != -1) {
                        recyclerView.scrollToPosition(initialMessagePosition);
                        adapter.highlightFocusMessage(initialMessagePosition);
                    }
                } else {
                    moveToBottom = true;
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }
            }
        });
        if (conversation.type == Conversation.ConversationType.ChatRoom) {
            joinChatRoom();
        }

        conversationViewModel.clearUnreadStatus(conversation);

        setTitle();
    }

    private void joinChatRoom() {
        chatRoomViewModel = ViewModelProviders.of(this).get(ChatRoomViewModel.class);
        chatRoomViewModel.joinChatRoom(conversation.target)
                .observe(this, new Observer<OperateResult<Boolean>>() {
                    @Override
                    public void onChanged(@Nullable OperateResult<Boolean> booleanOperateResult) {
                        if (booleanOperateResult.isSuccess()) {
                            String welcome = "欢迎 %s 加入聊天室";
                            TipNotificationContent content = new TipNotificationContent();
                            String userId = userViewModel.getUserId();
                            UserInfo userInfo = userViewModel.getUserInfo(userId, false);
                            if (userInfo != null) {
                                content.tip = String.format(welcome, userInfo.displayName);
                            } else {
                                content.tip = String.format(welcome, "<" + userId + ">");
                            }
                            messageViewModel.sendMessage(conversation, content);
                            loadMoreOldMessages();
                            setChatRoomConversationTitle();

                        } else {
                            Toast.makeText(getActivity(), "加入聊天室失败", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }
                    }
                });
    }

    private void quitChatRoom() {
        String welcome = "%s 离开了聊天室";
        TipNotificationContent content = new TipNotificationContent();
        String userId = userViewModel.getUserId();
        UserInfo userInfo = userViewModel.getUserInfo(userId, false);
        if (userInfo != null) {
            content.tip = String.format(welcome, userInfo.displayName);
        } else {
            content.tip = String.format(welcome, "<" + userId + ">");
        }
        messageViewModel.sendMessage(conversation, content);
        chatRoomViewModel.quitChatRoom(conversation.target);
    }

    private void setChatRoomConversationTitle() {
        chatRoomViewModel.getChatRoomInfo(conversation.target, System.currentTimeMillis())
                .observe(this, chatRoomInfoOperateResult -> {
                    if (chatRoomInfoOperateResult.isSuccess()) {
                        ChatRoomInfo chatRoomInfo = chatRoomInfoOperateResult.getResult();
                        conversationTitle = chatRoomInfo.title;
                        setActivityTitle(conversationTitle);
                    }
                });
    }

    private void setTitle() {
        if (!TextUtils.isEmpty(conversationTitle)) {
            setActivityTitle(conversationTitle);
        }

        if (conversation.type == Conversation.ConversationType.Single) {
            UserInfo userInfo = ChatManagerHolder.gChatManager.getUserInfo(conversation.target, false);
            conversationTitle = userViewModel.getUserDisplayName(userInfo);
        } else if (conversation.type == Conversation.ConversationType.Group) {
            if (groupInfo != null) {

                conversationTitle = groupInfo.name ;
            }
        } else if (conversation.type == Conversation.ConversationType.Channel) {
            ChannelViewModel channelViewModel = ViewModelProviders.of(this).get(ChannelViewModel.class);
            ChannelInfo channelInfo = channelViewModel.getChannelInfo(conversation.target, false);
            if (channelInfo != null) {
                conversationTitle = channelInfo.name;
            }

            if (!TextUtils.isEmpty(channelPrivateChatUser)) {
                UserInfo channelPrivateChatUserInfo = userViewModel.getUserInfo(channelPrivateChatUser, false);
                if (channelPrivateChatUserInfo != null) {
                    conversationTitle += "@" + userViewModel.getUserDisplayName(channelPrivateChatUserInfo);
                } else {
                    conversationTitle += "@<" + channelPrivateChatUser + ">";
                }
            }
        }
        setActivityTitle(conversationTitle);
    }

    private void setActivityTitle(String title) {
        ConversationActivity activity = (ConversationActivity) getActivity();
        if (activity != null) {
            activity.setTitle("");
            activity.tvTitle.setText(title);
        }
    }

    @OnTouch({R.id.contentLayout, R.id.msgRecyclerView})
    boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && inputPanel.extension.canHideOnScroll()) {
            inputPanel.collapse();
        }
        return false;
    }

    @Override
    public void onPortraitClick(UserInfo userInfo) {
        //点击头像,查看用户详情,管理员用户不跳转
        if(userInfo.name.equals("17319449662") || userInfo.displayName.equals("用户938")){
            return;
        }
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        intent.putExtra("userInfo", userInfo);
        startActivity(intent);
    }

    @Override
    public void onPortraitLongClick(UserInfo userInfo) {
        if (conversation.type == Conversation.ConversationType.Group) {
            SpannableString spannableString = mentionSpannable(userInfo);
            int position = inputPanel.editText.getSelectionEnd();
            inputPanel.editText.getEditableText().append(" ");
            inputPanel.editText.getEditableText().replace(position, position + 1, spannableString);
        } else {
            inputPanel.editText.getEditableText().append(userViewModel.getUserDisplayName(userInfo));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode >= ConversationExtension.REQUEST_CODE_MIN) {
            inputPanel.extension.onActivityResult(requestCode, resultCode, data);
            return;
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PICK_MENTION_CONTACT) {
            boolean isMentionAll = data.getBooleanExtra("mentionAll", false);
            SpannableString spannableString;
            if (isMentionAll) {
                spannableString = mentionAllSpannable();
            } else {
                String userId = data.getStringExtra("userId");
                UserInfo userInfo = userViewModel.getUserInfo(userId, false);
                spannableString = mentionSpannable(userInfo);
            }
            int position = inputPanel.editText.getSelectionEnd();
            position = position > 0 ? position - 1 : 0;
            inputPanel.editText.getEditableText().replace(position, position + 1, spannableString);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private SpannableString mentionAllSpannable() {
        String text = "@所有人 ";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new MentionSpan(true), 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private SpannableString mentionSpannable(UserInfo userInfo) {
        String text = "@" + userInfo.displayName + " ";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new MentionSpan(userInfo.uid), 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (inputPanel != null){
            inputPanel.onActivityPause();
        }
        if (messageViewModel != null){
            messageViewModel.stopPlayAudio();
        }

    }

    @Override
    public void onDestroy() {
        if (conversation.type == Conversation.ConversationType.ChatRoom) {
            quitChatRoom();
        }

        super.onDestroy();
        messageViewModel.messageLiveData().removeObserver(messageLiveDataObserver);
        messageViewModel.messageUpdateLiveData().removeObserver(messageUpdateLiveDatObserver);
        messageViewModel.messageRemovedLiveData().removeObserver(messageRemovedLiveDataObserver);
        messageViewModel.mediaUpdateLiveData().removeObserver(mediaUploadedLiveDataObserver);
        userViewModel.userInfoLiveData().removeObserver(userInfoUpdateLiveDataObserver);
        conversationViewModel.clearConversationMessageLiveData().removeObserver(clearConversationMessageObserver);
        inputPanel.onDestroy();
    }

    boolean onBackPressed() {
        boolean consumed = true;
        if (rootLinearLayout.getCurrentInput() != null) {
            rootLinearLayout.hideAttachedInput(true);
            inputPanel.collapse();
        } else if (multiMessageActionContainerLinearLayout.getVisibility() == View.VISIBLE) {
            toggleConversationMode();
        } else {
            consumed = false;
        }
        return consumed;
    }

    @Override
    public void onKeyboardShown() {
        inputPanel.onKeyboardShown();
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void onKeyboardHidden() {
        inputPanel.onKeyboardHidden();
    }

    private void reloadMessage() {
        conversationViewModel.getMessages(conversation, channelPrivateChatUser).observe(this, uiMessages -> {
            adapter.setMessages(uiMessages);
            adapter.notifyDataSetChanged();
        });
    }

    private void loadMoreOldMessages() {
        long fromMessageId = Long.MAX_VALUE;
        long fromMessageUid = Long.MAX_VALUE;
        if (adapter.getMessages() != null && !adapter.getMessages().isEmpty()) {
            fromMessageId = adapter.getItem(0).message.messageId;
            fromMessageUid = adapter.getItem(0).message.messageUid;
        }

        conversationViewModel.loadOldMessages(conversation, channelPrivateChatUser, fromMessageId, fromMessageUid, MESSAGE_LOAD_COUNT_PER_TIME)
                .observe(this, uiMessages -> {
                    adapter.addMessagesAtHead(uiMessages);

                    swipeRefreshLayout.setRefreshing(false);
                });
    }

    private void loadMoreNewMessages() {
        loadingNewMessage = true;
        adapter.showLoadingNewMessageProgressBar();
        conversationViewModel.loadNewMessages(conversation, channelPrivateChatUser, adapter.getItem(adapter.getItemCount() - 2).message.messageId, MESSAGE_LOAD_COUNT_PER_TIME)
                .observe(this, messages -> {
                    loadingNewMessage = false;
                    adapter.dismissLoadingNewMessageProgressBar();

                    if (messages == null || messages.isEmpty()) {
                        shouldContinueLoadNewMessage = false;
                    }
                    if (messages != null && !messages.isEmpty()) {
                        adapter.addMessagesAtTail(messages);
                    }
                });
    }

    private void updateTypingStatusTitle(TypingMessageContent typingMessageContent) {
        String typingDesc = "";
        switch (typingMessageContent.getType()) {
            case TypingMessageContent.TYPING_TEXT:
                typingDesc = "对方正在输入";
                break;
            case TypingMessageContent.TYPING_VOICE:
                typingDesc = "对方正在录音";
                break;
            case TypingMessageContent.TYPING_CAMERA:
                typingDesc = "对方正在拍照";
                break;
            case TypingMessageContent.TYPING_FILE:
                typingDesc = "对方正在发送文件";
                break;
            case TypingMessageContent.TYPING_LOCATION:
                typingDesc = "对方正在发送位置";
                break;
            default:
                typingDesc = "对方正在输入";
                break;
        }
        setActivityTitle(typingDesc);
        handler.postDelayed(resetConversationTitleRunnable, 5000);
    }

    private Runnable resetConversationTitleRunnable = this::resetConversationTitle;

    private void resetConversationTitle() {
        if (!TextUtils.equals(conversationTitle, Objects.requireNonNull(getActivity()).getTitle())) {
            setActivityTitle(conversationTitle);
            handler.removeCallbacks(resetConversationTitleRunnable);
        }
    }

    @Override
    public void onInputPanelExpanded() {
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    @Override
    public void onInputPanelCollapsed() {
        // do nothing
    }

    public void toggleMultiMessageMode(UiMessage message) {
        inputPanel.setVisibility(View.GONE);
        message.isChecked = true;
        adapter.setMode(ConversationMessageAdapter.MODE_CHECKABLE);
        adapter.notifyDataSetChanged();
        multiMessageActionContainerLinearLayout.setVisibility(View.VISIBLE);
        setupMultiMessageAction();
    }

    public void toggleConversationMode() {
        inputPanel.setVisibility(View.VISIBLE);
        multiMessageActionContainerLinearLayout.setVisibility(View.GONE);
        adapter.setMode(ConversationMessageAdapter.MODE_NORMAL);
        adapter.clearMessageCheckStatus();
        adapter.notifyDataSetChanged();
    }

    private void setupMultiMessageAction() {
        multiMessageActionContainerLinearLayout.removeAllViews();
        List<MultiMessageAction> actions = MultiMessageActionManager.getInstance().getConversationActions(conversation);
        for (MultiMessageAction action : actions) {

            action.onBind(this, conversation);
            TextView textView = new TextView(getActivity());
            textView.setCompoundDrawablePadding(10);
            textView.setCompoundDrawablesWithIntrinsicBounds(action.iconResId(), 0, 0, 0);
            textView.setText(action.title(getActivity()));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            multiMessageActionContainerLinearLayout.addView(textView, layoutParams);

            textView.setOnClickListener(v -> {
                List<UiMessage> checkedMessages = adapter.getCheckedMessages();
                if (action.confirm()) {
                    new MaterialDialog.Builder(getActivity()).content(action.confirmPrompt())
                            .negativeText("取消")
                            .positiveText("确认")
                            .onPositive((dialog, which) -> {
                                action.onClick(checkedMessages);
                                toggleConversationMode();
                            })
                            .build()
                            .show();

                } else {
                    action.onClick(checkedMessages);
                    toggleConversationMode();
                }
            });
        }
    }

    @Override
    public void onMessageCheck(UiMessage message, boolean checked) {
        List<UiMessage> checkedMessages = adapter.getCheckedMessages();
        setAllClickableChildViewState(multiMessageActionContainerLinearLayout, checkedMessages.size() > 0);
    }

    private void setAllClickableChildViewState(View view, boolean enable) {
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                setAllClickableChildViewState(((ViewGroup) view).getChildAt(i), enable);
            }
        }
        if (view.isClickable()) {
            view.setEnabled(enable);
        }
    }




    private View popView;
    private PopupWindow mConditionDialog;
    private boolean bright = false;

    private void initPopupWindow(UiMessage uiMessage) {

        if (mConditionDialog == null) {
            popView = LayoutInflater.from(getActivity()).inflate(R.layout.item_head_news, null);
            mConditionDialog = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mConditionDialog.setClippingEnabled(false);
            initConditionDialog(popView);
            int screenWidth = UtilTools.getScreenWidth(getActivity());
            //int screenHeight=UtilTools.getScreenHeight(getActivity());
            mConditionDialog.setWidth((int) (0.8f * screenWidth));
            mConditionDialog.setHeight(UtilTools.dip2px(getActivity(), 55));

            //设置background后在外点击才会消失
            mConditionDialog.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), UtilTools.dip2px(getActivity(), 5)));
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
            int offX = (UtilTools.getScreenWidth(getActivity()) - mConditionDialog.getWidth()) / 2;
            mConditionDialog.showAsDropDown(recyclerView, offX, UtilTools.dip2px(getActivity(), 60), Gravity.LEFT);
            //toggleBright();
            mConditionDialog.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    //toggleBright();
                }
            });
        } else {

            //mConditionDialog.showAtLocation(getActivity().getWindow().getDecorView(),  Gravity.TOP|Gravity.RIGHT, 0, 0);
            int offX = (UtilTools.getScreenWidth(getActivity()) - mConditionDialog.getWidth()) / 2;
            mConditionDialog.showAsDropDown(recyclerView, offX, UtilTools.dip2px(getActivity(), 60), Gravity.LEFT);
            //toggleBright();
        }
        updata(uiMessage);
    }

    TextView timeTv;
    TextView seeTV;
    TextView nameTV;
    TextView contentTV;
    ImageView headIv;

    private void initConditionDialog(View view) {
        seeTV = view.findViewById(R.id.see_tv);
        nameTV = view.findViewById(R.id.name_tv);
        contentTV = view.findViewById(R.id.content_tv);
        headIv = view.findViewById(R.id.head_iv);
        timeTv = view.findViewById(R.id.time_tv);

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

    private void updata(UiMessage message) {
        UserInfo userInfo= userViewModel.getUserInfo(message.message.sender,false);
        GlideUtils.loadCircleImage(getActivity(),userInfo.portrait, headIv);
        nameTV.setText(userInfo.displayName);
        timeTv.setText(TimeUtils.getMsgFormatTime(message.message.serverTime));
      /*  int ContentType_Unknown = 0;
        int ContentType_Text = 1;
        int ContentType_Voice = 2;
        int ContentType_Image = 3;
        int ContentType_Location = 4;
        int ContentType_File = 5;
        int ContentType_Video = 6;
        int ContentType_Sticker = 7;
        int ContentType_ImageText = 8;
        //自定义消息红包
        int getContentType_ImageRed = 2000;*/
        switch (message.message.content.getType()) {
            case 1: //text
                TextMessageContent messageContent = (TextMessageContent) message.message.content;
                contentTV.setText(messageContent.getContent());
                break;
            case 2: //语音
                contentTV.setText("向您发送了一条语音消息");
                break;
            case 3: //图片
                contentTV.setText("向您发送了一条图片消息");
                break;
            case 4: //位置
                contentTV.setText("向您发送了一条位置消息");
                break;
            case 5: //文件
                contentTV.setText("向您发送了一条文件消息");
                break;
            case 8: //图文
                contentTV.setText("向您发送了一条图文消息");
                break;
            case 2000: //红包
                contentTV.setText("向您发送了一条红包消息");
                break;

            default:
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

}
