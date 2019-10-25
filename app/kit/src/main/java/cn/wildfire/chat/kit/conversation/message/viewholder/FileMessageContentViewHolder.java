package cn.wildfire.chat.kit.conversation.message.viewholder;

import android.content.ComponentName;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lr.biyou.R;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.wildfire.chat.kit.annotation.EnableContextMenu;
import cn.wildfire.chat.kit.annotation.LayoutRes;
import cn.wildfire.chat.kit.annotation.MessageContentType;
import cn.wildfire.chat.kit.conversation.ConversationFragment;
import cn.wildfire.chat.kit.conversation.message.model.UiMessage;
import cn.wildfire.chat.kit.third.utils.UIUtils;
import cn.wildfire.chat.kit.utils.FileUtils;
import cn.wildfire.chat.kit.widget.BubbleImageView;
import cn.wildfirechat.message.FileMessageContent;
import cn.wildfirechat.message.core.MessageStatus;

@MessageContentType(FileMessageContent.class)
@LayoutRes(resId = R.layout.conversation_item_file_send)
@EnableContextMenu
public class FileMessageContentViewHolder extends MediaMessageContentViewHolder {

    @BindView(R.id.imageView)
    BubbleImageView imageView;
    private FileMessageContent fileMessageContent;

    public FileMessageContentViewHolder(ConversationFragment fragment, RecyclerView.Adapter adapter, View itemView) {
        super(fragment, adapter, itemView);
    }

    @Override
    public void onBind(UiMessage message) {
        super.onBind(message);
        fileMessageContent = (FileMessageContent) message.message.content;
        if (message.message.status == MessageStatus.Sending || message.isDownloading) {
            imageView.setPercent(message.progress);
            imageView.setProgressVisible(true);
            imageView.showShadow(false);
        } else {
            imageView.setProgressVisible(false);
            imageView.showShadow(false);
        }
        Glide.with(fragment).load(R.mipmap.ic_file)
                .apply(new RequestOptions().override(UIUtils.dip2Px(150), UIUtils.dip2Px(150)).centerCrop()).into(imageView);
    }

    @OnClick(R.id.imageView)
    public void onClick(View view) {
        if (message.isDownloading) {
            return;
        }
        File file = messageViewModel.mediaMessageContentFile(message);
        if (file == null) {
            return;
        }

        if (file.exists()) {
            Intent intent = FileUtils.getViewIntent(fragment.getContext(), file);
            ComponentName cn = intent.resolveActivity(fragment.getContext().getPackageManager());
            if (cn == null) {
                Toast.makeText(fragment.getContext(), "找不到能打开此文件的应用", Toast.LENGTH_SHORT).show();
                return;
            }
            fragment.startActivity(intent);
        } else {
            messageViewModel.downloadMedia(message, file);
        }
    }
}
