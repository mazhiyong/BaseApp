package com.lr.biyou.rongyun.ui.adapter.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lr.biyou.R;
import com.lr.biyou.rongyun.ui.adapter.models.SearchConversationModel;
import com.lr.biyou.rongyun.ui.interfaces.OnChatItemClickListener;
import com.lr.biyou.rongyun.utils.CharacterParser;
import com.lr.biyou.rongyun.utils.ImageLoaderUtils;
import io.rong.imlib.model.SearchConversationResult;

public class SearchConversationViewHolder extends BaseViewHolder<SearchConversationModel> {
    private ImageView portrait;
    private TextView tvName;
    private TextView tvDetail;
    private OnChatItemClickListener listener;
    private SearchConversationModel model;

    public SearchConversationViewHolder(@NonNull View itemView, OnChatItemClickListener l) {
        super(itemView);
        this.listener = l;
        portrait = itemView.findViewById(R.id.iv_portrait);
        tvName = itemView.findViewById(R.id.tv_name);
        tvDetail = itemView.findViewById(R.id.tv_detail);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.OnChatItemClicked(model);
                }
            }
        });
    }

    @Override
    public void update(SearchConversationModel searchConversationModel) {
        model = searchConversationModel;
        SearchConversationResult result = searchConversationModel.getBean();
        tvName.setText(searchConversationModel.getName());
        if (result.getMatchCount() > 1) {
            tvDetail.setText(String.format(itemView.getContext().getString(R.string.seal_search_item_chat_records), result.getMatchCount()));
        } else {
            tvDetail.setText(CharacterParser.getColoredChattingRecord(searchConversationModel.getFilter(), result.getConversation().getLatestMessage(), itemView.getContext()));
        }
        ImageLoaderUtils.displayUserPortraitImage(searchConversationModel.getPortraitUrl(), portrait);
    }


}
