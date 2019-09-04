package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;

import java.util.List;
import java.util.Map;

public class ModifyFileAdapter extends ListBaseAdapter {


    private LayoutInflater mLayoutInflater;
    public ModifyFileAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_mofify_file_type, parent, false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.mTitleTv.setText(item.get("name")+"");

        List<Map<String,Object>> files = (List<Map<String,Object>>) item.get("files");

        if(files == null){

        }else {
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(RecyclerView.VERTICAL);
            viewHolder.mRecyclerView.setLayoutManager(manager);

            ModifyFileDateAdapter mGridImageAdapter = new ModifyFileDateAdapter(mContext,files);
            viewHolder.mRecyclerView.setAdapter(mGridImageAdapter);

        }

    }


    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTv;
        private RecyclerView mRecyclerView;
        public ViewHolder(View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.titile);
            mRecyclerView = itemView.findViewById(R.id.list_view);

        }
    }

}
