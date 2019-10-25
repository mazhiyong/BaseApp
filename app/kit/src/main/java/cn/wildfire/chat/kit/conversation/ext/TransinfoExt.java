package cn.wildfire.chat.kit.conversation.ext;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.lr.biyou.R;

import cn.wildfire.chat.kit.annotation.ExtContextMenuItem;
import cn.wildfire.chat.kit.conversation.ext.core.ConversationExt;
import cn.wildfirechat.model.Conversation;

public class TransinfoExt extends ConversationExt {
    /**
     * @param containerView 扩展view的container
     * @param conversation
     */
    @ExtContextMenuItem(title = "转账")
    public void transInfo(View containerView, Conversation conversation) {
        Log.i("show","转账");
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
