package cn.wildfire.chat.kit.conversation.message.viewholder;

import android.content.Intent;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lqr.emoji.MoonUtils;
import com.lr.biyou.R;
import com.lr.biyou.ui.moudle2.activity.RedListActivity;
import com.lr.biyou.utils.tool.LogUtilDebug;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.kit.annotation.EnableContextMenu;
import cn.wildfire.chat.kit.annotation.MessageContentType;
import cn.wildfire.chat.kit.annotation.ReceiveLayoutRes;
import cn.wildfire.chat.kit.annotation.SendLayoutRes;
import cn.wildfire.chat.kit.conversation.ConversationFragment;
import cn.wildfire.chat.kit.conversation.message.model.UiMessage;
import cn.wildfirechat.message.RedPacketMessageContent;

@MessageContentType(RedPacketMessageContent.class)
@SendLayoutRes(resId = R.layout.conversation_item_redpacket_send)
@ReceiveLayoutRes(resId = R.layout.conversation_item_redpacket_receive)
@EnableContextMenu
public class RedPacketMessageContentViewHolder extends NormalMessageContentViewHolder {
    @NonNull
    protected ConversationFragment fragment;

    @BindView(R.id.contentTextView)
    TextView contentTextView;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.red_tv)
    TextView redTv;

    String redId;
    String redPackType;

    public RedPacketMessageContentViewHolder(ConversationFragment fragment, RecyclerView.Adapter adapter, View itemView) {
        super(fragment, adapter, itemView);
        this.fragment = fragment;
    }

    @Override
    public void onBind(UiMessage message) {
        MoonUtils.identifyFaceExpression(fragment.getContext(), contentTextView, ((RedPacketMessageContent) message.message.content).getContent(), ImageSpan.ALIGN_BOTTOM);
        redId = ((RedPacketMessageContent) message.message.content).cid;
        redPackType = ((RedPacketMessageContent) message.message.content).redPackType;
        if (redPackType.equals("1")){
            redTv.setText("币友红包");
            imageView.setVisibility(View.VISIBLE);
        }else {
            redTv.setText("币友转账");
            imageView.setVisibility(View.GONE);
        }


        LogUtilDebug.i("show", "cid***:" + redId);
        LogUtilDebug.i("show", "redPackType***:" + redPackType);

    }

    @OnClick(R.id.contentTextView)
    public void onClickTest(View view) {
        Intent intent = new Intent(fragment.getContext(), RedListActivity.class);
        intent.putExtra("id",redId);
        intent.putExtra("type",redPackType);
        fragment.getContext().startActivity(intent);

    }


}
