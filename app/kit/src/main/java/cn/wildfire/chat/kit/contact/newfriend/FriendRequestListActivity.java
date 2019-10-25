package cn.wildfire.chat.kit.contact.newfriend;

import android.content.Intent;
import android.view.MenuItem;

import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.basic.MbsConstans;

import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.third.utils.UIUtils;


public class FriendRequestListActivity extends WfcBaseActivity {

    @Override
    protected void afterViews() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        setTitle(UIUtils.getString(R.string.new_friend));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFrameLayout, new FriendRequestListFragment())
                .commit();
    }

    @Override
    protected int contentLayout() {
        return R.layout.fragment_container_activity;
    }

    @Override
    protected int menu() {
        return R.menu.contact_friend_request;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            addContact();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void addContact() {
        Intent intent = new Intent(this, SearchUserActivity.class);
        startActivity(intent);
    }
}
