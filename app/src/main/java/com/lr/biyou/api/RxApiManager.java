package com.lr.biyou.api;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.ArrayMap;

import com.lr.biyou.mvp.model.RequestModelImp;
import com.lr.biyou.utils.tool.LogUtilDebug;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;


public class RxApiManager implements RxActionManager<Object> {

    private static RxApiManager sInstance = null;

    //键是页面activity 如果一个页面有多个请求的话，   mListTags 会准确记录每个请求
    private ArrayMap<Object, List<RequestModelImp>> mListTags;
    //记录页面请求   键是页面activity  但是一个页面多个请求的话，会覆盖，只会记录最后请求的那一个
    private ArrayMap<Object, RequestModelImp> maps;

    private ArrayMap<Object, List<CompositeDisposable>> mListTags2;


    public static RxApiManager get() {

        if (sInstance == null) {
            synchronized (RxApiManager.class) {
                if (sInstance == null) {
                    sInstance = new RxApiManager();
                }
            }
        }
        return sInstance;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private RxApiManager() {
        maps = new ArrayMap<>();
        mListTags = new ArrayMap<>();
        mListTags2 = new ArrayMap<>();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void add(Object tag, RequestModelImp subscription) {
        if (mListTags.get(tag) == null) {
            List<RequestModelImp> list = new ArrayList<>();
            list.add(subscription);
            mListTags.put(tag, list);
        }else {
            List<RequestModelImp> list = mListTags.get(tag);
            if (list != null) {
                list.add(subscription);
            }
            mListTags.put(tag, list);
        }

        maps.put(tag, subscription);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void addCompositeDisposable(Object tag, CompositeDisposable compositeDisposable) {
        if (mListTags2.get(tag) == null) {
            List<CompositeDisposable> list = new ArrayList<>();
            list.add(compositeDisposable);
            mListTags2.put(tag, list);
        }else {
            List<CompositeDisposable> list = mListTags2.get(tag);
            if (list != null) {
                list.add(compositeDisposable);
            }
            mListTags2.put(tag, list);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void remove(Object tag) {
        if (!maps.isEmpty()) {
            maps.remove(tag);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void removeAll() {
        if (!maps.isEmpty()) {
            maps.clear();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void cancel(Object tag) {

        if (maps.isEmpty()) {
            return;
        }
        if (maps.get(tag) == null) {
            return;
        }
        maps.get(tag).onUnsubscribe();
        maps.remove(tag);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void cancelActivityAll(Object tag) {
        List<RequestModelImp> list = mListTags.get(tag);
        if (list != null && list.size()>0){
            for (RequestModelImp requestModelImp : list){
                requestModelImp.onUnsubscribe();
            }
            mListTags.remove(tag);
        }



        List<CompositeDisposable> list2 = mListTags2.get(tag);
        if (list2 != null && list2.size()>0){
            for (CompositeDisposable compositeDisposable : list2){
                if (compositeDisposable != null){
                    LogUtilDebug.i("打印log日志",tag.getClass().getSimpleName()+"onUnsubscribe  "+compositeDisposable.isDisposed());
                    //判断状态
                    if(!compositeDisposable.isDisposed()){
                        compositeDisposable.clear();  //注销
                        compositeDisposable.dispose();
                    }
                }
            }
            mListTags2.remove(tag);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void cancelAll() {
        if (maps.isEmpty()) {
            return;
        }
        Set<Object> keys = maps.keySet();
        for (Object apiKey : keys) {
            cancel(apiKey);
        }
    }
}
