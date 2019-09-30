package com.lr.biyou.mywidget.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.WindowInsets;
import android.widget.LinearLayout;
import androidx.annotation.RequiresApi;
import com.lr.biyou.utils.tool.LogUtilDebug;
import com.lr.biyou.utils.tool.UtilTools;
public class WindowInsetsFrameLayout extends LinearLayout {

    private Context mContenx;

   /* public WindowInsetsFrameLayout(Context context) {
        this(context, null);
    }

    public WindowInsetsFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }*/
    public WindowInsetsFrameLayout(Context context) {
        super(context);
        mContenx = context;
    }

    public WindowInsetsFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContenx = context;
    }

    public WindowInsetsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContenx = context;
    }

   /* public WindowInsetsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }*/



    /*@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WindowInsetsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                requestApplyInsets();
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++)
            getChildAt(index).dispatchApplyWindowInsets(insets);
        return insets;
    }*/


    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            insets.left = 0;
            insets.top = 0;
            insets.right = 0;
        }
        return super.fitSystemWindows(insets);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LogUtilDebug.i("show","window$$:"+insets.getSystemWindowInsetBottom());
            LogUtilDebug.i("show","windowAA:"+ UtilTools.getNavigationBarHeight(mContenx));
            if (insets.getSystemWindowInsetBottom()>0){ //键盘开启
                boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
                if (!hasHomeKey) {
                    return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0, insets.getSystemWindowInsetBottom()-UtilTools.getNavigationBarHeight(mContenx)));
                }else {
                    return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0, insets.getSystemWindowInsetBottom()));
                }
               // return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0, insets.getSystemWindowInsetBottom()-UtilTools.getNavigationBarHeight(mContenx)));
            }else { //键盘关闭

                return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0, insets.getSystemWindowInsetBottom()));
            }

        } else {
            return insets;
        }
    }
}