package com.lr.biyou.chatry.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.lr.biyou.chatry.db.model.FriendShipInfo;
import com.lr.biyou.chatry.db.model.FriendStatus;

import java.util.List;

import com.lr.biyou.chatry.model.AddFriendResult;
import com.lr.biyou.chatry.model.Resource;
import com.lr.biyou.chatry.model.SearchFriendInfo;
import com.lr.biyou.chatry.model.Status;
import com.lr.biyou.chatry.task.FriendTask;
import com.lr.biyou.chatry.utils.SingleSourceLiveData;
import com.lr.biyou.chatry.utils.SingleSourceMapLiveData;
import com.lr.biyou.chatry.utils.log.SLog;


public class SearchFriendNetViewModel extends AndroidViewModel {
    private static final String TAG = "SearchFriendNetViewModel";
    private FriendTask friendTask;
    private SingleSourceLiveData<Resource<SearchFriendInfo>> searchFriend;
    private SingleSourceMapLiveData<FriendShipInfo, Boolean> isFriend;
    private SingleSourceMapLiveData<Resource<AddFriendResult>, Resource<AddFriendResult>> addFriend;

    public SearchFriendNetViewModel(@NonNull Application application) {
        super(application);
        friendTask = new FriendTask(application);
        searchFriend = new SingleSourceLiveData<>();
        addFriend = new SingleSourceMapLiveData<>(new Function<Resource<AddFriendResult>, Resource<AddFriendResult>>() {
            @Override
            public Resource<AddFriendResult> apply(Resource<AddFriendResult> input) {
                if(input.status == Status.SUCCESS){
                    // 邀请后刷新好友列表
                    updateFriendList();
                }

                return input;
            }
        });
        isFriend = new SingleSourceMapLiveData<FriendShipInfo, Boolean>(new Function<FriendShipInfo, Boolean>() {
            @Override
            public Boolean apply(FriendShipInfo input) {
                if(input != null){
                    return FriendStatus.getStatus(input.getStatus()) == FriendStatus.IS_FRIEND
                            || FriendStatus.getStatus(input.getStatus()) == FriendStatus.IN_BLACK_LIST;
                } else {
                    return false;
                }
            }
        });
    }

    public void searchFriendFromServer(String stAccount, String region, String phone) {
        SLog.i(TAG, "searchFriendFromServer region: " + region + " phoneSearchFr: " + phone);
        searchFriend.setSource(friendTask.searchFriendFromServer(stAccount, region, phone));
    }

    public LiveData<Resource<SearchFriendInfo>> getSearchFriend() {
        return searchFriend;
    }

    public LiveData<Boolean> getIsFriend() {
        return isFriend;
    }

    public void isFriend(String userId) {
        isFriend.setSource(friendTask.getFriendShipInfoFromDB(userId));
    }

    public void inviteFriend(String userId, String message) {
        addFriend.setSource(friendTask.inviteFriend(userId, message));
    }

    public LiveData<Resource<AddFriendResult>> getAddFriend() {
        return addFriend;
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
}
