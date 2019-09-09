package com.lr.biyou.rongyun.ui.interfaces;

import com.lr.biyou.rongyun.db.model.FriendShipInfo;
import com.lr.biyou.rongyun.ui.adapter.models.CheckableContactModel;

public interface OnCheckContactClickListener {
    void onContactContactClick(CheckableContactModel<FriendShipInfo> contactModel);
}
