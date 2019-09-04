package com.wanou.framelibrary.utils;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Author by wodx521
 * Date on 2018/11/5.
 */
@SuppressLint({"SetTextI18n", "StaticFieldLeak"})
public class CountDownUtils {

    private CountDownTimer countDownTimer;

    public CountDownUtils() {
    }

    /**
     * 不适合一个页面多个计时器(因为监听为内部静态接口,在多个计时器设置监听时会覆盖之前的监听事件
     * 导致之前的时间不会生效)
     *
     * @param second   需要设置的倒计时秒数
     * @param textView 倒计时运行时需要设置文本变化的控件TextView或者Button
     */
    public void getTimer(long second, TextView textView, String finishText) {
        countDownTimer = new CountDownTimer(second * 1000 + 1500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long remainingSecond = millisUntilFinished / 1000 - 1;
                if (textView != null) {
                    textView.setClickable(false);
                    textView.setText(remainingSecond + " S");
                }
                if (remainingSecond == 0) {
                    //剩余0秒时,及时结束,调用结束方法
                    onFinish();
                }
            }

            @Override
            public void onFinish() {
                cancel();
                if (textView != null) {
                    textView.setClickable(true);
                    textView.setText(finishText);
                }
                if (mCountTimeFinishListener != null) {
                    mCountTimeFinishListener.onTimeFinishListener();
                    mCountTimeFinishListener = null;
                }
            }
        };
        countDownTimer.start();
    }

    /**
     * 取消所有在运行的计时器
     */
    public void cancelTimer() {
        if (countDownTimer!=null) {
            countDownTimer.onFinish();
        }
    }

    public interface CountTimeFinishListener {
        void onTimeFinishListener();
    }

    private  CountTimeFinishListener mCountTimeFinishListener;

    public void setTimeFinishListener(CountTimeFinishListener countTimeFinishListener) {
        mCountTimeFinishListener = countTimeFinishListener;
    }
}
