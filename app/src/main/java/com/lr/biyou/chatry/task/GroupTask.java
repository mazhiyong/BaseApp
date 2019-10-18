package com.lr.biyou.chatry.task;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.lr.biyou.chatry.db.DbManager;
import com.lr.biyou.chatry.db.dao.FriendDao;
import com.lr.biyou.chatry.db.dao.GroupDao;
import com.lr.biyou.chatry.db.dao.GroupMemberDao;
import com.lr.biyou.chatry.db.model.FriendShipInfo;
import com.lr.biyou.chatry.db.model.GroupEntity;
import com.lr.biyou.chatry.db.model.GroupMemberInfoEntity;
import com.lr.biyou.chatry.db.model.GroupNoticeInfo;
import com.lr.biyou.chatry.db.model.UserInfo;
import com.lr.biyou.chatry.file.FileManager;
import com.lr.biyou.chatry.im.IMManager;
import com.lr.biyou.chatry.model.AddMemberResult;
import com.lr.biyou.chatry.model.GroupMember;
import com.lr.biyou.chatry.model.GroupMemberInfoResult;
import com.lr.biyou.chatry.model.GroupNoticeInfoResult;
import com.lr.biyou.chatry.model.GroupNoticeResult;
import com.lr.biyou.chatry.model.GroupResult;
import com.lr.biyou.chatry.model.Resource;
import com.lr.biyou.chatry.model.Result;
import com.lr.biyou.chatry.model.Status;
import com.lr.biyou.chatry.model.UserSimpleInfo;
import com.lr.biyou.chatry.net.HttpClientManager;
import com.lr.biyou.chatry.net.RetrofitUtil;
import com.lr.biyou.chatry.net.service.GroupService;
import com.lr.biyou.chatry.ui.adapter.models.SearchGroupMember;
import com.lr.biyou.chatry.utils.NetworkBoundResource;
import com.lr.biyou.chatry.utils.NetworkOnlyResource;
import com.lr.biyou.chatry.utils.RongGenerate;
import com.lr.biyou.chatry.utils.SearchUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.imkit.tools.CharacterParser;
import io.rong.imlib.model.Conversation;
import okhttp3.RequestBody;

public class GroupTask {
    private GroupService groupService;
    private Context context;
    private DbManager dbManager;
    private FileManager fileManager;

    public GroupTask(Context context) {
        this.context = context.getApplicationContext();
        groupService = HttpClientManager.getInstance(context).getClient().createService(GroupService.class);
        dbManager = DbManager.getInstance(context);
        fileManager = new FileManager(context);
    }

    /**
     * 创建群组
     *
     * @param groupName
     * @param memberList
     * @return
     */
    public LiveData<Resource<GroupResult>> createGroup(String groupName, List<String> memberList) {
        return new NetworkOnlyResource<GroupResult, Result<GroupResult>>() {
            @NonNull
            @Override
            protected LiveData<Result<GroupResult>> createCall() {
                HashMap<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("name", groupName);
                bodyMap.put("memberIds", memberList);
                return groupService.createGroup(RetrofitUtil.createJsonRequest(bodyMap));
            }
        }.asLiveData();
    }

    /**
     * 添加群成员
     *
     * @param groupId
     * @param memberList
     * @return
     */
    public LiveData<Resource<List<AddMemberResult>>> addGroupMember(String groupId, List<String> memberList) {
        return new NetworkOnlyResource<List<AddMemberResult>, Result<List<AddMemberResult>>>() {
            @NonNull
            @Override
            protected LiveData<Result<List<AddMemberResult>>> createCall() {
                HashMap<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("groupId", groupId);
                bodyMap.put("memberIds", memberList);
                return groupService.addGroupMember(RetrofitUtil.createJsonRequest(bodyMap));
            }
        }.asLiveData();
    }

    /**
     * 加入群组
     *
     * @param groupId
     * @return
     */
    public LiveData<Resource<Void>> joinGroup(String groupId) {
        return new NetworkOnlyResource<Void, Result>() {
            @NonNull
            @Override
            protected LiveData<Result> createCall() {
                HashMap<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("groupId", groupId);
                return groupService.joinGroup(RetrofitUtil.createJsonRequest(bodyMap));
            }
        }.asLiveData();
    }

