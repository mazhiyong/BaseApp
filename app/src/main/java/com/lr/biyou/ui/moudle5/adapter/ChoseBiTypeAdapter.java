package com.lr.biyou.ui.moudle5.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.lr.biyou.R;
import com.lr.biyou.listener.CallBackTotal;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChoseBiTypeAdapter extends ListBaseAdapter implements Filterable {

    private List<Map<String,Object>> mBooleanList;
    private LayoutInflater mLayoutInflater;
    private List<Map<String,Object>> mSourceList = new ArrayList<>();

    public List<Map<String, Object>> getSourceList() {
        return mSourceList;
    }

    public void setSourceList(List<Map<String, Object>> sourceList) {
        mSourceList = sourceList;
    }

    public OnMyItemClickListener getOnMyItemClickListener() {
        return mOnMyItemClickListener;
    }

    public void setOnMyItemClickListener(OnMyItemClickListener onMyItemClickListener) {
        mOnMyItemClickListener = onMyItemClickListener;
    }

    private OnMyItemClickListener mOnMyItemClickListener;


    public List<Map<String,Object>> getBooleanList() {
        return mBooleanList;
    }

    public void setBooleanList(List<Map<String,Object>> booleanList) {
        mBooleanList = booleanList;
    }


    public CallBackTotal mBackTotal;

    public CallBackTotal getBackTotal() {
        return mBackTotal;
    }

    public void setBackTotal(CallBackTotal backTotal) {
        mBackTotal = backTotal;
    }

    public ChoseBiTypeAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

         viewHolder.mCheckBox.setChecked(false);

        for (Map<String,Object> map:mBooleanList){
            Map<String,Object> m= (Map<String, Object>) map.get("object");
            if(m==item){
                boolean b= (Boolean)map.get("select");
                if(b){
                    viewHolder.mCheckBox.setChecked(true);
                }else {
                    viewHolder.mCheckBox.setChecked(false);
                }
            }

        }

        viewHolder.mNameTv.setText(item.get("symbol")+"");
        viewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.mContentLay.performClick(); //调用mContentLay的点击触摸事件
            }
        });

        viewHolder.mContentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Map m : mBooleanList) {//全部设为未选中
                    Map<String,Object> mSelect= (Map<String, Object>)m.get("object");
                    if(mSelect==item){
                        m.put("select",true);
                    }else {
                        m.put("select",false);
                    }
                }

                if (mOnMyItemClickListener != null){
                    mOnMyItemClickListener.OnMyItemClickListener(viewHolder.mCheckBox,position);
                }
            }
        });

    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            //执行过滤操作
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    //没有过滤的内容，则使用源数据
                    mDataList = mSourceList;
                } else {
                    List<Map<String, Object>> filteredList = new ArrayList<>();
                    for (Map<String, Object> map : mSourceList) {
                        String str1 = map.get("symbol") + "";
                        //这里根据需求，添加匹配规则
                        if (str1.contains(charString) ) {
                            filteredList.add(map);
                        }
                    }

                    mDataList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataList;
                return filterResults;
            }

            //把过滤后的值返回出来
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mDataList = (List<Map<String, Object>>) filterResults.values;
                notifyDataSetChanged();
                if(mDataList!=null){
                    if(mBackTotal!=null){
                        mBackTotal.setTotal(mDataList.size());
                    }
                }
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_chose_bitype, parent, false));
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.check_box)
        CheckBox mCheckBox;
        @BindView(R.id.content_lay)
        LinearLayout mContentLay;
        @BindView(R.id.name_tv)
        TextView mNameTv;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
