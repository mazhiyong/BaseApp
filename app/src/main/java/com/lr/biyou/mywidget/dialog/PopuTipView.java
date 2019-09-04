package com.lr.biyou.mywidget.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.view.TriangleView;
import com.lr.biyou.R;
import com.lr.biyou.utils.tool.UtilTools;


public class PopuTipView extends PopupWindow{

	private int mHeight;
	private int mWidth;

	private View popView;

	private LinearLayout mJianTouLay;
	private LinearLayout mLlContent;
	private TriangleView mTriangleView;

	public PopuTipView(Context context,String msg,int id) {
		popView = View.inflate(context, id, null);
		popView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

		mTriangleView = popView.findViewById(R.id.triangle_view);
		mTriangleView.setColor(Color.parseColor("#A6000000"));
		LinearLayout.LayoutParams mTriangleLayoutParams = (LinearLayout.LayoutParams) mTriangleView.getLayoutParams();
		mTriangleLayoutParams.width = UtilTools.dip2px(context,6);
		mTriangleLayoutParams.height = UtilTools.dip2px(context,9);
		mTriangleView.setLayoutParams(mTriangleLayoutParams);


		mJianTouLay = popView.findViewById(R.id.jiantou_lay);


		TextView mTextView2 = popView.findViewById(R.id.tv_bubble);
		mTextView2.setText(msg);


		mLlContent = popView.findViewById(com.flyco.dialog.R.id.ll_content);
		mLlContent.setBackground(CornerUtils.cornerDrawable(Color.parseColor("#A6000000"), 15));

	/*	RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mLlContent.getLayoutParams();
		// mLlContent.setBackgroundDrawable( CornerUtils.cornerDrawable(Color.parseColor("#A6000000"), 15));
		mLlContent.setBackground(CornerUtils.cornerDrawable(Color.parseColor("#A6000000"), 15));
		mLayoutParams.setMargins(0, 0, 20, 0);
		mLlContent.setLayoutParams(mLayoutParams);*/



		//popView.requestFocus();//pop设置不setBackgroundDrawable情况，把焦点给popView，添加popView.setOnKeyListener。可实现点击外部不消失，点击反键才消失
		//			mPopupWindow.showAtLocation(mCityTv, Gravity.TOP|Gravity.RIGHT, 0, 0); //设置layout在PopupWindow中显示的位置
		// mPopupWindow.showAsDropDown(mCityLay);
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		popView.measure(w, h);
		int height = popView.getMeasuredHeight();
		int width = popView.getMeasuredWidth();
		System.out.println("measure width=" + width + " height=" + height);

		mHeight = height+popView.getPaddingTop()+popView.getPaddingBottom();
		mWidth = width+popView.getPaddingLeft()+popView.getPaddingRight();



		RelativeLayout.LayoutParams mLayoutParams = (RelativeLayout.LayoutParams) mLlContent.getLayoutParams();
		mLlContent.setBackground( CornerUtils.cornerDrawable(Color.parseColor("#A6000000"), 15));
		mLayoutParams.setMargins(0, 0, 0, 0);
		//mLayoutParams.addRule(RelativeLayout.ABOVE, R.id.jiantou_lay);
		mLlContent.setLayoutParams(mLayoutParams);

		this.update();
		this.setTouchable(true);
		this.setFocusable(true);

		this.setWidth(LayoutParams.WRAP_CONTENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setContentView(popView);
	}


	/**
	 * 显示已经改进度对话框
	 *
	 * @param parent
	 *            为该进度对话框设置从界面中的什么位置开始显示
	 */
	public void show(View parent,int type) {
		int[] location = new int[2];
		parent.getLocationOnScreen(location);
		RelativeLayout.LayoutParams layoutParams;
		switch (type) {
			case 1://上面靠view右边显示
				mTriangleView.setGravity(Gravity.BOTTOM);
				layoutParams = (RelativeLayout.LayoutParams)mJianTouLay.getLayoutParams();
				layoutParams.width = parent.getWidth();
				layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
				mJianTouLay.setLayoutParams(layoutParams);
				this.showAtLocation(parent, Gravity.TOP | Gravity.LEFT, location[0], location[1] - mHeight);
				break;
			case 2://上面靠view左边显示
				mTriangleView.setGravity(Gravity.BOTTOM);
				layoutParams = (RelativeLayout.LayoutParams)mJianTouLay.getLayoutParams();
				layoutParams.width = parent.getWidth();
				layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
				//layoutParams.addRule(RelativeLayout.BELOW, R.id.ll_content);
				layoutParams.addRule(RelativeLayout.ALIGN_RIGHT,R.id.ll_content);
//				mJianTouLay.setGravity(Gravity.RIGHT);
				mJianTouLay.setLayoutParams(layoutParams);
				this.showAtLocation(parent,Gravity.TOP | Gravity.LEFT,location[0]-(mWidth)+parent.getWidth(),location[1]-mHeight);
				break;
			case 3://下面靠view右边显示
				mTriangleView.setGravity(Gravity.TOP);
				layoutParams = (RelativeLayout.LayoutParams)mJianTouLay.getLayoutParams();
				layoutParams.width = parent.getWidth();
				layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
				//layoutParams.addRule(RelativeLayout.ABOVE, R.id.ll_content);
				mJianTouLay.setLayoutParams(layoutParams);
				this.showAtLocation(parent,Gravity.TOP | Gravity.LEFT, location[0], location[1] +parent.getHeight());
				break;
			case 4://下面靠view左边显示
				mTriangleView.setGravity(Gravity.TOP);
				layoutParams = (RelativeLayout.LayoutParams)mJianTouLay.getLayoutParams();
				layoutParams.width = parent.getWidth();
				layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
				layoutParams.addRule(RelativeLayout.ALIGN_RIGHT,R.id.ll_content);
				mJianTouLay.setLayoutParams(layoutParams);
				this.showAtLocation(parent,  Gravity.TOP |Gravity.LEFT, location[0]-(mWidth)+parent.getWidth(), location[1] +parent.getHeight());
				break;
		}

	}
}
