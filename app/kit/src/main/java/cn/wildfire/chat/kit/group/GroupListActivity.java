package cn.wildfire.chat.kit.group;

import android.content.Intent;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.basic.MbsConstans;

import java.util.ArrayList;

import butterknife.BindView;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfirechat.model.GroupInfo;

public class GroupListActivity extends WfcBaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    private boolean forResult;

    /**
     * intent里面置为{@code true}时，返回groupInfo，不直接打开群会话界面
     */
    public static final String INTENT_FOR_RESULT = "forResult";

    /**
     * for result时，单选，还是多选？
     */
    // TODO
    public static final String MODE_SINGLE = "single";
    public static final String MODE_MULTI = "multi";

    // TODO activity or fragment?
    public static Intent buildIntent(boolean pickForResult, boolean isMultiMode) {

        return null;
    }

    @Override
    protected int contentLayout() {
        return R.layout.fragment_container_activity;
    }

    @Override
    protected void afterViews() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        tvTitle.setText("群组列表");
        forResult = getIntent().getBooleanExtra(INTENT_FOR_RESULT, false);
        GroupListFragment fragment = new GroupListFragment();
        if (forResult) {
            fragment.setOnGroupItemClickListener(groupInfo -> {
                Intent intent = new Intent();
                // TODO 多选
                ArrayList<GroupInfo> groupInfos = new ArrayList<>();
                groupInfos.add(groupInfo);
                intent.putParcelableArrayListExtra("groupInfos", groupInfos);
                setResult(RESULT_OK, intent);
                finish();
            });
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFrameLayout, fragment)
                .commit();
    }
}
