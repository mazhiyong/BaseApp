package com.lr.biyou.ui.temporary.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.listener.OnItemClickListener;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.Map;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 预付款  列表  adapter
 */
public class AdvanceListAdapter extends ListBaseAdapter {


    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private OnItemClickListener mOnItemClickListener;

    private LayoutInflater mLayoutInflater;

    public AdvanceListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_advance_list, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);

        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.mQdDateTv.setText(item.get("sgndt")+"");
        viewHolder.mGhsTv.setText(item.get("ghsnm")+"");
        viewHolder.mHtCodeTv.setText(item.get("billid")+"");
        viewHolder.mAllJineTv.setText(UtilTools.getRMBMoney(item.get("totalamt")+""));

        viewHolder.mGoBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null){
                    mOnItemClickListener.onItemClickListener(viewHolder.mGoBorrow,position,item);
                }
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.qd_date_tv)
        TextView mQdDateTv;
        @BindView(R.id.ghs_tv)
        TextView mGhsTv;
        @BindView(R.id.ht_code_tv)
        TextView mHtCodeTv;
        @BindView(R.id.all_jine_tv)
        TextView mAllJineTv;
        @BindView(R.id.go_borrow)
        TextView mGoBorrow;
        @BindView(R.id.item_lay)
        CardView mItemLay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
