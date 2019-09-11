package com.lr.biyou.ui.moudle2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.rongyun.common.IntentExtra;
import com.lr.biyou.rongyun.db.model.GroupEntity;
import com.lr.biyou.rongyun.model.GroupMember;
import com.lr.biyou.rongyun.model.Resource;
import com.lr.biyou.rongyun.model.Status;
import com.lr.biyou.rongyun.ui.activity.GroupManagementsActivity;
import com.lr.biyou.rongyun.ui.adapter.GridGroupMemberAdapter;
import com.lr.biyou.rongyun.ui.view.SettingItemView;
import com.lr.biyou.rongyun.ui.widget.WrapHeightGridView;
import com.lr.biyou.rongyun.ui.widget.switchbutton.SwitchButton;
import com.lr.biyou.rongyun.utils.ToastUtils;
import com.lr.biyou.rongyun.viewmodel.GroupManagementViewModel;
import com.lr.biyou.ui.moudle.activity.LoginActivity;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class GroupChatManagerActivity extends BasicActivity implements View.OnClickListener {
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
    @BindView(R.id.talk_switch)
    SwitchButton talkSwitch;
    @BindView(R.id.protect_switch)
    SwitchButton protectSwitch;
    @BindView(R.id.join_switch)
    SwitchButton joinSwitch;



    /**
     * 最大显示成员数
     */
    private final int SHOW_GROUP_MEMBER_LIMIT = 30;

    private static final int MANAGEMENT_MAX = 5;
    private static final int REQUEST_SET_NEW_OWNER = 1000;

    private String groupId = "";
    private GridGroupMemberAdapter memberAdapter;
    private WrapHeightGridView groupMemberGv;
    private GroupManagementViewModel groupManagementViewModel;
    private SettingItemView setGroupManagerSiv;
    private SettingItemView muteAllSiv;
    private SettingItemView addCertifiSiv;
    private SettingItemView protectAllCertifiSiv;
    private boolean isMuteSivTouched = false;
    private boolean isCeriSivTouched = false;

    private final int SWITCH_OPEN = 0;
    private final int SWITCH_CLOSE = 1;

    /**
     * 跳转界面请求添加群组成员
     */
    private final int REQUEST_ADD_GROUP_MEMBER = 1000;
    /**
     * 跳转界面请求移除群组成员
     */
    private final int REQUEST_REMOVE_GROUP_MEMBER = 1001;

    private List<Map<String, Object>> managerList;
    private List<Map<String, Object>> memberList;
    private String identiType = ""; //身份类型  0普通用户，1管理员，2群主

    @Override
    public int getContentView() {
        return R.layout.activity_group_chat_manager;
    }

    @Override
    public void init() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        mTitleText.setText("管理员设置");
        mTitleText.setCompoundDrawables(null, null, null, null);
        divideLine.setVisibility(View.GONE);
        if (getIntent() != null){
            Bundle bundle =getIntent().getExtras();
            if (bundle != null){
                groupId = bundle.getString(IntentExtra.GROUP_ID);
                memberList = (List<Map<String, Object>>) bundle.getSerializable("DATA");
            }

        }


        initView();


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
                toMemberManage(isAdd);//添加或删除
            }

            @Override
            public void onMemberClicked(GroupMember groupMember) {
                //查看群成员信息
                showMemberInfo(groupMember);


            }
        });


        protectAllCertifiSiv = findViewById(R.id.protect_all);
        setGroupManagerSiv = findViewById(R.id.siv_set_group_manager);
        setGroupManagerSiv.setOnClickListener(this);
        SettingItemView transferSiv = findViewById(R.id.siv_transfer);
        transferSiv.setOnClickListener(this);
        muteAllSiv = findViewById(R.id.siv_mute_all);
        //群成员保护
        protectSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (protectSwitch.isChecked()){
                    protectSwitch.setChecked(false);
                }else {
                    protectSwitch.setChecked(true);
                }*/

                updateStatusAction("2");


            }
        });
        //禁言
        talkSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (talkSwitch.isChecked()){
                    talkSwitch.setChecked(false);
                }else {
                    talkSwitch.setChecked(true);
                }*/

                updateStatusAction("1");
            }
        });

        //群认证
        joinSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (joinSwitch.isChecked()){
                    joinSwitch.setChecked(false);
                }else {
                    joinSwitch.setChecked(true);
                }
