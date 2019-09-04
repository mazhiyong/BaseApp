package com.wanou.framelibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.wanou.framelibrary.GlobalApplication;

import java.util.Map;

/**
 * @author wodx521
 * @date on 2018/8/15
 */
public class SpUtils {
    @SuppressLint("StaticFieldLeak")
    private static Context context = GlobalApplication.getContext();
    /**
     * 保存在手机里面的文件名
     */
    private static String FILE_NAME = context.getPackageName();
    private static SharedPreferences sp;

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key    存储的键名称
     * @param object 存储对应键的值
     */
    public static void put(String key, Object object) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.apply();
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param fileName sp存储的文件名
     * @param key      存储的键名称
     * @param object   存储对应键的值
     */
    public static void put(String fileName, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.apply();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key           需要获取的键值
     * @param defaultObject 如果该键值不存在或者键对应的值不存在是返回的默认值
     * @return 返回键对应的值
     */
    public static Object get(String key, Object defaultObject) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param fileName      需要获取的sp文件名
     * @param key           需要获取的键值
     * @param defaultObject 如果该键值不存在或者键对应的值不存在是返回的默认值
     * @return 返回键对应的值
     */
    public static Object get(String fileName, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param fileName 需要获取的sp文件名
     * @param key      需要移除的键值
     */
    public static void remove(String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key).apply();
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key 需要移除的键值
     */
    public static void remove(String key) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key).apply();
    }

    /**
     * 清除所有数据
     */
    public static void clear() {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        sp.edit()
                .clear()
                .apply();
    }

    /**
     * 清除所有数据
     *
     * @param fileName 需要获取的sp文件名
     */
    public static void clear(String fileName) {
        context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key 需要查找的key
     * @return 返回boolean类型 true为包含该键,false为不包含该键
     */
    public static boolean contains(String key) {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp.contains(key);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param fileName 需要获取的sp文件名
     * @param key      需要查找的key
     * @return 返回boolean类型 true为包含该键,false为不包含该键
     */
    public static boolean contains(String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @return 返回的键值对
     */
    public static Map<String, ?> getAll() {
        if (sp == null) {
            sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return sp.getAll();
    }

    /**
     * 返回所有的键值对
     *
     * @param fileName 需要获取的sp文件名
     * @return 返回的键值对
     */
    public static Map<String, ?> getAll(String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        return sp.getAll();
    }
}
