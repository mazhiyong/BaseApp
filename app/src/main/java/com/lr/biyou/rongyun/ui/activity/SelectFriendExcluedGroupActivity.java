package com.lr.biyou.rongyun.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import com.lr.biyou.rongyun.common.IntentExtra;
import com.lr.biyou.rongyun.ui.fragment.SelectFriendsExcluedGroupFragment;
import com.lr.biyou.rongyun.ui.fragment.SelectMultiFriendFragment;

import static com.lr.biyou.rongyun.common.IntentExtra.STR_TARGET_ID;

/**
 * 除了当前群组 groupId 之外的人
 */
public class SelectFriendExcluedGroupActivity extends SelectMultiFriendsActivity {
    private String groupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        groupId = getIntent().getStringExtra(STR_TARGET_ID);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected SelectMultiFriendFragment getSelectMultiFriendFragment() {
        return new SelectFriendsExcluedGroupFragment(groupId);
    }

    @Override
    protected void onConfirmClicked(ArrayList<String> selectIds, ArrayList<String> selectGroups) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(IntentExtra.LIST_STR_ID_LIST, selectIds);
        setResult(RESULT_OK, intent);
        finish();
    }
}
