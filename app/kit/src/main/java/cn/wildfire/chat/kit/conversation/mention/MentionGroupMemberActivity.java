package cn.wildfire.chat.kit.conversation.mention;

import android.view.Menu;
import android.widget.TextView;

import com.lr.biyou.R;

import java.util.List;

import butterknife.BindView;
import cn.wildfire.chat.kit.search.SearchActivity;
import cn.wildfire.chat.kit.search.SearchableModule;
import cn.wildfirechat.model.GroupInfo;

public class MentionGroupMemberActivity extends SearchActivity {
    private GroupInfo groupInfo;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @Override
    protected void beforeViews() {
        super.beforeViews();
        groupInfo = getIntent().getParcelableExtra("groupInfo");
    }

    @Override
    protected void afterViews() {
        super.afterViews();
        tvTitle.setText("选择群成员");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mentionGroupMemberContainer, MentionGroupMemberFragment.newInstance(groupInfo))
                .commit();
    }

    @Override
    protected void afterMenus(Menu menu) {
        super.afterMenus(menu);
        searchView.setIconified(true);
    }

    @Override
    protected void initSearchModule(List<SearchableModule> modules) {
        modules.add(new GroupMemberSearchModule(groupInfo.target));
    }

    @Override
    protected int contentLayout() {
        return R.layout.group_mention_activity;
    }
}
