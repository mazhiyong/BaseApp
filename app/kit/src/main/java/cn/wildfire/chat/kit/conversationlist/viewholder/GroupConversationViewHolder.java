package cn.wildfire.chat.kit.conversationlist.viewholder;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.lr.biyou.R;

import cn.wildfire.chat.kit.ChatManagerHolder;
import cn.wildfire.chat.kit.annotation.ConversationInfoType;
import cn.wildfire.chat.kit.annotation.EnableContextMenu;
import cn.wildfirechat.model.Conversation;
import cn.wildfirechat.model.ConversationInfo;
import cn.wildfirechat.model.GroupInfo;

@ConversationInfoType(type = Conversation.ConversationType.Group, line = 0)
@EnableContextMenu
public class GroupConversationViewHolder extends ConversationViewHolder {

    public GroupConversationViewHolder(Fragment fragment, RecyclerView.Adapter adapter, View itemView) {
        super(fragment, adapter, itemView);
    }

    @Override
    protected void onBindConversationInfo(ConversationInfo conversationInfo) {
        GroupInfo groupInfo = ChatManagerHolder.gChatManager.getGroupInfo(conversationInfo.conversation.target, false);
        String name;
        String portrait;
        if (groupInfo != null) {
            name = groupInfo.name;
            portrait = groupInfo.portrait;
        } else {
            name = "Group<" + conversationInfo.conversation.target + ">";
            portrait = null;
        }
        Glide
                .with(fragment)
                .load(portrait)
                .placeholder(R.mipmap.ic_group_cheat)
                .transforms(new CenterCrop(), new RoundedCorners(10))
                .into(portraitImageView);
        nameTextView.setText(name);
    }

}
