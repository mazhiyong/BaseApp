package com.lr.biyou.ui.moudle4.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
       /* Fragment page = null;
        if (mFragments.size()> position){
            page = mFragments.get(position);
            if (page != null){
                return page;
            }
        }
        while (position > mFragments.size()){
            mFragments.add(null);
        }*/

        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments != null ? mFragments.size():0;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

}
