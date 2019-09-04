package com.lr.biyou.mywidget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.lr.biyou.R;
import com.lr.biyou.api.RxApiManager;


/**
 *

 * @Description:   自定义dialog

 */

public class TestDialog extends Dialog  implements DialogInterface.OnKeyListener {

	private ImageView mImageView;
	private TextView mTextView;
	private Animation mRotateAnimation;
	/**旋转动画的时间*/
	static final int ROTATION_ANIMATION_DURATION = 1200;
	/**动画插值*/
	static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
	private Context mContext;

	public TestDialog(Context context, int style){
		super(context, style);
		this.mContext = context;
		this.setOnKeyListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_trade_condition);


		Window window = this.getWindow();

		window.setGravity(Gravity.BOTTOM);
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.x = 0;
		lp.y = 0;
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = 600;
		//lp.dimAmount=0.0f;   // 背景没有黑色
		this.getWindow().setAttributes(lp);
		// 设置点击外围解散
		this.setCanceledOnTouchOutside(false);
		this.setCancelable(true);
		initAnimation();

//		window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
//				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);//不获得焦点
		//window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//背景不变暗
		//window.setGravity(Gravity.CENTER);

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
