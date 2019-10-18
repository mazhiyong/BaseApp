package com.lr.biyou.chatry.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.lr.biyou.R;

import static com.lr.biyou.chatry.ui.view.SealTitleBar.Type.NORMAL;

public class PublicServiceInfoActivity extends TitleBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTitleBar().setType(NORMAL);
        getTitleBar().setTitle(R.string.seal_public_account_information);
        setContentView(R.layout.seal_public_account_info);
    }
}
