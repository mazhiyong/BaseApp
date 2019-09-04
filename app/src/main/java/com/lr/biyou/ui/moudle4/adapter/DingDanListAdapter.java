package com.lr.biyou.ui.moudle4.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DingDanListAdapter extends ListBaseAdapter {



    private LayoutInflater mLayoutInflater;

    public DingDanListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_dingdan, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
//        map.put("type", "USDT");
//        map.put("formto", "币币到法币");
//        map.put("number", "100.00USDT");
//        map.put("time", "05/01 12:12:10");
        if (UtilTools.empty(item.get("bare"))){
            viewHolder.biliLay.setVisibility(View.GONE);
        }else {
            viewHolder.biliLay.setVisibility(View.VISIBLE);
            viewHolder.biliTv.setText(item.get("bare")+"");
        }

        viewHolder.typeTv.setText(item.get("type") + "");
        viewHolder.timeTv.setText(item.get("time") + "");
        viewHolder.numberTv.setText(item.get("number") + "");
        viewHolder.priceTv.setText(item.get("pice") + "");
        viewHolder.stateTv.setText(item.get("state") + "");

    //viewHolder.mStatusTv.setText(item.get("abstract")+"");
        /*if ((item.get("amt")+"").contains("-")){
            viewHolder.mMoneyTv.setText(UtilTools.getMoney(item.get("amt")+""));
        }else {
            viewHolder.mMoneyTv.setText("+"+UtilTools.getMoney(item.get("amt")+""));
        }*/

        viewHolder.tradeLay.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
//                Intent intent = new Intent(mContext, TradeDetailActivity.class);
//                intent.putExtra("DATA", (Serializable) item);
//                mContext.startActivity(intent);
    }
    });

}


public class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.bili_tv)
    TextView biliTv;
    @BindView(R.id.state_tv)
    TextView stateTv;
    @BindView(R.id.type_tv)
    TextView typeTv;
    @BindView(R.id.time_tv)
    TextView timeTv;
    @BindView(R.id.number_tv)
    TextView numberTv;
    @BindView(R.id.price_tv)
    TextView priceTv;
    @BindView(R.id.bili_lay)
    LinearLayout biliLay;
    @BindView(R.id.trade_lay)
    CardView tradeLay;


    public ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}


}
