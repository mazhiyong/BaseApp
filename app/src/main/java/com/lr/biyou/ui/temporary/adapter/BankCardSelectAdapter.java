package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.listener.OnMyItemClickListener;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BankCardSelectAdapter extends RecyclerView.Adapter<BankCardSelectAdapter.ViewHolder>{

    private OnMyItemClickListener mOnMyItemClickListener;
    private int mSelectItem =0;
    private Context mContext;
    private List<Map<String,Object>>  mDatas;

    public List<Map<String, Object>> getDatas() {
        return mDatas;
    }

    public void setDatas(List<Map<String, Object>> datas) {
        mDatas = datas;
    }

    public OnMyItemClickListener getOnMyItemClickListener() {
        return mOnMyItemClickListener;
    }

    public int getSelectItem() {
        return mSelectItem;
    }

    public void setOnMyItemClickListener(OnMyItemClickListener onMyItemClickListener) {
        mOnMyItemClickListener = onMyItemClickListener;
    }

    public void setSelectItem(int selectItem) {
        mSelectItem = selectItem;
    }

    public BankCardSelectAdapter(Context context, List<Map<String, Object>> datas) {
        mContext = context;
        mDatas = datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.bank_card_item_layout, parent,false);
        ViewHolder hondler=new ViewHolder(view);
        return hondler;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Map<String,Object> item = mDatas.get(position);
        String accid = item.get("accid")+"";
        String accsn = item.get("accsn")+"";
        String bankName = item.get("opnbnknm")+"";
        String logopath = item.get("logopath")+"";
        if (UtilTools.empty(bankName) ){
            if (accsn.equals("D")){
                bankName = "交易账户";
            }else {
                bankName = "";
            }
        }
        //设置银行和后四位卡号
        holder.mTextView.setText(bankName+"("+UtilTools.getIDCardXing(accid) +")");
        //设置银行卡对应的Logo
        GlideUtils.loadImage(mContext,logopath,holder.mImageView,R.drawable.default_bank);

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnMyItemClickListener!=null){
                    mOnMyItemClickListener.OnMyItemClickListener(v,position);
                }
                mSelectItem=position;
                notifyDataSetChanged();
            }
        });

        if (position == mSelectItem){
            holder.mLayout.setSelected(true);
        }else {
            holder.mLayout.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class  ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_bank_ico)
        ImageView mImageView;
        @BindView(R.id.tv_bank_num)
        TextView mTextView;
        @BindView(R.id.ll_layout)
        LinearLayout mLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