    /**
     * 群主或群管理将群成员移出群组
     *
     * @param groupId
     * @param memberList
     * @return
     */
    public LiveData<Resource<Void>> kickGroupMember(String groupId, List<String> memberList) {
        return new NetworkOnlyResource<Void, Result>() {
            @Override
            protected void saveCallResult(@NonNull Void item) {
                GroupMemberDao groupMemberDao = dbManager.getGroupMemberDao();
                if (groupMemberDao != null) {
                    groupMemberDao.deleteGroupMember(groupId, memberList);
                }
            }

            @NonNull
            @Override
            protected LiveData<Result> createCall() {
                HashMap<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("groupId", groupId);
                bodyMap.put("memberIds", memberList);
                return groupService.kickMember(RetrofitUtil.createJsonRequest(bodyMap));
            }
        }.asLiveData();
    }

    /**
     * 退出群组
     *
     * @param groupId
     * @return
     */
    public LiveData<Resource<Void>> quitGroup(String groupId) {
        return new NetworkOnlyResource<Void, Result>() {
            @Override
            protected void saveCallResult(@NonNull Void item) {
                GroupDao groupDao = dbManager.getGroupDao();
                if (groupDao != null) {
                    groupDao.deleteGroup(groupId);
                }

                GroupMemberDao groupMemberDao = dbManager.getGroupMemberDao();
                if (groupMemberDao != null) {
                    groupMemberDao.deleteGroupMember(groupId);
                }

                IMManager.getInstance().clearConversationAndMessage(groupId, Conversation.ConversationType.GROUP);
            }

            @NonNull
            @Override
            protected LiveData<Result> createCall() {
                HashMap<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("groupId", groupId);
                return groupService.quitGroup(RetrofitUtil.createJsonRequest(bodyMap));
            }
        }.asLiveData();
    }

    /**
     * 解散群组
     *
     * @param groupId
     * @return
     */
    public LiveData<Resource<Void>> dismissGroup(String groupId) {
        return new NetworkOnlyResource<Void, Result>() {
            @Override
            protected void saveCallResult(@NonNull Void item) {
                GroupDao groupDao = dbManager.getGroupDao();
                if (groupDao != null) {
                    groupDao.deleteGroup(groupId);
                }

                GroupMemberDao groupMemberDao = dbManager.getGroupMemberDao();
                if (groupMemberDao != null) {
                    groupMemberDao.deleteGroupMember(groupId);
                }

                IMManager.getInstance().clearConversationAndMessage(groupId, Conversation.ConversationType.GROUP);
            }

            @NonNull
            @Override
            protected LiveData<Result> createCall() {
                HashMap<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("groupId", groupId);
                return groupService.dismissGroup(RetrofitUtil.createJsonRequest(bodyMap));
            }
        }.asLiveData();
    }

    /**
     * 转移群主
     *
     * @param groupId
     * @param userId
     * @return
     */
    public LiveData<Resource<Void>> transferGroup(String groupId, String userId) {
        return new NetworkOnlyResource<Void, Result>() {
            @NonNull
            @Override
            protected LiveData<Result> createCall() {
                HashMap<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("groupId", groupId);
                bodyMap.put("userId", userId);
                return groupService.transferGroup(RetrofitUtil.createJsonRequest(bodyMap));
            }

        }.asLiveData();
    }

    /**
     * 重命名群名称
     *
     * @param groupId
     * @param groupName
     * @return
     */
    public LiveData<Resource<Void>> renameGroup(String groupId, String groupName) {
        return new NetworkOnlyResource<Void, Result>() {
            @Override
            protected void saveCallResult(@NonNull Void item) {
                // 更新数据库中群组的名称
                GroupDao groupDao = dbManager.getGroupDao();
                if (groupDao != null) {
                    int updateResult;
                    updateResult = groupDao.updateGroupName(groupId, groupName, CharacterParser.getInstance().getSelling(groupName));

                    // 更新成时同时更新缓存
                    if (updateResult > 0) {
                        GroupEntity groupInfo = groupDao.getGroupInfoSync(groupId);
                        if (groupInfo != null) {
                            IMManager.getInstance().updateGroupInfoCache(groupId, groupName, Uri.parse(groupInfo.getPortraitUri()));
                        }
                    }
                }

            }

            @NonNull
            @Override
            protected LiveData<Result> createCall() {
                HashMap<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("groupId", groupId);
                bodyMap.put("name", groupName);
                return groupService.renameGroup(RetrofitUtil.createJsonRequest(bodyMap));
            }
        }.asLiveData();
    }

