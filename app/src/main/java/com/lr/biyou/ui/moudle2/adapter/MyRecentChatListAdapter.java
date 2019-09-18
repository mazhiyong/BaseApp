package com.lr.biyou.ui.moudle2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.R;
import com.lr.biyou.listener.CallBackTotal;
import com.lr.biyou.listener.OnChildClickListener;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.tool.LogUtilDebug;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyRecentChatListAdapter extends ListBaseAdapter implements Filterable {


    private LayoutInflater mLayoutInflater;

    private OnChildClickListener mListener;

    private List<Map<String,Object>> mSourceList = new ArrayList<>();

    public List<Map<String, Object>> getSourceList() {
        return mSourceList;
    }

    public void setSourceList(List<Map<String, Object>> sourceList) {
        mSourceList = sourceList;
    }

    public void setmListener(OnChildClickListener mListener) {
        this.mListener = mListener;
    }


    public CallBackTotal mBackTotal;

    public CallBackTotal getBackTotal() {
        return mBackTotal;
    }

    public void setBackTotal(CallBackTotal backTotal) {
        mBackTotal = backTotal;
    }


    public MyRecentChatListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_my_recently, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        LogUtilDebug.i("show","integer:"+item.get("account")+"");
        if ((item.get("top")+"").equals("1")){
            viewHolder.tradeLay.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.moccasin));
        }


        if ((item.get("account")+"").equals("0") || (item.get("disturb")+"").equals("1")){
            viewHolder.notRedTv.setVisibility(View.GONE);
        }else {
            viewHolder.notRedTv.setVisibility(View.VISIBLE);
            viewHolder.notRedTv.setText(item.get("account")+"");
        }

        viewHolder.nameTv.setText(item.get("name") + "");
        viewHolder.contentTv.setText(item.get("content")+"");
        GlideUtils.loadImage(mContext,item.get("portrait")+"",viewHolder.headIv);
        viewHolder.tradeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onChildClickListener(viewHolder.itemView,position,item);
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
                Log.i("show","charString:"+charString);
                if (charString.isEmpty()) {
                    //没有过滤的内容，则使用源数据
                    mDataList = mSourceList;
                } else {
                    List<Map<String, Object>> filteredList = new ArrayList<>();
                    for (Map<String, Object> map : mSourceList) {
                        String str1 = map.get("name") + "";
                        Log.i("show","str1:"+str1);
                        //这里根据需求，添加匹配规则
                        if (str1.contains(charString) ) {
                            filteredList.add(map);
                            Log.i("show","搜索到符合条件的");
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


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.head_iv)
        ImageView headIv;
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.content_tv)
        TextView contentTv;
        @BindView(R.id.agree_tv)
        TextView agreeTv;
        @BindView(R.id.refuse_tv)
        TextView refuseTv;
        @BindView(R.id.added_tv)
        TextView addedTv;
        @BindView(R.id.not_red_tv)
        TextView notRedTv;
        @BindView(R.id.trade_lay)
        CardView tradeLay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
