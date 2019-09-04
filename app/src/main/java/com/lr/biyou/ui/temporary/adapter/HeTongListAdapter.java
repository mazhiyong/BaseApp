package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeTongListAdapter extends ListBaseAdapter {



    private LayoutInflater mLayoutInflater;
    private int mW = 0;

    public HeTongListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_hetong_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);

        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.mItemName.setText(item.get("httitle")+"");


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_name)
        TextView mItemName;
        @BindView(R.id.content_lay)
        LinearLayout mContentLay;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
