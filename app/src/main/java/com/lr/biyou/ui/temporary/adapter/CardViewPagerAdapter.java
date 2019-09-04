package com.lr.biyou.ui.temporary.adapter;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;


public class CardViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<View> mViews;

    public CardViewPagerAdapter(Context mContext,List<View> mDataList){
        this.mContext = mContext;
         this.mViews = mDataList;
    }

    @Override
    public int getCount() {
        return mViews.size();
//        return Integer.MAX_VALUE;
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        int pos = position % mViews.size();
        int pos = position;
        View mRootView = mViews.get(pos);
        ((ViewPager) container).removeView(mRootView);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
//        int pos = position % mViews.size();
        int pos = position;
        View mRootView = mViews.get(pos);
        //((ViewPager) container).addView(mRootView);


      /*  ViewParent viewParent = mRootView.getParent();
        if (viewParent != null) {
            container.removeView(mRootView);
        }*/
        ((ViewPager) container).addView(mRootView);
        return mRootView;
    }
    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

}
