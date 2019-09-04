package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder>{

    private OnMyItemClickListener mOnMyItemClickListener;
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


    public void setOnMyItemClickListener(OnMyItemClickListener onMyItemClickListener) {
        mOnMyItemClickListener = onMyItemClickListener;
    }

    public CompanyAdapter(Context context, List<Map<String, Object>> datas) {
        mContext = context;
        mDatas = datas;
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
        holder.mTextView.setText(mDatas.get(position).get("firmname")+"");
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnMyItemClickListener!=null){
                    mOnMyItemClickListener.OnMyItemClickListener(v,position);
                }
                notifyDataSetChanged();
            }
        });

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