    /**
     * 获取定时清理状态信息
     *
     * @param groupId
     * @return
     */
    public LiveData<Resource<Integer>> getRegularClearState(String groupId) {
        return new NetworkBoundResource<Integer, Result<Integer>>() {

            @Override
            protected void saveCallResult(@NonNull Result<Integer> item) {
                if (item.code == 200 && item.result != null) {
                    updateGroupRegularClearStateInDB(groupId, item.result);
                }
            }

            @NonNull
            @Override
            protected LiveData<Integer> loadFromDb() {
                GroupDao groupDao = dbManager.getGroupDao();
                LiveData<Integer> regularClearState = null;
                if (groupDao != null) {
                    regularClearState = groupDao.getRegularClearSync(groupId);
                }
                return regularClearState;
            }

            @NonNull
            @Override
            protected LiveData<Result<Integer>> createCall() {
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("groupId", groupId);
                return groupService.getRegularClearState(RetrofitUtil.createJsonRequest(paramMap));
            }
        }.asLiveData();
    }

    /**
     * 设置定时清理群消息
     *
     * @param groupId
     * @param clearStatus
     * @return
     */
    public LiveData<Resource<Void>> setRegularClear(String groupId, int clearStatus) {
        return new NetworkOnlyResource<Void, Result>() {
            @NonNull
            @Override
            protected LiveData<Result> createCall() {
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("groupId", groupId);
                paramMap.put("clearStatus", clearStatus);
                return groupService.setRegularClear(RetrofitUtil.createJsonRequest(paramMap));
            }

            @Override
            protected void saveCallResult(@NonNull Void item) {
                updateGroupRegularClearStateInDB(groupId, clearStatus);
            }
        }.asLiveData();
    }

    private void updateGroupRegularClearStateInDB(String groupId, int state) {
        GroupDao groupDao = dbManager.getGroupDao();
        if (groupDao == null) return;

        groupDao.updateRegularClearState(groupId, state);
    }

    /**
     * 设置群公告
     *
     * @param groupId
     * @param bulletin
     * @return
     */
    public LiveData<Resource<Void>> setGroupNotice(String groupId, String bulletin) {
        return new NetworkOnlyResource<Void, Result>() {
            @NonNull
            @Override
            protected LiveData<Result> createCall() {
                HashMap<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("groupId", groupId);
                bodyMap.put("bulletin", bulletin);
                return groupService.setGroupBulletin(RetrofitUtil.createJsonRequest(bodyMap));
            }
        }.asLiveData();
    }

    /**
     * 获取群公告
     *
     * @param groupId
     * @return
     */
    public LiveData<Resource<GroupNoticeResult>> getGroupNotice(String groupId) {
        return new NetworkOnlyResource<GroupNoticeResult, Result<GroupNoticeResult>>() {
            @Override
            protected void saveCallResult(@NonNull GroupNoticeResult result) {
                GroupDao groupDao = dbManager.getGroupDao();
                if (groupDao != null) {
                    groupDao.updateGroupNotice(groupId, result.getContent(), result.getTimestamp());
                }
            }

            @NonNull
            @Override
            protected LiveData<Result<GroupNoticeResult>> createCall() {
                return groupService.getGroupBulletin(groupId);
            }
        }.asLiveData();
    }

