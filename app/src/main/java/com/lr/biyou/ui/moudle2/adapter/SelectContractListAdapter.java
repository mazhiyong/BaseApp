package com.lr.biyou.ui.moudle2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.R;
import com.lr.biyou.listener.OnChildClickListener;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.utils.imageload.GlideUtils;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectContractListAdapter extends ListBaseAdapter {

    private List<Map<String, Object>> mBooleanList;

    public void setBooleanList(List<Map<String, Object>> booleanList) {
        mBooleanList = booleanList;
    }

    public List<Map<String, Object>> getBooleanList() {
        return mBooleanList;
    }

    private LayoutInflater mLayoutInflater;

    private OnChildClickListener mListener;

    public void setmListener(OnChildClickListener mListener) {
        this.mListener = mListener;
    }

    public SelectContractListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_select_contract, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;
        final Map<String,Object> bMap = mBooleanList.get(position);

        boolean isSelect = (Boolean) bMap.get("selected");
        viewHolder.mCheckBox.setChecked(isSelect);
        viewHolder.nameTv.setText(item.get("name") + "");
        GlideUtils.loadImage(mContext,item.get("portrait")+"",viewHolder.headIv);


        viewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.tradeLay.performClick();
            }
        });

        viewHolder.tradeLay.setOnClickListener(new View.OnClickListener() {
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

                if (mListener != null){
                    mListener.onChildClickListener(viewHolder.tradeLay,position,item);
                    notifyDataSetChanged();
                }
            }
        });



    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.check_box)
        CheckBox mCheckBox;
        @BindView(R.id.head_iv)
        ImageView headIv;
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.agree_tv)
        TextView agreeTv;
        @BindView(R.id.refuse_tv)
        TextView refuseTv;
        @BindView(R.id.added_tv)
        TextView addedTv;
        @BindView(R.id.trade_lay)
        CardView tradeLay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
