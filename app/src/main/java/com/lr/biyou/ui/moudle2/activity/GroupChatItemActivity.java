package com.lr.biyou.ui.moudle2.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.bean.MessageEvent;
import com.lr.biyou.mywidget.dialog.SureOrNoDialog;
import com.lr.biyou.rongyun.common.Constant;
import com.lr.biyou.rongyun.common.IntentExtra;
import com.lr.biyou.rongyun.db.model.GroupEntity;
import com.lr.biyou.rongyun.model.GroupMember;
import com.lr.biyou.rongyun.model.Resource;
import com.lr.biyou.rongyun.model.ScreenCaptureResult;
import com.lr.biyou.rongyun.model.Status;
import com.lr.biyou.rongyun.ui.activity.GroupMemberListActivity;
import com.lr.biyou.rongyun.ui.activity.GroupNoticeActivity;
import com.lr.biyou.rongyun.ui.activity.SearchHistoryMessageActivity;
import com.lr.biyou.rongyun.ui.adapter.GridGroupMemberAdapter;
import com.lr.biyou.rongyun.ui.dialog.CommonDialog;
import com.lr.biyou.rongyun.ui.dialog.GroupNoticeDialog;
import com.lr.biyou.rongyun.ui.dialog.LoadingDialog;
import com.lr.biyou.rongyun.ui.dialog.SelectCleanTimeDialog;
import com.lr.biyou.rongyun.ui.dialog.SelectPictureBottomDialog;
import com.lr.biyou.rongyun.ui.dialog.SimpleInputDialog;
import com.lr.biyou.rongyun.ui.view.SealTitleBar;
import com.lr.biyou.rongyun.ui.view.SettingItemView;
import com.lr.biyou.rongyun.ui.view.UserInfoItemView;
import com.lr.biyou.rongyun.ui.widget.WrapHeightGridView;
import com.lr.biyou.rongyun.ui.widget.switchbutton.SwitchButton;
import com.lr.biyou.rongyun.utils.CheckPermissionUtils;
import com.lr.biyou.rongyun.utils.ImageLoaderUtils;
import com.lr.biyou.rongyun.utils.ToastUtils;
import com.lr.biyou.rongyun.utils.log.SLog;
import com.lr.biyou.rongyun.viewmodel.GroupDetailViewModel;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imlib.model.Conversation;

/**
 * 群组详细界面
 */
