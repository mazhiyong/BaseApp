package com.lr.biyou.chatry.db;

import android.content.Context;

import androidx.room.Room;

import com.lr.biyou.chatry.db.dao.FriendDao;
import com.lr.biyou.chatry.db.dao.GroupDao;
import com.lr.biyou.chatry.db.dao.GroupMemberDao;
import com.lr.biyou.chatry.db.dao.UserDao;
import com.lr.biyou.chatry.common.LogTag;
import com.lr.biyou.chatry.utils.log.SLog;
import io.rong.imlib.MD5;

/**
 * 数据库管理类
 */
public class DbManager {
    private final String DB_NAME_FORMAT = "user_%s";
    private volatile static DbManager instance;
    private Context context;
    private SealTalkDatabase database;
    private String currentUserId;

    private DbManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public static DbManager getInstance(Context context) {
        if (instance == null) {
            synchronized (DbManager.class) {
                if (instance == null) {
                    instance = new DbManager(context);
                }
            }
        }

        return instance;
    }

    /**
     * 打开指定用户数据库
     *
     * @param userId
     */
    public void openDb(String userId) {
        if (currentUserId != null) {
            if (!currentUserId.equals(userId)) {
                closeDb();
            } else {
                SLog.d(LogTag.DB, "user:" + userId + ", has opened db.");
                return;
            }
        } else {
            // do nothing
        }
        currentUserId = userId;
        String userIdMD5 = MD5.encrypt(userId);
        database = Room.databaseBuilder(context, SealTalkDatabase.class, String.format(DB_NAME_FORMAT, userIdMD5))
                .fallbackToDestructiveMigration()
                .build();
        SLog.d(LogTag.DB, "openDb,userId:" + currentUserId);
    }

    public void closeDb() {
        if (database != null) {
            SLog.d(LogTag.DB, "closeDb,userId:" + currentUserId);
            database.close();
        }
        currentUserId = "";
    }

    public UserDao getUserDao() {
        if (database == null) {
            SLog.e(LogTag.DB, "Get Dao need openDb first.");
            return null;
        }
        //return database.getUserDao();
        return null;
    }

    public FriendDao getFriendDao() {
        if (database == null) {
            SLog.e(LogTag.DB, "Get Dao need openDb first.");
            return null;
        }
        return database.getFriendDao();
    }

    public GroupDao getGroupDao() {
        if (database == null) {
            SLog.e(LogTag.DB, "Get Dao need openDb first.");
            return null;
        }
        return database.getGroupDao();
    }

    public GroupMemberDao getGroupMemberDao() {
        if (database == null) {
            SLog.e(LogTag.DB, "Get Dao need openDb first.");
            return null;
        }
        return database.getGroupMemberDao();
    }
}
