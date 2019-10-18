package com.lr.biyou.chatry.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import com.lr.biyou.R;
import com.lr.biyou.chatry.db.model.GroupEntity;
import com.lr.biyou.chatry.ui.adapter.models.SearchModel;
import com.lr.biyou.chatry.ui.fragment.SearchGroupByNameFragment;
import com.lr.biyou.chatry.ui.interfaces.OnGroupItemClickListener;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import static com.lr.biyou.chatry.ui.view.SealTitleBar.Type.NORMAL;

public class GroupListActivity extends TitleBaseActivity implements OnGroupItemClickListener, TextWatcher {
    private static final String TAG = "GroupListActivity";
    private EditText editText;
    private SearchGroupByNameFragment searchGroupByNameFragment;
    private FrameLayout groupListContainerFl;
    private TextView emptyTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTitleBar().setType(NORMAL);
        getTitleBar().setTitle(R.string.seal_ac_search_group);
        setContentView(R.layout.activity_group_list);
        editText = findViewById(R.id.group_search);
        groupListContainerFl = findViewById(R.id.fl_content_fragment);
        emptyTv = findViewById(R.id.tv_empty_group_notice);
        editText.addTextChangedListener(this);
        searchGroupByNameFragment = new SearchGroupByNameFragment();
        searchGroupByNameFragment.setOnSearchResultListener(new SearchGroupByNameFragment.SearchResultListener() {
            @Override
            public void onSearchResult(String lastKeyWord, List<SearchModel> searchModels) {
                if(TextUtils.isEmpty(lastKeyWord) && (searchModels == null || searchModels.size() == 0)){
                    emptyTv.setVisibility(View.VISIBLE);
                    groupListContainerFl.setVisibility(View.GONE);
                } else {
                    emptyTv.setVisibility(View.GONE);
                    groupListContainerFl.setVisibility(View.VISIBLE);
                }
            }
        });
        searchGroupByNameFragment.init(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_content_fragment, searchGroupByNameFragment);
        transaction.commit();
    }

    @Override
    public void onGroupClicked(GroupEntity groupEntity) {
        RongIM.getInstance().startConversation(this, Conversation.ConversationType.GROUP, groupEntity.getId(), groupEntity.getName());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                searchGroupByNameFragment.search(s.toString());
            }
        }, 300);
    }
}
