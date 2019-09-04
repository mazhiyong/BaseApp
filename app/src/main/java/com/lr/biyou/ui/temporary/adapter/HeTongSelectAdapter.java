package com.lr.biyou.ui.temporary.adapter;

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
import com.lr.biyou.ui.temporary.activity.SelectPingZhengActivity;
import com.lr.biyou.utils.tool.UtilTools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class HeTongSelectAdapter extends RecyclerView.Adapter<HeTongSelectAdapter.MyHolder> {


    private OnChangeBankCardListener mOnChangeBankCardListener;

    public List<Map<String, Object>> getBooleanList() {
        return mBooleanList;
    }

    public void setBooleanList(List<Map<String, Object>> booleanList) {
        mBooleanList = booleanList;
    }

    private List<Map<String,Object>> mBooleanList = new ArrayList<>();

    private Context mContext;
    private List<Map<String, Object>> mDatas;

    private final int ITEM_TYPE_NORMAL = 0;
    private final int ITEM_TYPE_HEADER = 1;
    private final  int ITEM_TYPE_FOOTER = 2;
    private final int ITEM_TYPE_EMPTY = 3;
    private View mHeaderView;
    private View mFooterView;
    private View mEmptyView;

    public String getPatncode() {
        return mPatncode;
    }

    public void setPatncode(String patncode) {
        mPatncode = patncode;
    }

    private String mPatncode = "";

    private int mParentPosition;

    private LayoutInflater mLayoutInflater;



    private OnItemClickListener mClickListener;

    public void setClickListener(OnItemClickListener clickListener) {
        mClickListener = clickListener;
    }


    public HeTongSelectAdapter(Context context, List<Map<String, Object>> mDatas, OnChangeBankCardListener mOnChangeBankCardListener) {
        super();
        this.mContext = context;
        this.mDatas = mDatas;
        this.mOnChangeBankCardListener = mOnChangeBankCardListener;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        int itemCount = mDatas.size();
        if (null != mEmptyView && itemCount == 0) {
            itemCount++;
        }
        if (null != mHeaderView) {
            itemCount++;
        }
        if (null != mFooterView) {
            itemCount++;
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (null != mHeaderView && position == 0) {
            return ITEM_TYPE_HEADER;
        }
        if (null != mFooterView
                && position == getItemCount() - 1) {
            return ITEM_TYPE_FOOTER;
        }
        if (null != mEmptyView && mDatas.size() == 0){
            return ITEM_TYPE_EMPTY;
        }
        return ITEM_TYPE_NORMAL;
    }

    public void addHeaderView(View view) {
        mHeaderView = view;
        notifyItemInserted(0);
    }

    public void addFooterView(View view) {
        mFooterView = view;
        notifyItemInserted(getItemCount() - 1);
    }

    public void setEmptyView(View view) {
        mEmptyView = view;
        notifyDataSetChanged();
    }



    @Override
    // 填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(final MyHolder holder, final int position) {
        int type = getItemViewType(position);
        if (type == ITEM_TYPE_HEADER) {

        }else if (type == ITEM_TYPE_FOOTER){

            //添加合同
            holder.addHtLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String,Object> map = new HashMap<>();
                    if (mOnChangeBankCardListener != null){
                        mOnChangeBankCardListener.onButClickListener("1",map);
                    }
                }
            });
        }else if (type == ITEM_TYPE_EMPTY){

        }else {
            //合同列表
            Map<String, Object> map = mDatas.get(position);
            Map<String, Object> bo = mBooleanList.get(position);
            holder.mHetongCodeTv.setText(map.get("contno")+"");
            holder.mHetongDateTv.setText(map.get("contsgndt")+"");
            holder.mHetongMoneyTv.setText(UtilTools.getMoney(map.get("contmny")+""));

           List<Map<String,Object>> mDataChildList = (List<Map<String, Object>>) bo.get("list");

            boolean b = (Boolean) bo.get("selected");

            if (b){
                //holder.mCheckBox.setChecked(true);
                holder.mShowTv.setText("收起");
                holder.mRecyclerView.setVisibility(View.VISIBLE);

            }else {
                //holder.mCheckBox.setChecked(false);
                holder.mShowTv.setText("展开");
                holder.mRecyclerView.setVisibility(View.GONE);
            }

          if (holder.mShowTv.getText().toString().equals("收起")){
              holder.mRecyclerView.setVisibility(View.VISIBLE);
          }else {
              holder.mRecyclerView.setVisibility(View.GONE);
          }


          /*  holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mContentLay.performClick();
                }
            });*/

            holder.mShowTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mContentLay.performClick();
                }
            });

            holder.mContentLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (Map m:mBooleanList){
                        Map<String,Object> mSelectMap = (Map<String, Object>) m.get("value");
                        if (mSelectMap == map){
                            boolean b = (boolean) m.get("selected");
                            if (b){
                                m.put("selected",false);
                            }else {
                                m.put("selected",true);
                            }
                        }else {
                            m.put("selected",false);
                        }
                    }
                    if (mOnChangeBankCardListener != null && holder.mShowTv.getText().toString().trim().equals("展开")){
                        mOnChangeBankCardListener.onButClickListener("2",map);
                        holder.mShowTv.setText("收起");
                    }else {
                        holder.mShowTv.setText("展开");
                        holder.mRecyclerView.setVisibility(View.GONE);
                    }

                }


            });

            holder.mRelativeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SelectPingZhengActivity.class);
                    intent.putExtra("DATA", (Serializable) map);
                    mContext.startActivity(intent);
                }
            });



           List<Map<String,Object>> mBooleanChildList = new ArrayList<>();
            for (Map m : mDataChildList) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put("value", m);
                String status = m.get("poolsta")+"";
                if (status.equals("2")){
                    map2.put("selected",false);
                }else {
                    map2.put("selected",true);
                }
                mBooleanChildList.add(map2);
            }


            //dataType 数据类型 ：0 凭证  1 发票
            int dataType = 0;
            PingZhengSelectAdapter mAdapter = new PingZhengSelectAdapter(mContext,map,dataType);
            mAdapter.setBooleanList(mBooleanChildList);
            mAdapter.addAll(mDataChildList);

            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            holder.mRecyclerView.setLayoutManager(manager);
            holder.mRecyclerView.setAdapter(mAdapter);


            mAdapter.setOnChildClickListener(new OnChildClickListener() {
                @Override
                public void onChildClickListener(View view, int position, Map<String, Object> mParentMap) {

                    holder.mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                            //HeTongSelectAdapter.this.notifyDataSetChanged();
                        }
                    });
                    if (mClickListener!=null){
                        mClickListener.OnMyItemClickListener(mAdapter.getBooleanList(),mParentMap);
                    }
                }
            });
        }
    }

    // Generate palette synchronously and return it


    @Override
    // 重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    public MyHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        if (arg1 == ITEM_TYPE_HEADER) {
            return new MyHolder(mHeaderView,ITEM_TYPE_HEADER);
        } else if (arg1 == ITEM_TYPE_EMPTY) {
            return new MyHolder(mEmptyView,ITEM_TYPE_EMPTY);
        } else if (arg1 == ITEM_TYPE_FOOTER) {
            return new MyHolder(mFooterView,ITEM_TYPE_FOOTER);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_hetong_select, arg0,false);
            MyHolder holder = new MyHolder(view,ITEM_TYPE_NORMAL);
            return holder;
        }
    }

    // 定义内部类继承ViewHolder
    class MyHolder extends RecyclerView.ViewHolder {

        CardView addHtLay;
        TextView mHetongCodeTv;
        TextView mHetongDateTv;
        TextView mHetongMoneyTv;
        LinearLayout mContentLay;
        TextView mShowTv;
        TextView mRelativeTv;
        RecyclerView mRecyclerView;
        CheckBox mCheckBox;

        public MyHolder(View view,int type) {
            super(view);
            switch (type){
                case ITEM_TYPE_HEADER:
                    break;
                case ITEM_TYPE_FOOTER:
                    addHtLay = view.findViewById(R.id.hetong_add_lay);
                    break;
                case ITEM_TYPE_EMPTY:
                    break;
                case ITEM_TYPE_NORMAL:
                    mHetongCodeTv = view.findViewById(R.id.hetong_code_tv);
                    mHetongDateTv = view.findViewById(R.id.hetong_date_tv);
                    mHetongMoneyTv = view.findViewById(R.id.hetong_money_tv);
                    mContentLay = view.findViewById(R.id.content_lay);
                    mCheckBox = view.findViewById(R.id.check_box);
                    mShowTv = view.findViewById(R.id.tv_show);
                    mRecyclerView = view.findViewById(R.id.rcv);
                    mRelativeTv = view.findViewById(R.id.tv_relative);
                    break;
            }
        }
    }


    public interface OnChangeBankCardListener {
        void onButClickListener(String type,Map<String,Object> map);
    }

    public interface OnItemClickListener {
        void OnMyItemClickListener(List<Map<String, Object>>  list,Map<String,Object> mParentMap);
    }
}