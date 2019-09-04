package com.lr.biyou.ui.moudle.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


import com.lr.biyou.R;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.basic.MbsConstans;
import com.lr.biyou.mvp.view.RequestView;
import com.lr.biyou.ui.moudle.adapter.GuidePageAdapter;
import com.lr.biyou.utils.tool.JSONUtil;
import com.lr.biyou.utils.tool.SPUtils;
import com.lr.biyou.utils.tool.UtilTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.viewpager.widget.ViewPager;

/**
 *
 */
public class GuideViewPageActivity extends BasicActivity implements RequestView {

	private ViewPager mViewPager;
	private List<View> mViewList;
	private ImageView mImageView;
	private ImageView[] mImageViews;
	private LinearLayout mBottonLayout;
	private GuidePageAdapter mGuidePageAdapter;

	private SharedPreferences mShared;//存放配置信息的文件
	private TextView mStartBut;

	@Override
	public boolean isSupportSwipeBack() {
		return false;
	}
	@Override
	public int getContentView() {
		return R.layout.guide_view;
	}

	@Override
	public void init() {
		LayoutInflater inflater = getLayoutInflater();
		mViewPager = (ViewPager) findViewById(R.id.guidePages);
		mBottonLayout = (LinearLayout) findViewById(R.id.viewGroup);

		mViewList = new ArrayList<View>();
		mViewList.add(inflater.inflate(R.layout.startup1, null));
		mViewList.add(inflater.inflate(R.layout.startup2, null));

		mImageViews = new ImageView[mViewList.size()];

		for (int i = 0; i < mViewList.size(); i++) {
			mImageView = new ImageView(this);
			LayoutParams params = new LayoutParams(UtilTools.dip2px(this,8),UtilTools.dip2px(this,8));
			mImageView.setLayoutParams(params);
			mImageViews[i] = mImageView;

			if (i != 0) {
				params.leftMargin = 15;
			}
			if (i == 0) {
				// 默认选中第一张图片
				mImageViews[i].setBackgroundResource(R.drawable.circle_selector);
				mImageViews[i].setSelected(true);
			} else {
				mImageViews[i].setBackgroundResource(R.drawable.circle_selector);
				mImageViews[i].setSelected(false);

			}
			mBottonLayout.addView(mImageViews[i]);
		}

		mGuidePageAdapter = new GuidePageAdapter(this,mViewList);
		mViewPager.setAdapter(mGuidePageAdapter);

		mViewPager.addOnPageChangeListener(new GuidePageChangeListener());
		mShared = getSharedPreferences(MbsConstans.SharedInfoConstans.LOGIN_INFO, Context.MODE_PRIVATE);

		mStartBut =(TextView) mViewList.get(mViewList.size()-1).findViewById(R.id.startBtn);
		mStartBut.setVisibility(View.VISIBLE);
		mStartBut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SPUtils.put(GuideViewPageActivity.this, MbsConstans.SharedInfoConstans.IS_FIRST_START, MbsConstans.UpdateAppConstans.VERSION_APP_CODE+"");
				boolean bb = (boolean) SPUtils.get(GuideViewPageActivity.this, MbsConstans.SharedInfoConstans.LOGIN_OUT, true);
				Intent intent;
				if (bb) {
					intent = new Intent(GuideViewPageActivity.this, LoginActivity.class);
					GuideViewPageActivity.this.startActivity(intent);
					GuideViewPageActivity.this.finish();
				} else {
					MbsConstans.ACCESS_TOKEN = SPUtils.get(GuideViewPageActivity.this, MbsConstans.SharedInfoConstans.ACCESS_TOKEN, "").toString();
					MbsConstans.REFRESH_TOKEN = SPUtils.get(GuideViewPageActivity.this, MbsConstans.SharedInfoConstans.REFRESH_TOKEN, "").toString();
					String s = SPUtils.get(GuideViewPageActivity.this, MbsConstans.SharedInfoConstans.LOGIN_INFO, "").toString();
					MbsConstans.USER_MAP = JSONUtil.getInstance().jsonMap(s);
					if (MbsConstans.USER_MAP == null || MbsConstans.USER_MAP.isEmpty()) {
						intent = new Intent(GuideViewPageActivity.this, LoginActivity.class);
						startActivity(intent);
						finish();
					} else {
						MbsConstans.USER_MAP = JSONUtil.getInstance().jsonMap(s);
						intent = new Intent(GuideViewPageActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}

				}
			}
		});
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




	// 指引页面更改事件监听器
	class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < mImageViews.length; i++) {
				if (i == arg0) {
					mImageViews[i].setBackgroundResource(R.drawable.circle_selector);
					mImageViews[i].setSelected(true);
				} else {
					mImageViews[i].setBackgroundResource(R.drawable.circle_selector);
					mImageViews[i].setSelected(false);

				}
			}
		}
	}

}
