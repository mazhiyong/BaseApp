package com.lr.biyou.ui.moudle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.listener.OnMyItemClickListener;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonSelectAdapter extends RecyclerView.Adapter<CommonSelectAdapter.ViewHolder>{

    private OnMyItemClickListener mOnMyItemClickListener;
    private int mSelectItem =0;
    private Context mContext;
    private List<Map<String,Object>>  mDatas;



    public List<Map<String, Object>> getDatas() {
        return mDatas;
    }

    public void setDatas(List<Map<String, Object>> datas) {
        mDatas = datas;
    }

    public OnMyItemClickListener getOnMyItemClickListener() {
        return mOnMyItemClickListener;
    }

    public int getSelectItem() {
        return mSelectItem;
    }

    public void setOnMyItemClickListener(OnMyItemClickListener onMyItemClickListener) {
        mOnMyItemClickListener = onMyItemClickListener;
    }

    public void setSelectItem(int selectItem) {
        mSelectItem = selectItem;
    }

    public CommonSelectAdapter(Context context, List<Map<String, Object>> datas) {
        mContext = context;
        mDatas = datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_item_layout, parent,false);
        ViewHolder hondler=new ViewHolder(view);
        return hondler;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Map<String,Object> item = mDatas.get(position);
        holder.mTextView.setText(item.get("payfirmname") + "");
        //设置银行卡对应的Logo
     /*   GlideApp.with(mContext)
                .load(logopath)
                .apply(new RequestOptions().placeholder(R.drawable.tip_orange).error(R.drawable.tip_orange))
                .into(holder.mImageView);*/

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnMyItemClickListener!=null){
                    mOnMyItemClickListener.OnMyItemClickListener(v,position);
                }
                mSelectItem=position;
                notifyDataSetChanged();
            }
        });

        if (position == mSelectItem){
            holder.mLayout.setSelected(true);
        }else {
            holder.mLayout.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class  ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_bank_ico)
        ImageView mImageView;
        @BindView(R.id.tv_bank_num)
        TextView mTextView;
        @BindView(R.id.ll_layout)
        LinearLayout mLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