public class GroupChatItemActivity extends BasicActivity implements View.OnClickListener {

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.divide_line)
    View divideLine;
    @BindView(R.id.distru_switch)
    SwitchButton disSwitch;
    @BindView(R.id.top_switch)
    SwitchButton topSwitch;
    @BindView(R.id.manager_lay)
    LinearLayout managerLay;

    private final String TAG = "GroupDetailActivity";
    /**
     * 跳转界面请求添加群组成员
     */
    private final int REQUEST_ADD_GROUP_MEMBER = 1000;
    /**
     * 跳转界面请求移除群组成员
     */
    private final int REQUEST_REMOVE_GROUP_MEMBER = 1001;

    /**
     * 最大显示成员数
     */
    private final int SHOW_GROUP_MEMBER_LIMIT = 30;

    private SealTitleBar titleBar;
    private WrapHeightGridView groupMemberGv;

    private Button quitGroupBtn;
    private LoadingDialog loadingDialog;

    private String groupId;
    private Conversation.ConversationType conversationType;
    private GroupDetailViewModel groupDetailViewModel;
    private GridGroupMemberAdapter memberAdapter;
    private String groupName;
    private String grouportraitUrl;
    private UserInfoItemView groupPortraitUiv;
    private SettingItemView allGroupMemberSiv;
    private SettingItemView groupNameSiv;
    private SettingItemView notifyNoticeSiv;
    private SettingItemView onTopSiv;
    private SettingItemView isToContactSiv;
    private SettingItemView groupManagerSiv;
    private SettingItemView groupNoticeSiv;
    private SettingItemView cleanTimingSiv;
    private SettingItemView screenShotSiv;
    private TextView screenShotTip;

    private boolean isScreenShotSivClicked;
    private String lastGroupNoticeContent;
    private long lastGroupNoticeTime;
    private String groupCreatorId;

    private final int REQUEST_CODE_PERMISSION = 115;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

    private String qrcodeImgUrl = "";
    private String identiType = ""; //身份类型  ：0普通用户，1管理员，2群主
    private String name = "";
    private List<Map<String,Object>> memberList;


    @Override
    public int getContentView() {
        return R.layout.profile_activity_group_item;
    }

    @Override
    public void init() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText("聊天管理");
        mTitleText.setCompoundDrawables(null, null, null, null);
        divideLine.setVisibility(View.GONE);

        conversationType = (Conversation.ConversationType) intent.getSerializableExtra(IntentExtra.SERIA_CONVERSATION_TYPE);
        groupId = intent.getStringExtra(IntentExtra.STR_TARGET_ID);
        if (groupId == null || conversationType == null) {
            SLog.e(TAG, "targetId or conversationType is null, finish" + TAG);
            return;
        }

        initView();
        //initViewModel();


    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取群组信息
        getGroupInfoAction();
    }

    private void getGroupInfoAction() {

        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(GroupChatItemActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("group_id",groupId);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_GROUPS_INFO, map);

    }

    // 初始化布局
    private void initView() {
        // 群组成员网格
        groupMemberGv = findViewById(R.id.profile_gv_group_member);
        memberAdapter = new GridGroupMemberAdapter(this, SHOW_GROUP_MEMBER_LIMIT);
        memberAdapter.setAllowAddMember(true);
        groupMemberGv.setAdapter(memberAdapter);
        memberAdapter.setOnItemClickedListener(new GridGroupMemberAdapter.OnItemClickedListener() {
            @Override
            public void onAddOrDeleteMemberClicked(boolean isAdd) {
                toMemberManage(isAdd); //添加或删除
            }

            @Override
            public void onMemberClicked(GroupMember groupMember) {
                showMemberInfo(groupMember);
            }
        });

        // 全部群成员
        allGroupMemberSiv = findViewById(R.id.profile_siv_all_group_member);
        allGroupMemberSiv.setOnClickListener(this);

        // 查询历史消息
        findViewById(R.id.profile_siv_group_search_history_message).setOnClickListener(this);
        // 群头像
        groupPortraitUiv = findViewById(R.id.profile_uiv_group_portrait_container);
        groupPortraitUiv.setOnClickListener(this);
        // 群名称
        groupNameSiv = findViewById(R.id.profile_siv_group_name_container);
        groupNameSiv.setOnClickListener(this);
        // 群二维码
        findViewById(R.id.profile_siv_group_qrcode).setOnClickListener(this);
        // 群公告
        groupNoticeSiv = findViewById(R.id.profile_siv_group_notice);
        groupNoticeSiv.setOnClickListener(this);

        groupManagerSiv = findViewById(R.id.profile_siv_group_manager);


        // 消息免打扰
        notifyNoticeSiv = findViewById(R.id.profile_siv_message_notice);
        disSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (disSwitch.isChecked()){
                   disSwitch.setChecked(false);
                }else {
                    disSwitch.setChecked(true);
                }*/
                updateStatusAction("2");

            }
        });

        // 会话置顶
        onTopSiv = findViewById(R.id.profile_siv_group_on_top);
        topSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   if (topSwitch.isChecked()){
                    topSwitch.setChecked(false);
                }else {
                    topSwitch.setChecked(true);
                }*/
                updateStatusAction("1");
            }
        });

        // 投诉
        isToContactSiv = findViewById(R.id.profile_siv_group_save_to_contact);
        isToContactSiv.setOnClickListener(this);

        //截屏通知
        screenShotTip = findViewById(R.id.tv_screen_shot_tip);
        screenShotSiv = findViewById(R.id.profile_siv_group_screen_shot_notification);
        screenShotSiv.setSwitchTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isScreenShotSivClicked) {
                    isScreenShotSivClicked = true;
                }
                return false;
            }
        });
        screenShotSiv.setSwitchCheckListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //初始化不触发逻辑
                if (!isScreenShotSivClicked) {
                    return;
                }
                // 0 关闭 1 打开
                if (isChecked) {
                    //没有权限不开启设置
                    if (!requestReadPermissions()) {
                        return;
                    }
                    groupDetailViewModel.setScreenCaptureStatus(1);
                } else {
                    groupDetailViewModel.setScreenCaptureStatus(0);
                }
            }
        });

        // 消息删除
        findViewById(R.id.profile_siv_group_clean_message).setOnClickListener(this);

        // 退出群组
        quitGroupBtn = findViewById(R.id.profile_btn_group_quit);
        quitGroupBtn.setOnClickListener(this);


        groupManagerSiv.setOnClickListener(this);

        //定时清理群消息
        cleanTimingSiv = findViewById(R.id.profile_siv_group_clean_timming);
        cleanTimingSiv.setOnClickListener(this);
    }


    private void updateStatusAction(String type) {
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(GroupChatItemActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("group_id",groupId);
        map.put("type",type);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap,MethodUrl.CHAT_GROUP_CHANAGE_STATUS, map);
    }



    private boolean requestReadPermissions() {
        return CheckPermissionUtils.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION);
    }

    private void initViewModel() {
        groupDetailViewModel = ViewModelProviders.of(this,
                new GroupDetailViewModel.Factory(this.getApplication(), groupId, conversationType))
                .get(GroupDetailViewModel.class);

        // 获取自己的信息
        groupDetailViewModel.getMyselfInfo().observe(this, new Observer<GroupMember>() {
            @Override
            public void onChanged(GroupMember member) {
                // 更具身份去控制对应的操作
                if (member.getMemberRole() == GroupMember.Role.GROUP_OWNER) {
                    quitGroupBtn.setText(R.string.profile_dismiss_group);
                    // 根据是否是群组判断是否可以选择删除成员
                    memberAdapter.setAllowDeleteMember(true);
                    groupManagerSiv.setVisibility(View.VISIBLE);
                    //设置截屏通知开关是否可见
                    screenShotSiv.setVisibility(View.VISIBLE);
                    screenShotTip.setVisibility(View.VISIBLE);
                } else if (member.getMemberRole() == GroupMember.Role.MANAGEMENT) {
                    groupPortraitUiv.setClickable(false);
                    groupNameSiv.setClickable(false);
                    quitGroupBtn.setText(R.string.profile_quit_group);
                    memberAdapter.setAllowDeleteMember(true);
                    groupManagerSiv.setVisibility(View.GONE);
                    screenShotSiv.setVisibility(View.VISIBLE);
                    screenShotTip.setVisibility(View.VISIBLE);
                } else {
                    groupPortraitUiv.setClickable(false);
                    groupNameSiv.setClickable(false);
                    quitGroupBtn.setText(R.string.profile_quit_group);
                    memberAdapter.setAllowDeleteMember(false);
                    groupManagerSiv.setVisibility(View.GONE);
                    screenShotSiv.setVisibility(View.GONE);
                    screenShotTip.setVisibility(View.GONE);
                }
            }
        });



        // 获取清除历史消息结果
        groupDetailViewModel.getCleanHistoryMessageResult().observe(this, resource -> {
            if (resource.status == Status.SUCCESS) {
                ToastUtils.showToast(R.string.common_clear_success);
            } else if (resource.status == Status.ERROR) {
                ToastUtils.showToast(R.string.common_clear_failure);
            }
        });

        groupDetailViewModel.getRegularClearResult().observe(this, new Observer<Resource<Void>>() {
            @Override
            public void onChanged(Resource<Void> resultResource) {
                if (resultResource.status == Status.SUCCESS) {
                    ToastUtils.showToast(getString(R.string.seal_set_clean_time_success));
                    groupDetailViewModel.requestRegularState(groupId);
                } else {
                    ToastUtils.showToast(getString(R.string.seal_set_clean_time_fail));
                }
            }
        });

        groupDetailViewModel.getRegularState().observe(this, new Observer<Resource<Integer>>() {
            @Override
            public void onChanged(Resource<Integer> groupRegularClearResultResource) {
                if (groupRegularClearResultResource.data != null) {
                    updateCleanTimingSiv(groupRegularClearResultResource.data);
                } else {
                    cleanTimingSiv.setValue(getString(R.string.seal_set_clean_time_state_not));
                }
            }
        });

        // 获取截屏通知结果
        groupDetailViewModel.getScreenCaptureStatusResult().observe(this, new Observer<Resource<ScreenCaptureResult>>() {
            @Override
            public void onChanged(Resource<ScreenCaptureResult> screenCaptureResultResource) {
                if (screenCaptureResultResource.status == Status.SUCCESS) {
                    //0 关闭 1 打开
                    if (screenCaptureResultResource.data != null && screenCaptureResultResource.data.status == 1) {
                        screenShotSiv.setCheckedImmediately(true);
                    }
                }
            }
        });

        // 获取设置截屏通知结果
        groupDetailViewModel.getSetScreenCaptureResult().observe(this, new Observer<Resource<Void>>() {
            @Override
            public void onChanged(Resource<Void> voidResource) {
                if (voidResource.status == Status.SUCCESS) {
                    ToastUtils.showToast(getString(R.string.seal_set_clean_time_success));
                } else if (voidResource.status == Status.ERROR) {
                    ToastUtils.showToast(getString(R.string.seal_set_clean_time_fail));
                }
            }
        });
    }

    private void updateCleanTimingSiv(int state) {
        if (state == SelectCleanTimeDialog.CLEAR_STATUS_NOT) {
            cleanTimingSiv.setValue(getString(R.string.seal_set_clean_time_state_not));
        } else if (state == SelectCleanTimeDialog.CLEAR_STATUS_THIRTY_SIX) {
            cleanTimingSiv.setValue(getString(R.string.seal_dialog_select_clean_time_36));
        } else if (state == SelectCleanTimeDialog.CLEAR_STATUS_THREE) {
            cleanTimingSiv.setValue(getString(R.string.seal_dialog_select_clean_time_3));
        } else if (state == SelectCleanTimeDialog.CLEAR_STATUS_SEVEN) {
            cleanTimingSiv.setValue(getString(R.string.seal_dialog_select_clean_time_7));
        }
    }


    /**
     * 是否是群主
     *
     * @return
     */
    private boolean isGroupOwner() {
        if (groupDetailViewModel != null) {
            GroupMember selfGroupInfo = groupDetailViewModel.getMyselfInfo().getValue();
            if (selfGroupInfo != null) {
                return selfGroupInfo.getMemberRole() == GroupMember.Role.GROUP_OWNER;
            } else {
                return false;
            }
        }

        return false;
    }

    /**
     * 是否是群管理员
     *
     * @return
     */
    private boolean isGroupManager() {
        if (groupDetailViewModel != null) {
            GroupMember selfGroupInfo = groupDetailViewModel.getMyselfInfo().getValue();
            if (selfGroupInfo != null) {
                return selfGroupInfo.getMemberRole() == GroupMember.Role.MANAGEMENT;
            }
        }
        return false;
    }

    /**
     * 更新群信息
     *
     * @param groupInfo
     */
    private void updateGroupInfoView(GroupEntity groupInfo) {
        // 标题
        String title = getString(R.string.profile_group_info) + "(" + groupInfo.getMemberCount() + ")";
        titleBar.setTitle(title);

        // 群成员数量
        String allMemberTxt = getString(R.string.profile_all_group_member) + "(" + groupInfo.getMemberCount() + ")";
        allGroupMemberSiv.setContent(allMemberTxt);
        // 显示群组头像
        grouportraitUrl = groupInfo.getPortraitUri();
        ImageLoaderUtils.displayGroupPortraitImage(groupInfo.getPortraitUri(), groupPortraitUiv.getHeaderImageView());
        // 群名称
        groupName = groupInfo.getName();
        groupNameSiv.setValue(groupInfo.getName());

        // 是否在通讯录中
        int isInContact = groupInfo.getIsInContact();
        if (isInContact == 0) {
            isToContactSiv.setCheckedImmediately(false);
        } else {
            isToContactSiv.setCheckedImmediately(true);
        }

        groupCreatorId = groupInfo.getCreatorId();

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.profile_siv_all_group_member:
                showAllGroupMember();
                break;
            case R.id.profile_siv_group_search_history_message: //查看聊天记录
                showSearchHistoryMessage();
                break;
            case R.id.profile_uiv_group_portrait_container:
                setGroupPortrait();
                break;
            case R.id.profile_siv_group_name_container: //修改群名称
                editGroupName();
                break;
            case R.id.profile_siv_group_qrcode://群二维码
                //showGroupQrCode();
                intent = new Intent(this, GroupQrCodeActivity.class);
                intent.putExtra("DATA", qrcodeImgUrl);
                startActivity(intent);
                break;
            case R.id.profile_siv_group_notice:
                showGroupNotice();
                break;
            case R.id.profile_siv_group_clean_message: //清除聊天记录
                showCleanMessageDialog();
                break;
            case R.id.profile_btn_group_quit: //退出群聊
                quitOrDeleteGroup();
                break;
            case R.id.profile_siv_group_manager: //群管理
                intent = new Intent(this, GroupChatManagerActivity.class);
                intent.putExtra(IntentExtra.GROUP_ID, groupId);
                intent.putExtra("DATA", (Serializable)memberList);
                startActivity(intent);
                break;
            case R.id.profile_siv_group_clean_timming:
                showRegualrClearDialog();
                break;
            case R.id.profile_siv_group_save_to_contact://投诉
                intent = new Intent(this, ChoseReasonTypeActivity.class);
                intent.putExtra(IntentExtra.GROUP_ID, groupId);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 显示成员信息
     *
     * @param groupMember
     */
    private void showMemberInfo(GroupMember groupMember) {
        //Intent intent = new Intent(this, UserDetailActivity.class);
        Intent intent = new Intent(this, AddFriendActivity.class);
        String qrCodeText = "1,"+groupMember.getUserId();
        intent.putExtra("DATA", qrCodeText);
       /* intent.putExtra(IntentExtra.FRIEND_ID, groupMember.getUserId());
        Group groupInfo = RongUserInfoManager.getInstance().getGroupInfo(groupId);
        if (groupInfo != null) {
            intent.putExtra(IntentExtra.STR_GROUP_NAME, groupInfo.getName());
        }*/
        startActivity(intent);
    }

    /**
     * 显示管理成员界面
     *
     * @param isAdd
     */
    private void toMemberManage(boolean isAdd) {
        if (isAdd) { //添加
            Intent intent = new Intent(this, SelectContractListActivity.class);
            intent.putExtra("TYPE","2");
            startActivityForResult(intent, REQUEST_ADD_GROUP_MEMBER);
        } else { //删除
            Intent intent = new Intent(this, SelectContractListActivity.class);
            intent.putExtra("TYPE","2");
            intent.putExtra("DATA", (Serializable)memberList);
           /* ArrayList<String> excludeList = new ArrayList<>();  // 不可选择的成员 id 列表
            String currentId = IMManager.getInstance().getCurrentId();
            excludeList.add(currentId);

            // 判断自己是否为群主，若非群主则添加群主至不可选择列表
            if (groupCreatorId != null && !currentId.equals(groupCreatorId)) {
                excludeList.add(groupCreatorId);
            }

            intent.putExtra(IntentExtra.LIST_EXCLUDE_ID_LIST, excludeList);*/
            startActivityForResult(intent, REQUEST_REMOVE_GROUP_MEMBER);
        }
    }

    /**
     * 显示全部群组成员
     */
    public void showAllGroupMember() {
        Intent intent = new Intent(this, GroupMemberListActivity.class);
        intent.putExtra(IntentExtra.STR_TARGET_ID, groupId);
        startActivity(intent);
    }

    /**
     * 显示搜索历史消息
     */
    public void showSearchHistoryMessage() {
        Intent intent = new Intent(this, SearchHistoryMessageActivity.class);
        intent.putExtra(IntentExtra.STR_TARGET_ID, groupId);
        intent.putExtra(IntentExtra.SERIA_CONVERSATION_TYPE, Conversation.ConversationType.GROUP);
        //群名
        LogUtilDebug.i("show","groupName:"+groupName);
        intent.putExtra(IntentExtra.STR_CHAT_NAME, groupName);
        //群组的头像
        LogUtilDebug.i("show","grouportraitUrl:"+grouportraitUrl);
        intent.putExtra(IntentExtra.STR_CHAT_PORTRAIT, grouportraitUrl);
        startActivity(intent);


    }


    /**
     * 编辑群名称
     */
    private void editGroupName() {

        SimpleInputDialog dialog = new SimpleInputDialog();
        dialog.setInputHint(getString(R.string.profile_hint_new_group_name));
        dialog.setInputDialogListener(new SimpleInputDialog.InputDialogListener() {
            @Override
            public boolean onConfirmClicked(EditText input) {
               name = input.getText().toString();

                if (name.length() < Constant.GROUP_NAME_MIN_LENGTH || name.length() > Constant.GROUP_NAME_MAX_LENGTH) {
                    ToastUtils.showToast(getString(R.string.profile_group_name_word_limit_format, Constant.GROUP_NAME_MIN_LENGTH, Constant.GROUP_NAME_MAX_LENGTH));
                    return true;
                }

                if (AndroidEmoji.isEmoji(name) && name.length() < Constant.GROUP_NAME_EMOJI_MIN_LENGTH) {
                    ToastUtils.showToast(getString(R.string.profile_group_name_emoji_too_short));
                    return true;
                }

                // 重命名群名称
                //groupDetailViewModel.renameGroupName(name);
                editGroupNameAction();
                return true;
            }
        });
        dialog.show(getSupportFragmentManager(), null);
    }

    private void editGroupNameAction() {
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(GroupChatItemActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("group_id",groupId);
        map.put("name",name);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_GROUP_EDIT_NAME, map);
    }

    /**
     * 编辑群组头像
     */
    private void setGroupPortrait() {

        SelectPictureBottomDialog.Builder builder = new SelectPictureBottomDialog.Builder();
        builder.setOnSelectPictureListener(new SelectPictureBottomDialog.OnSelectPictureListener() {
            @Override
            public void onSelectPicture(Uri uri) {
                SLog.d(TAG, "select picture, uri:" + uri);
                groupDetailViewModel.setGroupPortrait(uri);
            }
        });
        SelectPictureBottomDialog dialog = builder.build();
        dialog.show(getSupportFragmentManager(), null);
    }

    /**
     * 编辑群公告
     */
    private void showGroupNotice() {
        // 判断是否是群组或管理员
        if (isGroupOwner() || isGroupManager()) {
            Intent intent = new Intent(this, GroupNoticeActivity.class);
            intent.putExtra(IntentExtra.STR_TARGET_ID, groupId);
            intent.putExtra(IntentExtra.SERIA_CONVERSATION_TYPE, Conversation.ConversationType.GROUP);
            startActivity(intent);
        } else {
            GroupNoticeDialog commonDialog = new GroupNoticeDialog();
            commonDialog.setNoticeContent(lastGroupNoticeContent);
            commonDialog.setNoticeUpdateTime(lastGroupNoticeTime);
            commonDialog.show(getSupportFragmentManager(), null);
        }
    }

    /**
     * 退出或删除群组
     */
    private void quitOrDeleteGroup() {
        CommonDialog.Builder builder = new CommonDialog.Builder();
        // 根据是否群组显示不同的提示
        if (identiType.equals("2")) {
            builder.setContentMessage(getString(R.string.profile_confirm_dismiss_group));
        } else {
            builder.setContentMessage(getString(R.string.profile_confirm_quit_group));
        }
        builder.setDialogButtonClickListener(new CommonDialog.OnDialogButtonClickListener() {
            @Override
            public void onPositiveClick(View v, Bundle bundle) {
                // 根据是否是群组，选择解散还是退出群组
                exitGroupAction();
            }

            @Override
            public void onNegativeClick(View v, Bundle bundle) {
            }
        });
        builder.build().show(getSupportFragmentManager(), null);
    }

    private void exitGroupAction() {

        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(GroupChatItemActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("group_id",groupId);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_GROUP_EXIT, map);
    }

    /**
     * 显示清除聊天消息对话框
     */
    private void showCleanMessageDialog() {
       /* PromptPopupDialog.newInstance(this,
                getString(R.string.profile_clean_group_chat_history)).setLayoutRes(io.rong.imkit.R.layout.rc_dialog_popup_prompt_warning)
                .setPromptButtonClickedListener(() -> {
                    groupDetailViewModel.cleanHistoryMessage();
                }).show();*/

        SureOrNoDialog sureOrNoDialog = new SureOrNoDialog(GroupChatItemActivity.this,true);
        sureOrNoDialog.initValue("提示","是否清除聊天记录？");
        sureOrNoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.cancel:
                        sureOrNoDialog.dismiss();
                        break;
                    case R.id.confirm:
                        sureOrNoDialog.dismiss();
                        //清除聊天记录
                        //pingCangAllAction();
                        break;
                }
            }
        });
        sureOrNoDialog.show();
        sureOrNoDialog.setCanceledOnTouchOutside(false);
        sureOrNoDialog.setCancelable(true);

    }

    /**
     * 显示定时清理时间选择框
     */
    private void showRegualrClearDialog() {
        if (!isGroupOwner()) {
            ToastUtils.showToast(getString(R.string.seal_set_clean_time_not_owner_tip));
            return;
        }
        SelectCleanTimeDialog dialog = new SelectCleanTimeDialog();
        dialog.setOnDialogButtonClickListener(new SelectCleanTimeDialog.OnDialogButtonClickListener() {
            @Override
            public void onThirtySixHourClick() {
                setRegularClear(SelectCleanTimeDialog.CLEAR_STATUS_THIRTY_SIX);
            }

            @Override
            public void onThreeDayClick() {
                setRegularClear(SelectCleanTimeDialog.CLEAR_STATUS_THREE);
            }

            @Override
            public void onSevenDayClick() {
                setRegularClear(SelectCleanTimeDialog.CLEAR_STATUS_SEVEN);
            }

            @Override
            public void onNotCleanClick() {
                setRegularClear(SelectCleanTimeDialog.CLEAR_STATUS_NOT);
            }
        });
        dialog.show(getSupportFragmentManager(), "regular_clear");
    }

    private void setRegularClear(int time) {
        groupDetailViewModel.setRegularClear(time);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;

        if (requestCode == REQUEST_ADD_GROUP_MEMBER && resultCode == RESULT_OK) {
            // 添加群组成员
            String ids = data.getStringExtra(IntentExtra.LIST_STR_ID_LIST);
            addGroupMemberAction(ids);
        } else if (requestCode == REQUEST_REMOVE_GROUP_MEMBER && resultCode == RESULT_OK) {
            // 删除群组成员
            String ids = data.getStringExtra(IntentExtra.LIST_STR_ID_LIST);
            deleteGroupMemberAction(ids);
        }
    }


    private void addGroupMemberAction(String ids) {
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(GroupChatItemActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("group_id",groupId);
        map.put("id",ids);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_GROUP_ADD, map);

    }

    private void deleteGroupMemberAction(String ids) {
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(GroupChatItemActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("group_id",groupId);
        map.put("id",ids);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_GROUP_DELETE, map);

    }


    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION && !CheckPermissionUtils.allPermissionGranted(grantResults)) {
            List<String> permissionsNotGranted = new ArrayList<>();
            for (String permission : permissions) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    permissionsNotGranted.add(permission);
                }
            }
            if (permissionsNotGranted.size() > 0) {
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, requestCode);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                            default:
                                break;
                        }
                    }
                };
                CheckPermissionUtils.showPermissionAlert(this, getResources().getString(R.string.seal_grant_permissions) + CheckPermissionUtils.getNotGrantedPermissionMsg(this, permissionsNotGranted), listener);
            } else {
                ToastUtils.showToast(getString(R.string.seal_set_clean_time_fail));
            }
        } else {
            //权限获得后在请求次网络设置状态
            groupDetailViewModel.setScreenCaptureStatus(1);
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        Intent intent;
        switch (mType){
            case MethodUrl.CHAT_GROUPS_INFO:
                switch (tData.get("code")+""){
                    case "0":
                        if (!UtilTools.empty(tData.get("data")+"")){
                            Map<String,Object> map = (Map<String, Object>) tData.get("data");
                            if (!UtilTools.empty(map)){
                                groupName = map.get("name")+"";
                                groupNameSiv.setValue(groupName);
                                grouportraitUrl = map.get("portrait")+"";
                                qrcodeImgUrl = map.get("qrcode")+"";
                                if ((map.get("disturb")+"").equals("0")){
                                    disSwitch.setChecked(false);
                                }else {
                                    disSwitch.setChecked(true);
                                }
                                if ((map.get("protect")+"").equals("0")){
                                    topSwitch.setChecked(false);
                                }else {
                                    topSwitch.setChecked(true);
                                }

                                if (!UtilTools.empty(map.get("member")+"")){
                                    memberList = (List<Map<String, Object>>) map.get("member");
                                    List<GroupMember> groupMemberList = new ArrayList<>();
                                    for (Map<String,Object> map1:memberList){
                                        GroupMember member = new GroupMember();
                                        member.setName(map1.get("name")+"");
                                        member.setUserId(map1.get("id")+"");
                                        member.setGroupId(groupId);
                                        member.setPortraitUri(map1.get("portrait")+"");
                                        if (UtilTools.empty(identiType)){
                                            member.setRole(0);
                                        }else {
                                            member.setRole(Integer.parseInt(identiType));
                                        }
                                        groupMemberList.add(member);
                                    }
                                    memberAdapter.setAllowDeleteMember(true);
                                    memberAdapter.updateListView(groupMemberList);
                                }

                                //身份
                                identiType = map.get("type")+"";
                                if (identiType.equals("2")){
                                    managerLay.setVisibility(View.VISIBLE);
                                }else {
                                    managerLay.setVisibility(View.GONE);
                                }


                            }
                        }

                        break;
                    case "1":
                        closeAllActivity();
                        intent = new Intent(GroupChatItemActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg")+"");
                        break;
                }

                break;

            case MethodUrl.CHAT_GROUP_CHANAGE_STATUS:
                switch (tData.get("code")+"") {
                    case "0":
                        showToastMsg(tData.get("msg") + "");
                        break;
                    case "1":
                        closeAllActivity();
                        intent = new Intent(GroupChatItemActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
                        break;

                }
                break;
            case MethodUrl.CHAT_GROUP_EDIT_NAME:
                switch (tData.get("code")+"") {
                    case "0":
                        showToastMsg(tData.get("msg") + "");
                        groupNameSiv.setValue(name);
                        break;
                    case "1":
                        closeAllActivity();
                        intent = new Intent(GroupChatItemActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
                        break;

                }
                break;
            case MethodUrl.CHAT_GROUP_ADD:
                switch (tData.get("code")+"") {
                    case "0":
                        showToastMsg(tData.get("msg") + "");
                        getGroupInfoAction();
                        break;
                    case "1":
                        closeAllActivity();
                        intent = new Intent(GroupChatItemActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
                        break;

                }
                break;
            case MethodUrl.CHAT_GROUP_DELETE:
                switch (tData.get("code")+"") {
                    case "0":
                        showToastMsg(tData.get("msg") + "");
                        getGroupInfoAction();
                        break;
                    case "1":
                        closeAllActivity();
                        intent = new Intent(GroupChatItemActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
                        break;

                }
                break;
            case MethodUrl.CHAT_GROUP_EXIT:
                switch (tData.get("code")+"") {
                    case "0":
                        showToastMsg(tData.get("msg") + "");
                        finish();
                        //Eventbus  发送事件
                        MessageEvent event = new MessageEvent();
                        event.setType(MbsConstans.MessageEventType.CLOSE_CONACTIVITY);
                        Map<Object,Object> map = new HashMap<>();
                        event.setMessage(map);
                        EventBus.getDefault().post(event);

                        break;
                    case "1":
                        closeAllActivity();
                        intent = new Intent(GroupChatItemActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
                        break;

                }
                break;
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
        dealFailInfo(map,mType);
    }
}
