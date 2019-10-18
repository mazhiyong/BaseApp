package com.lr.biyou.chatry.ui.fragment;

import androidx.lifecycle.ViewModelProviders;

import com.lr.biyou.chatry.viewmodel.CommonListBaseViewModel;
import com.lr.biyou.chatry.viewmodel.ForwardSelectContactViewModel;

public class ForwardSelectContactFragment extends CommonListBaseFragment {
    @Override
    protected CommonListBaseViewModel createViewModel() {
        return ViewModelProviders.of(this).get(ForwardSelectContactViewModel.class);
    }

    @Override
    protected boolean isUseSideBar() {
        return true;
    }
}
