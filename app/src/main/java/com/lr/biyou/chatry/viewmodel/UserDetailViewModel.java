package com.lr.biyou.chatry.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lr.biyou.basic.BasicApplication;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.chatry.db.model.FriendShipInfo;
import com.lr.biyou.chatry.db.model.UserInfo;
import com.lr.biyou.chatry.model.AddFriendResult;
import com.lr.biyou.chatry.model.Resource;
import com.lr.biyou.chatry.model.Status;
import com.lr.biyou.chatry.model.UserSimpleInfo;
import com.lr.biyou.chatry.task.FriendTask;
import com.lr.biyou.chatry.task.UserTask;
import com.lr.biyou.chatry.utils.SingleSourceLiveData;
import com.lr.biyou.chatry.utils.SingleSourceMapLiveData;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.List;

/**
 * 用户详细视图模型
 */
public class UserDetailViewModel extends AndroidViewModel {
    private MediatorLiveData<Resource<UserInfo>> userInfoLiveData = new MediatorLiveData<>();
    private SingleSourceMapLiveData<Resource<AddFriendResult>, Resource<AddFriendResult>> inviteResult;
    private SingleSourceMapLiveData<Resource<Void>, Resource<Void>> addBlackListResult;
    private SingleSourceMapLiveData<Resource<Void>, Resource<Void>> removeBlackListResult;
    private SingleSourceLiveData<Resource<Void>> removeFriendResult = new SingleSourceLiveData<>();
    private LiveData<Boolean> isInBlackList;

    private String userId;
    private UserTask userTask;
    private FriendTask friendTask;

    public UserDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public UserDetailViewModel(@NonNull Application application, String userId) {
        super(application);

        this.userId = userId;

        userTask = new UserTask(application);
        friendTask = new FriendTask(application);

        // 获取用于信息前先获取好友信息
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(BasicApplication.getContext(), MbsConstans.ACCESS_TOKEN, "").toString();
        }
        LiveData<Resource<FriendShipInfo>> friendInfo = friendTask.getFriendInfo(MbsConstans.ACCESS_TOKEN,userId);
        userInfoLiveData.addSource(friendInfo, friendShipInfoResource -> {
            // 当有结果时，获取用户信息。此前有好友信息则会更新用户表，没有则只获取用户信息
            if (friendShipInfoResource.status != Status.LOADING) {
                userInfoLiveData.removeSource(friendInfo);
                userInfoLiveData.addSource(userTask.getUserInfo(userId), resource -> userInfoLiveData.setValue(resource));
            }
        });
        LiveData<Resource<UserSimpleInfo>> blackListUser = userTask.getInBlackListUser(userId);
        isInBlackList = Transformations.map(blackListUser, resource -> {
            // 当用户在黑名单时，返回在黑名单状态
            UserSimpleInfo data = resource.data;
            return data != null;
        });

        addBlackListResult = new SingleSourceMapLiveData<>(new Function<Resource<Void>, Resource<Void>>() {
            @Override
            public Resource<Void> apply(Resource<Void> input) {
                if (input.status == Status.SUCCESS) {
                    // 在添加黑名单成功后刷新好友列表
                    updateFriendList();
                }
                return input;
            }
        });

        removeBlackListResult = new SingleSourceMapLiveData<>(new Function<Resource<Void>, Resource<Void>>() {
            @Override
            public Resource<Void> apply(Resource<Void> input) {
                if (input.status == Status.SUCCESS) {
                    // 在移除黑名单成功后刷新好友列表
                    updateFriendList();
                }
                return input;
            }
        });

        inviteResult = new SingleSourceMapLiveData<>(new Function<Resource<AddFriendResult>, Resource<AddFriendResult>>() {
            @Override
            public Resource<AddFriendResult> apply(Resource<AddFriendResult> input) {
                if (input.status == Status.SUCCESS) {
                    // 邀请好友后刷新好友列表
                    updateFriendList();
                }
                return input;
            }
        });

    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public LiveData<Resource<UserInfo>> getUserInfo() {
        return userInfoLiveData;
    }

    /**
     * 添加好友
     *
     * @param inviteMsg
     */
    public void inviteFriend(String inviteMsg) {
        inviteResult.setSource(friendTask.inviteFriend(userId, inviteMsg));
    }

    /**
     * 添加到黑名单
     */
    public void addToBlackList() {
        addBlackListResult.setSource(userTask.addToBlackList(userId));
    }

    /**
     * 移除黑名单
     */
    public void removeFromBlackList() {
        removeBlackListResult.setSource(userTask.removeFromBlackList(userId));
    }

    /**
     * 删除好友
     *
     * @param friendId
     */
    public void deleteFriend(String friendId) {
        removeFriendResult.setSource(friendTask.deleteFriend(friendId));
    }

    /**
     * 获取删除好友结果
     *
     * @return
     */
    public LiveData<Resource<Void>> getDeleteFriendResult() {
        return removeFriendResult;
    }

    /**
     * 判断当前用户是否在黑名单
     *
     * @return
     */
    public LiveData<Boolean> getIsInBlackList() {
        return isInBlackList;
    }

    /**
     * 获取添加到黑名单结果
     *
     * @return
     */
    public LiveData<Resource<Void>> getAddBlackListResult() {
        return addBlackListResult;
    }

    /**
     * 获取移除黑名单结果
     *
     * @return
     */
    public LiveData<Resource<Void>> getRemoveBlackListResult() {
        return removeBlackListResult;
    }

    /**
     * 获取邀请好友结果
     *
     * @return
     */
    public LiveData<Resource<AddFriendResult>> getInviteFriendResult() {
        return inviteResult;
    }

    /**
     * 刷新好友列表
     */
    private void updateFriendList() {
        LiveData<Resource<List<FriendShipInfo>>> allFriends = friendTask.getAllFriends();
        allFriends.observeForever(new Observer<Resource<List<FriendShipInfo>>>() {
            @Override
            public void onChanged(Resource<List<FriendShipInfo>> listResource) {
                if (listResource.status != Status.LOADING) {
                    allFriends.removeObserver(this);
                }
            }
        });
    }

    public static class Factory implements ViewModelProvider.Factory {
        private Application application;
        private String userId;

        public Factory(Application application, String userId) {
            this.application = application;
            this.userId = userId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            try {
                return modelClass.getConstructor(Application.class, String.class).newInstance(application, userId);
            } catch (Exception e) {
                throw new RuntimeException("Cannot create an instance of " + modelClass, e);
            }
        }
    }
}
