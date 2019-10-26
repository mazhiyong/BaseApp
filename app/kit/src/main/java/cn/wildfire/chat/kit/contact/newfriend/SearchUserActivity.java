package cn.wildfire.chat.kit.contact.newfriend;

import android.widget.TextView;

import com.lr.biyou.R;

import java.util.List;

import butterknife.BindView;
import cn.wildfire.chat.kit.search.SearchActivity;
import cn.wildfire.chat.kit.search.SearchableModule;

public class SearchUserActivity extends SearchActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @Override
    protected void beforeViews() {
    }

    @Override
    protected void afterViews() {
        super.afterViews();
        tvTitle.setText("搜索用户");
    }

    @Override
    protected void initSearchModule(List<SearchableModule> modules) {
        modules.add(new UserSearchModule());
    }
}
