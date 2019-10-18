package com.lr.biyou.chatry.ui.fragment;

import androidx.lifecycle.ViewModelProviders;

import com.lr.biyou.chatry.viewmodel.ForwardRecentListViewModel;
import com.lr.biyou.chatry.viewmodel.ForwardRecentSelectListViewModel;

/**
 *  转发最近联系人列表
 */
public class ForwordRecentMultiSelectListFragment extends ForwordRecentListFragment {

    @Override
    protected ForwardRecentListViewModel createViewModel() {
        return ViewModelProviders.of(this).get(ForwardRecentSelectListViewModel.class);
    }
}
