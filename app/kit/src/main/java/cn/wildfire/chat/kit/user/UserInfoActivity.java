package cn.wildfire.chat.kit.user;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mywidget.dialog.SureOrNoDialog;
import com.lr.biyou.ui.moudle.activity.MainActivity;

import butterknife.BindView;
import cn.wildfire.chat.kit.WfcBaseActivity;
import cn.wildfire.chat.kit.contact.ContactViewModel;
import cn.wildfirechat.model.UserInfo;

public class UserInfoActivity extends WfcBaseActivity {
    private UserInfo userInfo;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected int contentLayout() {
        return R.layout.fragment_container_activity;
    }

    @Override
    protected void afterViews() {
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        tvTitle.setText("用户详情");
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

            SureOrNoDialog sureOrNoDialog = new SureOrNoDialog(UserInfoActivity.this, true);
            sureOrNoDialog.initValue("提示", "是否确定删除好友？");
            sureOrNoDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.cancel:
                            sureOrNoDialog.dismiss();
                            break;
                        case R.id.confirm:
                            sureOrNoDialog.dismiss();
                            ContactViewModel contactViewModel = ViewModelProviders.of(UserInfoActivity.this).get(ContactViewModel.class);
                            contactViewModel.deleteFriend(userInfo.uid).observe(
                                    UserInfoActivity.this, booleanOperateResult -> {
                                        if (booleanOperateResult.isSuccess()) {
                                            Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(UserInfoActivity.this, "删除好友失败 " + booleanOperateResult.getErrorCode(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );

                            break;
                    }
                }
            });
            sureOrNoDialog.show();
            sureOrNoDialog.setCanceledOnTouchOutside(false);
            sureOrNoDialog.setCancelable(true);



            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
