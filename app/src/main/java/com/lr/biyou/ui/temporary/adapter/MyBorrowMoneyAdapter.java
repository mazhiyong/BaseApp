package com.lr.biyou.ui.temporary.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;

import java.util.Map;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
public class MyBorrowMoneyAdapter extends ListBaseAdapter {


    private LayoutInflater mLayoutInflater;
    private int mW = 0;

    public MyBorrowMoneyAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_borrow_mymoney_list, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);

        final ViewHolder viewHolder = (ViewHolder) holder;


        viewHolder.mTypeTv.setText(item.get("type")+"");
        viewHolder.mStateTv.setText(item.get("state")+"");
        viewHolder.mDateTv.setText(item.get("date")+"");
        viewHolder.mMoneyTv.setText(item.get("money")+"");
        viewHolder.mRateTv.setText(item.get("rate")+"");
        viewHolder.mQixianTv.setText(item.get("qixian")+"");
        viewHolder.mNumberTv.setText(item.get("number")+"");
        viewHolder.mPeopleTv.setText(item.get("peopel")+"");


       /* String type = item.get("creditcd") + "";
        switch (type) {
            case "L03":
                viewHolder.mTypeTv.setText("应收账款池");
                break;
            case "L11":
                viewHolder.mTypeTv.setText("信用融资");
                break;
            default:
                viewHolder.mTypeTv.setText("未知");
                break;
        }*/



       viewHolder.mUploadTv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });

       viewHolder.mPlanTv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });

       viewHolder.mHuankuanTv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.type_tv)
        TextView mTypeTv;
        @BindView(R.id.state_tv)
        TextView mStateTv;
        @BindView(R.id.date_tv)
        TextView mDateTv;
        @BindView(R.id.money_tv)
        TextView mMoneyTv;
        @BindView(R.id.rate_tv)
        TextView mRateTv;
        @BindView(R.id.qixian_tv)
        TextView mQixianTv;
        @BindView(R.id.number_tv)
        TextView mNumberTv;
        @BindView(R.id.people_tv)
        TextView mPeopleTv;
        @BindView(R.id.upload_tv)
        TextView mUploadTv;
        @BindView(R.id.plan_tv)
        TextView mPlanTv;
        @BindView(R.id.huankuan_tv)
        TextView mHuankuanTv;
        @BindView(R.id.item_lay)
        CardView mItemLay;
        @BindView(R.id.linearLay)
        LinearLayout mLinearLay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
