package com.lr.biyou.mywidget.viewpager;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by yukuo on 2016/5/10.
 * 一个可以自己设置是否可以滚动的viewpager
 */
public class SetingScrollBanner extends ViewPager {
    public SetingScrollBanner(Context context) {
        super(context);
    }

    public SetingScrollBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean scrollable = true;


    /**
     * 设置viewpager是否可以滚动
     *
     * @param enable
     */
    public void setScrollable(boolean enable) {
        scrollable = enable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (scrollable) {
            return super.onInterceptTouchEvent(event);
        } else {
            return false;
        }
    }
}
