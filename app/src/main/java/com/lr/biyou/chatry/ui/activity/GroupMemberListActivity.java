package com.lr.biyou.chatry.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.lr.biyou.R;
import com.lr.biyou.chatry.common.IntentExtra;
import com.lr.biyou.chatry.db.model.GroupEntity;
import com.lr.biyou.chatry.model.GroupMember;
import com.lr.biyou.chatry.model.Status;
import com.lr.biyou.chatry.ui.adapter.ListGroupMemberAdapter;
import com.lr.biyou.chatry.ui.view.SealTitleBar;
import com.lr.biyou.chatry.utils.ToastUtils;
import com.lr.biyou.chatry.utils.log.SLog;
import com.lr.biyou.chatry.viewmodel.GroupMemberListViewModel;

/**
 * 群组成员一览界面
 */
public class GroupMemberListActivity extends TitleBaseActivity {
    private final static String TAG = "GroupMemberListActivity";
    private SealTitleBar sealTitleBar;
    private EditText groupMemberSearchEt;
    private ListView groupMemberList;
    private ListGroupMemberAdapter listGroupMemberAdapter;
    private GroupMemberListViewModel groupMemberListViewModel;

    private String groupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sealTitleBar = getTitleBar();
        sealTitleBar.setTitle(R.string.profile_group_total_member);

        setContentView(R.layout.profile_activity_group_all_member);

        Intent intent = getIntent();
        if(intent == null) {
            SLog.e(TAG, "intent can not null ,to finish " + TAG);
            finish();
            return;
        }

        groupId = intent.getStringExtra(IntentExtra.STR_TARGET_ID);
        if(groupId == null) {
            SLog.e(TAG, "groupId can not null, to finish " + TAG);
            finish();
            return;
        }

        initView();
        initViewModel();
    }

    private void initView(){
        groupMemberSearchEt = findViewById(R.id.profile_et_group_member_search);
        groupMemberList = findViewById(R.id.profile_lv_group_member_list);
        listGroupMemberAdapter = new ListGroupMemberAdapter(this);
        groupMemberList.setAdapter(listGroupMemberAdapter);
        groupMemberList.setOnItemClickListener((parent, view, position, id) -> {
            GroupMember groupMember = listGroupMemberAdapter.getItem(position);
            showGroupMemberInfo(groupMember);
        });

        groupMemberSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                groupMemberListViewModel.requestGroupMember(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initViewModel(){
        groupMemberListViewModel = ViewModelProviders.of(this
                , new GroupMemberListViewModel.Factory(this.getApplication(), groupId)).get(GroupMemberListViewModel.class);

        // 获取并显示群组列表
        groupMemberListViewModel.getGroupMemberList().observe(this, resource -> {
            listGroupMemberAdapter.updateListView(resource.data);
        });

        // 获取群组信息
        groupMemberListViewModel.getGroupInfo().observe(this, resource -> {
            if (resource.data != null) {
                updateGroupInfoView(resource.data);
            }

            if (resource.status == Status.ERROR) {
                ToastUtils.showErrorToast(resource.code);
            }
        });
    }

    /**
     * 更新群信息
     *
     * @param groupInfo
     */
    private void updateGroupInfoView(GroupEntity groupInfo) {
        // 标题
        String title = getString(R.string.profile_group_total_member) + "(" + groupInfo.getMemberCount() + ")";
        sealTitleBar.setTitle(title);
    }

    /**
     * 显示群成员信息
     *
     * @param groupMember
     */
    private void showGroupMemberInfo(GroupMember groupMember){
      /*  Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra(IntentExtra.STR_TARGET_ID, groupMember.getUserId());
        Group groupInfo = RongUserInfoManager.getInstance().getGroupInfo(groupId);
        if (groupInfo != null) {
            intent.putExtra(IntentExtra.STR_GROUP_NAME, groupInfo.getName());
        }
        startActivity(intent);*/
    }
}
