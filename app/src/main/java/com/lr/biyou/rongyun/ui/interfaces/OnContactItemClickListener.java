package com.lr.biyou.rongyun.ui.interfaces;

import com.lr.biyou.rongyun.db.model.FriendShipInfo;

public interface OnContactItemClickListener {
    /**
     * 联系人列表人员点击监听
     *
     * @param friendShipInfo
     */
    void onItemContactClick(FriendShipInfo friendShipInfo);
}
