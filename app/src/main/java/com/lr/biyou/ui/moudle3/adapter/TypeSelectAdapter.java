package com.lr.biyou.ui.moudle3.adapter;

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

public class TypeSelectAdapter extends RecyclerView.Adapter<TypeSelectAdapter.ViewHolder> {


    private OnMyItemClickListener mOnMyItemClickListener;
    private int mSelectItem = 0;
    private Context mContext;
    private List<Map<String, Object>> mDatas;

    private int mType = 0;

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

    public TypeSelectAdapter(Context context, List<Map<String, Object>> datas, int type) {
        mContext = context;
        mDatas = datas;
        mType = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.type_item_layout, parent, false);
        ViewHolder hondler = new ViewHolder(view);
        return hondler;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvContent.setText(mDatas.get(position).get("name") + "");

        if (!UtilTools.empty(mDatas.get(position).get("area"))){
            holder.tvType.setText("/"+mDatas.get(position).get("area"));
        }else {
            holder.tvType.setText("  ");
        }
        holder.tvNum.setText(UtilTools.getNormalMoney(mDatas.get(position).get("price") + ""));
        holder.llLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnMyItemClickListener != null) {
                    mOnMyItemClickListener.OnMyItemClickListener(v, position);
                }
                mSelectItem = position;
                notifyDataSetChanged();
            }
        });

        if (position == mSelectItem) {
            holder.llLayout.setSelected(true);
        } else {
            holder.llLayout.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.ll_layout)
        LinearLayout llLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
