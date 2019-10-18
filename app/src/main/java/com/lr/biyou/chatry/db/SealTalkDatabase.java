package com.lr.biyou.chatry.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.lr.biyou.chatry.db.dao.FriendDao;
import com.lr.biyou.chatry.db.dao.GroupDao;
import com.lr.biyou.chatry.db.dao.GroupMemberDao;
import com.lr.biyou.chatry.db.dao.UserDao;
import com.lr.biyou.chatry.db.model.BlackListEntity;
import com.lr.biyou.chatry.db.model.FriendInfo;
import com.lr.biyou.chatry.db.model.GroupEntity;
import com.lr.biyou.chatry.db.model.GroupMemberInfoEntity;
import com.lr.biyou.chatry.db.model.GroupNoticeInfo;
import com.lr.biyou.chatry.db.model.PhoneContactInfoEntity;
import com.lr.biyou.chatry.db.model.UserInfo;

@Database(entities = {UserInfo.class, FriendInfo.class, GroupEntity.class, GroupMemberInfoEntity.class,
        BlackListEntity.class, GroupNoticeInfo.class, PhoneContactInfoEntity.class}, version = 2, exportSchema = false)
@TypeConverters(com.lr.biyou.chatry.db.TypeConverters.class)
public abstract class SealTalkDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();

    public abstract FriendDao getFriendDao();

    public abstract GroupDao getGroupDao();

    public abstract GroupMemberDao getGroupMemberDao();
}
