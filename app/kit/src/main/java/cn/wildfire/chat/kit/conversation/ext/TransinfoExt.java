package cn.wildfire.chat.kit.conversation.ext;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle2.activity.TransferMoneyActivity;
import com.lr.biyou.utils.tool.LogUtilDebug;

import cn.wildfire.chat.kit.annotation.ExtContextMenuItem;
import cn.wildfire.chat.kit.conversation.ext.core.ConversationExt;
import cn.wildfirechat.message.TypingMessageContent;
import cn.wildfirechat.model.Conversation;

public class TransinfoExt extends ConversationExt {
    /**
     * @param containerView 扩展view的container
     * @param conversation
     */
    @ExtContextMenuItem(title = "转账")
    public void transInfo(View containerView, Conversation conversation) {
        LogUtilDebug.i("show","会话类型:"+conversation.type);


        TypingMessageContent content = new TypingMessageContent(TypingMessageContent.TYPING_RED);
        messageViewModel.sendMessage(conversation, content);


        Intent intent = new Intent(activity, TransferMoneyActivity.class);
        if (conversation.type == Conversation.ConversationType.Single){
            intent.putExtra("type","1");
            intent.putExtra("id",conversation.target);
            startActivityForResult(intent, 101);
        }else {
            Toast.makeText(activity,"群聊不支持转账功能",Toast.LENGTH_LONG).show();
        }




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
        return R.mipmap.jrmf_ic_zhuanzhang;
    }

    @Override
    public String title(Context context) {
        return "转账";
    }
}
