package com.lr.biyou.rongyun.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lr.biyou.rongyun.model.PrivacyResult;
import com.lr.biyou.rongyun.model.Resource;
import com.lr.biyou.rongyun.task.PrivacyTask;
import com.lr.biyou.rongyun.utils.SingleSourceLiveData;

public class PrivacyViewModel extends AndroidViewModel {
    private PrivacyTask privacyTask;
    private SingleSourceLiveData<Resource<Void>> setPrivacyResult = new SingleSourceLiveData<>();
    private SingleSourceLiveData<Resource<PrivacyResult>> privacyResult = new SingleSourceLiveData<>();

    public PrivacyViewModel(@NonNull Application application) {
        super(application);
        privacyTask = new PrivacyTask(application);
        requestPrivacyState();
    }

    /**
     * 获取个人隐私设置状态
     */
    public void requestPrivacyState() {
        privacyResult.setSource(privacyTask.getPrivacyState());
    }

    public LiveData<Resource<PrivacyResult>> getPrivacyState(){
        return privacyResult;
    }

    /**
     * 用户隐私设置（可同时设置多项，传-1为不设置，0允许，1不允许）
     *
     * @param phoneVerify    是否可以通过电话号码查找
     * @param stSearchVerify 是否可以通过 SealTalk 号查找
     * @param friVerify      加好友验证
     * @param groupVerify    允许直接添加至群聊
     * @return
     */
    public void setPrivacy(int phoneVerify, int stSearchVerify,
                           int friVerify, int groupVerify) {
        setPrivacyResult.setSource(privacyTask.setPrivacy(phoneVerify, stSearchVerify, friVerify, groupVerify));
    }

    /**
     * 获取设置个人隐私返回结果
     *
     * @return
     */
    public LiveData<Resource<Void>> getSetPrivacyResult() {
        return setPrivacyResult;
    }
}
