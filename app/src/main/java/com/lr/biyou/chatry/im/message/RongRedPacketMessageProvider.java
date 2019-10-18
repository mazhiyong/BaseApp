package com.lr.biyou.chatry.im.message;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.redpackage.CustomDialog;
import com.lr.biyou.mywidget.redpackage.OnRedPacketDialogClickListener;
import com.lr.biyou.mywidget.redpackage.RedPacketViewHolder;
import com.lr.biyou.ui.moudle2.activity.RedListActivity;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.Map;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;


/**
 * 自定义融云IM消息提供者
 *
 * @author lsy
 */
@ProviderTag(messageContent = RongRedPacketMessage.class, showPortrait = true, showProgress = true, centerInHorizontal = false)
// 会话界面自定义UI注解
public class RongRedPacketMessageProvider extends IContainerItemProvider.MessageProvider<RongRedPacketMessage>  implements RequestView {

    /**
     * 初始化View
     */
    private Context context;
    private RongRedPacketMessage redPacketMessage;

    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.de_customize_message_red_packet, null);
        ViewHolder holder = new ViewHolder();
        holder.message = (TextView) view.findViewById(R.id.textView1);
        holder.status = (TextView) view.findViewById(R.id.textView2);
        holder.view = (View) view.findViewById(R.id.rc_img);
        holder.left_lay = view.findViewById(R.id.left_lay);
        holder.right_lay = view.findViewById(R.id.right_lay);
        holder.iv = view.findViewById(R.id.imageView1);
        view.setTag(holder);
        this.context = context;
        return view;
    }


    @Override
    public void bindView(View view, int i, RongRedPacketMessage rongRedPacketMessage, UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();

        // 更改气泡样式
        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {
            // 消息方向，自己发送的
            holder.view.setBackgroundResource(R.drawable.de_ic_bubble_right);
            holder.right_lay.setVisibility(View.VISIBLE);
            holder.left_lay.setVisibility(View.GONE);
        } else {
            // 消息方向，别人发送的
            holder.view.setBackgroundResource(R.drawable.de_ic_bubble_left);
            holder.right_lay.setVisibility(View.GONE);
            holder.left_lay.setVisibility(View.VISIBLE);
        }
        holder.message.setText(rongRedPacketMessage.getMessage()); // 设置消息内容

        if (rongRedPacketMessage.getType().equals("1")){
            holder.iv.setVisibility(View.VISIBLE);
        }else {
            holder.iv.setVisibility(View.GONE);
        }


        //holder.status.setText(rongRedPacketMessage.getStatus()); // 设置红包状态
    }

    @Override
    public Spannable getContentSummary(RongRedPacketMessage data) {
        return null;
    }

    //点击领取
    @Override
    public void onItemClick(View view, int position, RongRedPacketMessage content, UIMessage message) {
        Intent intent = new Intent(context, RedListActivity.class);
        intent.putExtra("id",content.getId());
        intent.putExtra("type",content.getType());
        context.startActivity(intent);

       /* redPacketMessage = content;
        RequestPresenterImp mRequestPresenterImp = new RequestPresenterImp(this, context);
        Map<String, Object> map = new HashMap<>();
        if (UtilTools.empty(MbsConstans.ACCESS_TOKEN)) {
            MbsConstans.ACCESS_TOKEN = SPUtils.get(context, MbsConstans.ACCESS_TOKEN, "").toString();
        }
        map.put("token", MbsConstans.ACCESS_TOKEN);
        map.put("id", content.getId());
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestPostToMap(mHeaderMap, MethodUrl.CHAT_RED_INFO, map);*/


    }


    @Override
    public void onItemLongClick(View view, int position, RongRedPacketMessage content, UIMessage message) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        if (!UtilTools.empty(tData.get("data"))){
            Map<String,Object> mapData = (Map<String, Object>) tData.get("data");
            if (!UtilTools.empty(mapData)){
                Map<String,Object> mapInfo = (Map<String, Object>) mapData.get("info");
                if (!UtilTools.empty(mapInfo)){
                    if (UtilTools.empty(MbsConstans.RONGYUN_MAP)) {
                        String s = SPUtils.get(context, MbsConstans.SharedInfoConstans.RONGYUN_DATA,"").toString();
                        MbsConstans.RONGYUN_MAP = JSONUtil.getInstance().jsonMap(s);
                    }
                    if ((mapInfo.get("text")+"").equals("等待领取") && !MbsConstans.RONGYUN_MAP.get("id").equals(mapInfo.get("rc_id")+"")){
                        showRedPacketDialog(redPacketMessage);
                    }else {
                        Intent intent = new Intent(context, RedListActivity.class);
                        LogUtilDebug.i("show","redPacketMessage000:"+redPacketMessage.getId());
                        intent.putExtra("id",redPacketMessage.getId());
                        context.startActivity(intent);
                    }
                }
            }
        }
    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

    }

    class ViewHolder {
        LinearLayout left_lay;
        LinearLayout right_lay;
        TextView status;
        TextView message;
        ImageView iv;
        View view;
    }

    private View mRedPacketDialogView;
    private RedPacketViewHolder mRedPacketViewHolder;
    private CustomDialog mRedPacketDialog;

    public void showRedPacketDialog(RongRedPacketMessage entity) {

        mRedPacketDialogView = View.inflate(context, R.layout.dialog_red_packet, null);
        mRedPacketViewHolder = new RedPacketViewHolder(context, mRedPacketDialogView);
        mRedPacketDialog = new CustomDialog(context, mRedPacketDialogView, R.style.custom_dialog);
        mRedPacketDialog.setCancelable(false);


        mRedPacketViewHolder.setData(entity);
        mRedPacketViewHolder.setOnRedPacketDialogClickListener(new OnRedPacketDialogClickListener() {
            @Override
            public void onCloseClick() {
                mRedPacketDialog.dismiss();
            }

            @Override
            public void onOpenClick() {
                //领取红包,调用接口
                //entity.setStatus("已领取");

            }
        });

        mRedPacketDialog.show();
    }

}
