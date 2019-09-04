package com.lr.biyou.di.module;

import android.content.Context;

import com.lr.biyou.mvp.presenter.RequestPresenterImp;
import com.lr.biyou.mvp.view.RequestView;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;


/**
 * MVP  中Presener的注解
 */
@Module
public class PersenerModule {
    private  RequestView mView;
    private  Context mContext;

    private  PersenerModule instance;

    /*public static PersenerModule getInstance(RequestView view,Context context) {
       // mView =view;
        //mContext = context;
        //if(instance == null){
            instance = new PersenerModule(view,context);
       // }
        return instance;
    }*/

    public PersenerModule(RequestView view,Context context) {
        mView = view;
        mContext = context;
    }

    @Provides
    @Singleton
    RequestView providerView(){
        return this.mView;
    }


    @Provides
    @Singleton
    Context providerContext(){
        //return ActivityManager.getInstance().currentActivity();
        return mContext;
    }

    //实例化Presenter层
    @Provides
    @Singleton
    RequestPresenterImp providerRequestPresenterImp(RequestView requestView, Context mContext){
        return new RequestPresenterImp(requestView,mContext);
    }

}
