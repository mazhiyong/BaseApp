package com.lr.biyou.ui.moudle1.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.ui.moudle1.activity.AddPayWayActivity;

import java.io.Serializable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaywayListAdapter extends ListBaseAdapter {


    private LayoutInflater mLayoutInflater;

    public PaywayListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_payway, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
//        map.put("kind","0");//0银行卡  1 支付宝 2 微信支付
//        map.put("type","1"); //0未添加  1 已添加
//        map.put("name","陈港生");
//        map.put("number","1929 9901 9881 112");
        String type = item.get("type") + "";
        if (type.equals("0")) {
            viewHolder.nameTv.setText("***");
            viewHolder.numberTv.setText("*****");
            viewHolder.editTv.setText("添加");
            viewHolder.editTv.setTextColor(ContextCompat.getColor(mContext,R.color.blue1));
        } else {
            viewHolder.nameTv.setText(item.get("name")+"");
            viewHolder.numberTv.setText(item.get("number")+"");
            viewHolder.editTv.setText("修改");
            viewHolder.editTv.setTextColor(ContextCompat.getColor(mContext,R.color.black));
        }
        String kind = item.get("kind") + "";
        switch (kind){
            case "0":
                viewHolder.typeTv.setText("银行卡");
                viewHolder.kindIv.setImageResource(R.drawable.icon4_bank);
                break;
            case "1":
                viewHolder.typeTv.setText("支付宝");
                viewHolder.kindIv.setImageResource(R.drawable.icon4_alipay);
                break;
            case "2":
                viewHolder.typeTv.setText("微信支付");
                viewHolder.kindIv.setImageResource(R.drawable.icon4_wechat);
                break;


        }

        //viewHolder.mStatusTv.setText(item.get("abstract")+"");
        /*if ((item.get("amt")+"").contains("-")){
            viewHolder.mMoneyTv.setText(UtilTools.getMoney(item.get("amt")+""));
        }else {
            viewHolder.mMoneyTv.setText("+"+UtilTools.getMoney(item.get("amt")+""));
        }*/

        viewHolder.tradeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddPayWayActivity.class);
                intent.putExtra("DATA", (Serializable) mDataList.get(position));
                mContext.startActivity(intent);

            }
        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.kind_iv)
        ImageView kindIv;
        @BindView(R.id.type_tv)
        TextView typeTv;
        @BindView(R.id.edit_tv)
        TextView editTv;
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.number_tv)
        TextView numberTv;
        @BindView(R.id.trade_lay)
        CardView tradeLay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
