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
public class FoodAdapter extends ListBaseAdapter {


    private LayoutInflater mLayoutInflater;
    private int mW = 0;
    public FoodAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.layout_list_item_food, parent, false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Map<String, Object> item = mDataList.get(position);

        final ViewHolder viewHolder = (ViewHolder) holder;
        if (position == mDataList.size()-1){
            viewHolder.textView.setText("添加");
        }else {
            viewHolder.textView.setText("牛肉"+position);
        }


       /* viewHolder.mTimeTv.setText(item.get("updatetime")+"");*/
      /*  Glide.with(mContext)
                .load(item.get("url")+"").asBitmap()
                .placeholder(R.drawable.ic_launcher)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                       LinearLayout.LayoutParams lp = ( LinearLayout.LayoutParams)viewHolder.mImageView.getLayoutParams();
                        lp.width = mW;
                        float tempHeight=height * ((float)  viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });lp.width / width);
                        lp.height =(int)tempHeight ;
                        viewHolder.mImageView.setLayoutParams(lp);
                        viewHolder.mImageView.setImageBitmap(bitmap);
                    }
                });*/


       // Glide.with(mContext).load(item.get("url")+"").asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(viewHolder.mImageView);


        /*Glide.with(mContext)
                .load(item.get("url")+"")
                //.centerCrop()
                .placeholder(R.drawable.ic_launcher)
                .crossFade()
                .into(viewHolder.mImageView);*/
    }


    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView mImageView;
        private ProgressBar mPd;
        private TextView mTimeTv;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.title_text);
        }
    }
}
