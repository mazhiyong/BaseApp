package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lr.biyou.R;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContcatsListAdapter extends RecyclerView.Adapter<ContcatsListAdapter.ViewHolder>{
     private Context mContext;
     private List<Map<String,Object>> mDatas;

    public ContcatsListAdapter(Context context, List<Map<String, Object>> datas) {
        mContext = context;
        mDatas = datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.contact_lsit_item_layout,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTextView.setText("");
        Glide.with(mContext).load("").apply(new RequestOptions().centerCrop().placeholder(R.drawable.tip_orange).error(R.drawable.tip_orange)).into(holder.mImageView);

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_contact_mes)
        TextView mTextView;
        @BindView(R.id.iv_contact_ico)
        ImageView mImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
