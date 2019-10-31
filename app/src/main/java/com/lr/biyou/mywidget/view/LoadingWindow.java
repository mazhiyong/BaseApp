package com.lr.biyou.mywidget.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lr.biyou.R;
import com.lr.biyou.api.RxApiManager;
import com.lr.biyou.utils.tool.UtilTools;


/**
 *

 * @Description:   加载等待动画

 */

public class LoadingWindow extends Dialog  implements DialogInterface.OnKeyListener {

	private ImageView mImageView;
	private TextView mTextView;
	private Animation mRotateAnimation;
	/**旋转动画的时间*/
	static final int ROTATION_ANIMATION_DURATION = 1200;
	/**动画插值*/
	static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
	private Context mContext;

	public LoadingWindow(Context context,int style){
		super(context, style);
		this.mContext = context;
		initView();
		this.setOnKeyListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}


	public void setTipText(String tipStr){
		mTextView.setVisibility(View.VISIBLE);
		mTextView.setText(tipStr);
	}


	public void initView(){

		View view = getLayoutInflater().inflate(R.layout.loading_view, null);
		mImageView = (ImageView) view.findViewById(R.id.load_imageview);
		mTextView = (TextView) view.findViewById(R.id.loading_text_tv);

		mTextView.setText("加载中，请稍后...");

		RequestOptions options = new RequestOptions()
				.centerCrop()
				.circleCrop()//设置圆形
				.placeholder(R.color.body_bg) //占位图
				.error(R.color.body_bg)       //错误图
				.priority(Priority.HIGH)
				.diskCacheStrategy(DiskCacheStrategy.ALL);
		Glide.with(mContext)
				.asGif()
				.load(R.drawable.loding)
				.apply(options)
				.into(mImageView);





		this.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));


		Window window = this.getWindow();

		WindowManager.LayoutParams lp = window.getAttributes();
		lp.x=0;
		lp.y= -UtilTools.dip2px(mContext,40);
		lp.dimAmount=0.0f;   // 背景没有黑色
		this.getWindow().setAttributes(lp);
		// 设置点击外围解散
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(true);
		initAnimation();

//		window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);//不获得焦点
		window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//背景不变暗
		//window.setGravity(Gravity.CENTER);


	}



	@SuppressLint("NewApi")
	private void copySystemUiVisibility() {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			getWindow().getDecorView().setSystemUiVisibility(
					((Activity)mContext).getWindow().getDecorView().getSystemUiVisibility()
			);
		}
	}

	@Override
	public void show() {
		// Set the dialog to not focusable.
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
		//copySystemUiVisibility();
		// Show the dialog with NavBar hidden.
		super.show();
		this.getWindow().getDecorView().setSystemUiVisibility(
				((Activity)mContext).getWindow().getDecorView().getSystemUiVisibility());
		// Set the dialog to focusable again.
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
	}


	AnimationDrawable aniDrawable;
	private void initAnimation(){
		//aniDrawable = (AnimationDrawable) mImageView.getDrawable();

	}

	public void showView(){
		// 为了防止在onCreate方法中只显示第一帧的解决方案之一
		/*mImageView.postDelayed(new Runnable() {
			@Override
			public void run() {
				aniDrawable.start();

			}
		},1000);*/
		show();
		//aniDrawable.start();

	}

	public void cancleView(){
		//aniDrawable.stop();
		this.dismiss();
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			RxApiManager.get().cancel(mContext);
			//cancleView();
			this.dismiss();
			return true;
		}
		return false;
	}
}
