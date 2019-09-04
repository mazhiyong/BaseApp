package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.activity.PDFLookActivity;

import java.util.List;
import java.util.Map;


public class JkHetongAdapter extends RecyclerView.Adapter<JkHetongAdapter.MyHolder> {


    private Context mContext;
    private List<Map<String, Object>> mDatas;

    public JkHetongAdapter(Context context, List<Map<String, Object>> mDatas) {
        super();
        this.mContext = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getItemCount() {
        int itemCount = mDatas.size();
        return itemCount;
    }


    @Override
    // 填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(MyHolder holder, final int position) {
        final Map<String, Object> map = mDatas.get(position);
        holder.mBankNameTv.setText(map.get("pdfname") + "");
        holder.mContentLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,PDFLookActivity.class);
                intent.putExtra("id",map.get("pdfurl")+"");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    // 重写onCreateViewHolder方法，返回一个自定义的ViewHolder
    public MyHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_jk_hetong, null);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // 定义内部类继承ViewHolder
    class MyHolder extends RecyclerView.ViewHolder {
        TextView mBankNameTv;
        RelativeLayout mContentLay;
        public MyHolder(View view) {
            super(view);
            mBankNameTv = view.findViewById(R.id.item_name);
            mContentLay = view.findViewById(R.id.content_lay);
        }

    }


}