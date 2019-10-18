package com.lr.biyou.chatry.ui.interfaces;

import com.lr.biyou.chatry.db.model.FriendShipInfo;
import com.lr.biyou.chatry.ui.adapter.models.CheckableContactModel;

public interface OnCheckContactClickListener {
    void onContactContactClick(CheckableContactModel<FriendShipInfo> contactModel);
}
