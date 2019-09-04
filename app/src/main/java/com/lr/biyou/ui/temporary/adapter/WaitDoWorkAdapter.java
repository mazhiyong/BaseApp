package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.ui.temporary.activity.ApplyAmountActivity;
import com.lr.biyou.ui.temporary.activity.BorrowDetailActivity;
import com.lr.biyou.ui.moudle4.activity.PayMoneyActivity;
import com.lr.biyou.ui.temporary.activity.PeopleCheckActivity;
import com.lr.biyou.ui.temporary.activity.SignLoanActivity;
import com.lr.biyou.utils.tool.UtilTools;

import java.io.Serializable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WaitDoWorkAdapter  extends ListBaseAdapter {
    private LayoutInflater mLayoutInflater;

    public WaitDoWorkAdapter(Context context) {
        mLayoutInflater =LayoutInflater.from(context);
        mContext=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.wait_do_list_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder= (ViewHolder) holder;
        final Map<String, Object> data = mDataList.get(position);
        // 1 预授信回退列表  2授信签署列表 3借款进度列表 4待还款列表  5共同借款人审核列表
        final String type= (String)data.get("type");
        String tvType="";
        String tvDate="";
        String tvMoney="";
        String tvBank_or_Date="";
        String tvStatus="";
        switch (type){
            case"1"://预授信回退列表
                ((ViewHolder) holder).mImageView.setImageResource(R.drawable.shenqing);
               /*  tvType=data.get("");
                 tvDate=data.get("");
                 tvMoney=data.get("");
                 tvBank_or_Date=data.get("");
                 tvDate=data.get("");*/
                tvType = "授信";

                tvDate = data.get("sqdate")+"";
                //tvDate = UtilTools.getStringFromSting2(tvDate,"")
                tvMoney = UtilTools.getRMBMoney(data.get("creditmoney")+"");
                tvBank_or_Date = data.get("zifangnme")+"";
                tvStatus = "去授信";
                viewHolder.mDateOrBankTv.setVisibility(View.VISIBLE);
                break;
            case "2"://授信签署列表
                ((ViewHolder) holder).mImageView.setImageResource(R.drawable.renzheng);
                tvType="签署";
                tvDate = data.get("flowdate")+"";
                //tvDate = UtilTools.getStringFromSting2(tvDate,"")
                tvMoney = UtilTools.getRMBMoney(data.get("creditmoney")+"");
                tvBank_or_Date = data.get("zifangnme")+"";
                String status = data.get("qsstate")+"";
                if (status.equals("0")){
                    tvStatus="去签署";
                }else if (status.equals("1")){
                    tvStatus="处理中";
                }
                viewHolder.mDateOrBankTv.setVisibility(View.VISIBLE);
                break;
            case "3"://借款进度列表
                ((ViewHolder) holder).mImageView.setImageResource(R.drawable.bromoney);
                tvType="借款";
                tvDate = data.get("flowdate")+"";
                //tvDate = UtilTools.getStringFromSting2(tvDate,"")
                tvMoney = UtilTools.getRMBMoney(data.get("reqmoney")+"");
                tvBank_or_Date = data.get("loanstepdesc")+"";
                tvStatus="放款审核中";
                viewHolder.mDateOrBankTv.setVisibility(View.GONE);
                break;
            case "4"://待还款列表
                ((ViewHolder) holder).mImageView.setImageResource(R.drawable.paymoney);
                tvType="还款";
                tvDate = data.get("repaydate")+"";
                //tvDate = UtilTools.getStringFromSting2(tvDate,"")
                tvMoney = UtilTools.getRMBMoney(data.get("repayamt")+"");
                String dateStr = data.get("repaydate")+"";
                dateStr ="截止还款日"+UtilTools.getStringFromSting2(dateStr,"yyyyMMdd","yyyy年MM月dd日");
                tvBank_or_Date = dateStr;
                tvStatus="去还款";
                viewHolder.mDateOrBankTv.setVisibility(View.VISIBLE);
                break;

            case "5"://共同借款人审核列表
                ((ViewHolder) holder).mImageView.setImageResource(R.drawable.qita);
                tvType="审核";
                tvDate = "";
                //tvDate = UtilTools.getStringFromSting2(tvDate,"")
                tvMoney = UtilTools.getRMBMoney(data.get("creditmoney")+"");
                tvBank_or_Date = data.get("firmname")+"";
                tvStatus="待审核";
                viewHolder.mDateOrBankTv.setVisibility(View.VISIBLE);
                break;
        }
        viewHolder.mTypeTv.setText(tvType);
        viewHolder.mDateTv.setText(tvDate);
        viewHolder.mMoneyTv.setText(tvMoney);
        viewHolder.mDateOrBankTv.setText(tvBank_or_Date);
        viewHolder.mStatusTv.setText(tvStatus);

        viewHolder.mItemLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //根据列表类型进行不同的处理操作
                // 1 预授信回退列表  2授信签署列表 3借款进度列表 4待还款列表  5共同借款人审核列表
                Intent intent;
                switch (type){
                    case "1":
                        intent= new Intent(mContext,ApplyAmountActivity.class);
                        intent.putExtra("TYPE",1);
                        intent.putExtra("precreid",data.get("precreid")+"");
                        mContext.startActivity(intent);
                        break;
                    case "2":
                        intent=new Intent(mContext, SignLoanActivity.class);
                        intent.putExtra("DATA",(Serializable) data);
                        intent.putExtra("status",data.get("qsstate")+"");
                        mContext.startActivity(intent);
                        break;
                    case "3":
                        intent=new Intent(mContext, BorrowDetailActivity.class);
                        intent.putExtra("DATA",(Serializable) data);
                        mContext.startActivity(intent);
                        break;
                    case "4":
                        intent=new Intent(mContext, PayMoneyActivity.class);
                        intent.putExtra("DATA",(Serializable) data);
                        mContext.startActivity(intent);
                        break;
                    case "5":
                        intent=new Intent(mContext, PeopleCheckActivity.class);
                        intent.putExtra("DATA",(Serializable) data);
                        mContext.startActivity(intent);
                        break;

                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_type)
        ImageView mImageView;
        @BindView(R.id.tv_tvpe)
        TextView mTypeTv;
        @BindView(R.id.tv_date)
        TextView mDateTv;
        @BindView(R.id.tv_money)
        TextView mMoneyTv;
        @BindView(R.id.tv_bank_or_date)
        TextView mDateOrBankTv;
        @BindView(R.id.status_tv)
        TextView mStatusTv;
        @BindView(R.id.item_waitdo)
        CardView mItemLay;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
