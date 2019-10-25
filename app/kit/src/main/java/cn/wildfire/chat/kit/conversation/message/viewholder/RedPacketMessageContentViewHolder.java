package cn.wildfire.chat.kit.conversation.message.viewholder;

import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.lqr.emoji.MoonUtils;
import com.lr.biyou.R;

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
    @BindView(R.id.contentTextView)
    TextView contentTextView;

    public RedPacketMessageContentViewHolder(ConversationFragment fragment, RecyclerView.Adapter adapter, View itemView) {
        super(fragment, adapter, itemView);
    }

    @Override
    public void onBind(UiMessage message) {
        MoonUtils.identifyFaceExpression(fragment.getContext(), contentTextView, ((RedPacketMessageContent) message.message.content).getContent(), ImageSpan.ALIGN_BOTTOM);


        String cid =((RedPacketMessageContent) message.message.content).cid;
        String redPackType =((RedPacketMessageContent) message.message.content).redPackType;

        Log.i("show","cid***:"+cid);
        Log.i("show","redPackType***:"+redPackType);

     /*   contentTextView.setMovementMethod(new LinkTextViewMovementMethod(new LinkClickListener() {
            @Override
            public boolean onLinkClick(String link) {
                WfcWebViewActivity.loadUrl(fragment.getContext(), "", link);
                return true;
            }
        }));*/
    }

    @OnClick(R.id.contentTextView)
    public void onClickTest(View view) {
        Toast.makeText(fragment.getContext(), "onTextMessage click: " + ((RedPacketMessageContent) message.message.content).getContent(), Toast.LENGTH_SHORT).show();
    }


}
