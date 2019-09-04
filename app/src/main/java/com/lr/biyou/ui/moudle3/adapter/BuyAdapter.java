package com.lr.biyou.ui.moudle3.adapter;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.R;
import com.lr.biyou.basic.BasicRecycleViewAdapter;
import com.lr.biyou.utils.tool.UtilTools;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class BuyAdapter extends BasicRecycleViewAdapter {
    private List<List<String>> buySell;
    private int precision;
    private Context mContext;

    public BuyAdapter(Context context) {
        super(context);
        mContext = context;
    }

    public void setBuyTradeInfo(List<List<String>> buySell, int precision) {
        this.buySell = buySell;
        this.precision = precision;
        notifyDataSetChanged();
    }

    public List<List<String>> getBuySell() {
        if (buySell == null) {
            return new ArrayList<>();
        }
        return buySell;
    }

    @Override
    protected int getItemRes() {
        return R.layout.item_handicap;
    }

    @Override
    protected RecyclerView.ViewHolder getViewHolder(View view) {
        return new BuyViewHolder(view);
    }

    @Override
    protected void bindClickListener(RecyclerView.ViewHolder viewHolder, int position) {
        BuyViewHolder buyViewHolder = (BuyViewHolder) viewHolder;
        List<String> strings;
        if (buySell != null) {
            if (buySell.size() < 5) {
                if (position > buySell.size() - 1) {
                    strings = new ArrayList<>();
                    strings.add("--");
                    strings.add("--");
                } else {
                    strings = buySell.get(position);
                }
            } else {
                strings = buySell.get(position);
            }
            String number = strings.get(1);
            if ("--".equals(number)) {
                buyViewHolder.tvNumber.setText(number);
            } else {
                double amount = Double.parseDouble(number);
                if (amount > 1000) {
                    double transferAmount = amount / 1000L;
                    buyViewHolder.tvNumber.setText(UtilTools.formatNumber(transferAmount, "0.000", RoundingMode.FLOOR) + "K");
                } else {
                    buyViewHolder.tvNumber.setText(UtilTools.formatNumber(amount, "0.000", RoundingMode.FLOOR));
                }
            }
            String price = strings.get(0);
            if ("--".equals(price)) {
                buyViewHolder.tvPrice.setText(price);
            } else {
                buyViewHolder.tvPrice.setText(UtilTools.formatDecimal(strings.get(0), precision));
            }
            buyViewHolder.tvPrice.setTextColor(ContextCompat.getColor(mContext,R.color.green_light));
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    static class BuyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPrice, tvNumber, textView59;

        public BuyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvNumber = itemView.findViewById(R.id.tvNumber);
            textView59 = itemView.findViewById(R.id.textView59);
        }
    }
}
