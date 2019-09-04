package com.lr.biyou.utils.tool;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用单例和弱引用解决崩溃问题
 */
public class MyDataUtil {
    private static MyDataUtil mInstance;
    private Map<String, Object> data;

    public static MyDataUtil getInstance() {
        if (mInstance == null){
            synchronized (MyDataUtil.class){
                if (mInstance == null){
                    mInstance = new MyDataUtil();
                }
            }
        }
        return mInstance;
    }

    private MyDataUtil() {
        data = new HashMap<>();
    }

    public void save(String id, Object object) {
        if (data != null){
            data.put(id, object);
        }
    }

    public Object retrieve(String id) {
        if (data == null || mInstance == null){
            throw new RuntimeException("你必须先初始化");
        }
        return data.get(id);
    }
}
