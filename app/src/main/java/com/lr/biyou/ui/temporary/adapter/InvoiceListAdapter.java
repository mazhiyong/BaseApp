package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;

import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class InvoiceListAdapter extends ListBaseAdapter {



    private LayoutInflater mLayoutInflater;

    public InvoiceListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_invoice_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.mTvCode.setText(item.get("fp_code")+"");
        viewHolder.mTvNumber.setText(item.get("fp_number")+"");
        viewHolder.mTvMoney.setText(item.get("fp_money")+"元");
        viewHolder.mTvDate.setText(item.get("fp_date")+"");


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_code)
        TextView mTvCode;
        @BindView(R.id.tv_number)
        TextView mTvNumber;
        @BindView(R.id.tv_money)
        TextView mTvMoney;
        @BindView(R.id.tv_date)
        TextView mTvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