    /**
     * 上传并设置群组头像
     *
     * @param groupId
     * @param portraitUrl
     * @return
     */
    public LiveData<Resource<Void>> uploadAndSetGroupPortrait(String groupId, Uri portraitUrl) {
        MediatorLiveData<Resource<Void>> result = new MediatorLiveData<>();
        // 先上传图片文件
        LiveData<Resource<String>> uploadResource = fileManager.uploadImage(portraitUrl);
        result.addSource(uploadResource, resource -> {
            if (resource.status != Status.LOADING) {
                result.removeSource(uploadResource);
            }

            if (resource.status == Status.ERROR) {
                result.setValue(Resource.error(resource.code, null));
                return;
            }

            if (resource.status == Status.SUCCESS) {
                String uploadUrl = resource.data;

                // 获取上传成功的地址后更新地址
                LiveData<Resource<Void>> setPortraitResource = setGroupPortrait(groupId, uploadUrl);
                result.addSource(setPortraitResource, portraitResultResource -> {
                    if (portraitResultResource.status != Status.LOADING) {
                        result.removeSource(setPortraitResource);
                    }

                    if (portraitResultResource.status == Status.ERROR) {
                        result.setValue(Resource.error(portraitResultResource.code, null));
                        return;
                    }

                    if (portraitResultResource.status == Status.SUCCESS) {
                        result.setValue(Resource.success(null));
                    }
                });
            }
        });

        return result;
    }

    /**
     * 设置群组头像
     *
     * @param groupId
     * @param portraitUrl 云存储空间的 url
     * @return
     */
    private LiveData<Resource<Void>> setGroupPortrait(String groupId, String portraitUrl) {
        return new NetworkOnlyResource<Void, Result>() {
            @Override
            protected void saveCallResult(@NonNull Void item) {
                // 更新数据库中群组的头像
                GroupDao groupDao = dbManager.getGroupDao();
                if (groupDao != null) {
                    int updateResult;
                    updateResult = groupDao.updateGroupPortrait(groupId, portraitUrl);

                    // 更新成时同时更新缓存
                    if (updateResult > 0) {
                        GroupEntity groupInfo = groupDao.getGroupInfoSync(groupId);
                        IMManager.getInstance().updateGroupInfoCache(groupId, groupInfo.getName(), Uri.parse(portraitUrl));
                    }
                }
            }

            @NonNull
            @Override
            protected LiveData<Result> createCall() {
                HashMap<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("groupId", groupId);
                bodyMap.put("portraitUri", portraitUrl);
                return groupService.setGroupPortraitUri(RetrofitUtil.createJsonRequest(bodyMap));
            }
        }.asLiveData();
    }

    /**
     * 设置群内昵称
     *
     * @param groupId
     * @param displayName
     * @return
     */
    public LiveData<Resource<Void>> setMemberDisplayName(String groupId, String displayName) {
        return new NetworkOnlyResource<Void, Result>() {
            @NonNull
            @Override
            protected LiveData<Result> createCall() {
                HashMap<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("groupId", groupId);
                bodyMap.put("displayName", displayName);
                return groupService.setMemberDisplayName(RetrofitUtil.createJsonRequest(bodyMap));
            }
        }.asLiveData();
    }

    /**
     * 获取群组信息
     *
     * @param groupId
     * @return
     */
    public LiveData<Resource<GroupEntity>> getGroupInfo(final String groupId) {
        return new NetworkBoundResource<GroupEntity, Result<GroupEntity>>() {
            @Override
            protected void saveCallResult(@NonNull Result<GroupEntity> item) {
                if (item.getResult() == null) return;

                GroupEntity groupEntity = item.getResult();
                GroupDao groupDao = dbManager.getGroupDao();
                if (groupDao != null) {
                    // 判断是否在通讯录中
                    int groupIsContact = groupDao.getGroupIsContactSync(groupId);

                    String portraitUri = groupEntity.getPortraitUri();
                    if (TextUtils.isEmpty(portraitUri)) {
                        portraitUri = RongGenerate.generateDefaultAvatar(context, groupEntity.getId(), groupEntity.getName());
                        groupEntity.setPortraitUri(portraitUri);
                    }
                    groupEntity.setNameSpelling(SearchUtils.fullSearchableString(groupEntity.getName()));
                    groupEntity.setNameSpellingInitial(SearchUtils.initialSearchableString(groupEntity.getName()));
                    groupEntity.setOrderSpelling(CharacterParser.getInstance().getSelling(groupEntity.getName()));
                    groupEntity.setIsInContact(groupIsContact);
                    groupDao.insertGroup(groupEntity);
                }

                // 更新 IMKit 缓存群组数据
                IMManager.getInstance().updateGroupInfoCache(groupEntity.getId(), groupEntity.getName(), Uri.parse(groupEntity.getPortraitUri()));
            }

            @NonNull
            @Override
            protected LiveData<GroupEntity> loadFromDb() {
                GroupDao groupDao = dbManager.getGroupDao();
                LiveData<GroupEntity> groupInfo = null;
                if (groupDao != null) {
                    groupInfo = groupDao.getGroupInfo(groupId);
                }
                return groupInfo;
            }

            @NonNull
            @Override
            protected LiveData<Result<GroupEntity>> createCall() {
                return groupService.getGroupInfo(groupId);
            }
        }.asLiveData();
    }

