package cn.wildfire.chat.kit.channel;

import android.content.Intent;
import android.view.MenuItem;

import androidx.core.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.basic.MbsConstans;

import cn.wildfire.chat.kit.WfcBaseActivity;


public class ChannelListActivity extends WfcBaseActivity {

    @Override
    protected int menu() {
        return R.menu.channel_list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.create) {
            createChannel();
            return true;
        } else if (item.getItemId() == R.id.subscribe) {
            subscribe();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int contentLayout() {
        return R.layout.fragment_container_activity;
    }

    @Override
    protected void afterViews() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerFrameLayout, new ChannelListFragment())
                .commit();
    }

    void createChannel() {
        Intent intent = new Intent(this, CreateChannelActivity.class);
        startActivity(intent);
        finish();
    }

    void subscribe() {
        Intent intent = new Intent(this, SearchChannelActivity.class);
        startActivity(intent);
    }
}
