package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.ui.temporary.activity.BorrowDetailActivity;
import com.lr.biyou.ui.temporary.activity.UploadDkYongTActivity;
import com.lr.biyou.utils.tool.SelectDataUtil;
import com.lr.biyou.utils.tool.UtilTools;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
public class BorrowAdapter extends ListBaseAdapter {


    private LayoutInflater mLayoutInflater;
    private int mW = 0;

    public BorrowAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_borrow_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);

        final ViewHolder viewHolder = (ViewHolder) holder;

        //借款状态（1：放款中 2：还款中 3：已结清 4：已驳回）
        String ss = item.get("loanstate")+"";
        if (UtilTools.empty(ss)){
            ss = "0";
        }


        int status = Integer.valueOf(ss);
        String statusStr = "";
        switch (status){
            case 1:
                statusStr = "放款中";
                viewHolder.mUploadLay.setVisibility(View.GONE);
                break;
            case 2:
                statusStr = "已放款";
                viewHolder.mUploadLay.setVisibility(View.VISIBLE);
                viewHolder.mUploadTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, UploadDkYongTActivity.class);
                        intent.putExtra("DATA",(Serializable) item);
                        mContext.startActivity(intent);
                    }
                });
                break;
            case 3:
                viewHolder.mUploadLay.setVisibility(View.GONE);
                statusStr = "已结清";
                break;
            case 4:
                viewHolder.mUploadLay.setVisibility(View.GONE);
                statusStr = "已驳回";
                break;
            default:
                viewHolder.mUploadLay.setVisibility(View.GONE);
                statusStr = "";
                break;
        }

        List<Map<String, Object>> list = SelectDataUtil.getNameCodeByType("loanState");
        Map<String,Object> mm = SelectDataUtil.getMapByKey(ss+"",list);
        statusStr = mm.get(ss+"")+"";


        viewHolder.mStatusTv.setText(statusStr);
        viewHolder.mBankNameTv.setText(item.get("zifangnme")+"");
        viewHolder.mTimeTv.setText(item.get("flowdate")+"");

        String dateTime = UtilTools.getStringFromSting2(item.get("flowdate")+"","yyyyMMdd","yyyy-MM-dd");
        viewHolder.mTimeTv.setText(dateTime+"");


        String money = UtilTools.getRMBMoney(item.get("reqmoney")+"");
        viewHolder.mMoneyTv.setText(money);

        viewHolder.mItemLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BorrowDetailActivity.class);
                intent.putExtra("DATA",(Serializable)item);
                mContext.startActivity(intent);

            }
        });
    }

    //{
    //		"jixishum": "2",
    //		"reqmoney": "100000",
    //		"flowdate": "20171019",
    //		"loansqid": "1729200000092879",
    //		"loancode": "L07",
    //		"zifangnme": "廊坊银行股份有限公司营业部",
    //		"loanstate": "3",
    //		"stopdate": "20181014",
    //		"zifangbho": "BOLF8888"
    //	}                                             借款状态（1：放款中 2：还款中 3：已结清 4：已驳回）
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.time_tv)
        TextView mTimeTv;
        @BindView(R.id.money_tv)
        TextView mMoneyTv;
        @BindView(R.id.status_tv)
        TextView mStatusTv;
        @BindView(R.id.bank_name_tv)
        TextView mBankNameTv;
        @BindView(R.id.upload_tv)
        TextView mUploadTv;
        @BindView(R.id.upload_lay)
        LinearLayout mUploadLay;
        @BindView(R.id.item_lay)
        CardView mItemLay;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
