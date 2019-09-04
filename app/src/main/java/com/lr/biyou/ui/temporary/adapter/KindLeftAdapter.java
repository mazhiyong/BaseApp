package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/30 0030.
 */
public class KindLeftAdapter extends ListBaseAdapter {


    private LayoutInflater mLayoutInflater;
    private int mW = 0;
    public KindLeftAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.kind_left_item, parent, false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Map<String, Object> item = mDataList.get(position);

        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.textView.setText("分类");

    }


    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView mImageView;
        private ProgressBar mPd;
        private TextView mTimeTv;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.cate_tv);
        }
    }
}
