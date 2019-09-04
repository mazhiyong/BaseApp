package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SkPeopleAdapter extends ListBaseAdapter {



    private LayoutInflater mLayoutInflater;

    public SkPeopleAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_sk_people, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        String accname = item.get("accname") + "";
        String bankname = item.get("bankname") + "";
        String accid = item.get("accid") + "";


        viewHolder.mNameValueTv.setText(accname);
        viewHolder.mBankNameTv.setText(bankname);
        viewHolder.mCardNumTv.setText(accid);

    }

    //"bankid":"3002",
    //"crossmark":"2",
    //"accid":"6224101646431619",
    //"wdcode":"503100000023",
    //"bankname":"南洋商业银行",
    //"wdname":"南洋商业银行（中国）有限公司北京建国门支行",
    //"accname":"嗯……在",
    //"acctype":"2"
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_value_tv)
        TextView mNameValueTv;
        @BindView(R.id.bank_name_tv)
        TextView mBankNameTv;
        @BindView(R.id.card_num_tv)
        TextView mCardNumTv;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
