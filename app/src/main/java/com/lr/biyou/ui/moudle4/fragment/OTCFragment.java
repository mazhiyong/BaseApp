package com.lr.biyou.ui.moudle4.fragment;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.lr.biyou.R;
import com.lr.biyou.basic.BasicFragment;
import com.lr.biyou.mywidget.dialog.DateSelectDialog;
import com.lr.biyou.mywidget.view.LoadingWindow;
import com.lr.biyou.ui.moudle4.adapter.MyViewPagerAdapter;
import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.utils.tool.LogUtilDebug;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * OTC
 */
public class OTCFragment extends BasicFragment {

    @BindView(R.id.iv)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    XTabLayout mTabLayout;

    List<Fragment> mFragments=new ArrayList<>();



    private BBTradeFragment bbTradeFragment;
    private FBTradeFragment fbTradeFragment;

    private int Position = 0;

    public OTCFragment() {
        // Required empty public constructor
    }





    @Override
    public int getLayoutId() {
        return R.layout.fragment_otc;
    }
    @Override
    public void init() {
        //getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
       // StatusBarUtil.setColorForSwipeBack(getActivity(), ContextCompat.getColor(getActivity(), MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);
        setBarTextColor();


        mTabLayout.addTab(mTabLayout.newTab().setText("币币交易"));
        mTabLayout.addTab(mTabLayout.newTab().setText("法币交易"));

        bbTradeFragment=new BBTradeFragment();
        mFragments.add(bbTradeFragment);
        fbTradeFragment=new FBTradeFragment();
        mFragments.add(fbTradeFragment);
        mViewPager.setAdapter(new MyViewPagerAdapter(getChildFragmentManager(),mFragments));
        mViewPager.addOnPageChangeListener(new XTabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0){
                   LogUtilDebug.i("show","BB可见");
                    bbTradeFragment.restartWs();
                }else {
                    bbTradeFragment.stopWs();
                   LogUtilDebug.i("show","FB可见");
                }
                Position = tab.getPosition();
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });





    }

    public void setBarTextColor(){
        StatusBarUtil.setLightMode(getActivity());
    }


    @Override
    public void showProgress() {


    }

    @Override
    public void disimissProgress() {

    }

    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

    }


    @Override
    public void onResume() {
        super.onResume();
       LogUtilDebug.i("show","onResume()*******");

    }

    @Override
    public void onPause() {
        super.onPause();
       LogUtilDebug.i("show","onPause()*******");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
           LogUtilDebug.i("show","onHiddenChanged()*******OTC不可见" + Position);
            switch (Position){
                case 0:
                    bbTradeFragment.stopWs();
                    break;
                case 1:

                    break;
            }
        }else {
           LogUtilDebug.i("show","onHiddenChanged()*******OTC可见"+Position);
            switch (Position){
                case 0:
                    bbTradeFragment.restartWs();
                    break;
                case 1:

                    break;
            }
        }

    }
}
