package com.lr.biyou.ui.moudle1.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.lr.biyou.R;
import com.lr.biyou.basic.BasicRecycleViewAdapter;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainCoinAdapter extends PagerAdapter {
    //上下文
    private Context mContext;
    //数据
    private List<Map<String,Object>> mData;
    private int currentPosition;
    private ItemClickListener itemClickListener;
    private SparseArray<RecyclerView> recyclerViewList = new SparseArray<>();

    public MainCoinAdapter(Activity activity, List<Map<String,Object>> data) {
        mContext = activity;
        mData = data;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setData(List<Map<String,Object>> listUp, int currentPosition) {
        mData = listUp;
        this.currentPosition = currentPosition;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(Map<String,Object> map);
    }

    @Override
    public int getCount() {
        // 根据传入数据来判断
        if (mData != null && mData.size() > 0) {
            if (mData.size() % 3 > 0) {
                return mData.size() / 3 + 1;
            } else {
                return mData.size() / 3;
            }
        }
        return 0;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // 根据索引取view, 如果view为空, 则添加view
//        RecyclerView recyclerView = recyclerViewList.get(position);
//        if (recyclerView == null) {
        // 创建 RecyclerView 并绑定 adapter 并设置adapter 的监听事件
        RecyclerView recyclerView = (RecyclerView) UtilTools.parseLayout(mContext,R.layout.coin_gridview);
        CoinInfoAdapter coinInfoAdapter = new CoinInfoAdapter(mContext);
        recyclerView.setAdapter(coinInfoAdapter);
        int start = position * 3;
        // 当位于最后一部分时
        List<Map<String,Object>> listUpBeans;
        if (position == getCount() - 1) {
            listUpBeans = new ArrayList<>(mData.subList(start, mData.size()));
        } else {
            listUpBeans = new ArrayList<>(mData.subList(start, start + 3));
        }
        coinInfoAdapter.setList(listUpBeans);
        coinInfoAdapter.setOnItemClickListener(new BasicRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Map<String,Object> map = coinInfoAdapter.getCoinList().get(position);
                if (itemClickListener != null) {
                    itemClickListener.onItemClickListener(map);
                }
            }
        });
        recyclerViewList.put(position, recyclerView);
//        }
        container.addView(recyclerView);
        return recyclerView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        recyclerViewList.remove(position);
        container.removeView((RecyclerView) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        CoinInfoAdapter coinInfoAdapter1 = (CoinInfoAdapter) ((RecyclerView) object).getAdapter();
        if (coinInfoAdapter1 != null) {
            int start = currentPosition * 3;
            // 当位于最后一部分时
            List<Map<String,Object>> listUpBeans;
            if (currentPosition == getCount() - 1) {
                listUpBeans = new ArrayList<>(mData.subList(start, mData.size()));
            } else {
                listUpBeans = new ArrayList<>(mData.subList(start, start + 3));
            }
            coinInfoAdapter1.setList(listUpBeans);
        }
        return -2;
    }


}
