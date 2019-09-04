package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.List;
import java.util.Map;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
public class ShouldShouMoneyAdapter extends ListBaseAdapter {


    private LayoutInflater mLayoutInflater;
    private int mW = 0;

    private List<Map<String, Object>> mBooleanList;

    public List<Map<String, Object>> getBooleanList() {
        return mBooleanList;
    }

    public void setBooleanList(List<Map<String, Object>> booleanList) {
        mBooleanList = booleanList;
    }

    private OnMyItemClickListener mOnMyItemClickListener;

    public void setOnMyItemClickListener(OnMyItemClickListener onMyItemClickListener) {
        mOnMyItemClickListener = onMyItemClickListener;
    }

    public ShouldShouMoneyAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_should_shou_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.mCheckBox.setChecked(false);
        for (Map<String,Object> map: mBooleanList){
            Map<String,Object> m = (Map<String, Object>) map.get("value");
            if (m == item){
                boolean b = (boolean) map.get("selected");
                if (b){
                    viewHolder.mCheckBox.setChecked(true);
                }else {
                    viewHolder.mCheckBox.setChecked(false);
                }
            }
        }


        viewHolder.mFukuanfangTv.setText(item.get("payfirmname")+"");
        viewHolder.mMoneyTv.setText(UtilTools.getRMBMoney(item.get("paymoney")+""));
        String status =  item.get("poolsta")+"";
        switch (status){
            case "0"://未入池
                viewHolder.mItemLay.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.wheat));

                break;
            case "2"://已入池
                viewHolder.mItemLay.setCardBackgroundColor(ContextCompat.getColor(mContext,R.color.white));

                break;
        }

        viewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.mItemLay.performClick();

            }
        });




        viewHolder.mItemLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Map map:mBooleanList){
                    Map<String,Object> mSelectMap = (Map<String, Object>) map.get("value");
                    if (mSelectMap ==  item){
                        boolean b = (boolean) map.get("selected");
                        if (b){
                            map.put("selected",false);
                        }else {
                            map.put("selected",true);
                        }
                    }
                }

                if (mOnMyItemClickListener != null){
                    mOnMyItemClickListener.OnMyItemClickListener(viewHolder.mCheckBox,position);
                }
            }
        });
    }


    public void cancelSelectAll(){
        for (Map map:mBooleanList){
            map.put("selected",false);
        }

        notifyDataSetChanged();
    }

    public void  selectAll(){
        for (Map map:mBooleanList){
            map.put("selected",true);
        }

        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.fukuanfang_tv)
        TextView mFukuanfangTv;
        @BindView(R.id.money_tv)
        TextView mMoneyTv;
        @BindView(R.id.item_lay)
        CardView mItemLay;
        @BindView(R.id.check_box)
        CheckBox mCheckBox;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
