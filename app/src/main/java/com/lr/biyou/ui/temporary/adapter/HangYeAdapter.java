package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.listener.OnSearchItemClickListener;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
public class HangYeAdapter extends ListBaseAdapter implements Filterable {


    private LayoutInflater mLayoutInflater;
    private List<Map<String,Object>> mSourceList = new ArrayList<>();
    private OnSearchItemClickListener mOnSearchItemClickListener;

    public List<Map<String, Object>> getSourceList() {
        return mSourceList;
    }
    public void setSourceList(List<Map<String, Object>> sourceList) {
        mSourceList = sourceList;
    }


    public OnSearchItemClickListener getOnSearchItemClickListener() {
        return mOnSearchItemClickListener;
    }

    public void setOnSearchItemClickListener(OnSearchItemClickListener onSearchItemClickListener) {
        mOnSearchItemClickListener = onSearchItemClickListener;
    }


    public HangYeAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_hangye, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);

        final ViewHolder viewHolder = (ViewHolder) holder;

        /*List<Map<String,Object>> mChildList = (List<Map<String,Object>>) item.get("typeList");
        if (mChildList == null || mChildList.size() == 0){
            viewHolder.mArrowView.setVisibility(View.GONE);
        }else {
            viewHolder.mArrowView.setVisibility(View.VISIBLE);
        }*/
        viewHolder.mArrowView.setVisibility(View.GONE);

        viewHolder.mItemName.setText(item.get("name")+"");
        viewHolder.mWangdianLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSearchItemClickListener  != null){
                    mOnSearchItemClickListener.OnSearchItemClickListener(item);
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
                        String str1 = map.get("name") + "";
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
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_name)
        TextView mItemName;
        @BindView(R.id.arrow_view)
        ImageView mArrowView;
        @BindView(R.id.wangdian_lay)
        RelativeLayout mWangdianLay;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}