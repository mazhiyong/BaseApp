package com.lr.biyou.ui.temporary.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.lr.biyou.R;
import com.lr.biyou.ui.temporary.adapter.ViewPagerAdapter;
import com.lr.biyou.basic.BasicActivity;
import com.lr.biyou.utils.imageload.CircleProgressView;
import com.lr.biyou.basic.MbsConstans;
import com.jaeger.library.StatusBarUtil;
import com.komi.slider.SliderConfig;
import com.komi.slider.SliderUtils;
import com.komi.slider.position.SliderPosition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 *
 * 显示查看大图界面
 *
 *
 */
public class ShowDetailPictrue extends BasicActivity implements View.OnClickListener{
	private ViewPager mViewPager;
	/**得到上一个界面点击图片的位置*/
	private int position=0;
	private ViewPagerAdapter mPagerAdapter;
	private List<View> mViews ;
	//装ViewPager中ImageView的数组
	//	private ImageView[] mYuanImageView;
	private List<Map<String, Object>> mDataList = new ArrayList<Map<String,Object>>();
	private List<Map<String, Object>> mFileList = new ArrayList<Map<String,Object>>();
	private TextView mTipTv;
	private ImageView mBackTv;
	private TextView mSaveTv;


	private LayoutInflater mLayoutInflater;

	@Override
	public int getContentView() {
		return R.layout.show_detail_pictrue_a;
	}
	@Override
	public boolean isSupportSwipeBack() {
		return false;
	}

	@Override
	public void init() {

		initSilder();
		//StatusBarUtil.setColorForSwipeBack(this, ContextCompat.getColor(this, R.color.black), 0);
		//StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.black), 0);
		StatusBarUtil.setTranslucentForImageView(this, 0, null);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏

		mLayoutInflater = LayoutInflater.from(this);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null){
			position=bundle.getInt("position", 0);
			mDataList = (List<Map<String,Object>>) bundle.getSerializable("DATA");
		}

		/*position = 0;

		Map<String,Object> map = new HashMap<>();
		map.put("remotepath","https://gagayi.oss-cn-beijing.aliyuncs.com/video/article.jpg");
        mDataList.add(map);

        map = new HashMap<>();
        map.put("remotepath","https://gagayi.oss-cn-beijing.aliyuncs.com/video/bear.png");
        mDataList.add(map);

        map = new HashMap<>();
        map.put("remotepath","https://gagayi.oss-cn-beijing.aliyuncs.com/video/river.jpg");
        mDataList.add(map);*/

		initViewPager();
	}

	//实现界面上下滑动退出界面效果
	private void initSilder() {
		SliderConfig mConfig = new SliderConfig.Builder()
				.primaryColor(Color.BLACK)
				.secondaryColor(Color.TRANSPARENT)
				.position(SliderPosition.VERTICAL)
				.slideEnter(true)
				.sensitivity(0.3f)
				.distanceThreshold(0.2f)
				//.edgePercent(1f)
				.edge(false)
				.build();
		SliderUtils.attachActivity(this, mConfig);
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

	}

	private void initViewPager(){

		/*SlideCloseLayout swipeableLayout = (SlideCloseLayout) findViewById(R.id.swipableLayout);
		swipeableLayout.setGradualBackground(getWindow().getDecorView().getBackground());
		//设置监听，滑动一定距离后让Activity结束
		swipeableLayout.setLayoutScrollListener(new SlideCloseLayout.LayoutScrollListener() {
			@Override
			public void onLayoutClosed() {
				onBackPressed();
			}
		});*/

		mTipTv = (TextView) findViewById(R.id.position_tv);
		mBackTv = (ImageView) findViewById(R.id.btn_back);
		mBackTv.setOnClickListener(this);
		mSaveTv = (TextView)findViewById(R.id.btn_save);
		mSaveTv.setOnClickListener(this);
		mViewPager = (ViewPager) findViewById(R.id.viewPager_show_bigPic);
		mFileList = mDataList;
		initView();
	}

	private void initView(){

		//mLinearLayout.removeAllViews();
		if (mFileList == null || mFileList.size()<=0) {
			return;
		}

		mViews = new ArrayList<View>();
		for (int i = 0; i < 4; i++) {
			/*PhotoView imageView = new PhotoView(this);
			//imageView.setScaleType(ScaleType.CENTER_CROP);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			imageView.setTag(R.id.glide_tag,mFileList.get(i));

			imageView.setLayoutParams(params);*/

			View view = mLayoutInflater.inflate(R.layout.item_viewpager_pic,null);
			RelativeLayout relativeLayout = view.findViewById(R.id.content_lay);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

			relativeLayout.setLayoutParams(params);
			/*SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(this);
			//imageView.setScaleType(ScaleType.CENTER_CROP);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			imageView.setTag(R.id.glide_tag,mFileList.get(i));

			imageView.setLayoutParams(params);*/
			mViews.add(view);
		}

		mPagerAdapter = new ViewPagerAdapter(this, mViews,mFileList);
		mViewPager.setAdapter(mPagerAdapter);
		//mViewPager.setOffscreenPageLimit(3);
		mViewPager.addOnPageChangeListener(new MyPageChangeListener());
		mViewPager.setCurrentItem(position);
		mTipTv.setText((position+1)+"/"+mFileList.size());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_back:
				this.finish();
				break;
			case R.id.btn_save:
				break;
		}
	}

	private void saveCroppedImage() {
		ImageView imageView = (ImageView) mViews.get(mViewPager.getCurrentItem());
		imageView.setDrawingCacheEnabled(true);
		Bitmap bmp = imageView.getDrawingCache();

		Map<String,Object> map  = mFileList.get(mViewPager.getCurrentItem());
		try {
			File saveFile = new File(MbsConstans.PIC_SAVE);

			String filepath = MbsConstans.PIC_SAVE+new Date().getTime()+".png";
			File file = new File(filepath);
			if (!saveFile.exists()) {
				saveFile.mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			File saveFile2 = new File(filepath);

			FileOutputStream fos = new FileOutputStream(saveFile2);
			bmp.compress(Bitmap.CompressFormat.JPEG, 50, fos);
			fos.flush();
			fos.close();
			imageView.setDrawingCacheEnabled(false);
			Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 *
	 * @author Administrator
	 *
	 */
	private class MyPageChangeListener implements OnPageChangeListener {

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {

			mSwipeBackHelper.setSwipeBackEnable(position == 0);
			setImageBackground(position);
			mTipTv.setText((position+1)+"/"+mFileList.size());

		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 设置选中的tip的背景
	 *
	 * @param selectItemsIndex
	 */
	private void setImageBackground(int selectItemsIndex) {
		//		for (int i = 0; i < mYuanImageView.length; i++) {
		//			if (i == selectItemsIndex) {
		//				mYuanImageView[i].setBackgroundResource(R.drawable.app_button_white_checked);
		//			} else {
		//				mYuanImageView[i].setBackgroundResource(R.drawable.app_button_white_normal);
		//			}
		//		}
	}

	@Override
	public void finish() {
		super.finish();
		if (mViews != null){
			for (int i = 0;i<mViews.size();i++){
				View mRootView = mViews.get(i);
				CircleProgressView mCircleProgressView = mRootView.findViewById(R.id.progressView);
				SubsamplingScaleImageView view = (SubsamplingScaleImageView) mRootView.findViewById(R.id.big_image_view);
				Glide.with(view.getContext()).clear(view);
				view.recycle();
				view = null;
				mCircleProgressView = null;
				mRootView = null;
			}
		}
        //Runtime.getRuntime().gc();
		overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
