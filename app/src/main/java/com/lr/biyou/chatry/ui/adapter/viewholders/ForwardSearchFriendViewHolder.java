package com.lr.biyou.chatry.ui.adapter.viewholders;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lr.biyou.R;
import com.lr.biyou.chatry.db.model.FriendDetailInfo;
import com.lr.biyou.chatry.db.model.FriendShipInfo;
import com.lr.biyou.chatry.ui.adapter.models.CheckType;
import com.lr.biyou.chatry.ui.adapter.models.SearchFriendModel;
import com.lr.biyou.chatry.ui.interfaces.OnContactItemClickListener;
import com.lr.biyou.chatry.utils.CharacterParser;
import com.lr.biyou.chatry.utils.ImageLoaderUtils;

public class ForwardSearchFriendViewHolder extends ForwardCheckViewHolder<SearchFriendModel> {
    private TextView tvDisplayName;
    private TextView tvNickName;
    private ImageView portrait;
    private View llDescription;
    private OnContactItemClickListener listener;
    private CheckBox checkBox;
    private SearchFriendModel searchFriendModel;

    public ForwardSearchFriendViewHolder(@NonNull View itemView, OnContactItemClickListener l) {
        super(itemView);
        this.listener = l;
        portrait = itemView.findViewById(R.id.iv_portrait);
        tvDisplayName = itemView.findViewById(R.id.tv_name);
        tvNickName = itemView.findViewById(R.id.tv_detail);
        llDescription = itemView.findViewById(R.id.ll_description);
        checkBox = itemView.findViewById(R.id.cb_select);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击高边状态
                if (listener != null && searchFriendModel != null) {
                    if (searchFriendModel.getCheckType() != CheckType.NONE && searchFriendModel.getCheckType() != CheckType.DISABLE) {
                        if (searchFriendModel.getCheckType() == CheckType.CHECKED) {
                            searchFriendModel.setCheckType(CheckType.UNCHECKED);
                            checkBox.setChecked(false);
                        } else  if (searchFriendModel.getCheckType() == CheckType.UNCHECKED) {
                            searchFriendModel.setCheckType(CheckType.CHECKED);
                            checkBox.setChecked(true);
                        }
                    }
                    listener.onItemContactClick(searchFriendModel.getBean());
                }
            }
        });
    }

    @Override
    public void update(SearchFriendModel searchFriendModel) {
        this.searchFriendModel = searchFriendModel;
        // 更接数据类型进行显示
        if (searchFriendModel.getCheckType() == CheckType.NONE) {
            checkBox.setVisibility(View.GONE);
        } else  if (searchFriendModel.getCheckType() == CheckType.DISABLE) {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setEnabled(false);
        } else {
            checkBox.setVisibility(View.VISIBLE);
            if (searchFriendModel.getCheckType() == CheckType.CHECKED) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        }

        FriendShipInfo friendShipInfo = searchFriendModel.getBean();
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

        ImageLoaderUtils.displayUserPortraitImage(info.getPortraitUri(), portrait);
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked) {
            searchFriendModel.setCheckType(CheckType.CHECKED);
            checkBox.setChecked(true);
        } else {
            searchFriendModel.setCheckType(CheckType.UNCHECKED);
            checkBox.setChecked(false);
        }
    }

}
