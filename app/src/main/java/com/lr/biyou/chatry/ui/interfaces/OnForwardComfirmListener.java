package com.lr.biyou.chatry.ui.interfaces;

import com.lr.biyou.chatry.db.model.FriendShipInfo;
import com.lr.biyou.chatry.db.model.GroupEntity;

import java.util.List;

public interface OnForwardComfirmListener {
    void onForward(List<GroupEntity> groups, List<FriendShipInfo> friendShipInfos);
}
