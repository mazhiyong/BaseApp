package com.lr.biyou.rongyun.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lr.biyou.rongyun.db.model.UserInfo;

import java.util.List;

import com.lr.biyou.rongyun.model.GroupMember;
import com.lr.biyou.rongyun.model.Resource;
import com.lr.biyou.rongyun.task.GroupTask;
import com.lr.biyou.rongyun.task.UserTask;
import com.lr.biyou.rongyun.utils.SingleSourceLiveData;

/**
 * 群组消息已读回执视图模型
 */
public class GroupReadReceiptViewModel extends AndroidViewModel {
    private SingleSourceLiveData<Resource<List<GroupMember>>> groupMemberList = new SingleSourceLiveData<>();
    private SingleSourceLiveData<Resource<UserInfo>> userInfo = new SingleSourceLiveData<>();

    private GroupTask groupTask;
    private UserTask userTask;

    public GroupReadReceiptViewModel(@NonNull Application application) {
        super(application);

        groupTask = new GroupTask(application);
        userTask = new UserTask(application);
    }

    public void requestGroupMemberList(String groupId){
        groupMemberList.setSource(groupTask.getGroupMemberInfoList(groupId));
    }

    public void requestUserInfo(String userId){
        userInfo.setSource(userTask.getUserInfo(userId));
    }

    public LiveData<Resource<List<GroupMember>>> getGroupMemberList(){
        return groupMemberList;
    }

    public LiveData<Resource<UserInfo>> getUserInfo(){
        return userInfo;
    }
}
