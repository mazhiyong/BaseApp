package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
public class PayTheAdapter extends ListBaseAdapter {



    private LayoutInflater mLayoutInflater;

    public PayTheAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_pay_the_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);

        final PayTheAdapter.ViewHolder viewHolder = (PayTheAdapter.ViewHolder) holder;


        String ss = item.get("amt") + "";
        String money = UtilTools.getRMBMoney(ss);

        viewHolder.mBankNameTv.setText(item.get("opnbnknm") + "");
        viewHolder.mNameTv.setText(item.get("accnm") + "");
        viewHolder.mBankNumTv.setText(UtilTools.getIDXing(item.get("accno") + ""));
        viewHolder.mMoneyTv.setText(money);


    }

    //	"loansqid": "1729200000092879",
    //	"accnm": "钢铁侠核心企业",
    //	"paysta": "1",
    //	"accno": "7894124512465214514",
    //	"opnbnkid": "104",
    //	"amt": "100000",
    //	"stflg": "2",
    //	"opnbnknm": "中国银行"

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_tv)
        TextView mNameTv;
        @BindView(R.id.bank_name_tv)
        TextView mBankNameTv;
        @BindView(R.id.bank_num_tv)
        TextView mBankNumTv;
        @BindView(R.id.money_tv)
        TextView mMoneyTv;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}