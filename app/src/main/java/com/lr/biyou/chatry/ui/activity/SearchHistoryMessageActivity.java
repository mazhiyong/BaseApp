package com.lr.biyou.chatry.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.lr.biyou.R;
import com.lr.biyou.chatry.common.IntentExtra;
import com.lr.biyou.chatry.ui.fragment.SearchMessageFragment;
import com.lr.biyou.chatry.ui.interfaces.OnMessageRecordClickListener;
import com.lr.biyou.chatry.viewmodel.SearchMessageModel;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;


public class SearchHistoryMessageActivity extends SealSearchBaseActivity implements OnMessageRecordClickListener {

    private SearchMessageFragment messageFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        String targetId = getIntent().getStringExtra(IntentExtra.STR_TARGET_ID);
        String name = getIntent().getStringExtra(IntentExtra.STR_CHAT_NAME);
        String portrait = getIntent().getStringExtra(IntentExtra.STR_CHAT_PORTRAIT);

        Conversation.ConversationType conversationType = (Conversation.ConversationType) getIntent().getSerializableExtra(IntentExtra.SERIA_CONVERSATION_TYPE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        messageFragment = new SearchMessageFragment();
        messageFragment.init(this, targetId, conversationType, name, portrait);
        transaction.replace(R.id.fl_content_fragment, messageFragment);
        transaction.commit();
        findViewById(R.id.left_back_lay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    @Override
    public void onMessageRecordClick(SearchMessageModel searchMessageModel) {
        Message message = searchMessageModel.getBean();
        RongIM.getInstance().startConversation(this,
                message.getConversationType(),
                message.getTargetId(), searchMessageModel.getName(),
                message.getSentTime());
    }


    @Override
    public void search(String search) {
        messageFragment.search(search);
    }
}
