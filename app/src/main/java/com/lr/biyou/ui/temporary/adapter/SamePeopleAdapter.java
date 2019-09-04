package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SamePeopleAdapter extends ListBaseAdapter {



    private LayoutInflater mLayoutInflater;

    public SamePeopleAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_same_people, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Map<String, Object> item = mDataList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        String idno = item.get("idno") + "";
        String relation = item.get("relation") + "";
        String name = item.get("name") + "";


        viewHolder.mNameValutTv.setText(name);
        viewHolder.mGxValueTv.setText("("+relation+")");
        viewHolder.mIdcardValueTv.setText(idno);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_valut_tv)
        TextView mNameValutTv;
        @BindView(R.id.gx_value_tv)
        TextView mGxValueTv;
        @BindView(R.id.idcard_value_tv)
        TextView mIdcardValueTv;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
