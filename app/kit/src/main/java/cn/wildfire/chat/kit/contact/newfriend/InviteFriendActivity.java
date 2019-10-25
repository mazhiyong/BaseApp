package cn.wildfire.chat.kit.contact.newfriend;

import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.basic.MbsConstans;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.contact.ContactViewModel;
import cn.wildfire.chat.kit.user.UserViewModel;
import cn.wildfirechat.model.UserInfo;

public class InviteFriendActivity extends WfcBaseActivity {
    @BindView(R.id.introTextView)
    TextView introTextView;

    private UserInfo userInfo;

    @Override
    protected void afterViews() {
        super.afterViews();
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        userInfo = getIntent().getParcelableExtra("userInfo");
        if (userInfo == null) {
            finish();
        }
        UserViewModel userViewModel =ViewModelProviders.of(this).get(UserViewModel.class);
        UserInfo me = userViewModel.getUserInfo(userViewModel.getUserId(), false);
        introTextView.setText("我是 " + (me == null ? "" : me.displayName));
    }

    @Override
    protected int contentLayout() {
        return R.layout.contact_invite_activity;
    }

    @Override
    protected int menu() {
        return R.menu.contact_invite;
    }

    @OnClick(R.id.clearImageButton)
    void clear() {
        introTextView.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.confirm) {
            invite();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void invite() {
        ContactViewModel contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
        contactViewModel.invite(userInfo.uid, introTextView.getText().toString())
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        if (aBoolean) {
                            Toast.makeText(InviteFriendActivity.this, "好友邀请已发送", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(InviteFriendActivity.this, "添加好友失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
