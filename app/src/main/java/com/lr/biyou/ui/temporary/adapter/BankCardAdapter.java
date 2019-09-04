package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BankCardAdapter extends ListBaseAdapter {

    public OnChangeBankCardListener getOnChangeBankCardListener() {
        return mOnChangeBankCardListener;
    }

    public void setOnChangeBankCardListener(OnChangeBankCardListener onChangeBankCardListener) {
        mOnChangeBankCardListener = onChangeBankCardListener;
    }

    private OnChangeBankCardListener mOnChangeBankCardListener;
    private LayoutInflater mLayoutInflater;
    public BankCardAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_bank_card, parent, false));
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;


        String type = item.get("my_type")+"";
        if (type.equals("0")){
            viewHolder.mTitleTv.setVisibility(View.GONE);
        }else {
            viewHolder.mTitleTv.setText(item.get("orgname")+"");
            viewHolder.mTitleTv.setVisibility(View.VISIBLE);
        }


//            holder.mViewPager.setPageMargin((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
//                    48, mContext.getResources().getDisplayMetrics()));
        // holder.mViewPager.setPageTransformer(false, new ScaleTransformer(this));

        List<Map<String,Object>> childList = new ArrayList<>();
        childList = (List<Map<String,Object>>) item.get("card_list");

        viewHolder.mRecyclerView.setHasFixedSize(true);
        viewHolder.mRecyclerView.setNestedScrollingEnabled(false);
        viewHolder.mRecyclerView.setItemViewCacheSize(20);
        if (childList != null && childList.size()>0){

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            viewHolder.mRecyclerView.setLayoutManager(linearLayoutManager);
            BankCardChildAdapter mBandCardChildAdapter = new BankCardChildAdapter(mContext,childList,1,mOnChangeBankCardListener,position);
            viewHolder.mRecyclerView.setAdapter(mBandCardChildAdapter);
        }else {
            if (childList== null){
                childList = new ArrayList<>();
            }
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_bank_bind, null);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            viewHolder.mRecyclerView.setLayoutManager(linearLayoutManager);
            BankCardChildAdapter mBandCardChildAdapter = new BankCardChildAdapter(mContext,childList,0,mOnChangeBankCardListener,position);
            mBandCardChildAdapter.addHeaderView(view);
            mBandCardChildAdapter.setPatncode(item.get("patncode")+"");
            viewHolder.mRecyclerView.setAdapter(mBandCardChildAdapter);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTv;
        private RecyclerView mRecyclerView;
        private ViewPager mViewPager;
        public ViewHolder(View itemView) {
            super(itemView);
            mTitleTv = itemView.findViewById(R.id.bank_title_tv);
            mRecyclerView = itemView.findViewById(R.id.bank_child_recycleview);
            mViewPager = itemView.findViewById(R.id.viewpager);
        }
    }


    public interface OnChangeBankCardListener {
        /*void onAddNewBanCardListener(Map<String,Object> map);
        void onShowMoney(Map<String,Object> map);*/
        void onButClickListener(String type,Map<String,Object> map,BankCardChildAdapter bankCardChildAdapter);
    }
}
