package com.lr.biyou.chatry.ui.interfaces;

import com.lr.biyou.chatry.db.model.FriendShipInfo;

public interface OnContactItemClickListener {
    /**
     * 联系人列表人员点击监听
     *
     * @param friendShipInfo
     */
    void onItemContactClick(FriendShipInfo friendShipInfo);
}
