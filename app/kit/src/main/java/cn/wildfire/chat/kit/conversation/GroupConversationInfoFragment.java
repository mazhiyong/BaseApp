package cn.wildfire.chat.kit.conversation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.kyleduo.switchbutton.SwitchButton;
import com.lr.biyou.R;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.chatry.utils.ToastUtils;
import com.lr.biyou.mywidget.dialog.SureOrNoDialog;
import com.lr.biyou.ui.moudle.activity.MainActivity;
import com.lr.biyou.ui.moudle2.activity.ChoseReasonTypeActivity;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.permission.PermissionsUtils;
import com.lr.biyou.utils.permission.RePermissionResultBack;
import com.lr.biyou.utils.tool.AppUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.yanzhenjie.permission.Permission;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.WfcScheme;
import cn.wildfire.chat.kit.WfcUIKit;
import cn.wildfire.chat.kit.common.OperateResult;
import cn.wildfire.chat.kit.conversationlist.ConversationListViewModel;
import cn.wildfire.chat.kit.conversationlist.ConversationListViewModelFactory;
import cn.wildfire.chat.kit.group.AddGroupMemberActivity;
import cn.wildfire.chat.kit.group.GroupMemberListActivity;
import cn.wildfire.chat.kit.group.GroupViewModel;
import cn.wildfire.chat.kit.group.RemoveGroupMemberActivity;
import cn.wildfire.chat.kit.group.SetGroupNameActivity;
import cn.wildfire.chat.kit.group.manage.GroupManageActivity;
import cn.wildfire.chat.kit.qrcode.QRCodeActivity;
import cn.wildfire.chat.kit.search.SearchMessageActivity;
import cn.wildfire.chat.kit.user.UserInfoActivity;
import cn.wildfire.chat.kit.user.UserViewModel;
import cn.wildfire.chat.kit.widget.OptionItemView;
import cn.wildfirechat.message.MessageContentMediaType;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.model.ConversationInfo;
import cn.wildfirechat.model.GroupInfo;
import cn.wildfirechat.model.GroupMember;
import cn.wildfirechat.model.ModifyGroupInfoType;
import cn.wildfirechat.model.UserInfo;
import cn.wildfirechat.remote.ChatManager;
import cn.wildfirechat.remote.GeneralCallback;
import cn.wildfirechat.remote.UploadMediaCallback;
import cn.wildfirechat.remote.UserSettingScope;

