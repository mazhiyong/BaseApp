package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.utils.imageload.GlideUtils;
import com.lr.biyou.utils.tool.PingYinUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BankNameListAdapter extends ListBaseAdapter {

    private LayoutInflater mLayoutInflater;

    public BankNameListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_bank_name, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;



        String catalog =  PingYinUtil.getPingYin((item.get("opnbnknm")+"").substring(0, 1));
        if(position == 0){
            viewHolder.mTitleTv.setVisibility(View.VISIBLE);
            viewHolder.mTitleTv.setText(catalog);
        }else{
            String lastCatalog =  PingYinUtil.getPingYin(((mDataList.get(position-1)).get("opnbnknm")+"").substring(0,1));
            if(catalog.equals(lastCatalog)){
                viewHolder.mTitleTv.setVisibility(View.GONE);
            }else{
                viewHolder.mTitleTv.setVisibility(View.VISIBLE);
                viewHolder.mTitleTv.setText(catalog);
            }
        }


        String name = item.get("opnbnknm")+"";
        GlideUtils.loadCircleImage(mContext,item.get("logopath")+"",viewHolder.mImageView);

        viewHolder.mNameValueTv.setText(name);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_view)
        ImageView mImageView;
        @BindView(R.id.name_value_tv)
        TextView mNameValueTv;
        @BindView(R.id.title_tv)
        TextView mTitleTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public void simpleOrder(){
        if (mDataList != null && mDataList.size()>0) {
            try {
                Collections.sort(mDataList,new Comparator<Map<String,Object>>(){
                    @Override
                    public int compare(Map<String,Object> arg0, Map<String,Object> arg1) {
                        // TODO Auto-generated method stub
                        String str1 = PingYinUtil.getPingYin2(arg0.get("opnbnknm")+"");
                        String str2 = PingYinUtil.getPingYin2(arg1.get("opnbnknm")+"");
                        return str1.compareTo(str2);
                    }
                });
            } catch (ConcurrentModificationException e) {
            }
        }
    }


    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(String s) {
        for (int i = 0; i < getItemCount(); i++) {
            Map<String,Object> item = mDataList.get(i);
            String catalog =  PingYinUtil.getPingYin((item.get("opnbnknm")+"").substring(0, 1));
            if (catalog.equals(s)) {
                return i;
            }
        }
        return -1;
    }

}
