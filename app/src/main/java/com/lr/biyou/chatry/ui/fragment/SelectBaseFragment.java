package com.lr.biyou.chatry.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.Observer;

import com.lr.biyou.chatry.common.IntentExtra;
import com.lr.biyou.chatry.ui.adapter.SelectFriendAdapter;
import com.lr.biyou.chatry.ui.adapter.models.CheckableContactModel;
import com.lr.biyou.chatry.ui.adapter.models.ContactModel;
import com.lr.biyou.chatry.ui.interfaces.OnCheckContactClickListener;
import com.lr.biyou.chatry.utils.log.SLog;
import com.lr.biyou.chatry.viewmodel.SelectBaseViewModel;

import java.util.ArrayList;
import java.util.List;

import com.lr.biyou.R;

public class SelectBaseFragment extends BaseContactFragment implements OnCheckContactClickListener {
    private static final String TAG = "SelectBaseFragment";
    private SelectFriendAdapter adapter;
    private SelectBaseViewModel viewModel;
    protected ArrayList<String> uncheckableInitIdList;//初始化 不能选的列表
    protected ArrayList<String> excludeInitIdList; // 初始化 从列表中排除
    protected ArrayList<String> checkedInitIdList;//初始化 已经选择的列表
    protected ArrayList<String> checkedInitGroupList;//初始化 已经选择群组的列表

    public SelectBaseFragment() {

    }

    @Override
    protected void onInitView(Bundle savedInstanceState, Intent intent) {
        super.onInitView(savedInstanceState, intent);
        uncheckableInitIdList = intent.getStringArrayListExtra(IntentExtra.LIST_CAN_NOT_CHECK_ID_LIST); //不可选的列表
        excludeInitIdList = intent.getStringArrayListExtra(IntentExtra.LIST_EXCLUDE_ID_LIST); // 不在列表中
        adapter = new SelectFriendAdapter(this);
        recyclerView.setAdapter(adapter);
        viewModel = getViewModel();
        if (viewModel == null) return;
        checkedInitIdList = intent.getStringArrayListExtra(IntentExtra.LIST_ALREADY_CHECKED_FRIEND_ID_LIST);    //已经选择的列表
        checkedInitGroupList = intent.getStringArrayListExtra(IntentExtra.LIST_ALREADY_CHECKED_GROUP_ID_LIST); //已经选择的群组
        viewModel.getFriendShipLiveData().observe(this, observable);
        viewModel.getGroupMembersLiveData().observe(this, observable);
        viewModel.getExcludeGroupLiveData().observe(this, observable);
        onLoadData(viewModel);
    }

    private Observer observable = new Observer<List<ContactModel>>() {

        @Override
        public void onChanged(List<ContactModel> models) {
            SLog.i(TAG, "onChanged()");
            adapter.setData(models);
            onDataShowed();
        }
    };

    /**
     * 数据已经展示
     */
    protected void onDataShowed(){

    }

    protected void onLoadData(SelectBaseViewModel viewModel) {
        viewModel.loadFriendShip(uncheckableInitIdList, checkedInitIdList, checkedInitGroupList);
    }

    public String getTitle() {
        return getString(R.string.seal_select_group_member);
    }

    protected SelectBaseViewModel getViewModel() {
        return null;
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        recyclerView.scrollToPosition(viewModel.getIndex(s));
    }

    public ArrayList<String> getCheckedList() {
        return viewModel.getCheckedList();
    }

    public ArrayList<String> getCheckedInitGroupList() {
        return checkedInitGroupList;//直接返回了，现在选人列表不支持选群组，无变化
    }

    @Override
    public void onContactContactClick(CheckableContactModel contactModel) {
        viewModel.onItemClicked(contactModel);
        adapter.notifyDataSetChanged();
    }

    public ArrayList<String> getCheckedFriendList() {
        return viewModel.getCheckedList();
    }

    public ArrayList<String> getCheckedGroupList() {
        return viewModel.getCheckedGroupList();
    }

}
