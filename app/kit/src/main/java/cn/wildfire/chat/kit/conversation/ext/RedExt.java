package cn.wildfire.chat.kit.conversation.ext;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.lr.biyou.R;

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


        TypingMessageContent content = new TypingMessageContent(TypingMessageContent.TYPING_RED);
        messageViewModel.sendMessage(conversation, content);


        RedPacketMessageContent messageContent = new RedPacketMessageContent();
        messageContent.setContent("大吉大利,恭喜发财");
        messageContent.cid = "A0002";
        messageContent.redPackType = "1";
     /*   JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("cid","XGIABGA");
            jsonObj.put("redPackType", "1");
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }
        messageContent.extra = jsonObj.toString();
        Log.i("show","jsonObj:"+jsonObj.toString());
*/
        messageViewModel.sendRedMessage(conversation,messageContent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {


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
