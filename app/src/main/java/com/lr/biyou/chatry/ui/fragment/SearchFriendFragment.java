package com.lr.biyou.chatry.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.lr.biyou.chatry.ui.adapter.models.SearchModel;
import com.lr.biyou.chatry.ui.interfaces.OnContactItemClickListener;

import java.util.List;

public class SearchFriendFragment extends SearchBaseFragment {

    public void init(OnContactItemClickListener onContactItemClickListener){
        init(null, null, null, onContactItemClickListener, null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        viewModel.getSearchFriend().observe(this, new Observer<List<SearchModel>>() {
            @Override
            public void onChanged(List<SearchModel> searchModels) {
                updateData(searchModels);
            }
        });
        if (!TextUtils.isEmpty(initSearch)) {
            search(initSearch);
        }
        return view;
    }

    @Override
    public void search(String search) {
        super.search(search);
        if (viewModel != null) {
            viewModel.searchFriend(search);
        }
    }
}
