package com.lr.biyou.mywidget.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.lr.biyou.BuildConfig;
import com.lr.biyou.R;
import com.lr.biyou.basic.BasicApplication;

import java.lang.ref.WeakReference;


public class WeiboHeaderPagerBehavior extends ViewOffsetBehavior {
    private static final String TAG = "UcNewsHeaderPager";
    public static final int STATE_OPENED = 0;
    public static final int STATE_CLOSED = 1;
    public static final int DURATION_SHORT = 300;
    public static final int DURATION_LONG = 600;

    private int mCurState = STATE_OPENED;
    private OnPagerStateListener mPagerStateListener;

    private OverScroller mOverScroller;

    private WeakReference<CoordinatorLayout> mParent;
    private WeakReference<View> mChild;

    public void setPagerStateListener(OnPagerStateListener pagerStateListener) {
        mPagerStateListener = pagerStateListener;
    }

    public WeiboHeaderPagerBehavior() {
        init();
    }

    public WeiboHeaderPagerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mOverScroller = new OverScroller(BasicApplication.getContext());
    }

    @Override
    protected void layoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        super.layoutChild(parent, child, layoutDirection);
        mParent = new WeakReference<CoordinatorLayout>(parent);
        mChild = new WeakReference<View>(child);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View
            directTargetChild, View target, int nestedScrollAxes) {


        boolean canScroll = canScroll(child, 0);
        //拦截垂直方向上的滚动事件且当前状态是打开的并且还可以继续向上收缩
        boolean b = (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0 && canScroll &&
                !isClosed(child);

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onStartNestedScroll: nestedScrollAxes=" + nestedScrollAxes + " b = " + b);
        }
        return b;

    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        Log.i(TAG, "onNestedScroll: dyConsumed =" + dyConsumed);
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target,
                                    float velocityX, float velocityY) {
        // consumed the flinging behavior until Closed

        boolean coumsed = !isClosed(child);
        Log.i(TAG, "onNestedPreFling: coumsed=" + coumsed);
        return coumsed;
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target,
                                 float velocityX, float velocityY, boolean consumed) {
        Log.i(TAG, "onNestedFling: velocityY=" + velocityY);
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY,
                consumed);

    }

    private boolean isClosed(View child) {
        boolean isClosed = child.getTranslationY() == getHeaderOffsetRange();
        return isClosed;
    }

    public boolean isClosed() {
        return mCurState == STATE_CLOSED;
    }

    private void changeState(int newState) {
        if (mCurState != newState) {
            mCurState = newState;
            if (mCurState == STATE_OPENED) {
                if (mPagerStateListener != null) {
                    mPagerStateListener.onPagerOpened();
                }

            } else {
                if (mPagerStateListener != null) {
                    mPagerStateListener.onPagerClosed();
                }

            }
        }

    }

    // 表示 Header TransLationY 的值是否达到我们指定的阀值， headerOffsetRange，到达了，返回 false，
    // 否则，返回 true。注意 TransLationY 是负数。
    private boolean canScroll(View child, float pendingDy) {
        int pendingTranslationY = (int) (child.getTranslationY() - pendingDy);
        int headerOffsetRange = getHeaderOffsetRange();
        if (pendingTranslationY >= headerOffsetRange && pendingTranslationY <= 0) {
            return true;
        }
        return false;
    }


    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, final View child, MotionEvent
            ev) {

        boolean closed = isClosed();
        Log.i(TAG, "onInterceptTouchEvent: closed=" + closed);
        if (ev.getAction() == MotionEvent.ACTION_UP && !closed) {
            handleActionUp(parent, child);
        }

        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target,
                                  int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        //dy>0 scroll up;dy<0,scroll down
        Log.i(TAG, "onNestedPreScroll: dy=" + dy);
        float halfOfDis = dy;
        //    不能滑动了，直接给 Header 设置 终值，防止出错
        if (!canScroll(child, halfOfDis)) {
            child.setTranslationY(halfOfDis > 0 ? getHeaderOffsetRange() : 0);
        } else {
            child.setTranslationY(child.getTranslationY() - halfOfDis);
        }
        //consumed all scroll behavior after we started Nested Scrolling
        consumed[1] = dy;
    }

    //    需要注意的是  Header 我们是通过 setTranslationY 来移出屏幕的，所以这个值是负数
    private int getHeaderOffsetRange() {
        return BasicApplication.getContext().getResources().getDimensionPixelOffset(R.dimen
                .weibo_header_offset);
    }

    private void handleActionUp(CoordinatorLayout parent, final View child) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "handleActionUp: ");
        }
        if (mFlingRunnable != null) {
            child.removeCallbacks(mFlingRunnable);
            mFlingRunnable = null;
        }
        mFlingRunnable = new FlingRunnable(parent, child);
        if (child.getTranslationY() < getHeaderOffsetRange() / 6.0f) {
            mFlingRunnable.scrollToClosed(DURATION_SHORT);
        } else {
            mFlingRunnable.scrollToOpen(DURATION_SHORT);
        }

    }

    private void onFlingFinished(CoordinatorLayout coordinatorLayout, View layout) {
        changeState(isClosed(layout) ? STATE_CLOSED : STATE_OPENED);
    }

    public void openPager() {
        openPager(DURATION_LONG);
    }

    /**
     * @param duration open animation duration
     */
    public void openPager(int duration) {
        View child = mChild.get();
        CoordinatorLayout parent = mParent.get();
        if (isClosed() && child != null) {
            if (mFlingRunnable != null) {
                child.removeCallbacks(mFlingRunnable);
                mFlingRunnable = null;
            }
            mFlingRunnable = new FlingRunnable(parent, child);
            mFlingRunnable.scrollToOpen(duration);
        }
    }

    public void closePager() {
        closePager(DURATION_LONG);
    }

    /**
     * @param duration close animation duration
     */
    public void closePager(int duration) {
        View child = mChild.get();
        CoordinatorLayout parent = mParent.get();
        if (!isClosed()) {
            if (mFlingRunnable != null) {
                child.removeCallbacks(mFlingRunnable);
                mFlingRunnable = null;
            }
            mFlingRunnable = new FlingRunnable(parent, child);
            mFlingRunnable.scrollToClosed(duration);
        }
    }

    private FlingRunnable mFlingRunnable;

    /**
     * For animation , Why not use {@link android.view.ViewPropertyAnimator } to play animation
     * is of the
     * other {@link CoordinatorLayout.Behavior} that depend on this could not receiving the
     * correct result of
     * {@link View#getTranslationY()} after animation finished for whatever reason that i don't know
     */
    private class FlingRunnable implements Runnable {
        private final CoordinatorLayout mParent;
        private final View mLayout;

        FlingRunnable(CoordinatorLayout parent, View layout) {
            mParent = parent;
            mLayout = layout;
        }

        public void scrollToClosed(int duration) {
            float curTranslationY = ViewCompat.getTranslationY(mLayout);
            float dy = getHeaderOffsetRange() - curTranslationY;
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "scrollToClosed:offest:" + getHeaderOffsetRange());
                Log.d(TAG, "scrollToClosed: cur0:" + curTranslationY + ",end0:" + dy);
                Log.d(TAG, "scrollToClosed: cur:" + Math.round(curTranslationY) + ",end:" + Math
                        .round(dy));
                Log.d(TAG, "scrollToClosed: cur1:" + (int) (curTranslationY) + ",end:" + (int) dy);
            }
            mOverScroller.startScroll(0, Math.round(curTranslationY - 0.1f), 0, Math.round(dy +
                    0.1f), duration);
            start();
        }

        public void scrollToOpen(int duration) {
            float curTranslationY = ViewCompat.getTranslationY(mLayout);
            mOverScroller.startScroll(0, (int) curTranslationY, 0, (int) -curTranslationY,
                    duration);
            start();
        }

        private void start() {
            if (mOverScroller.computeScrollOffset()) {
                mFlingRunnable = new FlingRunnable(mParent, mLayout);
                ViewCompat.postOnAnimation(mLayout, mFlingRunnable);
            } else {
                onFlingFinished(mParent, mLayout);
            }
        }

        @Override
        public void run() {
            if (mLayout != null && mOverScroller != null) {
                if (mOverScroller.computeScrollOffset()) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "run: " + mOverScroller.getCurrY());
                    }
                    ViewCompat.setTranslationY(mLayout, mOverScroller.getCurrY());
                    ViewCompat.postOnAnimation(mLayout, this);
                } else {
                    onFlingFinished(mParent, mLayout);
                }
            }
        }
    }

    /**
     * callback for HeaderPager 's state
     */
    public interface OnPagerStateListener {
        /**
         * do callback when pager closed
         */
        void onPagerClosed();

        /**
         * do callback when pager opened
         */
        void onPagerOpened();
    }

}
