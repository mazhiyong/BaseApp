package com.lr.biyou.chatry.ui.fragment;

import androidx.lifecycle.ViewModelProviders;

import com.lr.biyou.chatry.common.IntentExtra;
import com.lr.biyou.chatry.viewmodel.CommonListBaseViewModel;
import com.lr.biyou.chatry.viewmodel.ForwardGroupListViewModel;

public class ForwardGroupListFragment extends CommonListBaseFragment {

    @Override
    protected CommonListBaseViewModel createViewModel() {
        boolean isSelect = getArguments().getBoolean(IntentExtra.IS_SELECT, false);
        return ViewModelProviders.of(this, new ForwardGroupListViewModel.Factory(isSelect, getActivity().getApplication())).get(ForwardGroupListViewModel.class);
    }
}
