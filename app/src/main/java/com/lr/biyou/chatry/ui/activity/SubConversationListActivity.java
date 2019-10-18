package com.lr.biyou.chatry.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.chatry.ui.adapter.SubConversationListAdapterEx;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongContext;
import io.rong.imkit.fragment.SubConversationListFragment;

/**
 *  聚合会话列表
 */
public class SubConversationListActivity extends BasicActivity {

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.divide_line)
    View divideLine;

    @Override
    public int getContentView() {
        return R.layout.conversation_activity_subconversation_list;
    }

    @Override
    public void init() {

        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        SubConversationListFragment fragment = new SubConversationListFragment();
        fragment.setAdapter(new SubConversationListAdapterEx(RongContext.getInstance()));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();

        Intent intent = getIntent();
        if (intent.getData() == null) {
            return;
        }
        //聚合会话参数
        String type = intent.getData().getQueryParameter("type");


        if (type == null)
            return;

        mTitleText.setCompoundDrawables(null, null, null, null);
        divideLine.setVisibility(View.GONE);
        mBackText.setText("");

        if (type.equals("group")) {
            mTitleText.setText(R.string.seal_conversation_sub_group);
        } else if (type.equals("private")) {
            mTitleText.setText(R.string.seal_conversation_sub_private);
        } else if (type.equals("discussion")) {
            mTitleText.setText(R.string.seal_conversation_sub_discussion);
        } else if (type.equals("system")) {
            mTitleText.setText("系统消息");

        } else {
            mTitleText.setText(R.string.seal_conversation_sub_defult);
        }
    }

    @OnClick({R.id.left_back_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back_lay:
                finish();
                break;
        }
    };
    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

    }
}
