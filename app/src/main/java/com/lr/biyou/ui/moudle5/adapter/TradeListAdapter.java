package com.lr.biyou.ui.moudle5.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TradeListAdapter extends ListBaseAdapter {

    private LayoutInflater mLayoutInflater;

    public TradeListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_trade, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        String kind = item.get("type")+"";
        if (kind.equals("充币")){
            viewHolder.kindIv.setImageResource(R.drawable.icon6_chongbi);
        }else {
            viewHolder.kindIv.setImageResource(R.drawable.icon6_tibi);
        }
        viewHolder.typeTv.setText(item.get("symbol")+"");
        viewHolder.timeTv.setText(item.get("time")+"");
        viewHolder.numberTv.setText(UtilTools.formatDecimal(item.get("number")+"",2));
        viewHolder.stateTv.setText(item.get("status")+"");

        //viewHolder.mStatusTv.setText(item.get("abstract")+"");
        /*if ((item.get("amt")+"").contains("-")){
            viewHolder.mMoneyTv.setText(UtilTools.getMoney(item.get("amt")+""));
        }else {
            viewHolder.mMoneyTv.setText("+"+UtilTools.getMoney(item.get("amt")+""));
        }*/

        viewHolder.tradeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.kind_iv)
        ImageView kindIv;
        @BindView(R.id.type_tv)
        TextView typeTv;
        @BindView(R.id.time_tv)
        TextView timeTv;
        @BindView(R.id.number_tv)
        TextView numberTv;
        @BindView(R.id.state_tv)
        TextView stateTv;
        @BindView(R.id.trade_lay)
        CardView tradeLay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
