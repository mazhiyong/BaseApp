package com.lr.biyou.ui.temporary.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.listener.OnChildClickListener;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.ui.temporary.activity.InvoiceActivity;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 */
public class PingZhengSelectAdapter extends ListBaseAdapter {

    private List<Map<String, Object>> mBooleanList;

    private Map<String,Object> mHeTongMap ;

    public void setBooleanList(List<Map<String, Object>> booleanList) {
        mBooleanList = booleanList;
    }

    public List<Map<String, Object>> getBooleanList() {
        return mBooleanList;
    }

    private OnChildClickListener onChildClickListener;

    public void setOnChildClickListener(OnChildClickListener onChildClickListener) {
        this.onChildClickListener = onChildClickListener;
    }


    private LayoutInflater mLayoutInflater;
    private int mW = 0;

    private int ITEM_TYPE = -1;
    private int dataType;

    public PingZhengSelectAdapter(Context context,Map<String,Object> mHeTongMap,int dataType) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.dataType = dataType;
        this.mHeTongMap = mHeTongMap;
    }

    @Override
    public int getItemCount() {
        if (mDataList.size() == 0){
            ITEM_TYPE = 0;
            return 1;
        }else {
            ITEM_TYPE = 1;
            return mDataList.size();
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_pingzheng_list, parent, false));
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
         if (ITEM_TYPE == 0){ //无数据
             viewHolder.mContentLay.setVisibility(View.GONE);
             viewHolder.mTipTv.setVisibility(View.VISIBLE);
             switch (dataType){
                 case 0 ://凭证
                     viewHolder.mTipTv.setText("暂无相关凭证数据");
                     break;
                 case 1: //发票
                     viewHolder.mTipTv.setText("导入发票数据");
                     viewHolder.mTipTv.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             mContext.startActivity(new Intent(mContext, InvoiceActivity.class));
                         }
                     });
                     break;
             }
         }else {  //有数据
             viewHolder.mContentLay.setVisibility(View.VISIBLE);
             switch (dataType){
                 case 0 ://凭证
                     viewHolder.mTipTv.setVisibility(View.GONE);
                     break;
                 case 1: //发票
                     if ((position+1)==mDataList.size()){ //列项最后一个显示
                         viewHolder.mTipTv.setVisibility(View.VISIBLE);
                         viewHolder.mTipTv.setText("导入发票数据");
                         viewHolder.mTipTv.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 mContext.startActivity(new Intent(mContext, InvoiceActivity.class));
                             }
                         });
                     }else {
                         viewHolder.mContentLay.setVisibility(View.VISIBLE);
                         viewHolder.mTipTv.setVisibility(View.GONE);
                     }
                     break;
             }



             final Map<String, Object> item = mDataList.get(position);

             final Map<String,Object> bMap = mBooleanList.get(position);

             boolean isSelect = (Boolean) bMap.get("selected");
             viewHolder.mCheckBox.setChecked(isSelect);

             String status = item.get("poolsta")+"";
             if (status.equals("2")){
                 viewHolder.mContentLay.setBackgroundResource(R.color.whitecc);
                 viewHolder.mAllContentLay.setEnabled(false);
                 viewHolder.mCheckBox.setEnabled(false);
                 viewHolder.mCheckBox.setVisibility(View.GONE);
                 viewHolder.mCheckBox.setChecked(false);
                 viewHolder.mContentLay.setEnabled(false);
             }else {
                 viewHolder.mContentLay.setBackgroundResource(R.color.white);
                 viewHolder.mAllContentLay.setEnabled(true);
                 viewHolder.mCheckBox.setEnabled(true);
                 viewHolder.mCheckBox.setVisibility(View.VISIBLE);
                 viewHolder.mCheckBox.setChecked(isSelect);
                 viewHolder.mContentLay.setEnabled(true);
             }

      /*  for (Map<String,Object> map: mBooleanList){
            Map<String,Object> m = (Map<String, Object>) map.get("value");
            if (m == item){
                boolean b = (boolean) map.get("selected");
                if (b){
                    viewHolder.mCheckBox.setChecked(true);
                }else {
                    viewHolder.mCheckBox.setChecked(false);
                }
            }
        }*/

             viewHolder.mNameTv.setText(item.get("payfirmname") + "");
             viewHolder.mNumberTv.setText(item.get("billid")+"");
             viewHolder.mMoneyTv.setText(UtilTools.getRMBMoney(item.get("paymoney")+""));


             viewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     viewHolder.mContentLay.performClick();

                 }
             });


             viewHolder.mContentLay.setOnClickListener(new View.OnClickListener() {
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

                     if (onChildClickListener != null){
                         onChildClickListener.onChildClickListener(viewHolder.mContentLay,position,mHeTongMap);
                         notifyDataSetChanged();
                     }


                 }
             });

         }


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
        @BindView(R.id.number_tv)
        TextView mNumberTv;
        @BindView(R.id.name_tv)
        TextView mNameTv;
        @BindView(R.id.money_tv)
        TextView mMoneyTv;
        @BindView(R.id.content_lay)
        LinearLayout mContentLay;
        @BindView(R.id.check_box)
        CheckBox mCheckBox;
        @BindView(R.id.tv_tip)
        TextView mTipTv;
        @BindView(R.id.all_content_lay)
        LinearLayout mAllContentLay;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
