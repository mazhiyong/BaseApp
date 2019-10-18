package com.lr.biyou.chatry.ui.adapter.viewholders;

import android.view.View;

import androidx.annotation.NonNull;

import com.lr.biyou.R;
import com.lr.biyou.chatry.db.model.GroupEntity;
import com.lr.biyou.chatry.ui.adapter.models.ListItemModel;
import com.lr.biyou.chatry.ui.view.UserInfoItemView;
import com.lr.biyou.chatry.utils.ImageLoaderUtils;

public class ForwardSelectedViewHolder extends BaseItemViewHolder<ListItemModel> {
    private final UserInfoItemView userInfoUiv;
    private View.OnClickListener listener;

    public ForwardSelectedViewHolder(@NonNull View itemView) {
        super(itemView);
        userInfoUiv = itemView.findViewById(R.id.uiv_userinfo);
        userInfoUiv.setDividerVisibility(View.VISIBLE);
        userInfoUiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
    }


    @Override
    public void setOnClickItemListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void update(ListItemModel model) {
        if (model != null) {
            if (model.getItemView().getType() == ListItemModel.ItemView.Type.GROUP ) {
                GroupEntity group = (GroupEntity)model.getData();
              userInfoUiv.setName(model.getDisplayName() + "（" + group.getMemberCount()+"）");
            } else if (model.getItemView().getType() == ListItemModel.ItemView.Type.GROUP ) {
                userInfoUiv.setName(model.getDisplayName());
            }
            ImageLoaderUtils.displayGroupPortraitImage(model.getPortraitUrl(), userInfoUiv.getHeaderImageView());
        }
    }
}
