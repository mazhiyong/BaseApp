package com.lr.biyou.mywidget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.listener.ReLoadingData;


public class PageView extends FrameLayout implements View.OnClickListener {

	private View mLoading;
	private View mNetworkError;

	private View mEmpty;
	private View mEmptyLayout;
	private TextView mEmptyText;
	private View mContent;
	private View mOther;

	private Object mSubscriber;
	private static final String mMethodName = "onEventErrorRefresh";


	public ReLoadingData getReLoadingData() {
		return mReLoadingData;
	}

	public void setReLoadingData(ReLoadingData reLoadingData) {
		mReLoadingData = reLoadingData;
	}

	private ReLoadingData mReLoadingData;

	public PageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.page_view, this);
		initView();
	}

	private void initView() {
		mLoading = findViewById(R.id.page_view_loading);
		//ImageView mImageView = (ImageView) mLoading.findViewById(R.id.page_view_loading);
		/*AnimationDrawable aniDrawable;              android:src="@drawable/loading_anim"
		aniDrawable = (AnimationDrawable) ((ImageView)mLoading).getDrawable();
		aniDrawable.start();*/

		mEmpty = findViewById(R.id.page_view_empty);
		//mEmptyLayout = findViewById(R.id.page_view_empty_layout);
		//mEmptyLayout.setOnClickListener(this);

		mEmptyText = (TextView) findViewById(R.id.page_view_empty_text);
		mNetworkError = findViewById(R.id.page_view_network_error);
		mEmptyText.setOnClickListener(this);
		mNetworkError.setOnClickListener(this);

		hideAll();
	}

	private void hideAll() {
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			getChildAt(i).setVisibility(View.GONE);
		}
	}

	public void subscribRefreshEvent(Object subscriber) {
		mSubscriber = subscriber;
	}
	
	public PageView setEmptyText(String text) {
		mEmptyText.setText(text);
		return this;
	}

	public PageView setEmptyText(int resId) {
		mEmptyText.setText(resId);
		return this;
	}

	public PageView setEmptyIcon(int resId) {
		mEmptyText.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
		return this;
	}

	public void setEmptyView(int resId) {
		mEmpty = findViewById(resId);
		hideView(mEmpty);
	}

	public void setOther(int resId) {
		mOther = findViewById(resId);
		hideView(mOther);
	}

	public void showOther() {
		hideAll();
		showView(mOther);
	}

	public void setContentView(View content) {
		mContent = content;
		hideView(mContent);
	}

	public void showLoading() {
		hideAll();
		showView(mLoading);
	}

	public void hideLoading() {
		hideView(mLoading);
	}

	public void showNetworkError() {
		hideAll();
		showView(mNetworkError);
	}

	public void showContent(boolean show) {
		if (show) {
			showContent();
		}
	}

	public void showContent() {
		hideAll();
		showView(mContent);
	}

	public void showEmpty(boolean show) {
		if (show) {
			showEmpty();
		}
	}

	public void showEmpty() {
		hideAll();
		showView(mEmpty);
	}

	private static void hideView(View view) {
		if (view != null) {
			view.setVisibility(View.GONE);
		}
	}

	private static void showView(View view) {
		if (view != null) {
			view.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		if (mReLoadingData != null ){
//			showLoading();
			//showEmpty();
			//EventBusUtils.postEvent(mSubscriber, mMethodName);
			mReLoadingData.reLoadingData();
		}
	}

}
