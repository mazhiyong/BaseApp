package com.lr.biyou.mywidget.behavior;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.lr.biyou.BuildConfig;
import com.lr.biyou.R;
import com.lr.biyou.basic.BasicApplication;

import java.util.List;

/**
 * 可滚动的 Content Behavior
 * <p/>
 * Created by xujun
 */
public class WeiboContentBehavior extends HeaderScrollingViewBehavior {
    private static final String TAG = "WeiboContentBehavior";

    public WeiboContentBehavior() {
    }

    public WeiboContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return isDependOn(dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onDependentViewChanged");
        }
        offsetChildAsNeeded(parent, child, dependency);
        return false;
    }

    private void offsetChildAsNeeded(CoordinatorLayout parent, View child, View dependency) {
        float dependencyTranslationY = dependency.getTranslationY();
        int translationY = (int) (-dependencyTranslationY / (getHeaderOffsetRange() * 1.0f) *
                getScrollRange(dependency));
        Log.i(TAG, "offsetChildAsNeeded: translationY=" + translationY);
        child.setTranslationY(translationY);

    }

    @Override
    protected View findFirstDependency(List<View> views) {
        for (int i = 0, z = views.size(); i < z; i++) {
            View view = views.get(i);
            if (isDependOn(view)) return view;
        }
        return null;
    }

    @Override
    protected int getScrollRange(View v) {
        if (isDependOn(v)) {
            return Math.max(0, v.getMeasuredHeight() - getFinalHeight());
        } else {
            return super.getScrollRange(v);
        }
    }

    private int getHeaderOffsetRange() {
        return BasicApplication.getContext().getResources().getDimensionPixelOffset(R.dimen
                .weibo_header_offset);
    }

    private int getFinalHeight() {
        Resources resources =BasicApplication.getContext().getResources();

        return 0;
    }

    private boolean isDependOn(View dependency) {
        return dependency != null && dependency.getId() == R.id.appbar_layout;
    }
}
