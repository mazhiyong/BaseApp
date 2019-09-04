package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectBankAdapter extends ListBaseAdapter {


    private List<Boolean> mBooleanList;
    private LayoutInflater mLayoutInflater;

    private OnMyItemClickListener mOnMyItemClickListener;


    public OnMyItemClickListener getOnMyItemClickListener() {
        return mOnMyItemClickListener;
    }

    public void setOnMyItemClickListener(OnMyItemClickListener onMyItemClickListener) {
        mOnMyItemClickListener = onMyItemClickListener;
    }


    public List<Boolean> getBooleanList() {
        return mBooleanList;
    }

    public void setBooleanList(List<Boolean> booleanList) {
        mBooleanList = booleanList;
    }


    public SelectBankAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        boolean b = mBooleanList.get(position);
        if (b){
            viewHolder.mCheckBox.setChecked(true);
        }else {
            viewHolder.mCheckBox.setChecked(false);
        }


        viewHolder.mBankCardTv.setText(item.get("crdno")+"");
        viewHolder.mBankCardTv.setText(UtilTools.getIDXing(item.get("crdno")+""));

        viewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.mContentLay.performClick();
            }
        });

        viewHolder.mContentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0;i<mBooleanList.size();i++) {//全部设为未选中
                    boolean isChecked = mBooleanList.get(i);
                    if (i == position){
                        mBooleanList.set(position,!isChecked);
                    }else {
                        mBooleanList.set(i,false);
                    }
                }
                if (mOnMyItemClickListener != null){
                }
            }
        });

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_select_bank, parent, false));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.check_box)
        CheckBox mCheckBox;
        @BindView(R.id.uesr_name_tv)
        TextView mUserNameTv;
        @BindView(R.id.bank_name_tv)
        TextView mBankNameTv;
        @BindView(R.id.bank_card_tv)
        TextView mBankCardTv;
        @BindView(R.id.content_lay)
        LinearLayout mContentLay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
