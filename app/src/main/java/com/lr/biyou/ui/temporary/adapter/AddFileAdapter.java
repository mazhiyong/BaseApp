package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;
import java.util.Map;

public class AddFileAdapter extends ListBaseAdapter {

    private String mSign = "";

    private GridImageAdapter.onAddPicClickListener onAddPicClickListene;
    private GridImageAdapter.OnItemClickListener mOnItemClickListener;

    public GridImageAdapter.onAddPicClickListener getOnAddPicClickListene() {
        return onAddPicClickListene;
    }

    public void setOnAddPicClickListene(GridImageAdapter.onAddPicClickListener onAddPicClickListene) {
        this.onAddPicClickListene = onAddPicClickListene;
    }

    public GridImageAdapter.OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(GridImageAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    private LayoutInflater mLayoutInflater;
    public AddFileAdapter(Context context,String mSign) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.mSign = mSign;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_add_file, parent, false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.mTitleTv.setText(item.get("name")+"");
        String bixuan = item.get("isreq")+"";
        if (bixuan.equals("1")){
            viewHolder.mBixuanTv.setVisibility(View.VISIBLE);
        }else {
            viewHolder.mBixuanTv.setVisibility(View.GONE);
        }

        //文件类型  然后里面可以添加图片  和显示图片
        FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        viewHolder.mRecyclerView.setLayoutManager(manager);
        String code = "";
        if (mSign.equals("1")){
            code = item.get("connpk")+"";
        }else {
            code = item.get("filetype")+"";
        }
        GridImageAdapter mGridImageAdapter = new GridImageAdapter(mContext,onAddPicClickListene,code);
        mGridImageAdapter.setSelectMax(200);
        if(item.get("selectPicList") == null){

        }else {
            List<LocalMedia> selectPicList = (List<LocalMedia>) item.get("selectPicList");
            if (selectPicList!= null&& selectPicList.size()>0){
                mGridImageAdapter.setList(selectPicList);
                mGridImageAdapter.notifyDataSetChanged();
            }
        }

        mGridImageAdapter.setOnItemClickListener(mOnItemClickListener);
        viewHolder.mRecyclerView.setAdapter(mGridImageAdapter);
    }


    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTv;
        private RecyclerView mRecyclerView;
        private TextView mBixuanTv;
        public ViewHolder(View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.titile);
            mBixuanTv = itemView.findViewById(R.id.bixuan_tv);
            mRecyclerView = itemView.findViewById(R.id.list_view);
        }
    }

}
