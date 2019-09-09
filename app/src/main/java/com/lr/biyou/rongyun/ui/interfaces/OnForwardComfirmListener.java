package com.lr.biyou.rongyun.ui.interfaces;

import com.lr.biyou.rongyun.db.model.FriendShipInfo;
import com.lr.biyou.rongyun.db.model.GroupEntity;

import java.util.List;

public interface OnForwardComfirmListener {
    void onForward(List<GroupEntity> groups, List<FriendShipInfo> friendShipInfos);
}
