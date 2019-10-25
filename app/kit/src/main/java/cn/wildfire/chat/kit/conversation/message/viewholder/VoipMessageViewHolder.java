package cn.wildfire.chat.kit.conversation.message.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.wildfire.chat.kit.WfcUIKit;
import cn.wildfire.chat.kit.annotation.EnableContextMenu;
import cn.wildfire.chat.kit.annotation.MessageContentType;
import cn.wildfire.chat.kit.annotation.ReceiveLayoutRes;
import cn.wildfire.chat.kit.annotation.SendLayoutRes;
import cn.wildfire.chat.kit.conversation.ConversationFragment;
import cn.wildfire.chat.kit.conversation.message.model.UiMessage;
import cn.wildfirechat.message.CallStartMessageContent;

@MessageContentType(CallStartMessageContent.class)
@ReceiveLayoutRes(resId = R.layout.conversation_item_voip_receive)
@SendLayoutRes(resId = R.layout.conversation_item_voip_send)
@EnableContextMenu
public class VoipMessageViewHolder extends NormalMessageContentViewHolder {
    @BindView(R.id.contentTextView)
    TextView textView;

    public VoipMessageViewHolder(ConversationFragment fragment, RecyclerView.Adapter adapter, View itemView) {
        super(fragment, adapter, itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(UiMessage message) {
        CallStartMessageContent content = (CallStartMessageContent) message.message.content;
        if (content.getStatus() == 0) {
            textView.setText("对方未接听");
        } else if (content.getStatus() == 1) {
            textView.setText("通话中");
        } else {
            String text;
            if (content.getConnectTime() > 0) {
                long duration = (content.getEndTime() - content.getConnectTime()) / 1000;
                if (duration > 3600) {
                    text = String.format("通话时长 %d:%02d:%02d", duration / 3600, (duration % 3600) / 60, (duration % 60));
                } else {
                    text = String.format("通话时长 %02d:%02d", duration / 60, (duration % 60));
                }
            } else {
                text = "对方未接听";
            }
            textView.setText(text);
        }
    }

    @OnClick(R.id.contentTextView)
    public void call(View view) {
        if (((CallStartMessageContent) message.message.content).getStatus() == 1) {
            return;
        }
        WfcUIKit.onCall(fragment.getContext(), message.message.conversation.target, true, false);
    }
}
