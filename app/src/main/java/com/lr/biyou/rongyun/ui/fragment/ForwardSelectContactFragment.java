package com.lr.biyou.rongyun.ui.fragment;

import androidx.lifecycle.ViewModelProviders;

import com.lr.biyou.rongyun.viewmodel.CommonListBaseViewModel;
import com.lr.biyou.rongyun.viewmodel.ForwardSelectContactViewModel;

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
