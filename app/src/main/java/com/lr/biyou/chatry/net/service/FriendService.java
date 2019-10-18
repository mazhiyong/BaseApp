package com.lr.biyou.chatry.net.service;

import androidx.lifecycle.LiveData;

import com.lr.biyou.chatry.db.model.FriendShipInfo;
import com.lr.biyou.chatry.model.AddFriendResult;
import com.lr.biyou.chatry.model.GetContactInfoResult;
import com.lr.biyou.chatry.model.Result;
import com.lr.biyou.chatry.model.SearchFriendInfo;
import com.lr.biyou.chatry.net.SealTalkUrl;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface FriendService {

    /**
     * 获取所有好友信息
     *
     * @return
     */
    @GET(SealTalkUrl.GET_FRIEND_ALL)
    LiveData<Result<List<FriendShipInfo>>> getAllFriendList();

    /**
     * 获取好友信息  ***
     *
     * @param
     * @return
     */
  /*  @GET(SealTalkUrl.GET_FRIEND_PROFILE)
    LiveData<Result<FriendShipInfo>> getFriendInfo(@Path("friendId") String friendId);*/

    @POST(SealTalkUrl.GET_FRIEND_PROFILE)
    LiveData<Map<String,Object>> getFriendInfo(@Body RequestBody body);

    /**
     * 同意添加好友
     *
     * @return
     */
    @POST(SealTalkUrl.ARGEE_FRIENDS)
    LiveData<Result<Boolean>> agreeFriend(@Body RequestBody body);

    /**
     * 忽略好友请求
     *
     * @return
     */
    @POST(SealTalkUrl.INGORE_FRIENDS)
    LiveData<Result<Void>> ingoreFriend(@Body RequestBody body);

    /**
     * 设置好友备注名
     *
     * @param body
     * @return
     */
    @POST(SealTalkUrl.SET_DISPLAY_NAME)
    LiveData<Result> setFriendAlias(@Body RequestBody body);

    /**
     * 申请添加好友
     *
     * @param body
     * @return
     */
    @POST(SealTalkUrl.INVITE_FRIEND)
    LiveData<Result<AddFriendResult>> inviteFriend(@Body RequestBody body);

    /**
     * 搜索好友
     *
     * @param queryMap
     * @return
     */
    @GET(SealTalkUrl.FIND_FRIEND)
    LiveData<Result<SearchFriendInfo>> searchFriend(@QueryMap(encoded = true) Map<String, String> queryMap);

    @POST(SealTalkUrl.DELETE_FREIND)
    LiveData<Result> deleteFriend(@Body RequestBody body);

    /**
     * 获取手机通讯录中的人员信息
     *
     * @param body
     * @return
     */
    @POST(SealTalkUrl.GET_CONTACTS_INFO)
    LiveData<Result<List<GetContactInfoResult>>> getContactsInfo(@Body RequestBody body);
}
