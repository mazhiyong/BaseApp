package com.lr.biyou.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DarggeR中 Application的Module (全局单例 变量的 初始化)
 */
@Module //注入module
public class AppModule {
    private Context mContext;

    public AppModule(Context applcation) {
        mContext = applcation;
    }

    // 实例化APP全局可用的Context
    @Provides
    @Singleton
    public Context providerContext(){
        return mContext;
    }




}
