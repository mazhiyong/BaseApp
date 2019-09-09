package com.lr.biyou.rongyun.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.lr.biyou.rongyun.db.model.FriendShipInfo;
import com.lr.biyou.rongyun.db.model.GroupEntity;

import java.util.List;

import com.lr.biyou.rongyun.common.BatchForwardHelper;
import com.lr.biyou.rongyun.common.ErrorCode;
import com.lr.biyou.rongyun.model.Resource;
import io.rong.contactcard.message.ContactMessage;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.LocationMessage;

public class ForwardActivityViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> isSingleLiveData;
    private MutableLiveData<Resource> forwardSuccessLiveData;

    public ForwardActivityViewModel(@NonNull Application application) {
        super(application);
        isSingleLiveData = new MutableLiveData<>();
        forwardSuccessLiveData = new MutableLiveData<>();
    }

    public void setIsSinglePick(boolean isSingle) {
        isSingleLiveData.postValue(isSingle);
    }

    public void switchMutiSingle() {
        isSingleLiveData.postValue(!isSingleLiveData.getValue());
    }

    public LiveData<Boolean> getIsSingleLiveData() {
        return isSingleLiveData;
    }


    public void ForwardMessage(List<GroupEntity> groupEntityList, List<FriendShipInfo> friendShipInfoList, List<Message> messageListe) {
        if (groupEntityList != null) {
            for (GroupEntity groupEntity : groupEntityList) {
                forwardMessage(Conversation.ConversationType.GROUP, groupEntity.getId(), messageListe);
            }
        }
        if (friendShipInfoList != null) {
            for (FriendShipInfo friendShipInfo : friendShipInfoList) {
                forwardMessage(Conversation.ConversationType.PRIVATE, friendShipInfo.getUser().getId(), messageListe);
            }
        }

    }

    /**
     * 实际转发消息
     *
     * @param conversationType
     * @param targetId
     */
    private void forwardMessage(Conversation.ConversationType conversationType, String targetId, List<Message> messageList) {
        for (Message fwdMessage : messageList) {
            MessageContent messageContent = fwdMessage.getContent();
            if (messageContent != null) {
                messageContent.setUserInfo(null);
            }
            if (messageContent instanceof ContactMessage) {
                String portraitUrl = ((ContactMessage) messageContent).getImgUrl();
                if (TextUtils.isEmpty(portraitUrl) || portraitUrl.toLowerCase().startsWith("file://")) {
                    portraitUrl = null;
                }
                String sendContactMsgUserName = "";
                UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(RongIM.getInstance().getCurrentUserId());
                if (userInfo != null) {
                    sendContactMsgUserName = userInfo.getName();
                }
                ContactMessage contactMessage = ContactMessage.obtain(((ContactMessage) messageContent).getId(),
                        ((ContactMessage) messageContent).getName(), portraitUrl,
                        RongIM.getInstance().getCurrentUserId(), sendContactMsgUserName, null);
                Message message = Message.obtain(targetId, conversationType, contactMessage);
                sendMessage(message);

            } else if (messageContent instanceof LocationMessage) {//判断是否是定位消息
                Message message = Message.obtain(targetId, conversationType, messageContent);
                sendMessage(message);
            } else {
                Message message = Message.obtain(targetId, conversationType, messageContent);
                sendMessage(message);

            }
        }
    }

    /**
     * 这里需要延迟 300ms 来发送，防止消息阻塞
     *
     * @param message
     */
    private void sendMessage(Message message) {
        BatchForwardHelper.getInstance().batchSendMessage(message, callback);

    }

    public MutableLiveData<Resource> getForwardSuccessLiveData() {
        return forwardSuccessLiveData;
    }

    private IRongCallback.ISendMediaMessageCallback callback = new IRongCallback.ISendMediaMessageCallback() {
        @Override
        public void onProgress(Message message, int i) {

        }

        @Override
        public void onCanceled(Message message) {

        }

        @Override
        public void onAttached(Message message) {

        }

        @Override
        public void onSuccess(Message message) {
            forwardSuccessLiveData.postValue(Resource.success(null));
        }

        @Override
        public void onError(Message message, RongIMClient.ErrorCode errorCode) {
            if (errorCode == RongIMClient.ErrorCode.RC_NET_UNAVAILABLE || errorCode ==  RongIMClient.ErrorCode.RC_NET_CHANNEL_INVALID) {
                forwardSuccessLiveData.postValue(Resource.error(ErrorCode.NETWORK_ERROR.getCode(), null));
            } else {
                forwardSuccessLiveData.postValue(Resource.error(ErrorCode.UNKNOWN_ERROR.getCode(), null));
            }
        }
    };
}