    /**
     * 获取群组信息 ( 同步方法 )
     *
     * @param groupId
     * @return
     */
    public GroupEntity getGroupInfoSync(final String groupId) {
        return dbManager.getGroupDao().getGroupInfoSync(groupId);
    }

    /**
     * 获取群组 list 信息 ( 同步方法 )
     *
     * @param groupIds
     * @return
     */
    public List<GroupEntity> getGroupInfoListSync(String[] groupIds) {
        return dbManager.getGroupDao().getGroupInfoListSync(groupIds);
    }

    /**
     * 获取群组 list 信息 ( 异步 )
     *
     * @param groupIds
     * @return
     */
    public LiveData<List<GroupEntity>> getGroupInfoList(String[] groupIds) {
        return dbManager.getGroupDao().getGroupInfoList(groupIds);
    }

    public LiveData<GroupEntity> getGroupInfoInDB(String groupIds) {
        return dbManager.getGroupDao().getGroupInfo(groupIds);
    }

    /**
     * 获取群成员列表,通过成员名称筛选
     *
     * @param groupId
     * @param filterByName 通过姓名模糊匹配
     * @return
     */
    public LiveData<Resource<List<GroupMember>>> getGroupMemberInfoList(final String groupId, String filterByName) {
        return new NetworkBoundResource<List<GroupMember>, Result<List<GroupMemberInfoResult>>>() {
            @Override
            protected void saveCallResult(@NonNull Result<List<GroupMemberInfoResult>> item) {
                if (item.getResult() == null) return;

                GroupMemberDao groupMemberDao = dbManager.getGroupMemberDao();
                //UserDao userDao = dbManager.getUserDao();
                FriendDao friendDao = dbManager.getFriendDao();

                // 获取新数据前清除掉原成员信息
                if (groupMemberDao != null) {
                    groupMemberDao.deleteGroupMember(groupId);
                }

                List<GroupMemberInfoResult> result = item.getResult();
                List<GroupMemberInfoEntity> groupEntityList = new ArrayList<>();
                List<UserInfo> newUserList = new ArrayList<>();
                for (GroupMemberInfoResult info : result) {
                    UserSimpleInfo user = info.getUser();
                    GroupMemberInfoEntity groupEntity = new GroupMemberInfoEntity();
                    groupEntity.setGroupId(groupId);

                    // 默认优先显示群备注名。当没有群备注时，则看此用户为当前用户的好友，如果是好友则显示备注名称。其次再试显示用户名
                    String displayName = TextUtils.isEmpty(info.getDisplayName()) ? "" : info.getDisplayName();
                    String cacheName = displayName;

                    if (TextUtils.isEmpty(cacheName)) {
                        // Kit 缓存中是需要优先显示备注备注的
                        FriendShipInfo friendInfoSync = friendDao.getFriendInfoSync(user.getId());
                        if (friendInfoSync != null) {
                            cacheName = friendInfoSync.getDisplayName();
                            if (TextUtils.isEmpty(cacheName)) {
                                cacheName = friendInfoSync.getUser().getNickname();
                            }
                        } else {
                            cacheName = user.getName();
                        }
                    }

                    groupEntity.setNickName(displayName);
                    groupEntity.setNickNameSpelling(CharacterParser.getInstance().getSelling(displayName));
                    groupEntity.setUserId(user.getId());
                    groupEntity.setRole(info.getRole());
                    groupEntity.setCreateTime(info.getCreatedTime());
                    groupEntity.setUpdateTime(info.getUpdatedTime());
                    groupEntity.setJoinTime(info.getTimestamp());
                    groupEntityList.add(groupEntity);

                    // 更新 IMKit 缓存群组成员数据
                    IMManager.getInstance().updateGroupMemberInfoCache(groupId, user.getId(), cacheName);

                   /* if (userDao != null) {
                        // 更新已存在的用户信息
                        String portraitUri = user.getPortraitUri();

                        // 当没有头像时生成默认头像
                        if (TextUtils.isEmpty(portraitUri)) {
                            portraitUri = RongGenerate.generateDefaultAvatar(context, user.getId(), user.getName());
                            user.setPortraitUri(portraitUri);
                        }
                        int updateResult = userDao.updateNameAndPortrait(user.getId(), user.getName(), CharacterParser.getInstance().getSelling(user.getName()), user.getPortraitUri());

                        // 当没有更新成功时，添加到新用户列表中
                        if (updateResult == 0) {
                            UserInfo userInfo = new UserInfo();
                            userInfo.setId(user.getId());
                            userInfo.setName(user.getName());
                            userInfo.setNameSpelling(SearchUtils.fullSearchableString(user.getName()));
                            userInfo.setPortraitUri(user.getPortraitUri());
                            newUserList.add(userInfo);
                        }
                    }*/
                }

                // 更新群组成员
                if (groupMemberDao != null) {
                    groupMemberDao.insertGroupMemberList(groupEntityList);
                }

               /* if (userDao != null) {
                    // 插入新的用户信息
                    userDao.insertUserListIgnoreExist(newUserList);
                }
*/
            }

            @Override
            protected boolean shouldFetch(@Nullable List<GroupMember> data) {
                boolean shouldFetch = true;
                // 当数据库中有群成员数据时，当用姓名进行筛选时不进行网络请求
                if (data != null && data.size() > 0 && !TextUtils.isEmpty(filterByName)) {
                    shouldFetch = false;
                }
                return shouldFetch;
            }

            @NonNull
            @Override
            protected LiveData<List<GroupMember>> loadFromDb() {
                GroupMemberDao groupMemberDao = dbManager.getGroupMemberDao();
                if (groupMemberDao != null) {
                    if (TextUtils.isEmpty(filterByName)) {
                        return groupMemberDao.getGroupMemberList(groupId);
                    } else {
                        return groupMemberDao.getGroupMemberList(groupId, filterByName);
                    }
                }
                return new MutableLiveData<>(null);
            }

            @NonNull
            @Override
            protected LiveData<Result<List<GroupMemberInfoResult>>> createCall() {
                return groupService.getGroupMemberList(groupId);
            }
        }.asLiveData();
    }

