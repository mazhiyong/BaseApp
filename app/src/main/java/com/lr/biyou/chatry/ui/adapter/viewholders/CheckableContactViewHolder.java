package com.lr.biyou.chatry.ui.adapter.viewholders;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lr.biyou.R;
import com.lr.biyou.chatry.db.model.FriendDetailInfo;
import com.lr.biyou.chatry.db.model.FriendShipInfo;
import com.lr.biyou.chatry.ui.adapter.models.CheckableContactModel;
import com.lr.biyou.chatry.ui.interfaces.OnCheckContactClickListener;
import com.lr.biyou.chatry.ui.widget.SelectableRoundedImageView;
import com.lr.biyou.chatry.utils.ImageLoaderUtils;

public class CheckableContactViewHolder extends CheckableBaseViewHolder<CheckableContactModel<FriendShipInfo>> {

    private TextView nameTextView;
    private SelectableRoundedImageView protraitImageView;
    private OnCheckContactClickListener checkableItemClickListener;
    private CheckableContactModel<FriendShipInfo> model;
    private ImageView checkBox;


    public CheckableContactViewHolder(@NonNull View itemView, OnCheckContactClickListener listener) {
        super(itemView);
        checkableItemClickListener = listener;
        protraitImageView = itemView.findViewById(R.id.iv_portrait);
        nameTextView = itemView.findViewById(R.id.tv_friendname);
        checkBox = itemView.findViewById(R.id.cb_select);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkableItemClickListener.onContactContactClick(model);
            }
        });
    }

    @Override
    public void update(CheckableContactModel<FriendShipInfo> friendShipInfoCheckableContactModel) {
        model = friendShipInfoCheckableContactModel;
        FriendShipInfo friendShipInfo = friendShipInfoCheckableContactModel.getBean();
        FriendDetailInfo info = friendShipInfo.getUser();
        nameTextView.setText(TextUtils.isEmpty(friendShipInfo.getDisplayName()) ? info.getNickname() : friendShipInfo.getDisplayName());
        ImageLoaderUtils.displayUserPortraitImage(info.getPortraitUri(), protraitImageView);
        updateCheck(checkBox, friendShipInfoCheckableContactModel.getCheckType());
    }

}