*/
                updateStatusAction("3");
            }
        });

        //获取管理员信息
        getGroupInfoAction();
    }





    private void getGroupInfoAction() {

        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(GroupChatManagerActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("group_id", groupId);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_GROUP_ADMIN, map);

    }


    private void updateStatusAction(String type) {
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(GroupChatManagerActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("group_id",groupId);
        map.put("type",type);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap,MethodUrl.CHAT_GROUP_MANAGE, map);
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
    private void initViewModel() {
        groupManagementViewModel = ViewModelProviders.of(this, new GroupManagementViewModel.Factory(groupId, getApplication())).get(GroupManagementViewModel.class);

        groupManagementViewModel.getGroupManagements().observe(this, new Observer<Resource<List<GroupMember>>>() {
            @Override
            public void onChanged(Resource<List<GroupMember>> resource) {
                int managementNumber = resource.data == null ? 0 : resource.data.size();
                // 减掉1 ， 因为有一条是添加管理员的item
                setGroupManagerSiv.setValue(managementNumber + "/" + MANAGEMENT_MAX);
            }
        });

        groupManagementViewModel.getGroupInfo().observe(this, new Observer<GroupEntity>() {
            @Override
            public void onChanged(GroupEntity groupEntity) {
                if (groupEntity != null) {
                    if (groupEntity.getIsMute() == 1) {
                        muteAllSiv.setCheckedImmediately(true);
                    }
                    if (groupEntity.getCertiStatus() == SWITCH_OPEN) {
                        addCertifiSiv.setCheckedImmediately(true);
                    }
                }
            }
        });

        groupManagementViewModel.getMuteAllResult().observe(this, new Observer<Resource<Void>>() {
            @Override
            public void onChanged(Resource<Void> voidResource) {
                if (voidResource.status == Status.SUCCESS) {
                    ToastUtils.showToast(R.string.seal_set_clean_time_success);
                } else if (voidResource.status == Status.ERROR) {
                    ToastUtils.showToast(R.string.seal_set_clean_time_fail);
                }
            }
        });

        groupManagementViewModel.getCerifiResult().observe(this, new Observer<Resource<Void>>() {
            @Override
            public void onChanged(Resource<Void> voidResource) {
//                if (voidResource.status == Status.SUCCESS) {
//                    ToastUtils.showToast(R.string.seal_set_clean_time_success);
//                } else if (voidResource.status == Status.ERROR) {
//                    ToastUtils.showToast(R.string.seal_set_clean_time_fail);
//                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.siv_set_group_manager: //设置管理员
                Intent intent = new Intent(this, GroupManagementsActivity.class);
                intent.putExtra(IntentExtra.GROUP_ID, groupId);
                startActivity(intent);
                break;
            case R.id.siv_transfer: //群转让
                Intent intentTransfer = new Intent(this, SelectContractListActivity.class);
                intentTransfer.putExtra("TYPE", "4");
                intentTransfer.putExtra(IntentExtra.GROUP_ID, groupId);
                startActivityForResult(intentTransfer,REQUEST_SET_NEW_OWNER);
                break;
            default:
                // Do nothing
                break;
        }
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
        }else if (requestCode == REQUEST_SET_NEW_OWNER && resultCode == RESULT_OK){
            //转让
            String id = data.getStringExtra(IntentExtra.LIST_STR_ID_LIST);
            transGroupAction(id);

        }
    }

    private void addGroupMemberAction(String ids) {
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(GroupChatManagerActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("group_id",groupId);
        map.put("id",ids);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_GROUP_ADMIN_ADD, map);

    }

    private void transGroupAction(String id) {
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(GroupChatManagerActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("group_id",groupId);
        map.put("id",id);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_GROUP_TRANSFER, map);

    }

    private void deleteGroupMemberAction(String ids) {
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(GroupChatManagerActivity.this, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("group_id",groupId);
        map.put("id",ids);
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_GROUP_ADMIN_DELETE, map);

    }




    /**
     * 显示管理成员界面
     *
     * @param isAdd
     */
    private void toMemberManage(boolean isAdd) {
        if (isAdd) { //添加
            Intent intent = new Intent(this, SelectContractListActivity.class);
            intent.putExtra("TYPE","3");
            intent.putExtra("DATA", (Serializable) memberList);
            startActivityForResult(intent, REQUEST_ADD_GROUP_MEMBER);
        } else { //删除
            Intent intent = new Intent(this, SelectContractListActivity.class);
            intent.putExtra("TYPE","3");
            intent.putExtra("DATA", (Serializable) managerList);
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

    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        Intent intent;
        switch (mType) {
            case MethodUrl.CHAT_GROUP_ADMIN:
                switch (tData.get("code") + "") {
                    case "0":
                        if (!UtilTools.empty(tData.get("data") + "")) {
                            Map<String, Object> map = (Map<String, Object>) tData.get("data");
                            if (!UtilTools.empty(map)) {
                                if (!UtilTools.empty(map.get("member") + "")) {
                                    managerList = (List<Map<String, Object>>) map.get("member");
                                    if (managerList != null && managerList.size() > 0) {
                                        List<GroupMember> groupMemberList = new ArrayList<>();
                                        for (Map<String, Object> map1 : managerList) {
                                            GroupMember member = new GroupMember();
                                            member.setName(map1.get("name") + "");
                                            member.setUserId(map1.get("id") + "");
                                            member.setGroupId(groupId);
                                            member.setPortraitUri(map1.get("portrait") + "");
                                            groupMemberList.add(member);
                                        }
                                        memberAdapter.setAllowDeleteMember(true);
                                        memberAdapter.updateListView(groupMemberList);
                                    }

                                }

                                identiType = map.get("administrators") + "";

                                if ((map.get("talk")+"").equals("0")){
                                    talkSwitch.setChecked(false);
                                }else {
                                    talkSwitch.setChecked(true);
                                }
                                if ((map.get("protect")+"").equals("0")){
                                    protectSwitch.setChecked(false);
                                }else {
                                    protectSwitch.setChecked(true);
                                }
                                if ((map.get("join")+"").equals("0")){
                                    joinSwitch.setChecked(false);
                                }else {
                                    joinSwitch.setChecked(true);
                                }


                            }
                        }

                        break;
                    case "1":
                        closeAllActivity();
                        intent = new Intent(GroupChatManagerActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
                        break;
                }

                break;
            case MethodUrl.CHAT_GROUP_MANAGE:
                switch (tData.get("code") + "") {
                    case "0":
                        showToastMsg(tData.get("msg") + "");
                        break;
                    case "1":
                        closeAllActivity();
                        intent = new Intent(GroupChatManagerActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
                        break;
                }
                break;
            case MethodUrl.CHAT_GROUP_ADMIN_ADD:
                switch (tData.get("code")+"") {
                    case "0":
                        showToastMsg(tData.get("msg") + "");
                        getGroupInfoAction();
                        break;
                    case "1":
                        closeAllActivity();
                        intent = new Intent(GroupChatManagerActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case "-1":
                        showToastMsg(tData.get("msg") + "");
                        break;

                }
                break;


            case MethodUrl.CHAT_GROUP_TRANSFER:
                switch (tData.get("code")+"") {
                    case "0":
                        showToastMsg(tData.get("msg") + "");
                        finish();
                        break;
                    case "1":
                        closeAllActivity();
                        intent = new Intent(GroupChatManagerActivity.this, LoginActivity.class);
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
        dealFailInfo(map, mType);
    }


}
