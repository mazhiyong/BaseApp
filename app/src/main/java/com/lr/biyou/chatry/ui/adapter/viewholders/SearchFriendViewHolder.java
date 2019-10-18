package com.lr.biyou.chatry.ui.adapter.viewholders;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lr.biyou.R;
import com.lr.biyou.chatry.db.model.FriendDetailInfo;
import com.lr.biyou.chatry.db.model.FriendShipInfo;
import com.lr.biyou.chatry.ui.adapter.models.SearchFriendModel;
import com.lr.biyou.chatry.ui.interfaces.OnContactItemClickListener;
import com.lr.biyou.chatry.utils.CharacterParser;
import com.lr.biyou.chatry.utils.ImageLoaderUtils;

public class SearchFriendViewHolder extends BaseViewHolder<SearchFriendModel> {
    private TextView tvNickName;
    private TextView tvDisplayName;
    private ImageView portrait;
    private View llDescription;
    private OnContactItemClickListener listener;
    private FriendShipInfo friendShipInfo;

    public SearchFriendViewHolder(@NonNull View itemView, OnContactItemClickListener l) {
        super(itemView);
        this.listener = l;
        portrait = itemView.findViewById(R.id.iv_portrait);
        tvDisplayName = itemView.findViewById(R.id.tv_name);
        tvNickName = itemView.findViewById(R.id.tv_detail);
        llDescription = itemView.findViewById(R.id.ll_description);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemContactClick(friendShipInfo);
                }
            }
        });
    }

    @Override
    public void update(SearchFriendModel searchFriendModel) {

        friendShipInfo = searchFriendModel.getBean();
        FriendDetailInfo info = friendShipInfo.getUser();

        if (!TextUtils.isEmpty(friendShipInfo.getDisplayName())) {
            llDescription.setVisibility(View.VISIBLE);

            if(searchFriendModel.getAliseStart() != -1){
                tvDisplayName.setText(CharacterParser.getSpannable(friendShipInfo.getDisplayName(), searchFriendModel.getAliseStart(), searchFriendModel.getAliseEnd()));
            } else {
                tvDisplayName.setText(friendShipInfo.getDisplayName());
            }

            if (searchFriendModel.getNameStart() != -1){
                tvNickName.setText(CharacterParser.getSpannable(info.getNickname(), searchFriendModel.getNameStart(), searchFriendModel.getNameEnd()));
            } else {
                tvNickName.setText(info.getNickname());
            }
        } else {
            if(searchFriendModel.getNameStart() != -1){
                tvDisplayName.setText(CharacterParser.getSpannable(info.getNickname(), searchFriendModel.getNameStart(), searchFriendModel.getNameEnd()));
            } else {
                tvDisplayName.setText(info.getNickname());
            }
            llDescription.setVisibility(View.GONE);
        }
        ImageLoaderUtils.displayUserPortraitImage(info.getPortraitUri(),portrait);

    }


}