    /**
     * 获取群成员列表
     *
     * @param groupId
     * @return
     */
    public LiveData<Resource<List<GroupMember>>> getGroupMemberInfoList(final String groupId) {
        return getGroupMemberInfoList(groupId, null);
    }

    public LiveData<List<SearchGroupMember>> searchGroup(String match) {
        return dbManager.getGroupDao().searchGroup(match);
    }

    public LiveData<List<GroupEntity>> searchGroupByName(String match) {
        return dbManager.getGroupDao().searchGroupByName(match);
    }

    public LiveData<List<GroupEntity>> searchGroupContactByName(String match) {
        return dbManager.getGroupDao().searchGroupContactByName(match);
    }


    /**
     * 删除管理员
     *
     * @param groupId
     * @param memberIds
     * @return
     */
    public LiveData<Resource<Void>> removeManager(String groupId, String[] memberIds) {
        return new NetworkOnlyResource<Void, Result>() {
            @NonNull
            @Override
            protected LiveData createCall() {
                HashMap<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("groupId", groupId);
                bodyMap.put("memberIds", memberIds);
                RequestBody body = RetrofitUtil.createJsonRequest(bodyMap);
                return groupService.removeManager(body);
            }
        }.asLiveData();
    }

    /**
     * 设置禁言
     *
     * @param groupId
     * @param muteStatus 1开启禁言，0关闭禁言
     * @param userId     可发言用户id，不设置除了群主和管理员，全员禁言
     * @return
     */
    public LiveData<Resource<Void>> setMuteAll(String groupId, int muteStatus, String userId) {
        return new NetworkOnlyResource<Void, Result>() {
            @NonNull
            @Override
            protected LiveData<Result> createCall() {
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("groupId", groupId);
                paramMap.put("muteStatus", muteStatus);
                if (!TextUtils.isEmpty(userId)) {
                    paramMap.put("userId", userId);
                }
                return groupService.muteAll(RetrofitUtil.createJsonRequest(paramMap));
            }

            @Override
            protected void saveCallResult(@NonNull Void item) {
                GroupDao groupDao = dbManager.getGroupDao();
                if (groupDao != null) {
                    groupDao.updateMuteAllState(groupId, muteStatus);
                }
            }
        }.asLiveData();
    }

