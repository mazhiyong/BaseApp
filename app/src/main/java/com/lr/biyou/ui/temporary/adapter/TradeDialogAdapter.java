package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.listener.OnMyItemClickListener;

import java.util.List;
import java.util.Map;


public class TradeDialogAdapter extends RecyclerView.Adapter<TradeDialogAdapter.MyHolder> {


    private Context mContext;



    private int mSelectItme=0;

    private List<Map<String, Object>> mDatas;
    private OnMyItemClickListener mOnItemClickListener;

    public List<Map<String, Object>> getDatas() {
        return mDatas;
    }

    public void setDatas(List<Map<String, Object>> datas) {
        mDatas = datas;
    }
    public OnMyItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnMyItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
    public int getSelectItme() {
        return mSelectItme;
    }

    public void setSelectItme(int selectItme) {
        mSelectItme = selectItme;
    }

    public TradeDialogAdapter(Context context, List<Map<String, Object>> mDatas) {
        super();
        this.mContext = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getItemCount() {
        int itemCount = mDatas.size();
        return itemCount;
    }



    @Override
    // 填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(MyHolder holder, final int position) {
        final Map<String, Object> map = mDatas.get(position);
        holder.mTypeTv.setText(map.get("name")+"");

        if (mSelectItme == position){
            holder.mTypeTv.setSelected(true);
        }else {
            holder.mTypeTv.setSelected(false);
        }

        holder.mTypeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null){
                    mOnItemClickListener.OnMyItemClickListener(v,position);
                    setSelectItme(position);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    // 重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    public MyHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        // 填充布局

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_trade_dialog, null);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // 定义内部类继承ViewHolder
    class MyHolder extends RecyclerView.ViewHolder {
        TextView mTypeTv;

        public MyHolder(View view) {
            super(view);
            mTypeTv = view.findViewById(R.id.type_tv);
        }
    }



}