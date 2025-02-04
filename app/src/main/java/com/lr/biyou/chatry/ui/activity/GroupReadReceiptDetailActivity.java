package com.lr.biyou.chatry.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.lr.biyou.R;
import com.lr.biyou.chatry.common.IntentExtra;
import com.lr.biyou.chatry.db.model.UserInfo;
import com.lr.biyou.chatry.model.GroupMember;
import com.lr.biyou.chatry.model.Resource;
import com.lr.biyou.chatry.ui.adapter.GridGroupMemberAdapter;
import com.lr.biyou.chatry.ui.view.SealTitleBar;
import com.lr.biyou.chatry.ui.widget.ReadReceiptViewPager;
import com.lr.biyou.chatry.utils.log.SLog;
import com.lr.biyou.chatry.viewmodel.GroupReadReceiptViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imkit.utils.RongDateUtils;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * 群已读回执
 */
public class GroupReadReceiptDetailActivity extends TitleBaseActivity {
    private final String TAG = "GroupReadReceiptDetailActivity";
    /**
     * 默认消息区显示行数
     */
    private final int DEFAULT_MESSAGE_SHOW_LINE = 4;

    private Message targetMessage;

    private TextView sendUserNameTv;
    private TextView sendMsgTv;
    private TextView sendTimeTv;

    private TextView readPromptTv;
    private GridView readMemberGv;
    private GridGroupMemberAdapter readMemberAdapter;
    TextView readTabTextTv;
    TextView readUnderLineTv;

    private TextView unReadPromptTv;
    private GridView unReadMemberGv;
    private GridGroupMemberAdapter unReadMemberAdapter;
    TextView unReadTabTextTv;
    TextView unReadUnderLineTv;

    private GroupReadReceiptViewModel receiptViewModel;
    private ReadReceiptViewPager memberViewPager;
    private List<View> pageContainer = new ArrayList<>();

    // 消息区是否展开
    private boolean isMsgExpend = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SealTitleBar titleBar = getTitleBar();
        titleBar.setTitle(R.string.seal_conversation_read_receipt_detail);

        Intent intent = getIntent();
        if (intent == null) {
            SLog.e(TAG, "intent is null, to finish.");
            finish();
            return;
        }

        targetMessage = (Message) intent.getParcelableExtra(IntentExtra.PARCEL_MESSAGE);

        if (targetMessage == null) {
            SLog.e(TAG, "message is null, to finish.");
            finish();
            return;
        }

        setContentView(R.layout.conversation_activity_group_read_receipt_detail);

