package com.lr.biyou.rongyun.ui.fragment;

import androidx.lifecycle.ViewModelProviders;

import com.lr.biyou.rongyun.viewmodel.ForwardRecentListViewModel;
import com.lr.biyou.rongyun.viewmodel.ForwardRecentSelectListViewModel;

/**
 *  转发最近联系人列表
 */
public class ForwordRecentMultiSelectListFragment extends ForwordRecentListFragment {

    @Override
    protected ForwardRecentListViewModel createViewModel() {
        return ViewModelProviders.of(this).get(ForwardRecentSelectListViewModel.class);
    }
}
