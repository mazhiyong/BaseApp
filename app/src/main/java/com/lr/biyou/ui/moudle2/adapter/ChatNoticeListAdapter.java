package com.lr.biyou.ui.moudle2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.R;
import com.lr.biyou.listener.OnChildClickListener;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.ui.moudle1.activity.NoticeDetialActivity;
import com.lr.biyou.utils.imageload.GlideUtils;

import java.io.Serializable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatNoticeListAdapter extends ListBaseAdapter {


    private LayoutInflater mLayoutInflater;

    private OnChildClickListener mListener;

    public void setmListener(OnChildClickListener mListener) {
        this.mListener = mListener;
    }

    public ChatNoticeListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_chat_notice, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.nameTv.setText(item.get("name") + "");
        GlideUtils.loadImage(mContext,item.get("portrait")+"",viewHolder.headIv);
        switch (item.get("status")+""){
            case "0": //待审核
                viewHolder.agreeTv.setVisibility(View.VISIBLE);
                viewHolder.refuseTv.setVisibility(View.VISIBLE);
                viewHolder.addedTv.setVisibility(View.GONE);
                break;

            case "1"://同意
                viewHolder.agreeTv.setVisibility(View.GONE);
                viewHolder.refuseTv.setVisibility(View.GONE);
                viewHolder.addedTv.setVisibility(View.VISIBLE);
                viewHolder.addedTv.setText("已添加");
                break;

            case "2"://拒绝
                viewHolder.agreeTv.setVisibility(View.GONE);
                viewHolder.refuseTv.setVisibility(View.GONE);
                viewHolder.addedTv.setVisibility(View.VISIBLE);
                viewHolder.addedTv.setText("已拒绝");
                break;
        }

        viewHolder.agreeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onChildClickListener(viewHolder.itemView,1,item);
                }
            }
        });

        viewHolder.refuseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onChildClickListener(viewHolder.itemView,2,item);
                }
            }
        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.head_iv)
        ImageView headIv;
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.agree_tv)
        TextView agreeTv;
        @BindView(R.id.refuse_tv)
        TextView refuseTv;
        @BindView(R.id.added_tv)
        TextView addedTv;
        @BindView(R.id.trade_lay)
        CardView tradeLay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