        initView();
        initViewModel();
    }

    private void initView() {
        // 发送信息人名称
        sendUserNameTv = findViewById(R.id.conversation_tv_read_send_user_name);

        // 发送消息时间
        sendTimeTv = findViewById(R.id.conversation_tv_read_send_time);
        sendTimeTv.setText(RongDateUtils.getConversationFormatDate(targetMessage.getSentTime(), this));

        // 发送消息内容
        sendMsgTv = findViewById(R.id.conversation_tv_read_send_message);
        TextMessage textMessage = (TextMessage)targetMessage.getContent();
        SpannableStringBuilder spannable = new SpannableStringBuilder(textMessage.getContent());
        AndroidEmoji.ensure(spannable);
        sendMsgTv.setText(spannable);

        // 展开消息区按钮
        ImageView msgExpandIv = findViewById(R.id.conversation_iv_read_msg_expand);
        msgExpandIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMsgExpend == false) { //未展开
                    isMsgExpend = true;
                    msgExpandIv.setImageDrawable(getResources().getDrawable(R.drawable.conversation_ic_read_receipt_up_arrow));
                    sendMsgTv.setMaxLines(Integer.MAX_VALUE);
                } else { //展开
                    isMsgExpend = false;
                    msgExpandIv.setImageDrawable(getResources().getDrawable(R.drawable.conversation_ic_read_receipt_down_arrow));
                    sendMsgTv.setMaxLines(DEFAULT_MESSAGE_SHOW_LINE);
                }
            }
        });

        // 根据消息显示行数隐藏消息扩展按钮
        sendMsgTv.post(new Runnable() {
            @Override
            public void run() {
                msgExpandIv.setVisibility(sendMsgTv.getLineCount() > DEFAULT_MESSAGE_SHOW_LINE ? View.VISIBLE : View.GONE);
                sendMsgTv.setMaxLines(DEFAULT_MESSAGE_SHOW_LINE);
            }
        });

        // 已读未读成员页
        memberViewPager = findViewById(R.id.conversation_vp_read_member_page);

        // 已读标签
        readUnderLineTv = findViewById(R.id.conversation_tv_read_tab_underline_read);
        readTabTextTv = findViewById(R.id.conversation_tv_read_tab_read);
        readTabTextTv.setText(getString(R.string.seal_conversation_read_receipt_read_persons_format, 0));
        readTabTextTv.setOnClickListener(v -> memberViewPager.setCurrentItem(0, false));
        setTabSelectedBg(readUnderLineTv,readTabTextTv, true);

        // 未读标签
        unReadUnderLineTv = findViewById(R.id.conversation_tv_read_tab_underline_unread);
        unReadTabTextTv = findViewById(R.id.conversation_tv_read_tab_unread);
        unReadTabTextTv.setText(getString(R.string.seal_conversation_read_receipt_unread_persons_format, 0));
        unReadTabTextTv.setOnClickListener(v -> memberViewPager.setCurrentItem(1, false));
        setTabSelectedBg(unReadUnderLineTv, unReadTabTextTv, false);

        // 未读页面
        View unreadView = LayoutInflater.from(this).inflate(R.layout.conversation_layout_read_receipt_member, null);
        unReadPromptTv = unreadView.findViewById(R.id.conversation_tv_read_member_prompt_text);
        unReadPromptTv.setText(R.string.seal_conversation_read_receipt_no_person_unread);
        unReadMemberGv = unreadView.findViewById(R.id.conversation_gv_read_member);
        unReadMemberAdapter = new GridGroupMemberAdapter(this, 0);
        unReadMemberGv.setAdapter(unReadMemberAdapter);

        // 已读页面
        View readView = LayoutInflater.from(this).inflate(R.layout.conversation_layout_read_receipt_member, null);
        readPromptTv = readView.findViewById(R.id.conversation_tv_read_member_prompt_text);
        readPromptTv.setText(R.string.seal_conversation_read_receipt_no_person_read);
        readMemberGv = readView.findViewById(R.id.conversation_gv_read_member);
        readMemberAdapter = new GridGroupMemberAdapter(this, 0);
        readMemberGv.setAdapter(readMemberAdapter);

        // 成员点击事件
        GridGroupMemberAdapter.OnItemClickedListener memberClickedListener = new GridGroupMemberAdapter.OnItemClickedListener() {
            @Override
            public void onAddOrDeleteMemberClicked(boolean isAdd) {
            }

            @Override
            public void onMemberClicked(GroupMember groupMember) {
                showMemberInfo(groupMember);
            }
        };
        readMemberAdapter.setOnItemClickedListener(memberClickedListener);
        unReadMemberAdapter.setOnItemClickedListener(memberClickedListener);

        // 添加已读和未读页面
        pageContainer.add(readView);
        pageContainer.add(unreadView);

        // 设置页面适配
        memberViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return pageContainer.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(pageContainer.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                (container).addView(pageContainer.get(position));
                return pageContainer.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }
        });

        // 设置页面切换监听
        memberViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setTabSelectedBg(unReadUnderLineTv, unReadTabTextTv,false);
                    setTabSelectedBg(readUnderLineTv, readTabTextTv,true);
                } else if (position == 1) {
                    setTabSelectedBg(unReadUnderLineTv, unReadTabTextTv,true);
                    setTabSelectedBg(readUnderLineTv, readTabTextTv,false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 切换标签颜色
     *
     * @param line
     * @param text
     * @param isSelected
     */
    private void setTabSelectedBg(TextView line, TextView text, boolean isSelected) {
        if(isSelected){
            line.setBackgroundColor(getResources().getColor(R.color.text_blue));
            line.setHeight(6);
            text.setTextColor(getResources().getColor(R.color.text_blue));
        }else {
            line.setBackgroundColor(getResources().getColor(R.color.common_divider));
            line.setHeight(1);
            text.setTextColor(getResources().getColor(R.color.text_black));
        }
    }

    private void initViewModel() {
        receiptViewModel = ViewModelProviders.of(this).get(GroupReadReceiptViewModel.class);
        receiptViewModel.getGroupMemberList().observe(this, new Observer<Resource<List<GroupMember>>>() {
            @Override
            public void onChanged(Resource<List<GroupMember>> resource) {
                if (resource.data != null) {
                    updateReadGroupMemberList(resource.data);
                }
            }
        });

        receiptViewModel.getUserInfo().observe(this, new Observer<Resource<UserInfo>>() {
            @Override
            public void onChanged(Resource<UserInfo> resource) {
                if (resource.data != null) {
                    updateSendMessageUserInfo(resource.data);
                }
            }
        });

        // 获取群组成员列表
        String groupId = targetMessage.getTargetId();
        receiptViewModel.requestGroupMemberList(groupId);

        // 请求发送者的信息
        receiptViewModel.requestUserInfo(targetMessage.getSenderUserId());
    }

    /**
     * 更新群组已读和未读成员
     */
    private void updateReadGroupMemberList(List<GroupMember> groupMemberList) {
        // 未读成员列表
        List<GroupMember> unReadMemberList = new ArrayList<>();
        // 已读成员列表
        List<GroupMember> readMemberList = new ArrayList<>();
        // 获取消息中已读成员列表
        HashMap<String, Long> respondUserIdMap = targetMessage.getReadReceiptInfo().getRespondUserIdList();
        List<Map.Entry<String, Long>> respondUserIdList = new ArrayList<>(respondUserIdMap.entrySet());
        // 通过已读时间进行排序
        Collections.sort(respondUserIdList, (firstMapEntry, secondMapEntry) ->
                firstMapEntry.getValue().compareTo(secondMapEntry.getValue()));

        List<String> readMemberIdList = new ArrayList<>(respondUserIdMap.keySet());
        // 自己的id
        String currentUserId = RongIM.getInstance().getCurrentUserId();

        List<GroupMember> tempMemberList = new ArrayList<>(groupMemberList);

        for(String readMemberId : readMemberIdList) {
            for (GroupMember groupMember : tempMemberList) {
                // 已读成员
                if (readMemberId.equals(groupMember.getUserId())) {
                    readMemberList.add(groupMember);
                    break;
                }
            }
            // 移除掉已读成员
            tempMemberList.removeAll(readMemberList);
        }
        // 从已读以外成员中排除掉自己
        GroupMember memberMe = null;
        for (GroupMember groupMember : tempMemberList) {
            if (currentUserId.equals(groupMember.getUserId())) {
                memberMe = groupMember;
                break;
            }
        }
        tempMemberList.remove(memberMe);

        // 除了自己和已读成员剩下为未读成员
        unReadMemberList.addAll(tempMemberList);

        // 更新未读列表
        if(unReadMemberList.size() == 0){
            unReadPromptTv.setVisibility(View.VISIBLE);
            unReadMemberGv.setVisibility(View.GONE);
        }else {
            unReadPromptTv.setVisibility(View.GONE);
            unReadMemberAdapter.updateListView(unReadMemberList);
            unReadMemberGv.setVisibility(View.VISIBLE);
        }

        // 更新已读列表
        if(readMemberList.size() == 0){
            readPromptTv.setVisibility(View.VISIBLE);
            readMemberGv.setVisibility(View.GONE);
        }else {
            readPromptTv.setVisibility(View.GONE);
            readMemberAdapter.updateListView(readMemberList);
            readMemberGv.setVisibility(View.VISIBLE);
        }

        // 更新标签人数
        unReadTabTextTv.setText(getString(R.string.seal_conversation_read_receipt_unread_persons_format, unReadMemberList.size()));
        readTabTextTv.setText(getString(R.string.seal_conversation_read_receipt_read_persons_format, readMemberList.size()));
    }

    /**
     * 更新发送者信息
     *
     * @param userInfo
     */
    private void updateSendMessageUserInfo(UserInfo userInfo) {
        sendUserNameTv.setText(userInfo.getName());
    }

    /**
     * 显示成员信息
     *
     * @param groupMember
     */
    private void showMemberInfo(GroupMember groupMember) {
       /* Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra(IntentExtra.STR_TARGET_ID, groupMember.getGroupId());
        Group groupInfo = RongUserInfoManager.getInstance().getGroupInfo(targetMessage.getTargetId());
        if (groupInfo != null) {
            intent.putExtra(IntentExtra.STR_GROUP_NAME, groupInfo.getName());
        }
        startActivity(intent);*/
    }
}
