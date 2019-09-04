package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.listener.OnItemClickListener;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.ui.temporary.activity.AdvancePayListActivity;
import com.lr.biyou.ui.temporary.activity.BorrowMoneyActivity;
import com.lr.biyou.ui.temporary.activity.SelectFukuanFangActivity;
import com.lr.biyou.utils.tool.UtilTools;

import java.io.Serializable;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
public class BorrowSelectAdapter extends ListBaseAdapter {


    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private OnItemClickListener mOnItemClickListener;

    private LayoutInflater mLayoutInflater;
    private int mW = 0;

    public BorrowSelectAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_borrow_select_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);

        final ViewHolder viewHolder = (ViewHolder) holder;
        String type = item.get("creditcd")+"";
        switch (type){
            case "L03":
                viewHolder.mTypeTv.setText("应收账款池");
                viewHolder.mGoBorrow.setVisibility(View.VISIBLE);
                break;
            case "L11":
                viewHolder.mTypeTv.setText("信用融资");
                viewHolder.mGoBorrow.setVisibility(View.VISIBLE);
                break;
            case "L08":
                viewHolder.mTypeTv.setText("预付款融资");
                viewHolder.mGoBorrow.setVisibility(View.VISIBLE);
                break;
            default:
                viewHolder.mTypeTv.setText("未知");
                viewHolder.mGoBorrow.setVisibility(View.INVISIBLE);
                break;
        }

        String money = UtilTools.getRMBMoney(item.get("leftmoney") + "");
        viewHolder.mAmountTv.setText(money);
        viewHolder.mRateTv.setText(UtilTools.getlilv(item.get("daiklilv")+""));
        viewHolder.mBorrowTv.setText(item.get("zifangnme")+"");
        viewHolder.mGoBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  if (mOnItemClickListener != null){
                    mOnItemClickListener.onItemClickListener(viewHolder.mGoBorrow,position,item);
                }*/
                Intent intent;
                switch (type){
                    case "L03":
                        //多个付款方
                        intent = new Intent(mContext, SelectFukuanFangActivity.class);
                        intent.putExtra("DATA",(Serializable) item);
                        mContext.startActivity(intent);
                        break;
                    case "L11":
                        intent = new Intent(mContext, BorrowMoneyActivity.class);
                        intent.putExtra("DATA",(Serializable) item);
                        mContext.startActivity(intent);
                        break;
                    case "L08":
                        intent = new Intent(mContext, AdvancePayListActivity.class);
                        intent.putExtra("DATA",(Serializable) item);
                        mContext.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.type_tv)
        TextView mTypeTv;
        @BindView(R.id.amount_tv)
        TextView mAmountTv;
        @BindView(R.id.rate_tv)
        TextView mRateTv;
        @BindView(R.id.borrow_tv)
        TextView mBorrowTv;
        @BindView(R.id.go_borrow)
        TextView mGoBorrow;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
