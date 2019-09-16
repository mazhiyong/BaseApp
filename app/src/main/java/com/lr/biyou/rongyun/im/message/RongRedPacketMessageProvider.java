package com.lr.biyou.rongyun.im.message;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.mywidget.redpackage.CustomDialog;
import com.lr.biyou.mywidget.redpackage.OnRedPacketDialogClickListener;
import com.lr.biyou.mywidget.redpackage.RedPacketViewHolder;
import com.lr.biyou.ui.moudle2.activity.RedListActivity;

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
public class RongRedPacketMessageProvider extends IContainerItemProvider.MessageProvider<RongRedPacketMessage> {

    /**
     * 初始化View
     */
    private Context context;

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
        holder.status.setText(rongRedPacketMessage.getStatus()); // 设置红包状态
    }

    @Override
    public Spannable getContentSummary(RongRedPacketMessage data) {
        return null;
    }

    //点击领取
    @Override
    public void onItemClick(View view, int position, RongRedPacketMessage content, UIMessage message) {
        /*if (content.getStatus().equals("待领取")) {
            showRedPacketDialog(content);
        } else {
            Toast.makeText(context, "已领取", Toast.LENGTH_LONG).show();
        }*/

        Intent intent = new Intent(context, RedListActivity.class);
        intent.putExtra("id",content.getId());
        context.startActivity(intent);

    }


    @Override
    public void onItemLongClick(View view, int position, RongRedPacketMessage content, UIMessage message) {

    }

    class ViewHolder {
        LinearLayout left_lay;
        LinearLayout right_lay;
        TextView status;
        TextView message;
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
