package com.lr.biyou.ui.moudle3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.R;
import com.lr.biyou.listener.OnChildClickListener;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class ShouMoneyListAdapter extends ListBaseAdapter {



    private LayoutInflater mLayoutInflater;
    private int mW = 0;
    private OnChildClickListener mCallBack;

    public void setmCallBack(OnChildClickListener mCallBack) {
        this.mCallBack = mCallBack;
    }

    public ShouMoneyListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        LogUtilDebug.i("show", "init adapter");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_shoumoney_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        switch (item.get("kind")+""){
            case "0": //持仓
                viewHolder.dongtaiTv.setText("收益(BTC)");

                String profit = item.get("profit")+"";
                if (UtilTools.empty(profit)){
                    viewHolder.tv3.setText("--");
                }else {
                    //viewHolder.tv3.setText(UtilTools.getNormalMoney(profit));
                    viewHolder.tv3.setText(profit);
                }
                viewHolder.dealTv.setText("平仓");
                break;

            case "1": //委托
                viewHolder.dongtaiTv.setText("触发价(USD)");
                String trigger = item.get("trigger")+"";
                if (UtilTools.empty(trigger)){
                    viewHolder.tv3.setText("--");
                }else {
                    //viewHolder.tv3.setText(UtilTools.getNormalMoney(trigger));
                    viewHolder.tv3.setText(trigger);
                }
                viewHolder.dealTv.setText("撤销");
                break;

            case "2": //成交
                viewHolder.dongtaiTv.setText("收益(BTC)");
                String profit1 = item.get("profit")+"";
                if (UtilTools.empty(profit1)){
                    viewHolder.tv3.setText("--");
                }else {
                    //viewHolder.tv3.setText(UtilTools.getNormalMoney(profit1));
                    viewHolder.tv3.setText(profit1);
                }
                viewHolder.dealTv.setText("成交");
                viewHolder.dealTv.setTextColor(ContextCompat.getColor(mContext,R.color.gray));
                break;
        }

        //viewHolder.dealTv.setText(item.get("typeText")+"");


        String lever = item.get("lever")+"";
        if (UtilTools.empty(lever)){
            lever = "1";
        }
        if ((item.get("type") + "").equals("0")) { //开多
            viewHolder.typeTv.setText("开多" + lever +"X");
            viewHolder.typeTv.setTextColor(ContextCompat.getColor(mContext,R.color.green_light));
        } else { //开空
            viewHolder.typeTv.setText("开空" + lever +"X");
            viewHolder.typeTv.setTextColor(ContextCompat.getColor(mContext,R.color.red_light));
        }
        String name = item.get("name")+"";
        if (UtilTools.empty(name)){
            viewHolder.nameTv.setText("--");
        }else {
            viewHolder.nameTv.setText(name);
        }

        String time= item.get("time")+"";
        if (UtilTools.empty(name)){
            viewHolder.timeTv.setText("--");
        }else {
            viewHolder.timeTv.setText(time);
        }


        String number= item.get("number")+"";
        if (UtilTools.empty(number)){
            viewHolder.tv1.setText("--");
        }else {
            //viewHolder.tv1.setText(UtilTools.getNormalMoney(number));
            viewHolder.tv1.setText(number);
        }

        String depot_avg= item.get("depot_avg")+"";
        if (UtilTools.empty(depot_avg)){
            viewHolder.tv2.setText("--");
        }else {
            //viewHolder.tv2.setText(UtilTools.getNormalMoney(depot_avg));
            viewHolder.tv2.setText(depot_avg);
        }


        String finish_number = item.get("finish_number")+"";
        if (UtilTools.empty(finish_number)){
            viewHolder.tv4.setText("--");
        }else {
            //viewHolder.tv4.setText(UtilTools.getNormalMoney(finish_number));
            viewHolder.tv4.setText(finish_number);
        }



        String average_price = item.get("average_price")+"";
        if (UtilTools.empty(average_price)){
            viewHolder.tv5.setText("--");
        }else {
            //viewHolder.tv5.setText(UtilTools.getNormalMoney(average_price));
            viewHolder.tv5.setText(average_price);
        }

        String fee= item.get("fee")+"";
        if (UtilTools.empty(fee)){
            viewHolder.tv6.setText("--");
        }else {
            //viewHolder.tv6.setText(UtilTools.getNormalMoney(fee));
            viewHolder.tv6.setText(fee);
        }

        String stop_profit= item.get("stop_profit")+"";
        if (UtilTools.empty(stop_profit)){
            viewHolder.tv7.setText("--");
        }else {
            //viewHolder.tv7.setText(UtilTools.getNormalMoney(stop_profit));
            viewHolder.tv7.setText(stop_profit);
        }

        String loss_limit= item.get("loss_limit")+"";
        if (UtilTools.empty(loss_limit)){
            viewHolder.tv8.setText("--");
        }else {
            //viewHolder.tv8.setText(UtilTools.getNormalMoney(loss_limit));
            viewHolder.tv8.setText(loss_limit);
        }

        String bond= item.get("bond")+"";
        if (UtilTools.empty(bond)){
            viewHolder.tv9.setText("--");
        }else {
            //viewHolder.tv9.setText(UtilTools.getNormalMoney(bond));
            viewHolder.tv9.setText(bond);
        }


        viewHolder.dealTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (mCallBack != null) {
                        mCallBack.onChildClickListener(viewHolder.itemLay, position, item);
                    }
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.dongtai_tv)
        TextView dongtaiTv;
        @BindView(R.id.type_tv)
        TextView typeTv;
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.time_tv)
        TextView timeTv;
        @BindView(R.id.deal_tv)
        TextView dealTv;
        @BindView(R.id.tv_1)
        TextView tv1;
        @BindView(R.id.tv_2)
        TextView tv2;
        @BindView(R.id.tv_3)
        TextView tv3;
        @BindView(R.id.tv_4)
        TextView tv4;
        @BindView(R.id.tv_5)
        TextView tv5;
        @BindView(R.id.tv_6)
        TextView tv6;
        @BindView(R.id.tv_7)
        TextView tv7;
        @BindView(R.id.tv_8)
        TextView tv8;
        @BindView(R.id.tv_9)
        TextView tv9;
        @BindView(R.id.item_lay)
        CardView itemLay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
