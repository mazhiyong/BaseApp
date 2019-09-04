package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.listener.OnMyItemClickListener;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignHeTongAdapter extends RecyclerView.Adapter<SignHeTongAdapter.MyHolder> {


    private Context mContext;

    public List<Map<String, Object>> getDatas() {
        return mDatas;
    }

    public void setDatas(List<Map<String, Object>> datas) {
        mDatas = datas;
    }

    private List<Map<String, Object>> mDatas;

    public OnMyItemClickListener getOnMyItemClickListener() {
        return mOnMyItemClickListener;
    }

    public void setOnMyItemClickListener(OnMyItemClickListener onMyItemClickListener) {
        mOnMyItemClickListener = onMyItemClickListener;
    }

    private OnMyItemClickListener mOnMyItemClickListener;


    public SignHeTongAdapter(Context context, List<Map<String, Object>> list) {
        super();
        this.mContext = context;
        mDatas = list;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    // 填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(final MyHolder holder, final int position) {

        final Map<String, Object> map = mDatas.get(position);
        holder.mItemName.setText(map.get("pdfname")+"");
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mOnMyItemClickListener != null){
                    mOnMyItemClickListener.OnMyItemClickListener(v,position);
                }
                notifyDataSetChanged();
            }
        });


    }

    @Override
    // 重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    public MyHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        // 填充布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hetong_list, arg0,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }


    // 定义内部类继承ViewHolder
    class MyHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_name)
        TextView mItemName;
        @BindView(R.id.content_lay)
        LinearLayout mLayout;
        public MyHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

}