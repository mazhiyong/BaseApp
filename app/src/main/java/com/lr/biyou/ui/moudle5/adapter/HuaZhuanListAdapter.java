package com.lr.biyou.ui.moudle5.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HuaZhuanListAdapter extends ListBaseAdapter {


    private LayoutInflater mLayoutInflater;

    public HuaZhuanListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_huazhuan, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
//        map.put("type", "USDT");
//        map.put("formto", "币币到法币");
//        map.put("number", "100.00USDT");
//        map.put("time", "05/01 12:12:10");

        viewHolder.typeTv.setText(item.get("symbol") + "");
        viewHolder.timeTv.setText(item.get("time") + "");
        viewHolder.numberTv.setText(item.get("number") + "");
        viewHolder.fromtoTv.setText(item.get("typeText") + "");

        //viewHolder.mStatusTv.setText(item.get("abstract")+"");
        /*if ((item.get("amt")+"").contains("-")){
            viewHolder.mMoneyTv.setText(UtilTools.getMoney(item.get("amt")+""));
        }else {
            viewHolder.mMoneyTv.setText("+"+UtilTools.getMoney(item.get("amt")+""));
        }*/

        viewHolder.tradeLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, TradeDetailActivity.class);
//                intent.putExtra("DATA", (Serializable) item);
//                mContext.startActivity(intent);
            }
        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.type_tv)
        TextView typeTv;
        @BindView(R.id.number_tv)
        TextView numberTv;
        @BindView(R.id.fromto_tv)
        TextView fromtoTv;
        @BindView(R.id.time_tv)
        TextView timeTv;
        @BindView(R.id.trade_lay)
        CardView tradeLay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