    /**
     * 入群认证
     *
     * @param groupId
     * @param certiStatus 认证状态： 0 开启(需要认证)、1 关闭（不需要认证）
     * @return
     */
    public LiveData<Resource<Void>> setCertification(String groupId, int certiStatus) {
        return new NetworkOnlyResource<Void, Result<Void>>() {

            @NonNull
            @Override
            protected LiveData<Result<Void>> createCall() {
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("groupId", groupId);
                paramMap.put("certiStatus", certiStatus);
                return groupService.setCertification(RetrofitUtil.createJsonRequest(paramMap));
            }

            @Override
            protected void saveCallResult(@NonNull Void item) {
                GroupDao groupDao = dbManager.getGroupDao();
                if (groupDao != null) {
                    groupDao.updateCertiStatus(groupId, certiStatus);
                }
                super.saveCallResult(item);
            }
        }.asLiveData();
    }

    /**
     * 添加管理员
     *
     * @param groupId
     * @param membersIds
     * @return
     */
    public LiveData<Resource<Void>> addManager(String groupId, String[] membersIds) {
        return new NetworkOnlyResource<Void, Result>() {
            @NonNull
            @Override
            protected LiveData createCall() {
                HashMap<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("groupId", groupId);
                bodyMap.put("memberIds", membersIds);
                RequestBody body = RetrofitUtil.createJsonRequest(bodyMap);
                return groupService.addManager(body);
            }
        }.asLiveData();
    }

    /**
     * 获取所有群信息
     *
     * @return
     */
    public LiveData<List<GroupEntity>> getAllGroupInfoList() {
        return dbManager.getGroupDao().getAllGroupInfoList();
    }

    public LiveData<GroupEntity> getGroupInfoFromDB(String groupId) {
        return dbManager.getGroupDao().getGroupInfo(groupId);
    }

    /**
     * 群组保存到通讯录
     *
     * @return
     */
    public LiveData<Resource<Void>> saveGroupToContact(String groupId) {
        return new NetworkOnlyResource<Void, Result>() {
            @Override
            protected void saveCallResult(@NonNull Void item) {
                updateGroupContactStateInDB(groupId, true);
            }

            @NonNull
            @Override
            protected LiveData<Result> createCall() {
                HashMap<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("groupId", groupId);
                return groupService.saveToContact(RetrofitUtil.createJsonRequest(bodyMap));
            }
        }.asLiveData();
    }

    /**
     * 群组从通讯录中移除
     *
     * @return
     */
    public LiveData<Resource<Void>> removeGroupFromContact(String groupId) {
        return new NetworkOnlyResource<Void, Result>() {
            @Override
            protected void saveCallResult(@NonNull Void item) {
                updateGroupContactStateInDB(groupId, false);
            }

            @NonNull
            @Override
            protected LiveData<Result> createCall() {
                HashMap<String, Object> bodyMap = new HashMap<>();
                bodyMap.put("groupId", groupId);
                return groupService.removeFromContact(RetrofitUtil.createJsonRequest(bodyMap));
            }
        }.asLiveData();
    }

    /**
     * 更新群组在通讯录状态
     *
     * @param isToContact
     */
    private void updateGroupContactStateInDB(String groupId, boolean isToContact) {
        GroupDao groupDao = dbManager.getGroupDao();
        if (groupDao == null) return;

        groupDao.updateGroupContactState(groupId, isToContact ? 1 : 0);
    }

    /**
     * 获取群通知消息详情
     *
     * @return
     */

