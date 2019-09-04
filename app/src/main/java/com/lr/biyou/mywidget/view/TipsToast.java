package com.lr.biyou.mywidget.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lr.biyou.R;
import com.lr.biyou.basic.BasicApplication;

/**
 * 自定义提示Toast
 * 
 */
public class TipsToast extends Toast {
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public TipsToast(Context context) {
        super(context);
    }

    public static TipsToast makeText( CharSequence text, int duration) {
        TipsToast result = new TipsToast(BasicApplication.getContext());

        LayoutInflater inflate = (LayoutInflater) BasicApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.view_tips, null);
        TextView tv = (TextView) v.findViewById(R.id.tips_msg);
        tv.setText(text);

        result.setView(v);
        // setGravity方法用于设置位置，此处为垂直居中
        result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        result.setDuration(duration);

        return result;
    }

    public static TipsToast makeText( int resId, int duration) throws Resources.NotFoundException {
        return makeText( BasicApplication.getContext().getResources().getText(resId), duration);
    }

    public void setIcon(int iconResId) {
        if (getView() == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        ImageView iv = (ImageView) getView().findViewById(R.id.tips_icon);
        if (iv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        iv.setImageResource(iconResId);
    }
    @Override
    public void setText(CharSequence s) {
        if (getView() == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        TextView tv = (TextView) getView().findViewById(R.id.tips_msg);
        if (tv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        tv.setText(s);
    }


    /**
     * 提示消息
     * @param resId
     */
    public static void showToastMsg( final int resId){
        mHandler.post(new Runnable(){
            @Override
            public void run() {
                //Toast.makeText(BasicActivity.this, resId, Toast.LENGTH_SHORT).show();
                showTips(resId);
            }
        });
    }

    /**
     * 提示消息
     */
    public static void showToastMsg(final String msg){
        mHandler.post(new Runnable(){
            @Override
            public void run() {
                //Toast.makeText(BasicActivity.this, msg, Toast.LENGTH_SHORT).show();
                showTips(msg);
            }
        });
    }

    private void showTips(int iconResId, int msgResId) {
        TipsToast	tipsToast = TipsToast.makeText( msgResId, TipsToast.LENGTH_SHORT);
        tipsToast.show();
        tipsToast.setIcon(iconResId);
        tipsToast.setText(msgResId);
    }
    private static void showTips( int msgResId) {
        TipsToast	tipsToast = TipsToast.makeText( msgResId, TipsToast.LENGTH_SHORT);
        tipsToast.show();
        //tipsToast.setIcon(iconResId);
        tipsToast.setText(msgResId);
    }
    private static void showTips( String msgResId) {
        TipsToast	tipsToast = TipsToast.makeText( msgResId, TipsToast.LENGTH_SHORT);
        tipsToast.show();
        //tipsToast.setIcon(iconResId);
        tipsToast.setText(msgResId);
    }

}
