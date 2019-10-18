package com.lr.biyou.chatry.ui.adapter.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lr.biyou.R;
import com.lr.biyou.basic.BasicApplication;
import com.lr.biyou.chatry.ui.interfaces.OnMessageRecordClickListener;
import com.lr.biyou.chatry.utils.CharacterParser;
import com.lr.biyou.chatry.utils.ImageLoaderUtils;
import com.lr.biyou.chatry.viewmodel.SearchMessageModel;

import io.rong.imkit.utils.RongDateUtils;

public class SearchMessageViewHolder extends BaseViewHolder<SearchMessageModel> {
    private ImageView ivPortrait;
    private TextView tvChatName;
    private TextView tvContent;
    private TextView tvDate;
    private OnMessageRecordClickListener listener;
    private SearchMessageModel model;

    public SearchMessageViewHolder(@NonNull View itemView, OnMessageRecordClickListener l) {
        super(itemView);
        ivPortrait = itemView.findViewById(R.id.item_iv_record_image);
        tvChatName = itemView.findViewById(R.id.item_tv_chat_name);
        tvContent = itemView.findViewById(R.id.item_tv_chatting_records_detail);
        tvDate = itemView.findViewById(R.id.item_tv_chatting_records_date);
        listener = l;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMessageRecordClick(model);
                }
            }
        });
    }

    @Override
    public void update(SearchMessageModel searchMessageModel) {
        model = searchMessageModel;
        tvChatName.setText(searchMessageModel.getName());
        tvContent.setText(CharacterParser.getColoredChattingRecord(searchMessageModel.getSearch(), searchMessageModel.getBean().getContent(), tvContent.getContext()));
        String sendTime = RongDateUtils.getConversationFormatDate(searchMessageModel.getBean().getSentTime(), BasicApplication.getContext());
        tvDate.setText(sendTime);
        ImageLoaderUtils.displayUserPortraitImage(searchMessageModel.getPortiaitUrl(),ivPortrait);
    }

}
