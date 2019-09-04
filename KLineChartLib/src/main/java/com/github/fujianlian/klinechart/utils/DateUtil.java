package com.github.fujianlian.klinechart.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 时间工具类
 * Created by tifezh on 2016/4/27.
 */
public class DateUtil {
    public static SimpleDateFormat longTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    public static SimpleDateFormat middleTimeFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
    public static SimpleDateFormat shortTimeFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
    public static SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);

}
