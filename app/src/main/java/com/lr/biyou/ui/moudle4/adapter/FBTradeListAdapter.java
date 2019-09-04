package com.lr.biyou.ui.moudle4.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
public class FBTradeListAdapter extends ListBaseAdapter {


    private LayoutInflater mLayoutInflater;
    private int mW = 0;
    private OnChildClickListener mCallBack;

    public void setmCallBack(OnChildClickListener mCallBack) {
        this.mCallBack = mCallBack;
    }

    public FBTradeListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        LogUtilDebug.i("show", "init adapter");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_fbtrade_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        String type = item.get("kind")+"";
        if (type.equals("0")){
            viewHolder.tvBuy.setBackgroundResource(R.drawable.btn_next);
            viewHolder.tvBuy.setText("购买");
        }else {
            viewHolder.tvBuy.setBackgroundResource(R.drawable.btn_sell);
            viewHolder.tvBuy.setText("出售");
        }
        viewHolder.nameTv.setText(item.get("name") + "");
        viewHolder.tvPrice.setText(UtilTools.getRMBMoney(item.get("price")+""));
        viewHolder.tvProgress.setText("进度 "+item.get("progress"));
        viewHolder.tvNumber.setText("数量 " + item.get("number")+"  "+item.get("type"));

//        viewHolder.itemLay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mCallBack != null) {
//                    mCallBack.onChildClickListener(viewHolder.itemLay, position, item);
//                }
//            }
//        });
        viewHolder.tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onChildClickListener(viewHolder.itemLay, position, item);
                }
            }
        });

        viewHolder.ivQianbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"钱包",Toast.LENGTH_LONG).show();
            }
        });

        viewHolder.ivAlipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"支付宝",Toast.LENGTH_LONG).show();
            }
        });

        viewHolder.ivWeipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"微信",Toast.LENGTH_LONG).show();
            }
        });


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_progress)
        TextView tvProgress;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.iv_qianbao)
        ImageView ivQianbao;
        @BindView(R.id.iv_alipay)
        ImageView ivAlipay;
        @BindView(R.id.iv_weipay)
        ImageView ivWeipay;
        @BindView(R.id.tv_buy)
        TextView tvBuy;
        @BindView(R.id.item_lay)
        CardView itemLay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
