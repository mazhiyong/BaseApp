package com.lr.biyou.mywidget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 此ViewPager解决与父容器ScrollView冲突的问题,无法完美解决.有卡顿
 * 此自定义组件和下拉刷新scrollview配合暂时小完美，有待改善
 * 
 */

public class IndexGridView extends GridView {
	public IndexGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public IndexGridView(Context context) {
		super(context);
	}

	public IndexGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	/**
	 * 下面这段代码是解决girdView空白点击事件的处理
	 */
	private OnTouchInvalidPositionListener mTouchInvalidPosListener;
	public interface OnTouchInvalidPositionListener {
		/**
		 * motionEvent 可使用 MotionEvent.ACTION_DOWN 或者 MotionEvent.ACTION_UP等来按需要进行判断
		 * @return 是否要终止事件的路由
		 */
		boolean onTouchInvalidPosition(int motionEvent);
	}
	/**
	 * 点击空白区域时的响应和处理接口
	 */
	public void setOnTouchInvalidPositionListener(OnTouchInvalidPositionListener listener) {
		mTouchInvalidPosListener = listener;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(mTouchInvalidPosListener == null) {
			return super.onTouchEvent(event);
		}
		if (!isEnabled()) {
			return isClickable() || isLongClickable();
		}
		final int motionPosition = pointToPosition((int)event.getX(), (int)event.getY());
		if( motionPosition == INVALID_POSITION ) {
			super.onTouchEvent(event);
			return mTouchInvalidPosListener.onTouchInvalidPosition(event.getActionMasked());
		}
		return super.onTouchEvent(event);
	}
}
