package com.wanou.framelibrary.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.wanou.framelibrary.BuildConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 *LogUtil工具类，可控制Log输出开关、保存Log到文件、过滤输出等级
 *
 * @author wodx521
 * @date on 2018/8/14
 */
@SuppressLint("SimpleDateFormat")
public class LogUtils {
    // 日志写入文件开关
    private static Boolean LogUtil_TO_FILE = true;
    private static boolean IS_LOG = BuildConfig.DEBUG;
    // 默认的tag
    private static String LogUtil_TAG = "debug_log";
    // sd卡中日志文件的最多保存天数
    private static int LogUtil_SAVE_DAYS = 7;
    // 日志的输出格式
    private final static SimpleDateFormat LogUtil_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // 日志文件格式
    private final static SimpleDateFormat FILE_SUFFIX = new SimpleDateFormat("yyyy-MM-dd");
    private static Context context;
    // 日志文件保存路径
    private static String LogUtil_FILE_PATH;
    // 日志文件保存名称
    private static String LogUtil_FILE_NAME = "Log";

    public static void initLogUtils(Application application) {
        context = application;
       LogUtil_FILE_PATH = Objects.requireNonNull(context.getExternalCacheDir()).getPath();
    }

    /**
     * Warn
     */
    public static void w(Object msg) {
        w(LogUtil_TAG, msg);
    }

    public static void w(String tag, Object msg) {
        w(tag, msg, null);
    }

    public static void w(String tag, Object msg, Throwable tr) {
       LogUtil(tag, msg.toString(), tr, 'w');
    }

    /**
     * Error
     */
    public static void e(Object msg) {
        e(LogUtil_TAG, msg);
    }

    public static void e(String tag, Object msg) {
        e(tag, msg, null);
    }

    public static void e(String tag, Object msg, Throwable tr) {
       LogUtil(tag, msg.toString(), tr, 'e');
    }

    /**
     * Debug
     */
    public static void d(Object msg) {
        d(LogUtil_TAG, msg);
    }

    public static void d(String tag, Object msg) {// 调试信息
        d(tag, msg, null);
    }

    public static void d(String tag, Object msg, Throwable tr) {
       LogUtil(tag, msg.toString(), tr, 'd');
    }

    /**
     * Info
     */
    public static void i(Object msg) {
        i(LogUtil_TAG, msg);
    }

    public static void i(String tag, Object msg) {
        i(LogUtil_TAG, msg, null);
    }

    public static void i(String tag, Object msg, Throwable tr) {
       LogUtil(tag, msg.toString(), tr, 'i');
    }

    /**
     * Verbose
     */
    public static void v(Object msg) {
        v("show", msg);
    }

    public static void v(String tag, Object msg) {
        v(tag, msg, null);
    }

    public static void v(String tag, Object msg, Throwable tr) {
       LogUtil(tag, msg.toString(), tr, 'v');
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag   tag
     * @param msg   日志信息
     * @param level 日志层级
     */
    private static void LogUtil(String tag, String msg, Throwable tr, char level) {
        //判断日志层级输出日志
        if ('e' == level) {
           LogUtils.e(tag, createMessage(msg), tr);
        } else if ('w' == level) {
           LogUtils.w(tag, createMessage(msg), tr);
        } else if ('d' == level) {
           LogUtils.d(tag, createMessage(msg), tr);
        } else if ('i' == level) {
           LogUtils.i(tag, createMessage(msg), tr);
        } else {
           LogUtils.v(tag, createMessage(msg), tr);
        }

        if (LogUtil_TO_FILE) {
           LogUtil2File(String.valueOf(level), tag, TextUtils.isEmpty(msg + tr) ? "" : "\n" + msg + ":" );
        }
    }

    private static String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts != null) {
            for (StackTraceElement st : sts) {
                if (st.isNativeMethod()) {
                    continue;
                }
                if (st.getClassName().equals(Thread.class.getName())) {
                    continue;
                }
                if ("LogUtils.java".equals(st.getFileName())) {
                    continue;
                }
                return "[" + Thread.currentThread().getName() + "("
                        + Thread.currentThread().getId() + "): " + st.getFileName()
                        + ":" + st.getLineNumber() + "]";
            }
        }
        return null;
    }

    private static String createMessage(String msg) {
        String functionName = getFunctionName();
        return (functionName == null ? msg : (functionName + " - " + msg));
    }

    /**
     * 打开日志文件并写入日志
     **/
    private synchronized static void LogUtil2File(String myLogType, String tag, String text) {
        Date nowTime = new Date();
        String date = FILE_SUFFIX.format(nowTime);
        // 日志输出格式
        String dateLogContent =LogUtil_FORMAT.format(nowTime) + ":" + myLogType + ":" + tag + ":" + text;
        File destDir = new File(LogUtil_FILE_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File file = new File(LogUtil_FILE_PATH,LogUtil_FILE_NAME + date);
        try {
            FileWriter filerWriter = new FileWriter(file, true);
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(dateLogContent);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定的日志文件
     */
    public static void delFile() {
        String needDelFile = FILE_SUFFIX.format(getDateBefore());
        File file = new File(LogUtil_FILE_PATH,LogUtil_FILE_NAME + needDelFile);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 得到LOG_SAVE_DAYS天前的日期
     *
     * @return
     */
    private static Date getDateBefore() {
        Date nowTime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowTime);
        now.set(Calendar.DATE, now.get(Calendar.DATE) -LogUtil_SAVE_DAYS);
        return now.getTime();
    }
}
