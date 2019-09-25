package com.lr.biyou.ui.moudle1.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.R;
import com.lr.biyou.basic.BasicRecycleViewAdapter;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CoinInfoAdapter extends BasicRecycleViewAdapter {
    private List<Map<String,Object>> coinList;

    private Context mContext;
    public CoinInfoAdapter(Context context) {
        super(context);
        mContext = context;
    }

    public void setList(List<Map<String,Object>> coinList) {
        this.coinList = coinList;
        notifyDataSetChanged();
    }

    @Override
    protected int getItemRes() {
        return R.layout.item_coin_info;
    }

    public List<Map<String,Object>> getCoinList() {
        if (coinList == null) {
            return new ArrayList<>();
        }
        return coinList;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int widthPixels = metrics.widthPixels;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = widthPixels / 3;
        view.setLayoutParams(layoutParams);
        return new CoinInfoViewHolder(view);
    }

    @Override
    protected void bindClickListener(RecyclerView.ViewHolder viewHolder, int position) {
        CoinInfoViewHolder coinInfoViewHolder = (CoinInfoViewHolder) viewHolder;
        Map map = coinList.get(position);
       coinInfoViewHolder.tvCoinName.setText(map.get("name") + "/"+map.get("area"));
       coinInfoViewHolder.tvCoinPrice.setText(UtilTools.getNormalMoney(map.get("price")+""));
       coinInfoViewHolder.tvCoinConvert.setText("≈ "+map.get("cny")+" CNY");
       //coinInfoViewHolder.tvCoinConvert.setText(UiTools.getString(R.string.defaultCny).replace("%S", frontBean.getCnyNumber()));

        if (UtilTools.empty(MbsConstans.COLOR_LOW) || UtilTools.empty(MbsConstans.COLOR_TOP)){
            //0 红跌绿涨   1红涨绿跌
            String colorType =  SPUtils.get(mContext, MbsConstans.SharedInfoConstans.COLOR_TYPE,"0").toString();
            if (colorType.equals("0")){
                MbsConstans.COLOR_LOW = MbsConstans.COLOR_RED;
                MbsConstans.COLOR_TOP = MbsConstans.COLOR_GREEN;
            }else {
                MbsConstans.COLOR_LOW = MbsConstans.COLOR_GREEN;
                MbsConstans.COLOR_TOP = MbsConstans.COLOR_RED;

            }
        }

        if ((map.get("increase") + "").contains("-")) {
            coinInfoViewHolder.tvCoinRatio.setText(map.get("increase") + "");

            coinInfoViewHolder.tvCoinPrice.setTextColor(Color.parseColor(MbsConstans.COLOR_LOW));
            coinInfoViewHolder.tvCoinRatio.setTextColor(Color.parseColor(MbsConstans.COLOR_LOW));
        } else {
            coinInfoViewHolder.tvCoinRatio.setText("" +map.get("increase"));
            coinInfoViewHolder.tvCoinPrice.setTextColor(Color.parseColor(MbsConstans.COLOR_TOP));
           coinInfoViewHolder.tvCoinRatio.setTextColor(Color.parseColor(MbsConstans.COLOR_TOP));
        }
    }

    @Override
    public int getItemCount() {
        if (coinList != null && coinList.size() > 0) {
            return coinList.size();
        }
        return 0;
    }

    static class CoinInfoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCoinName, tvCoinPrice, tvCoinRatio, tvCoinConvert;

        public CoinInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCoinName = itemView.findViewById(R.id.tvCoinName);
            tvCoinPrice = itemView.findViewById(R.id.tvCoinPrice);
            tvCoinRatio = itemView.findViewById(R.id.tvCoinRatio);
            tvCoinConvert = itemView.findViewById(R.id.tvCoinConvert);
        }
    }
}
