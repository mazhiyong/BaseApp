package com.lr.biyou.chatry.ui.fragment;

import androidx.lifecycle.ViewModelProviders;

import com.lr.biyou.chatry.viewmodel.SelectBaseViewModel;
import com.lr.biyou.chatry.viewmodel.SelectSingleViewModel;

public class SelectSingleFragment extends SelectBaseFragment {

    private static final String TAG = "SelectSingleFragment";
    @Override
    protected SelectBaseViewModel getViewModel() {
        return ViewModelProviders.of(this).get(SelectSingleViewModel.class);
    }
}
