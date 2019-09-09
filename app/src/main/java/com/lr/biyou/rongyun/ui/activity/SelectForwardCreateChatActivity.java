package com.lr.biyou.rongyun.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lr.biyou.R;

/**
 * 转发单选创建群组
 */
public class SelectForwardCreateChatActivity extends SelectCreateGroupActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTitleBar().setTitle(getString(R.string.seal_select_group_member));
    }
}
