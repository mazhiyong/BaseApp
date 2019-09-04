package com.lr.biyou.di.module;
import com.lr.biyou.api.ApiManagerService;
import com.lr.biyou.di.component.DaggerNetComponent;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 *MVP persener和modle层的对象的实例化
 */

@Module
public class MVPModule {


    @Inject
    ApiManagerService mApiManager;

    //ApiManagerService单例对象
    @Provides
    @Singleton
    ApiManagerService providerApiManage(){
        DaggerNetComponent.create().injectTo(this);
        return mApiManager;
    }

    //订阅类 单例对象
    @Provides
    @Singleton
    CompositeDisposable providerCompos(){
        return new CompositeDisposable();
    }
}
