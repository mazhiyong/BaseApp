package cn.wildfire.chat.kit.group.manage;

import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.basic.MbsConstans;

import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfirechat.model.GroupInfo;

public class GroupMemberPermissionActivity extends WfcBaseActivity {

    @Override
    protected void afterViews() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        GroupInfo groupInfo = getIntent().getParcelableExtra("groupInfo");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFrameLayout, GroupMemberPermissionFragment.newInstance(groupInfo))
                .commit();
    }

    @Override
    protected int contentLayout() {
        return R.layout.fragment_container_activity;
    }
}
