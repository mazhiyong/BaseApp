package com.lr.biyou.ui.moudle4.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.R;
import com.lr.biyou.basic.BasicRecycleViewAdapter;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.List;
import java.util.Map;

public class EntrustListAdapter extends BasicRecycleViewAdapter {
    private List<Map<String,Object>> orderList;

    public EntrustListAdapter(Context context) {
        super(context);
    }

    public void setOrderList(List<Map<String,Object>> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    @Override
    protected int getItemRes() {
        return R.layout.item_entrust_list;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        return new EntrustListViewHolder(view);
    }

    @Override
    protected void bindClickListener(RecyclerView.ViewHolder viewHolder, int position) {
        EntrustListViewHolder entrustListViewHolder = (EntrustListViewHolder) viewHolder;
        Map<String,Object> map = orderList.get(position);

//        map.put("id",i);
//        map.put("uid",i);
//        map.put("symbol","BTC");
//        map.put("area","郑州");
//        map.put("price","1000");
//        map.put("average","888");
//        map.put("number","26");
//        map.put("surplus","10");
//        map.put("finish","10");
//        map.put("cancel","6");
//        map.put("createTime","2019/08/22 14:00");
//        map.put("statusText","交易中");
//        map.put("directionText","纵向");
        entrustListViewHolder.tvEntrustTime.setText(map.get("createTime")+"");
        entrustListViewHolder.tvCoinInfo.setText(map.get("symbol")+"");
        entrustListViewHolder.tvTradeDec.setText(map.get("directionText")+"");
        entrustListViewHolder.tvEntrustNumber.setText(UtilTools.formatNumber(map.get("number")+"", "#.########"));
        entrustListViewHolder.tvEntrustAmount.setText(UtilTools.formatNumber(map.get("total")+"", "#.########"));
        entrustListViewHolder.tvTradeedAmount.setText(UtilTools.formatNumber(map.get("finish")+"", "#.########"));
        entrustListViewHolder.tvEntrustPrice.setText(UtilTools.formatNumber(map.get("price")+"", "#.########"));
        entrustListViewHolder.tvEntrustStatus.setText(map.get("statusText")+"");
    }

    @Override
    public int getItemCount() {
        if (orderList != null && orderList.size() > 0) {
            return orderList.size();
        }
        return 0;
    }

    static class EntrustListViewHolder extends RecyclerView.ViewHolder {
        private TextView tvEntrustTime, tvCoinInfo, tvTradeDec, tvEntrustAmount, tvTradeedAmount,
                tvEntrustPrice, tvEntrustStatus, tvEntrustNumber;

        public EntrustListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEntrustTime = itemView.findViewById(R.id.tvEntrustTime);
            tvCoinInfo = itemView.findViewById(R.id.tvCoinInfo);
            tvTradeDec = itemView.findViewById(R.id.tvTradeDec);
            tvEntrustNumber = itemView.findViewById(R.id.tvEntrustNumber);
            tvEntrustAmount = itemView.findViewById(R.id.tvEntrustAmount);
            tvTradeedAmount = itemView.findViewById(R.id.tvTradeedAmount);
            tvEntrustPrice = itemView.findViewById(R.id.tvEntrustPrice);
            tvEntrustStatus = itemView.findViewById(R.id.tvEntrustStatus);
        }
    }
}
