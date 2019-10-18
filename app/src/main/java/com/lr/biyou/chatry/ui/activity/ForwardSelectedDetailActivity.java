package com.lr.biyou.chatry.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import com.lr.biyou.R;
import com.lr.biyou.chatry.common.ErrorCode;
import com.lr.biyou.chatry.common.IntentExtra;
import com.lr.biyou.chatry.db.model.FriendShipInfo;
import com.lr.biyou.chatry.db.model.GroupEntity;
import com.lr.biyou.chatry.model.Resource;
import com.lr.biyou.chatry.model.Status;
import com.lr.biyou.chatry.ui.dialog.CommonDialog;
import com.lr.biyou.chatry.ui.dialog.ForwardDialog;
import com.lr.biyou.chatry.ui.fragment.ForwardSelectedDetailFragment;
import com.lr.biyou.chatry.viewmodel.ForwardActivityViewModel;
import io.rong.imlib.model.Message;

public class ForwardSelectedDetailActivity extends TitleBaseActivity {

    private ArrayList<GroupEntity> seletedGroup;
    private ArrayList<FriendShipInfo>  selectedFriends;
    private ForwardActivityViewModel forwardActivityViewModel;
    private List<Message> messageList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forward_activity_selected_detail);
        messageList = getIntent().getParcelableArrayListExtra(IntentExtra.FORWARD_MESSAGE_LIST);
        seletedGroup = getIntent().getParcelableArrayListExtra(IntentExtra.GROUP_LIST);
        selectedFriends = getIntent().getParcelableArrayListExtra(IntentExtra.FRIEND_LIST);
        initView();
        initViewModel();
    }

    /**
     * 初始化viewmodel
     */
    private void initViewModel() {
        forwardActivityViewModel = ViewModelProviders.of(this).get(ForwardActivityViewModel.class);
        forwardActivityViewModel.getForwardSuccessLiveData().observe(this, new Observer<Resource>() {
            @Override
            public void onChanged(Resource resource) {
                if (resource.status == Status.SUCCESS) {
                    showToast(R.string.seal_forward__message_success);
                } else {
                    if (resource.code == ErrorCode.NETWORK_ERROR.getCode()) {
                        showToast(resource.message);
                    } else {
                        showToast(R.string.seal_select_forward_message_defeat);
                    }
                }
                Intent intent = new Intent();
                intent.putExtra(IntentExtra.FORWARD_FINISH, true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * 转发消息
     */
    private void forwardMessage(List<GroupEntity> groups, List<FriendShipInfo> friends, List<Message> messages) {
        if (forwardActivityViewModel != null) {
            forwardActivityViewModel.ForwardMessage(groups, friends, messages);
        }
    }

    /**
     * 初始化 View
     */
    private void initView() {
        getTitleBar().setTitle(R.string.seal_forward_selected_detail_title);
        getTitleBar().setOnBtnRightClickListener(getString(R.string.seal_forward_selected_detail_comfirm), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((seletedGroup == null || seletedGroup.size() <=0) && (selectedFriends == null || selectedFriends.size() <= 0) ) {
                    return;
                }
                showForwardDialog(seletedGroup, selectedFriends, messageList);
            }
        });

        getTitleBar().setOnBtnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(IntentExtra.GROUP_LIST, seletedGroup);
                intent.putParcelableArrayListExtra(IntentExtra.FRIEND_LIST, selectedFriends);
                intent.putExtra(IntentExtra.FORWARD_FINISH, false);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        updateTitle(selectedFriends, seletedGroup);

        final ForwardSelectedDetailFragment forwardSelectedDetailFragment = new ForwardSelectedDetailFragment();

        forwardSelectedDetailFragment.setOnLeftSelectedListener(new ForwardSelectedDetailFragment.OnLeftSelectedListener() {
            @Override
            public void onLeftSelected(ArrayList<FriendShipInfo> friendShipInfos, ArrayList<GroupEntity> groupEntities) {
                selectedFriends = friendShipInfos;
                seletedGroup = groupEntities;
                updateTitle(selectedFriends, seletedGroup);
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.ll_selected_detail_container, forwardSelectedDetailFragment)
                .show(forwardSelectedDetailFragment).commit();
    }

    /**
     * 初始化数据
     */
    private void updateTitle(List<FriendShipInfo> selectedFriends, List<GroupEntity> seletedGroup) {
        if ((seletedGroup == null || seletedGroup.size() <= 0) && (selectedFriends == null || selectedFriends.size() <= 0)) {
            getTitleBar().getTvRight().setTextColor(getResources().getColor(R.color.text_gray));
            getTitleBar().getTvRight().setClickable(false);
        }
    }

    /**
     * 转发给多人的 dialog
     * @param groups
     * @param friendShipInfos
     */
    private void showForwardDialog(List<GroupEntity> groups, List<FriendShipInfo> friendShipInfos, List<Message> messageList) {
        ForwardDialog.Builder builder = new ForwardDialog.Builder();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(IntentExtra.GROUP_LIST, groups == null? null : (ArrayList<GroupEntity>) groups);
        bundle.putParcelableArrayList(IntentExtra.FRIEND_LIST, friendShipInfos == null? null : (ArrayList<FriendShipInfo>) friendShipInfos);
        bundle.putParcelableArrayList(IntentExtra.FORWARD_MESSAGE_LIST, (ArrayList<Message>)messageList);
        builder.setExpandParams(bundle);
        builder.setDialogButtonClickListener(new CommonDialog.OnDialogButtonClickListener() {
            @Override
            public void onPositiveClick(View v, Bundle bundle) {
                final ArrayList<GroupEntity> groupEntities = bundle.getParcelableArrayList(IntentExtra.GROUP_LIST);
                final ArrayList<FriendShipInfo> friendShipInfos = bundle.getParcelableArrayList(IntentExtra.FRIEND_LIST);
                final ArrayList<Message> messages = bundle.getParcelableArrayList(IntentExtra.FORWARD_MESSAGE_LIST);
                forwardMessage(groupEntities, friendShipInfos, messages);
            }

            @Override
            public void onNegativeClick(View v, Bundle bundle) {
            }
        });
        final CommonDialog dialog = builder.build();
        dialog.show(getSupportFragmentManager(), "forward_dialog");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(IntentExtra.GROUP_LIST, seletedGroup);
            intent.putParcelableArrayListExtra(IntentExtra.FRIEND_LIST, selectedFriends);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
