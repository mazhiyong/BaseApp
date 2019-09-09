package com.lr.biyou.rongyun.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;

import com.lr.biyou.rongyun.ui.adapter.models.CheckableContactModel;
import com.lr.biyou.rongyun.ui.interfaces.OnSelectCountChangeListener;
import com.lr.biyou.rongyun.viewmodel.SelectBaseViewModel;
import com.lr.biyou.rongyun.viewmodel.SelectMultiViewModel;

public class SelectMultiFriendFragment extends SelectBaseFragment {
    private static final String TAG = "SelectMultiFriendFragment";
    private OnSelectCountChangeListener onSelectCountChangeListener;

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        super.onInitView(savedInstanceState, intent);
    }

    @Override
    protected SelectBaseViewModel getViewModel() {
        return ViewModelProviders.of(getActivity()).get(SelectMultiViewModel.class);
    }

    @Override
    public void onContactContactClick(CheckableContactModel contactModel) {
        super.onContactContactClick(contactModel);
        changeCheckCount();
    }

    public void setOnSelectCountChangeListener(OnSelectCountChangeListener listener) {
        onSelectCountChangeListener = listener;
    }

    @Override
    protected void onDataShowed() {
        changeCheckCount();
    }

    private void changeCheckCount() {
        if (onSelectCountChangeListener != null) {
            int groupCount = 0;
            if (getCheckedGroupList() != null) {
                groupCount = getCheckedGroupList().size();
            }
            onSelectCountChangeListener.onSelectCountChange(groupCount, getCheckedList().size());
        }
    }
}
