package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;
import com.lr.biyou.utils.tool.UtilTools;
import com.lr.biyou.mywidget.view.SwipeMenuView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShouKuanRenAdapter extends ListBaseAdapter {



    private LayoutInflater mLayoutInflater;

    public ShouKuanRenAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    private boolean mSwipeEnable = true;

    public boolean isSwipeEnable() {
        return mSwipeEnable;
    }

    public void setSwipeEnable(boolean swipeEnable) {
        mSwipeEnable = swipeEnable;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SuperViewHolder(mLayoutInflater.inflate(R.layout.item_shoukuanren_swipe, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SuperViewHolder viewHolder = (SuperViewHolder) holder;

        Map<String, Object> map = mDataList.get(position);

        //这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
        //((SwipeMenuView) holder.itemView).setIos(false).setLeftSwipe(position % 2 == 0 ? true : false);
        ((SwipeMenuView) holder.itemView).setIos(false).setLeftSwipe(true).setSwipeEnable(mSwipeEnable);;


        viewHolder.mNameValueTv.setText(map.get("accname")+"");
        viewHolder.mBankNameTv.setText(map.get("bankname")+"");
        viewHolder.mCardNumTv.setText(UtilTools.getIDXing(map.get("accid")+""));

        viewHolder.mMoneyTv.setText(MbsConstans.RMB+" "+UtilTools.getNormalMoney(map.get("amt")+""));


        viewHolder.mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(position);
                }
            }
        });
        //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
        viewHolder.mSwipeContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);

        void onTop(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    /**
     * ViewHolder基类
     */
    public class SuperViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.swipe_content)
        LinearLayout mSwipeContent;
        @BindView(R.id.name_value_tv)
        TextView mNameValueTv;
        @BindView(R.id.bank_name_tv)
        TextView mBankNameTv;
        @BindView(R.id.card_num_tv)
        TextView mCardNumTv;
        @BindView(R.id.btnDelete)
        Button mBtnDelete;
        @BindView(R.id.money_tv)
        TextView mMoneyTv;
        public SuperViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}

