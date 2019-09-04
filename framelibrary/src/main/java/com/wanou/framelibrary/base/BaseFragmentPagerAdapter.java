package com.wanou.framelibrary.base;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Author by wodx521
 * Date on 2018/12/27.
 */
public abstract class BaseFragmentPagerAdapter extends FragmentPagerAdapter {
    protected List<BaseFragment> list = new ArrayList<>();
    protected List<String> listTab = new ArrayList<>();

    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(BaseFragment baseFragment,String tab) {
        listTab.add(tab);
        list.add(baseFragment);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // 防止销毁fragment
    }

    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }
}
