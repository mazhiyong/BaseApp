package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.activity.ModifyFileActivity;
import com.lr.biyou.ui.moudle.activity.ShowDetailPictrue;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.utils.tool.UtilTools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModifyFileDateAdapter extends  RecyclerView.Adapter<ModifyFileDateAdapter.ViewHolder> {

    private Context mContext;
    private List<Map<String,Object>> mDataList ;

    private LayoutInflater mLayoutInflater;
    public ModifyFileDateAdapter(Context context,List<Map<String,Object>> mDataList) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mDataList = mDataList;
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        if (UtilTools.empty(item.get("opttime"))){
            viewHolder.mTitleTv.setText("");
            viewHolder.mTitleTv.setVisibility(View.GONE);
        }else {
            viewHolder.mTitleTv.setText(item.get("opttime")+"");
            viewHolder.mTitleTv.setVisibility(View.VISIBLE);
        }


        final  List<Map<String,Object>> images = (List<Map<String,Object>>) item.get("optFiles");

        ArrayList<String> strings = new ArrayList<String>();
        if(images == null){

        }else {
            for (int i = 0;i<images.size();i++){
                strings.add(images.get(i).get("remotepath")+"");
            }

            GridLayoutManager manager = new GridLayoutManager(mContext, 4, RecyclerView.VERTICAL, false);
            viewHolder.mRecyclerView.setLayoutManager(manager);
            RecyclerView.RecycledViewPool pool = viewHolder.mRecyclerView.getRecycledViewPool();
            pool.setMaxRecycledViews(0,10);
            viewHolder.mRecyclerView.setRecycledViewPool(pool);

            ModifyShowImageAdapter mGridImageAdapter = new ModifyShowImageAdapter(mContext,images);
            viewHolder.mRecyclerView.setAdapter(mGridImageAdapter);
            mGridImageAdapter.setOnMyItemClickListener(new OnMyItemClickListener() {
                @Override
                public void OnMyItemClickListener(View view, int position) {

                    Intent intent = new Intent(mContext, ShowDetailPictrue.class);
                    intent.putExtra("position",position);
                    intent.putExtra("DATA",(Serializable) images);
                    mContext.startActivity(intent);
                    //通过淡入淡出的效果关闭和显示Activity
                    ((ModifyFileActivity)mContext).overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

                    //((AppCompatActivity)mContext).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            });
        }

    }
    /**
     * 创建ViewHolder
     */
    @Override
    public ModifyFileDateAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mLayoutInflater.inflate(R.layout.item_mofify_file_date,  viewGroup, false);
        ModifyFileDateAdapter.ViewHolder viewHolder = new ModifyFileDateAdapter.ViewHolder(view);
        return viewHolder;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTv;
        private RecyclerView mRecyclerView;
        public ViewHolder(View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.titile);
            mRecyclerView = itemView.findViewById(R.id.list_view);

        }
    }



}
