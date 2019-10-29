package cn.wildfire.chat.kit.conversation.ext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle2.activity.RedMoneyActivity;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.UtilTools;

import cn.wildfire.chat.kit.annotation.ExtContextMenuItem;
import cn.wildfire.chat.kit.conversation.ext.core.ConversationExt;
import cn.wildfirechat.message.RedPacketMessageContent;
import cn.wildfirechat.message.TypingMessageContent;
import cn.wildfirechat.model.Conversation;

import static android.app.Activity.RESULT_OK;

public class RedExt extends ConversationExt {
    /**
     * @param containerView 扩展view的container
     * @param conversation
     */
    @ExtContextMenuItem(title = "红包")
    public void redPacket(View containerView, Conversation conversation) {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");//无类型限制
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(intent, 100);
//        TypingMessageContent content = new TypingMessageContent(TypingMessageContent.TYPING_FILE);
//        messageViewModel.sendMessage(conversation, content);

        LogUtilDebug.i("show","会话类型:"+conversation.type);
        TypingMessageContent content = new TypingMessageContent(TypingMessageContent.TYPING_RED);
        messageViewModel.sendMessage(conversation, content);

        Intent intent = new Intent(activity, RedMoneyActivity.class);
        intent.putExtra("id",conversation.target);
        if (conversation.type == Conversation.ConversationType.Single){
            intent.putExtra("type","1");
        }else {
            intent.putExtra("type","2");
        }


        startActivityForResult(intent, 101);




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

                    Bundle bundle = data.getExtras();
                    if (bundle != null){
                        String cid = bundle.get("red_id")+"";
                        String content ;
                        if (UtilTools.empty(bundle.get("text"))){
                            content = "大吉大利,恭喜发财";
                        }else {
                            content = bundle.get("text")+"";
                        }

                        RedPacketMessageContent messageContent = new RedPacketMessageContent();
                        messageContent.setContent(content);
                        messageContent.cid = cid;
                        messageContent.redPackType = "1";
                        messageViewModel.sendRedMessage(conversation,messageContent);
                    }



        }


    }

    @Override
    public int priority() {
        return 100;
    }

    @Override
    public int iconResId() {
        return R.mipmap._ic_hongbao;
    }

    @Override
    public String title(Context context) {
        return "红包";
    }
}
