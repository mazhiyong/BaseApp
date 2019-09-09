package com.lr.biyou.rongyun.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;

import com.lr.biyou.rongyun.db.model.FriendShipInfo;
import com.lr.biyou.rongyun.db.model.GroupEntity;
import com.lr.biyou.rongyun.ui.adapter.models.ListItemModel;

import java.util.ArrayList;
import java.util.List;

import com.lr.biyou.R;

public class ForwardRecentSelectListViewModel extends ForwardRecentListViewModel {

    public ForwardRecentSelectListViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected ListItemModel createFriendModel(FriendShipInfo info) {
        ListItemModel model = super.createFriendModel(info);
        model.setCheckStatus(ListItemModel.CheckStatus.UNCHECKED);
        return model;
    }

    @Override
    protected ListItemModel createGroupModel(GroupEntity entity) {
        ListItemModel model = super.createGroupModel(entity);
        model.setCheckStatus(ListItemModel.CheckStatus.UNCHECKED);
        return model;
    }

    @Override
    protected List<ListItemModel> getBeforeItems() {
        List<ListItemModel> beforeItems = new ArrayList<>();
        beforeItems.add(createFunModel("1", getApplication().getString(R.string.seal_select_forward_more_contact)));
        beforeItems.add(createTextModel(getApplication().getString(R.string.seal_select_forward_message_recent_chat)));
        return beforeItems;
    }
}
