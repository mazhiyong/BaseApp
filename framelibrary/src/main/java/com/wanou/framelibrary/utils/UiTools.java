package com.wanou.framelibrary.utils;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Author by wodx521
 * Date on 2018/11/5.
 */
public class UiTools {
    private static Toast toast;
    @SuppressLint("StaticFieldLeak")
    private static DecimalFormat decimalFormat = new DecimalFormat();
    private static Resources resources;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static StringBuffer stringBuffer = new StringBuffer();

    public static void initUiTools(Application application) {
        resources = application.getResources();
        context = application.getApplicationContext();
    }

    public static Resources getResources() {
        return resources;
    }

    public static Context getContext() {
        return context;
    }

    /**
     * 获取屏幕宽
     *
     * @param activity 当前页面的activity
     * @return 当前页面宽
     */
    public static int getDeviceWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        return p.x;
    }

    /**
     * 获取屏幕的高
     *
     * @param activity 当前页面的activity
     * @return 当前页面高
     */
    public static int getDeviceHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        return p.y;
    }

    /**
     * 判断传入的字符串是否为空
     *
     * @param strings 传入的字符串数组
     * @return 全部非空返回true 否则返回false
     */
    public static boolean noEmpty(String... strings) {
        if (strings != null && strings.length > 0) {
            for (String string : strings) {
                if (TextUtils.isEmpty(string)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 要获取内容的TextView
     *
     * @param textView TextView
     * @return TextView内容
     */
    public static String getText(TextView textView) {
        return textView.getText().toString().trim();
    }

    /**
     * 要获取内容的TextView
     *
     * @param textView TextView
     * @return TextView内容
     */
    public static String getHint(TextView textView) {
        return textView.getHint().toString().trim();
    }

    /**
     * 格式化数字为指定格式
     *
     * @param number  需要格式化的数字
     * @param pattern 指定格式
     * @return 格式化完成后的字符串
     */
    public static String formatNumber(Object number, String pattern) {
        try {
            decimalFormat.applyPattern(pattern);
            if (number instanceof Double) {
                return decimalFormat.format((double) number);
            } else if (number instanceof Long) {
                return decimalFormat.format((long) number);
            } else if (number instanceof String) {
                if (noEmpty((String) number)) {
                    if (((String) number).contains(".")) {
                        Double aDouble = Double.valueOf((String) number);
                        return formatNumber(aDouble, pattern);
                    } else {
                        Long aLong = Long.valueOf((String) number);
                        return formatNumber(aLong, pattern);
                    }
                }
                return "0";
            } else {
                return decimalFormat.format(number);
            }
        } catch (Exception e) {
            return "0";
        }
    }

    public static String formatNumber(Object number, String pattern,RoundingMode roundingMode) {
        try {
            decimalFormat.applyPattern(pattern);
            decimalFormat.setRoundingMode(roundingMode);
            if (number instanceof Double) {
                return decimalFormat.format((double) number);
            } else if (number instanceof Long) {
                return decimalFormat.format((long) number);
            } else if (number instanceof String) {
                if (noEmpty((String) number)) {
                    if (((String) number).contains(".")) {
                        Double aDouble = Double.valueOf((String) number);
                        return formatNumber(aDouble, pattern);
                    } else {
                        Long aLong = Long.valueOf((String) number);
                        return formatNumber(aLong, pattern);
                    }
                }
                return "0";
            } else {
                return decimalFormat.format(number);
            }
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * 格式化数字为指定格式
     *
     * @param number  需要格式化的数字
     * @param pattern 指定格式
     * @return 格式化完成后的字符串
     */
    public static String formatDecimal(Object number, int pattern, RoundingMode roundingMode) {
        try {
            decimalFormat.setGroupingUsed(false);
            decimalFormat.applyPattern("");
            decimalFormat.setRoundingMode(roundingMode);
            decimalFormat.setMaximumFractionDigits(pattern);
            if (number instanceof Double) {
                return decimalFormat.format((double) number);
            } else if (number instanceof Long) {
                return decimalFormat.format((long) number);
            } else if (number instanceof String) {
                if (noEmpty((String) number)) {
                    if (((String) number).contains(".")) {
                        Double aDouble = Double.valueOf((String) number);
                        return formatDecimal(aDouble, pattern, roundingMode);
                    } else {
                        Long aLong = Long.valueOf((String) number);
                        return formatDecimal(aLong, pattern, roundingMode);
                    }
                }
                return "0";
            } else {
                return decimalFormat.format(number);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    public static String formatDecimal(Object number, int pattern) {
        try {
            decimalFormat.setGroupingUsed(false);
            decimalFormat.setMaximumFractionDigits(pattern);
            stringBuffer.setLength(0);
            if (pattern > 0) {
                stringBuffer.append("0.");
                for (int i = 0; i < pattern; i++) {
                    stringBuffer.append("0");
                }
            } else {
                stringBuffer.append("0");
            }
            decimalFormat.applyPattern(stringBuffer.toString());
            if (number instanceof Double) {
                return decimalFormat.format((double) number);
            } else if (number instanceof Long) {
                return decimalFormat.format((long) number);
            } else if (number instanceof String) {
                if (noEmpty((String) number)) {
                    if (((String) number).contains(".")) {
                        Double aDouble = Double.valueOf((String) number);
                        return formatDecimal(aDouble, pattern);
                    } else {
                        Long aLong = Long.valueOf((String) number);
                        return formatDecimal(aLong, pattern);
                    }
                }
                return "0";
            } else {
                return decimalFormat.format(number);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    public static void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        toast.setText(text);
        toast.show();
    }

    public static void showToast(@StringRes int stringRes) {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        toast.setText(stringRes);
        toast.show();
    }

    public static void showToastLong(String text) {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        }
        toast.setText(text);
        toast.show();
    }

    public static void showToastLong(@StringRes int stringRes) {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_LONG);
        }
        toast.setText(stringRes);
        toast.show();
    }

    /**
     * 获取资源id对应的字符串
     *
     * @param resId 资源ID
     * @return 返回对应字符串
     */
    public static String getString(int resId) {
        return resources.getString(resId);
    }

    /**
     * 获取资源id对应的字符串数组
     *
     * @param resId 资源ID
     * @return 返回对应字符串数组
     */
    public static String[] getStringArray(int resId) {
        return resources.getStringArray(resId);
    }

    public static int[] getIntArray(int resId) {
        return resources.getIntArray(resId);
    }

    /**
     * 获取资源id对应的Drawable对象
     *
     * @param resId 资源ID
     * @return 返回Drawable对象
     */
    public static Drawable getDrawable(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return resources.getDrawable(resId, null);
        } else {
            return resources.getDrawable(resId);
        }
    }

    public static Float getDimen(int resId) {
        return resources.getDimension(resId);
    }

    /**
     * 获取资源id对应的颜色值
     *
     * @param resId 资源ID
     * @return 返回对应颜色值
     */
    public static int getColor(int resId) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? resources.getColor(resId, null) : resources.getColor(resId);
    }

    public static Animation getAnimation(int resId) {
        return AnimationUtils.loadAnimation(context, resId);
    }

    public static Animator getAnimator(int resId) {
        return AnimatorInflater.loadAnimator(context, resId);
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     *
     * @param dip 需要转换的dp值
     * @return 返回转换后的值
     */
    public static int dip2px(float dip) {
        final float scale = resources.getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     *
     * @param px 需要转换的dp值
     * @return 返回转换后的值
     */
    public static int px2dip(int px) {
        final float scale = resources.getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static View parseLayout(int resLayout, ViewGroup root) {
        LayoutInflater from = LayoutInflater.from(context);
        return from.inflate(resLayout, root, false);
    }

    public static View parseLayout(int resLayout) {
        return View.inflate(context, resLayout, null);
    }

    public static String getPattern(double formatValue) {
        String stringValue = formatValue + "";
        return stringValue.replaceAll("[^.?]", "#");
    }

    public static String getPattern(String formatValue) {
        return formatValue.replaceAll("[^.?]", "#");
    }

    public static String getTime(long second) {
        long days = second / 86400;//转换天数
        second = second % 86400;//剩余秒数
        long hours = second / 3600;//转换小时数
        String hour;
        if (hours > 10) {
            hour = hours + "";
        } else {
            hour = "0" + hours;
        }
        second = second % 3600;//剩余秒数
        String minute;
        long minutes = second / 60;//转换分钟
        if (minutes > 10) {
            minute = minutes + "";
        } else {
            minute = "0" + minutes;
        }
        second = second % 60;//剩余秒数
        String seconds;
        if (second > 10) {
            seconds = second + "";
        } else {
            seconds = "0" + second;
        }
        if (days > 0) {
            return days + "天 " + hour + ":" + minute + ":" + seconds;
        } else {
            return hour + ":" + minute + ":" + seconds;
        }
    }
}
