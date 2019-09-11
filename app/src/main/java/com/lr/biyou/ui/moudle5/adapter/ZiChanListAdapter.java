package com.lr.biyou.ui.moudle5.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
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
public class ZiChanListAdapter extends ListBaseAdapter {

    private LayoutInflater mLayoutInflater;
    private int mW = 0;
    private OnChildClickListener mCallBack;

    public void setmCallBack(OnChildClickListener mCallBack) {
        this.mCallBack = mCallBack;
    }

    public ZiChanListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        LogUtilDebug.i("show", "init adapter");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_zichan_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
//        map.put("","USDT");
//        map.put("money1","100000");
//        map.put("money2","909010");
//        map.put("money3","608109291");
        viewHolder.typeTv.setText(item.get("symbol") + "");
        viewHolder.moneyAvaivable.setText(UtilTools.formatDecimal(item.get("balance")+"",2));
        if (UtilTools.empty(item.get("frozen"))){
            viewHolder.dongjieLay.setVisibility(View.GONE);
        }else {
            viewHolder.moneyLimit.setText(UtilTools.formatDecimal(item.get("frozen")+"",2));
        }
        viewHolder.moneyZhehe.setText(UtilTools.formatDecimal(item.get("cny")+"",2));

        viewHolder.itemLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onChildClickListener(viewHolder.itemLay, position, item);
                }
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.type_tv)
        TextView typeTv;
        @BindView(R.id.money_avaivable)
        TextView moneyAvaivable;
        @BindView(R.id.money_limit)
        TextView moneyLimit;
        @BindView(R.id.money_zhehe)
        TextView moneyZhehe;
        @BindView(R.id.item_lay)
        CardView itemLay;
        @BindView(R.id.dongjie_lay)
        LinearLayout dongjieLay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
