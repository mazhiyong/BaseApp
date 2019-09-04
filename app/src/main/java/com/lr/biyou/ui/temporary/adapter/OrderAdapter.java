package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/30 0030.
 */
public class OrderAdapter extends ListBaseAdapter {


    private LayoutInflater mLayoutInflater;
    private int mW = 0;

    public OrderAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.layout_list_item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Map<String, Object> item = mDataList.get(position);

        final ViewHolder viewHolder = (ViewHolder) holder;

        List<Map<String,Object>> listMap = (List<Map<String,Object>>) item.get("goods_list");

        if (listMap != null && listMap.size() >0){
            Map<String,Object> map = listMap.get(0);
            viewHolder.mGoodsNameTv.setText(map.get("goods_name")+"");
        }else {
            viewHolder.mGoodsNameTv.setText("");
        }

        viewHolder.mTimeTv.setText(item.get("update_date")+"");

        double price = Double.valueOf(item.get("total_fee")+"");
        String priceStr = UtilTools.fromDouble(price);
        viewHolder.mMoneyTv.setText(UtilTools.fromDouble(price));
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

        TextView mGoodsNameTv;
        TextView mMemoTv;
        TextView mMoneySignTv;
        TextView mTimeTv;
        TextView mMoneyTv;
        TextView mDealSign;


        public ViewHolder(View itemView) {
            super(itemView);
            mGoodsNameTv = (TextView) itemView.findViewById(R.id.goods_name_tv);
            mMemoTv = (TextView) itemView.findViewById(R.id.memo_tv);
            mMoneySignTv = (TextView) itemView.findViewById(R.id.money_sign_tv);
            mTimeTv = (TextView) itemView.findViewById(R.id.time_tv);
            mDealSign = (TextView) itemView.findViewById(R.id.deal_sign);
            mMoneyTv = (TextView) itemView.findViewById(R.id.money_tv);
        }
    }
}
