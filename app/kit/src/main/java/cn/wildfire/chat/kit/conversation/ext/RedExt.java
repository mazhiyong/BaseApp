package cn.wildfire.chat.kit.conversation.ext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lr.biyou.R;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mvp.presenter.RequestPresenterImp;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.ui.moudle2.activity.RedMoneyActivity;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.wildfire.chat.kit.annotation.ExtContextMenuItem;
import cn.wildfire.chat.kit.conversation.ext.core.ConversationExt;
import cn.wildfirechat.message.RedPacketMessageContent;
import cn.wildfirechat.model.Conversation;

import static android.app.Activity.RESULT_OK;

public class RedExt extends ConversationExt implements RequestView {
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
       /* TypingMessageContent content = new TypingMessageContent(TypingMessageContent.TYPING_RED);
        messageViewModel.sendMessage(conversation, content);*/

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
                        //群聊红包
                        if (conversation.type == Conversation.ConversationType.Group){
                            setGroupMsgAction(conversation.target,content);
                        }

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
    RequestPresenterImp mRequestPresenterImp;
    private void setGroupMsgAction(String targetId,String msgContent) {
        if (mRequestPresenterImp == null){
            mRequestPresenterImp = new RequestPresenterImp(this,activity);
        }
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = com.lr.biyou.utils.tool.SPUtils.get(activity, MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
        }
        if (UtilTools.empty(MbsConstans.RONGYUN_MAP)){
            String s = SPUtils.get(activity, MbsConstans.SharedInfoConstans.RONGYUN_DATA,"").toString();
            MbsConstans.RONGYUN_MAP = JSONUtil.getInstance().jsonMap(s);
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("group_id", targetId);
        Map<String,String> mapContent = new HashMap<>();
        mapContent.put("FormID",targetId);
        mapContent.put("FormuserID",MbsConstans.RONGYUN_MAP.get("id")+"");
        Random random = new Random();
        int num = random.nextInt(99)%(99-10+1) + 10;
        mapContent.put("msgID",String.valueOf(System.currentTimeMillis())+num);
        mapContent.put("time",String.valueOf(System.currentTimeMillis()));
        mapContent.put("Content",msgContent);
        mapContent.put("mentionedType","0");
        map.put("content",JSONUtil.getInstance().objectToJson(mapContent));
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_ROBOT_SEND_NEWS, map);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

    }
}
