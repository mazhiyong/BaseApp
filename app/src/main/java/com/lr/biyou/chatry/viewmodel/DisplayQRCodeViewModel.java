package com.lr.biyou.chatry.viewmodel;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.lr.biyou.chatry.db.model.GroupEntity;
import com.lr.biyou.chatry.db.model.UserInfo;
import com.lr.biyou.chatry.model.Resource;
import com.lr.biyou.chatry.file.FileManager;
import com.lr.biyou.chatry.task.GroupTask;
import com.lr.biyou.chatry.qrcode.QRCodeManager;
import com.lr.biyou.chatry.task.UserTask;
import com.lr.biyou.chatry.utils.SingleSourceLiveData;

public class DisplayQRCodeViewModel extends AndroidViewModel {
    private SingleSourceLiveData<Resource<Bitmap>> qrCodeResult = new SingleSourceLiveData<>();
    private SingleSourceLiveData<Resource<GroupEntity>> groupInfo = new SingleSourceLiveData<>();
    private SingleSourceLiveData<Resource<String>> saveLocalBitmapResult = new SingleSourceLiveData<>();
    private SingleSourceLiveData<Resource<String>> saveCacheBitmapResult = new SingleSourceLiveData<>();
    private SingleSourceLiveData<Resource<UserInfo>> userInfoResult = new SingleSourceLiveData<>();
    private UserTask userTask;
    private GroupTask groupTask;
    private QRCodeManager qrCodeManager;
    private FileManager fileManager;

    public DisplayQRCodeViewModel(@NonNull Application application) {
        super(application);

        userTask = new UserTask(application);
        groupTask = new GroupTask(application);
        qrCodeManager = new QRCodeManager(application);
        fileManager = new FileManager(application);
    }

    public void requestGroupQRCode(String groupId, String sharedUserId, int qrCodeWidth, int qrCodeHeight) {
        qrCodeResult.setSource(qrCodeManager.getGroupQRCode(groupId, sharedUserId, qrCodeWidth, qrCodeHeight));
    }

    public void requestUserQRCode(String userId, int qrCodeWidth, int qrCodeHeight) {
        qrCodeResult.setSource(qrCodeManager.getUserQRCode(userId, qrCodeWidth, qrCodeHeight));
    }

    public LiveData<Resource<Bitmap>> getQRCode() {
        return qrCodeResult;
    }

    public void requestGroupInfo(String groupId) {
        groupInfo.setSource(groupTask.getGroupInfo(groupId));
    }

    public void requestUserInfo(String userId) {
        userInfoResult.setSource(userTask.getUserInfo(userId));
    }

    public LiveData<Resource<GroupEntity>> getGroupInfo() {
        return groupInfo;
    }

    public LiveData<Resource<UserInfo>> getUserInfo() {
        return userInfoResult;
    }

    public void saveQRCodeToLocal(Bitmap bitmap) {
        saveLocalBitmapResult.setSource(fileManager.saveBitmapToPictures(bitmap));
    }

    public LiveData<Resource<String>> getSaveLocalBitmapResult() {
        return saveLocalBitmapResult;
    }

    public SingleSourceLiveData<Resource<String>> getSaveCacheBitmapResult() {
        return saveCacheBitmapResult;
    }

    public void saveQRCodeToCache(Bitmap bitmap) {
        saveCacheBitmapResult.setSource(fileManager.saveBitmapToCache(bitmap));
    }
}