public class GroupConversationInfoFragment extends Fragment implements ConversationMemberAdapter.OnMemberClickListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.contentNestedScrollView)
    NestedScrollView contentNestedScrollView;

    // group
    @BindView(R.id.groupLinearLayout_0)
    LinearLayout groupLinearLayout_0;
    @BindView(R.id.groupNameOptionItemView)
    OptionItemView groupNameOptionItemView;
    @BindView(R.id.groupQRCodeOptionItemView)
    OptionItemView groupQRCodeOptionItemView;
    @BindView(R.id.groupNoticeLinearLayout)
    LinearLayout noticeLinearLayout;
    @BindView(R.id.groupNoticeTextView)
    TextView noticeTextView;
    @BindView(R.id.groupManageOptionItemView)
    OptionItemView groupManageOptionItemView;
    @BindView(R.id.showAllMemberButton)
    Button showAllGroupMemberButton;

    @BindView(R.id.groupLinearLayout_1)
    LinearLayout groupLinearLayout_1;
    @BindView(R.id.myGroupNickNameOptionItemView)
    OptionItemView myGroupNickNameOptionItemView;
    @BindView(R.id.showGroupMemberAliasSwitchButton)
    SwitchButton showGroupMemberNickNameSwitchButton;

    @BindView(R.id.quitButton)
    Button quitGroupButton;

    @BindView(R.id.markGroupLinearLayout)
    LinearLayout markGroupLinearLayout;
    @BindView(R.id.markGroupSwitchButton)
    SwitchButton markGroupSwitchButton;

    // common
    @BindView(R.id.memberRecyclerView)
    RecyclerView memberReclerView;
    @BindView(R.id.stickTopSwitchButton)
    SwitchButton stickTopSwitchButton;
    @BindView(R.id.silentSwitchButton)
    SwitchButton silentSwitchButton;
    @BindView(R.id.head_iv)
    ImageView headIv;
    @BindView(R.id.head_lay)
    LinearLayout headLay;
    @BindView(R.id.tousuMessageOptionItemView)
    OptionItemView tousuMessageOptionItemView;

    private ConversationInfo conversationInfo;
    private ConversationMemberAdapter conversationMemberAdapter;
    private ConversationViewModel conversationViewModel;
    private UserViewModel userViewModel;

    private GroupViewModel groupViewModel;
    private GroupInfo groupInfo;
    // me in group
    private GroupMember groupMember;


    public static GroupConversationInfoFragment newInstance(ConversationInfo conversationInfo) {
        GroupConversationInfoFragment fragment = new GroupConversationInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable("conversationInfo", conversationInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        assert args != null;
        conversationInfo = args.getParcelable("conversationInfo");
        assert conversationInfo != null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.conversation_info_group_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        conversationViewModel = WfcUIKit.getAppScopeViewModel(ConversationViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        groupLinearLayout_0.setVisibility(View.VISIBLE);
        groupLinearLayout_1.setVisibility(View.VISIBLE);
        markGroupLinearLayout.setVisibility(View.VISIBLE);
        markGroupSwitchButton.setOnCheckedChangeListener(this);
        quitGroupButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        LogUtilDebug.i("show", "群id>>>>>:" + conversationInfo.conversation.target);

        groupInfo = groupViewModel.getGroupInfo(conversationInfo.conversation.target, false);

        LogUtilDebug.i("show", "群id2>>>>>:" + conversationInfo.conversation.target);

        GlideUtils.loadImage(getActivity(), groupInfo.portrait, headIv);
        if (groupInfo != null) {
            groupMember = ChatManager.Instance().getGroupMember(groupInfo.target, ChatManager.Instance().getUserId());
        }

        if (groupMember == null || groupMember.type == GroupMember.GroupMemberType.Removed) {
            Toast.makeText(getActivity(), "你不在群组或发生错误, 请稍后再试", Toast.LENGTH_SHORT).show();
            getActivity().finish();
            return;
        }
        loadAndShowGroupMembers(true);

        userViewModel.userInfoLiveData().observe(this, userInfos -> loadAndShowGroupMembers(false));


        observerFavGroupsUpdate();
        observerGroupInfoUpdate();
        observerGroupMembersUpdate();
    }

    private void observerFavGroupsUpdate() {
        groupViewModel.getMyGroups().observe(this, listOperateResult -> {
            if (listOperateResult.isSuccess()) {
                for (GroupInfo info : listOperateResult.getResult()) {
                    if (groupInfo.target.equals(info.target)) {
                        markGroupSwitchButton.setChecked(true);
                        break;
                    }
                }
            }
        });
    }




    private void observerGroupMembersUpdate() {
        groupViewModel.groupMembersUpdateLiveData().observe(this, groupMembers -> {
            loadAndShowGroupMembers(false);
        });
    }

    private void observerGroupInfoUpdate() {
        groupViewModel.groupInfoUpdateLiveData().observe(this, groupInfos -> {
            for (GroupInfo groupInfo : groupInfos) {
                if (groupInfo.target.equals(this.groupInfo.target)) {
                    groupNameOptionItemView.setDesc(groupInfo.name);
                    loadAndShowGroupMembers(false);
                    break;
                }
            }

        });
    }


    private void loadAndShowGroupMembers(boolean refresh) {
        groupViewModel.getGroupMembersLiveData(conversationInfo.conversation.target, refresh)
                .observe(this, groupMembers -> {
                    progressBar.setVisibility(View.GONE);
                    showGroupMembers(groupMembers);
                    showGroupManageViews();
                    contentNestedScrollView.setVisibility(View.VISIBLE);
                });
    }



    private void showGroupManageViews() {
        if (groupMember.type == GroupMember.GroupMemberType.Manager || groupMember.type == GroupMember.GroupMemberType.Owner) {
            groupManageOptionItemView.setVisibility(View.VISIBLE);
        }

        showGroupMemberNickNameSwitchButton.setChecked("1".equals(userViewModel.getUserSetting(UserSettingScope.GroupHideNickname, groupInfo.target)));
        showGroupMemberNickNameSwitchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            userViewModel.setUserSetting(UserSettingScope.GroupHideNickname, groupInfo.target, isChecked ? "1" : "0");
        });

        myGroupNickNameOptionItemView.setDesc(groupMember.alias);
        groupNameOptionItemView.setDesc(groupInfo.name);

        stickTopSwitchButton.setChecked(conversationInfo.isTop);
        silentSwitchButton.setChecked(conversationInfo.isSilent);
        stickTopSwitchButton.setOnCheckedChangeListener(this);
        silentSwitchButton.setOnCheckedChangeListener(this);

        if (groupInfo != null && ChatManagerHolder.gChatManager.getUserId().equals(groupInfo.owner)) {
            quitGroupButton.setText(R.string.delete_and_dismiss);
        } else {
            quitGroupButton.setText(R.string.delete_and_exit);
        }
    }

    private void showGroupMembers(List<GroupMember> groupMembers) {
        if (groupMembers == null || groupMembers.isEmpty()) {
            return;
        }
        String userId = ChatManager.Instance().getUserId();
        List<String> memberIds = new ArrayList<>();
        for (GroupMember member : groupMembers) {
            memberIds.add(member.memberId);
        }

        boolean enableRemoveMember = false;
        boolean enableAddMember = false;
        if (groupInfo.joinType == 2) {
            if (groupMember.type == GroupMember.GroupMemberType.Owner || groupMember.type == GroupMember.GroupMemberType.Manager) {
                enableAddMember = true;
                enableRemoveMember = true;
            }
        } else {
            enableAddMember = true;
            if (groupMember.type != GroupMember.GroupMemberType.Normal || userId.equals(groupInfo.owner)) {
                enableRemoveMember = true;
            }
        }
        int maxShowMemberCount = 45;
        if (enableAddMember) {
            maxShowMemberCount--;
        }
        if (enableRemoveMember) {
            maxShowMemberCount--;
        }
        if (memberIds.size() > maxShowMemberCount) {
            showAllGroupMemberButton.setVisibility(View.VISIBLE);
            memberIds = memberIds.subList(0, maxShowMemberCount);
        }

        conversationMemberAdapter = new ConversationMemberAdapter(enableAddMember, enableRemoveMember);
        List<UserInfo> members = UserViewModel.getUsers(memberIds, groupInfo.target);

        conversationMemberAdapter.setMembers(members);
        conversationMemberAdapter.setOnMemberClickListener(this);
        memberReclerView.setAdapter(conversationMemberAdapter);
        memberReclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        memberReclerView.setNestedScrollingEnabled(false);
        memberReclerView.setHasFixedSize(true);
        memberReclerView.setFocusable(false);

    }

    @OnClick(R.id.groupNameOptionItemView)
    void updateGroupName() {
        if (groupInfo.type != GroupInfo.GroupType.Restricted
                || (groupMember.type == GroupMember.GroupMemberType.Manager || groupMember.type == GroupMember.GroupMemberType.Owner)) {
            Intent intent = new Intent(getActivity(), SetGroupNameActivity.class);
            intent.putExtra("groupInfo", groupInfo);
            startActivity(intent);
        }
    }

    //更新群公告
    @OnClick(R.id.groupNoticeLinearLayout)
    void updateGroupNotice() {
        // TODO
    }

    //更新群头像
    @OnClick(R.id.head_lay)
    void updateGroupHeadImage() {

        ActionSheetDialogNoTitle();
    }


    //投诉
    @OnClick(R.id.tousuMessageOptionItemView)
    public void onViewClicked() {
        Intent intent = new Intent(getActivity(), ChoseReasonTypeActivity.class);
        intent.putExtra("id", conversationInfo.conversation.target);
        intent.putExtra("type","2");
        startActivity(intent);
    }

    @OnClick(R.id.groupManageOptionItemView)
    void manageGroup() {
        Intent intent = new Intent(getActivity(), GroupManageActivity.class);
        intent.putExtra("groupInfo", groupInfo);
        startActivity(intent);
    }

    @OnClick(R.id.showAllMemberButton)
    void showAllGroupMember() {
        Intent intent = new Intent(getActivity(), GroupMemberListActivity.class);
        intent.putExtra("groupInfo", groupInfo);
        startActivity(intent);
    }

    @OnClick(R.id.myGroupNickNameOptionItemView)
    void updateMyGroupAlias() {
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .input("请输入你的群昵称", groupMember.alias, false, (dialog1, input) -> {
                    groupViewModel.modifyMyGroupAlias(groupInfo.target, input.toString().trim())
                            .observe(GroupConversationInfoFragment.this, operateResult -> {
                                if (operateResult.isSuccess()) {
                                    myGroupNickNameOptionItemView.setDesc(input.toString().trim());
                                } else {
                                    Toast.makeText(getActivity(), "修改群昵称失败:" + operateResult.getErrorCode(), Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .negativeText("取消")
                .positiveText("确定")
                .onPositive((dialog12, which) -> {
                    dialog12.dismiss();
                })
                .build();
        dialog.show();
    }

    @OnClick(R.id.quitButton)
    void quitGroup() {

        SureOrNoDialog sureOrNoDialog = new SureOrNoDialog(getActivity(), true);
        if (groupInfo != null && userViewModel.getUserId().equals(groupInfo.owner)) {
            sureOrNoDialog.initValue("提示", "是否解散当前群聊？");
        }else {
            sureOrNoDialog.initValue("提示", "是否确定退出当前群聊？");
        }
        sureOrNoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cancel:
                        sureOrNoDialog.dismiss();
                        break;
                    case R.id.confirm:
                        sureOrNoDialog.dismiss();
                        if (groupInfo != null && userViewModel.getUserId().equals(groupInfo.owner)) {
                            groupViewModel.dismissGroup(conversationInfo.conversation.target, Collections.singletonList(0)).observe(getActivity(), aBoolean -> {
                                if (aBoolean != null && aBoolean) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getActivity(), "退出群组失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            groupViewModel.quitGroup(conversationInfo.conversation.target, Collections.singletonList(0)).observe(getActivity(), aBoolean -> {
                                if (aBoolean != null && aBoolean) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getActivity(), "退出群组失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        break;
                }
            }
        });
        sureOrNoDialog.show();
        sureOrNoDialog.setCanceledOnTouchOutside(false);
        sureOrNoDialog.setCancelable(true);

    }

    @OnClick(R.id.clearMessagesOptionItemView)
    void clearMessage() {
        SureOrNoDialog sureOrNoDialog = new SureOrNoDialog(getActivity(), true);
        sureOrNoDialog.initValue("提示", "是否清空聊天记录？");
        sureOrNoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.cancel:
                        sureOrNoDialog.dismiss();
                        break;
                    case R.id.confirm:
                        sureOrNoDialog.dismiss();
                        conversationViewModel.clearConversationMessage(conversationInfo.conversation);
                        break;
                }
            }
        });
        sureOrNoDialog.show();
        sureOrNoDialog.setCanceledOnTouchOutside(false);
        sureOrNoDialog.setCancelable(true);

    }

    @OnClick(R.id.groupQRCodeOptionItemView)
    void showGroupQRCode() {
        String qrCodeValue = WfcScheme.QR_CODE_PREFIX_GROUP + groupInfo.target;
        Intent intent = QRCodeActivity.buildQRCodeIntent(getActivity(), "群二维码", groupInfo.portrait, qrCodeValue);
        startActivity(intent);
    }

    @OnClick(R.id.searchMessageOptionItemView)
    void searchGroupMessage() {
        Intent intent = new Intent(getActivity(), SearchMessageActivity.class);
        intent.putExtra("conversation", conversationInfo.conversation);
        startActivity(intent);
    }

    @Override
    public void onUserMemberClick(UserInfo userInfo) {
        if (groupInfo != null && groupInfo.privateChat == 1 && groupMember.type == GroupMember.GroupMemberType.Normal) {
            return;
        }
        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
        intent.putExtra("userInfo", userInfo);
        startActivity(intent);
    }

    @Override
    public void onAddMemberClick() {
        Intent intent = new Intent(getActivity(), AddGroupMemberActivity.class);
        intent.putExtra("groupInfo", groupInfo);
        startActivity(intent);
    }

    @Override
    public void onRemoveMemberClick() {
        if (groupInfo != null) {
            Intent intent = new Intent(getActivity(), RemoveGroupMemberActivity.class);
            intent.putExtra("groupInfo", groupInfo);
            startActivity(intent);
        }
    }

    private void stickTop(boolean top) {
        ConversationListViewModel conversationListViewModel = ViewModelProviders
                .of(this, new ConversationListViewModelFactory(Arrays.asList(Conversation.ConversationType.Single, Conversation.ConversationType.Group, Conversation.ConversationType.Channel), Arrays.asList(0)))
                .get(ConversationListViewModel.class);
        conversationListViewModel.setConversationTop(conversationInfo, top);
    }

    private void markGroup(boolean mark) {
        groupViewModel.setFavGroup(groupInfo.target, mark);
    }

    private void silent(boolean silent) {
        conversationViewModel.setConversationSilent(conversationInfo.conversation, silent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.markGroupSwitchButton:
                markGroup(isChecked);
                break;
            case R.id.stickTopSwitchButton:
                stickTop(isChecked);
                break;
            case R.id.silentSwitchButton:
                silent(isChecked);
                break;
            default:
                break;
        }

    }


    private void ActionSheetDialogNoTitle() {
        final String[] stringItems = {"拍照", "从手机相册选择"};
        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(), stringItems, null);
        dialog.isTitleShow(false).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                switch (position) {
                    case 0:
                        PermissionsUtils.requsetRunPermission(getActivity(), new RePermissionResultBack() {
                            @Override
                            public void requestSuccess() {
                                //toast(R.string.successfully);
                                photoPic();
                            }

                            @Override
                            public void requestFailer() {
                                ToastUtils.showToast(R.string.failure);
                            }
                        }, Permission.Group.STORAGE, Permission.Group.CAMERA);


                        break;
                    case 1:
                        PermissionsUtils.requsetRunPermission(getActivity(), new RePermissionResultBack() {
                            @Override
                            public void requestSuccess() {
                                //toast(R.string.successfully);
                                localPic();
                            }

                            @Override
                            public void requestFailer() {
                                ToastUtils.showToast(R.string.failure);
                            }
                        }, Permission.Group.STORAGE);


                        break;
                }

            }
        });
    }

    private void photoPic() {
        /**
         * 下面这句还是老样子，调用快速拍照功能，至于为什么叫快速拍照，大家可以参考如下官方
         * 文档，you_sdk_path/docs/guide/topics/media/camera.html
         * 我刚看的时候因为太长就认真看，其实是错的，这个里面有用的太多了，所以大家不要认为
         * 官方文档太长了就不看了，其实是错的，这个地方也错了，必须改正
         */
        Uri uri;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //下面这句指定调用相机拍照后的照片存储的路径
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(getActivity(), AppUtil.getAppProcessName(getActivity()) + ".FileProvider", new File(Environment.getExternalStorageDirectory(), "xiaoma.jpg"));
        } else {
            uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "xiaoma.jpg"));
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 2);
    }

    private void localPic() {

        /**
         * 刚开始，我自己也不知道ACTION_PICK是干嘛的，后来直接看Intent源码，
         * 可以发现里面很多东西，Intent是个很强大的东西，大家一定仔细阅读下
         */

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        /**
         * 下面这句话，与其它方式写是一样的效果，如果：
         * intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         * intent.setType(""image/*");设置数据类型
         * 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
         * 这个地方小马有个疑问，希望高手解答下：就是这个数据URI与类型为什么要分两种形式来写呀？有什么区别？
         */
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 1);
    }


    private Intent dataIntent = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Uri uri = null;
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (data != null) {
                    startPhotoZoom(data.getData());
                }
                break;
            // 如果是调用相机拍照时
            case 2:

                File temp = new File(Environment.getExternalStorageDirectory() + "/xiaoma.jpg");
                if (temp.exists()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(getActivity(), AppUtil.getAppProcessName(getActivity()) + ".FileProvider", temp);
                    } else {
                        uri = Uri.fromFile(temp);
                    }
                    startPhotoZoom(uri);
                }

                //			path=photoSavePath+photoSaveName;
                //			uri = Uri.fromFile(new File(path));
                //			Intent intent2=new Intent(getActivity(), ClipActivity.class);
                //			intent2.putExtra("path", path);
                //			startActivityForResult(intent2, 4);
                break;
            // 取得裁剪后的图片
            case 3:
                /**
                 * 非空判断大家一定要验证，如果不验证的话，
                 * 在剪裁之后如果发现不满意，要重新裁剪，丢弃
                 * 当前功能时，会报NullException，小马只
                 * 在这个地方加下，大家可以根据不同情况在合适的
                 * 地方做判断处理类似情况
                 *
                 */
            /*    Toast.makeText(BusinessInfoActivity.this,"EEEEEEEEEEEEEEEEEEEEEEEEEEEEEE",Toast.LENGTH_LONG).show();
                if(data != null){
                    Toast.makeText(BusinessInfoActivity.this,"QQQQQQQQQQQQQQQQQQQQQQQQQQQQQ",Toast.LENGTH_LONG).show();
                    dataIntent = data;
                    setPicToView(data);
                }else {
                    Toast.makeText(BusinessInfoActivity.this,"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$",Toast.LENGTH_LONG).show();

                }
*/

                // 将Uri图片转换为Bitmap
                Bitmap bitmap = null;
                try {
                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uritempFile));
                    // TODO，将裁剪的bitmap显示在imageview控件上
                    Drawable dr = new BitmapDrawable(getResources(), bitmap);
                    saveCroppedImage(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                //ImageLoader.getmContext().displayImage(MbsConstans.Pic_Path+MbsConstans.memberUser.getHeadPath(),mHeadImage);
                // UtilTools.showImage(MbsConstans.Pic_Path+MbsConstans.memberUser.getHeadPath(),mHeadImage, R.drawable.no_def);

                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(getResources(), photo);
            uploadPic();
        }
    }

    private void uploadPic() {
        if (dataIntent != null) {
            Bundle extras = dataIntent.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                if (photo == null) {
                    return;
                }
                saveCroppedImage(photo);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);

            }
        } else {
            ToastUtils.showToast("图片不存在");
        }
    }


    private String mHeadImgPath = "";

    private void saveCroppedImage(Bitmap bmp) {

        try {
            File saveFile = new File(MbsConstans.HEAD_IMAGE_PATH);

            mHeadImgPath = MbsConstans.HEAD_IMAGE_PATH + new Date().getTime() + ".png";
            File file = new File(mHeadImgPath);
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            File saveFile2 = new File(mHeadImgPath);

            FileOutputStream fos = new FileOutputStream(saveFile2);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();

            // uploadAliPic(new Date().getTime()+".png",filepath);

            //上传头像
            //更新群头像
            ChatManager.Instance().uploadMediaFile(mHeadImgPath, MessageContentMediaType.PORTRAIT.getValue(), new UploadMediaCallback() {
                @Override
                public void onSuccess(String result) {
                    //群头像文件上传成功
                    MutableLiveData<OperateResult<Boolean>> result1 = new MutableLiveData<>();
                    ChatManager.Instance().modifyGroupInfo(conversationInfo.conversation.target, ModifyGroupInfoType.Modify_Group_Portrait, result, Collections.singletonList(0), null, new GeneralCallback() {
                        @Override
                        public void onSuccess() {
                            ToastUtils.showToast("群头像更新成功");
                            GlideUtils.loadImage(getActivity(), mHeadImgPath, headIv);
                            result1.setValue(new OperateResult<>(0));
                        }

                        @Override
                        public void onFail(int errorCode) {
                            ToastUtils.showToast("群头像更新失败");
                            result1.setValue(new OperateResult<>(errorCode));
                        }
                    });
                }

                @Override
                public void onProgress(long uploaded, long total) {

                }

                @Override
                public void onFail(int errorCode) {
                    ToastUtils.showToast("群头像文件上传失败");
                    //groupLiveData.setValue(new OperateResult<>("上传群头像失败", errorCode));
                }
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String imgName = "";
    private Uri uritempFile;

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        /*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
         * yourself_sdk_path/docs/reference/android/content/Intent.html
         * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能,
         * 是直接调本地库的，小马不懂C C++  这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么
         * 制做的了...吼吼
         */
    /*    Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image*//*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);*/
        File file = new File(MbsConstans.BASE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.putExtra("crop", "true");
        // intent.putExtra("noFaceDetection", true);
        // 宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪图片宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);

        // intent.putExtra("scale", true);
        // intent.putExtra("return-data", true);
        // this.startActivityForResult(intent, AppFinal.RESULT_CODE_PHOTO_CUT);
        /**
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
        imgName = System.currentTimeMillis() + ".jpg";
        uritempFile = Uri.parse("file:///" + MbsConstans.BASE_PATH + "/" + imgName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, 3);

    }



}
