package com.lr.biyou.chatry.ui.activity;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import com.lr.biyou.chatry.common.IntentExtra;
import com.lr.biyou.chatry.ui.fragment.SelectGroupMemberMultiFragment;
import com.lr.biyou.chatry.ui.fragment.SelectMultiFriendFragment;

import static com.lr.biyou.chatry.common.IntentExtra.STR_TARGET_ID;

/**
 * 选择当前群组 groupId 内的人
 */
public class SelectGroupMemberActivity extends SelectMultiFriendsActivity {
    private String groupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        groupId = getIntent().getStringExtra(STR_TARGET_ID);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected SelectMultiFriendFragment getSelectMultiFriendFragment() {
        return new SelectGroupMemberMultiFragment(groupId);
    }

    @Override
    protected void onConfirmClicked(ArrayList<String> selectIds, ArrayList<String> selectGroups) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(IntentExtra.LIST_STR_ID_LIST, selectIds);
        setResult(RESULT_OK, intent);
        finish();
    }
}
