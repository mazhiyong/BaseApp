package com.lr.biyou.ui.moudle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.R;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KindSelectAdapter extends RecyclerView.Adapter<KindSelectAdapter.ViewHolder>{

    private OnMyItemClickListener mOnMyItemClickListener;
    private int mSelectItem =0;
    private Context mContext;
    private List<Map<String,Object>>  mDatas;

    private int  mType=0;
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

    public KindSelectAdapter(Context context, List<Map<String, Object>> datas, int type) {
        mContext = context;
        mDatas = datas;
        mType=type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.kind_item_layout, parent,false);
        ViewHolder hondler=new ViewHolder(view);
        return hondler;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        if (!UtilTools.empty(mDatas.get(position).get("type")+"")){
            holder.mTextView.setText(mDatas.get(position).get("type")+"");
        }

        if (!UtilTools.empty(mDatas.get(position).get("name")+"")){
            holder.mTextView.setText(mDatas.get(position).get("name")+"");
        }

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
