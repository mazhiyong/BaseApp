package com.lr.biyou.chatry.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.lr.biyou.chatry.db.model.GroupMemberInfoEntity;

import java.util.List;

import com.lr.biyou.chatry.model.GroupMember;

@Dao
public interface GroupMemberDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGroupMemberList(List<GroupMemberInfoEntity> groupMemberInfoEntities);

    @Query("SELECT group_member.user_id, group_member.group_id, group_member.nickname, group_member.role, group_member.join_time, nickname_spelling,user.name, user.name_spelling, user.portrait_uri, user.alias" +
            " FROM group_member " +
            "INNER JOIN user ON group_member.user_id = user.id " +
            "where group_id=:groupId " +
            "ORDER BY join_time asc")
    LiveData<List<GroupMember>> getGroupMemberList(String groupId);

    @Query("SELECT group_member.group_id" +
            " FROM group_member " +
            "INNER JOIN user ON group_member.user_id = user.id " +
            "where user_id=:userId")
    List<String> getGroupIdListByUserId(String userId);

    @Query("SELECT group_member.user_id, group_member.group_id, group_member.nickname, group_member.role, group_member.join_time, nickname_spelling,user.name, user.name_spelling, user.portrait_uri, user.alias" +
            " FROM group_member " +
            "INNER JOIN user ON group_member.user_id = user.id " +
            "where group_id=:groupId and " +
            "(group_member.nickname like '%' || :filterByName || '%' " +
            "OR user.name like '%' || :filterByName || '%')" +
            "ORDER BY join_time asc")
    LiveData<List<GroupMember>> getGroupMemberList(String groupId, String filterByName);

    @Query("DELETE FROM group_member where group_id=:groupId and user_id in (:memberIdList)")
    void deleteGroupMember(String groupId, List<String> memberIdList);

    @Query("DELETE FROM group_member where group_id=:groupId")
    void deleteGroupMember(String groupId);
}
