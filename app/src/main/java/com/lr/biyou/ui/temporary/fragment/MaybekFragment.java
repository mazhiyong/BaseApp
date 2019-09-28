package com.lr.biyou.ui.temporary.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.jaeger.library.StatusBarUtil;
import com.lr.biyou.R;
import com.lr.biyou.basic.BasicFragment;
import com.lr.biyou.mvp.presenter.RequestPresenterImp;
import com.lr.biyou.mvp.view.RequestView;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;


/**
 * Coordinatorlayout+AppbarLayout+CollapsingToolbarLayout
 */
public class MaybekFragment extends BasicFragment implements RequestView {

    @BindView(R.id.lay_header)
    RelativeLayout mLayHeader;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab_layout)
    XTabLayout mTabLayout;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.bga_banner)
    ImageView mBgaBanner;






    @Inject
    RequestPresenterImp mPresenterImp;
    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_lay)
    LinearLayout mLeftLay;
    @BindView(R.id.right_text_tv)
    TextView mRightTextTv;
    @BindView(R.id.right_img)
    ImageView mRightImg;
    @BindView(R.id.right_lay)
    LinearLayout mRightLay;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_maybek;
    }

    @Override
    public void init() {
        //setAvatorChange();

        mBackImg.setVisibility(View.GONE);
        mBackText.setVisibility(View.GONE);
        mLeftLay.setVisibility(View.GONE);

        mRightImg.setVisibility(View.GONE);
        mRightTextTv.setVisibility(View.GONE);
        mRightLay.setVisibility(View.GONE);

        mTitleText.setText("首页");



        initBanner();

        mTabLayout.addTab(mTabLayout.newTab().setText("找学长"));
        mTabLayout.addTab(mTabLayout.newTab().setText("寻师姐"));



    }

    public void setBarTextColor() {
        StatusBarUtil.setLightMode(getActivity());
    }

    /**
     * 渐变toolbar背景
     */
    private void setAvatorChange() {
        mAppbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //verticalOffset始终为0以下的负数
                float percent = (Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange());

                mToolbar.setBackgroundColor(changeAlpha(Color.WHITE, percent));
            }
        });
    }

    /**
     * 根据百分比改变颜色透明度
     */
    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }


    private void initBanner() {



    }


    /**
     * @descriptoin 请求前加载progress
     */
    @Override
    public void showProgress() {

    }

    /**
     * @descriptoin 请求结束之后隐藏progress
     */
    @Override
    public void disimissProgress() {

    }

    /**
     * @param tData
     * @param mType
     * @descriptoin 请求数据成功 返回数据类型   请求类型
     */
    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {

    }

    /**
     * @param map
     * @param mType
     * @descriptoin 请求数据错误 错误信息  请求类型
     */
    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

    }
}