    public LiveData<Resource<List<GroupNoticeInfo>>> getGroupNoticeInfo() {
        return new NetworkBoundResource<List<GroupNoticeInfo>, Result<List<GroupNoticeInfoResult>>>() {

            @Override
            protected void saveCallResult(@NonNull Result<List<GroupNoticeInfoResult>> item) {
                if (item.getResult() == null) return;

                GroupDao groupDao = dbManager.getGroupDao();

                List<GroupNoticeInfoResult> resultList = item.getResult();
                List<GroupNoticeInfo> infoList = new ArrayList<>();
                if (resultList != null && resultList.size() > 0) {
                    List<String> idList = new ArrayList<>();
                    for (GroupNoticeInfoResult infoResult : resultList) {
                        GroupNoticeInfo noticeInfo = new GroupNoticeInfo();
                        noticeInfo.setId(infoResult.id);
                        idList.add(infoResult.id);
                        noticeInfo.setCreatedAt(infoResult.createdAt);
                        noticeInfo.setCreatedTime(infoResult.timestamp);
                        noticeInfo.setType(infoResult.type);
                        noticeInfo.setStatus(infoResult.status);
                        if (infoResult.receiver != null) {
                            noticeInfo.setReceiverId(infoResult.receiver.id);
                            noticeInfo.setReceiverNickName(infoResult.receiver.nickname);
                        }
                        if (infoResult.requester != null) {
                            noticeInfo.setRequesterId(infoResult.requester.id);
                            noticeInfo.setRequesterNickName(infoResult.requester.nickname);
                        }
                        if (infoResult.group != null) {
                            noticeInfo.setGroupId(infoResult.group.id);
                            noticeInfo.setGroupNickName(infoResult.group.name);
                        }
                        infoList.add(noticeInfo);
                    }
                    //防止直接 delteAll 导致的返回数据 success 状态导致返回错误的空数据结果
                    groupDao.deleteAllGroupNotice(idList);
                    groupDao.insertGroupNotice(infoList);
                } else if (resultList != null){
                    // 返回无通知数据时清空数据库的数据
                    groupDao.deleteAllGroupNotice();
                }

            }


            @NonNull
            @Override
            protected LiveData<List<GroupNoticeInfo>> loadFromDb() {
                GroupDao groupDao = dbManager.getGroupDao();
                if (groupDao != null) {
                    LiveData<List<GroupNoticeInfo>> liveInfoList = groupDao.getGroupNoticeList();
                    return liveInfoList;
                }
                return new MutableLiveData<>(null);
            }

            @NonNull
            @Override
            protected LiveData<Result<List<GroupNoticeInfoResult>>> createCall() {
                return groupService.getGroupNoticeInfo();
            }

        }.asLiveData();
    }

    /**
     * 设置消息状态
     *
     * @param groupId
     * @param receiverId
     * @param status     0 忽略、 1 同意
     * @return
     */
    public LiveData<Resource<Void>> setNoticeStatus(String groupId, String receiverId, String status, String noticeId) {
        return new NetworkOnlyResource<Void, Result<Void>>() {

            @NonNull
            @Override
            protected LiveData<Result<Void>> createCall() {
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("groupId", groupId);
                paramMap.put("receiverId", receiverId);
                paramMap.put("status", Integer.valueOf(status));
                return groupService.setGroupNoticeStatus(RetrofitUtil.createJsonRequest(paramMap));
            }

            @Override
            protected void saveCallResult(@NonNull Void item) {
                GroupDao groupDao = dbManager.getGroupDao();
                // 更新通知状态
                if (groupDao != null) {
                    groupDao.updateGroupNoticeStatus(noticeId, Integer.valueOf(status));
                }
                super.saveCallResult(item);
            }
        }.asLiveData();
    }

    /**
     * 清空所有消息
     *
     * @return
     */
    public LiveData<Resource<Void>> clearGroupNotice() {
        return new NetworkOnlyResource<Void, Result<Void>>() {

            @NonNull
            @Override
            protected LiveData<Result<Void>> createCall() {
                return groupService.clearGroupNotice();
            }

            @Override
            protected void saveCallResult(@NonNull Void item) {
                GroupDao groupDao = dbManager.getGroupDao();
                if (groupDao != null) {
                    groupDao.deleteAllGroupNotice();
                }
                super.saveCallResult(item);
            }
        }.asLiveData();
    }

}
