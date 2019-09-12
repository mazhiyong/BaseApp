package com.lr.biyou.ui.moudle5.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lr.biyou.R;
import com.lr.biyou.listener.CallBackTotal;
import com.lr.biyou.listener.OnChildClickListener;
import com.lr.biyou.mywidget.view.SwipeMenuView;
import com.lr.biyou.ui.moudle.adapter.ListBaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 列表适配器
 */
public class AddressManageAdapter extends ListBaseAdapter implements Filterable {

    private OnChildClickListener mClickListener;

    public void setmClickListener(OnChildClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }
    private LayoutInflater mLayoutInflater;

    private List<Map<String, Object>> mSoorceList = new ArrayList<>();

    public List<Map<String, Object>> getSoorceList() {
        return mSoorceList;
    }

    public void setSoorceList(List<Map<String, Object>> soorceList) {
        mSoorceList = soorceList;
    }

    public LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    //滑动效果按钮的监听
    public interface onSwipeListener {
        //删除
        void onDel(int pos);

        //置顶
        void onTop(int pos);
    }

    private SwipeMenuAdapter.onSwipeListener mOnSwipeListener;

    public SwipeMenuAdapter.onSwipeListener getOnSwipeListener() {
        return mOnSwipeListener;
    }

    public void setOnSwipeListener(SwipeMenuAdapter.onSwipeListener onSwipeListener) {
        mOnSwipeListener = onSwipeListener;
    }

    public AddressManageAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public CallBackTotal mBackTotal;

    public CallBackTotal getBackTotal() {
        return mBackTotal;
    }

    public void setBackTotal(CallBackTotal backTotal) {
        mBackTotal = backTotal;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHonlder(mLayoutInflater.inflate(R.layout.address_manage_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHonlder viewHolder = (ViewHonlder) holder;
        final Map<String, Object> data = mDataList.get(position);
        viewHolder.typeTv.setText(data.get("text") + "");
        viewHolder.linkTv.setText(data.get("address") + "");
        //查看详情
        viewHolder.llItemLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null){
                    mClickListener.onChildClickListener(viewHolder.llItemLay,position,data);
                }
            }
        });
        //滑动删除
        ((SwipeMenuView) viewHolder.itemView).setIos(true).setLeftSwipe(true);
        viewHolder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onViewClick(data, 1);
                }
            }
        });

    }

    //过滤器
    @Override
    public Filter getFilter() {
        return new Filter() {
            //执行过滤操作
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charStr = charSequence.toString();
                if (charStr.isEmpty()) {
                    //没有过滤的内容，则使用源数据
                    mDataList = mSoorceList;
                } else {
                    List<Map<String, Object>> mFilterList = new ArrayList<>();
                    for (Map<String, Object> map : mSoorceList) {
                        String key1 = map.get("name") + "";
                        String key2 = map.get("mname") + "";
                        String key3 = map.get("legalName") + "";
                        String key4 = map.get("telephone") + "";
                        if (key1.contains(charStr) || key2.contains(charStr) || key3.contains(charStr) || key4.contains(charStr)) {
                            mFilterList.add(map);
                        }
                    }
                    mDataList = mFilterList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataList;
                return filterResults;
            }

            //把过滤后的值返回出来
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mDataList = (List<Map<String, Object>>) filterResults.values;
                notifyDataSetChanged();
                if (mBackTotal != null) {
                    if (mDataList != null) {
                        mBackTotal.setTotal(mDataList.size());
                    }
                }

            }
        };
    }


    class ViewHonlder extends RecyclerView.ViewHolder {

        @BindView(R.id.type_tv)
        TextView typeTv;
        @BindView(R.id.link_tv)
        TextView linkTv;
        @BindView(R.id.ll_item_lay)
        LinearLayout llItemLay;
        @BindView(R.id.bt_Delete)
        Button btDelete;

        public ViewHonlder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
