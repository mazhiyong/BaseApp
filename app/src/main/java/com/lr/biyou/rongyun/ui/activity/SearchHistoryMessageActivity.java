package com.lr.biyou.rongyun.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.lr.biyou.R;
import com.lr.biyou.rongyun.common.IntentExtra;
import com.lr.biyou.rongyun.ui.fragment.SearchMessageFragment;
import com.lr.biyou.rongyun.ui.interfaces.OnMessageRecordClickListener;
import com.lr.biyou.rongyun.viewmodel.SearchMessageModel;
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
