package com.lr.biyou.utils.tool;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class DataHolder {
  Map<String, WeakReference<Object>> data = new HashMap<String, WeakReference<Object>>();

  private static DataHolder mInstance;

  public static DataHolder getInstance() {
    if (mInstance == null){
      synchronized (DataHolder.class){
        if (mInstance == null){
          mInstance = new DataHolder();
        }
      }
    }
    return mInstance;
  }
  public void save(String id, Object object) {
    data.put(id, new WeakReference<Object>(object));
  }

  public Object retrieve(String id) {
    WeakReference<Object> objectWeakReference = data.get(id);
    return objectWeakReference.get();
  }
}