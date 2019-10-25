package cn.wildfire.chat.kit.user;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.ui.moudle.activity.MainActivity;

import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.contact.ContactViewModel;
import cn.wildfirechat.model.UserInfo;

public class UserInfoActivity extends WfcBaseActivity {
    private UserInfo userInfo;

    @Override
    protected int contentLayout() {
        return R.layout.fragment_container_activity;
    }

    @Override
    protected void afterViews() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        userInfo = getIntent().getParcelableExtra("userInfo");
        if (userInfo == null) {
            finish();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerFrameLayout, UserInfoFragment.newInstance(userInfo))
                    .commit();
        }
    }

    @Override
    protected int menu() {
        return R.menu.user_info;
    }

    @Override
    protected void afterMenus(Menu menu) {
        super.afterMenus(menu);
        ContactViewModel contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        if (!contactViewModel.isFriend(userInfo.uid)) {
            MenuItem item = menu.findItem(R.id.delete);
            item.setEnabled(false);
            item.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            ContactViewModel contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
            contactViewModel.deleteFriend(userInfo.uid).observe(
                    this, booleanOperateResult -> {
                        if (booleanOperateResult.isSuccess()) {
                            Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "delete friend error " + booleanOperateResult.getErrorCode(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
