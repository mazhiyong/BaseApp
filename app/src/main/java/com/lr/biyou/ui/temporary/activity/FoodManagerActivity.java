package com.lr.biyou.ui.temporary.activity;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.FragmentAdapter;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.ui.temporary.fragment.FoodFragment;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class FoodManagerActivity extends BasicActivity {

    @BindView(R.id.back_img)
    ImageView mBackImg;
    @BindView(R.id.back_text)
    TextView mBackText;
    @BindView(R.id.left_back_lay)
    LinearLayout mLeftBackLay;
    @BindView(R.id.title_text)
    TextView mTitleText;
    @BindView(R.id.right_img)
    ImageView mRightImg;
    @BindView(R.id.top_layout)
    LinearLayout mTitleBarView;
    @BindView(R.id.rb_but1)
    RadioButton mRbBut1;
    @BindView(R.id.rb_but2)
    RadioButton mRbBut2;
    @BindView(R.id.rb_but3)
    RadioButton mRbBut3;
    @BindView(R.id.rb_but4)
    RadioButton mRbBut4;
    @BindView(R.id.rb_but5)
    RadioButton mRbBut5;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.food_manager_page)
    ViewPager mFoodManagerPage;

    //页面适配器
    private FragmentAdapter fragmentAdapter;
    //页面管理者
    private FragmentManager fm;
    //页面集合
    private List<Fragment> listFragment = new ArrayList<Fragment>();


    @Override
    public int getContentView() {
        return R.layout.activity_food_manager;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.yellow), 0);
        fm = getSupportFragmentManager();
        mFoodManagerPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSwipeBackHelper.setSwipeBackEnable(position == 0);
                switch (position){
                    case 0:
                        mRadioGroup.check(R.id.rb_but1);
                        break;
                    case 1:
                        mRadioGroup.check(R.id.rb_but2);
                        break;
                    case 2:
                        mRadioGroup.check(R.id.rb_but3);
                        break;
                    case 3:
                        mRadioGroup.check(R.id.rb_but4);
                        break;
                    case 4:
                        mRadioGroup.check(R.id.rb_but5);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        listFragment.add(0, new FoodFragment());
        listFragment.add(1, new FoodFragment());
        listFragment.add(2, new FoodFragment());
        listFragment.add(3, new FoodFragment());
        listFragment.add(4, new FoodFragment());

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), listFragment);
        mFoodManagerPage.setAdapter(fragmentAdapter);
        mFoodManagerPage.setOffscreenPageLimit(0);



        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.rb_but1:
                        mFoodManagerPage.setCurrentItem(0);
                        break;
                    case R.id.rb_but2:
                        mFoodManagerPage.setCurrentItem(1);
                        break;
                    case R.id.rb_but3:
                        mFoodManagerPage.setCurrentItem(2);
                        break;
                    case R.id.rb_but4:
                        mFoodManagerPage.setCurrentItem(3);
                        break;
                    case R.id.rb_but5:
                        mFoodManagerPage.setCurrentItem(4);
                        break;
                }
            }
        });
    }

    /**
     * @descriptoin 请求前加载progress
     * @author dc
     * @date 2017/2/16 11:00
     */
    @Override
    public void showProgress() {

    }

    /**
     * @descriptoin 请求结束之后隐藏progress
     * @author dc
     * @date 2017/2/16 11:01
     */
    @Override
    public void disimissProgress() {

    }

    /**
     * @param tData 数据类型
     * @param mType
     * @descriptoin 请求数据成功
     * @author dc
     * @date 2017/2/16 11:01
     */
    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {

    }

    /**
     * @param map
     * @param mType
     * @descriptoin 请求数据错误
     * @date 2017/2/16 11:01
     */
    @Override
    public void loadDataError(Map<String, Object> map, String mType) {

    }
}
