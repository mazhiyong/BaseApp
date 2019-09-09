package com.lr.biyou.rongyun.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.lr.biyou.rongyun.db.dao.FriendDao;
import com.lr.biyou.rongyun.db.dao.GroupDao;
import com.lr.biyou.rongyun.db.dao.GroupMemberDao;
import com.lr.biyou.rongyun.db.dao.UserDao;
import com.lr.biyou.rongyun.db.model.BlackListEntity;
import com.lr.biyou.rongyun.db.model.FriendInfo;
import com.lr.biyou.rongyun.db.model.GroupEntity;
import com.lr.biyou.rongyun.db.model.GroupMemberInfoEntity;
import com.lr.biyou.rongyun.db.model.GroupNoticeInfo;
import com.lr.biyou.rongyun.db.model.PhoneContactInfoEntity;
import com.lr.biyou.rongyun.db.model.UserInfo;

@Database(entities = {UserInfo.class, FriendInfo.class, GroupEntity.class, GroupMemberInfoEntity.class,
        BlackListEntity.class, GroupNoticeInfo.class, PhoneContactInfoEntity.class}, version = 2, exportSchema = false)
@TypeConverters(com.lr.biyou.rongyun.db.TypeConverters.class)
public abstract class SealTalkDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();

    public abstract FriendDao getFriendDao();

    public abstract GroupDao getGroupDao();

    public abstract GroupMemberDao getGroupMemberDao();
}
