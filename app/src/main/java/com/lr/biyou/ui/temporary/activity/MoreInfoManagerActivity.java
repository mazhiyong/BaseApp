package com.lr.biyou.ui.temporary.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.FragmentAdapter;
import com.lr.biyou.api.MethodUrl;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.ui.temporary.fragment.BaseInfoFragment;
import com.lr.biyou.ui.temporary.fragment.WorkInfoFragment;
import com.lr.biyou.listener.ReLoadingData;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.mywidget.view.PageView;
import com.lr.biyou.basic.MbsConstans;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MoreInfoManagerActivity extends BasicActivity implements RequestView,ReLoadingData {
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
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.info_manager_page)
    ViewPager mFoodManagerPage;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.page_view)
    PageView mPageView;
    @BindView(R.id.bianji_but)
    Button mBianjiBut;
    @BindView(R.id.bottom_lay)
    LinearLayout mBottomLay;
    @BindView(R.id.img1)
    ImageView mImageView;
    @BindView(R.id.img2)
    ImageView mImageView2;
    @BindView(R.id.lay1)
    RelativeLayout mLay1;
    @BindView(R.id.lay2)
    RelativeLayout mLay2;
    private Map<String, Object> mData;

    //页面适配器
    private FragmentAdapter fragmentAdapter;
    //页面管理者
    private FragmentManager fm;
    //页面集合
    private List<Fragment> listFragment = new ArrayList<Fragment>();

    private String mRequestTag = "";

    private BaseInfoFragment baseInfoFragment ;
    private WorkInfoFragment workInfoFragment ;


    @Override
    public int getContentView() {
        return R.layout.activity_more_info_manager;
    }

    @Override
    public void init() {
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, MbsConstans.TOP_BAR_COLOR), MbsConstans.ALPHA);

        mTitleText.setText(getResources().getString(R.string.base_msg));
        mRightImg.setImageResource(R.drawable.modify_info);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MbsConstans.BroadcastReceiverAction.USER_INFO_UPDATE);
        registerReceiver(mBroadcastReceiver,intentFilter);

        mPageView.setContentView(mContent);
        mPageView.subscribRefreshEvent(this);
        mPageView.setReLoadingData(this);
        mPageView.showLoading();
        mRightImg.setVisibility(View.GONE);
        getMoreInfo();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(MbsConstans.BroadcastReceiverAction.USER_INFO_UPDATE)){
                getMoreInfo();
            }
        }
    };




    private void initFragment() {
        fm = getSupportFragmentManager();
        mFoodManagerPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSwipeBackHelper.setSwipeBackEnable(position == 0);
                switch (position) {
                    case 0:
                        mRadioGroup.check(R.id.rb_but1);
                        break;
                    case 1:
                        mRadioGroup.check(R.id.rb_but2);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        baseInfoFragment = new BaseInfoFragment();
        workInfoFragment = new WorkInfoFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA", (Serializable) mData);//这里的values就是我们要传的值

        baseInfoFragment.setArguments(bundle);
        workInfoFragment.setArguments(bundle);

        listFragment.add(0, baseInfoFragment);
        listFragment.add(1, workInfoFragment);

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), listFragment);
        mFoodManagerPage.setAdapter(fragmentAdapter);
        mFoodManagerPage.setOffscreenPageLimit(0);
        mImageView.setVisibility(View.VISIBLE);
        mImageView2.setVisibility(View.INVISIBLE);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rb_but1:
                        mFoodManagerPage.setCurrentItem(0);
                        mImageView.setVisibility(View.VISIBLE);
                        mImageView2.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.rb_but2:
                        mFoodManagerPage.setCurrentItem(1);
                        mImageView.setVisibility(View.INVISIBLE);
                        mImageView2.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });


    }

    /**
     * 获取用户更多资料信息
     */
    private void getMoreInfo() {

        mRequestTag = MethodUrl.userMoreInfo;
        Map<String, String> map = new HashMap<>();
        Map<String, String> mHeaderMap = new HashMap<String, String>();
        mRequestPresenterImp.requestGetToMap(mHeaderMap, MethodUrl.userMoreInfo, map);
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void disimissProgress() {
        dismissProgressDialog();
    }


    /**
     * @param tData 数据类型
     * @param mType
     */
    @Override
    public void loadDataSuccess(Map<String, Object> tData, String mType) {
        //

        Intent intent;
        switch (mType) {
            case MethodUrl.userMoreInfo:
                mData = tData;
                if (mData != null){
                    String mo = mData.get("canMod")+"";//是否可以修改（0：不可修改 1：可以修改）
                    if (mo.equals("1")){
                        mRightImg.setVisibility(View.VISIBLE);
                    }else {
                        mRightImg.setVisibility(View.GONE);
                    }
                }else {
                    mRightImg.setVisibility(View.GONE);
                }

                if (fragmentAdapter == null){
                    initFragment();
                    mPageView.showContent();
                }else {
                    baseInfoFragment.updateValue(mData);
                    workInfoFragment.updateValue(mData);
                }
                break;
            case MethodUrl.REFRESH_TOKEN://获取refreshToken返回结果
                MbsConstans.REFRESH_TOKEN = tData.get("refresh_token") + "";
                mIsRefreshToken = false;
                switch (mRequestTag) {
                    case MethodUrl.userMoreInfo:
                        getMoreInfo();
                        break;
                }
                break;
        }

    }

    @Override
    public void loadDataError(Map<String, Object> map, String mType) {
       dealFailInfo(map,mType);
    }


    @OnClick({R.id.back_img, R.id.left_back_lay,R.id.bianji_but,R.id.right_img,R.id.lay1,R.id.lay2,R.id.rb_but2,R.id.rb_but1})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rb_but1:
            case R.id.lay1:
                mRadioGroup.check(R.id.rb_but1);
                break;
            case R.id.rb_but2:
            case R.id.lay2:
                mRadioGroup.check(R.id.rb_but2);
                break;
            case R.id.right_img:
                intent = new Intent(MoreInfoManagerActivity.this,PerfectInfoActivity.class);
                intent.putExtra("type","2");//代表预览资料中的   编辑资料跳转的
                startActivity(intent);
                break;
            case R.id.bianji_but:
                intent = new Intent(MoreInfoManagerActivity.this,PerfectInfoActivity.class);
                intent.putExtra("type","2");//代表预览资料中的   编辑资料跳转的
                startActivity(intent);
                break;
            case R.id.back_img:
                finish();
                break;
            case R.id.left_back_lay:
                finish();
                break;
        }
    }

    @Override
    public void reLoadingData() {
        getMoreInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
