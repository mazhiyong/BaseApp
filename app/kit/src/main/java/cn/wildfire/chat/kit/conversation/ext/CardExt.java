package cn.wildfire.chat.kit.conversation.ext;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.lr.biyou.R;

import cn.wildfire.chat.kit.annotation.ExtContextMenuItem;
import cn.wildfire.chat.kit.conversation.ext.core.ConversationExt;
import cn.wildfirechat.model.Conversation;

public class CardExt extends ConversationExt {
    /**
     * @param containerView 扩展view的container
     * @param conversation
     */
    @ExtContextMenuItem(title = "名片")
    public void card(View containerView, Conversation conversation) {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");//无类型限制
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent, 100);
//        TypingMessageContent content = new TypingMessageContent(TypingMessageContent.TYPING_FILE);
//        messageViewModel.sendMessage(conversation, content);

          Log.i("show","名片");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


    }

    @Override
    public int priority() {
        return 100;
    }

    @Override
    public int iconResId() {
        return R.mipmap.rc_contact_plugin_icon_unpressed;
    }

    @Override
    public String title(Context context) {
        return "名片";
    }
}
