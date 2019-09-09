package com.lr.biyou.rongyun.ui.fragment;

import com.lr.biyou.rongyun.viewmodel.SelectBaseViewModel;

public class SelectFriendsExcluedGroupFragment extends SelectMultiFriendFragment {
    private String groupId;

    public SelectFriendsExcluedGroupFragment(String groupId) {
        this.groupId = groupId;
    }

    @Override
    protected void onLoadData(SelectBaseViewModel viewModel) {
        viewModel.loadFriendShipExclude(groupId, uncheckableInitIdList);
    }
}
