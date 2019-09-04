package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.ui.temporary.activity.BorrowDetailActivity;
import com.lr.biyou.ui.moudle4.activity.PayMoneyActivity;
import com.lr.biyou.utils.tool.UtilTools;
import com.lr.biyou.mywidget.view.SwipeMenuView;

import java.io.Serializable;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceRemindInfoAdapter extends ListBaseAdapter {
    private LayoutInflater mLayoutInflater;

    //滑动效果按钮的监听
    public interface onSwipeListener {
        //删除
        void onDel(int pos);
        //置顶
        void onTop(int pos);
    }

    private SwipeMenuAdapter2.onSwipeListener mOnSwipeListener;

    public SwipeMenuAdapter2.onSwipeListener getOnSwipeListener() {
        return mOnSwipeListener;
    }

    public void setOnSwipeListener(SwipeMenuAdapter2.onSwipeListener onSwipeListener) {
        mOnSwipeListener = onSwipeListener;
    }

    public ServiceRemindInfoAdapter(Context context) {
        mLayoutInflater =LayoutInflater.from(context);
        mContext=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHonlder(mLayoutInflater.inflate(R.layout.service_remind_list_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHonlder  viewHolder= (ViewHonlder) holder;
        final Map<String, Object> data = mDataList.get(position);
        // 1 预授信回退列表  2授信签署列表 3借款进度列表 4待还款列表  5共同借款人审核列表
        final String type= (String)data.get("type");
        String tvType="";
        String tvDate="";
        String tvMoney="";
        String tvBank_or_Date="";
        String tvStatus="";
        switch (type){
            case"1":
                viewHolder.mImageView.setImageResource(R.drawable.shenqing);
               /*  tvType=data.get("");
                 tvDate=data.get("");
                 tvMoney=data.get("");
                 tvBank_or_Date=data.get("");
                 tvDate=data.get("");*/
                tvType="授信";
                tvDate="2018-09-19 14:00";
                tvMoney= UtilTools.getRMBMoney("100000000");
                tvBank_or_Date="郑州银行金水区支行";
                tvStatus="去授信";

                break;
            case "2":
                viewHolder.mImageView.setImageResource(R.drawable.renzheng);
                tvType="签署";
                tvDate="2018-09-19 14:00";
                tvMoney=UtilTools.getRMBMoney("100000000");
                tvBank_or_Date="郑州银行金水区支行";
                tvStatus="去签署";
                break;
            case "3":
                viewHolder.mImageView.setImageResource(R.drawable.bromoney);
                tvType="借款";
                tvDate="2018-09-19 14:00";
                tvMoney=UtilTools.getRMBMoney("100000000");
                tvBank_or_Date="郑州银行金水区支行";
                tvStatus="借款中";
                break;
            case "4":
                viewHolder.mImageView.setImageResource(R.drawable.paymoney);
                tvType="还款";
                tvDate="2018-09-19 14:00";
                tvMoney=UtilTools.getRMBMoney("100000000");
                tvBank_or_Date="4月25日应还金额";
                tvStatus="去还款";
                break;

            case "5":
                viewHolder.mImageView.setImageResource(R.drawable.qita);
                tvType="审核";
                tvDate="2018-09-19 14:00";
                tvMoney=UtilTools.getRMBMoney("100000000");
                tvBank_or_Date="郑州银行金水区支行";
                tvStatus="待审核";
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


                        break;
                    case "2":


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


                        break;

                }
            }
        });
        //滑动删除
        ((SwipeMenuView)viewHolder.itemView).setIos(true).setLeftSwipe(true);
        viewHolder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(null!=mOnSwipeListener){
                     mOnSwipeListener.onDel(position);

                 }
            }
        });

    }

    class ViewHonlder extends RecyclerView.ViewHolder{

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
        @BindView(R.id.bt_Delete)
        Button btDelete;

        public ViewHonlder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
